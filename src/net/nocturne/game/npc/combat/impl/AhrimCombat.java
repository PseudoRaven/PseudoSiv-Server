package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.Utils;

public class AhrimCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 2025 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);
		if (damage != 0 && target instanceof Player && Utils.random(6) == 0) {
			target.setNextGraphics(new Graphics(400, 0, 100));
			Player targetPlayer = (Player) target;
			int currentLevel = targetPlayer.getSkills().getLevel(
					Skills.STRENGTH);
			targetPlayer.getSkills().set(Skills.STRENGTH,
					currentLevel < 5 ? 0 : currentLevel - 5);
		}
		Projectile projectile = World.sendProjectileNew(target, npc,
				defs.getAttackProjectile(), 41, 16, 35, 2, 16, Utils.random(5));
		npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
		delayHit(npc, Utils.projectileTimeToCycles(projectile.getEndTime()),
				target, getMagicHit(npc, damage));
		return npc.getAttackSpeed();
	}
}
