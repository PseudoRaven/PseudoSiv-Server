package net.nocturne.game.npc.combat.impl.godwars2;

import net.nocturne.game.Entity;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.godwars2.twinfuries.Avaryss;

public class AvaryssCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 22455 };
	}

	public void rangedAttack(NPC npc, Entity target) {
		/*
		 * Avaryss avaryss = (Avaryss) npc; WorldTile toTile1 =
		 * target.transform(0, 0, 0);
		 * GameExecutorManager.slowExecutor.schedule(() -> {
		 * avaryss.setNextAnimation(new Animation(28503)); }, 600,
		 * TimeUnit.MILLISECONDS); avaryss.setNextAnimation(new
		 * Animation(28504)); avaryss.setNextForceMovement(new ForceMovement(new
		 * WorldTile(avaryss), 1, toTile1, 2, target.getDirection()));
		 */
	}

	@Override
	public int attack(NPC npc, Entity target) {
		Avaryss avaryss = (Avaryss) npc;
		switch (avaryss.getPhase()) {
		case 0:
			rangedAttack(npc, target);
			break;
		}
		avaryss.nextPhase();
		if (avaryss.getPhase() < 0 || avaryss.getPhase() > 0)
			avaryss.setPhase(0);
		return npc.getAttackSpeed();
	}

}