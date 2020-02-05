package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class MageOfZamorak extends Dialogue {

	private int npcId = 2258;

	@Override
	public void start() {
		sendNPCDialogue(npcId, MAD, "This is no place to talk!",
				"Meet me at the Varrock Choas Temple!");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1)
			end();
	}

	@Override
	public void finish() {

	}
}
