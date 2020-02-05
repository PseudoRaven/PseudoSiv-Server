package net.nocturne.game.player.cutscenes;

import java.util.ArrayList;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.actions.CutsceneAction;
import net.nocturne.game.player.cutscenes.actions.InterfaceAction;
import net.nocturne.game.player.cutscenes.actions.LookCameraAction;
import net.nocturne.game.player.cutscenes.actions.PosCameraAction;

public class MasterOfFear extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return true;
	}

	@Override
	public CutsceneAction[] getActions(Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<>();
		actionsList.add(new InterfaceAction(115, 2));
		actionsList.add(new PosCameraAction((player.getX() + 5),
				(player.getY() + 3), 1500, -1));
		actionsList.add(new LookCameraAction((player.getX() - 2), (player
				.getY()), 1500, 5));
		return actionsList.toArray(new CutsceneAction[0]);
	}
}
