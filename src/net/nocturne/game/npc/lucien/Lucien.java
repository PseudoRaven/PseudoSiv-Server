package net.nocturne.game.npc.lucien;

import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Lucien extends NPC {

	final WorldTile iceTile = Utils.getRandomTile(2200, 4261, 2208, 4268, 1);
	private EnhancedIceTitan iceTitan;

	public Lucien(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, -1, true, false);
		setLureDelay(0);
		setCapDamage(3000);
		setCombatLevel(59999);
		setName("Lucien");
		setRun(true);
		this.setCanBeAttackFromOutOfArea(true);
		setForceMultiArea(true);
	}

	public void processNPC() {
		super.processNPC();
		if (isDead()) {
			removeEnhancedTitan();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (iceTitan != null)
			iceTitan.sendDeath(source);
	}

	public void spawnEnhancedIceTitan() {
		if (iceTitan != null)
			return;
		iceTitan = new EnhancedIceTitan(iceTile, this);
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.7;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.7;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.7;
	}

	public void removeEnhancedTitan() {
		if (iceTitan == null)
			return;
		iceTitan.finish();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		super.handleIngoingHit(hit);
		if (hit.getSource() != null) {
			int recoil = (int) (hit.getDamage() * 0.4);
			if (recoil > 0)
				hit.getSource().applyHit(
						new Hit(this, recoil, HitLook.REFLECTED_DAMAGE));
		}
	}

}
