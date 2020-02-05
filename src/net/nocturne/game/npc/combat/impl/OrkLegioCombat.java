package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.utils.Utils;

public class OrkLegioCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Ork legion" };
	}

	public String[] messages = { "For Bork!", "Die Human!", "To the attack!",
			"All together now!" };

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions cdef = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(cdef.getAttackEmote()));
		if (Utils.random(3) == 0)
			npc.setNextForceTalk(new ForceTalk(messages[Utils
					.random(messages.length > 3 ? 3 : 0)]));
		delayHit(npc, 0, target, getMeleeHit(npc, cdef.getMaxHit()));
		return cdef.getAttackGfx();
	}
}