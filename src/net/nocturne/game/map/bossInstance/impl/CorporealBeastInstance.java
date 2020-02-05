package net.nocturne.game.map.bossInstance.impl;

import net.nocturne.game.World;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.map.bossInstance.InstanceSettings;
import net.nocturne.game.player.Player;

public class CorporealBeastInstance extends BossInstance {

	public CorporealBeastInstance(Player owner, InstanceSettings settings) {
		super(owner, settings);
	}

	@Override
	public int[] getMapPos() {
		return new int[] { 368, 544 };
	}

	@Override
	public int[] getMapSize() {
		return new int[] { 1, 1 };
	}

	@Override
	public void loadMapInstance() {
		World.spawnNPC(8133, getTile(2993, 4378, 2), -1, true, false)
				.setBossInstance(this);
	}
}