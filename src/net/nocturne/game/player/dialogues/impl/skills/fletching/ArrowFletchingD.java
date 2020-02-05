package net.nocturne.game.player.dialogues.impl.skills.fletching;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.fletching.ArrowFletching;
import net.nocturne.game.player.actions.skills.fletching.ArrowFletching.FletchArrowAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class ArrowFletchingD extends Dialogue {
	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		FletchArrowAction fletch = (FletchArrowAction) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, fletch
				.getProducedBow().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		FletchArrowAction fletch = FletchArrowAction.getBarByProduce(result
				.getProduce());
		if (fletch == null)
			return;
		player.getActionManager().setAction(
				new ArrowFletching(fletch, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
