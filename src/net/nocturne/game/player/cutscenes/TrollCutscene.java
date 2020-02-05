package net.nocturne.game.player.cutscenes;

import java.util.ArrayList;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.actions.*;

public class TrollCutscene extends Cutscene {

	@Override
	public boolean hiddenMinimap() {
		return false;
	}

	@Override
	public CutsceneAction[] getActions(final Player player) {
		ArrayList<CutsceneAction> actionsList = new ArrayList<>();

		actionsList.add(new CutsceneCodeAction(() -> {
		}, 6));
		actionsList.add(new CutsceneCodeAction(() -> {
			player.getDialogueManager().finishDialogue();
			// Dialogue.sendEmptyDialogue(player);
			}, -1));
		int DRAGONKK = 1;
		actionsList.add(new CreateNPCAction(DRAGONKK, 15784, 3092, 3497, 0,
				"Vision of dragonkk", 1337, -1));
		int APACHE = 2;
		actionsList.add(new CreateNPCAction(APACHE, 15783, 3093, 3497, 0,
				"Apache ah64", -1, -1));
		actionsList.add(new NPCFaceTileAction(DRAGONKK, 3903, 3497, -1));
		actionsList.add(new NPCFaceTileAction(APACHE, 3902, 3497, -1));

		actionsList.add(new NPCForceTalkAction(DRAGONKK,
				"It's time for you to die apache!", 4));
		actionsList.add(new NPCForceTalkAction(APACHE,
				"Noooooooooo, please, i will kiss your ass all day!", 4));
		actionsList.add(new NPCForceTalkAction(DRAGONKK, "Fucking die!", 4));
		actionsList
				.add(new NPCAnimationAction(APACHE, new Animation(17542), -1));
		actionsList.add(new NPCGraphicAction(APACHE, new Graphics(3402), 13));
		actionsList.add(new DestroyCachedObjectAction(APACHE, -1));
		actionsList.add(new NPCFaceTileAction(DRAGONKK, player.getX(), player
				.getY(), -1));
		actionsList.add(new NPCForceTalkAction(DRAGONKK, "And you, "
				+ player.getDisplayName() + ", stop looking at me!", 4));
		return actionsList.toArray(new CutsceneAction[actionsList.size()]);
	}

	@Override
	public void stopCutscene(Player player) {
		super.stopCutscene(player);
	}

}
