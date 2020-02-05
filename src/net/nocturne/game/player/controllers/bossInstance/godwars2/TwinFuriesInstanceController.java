package net.nocturne.game.player.controllers.bossInstance.godwars2;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.godwars2.twinfuries.Avaryss;
import net.nocturne.game.npc.godwars2.twinfuries.Nymora;
import net.nocturne.game.player.controllers.Controller;

public class TwinFuriesInstanceController extends Controller {

	private static int[] boundChunks;

	static int sizeX = 8;
	static int sizeY = 8;

	static int chunkX = 384;
	static int chunkY = 880;
	int x, y;

	@Override
	public void start() {
		Object[][][][] data = new Object[4][16][16][];
		player.lock();
		System.out.println("controller");
		boundChunks = MapBuilder.findEmptyChunkBound(sizeX, sizeY);
		MapBuilder.copyAllPlanesMap(chunkX, chunkY, boundChunks[0] + x,
				boundChunks[1] + y, 8, 8);
		GameExecutorManager.slowExecutor.schedule(() -> {
			player.getInterfaceManager().sendOverlayInterface(1648);
			player.getVarsManager().sendVar(6372, 200000);
			player.getVarsManager().sendVar(5775, 200000);
		}, 2400, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(
				() -> {
					player.getPackets().sendIComponentText(1648, 27,
							"The Twin Furies");
				}, 6500, TimeUnit.MILLISECONDS);
		player.unlock();
		player.setNextWorldTile(getWorldTile(33, 30, 1));
		Avaryss avaryss = new Avaryss(22455, new WorldTile(getWorldTile(27, 36,
				1)), -1, true, false);
		Nymora nymora = new Nymora(22456,
				new WorldTile(getWorldTile(32, 36, 1)), -1, true, false);
		avaryss.setNextAnimation(new Animation(28242));
		nymora.setNextAnimation(new Animation(28242));
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
			player.setNextWorldTile(new WorldTile(3115, 7060, 1));
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

}