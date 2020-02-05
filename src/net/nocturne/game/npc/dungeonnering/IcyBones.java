package net.nocturne.game.npc.dungeonnering;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public final class IcyBones extends DungeonBoss {

	public IcyBones(int id, WorldTile tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, manager, reference);
		spikes = new ArrayList<WorldObject>();
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	private List<WorldObject> spikes;

	public void removeSpikes() {
		if (spikes.isEmpty())
			return;
		for (WorldObject object : spikes)
			World.removeObject(object);
		spikes.clear();
	}

	public boolean sendSpikes() {
		if (!spikes.isEmpty())
			return false;
		int size = getSize();
		for (int x = -1; x < 7; x++) {
			for (int y = -1; y < 7; y++) {
				if (((x != -1 && x != 6) && (y != -1 && y != 6))
						|| Utils.random(2) != 0)
					continue;
				WorldTile tile = transform(x - size, y - size, 0);
				RoomReference current = getManager().getCurrentRoomReference(
						tile);
				if (current.getX() != getReference().getX()
						|| current.getY() != getReference().getY()
						|| !World.isFloorFree(tile.getPlane(), tile.getX(),
								tile.getY()))
					continue;
				WorldObject object = new WorldObject(52285 + Utils.random(3),
						10, Utils.random(4), tile.getX(), tile.getY(),
						tile.getPlane());
				spikes.add(object);
				World.spawnObject(object);
				for (Player player : getManager().getParty().getTeam()) {
					if (player.getX() == object.getX()
							&& player.getY() == object.getY())
						player.applyHit(new Hit(this, 1 + Utils
								.random(getMaxHit()), HitLook.REGULAR_DAMAGE));
				}
			}
		}
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				removeSpikes();
			}

		}, 10);
		return true;
	}

}
