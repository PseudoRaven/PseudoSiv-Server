package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class SimpleNPCMessage extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		String[] messages = new String[parameters.length - 1];
		for (int i = 0; i < messages.length; i++)
			messages[i] = (String) parameters[i + 1];
		sendNPCDialogue(npcId, NORMAL, messages);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
	}

	@Override
	public void finish() {

	}
}