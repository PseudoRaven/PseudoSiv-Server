package net.nocturne.game.player.dialogues.impl.skills;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.actions.skills.smithing.Smithing;
import net.nocturne.game.player.actions.skills.smithing.Smithing.ForgingBar;
import net.nocturne.game.player.content.SkillsDialogue;
import net.nocturne.game.player.content.SkillsDialogue.SkillDialogueResult;
import net.nocturne.game.player.dialogues.Dialogue;

public class SmithingD extends Dialogue {

	private WorldObject object;

	public int type;
	public boolean dungeoneering;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		ForgingBar bar = (ForgingBar) parameters[1];
		SkillsDialogue.sendSkillDialogueByProduce(player, bar.getProducedItem()
				.getId());
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
		if (componentId == 1)
			return;
		SkillDialogueResult result = SkillsDialogue.getResult(player);
		ForgingBar bar = ForgingBar.getBarByProduce(result.getProduce());
		if (bar == null)
			return;
		player.getActionManager().setAction(
				new Smithing(bar, object, result.getQuantity()));
	}

	@Override
	public void finish() {
	}

	public WorldObject getObject() {
		return object;
	}

	public void setObject(WorldObject object) {
		this.object = object;
	}
}