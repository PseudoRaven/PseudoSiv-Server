package net.nocturne.game.npc.dagannoth;

import java.util.ArrayList;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;

@SuppressWarnings("serial")
public class DagannothFledgeling extends NPC {

	public DagannothFledgeling(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> targets = getPossibleTargets(true, false);
		ArrayList<Entity> targetsCleaned = new ArrayList<>();
		for (Entity t : targets) {
			if (!(t instanceof NPC) || ((NPC) t).getId() != 2879)
				continue;
			targetsCleaned.add(t);
		}
		return targetsCleaned;
	}
}
