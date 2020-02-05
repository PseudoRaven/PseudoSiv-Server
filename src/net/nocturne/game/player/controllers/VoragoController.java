package net.nocturne.game.player.controllers;

import net.nocturne.Settings;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.vorago.VoragoHandler;

public class VoragoController extends Controller {

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void forceClose() {
		player.setNextWorldTile(Settings.STARTER_LOCATION);
		VoragoHandler.removePlayer(player);
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		VoragoHandler.removePlayer(player);
		removeController();
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		VoragoHandler.removePlayer(player);
		removeController();
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int slotId2, int packetId) {
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		removeController();
		return false;
	}

	@Override
	public boolean logout() {
		VoragoHandler.removePlayer(player);
		return true;
	}

	@Override
	public boolean sendDeath() {
		VoragoHandler.removePlayer(player);
		removeController();
		return true;
	}

}
