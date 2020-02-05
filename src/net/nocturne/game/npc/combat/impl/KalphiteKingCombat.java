package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Colour;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.kalphite.KalphiteKing;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class KalphiteKingCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 16697, 16698, 16699 };
	}

	private void rangedBasic(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(19450));
		for (Entity e : npc.getPossibleTargets()) {
			((Player) e).getPackets().sendGameMessage("Basic");
			Projectile projectile = World.sendProjectile(npc, e, false, true,
					0, 3747, 30, 30, 25, 2, 0, 0);
			int delay = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
			Hit hit = getRangedHit(npc,
					getMaxHit(npc, 1460, NPCCombatDefinitions.RANGE, e));
			hit.setAbilityMark();
			delayHit(npc, delay, e, hit);
		}
	}

	private void rangedFragmentation(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(19450));
		List<Entity> list = npc.getPossibleTargets();
		Collections.shuffle(list);
		int c = 0;
		for (Entity e : list) {
			if (c < 3) {
				Projectile projectile = World.sendProjectile(npc, e, false,
						true, 0, 3747, 30, 30, 25, 2, 0, 0);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						e.getEffectsManager()
								.startEffect(
										new Effect(EffectType.FRAGMENTATION,
												10, HitLook.RANGE_DAMAGE,
												new Graphics(3574), Utils
														.random(1000, 1800), 2,
												npc, new WorldTile(e)));
					}
				}, Utils.projectileTimeToCycles(projectile.getEndTime()) - 1);
				c++;
			}
		}
	}

	private void rangedIncendiary(NPC npc, Entity target) {
		((Player) target).getPackets().sendGameMessage("Incendiary Shot!");
		npc.setNextAnimation(new Animation(19450));
		List<Entity> list = ((KalphiteKing) npc).getPossibleTargets();
		Collections.shuffle(list);
		int c = 0;
		while (c < 3) {
			for (Entity t : list) {
				Projectile projectile = World.sendProjectile(npc, t, false,
						true, 0, 3747, 30, 30, 25, 2, 0, 0);
				Hit hit = new Hit(npc, Utils.random(3000, 5000),
						HitLook.RANGE_DAMAGE);
				hit.setAbilityMark();
				delayHit(
						npc,
						Utils.projectileTimeToCycles(projectile.getEndTime()) + 2,
						t, hit);
				t.getEffectsManager().startEffect(
						new Effect(EffectType.INCENDIARY_SHOT, 5));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						t.setNextGraphics(new Graphics(3522));
					}
				}, 3);
				c++;
			}
		}
	}

	private void green(NPC npc, Entity target) {
		KalphiteKing king = (KalphiteKing) npc;
		king.setForceFollowClose(true);
		king.setNextAnimation(new Animation(19464));
		king.setNextGraphics(new Graphics(3738));
		Projectile projectile = World.sendProjectile(npc, target, 3739, 58, 30,
				25, 75, 10, 0);
		target.setNextGraphics(new Graphics(3740, projectile.getEndTime(), 0));
		if (target instanceof Player) {
			((Player) target).lock(11);
			((Player) target).stopAll();
			((Player) target).setNextAnimation(new Animation(-1));
			((Player) target).setNextColour(new Colour(projectile.getEndTime(),
					580 - projectile.getEndTime(), 100, 40, 40, 100));
			((Player) target)
					.getPackets()
					.sendGameMessage(
							"The Kalphite King has imobilised you while preparing for a powerful attack. You are unable to move.");
			((Player) target).getEffectsManager().removeEffect(
					EffectType.DEVOTION);
			((Player) target).getEffectsManager().removeEffect(
					EffectType.BARRICADE);
			((Player) target).getEffectsManager().removeEffect(
					EffectType.IMMORTALITY);
			((Player) target).getEffectsManager().removeEffect(
					EffectType.RESONANCE);
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				king.setForceFollowClose(true);
			}
		}, 8);
	}

	private boolean excecuteGreen(NPC npc, Entity target) {
		if (target instanceof Player) {
			((Player) target).getPackets().sendGameMessage("Checking...");
		}
		if (Utils.isOnRange(npc, target, 2)) {
			npc.setNextAnimation(new Animation(19449));
			target.resetReceivedHits();
			target.resetReceivedDamage();
			target.getEffectsManager().removeEffect(EffectType.SLAUGHTER);
			target.getEffectsManager().removeEffect(EffectType.FRAGMENTATION);
			target.applyHit(new Hit(npc, target.getHitpoints(),
					HitLook.MELEE_DAMAGE));
			npc.setForceFollowClose(false);
			((KalphiteKing) npc).nextPhase();
			return true;
		}
		return false;
	}

	private void dig(NPC npc, Entity target) {
		((KalphiteKing) npc).dig(target);
	}

	private void rangedStun(NPC npc, Entity target) {
		for (Entity t : npc.getPossibleTargets()) {
			if (t instanceof Player && Utils.random(10) < 6)
				t.getEffectsManager().startEffect(
						new Effect(EffectType.STUNNED, 8));
		}
	}

	private void quake(NPC npc, Entity target) {
		KalphiteKing king = (KalphiteKing) npc;
		king.setNextAnimation(new Animation(19435));
		king.setNextGraphics(new Graphics(3734));
		for (Entity t : npc.getPossibleTargets()) {
			if (Utils.isOnRange(king, t, 3)) {
				Hit hit = getMeleeHit(
						npc,
						getMaxHit(npc, Utils.random(1000, 3000),
								NPCCombatDefinitions.MELEE, t));
				hit.setAbilityMark();
				delayHit(npc, 1, t, hit);
				if (t instanceof Player)
					((Player) t).getSkills().drainLevel(Skills.DEFENCE,
							hit.getDamage() / 200);
			}
		}
	}

	private void meleePush(NPC npc, Entity target) {
		KalphiteKing king = (KalphiteKing) npc;
		king.setTarget(null);
		final byte[] dirs = Utils.getOrthogonalDirection(
				npc.getLastWorldTile(), target.getLastWorldTile());
		if (dirs[0] == 1) // To make it face the correct way
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 10, king
					.getY() + 2, king.getPlane()));
		else if (dirs[0] == -1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() - 10, king
					.getY() + 2, king.getPlane()));
		else if (dirs[1] == 1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 2, king
					.getY() + 10, king.getPlane()));
		else if (dirs[1] == -1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 2, king
					.getY() - 10, king.getPlane()));
		king.setNextAnimation(new Animation(19449));
		List<Entity> targets = king.getPossibleTargets();
		for (Entity t : targets) {
			if (t instanceof Player) // Ugly but simple code for checking if
										// player should be pushed back
			{
				Hit hit = getMeleeHit(npc, Utils.random(800, 3600));
				hit.setAbilityMark();
				if (dirs[0] == 1) {
					if (t.getY() - king.getY() >= -1
							&& t.getY() - king.getY() <= 4
							&& t.getX() - king.getX() >= 2
							&& t.getX() - king.getX() <= 6) {
						t.setNextAnimation(new Animation(10070));
						t.setNextForceMovement(new ForceMovement(t, 0,
								new WorldTile(t.getX() + dirs[0], t.getY()
										+ dirs[1], t.getPlane()), 1,
								ForceMovement.EAST));
						delayHit(npc, 0, t, hit);
					}
				}
				if (dirs[0] == -1) {
					if (t.getY() - king.getY() >= -1
							&& t.getY() - king.getY() <= 4
							&& t.getX() - king.getX() <= 0
							&& t.getX() - king.getX() <= -6) {
						t.setNextForceMovement(new ForceMovement(t, 0,
								new WorldTile(t.getX() + dirs[0], t.getY()
										+ dirs[1], t.getPlane()), 1,
								ForceMovement.WEST));
						t.setNextAnimation(new Animation(10070));
						delayHit(npc, 0, t, hit);
					}
				}
				if (dirs[1] == 1) {
					if (t.getX() - king.getX() >= -2
							&& t.getX() - king.getX() <= 5
							&& t.getY() - king.getY() >= 2
							&& t.getY() - king.getY() <= 6) {
						t.setNextForceMovement(new ForceMovement(t, 0,
								new WorldTile(t.getX() + dirs[0], t.getY()
										+ dirs[1], t.getPlane()), 1,
								ForceMovement.NORTH));
						t.setNextAnimation(new Animation(10070));
						delayHit(npc, 0, t, hit);
					}
				}
				if (dirs[1] == -1) {
					if (t.getX() - king.getX() >= -2
							&& t.getX() - king.getX() <= 5
							&& t.getY() - king.getY() <= -2
							&& t.getY() - king.getY() >= -6) {
						t.setNextForceMovement(new ForceMovement(t, 0,
								new WorldTile(t.getX() + dirs[0], t.getY()
										+ dirs[1], t.getPlane()), 1,
								ForceMovement.SOUTH));
						t.setNextAnimation(new Animation(10070));
						delayHit(npc, 0, t, hit);
					}
				}
			}
		}
	}

	private void meleeDismember(NPC npc, Entity target) {
		Entity t = null;
		try {
			List<Entity> targets = npc.getPossibleTargets();
			Collections.shuffle(targets);
			t = targets.get(0);
		} catch (Exception e) {
			((Player) target).getPackets()
					.sendGameMessage("Bleed didn't work!");
		}
		if (t != null) {
			npc.setTarget(t);
			npc.setNextFaceEntity(t);
			npc.setNextAnimation(new Animation(19449));
			t.getEffectsManager().startEffect(
					new Effect(EffectType.DISMEMBER, 10, HitLook.MELEE_DAMAGE,
							new Graphics(3464), Utils.random(500, 1000), 2,
							npc, new WorldTile(target)));
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.setTarget(target);
			}
		}, 1);
	}

	private void meleeSlaughter(NPC npc, Entity target) {
		Entity t = null;
		try {
			List<Entity> targets = npc.getPossibleTargets();
			Collections.shuffle(targets);
			t = targets.get(0);
		} catch (Exception e) {
			((Player) target).getPackets()
					.sendGameMessage("Bleed didn't work!");
		}
		if (t != null) {
			npc.setTarget(t);
			npc.setNextFaceEntity(t);
			npc.setNextAnimation(new Animation(19449));
			t.getEffectsManager().startEffect(
					new Effect(EffectType.SLAUGHTER, 10, HitLook.MELEE_DAMAGE,
							new Graphics(3464), Utils.random(1000, 5200), 2,
							npc, new WorldTile(target)));
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.setTarget(target);
			}
		}, 1);
	}

	private void rush(NPC npc, Entity target) {
		KalphiteKing king = (KalphiteKing) npc;
		king.setTarget(null);
		final byte[] dirs = Utils.getOrthogonalDirection(
				npc.getLastWorldTile(), target.getLastWorldTile());
		if (dirs[0] == 1) // To make it face the correct way
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 10, king
					.getY() + 2, king.getPlane()));
		else if (dirs[0] == -1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() - 10, king
					.getY() + 2, king.getPlane()));
		else if (dirs[1] == 1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 2, king
					.getY() + 10, king.getPlane()));
		else if (dirs[1] == -1)
			king.setNextFaceWorldTile(new WorldTile(king.getX() + 2, king
					.getY() - 10, king.getPlane()));
		WorldTile lastTile = null;
		int distance;
		for (distance = 1; distance < 10; distance++) {
			WorldTile nextTile = new WorldTile(new WorldTile(king.getX()
					+ (dirs[0] * distance), king.getY() + (dirs[1] * distance),
					king.getPlane()));
			if (!World.isFloorFree(nextTile.getPlane(), nextTile.getX(),
					nextTile.getY(), king.getSize()))
				break;
			lastTile = nextTile;
		}
		if (lastTile == null || distance <= 2) {
			king.setNextAnimation(new Animation(19447));
			king.setNextGraphics(new Graphics(3735));
			for (Entity t : king.getPossibleTargets()) {
				if (!Utils.isOnRange(king, t, 1))
					continue;
				delayHit(npc, 0, t,
						getRegularHit(npc, Utils.random(2600) + 2600));
			}
		} else {
			king.setNextAnimation(new Animation(19457));
			final int maxStep = distance / 2;
			king.setCantInteract(true);
			king.setNextAnimation(new Animation(maxStep + 19456));
			int totalTime = distance / 2;
			final WorldTile firstTile = new WorldTile(king);
			int dir = king.getDirection();
			king.setNextForceMovement(new ForceMovement(firstTile, 5, lastTile,
					totalTime + 5, dir));
			WorldTile tpTile = lastTile;
			final ArrayList<Entity> targets = king.getPossibleTargets();
			WorldTasksManager.schedule(new WorldTask() {
				int step = 0;

				@Override
				public void run() {
					if (step == maxStep - 1) {
						king.setCantInteract(false);
						king.setTarget(target);
						stop();
						return;
					}
					if (step == 1)
						king.setNextWorldTile(tpTile);
					WorldTile kingTile = new WorldTile(firstTile.getX()
							+ (dirs[0] * step * 2), firstTile.getY()
							+ (dirs[1] * step * 2), king.getPlane());
					int leftSteps = (maxStep - step) + 1;
					for (Entity t : targets) {
						if (!(t instanceof Player))
							continue;
						Player player = (Player) t;
						if (player.isLocked())
							continue;
						if (Utils.colides(kingTile, t, king.getSize(), 1)) {
							WorldTile lastTileForP = null;
							int stepCount = 0;
							for (int thisStep = 1; thisStep <= leftSteps; thisStep++) {
								WorldTile nextTile = new WorldTile(
										new WorldTile(
												player.getX()
														+ (dirs[0] * thisStep * 2),
												player.getY()
														+ (dirs[1] * thisStep * 2),
												player.getPlane()));
								if (!World.isFloorFree(nextTile.getPlane(),
										nextTile.getX(), nextTile.getY()))
									break;
								lastTileForP = nextTile;
								stepCount = thisStep;
							}
							if (lastTileForP == null)
								continue;
							player.setNextForceMovement(new ForceMovement(
									player, 0, lastTileForP, stepCount, Utils
											.getAngle(
													firstTile.getX()
															- player.getX(),
													firstTile.getY()
															- player.getY())));
							player.setNextAnimation(new Animation(10070));
							player.lock(stepCount + 1);
							delayHit(
									npc,
									0,
									t,
									getRegularHit(npc, Utils.random(1800, 3600)));
							final WorldTile lastTileForP_T = lastTileForP;
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.setNextWorldTile(lastTileForP_T);
									player.faceEntity(king);
								}
							}, 0);
						}
					}
					step++;
				}
			}, 3, 0);
		}
	}

	private void mageBomb(NPC npc, Entity target, int n, boolean bleed) {
		npc.setNextAnimation(new Animation(19448));
		npc.setNextGraphics(new Graphics(3742));
		for (Entity t : npc.getPossibleTargets()) {
			WorldTile tile = new WorldTile(t).transform(n, 0, 0);
			Projectile projectile = World.sendProjectileNew(npc, tile, 3743,
					100, 70, 80, 1, 16, 0);
			// if(twoOrbs)
			// World.sendProjectileNew(npc, tile.transform(0, -1, 0), 3743, 100,
			// 30, 80, 1, 16, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					mageBombGraphics(npc, tile);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							World.sendGraphics(npc, new Graphics(3752), tile);
							for (Entity t : npc.getPossibleTargets()) {
								if (t.withinDistance(tile, 2)) {
									if (!bleed) {
										Hit hit = new Hit(npc, Utils.random(
												1500, 4000),
												HitLook.MAGIC_DAMAGE);
										hit.setAbilityMark();
										t.applyHit(hit);
									} else
										t.getEffectsManager()
												.startEffect(
														new Effect(
																EffectType.COMBUST,
																10,
																HitLook.MAGIC_DAMAGE,
																new Graphics(
																		3574),
																Utils.random(
																		1000,
																		1800) + 1,
																4,
																npc,
																new WorldTile(t)));
								}
							}
						}
					}, 3);
				}
			}, Utils.projectileTimeToCycles(projectile.getEndTime()) - 1);
		}
	}

	private void mageDoubleBomb(NPC npc, Entity target) {
		mageBomb(npc, target, 1, false);
		mageBomb(npc, target, -1, false);
	}

	private void mageBlueBall(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(19448));
		npc.setNextGraphics(new Graphics(3757));
		for (Entity t : npc.getPossibleTargets()) {
			Projectile projectile = World.sendProjectileNew(npc, t, 3758, 100,
					30, 80, 2, 16, 0);
			int delay = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
			Hit hit = getMagicHit(
					npc,
					getMaxHit(npc, Utils.random(2000, 4500),
							NPCCombatDefinitions.MAGE, t));
			t.setStunDelay(8);
			hit.setAbilityMark();
			delayHit(npc, delay, t, hit);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (t instanceof Player)
						((Player) t).getPackets().sendGameMessage(
								"You've been prevented from moving.");
				}
			}, delay);
		}
	}

	private void mageCombust(NPC npc, Entity target) {
		mageBomb(npc, target, 0, true);
	}

	private void mageBombGraphics(NPC npc, WorldTile tile) {
		World.sendGraphics(npc, new Graphics(3743, 0, 950), tile);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.sendGraphics(npc, new Graphics(3743, 0, 990), tile);
			}
		}, 575, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.sendGraphics(npc, new Graphics(3743, 0, 1030), tile);
			}
		}, 1150, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.sendGraphics(npc, new Graphics(3743, 0, 1070), tile);
			}
		}, 1725, TimeUnit.MILLISECONDS);
	}

	@Override
	public int attack(NPC npc, Entity target) {
		KalphiteKing king = (KalphiteKing) npc;
		((Player) target).getPackets().sendGameMessage(
				"Phase " + king.getPhase());
		if ((king.isStunned() || king.isBound())
				&& (king.getId() == 16699 || king.getId() == 16698)) {
			king.getEffectsManager().removeEffect(EffectType.STUNNED);
			king.getEffectsManager().removeEffect(EffectType.BOUND);
			king.getEffectsManager().removeEffect(EffectType.BINDING_SHOT);
			if (king.getPhase() != 5 && king.getPhase() != 6) // Can't skip
																// green after
																// he started it
				king.nextPhase();
			else if (king.getPhase() == 5)
				king.setPhase(7);
			((Player) target).getPackets().sendGameMessage(
					"Was stunned! - Attack skipped");
			if (king.getPhase() < 0 || king.getPhase() > 9)
				king.setPhase(0);
			return king.getAttackSpeed() + 5;
		}
		if (Utils.random(20) == 1 && !king.isShieldActive) {
			king.activateShield();
		}
		// When he spawns minions, he skips an attack only if it's not melee
		// phase and he hasnt thrown the green ball before the 1 hit
		if ((king.getHPPercentage() < 75 && king.getSpawnCount() < 1)
				&& ((king.getPhase() != 6 && king.getId() == 16699)
						|| (king.getPhase() != 9 && king.getId() == 16697) || king
						.getId() == 16698)) {
			king.marauders();
			king.spawnCount++;
			if (!(king.getPhase() == 5 && king.getId() == 16697)
					&& !(king.getId() == 16697)) {
				king.nextPhase();
			} else if (king.getPhase() == 5 && king.getId() == 16699) {
				king.setPhase(7);
			}
			return 17;
		} else if ((king.getHPPercentage() < 25 && king.getSpawnCount() < 2)
				&& ((king.getPhase() != 6 && king.getId() == 16699)
						|| (king.getPhase() != 9 && king.getId() == 16697) || king
						.getId() == 16698)) { // Same here
			king.marauders();
			king.marauders();
			king.spawnCount++;
			if (!(king.getPhase() == 5 && king.getId() == 16697)
					&& !(king.getId() == 16697)) {
				king.nextPhase();
			} else if (king.getPhase() == 5 && king.getId() == 16699) {
				king.setPhase(7);
			}
			return 17;
		}
		if (Utils.random(30) == 15 && king.getPhase() != 5
				&& king.getPhase() != 9) {
			king.switchPhase();
		}
		if (npc.getId() == 16699) {
			switch (king.getPhase()) {
			case 0:
				rangedBasic(npc, target);
				break;
			case 1:
				rangedStun(npc, target);
				break;
			case 2:
				rangedFragmentation(npc, target);
				break;
			case 3:
				rangedBasic(npc, target);
				break;
			case 4:
				rangedIncendiary(npc, target);
				break;
			case 5:
				rangedStun(npc, target);
				break;
			case 6:
				rangedFragmentation(npc, target);
				king.nextPhase();
				return 17;
			case 7:
				rangedBasic(npc, target);
				break;
			case 8:
				rangedStun(npc, target);
				break;
			case 9:
				green(npc, target);
				king.nextPhase();
				return 8;
			case 10:
				if (excecuteGreen(npc, target))
					return npc.getAttackSpeed();
				else
					return 1;
			}
			king.nextPhase();
			if (king.getPhase() < 0 || king.getPhase() > 9)
				king.setPhase(0);
			return npc.getAttackSpeed();
		} else if (npc.getId() == 16697) {
			switch (king.getPhase()) {
			case 0:
				meleeSlaughter(npc, target);
				break;
			case 1:
				quake(npc, target);
				break;
			case 2:
				meleeDismember(npc, target);
				break;
			case 3:
				meleePush(npc, target);
				break;
			case 4:
				meleeSlaughter(npc, target);
				break;
			case 5:
				rush(npc, target);
				break;
			case 6:
				meleeSlaughter(npc, target);
				king.nextPhase();
				return 17;
			case 7:
				quake(npc, target);
				break;
			case 8:
				meleeDismember(npc, target);
				break;
			case 9:
				green(npc, target);
				king.nextPhase();
				return 8;
			case 10:
				if (excecuteGreen(npc, target))
					return npc.getAttackSpeed();
				else
					return 1;
			}
			king.nextPhase();
			if (king.getPhase() < 0 || king.getPhase() > 9)
				king.setPhase(0);
			return npc.getAttackSpeed();
		} else if (npc.getId() == 16698) {
			switch (king.getPhase()) {
			case 0:
				mageBomb(npc, target, 0, false);
				break;
			case 1:
				mageBlueBall(npc, target);
				break;
			case 2:
				mageBomb(npc, target, 0, false);
				break;
			case 3:
				mageBomb(npc, target, 0, false);
				break;
			case 4:
				dig(npc, target);
				break;
			case 5:
				mageDoubleBomb(npc, target);
				break;
			case 6:
				mageBomb(npc, target, 0, false);
				break;
			case 7:
				rush(npc, target);
				king.nextPhase();
				return 17;
			case 8:
				mageBlueBall(npc, target);
				break;
			case 9:
				mageCombust(npc, target);
				break;
			case 10:
				mageBomb(npc, target, 0, false);
				break;
			}
			king.nextPhase();
			if (king.getPhase() < 0 || king.getPhase() > 9)
				king.setPhase(0);
			return npc.getAttackSpeed() + 3;
		}
		return npc.getAttackSpeed();
	}
}