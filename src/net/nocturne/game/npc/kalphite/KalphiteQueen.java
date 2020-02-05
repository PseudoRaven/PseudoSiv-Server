package net.nocturne.game.npc.kalphite;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class KalphiteQueen extends NPC {

	private HeadIcon[][] PRAYER_ICONS = { { new HeadIcon(440, 6) },// Familiar,
																	// Magic,
																	// Range
			{ new HeadIcon(440, 0) },// Melee
	};

	// private int nextSoldierTick;

	public KalphiteQueen(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
		setIntelligentRouteFinder(true);
		requestIconRefresh();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (getId() == 1158 || getId() == 16707) {
			Entity src = hit.getSource();
			if (src instanceof Familiar
					|| hit.getLook() == HitLook.RANGE_DAMAGE
					|| hit.getLook() == HitLook.MAGIC_DAMAGE)
				hit.setDamage(0);
		} else {
			if (hit.getLook() == HitLook.MELEE_DAMAGE)
				hit.setDamage(0);
		}
		super.handleIngoingHit(hit);
	}

	@Override
	public void processNPC() {
		if (getId() == 1160 || getId() == 16708) {
			/*
			 * nextSoldierTick++; if (nextSoldierTick % 60 == 0) {//Approx 30
			 * seconds.
			 * 
			 * nextSoldierTick = 0; }
			 */
		}
		super.processNPC();
	}

	@Override
	public void sendDeath(Entity source) {
		if (source instanceof Player)
			((Player) source).getTimersManager().removeTimer(
					RecordKey.KALPHITE_QUEEN);
		if (getId() != 1158 && getId() != 16707)
			increaseKills(RecordKey.KALPHITE_QUEEN, false);
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (getId() == 1158 || getId() == 16707) {
						setCantInteract(true);
						setNextNPCTransformation(getId() == 16707 ? 16708
								: 1160);
						setNextGraphics(new Graphics(getId() == 16707 ? 5037
								: 5038));
						setNextAnimation(new Animation(24293));
						setDirection(Utils.getAngle(-1, 0));// I hope this is
															// west XD
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								reset();
								setCantInteract(false);
								requestIconRefresh();
							}

						}, 6);
					} else {
						giveXP();
						drop();
						reset();
						setLocation(getRespawnTile());
						loadMapRegions();
						finish();
						if (!isSpawned())
							setRespawnTask();
						setNextNPCTransformation(getId() == 16708 ? 16707
								: 1158);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public HeadIcon[] getIcons() {
		return PRAYER_ICONS[getId() == 1160 || getId() == 16708 ? 1 : 0];
	}
}
