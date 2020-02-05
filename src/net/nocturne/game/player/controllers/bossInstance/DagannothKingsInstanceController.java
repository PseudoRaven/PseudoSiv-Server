package net.nocturne.game.player.controllers.bossInstance;

import net.nocturne.game.WorldObject;
import net.nocturne.game.map.bossInstance.BossInstance;

public class DagannothKingsInstanceController extends BossInstanceController {

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 10229) {
			getInstance().leaveInstance(player, BossInstance.EXITED);
			removeController();
			return false;
		}
		return true;
	}
}
