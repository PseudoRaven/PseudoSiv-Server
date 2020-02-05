package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Utils;

public class EssenceMining extends MiningBase {

	public enum EssenceDefinitions {
		Rune_Essence(1, 5, ItemIdentifiers.RUNE_ESSENCE, 1, 1), Pure_Essence(
				30, 5, ItemIdentifiers.PURE_ESSENCE, 1, 1);
		private final int level;
		private final double xp;
		private final int oreId;
		private final int oreBaseTime;
		private final int oreRandomTime;

		EssenceDefinitions(int level, double xp, int oreId, int oreBaseTime,
				int oreRandomTime) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

	}

	private final WorldObject rock;
	private final EssenceDefinitions definitions;
	private PickAxeDefinitions axeDefinitions;

	public EssenceMining(WorldObject rock, EssenceDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player, false);
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage(
				"You swing your pickaxe at the rock.");
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int mineTimer = definitions.getOreBaseTime()
				- player.getSkills().getLevel(Skills.MINING)
				- Utils.random(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Utils.random(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccuracyMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (axeDefinitions == null) {
			player.getPackets()
					.sendGameMessage(
							"You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of " + definitions.getLevel()
							+ " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(axeDefinitions.getAnimationId()));
		return checkRock();
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = hasMiningSuit(player) ? 1.025 : 1;
		player.getSkills().addXp(Skills.MINING, definitions.getXp() * xpBoost);
		player.getGamePointManager().addGamePointItem(
				new Item(definitions.getOreId(), 1));
		String oreName = ItemDefinitions
				.getItemDefinitions(definitions.getOreId()).getName()
				.toLowerCase();
		player.getPackets().sendGameMessage("You mine some " + oreName + ".",
				true);
		player.getGamePointManager().increaseGamePoints(
				(int) (definitions.getXp() * xpBoost));
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					definitions.getOreId(), Skills.MINING);
	}

	private boolean checkRock() {
		return World.containsObjectWithId(rock, rock.getId());
	}
}
