package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class ForumThreadId extends Dialogue {

	@Override
	public void start() {
		player.getInterfaceManager().sendDialogueInterface(1100);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();

	}

	@Override
	public void finish() {

	}

}
