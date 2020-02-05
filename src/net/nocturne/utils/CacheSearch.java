package net.nocturne.utils;

import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;

public class CacheSearch {

	public static void handleSearch(Player player, String name) {
		searchForItem(player, name);
		searchForNPC(player, name);
		searchForObject(player, name);
	}

	private static void searchForItem(Player player, String itemName) {
		int count = 0;
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			Item item = new Item(i);
			if (item.getDefinitions().getName().toLowerCase()
					.contains(itemName.toLowerCase())) {
				count++;
				int MAX_RESULTS = 250;
				if (count == MAX_RESULTS) {
					player.getPackets()
							.sendPanelBoxMessage(
									"<col=FF0000>Found over 250 results for "
											+ Utils.formatPlayerNameForDisplay(itemName)
											+ ". Only 250 listed.");
					return;
				}
				String suffix = item.getDefinitions().isNoted() ? "(noted)"
						: "";
				player.getPackets().sendPanelBoxMessage(
						"<col=00FFFF>"
								+ Utils.formatPlayerNameForDisplay(item
										.getName()) + suffix
								+ "</col> (Id: <col=00FF00>" + item.getId()
								+ "</col>)");
			}
		}
		player.getPackets().sendPanelBoxMessage(
				"<col=FF0000>Found " + count + " results for the item "
						+ Utils.formatPlayerNameForDisplay(itemName) + ".");
	}

	private static void searchForNPC(Player player, String npcName) {
		int count = 0;
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			String name = NPCDefinitions.getNPCDefinitions(i).getName();
			if (name.toLowerCase().contains(npcName.toLowerCase())) {
				count++;
				int MAX_RESULTS = 250;
				if (count == MAX_RESULTS) {
					player.getPackets().sendPanelBoxMessage(
							"<col=FF0000>Found over 250 results for "
									+ Utils.formatPlayerNameForDisplay(npcName)
									+ ". Only 250 listed.");
					return;
				}
				player.getPackets().sendPanelBoxMessage(
						"<col=00FFFF>" + Utils.formatPlayerNameForDisplay(name)
								+ "</col> (Id: <col=00FF00>" + i + "</col>)");
			}
		}
		player.getPackets().sendPanelBoxMessage(
				"<col=FF0000>Found " + count + " results for the npc "
						+ Utils.formatPlayerNameForDisplay(npcName) + ".");
	}

	private static void searchForObject(Player player, String objectName) {
		int count = 0;
		for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
			try {
				String name = ObjectDefinitions.getObjectDefinitions(i).name;
				if (name.toLowerCase().contains(objectName.toLowerCase())) {
					count++;
					int MAX_RESULTS = 250;
					if (count == MAX_RESULTS) {
						player.getPackets()
								.sendPanelBoxMessage(
										"<col=FF0000>Found over 250 results for "
												+ Utils.formatPlayerNameForDisplay(objectName)
												+ ". Only 250 listed.");
						return;
					}
					player.getPackets().sendPanelBoxMessage(
							"<col=00FFFF>"
									+ Utils.formatPlayerNameForDisplay(name)
									+ "</col> (Id: <col=00FF00>" + i
									+ "</col>)");
				}
			} catch (Exception e) {

			}
		}
		player.getPackets().sendPanelBoxMessage(
				"<col=FF0000>Found " + count + " results for the object "
						+ Utils.formatPlayerNameForDisplay(objectName) + ".");
	}

}
