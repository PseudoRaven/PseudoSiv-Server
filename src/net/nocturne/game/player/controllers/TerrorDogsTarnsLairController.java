package net.nocturne.game.player.controllers;

import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;

class TerrorDogsTarnsLairController extends Controller {

	private boolean drain;

	@Override
	public void start() {
		player.useStairs(-1, new WorldTile(3149, 4658, 0), 0, 1);
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 20572 && object.getX() == 3149
				&& object.getY() == 4659) {
			player.useStairs(-1, new WorldTile(3149, 4664, 0), 0, 1);
			removeController();
			return false;
		}
		return true;
	}

	@Override
	public void process() {
		if ((drain = !drain))
			player.getPrayer().drain(10);
	}

	@Override
	public boolean login() {
		return false;
	}

	@Override
	public boolean sendDeath() {
		removeController();
		return true;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {

	}

	@Override
	public void magicTeleported(int type) {
		removeController();
	}
}
