package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.SmithingCombinations.Combos;
import net.nocturne.game.player.dialogues.Dialogue;

public class SirenicArmor extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1)
			SmithingCombinations.processCombo(player, Combos.SIRENIC_MASK);
		else if (componentId == OPTION_2)
			SmithingCombinations.processCombo(player, Combos.SIRENIC_CHAPS);
		else if (componentId == OPTION_3)
			SmithingCombinations.processCombo(player, Combos.SIRENIC_HAUBERK);
		end();
	}

	@Override
	public void start() {
		sendOptionsDialogue("Choose an Option", "Sirenic mask (14)",
				"Sirenic chaps (28)", "Sirenic hauberk (42)", "None");
	}

}
