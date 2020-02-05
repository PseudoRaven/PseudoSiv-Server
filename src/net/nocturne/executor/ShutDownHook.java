package net.nocturne.executor;

import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.Lottery;
import net.nocturne.login.Login;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializableFilesManager;

public class ShutDownHook extends Thread {

	private static final ShutDownHook INSTANCE = new ShutDownHook();

	private ShutDownHook() {

	}

	@Override
	public void run() {
		try {
			Logger.log("ShutDownHook", "Shutting down the server.");
			Logger.log("ShutDownHook", "Preparing players for shutdown.");
			Lottery lot = new Lottery();
			lot.cancelLottery();
			for (Player player : World.getPlayers()) {
				if (player == null || !player.hasStarted()
						|| player.hasFinished())
					continue;
				if (player.getClanManager() != null
						&& player.getClanManager().getClan() != null)
					SerializableFilesManager.saveClan(player.getClanManager()
							.getClan());
				Login.saveFiles();
				player.getPackets().sendLogout(false);
			}
			Login.saveFiles();
		} catch (Exception e) {
			Logger.log("ShutDownHook", "Error while shutting down.");
		}
	}

	public static ShutDownHook get() {
		return INSTANCE;
	}
}