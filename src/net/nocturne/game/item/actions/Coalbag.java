package net.nocturne.game.item.actions;

import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;

public class Coalbag {

	public static String getCoal(Player player) {
		return "Your coal bag contains " + player.getCoalBag() + "/1000 space.";
	}

	public static void addCoal(Player player) {
		if (player.getInventory().containsItem(ItemIdentifiers.COAL, 1)) {
			if (player.getCoalBag() >= 1000) {
				player.getPackets().sendGameMessage("Your coal bag is full.");
				return;
			}
			int amount = player.getInventory()
					.getAmountOf(ItemIdentifiers.COAL);
			player.getInventory().deleteItem(ItemIdentifiers.COAL, amount);
			player.setCoalBag(player.getCoalBag() + amount);
			player.getPackets().sendGameMessage(
					"You fill the coal bag, " + player.getCoalBag()
							+ "/1000 space left.");
		} else {
			player.getPackets().sendGameMessage(
					"You don't have any coal in your inventory.");
		}
	}

	public static void withdrawCoal(Player player, int amount) {
		if (amount > 1000) {
			player.getPackets().sendGameMessage(
					"You can't withdraw more than 1000 at a time.");
			return;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			return;
		}
		if (player.getCoalBag() < amount) {
			player.getPackets().sendGameMessage(
					"You don't have " + amount + " coal in your coalbag.");
			if (player.getCoalBag() > 0) {
				player.getInventory().addItem(ItemIdentifiers.COAL_NOTED,
						player.getCoalBag());
				player.setCoalBag(0);
			}
			return;
		}
		player.getInventory().addItem(454, amount);
		player.setCoalBag(player.getCoalBag() - amount);
		player.getPackets().sendGameMessage(
				"You withdraw coal from the coal bag, " + player.getCoalBag()
						+ "/1000 space left.");
	}
}