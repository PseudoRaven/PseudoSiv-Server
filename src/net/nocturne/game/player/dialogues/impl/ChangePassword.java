package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChangePassword extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Open cpanel page to edit password?",
				"Yes, please.", "No thanks.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				player.stopAll();
				player.getPackets().sendOpenURL("https://nocturne.org");
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}