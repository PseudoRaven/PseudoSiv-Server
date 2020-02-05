package net.nocturne.game.player.dialogues.impl.construction;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.construction.HouseConstants;
import net.nocturne.game.player.dialogues.Dialogue;

public class WeaponsRackD extends Dialogue {

	private static final String[] RACK_NAMES = { "Red Boxing Gloves",
			"Blue Boxing Gloves", "Wooden Sword", "Wooden Shield",
			"Pugle Stick" };

	@Override
	public void start() {
		int length = (int) this.parameters[0];
		String[] NAMES = new String[length];
		for (int index = 0; index < length; index++) {
		}
		System.arraycopy(RACK_NAMES, 0, NAMES, 0, length);

		sendOptionsDialogue("Select a weapon of your choice.", NAMES);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		player.getInventory().addItem(
				new Item(HouseConstants.WEAPON_RACK[componentId == 11 ? 0
						: componentId - 12], 1));
		end();
	}

	@Override
	public void finish() {

	}
}
