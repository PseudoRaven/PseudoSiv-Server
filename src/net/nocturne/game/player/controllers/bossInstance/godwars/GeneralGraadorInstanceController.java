package net.nocturne.game.player.controllers.bossInstance.godwars;

import net.nocturne.game.WorldObject;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.player.controllers.bossInstance.BossInstanceController;

public class GeneralGraadorInstanceController extends BossInstanceController {

	/**
	 * @author: miles M
	 */

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		switch (object.getId()) {
		case 26425:
			getInstance().leaveInstance(player, BossInstance.EXITED);
			removeController();
			return false;
		default:
			return true;
		}
	}

	@Override
	public boolean processObjectClick2(final WorldObject object) {

		switch (object.getId()) {
		case 26289:
			getInstance().leaveInstance(player, BossInstance.EXITED);
			removeController();
			return false;
		default:
			return true;

		}
	}
}