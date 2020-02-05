package net.nocturne.game.player.dialogues.impl.actionBar;

import net.nocturne.game.player.dialogues.Dialogue;

public class ClearActionBar extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Are you sure you want to clear your action bar?",
				"Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1)
			player.getActionbar().clearBar();
		end();
	}

	@Override
	public void finish() {

	}

}
