package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.player.actions.skills.crafting.PotteryOven;
import net.nocturne.game.player.actions.skills.crafting.PotteryOven.FinishPot;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class PotteryOvenD extends Dialogue {

	@Override
	public void start() {
		FinishPot pot = (FinishPot) parameters[0];
		SkillsDialogue.sendSkillDialogueByProduce(player, pot.getProducedPot()
				.getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		FinishPot pot = FinishPot.getPotByProduce(result.getProduce());
		if (pot == null)
			return;
		player.getActionManager().setAction(
				new PotteryOven(pot, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
