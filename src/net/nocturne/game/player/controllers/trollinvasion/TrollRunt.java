package net.nocturne.game.player.controllers.trollinvasion;

import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class TrollRunt extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Troll runt", 7361, 7362 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = getMaxHit(npc, 200, NPCCombatDefinitions.MELEE, target);
		delayHit(npc, 2, target, getMeleeHit(npc, hit));

		return defs.getAttackGfx();
	}

}
