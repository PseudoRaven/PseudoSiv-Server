package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class ArdougneCloak extends Dialogue {

	@Override
	public void start() {
		boolean all = (boolean) parameters[0];
		if (all)
			sendOptionsDialogue("Where would you like to teleport to?",
					"Kandarin Monastery.", "Ardougne Farm.", "Nowhere.");
		else
			sendOptionsDialogue("Where would you like to teleport to?",
					"Kandarin Monastery.", "Nowhere.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		boolean all = (boolean) parameters[0];
		switch (componentId) {
		case OPTION_1:
			end();
			WorldTasksManager.schedule(new WorldTask() {
				int timer;

				@Override
				public void run() {
					switch (timer) {
					case 0:
						player.lock(2);
						player.setNextGraphics(new Graphics(2670));
						player.setNextAnimation(new Animation(3254));
						break;
					case 1:
						player.setNextWorldTile(new WorldTile(2606, 3222, 0));
						break;
					case 2:
						player.setNextGraphics(new Graphics(2671));
						player.setNextAnimation(new Animation(3255));
						break;
					}
					timer++;
				}
			}, 0, 1);
			break;
		case OPTION_2:
			end();
			if (!all)
				return;
			WorldTasksManager.schedule(new WorldTask() {
				int timer;

				@Override
				public void run() {
					switch (timer) {
					case 0:
						player.lock(2);
						player.setNextGraphics(new Graphics(2670));
						player.setNextAnimation(new Animation(3254));
						break;
					case 1:
						player.setNextWorldTile(new WorldTile(2674, 3375, 0));
						break;
					case 2:
						player.setNextGraphics(new Graphics(2671));
						player.setNextAnimation(new Animation(3255));
						break;
					}
					timer++;
				}
			}, 0, 1);
			break;
		case OPTION_3:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}