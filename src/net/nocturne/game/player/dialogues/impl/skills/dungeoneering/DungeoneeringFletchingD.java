package net.nocturne.game.player.dialogues.impl.skills.dungeoneering;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringFletching;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringFletching.DGFletch;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class DungeoneeringFletchingD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		DGFletch fletch = (DGFletch) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, fletch
				.getProducedBow().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		DGFletch fletch = DGFletch.getBarByProduce(result.getProduce());
		if (fletch == null)
			return;
		player.getActionManager().setAction(
				new DungeoneeringFletching(fletch, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}