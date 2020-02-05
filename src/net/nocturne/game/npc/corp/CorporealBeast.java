package net.nocturne.game.npc.corp;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.TimersManager.RecordKey;

@SuppressWarnings("serial")
public class CorporealBeast extends NPC {

	private DarkEnergyCore core;

	public CorporealBeast(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(3000);
		setIntelligentRouteFinder(true);
	}

	public void spawnDarkEnergyCore() {
		if (core != null)
			return;
		core = new DarkEnergyCore(this);
	}

	void removeDarkEnergyCore() {
		if (core == null)
			return;
		core.finish();
		core = null;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		checkReset();
	}

	private void checkReset() {
		int maxhp = getMaxHitpoints();
		if (maxhp > getHitpoints() && !isUnderCombat()
				&& getPossibleTargets().isEmpty())
			setHitpoints(maxhp);
	}

	@Override
	public void sendDeath(Entity source) {
		if (source instanceof Player)
			((Player) source).getTimersManager().removeTimer(
					RecordKey.CORPOREAL_BEAST);
		increaseKills(RecordKey.CORPOREAL_BEAST, false);
		super.sendDeath(source);
		if (core != null)
			core.sendDeath(source);
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		reduceHit(hit);
		super.handleIngoingHit(hit);
	}

	private void reduceHit(Hit hit) {
		if (!(hit.getSource() instanceof Player)
				|| (hit.getLook() != HitLook.MELEE_DAMAGE
						&& hit.getLook() != HitLook.RANGE_DAMAGE && hit
						.getLook() != HitLook.MAGIC_DAMAGE))
			return;
		Player from = (Player) hit.getSource();
		int weaponId = from.getEquipment().getWeaponId();
		String name = weaponId == -1 ? "null" : ItemDefinitions
				.getItemDefinitions(weaponId).getName().toLowerCase();
		if (hit.getLook() != HitLook.MELEE_DAMAGE || !name.contains("spear"))
			hit.setDamage(hit.getDamage() / 2);

	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.8;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.8;
	}

	public boolean canSpawnCore(int size) {
		double modifier = 0.5;// 50 % HP
		if (size >= 5)
			modifier = 0.85;// 85 % HP
		else if (size >= 2)
			modifier = 0.75;// 75 % HP
		return getHitpoints() < (getMaxHitpoints() * modifier);
	}
}