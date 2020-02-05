package net.nocturne.game.npc.others;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Entity;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Logger;

@SuppressWarnings("serial")
public class DoorSupport extends NPC {

	public DoorSupport(int id, WorldTile tile) {
		super(id, tile, -1, true, true);
		setCantFollowUnderCombat(true);
	}

	@Override
	public void processNPC() {
		cancelFaceEntityNoCheck();
	}

	public boolean canDestroy(Player player) {
		if (getId() == 2446)
			return player.getY() < getY();
		if (getId() == 2440)
			return player.getY() > getY();
		return player.getX() > getX();
	}

	@Override
	public void sendDeath(Entity killer) {
		setNextNPCTransformation(getId() + 1);
		final WorldObject door = World.getObjectWithId(this, 8967);
		if (door != null)
			World.removeObject(door);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					setNextNPCTransformation(getId() - 1);
					reset();
					if (door != null)
						World.spawnObject(door);
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, 60, TimeUnit.SECONDS);
	}

}
