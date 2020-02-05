package net.nocturne.game.npc.combat.impl.riseofthesix;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.riseofthesix.Karil;

public class KarilCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 18543 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Karil boss = (Karil) npc;
		Projectile projectile = World.sendProjectileNew(boss, target, 955, 5,
				10, 30, 0.9, 0, 0);
		int damage = getMaxHit(boss, 3000, NPCCombatDefinitions.RANGE, target);
		npc.setNextAnimation(new Animation(18232));
		delayHit(npc, 3, target, getRangedHit(npc, damage));
		return 7;
	}

}