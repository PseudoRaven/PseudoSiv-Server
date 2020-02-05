package net.nocturne.game.player.content;


import java.util.Random;

import net.nocturne.game.*;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class WildernessObelisk {

	/**
	 * Pre-Teleport - 4 Second wait until the player is teleported
	 */
	public static void preTeleport(Player player, final WorldObject object) {
		player.getPackets().sendGameMessage("You activate the obelisk and hear a faint rumbling sound.");
		WorldTasksManager.schedule(new WorldTask() {
			int timer;

			@Override
			public void run() {
				if (timer == 4) {
					handleObelisk(object);
					stop();
				}
				timer++;
			}
		}, 0, 1);
	}

	/**
	 * Teleports all of the players standing on the Obelisk
	 */
	private static void handleObelisk(WorldObject object) {
		final WorldTiles loc = randomWorldTile();
		for (WorldTiles WorldTile : WorldTiles.values()) {
			if (WorldTile.getObjectId() == object.getId()) {
				for (final Player p : World.getPlayers()) {
					if (isOnObelisk(p, WorldTile)) {
						WorldTasksManager.schedule(new WorldTask() {
							int timer;

							@Override
							public void run() {
								p.setNextGraphics(new Graphics(661));
								p.setNextAnimation(new Animation(8939));
								if (timer == 1) {
									p.setNextWorldTile(new WorldTile(
											Utils.random(loc.getWorldTile()
													.getX() - 1, loc
													.getWorldTile().getX() + 1),
											Utils.random(loc.getWorldTile()
													.getY() - 1, loc
													.getWorldTile().getY() + 1),
											0));
									p.setNextAnimation(new Animation(8941));
									stop();
								}
								timer++;
							}
						}, 0, 1);
					}
				}
			}
		}
	}

	/**
	 * Generates a random WorldTile from the WorldTiles enum
	 */
	private static WorldTiles randomWorldTile() {
		int pick = new Random().nextInt(WorldTiles.values().length);
		return WorldTiles.values()[pick];
	}

	/**
	 * Checks if the player is within the Obelisk
	 */
	private static boolean isOnObelisk(WorldTile tile, WorldTiles ob) {
		return (tile.getX() >= ob.getTopLeftX()
				&& tile.getX() <= ob.getBottomRightX()
				&& tile.getY() >= ob.getBottomRightY() && tile.getY() <= ob
				.getTopLeftY());
	}

	private enum WorldTiles {
		WorldTile_1(3154, 3158, 3618, 3622, new WorldTile(3156, 3620, 0), 65619),
		WorldTile_2(3217, 3221, 3654, 3658, new WorldTile(3219, 3656, 0), 65620),
		WorldTile_3(3033, 3037, 3730, 3734, new WorldTile(3035, 3732, 0), 65617),
		WorldTile_4(3104, 3108, 3792, 3796, new WorldTile(3106, 3794, 0), 65618),
		WorldTile_5(2978, 2982, 3864, 3868, new WorldTile(2980, 3866, 0), 65616),
		WorldTile_6(3305, 3309, 3914, 3918, new WorldTile(3307, 3916, 0), 65621);

		private int topLeftX, bottomRightX, bottomRightY, topLeftY;
		private WorldTile WorldTile;
		private int objectId;

		WorldTiles(int topLeftX, int bottomRightX, int bottomRightY,
		           int topLeftY, WorldTile WorldTile, int objectId)
		{

			this.topLeftX=topLeftX;
			this.bottomRightX=bottomRightX;
			this.bottomRightY=bottomRightY;
			this.topLeftY=topLeftY;
			this.WorldTile=WorldTile;
			this.objectId=objectId;
		}

		public int getTopLeftX() {

			return topLeftX;
		}

		public int getBottomRightX() {

			return bottomRightX;
		}

		public int getBottomRightY() {

			return bottomRightY;
		}

		public int getTopLeftY() {

			return topLeftY;
		}

		public WorldTile getWorldTile() {

			return WorldTile;
		}

		public int getObjectId() {

			return objectId;
		}
	}
}
