package net.nocturne.game.player.content.activities.partyroom;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

/**
 * @author Miles Black (bobismyname)
 * @date Nov 2, 2016
 */

public class Balloon extends WorldObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6014958764470013424L;
	private Item item;
	private boolean popped = false;

	public Balloon(int id, int type, int rotation, int x, int y, int plane) {
		super(id, type, rotation, x, y, plane);
	}

	public void handlePop(final Player player) {
		if (!popped) {
			if (player.isAnIronMan())
				return;
			player.setNextAnimation(new Animation(794));
			popped = true;
			player.lock();
			World.removeObject(this);
			final WorldObject poppedBalloon = new WorldObject(getId() + 8, 10,
					this.getRotation(), getX(), getY(), getPlane());
			World.spawnObject(poppedBalloon);

			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (item != null)
						World.addGroundItem(item, new WorldTile(getX(), getY(),
								getPlane()), player, true, 60, 2);
					World.removeObject(poppedBalloon);
					player.unlock();
				}
			}, 1);
		}
	}

	public Balloon setItem(Item item) {
		this.item = item;
		return this;
	}

}