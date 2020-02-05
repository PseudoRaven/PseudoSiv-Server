package net.nocturne.game.player.dialogues.impl.skills.fletching;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.fletching.Fletching;
import net.nocturne.game.player.actions.skills.fletching.Fletching.FletchAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class FletchingD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		FletchAction fletch = (FletchAction) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, fletch
				.getProducedBow().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		FletchAction fletch = FletchAction.getBarByProduce(result.getProduce());
		if (fletch == null)
			return;
		player.getActionManager().setAction(
				new Fletching(fletch, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
