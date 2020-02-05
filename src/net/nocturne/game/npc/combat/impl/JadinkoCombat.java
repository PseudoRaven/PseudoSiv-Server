package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class JadinkoCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13820, 13821, 13822 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		boolean isDistanced = !Utils.isOnRange(npc, target, 0);
		if (target instanceof Player
				&& ((Player) target).getPrayer().isMeleeProtecting())
			isDistanced = true;
		if (isDistanced)
			magicAttack(npc, target);
		else
			meleeAttack(npc, target);
		return npc.getAttackSpeed();
	}

	private void magicAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(npc.getId() == 13820 ? 3031 : 3215));
		delayHit(
				npc,
				2,
				target,
				getMagicHit(npc,
						getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
	}

	private void meleeAttack(NPC npc, Entity target) {
		npc.setNextAnimation(new Animation(npc.getId() == 13820 ? 3009 : 3214));
		delayHit(
				npc,
				0,
				target,
				getMeleeHit(npc,
						getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
	}
}