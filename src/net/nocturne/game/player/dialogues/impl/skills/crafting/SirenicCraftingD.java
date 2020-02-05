package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.player.actions.skills.crafting.SirenicCrafting;
import net.nocturne.game.player.actions.skills.crafting.SirenicCrafting.CraftSirenicAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class SirenicCraftingD extends Dialogue {

	@Override
	public void start() {
		CraftSirenicAction craft = (CraftSirenicAction) parameters[0];
		SkillsDialogue.sendSkillDialogueByProduce(player, craft
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CraftSirenicAction craft = CraftSirenicAction.getBarByProduce(result
				.getProduce());
		if (craft == null)
			return;
		player.getActionManager().setAction(
				new SirenicCrafting(craft, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
