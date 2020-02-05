package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;

public class AbbysalTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7350, 7349 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		int damage = 0;
		damage = getMaxHit(npc, NPCCombatDefinitions.MELEE, target);
		npc.setNextAnimation(new Animation(7980));
		npc.setNextGraphics(new Graphics(1490));

		if (target instanceof Player) { // cjay failed dragonkk saved the day
			Player player = (Player) target;
			if (damage > 0 && player.getPrayer().getPoints() > 0)
				player.getPrayer().drain(damage / 2);
		}
		delayHit(npc, 0, target, getMeleeHit(npc, damage));
		return npc.getAttackSpeed();
	}
}
