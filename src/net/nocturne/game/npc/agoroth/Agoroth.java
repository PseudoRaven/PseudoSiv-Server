package net.nocturne.game.npc.agoroth;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;

@SuppressWarnings("serial")
public class Agoroth extends NPC {

	private boolean spawnedTentacles = false;

	public Agoroth(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setForceAgressive(true);
		setForceMultiArea(true);
		setForceTargetDistance(15);
		setCantFollowUnderCombat(true);
	}

	@Override
	public void sendDeath(Entity source) {
		spawnedTentacles = false;
		super.sendDeath(source);
	}

	@Override
	public void processNPC() {
		cancelFaceEntityNoCheck();
		super.processNPC();
	}

	@Override
	public boolean addWalkSteps(final int destX, final int destY,
			int maxStepsCount, boolean check) {
		return false;
	}

	public boolean canSpawnTentacles() {
		return !spawnedTentacles && getHitpoints() < (getMaxHitpoints() * 0.5);
	}

	public void spawnTentacles() {
		spawnedTentacles = true;
		new AgorothTentacle(19326, new WorldTile(this.getX() - 3,
				this.getY() - 1, 0), 23361, this);
		new AgorothTentacle(19327, new WorldTile(this.getX() - 3,
				this.getY() - 2, 0), 23373, this);
		new AgorothTentacle(19330, new WorldTile(this.getX() - 3,
				this.getY() - 4, 0), 23366, this);
		new AgorothTentacle(19328, new WorldTile(this.getX() + 3,
				this.getY() - 1, 0), 23378, this);
		new AgorothTentacle(19329, new WorldTile(this.getX() + 3,
				this.getY() - 2, 0), 23373, this);
		new AgorothTentacle(19331, new WorldTile(this.getX() + 3,
				this.getY() - 4, 0), 23378, this);
	}

}