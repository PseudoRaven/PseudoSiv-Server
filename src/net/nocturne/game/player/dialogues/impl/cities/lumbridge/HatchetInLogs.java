package net.nocturne.game.player.dialogues.impl.cities.lumbridge;

import net.nocturne.game.player.dialogues.Dialogue;

public class HatchetInLogs extends Dialogue {

	@Override
	public void start() {
		sendHandedItem(1351, "You take the hatchet from the stump.");
		player.getInventory().addItem(1351, 1);
		stage = -1;

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;

		default:
			end();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
