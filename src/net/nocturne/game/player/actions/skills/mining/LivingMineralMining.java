package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.game.Animation;
import net.nocturne.game.npc.others.LivingRock;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Utils;

public class LivingMineralMining extends MiningBase {

	private LivingRock rock;
	private PickAxeDefinitions axeDefinitions;

	public LivingMineralMining(LivingRock rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player, false);
		if (!checkAll(player))
			return false;
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int oreBaseTime = 50;
		int oreRandomTime = 20;
		int mineTimer = oreBaseTime
				- player.getSkills().getLevel(Skills.MINING)
				- Utils.random(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + oreRandomTime)
			mineTimer = 1 + Utils.random(oreRandomTime);
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
		if (!rock.canMine(player)) {
			player.getPackets()
					.sendGameMessage(
							"You must wait at least one minute before you can mine a living rock creature that someone else defeated.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (73 > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of 73 to mine this rock.");
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
		rock.takeRemains();
		player.setNextAnimation(new Animation(-1));
		return -1;
	}

	private void addOre(Player player) {
		double xpBoost = hasMiningSuit(player) ? 1.025 : 1;
		player.getSkills().addXp(Skills.MINING, 25 * xpBoost);
		player.getInventory().addItem(15263, Utils.random(5, 25));
		player.getPackets().sendGameMessage(
				"You manage to mine some living minerals.", true);
	}

	private boolean checkRock() {
		return !rock.hasFinished();
	}
}
