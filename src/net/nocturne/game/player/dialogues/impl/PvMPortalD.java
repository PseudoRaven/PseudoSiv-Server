package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.dialogues.Dialogue;

public class PvMPortalD extends Dialogue {

	@Override
	public void start() {

		sendDialogue("To retune this portal will cost 100,000 coins");
		stage = 1;
	}

	private boolean checkForCoins() {
		if (player.getInventory().getCoinsAmount() < 100000) {
			sendDialogue("You don't have enough coins on you to retune this portal.");
			return false;
		}
		return true;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		switch (stage) {
		case 1:
			if (!checkForCoins())
				stage = 7;
			else {
				sendOptionsDialogue(
						"Where would you like to direct this portal?", "Bork",
						"Glacor cavern", "Tormented demons", "Airut peninsula",
						"[More options]");
				stage = 2;
			}
			break;

		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getVarsManager().sendVarBit(25113, 1);
				player.getVarbits().put(25113, 1);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Bork.");
				stage = 7;
				break;
			case OPTION_2:
				player.getVarsManager().sendVarBit(25113, 2);
				player.getVarbits().put(25113, 2);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Glacor cavern.");
				stage = 7;
				break;
			case OPTION_3:
				player.getVarsManager().sendVarBit(25113, 3);
				player.getVarbits().put(25113, 3);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Tormented Demons.");
				stage = 7;
				break;
			case OPTION_4:
				player.getVarsManager().sendVarBit(25113, 4);
				player.getVarbits().put(25113, 4);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Airut peninsula.");
				stage = 7;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Giant mole", "Barrows (Rise of the Six)",
						"Dagannoth Kings", "Corporeal Beast", "[More options]");
				stage = 3;
				break;
			}
			break;

		case 3:
			switch (componentId) {
			case OPTION_1:
				player.getVarsManager().sendVarBit(25113, 5);
				player.getVarbits().put(25113, 5);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Giant Mole.");
				stage = 7;
				break;
			case OPTION_2:
				player.getVarsManager().sendVarBit(25113, 6);
				player.getVarbits().put(25113, 6);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Barrows (Rise of the Six).");
				stage = 7;
				break;
			case OPTION_3:
				player.getVarsManager().sendVarBit(25113, 7);
				player.getVarbits().put(25113, 7);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Dagannoth Kings.");
				stage = 7;
				break;
			case OPTION_4:
				player.getVarsManager().sendVarBit(25113, 8);
				player.getVarbits().put(25113, 8);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Corporeal Beast.");
				stage = 7;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"King Black Dragon", "Queen Black Dragon",
						"Kalphite Queen", "Kalphite King", "[More options]");
				stage = 4;
				break;
			}
			break;

		case 4:
			switch (componentId) {
			case OPTION_1:
				player.getVarsManager().sendVarBit(25113, 9);
				player.getVarbits().put(25113, 9);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the King Black Dragon.");
				stage = 7;
				break;
			case OPTION_2:
				player.getVarsManager().sendVarBit(25113, 10);
				player.getVarbits().put(25113, 10);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Queen Black Dragon.");
				stage = 7;
				break;
			case OPTION_3:
				player.getVarsManager().sendVarBit(25113, 11);
				player.getVarbits().put(25113, 11);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Kalphite Queen.");
				stage = 7;
				break;
			case OPTION_4:
				player.getVarsManager().sendVarBit(25113, 12);
				player.getVarbits().put(25113, 12);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Kalphite King.");
				stage = 7;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?",
						"Commander Zilyana", "General Graardor", "Kree'arra",
						"K'ril Tsutsaroth ", "[More options]");
				stage = 5;
				break;
			}
			break;

		case 5:
			switch (componentId) {
			case OPTION_1:
				player.getVarsManager().sendVarBit(25113, 13);
				player.getVarbits().put(25113, 13);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Commander Zilyana.");
				stage = 7;
				break;
			case OPTION_2:
				player.getVarsManager().sendVarBit(25113, 14);
				player.getVarbits().put(25113, 14);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards General Graardor.");
				stage = 7;
				break;
			case OPTION_3:
				player.getVarsManager().sendVarBit(25113, 15);
				player.getVarbits().put(25113, 15);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Kree'arra.");
				stage = 7;
				break;
			case OPTION_4:
				player.getVarsManager().sendVarBit(25113, 16);
				player.getVarbits().put(25113, 16);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards K'ril Tsutsaroth.");
				stage = 7;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?", "Nex",
						"Legios", "Araxxor and Araxxi", "Vorago",
						"[More options]");
				stage = 6;
				break;
			}
			break;

		case 6:
			switch (componentId) {
			case OPTION_1:
				player.getVarsManager().sendVarBit(25113, 17);
				player.getVarbits().put(25113, 17);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Nex.");
				stage = 7;
				break;
			case OPTION_2:
				player.getVarsManager().sendVarBit(25113, 18);
				player.getVarbits().put(25113, 18);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards the Legios.");
				stage = 7;
				break;
			case OPTION_3:
				player.getVarsManager().sendVarBit(25113, 19);
				player.getVarbits().put(25113, 19);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Araxxor and Araxxi.");
				stage = 7;
				break;
			case OPTION_4:
				player.getVarsManager().sendVarBit(25113, 20);
				player.getVarbits().put(25113, 20);
				player.getInventory().removeItemMoneyPouch(
						new Item(995, 100000));
				sendDialogue("You focus this portal towards Vorago.");
				stage = 7;
				break;
			case OPTION_5:
				sendOptionsDialogue(
						"Where would you like to direct this portal?", "Bork",
						"Glacor cavern", "Tormented demons", "Airut peninsula",
						"[More options]");
				stage = 2;
				break;
			}
			break;

		case 7:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
