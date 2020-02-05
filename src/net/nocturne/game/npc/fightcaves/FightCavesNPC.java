package net.nocturne.game.npc.fightcaves;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
public class FightCavesNPC extends NPC {

	public FightCavesNPC(int id, WorldTile tile) {
		super(id, tile, -1, true, true);
		setForceAgressive(true);
		setNoDistanceCheck(true);
	}

	@Override
	public void sendDeath(Entity source) {
		setNextGraphics(new Graphics(2924 + getSize()));
		super.sendDeath(source);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		List<Integer> playerIndexes = World.getRegion(getRegionId())
				.getPlayerIndexes();
		if (playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = World.getPlayers().get(npcIndex);
				if (player == null || player.getAppearence().isHidden()
						|| player.isDead() || player.hasFinished()
						|| !player.isRunning())
					continue;
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}
}
