package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.game.Animation;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.others.SaltyCrabletine;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.skills.mining.MiningBase.PickAxeDefinitions;

public class SaltyCrabletineMining extends Action {

	private SaltyCrabletine crab;
	private PickAxeDefinitions axeDefinitions;

	public SaltyCrabletineMining(NPC npc) {
		this.crab = (SaltyCrabletine) npc;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = MiningBase.getPickAxeDefinitions(player, false);
		if (!crab.canMine(player))
			return false;
		else if (axeDefinitions == null) {
			player.getPackets().sendGameMessage(
					"You need a pickaxe in order to do this.");
			return false;
		}
		player.getPackets().sendGameMessage(
				"You swing your pickaxe at the crabletine.", true);
		crab.startMining(player);
		setActionDelay(player, 10);
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(axeDefinitions.getAnimationId()));
		return crab.canMine(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (!crab.canMine(player)) {
			player.setNextAnimation(new Animation(-1));
			player.stopAll();
			return -1;
		} else if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		player.getSkills().addXp(Skills.MINING, 455.5);
		player.getInventory().addItem(ItemIdentifiers.SEA_SALT, 1);
		player.getPackets().sendGameMessage(
				"You successfully mine some sea salt.");
		return 10;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		crab.stopMining();
	}

}
