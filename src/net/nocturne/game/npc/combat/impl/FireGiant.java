package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.utils.Utils;

public class FireGiant extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Fire giant", "Moss giant" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		boolean isDistance = !Utils.isOnRange(npc, target, 0);
		int style = Utils.random(isDistance ? 1 : 0, 2);
		switch (style) {
		case 0:// MELEE
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		case 1:// RANGED
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			Projectile projectile = World.sendProjectileNew(npc, target, 276,
					40, 30, 30, 3, 0, 5);
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					target,
					getRangedHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			break;
		}
		return npc.getAttackSpeed();
	}

}
