package net.nocturne.game.npc.araxxor;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class WebShield implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player victim) {
		// npc.addFreezeDelay(15);
		npc.setLocked(true);
		npc.setTarget(null);
		npc.setPhase(1);
		victim.AraxxorAttackCount = 0;
		victim.araxxorHeal = true;
		npc.setNextAnimation(new Animation(24075));
		npc.setNextGraphics(new Graphics(4987));
		WorldTasksManager.schedule(new WorldTask() {

			private int time = 0;

			@Override
			public void run() {
				if (time % 2 == 0)
					npc.heal(100);
				if (time == 7)
					victim.araxxorHeal = false;
				time++;
			}
		}, 3, 0);
		return 25;
	}

	@Override
	public boolean canAttack(Araxxor npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 10;
	}
}
