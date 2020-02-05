package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChaoticMurmur extends Dialogue {

	@Override
	public void start() {
		sendItemDialogues(
				25034,
				"",
				"Creating a chaotic necklace will permanently remove the Saradomin's murmur component. Are you sure you wish to proceed?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Warning: Use Saradomin's murmur?", "Yes.",
					"No.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getInventory().deleteItem(25034, 1);
				player.getInventory().deleteItem(31449, 1);
				player.getInventory().addItem(31445, 1);
				player.getPackets()
						.sendGameMessage(
								"You have successfully created a Farsight sniper necklace.");
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
