package net.nocturne.game.player.content.activities.events;

import net.nocturne.Settings;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.activities.XPWell;
import net.nocturne.game.player.content.activities.minigames.warbands.Warbands;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 */

public class WorldEvents {

	private static int INTER = 1157;
	public static String EVIL_TREE_STATUS = "Currently Unavailable...";
	public static String PENGUIN_HS_STATUS = "Currently Unavailable...";
	public static String HARMONIZED_STATUS = "Currently Unavailable...";

	public static void sendInterface(Player player) {
		player.getInterfaceManager().sendCentralInterface(INTER);
		player.getPackets().sendIComponentText(INTER, 92, Settings.SERVER_NAME + "'s Event Panel");
		player.getPackets().sendIComponentText(INTER, 95, Settings.SERVER_NAME + "'s Event Panel can be used to find out current events and reach locations for these events.");
		player.getPackets().sendIComponentText(INTER, 33, "Event Name");
		player.getPackets().sendIComponentText(INTER, 34, "Current Status");
		player.getPackets().sendIComponentText(INTER, 46, "");
		player.getPackets().sendIComponentText(INTER, 47, "Evil Tree (Click to Teleport)");
		player.getPackets().sendIComponentText(INTER, 48, EVIL_TREE_STATUS);
		player.getPackets().sendIComponentText(INTER, 49, "");
		player.getPackets().sendIComponentText(INTER, 50, "Penguin Hide and Seek");
		player.getPackets().sendIComponentText(INTER, 51, PENGUIN_HS_STATUS);
		player.getPackets().sendIComponentText(INTER, 52, "");
		player.getPackets().sendIComponentText(INTER, 53, "Harmonized Rocks");
		player.getPackets().sendIComponentText(INTER, 54, HARMONIZED_STATUS);
		player.getPackets().sendIComponentText(INTER, 55, "");
		player.getPackets().sendIComponentText(INTER, 56, "Shooting Stars");
		player.getPackets().sendIComponentText(INTER, 57, "Available: Check your house telescope for the location!");
		player.getPackets().sendIComponentText(INTER, 58, "");
		player.getPackets().sendIComponentText(INTER, 59, "Skiller Dream's xp Bonus");
		player.getPackets().sendIComponentText(INTER, 60, (World.MODIFIER / 10)+"X xp bonus for "+ Skills.SKILL_NAME[World.SKILL_ID]+"!");
		player.getPackets().sendIComponentText(INTER, 61, "");
		player.getPackets().sendIComponentText(INTER, 62, "Wilderness Warbands");
		player.getPackets().sendIComponentText(INTER, 63, "Current Status: " + (Warbands.warband == null ? "None" : Utils.getHoursMinsLeft(Warbands.warband.time)));
		player.getPackets().sendIComponentText(INTER, 64, "");
		player.getPackets().sendIComponentText(INTER, 65, "Global Events");
		player.getPackets().sendIComponentText(INTER, 66, GlobalEvents.getEvent() == null ? "None" : GlobalEvents.getEvent().getDescription());
		player.getPackets().sendIComponentText(INTER, 67, "");
		player.getPackets().sendIComponentText(INTER, 68, "XP Well");
		player.getPackets().sendIComponentText(INTER, 69, "Currently " + XPWell.getWellAmount() + "/" + XPWell.getWellGoal() + "gp.");
		player.getPackets().sendIComponentText(INTER, 70, "");
		player.getPackets().sendIComponentText(INTER, 71, "Empty");
		player.getPackets().sendIComponentText(INTER, 72, "-----");
		player.getPackets().sendIComponentText(INTER, 74, "");
		player.getPackets().sendIComponentText(INTER, 75, "Empty");
		player.getPackets().sendIComponentText(INTER, 76, "-----");
	}

	public static void handleButtons(Player player, int componentId) {
		switch(componentId) {
			case 0:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2456,2833, 0));
				break;
			case 2:
			case 4:
				player.getPackets().sendGameMessage("This is a hint, you need to find the location yourself.");
				break;
			case 1:
			case 3:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				player.getPackets().sendGameMessage("There is no action available for this event.");
				break;
		}

	}

}
