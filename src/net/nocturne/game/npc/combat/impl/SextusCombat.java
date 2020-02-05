package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.Random;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

/**
 * @author Nosz
 * 
 * 
 * 
 */
public class SextusCombat extends CombatScript {
	public float damageBuff;
	int Stage;
	boolean firstloop = false;

	@Override
	public Object[] getKeys() {
		return new Object[] { 17154 };
	}

	public void checkHitpoints(final NPC npc, final Player p) {
		if (npc.getHitpoints() > (npc.getMaxHitpoints() / 4) * 3) { // 100%
			p.getTemporaryAttributtes().put("SextusStage", 0);
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4) * 3
				&& (int) p.getTemporaryAttributtes().get("SextusStage") == 0) { // 75%
			p.getTemporaryAttributtes().put("SextusStage", 1);
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4) * 2
				&& (int) p.getTemporaryAttributtes().get("SextusStage") == 1) { // 50%
			p.getTemporaryAttributtes().put("SextusStage", 2);
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4)
				&& (int) p.getTemporaryAttributtes().get("SextusStage") == 2) { // 25%
			p.getTemporaryAttributtes().put("SextusStage", 3);
		}
	}

	@Override
	public int attack(final net.nocturne.game.npc.NPC npc, final Entity target) {
		final ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		if (npc.getPossibleTargets().size() != 0) {
			for (final Entity t : possibleTargets) {
				if (t instanceof Player) {
					final Player p = (Player) t;
					checkHitpoints(npc, p);
					WorldTasksManager.schedule(new WorldTask() {// NORMAL ATTACK
								Random rn = new Random();
								int max = 1000;
								int min = 750;
								int range = max - min + 1;
								int randomNum = rn.nextInt(range) + min;
								private int gameTick;

								@Override
								public void run() {
									gameTick++;
									if (gameTick == 1) {
										npc.setNextAnimation(new Animation(
												20277));
										npc.setNextGraphics(new Graphics(3975));
									}
									if (gameTick == 2) {
										World.sendProjectile(npc, t, 3978, 10,
												6, 40, 5, 0, 0);
										p.applyHit(new Hit(p, randomNum,
												HitLook.MAGIC_DAMAGE, 0));

									}
								}

							}, 0, 0);

					WorldTasksManager.schedule(new WorldTask() { // AOE SPELL
								int x;
								int y;
								int stage2X;
								int stage2Y;
								int stage3X;
								int stage3Y;
								int stage4X;
								int stage4Y;
								int max = 3000;
								int min = 1000;
								Random rn = new Random();
								int range = max - min + 1;
								int randomNum = rn.nextInt(range) + min;
								private int gameTick;

								@Override
								public void run() {
									gameTick++;
									if ((int) p.getTemporaryAttributtes().get(
											"SextusStage") == 0) {

										if (gameTick == 1) {
											npc.setNextAnimation(new Animation(
													20277));
											x = p.getX();
											y = p.getY();

										}
										if (gameTick == 4) {

											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(x, y, 1));

										}

										if (gameTick == 5) {
											if (target.getX() == x
													&& target.getY() == y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}

										}
									}
									// stage 2
									if ((int) p.getTemporaryAttributtes().get(
											"SextusStage") == 1) {

										if (gameTick == 1) {
											npc.setNextAnimation(new Animation(
													20277));
											x = p.getX();
											y = p.getY();

											if (firstloop == false) {
												stage2X = x;
												stage2Y = y;

											}

										}
										if (gameTick == 4) {

											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(x, y, 1));
										}
										if (gameTick == 5) {
											if (target.getX() == x
													&& target.getY() == y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}

										}
										if (gameTick == 7) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage2X,
															stage2Y, 1));
										}
										if (gameTick == 8) {
											if (target.getX() == stage2X
													&& target.getY() == stage2Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
									}
									// stage 3
									if ((int) p.getTemporaryAttributtes().get(
											"SextusStage") == 2) {
										if (gameTick == 1) {
											npc.setNextAnimation(new Animation(
													20277));
											x = p.getX();
											y = p.getY();

											if (firstloop == false) {
												stage2X = x;
												stage2Y = y;
											}
										}
										if (gameTick == 4) {

											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(x, y, 1));

										}
										if (gameTick == 5) {
											if (target.getX() == x
													&& target.getY() == y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
										if (gameTick == 7) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage2X,
															stage2Y, 1));
											stage3X = stage2X;
											stage3Y = stage2Y;
										}
										if (gameTick == 8) {
											if (target.getX() == stage2X
													&& target.getY() == stage2Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
										if (gameTick == 9) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage3X,
															stage3Y, 1));
											stage4X = stage3X;
											stage4Y = stage3Y;
										}
										if (gameTick == 10) {
											if (target.getX() == stage3X
													&& target.getY() == stage3Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
									}
									// stage 4
									if ((int) p.getTemporaryAttributtes().get(
											"SextusStage") == 3) {
										if (gameTick == 1) {
											npc.setNextAnimation(new Animation(
													20277));
											x = p.getX();
											y = p.getY();

											if (firstloop == false) {
												stage2X = x;
												stage2Y = y;
											}
										}
										if (gameTick == 4) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(x, y, 1));
										}

										if (gameTick == 5) {
											if (target.getX() == x
													&& target.getY() == y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}

										}
										if (gameTick == 7) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage2X,
															stage2Y, 1));
											stage3X = stage2X;
											stage3Y = stage2Y;
										}
										if (gameTick == 8) {
											if (target.getX() == stage2X
													&& target.getY() == stage2Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
										if (gameTick == 9) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage3X,
															stage3Y, 1));
											stage4X = stage3X;
											stage4Y = stage3Y;
										}
										if (gameTick == 10) {
											if (target.getX() == stage3X
													&& target.getY() == stage3Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
										if (gameTick == 11) {
											World.sendGraphics(null,
													new Graphics(3974),
													new WorldTile(stage4X,
															stage4Y, 1));
										}
										if (gameTick == 12) {
											if (target.getX() == stage4X
													&& target.getY() == stage4Y) {
												p.applyHit(new Hit(p,
														randomNum,
														HitLook.MAGIC_DAMAGE, 0));
											}
										}
									}
								}

							}, 0, 0);
				}
			}
		}

		return npc.getAttackSpeed();
	}
}