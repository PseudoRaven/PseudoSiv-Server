package net.nocturne.game.npc.combat.impl.godwars2;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars2.helwyr.CywirAlpha;
import net.nocturne.game.npc.godwars2.helwyr.Helwyr;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.HelwyrInstanceController;
import net.nocturne.utils.Utils;

public class HelwyrCombat extends CombatScript {

	public static WorldTile random1;
	public static WorldTile random2;
	public static WorldTile random3;
	public static WorldTile random4;
	public static WorldTile random5;
	public static WorldTile random6;
	public static WorldTile bleedTile;
	private static int direction;
	private static WorldTile frenzy;

	@Override
	public Object[] getKeys() {
		return new Object[] { 22438 };
	}

	public void bite(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextAnimation(new Animation(28205));
		delayHit(
				npc,
				0,
				target,
				getMeleeHit(
						npc,
						getMaxHit(npc, Utils.random(500, 1224),
								NPCCombatDefinitions.MELEE, target)));
	}

	public void nature(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextForceTalk(new ForceTalk("Nature, lend me your aid!"));
		HelwyrInstanceController.addTiles();
		HelwyrInstanceController.setTiles();
		random1 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		random2 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		random3 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		helwyr.setNextAnimation(new Animation(28207));
		Projectile projectile1 = World.sendProjectileNew(helwyr, random1, 6122,
				70, 0, 0, 2, 0, 0);
		Projectile projectile2 = World.sendProjectileNew(helwyr, random2, 6122,
				70, 0, 0, 2, 0, 0);
		Projectile projectile3 = World.sendProjectileNew(helwyr, random3, 6122,
				70, 0, 0, 2, 0, 0);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random1));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random1)), 59400);
		}, Utils.projectileTimeToCycles(projectile1.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random2));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random2)), 59400);
		}, Utils.projectileTimeToCycles(projectile2.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random3));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random3)), 59400);
		}, Utils.projectileTimeToCycles(projectile3.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random1));
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random2));
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random3));
			/*
			 * random1 = new WorldTile(HelwyrInstanceController.getWorldTile(0,
			 * 63, 1)); random2 = new
			 * WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
			 * random3 = new WorldTile(HelwyrInstanceController.getWorldTile(0,
			 * 63, 1));
			 */
		}, 59400, TimeUnit.MILLISECONDS);
	}

	public void natureSecond(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextForceTalk(new ForceTalk("Nature, lend me your aid!"));
		HelwyrInstanceController.addTiles();
		HelwyrInstanceController.setTiles();
		random4 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		random5 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		random6 = HelwyrInstanceController.Tiles.get(Utils.random(24));
		helwyr.setNextAnimation(new Animation(28207));
		Projectile projectile1 = World.sendProjectileNew(helwyr, random4, 6122,
				70, 0, 0, 2, 0, 0);
		Projectile projectile2 = World.sendProjectileNew(helwyr, random5, 6122,
				70, 0, 0, 2, 0, 0);
		Projectile projectile3 = World.sendProjectileNew(helwyr, random6, 6122,
				70, 0, 0, 2, 0, 0);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random4));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random4)), 59400);
		}, Utils.projectileTimeToCycles(projectile1.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random5));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random5)), 59400);
		}, Utils.projectileTimeToCycles(projectile2.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6124), new WorldTile(
					random6));
			World.spawnTemporaryObject(new WorldObject(101900, 10, 3,
					new WorldTile(random6)), 59400);
		}, Utils.projectileTimeToCycles(projectile3.getEndTime() * 600),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random4));
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random5));
			World.sendGraphics(target, new Graphics(6125), new WorldTile(
					random6));
			/*
			 * random4 = new WorldTile(HelwyrInstanceController.getWorldTile(0,
			 * 63, 1)); random5 = new
			 * WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
			 * random6 = new WorldTile(HelwyrInstanceController.getWorldTile(0,
			 * 63, 1));
			 */
		}, 59400, TimeUnit.MILLISECONDS);
	}

	public void bleed(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextForceTalk(new ForceTalk("YOU. WILL. BLEED!"));
		helwyr.setNextAnimation(new Animation(28214));
		/*
		 * helwyr.setCantInteract(true); helwyr.setCantFollowUnderCombat(true);
		 * helwyr.setCantSetTargetAutoRelatio(true); helwyr.setTarget(null);
		 */
		direction = target.getDirection();
		helwyr.setDirection(direction);
		helwyr.setNextGraphics(new Graphics(6126));
		System.out.println("X: " + helwyr.getX() + ", Y: " + helwyr.getY());
		bleedTile = new WorldTile(target.getX(), target.getY(), 1);
		GameExecutorManager.slowExecutor
				.schedule(
						() -> {
							if (target.getX() == bleedTile.getX()
									&& target.getY() == bleedTile.getY()) {
								target.applyHit(new Hit(target, Utils.random(
										1500, 3000), HitLook.MELEE_DAMAGE));
								((Player) target)
										.getPackets()
										.sendEntityMessage(1, 15263739, target,
												"Helwyr's continued attacks cause you to lose even more blood!");
							}
							helwyr.setCantSetTargetAutoRelatio(false);
						}, 1500, TimeUnit.MILLISECONDS);
		helwyr.setDirection(direction);
	}

	public void frenzy(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextForceTalk(new ForceTalk("You cannot escape me. Aaaargh!"));
		helwyr.setNextAnimation(new Animation(28215));
		frenzy = new WorldTile(helwyr.getX(), helwyr.getY(), 1);
		if (target.withinDistance(frenzy, 5)) {
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getMaxHit(npc, Utils.random(900, 1800),
									NPCCombatDefinitions.MELEE, target)));
		}
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (target.withinDistance(frenzy, 5)) {
						delayHit(
								npc,
								0,
								target,
								getMeleeHit(
										npc,
										getMaxHit(npc, Utils.random(900, 1800),
												NPCCombatDefinitions.MELEE,
												target)));
					}
				}, 1200, TimeUnit.MILLISECONDS);
		if (target.withinDistance(frenzy, 5)) {
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getMaxHit(npc, Utils.random(900, 1800),
									NPCCombatDefinitions.MELEE, target)));
		}
		GameExecutorManager.slowExecutor.schedule(() -> {

		}, 2400, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (target.withinDistance(frenzy, 5)) {
						delayHit(
								npc,
								0,
								target,
								getMeleeHit(
										npc,
										getMaxHit(npc, Utils.random(900, 1800),
												NPCCombatDefinitions.MELEE,
												target)));
					}
				}, 3600, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (target.withinDistance(frenzy, 5)) {
						delayHit(
								npc,
								0,
								target,
								getMeleeHit(
										npc,
										getMaxHit(npc, Utils.random(900, 1800),
												NPCCombatDefinitions.MELEE,
												target)));
					}
				}, 4800, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (target.withinDistance(frenzy, 5)) {
						delayHit(
								npc,
								0,
								target,
								getMeleeHit(
										npc,
										getMaxHit(npc, Utils.random(900, 1800),
												NPCCombatDefinitions.MELEE,
												target)));
					}
				}, 6000, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (target.withinDistance(frenzy, 5)) {
						delayHit(
								npc,
								0,
								target,
								getMeleeHit(
										npc,
										getMaxHit(npc, Utils.random(900, 1800),
												NPCCombatDefinitions.MELEE,
												target)));
					}
				}, 6000, TimeUnit.MILLISECONDS);
	}

	public void howl(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		helwyr.setNextAnimation(new Animation(28213));
		helwyr.setNextGraphics(new Graphics(6127));
		int randomX1 = Utils.random(HelwyrInstanceController.getX(42),
				HelwyrInstanceController.getX(61));
		int randomY1 = Utils.random(HelwyrInstanceController.getY(18),
				HelwyrInstanceController.getY(37));
		int randomX2 = Utils.random(HelwyrInstanceController.getX(42),
				HelwyrInstanceController.getX(61));
		int randomY2 = Utils.random(HelwyrInstanceController.getY(18),
				HelwyrInstanceController.getY(37));
		int spawnTileX1 = helwyr.getX() + Utils.random(5, 10);
		int spawnTileY1 = helwyr.getY() + Utils.random(5, 10);
		int spawnTileX2 = helwyr.getX() + Utils.random(5, 10);
		int spawnTileY2 = helwyr.getY() + Utils.random(5, 10);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					if (World.isTileFree(1, spawnTileX1, spawnTileY1,
							helwyr.getSize())) {
						CywirAlpha wolf1 = new CywirAlpha(22439, new WorldTile(
								spawnTileX1, spawnTileY1, 1), -1, true, false);
						wolf1.setNextAnimation(new Animation(23582));
						HelwyrInstanceController.Wolves.add(wolf1);
					} else {
						CywirAlpha wolf1 = new CywirAlpha(22439, new WorldTile(
								randomX1, randomY1, 1), -1, true, false);
						wolf1.setNextAnimation(new Animation(23582));
						HelwyrInstanceController.Wolves.add(wolf1);
					}
					if (World.isTileFree(1, spawnTileX2, spawnTileY2,
							helwyr.getSize())) {
						CywirAlpha wolf2 = new CywirAlpha(22439, new WorldTile(
								spawnTileX2, spawnTileY2, 1), -1, true, false);
						wolf2.setNextAnimation(new Animation(23582));
						HelwyrInstanceController.Wolves.add(wolf2);
					} else {
						CywirAlpha wolf2 = new CywirAlpha(22439, new WorldTile(
								randomX2, randomY2, 1), -1, true, false);
						wolf2.setNextAnimation(new Animation(23582));
						HelwyrInstanceController.Wolves.add(wolf2);
					}
				}, 600, TimeUnit.MILLISECONDS);
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Helwyr helwyr = (Helwyr) npc;
		switch (helwyr.getPhase()) {
		case 1:
			nature(npc, target);
			break;
		case 2:
			bite(npc, target);
			break;
		case 3:
			bite(npc, target);
			break;
		case 4:
			bite(npc, target);
			break;
		case 5:
			bleed(npc, target);
			break;
		case 6:
			bite(npc, target);
			break;
		case 7:
			bite(npc, target);
			break;
		case 8:
			bite(npc, target);
			break;
		case 9:
			frenzy(npc, target);
			break;
		case 10:
			bite(npc, target);
			break;
		case 11:
			bite(npc, target);
			break;
		case 12:
			bite(npc, target);
			break;
		case 13:
			howl(npc, target);
			break;
		case 14:
			bite(npc, target);
			break;
		case 15:
			bite(npc, target);
			break;
		case 16:
			bite(npc, target);
			break;
		case 17:
			natureSecond(npc, target);
			break;
		case 18:
			bite(npc, target);
			break;
		case 19:
			bite(npc, target);
			break;
		case 20:
			bite(npc, target);
			break;
		case 21:
			bleed(npc, target);
			break;
		case 22:
			bite(npc, target);
			break;
		case 23:
			bite(npc, target);
			break;
		case 24:
			bite(npc, target);
			break;
		case 25:
			frenzy(npc, target);
			break;
		case 26:
			bite(npc, target);
			break;
		case 27:
			bite(npc, target);
			break;
		case 28:
			bite(npc, target);
			break;
		case 29:
			howl(npc, target);
			break;
		case 30:
			bite(npc, target);
			break;
		case 31:
			bite(npc, target);
			break;
		case 32:
			bite(npc, target);
			break;
		}
		helwyr.nextPhase();
		if (helwyr.getPhase() < 0 || helwyr.getPhase() > 32)
			helwyr.setPhase(0);
		return npc.getAttackSpeed();
	}

}