package net.nocturne.game.player.dialogues.impl.cities.varrock;

import net.nocturne.game.player.dialogues.Dialogue;

public class Schoolboy extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, HAPPY, "Maz...Zar...Za-mor-ak is the bestest!");
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
