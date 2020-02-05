package net.nocturne.game.npc.combat.impl.dung;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.utils.Utils;

public class DeathslingerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 11208, 11210, 11212, 11214, 11216, 11218, 11220,
				11222, 11224, 11226 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		npc.getCombatDefinitions();
		boolean usingSpecial = false;
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			usingSpecial = familiar.hasSpecialOn();
		}
		int tier = (npc.getId() - 11208) / 2;

		int damage = 0;
		if (usingSpecial) {
			npc.setNextGraphics(new Graphics(2447));
			damage = getMaxHit(
					npc,
					(int) (npc.getMaxHit(NPCCombatDefinitions.RANGE) * (1.05 * tier)),
					NPCCombatDefinitions.RANGE, target);
			if (Utils.random(11 - tier) == 0)
				EffectsManager.makePoisoned(target, 100);
		} else
			damage = getMaxHit(npc, NPCCombatDefinitions.RANGE, target);
		npc.setNextAnimation(new Animation(13615));
		World.sendProjectile(npc, target, 2448, 41, 16, 41, 35, 16, 0);
		delayHit(npc, 2, target, getRangedHit(npc, damage));
		return npc.getAttackSpeed();
	}
}
