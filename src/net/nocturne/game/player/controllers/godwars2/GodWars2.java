package net.nocturne.game.player.controllers.godwars2;

import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.RouteEvent;
import net.nocturne.game.player.controllers.Controller;

public class GodWars2 extends Controller {

	private static final int SEREN = 0;
	private static final int SLISKE = 1;
	private static final int ZAMORAK = 2;
	private static final int ZAROS = 3;
	private int[] killCount = new int[4];
	private int sector;

	@Override
	public void start() {
		sendInterfaces();
		refresh();
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendMinigameInterface(1746);
	}

	public void removeInterfaces() {
		player.getInterfaceManager().removeInterface(1746);
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean sendDeath() {
		removeController();
		removeInterfaces();
		return true;
	}

	@Override
	public boolean login() {
		player.getControllerManager().startController("GodWars2");
		return false;
	}

	public void incrementKillCount(int index) {
		killCount[index]++;
		refresh();
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 101909) {
			int index = ZAROS;
			int requiredKc = 40;
			if (player.getY() >= 6902) {
				if (killCount[index] >= requiredKc) {
					WorldTile tile = new WorldTile(3115, 6900, 1);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					// killCount[index] -= requiredKc;
				} else
					player.getPackets()
							.sendGameMessage(
									"The door is locked by the power of Zaros! You must have at least 40 Zarosian killcount to enter.");
			} else {
				WorldTile tile = new WorldTile(3117, 6902, 1);
				player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
			}
			sector = index;
			refresh();
			return false;
		} else if (object.getId() == 101906) {
			int index = ZAMORAK;
			int requiredKc = 40;
			if (player.getY() <= 7046) {
				if (killCount[index] >= requiredKc) {
					WorldTile tile = new WorldTile(3126, 7048, 1);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					// killCount[index] -= requiredKc;
					sector = index;
					refresh();
				} else
					player.getPackets()
							.sendGameMessage(
									"The door is locked by the power of Zamorak! You must have at least 40 Zamorakian killcount to enter.");
			} else {
				WorldTile tile = new WorldTile(3128, 7046, 1);
				player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
			}
			return false;
		} else if (object.getId() == 101901) {
			int index = SLISKE;
			int requiredKc = 40;
			if (player.getY() <= 7053) {
				if (killCount[index] >= requiredKc) {
					WorldTile tile = new WorldTile(3279, 7055, 1);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					// killCount[index] -= requiredKc;
				} else
					player.getPackets()
							.sendGameMessage(
									"The door is locked by the power of Sliske! You must have at least 40 Sliskean killcount to enter.");
			} else {
				WorldTile tile = new WorldTile(3277, 7053, 1);
				player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
			}
			sector = index;
			refresh();

			return false;
		} else if (object.getId() == 101897) {
			int index = SEREN;
			int requiredKc = 40;
			if (player.getX() <= 3271) {
				if (killCount[index] >= requiredKc) {
					WorldTile tile = new WorldTile(3274, 6901, 1);
					player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
					// killCount[index] -= requiredKc;
				} else
					player.getPackets()
							.sendGameMessage(
									"The door is locked by the power of Seren! You must have at least 40 Serenist killcount to enter.");
			} else {
				WorldTile tile = new WorldTile(3271, 6903, 1);
				player.addWalkSteps(tile.getX(), tile.getY(), -1, false);
			}
			sector = index;
			refresh();
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		removeController();
		removeInterfaces();
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		removeController();
		removeInterfaces();
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		removeController();
		removeInterfaces();
		return true;
	}

	private void refresh() {
		player.getPackets().sendIComponentText(1746, 47, "" + killCount[SEREN]);
		player.getPackets()
				.sendIComponentText(1746, 54, "" + killCount[SLISKE]);
		player.getPackets().sendIComponentText(1746, 60,
				"" + killCount[ZAMORAK]);
		player.getPackets().sendIComponentText(1746, 66, "" + killCount[ZAROS]);
		player.getPackets().sendIComponentText(1746, 10, "" + killCount[SEREN]);
		player.getPackets()
				.sendIComponentText(1746, 17, "" + killCount[SLISKE]);
		player.getPackets().sendIComponentText(1746, 23,
				"" + killCount[ZAMORAK]);
		player.getPackets().sendIComponentText(1746, 29, "" + killCount[ZAROS]);
	}

}
