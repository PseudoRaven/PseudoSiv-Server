package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.SmithingCombinations.Combos;
import net.nocturne.game.player.dialogues.Dialogue;

public class MalevolentArmor extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1)
			SmithingCombinations.processCombo(player, Combos.MALEVOLENT_HELM);
		else if (componentId == OPTION_2)
			SmithingCombinations.processCombo(player, Combos.MALEVOLENT_GREAVES);
		else if (componentId == OPTION_3)
			SmithingCombinations.processCombo(player, Combos.MALEVOLENT_CUIRASS);
		end();
	}

	@Override
	public void start() {
		sendOptionsDialogue("Choose an Option",
				"Malevolent helm (14)", "Malevolent greaves (28)", "Malevolent cuirass (42)", "None");
	}

}
