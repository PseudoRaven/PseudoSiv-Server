package net.nocturne.game.player.dialogues.impl.skills.fletching;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.fletching.BoltFletching;
import net.nocturne.game.player.actions.skills.fletching.BoltFletching.FletchBoltAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class BoltFletchingD extends Dialogue {
	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		FletchBoltAction fletch = (FletchBoltAction) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, fletch
				.getProducedBow().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		FletchBoltAction fletch = FletchBoltAction.getBarByProduce(result
				.getProduce());
		if (fletch == null)
			return;
		player.getActionManager().setAction(
				new BoltFletching(fletch, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
