package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.Animation;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class DragonFireShieldCreating extends Dialogue {

	@Override
	public void start() {
		if (!player.getInventory().containsItem(11286, 1)
				&& player.getInventory().containsItem(1540, 1)) {
			sendDialogue("You must have both the draconic visage and the anti-dragon-shield in your inventory to complete the shield.");
			stage = 5;
			return;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < 90) {
			sendDialogue("You need a level of 90 Smithing in order to complete this shield.");
			stage = 5;
			return;
		}
		sendItemDialogue(
				11286,
				"",
				"You set to work, trying to attach the ancient draconic visage to your anti-dragonbreath shield. It's not easy to work with the"
						+ " ancient artefact and it takes all of your skill as a master smith.");
		player.lock(2);
		player.setNextAnimation(new Animation(22143));
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("How would you like to forge the shield?",
					"Forge towards Melee.", "Forge towards Magic.",
					"Forge towards Ranged.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				sendItemDialogue(
						11284,
						"",
						"Even for an expert armourer it is not an easy task, but eventually it is ready. You have crafted the draconic visage and anti-dragonbreath shield into a dragonfire shield.");
				player.setNextAnimation(new Animation(22143));
				player.getInventory().deleteItem(11286, 1);
				player.getInventory().deleteItem(1540, 1);
				player.getInventory().addItem(11284, 1);
				player.getSkills().addXp(Skills.SMITHING, 2000);
				stage = 5;
				break;
			case OPTION_2:
				sendItemDialogue(
						25559,
						"",
						"Even for an expert armourer it is not an easy task, but eventually it is ready. You have crafted the draconic visage and anti-dragonbreath shield into a dragonfire shield.");
				player.setNextAnimation(new Animation(22143));
				player.getInventory().deleteItem(11286, 1);
				player.getInventory().deleteItem(1540, 1);
				player.getInventory().addItem(25559, 1);
				player.getSkills().addXp(Skills.SMITHING, 2000);
				stage = 5;
				break;
			case OPTION_3:
				sendItemDialogue(
						25562,
						"",
						"Even for an expert armourer it is not an easy task, but eventually it is ready. You have crafted the draconic visage and anti-dragonbreath shield into a dragonfire shield.");
				player.setNextAnimation(new Animation(22143));
				player.getInventory().deleteItem(11286, 1);
				player.getInventory().deleteItem(1540, 1);
				player.getInventory().addItem(25562, 1);
				player.getSkills().addXp(Skills.SMITHING, 2000);
				stage = 5;
				break;
			}
			break;
		case 5:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
