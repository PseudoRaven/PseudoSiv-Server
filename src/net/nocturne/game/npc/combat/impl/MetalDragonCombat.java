package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.smithing.DragonfireShield;
import net.nocturne.game.player.content.Combat;
import net.nocturne.utils.Utils;

public class MetalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon",
				"Adamant dragon", "Rune dragon", "Celestial dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (Utils.random(2) == 0) {
			int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);
			if (damage > 200 && target instanceof Player) {
				Player player = (Player) target;
				boolean hasSuperPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.SUPER_FIRE_IMMUNITY);
				boolean hasRegularPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.FIRE_IMMUNITY);
				boolean hasShield = Combat.hasAntiDragProtection(target);
				if (hasSuperPot) {
					damage = 0;
					player.getPackets()
							.sendGameMessage(
									"Your potion fully protects you from the dragon's fiery breath.");
				} else {
					if (hasRegularPot)
						damage *= 0.5;
					if (hasShield) {
						player.getPackets()
								.sendGameMessage(
										"Your shield absorbs some of the dragon's fiery breath!");
						damage = hasRegularPot ? 0 : damage / 2;
					}
				}
				DragonfireShield.chargeDFS(player, false);
			}
			npc.setNextAnimation(new Animation(13160));
			Projectile projectile = World.sendProjectileNew(npc, target, 2464,
					28, 16, 35, 2, 16, 0);
			delayHit(npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					target, getRegularHit(npc, damage));
		} else {
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
		}
		return npc.getAttackSpeed();
	}

}
