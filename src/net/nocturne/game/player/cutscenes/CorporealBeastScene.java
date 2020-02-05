package net.nocturne.game.player.cutscenes;

import java.util.ArrayList;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.actions.CutsceneAction;
import net.nocturne.game.player.cutscenes.actions.DialogueAction;
import net.nocturne.game.player.cutscenes.actions.LookCameraAction;
import net.nocturne.game.player.cutscenes.actions.PosCameraAction;

class CorporealBeastScene extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public boolean allowSkipCutscene() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<>();
		actionsList.add(new LookCameraAction(2993, 4378, 1000, -1));
		actionsList.add(new PosCameraAction(2984, 4383, 5000, -1));
		actionsList.add(new DialogueAction("You peek through the door."));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

}
