package net.nocturne.game.player.dialogues.impl.clans;

import net.nocturne.game.player.dialogues.Dialogue;

public class ClanMotto extends Dialogue {

	@Override
	public void start() {
		player.getInterfaceManager().sendDialogueInterface(1103);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();

	}

	@Override
	public void finish() {

	}

}
