package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.Random;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
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
public class QuintusCombat extends CombatScript {
	public float damageBuff;
	int Stage;
	int stageOneCompleted;

	public static void destroyLazors(final NPC npc, final Player p, int object) {
		for (int c = 0; c < 20; c++) {
			World.spawnObject(new WorldObject(object, 5, 0, (int) p
					.getTemporaryAttributtes().get("LazorOneX"), (int) p
					.getTemporaryAttributtes().get("LazorOneY") + c, 1), true);
		}
		for (int d = 0; d < 20; d++) {
			World.spawnObject(new WorldObject(object, 5, 1, (int) p
					.getTemporaryAttributtes().get("LazorOneX"), (int) p
					.getTemporaryAttributtes().get("LazorOneY") - d, 1), true);
		}
		for (int a = 0; a < 20; a++) {
			World.spawnObject(new WorldObject(object, 5, 1, (int) p
					.getTemporaryAttributtes().get("LazorTwoX") + a, (int) p
					.getTemporaryAttributtes().get("LazorTwoY"), 1), true);
		}
		for (int f = 0; f < 20; f++) {
			World.spawnObject(new WorldObject(object, 5, 1, (int) p
					.getTemporaryAttributtes().get("LazorTwoX") - f, (int) p
					.getTemporaryAttributtes().get("LazorTwoY"), 1), true);
		}
		for (int b = 0; b < 20; b++) {
			World.spawnObject(new WorldObject(object, 5, 0, (int) p
					.getTemporaryAttributtes().get("LazorThreeX"), (int) p
					.getTemporaryAttributtes().get("LazorThreeY") + b, 1), true);
		}
		for (int g = 0; g < 20; g++) {
			World.spawnObject(new WorldObject(object, 5, 0, (int) p
					.getTemporaryAttributtes().get("LazorThreeX"), (int) p
					.getTemporaryAttributtes().get("LazorThreeY") - g, 1), true);
		}
	}

	public void spawnLazors(final NPC npc, final Player p, int object) {
		if (npc.getHitpoints() > (npc.getMaxHitpoints() / 4) * 3) { // makes
																	// sure the
																	// player
																	// cant find
																	// a way to
																	// skip
																	// stages
			p.getTemporaryAttributtes().put("QuintusStage", 0);
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4) * 3
				&& (int) p.getTemporaryAttributtes().get("QuintusStage") == 0) { // 75%
			p.getTemporaryAttributtes().put("LazorOneX", p.getX());
			p.getTemporaryAttributtes().put("LazorOneY", p.getY());
			for (int i = 0; i < 20; i++) {
				World.spawnObject(
						new WorldObject(object, 5, 0, p.getX(), p.getY() + i, 1),
						true);
				World.spawnObject(
						new WorldObject(object, 5, 0, p.getX(), p.getY() - i, 1),
						true);
				checkCoords(p);
				p.getTemporaryAttributtes().put("QuintusStage", 1);
			}
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4) * 2
				&& (int) p.getTemporaryAttributtes().get("QuintusStage") == 1) { // 50%
			p.getTemporaryAttributtes().put("LazorTwoX", p.getX());
			p.getTemporaryAttributtes().put("LazorTwoY", p.getY());
			for (int i = 0; i < 20; i++) {
				World.spawnObject(
						new WorldObject(object, 5, 1, p.getX() - i, p.getY(), 1),
						true);
				World.spawnObject(
						new WorldObject(object, 5, 1, p.getX() + i, p.getY(), 1),
						true);
				p.getTemporaryAttributtes().put("QuintusStage", 2);
			}
		}
		if (npc.getHitpoints() <= (npc.getMaxHitpoints() / 4)
				&& (int) p.getTemporaryAttributtes().get("QuintusStage") == 2) { // 25%
			p.getTemporaryAttributtes().put("LazorThreeX", p.getX());
			p.getTemporaryAttributtes().put("LazorThreeY", p.getY());
			for (int i = 0; i < 20; i++) {
				World.spawnObject(
						new WorldObject(object, 5, 0, p.getX(), p.getY() - i, 1),
						true);
				World.spawnObject(
						new WorldObject(object, 5, 0, p.getX(), p.getY() + i, 1),
						true);
				p.getTemporaryAttributtes().put("QuintusStage", 3);
			}
		}
	}

	public void checkCoords(final Player p) {
		WorldTasksManager.schedule(new WorldTask() {
			int time;

			@Override
			public void run() {
				time++;
				if (time > 1) {
					if (p.getX() == (int) p.getTemporaryAttributtes().get(
							"LazorOneX")
							&& (int) p.getTemporaryAttributtes().get(
									"QuintusStage") >= 1) {
						p.applyHit(new Hit(p, 450, HitLook.MAGIC_DAMAGE, 0));
						return;
					}
					if (p.getY() == (int) p.getTemporaryAttributtes().get(
							"LazorTwoY")
							&& (int) p.getTemporaryAttributtes().get(
									"QuintusStage") >= 2) {
						p.applyHit(new Hit(p, 450, HitLook.MAGIC_DAMAGE, 0));
						return;
					}
					if (p.getX() == (int) p.getTemporaryAttributtes().get(
							"LazorThreeX")
							&& (int) p.getTemporaryAttributtes().get(
									"QuintusStage") == 3) {
						p.applyHit(new Hit(p, 450, HitLook.MAGIC_DAMAGE, 0));
						return;
					}
				}
			}
		}, 0, 0);
	}

	@Override
	public Object[] getKeys() {
		return new Object[] { 17153 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
		if (npc.getPossibleTargets().size() != 0) {
			for (final Entity t : possibleTargets) {
				if (t instanceof Player) {
					final Player p = (Player) t;
					if (npc.getHitpoints() > (npc.getMaxHitpoints() / 4) * 3) { // 75%
						p.getTemporaryAttributtes().put("QuintusStage", 0);
					}
					spawnLazors(npc, p, 84675);
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
								int max = 3000;
								int min = 1000;
								Random rn = new Random();
								int range = max - min + 1;
								int randomNum = rn.nextInt(range) + min;
								private int gameTick;

								@Override
								public void run() {
									gameTick++;
									if (gameTick == 1) {
										npc.setNextAnimation(new Animation(
												20277));
										x = p.getX();
										y = p.getY();

									}
									if (gameTick == 4) {
										World.sendGraphics(null, new Graphics(
												3974), new WorldTile(x, y, 1));
									}

									if (gameTick == 5) {
										if (target.getX() == x
												&& target.getY() == y) {
											p.applyHit(new Hit(p, randomNum,
													HitLook.MAGIC_DAMAGE, 0));
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
