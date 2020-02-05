package net.nocturne.game.npc.riseofthesix;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Karil extends NPC {

	private static final long serialVersionUID = -3616663005381480284L;
	private NPC karil;
	private boolean FlightMode = false;
	public boolean canDie = false;
	public int hammerDamage = 0;
	public boolean pillarMode = false;

	public Karil(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceFollowClose(false);
		setForceAgressive(true);
		setForceMultiArea(true);
		karil = this;
		setHitpoints(50000);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (pillarMode == true && getHitpoints() <= 4000) {
			hit.setDamage(0);
		}
		if (hit.getLook() == HitLook.RANGE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 4);
		}
		if (hit.getLook() == HitLook.MELEE_DAMAGE) {
			hit.setDamage(hit.getDamage() / 2);
		}
		if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
			hit.setDamage(hit.getDamage() / 6);
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public Hit handleOutgoingHit(Hit hit, Entity target) {
		return hit;
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;

		if (Utils.random(45) == 4 && getId() == 18538
				&& karil.getHitpoints() >= 1 && !isCantInteract()
				&& isUnderCombat()) {
			final WorldTile tile = getSurgeTile(karil, 0, 10, true,
					karil.getAttackedBy());
			if (tile == null || tile.matches(karil)) {
				return;
			}
			karil.lock(2);
			karil.setNextAnimation(new Animation(18358));
			karil.setNextGraphics(new Graphics(3537, 5, 0));
			karil.setNextForceMovement(new ForceMovement(karil, 0, tile, 1,
					Utils.getAngle(tile.getX() - karil.getX(), tile.getY()
							- karil.getY())));
			karil.setAttackingDelay(Utils.currentTimeMillis() + 4000);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					karil.setNextWorldTile(tile);
				}
			}, 1);
		}
		super.processNPC();
	}

	private static WorldTile getSurgeTile(NPC player, int start, int end,
			boolean increment, Entity target) {
		byte[] dirs = Utils.getDirection(player.getDirection());
		WorldTile lastStep = null;
		for (int steps = start; increment ? steps < end : steps > end; steps += (increment ? 1
				: -1)) {
			WorldTile step = new WorldTile(player.getX() + (dirs[0] * steps),
					player.getY() + (dirs[1] * steps), player.getPlane());
			if (target != null
					&& Utils.colides(target.getX(), target.getY(),
							target.getSize(), step.getX(), step.getY(),
							player.getSize())
					|| !player.clipedProjectile(step, true)
					|| !World.isTileFree(step.getPlane(), step.getX(),
							step.getY(), player.getSize()))
				break;
			lastStep = step;
		}
		return lastStep;
	}

	@Override
	public void sendDeath(Entity source) {
		getMostDamageReceivedSourcePlayer().getTimersManager().increaseKills(
				RecordKey.THE_BARROWS_RISE_OF_THE_SIX, false);
		getMostDamageReceivedSourcePlayer()
				.getPackets()
				.sendEntityMessage(0, 16711680, this,
						"As you defeat Karil the Tainted, the shadow engulfs the remaining wights!");
		if (canDie == false)
			return;
	}

}
