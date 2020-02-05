package net.nocturne.game.player.cutscenes;

import java.util.ArrayList;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.actions.CutsceneAction;

public class HefinWallRun extends Cutscene {

	/**
	 * @author: miles M
	 */

	@Override
	public boolean hiddenMinimap() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<>();
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}
}