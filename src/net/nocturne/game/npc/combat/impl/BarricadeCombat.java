package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;

public class BarricadeCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Barricade" };
	}

	/**
	 * empty
	 */
	@Override
	public int attack(NPC npc, Entity target) {
		return 0;
	}
}
