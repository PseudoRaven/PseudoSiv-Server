package net.nocturne.game.item.actions;

import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.utils.Color;

/**
 * @author Miles Black (bobismyname)
 * @author Delcorum
 */

public class StrangeRock {

	public static boolean inspect(final Player player, int id) {
		if (id == ItemIdentifiers.STRANGE_TELEORB) {
			player.getDialogueManager().startDialogue("StrangeRock", "inspect");
			return true;
		}
		return false;
	}

	public static boolean lookAt(final Player player, int slotId, int id) {
		if (id >= ItemIdentifiers.STRANGE_ROCK_COOKING
				&& id <= ItemIdentifiers.STRANGE_ROCK_HUNTER) {
			player.getInventory().sendExamine(slotId);
			return true;
		}
		return false;
	}

	public static boolean teleport(final Player player, int id) {
		if (id == ItemIdentifiers.STRANGE_TELEORB) {
			if (!player.getInventory().containsItem(
					ItemIdentifiers.STRANGE_TELEORB, 1)) {
				player.getPackets().sendGameMessage(Color.ORANGE,
						"You don't even have a strange teleorb.");
				return true;
			}
			player.getDialogueManager()
					.startDialogue("StrangeRock", "teleport");
			return true;
		}
		return false;
	}

	public static void createTeleport(Player player) {
		if (hasFullRock(player)) {
			for (int i = ItemIdentifiers.STRANGE_ROCK_COOKING; i <= ItemIdentifiers.STRANGE_ROCK_HUNTER; i += 2)
				player.getInventory().deleteItem(i, 1);
			player.getInventory().addItemDrop(ItemIdentifiers.STRANGE_TELEORB,
					1, player);
			player.getPackets()
					.sendGameMessage(Color.ORANGE,
							"You put together the strange rocks and create a teleportation orb.");
			player.getCompCapeManager().increaseRequirement(
					Requirement.STRANGE_ROCKS, 1);
			return;
		}
		player.getPackets()
				.sendGameMessage(Color.ORANGE,
						"You need 15 strange rocks from each skill to create a teleportation orb.");
		player.getPackets()
				.sendGameMessage(Color.ORANGE,
						"Strange rocks are obtained through training noncombat skills.");
	}

	public static void earnRock(Player player, int skillId) {

		switch (skillId) {
		case Skills.COOKING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_COOKING, 1);
			break;
		case Skills.WOODCUTTING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_WOODCUTTING, 1);
			break;
		case Skills.FLETCHING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_FLETCHING, 1);
			break;
		case Skills.FISHING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_FISHING, 1);
			break;
		case Skills.FIREMAKING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_FIREMAKING, 1);
			break;
		case Skills.CRAFTING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_CRAFTING, 1);
			break;
		case Skills.SMITHING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_SMITHING, 1);
			break;
		case Skills.HERBLORE:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_HERBLORE, 1);
			break;
		case Skills.AGILITY:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_AGILTY, 1);
			break;
		case Skills.THIEVING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_THIEVING, 1);
			break;
		case Skills.FARMING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_FARMING, 1);
			break;
		case Skills.RUNECRAFTING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_RUNECRAFTING, 1);
			break;
		case Skills.CONSTRUCTION:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_CONSTRUCTION, 1);
			break;
		case Skills.HUNTER:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_HUNTER, 1);
			break;
		case Skills.MINING:
			player.getInventory().addItemDrop(
					ItemIdentifiers.STRANGE_ROCK_MINING, 1);
			break;
		}
		player.getPackets().sendGameMessage(Color.ORANGE,
				"You find a strange rock in your inventory while skilling.");
		player.getPackets()
				.sendGameMessage(Color.ORANGE,
						"Maybe I should collect all 15 different types and combine them.");
	}

	private static boolean hasFullRock(Player player) {
		return player.getInventory().containsItem(
				ItemIdentifiers.STRANGE_ROCK_CONSTRUCTION, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_FARMING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_COOKING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_THIEVING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_MINING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_HUNTER, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_RUNECRAFTING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_AGILTY, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_SMITHING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_HERBLORE, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_CRAFTING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_FIREMAKING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_FISHING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_FLETCHING, 1)
				&& player.getInventory().containsItem(
						ItemIdentifiers.STRANGE_ROCK_WOODCUTTING, 1);
	}

}
