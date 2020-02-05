package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class KreearraCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6222 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		if (!npc.isUnderCombat()) {
			npc.setNextAnimation(new Animation(17396));
			delayHit(
					npc,
					1,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			return npc.getAttackSpeed();
		}
		npc.setNextAnimation(new Animation(17397));
		boolean isMagic = Utils.random(2) == 0;
		for (Entity t : npc.getPossibleTargets()) {
			Projectile projectile = World.sendProjectileNew(npc, t,
					isMagic ? 3388 : 3389, 41, 16, 30, 2, 0, 0);
			if (isMagic)
				delayHit(
						npc,
						Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
						t,
						getMagicHit(npc,
								getMaxHit(npc, NPCCombatDefinitions.MAGE, t)));
			else {
				delayHit(
						npc,
						Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
						t,
						getRangedHit(npc,
								getMaxHit(npc, NPCCombatDefinitions.RANGE, t)));
				for (int c = 0; c < 10; c++) {
					int dir = Utils.random(Utils.DIRECTION_DELTA_X.length);
					if (World.checkWalkStep(target.getPlane(), target.getX(),
							target.getY(), dir, 1)) {
						if (t instanceof Player)
							((Player) t).stopAll();
						t.setNextWorldTile(new WorldTile(target.getX()
								+ Utils.DIRECTION_DELTA_X[dir], target.getY()
								+ Utils.DIRECTION_DELTA_Y[dir], target
								.getPlane()));
						break;
					}
				}
			}
		}
		return npc.getAttackSpeed();
	}
}
