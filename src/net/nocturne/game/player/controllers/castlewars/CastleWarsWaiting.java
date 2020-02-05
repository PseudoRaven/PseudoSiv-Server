package net.nocturne.game.player.controllers.castlewars;

import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.content.activities.minigames.CastleWars;
import net.nocturne.game.player.controllers.Controller;

public class CastleWarsWaiting extends Controller {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	// You can't leave just like that!

	public void leave() {
		player.getInterfaceManager().removeMinigameInterface();
		CastleWars.removeWaitingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendMinigameInterface(57);
	}

	@Override
	public boolean canRemoveEquip(int slot, int itemId) {
		if (slot == Equipment.SLOT_HAT || slot == Equipment.SLOT_CAPE) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE || slotId == Equipment.SLOT_HAT) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		removeController();
		leave();
		return true;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(CastleWars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 83620 || id == 83510) {
			removeController();
			leave();
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeController();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
