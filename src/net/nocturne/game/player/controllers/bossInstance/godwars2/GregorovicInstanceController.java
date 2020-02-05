package net.nocturne.game.player.controllers.bossInstance.godwars2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.godwars2.gregorovic.Gregorovic;
import net.nocturne.game.npc.godwars2.gregorovic.Shadow;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.Controller;

public class GregorovicInstanceController extends Controller {

	private static int[] boundChunks;

	public static List<Shadow> firstShadows = new ArrayList<Shadow>();
	public static List<Shadow> secondShadows = new ArrayList<Shadow>();
	public static List<Player> Player = new ArrayList<Player>();
	public static List<NPC> Gregorovic = new ArrayList<NPC>();

	static int sizeX = 8;
	static int sizeY = 8;

	static int chunkX = 407;
	static int chunkY = 879;
	int x, y;

	@Override
	public void start() {
		Object[][][][] data = new Object[4][16][16][];
		Player.add(player);
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
			player.getPackets().sendIComponentText(1648, 27, "Gregorovic");
		}, 6500, TimeUnit.MILLISECONDS);
		player.unlock();
		player.setNextWorldTile(getWorldTile(32, 32, 1));
		Gregorovic gregorovic = new Gregorovic(22443, new WorldTile(
				getWorldTile(43, 43, 1)), -1, true, false);
		Gregorovic.add(gregorovic);
		gregorovic.setNextAnimation(new Animation(28223));
		gregorovic.resetVariables();
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		endDungeon(true);
		removeController();
		removeInterfaces();
		firstShadows.removeAll(firstShadows);
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
			player.setNextWorldTile(new WorldTile(3287, 7062, 1));
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