//@author Nath

package net.nocturne.game.player.dialogues.impl.home;

import net.nocturne.game.player.dialogues.Dialogue;

public class ApolloDialogue extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		sendNPCDialogue(68, HAPPY, "Hello, " + player.getDisplayName() + "");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		// case -1:
		// sendNPCDialogue(npcId, NONONO_FACE, "Hello" );
		// stage = 1;
		// end();
		// break;
		case -1:
			// sendNPCDialogue(npcId, HAPPY,
			// "Hello");
			sendPlayerDialogue(HAPPY, "Hi There, got anything for me?");
			stage = 1;
			sendNPCDialogue(npcId, NONONO_FACE,
					"Nothing for you yet.. but it's cold up here!");
			break;
		}
	}

	@Override
	public void finish() {

	}
}