package net.nocturne.game.npc.godwars2.helwyr;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;

@SuppressWarnings("serial")
public class CywirAlpha extends NPC {
	private int phase;

	public CywirAlpha(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		setForceAgressive(true);
	}

	@Override
	public void processNPC() {
		super.processNPC();
	}

	@Override
	public void setRespawnTask() {

	}

	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
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
