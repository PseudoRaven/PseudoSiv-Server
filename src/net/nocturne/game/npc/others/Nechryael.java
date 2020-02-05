package net.nocturne.game.npc.others;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;

@SuppressWarnings("serial")
public class Nechryael extends NPC {

	public Nechryael(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processNPC() {
		super.processNPC();
	}

}