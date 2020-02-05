package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.npc.others.Ugi;
import net.nocturne.game.player.TreasureTrailsManager;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Utils;

public class UgiDialouge extends Dialogue {

	@Override
	public void start() {
		Ugi npc = (Ugi) parameters[0];
		stage = npc.getTarget() == player
				&& player.getTreasureTrailsManager().getPhase() == 4 ? (byte) 0
				: (byte) -1;
		run(-1, -1, -1);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		Ugi npc = (Ugi) parameters[0];
		if (stage == 0) {
			sendNPCDialogue(npc.getId(), NORMAL,
					TreasureTrailsManager.UGIS_QUOTES[Utils
							.random(TreasureTrailsManager.UGIS_QUOTES.length)]);
			stage = 1;
		} else if (stage == 1) {
			sendPlayerDialogue(NORMAL, "What?");
			stage = 2;
		} else if (stage == 2) {
			end();
			npc.finish();

			player.getTreasureTrailsManager().setPhase(5);
			player.getTreasureTrailsManager().setNextClue(1);
		} else if (stage == -1) {
			sendNPCDialogue(npc.getId(), NORMAL,
					TreasureTrailsManager.UGI_BADREQS);
			stage = -2;
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}
}
