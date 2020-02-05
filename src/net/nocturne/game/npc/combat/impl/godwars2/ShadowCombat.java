package net.nocturne.game.npc.combat.impl.godwars2;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars2.gregorovic.Shadow;
import net.nocturne.utils.Utils;

public class ShadowCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22444 };
	}

	public void shurikenAttack(NPC npc, Entity target) {
		Shadow shadow = (Shadow) npc;
		shadow.setNextAnimation(new Animation(28228));
		Projectile projectile = World.sendProjectileNew(npc, target, 6133, 40, 30, 125, 1, 0, 0);
		delayHit(npc, Utils.projectileTimeToCycles(projectile.getEndTime()) - 1, target,
				getRangedHit(npc, getMaxHit(npc, Utils.random(500, 816), NPCCombatDefinitions.RANGE, target)));
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Shadow shadow = (Shadow) npc;
		switch (shadow.getPhase()) {
		case 0:
			shurikenAttack(npc, target);
			break;
		}
		shadow.nextPhase();
		if (shadow.getPhase() < 0 || shadow.getPhase() > 0)
			shadow.setPhase(0);
		return npc.getAttackSpeed();
	}

}