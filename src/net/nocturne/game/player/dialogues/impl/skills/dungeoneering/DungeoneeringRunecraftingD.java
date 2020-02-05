package net.nocturne.game.player.dialogues.impl.skills.dungeoneering;

import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringRunecrafting;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringRunecrafting.ImbueAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class DungeoneeringRunecraftingD extends Dialogue {

	@Override
	public void start() {
		ImbueAction imbue = (ImbueAction) parameters[0];
		SkillsDialogue.sendSkillDialogueByProduce(player, imbue
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		ImbueAction imbue = ImbueAction.getBarByProduce(result.getProduce());
		if (imbue == null)
			return;
		player.getActionManager().setAction(
				new DungeoneeringRunecrafting(imbue, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}