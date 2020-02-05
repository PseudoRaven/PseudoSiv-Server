package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.content.activities.minigames.ZarosGodwars;
import net.nocturne.game.player.dialogues.Dialogue;

public final class NexEntrance extends Dialogue {

	@Override
	public void start() {
		sendDialogue("The room beyond this point is a prison!",
				"There is no way out other than death or teleport.",
				"Only those who endure dangerous encounters should proceed.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			stage = 0;
			sendOptionsDialogue("There are currently "
					+ ZarosGodwars.getPlayers().size()
					+ " people fighting.<br>Do you wish to join them?",
					"Climb down.", "Stay here.");
		} else if (stage == 0) {
			if (componentId == OPTION_1) {
				player.setNextWorldTile(new WorldTile(2911, 5204, 0));
				player.getControllerManager().startController("ZEDController");
			}
			end();
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
