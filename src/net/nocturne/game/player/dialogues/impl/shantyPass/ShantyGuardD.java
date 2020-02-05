package net.nocturne.game.player.dialogues.impl.shantyPass;

import net.nocturne.game.player.dialogues.Dialogue;

public class ShantyGuardD extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		sendNPCDialogue(
				npcId,
				9827,
				"Go talk to Shantay. I'm on dutry and I don't have time to talk to the likes of you!");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue("The guard seems quite bad tempered, probably from having to wear heavy armour in this intense heat.");
		} else
			end();
	}

	@Override
	public void finish() {

	}

}
