package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.slayer.Slayer;
import net.nocturne.game.player.content.Combat;
import net.nocturne.utils.Utils;

public class CockatriceCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 1620 };
	}

	@Override
	public int attack(NPC npc, final Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		Projectile projectile = World.sendProjectileNew(npc, target, 1468, 34,
				14, 35, 3, 0, 0);
		int endTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
		if (!Slayer.hasReflectiveEquipment(target)) {
			Player targetPlayer = (Player) target;
			int randomSkill = Combat.COMBAT_SKILLS[Utils
					.random(Combat.COMBAT_SKILLS.length)];
			int currentLevel = targetPlayer.getSkills().getLevel(randomSkill);
			targetPlayer.getSkills().set(randomSkill,
					currentLevel < 3 ? 0 : currentLevel / 4);
			npc.setNextAnimation(new Animation(7766));
			npc.setNextGraphics(new Graphics(1467));
			delayHit(npc, endTime, target,
					getMagicHit(npc, targetPlayer.getMaxHitpoints() / 11));
		} else
			delayHit(
					npc,
					endTime,
					target,
					getMagicHit(npc,
							getMaxHit(npc, npc.getAttackStyle(), target)));
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return npc.getAttackSpeed();
	}
}
