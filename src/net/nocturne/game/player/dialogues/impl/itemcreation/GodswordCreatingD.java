package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.Animation;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class GodswordCreatingD extends Dialogue {

	@Override
	public void start() {
		if (!player.getInventory().containsItem(11710, 1)
				&& !player.getInventory().containsItem(11712, 1)
				&& !player.getInventory().containsItem(11714, 1)) {
			sendDialogue("You need all the shards in order to fix this ancient sword.");
			stage = 2;
			return;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < 80) {
			sendDialogue("You need a level of 80 Smithing in order to fix this ancient sword.");
			stage = 2;
			return;
		}
		sendDialogue("You set to work, trying to fix the ancient sword.");
		player.setNextAnimation(new Animation(22143));
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendItemDialogue(
					11690,
					"",
					"Even for an experienced smith it is not an easy task, but eventually it is done.");
			player.setNextAnimation(new Animation(22143));
			player.getInventory().deleteItem(11710, 1);
			player.getInventory().deleteItem(11712, 1);
			player.getInventory().deleteItem(11714, 1);
			player.getInventory().addItem(11690, 1);
			player.getSkills().addXp(Skills.SMITHING, 200);
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
