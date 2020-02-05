package net.nocturne.game.npc.dungeonnering;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class HobgoblinGeomancer extends DungeonBoss {

	public HobgoblinGeomancer(int id, WorldTile tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, manager, reference);
	}

	public void sendTeleport(final WorldTile tile, final RoomReference room) {
		setCantInteract(true);
		setNextAnimation(new Animation(12991, 70));
		setNextGraphics(new Graphics(1576, 70, 0));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				setCantInteract(false);
				setNextAnimation(new Animation(-1));
				setNextWorldTile(Utils.getFreeTile(getManager()
						.getRoomCenterTile(room), 6));
				resetReceivedHits();
			}
		}, 5);
	}
}
