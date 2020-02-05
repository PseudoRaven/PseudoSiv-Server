package net.nocturne.game.npc.combat.impl.godwars2;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars2.vindictagorvek.Vindicta;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GorvekAndVindictaInstanceController;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class VindictaCombat extends CombatScript {

	private int location = Utils.random(3);

	@Override
	public Object[] getKeys() {
		return new Object[] { 22459, 22460 };
	}

	private void slice(NPC npc, Entity target) {
		if (npc.getId() == 22459) {
			npc.setNextAnimation(new Animation(28253));
			Hit hit = getMeleeHit(
					npc,
					getMaxHit(npc, Utils.random(1500, 3000),
							NPCCombatDefinitions.MELEE, target));
			delayHit(npc, 0, target, hit);
		}
	}

	private void hurricane(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(28256));
		npc.setNextGraphics(new Graphics(6111));
		for (Entity t : npc.getPossibleTargets()) {
			Hit hit = getMeleeHit(
					npc,
					getMaxHit(npc, Utils.random(2000, 3000),
							NPCCombatDefinitions.MELEE, t));
			delayHit(npc, 0, t, hit);
		}
	}

	private void fly(NPC npc) {
		if (npc.getId() == 22460) {
			Vindicta vindicta = (Vindicta) npc;
			vindicta.setNextAnimation(new Animation(28275));
			vindicta.setNextGraphics(new Graphics(6118));
			vindicta.setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					land(npc);
					vindicta.setCantInteract(false);
					stop();
				}
			}, 2, 1);
		}
	}

	private void land(NPC npc) {
		if (npc.getId() == 22460) {
			Vindicta vindicta = (Vindicta) npc;
			if (location == 0 || location == 1)
				vindicta.setNextWorldTile(new WorldTile(
						GorvekAndVindictaInstanceController.getWorldTile(18,
								10, 1)));
			else if (location == 2)
				vindicta.setNextWorldTile(new WorldTile(
						GorvekAndVindictaInstanceController.getWorldTile(18,
								33, 1)));
			else if (location == 3)
				vindicta.setNextWorldTile(new WorldTile(
						GorvekAndVindictaInstanceController.getWorldTile(33,
								10, 1)));
			npc.setNextAnimation(new Animation(28276));
			npc.setNextGraphics(new Graphics(6118));
		}
	}

	private void stab(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(28273));
		Hit hit = getMeleeHit(
				npc,
				getMaxHit(npc, Utils.random(2000, 3500),
						NPCCombatDefinitions.MELEE, target));
		delayHit(npc, 0, target, hit);
	}

	private void dragonBreath(NPC npc, Entity target) {
		Vindicta vindicta = (Vindicta) npc;
		if (vindicta.getVindictaFirePhaseTwo() == 0) {
			vindicta.setVindictaFirePhaseTwo(1);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X = target.getX();
							vindicta.vindictaP2Y = target.getY();
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X = target.getX();
							vindicta.vindictaP2Y = target.getY();
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 1) {
			vindicta.setVindictaFirePhaseTwo(2);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X2 = target.getX();
							vindicta.vindictaP2Y2 = target.getY();
							vindicta.vindictaP2X = -1;
							vindicta.vindictaP2Y = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X2 = target.getX();
							vindicta.vindictaP2Y2 = target.getY();
							vindicta.vindictaP2X = -1;
							vindicta.vindictaP2Y = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 2) {
			vindicta.setVindictaFirePhaseTwo(3);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X3 = target.getX();
							vindicta.vindictaP2Y3 = target.getY();
							vindicta.vindictaP2X2 = -1;
							vindicta.vindictaP2Y2 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X3 = target.getX();
							vindicta.vindictaP2Y3 = target.getY();
							vindicta.vindictaP2X2 = -1;
							vindicta.vindictaP2Y2 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 3) {
			vindicta.setVindictaFirePhaseTwo(4);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X4 = target.getX();
							vindicta.vindictaP2Y4 = target.getY();
							vindicta.vindictaP2X3 = -1;
							vindicta.vindictaP2Y3 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X4 = target.getX();
							vindicta.vindictaP2Y4 = target.getY();
							vindicta.vindictaP2X3 = -1;
							vindicta.vindictaP2Y3 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 4) {
			vindicta.setVindictaFirePhaseTwo(5);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X5 = target.getX();
							vindicta.vindictaP2Y5 = target.getY();
							vindicta.vindictaP2X4 = -1;
							vindicta.vindictaP2Y4 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X5 = target.getX();
							vindicta.vindictaP2Y5 = target.getY();
							vindicta.vindictaP2X4 = -1;
							vindicta.vindictaP2Y4 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 5) {
			vindicta.setVindictaFirePhaseTwo(6);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaP2X6 = target.getX();
							vindicta.vindictaP2Y6 = target.getY();
							vindicta.vindictaP2X5 = -1;
							vindicta.vindictaP2Y5 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaP2X6 = target.getX();
							vindicta.vindictaP2Y6 = target.getY();
							vindicta.vindictaP2X5 = -1;
							vindicta.vindictaP2Y5 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		}
	}

	private void rangedAttack(NPC npc, Entity target) {
		Vindicta vindicta = (Vindicta) npc;
		vindicta.setNextAnimation(new Animation(28271));
		Projectile projectile = World.sendProjectileNew(npc, target, 6116, 15,
				15, 0, 1.1, 0, 0);
		delayHit(
				npc,
				Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
				target,
				getRangedHit(
						npc,
						getMaxHit(npc, Utils.random(2000, 4000),
								NPCCombatDefinitions.RANGE, target)));
	}

	private void startDragonFire(NPC npc, Entity target) {
		Vindicta vindicta = (Vindicta) npc;
		vindicta.setVindictaX(-1);
		vindicta.setVindictaY(-1);
		if (vindicta.getVindictaFirePhase() == 0) {
			vindicta.setVindictaFirePhase(1);
			NPC gorvek = World.getNPC(22463);
			npc.setNextAnimation(new Animation(28259));
			World.sendGraphics(target, new Graphics(6114),
					new WorldTile(target.getX() + 1, target.getY() + 1, 1));
			WorldTasksManager.schedule(new WorldTask() {
				int fire = Utils.random(2);

				@Override
				public void run() {
					GameExecutorManager.slowExecutor.schedule(() -> {
						gorvek.setNextWorldTile(new WorldTile(
								GorvekAndVindictaInstanceController
										.getWorldTile(63, 62, 1)));
					}, 590, TimeUnit.MILLISECONDS);
					for (int i = 0; i < 20; i++) {
						if (fire == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX = target.getX();
							vindicta.vindictaY = target.getY();
						}
						if (fire == 0 || fire == 2) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX = target.getX();
							vindicta.vindictaY = target.getY();
						}
					}
					stop();
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhase() == 1) {
			vindicta.setVindictaFirePhase(2);
			NPC gorvek = World.getNPC(22463);
			npc.setNextAnimation(new Animation(28259));
			World.sendGraphics(target, new Graphics(6114),
					new WorldTile(target.getX(), target.getY(), 1));
			WorldTasksManager.schedule(new WorldTask() {
				int fire = Utils.random(2);

				@Override
				public void run() {
					GameExecutorManager.slowExecutor.schedule(() -> {
						gorvek.setNextWorldTile(new WorldTile(
								GorvekAndVindictaInstanceController
										.getWorldTile(63, 62, 1)));
					}, 590, TimeUnit.MILLISECONDS);
					for (int i = 0; i < 20; i++) {
						if (fire == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX2 = target.getX();
							vindicta.vindictaY2 = target.getY();
							vindicta.vindictaX = -1;
							vindicta.vindictaY = -1;
						}
						if (fire == 0 || fire == 2) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX2 = target.getX();
							vindicta.vindictaY2 = target.getY();
							vindicta.vindictaX = -1;
							vindicta.vindictaY = -1;
						}
					}
					stop();
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhase() == 2) {
			vindicta.setVindictaFirePhase(3);
			NPC gorvek = World.getNPC(22463);
			npc.setNextAnimation(new Animation(28259));
			World.sendGraphics(target, new Graphics(6114),
					new WorldTile(target.getX(), target.getY(), 1));
			WorldTasksManager.schedule(new WorldTask() {
				int fire = Utils.random(2);

				@Override
				public void run() {
					GameExecutorManager.slowExecutor.schedule(() -> {
						gorvek.setNextWorldTile(new WorldTile(
								GorvekAndVindictaInstanceController
										.getWorldTile(63, 62, 1)));
					}, 590, TimeUnit.MILLISECONDS);
					for (int i = 0; i < 20; i++) {
						if (fire == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX3 = target.getX();
							vindicta.vindictaY3 = target.getY();
							vindicta.vindictaX2 = -1;
							vindicta.vindictaY2 = -1;
						}
						if (fire == 0 || fire == 2) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX3 = target.getX();
							vindicta.vindictaY3 = target.getY();
							vindicta.vindictaX2 = -1;
							vindicta.vindictaY2 = -1;
						}
					}
					stop();
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 3) {
			vindicta.setVindictaFirePhaseTwo(4);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX4 = target.getX();
							vindicta.vindictaY4 = target.getY();
							vindicta.vindictaX3 = -1;
							vindicta.vindictaY3 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX4 = target.getX();
							vindicta.vindictaY4 = target.getY();
							vindicta.vindictaX3 = -1;
							vindicta.vindictaY3 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 4) {
			vindicta.setVindictaFirePhaseTwo(5);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX5 = target.getX();
							vindicta.vindictaY5 = target.getY();
							vindicta.vindictaX4 = -1;
							vindicta.vindictaY4 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX5 = target.getX();
							vindicta.vindictaY5 = target.getY();
							vindicta.vindictaX4 = -1;
							vindicta.vindictaY4 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		} else if (vindicta.getVindictaFirePhaseTwo() == 5) {
			vindicta.setVindictaFirePhaseTwo(6);
			npc.setNextAnimation(new Animation(28277));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (int i = 0; i < 20; i++) {
						if (location == 0 || location == 1) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() + i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() - i, 1));
							vindicta.vindictaX6 = target.getX();
							vindicta.vindictaY6 = target.getY();
							vindicta.vindictaX5 = -1;
							vindicta.vindictaY5 = -1;
						} else if (location == 2 || location == 3) {
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() + i, target
											.getY() - i, 1));
							World.sendGraphics(
									target,
									new Graphics(6112),
									new WorldTile(target.getX() - i, target
											.getY() + i, 1));
							vindicta.vindictaX6 = target.getX();
							vindicta.vindictaY6 = target.getY();
							vindicta.vindictaX5 = -1;
							vindicta.vindictaY5 = -1;
						}
						stop();
					}
				}
			}, 1, 1);
			checkCoords(npc, target);
		}
	}

	public void checkCoords(final NPC npc, final Entity target) {
		Vindicta vindicta = (Vindicta) npc;
		vindicta.setVindictaX(-1);
		vindicta.setVindictaY(-1);
		if (npc.getId() == 22459) {
			if (vindicta.getVindictaFirePhase() == 1) {
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX
												- i || target.getX() == vindicta.vindictaX
												+ i)
												&& (target.getY() == vindicta.vindictaY
														- i || target.getY() == vindicta.vindictaY
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhase() == 2) {
				vindicta.vindictaX = -1;
				vindicta.vindictaY = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX2
												- i || target.getX() == vindicta.vindictaX2
												+ i)
												&& (target.getY() == vindicta.vindictaY2
														- i || target.getY() == vindicta.vindictaY2
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhase() == 3) {
				vindicta.vindictaX2 = -1;
				vindicta.vindictaY2 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX3
												- i || target.getX() == vindicta.vindictaX3
												+ i)
												&& (target.getY() == vindicta.vindictaY3
														- i || target.getY() == vindicta.vindictaY3
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhase() == 4) {
				vindicta.vindictaX3 = -1;
				vindicta.vindictaY3 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX4
												- i || target.getX() == vindicta.vindictaX4
												+ i)
												&& (target.getY() == vindicta.vindictaY4
														- i || target.getY() == vindicta.vindictaY4
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhase() == 5) {
				vindicta.vindictaX4 = -1;
				vindicta.vindictaY4 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX5
												- i || target.getX() == vindicta.vindictaX5
												+ i)
												&& (target.getY() == vindicta.vindictaY5
														- i || target.getY() == vindicta.vindictaY5
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhase() == 6) {
				vindicta.vindictaX6 = -1;
				vindicta.vindictaY6 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaX6
												- i || target.getX() == vindicta.vindictaX6
												+ i)
												&& (target.getY() == vindicta.vindictaX6
														- i || target.getY() == vindicta.vindictaY6
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			}

		} else if (npc.getId() == 22460) {
			if (vindicta.getVindictaFirePhaseTwo() == 1) {
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X
												- i || target.getX() == vindicta.vindictaP2X
												+ i)
												&& (target.getY() == vindicta.vindictaP2Y
														- i || target.getY() == vindicta.vindictaP2Y
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhaseTwo() == 2) {
				vindicta.vindictaP2X = -1;
				vindicta.vindictaP2Y = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X2
												- i || target.getX() == vindicta.vindictaP2X2
												+ i)
												&& (target.getY() == vindicta.vindictaP2Y2
														- i || target.getY() == vindicta.vindictaP2Y2
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhaseTwo() == 3) {
				vindicta.vindictaP2X2 = -1;
				vindicta.vindictaP2Y2 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X3
												- i || target.getX() == vindicta.vindictaP2X3
												+ i)
												&& (target.getY() == vindicta.vindictaP2Y3
														- i || target.getY() == vindicta.vindictaP2Y3
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhaseTwo() == 4) {
				vindicta.vindictaP2X3 = -1;
				vindicta.vindictaP2Y3 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X4
												- i || target.getX() == vindicta.vindictaP2X4
												+ i)
												&& (target.getY() == vindicta.vindictaP2Y4
														- i || target.getY() == vindicta.vindictaP2Y4
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhaseTwo() == 5) {
				vindicta.vindictaP2X4 = -1;
				vindicta.vindictaP2Y4 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X5
												- i || target.getX() == vindicta.vindictaP2X5
												+ i)
												&& (target.getY() == vindicta.vindictaP2Y5
														- i || target.getY() == vindicta.vindictaP2Y5
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			} else if (vindicta.getVindictaFirePhaseTwo() == 6) {
				vindicta.vindictaP2X5 = -1;
				vindicta.vindictaP2Y5 = -1;
				GameExecutorManager.slowExecutor
						.scheduleWithFixedDelay(
								() -> {
									for (int i = 0; i < 20; i++) {
										if ((target.getX() == vindicta.vindictaP2X6
												- i || target.getX() == vindicta.vindictaP2X6
												+ i)
												&& (target.getY() == vindicta.vindictaP2X6
														- i || target.getY() == vindicta.vindictaP2Y6
														+ i)) {
											target.applyHit(new Hit(target,
													Utils.random(500, 1000),
													HitLook.MAGIC_DAMAGE));
										}
									}
								}, 600, 600, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Vindicta vindicta = (Vindicta) npc;
		if (npc.getId() == 22459) {
			switch (vindicta.getPhase()) {
			case 0:
				slice(npc, target);
				break;
			case 1:
				slice(npc, target);
				break;
			case 2:
				hurricane(npc, target);
				break;
			case 4:
				slice(npc, target);
				break;
			case 5:
				slice(npc, target);
				break;
			case 6:
				startDragonFire(npc, target);
				break;
			}
			vindicta.nextPhase();
			if (vindicta.getPhase() < 0 || vindicta.getPhase() > 6)
				vindicta.setPhase(0);
			return npc.getAttackSpeed();
		} else if (npc.getId() == 22460) {
			switch (vindicta.getPhase()) {
			case 0:
				stab(npc, target);
				break;
			case 2:
				rangedAttack(npc, target);
				break;
			case 3:
				stab(npc, target);
				break;
			case 4:
				fly(npc);
				break;
			case 5:
				dragonBreath(npc, target);
				break;
			}
			vindicta.nextPhase();
			if (vindicta.getPhase() < 0 || vindicta.getPhase() > 5)
				vindicta.setPhase(0);
			return npc.getAttackSpeed() - 1;
		}
		return npc.getAttackSpeed();
	}

}
