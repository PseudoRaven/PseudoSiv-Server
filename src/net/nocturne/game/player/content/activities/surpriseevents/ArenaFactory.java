package net.nocturne.game.player.content.activities.surpriseevents;

import net.nocturne.game.player.content.activities.surpriseevents.arenaimpl.CastleArena;
import net.nocturne.game.player.content.activities.surpriseevents.arenaimpl.ElvenArena;
import net.nocturne.utils.Utils;

public class ArenaFactory {

	public static EventArena randomEventArena(boolean multi) {
		int rnd = Utils.random(2);
		switch (rnd) {
		case 0:
		default:
			return new ElvenArena(multi);
		case 1:
			return new CastleArena(multi);

		}

	}

}
