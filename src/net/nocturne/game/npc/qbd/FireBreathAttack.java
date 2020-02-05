package net.nocturne.game.npc.qbd;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.prayer.Prayer;
import net.nocturne.game.player.actions.skills.smithing.DragonfireShield;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

/**
 * Represents a default fire breath attack.
 * 
 * @author Emperor
 * 
 */
public final class FireBreathAttack implements QueenAttack {

	/**
	 * The animation of the attack.
	 */
	private static final Animation ANIMATION = new Animation(16721);

	/**
	 * The graphic of the attack.
	 */
	private static final Graphics GRAPHIC = new Graphics(3143);

	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.setNextAnimation(ANIMATION);
		npc.setNextGraphics(GRAPHIC);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				super.stop();
				String message = getProtectMessage(victim);
				int hit;
				if (message != null) {
					hit = Utils.random(500 + Utils.random(200),
							message.contains("prayer") ? 1750 : 1550);
					victim.getPackets().sendGameMessage(message);
					DragonfireShield.chargeDFS(victim, true);
				} else {
					hit = Utils.random(700 + Utils.random(750), 2500);
					victim.getPackets().sendGameMessage(
							"You are horribly burned by the dragon's breath!",
							true);
				}
				victim.setNextAnimation(new Animation(Combat
						.getDefenceEmote(victim)));
				victim.applyHit(new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
			}
		}, 1);
		return Utils.random(4, 15); // Attack delay seems to be random a lot.
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

	/**
	 * Gets the dragonfire protect message.
	 * 
	 * @param player
	 *            The player.
	 * @return The message to send, or {@code null} if the player was
	 *         unprotected.
	 */
	public static final String getProtectMessage(Player player) {
		if (Combat.hasAntiDragProtection(player)) {
			return "Your shield absorbs most of the dragon's breath!";
		}
		if (player.hasFireImmunity()) {
			return "Your potion absorbs most of the dragon's breath!";
		}
		if (player.getPrayer().isActive(Prayer.CHIVALRY)
				|| player.getPrayer().isActive(Prayer.SAP_DEFENCE)) {
			return "Your prayer absorbs most of the dragon's breath!";
		}
		return null;
	}
}