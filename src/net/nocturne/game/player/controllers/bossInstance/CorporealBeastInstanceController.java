package net.nocturne.game.player.controllers.bossInstance;

import net.nocturne.game.WorldObject;
import net.nocturne.game.map.bossInstance.BossInstance;

public class CorporealBeastInstanceController extends BossInstanceController {

	@Override
	public boolean processObjectClick1(final WorldObject object) {

		switch (object.getId()) {
		case 38811:
			getInstance().leaveInstance(player, BossInstance.EXITED);
			removeController();
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {
		switch (object.getId()) {
		case 38811:
			player.getPackets()
					.sendGameMessage(
							"You can't peak when you are already inside the encounter!");
			return false;
		default:
			return true;

		}
	}
}
