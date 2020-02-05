package net.nocturne.game.npc.riseofthesix;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.utils.Utils;

public class Torag extends NPC {

	private static final long serialVersionUID = -3616663005381480284L;
	private NPC torag;
	private boolean canDie = false;
	public boolean isHammering = false;
	public int hammerDamage = 0;

	public Torag(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceFollowClose(true);
		setForceAgressive(true);
		setForceMultiArea(true);
		torag = this;
		setHitpoints(50000);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (hammerDamage >= 2500 && isHammering == true) {
			isHammering = false;
			hammerDamage = 0;
			getMostDamageReceivedSourcePlayer().setNextAnimation(
					new Animation(21938));
			getMostDamageReceivedSourcePlayer().setFreezeDelay(1);
			getMostDamageReceivedSourcePlayer().getAppearence().setRenderEmote(
					-1);
		}
		if (isHammering == true) {
			hammerDamage = hammerDamage + hit.getDamage();
			hit.setDamage(0);
			return;
		}
		if (isHammering == true) {// justincase
			hit.setDamage(0);
		}
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

	@Override
	public Hit handleOutgoingHit(Hit hit, Entity target) {
		if (isHammering) {
			setCantInteract(false);
			setNextAnimation(new Animation(21935));
			return null;
		}
		if (Utils.random(30) == 1 && isHammering == false && hammerDamage == 0
				&& getMostDamageReceivedSourcePlayer() != null) {
			isHammering = true;
			getMostDamageReceivedSourcePlayer().setFreezeDelay(
					Integer.MAX_VALUE);
			;
			GameExecutorManager.slowExecutor.schedule(
					() -> {
						setNextAnimation(new Animation(21933));
						getMostDamageReceivedSourcePlayer().setNextAnimation(
								new Animation(21934));
						getMostDamageReceivedSourcePlayer().getAppearence()
								.setRenderEmote(2985);
						;
					}, 1, TimeUnit.SECONDS);
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
						"As you defeat Torag the Corrupted, the shadow engulfs the remaining wights!");
		if (canDie == false)
			return;
	}

}
