package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class GorakCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Gorak" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = getMaxHit(npc, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		delayHit(npc, 0, target, new Hit(npc, damage, HitLook.REGULAR_DAMAGE));
		return npc.getAttackSpeed();
	}
}
