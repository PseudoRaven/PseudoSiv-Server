package net.nocturne.game.player.controllers.pestcontrol;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.content.activities.minigames.pest.Lander;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.utils.Utils;

public final class PestControlLobby extends Controller {

	private Lander lander;

	@Override
	public void start() {
		this.lander = Lander.getLanders()[(Integer) getArguments()[0]];
	}

	@Override
	public void sendInterfaces() {
		int remainingTime = lander.getTimer().getMinutes();
		player.getPackets().sendIComponentText(407, 3,
				Utils.fixChatMessage(lander.toString()));
		player.getPackets().sendIComponentText(
				407,
				13,
				"Next Departure: " + remainingTime + " minutes "
						+ (!(remainingTime % 2 == 0) ? " 30 seconds" : ""));
		player.getPackets().sendIComponentText(407, 14,
				"Player's Ready: " + lander.getByStanders().size());
		player.getPackets().sendIComponentText(407, 16,
				"Commendations: " + player.getCommendation());
		player.getInterfaceManager().sendMinigameInterface(407);
	}

	@Override
	public void magicTeleported(int teleType) {
		player.getControllerManager().forceStop();
	}

	@Override
	public boolean sendDeath() {
		player.getControllerManager().forceStop();
		return true;
	}

	@Override
	public void forceClose() {
		player.getInterfaceManager().removeMinigameInterface();
		if (lander != null && player != null)
			lander.exit(player);
	}

	@Override
	public boolean logout() {
		if (lander != null && player != null)
			lander.remove(player);
		return false;
	}

	@Override
	public boolean canSummonFamiliar() {
		player.getPackets()
				.sendGameMessage(
						"You feel it's best to keep your Familiar away during this game.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		switch (object.getId()) {
		case 14314:
		case 25629:
		case 25630:
			player.getDialogueManager().startDialogue("LanderD");
			return true;
		}
		return true;
	}
}
