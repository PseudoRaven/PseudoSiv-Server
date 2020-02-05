package net.nocturne.game.npc.godwars2.helwyr;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.impl.godwars2.HelwyrCombat;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.HelwyrInstanceController;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")

public class Helwyr extends NPC {
	private int phase;

	public Helwyr(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		Player target = HelwyrInstanceController.Player.get(0);
		if (getPhase() >= 1 && getPhase() <= 32){
			if (HelwyrCombat.random1 != null && HelwyrCombat.random2 != null && HelwyrCombat.random3 != null)
				if (target.withinDistance(HelwyrCombat.random1, 2) || target.withinDistance(HelwyrCombat.random2, 2)
						|| target.withinDistance(HelwyrCombat.random3, 2)) {
					target.applyHit(new Hit(target, Utils.random(100, 250), HitLook.REGULAR_DAMAGE));
				}
		} 
		if (HelwyrCombat.random4 != null && HelwyrCombat.random5 != null && HelwyrCombat.random6 != null) {
			if (target.withinDistance(HelwyrCombat.random4, 2) || target.withinDistance(HelwyrCombat.random5, 2)
							|| target.withinDistance(HelwyrCombat.random6, 2)) {
				target.applyHit(new Hit(target, Utils.random(100, 250), HitLook.REGULAR_DAMAGE));
			}
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		Player player = (Player) hit.getSource();
		player.getVarsManager().sendVarBit(28663, getHitpoints());
		player.getVarsManager().sendVar(5775, getHitpoints() - hit.getDamage());
	}

	@Override
	public void sendDeath(final Entity source) {
		Player player = (Player) source;
		player.getInterfaceManager().removeInterface(1648);
		player.getVarsManager().sendVar(6372, 0);
		player.getVarsManager().sendVar(5775, 0);
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random1)));
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random2)));
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random3)));
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random4)));
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random5)));
		World.spawnObject(new WorldObject(101899, 10, 3, new WorldTile(HelwyrCombat.random6)));
		HelwyrInstanceController.Tiles.clear();
		HelwyrCombat.random1 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrCombat.random2 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrCombat.random3 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrCombat.random4 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrCombat.random5 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrCombat.random6 = new WorldTile(HelwyrInstanceController.getWorldTile(0, 63, 1));
		HelwyrInstanceController.Wolves.forEach(n -> {
			n.sendDeath(source);
		});
		super.sendDeath(source);
	}

	@Override
	public void spawn() {
		super.spawn();
		setNextAnimation(new Animation(28200));
		setNextGraphics(new Graphics(6120));
		setNextGraphics(new Graphics(6085));
		GameExecutorManager.slowExecutor.schedule(() -> {
			HelwyrInstanceController.Player.forEach(player -> {
				player.getInterfaceManager().sendOverlayInterface(1648);
				player.getVarsManager().sendVar(6372, 200000);
				player.getVarsManager().sendVar(5775, 200000);
				phase = 0;
				GameExecutorManager.slowExecutor.schedule(() -> {
					player.getPackets().sendIComponentText(1648, 27, "Helwyr");
				}, 6410, TimeUnit.MILLISECONDS);
			});
		}, 2400, TimeUnit.MILLISECONDS);
	}

	public int getPhase() {
		return phase;
	}

	public void nextPhase() {
		phase++;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

}
