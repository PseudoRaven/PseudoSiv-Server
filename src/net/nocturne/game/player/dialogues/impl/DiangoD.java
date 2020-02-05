package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class DiangoD extends Dialogue {
	private int npcId = 970;

	@Override
	public void start() {
		sendNPCDialogue(npcId, NORMAL, "Howdy there, partner.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		switch (stage) {
		case 1:
			sendPlayerDialogue(NORMAL, "Hi, Diango.");
			stage = 2;
			break;
		case 2:
			sendOptionsDialogue("select an option",
					"The expert skillcape shard bag.", "Nevermind");
			stage = 3;
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				sendNPCDialogue(
						npcId,
						NORMAL,
						"The expert skillcape shard bag is used to store the skill shards you receive from skilling after you reach a level 99 in that specific skill.");
				stage = 4;
				break;
			}
			break;
		case 4:
			if (!player.getInventory().containsItem(33262, 1)
					&& !player.getBank().containsItem(33262)) {
				sendNPCDialogue(npcId, NORMAL,
						"It seems that you don't have one yet. Would you like one?");
				stage = 5;
			} else {
				sendNPCDialogue(npcId, NORMAL,
						"It seems that you already have an expert skillcape shard bag.");
				stage = 7;
			}
			break;
		case 5:
			sendOptionsDialogue("Would you like one?", "Yes.", "No.");
			stage = 6;
			break;
		case 6:
			switch (componentId) {
			case OPTION_1:
				sendItemDialogue(33262,
						"You received an expert skillcape shard bag from Diango.");
				player.getInventory().addItem(33262, 1);
				stage = 7;
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		case 7:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
