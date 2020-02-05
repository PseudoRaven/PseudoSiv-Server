package net.nocturne.game.npc.others;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
public class GuthixBlessing extends NPC {

	private Player target;
	private int nextHeal;

	public GuthixBlessing(Player target, WorldTile tile) {
		super(16980, tile, -1, true, true);
		this.target = target;
		nextHeal = 0;
	}

	@Override
	public void processNPC() {
		if (target == null || target.isDead() || target.hasFinished()) {
			finish();
			return;
		} else if (isDead()
				|| target.getHitpoints() == target.getMaxHitpoints()
				|| getPlane() != target.getPlane() || !withinDistance(target)) {
			target.getEffectsManager().removeEffect(EffectType.GUTHIX_BLESSING);
			return;
		}
		if (nextHeal > 2 && nextHeal % 3 == 0)// It skips the first cycle ;)
			target.heal((int) (target.getMaxHitpoints() * 0.08), 0, 0, true);
		nextHeal++;
		sendFollow(target);
	}

	public Player getTarget() {
		return target;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
	}
}
