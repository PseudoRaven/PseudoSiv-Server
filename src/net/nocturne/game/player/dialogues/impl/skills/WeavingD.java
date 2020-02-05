package net.nocturne.game.player.dialogues.impl.skills;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.divination.WeavingEnergy;
import net.nocturne.game.player.actions.skills.divination.WeavingEnergy.Energy;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class WeavingD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		Energy energy = (Energy) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, energy
				.getProduceEnergy().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		Energy energy = Energy.getEnergyProduce(result.getProduce());
		if (energy == null)
			return;
		player.getActionManager().setAction(
				new WeavingEnergy(energy, item, result.getQuantity()));
	}

	@Override
	public void finish() {

	}
}