package net.nocturne.utils;

import java.util.ArrayList;

import net.nocturne.Settings;

public final class AntiFlood {

	/**
	 * @author: Apache Ah64
	 * @author: miles M
	 */

	private static ArrayList<String> connections = new ArrayList<String>(
			Settings.PLAYERS_LIMIT * 100);

	public static void add(String ip) {
		connections.add(ip);
	}

	public static void remove(String ip) {
		connections.remove(ip);
	}

	public static int getSessionsIP(String ip) {
		int amount = 1;
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).equalsIgnoreCase(ip))
				amount++;
		}
		return amount;
	}
}