package net.nocturne.game.npc.combat.impl.dung;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.dungeonnering.FleshspoilerHaasghenahk;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class FleshspoilerHaasghenahkCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 11925, 11895 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final FleshspoilerHaasghenahk boss = (FleshspoilerHaasghenahk) npc;

		for (Entity t : npc.getPossibleTargets()) {
			if (Utils.colides(t.getX(), t.getY(), t.getSize(), npc.getX(),
					npc.getY(), npc.getSize()))
				delayHit(
						npc,
						0,
						t,
						getRegularHit(npc,
								getMaxHit(npc, NPCCombatDefinitions.MELEE, t)));
		}
		if (boss.isSecondStage())
			return 0;
		boolean magicOnly = boss.canUseMagicOnly();
		if (magicOnly || Utils.random(5) == 0) {
			if (magicOnly) {
				if (target instanceof Player) {
					Player player = (Player) target;
					if (player.getPrayer().isMageProtecting()
							&& Utils.random(3) == 0)
						boss.setUseMagicOnly(false);
				}
			}
			npc.setNextAnimation(new Animation(14463));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
		} else {
			npc.setNextAnimation(new Animation(13320));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
		}
		return 6;
	}
}
