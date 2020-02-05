package net.nocturne.game.npc.combat.impl.dung;

import net.nocturne.game.*;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.dungeonnering.HopeDevourer;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class HopeDevourerCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 12886 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final HopeDevourer boss = (HopeDevourer) npc;
		final DungeonManager manager = boss.getManager();

		boolean stomp = false;
		for (Player player : manager.getParty().getTeam()) {
			if (Utils.colides(player.getX(), player.getY(), player.getSize(),
					npc.getX(), npc.getY(), npc.getSize())) {
				stomp = true;
				delayHit(
						npc,
						0,
						player,
						getRegularHit(
								npc,
								getMaxHit(npc, NPCCombatDefinitions.MELEE,
										player)));
			}
		}
		if (stomp) {
			npc.setNextAnimation(new Animation(14459));
			return 6;
		}

		if (Utils.random(10) == 0) {
			npc.setNextForceTalk(new ForceTalk("Grrrrrrrrrroooooooooaaaarrrrr"));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					npc.setNextAnimation(new Animation(14460));
					npc.setNextGraphics(new Graphics(2844, 30, 0));
					int healedDamage = 0;
					for (Entity t : npc.getPossibleTargets()) {
						Player player = (Player) t;
						int damage = (int) Utils.random(
								npc.getMaxHit(NPCCombatDefinitions.MAGE) * .85,
								npc.getMaxHit(NPCCombatDefinitions.MAGE));
						if (damage > 0
								&& player.getPrayer().isUsingProtectionPrayer()) {
							healedDamage += damage;
							player.getEffectsManager().startEffect(
									new Effect(EffectType.PROTECTION_DISABLED,
											8));
							t.setNextGraphics(new Graphics(2845, 75, 0));
							delayHit(npc, 0, t, getMagicHit(npc, damage));
						}
					}
					npc.heal(healedDamage);
				}
			}, 2);
			return 8;
		}

		if (!Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(),
				target.getX(), target.getY(), target.getSize(), 0))
			return 0;

		if (Utils.random(5) == 0) {
			npc.setNextAnimation(new Animation(14458));
			final int damage = (int) Utils.random(
					npc.getMaxHit(NPCCombatDefinitions.MELEE) * .85,
					npc.getMaxHit(NPCCombatDefinitions.MELEE));
			if (target instanceof Player) {
				Player player = (Player) target;
				player.getSkills()
						.set(Skills.DEFENCE,
								(int) (player.getSkills().getLevel(
										Skills.DEFENCE) - (damage * .05)));
			}
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			WorldTasksManager.schedule(new WorldTask() {
				private int ticks;
				private WorldTile tile;

				@Override
				public void run() {
					ticks++;
					if (ticks == 1) {
						if (target instanceof Player) {
							Player player = (Player) target;
							player.lock(2);
							player.stopAll();
						}
						byte[] dirs = Utils.getDirection(npc.getDirection());
						for (int distance = 2; distance >= 0; distance--) {
							tile = new WorldTile(new WorldTile(target.getX()
									+ (dirs[0] * distance), target.getY()
									+ (dirs[1] * distance), target.getPlane()));
							if (World.isFloorFree(tile.getPlane(), tile.getX(),
									tile.getY()) && manager.isAtBossRoom(tile))
								break;
							else if (distance == 0)
								tile = new WorldTile(target);
						}
						target.faceEntity(boss);
						target.setNextAnimation(new Animation(10070));
						target.setNextForceMovement(new ForceMovement(target,
								0, tile, 2, target.getDirection()));
					} else if (ticks == 2) {
						target.setNextWorldTile(tile);
						stop();
						return;
					}
				}
			}, 0, 0);
		} else {
			npc.setNextAnimation(new Animation(14457));
			int damage = (int) Utils.random(
					npc.getMaxHit(NPCCombatDefinitions.MELEE) * .75,
					npc.getMaxHit(NPCCombatDefinitions.MELEE));
			if (target instanceof Player) {
				Player player = (Player) target;
				if (player.getPrayer().isMeleeProtecting()) {
					player.getPackets().sendGameMessage(
							"Your prayer completely negates the attack.", true);
					damage = 0;
				}
			}
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
		}
		return 6;
	}
}
