package net.nocturne.game.player.content.activities.reaper;

import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class ReaperRewardsShop {

	/**
	 * @author: Tommeh
	 */

	public static int INTER = 754;

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 98:
			player.getPackets().sendHideIComponent(754, 40, false);
			break;
		case 116:
			player.getPackets().sendGameMessage(
					"This feature has yet to be added.");
			break;
		case 134:
			player.getPackets().sendGameMessage(
					"This feature has yet to be added.");
			break;
		case 152:
			player.getPackets().sendGameMessage(
					"This feature has yet to be added.");
			break;
		case 170:
			player.getPackets().sendGameMessage(
					"This feature has yet to be added.");
			break;
		case 165:
			if (player.reaperPoints >= 300) {
				player.reaperPoints -= 300;
				player.getInventory().addItem(31851, 1);
				player.getPackets().sendGameMessage(
						"You receive: Incomplete Hydrix.");
				ReaperRewardsShop.openShop(player);
			} else {
				player.getPackets().sendGameMessage(
						"Sorry. That would cost 300 and you only have "
								+ player.getReaperPoints() + " Reaper points.");
			}
			break;
		case 199:
			player.getPackets().sendHideIComponent(754, 40, false);
			player.getPackets().sendIComponentText(754, 261, "75");
			player.getPackets().sendIComponentText(754, 263, "750");
			player.getPackets().sendIComponentText(754, 258,
					"an alternative loot beam");
		default:
		}
	}

	public static void openShop(Player player) {
		player.getInterfaceManager().sendCentralInterface(INTER);
		/*
		 * player.getPackets().sendHideIComponent(754,99,false);
		 * player.getPackets().sendHideIComponent(754,117,false);
		 * player.getPackets().sendHideIComponent(754,135,false);
		 * player.getPackets().sendHideIComponent(754,153,false);
		 * player.getPackets().sendHideIComponent(754,171,false);
		 * //player.getPackets().sendHideIComponent(754,189,false);
		 * player.getPackets().sendHideIComponent(754,200,false); //rainbow
		 * player.getPackets().sendHideIComponent(754,215,false);// additional
		 * tasks player.getPackets().sendHideIComponent(754,233,
		 * false);//bonfire boost
		 * player.getPackets().sendHideIComponent(754,248, false);//instance
		 * boost
		 */
		player.getVarsManager().sendVarBit(9071,
				player.getSlayerManager().getPoints());
		player.getVarsManager().sendVarBit(22905, player.getReaperPoints());
	}
}