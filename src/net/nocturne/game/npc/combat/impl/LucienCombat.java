package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.lucien.Lucien;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public class LucienCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 14256 };
	}

	/**
	 * 
	 MISSED(8), REGULAR_DAMAGE(3), MELEE_DAMAGE(0), RANGE_DAMAGE(1),
	 * MAGIC_DAMAGE( 2), REFLECTED_DAMAGE(4), ABSORB_DAMAGE(5),
	 * POISON_DAMAGE(6), DESEASE_DAMAGE( 7), HEALED_DAMAGE(9),
	 * CRITICAL_DAMAGE(10), CANNON_DAMAGE(13);
	 */

	public final static String[] SPECIAL_MESSAGES = { "Puny mortal!!!",
			"Are you ready to die?!", "Get ready to get wrecked!!!",
			"Time for my special!", "HA Ha HA. Here it comes!" };
	public static int attackHit;

	@Override
	public int attack(final NPC npc, final Entity target) {
		int attackStyle = Utils.random(7);
		attackHit = attackStyle;

		if (target instanceof Player) {
			if (((Player) target).getFamiliar() != null) {
				if (target.getAttackedByDelay() < System.currentTimeMillis()) {
					sendFamiliarDeath(npc, target);
					return 4;
				}
			}
		} else if (target instanceof Familiar) {
			if (((Familiar) target).getOwner() != null) {
				if (((Familiar) target).getOwner().getAttackedByDelay() < System
						.currentTimeMillis()) {
					npc.setNextAnimation(new Animation(11338));
					delayHit(npc, 2, target,
							getMagicHit(npc, target.getHitpoints()));
					npc.setNextForceTalk(new ForceTalk(
							"STOP PLAYING GAMES WITH ME, HUMAN!"));
					return 4;
				}
			}
		}
		if (Utils.random(15) == 0) {
			channelingSpinningSkull(npc);

		} else if (Utils.random(10) == 0) {
			if (Utils.random(3) == 0) {
				sendIceTitan(npc);
				return 7;
			}
			prayingToZaros(npc);
		} else if (attackStyle < 3 && target.withinDistance(npc, 1, 2, 2)) {
			meleeAttack(npc, target);
			if (npc.isDead()) {
				((Lucien) npc).removeEnhancedTitan();
			}
		} else {
			switch (attackStyle) {
			case 0:
				sendBlueBall(npc, target);
				break;
			case 1:
				sendRedLightning(npc, target);
				break;
			case 2:
				sendBlackBall(npc, target);
				break;
			case 3:
				sendFireBall(npc, target);
				break;
			case 4:
				sendWhiteRingExplosion(npc, target);
				break;
			case 5:
				teleportToPlayer(npc, target);
				break;

			}
		}
		return 5;
	}

	public void sendIceTitan(NPC npc) {
		npc.setNextAnimation(new Animation(11365));
		final WorldTile iceTile = Utils
				.getRandomTile(2200, 4261, 2208, 4268, 1);
		World.sendGraphics(npc, new Graphics(1978),
				new WorldTile(iceTile.getX() + 1, iceTile.getY() + 1, 1));
		npc.setNextForceTalk(new ForceTalk("Is it getting COLD?"));
		((Lucien) npc).spawnEnhancedIceTitan();
	}

	public void sendFamiliarDeath(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(11338));
		delayHit(npc, 2, ((Player) target).getFamiliar(),
				getMagicHit(npc, target.getHitpoints()));
		npc.setNextForceTalk(new ForceTalk("STOP PLAYING GAMES WITH ME, HUMAN!"));
	}

	public void channelingSpinningSkull(final NPC npc) {
		npc.setNextAnimation(new Animation(11373));
		npc.setNextGraphics(new Graphics(1898));
		npc.setNextForceTalk(new ForceTalk(SPECIAL_MESSAGES[Utils
				.random(SPECIAL_MESSAGES.length)]));
		ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
		for (Entity t : possibleTargets) {
			if (t instanceof Player) {
				Player p = (Player) t;
				if (!p.getMusicsManager().hasMusic(1008)) {
					p.getMusicsManager().playMusic(581);
					p.getMusicsManager().playMusic(584);
					p.getMusicsManager().playMusic(579);
					p.getMusicsManager().playMusic(1008);
				}
			}
			String key = t.getX() + "_" + t.getY();
			if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
				tiles.put(key, new int[] { t.getX(), t.getY() });
				World.sendProjectile(npc,
						new WorldTile(t.getX(), t.getY(), npc.getPlane()),
						1900, 34, 0, 50, 35, 16, 0);
			}
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
				for (int[] tile : tiles.values()) {
					World.sendGraphics(null, new Graphics(1896), new WorldTile(
							tile[0], tile[1], 1));
					for (Entity t : possibleTargets)
						if (t.getX() == tile[0] && t.getY() == tile[1])
							t.applyHit(new Hit(npc, Utils.random(300) + 300,
									HitLook.REGULAR_DAMAGE));
				}
				stop();
			}
		}, 3);
	}

	public void prayingToZaros(final NPC npc) {
		npc.setNextGraphics(new Graphics(444));
		npc.setNextForceTalk(new ForceTalk("GIVE ME ENERGY ZAROS!"));
		ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		int totalHit = 0;
		for (Entity t : possibleTargets) {
			if (t instanceof Familiar) {
				int hit = Utils.random(400, 800);
				if (hit > t.getHitpoints())
					hit = t.getHitpoints();
				totalHit += hit;
				delayHit(npc, 2, t, new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
			}
		}
		final int finalHit = totalHit;
		if (finalHit > 0) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					npc.applyHit(new Hit(npc, finalHit, HitLook.HEALED_DAMAGE));
					stop();
				}
			}, 2);
		}
	}

	public void meleeAttack(final NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(11355));
		delayHit(
				npc,
				1,
				target,
				getMeleeHit(
						npc,
						getMaxHit(npc, Utils.random(400),
								NPCCombatDefinitions.MELEE, target)));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.setNextAnimation(new Animation(11363));
			}
		}, 1 / 2);
	}

	public void sendBlueBall(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(11338));
		delayHit(
				npc,
				3,
				target,
				getMagicHit(
						npc,
						getMaxHit(npc, Utils.random(400),
								NPCCombatDefinitions.MAGE, target)));
		World.sendProjectile(npc, target, 2963, 34, 16, 40, 35, 16, 0);
	}

	public void sendRedLightning(NPC npc, final Entity target) {
		npc.setNextAnimation(new Animation(11338));
		delayHit(
				npc,
				3,
				target,
				getRangedHit(
						npc,
						getMaxHit(npc, Utils.random(400),
								NPCCombatDefinitions.RANGE, target)));
		World.sendProjectile(npc, target, 1904, 34, 16, 30, 60, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				target.setNextGraphics(new Graphics(1910));
			}

		}, 2);
	}

	public void sendBlackBall(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(11318));
		npc.setNextGraphics(new Graphics(1901));
		World.sendProjectile(npc, target, 1899, 34, 16, 30, 95, 16, 0);
		delayHit(
				npc,
				3,
				target,
				getMagicHit(
						npc,
						getMaxHit(npc, Utils.random(400),
								NPCCombatDefinitions.MAGE, target)));

	}

	public void sendFireBall(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(11375));
		target.setNextAnimation(new Animation(11051));
		target.setNextGraphics(new Graphics(2954));
		((Player) target).stopAll();
		int tempX = (int) (Utils.random(1, 3) + target.getX());
		int tempY = (int) (Utils.random(1, 3) + target.getY());
		target.addWalkSteps(tempX, tempY);
		((Player) target).getPackets().sendGameMessage(Color.RED,
				"You have been knocked back due to the blast!");
		delayHit(npc, 2, target, new Hit(npc, Utils.random(200, 400),
				HitLook.RANGE_DAMAGE));

	}

	public void sendWhiteRingExplosion(final NPC npc, final Entity target) {
		npc.setNextAnimation(new Animation(11364));
		npc.setNextGraphics(new Graphics(2600));
		npc.setNextGraphics(new Graphics(3427));
		npc.setCantInteract(true);
		npc.getCombat().removeTarget();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				for (Entity t : npc.getPossibleTargets()) {
					int distance = Utils.getDistance(t, npc);
					double ratio = 1;
					if (distance <= 3)
						ratio = 1;
					else
						ratio = 1 - (distance * 0.1);
					if (ratio <= 0.1)
						ratio = 0.1;
					if (target instanceof Player) {
						if (target.withinDistance(npc, 1, 4, 4)) {
							((Player) target).stopAll();
							int tempX = (int) (Utils.random(1, 3) + target
									.getX());
							int tempY = (int) (Utils.random(1, 3) + target
									.getY());
							target.addWalkSteps(tempX, tempY);
							((Player) target)
									.getPackets()
									.sendGameMessage(Color.RED,
											"You have been knocked back due to the blast!");

							t.applyHit(new Hit(npc, (int) (Utils.random(450,
									650) * ratio), HitLook.MAGIC_DAMAGE, 0));
						}
					}
				}
				npc.getCombat().addCombatDelay(3);
				npc.setCantInteract(false);
				npc.setTarget(target);
			}
		}, 4);
	}

	public void teleportToPlayer(final NPC npc, final Entity target) {
		npc.setCantInteract(true);
		npc.setNextAnimation(new Animation(11319));
		npc.getCombat().removeTarget();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.setCantInteract(false);
				npc.setTarget(target);
				int size = npc.getSize();
				int[][] dirs = Utils.getCoordOffsetsNear(size);
				for (int dir = 0; dir < dirs[0].length; dir++) {
					final WorldTile tile = new WorldTile(new WorldTile(target
							.getX() + dirs[0][dir], target.getY()
							+ dirs[1][dir], target.getPlane()));
					if (tile.withinDistance(npc, 20)
							&& World.canMoveNPC(tile.getPlane(), tile.getX(),
									tile.getY(), size)) {
						npc.setNextWorldTile(new WorldTile(tile.getX(), tile
								.getY(), 1));
					}
				}
			}
		}, 3);
	}
}