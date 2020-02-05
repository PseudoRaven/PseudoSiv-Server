package net.nocturne.game.item.actions;

import java.text.DecimalFormat;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.Player;

public class RepairItems {

	public static void checkPrice(Player player, int itemId, int amount) {
		final int price = getDegradedPrice(player, itemId);
		if (price <= 0)
			return;
		player.getDialogueManager().startDialogue(
				"SimpleNPCMessage",
				945,
				"These items will cost you "
						+ getFormattedNumber(price * amount) + " coins.");
	}

	private static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,##0").format(amount);
	}

	public static boolean repair(Player player, int itemId, int amount) {
		if (ItemConstants.getItemDegrade(itemId) == -1)
			return false;
		final Item item = new Item(itemId, 1);
		final int price = getDegradedPrice(player, itemId);
		if (amount == 1) {
			if (player.getInventory().containsItem(995, price)) {
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().removeItemMoneyPouch(995, price);
				player.getInventory().addItemMoneyPouch(
						ItemConstants.getItemFixed(itemId), 1);
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"You have repaired your item(" + item.getName()
								+ ") for " + getFormattedNumber(price)
								+ " coins.");
			} else {
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"You dont have enough money to repair this item."
								+ "You need " + getFormattedNumber(price)
								+ " coins.");
			}
		} else {
			if (player.getInventory().containsItem(995, price * amount)) {
				player.getInventory().deleteItem(itemId, amount);
				player.getInventory().removeItemMoneyPouch(995, price * amount);
				player.getInventory().addItemMoneyPouch(
						ItemConstants.getItemFixed(itemId), amount);
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"You have repaired your items(" + amount + " X "
								+ item.getName() + ") for "
								+ getFormattedNumber(price * amount)
								+ " coins.");
			} else {
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"You dont have enough money to repair these items."
								+ "You need "
								+ getFormattedNumber(price * amount)
								+ " coins.");
			}
		}
		return true;
	}

	private static int getFullPrice(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		if (name.contains("razorback") || name.contains("celestial gloves")
				|| name.contains("ascension grips")
				|| name.contains("flarefrost") || name.contains("emberkeen")
				|| name.contains("hailfire"))
			return 15000000;
		// pvp armors
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919
				|| id == 13922 || id == 13925 || id == 13928 || id == 13931
				|| id == 13934 || id == 13937 || id == 13940 || id == 13943
				|| id == 13946 || id == 13949 || id == 13952)
			return 5000000;
		if (id == 13960 || id == 22959 || id == 13966 || id == 13969
				|| id == 13972 || id == 13975)
			return 5000000;
		if ((id >= 14094 && id <= 14121) || (id >= 21527 && id <= 21530))
			return 5000000;
		if (id == 22494)
			return 5000000;
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869
				|| id == 13872 || id == 13875 || id == 13878 || id == 13886
				|| id == 13889 || id == 13892 || id == 13895 || id == 13898
				|| id == 13901 || id == 13904 || id == 13907 || id == 13960)
			return 5000000;
		// nex armors
		if (name.contains("torva") || name.contains("pernix")
				|| name.contains("virtus") || name.contains("zaryte"))
			return 10000000;
		// ROTS
		if ((name.contains("malevolent") && !name.contains("energy"))
				|| name.contains("vengeful") || name.contains("merciless"))
			return 15000000;
		// Sirenic
		if (name.contains("sirenic"))
			return 15000000;
		// Drygore
		if (name.contains("drygore") || name.contains("repriser"))
			return 20000000;
		// Ascension
		if (name.contains("ascension") && name.contains("crossbow"))
			return 20000000;
		// Siesmic
		if (name.contains("siesmic"))
			return 20000000;
		// Noxious
		if (name.contains("noxious"))
			return 20000000;
		if (!name.contains("superior")
				&& (name.contains("sea singer") || name.contains("death lotus") || name
						.contains("tetsu")))
			return 15000000;
		return -1;
	}

	public static int getDegradedPrice(Player player, int itemId) {
		double charges = player.getCharges().getCharges(itemId);
		int maxPrice = getFullPrice(itemId);
		if (charges == 0)
			return maxPrice;
		int discount = (int) (maxPrice * (charges / getTotalCharges(itemId)));
		if (discount > maxPrice)
			discount = maxPrice;
		return maxPrice - discount;
	}

	// return amt of charges
	private static double getTotalCharges(int id) {
		return ItemConstants.getItemDefaultCharges(id);
	}

}