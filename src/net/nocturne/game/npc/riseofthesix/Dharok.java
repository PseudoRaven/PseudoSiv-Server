package net.nocturne.game.npc.riseofthesix;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.utils.Utils;

public class Dharok extends NPC {

	private static final long serialVersionUID = -3616663005381480284L;
	private NPC dharok;
	private boolean canDie = false;
	public boolean isCharging = false;
	public int chargeDamage = 0;

	public Dharok(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceFollowClose(true);
		setForceAgressive(true);
		setForceMultiArea(true);
		dharok = this;
		setHitpoints(50000);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (isCharging == true) {
			chargeDamage = chargeDamage + hit.getDamage();
			hit.setDamage(0);
			return;
		}
		if (Utils.random(40) == 5 && isCharging == false && chargeDamage == 0) {
			dharok.setNextForceTalk(new ForceTalk("Give me everything!"));
			dharok.setFreezeDelay(100000);
			dharok.setForceFollowClose(false);
			dharok.setCantFollowUnderCombat(true);
			isCharging = true;
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					World.sendGraphics(dharok,
							new Graphics(4406, 0, 0, 5, true), new WorldTile(
									dharok));
					dharok.setNextAnimation(new Animation(21940));
				}
			}, 1, TimeUnit.SECONDS);
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					dharok.setFreezeDelay(1);
					dharok.setCantFollowUnderCombat(false);
					dharok.setForceFollowClose(true);
					isCharging = false;
				}
			}, 11, TimeUnit.SECONDS);
		}
		if (hit.getLook() == HitLook.RANGE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 3);
		}
		if (hit.getLook() == HitLook.MELEE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 2);
		}
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			hit.setDamage(hit.getDamage());
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public Hit handleOutgoingHit(Hit hit, Entity target) {
		if (chargeDamage >= 1 && isCharging == false) {
			hit.setDamage(hit.getDamage() + chargeDamage);
			chargeDamage = 0;
		}
		return hit;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		super.processNPC();
	}

	@Override
	public void sendDeath(Entity source) {
		getMostDamageReceivedSourcePlayer().getTimersManager().increaseKills(
				RecordKey.THE_BARROWS_RISE_OF_THE_SIX, false);
		getMostDamageReceivedSourcePlayer()
				.getPackets()
				.sendEntityMessage(0, 16711680, this,
						"As you defeat Dharok the Wretched, the shadow engulfs the remaining wights!");
		if (canDie == false)
			return;
	}

}
