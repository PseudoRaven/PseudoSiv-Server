package net.nocturne.game.npc.dragons;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.TimersManager.RecordKey;

@SuppressWarnings("serial")
public class KingBlackDragon extends NPC {

	public KingBlackDragon(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setDropRateFactor(2);
		setIntelligentRouteFinder(true);
	}

	@Override
	public void sendDeath(final Entity source) {
		if (source instanceof Player)
			((Player) source).getTimersManager().removeTimer(
					RecordKey.KING_BLACK_DRAGON);
		increaseKills(RecordKey.KING_BLACK_DRAGON, false);
		super.sendDeath(source);
	}

}
