package net.nocturne.game.player.controllers.trollinvasion;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.utils.Utils;

public class TrollGeneral extends CombatScript {

	@Override
	public Object[] getKeys() {

		return new Object[] { "Troll general", 12291 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		npc.setNextAnimation(new Animation(13788));

		hit = getMaxHit(npc, Utils.random(300, 400),
				NPCCombatDefinitions.MELEE, target);
		delayHit(npc, 1, target, getMeleeHit(npc, hit));
		return defs.getAttackGfx();
	}

}
