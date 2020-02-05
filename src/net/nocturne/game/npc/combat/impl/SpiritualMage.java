package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class SpiritualMage extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6257, 6221, 6278 };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);
		npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		delayHit(npc, 2, target, getMagicHit(npc, damage));
		if (damage > 0) {
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					target.setNextGraphics(new Graphics(defs
							.getAttackProjectile()));
				}
			}, 2);
		}
		return npc.getAttackSpeed();
	}
}
