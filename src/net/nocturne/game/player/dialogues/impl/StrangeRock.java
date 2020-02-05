package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class StrangeRock extends Dialogue {

	String option;

	@Override
	public void start() {
		if (parameters.length > 0) {
			option = (String) parameters[0];
			if (option.equals("inspect"))
				sendPlayerDialogue(9827, "Hmm... I wonder what this does...");
			else if (option.equals("teleport")) {
				teleport();
				player.getInterfaceManager().removeCentralInterface();
				end();
			}
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (option.equals("inspect")) {

			switch (stage) {
			case -1:
				sendDialogue("You study the stone, and it looks very old...");
				stage++;
				break;
			case 0:
				sendPlayerDialogue(9827, "I think I found an inscription!");
				stage++;
				break;
			case 1:
				sendDialogue("The stone reads that 'Those who dare to enter Lucien's throne will be destroyed. BEWARE Lucien.'");
				stage++;
				break;
			case 2:
				sendNPCDialogue(
						15353,
						9842,
						"Ha! You've found my stone. I hope you come looking for me, because I am ready to fight!");
				stage++;
				break;
			default:
				end();
				break;

			}

		} else if (option.equals("teleport")) {
			teleport();
			player.getInterfaceManager().removeCentralInterface();
			end();
		}
	}

	public void teleport() {
		player.setNextAnimation(new Animation(9597));
		player.setNextGraphics(new Graphics(1680));
		player.getInventory().deleteItem(14534, 1);
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.setNextAnimation(new Animation(4731));
					stage = 1;
				} else if (stage == 1) {
					player.getControllerManager().startController(
							"LucienController");
					player.setNextAnimation(new Animation(-1));
					stage = 2;
				} else if (stage == 2) {
					player.resetReceivedDamage();
					player.unlock();
					stop();
				}
			}
		}, 0, 1);
	}

	@Override
	public void finish() {

	}

}
