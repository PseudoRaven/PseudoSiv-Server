package net.nocturne.game.player.dialogues.impl.home;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

public class Arianwyn extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, HAPPY, "Hello, " + player.getDisplayName()
				+ "! May I interest you in some weapon & equipment supplies?");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE,
					"Show me your melee supplies.",
					"Show me your magic supplies.",
					"Show me your ranged supplies.", "Show me your food shop.",
					"No thanks.");
			stage = 1;
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				ShopsHandler.openShop(player, 181);
				end();
				break;
			case OPTION_2:
				ShopsHandler.openShop(player, 182);
				end();
				break;
			case OPTION_3:
				ShopsHandler.openShop(player, 183);
				end();
				break;
			case OPTION_4:
				ShopsHandler.openShop(player, 200);
				end();
				break;
			case OPTION_5:
				stage = 2;
				sendPlayerDialogue(NORMAL, "No thanks.");
				break;
			}
			break;
		case 2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}