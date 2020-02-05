package net.nocturne.game.item.actions;

import net.nocturne.Settings;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationLobbyController;
import net.nocturne.utils.Utils;

public class Bonds {

	private static int bondAmount = 0;

	public static void redeem(Player player) {
		if (player.getInventory().containsItem(ItemIdentifiers.BOND, 1)
				|| player.getInventory().containsItem(
						ItemIdentifiers.BOND_UNTRADEABLE, 1))
			openInterface(player, true);
		else
			player.getPackets().sendGameMessage(
					"You don't have a bond in your inventory.");
	}

	public static void handleButtonClick(Player player, int interfaceId,
			int componentId) {
		if (interfaceId == 229) {
			switch (componentId) {
			case 58:
				player.getDialogueManager().startDialogue("BondsD");
				break;
			case 18:
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
				break;
			case 25:
				openInterface(player, false);
				break;
			}
		}
		if (interfaceId == 230) {
			switch (componentId) {
			case 52:
				openInterface(player, true);
				break;
			case 58:
				convertBonds(player);
				break;
			case 63:
				changeBondAmount(player, true);
				break;
			case 68:
				changeBondAmount(player, false);
				break;
			}
		}
	}

	public static void useOnPlayer(Player player, Player usedOn) {
		if (player.getInventory().containsItem(ItemIdentifiers.BOND, 1)) {
			if (usedOn.isLocked()) {
				player.getDialogueManager()
						.startDialogue(
								"SimpleMessage",
								usedOn.getDisplayName()
										+ " is too busy at the moment.");
				usedOn.getPackets()
						.sendGameMessage(
								player.getDisplayName()
										+ " is offering you a bond. But you are too busy at the moment.",
								true);
				return;
			}
			if (usedOn.isBeginningAccount()) {
				player.getDialogueManager()
						.startDialogue("SimpleMessage",
								"Starter accounts cannot take bonds for the first hour of playing time.");
				usedOn.getPackets()
						.sendGameMessage(
								"Starter accounts cannot take bonds for the first hour of playing time.");
				return;
			}
			if (usedOn.getControllerManager().getController() != null
					&& usedOn.getControllerManager().getController() instanceof StealingCreationLobbyController) {
				player.getDialogueManager()
						.startDialogue(
								"SimpleMessage",
								usedOn.getDisplayName()
										+ " is too busy at the moment.");
				usedOn.getPackets()
						.sendGameMessage(
								player.getDisplayName()
										+ " is offering you a bond. But you are too busy at the moment.",
								true);
				return;
			}
			if (!usedOn.withinDistance(player, 14)) {
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"Unable to find target " + usedOn.getDisplayName()
								+ ".");
				return;
			}
			usedOn.stopAll();
			player.stopAll();
			player.getDialogueManager().startDialogue("SimpleMessage",
					"Offering " + usedOn.getDisplayName() + " a bond...");
			usedOn.getDialogueManager().startDialogue("UseBond", player);
		} else {
			player.getPackets().sendGameMessage(
					"You don't have a bond in your inventory.");
		}

	}

	private static void changeBondAmount(Player player, boolean increase) {
		if (increase) {
			long maxValue = (bondAmount + 1) * 500000;
			if (maxValue >= Integer.MAX_VALUE
					|| (increase && bondAmount + 1 > player.getInventory()
							.getAmountOf(ItemIdentifiers.BOND_UNTRADEABLE)))
				return;
		} else if (!increase && bondAmount - 1 < 0)
			bondAmount = 0;
		else
			bondAmount = increase ? ++bondAmount : --bondAmount;
		player.getPackets().sendIComponentText(230, 19,
				Utils.format(bondAmount * getValue()));
		player.getPackets().sendIComponentText(230, 31,
				Utils.format(bondAmount));
	}

	public static void openInterface(Player player, boolean main) {
		if (main) {
			player.getInterfaceManager().sendCentralInterface(229);
			player.getPackets().sendIComponentText(229, 33, "You have");
			player.getPackets().sendIComponentText(
					229,
					34,
					player.getInventory().getAmountOf(ItemIdentifiers.BOND)
							+ " bonds");
			player.getPackets().sendIComponentText(
					229,
					41,
					player.getInventory().getAmountOf(
							ItemIdentifiers.BOND_UNTRADEABLE)
							+ " bonds");
			player.getVarsManager().sendVar(4563, 95);
		} else {
			player.getInterfaceManager().sendCentralInterface(230);
			player.getPackets().sendIComponentText(230, 0, "1,000,000");
			player.getPackets().sendIComponentText(230, 3,
					Utils.format(player.getInventory().getCoinsAmount()));
			player.getPackets().sendIComponentText(
					230,
					5,
					Utils.format(player.getInventory().getAmountOf(
							ItemIdentifiers.BOND_UNTRADEABLE)));
			player.getPackets().sendIComponentText(230, 19,
					Utils.format(bondAmount * getValue()));
			player.getPackets().sendIComponentText(230, 31,
					Utils.format(bondAmount));
		}
	}

	private static void convertBonds(Player player) {
		if (bondAmount <= 0)
			return;
		if (player.getInventory().containsItem(
				ItemIdentifiers.BOND_UNTRADEABLE, bondAmount)
				&& player.getInventory().getCoinsAmount() >= bondAmount
						* getValue()) {
			player.closeInterfaces();
			player.getInventory().removeItemMoneyPouch(
					ItemIdentifiers.BOND_UNTRADEABLE, bondAmount);
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.COINS,
					bondAmount * getValue());
			player.getInventory().addItemMoneyPouch(ItemIdentifiers.BOND,
					bondAmount);
			player.getPackets().sendGameMessage(
					"You have successfully converted your untradeable bonds!");
		} else
			player.getPackets()
					.sendGameMessage(
							"Please make sure you have the right amount of bonds and coins to do this.");
	}

	public static int getValue() {
		return 1000000;
	}
}
