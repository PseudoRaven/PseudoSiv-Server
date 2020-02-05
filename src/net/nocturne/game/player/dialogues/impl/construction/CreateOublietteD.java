package net.nocturne.game.player.dialogues.impl.construction;

import net.nocturne.game.player.actions.skills.construction.HouseConstants;
import net.nocturne.game.player.actions.skills.construction.House.RoomReference;
import net.nocturne.game.player.actions.skills.construction.HouseConstants.Room;
import net.nocturne.game.player.dialogues.Dialogue;

public class CreateOublietteD extends Dialogue {

	private RoomReference room;

	@Override
	public void start() {
		this.room = (RoomReference) parameters[0];
		sendOptionsDialogue(
				"This trapdoor does not lead anywhere. Do you want to build a oubliette at the bottom?",
				"Yes.", "No.");

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			int slot = room.getTrapdoorSlot();
			if (slot != -1) {
				RoomReference newRoom = new RoomReference(Room.OUTBLIETTE,
						room.getX(), room.getY(), 0, room.getRotation());
				newRoom.addObject(HouseConstants.Builds.LADDER, slot); // TODO
				player.getHouse().createRoom(newRoom);
			}
		}
		end();
	}

	@Override
	public void finish() {

	}

}
