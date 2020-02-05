package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.npc.araxxi.Araxxi;
import net.nocturne.game.player.dialogues.Dialogue;

public class AraxxiReward extends Dialogue {

	private Araxxi araxxi;

	@Override
	public void start() {
		araxxi = (Araxxi) parameters[0];
		super.sendDialogue("You search araxxi's body.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		araxxi.openRewardChest(player);
		super.end();
	}

	@Override
	public void finish() {

	}
}