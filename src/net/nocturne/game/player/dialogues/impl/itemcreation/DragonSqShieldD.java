package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.Animation;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class DragonSqShieldD extends Dialogue {

	@Override
	public void start() {
		if (player.getInventory().containsItem(2366, 1)
				&& player.getInventory().containsItem(2368, 1)
				&& player.getSkills().getLevel(Skills.SMITHING) >= 60) {
			sendItemDialogue(
					2366,
					"",
					"You set to work trying to fix the ancient shield. It's seen some heavy action and needs some serious work doing to it.");
			player.setNextAnimation(new Animation(22143));
			stage = 1;
		} else {
			sendDialogue("You don't meet the requirements to create a dragon square shield.");
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendItemDialogue(
					1187,
					"",
					"Even for an experienced armourer it is not an easy task, but eventually it is ready. You have restored the dragon square shield to its former glory.");
			player.setNextAnimation(new Animation(22143));
			player.getInventory().deleteItem(2366, 1);
			player.getInventory().deleteItem(2368, 1);
			player.getInventory().addItem(1187, 1);
			player.getSkills().addXp(Skills.SMITHING, 75);
			stage = 2;
			break;
		case 2:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}
}
