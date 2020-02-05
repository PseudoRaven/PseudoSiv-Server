package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.utils.Utils;

public class SwarmCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 411 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		delayHit(npc, 0, target, getRegularHit(npc, Utils.random(100)));
		return 6;
	}
}
