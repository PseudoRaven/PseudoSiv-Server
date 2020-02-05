package net.nocturne.game.player.controllers.bossInstance;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.map.bossInstance.impl.VoragoInstance;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class VoragoInstanceController extends BossInstanceController {

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (object.getId() == 84909) {
			player.lock();
			player.setNextAnimation(new Animation(828));
			WorldTasksManager.schedule(new WorldTask() { // to remove at same
															// time it teleports
						@Override
						public void run() {
							getInstance().leaveInstance(player,
									BossInstance.EXITED);
							removeController();
						}
					}, 0);
			return false;
		}

		return true;
	}

	@Override
	public boolean logout() {
		// if player is at battle in public version and logs out, it force kicks
		// player out of instance, otherwise player can relog inside
		if (getInstance().isPublic()
				&& getVoragoInstance().isPlayerInside(player)) {
			player.setLocation(getInstance().getBoss().getOutsideTile());
			removeController();
		}
		return super.logout();
	}

	public VoragoInstance getVoragoInstance() {
		return (VoragoInstance) getInstance();
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (getVoragoInstance().isPlayerOnBattle(player)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You can't leave just like that!");
			return false;
		}
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (getVoragoInstance().isPlayerOnBattle(player)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You can't leave just like that!");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		if (getVoragoInstance().isPlayerOnBattle(player)) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You can't leave just like that!");
			return false;
		}
		return true;
	}

}
