package net.nocturne.game.player.dialogues.impl.skills.runecrafting;

import net.nocturne.game.player.actions.skills.runecrafting.TectonicCrafting;
import net.nocturne.game.player.actions.skills.runecrafting.TectonicCrafting.CraftTectonicAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class TectonicCraftingD extends Dialogue {

	@Override
	public void start() {
		CraftTectonicAction craft = (CraftTectonicAction) parameters[0];
		SkillsDialogue.sendSkillDialogueByProduce(player, craft
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CraftTectonicAction craft = CraftTectonicAction.getBarByProduce(result
				.getProduce());
		if (craft == null)
			return;
		player.getActionManager().setAction(
				new TectonicCrafting(craft, result.getQuantity()));
	}

	@Override
	public void finish() {
	}

}
