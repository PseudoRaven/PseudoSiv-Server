package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class RuneSpanLeaving extends Dialogue {

	@Override
	public void start() {
		sendDialogue("All your runes will be converted into points when you leave.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 1) {
			sendOptionsDialogue("Teleport to the Wizards' Tower?", "Yes", "No");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 11) {
				player.getControllerManager().forceStop();
			}
			end();
		}
	}

	@Override
	public void finish() {

	}
}
