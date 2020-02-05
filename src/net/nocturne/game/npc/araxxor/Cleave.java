package net.nocturne.game.npc.araxxor;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Cleave implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player victim) {
		// npc.addFreezeDelay(10);

		npc.setLocked(true);
		WorldTasksManager.schedule(new WorldTask() {
			private int time;

			@Override
			public void run() {
				time++;
				npc.setPhase(1);
				victim.AraxxorAttackCount = 0;
				npc.setNextAnimation(new Animation(24096));
				npc.setNextGraphics(new Graphics(4986));
				if (time == 4) {
					if (victim.withinDistance(npc, 5)) {
						npc.setLocked(false);
						CombatScript.delayHit(npc, 2, victim, new Hit(npc,
								CombatScript.getMaxHit(npc, 3000, npc),
								HitLook.REGULAR_DAMAGE));
						stop();
					}
				}
			}
		}, 10);
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(Araxxor npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 5;
	}

}
