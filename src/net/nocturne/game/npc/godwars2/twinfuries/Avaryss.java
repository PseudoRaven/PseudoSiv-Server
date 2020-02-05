package net.nocturne.game.npc.godwars2.twinfuries;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
public class Avaryss extends NPC {
	private int phase;

	public Avaryss(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		Player player = (Player) hit.getSource();
		for (NPC nymora : World.getNPCs()) {
			int lifepoints = (getHitpoints() + nymora.getHitpoints()) / 2;
			if (nymora.getId() == 22456 && nymora.withinDistance(player, 100)) {
				player.getVarsManager().sendVarBit(28663, lifepoints);
				player.getVarsManager().sendVar(5775,
						lifepoints - hit.getDamage());
			}
		}
	}

	@Override
	public void sendDeath(final Entity source) {
		Player player = (Player) source;
		for (NPC nymora : World.getNPCs()) {
			int value = player.getVarsManager().getValue(5775);
			if (value <= 0 && nymora.withinDistance(player, 100)) {
				player.getInterfaceManager().removeInterface(1648);
				player.getVarsManager().sendVar(6372, 0);
				player.getVarsManager().sendVar(5775, 0);
				super.sendDeath(source);
			}
		}
	}

	@Override
	public void spawn() {
		super.spawn();
		setNextAnimation(new Animation(28264));
	}

	public int getPhase() {
		return phase;
	}

	public void nextPhase() {
		phase++;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

}