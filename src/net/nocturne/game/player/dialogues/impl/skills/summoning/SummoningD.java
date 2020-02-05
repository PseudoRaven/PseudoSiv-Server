package net.nocturne.game.player.dialogues.impl.skills.summoning;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.actions.skills.summoning.Summoning;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class SummoningD extends Dialogue {

	private WorldObject object;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		Pouch pouch = (Pouch) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player,
				pouch.getRealPouchId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		end();
		Pouch pouch = Pouch.getPouchByProduce(result.getProduce());
		if (pouch == null)
			return;
		player.getActionManager().setAction(
				new Summoning(pouch, object, result.getQuantity()));
	}

	@Override
	public void finish() {
	}
}