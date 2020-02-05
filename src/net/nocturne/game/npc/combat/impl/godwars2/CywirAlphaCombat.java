package net.nocturne.game.npc.combat.impl.godwars2;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars2.helwyr.CywirAlpha;
import net.nocturne.utils.Utils;

public class CywirAlphaCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22439 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		CywirAlpha wolf = (CywirAlpha) npc;
		wolf.setNextAnimation(new Animation(23578));
		delayHit(
				npc,
				0,
				target,
				getMeleeHit(
						npc,
						getMaxHit(npc, Utils.random(200, 792),
								NPCCombatDefinitions.MELEE, target)));
		return npc.getAttackSpeed();
	}

}