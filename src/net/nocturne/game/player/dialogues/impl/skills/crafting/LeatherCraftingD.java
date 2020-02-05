package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.crafting.LeatherCrafting;
import net.nocturne.game.player.actions.skills.crafting.LeatherCrafting.CraftAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class LeatherCraftingD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		CraftAction craft = (CraftAction) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, craft
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CraftAction craft = CraftAction.getBarByProduce(result.getProduce());
		if (craft == null)
			return;
		player.getActionManager().setAction(
				new LeatherCrafting(craft, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
