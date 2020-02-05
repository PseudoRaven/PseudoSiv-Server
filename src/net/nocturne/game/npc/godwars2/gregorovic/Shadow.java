package net.nocturne.game.npc.godwars2.gregorovic;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Shadow extends NPC {

	private int phase;

	public Shadow(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setIntelligentRouteFinder(true);
	}

	@Override
	public void sendDeath(final Entity source) {
		Player player = (Player) source;
		super.sendDeath(source);

	}

	public void firstSwitchTiles() {
		int random = Utils.random(3);
		if (random == 0 || random == 1) {
			NPC shadow = GregorovicInstanceController.firstShadows.get(0);
			GregorovicInstanceController.Gregorovic.forEach(gregorovic -> {
				shadow.setNextWorldTile(new WorldTile(gregorovic.getX(),
						gregorovic.getY(), 1));
				gregorovic.setNextWorldTile(new WorldTile(shadow.getX(), shadow
						.getY(), 1));
				shadow.setNextGraphics(new Graphics(6137));
				gregorovic.setNextGraphics(new Graphics(6137));
			});
		} else if (random == 2 || random == 3) {
			NPC shadow = GregorovicInstanceController.firstShadows.get(1);
			GregorovicInstanceController.Gregorovic.forEach(gregorovic -> {
				shadow.setNextWorldTile(new WorldTile(gregorovic.getX(),
						gregorovic.getY(), 1));
				gregorovic.setNextWorldTile(new WorldTile(shadow.getX(), shadow
						.getY(), 1));
				shadow.setNextGraphics(new Graphics(6137));
				gregorovic.setNextGraphics(new Graphics(6137));
			});
		}
	}

	public void secondSwitchTiles() {
		int random = Utils.random(5);
		if (random == 0 || random == 1) {
			NPC shadow = GregorovicInstanceController.secondShadows.get(0);
			GregorovicInstanceController.Gregorovic.forEach(gregorovic -> {
				if (!shadow.isDead()) {
					shadow.setNextWorldTile(new WorldTile(gregorovic.getX(),
							gregorovic.getY(), 1));
					gregorovic.setNextWorldTile(new WorldTile(shadow.getX(),
							shadow.getY(), 1));
					shadow.setNextGraphics(new Graphics(6137));
					gregorovic.setNextGraphics(new Graphics(6137));
				}
			});
		} else if (random == 2 || random == 3) {
			NPC shadow = GregorovicInstanceController.secondShadows.get(1);
			GregorovicInstanceController.Gregorovic.forEach(gregorovic -> {
				if (!shadow.isDead()) {
					shadow.setNextWorldTile(new WorldTile(gregorovic.getX(),
							gregorovic.getY(), 1));
					gregorovic.setNextWorldTile(new WorldTile(shadow.getX(),
							shadow.getY(), 1));
					shadow.setNextGraphics(new Graphics(6137));
					gregorovic.setNextGraphics(new Graphics(6137));
				}
			});
		} else if (random == 4 || random == 5) {
			NPC shadow = GregorovicInstanceController.secondShadows.get(2);
			GregorovicInstanceController.Gregorovic.forEach(gregorovic -> {
				if (!shadow.isDead()) {
					shadow.setNextWorldTile(new WorldTile(gregorovic.getX(),
							gregorovic.getY(), 1));
					gregorovic.setNextWorldTile(new WorldTile(shadow.getX(),
							shadow.getY(), 1));
					shadow.setNextGraphics(new Graphics(6137));
					gregorovic.setNextGraphics(new Graphics(6137));
				}
			});
		}
	}

	@Override
	public void setRespawnTask() {

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