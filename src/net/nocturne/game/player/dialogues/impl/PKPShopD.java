package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

public class PKPShopD extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Select a PKP Shop", "First Shop", "Second Shop");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			ShopsHandler.openShop(player, 500);
		} else if (componentId == OPTION_2) {
			ShopsHandler.openShop(player, 501);
		}
		end();
	}

	@Override
	public void finish() {

	}
}
