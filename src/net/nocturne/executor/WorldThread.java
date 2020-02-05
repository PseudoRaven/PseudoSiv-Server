package net.nocturne.executor;

import net.nocturne.Settings;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public final class WorldThread extends Thread {

	public static volatile long WORLD_CYCLE;

	protected WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	@Override
	public final void run() {
		try {
			while (!GameExecutorManager.executorShutdown) {
				WORLD_CYCLE++; // made the cycle update at begin instead of end
								// cuz
				// at end theres 600ms then to next cycle
				long currentTime = Utils.currentTimeMillis();
				// long debug = Utils.currentTimeMillis();
				WorldTasksManager.processTasks();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					try {
						player.processEntity();
					} catch (ArrayIndexOutOfBoundsException e) {
						Logger.handle(e,
								"PLAYER ISSUE: " + player.getUsername());
					}
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					try {
						npc.processEntity();
					} catch (ArrayIndexOutOfBoundsException e) {
						Logger.handle(e, "NPC ISSUE: " + npc.getId());
					}
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					if (player.afk < Utils.currentTimeMillis())
						player.logout(false);
					player.processEntityUpdate();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.processEntityUpdate();
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
					player.processProjectiles();// waits for player to walk and
					// so on
				}
				World.removeProjectiles();
				// System.out.print(" ,PLAYER UPDATE: "+(Utils.currentTimeMillis()-debug)+", "+World.getPlayers().size()+", "+World.getNPCs().size());
				// debug = Utils.currentTimeMillis();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					player.resetMasks();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished())
						continue;
					npc.resetMasks();
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					if (!player.getSession().getChannel().isConnected())
						player.finish(); // requests finish, wont do anything if
					// already requested btw
				}
				for (Player player : World.getLobbyPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.hasFinished())
						continue;
					if (!player.getSession().getChannel().isConnected())
						player.finish(); // requests finish, wont do anything if
					// already requested btw
				}
				// //
				// Logger.log(this,
				// "TOTAL: "+(Utils.currentTimeMillis()-currentTime));
				long sleepTime = Settings.WORLD_CYCLE_TIME + currentTime
						- Utils.currentTimeMillis();
				if (sleepTime <= 0)
					continue;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Logger.handle(e, "Thread Sleep");
				}
			}
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}