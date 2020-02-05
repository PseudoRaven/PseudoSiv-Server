package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.activities.minigames.ShootingStars;

public class ShootingStarMining extends MiningBase {

	private WorldObject rock;
	private PickAxeDefinitions axeDefinitions;

	public ShootingStarMining(WorldObject rock) {
		this.rock = rock;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player, false);
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage(
				"You swing your pickaxe at the rock.");
		setActionDelay(player, getMiningDelay());
		return true;
	}

	private int getMiningDelay() {
		return ShootingStars.getStarSize() * 2;
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
		int level = ShootingStars.getLevel();
		if (level > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage(
					"You need a mining level of " + level
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
		if (!player.getInventory().hasFreeSlots()
				&& !player.getInventory().containsItem(ShootingStars.STARDUST,
						1)) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay();
	}

	private void addOre(Player player) {
		double xpBoost = hasMiningSuit(player) ? 1.025 : 1;
		player.getSkills()
				.addXp(Skills.MINING, ShootingStars.getXP() * xpBoost);
		if (player.getInventory().containsItem(ShootingStars.STARDUST, 200)) {
			player.getPackets().sendGameMessage(
					"You may only mine 200 stardust during an event.");
			return;
		}
		player.getInventory().addItem(ShootingStars.STARDUST, 1);
		player.getPackets().sendGameMessage("You mine some stardust.", true);
		ShootingStars.reduceStarLife();
	}

	private boolean checkRock() {
		return World.containsObjectWithId(rock, rock.getId());
	}
}