package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.others.Bork;

public class BorkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bork" };
	}

	@Override
	public int attack(NPC npc, Entity target) {

		final NPCCombatDefinitions combatDefinitions = npc
				.getCombatDefinitions();
		Bork bork = (Bork) npc;
		if (npc.getHitpoints() <= (combatDefinitions.getHitpoints() * 0.6)
				&& !bork.isSpawnedMinions()) {
			bork.spawnMinions();
			return 0;
		}
		npc.setNextAnimation(new Animation(combatDefinitions.getAttackEmote()));
		delayHit(
				npc,
				0,
				target,
				getMeleeHit(npc,
						getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
		return npc.getAttackSpeed();
	}
}