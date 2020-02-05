package net.nocturne.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nocturne.game.player.Player;

public class ScrollMessage {

	private static final Map<String, ScrollMessage> LOADED = new HashMap<String, ScrollMessage>();
	private String title;
	private String[] messages;

	public static boolean displayScrollMessage(Player player, String scroll,
			String... defaultTextColor) {
		final ScrollMessage scrollMessage = LOADED.get(scroll.toLowerCase());
		if (scrollMessage == null)
			return false;
		if (defaultTextColor.length > 0) {
			String hexCode = defaultTextColor[0];
			for (String message : scrollMessage.messages) {
				if (!message.contains("<col=")) {
					message = "<col=" + hexCode + ">" + message;
				}
			}
		}
		sendScroll(player, scrollMessage.title, scrollMessage.messages);
		return true;
	}

	public static boolean displayScrollMessageUpdate(Player player,
			String scroll) {
		load();
		final ScrollMessage scrollMessage = LOADED.get(scroll.toLowerCase());
		if (scrollMessage == null)
			return false;
		sendScroll(player, scrollMessage.title, scrollMessage.messages);
		return true;
	}

	private static void sendScroll(Player player, String title,
			String[] messages) {
		player.getInterfaceManager().sendCentralInterface(868);
		player.getPackets().sendIComponentText(868, 1, title);
		int index = 0;
		for (String text : messages)
			player.getPackets().sendIComponentText(868, 10 + index++, text);
		for (int i = index; i < 299; i++)
			player.getPackets().sendIComponentText(868, 10 + i, "");
	}

	public static void load() {
		LOADED.clear();
		try {
			File[] files = new File("./data/scrolls").listFiles();
			if (files == null || files.length <= 0)
				return;
			for (final File file : files) {
				if (file == null || file.isDirectory())
					continue;
				/**
				 * Loading
				 */
				final BufferedReader br = new BufferedReader(new FileReader(
						file));
				final ScrollMessage scrollMessage = new ScrollMessage();
				scrollMessage.title = br.readLine();
				String line;
				List<String> messages = new ArrayList<String>();
				while ((line = br.readLine()) != null)
					messages.add(line);
				br.close();
				/**
				 * Setting
				 */
				scrollMessage.messages = new String[messages.size()];
				for (int i = 0; i < messages.size(); i++)
					scrollMessage.messages[i] = messages.get(i);
				LOADED.put(file.getName().replaceAll(".txt", "").toLowerCase(),
						scrollMessage);
				messages.clear();
				messages = null;
			}
			files = new File("./data/scrolls/skills").listFiles();
			if (files == null || files.length <= 0)
				return;
			for (final File file : files) {
				if (file == null || file.isDirectory())
					continue;
				/**
				 * Loading
				 */
				final BufferedReader br = new BufferedReader(new FileReader(
						file));
				final ScrollMessage scrollMessage = new ScrollMessage();
				scrollMessage.title = br.readLine();
				String line;
				List<String> messages = new ArrayList<String>();
				while ((line = br.readLine()) != null)
					messages.add(line);
				br.close();
				/**
				 * Setting
				 */
				scrollMessage.messages = new String[messages.size()];
				for (int i = 0; i < messages.size(); i++)
					scrollMessage.messages[i] = messages.get(i);
				LOADED.put(file.getName().replaceAll(".txt", "").toLowerCase(),
						scrollMessage);
				messages.clear();
				messages = null;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
