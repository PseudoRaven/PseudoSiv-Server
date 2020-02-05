package net.nocturne.game.player.actions.skills.smithing;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

public class DragonfireShield {

	public static void joinPieces(Player player) {
		if (!player.getInventory().containsItemToolBelt(Smithing.HAMMER)
				|| !player.getInventory().getItems()
						.contains(new Item(ItemIdentifiers.HAMMER))) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a hammer in order to work with the visage.");
			return;
		}
		if (!player.getSkills().hasLevel(Skills.SMITHING, 90))
			return;
		if (!player.getInventory().containsItem(1540, 1)) {
			player.getDialogueManager()
					.startDialogue("SimpleMessage",
							"You need a anti-dragon shield to forge a dragonfire shield.");
			return;
		}
		if (!player.getInventory().containsItem(11286, 1)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You need a dragonic visage to forge a dragonfire shield.");
			return;
		}
		createShield(player);
	}

	private static void createShield(Player player) {
		player.lock(2);
		player.setNextAnimation(new Animation(898));
		player.getInventory().deleteItem(ItemIdentifiers.ANTIDRAGON_SHIELD, 1);
		player.getInventory().deleteItem(ItemIdentifiers.DRACONIC_VISAGE, 1);
		player.getInventory().addItem(ItemIdentifiers.DRAGONFIRE_SHIELD, 1);
		player.getSkills().addXp(Skills.SMITHING, 2000);
		player.getDialogueManager()
				.startDialogue(
						"SimpleDialogue",
						"Even for an experienced smith it is not an easy task, but eventually it is done.");
	}

	public static void chargeDFS(Player player, boolean fully) {
		int shieldId = player.getEquipment().getShieldId();
		if (shieldId != 11284 && shieldId != 11283)
			return;
		if (shieldId == ItemIdentifiers.DRAGONFIRE_SHIELD) {
			player.getEquipment().getItem(Equipment.SLOT_SHIELD)
					.setId(ItemIdentifiers.DRAGONFIRE_SHIELD);
			player.getEquipment().refresh(Equipment.SLOT_SHIELD);
			player.getAppearence().generateAppearenceData();
		}
		if (player.getCharges().getCharges(ItemIdentifiers.DRAGONFIRE_SHIELD) == 50) {
			player.getPackets().sendGameMessage(
					"Your dragonfire shield is already full.", true);
			return;
		}
		player.getCharges().addCharges(11283, fully ? 50 : 1,
				Equipment.SLOT_SHIELD);
		player.getCombatDefinitions().refreshBonuses();
		player.setNextAnimationNoPriority(new Animation(6695));
		player.setNextGraphics(new Graphics(1164));
		player.getPackets().sendGameMessage(
				"Your dragonfire shield glows more brightly.", true);
	}

	public static void empty(Player player) {
		player.lock(1);
		player.getCharges().addCharges(ItemIdentifiers.DRAGONFIRE_SHIELD, -50,
				-1);
		player.setNextGraphics(new Graphics(1168));
		player.setNextAnimation(new Animation(6700));
		player.getPackets()
				.sendGameMessage("You empty your dragonfire shield.");
	}

	public static boolean isDragonFireShield(int id) {
		return id == ItemIdentifiers.ANTIDRAGON_SHIELD;
	}
}