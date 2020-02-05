package net.nocturne.game.player.controllers;

import net.nocturne.game.player.content.activities.minigames.ZarosGodwars;

public class ZEDController extends Controller {

	@Override
	public void start() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
	}

	@Override
	public boolean logout() {
		ZarosGodwars.removePlayer(player);
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		ZarosGodwars.addPlayer(player);
		sendInterfaces();
		return false;
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendMinigameInterface(601);
		player.getPackets().sendHideIComponent(601, 17, false);
		player.getPackets().sendHideIComponent(601, 22, false);
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeController();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
		removeController();
	}

	@Override
	public void forceClose() {
		remove();
	}

	public void remove() {
		ZarosGodwars.removePlayer(player);
		player.getPackets().sendCSVarInteger(1435, 255);
		player.getInterfaceManager().removeMinigameInterface();
	}
}
