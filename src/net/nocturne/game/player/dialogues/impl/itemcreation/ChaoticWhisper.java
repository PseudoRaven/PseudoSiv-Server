package net.nocturne.game.player.dialogues.impl.itemcreation;

import net.nocturne.game.player.dialogues.Dialogue;

public class ChaoticWhisper extends Dialogue {

	@Override
	public void start() {
		sendItemDialogues(25028, "",
				"Creating a chaotic necklace will permanently remove the Saradomin's whisper component. Are you sure you wish to proceed?");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId){
		switch (stage) {
		case 1:
			sendOptionsDialogue("Warning: Use Saradomin's whisper?", "Yes.", "No.");
			stage = 2;
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				player.getInventory().deleteItem(25028, 1);
				player.getInventory().deleteItem(31449, 1);
				player.getInventory().addItem(31448, 1);
				player.getPackets().sendGameMessage("You have successfully created a Brawler's knockout necklace.");
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
