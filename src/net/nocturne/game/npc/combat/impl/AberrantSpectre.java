package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.World;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.slayer.Slayer;
import net.nocturne.utils.Utils;

public class AberrantSpectre extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Aberrant spectre" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions def = npc.getCombatDefinitions();
		if (!Slayer.hasNosepeg(target)) {
			Player targetPlayer = (Player) target;
			if (!(targetPlayer.getPrayer().isMageProtecting()
					|| targetPlayer.getEquipment().containsOneItem(
							ItemIdentifiers.SLAYER_HELMET) || targetPlayer
					.getEquipment().containsOneItem(ItemIdentifiers.FACE_MASK))) {
				int randomSkill = Utils.random(0, 6);
				int currentLevel = targetPlayer.getSkills().getLevel(
						randomSkill);
				targetPlayer.getSkills().set(randomSkill,
						currentLevel < 5 ? 0 : currentLevel - 5);
				targetPlayer
						.getPackets()
						.sendGameMessage(
								"The smell of the abberrant spectre make you feel slightly weaker.",
								true);
			}
			delayHit(npc, 1, target,
					getMagicHit(npc, targetPlayer.getMaxHitpoints() / 10));
			// TODO player emote hands on ears
		} else
			delayHit(
					npc,
					1,
					target,
					getMagicHit(npc,
							getMaxHit(npc, npc.getAttackStyle(), target)));
		World.sendProjectile(npc, target, def.getAttackProjectile(), 18, 18,
				50, 25, 0, 0);
		npc.setNextAnimation(new Animation(def.getAttackEmote()));
		return npc.getAttackSpeed();
	}
}