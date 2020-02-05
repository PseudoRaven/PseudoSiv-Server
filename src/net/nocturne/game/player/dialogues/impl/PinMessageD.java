package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class PinMessageD extends Dialogue {

	@Override
	public void start() {
		sendDialogue(new String[] { (String) this.parameters[0] });
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			end();
			player.closeInterfaces();
		}
	}

	@Override
	public void finish() {

	}
}
