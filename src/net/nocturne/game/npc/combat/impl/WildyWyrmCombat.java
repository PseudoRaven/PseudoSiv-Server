package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class WildyWyrmCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3334, 20630 };
	}

	public static void attackMageTarget(final WorldTile tile,
			final List<Player> arrayList, Entity fromEntity, final NPC npc,
			Entity t, final int projectile, final int gfx, final int damage) {
		if (damage < 20)
			return;
		final Entity target = t == null ? KalphiteQueenCombat.getTarget(
				arrayList, fromEntity, npc) : t;
		if (target == null)
			return;

		if (target instanceof Player)
			arrayList.add((Player) target);
		final Projectile proj = World.sendProjectileNew(fromEntity, target,
				projectile, fromEntity == npc ? 100 : 20, 30, 30, 3, 0, 0);
		int endTime = Utils.projectileTimeToCycles(proj.getEndTime()) - 1;
		delayHit(npc, endTime, target, getMagicHit(npc, damage));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.setNextGraphics(new Graphics(gfx, 0, 100));
				attackMageTarget(tile, arrayList, target, npc, null,
						projectile, gfx, damage / 2);
			}
		}, endTime);
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final List<Entity> possibleTargets = npc.getPossibleTargets();
		int size = npc.getSize();
		for (Entity t : possibleTargets) {
			if (Utils.colides(t.getX(), t.getY(), t.getSize(), npc.getX(),
					npc.getY(), size))
				delayHit(npc, 0, t, getRegularHit(npc, Utils.random(500) + 600));
		}
		int attackStyle = Utils.random(Utils.isOnRange(npc.getX(), npc.getY(),
				size, target.getX(), target.getY(), target.getSize(), 0) ? 3
				: 2);
		switch (attackStyle) {
		case 0: // magic
			npc.setNextAnimation(new Animation(12794));
			attackMageTarget(new WorldTile(target), new ArrayList<Player>(),
					npc, npc, target, 2731, 2738, Utils.random(300) + 300);
			break;
		case 1:// range
			final WorldTile tile = new WorldTile(target);
			npc.setNextAnimation(new Animation(12794));
			final Projectile projectile = World.sendProjectileNew(npc, target,
					2735, 100, 25, 35, 3, 10, 0);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (Entity t : possibleTargets) {
						if (!t.withinDistance(tile, 5))
							continue;
						Projectile p = World.sendProjectileNew(tile, t, 2735,
								0, 16, projectile.getEndTime(), 2, 10, 0);
						delayHit(
								npc,
								Utils.projectileTimeToCycles(p.getEndTime()) - 1,
								t, getRangedHit(npc, Utils.random(1001) + 3000));
					}
				}
			});
			break;
		default: // melee
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			for (Entity t : possibleTargets) {
				if (t.withinDistance(target, 1))
					delayHit(
							npc,
							0,
							t,
							getMeleeHit(
									npc,
									getMaxHit(npc, 2000,
											NPCCombatDefinitions.MELEE, t)));
			}
			break;
		}
		return npc.getAttackSpeed();
	}

}
