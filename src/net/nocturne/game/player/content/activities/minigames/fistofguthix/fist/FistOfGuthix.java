package net.nocturne.game.player.content.activities.minigames.fistofguthix.fist;

import java.util.LinkedList;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;

public class FistOfGuthix {

	public static LinkedList<Player> LOBBY_PLAYERS = new LinkedList<Player>();

	public static LinkedList<Player> lobbyPlayers() {
		return LOBBY_PLAYERS;
	}

	public static void lobbyPlayers(LinkedList<Player> lOBBY_PLAYERS) {
		LOBBY_PLAYERS = lOBBY_PLAYERS;
	}

	public static void enter(Player player) {
		lobbyPlayers().add(player);
		player.getControllerManager().startController("FistOfGuthixController",
				false);
		player.setNextWorldTile(new WorldTile(1642, 5600, 0));
	}
}