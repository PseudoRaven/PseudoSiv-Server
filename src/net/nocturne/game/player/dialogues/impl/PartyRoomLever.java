package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.activities.partyroom.PartyRoom;
import net.nocturne.game.player.dialogues.Dialogue;

public class PartyRoomLever extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			if (player.getInventory().containsItem(995, 1000)) {
				player.getInventory().removeItemMoneyPouch(995, 1000);
				PartyRoom.purchase(player, true);
			} else
				sendDialogue("You do not have enough money.");
		} else if (componentId == OPTION_2) {
			if (player.getInventory().containsItem(995, 500)) {
				player.getInventory().removeItemMoneyPouch(995, 500);
				PartyRoom.purchase(player, false);
			} else
				sendDialogue("You do not have enough money.");
		}
		end();
	}

	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE,
				"Balloon Bonanza (1000 coins).", "Nightly Dance (500 coins).",
				"No action.");
	}
}