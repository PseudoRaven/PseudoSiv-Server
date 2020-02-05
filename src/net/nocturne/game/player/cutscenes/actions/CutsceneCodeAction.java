package net.nocturne.game.player.cutscenes.actions;

import net.nocturne.game.player.Player;

public class CutsceneCodeAction extends CutsceneAction {

	private Runnable runnable;

	public CutsceneCodeAction(Runnable runnable, int actionDelay) {
		super(-1, actionDelay);
		this.runnable = runnable;
	}

	@Override
	public void process(Player player, Object[] cache) {
		runnable.run();
	}

}
