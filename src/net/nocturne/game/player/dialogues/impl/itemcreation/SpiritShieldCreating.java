package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class SpiritShieldCreating extends Dialogue {

	Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		if (!player.getInventory().containsItem(13736, 1)) {
			sendDialogue("You need a blessed spirit shield in order to complete this ancient shield.");
			stage = 10;
			return;
		}

		if (player.getSkills().getLevel(Skills.PRAYER) < 90) {
			sendDialogue("You need a level of 90 Prayer in order to complete this shield.");
			stage = 10;
			return;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < 85) {
			sendDialogue("You need a level of 85 Smithing in order to complete this shield.");
			stage = 10;
			return;
		}
		sendDialogue("Are you sure you want to attach the "
				+ item.getName().toLowerCase()
				+ " to the blessed spirit shield?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Select an Option", "Yes.", "No,");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				sendItemDialogue(item.getId() - 8, "",
						"You successfully attach the "
								+ item.getName().toLowerCase()
								+ " to the blessed spirit shield.");
				player.getInventory().deleteItem(13736, 1);
				player.getInventory().deleteItem(item.getId(), 1);
				player.getInventory().addItem(item.getId() - 8, 1);
				player.setNextAnimation(new Animation(898));
				stage = 10;
				break;
			case OPTION_2:
				stage = 10;
				break;
			}
			break;
		case 10:
			end();
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}