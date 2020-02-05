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
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars2.gregorovic.Gregorovic;
import net.nocturne.game.npc.godwars2.gregorovic.Spirit;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.utils.Utils;

public class GregorovicCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22443 };
	}

	public void shurikenAttack(NPC npc, Entity target) {
		Gregorovic gregorovic = (Gregorovic) npc;
		gregorovic.setNextAnimation(new Animation(28228));
		Projectile projectile = World.sendProjectileNew(npc, target, 6132, 40,
				30, 125, 1, 0, 0);
		delayHit(
				npc,
				Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
				target,
				getRangedHit(
						npc,
						getMaxHit(npc, Utils.random(500, 816),
								NPCCombatDefinitions.RANGE, target)));
	}

	public void trickKnife(NPC npc, Entity target) {// TODO
		Gregorovic gregorovic = (Gregorovic) npc;
		gregorovic.setNextAnimation(new Animation(28229));
		World.sendProjectileNew(npc, target, 6135, 30, 40, 50, 0.5, 0, 0);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.sendProjectileNew(target, npc, 6135, 30, 40, 50, 0.5, 0,
						0);
			}
		}, Utils.projectileTimeToCycles(new Projectile(npc, target, false,
				false, 1, 6135, 30, 40, 0, 0, 0, 0).getEndTime()),
				TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.sendProjectileNew(target, npc, 6135, 30, 40, 50, 0.5, 0,
						0);
			}
		}, Utils.projectileTimeToCycles(new Projectile(target, npc, false,
				false, 1, 6135, 30, 40, 0, 0, 0, 0).getEndTime()),
				TimeUnit.MILLISECONDS);

	}

	public void summonSpirit(NPC npc, Entity target) {
		Gregorovic gregorovic = (Gregorovic) npc;
		int randomX1 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY1 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		gregorovic.setNextForceTalk(new ForceTalk("RISE, CHILD!"));
		Spirit spirit = new Spirit(22450, new WorldTile(randomX1, randomY1, 1),
				-1, true, false);
		spirit.setNextAnimation(new Animation(24724));
		spirit.setForceWalk(gregorovic);
		if ((spirit.getX() == gregorovic.getX() + 1 || spirit.getX() == gregorovic
				.getX() - 1)
				&& (spirit.getY() == gregorovic.getY() + 1 || spirit.getY() == gregorovic
						.getY() - 1))
			System.out.println("Matched.");
	}

	public void glaiveThrow(NPC npc, Entity target) {
		Gregorovic gregorovic = (Gregorovic) npc;
		gregorovic.setNextAnimation(new Animation(28494));
		int randomX1 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY1 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX2 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY2 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX3 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY3 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX4 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY4 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX5 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY5 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX6 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY6 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX7 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY7 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX8 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY8 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX9 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY9 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX10 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY10 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX11 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY11 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX12 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY12 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX13 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY13 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX14 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY14 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX15 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY15 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX16 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY16 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX17 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY17 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX18 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY18 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX19 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY19 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX20 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY20 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX21 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY21 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX22 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY22 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX23 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY23 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX24 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY24 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX25 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY25 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		GameExecutorManager.slowExecutor.schedule(() -> {
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX1, randomY1, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX2, randomY2, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX3, randomY3, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX4, randomY4, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX5, randomY5, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX5, randomY5, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX6, randomY6, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX7, randomY7, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX8, randomY8, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX9, randomY9, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX10, randomY10, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX11, randomY11, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX12, randomY12, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX13, randomY13, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX14, randomY14, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX15, randomY15, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX16, randomY16, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX17, randomY17, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX18, randomY18, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX19, randomY19, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX20, randomY20, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX21, randomY21, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX22, randomY22, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX23, randomY23, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX24, randomY24, 1));
			World.sendGraphics(target, new Graphics(6139), new WorldTile(
					randomX25, randomY25, 1));
		}, 600, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor
				.schedule(
						() -> {
							if ((target.getX() == randomX1
									|| target.getX() == randomX2
									|| target.getX() == randomX3
									|| target.getX() == randomX4
									|| target.getX() == randomX5
									|| target.getX() == randomX6
									|| target.getX() == randomX7
									|| target.getX() == randomX8
									|| target.getX() == randomX9
									|| target.getX() == randomX10
									|| target.getX() == randomX11
									|| target.getX() == randomX12
									|| target.getX() == randomX13
									|| target.getX() == randomX14
									|| target.getX() == randomX15
									|| target.getX() == randomX16
									|| target.getX() == randomX17
									|| target.getX() == randomX18
									|| target.getX() == randomX19
									|| target.getX() == randomX20
									|| target.getX() == randomX21
									|| target.getX() == randomX22
									|| target.getX() == randomX23
									|| target.getX() == randomX24 || target
									.getX() == randomX25)
									&& (target.getY() == randomY1
											|| target.getY() == randomY2
											|| target.getY() == randomY3
											|| target.getY() == randomY4
											|| target.getY() == randomY5
											|| target.getY() == randomY6
											|| target.getY() == randomY7
											|| target.getY() == randomY8
											|| target.getY() == randomY9
											|| target.getY() == randomY10
											|| target.getY() == randomY11
											|| target.getY() == randomY12
											|| target.getY() == randomY13
											|| target.getY() == randomY14
											|| target.getY() == randomY15
											|| target.getY() == randomY16
											|| target.getY() == randomY17
											|| target.getY() == randomY18
											|| target.getY() == randomY19
											|| target.getY() == randomY20
											|| target.getY() == randomY21
											|| target.getY() == randomY22
											|| target.getY() == randomY23
											|| target.getY() == randomY24 || target
											.getY() == randomY25)) {
								target.applyHit(new Hit(target, Utils.random(
										1000, 1700), HitLook.MAGIC_DAMAGE));
							}
						}, 4200, TimeUnit.MILLISECONDS);
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Gregorovic gregorovic = (Gregorovic) npc;
		switch (gregorovic.getPhase()) {
		case 0:
			shurikenAttack(npc, target);
			break;
		case 1:
			shurikenAttack(npc, target);
			break;
		case 2:
			shurikenAttack(npc, target);
			break;
		case 3:
			glaiveThrow(npc, target);
			break;
		case 4:
			trickKnife(npc, target);
			break;
		}
		gregorovic.nextPhase();
		if (gregorovic.getPhase() < 0 || gregorovic.getPhase() > 4)
			gregorovic.setPhase(0);
		return npc.getAttackSpeed();
	}

}