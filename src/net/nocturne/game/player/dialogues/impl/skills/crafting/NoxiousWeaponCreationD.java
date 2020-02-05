package net.nocturne.game.player.dialogues.impl.skills.crafting;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.actions.skills.crafting.NoxiousWeaponCreation;
import net.nocturne.game.player.actions.skills.crafting.NoxiousWeaponCreation.CreateWeapon;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class NoxiousWeaponCreationD extends Dialogue {

	private Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		CreateWeapon weapon = (CreateWeapon) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, weapon
				.getProducedWeapon().getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		CreateWeapon weapon = CreateWeapon.getWeaponByProduce(result
				.getProduce());
		if (weapon == null)
			return;
		player.getActionManager().setAction(
				new NoxiousWeaponCreation(weapon, item, result.getQuantity()));
	}

	@Override
	public void finish() {
	}

}
