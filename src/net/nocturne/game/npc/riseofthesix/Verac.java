package net.nocturne.game.npc.riseofthesix;

import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.TimersManager.RecordKey;

@SuppressWarnings("serial")
public class Verac extends NPC {

	private int axeDmg;
	private boolean axeSpec;
	private boolean special;
	private int specialType;
	NPC verac = this;
	public boolean canDie = false;

	public Verac(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceFollowClose(true);
		setForceAgressive(true);
		setForceMultiArea(true);
		setHitpoints(50000);
		setTarget(getCurrentFaceEntity());
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public void sendSpec(int type) {
		setSpecial(true);
		setSpecialType(type);
		switch (type) {
		case 0:
			break;
		case 1:
			break;
		}
	}

	@Override
	public Hit handleOutgoingHit(Hit hit, Entity target) {
		return hit;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hit.getLook() == HitLook.RANGE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 6);
		}
		if (hit.getLook() == HitLook.MELEE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 4);
		}
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			hit.setDamage(hit.getDamage() / 2);
		}
		super.handleIngoingHit(hit);
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	@Override
	public void sendDeath(Entity source) {
		getMostDamageReceivedSourcePlayer().getTimersManager().increaseKills(
				RecordKey.THE_BARROWS_RISE_OF_THE_SIX, false);
		getMostDamageReceivedSourcePlayer()
				.getPackets()
				.sendEntityMessage(0, 16711680, this,
						"As you defeat Verac the Defiled, the shadow engulfs the remaining wights!");
		if (canDie == false)
			return;
	}

}
