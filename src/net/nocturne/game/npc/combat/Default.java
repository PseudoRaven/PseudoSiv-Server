package net.nocturne.game.npc.combat;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.familiar.Familiar.SpecialAttack;
import net.nocturne.utils.Utils;

public class Default extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Default" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		if (!npc.getDefinitions().hasAttackOption())
			return -1;
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = npc.getAttackStyle();
		if (npc instanceof Familiar) {
			Familiar familiar = (Familiar) npc;
			if (familiar.getSpecialAttack() == SpecialAttack.ENTITY
					&& familiar.hasSpecialOn())
				if (familiar.submitSpecial(target))
					return npc.getAttackSpeed();
		}
		if (attackStyle == NPCCombatDefinitions.MELEE) {
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(
							npc,
							getMaxHit(npc, npc.getMaxHit(attackStyle),
									attackStyle, target)));
		} else {
			int damage = getMaxHit(npc, attackStyle, target);
			int delay;

			int projectileId = defs.getAttackProjectile();

			if (projectileId == -1 && defs.getAttackGfx() == -1
					&& attackStyle == NPCCombatDefinitions.MAGE)
				projectileId = 2730; // default gfx for magic npcs(uses it if
										// none setted)
			if (projectileId != -1) {
				delay = Utils.projectileTimeToCycles(World.sendProjectileNew(
						npc, target, projectileId, 40, 39, 30, 2, 16, 5)
						.getEndTime()) - 1;

			} else
				delay = 2;
			delayHit(
					npc,
					delay,
					target,
					attackStyle == NPCCombatDefinitions.RANGE ? getRangedHit(
							npc, damage) : getMagicHit(npc, damage));
		}
		if (defs.getAttackGfx() != -1)
			npc.setNextGraphics(new Graphics(defs.getAttackGfx(), 0,
					attackStyle == NPCCombatDefinitions.RANGE ? 100 : 0));
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		return npc.getAttackSpeed();
	}
}
