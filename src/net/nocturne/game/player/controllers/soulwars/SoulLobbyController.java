/**
 * @author miles Magenz - Ventyz Productions
 *
 * SoulLobbyController.java created on 31.des.2014.
 */
package net.nocturne.game.player.controllers.soulwars;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.content.activities.minigames.soulwars.SoulLobby;
import net.nocturne.game.player.controllers.Controller;

// TODO: Auto-generated Javadoc
/**
 * The Class SoulLobbyController.
 */
public class SoulLobbyController extends Controller {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.ventyz.server.model.character.controllers.Controller#start()
	 */
	@Override
	public void start() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.nocturne.game.player.Controllers.Controller#login()
	 */

	@Override
	public boolean login() {
		if (player.didPassBlue) {
			SoulLobby.removeBlue(player);
		} else if (player.didPassRed) {
			SoulLobby.removeRed(player);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.nocturne.game.player.Controllers.Controller#logout()
	 */
	@Override
	public boolean logout() {
		if (player.didPassBlue) {
			SoulLobby.removeBlue(player);
		}
		if (player.didPassRed) {
			SoulLobby.removeRed(player);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.nocturne.game.player.Controllers.Controller#processMagicTeleport(net.
	 * kagani.game.WorldTile)
	 */

	// TODO object click for exit

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ventyz.server.model.character.controllers.Controller#processItemTeleport
	 * (net.ventyz.server.world.WorldTile)
	 */
	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ventyz.server.model.character.controllers.Controller#magicTeleported
	 * (int)
	 */
	@Override
	public void magicTeleported(int type) {
		player.getControllerManager().forceStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ventyz.server.model.character.controllers.Controller#processButtonClick
	 * (int, int, int, int)
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int slotId2, int packetId) {
		if (interfaceId == 193 && componentId == 48) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You cant teleport during SoulWars.");
			return false;
		}
		if (interfaceId == 192 && componentId == 24) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You cant teleport during SoulWars.");
			return false;
		}
		if (interfaceId == 182 && componentId == 13) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"Please leave SoulWars before doing this.");
			return false;
		}
		if (interfaceId == 182 && componentId == 6) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"Please leave SoulWars before doing this.");
			return false;
		}
		return true;
	}

}
