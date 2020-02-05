package net.nocturne.api.http;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.executor.PlayerHandlerThread;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializationUtilities;
import net.nocturne.utils.Utils;

public class HTTPInstruction {

	/**
	 * @author: Pax M
	 * @author: Lukafurlan
	 */

	public static String friendsChat;

	public static String getFriendsChat(String parameters) {
		return friendsChat;
	}

	public static String getPlayersCount(String parameters) {
		return World.getPlayers().size() + "";
	}

	public static String getStaffCount(String parameters) {
		return World.getStaffCount() + "";
	}

	public static String getLogger(String parameters) {
		return Logger.getMapContents();
	}

	public static String getUptime(String parameters) {
		long ticks = Engine.currentTime - Utils.currentTimeMillis();
		int seconds = Math.abs((int) (ticks / 1000) % 60);
		int minutes = Math.abs((int) ((ticks / (1000 * 60)) % 60));
		int hours = Math.abs((int) ((ticks / (1000 * 60 * 60)) % 24));
		int days = Math.abs((int) ((ticks / (1000 * 60 * 60 * 60)) % 24));
		return days + (days != 1 ? " d" : "d") + ", " + hours
				+ (hours != 1 ? " h" : " h") + ", " + minutes
				+ (minutes != 1 ? " mins" : " min") + ", " + seconds
				+ (seconds != 1 ? " sec" : "sec");
	}

	public static String getPlayers(String parameters) {
		Map<String, Integer> playerList = new HashMap<>();
		for (Player players : World.getPlayers()) {
			if (players != null)
				playerList.put(players.getUsername(), players.getRights());
		}
		return playerList.toString();
	}

	public static String isOnline(String parameters) {
		Map<String, String> params = HTTPService.queryToMap(parameters);
		Map<String, Integer> playerList = new HashMap<>();
		for (Player players : World.getPlayers()) {
			if (players != null)
				playerList.put(players.getUsername(), players.getRights());
		}
		if (playerList.get(params.get("playerName")) != null)
			return "true";
		else
			return "false";
	}

	public static void saveAll(String parameters) {
		for (Player p : World.getPlayers()) {
			try {
				if (p == null || !p.hasStarted() || p.hasFinished())
					continue;

				byte[] data = SerializationUtilities.tryStoreObject(p);
				if (data == null || data.length <= 0)
					continue;
				GrandExchange.save();
				PlayerHandlerThread.addSave(p.getUsername(), data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Logger.log("HTTPInstruction", "Saved " + World.getPlayers().size()
				+ " players on world " + Settings.WORLD_ID + ".");
	}

	public static void updateServer(String parameters) {
		int delay = 300;
		if (Integer.parseInt(parameters) > 0) {
			int seconds = Integer.parseInt(parameters) / 2;
			delay = seconds;
			Logger.log("HTTPInstruction", seconds + " seconds");
		} else
			Logger.log("HTTPInstruction",
					"Boot value is below 1. Sending default: 300 seconds.");
		Engine.shutdown(delay, true, true);
	}

	public static void sendMessage(String parameters) {
		World.sendEngineMessage(parameters);
		Logger.log("HTTPInstruction", parameters);
	}
}