package net.nocturne.game.npc.qbd;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.prayer.Prayer;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

/**
 * Handles the Queen Black Dragon's range attack.
 * 
 * @author Emperor
 * 
 */
public final class RangeAttack implements QueenAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(16718);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.setNextAnimation(ANIMATION);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				stop();
				int hit = Utils.random(Utils.random(1400) + 500, 2990);
				if (victim.getPrayer().isActive(Prayer.SAP_STRENGTH)) {
					victim.setNextAnimation(new Animation(12573));
					victim.setNextGraphics(new Graphics(2229));
					victim.getPackets()
							.sendGameMessage(
									"You are unable to reflect damage back to this creature.");
					hit /= 2;
				} else if (victim.getPrayer().isActive(Prayer.RAPID_RENEWAL)) {
					victim.setNextAnimation(new Animation(Combat
							.getDefenceEmote(victim)));
					hit /= 2;
				} else {
					victim.setNextAnimation(new Animation(Combat
							.getDefenceEmote(victim)));
				}
				victim.applyHit(new Hit(npc, hit, hit == 0 ? HitLook.MISSED
						: HitLook.RANGE_DAMAGE));
			}
		}, 1);
		return Utils.random(4, 15);
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

}