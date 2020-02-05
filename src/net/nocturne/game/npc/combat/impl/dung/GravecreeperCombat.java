package net.nocturne.game.npc.combat.impl.dung;

import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.dungeonnering.Gravecreeper;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class GravecreeperCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 11708 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Gravecreeper boss = (Gravecreeper) npc;
		if (boss.getSpecialDelay() != -2
				&& (boss.getSpecialDelay() == -1 || (Utils.random(10) == 0 && boss
						.getSpecialDelay() <= Utils.currentWorldCycle()))) { // might
																				// change
																				// this
																				// chance
																				// here
			if (boss.getSpecialDelay() != -1 && Utils.random(5) != 0) {
				boss.setNextForceTalk(new ForceTalk("Burrnnn!"));
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						boss.createBurnTiles(new WorldTile(boss));
					}
				}, 1);
				boss.setSpecialDelay(Utils.currentWorldCycle()
						+ Gravecreeper.BURN_DELAY);
				if (Utils.isOnRange(npc.getX(), npc.getY(), npc.getSize(),
						target.getX(), target.getY(), target.getSize(), 0)) {
					boss.setForceFollowClose(true);
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							boss.setForceFollowClose(false);
						}
					}, 7);
				}
				return 4;
			} else {
				boss.useSpecial();
				return 4;
			}
		}

		boolean atDistance = !Utils.isOnRange(npc.getX(), npc.getY(),
				npc.getSize(), target.getX(), target.getY(), target.getSize(),
				0);
		int attack = Utils.random(!atDistance ? 2 : 1);
		switch (attack) {
		case 0:// range
			npc.setNextAnimation(new Animation(14504));
			World.sendProjectile(npc, target, 2753, 65, 65, 30, 0, 0, 0);
			delayHit(
					npc,
					1,
					target,
					getRangedHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.RANGE, target)));
			break;
		case 1:// melee
			npc.setNextAnimation(new Animation(14503));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		}
		return 4;
	}
}
