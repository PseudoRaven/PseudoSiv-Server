package net.nocturne.game.player.controllers.trollinvasion;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class TrollMountain extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Mountain troll" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int hit = 0;
		hit = getMaxHit(npc, 250, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(13786));
		delayHit(npc, 2, target, getMeleeHit(npc, hit));
		return defs.getAttackGfx();
	}

}
