package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.utils.Utils;

public class MysteriousShadeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Mysterious shade" };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		final boolean rangeAttack = Utils.random(1) == 0;

		npc.setNextAnimation(new Animation(rangeAttack ? 13396 : 13398));
		npc.setNextGraphics(new Graphics(rangeAttack ? 2514 : 2515));
		Projectile projectile = World.sendProjectileNew(npc, target,
				rangeAttack ? 2510 : 2511, 18, 18, 25, 3, 0, 0);
		if (rangeAttack)
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					target,
					getRangedHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
		else
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
		target.setNextGraphics(new Graphics(rangeAttack ? 2512 : 2513,
				projectile.getEndTime(), 0));
		return npc.getAttackSpeed();
	}
}
