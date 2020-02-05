package net.nocturne.game.player.content.activities.minigames;

import net.nocturne.game.player.Player;

public class ArtisanWorkshop {
	private static final int BRONZEINGOTS = 20502;
	private static final int IRONINGOT = 20503;

	private static final int IRONINGOTS = 20632;
	private static final int STEELINGOTS = 20504;
	private static final int MITHRILINGOTS = 20634;
	private static final int ADAMANTINGOTS = 20635;
	private static final int RUNEINGOTS = 20636;
	private static final int FULLINVENTORY = 28;

	public static void GiveBronzeIngots(Player player) {
		player.getInventory().addItem(BRONZEINGOTS, 28);
	}

	public static void TakeBronzeIngots(Player player) {
		player.getInventory().deleteItem(BRONZEINGOTS, 28);
	}

	public static void TakeIronIngots(Player player) {
		player.getInventory().addItem(IRONINGOT, 28);
	}

	public static void GiveIronIngots(Player player) {
		player.getInventory().deleteItem(IRONINGOT, 28);
	}

	public static void TakeSteelIngots(Player player) {
		player.getInventory().addItem(STEELINGOTS, 28);
	}

	public static void GiveSteelIngots(Player player) {
		player.getInventory().deleteItem(STEELINGOTS, 28);
	}

	public static void SendInformationBoard(Player player) {
		player.getInterfaceManager().sendCentralInterface(891);
	}

	public static void DepositIngots(Player player) {
		if (player.getInventory().containsItem(IRONINGOTS, FULLINVENTORY)) {
			player.getInventory().deleteItem(IRONINGOTS, FULLINVENTORY);
			player.getInventory().refresh();
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any ingots to return.");
		}
		if (player.getInventory().containsItem(STEELINGOTS, FULLINVENTORY)) {
			player.getInventory().deleteItem(STEELINGOTS, FULLINVENTORY);
			player.getInventory().refresh();
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any ingots to return.");
		}
		if (player.getInventory().containsItem(MITHRILINGOTS, FULLINVENTORY)) {
			player.getInventory().deleteItem(MITHRILINGOTS, FULLINVENTORY);
			player.getInventory().refresh();
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any ingots to return.");
		}
		if (player.getInventory().containsItem(ADAMANTINGOTS, FULLINVENTORY)) {
			player.getInventory().deleteItem(ADAMANTINGOTS, FULLINVENTORY);
			player.getInventory().refresh();
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any ingots to return.");
		}
		if (player.getInventory().containsItem(RUNEINGOTS, FULLINVENTORY)) {
			player.getInventory().deleteItem(RUNEINGOTS, FULLINVENTORY);
			player.getInventory().refresh();
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any ingots to return.");
		}
	}

	public static void handleButtons(Player player, int componentId) {

		switch (componentId) {
		case 201:
			player.getInventory().addItem(IRONINGOTS, FULLINVENTORY);
			player.getPackets()
					.sendGameMessage(
							"You have taken full inventory of iron ingots from the machine.",
							true);
			break;
		case 213:
			player.getInventory().addItem(STEELINGOTS, FULLINVENTORY);
			player.getPackets()
					.sendGameMessage(
							"You have taken full inventory of steel ingots from the machine.",
							true);
			break;
		case 225:
			player.getInventory().addItem(MITHRILINGOTS, FULLINVENTORY);
			player.getPackets()
					.sendGameMessage(
							"You have taken full inventory of mithril ingots from the machine.",
							true);
			break;
		case 237:
			player.getInventory().addItem(ADAMANTINGOTS, FULLINVENTORY);
			player.getPackets()
					.sendGameMessage(
							"You have taken full inventory of adamant ingots from the machine.",
							true);
			break;
		case 249:
			player.getInventory().addItem(RUNEINGOTS, FULLINVENTORY);
			player.getPackets()
					.sendGameMessage(
							"You have taken full inventory of rune ingots from the machine.",
							true);
			break;

		}
		player.getInventory().refresh();
		player.closeInterfaces();
	}
}