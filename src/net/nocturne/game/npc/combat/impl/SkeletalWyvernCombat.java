package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class SkeletalWyvernCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 3068, 3069, 3070, 3071 };
	}

	private static boolean hasShield(Entity target) {
		if (!(target instanceof Player))
			return true;
		int shieldId = ((Player) target).getEquipment().getShieldId();
		return shieldId == 2890 || shieldId == 9731 || shieldId == 20436
				|| shieldId == 20438 || shieldId == 18691
				|| (shieldId >= 11283 && shieldId <= 12285);
	}

	@Override
	public int attack(NPC npc, Entity target) {
		int attackStyle = Utils.random(Utils.isOnRange(npc.getX(), npc.getY(),
				npc.getSize(), target.getX(), target.getY(), target.getSize(),
				0) ? 3 : 2);
		switch (attackStyle) {
		case 0:
			npc.setNextAnimation(new Animation(1592));
			npc.setNextGraphics(new Graphics(501));
			target.setNextGraphics(new Graphics(502));
			if (Utils.random(10) == 0)
				target.setBoundDelay(8);
			delayHit(
					npc,
					1,
					target,
					getRegularHit(npc,
							Utils.random(hasShield(target) ? 200 : 2100)));
			break;
		case 1:
			npc.setNextAnimation(new Animation(1593));
			npc.setNextGraphics(new Graphics(499));
			delayHit(
					npc,
					1,
					target,
					getRangedHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			break;
		case 2:
			npc.setNextAnimation(new Animation(1589 + Utils.random(2)));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		}
		return npc.getAttackSpeed();
	}

}
