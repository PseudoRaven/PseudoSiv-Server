package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class FightKilnDialogue extends Dialogue {

	@Override
	public void start() {
		player.lock();
		sendDialogue("You journey directly to the Kiln.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
	}

	@Override
	public void finish() {
		player.getControllerManager().startController("FightKilnController", 0);
	}

}
