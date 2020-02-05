package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.smithing.DragonfireShield;
import net.nocturne.game.player.content.Combat;
import net.nocturne.utils.Utils;

public class MithrilDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Mithril dragon" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		switch (Utils.random(Utils.isOnRange(npc.getX(), npc.getY(),
				npc.getSize(), target.getX(), target.getY(), target.getSize(),
				0) ? 4 : 3)) {
		case 3: // melee
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		case 2: // magic
			npc.setNextAnimation(new Animation(13160));
			Projectile projectile = World.sendProjectileNew(npc, target, 2705,
					28, 16, 35, 2, 16, 0);
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()),
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
			break;
		case 1: // range
			npc.setNextAnimation(new Animation(13160));
			projectile = World.sendProjectileNew(npc, target, 16, 28, 16, 35,
					2, 16, 0);
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()),
					target,
					getRangedHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			break;
		case 0: // dragonfire
			int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, target);
			boolean negateDamage = target instanceof Familiar;
			if (damage > 200 && target instanceof Player) {
				Player player = (Player) target;
				// Rest should all be types of dragon-fire.

				boolean hasSuperPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.SUPER_FIRE_IMMUNITY);
				boolean hasRegularPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.FIRE_IMMUNITY);
				boolean hasShield = Combat.hasAntiDragProtection(target);
				boolean hasPrayer = player.getPrayer().isMageProtecting();
				negateDamage = hasRegularPot && hasShield || hasRegularPot
						&& hasPrayer || hasSuperPot;
				if (hasSuperPot)
					player.getPackets()
							.sendGameMessage(
									"Your potion fully protects you from the dragon's fiery breath.");
				else if (hasShield)
					player.getPackets()
							.sendGameMessage(
									"Your shield absorbs some of the dragon's fiery breath!");
				else if (hasPrayer)
					player.getPackets()
							.sendGameMessage(
									"Your prayers absorbs some of the dragon's fiery breath!");
				DragonfireShield.chargeDFS(player, false);
			}
			if (negateDamage)
				damage *= 0.30915576694411414982164090368609;// Just leave it
																// like this,
																// works fine.
			npc.setNextAnimation(new Animation(13164));
			npc.setNextGraphics(new Graphics(1, 0, 100));
			delayHit(npc, 1, target, getRegularHit(npc, damage));
			break;
		}

		return npc.getAttackSpeed();

	}

}
