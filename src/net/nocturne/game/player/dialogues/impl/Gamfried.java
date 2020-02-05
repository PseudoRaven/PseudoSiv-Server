package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class Gamfried extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		sendNPCDialogue(npcId, NORMAL, "");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {

	}

	@Override
	public void finish() {

	}
}
