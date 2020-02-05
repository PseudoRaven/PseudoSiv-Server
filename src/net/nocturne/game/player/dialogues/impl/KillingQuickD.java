package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class KillingQuickD extends Dialogue {

	@Override
	public void start() {
		sendDialogue("This area is dangerous, you will lose your items if you die. Would you still like to proceed?");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes, I'm fearless.",
					"I'm not quite ready.");
			stage = 19;
		} else if (stage == 19) {
			if (componentId == OPTION_1)
				player.getControllerManager().startController(
						"SlaughterFieldsController");
			end();
		}
	}

	@Override
	public void finish() {

	}
}
