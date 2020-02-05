package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.dialogues.Dialogue;

public class CraftingD extends Dialogue {

	@Override
	public void start() {
		SkillsDialogue.sendSkillDialogueByProduce(player, 0);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillsDialogue.getResult(player);
		end();
	}

	@Override
	public void finish() {

	}
}