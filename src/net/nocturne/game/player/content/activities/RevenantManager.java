package net.nocturne.game.player.content.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Dec 18, 2016
 */

public class RevenantManager {

	private static final int[] revenantIds = { 13465, 13466, 13467, 13468,
			13469, 13470, 13471, 13472, 13473, 13474, 13475, 13476, 13477,
			13478, 13479, 13480, 13481 };
	private static List<NPC> revenants = new ArrayList<>();
	private static final int MAX_AMOUNT = 10;

	public static void process() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				int maxAttempts = 2 + Utils.random(2);
				for (int attempts = 0; attempts < maxAttempts; attempts++) {
					int randomId = revenantIds[Utils.random(revenantIds.length)];
					if (!containsRevenants(randomId)) {
						int x = 2960 + Utils.random(3340 - 2960);
						int y = 3550 + Utils.random(3960 - 3550);
						NPC rev = World.spawnNPC(randomId, new WorldTile(x, y,
								0), -1, true, true);
						assert rev != null;
						rev.setNoClipWalking(true);
						revenants.add(rev);
					}
				}
			}
		}, 0, 15, TimeUnit.MINUTES);
	}

	private static boolean containsRevenants(int id) {
		int number = 0;
		List<NPC> toRemove = new ArrayList<>();
		for (NPC revenant : revenants) {
			if (revenant == null || revenant.hasFinished()) {
				toRemove.add(revenant);
				continue;
			}
			if (revenant.getId() == id)
				number++;
		}
		for (NPC npc : toRemove) {
			revenants.remove(npc);
		}
		return number >= MAX_AMOUNT;
	}
}