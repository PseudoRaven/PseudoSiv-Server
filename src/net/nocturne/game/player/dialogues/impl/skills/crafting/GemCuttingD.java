package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.crafting.GemCutting;
import net.nocturne.game.player.actions.skills.crafting.GemCutting.CraftGemAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class GemCuttingD extends Dialogue {
	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		CraftGemAction craft = (CraftGemAction) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, craft
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CraftGemAction craft = CraftGemAction.getBarByProduce(result
				.getProduce());
		if (craft == null)
			return;
		player.getActionManager().setAction(
				new GemCutting(craft, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
