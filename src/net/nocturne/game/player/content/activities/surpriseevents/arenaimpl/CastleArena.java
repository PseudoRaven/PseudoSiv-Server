package net.nocturne.game.player.content.activities.surpriseevents.arenaimpl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.player.content.activities.surpriseevents.EventArena;
import net.nocturne.utils.Utils;

public class CastleArena extends EventArena {

	/**
	 * Contains base positions.
	 */
	private int[] base;

	public CastleArena(boolean multicombat) {
		super(multicombat);
	}

	@Override
	public void create() {
		if (base != null)
			throw new RuntimeException("Area already created.");
		int b = MapBuilder.findEmptyRegionHash(8, 8);
		base = new int[] { (b >> 8) << 6, (b & 0xFF) << 6 };
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				int chunkX = (base[0] >> 3) + x;
				int chunkY = (base[1] >> 3) + y;
				MapBuilder.copyChunk((44 << 3) + x, (79 << 3) + y, 0, chunkX,
						chunkY, 0, 0);
			}
		}
		registerArena();
	}

	@Override
	public void destroy() {
		if (base == null)
			throw new RuntimeException("Area already destroyed.");
		MapBuilder.destroyMap(base[0] >> 3, base[1] >> 3, 16, 16);
		base = null;
		unregisterArena();
	}

	@Override
	public int minX() {
		return base[0];
	}

	@Override
	public int minY() {
		return base[1];
	}

	@Override
	public int maxX() {
		return base[0] + (8 << 3);
	}

	@Override
	public int maxY() {
		return base[1] + (8 << 3);
	}

	@Override
	public WorldTile randomSpawn() {
		return new WorldTile(minX() + 29 + Utils.random(6), minY() + 12
				+ Utils.random(6), 0);

	}

}
