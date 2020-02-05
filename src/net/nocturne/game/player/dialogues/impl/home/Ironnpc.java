package net.nocturne.game.player.dialogues.impl.home;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;
import net.nocturne.game.item.Item;

public class Ironnpc extends Dialogue {

	/**
	 * @author: Nath
	 */

	@Override
	public void start() {
		if (player.isIronman() || (player.isHardcoreIronman())) {
			stage = 1;
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Summoning Shop",
					"Skilling Shop", "General Shop", "Slayer Shop", "Goodbye");
		} else {
			player.getPackets().sendGameMessage(
					"This npc ignores players on regular gamemode.");
		}
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
				ShopsHandler.openShop(player, 216);
				break;
			case OPTION_2:
				ShopsHandler.openShop(player, 217);
				break;
			case OPTION_3:
				ShopsHandler.openShop(player, 218);
				break;
			case OPTION_4:
				ShopsHandler.openShop(player, 219);
				break;
			case OPTION_5:
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