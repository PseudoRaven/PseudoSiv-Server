package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class DagannothKingCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2881, 2882, 2883 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		switch (npc.getId()) {
		case 2881:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					2,
					target,
					getRangedHit(
							npc,
							getMaxHit(npc, 1788, NPCCombatDefinitions.RANGE,
									target)));
			World.sendProjectileNew(npc, target, 6359, 100, 70, 55, 3, 1, 0);
			break;
		case 2882:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					2,
					target,
					getMagicHit(
							npc,
							getMaxHit(npc, 1788, NPCCombatDefinitions.MAGE,
									target)));
			npc.setNextGraphics(new Graphics(6355, 1, 150));
			World.sendProjectileNew(npc, target, 6356, 65, 50, 55, 1.4, 1, 1);
			break;
		case 2883:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					1,
					target,
					getMeleeHit(
							npc,
							getMaxHit(npc, 1788, NPCCombatDefinitions.MELEE,
									target)));
			break;
		}
		return npc.getAttackSpeed();
	}
}