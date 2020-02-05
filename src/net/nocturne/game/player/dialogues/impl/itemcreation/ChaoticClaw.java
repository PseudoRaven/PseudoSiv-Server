package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChaoticClaw extends Dialogue {

	@Override
	public void start() {
		if (!player.getInventory().containsItem(27068, 10)){
			sendDialogue("You need atleast 10 chaotic spikes.");
			stage = 3;
			return;
		}
		sendItemDialogues(27069, "",
				"Creating a chaotic claw will permanently remove the dragon claw component. Are you sure you wish to proceed?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Warning: Use the dragon claw?", "Yes.", "No.");
			stage = 2;
			break;

		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getInventory().deleteItem(14484, 1);
				player.getInventory().deleteItem(27068, 5);
				player.getInventory().addItem(27069, 1);
				player.getPackets().sendGameMessage("You have successfully created a Chaotic claw.");
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
