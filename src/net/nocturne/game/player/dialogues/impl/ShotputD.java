package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class ShotputD extends Dialogue {

	private boolean is18LB;

	@Override
	public void start() {
		is18LB = (boolean) this.parameters[0];
		player.setNextAnimation(new Animation(827));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Standing Throw.",
						"Step and throw.", "Spin and throw.");
			}
		});
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		Controller Controller = player.getControllerManager().getController();
		if (Controller == null || !(Controller instanceof WarriorsGuild)) {
			end();
			return;
		}
		WarriorsGuild currentGuild = (WarriorsGuild) Controller;
		if (componentId == OPTION_1) {
			currentGuild.prepareShotput((byte) 0, is18LB);
			player.setNextAnimation(new Animation(15079));
		} else if (componentId == OPTION_2) {
			currentGuild.prepareShotput((byte) 1, is18LB);
			player.setNextAnimation(new Animation(15080));
		} else {
			currentGuild.prepareShotput((byte) 2, is18LB);
			player.setNextAnimation(new Animation(15078));
		}
		end();
	}

	@Override
	public void finish() {

	}
}
