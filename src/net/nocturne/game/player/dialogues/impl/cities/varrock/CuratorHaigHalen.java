package net.nocturne.game.player.dialogues.impl.cities.varrock;

import net.nocturne.game.player.dialogues.Dialogue;

public class CuratorHaigHalen extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, NORMAL, "Welcome to the museum of Varrock.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
