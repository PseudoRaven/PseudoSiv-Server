package net.nocturne.game.player.content;

import net.nocturne.Settings;
import net.nocturne.game.player.Player;

public class Commands {

	/**
	 * @author: miles M
	 */

	public static final int INTER = 1166;

	public static void send(Player player) {
		player.getInterfaceManager().sendCentralInterface(INTER);
		player.getPackets().sendIComponentText(INTER, 23, "Commands");
		player.getPackets().sendIComponentText(INTER, 2, "");
		String list = "";
		list += "<col=FF0000>::website</col> - Opens up website<br><col=FF0000>::forums</col> - Opens up forums<br><col=FF0000>::store</col> - Opens up store<br><col=FF0000>::hiscores</col> - Opens up hiscores<br><col=FF0000>::vote</col> - Opens up vote<br><col=FF0000>::rules</col> - Opens up rules<br><col=FF0000>::players</col> - Displays online players<br><col=FF0000>::kdr</col> - Displays kill/death ratio<br><col=FF0000>::uptime</col> - Uptime of "
				+ Settings.SERVER_NAME
				+ "<br><col=FF0000>::votepoints</col> - Displays vote points<br><col=FF0000>::changepass</col> - Edit your password<br><col=FF0000>::topic [id]</col> - Opens up topic id";
		player.getPackets().sendIComponentText(1166, 1, list);
	}
	
	public static void testcommand(Player player) {
		player.getPackets().sendGameMessage("Did that work??? ", true);
	}
}