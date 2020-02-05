package net.nocturne.game.player.dialogues.impl.home;

import net.nocturne.game.item.actions.Drinkables;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

public class Jatix extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, HAPPY, "Hello, " + player.getDisplayName()
				+ "! May I interest you in some herblore supplies?");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes, please.",
					"Decant my potions.", "No thanks.");
			stage = 1;
			break;
		case 0:
			end();
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				ShopsHandler.openShop(player, 186);
				end();
				break;
			case OPTION_2:
				stage = 0;
				Drinkables.decantPotsInv(player);
				sendNPCDialogue(npcId, LAUGHING,
						"Tis the work of a herblore master.");
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
		// TODO Auto-generated method stub
	}
}