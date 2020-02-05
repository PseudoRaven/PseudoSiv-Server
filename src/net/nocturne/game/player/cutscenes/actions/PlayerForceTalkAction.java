package net.nocturne.game.player.cutscenes.actions;

import net.nocturne.game.ForceTalk;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.dialogues.Dialogue;

public class PlayerForceTalkAction extends CutsceneAction {

	private String text;

	public PlayerForceTalkAction(String text, int actionDelay) {
		super(-1, actionDelay);
		this.text = text;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextForceTalk(new ForceTalk(text));
		Dialogue.sendPlayerDialogueNoContinue(player, 9827, text);
	}

}
