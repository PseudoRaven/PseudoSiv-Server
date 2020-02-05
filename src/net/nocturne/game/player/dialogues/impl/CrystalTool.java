package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.item.actions.SmithingCombinations;
import net.nocturne.game.item.actions.SmithingCombinations.Combos;
import net.nocturne.game.player.dialogues.Dialogue;

public class CrystalTool extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == 0) {
			switch(componentId)
			{
				case OPTION_1:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_CHIME);
					end();
				case OPTION_2:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_SAW);
					end();
				case OPTION_3:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_CHISEL);
					end();
				case OPTION_4:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_HAMMER);
					end();
				case OPTION_5:
					stage = 1;
					sendOptionsDialogue("Choose an Option",
							"Crystal Knife (150)", "Crystal Tinderbox (150)", "None");


			}
		} else if (stage == 1) {
			switch (componentId)
			{
				case OPTION_1:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_KNIFE);
					end();
				case OPTION_2:
					SmithingCombinations.processCombo(player, Combos.CRYSTAL_TINDERBOX);
					end();
				default:
					end();


			}
		}
	}

	@Override
	public void start() {
		stage = 0;
			sendOptionsDialogue("Choose an Option",
				"Crystal Chime (150)", "Crystal Saw (150)", "Crystal Chisel (150)", "Crystal Hammer (150)", "More");
	}

}
