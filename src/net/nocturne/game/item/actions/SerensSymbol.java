package net.nocturne.game.item.actions;

import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

public class SerensSymbol {

	public static void joinPieces(Player player) {

		if (!player.getInventory().containsOneItem(
				ItemIdentifiers.SERENS_SYMBOL)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need an uncharged Seren Symbol in order to combine.");
			return;
		}
		if (canMake(player)) {
			player.getInventory().removeItems(PIECES);
			player.getInventory().removeItemMoneyPouch(
					ItemIdentifiers.SERENS_SYMBOL, 1);
			player.getInventory().addItem(ItemIdentifiers.SERENS_SYMBOL, 1);

		} else
			player.getPackets().sendGameMessage(
					"You do not have all of the symbol pieces to continue.");
	}

	private static boolean canMake(Player player) {
		return player.getInventory().containsItems(PIECES)
				&& player.getInventory().containsOneItem(
						ItemIdentifiers.SERENS_SYMBOL_UNF);
	}

	private static final Item[] PIECES = new Item[] {
			new Item(ItemIdentifiers.AMLODD_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.CADARN_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.CRWYS_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.HEFIN_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.IORWERTH_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.ITHELL_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.MEILYR_SYMBOL_PIECE, 1),
			new Item(ItemIdentifiers.TRAHAEARN_SYMBOL_PIECE, 1),

	};

	public static void Activate(Player player) {
		if (!player.getInventory().containsOneItem(
				ItemIdentifiers.SERENS_SYMBOL))
			return;
		handleXP(player);
		player.getInventory().removeItemMoneyPouch(
				ItemIdentifiers.SERENS_SYMBOL, 1);
		player.getInventory().addItemMoneyPouch(
				ItemIdentifiers.SERENS_SYMBOL_DRAINED, 1);
		player.getPackets()
				.sendGameMessage(
						"As you gain knowledge from the artifact, it drains to a normal Symbol.");
	}

	private static void handleXP(Player player) {
		if (!player.getInventory().containsOneItem(
				ItemIdentifiers.SERENS_SYMBOL))
			return;

		for (int i = 0; i < AvailableSkills.length; i++)
			player.getSkills().addXp(AvailableSkills[i], 15000);

	}

	private static final int[] AvailableSkills = { Skills.AGILITY,
			Skills.ATTACK, Skills.CRAFTING, Skills.CONSTRUCTION,
			Skills.DIVINATION, Skills.DUNGEONEERING, Skills.DEFENCE,
			Skills.FARMING, Skills.HERBLORE, Skills.MAGIC, Skills.MINING,
			Skills.PRAYER, Skills.RANGED, Skills.SLAYER, Skills.SMITHING,
			Skills.STRENGTH, Skills.SUMMONING, Skills.WOODCUTTING };

}
