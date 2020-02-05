package net.nocturne.game.player;

import java.io.Serializable;

import net.nocturne.game.World;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.utils.Color;

@SuppressWarnings("serial")
public class DonationManager implements Serializable {

	private int amountDonated;
	private Player player;

	public DonationManager() {
		amountDonated = 0;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void increaseDonationAmount(int amount) {
		handleUpgrade(amountDonated, amountDonated + amount);
		amountDonated += amount;
	}

	private int getTotalDonated() {
		return amountDonated;
	}

	public boolean isDonator() {
		return amountDonated >= 10;
	}

	public boolean isExtremeDonator() {
		return amountDonated >= 15;
	}

	public boolean isLegendaryDonator() {
		return amountDonated >= 25;
	}

	public boolean isSupremeDonator() {
		return amountDonated >= 50;
	}

	public boolean isDivineDonator() {
		return amountDonated >= 100;
	}

	public boolean isAngelicDonator() {
		return amountDonated >= 200;
	}

	public boolean isDemonicDonator() {
		return amountDonated >= 400;
	}

	public boolean isHeroicDonator() {
		return amountDonated >= 800;
	}

	public void handleBond() {
		if (!(player.getInventory().containsItem(ItemIdentifiers.BOND, 1) || player
				.getInventory().containsItem(ItemIdentifiers.BOND_UNTRADEABLE,
						1)))
			return;
		if (player.getInventory().containsItem(
				ItemIdentifiers.BOND_UNTRADEABLE, 1))
			player.getInventory().removeItemMoneyPouch(
					ItemIdentifiers.BOND_UNTRADEABLE, 1);
		else
			player.getInventory().removeItemMoneyPouch(ItemIdentifiers.BOND, 1);
		increaseDonationAmount(5);
		player.getPackets().sendGameMessage(
				Color.GREEN,
				"Congratulations. You have just redeemed a bond! You have donated a total of "
						+ getTotalDonated() + ".");
	}

	private void handleUpgrade(int prevAmount, int newAmount) {
		if (!getDonatorTitle(prevAmount).equalsIgnoreCase(
				getDonatorTitle(newAmount))) {
			player.getBank().addItem(new Item(ItemIdentifiers.KEEPSAKE_KEY, 1),
					true);
			player.getPackets().sendGameMessage(Color.YELLOW,
					"A dragon keepsake key has been added to your bank!");
			World.sendNews(player.getDisplayName() + " has just upgraded "
					+ (player.getAppearence().isMale() ? "his" : "her")
					+ " donator status from "
					+ getDonatorTitle(prevAmount).toLowerCase() + " to "
					+ getDonatorTitle(newAmount).toLowerCase() + "!", 0, true);
		}
	}

	public String getDonatorTitle() {
		return getDonatorTitle(amountDonated);
	}

	private String getDonatorTitle(int amount) {
		if (amount >= 10 && amount < 14)
			return "Regular";
		else if (amount >= 15 && amount < 25)
			return "Extreme";
		else if (amount >= 25 && amount < 50)
			return "Legendary";
		else if (amount >= 50 && amount < 100)
			return "Supreme";
		else if (amount >= 100 && amount < 200)
			return "Divine";
		else if (amount >= 200 && amount < 400)
			return "Angelic";
		else if (amount >= 400 && amount < 800)
			return "Demonic";
		else if (amount >= 800)
			return "Heroic";
		return "Non";
	}
}