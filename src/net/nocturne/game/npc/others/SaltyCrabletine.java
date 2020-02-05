package net.nocturne.game.npc.others;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Jan 16, 2017
 */

public class SaltyCrabletine extends NPC {

	private static final long serialVersionUID = 2153651967191821986L;
	private Stopwatch stopwatch;
	private Player player;

	public SaltyCrabletine(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		if (stopwatch != null) {
			if (stopwatch.elapsed(TimeUnit.MINUTES) >= 10)
				stopwatch = Stopwatch.createStarted();
			else if (player != null)
				addFreezeDelay(100);
		}
		super.processNPC();
	}

	public void startMining(Player player) {
		this.player = player;
		if (stopwatch == null)
			stopwatch = Stopwatch.createStarted();
		else if (!stopwatch.isRunning())
			stopwatch.start();
	}

	public void stopMining() {
		setFreezeDelay(0);
		if (stopwatch != null)
			stopwatch.stop();
	}

	public boolean canMine(Player player) {
		if (!player.getSkills().hasLevel(Skills.MINING, 90))
			return false;
		if (stopwatch != null && stopwatch.elapsed(TimeUnit.SECONDS) >= 360) {
			player.getPackets()
					.sendGameMessage(
							"The salt crystals on the nearby crablet have disappeared.");
			setFreezeDelay(0);
			return false;
		}
		return true;
	}
}
