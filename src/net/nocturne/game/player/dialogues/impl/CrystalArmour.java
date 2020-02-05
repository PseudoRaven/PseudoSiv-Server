package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.SmithingCombinations.Combos;
import net.nocturne.game.player.dialogues.Dialogue;

public class CrystalArmour extends Dialogue {

	boolean crystal;

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 0) {
			if (componentId == OPTION_1) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_DEFLECTOR);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_DEFLECTOR);
			} else if (componentId == OPTION_2) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_WARD);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_WARD);
			} else if (componentId == OPTION_3) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_SHIELD);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_SHIELD);
			}
			end();
		}
	}

	@Override
	public void start() {
		crystal = (Boolean) parameters[0];
		stage = 0;
		if (crystal)
			sendOptionsDialogue("Choose an Option", "Crystal Deflector (375)",
					"Crystal Ward (375)", "Crystal Shield (375)", "None");
		else
			sendOptionsDialogue("Choose an Option", "Attuned Deflector (1000)",
					"Attuned Ward (1000)", "Attuned Shield (1000)", "None");
	}

}
