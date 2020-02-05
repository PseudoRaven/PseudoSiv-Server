package net.nocturne.game.item.actions;

import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.network.decoders.WorldPacketsDecoder;

public class RottenPotato {

	/**
	 * @author: miles M
	 */

	private static final int EAT = WorldPacketsDecoder.ACTION_BUTTON1_PACKET,

	HEAL = WorldPacketsDecoder.ACTION_BUTTON2_PACKET,

	CM_TOOL = WorldPacketsDecoder.ACTION_BUTTON3_PACKET,

	USE = -1, COMMANDS = WorldPacketsDecoder.ACTION_BUTTON6_PACKET,

	DROP = WorldPacketsDecoder.ACTION_BUTTON8_PACKET,

	EXAMINE = WorldPacketsDecoder.ACTION_BUTTON10_PACKET

	;

	public static void handlePotato(Player player, int packetId) {
		if (player.getRights() < 2) {
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, Integer.MAX_VALUE);
			return;
		}
		switch (packetId) {
		case EAT:
			player.getDialogueManager().startDialogue("OP1");
			break;
		case HEAL:
			heal(player);
			break;
		case CM_TOOL:
			player.getDialogueManager().startDialogue("OP3");
			break;
		case USE:
			break;
		case COMMANDS:
			player.getDialogueManager().startDialogue("OP5");
			break;
		case DROP:
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, 1);
			player.getPackets().sendGameMessage("You're an idiot... do ;;rottenpotato to get another one.",
					true);
			break;
		case EXAMINE:
			player.getPackets().sendGameMessage("This will help you be a better mod.", true);
			break;
		}
	}

	private static void heal( Player player ) {
		if (player.getRights() == 2) {
			player.getPrayer().boost(990);
			player.setRunEnergy(100);
			player.getCombatDefinitions().increaseSpecialAttack(100);
			player.getAppearence().generateAppearenceData();
			player.heal(player.getMaxHitpoints());
			player.getPackets().sendGameMessage("You have been healed.", true);
		} else
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, Integer.MAX_VALUE);
	}

	public static void useOnPlayer(Player player, Player target) {
		if (player.getRights() == 2)
			player.getDialogueManager().startDialogue("OPuOP", target);
		else
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, Integer.MAX_VALUE);
	}

	public static void useOnNpc(Player player, NPC npc) {
		if (player.getRights() == 2)
			player.getDialogueManager().startDialogue("OPuON", npc);
		else
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, Integer.MAX_VALUE);
	}

	public static void useOnObject(Player player, Object object) {
		if (player.getRights() == 2)
			player.getDialogueManager().startDialogue("OPuOB", object);
		else
			player.getInventory().deleteItem(ItemIdentifiers.ROTTEN_POTATO, Integer.MAX_VALUE);
	}
}