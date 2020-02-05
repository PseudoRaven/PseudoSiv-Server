package net.nocturne.game.npc.combat.impl.riseofthesix;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.riseofthesix.Dharok;

public class DharokCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18540 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Dharok boss = (Dharok) npc;
		if (boss.isCharging) {
			return 0;
		}
		int damage = getMaxHit(npc, 2000, NPCCombatDefinitions.MELEE, target);
		if (npc.getHitpoints() <= 40000 && npc.getHitpoints() >= 30001) {
			damage = getMaxHit(npc, 3000, NPCCombatDefinitions.MELEE, target);
		}
		if (npc.getHitpoints() <= 30000 && npc.getHitpoints() >= 20001) {
			damage = getMaxHit(npc, 4000, NPCCombatDefinitions.MELEE, target);
		}
		if (npc.getHitpoints() <= 20000 && npc.getHitpoints() >= 10001) {
			damage = getMaxHit(npc, 5000, NPCCombatDefinitions.MELEE, target);
		}
		if (npc.getHitpoints() <= 10000 && npc.getHitpoints() >= 5001) {
			damage = getMaxHit(npc, 6000, NPCCombatDefinitions.MELEE, target);
		}
		if (npc.getHitpoints() <= 5000) {
			damage = getMaxHit(npc, 7000, NPCCombatDefinitions.MELEE, target);
		}
		npc.setNextAnimation(new Animation(npc.getCombatDefinitions()
				.getAttackEmote()));
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return 7;
	}

}
