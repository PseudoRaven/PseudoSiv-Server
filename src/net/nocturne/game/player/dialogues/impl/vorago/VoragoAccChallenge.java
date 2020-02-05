package net.nocturne.game.player.dialogues.impl.vorago;

import net.nocturne.game.World;
import net.nocturne.game.player.dialogues.Dialogue;

public class VoragoAccChallenge extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue(World.ChallengerName
				+ " has challenged Vorago.<br>"
				+ " Are you willing to face the test with them?", "Yes.", "No.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			switch (componentId) {
			case OPTION_1:
				player.accChallenge = true;
				player.getPackets()
						.sendGameMessage(
								"<col=ee7600>You've joined </col>"
										+ "<col=ee7600>"
										+ World.ChallengerName
										+ "</col>"
										+ "<col=ee7600>.</col>"
										+ "<col=ee7600> Vorago is charging a massive attack.</col>");
				end();
				break;
			case OPTION_2:
				player.accChallenge = false;
				end();
				break;
			}
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