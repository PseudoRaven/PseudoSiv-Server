package net.nocturne.game.npc.araxxor;

import net.nocturne.game.Animation;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class RangeAttack implements AraxxorAttack {

	@Override
	public int attack(final Araxxor npc, final Player p) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				p.AraxxorAttackCount++;
				stop();
				int hit = 0;
				World.sendProjectile(npc, p, 4990, 41, 16, 21, 0, 16, -20);
				hit = Utils.random(0 + Utils.random(1000), 3000);
				npc.setNextAnimation(new Animation(24047));
				p.setNextAnimation(new Animation(Combat.getDefenceEmote(p)));

				p.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED
						: HitLook.RANGE_DAMAGE));
			}
		});
		// return Utils.random(4, 6);
		return 6;
	}

	@Override
	public boolean canAttack(Araxxor npc, Player victim) {
		return victim.getY() > npc.getBase().getY() + 10;
	}

}
