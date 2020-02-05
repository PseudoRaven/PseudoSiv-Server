package net.nocturne.game.player.controllers.bossInstance.godwars2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.impl.godwars2.HelwyrCombat;
import net.nocturne.game.npc.godwars2.helwyr.CywirAlpha;
import net.nocturne.game.npc.godwars2.helwyr.Helwyr;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.Controller;

public class HelwyrInstanceController extends Controller {

	private static int[] boundChunks;

	public static List<CywirAlpha> Wolves = new ArrayList<CywirAlpha>();
	public static List<Player> Player = new ArrayList<Player>();
	public static List<NPC> Helwyr = new ArrayList<NPC>();
	public static List<WorldTile> Tiles = new ArrayList<WorldTile>();

	static int sizeX = 8;
	static int sizeY = 8;

	static int chunkX = 405;
	static int chunkY = 857;
	int x, y;

	@Override
	public void start() {
		Object[][][][] data = new Object[4][16][16][];
		Player.add(player);
		Player.set(0, player);
		player.lock();
		boundChunks = MapBuilder.findEmptyChunkBound(sizeX, sizeY);
		MapBuilder.copyAllPlanesMap(chunkX, chunkY, boundChunks[0] + x,
				boundChunks[1] + y, 8, 8);
		GameExecutorManager.slowExecutor.schedule(() -> {
			player.getInterfaceManager().sendOverlayInterface(1648);
			player.getVarsManager().sendVar(6372, 200000);
			player.getVarsManager().sendVar(5775, 200000);
		}, 2400, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(() -> {
			player.getPackets().sendIComponentText(1648, 27, "Helwyr");
		}, 6500, TimeUnit.MILLISECONDS);
		player.unlock();
		player.setNextWorldTile(getWorldTile(40, 39, 1));
		Helwyr helwyr = new Helwyr(22438,
				new WorldTile(getWorldTile(52, 25, 1)), -1, true, false);
		Helwyr.add(helwyr);
		helwyr.setNextAnimation(new Animation(28200));
		helwyr.setNextGraphics(new Graphics(6120));
		helwyr.setNextGraphics(new Graphics(6085));
		addTiles();
		setTiles();
	}

	public static void addTiles() {
		Tiles.add(new WorldTile(getWorldTile(42, 18, 1)));
		Tiles.add(new WorldTile(getWorldTile(42, 23, 1)));
		Tiles.add(new WorldTile(getWorldTile(42, 28, 1)));
		Tiles.add(new WorldTile(getWorldTile(42, 33, 1)));
		Tiles.add(new WorldTile(getWorldTile(42, 38, 1)));
		Tiles.add(new WorldTile(getWorldTile(46, 18, 1)));
		Tiles.add(new WorldTile(getWorldTile(47, 23, 1)));
		Tiles.add(new WorldTile(getWorldTile(47, 28, 1)));
		Tiles.add(new WorldTile(getWorldTile(47, 33, 1)));
		Tiles.add(new WorldTile(getWorldTile(47, 38, 1)));
		Tiles.add(new WorldTile(getWorldTile(51, 18, 1)));
		Tiles.add(new WorldTile(getWorldTile(52, 23, 1)));
		Tiles.add(new WorldTile(getWorldTile(52, 28, 1)));
		Tiles.add(new WorldTile(getWorldTile(52, 33, 1)));
		Tiles.add(new WorldTile(getWorldTile(52, 37, 1)));
		Tiles.add(new WorldTile(getWorldTile(56, 18, 1)));
		Tiles.add(new WorldTile(getWorldTile(57, 23, 1)));
		Tiles.add(new WorldTile(getWorldTile(57, 28, 1)));
		Tiles.add(new WorldTile(getWorldTile(57, 33, 1)));
		Tiles.add(new WorldTile(getWorldTile(56, 38, 1)));
		Tiles.add(new WorldTile(getWorldTile(61, 18, 1)));
		Tiles.add(new WorldTile(getWorldTile(62, 23, 1)));
		Tiles.add(new WorldTile(getWorldTile(61, 27, 1)));
		Tiles.add(new WorldTile(getWorldTile(62, 32, 1)));
		Tiles.add(new WorldTile(getWorldTile(61, 37, 1)));
	}

	public static void setTiles() {
		Tiles.set(0, new WorldTile(getWorldTile(42, 18, 1)));
		Tiles.set(1, new WorldTile(getWorldTile(42, 23, 1)));
		Tiles.set(2, new WorldTile(getWorldTile(42, 28, 1)));
		Tiles.set(3, new WorldTile(getWorldTile(42, 33, 1)));
		Tiles.set(4, new WorldTile(getWorldTile(42, 38, 1)));
		Tiles.set(5, new WorldTile(getWorldTile(46, 18, 1)));
		Tiles.set(6, new WorldTile(getWorldTile(47, 23, 1)));
		Tiles.set(7, new WorldTile(getWorldTile(47, 28, 1)));
		Tiles.set(8, new WorldTile(getWorldTile(47, 33, 1)));
		Tiles.set(9, new WorldTile(getWorldTile(47, 38, 1)));
		Tiles.set(10, new WorldTile(getWorldTile(51, 18, 1)));
		Tiles.set(11, new WorldTile(getWorldTile(52, 23, 1)));
		Tiles.set(12, new WorldTile(getWorldTile(52, 28, 1)));
		Tiles.set(13, new WorldTile(getWorldTile(52, 33, 1)));
		Tiles.set(14, new WorldTile(getWorldTile(52, 37, 1)));
		Tiles.set(15, new WorldTile(getWorldTile(56, 18, 1)));
		Tiles.set(16, new WorldTile(getWorldTile(57, 23, 1)));
		Tiles.set(17, new WorldTile(getWorldTile(57, 28, 1)));
		Tiles.set(18, new WorldTile(getWorldTile(57, 33, 1)));
		Tiles.set(19, new WorldTile(getWorldTile(56, 38, 1)));
		Tiles.set(20, new WorldTile(getWorldTile(61, 18, 1)));
		Tiles.set(21, new WorldTile(getWorldTile(62, 23, 1)));
		Tiles.set(22, new WorldTile(getWorldTile(61, 27, 1)));
		Tiles.set(23, new WorldTile(getWorldTile(62, 32, 1)));
		Tiles.set(24, new WorldTile(getWorldTile(61, 37, 1)));
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		endDungeon(true);
		removeController();
		removeInterfaces();
		return true;
	}

	public void removeInterfaces() {
		player.getInterfaceManager().removeInterface(1648);
		player.getInterfaceManager().removeInterface(1746);
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		endDungeon(true);
		removeController();
		removeInterfaces();
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		endDungeon(true);
		removeController();
		removeInterfaces();
		return false;
	}

	@Override
	public boolean logout() {
		// endDungeon(true);
		return true;
	}

	@Override
	public boolean login() {
		endDungeon(true);
		removeController();
		GameExecutorManager.slowExecutor.schedule(() -> {
			player.setNextWorldTile(new WorldTile(3279, 6897, 1));
			player.getControllerManager().startController("GodWars2");
		}, 600, TimeUnit.MILLISECONDS);
		return true;
	}

	@Override
	public boolean sendDeath() {
		endDungeon(true);
		removeController();
		removeInterfaces();
		player.getControllerManager().startController("DeathEvent");
		return true;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		return true;
	}

	private void removeMapChunks() {
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				MapBuilder.destroyMap(boundChunks[0], boundChunks[1], sizeX,
						sizeY);
			}
		}, 3500, TimeUnit.MILLISECONDS);
	}

	public void endDungeon(boolean logout) {
		Tiles.clear();
		if (HelwyrCombat.random1 != null && HelwyrCombat.random2 != null
				&& HelwyrCombat.random3 != null && HelwyrCombat.random4 != null
				&& HelwyrCombat.random5 != null && HelwyrCombat.random6 != null) {
			HelwyrCombat.random1 = new WorldTile(getWorldTile(0, 63, 1));
			HelwyrCombat.random2 = new WorldTile(getWorldTile(0, 63, 1));
			HelwyrCombat.random3 = new WorldTile(getWorldTile(0, 63, 1));
			HelwyrCombat.random4 = new WorldTile(getWorldTile(0, 63, 1));
			HelwyrCombat.random5 = new WorldTile(getWorldTile(0, 63, 1));
			HelwyrCombat.random6 = new WorldTile(getWorldTile(0, 63, 1));
		}
		removeMapChunks();
	}

	public static WorldTile getWorldTile(int mapX, int mapY, int plane) {
		return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8
				+ mapY, plane);
	}

	public static int getX(int mapX) {
		return boundChunks[0] * 8 + mapX;
	}

	public static int getY(int mapY) {
		return boundChunks[1] * 8 + mapY;
	}

}