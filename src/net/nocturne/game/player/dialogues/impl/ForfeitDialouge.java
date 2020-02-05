package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class ForfeitDialouge extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Forfeit Duel?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (componentId) {
		case OPTION_1:
			if (!player.getDuelRules().getRule(7))
				player.getControllerManager().forceStop();
			else
				sendDialogue("You can't forfeit during this duel.");
			break;
		}
		end();
	}

	@Override
	public void finish() {

	}

}
