package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class TelescopeD extends Dialogue {

	@Override
	public void start() {
		player.getInterfaceManager().sendCentralInterface(782);
		String[] messages = new String[parameters.length];
		for (int i = 0; i < messages.length; i++)
			messages[i] = (String) parameters[i];
		sendDialogue(messages);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		player.getInterfaceManager().removeCentralInterface();
		end();
	}

	@Override
	public void finish() {

	}
}