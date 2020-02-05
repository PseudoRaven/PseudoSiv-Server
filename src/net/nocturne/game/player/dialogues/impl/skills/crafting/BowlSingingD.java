package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.actions.skills.crafting.BowlSinging;
import net.nocturne.game.player.actions.skills.crafting.BowlSinging.CreateCrystal;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class BowlSingingD extends Dialogue {

	private WorldObject object;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		CreateCrystal crystal = (CreateCrystal) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, crystal
				.getProducedBar().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CreateCrystal crystal = CreateCrystal.getBarByProduce(result
				.getProduce());
		if (crystal == null)
			return;
		player.getActionManager().setAction(
				new BowlSinging(crystal, object, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}
