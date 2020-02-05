package net.nocturne.game.player.dialogues.impl.skills.dungeoneering;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringMagicCrafting;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringMagicCrafting.CraftMageDung;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class DungeoneeringMagicCraftingD extends Dialogue {
	private Item item;

	@Override
	public void start() {

		item = (Item) parameters[0];
		CraftMageDung cape = (CraftMageDung) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, cape.getProducedBar()
				.getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CraftMageDung cape = CraftMageDung.getBarByProduce(result.getProduce());
		if (cape == null)
			return;
		player.getActionManager()
				.setAction(
						new DungeoneeringMagicCrafting(cape, item, result
								.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
