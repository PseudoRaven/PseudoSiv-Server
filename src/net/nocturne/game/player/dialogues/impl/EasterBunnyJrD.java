package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.QuestManager.Quests;
import net.nocturne.game.player.dialogues.Dialogue;

public class EasterBunnyJrD extends Dialogue {

	@Override
	public void start() {
		if (player.getQuestManager().getQuestStage(Quests.EASTER_2014) == 0) {
			stage = 1;
		}
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 1) {

		}
	}

	@Override
	public void finish() {

	}
}
