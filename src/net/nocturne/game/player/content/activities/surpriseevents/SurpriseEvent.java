package net.nocturne.game.player.content.activities.surpriseevents;

import net.nocturne.game.player.Player;

public interface SurpriseEvent {

	/**
	 * Start's event.
	 */
	void start();

	/**
	 * Trie's to join the event.
	 */
	void tryJoin(Player player);
}
