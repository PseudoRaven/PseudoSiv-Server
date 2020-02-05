package net.nocturne.game.item.actions;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

/**
 * @author Miles Black (bobismyname)
 */

public class SmithingCombinations {

	public enum Combos {

		STUDDED_BODY(new Item(2370, 1), new Item(1129, 1), 1133, 1), STUDDED_GLOVES(
				new Item(2370, 1), new Item(1059, 1), 25877, 1), STUDDED_BOOTS(
				new Item(2370, 1), new Item(1061, 1), 25823, 1), STUDDED_CHAPS(
				new Item(2370, 1), new Item(1095, 1), 1097, 1), STUDDED_SHIELD(
				new Item(2370, 1), new Item(25806, 1), 25810, 1), CRYSTAL_BOW(
				new Item(32206, 1), new Item(32622, 750), 4212, 75), CRYSTAL_HALBERD(
				new Item(32206, 1), new Item(32622, 750), 32219, 75), CRYSTAL_STAFF(
				new Item(32206, 1), new Item(32622, 750), 32210, 75), CRYSTAL_CHAKRAM(
				new Item(32206, 1), new Item(32622, 375), 32231, 75), CRYSTAL_OFF_CHAKRAM(
				new Item(32206, 1), new Item(32622, 375), 32234, 75), CRYSTAL_DAGGER(
				new Item(32206, 1), new Item(32622, 375), 32222, 75), CRYSTAL_OFF_DAGGER(
				new Item(32206, 1), new Item(32622, 375), 32225, 75), CRYSTAL_WAND(
				new Item(32206, 1), new Item(32622, 375), 32213, 75), CRYSTAL_ORB(
				new Item(32206, 1), new Item(32622, 375), 32216, 75), ATTUNED_BOW(
				new Item(32625, 1), new Item(32622, 2000), 32653, 90), ATTUNED_HALBERD(
				new Item(32625, 1), new Item(32622, 2000), 32647, 90), ATTUNED_STAFF(
				new Item(32625, 1), new Item(32622, 2000), 32659, 90), ATTUNED_CHAKRAM(
				new Item(32625, 1), new Item(32622, 1000), 32655, 90), ATTUNED_OFF_CHAKRAM(
				new Item(32625, 1), new Item(32622, 1000), 32657, 90), ATTUNED_DAGGER(
				new Item(32625, 1), new Item(32622, 1000), 32649, 90), ATTUNED_OFF_DAGGER(
				new Item(32625, 1), new Item(32622, 1000), 32651, 90), ATTUNED_WAND(
				new Item(32625, 1), new Item(32622, 1000), 32661, 90), ATTUNED_ORB(
				new Item(32625, 1), new Item(32622, 1000), 32663, 90), CRYSTAL_DEFLECTOR(
				new Item(32623, 1), new Item(32622, 375), 32243, 75), CRYSTAL_WARD(
				new Item(32623, 1), new Item(32622, 375), 32237, 75), CRYSTAL_SHIELD(
				new Item(32623, 1), new Item(32622, 375), 32240, 75), ATTUNED_DEFLECTOR(
				new Item(32626, 1), new Item(32622, 1000), 32629, 90), ATTUNED_WARD(
				new Item(32626, 1), new Item(32622, 1000), 32631, 90), ATTUNED_SHIELD(
				new Item(32626, 1), new Item(32622, 1000), 32627, 90), CRYSTAL_CHIME(
				new Item(32208, 1), new Item(32622, 150), 32644, 80), CRYSTAL_SAW(
				new Item(32208, 1), new Item(32622, 150), 32633, 80), CRYSTAL_CHISEL(
				new Item(32208, 1), new Item(32622, 150), 32642, 80), CRYSTAL_HAMMER(
				new Item(32208, 1), new Item(32622, 150), 32640, 80), CRYSTAL_KNIFE(
				new Item(32208, 1), new Item(32622, 150), 32635, 80), CRYSTAL_TINDERBOX(
				new Item(32208, 1), new Item(32622, 150), 32637, 80), LAVA_WHIP(
				new Item(34151, 1), new Item(4151, 1), 34150, 1), STAFF_OF_DARKNESS(
				new Item(34153, 1), new Item(15486, 1), 34155, 1), STRYKEBOW(
				new Item(34156, 1), new Item(11235, 1), 34158, 1), MALEVOLENT_HELM(
				new Item(30027, 14), new Item(30028, 1), 30005, 91), MALEVOLENT_GREAVES(
				new Item(30027, 28), new Item(30028, 2), 30011, 92), MALEVOLENT_CUIRASS(
				new Item(30027, 42), new Item(30028, 3), 30008, 93), SIRENIC_MASK(
				new Item(29863, 14), new Item(29864, 1), 29854, 91), SIRENIC_CHAPS(
				new Item(29863, 28), new Item(29864, 2), 29860, 92), SIRENIC_HAUBERK(
				new Item(29863, 42), new Item(29864, 3), 29857, 93), AHRIM_BOBBLE(
				new Item(11846, 1), new Item(30027, 250), 30031, 1), DHAROK_BOBBLE(
				new Item(11848, 1), new Item(30027, 250), 30032, 1), GUTHAN_BOBBLE(
				new Item(11850, 1), new Item(30027, 250), 30033, 1), KARIL_BOBBLE(
				new Item(11852, 1), new Item(30027, 250), 30034, 1), TORAG_BOBBLE(
				new Item(11854, 1), new Item(30027, 250), 30035, 1), VERAC_BOBBLE(
				new Item(11856, 1), new Item(30027, 250), 30036, 1), KALPHITE_DEFENDER1(
				new Item(ItemIdentifiers.PERFECT_CHITIN, 1), new Item(
						ItemIdentifiers.OFFHAND_DRYGORE_MACE, 1),
				ItemIdentifiers.KALPHITE_DEFENDER, 1), KALPHITE_DEFENDER2(
				new Item(ItemIdentifiers.PERFECT_CHITIN, 1), new Item(
						ItemIdentifiers.OFFHAND_DRYGORE_RAPIER, 1),
				ItemIdentifiers.KALPHITE_DEFENDER, 1), KALPHITE_DEFENDER3(
				new Item(ItemIdentifiers.PERFECT_CHITIN, 1), new Item(
						ItemIdentifiers.OFFHAND_DRYGORE_LONGSWORD, 1),
				ItemIdentifiers.KALPHITE_DEFENDER, 1), KALPHITE_REPRISER(
				new Item(ItemIdentifiers.PERFECT_CHITIN, 1), new Item(
						ItemIdentifiers.OFFHAND_ASCENSION_CROSSBOW, 1),
				ItemIdentifiers.KALPHITE_REPRISER, 1), KALPHITE_REBOUNDER(
				new Item(ItemIdentifiers.PERFECT_CHITIN, 1), new Item(
						ItemIdentifiers.SEISMIC_SINGULARITY, 1),
				ItemIdentifiers.KALPHITE_REBOUNDER, 1), DOOGLE_SARDINE(
				new Item(327, 1), new Item(1573, 1), 1552, 1), VINE_WHIP(
				new Item(21369, 1), new Item(4151, 1), 21371, 1), BLESSED_SPIRIT(
				new Item(13734, 1), new Item(13754, 1), 13736, 1), DRAGON_SQUARE(
				new Item(2366, 1), new Item(2368, 1), 1187, 60), DRAGONFIRE_SHIELD(
				new Item(11286, 1), new Item(1540, 1), 11283, 90), ARCANE_GODSWORD(
				new Item(11690, 1), new Item(13746, 1), 29961, 99), DIVINE_GODSWORD(
				new Item(11690, 1), new Item(13748, 1), 29960, 99), ELYSIAN_GODSWORD(
				new Item(11690, 1), new Item(13750, 1), 29959, 99), SPECTRAL_GODSWORD(
				new Item(11690, 1), new Item(13752, 1), 29958, 99), SLAYER_HELMET(
				new Item(11690, 1), new Item(13752, 1), 29958, 99), ANCIENT_DEFENDER(
				new Item(ItemIdentifiers.CHAOTIC_SPLINT, 1), new Item(
						ItemIdentifiers.ANCIENT_EMBLEM, 1),
				ItemIdentifiers.ANCIENT_DEFENDER, 1), ANCIENT_LANTERN(new Item(
				ItemIdentifiers.CHAOTIC_SPLINT, 1), new Item(
				ItemIdentifiers.ANCIENT_EMBLEM, 1),
				ItemIdentifiers.ANCIENT_LANTERN, 1), ANCIENT_REPRISER(new Item(
				ItemIdentifiers.CHAOTIC_SPLINT, 1), new Item(
				ItemIdentifiers.ANCIENT_EMBLEM, 1),
				ItemIdentifiers.ANCIENT_REPRISER, 1), NOXIOUS_LONGBOW(new Item(
				ItemIdentifiers.SPIDER_LEG, 1), new Item(
				ItemIdentifiers.ARAXXIS_WEB, 1),
				ItemIdentifiers.NOXIOUS_LONGBOW, 1), NOXIOUS_STAFF(new Item(
				ItemIdentifiers.SPIDER_LEG, 1), new Item(
				ItemIdentifiers.ARAXXIS_EYE, 1), ItemIdentifiers.NOXIOUS_STAFF,
				1), NOXIOUS_SCYTHE(new Item(ItemIdentifiers.SPIDER_LEG, 1),
				new Item(ItemIdentifiers.ARAXXIS_FANG, 1),
				ItemIdentifiers.NOXIOUS_SCYTHE, 1);

		public static Combos forId(int id) {
			return combos.get(id);
		}

		public static Map<Integer, Combos> combos = new HashMap<Integer, Combos>();

		static {
			for (final Combos combo : Combos.values()) {
				combos.put(combo.usedWith.getId(), combo);
			}
		}

		private final Item usedWith;
		private final Item itemUsed;
		private final int combinedId;
		private final int level;

		Combos(Item usedWith, Item itemUsed, int combinedId, int level) {
			this.usedWith = usedWith;
			this.itemUsed = itemUsed;
			this.combinedId = combinedId;
			this.level = level;
		}

		public int getCombinedId() {
			return combinedId;
		}

		public Item getItemUsed() {
			return itemUsed;
		}

		public int getLevel() {
			return level;
		}

		public Item getUsedWith() {
			return usedWith;
		}
	}

	public static boolean combineItem(Player player, int usedWith, int itemUsed) {
		if (Combos.forId(usedWith) != null || Combos.forId(itemUsed) != null) {
			Combos combo = Combos.forId(usedWith) != null ? Combos
					.forId(usedWith) : Combos.forId(itemUsed);
			if (!player.getSkills().hasLevel(Skills.SMITHING, combo.getLevel()))
				return true;
			if (combo == Combos.MALEVOLENT_CUIRASS
					|| combo == Combos.MALEVOLENT_GREAVES
					|| combo == Combos.MALEVOLENT_HELM) {
				player.getDialogueManager().startDialogue("MalevolentArmor");
				return true;
			} else if (combo == Combos.SIRENIC_MASK
					|| combo == Combos.SIRENIC_HAUBERK
					|| combo == Combos.SIRENIC_CHAPS) {
				player.getDialogueManager().startDialogue("SirenicArmor");
				return true;
			} else if (combo == Combos.CRYSTAL_BOW
					|| combo == Combos.CRYSTAL_HALBERD
					|| combo == Combos.CRYSTAL_STAFF
					|| combo == Combos.CRYSTAL_CHAKRAM
					|| combo == Combos.CRYSTAL_OFF_CHAKRAM
					|| combo == Combos.CRYSTAL_DAGGER
					|| combo == Combos.CRYSTAL_OFF_DAGGER
					|| combo == Combos.CRYSTAL_WAND
					|| combo == Combos.CRYSTAL_ORB) {
				player.getDialogueManager()
						.startDialogue("CrystalWeapon", true);
				return true;
			} else if (combo == Combos.ATTUNED_BOW
					|| combo == Combos.ATTUNED_HALBERD
					|| combo == Combos.ATTUNED_STAFF
					|| combo == Combos.ATTUNED_CHAKRAM
					|| combo == Combos.ATTUNED_OFF_CHAKRAM
					|| combo == Combos.ATTUNED_DAGGER
					|| combo == Combos.ATTUNED_OFF_DAGGER
					|| combo == Combos.ATTUNED_WAND
					|| combo == Combos.ATTUNED_ORB) {
				player.getDialogueManager().startDialogue("CrystalWeapon",
						false);
				return true;
			} else if (combo == Combos.CRYSTAL_DEFLECTOR
					|| combo == Combos.CRYSTAL_WARD
					|| combo == Combos.CRYSTAL_SHIELD) {
				player.getDialogueManager()
						.startDialogue("CrystalArmour", true);
				return true;
			} else if (combo == Combos.ATTUNED_DEFLECTOR
					|| combo == Combos.ATTUNED_WARD
					|| combo == Combos.ATTUNED_SHIELD) {
				player.getDialogueManager().startDialogue("CrystalArmour",
						false);
				return true;
			} else if (combo == Combos.CRYSTAL_CHIME
					|| combo == Combos.CRYSTAL_SAW
					|| combo == Combos.CRYSTAL_CHISEL
					|| combo == Combos.CRYSTAL_HAMMER
					|| combo == Combos.CRYSTAL_KNIFE
					|| combo == Combos.CRYSTAL_TINDERBOX) {
				player.getDialogueManager().startDialogue("CrystalTool");
				return true;
			} else if (combo == Combos.ANCIENT_DEFENDER
					|| combo == Combos.ANCIENT_LANTERN
					|| combo == Combos.ANCIENT_REPRISER) {
				player.getDialogueManager().startDialogue("ChaoticSplint");
				return true;
			}
			if (player.getInventory().containsItems(combo.getUsedWith(),
					combo.getItemUsed())) {
				player.getInventory().deleteItem(combo.getItemUsed());
				player.getInventory().deleteItem(combo.getUsedWith());
				player.getInventory().addItemMoneyPouch(combo.getCombinedId(),
						1);
				player.getPackets().sendGameMessage(
						"You combine the "
								+ combo.getUsedWith().getName().toLowerCase()
								+ " with the "
								+ combo.getItemUsed().getName().toLowerCase()
								+ " and create a "
								+ ItemDefinitions
										.getItemDefinitions(
												combo.getCombinedId())
										.getName().toLowerCase() + ".");
			}
			return true;
		}
		return false;
	}

	public static void processCombo(Player player, Combos combo) {
		if (!player.getSkills().hasLevel(Skills.SMITHING, combo.getLevel()))
			return;
		if (player.getInventory().containsItems(combo.getUsedWith(),
				combo.getItemUsed())) {
			player.getInventory().deleteItem(combo.getItemUsed());
			player.getInventory().deleteItem(combo.getUsedWith());
			player.getInventory().addItemMoneyPouch(combo.getCombinedId(), 1);
			player.getPackets().sendGameMessage(
					"You combine the "
							+ combo.getUsedWith().getName().toLowerCase()
							+ " with the "
							+ combo.getItemUsed().getName().toLowerCase()
							+ " and create a "
							+ ItemDefinitions
									.getItemDefinitions(combo.getCombinedId())
									.getName().toLowerCase() + ".");
		}
	}

	public static boolean dismantleItem(Player player, int itemId) {
		for (final Combos c : Combos.values()) {
			if (c.getCombinedId() == itemId) {
				player.getInventory().deleteItem(itemId, 1);
				player.getInventory().addItemMoneyPouch(c.getUsedWith());
				player.getInventory().addItemMoneyPouch(c.getItemUsed());
				player.getPackets().sendGameMessage(
						"You successfully split the "
								+ ItemDefinitions.getItemDefinitions(itemId)
										.getName().toLowerCase() + " apart.");
				return true;
			}
		}
		return false;
	}

}