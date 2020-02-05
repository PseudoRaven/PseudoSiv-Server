package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.*;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.smithing.DragonfireShield;
import net.nocturne.game.player.content.Combat;
import net.nocturne.utils.Utils;

public class KingBlackDragonCombat extends CombatScript {

	private static final String[] ADVERBS = { "poisonous", "freezing",
			"shocking" };
	private static final int[][] ATTACKS = { { 17786, 3441, 3442, 3443 },
			{ 17785, 3435, 3436, 3437 }, { 17783, 3438, 3439, 3440 },
			{ 17784, 3432, 3433, 3434 }, };

	@Override
	public Object[] getKeys() {

		return new Object[] { 50 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {

		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		boolean isDistanced = !Utils.isOnRange(npc.getX(), npc.getY(),
				npc.getSize(), target.getX(), target.getY(), target.getSize(),
				0);
		int style = Utils.random(isDistanced ? 4 : 5);

		if (style == 4) {// MELEE
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			return npc.getAttackSpeed();
		} else {
			int damage = getMaxHit(npc, 4500, NPCCombatDefinitions.MAGE, target);
			boolean negateDamage = target instanceof Familiar;
			if (damage > 200 && target instanceof Player) {
				Player player = (Player) target;
				// Rest should all be types of dragon-fire.
				boolean hasSuperPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.SUPER_FIRE_IMMUNITY);
				boolean hasRegularPot = player.getEffectsManager()
						.hasActiveEffect(EffectType.FIRE_IMMUNITY);
				boolean hasShield = Combat.hasAntiDragProtection(target);
				negateDamage = handleNegateDamage(style, player, hasSuperPot,
						hasRegularPot, hasShield);
			}
			if (negateDamage)
				damage *= 0.30915576694411414982164090368609;

			if (style == 1 && Utils.random(5) == 0)
				EffectsManager.makePoisoned(target, 100);

			else if (style == 2 && damage > 200)
				target.setBoundDelay(5, true);

			else if (style == 3 && damage > 200 && target instanceof Player) {
				Player player = ((Player) target);
				int drain = Utils.random(1, 2);
				for (int skill = 0; skill <= Skills.MAGIC; skill++)
					player.getSkills().set(skill,
							player.getSkills().getLevel(skill) - drain);
				player.getPackets().sendGameMessage(
						"You feel drained from the dragon's shocking breath.");
			}

			handleProjectile(npc, target, style, damage);
		}
		return npc.getAttackSpeed();
	}

	private void handleProjectile(NPC npc, Entity target, int style, int damage) {

		final int[] ATTACK_DATA = ATTACKS[style];
		final Animation ATTACK_ANIM = new Animation(ATTACK_DATA[0]);
		npc.setNextAnimation(ATTACK_ANIM);
		Projectile projectile = World.sendProjectile(npc, target, false, true,
				-1, ATTACK_DATA[2], 60, 41, (style == 1 || style == 2) ? 30
						: 40, 2, 0, 0);
		npc.setNextGraphics(new Graphics(ATTACK_DATA[1], 0, projectile
				.getStartHeight()));
		delayHit(npc,
				Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
				target, getMagicHit(npc, damage));
		target.setNextGraphics(new Graphics(ATTACK_DATA[3], projectile
				.getEndTime(), 0));
	}

	private boolean handleNegateDamage(int style, Player player,
			boolean hasSuperPot, boolean hasRegularPot, boolean hasShield) {

		boolean negateDamage;
		if (style == 0) {
			negateDamage = hasRegularPot && hasShield || hasSuperPot;
			if (hasSuperPot)
				player.getPackets()
						.sendGameMessage(
								"Your potion fully protects you from the dragon's fiery breath.");
		} else {
			boolean hasPrayer = player.getPrayer().isMageProtecting();
			negateDamage = hasShield || hasRegularPot && hasPrayer;
			if (hasShield)
				player.getPackets().sendGameMessage(
						"Your shield absorbs some of the dragon's "
								+ ADVERBS[style - 1] + " breath!");
		}
		DragonfireShield.chargeDFS(player, false);
		return negateDamage;
	}
}
