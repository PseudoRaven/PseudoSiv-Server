package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.others.Dreadnip;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class DreadNipCombat extends CombatScript {

	private String[] DREADNIP_ATTACK_MESSAGE = {
			"Your dreadnip stunned its target!",
			"Your dreadnip poisened its target!" };

	@Override
	public Object[] getKeys() {
		return new Object[] { 14416 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Dreadnip dreadNip = (Dreadnip) npc;
		if (dreadNip.getTicks() <= 3)
			return 0;
		npc.setNextAnimation(new Animation(-1));
		int attackStyle = Utils.random(3);
		switch (attackStyle) {
		case 0:
			break;
		case 1:
			int secondsDelay = 5 + Utils.random(4);
			target.setStunDelay((int) (secondsDelay / 0.6));
			if (target instanceof Player) {
				Player player = (Player) target;
				player.getActionManager().addActionDelay(secondsDelay);
			} else {
				NPC npcTarget = (NPC) target;
				npcTarget.getCombat().setCombatDelay(
						npcTarget.getCombat().getCombatDelay() + secondsDelay);
			}
			break;
		case 2:
			EffectsManager.makePoisoned(target, 108);
			break;
		}
		if (attackStyle != 0)
			dreadNip.getOwner().getPackets()
					.sendGameMessage(DREADNIP_ATTACK_MESSAGE[attackStyle - 1]);
		delayHit(
				npc,
				0,
				target,
				new Hit(npc, getMaxHit(npc, 550, NPCCombatDefinitions.MELEE,
						target), HitLook.REGULAR_DAMAGE));
		return 5;
	}
}
