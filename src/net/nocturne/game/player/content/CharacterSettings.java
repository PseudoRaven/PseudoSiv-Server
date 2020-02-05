package net.nocturne.game.player.content;

import net.nocturne.game.player.Player;
import net.nocturne.utils.ScrollMessage;

public class CharacterSettings {

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 2:
			StaffList.send(player);
			break;
		case 4:
			player.getDialogueManager().startDialogue("LadyIthell", 19560);
			break;
		case 6:
			player.getDialogueManager().startDialogue("Ariane", 23009, null);
			break;
		case 8:
			player.getDialogueManager().startDialogue("OpenURLPrompt", "vote");
			break;
		case 10:
			player.getDialogueManager().startDialogue("OpenURLPrompt", "");
			break;
		case 12:
			player.getDialogueManager().startDialogue("TicketDialogue");
			break;
		case 14:
			ScrollMessage.displayScrollMessageUpdate(player, "donatorinfo");
			break;
		default:
			player.getPackets().sendGameMessage(
					"This button does not have an action set.");
			break;
		}
	}
}