package net.nocturne.game.player.cutscenes.actions;

import net.nocturne.game.player.Player;

public class PlayerMusicEffectAction extends CutsceneAction {

	private int id;

	public PlayerMusicEffectAction(int id, int actionDelay) {
		super(-1, actionDelay);
		this.id = id;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.getMusicsManager().playMusicEffect(id);
	}

}
