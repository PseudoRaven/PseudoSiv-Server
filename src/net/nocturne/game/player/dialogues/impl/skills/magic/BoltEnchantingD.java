package net.nocturne.game.player.dialogues.impl.skills.magic;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.magic.BoltEnchanting;
import net.nocturne.game.player.actions.skills.magic.BoltEnchanting.EnchantAction;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class BoltEnchantingD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		EnchantAction enchant = (EnchantAction) parameters[0];
		SkillsDialogue.sendSkillDialogueByProduce(player, enchant
				.getProducedBow().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		EnchantAction enchant = EnchantAction.getBarByProduce(result
				.getProduce());
		if (enchant == null)
			return;
		player.getActionManager().setAction(
				new BoltEnchanting(enchant, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}