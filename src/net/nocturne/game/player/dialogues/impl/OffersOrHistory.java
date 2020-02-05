package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.dialogues.Dialogue;

public class OffersOrHistory extends Dialogue {

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Show all offers.",
				"Show my history.", "Nevermind.");
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
				GrandExchange.showOffers(player);
				end();
				break;
			case OPTION_2:
				player.getGeManager().openHistory();
				end();
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