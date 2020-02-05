package net.nocturne.game.player.content;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Color;

public class GnomeGlider {

	private static final WorldTile

	MOUNTAIN = new WorldTile(2848, 3495, 1),

	GRAND_TREE = new WorldTile(2465, 3501, 3),

	CASTLE = new WorldTile(3321, 3427, 0),

	DESERT = new WorldTile(3283, 3213, 0),

	KARAMJA = new WorldTile(2971, 2969, 0),

	GNOME_VILLAGE = new WorldTile(2542, 2971, 0),

	PRIFDDINAS = new WorldTile(2208, 3452, 1),

	FELDIP_HILLS = new WorldTile(2542, 2971, 0);

	public static void sendInterface(Player player) {
		player.getVarsManager().sendVar(4772, 1);
		player.getVarsManager().sendVar(2671, 200);
		player.getVarsManager().sendVarBit(23984, 400);
		player.getVarsManager().sendVarBit(9547, 120);
		player.getInterfaceManager().sendCentralInterface(138);
	}

	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 69:
			sendGnomeGliderTeleport(player, MOUNTAIN);
			break;
		case 37:
			sendGnomeGliderTeleport(player, GRAND_TREE);
			break;
		case 77:
			sendGnomeGliderTeleport(player, CASTLE);
			break;
		case 85:
			sendGnomeGliderTeleport(player, DESERT);
			break;
		case 61:
			sendGnomeGliderTeleport(player, KARAMJA);
			break;
		case 45:
			sendGnomeGliderTeleport(player, GNOME_VILLAGE);
			break;
		case 53:
			sendGnomeGliderTeleport(player, FELDIP_HILLS);
			break;
		case 93:
			if (player.getSkills().getTotalLevel() < 1500) {
				player.getInterfaceManager().removeCentralInterface();
				player.unlock();
				player.getPackets()
						.sendGameMessage(Color.MAROON,
								"You must have a total level of 1500 or greater to access this area.");
				return;
			}
			sendGnomeGliderTeleport(player, PRIFDDINAS);
			break;
		}
	}

	private static void sendGnomeGliderTeleport(Player player,
			final WorldTile tile) {
		player.setNextWorldTile(tile);
		player.getInterfaceManager().removeCentralInterface();
		player.unlock();
		player.getPackets().sendGameMessage(
				"You travel using the gnome glider.", true);
	}
}