package net.nocturne.game.npc.glacior;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;

@SuppressWarnings("serial")
public class Glacor extends Glacyte {

	private List<Glacyte> glacites;
	private boolean rangeAttack;

	public Glacor(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(null, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		// setCapDamage(50000);

		setEffect((byte) -1);
		setGlacor(this);
		setSpawned(false);
		setDropRateFactor(2);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (glacites == null) {
			if (getHitpoints() <= getMaxHitpoints() / 2) {
				glacites = new ArrayList<>(2);
				createGlacites();
			}
		} else if (glacites.size() != 0)
			hit.setDamage(0);
		super.handleIngoingHit(hit);
	}

	private void createGlacites() {
		for (int index = 0; index < 3; index++) {
			for (int tileAttempt = 0; tileAttempt < 10; tileAttempt++) {
				WorldTile tile = new WorldTile(this, 2);
				if (World.isTileFree(0, tile.getX(), tile.getY(), 1)) {
					glacites.add(new Glacyte(this, 14302 + index, tile, -1,
							true));
					for (NPC gNpc : glacites) {
						gNpc.setForceAgressive(true);
					}
					break;
				}
			}
		}
	}

	void verifyGlaciteEffect(Glacyte glacite) {
		if (glacites.size() == 1)
			setEffect(glacites.get(0).getEffect());
		glacites.remove(glacite);
	}

	@Override
	public void sendDeath(Entity killer) {
		super.sendDeath(killer);
		glacites = null;
	}

	public boolean isRangeAttack() {
		return rangeAttack;
	}

	public void setRangeAttack(boolean rangeAttack) {
		this.rangeAttack = rangeAttack;
	}

	void resetMinions() {
		glacites = null;
		setEffect((byte) -1);
	}
}
