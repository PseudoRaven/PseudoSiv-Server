package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.activities.minigames.fistofguthix.MinigameManager;
import net.nocturne.game.player.controllers.FistOfGuthixController;
import net.nocturne.game.player.dialogues.Dialogue;

public class LeaveFistOfGuthix extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Really leave?", "Yes", "No");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			switch (componentId) {
			case OPTION_1:
				if ((FistOfGuthixController) player.getControllerManager()
						.getController() != null)
					((FistOfGuthixController) player.getControllerManager()
							.getController()).exit(true);
				if (MinigameManager.INSTANCE().fistOfGuthix().team(player) != null) {
					MinigameManager.INSTANCE().fistOfGuthix().team(player)
							.forfeit(player);
				}
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
		}
	}

	@Override
	public void finish() {
	}

}
