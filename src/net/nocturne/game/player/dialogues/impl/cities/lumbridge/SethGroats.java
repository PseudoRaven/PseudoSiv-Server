package net.nocturne.game.player.dialogues.impl.cities.lumbridge;

import net.nocturne.game.player.dialogues.Dialogue;

public class SethGroats extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, PLAIN_TALKING,
				"M'arnin'....going to milk me cowsies!");
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
