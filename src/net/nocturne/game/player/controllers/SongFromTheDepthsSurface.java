package net.nocturne.game.player.controllers;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.MusicsManager;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

/**
 * @author Paty
 **/

class SongFromTheDepthsSurface extends Controller {

	private boolean tempvalue = false;

	@Override
	public void start() {
		FadingScreen.fade(
				player,
				() -> {
					player.getPackets().sendGameMessage(
							"Starting surface world controller.");
					player.getInterfaceManager().sendFadingInterface(1280);
				});
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		final int id = object.getId();

		if (id == 72450) {
			player.lock(6);
			FadingScreen.fade(
					player,
					() -> {
						leave(0);
						player.getControllerManager().startController(
								"SongFromTheDepths");
					});
		}

		return false;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(player.getDeathAnimation());
				} else if (loop == 1) {
					player.getPackets()
							.sendGameMessage(
									"You feel the effects of the potion fade and you return to the living realm.");
				} else if (loop == 3) {
					player.reset();
					removeController();
					player.getInterfaceManager().removeFadingInterface();
					player.setNextWorldTile(new WorldTile(player.getX(), player
							.getY(), player.getPlane()));
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getMusicsManager().playMusicEffect(
							MusicsManager.DEATH_MUSIC_EFFECT);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void magicTeleported(int type) {
		if (tempvalue) {
			this.forceClose();
		} else {
			tempvalue = true;
		}
	}

	public void leave(int type) {
		if (type == 0) {
			player.reset();
			removeController();
			player.getInterfaceManager().removeFadingInterface();
			player.setNextAnimation(new Animation(-1));
		}
		if (type == 1) {
			player.setNextAnimation(new Animation(-1));
			player.reset();
			removeController();
			player.getInterfaceManager().removeFadingInterface();
		}
		if (type == 2) {
			removeController();
			player.getPackets()
					.sendGameMessage(
							"You feel the effects of the potion fade and you return to the living realm.");
			FadingScreen.fade(player, () -> {
				player.setNextAnimation(new Animation(-1));
				player.reset();
				player.getInterfaceManager().removeFadingInterface();
			});
		}
	}

	@Override
	public boolean logout() {
		leave(0);
		return false;
	}

	@Override
	public boolean login() {
		player.getPackets().sendGameMessage(
				"This should never happen [ERR: LOGIN SFTDS].");
		leave(0);
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		return false;
	}

	@Override
	public void moved() {
		if (player.getX() < 2938 || player.getY() < 3190
				|| player.getX() > 2998 || player.getY() > 3260) {
			leave(2);
		}
	}

	@Override
	public void forceClose() {
		removeController();
		player.setNextAnimation(new Animation(-1));
		player.getInterfaceManager().removeFadingInterface();
		player.reset();
	}
}
