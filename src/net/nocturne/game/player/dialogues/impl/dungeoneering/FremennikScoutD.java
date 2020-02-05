package net.nocturne.game.player.dialogues.impl.dungeoneering;

import net.nocturne.game.player.actions.skills.dungeoneering.rooms.PuzzleRoom;
import net.nocturne.game.player.actions.skills.dungeoneering.rooms.puzzles.FremennikCampRoom;
import net.nocturne.game.player.dialogues.Dialogue;

public class FremennikScoutD extends Dialogue {

	@Override
	public void start() {
		PuzzleRoom room = (PuzzleRoom) parameters[0];
		if (room.isComplete()) {
			sendNPCDialogue(
					FremennikCampRoom.FREMENNIK_SCOUT,
					NORMAL,
					"Wonderful! That was the last of them. As promised, I'll unlock the door for you.");
			stage = 100;
		} else {
			sendNPCDialogue(FremennikCampRoom.FREMENNIK_SCOUT, NORMAL,
					"Need some tools?");
			stage = 1;
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 1) {
			if (!player.getInventory().containsItemToolBelt(17754)) {
				player.getInventory().addItem(17754, 1);
			}
			stage = 100;
		}
		if (stage == 100) {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
