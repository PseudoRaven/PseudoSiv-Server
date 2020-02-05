package net.nocturne.game.player.events;

import net.nocturne.game.player.Player;

public abstract class Event {

	/**
	 * @author: miles M
	 */

	protected Player player;

	public final void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public abstract void start();

	public abstract void stop();

	protected final void stopEvent() {
		player.getControllerManager().removeControllerWithoutCheck();
	}
}