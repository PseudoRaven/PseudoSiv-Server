package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.agoroth.Agoroth;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class AgorothCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 19332 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Utils.isOnRange(npc.getX(), npc.getY(),
				npc.getSize(), target.getX(), target.getY(), target.getSize(),
				1) ? (Utils.random(1, 3) == 1 ? 1 : 0) : 1;
		Agoroth main = (Agoroth) npc;
		if (main.canSpawnTentacles())
			main.spawnTentacles();
		switch (attackStyle) {
		case 0:
			npc.setNextFaceEntity(target);
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(npc, 0, target, getMeleeHit(npc, Utils.random(3500, 4250)));
			break;
		case 1:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			int attack = Utils.random(15);
			if (attack == 1) { // multi-hit spell
				target.setNextGraphics(new Graphics(1026));
				delayHit(npc, 2, target,
						getMagicHit(npc, Utils.random(500, 800)),
						getMagicHit(npc, Utils.random(500, 800)),
						getMagicHit(npc, Utils.random(500, 800)),
						getMagicHit(npc, Utils.random(500, 800)));
			} else if ((attack >= 2 && attack <= 5) || attack >= 11) { // regular
																		// magic
																		// attack
				npc.setNextGraphics(new Graphics(15));
				npc.getPossibleTargets()
						.stream()
						.forEachOrdered(
								t -> {
									t.setNextGraphics(new Graphics(2702));
									delayHit(
											npc,
											2,
											t,
											getMagicHit(npc,
													Utils.random(2000, 2300)));
								});
			} else if (attack >= 6 && attack <= 7) { // electric shock
				Projectile projectile = World.sendProjectileNew(npc, target,
						135, 140, 35, 35, 5, 16, 0);
				int endTime = Utils.projectileTimeToCycles(projectile
						.getEndTime()) - 1;
				delayHit(npc, endTime, 1740, target,
						getMagicHit(npc, Utils.random(3500, 4500)));
				target.getEffectsManager().startEffect(
						new Effect(EffectType.STUNNED, 10));
				if (target instanceof Player)
					((Player) target).getPackets().sendGameMessage(
							"You have been stunned by the electricity!");
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						npc.getPossibleTargets()
								.stream()
								.filter(t -> t.withinDistance(target, 3)
										&& t != target)
								.forEachOrdered(
										t -> {
											Projectile projectile = World
													.sendProjectileNew(target,
															t, 135, 35, 35, 35,
															5, 16, 0);
											int endTime = Utils
													.projectileTimeToCycles(projectile
															.getEndTime()) - 1;
											delayHit(
													npc,
													endTime,
													1740,
													t,
													getMagicHit(npc, Utils
															.random(3000, 4000)));
											t.getEffectsManager().startEffect(
													new Effect(
															EffectType.STUNNED,
															8));
											if (t instanceof Player)
												((Player) t)
														.getPackets()
														.sendGameMessage(
																"You have been stunned by the electricity!");
										});
					}
				}, endTime);
			} else if (attack == 8) { // earthquake atack
				npc.setNextGraphics(new Graphics(4814));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextGraphics(new Graphics(4815));
						npc.getPossibleTargets()
								.stream()
								.forEachOrdered(
										t -> {
											delayHit(
													npc,
													1,
													4810,
													t,
													getMeleeHit(npc,
															Utils.random(5000)));
											t.getEffectsManager().startEffect(
													new Effect(
															EffectType.STUNNED,
															2));
										});
					}
				}, 1);
			} else if (attack == 9) { // poison attack
				npc.getPossibleTargets()
						.stream()
						.forEachOrdered(
								t -> {
									if (!t.getEffectsManager().hasActiveEffect(
											EffectType.POISON)) {
										t.setNextGraphics(new Graphics(4817));
										EffectsManager.makePoisoned(t, 10);
									}
								});
			} else if (attack == 10) { // powerful magic attack
				npc.setNextGraphics(new Graphics(15));
				npc.getPossibleTargets()
						.stream()
						.forEachOrdered(
								t -> {
									target.setNextGraphics(new Graphics(2702));
									delayHit(
											npc,
											2,
											t,
											getMagicHit(npc,
													Utils.random(3500, 5000)));
								});
			}
			break;
		}
		return 10;
	}
}