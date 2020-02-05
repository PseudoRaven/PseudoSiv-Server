package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Utils;

public class GemMining extends MiningBase {

	private WorldObject rock;
	private PickAxeDefinitions axeDefinitions;

	public GemMining(WorldObject rock) {
		this.rock = rock;
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
		int mineTimer = 50 - player.getSkills().getLevel(Skills.MINING)
				- Utils.random(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + 10)
			mineTimer = 1 + Utils.random(10);
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
		if (45 > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of 45 to mine this rock.");
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
		World.spawnObjectTemporary(
				new WorldObject(11193, rock.getType(), rock.getRotation(), rock
						.getX(), rock.getY(), rock.getPlane()), 60000);
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return -1;
	}

	private static boolean isAtPriff(Player player) {
		WorldTile tile = player.getMiddleWorldTile();
		return ((tile.getX() > 2210 && tile.getX() < 2250)
				&& tile.getY() > 3310 && tile.getY() < 3340);
	}

	private void addOre(Player player) {
		double xpBoost = hasMiningSuit(player) ? 1.025 : 1;
		player.getSkills().addXp(Skills.MINING, xpBoost * 65);
		double random = Utils.random(0, 100);
		if (isAtPriff(player))
			player.getInventory().addItem(
					random <= 3.79 ? 1617 : random <= 8.97 ? 1619
							: random <= 28.27 ? 1621 : 1623, 1);
		else
			player.getInventory().addItem(
					random <= 3.5 ? 1617 : random <= 3.7 ? 1619
							: random <= 4.1 ? 1621 : random <= 7 ? 1623
									: random <= 12.7 ? 1629
											: random <= 21.9 ? 1627 : 1625, 1);
		if (player.getClanManager() != null
				&& player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources();
		player.getPackets().sendGameMessage("You receive a gem.");
	}

	private boolean checkRock() {
		return World.containsObjectWithId(rock, rock.getId());
	}
}
