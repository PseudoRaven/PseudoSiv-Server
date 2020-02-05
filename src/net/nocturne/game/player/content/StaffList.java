package net.nocturne.game.player.content;

import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class StaffList {

	private final static int INTERFACE = 1158;

	private final static String ONLINE = "<col=02AB2F>Online</col>";
	private final static String OFFLINE = "<col=DB0000>Offline</col>";

	private enum Staff {

		OWNER("Liam", "Owner", "danny"),

		CO_OWNER("Charity", "Co-Owner", "queen_chare", "confused", "charity"),

		ADMINISTRATOR1("Klein", "Administrator", "klein_curse"),

		ADMINISTRATOR2("Arachnid", "Administrator", "arachnid"),

		MODERATOR1("Nexus", "Moderator", "nexus"),

		SUPPORT1("Bob", "Supporter", "bob"),

		SUPPORT2("Party Blues", "Supporter", "party_blues", "party blues");

		private final String username, position;
		private final String[] usernames;

		Staff(String username, String position, String... usernames) {
			this.username = username;
			this.position = position;
			this.usernames = usernames;
		}

		public String getUsername() {
			return username;
		}

		public String getPosition() {
			return position;
		}

		public String getOnline() {
			for (String name : usernames) {
				if (World.containsPlayer(name))
					return ONLINE;
			}
			return OFFLINE;
		}
	}

	public static void send(Player player) {
		player.getInterfaceManager().sendCentralInterface(INTERFACE);
		for (int i = 0; i < Utils
				.getInterfaceDefinitionsComponentsSize(INTERFACE); i++)
			player.getPackets().sendIComponentText(INTERFACE, i, "");
		player.getPackets().sendIComponentText(INTERFACE, 74, "Staff List");
		int componentId = 8;
		int number = 1;
		for (Staff staff : Staff.values()) {
			if (componentId >= 56) // end of interface
				return;
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					"" + number++);
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getUsername());
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getPosition());
			player.getPackets().sendIComponentText(INTERFACE, componentId++,
					staff.getOnline());
			componentId++;
		}
	}
}