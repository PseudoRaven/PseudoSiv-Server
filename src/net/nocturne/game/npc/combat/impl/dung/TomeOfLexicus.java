package net.nocturne.game.npc.combat.impl.dung;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public class TomeOfLexicus extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 9856, 9857, 9858 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		int type = npc.getId() - 9856;
		switch (type) {
		case 0:
			npc.setNextAnimation(new Animation(13479));
			delayHit(
					npc,
					0,
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
			break;
		case 1:
		case 2:
			boolean range_style = type == 1;
			npc.setNextAnimation(new Animation(13480));
			npc.setNextGraphics(new Graphics(range_style ? 2408 : 2424));
			World.sendProjectile(npc, target, range_style ? 2409 : 2425, 40,
					40, 54, 35, 5, 0);
			if (range_style)
				delayHit(
						npc,
						1,
						target,
						getRangedHit(
								npc,
								getMaxHit(npc, NPCCombatDefinitions.RANGE,
										target)));
			else
				delayHit(
						npc,
						1,
						target,
						getMagicHit(
								npc,
								getMaxHit(npc, NPCCombatDefinitions.MAGE,
										target)));
			target.setNextGraphics(new Graphics(range_style ? 2410 : 2426, 75,
					0));
			break;
		}
		return 4;
	}
}
