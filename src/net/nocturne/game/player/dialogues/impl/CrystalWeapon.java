package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.SmithingCombinations.Combos;
import net.nocturne.game.player.dialogues.Dialogue;

public class CrystalWeapon extends Dialogue {

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
							Combos.CRYSTAL_BOW);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_BOW);
				end();
			} else if (componentId == OPTION_2) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_HALBERD);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_HALBERD);
				end();
			} else if (componentId == OPTION_3) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_STAFF);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_STAFF);
				end();
			} else if (componentId == OPTION_4) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_CHAKRAM);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_CHAKRAM);
				end();
			} else if (componentId == OPTION_5) {
				stage = 1;
				if (crystal)
					sendOptionsDialogue("Choose an Option",
							"Crystal Off-hand Chakram (375)",
							"Crystal Dagger (375)",
							"Crystal Off-hand Dagger (375)",
							"Crystal Wand (375)", "Crystal Orb (375)");
				else
					sendOptionsDialogue("Choose an Option",
							"Atuned Off-hand Chakram (1000)",
							"Atuned Dagger (1000)",
							"Atuned Off-hand Dagger (1000)",
							"Atuned Wand (1000)", "Atuned Orb (1000)");
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_OFF_CHAKRAM);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_OFF_CHAKRAM);
				end();
			} else if (componentId == OPTION_2) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_DAGGER);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_DAGGER);
				end();
			} else if (componentId == OPTION_3) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_OFF_DAGGER);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_OFF_DAGGER);
				end();
			} else if (componentId == OPTION_4) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_WAND);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_WAND);
				end();
			} else if (componentId == OPTION_5) {
				if (crystal)
					SmithingCombinations.processCombo(player,
							Combos.CRYSTAL_ORB);
				else
					SmithingCombinations.processCombo(player,
							Combos.ATTUNED_ORB);
				end();
			}
		}
	}

	@Override
	public void start() {
		crystal = (Boolean) parameters[0];
		stage = 0;
		if (crystal)
			sendOptionsDialogue("Choose an Option", "Crystal Bow (750)",
					"Crystal Halberd (750)", "Crystal Staff (750)",
					"Crystal Chakram (375)", "More");
		else
			sendOptionsDialogue("Choose an Option", "Atuned Bow (2000)",
					"Atuned Halberd (2000)", "Atuned Staff (2000)",
					"Atuned Chakram (1000)", "More");
	}

}