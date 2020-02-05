package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class ShiftCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3732, 3733, 3734, 3735, 3736, 3737, 3738, 3739,
				3740, 3741 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		delayHit(npc, 0, target,
				getMeleeHit(npc, getMaxHit(npc, npc.getAttackStyle(), target)));
		return npc.getAttackSpeed();
	}
}
