package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChaoticSpear extends Dialogue {

	@Override
	public void start() {
		if (!player.getInventory().containsItem(27068, 10)) {
			sendDialogue("You need atleast 10 chaotic spikes.");
			stage = 3;
			return;
		}
		sendItemDialogues(
				31463,
				"",
				"Creating a chaotic spear will permanently remove the Zamorakian spear component. Are you sure you wish to proceed?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Warning: Use the zamorakian spear?", "Yes.",
					"No.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getInventory().deleteItem(11716, 1);
				player.getInventory().deleteItem(27068, 10);
				player.getInventory().addItem(31463, 1);
				player.getPackets().sendGameMessage(
						"You have successfully created a Chaotic spear.");
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		case 3:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
