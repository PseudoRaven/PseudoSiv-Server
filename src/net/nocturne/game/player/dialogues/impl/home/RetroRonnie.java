package net.nocturne.game.player.dialogues.impl.home;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

public class RetroRonnie extends Dialogue {

	/**
	 * @author: Nath
	 */

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Retrocapes",
				"Retrocapes Continued", "Exit");

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
				ShopsHandler.openShop(player, 227);
				break;
			case OPTION_2:
				ShopsHandler.openShop(player, 228);
				break;
			case OPTION_3:
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