package net.nocturne.game.player.controllers.trollinvasion;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class TrollRanger extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Troll ranger" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int damage = getMaxHit(npc, 300, NPCCombatDefinitions.RANGE, target);
		npc.setNextGraphics(new Graphics(850));
		npc.setNextAnimation(new Animation(2134));
		// World.sendProjectile(npc, target, 32, 34, 16, 30, 35, 16, 0);// 32
		delayHit(npc, 2, target, getRangedHit(npc, damage));
		return defs.getAttackGfx();
	}

}
