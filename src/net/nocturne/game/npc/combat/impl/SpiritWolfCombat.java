package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;

public class SpiritWolfCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6829, 6828 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			npc.setNextAnimation(new Animation(8293));
			npc.setNextGraphics(new Graphics(1334));
			World.sendProjectileNew(npc, target, 1333, 34, 16, 35, 2, 10, 0);
			if (target instanceof NPC) {
				if (target.getSize() < 2)
					target.setAttackedByDelay(3000);// three seconds
				else
					familiar.getOwner()
							.getPackets()
							.sendGameMessage(
									"Your familiar cannot scare that monster.");
			} else if (target instanceof Player)
				familiar.getOwner()
						.getPackets()
						.sendGameMessage("Your familiar cannot scare a player.");
			else if (target instanceof Familiar)
				familiar.getOwner()
						.getPackets()
						.sendGameMessage(
								"Your familiar cannot scare other familiars.");
		} else {
			npc.setNextAnimation(new Animation(6829));
			delayHit(
					npc,
					1,
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
		}
		return npc.getAttackSpeed();
	}

}
