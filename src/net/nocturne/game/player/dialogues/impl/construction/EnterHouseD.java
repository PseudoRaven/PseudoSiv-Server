package net.nocturne.game.player.dialogues.impl.construction;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.construction.House;
import net.nocturne.game.player.dialogues.Dialogue;

public class EnterHouseD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Go to your house.",
				"Go to your house (building mode).", "Go to a friend's house.",
				"Nevermind.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1 || componentId == OPTION_2) {
			// this method checks location u entering from
			enterHouse(player, componentId == OPTION_2);
		} else if (componentId == OPTION_3) {
			enterFriendsHouse(player);
		}
		end();

	}

	public static void enterHouse(Player player, boolean forceBuildMode) {
		player.getHouse().setBuildMode(forceBuildMode);
		House.enterHouse(player, player.getDisplayName());
	}

	public static void enterFriendsHouse(Player player) {
		player.getTemporaryAttributtes().put("enterhouse", Boolean.TRUE);
		player.getPackets().sendInputNameScript(
				"Enter the name of the house you'd like to enter:");
	}

	@Override
	public void finish() {

	}
}