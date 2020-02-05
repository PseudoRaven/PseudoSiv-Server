package net.nocturne.game.player.dialogues.impl.construction;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.dialogues.Dialogue;

public class RemoveBuildD extends Dialogue {

	WorldObject object;

	@Override
	public void start() {
		this.object = (WorldObject) parameters[0];
		sendOptionsDialogue("Really remove it?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			player.getHouse().removeBuild(object);
		}
		end();
	}

	@Override
	public void finish() {

	}

}
