package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.smithing.Smelting;
import net.nocturne.game.player.actions.skills.smithing.Smelting.SmeltingBar;
import net.nocturne.game.player.content.SkillsDialogueOld;
import net.nocturne.game.player.content.SkillsDialogueOld.ItemNameFilter;
import net.nocturne.game.player.dialogues.Dialogue;

public class SingleSmithingD extends Dialogue {

	private WorldObject object;
	private SmeltingBar[] bars;

	@Override
	public void start() {
		object = (WorldObject) parameters[0];
		bars = (SmeltingBar[]) parameters[1];
		int count = 0;
		int[] ids = new int[bars.length];
		for (SmeltingBar bar : bars)
			ids[count++] = bar.getProducedBar().getId();
		SkillsDialogueOld
				.sendSkillsDialogue(
						player,
						SkillsDialogueOld.MAKE,
						"How many bars you would like to smelt?<br>Choose a number, then click the bar to begin.",
						28, ids, new ItemNameFilter() {
							int count = 0;

							@Override
							public String rename(String name) {
								SmeltingBar bar = SmeltingBar.values()[count++];
								if (player.getSkills()
										.getLevel(Skills.SMITHING) < bar
										.getLevelRequired())
									name = "<col=ff0000>" + name
											+ "<br><col=ff0000>Level "
											+ bar.getLevelRequired();
								return name;

							}
						});
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		int idx = SkillsDialogueOld.getItemSlot(componentId);
		if (idx > bars.length) {
			end();
			return;
		}
		player.getActionManager().setAction(
				new Smelting(bars[idx], object, SkillsDialogueOld
						.getQuantity(player)));
		end();
	}

	@Override
	public void finish() {
	}

}
