package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChooseBank extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			player.getTemporaryAttributtes().remove("ClanBank");
			player.getTemporaryAttributtes().remove("Bank2");
			player.getBank().openBank();
		} else if (componentId == OPTION_2) {
			if (player.getDonationManager().isDivineDonator()) {
				player.getTemporaryAttributtes().remove("ClanBank");
				player.getTemporaryAttributtes().put("Bank2", true);
				player.getBank().openBank();
			} else
				sendDialogue("You must be a divine donator or higher to use a secondary bank.");
		} else if (componentId == OPTION_3) {
			if (player.getClanManager().getClan() != null) {
				player.getTemporaryAttributtes().remove("Bank2");
				player.getTemporaryAttributtes().put("ClanBank", true);
				player.getBank().openBank();
			} else
				sendDialogue("You must be in a clan to use a clan bank.");
		}
		end();
	}

	@Override
	public void start() {
		if (player.usingBankDialogue())
			sendOptionsDialogue("Choose an Option",
					"Regular Bank", "Secondary Bank", "Clan Bank", "None");
		else {
			end();
			player.getTemporaryAttributtes().remove("ClanBank");
			player.getTemporaryAttributtes().remove("Bank2");
			player.getBank().openBank();
		}
	}

}