package net.nocturne.game.npc.godwars2.gregorovic;

import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Gregorovic extends NPC {
	private int phase;
	private boolean spawned1 = false;
	private boolean spawned2 = false;
	public boolean switched1 = false;
	public boolean switched2 = false;

	public Gregorovic(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
		setIntelligentRouteFinder(true);
		resetVariables();
	}

	public void resetVariables() {
		spawned1 = false;
		spawned2 = false;
		switched1 = false;
		switched2 = false;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		int randomX1 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY1 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX2 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY2 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		int randomX3 = Utils.random(GregorovicInstanceController.getX(33),
				GregorovicInstanceController.getX(54));
		int randomY3 = Utils.random(GregorovicInstanceController.getY(33),
				GregorovicInstanceController.getY(54));
		if (!spawned1 && getHitpoints() <= 140000) {
			spawned1 = true;
			Shadow shadow1 = new Shadow(22444, new WorldTile(randomX1,
					randomY1, 1), -1, true, false);
			Shadow shadow2 = new Shadow(22444, new WorldTile(randomX2,
					randomY2, 1), -1, true, false);
			GregorovicInstanceController.firstShadows.add(shadow1);
			GregorovicInstanceController.firstShadows.add(shadow2);
			GregorovicInstanceController.firstShadows.set(0, shadow1);
			GregorovicInstanceController.firstShadows.set(1, shadow2);
			shadow1.setNextAnimation(new Animation(28232));
			shadow2.setNextAnimation(new Animation(28232));
		} else if (!spawned2 && getHitpoints() <= 60000) {
			spawned2 = true;
			Shadow shadow3 = new Shadow(22444, new WorldTile(randomX1,
					randomY1, 1), -1, true, false);
			Shadow shadow4 = new Shadow(22444, new WorldTile(randomX2,
					randomY2, 1), -1, true, false);
			Shadow shadow5 = new Shadow(22444, new WorldTile(randomX3,
					randomY3, 1), -1, true, false);
			GregorovicInstanceController.secondShadows.add(shadow3);
			GregorovicInstanceController.secondShadows.add(shadow4);
			GregorovicInstanceController.secondShadows.add(shadow5);
			GregorovicInstanceController.secondShadows.set(0, shadow3);
			GregorovicInstanceController.secondShadows.set(1, shadow4);
			GregorovicInstanceController.secondShadows.set(2, shadow5);
			shadow3.setNextAnimation(new Animation(28232));
			shadow4.setNextAnimation(new Animation(28232));
			shadow5.setNextAnimation(new Animation(28232));
		}
		if (!switched1 && getHitpoints() <= 140000) {
			switched1 = true;
			GameExecutorManager.slowExecutor
					.schedule(
							() -> {
								int random = Utils.random(3);
								if (random == 0 || random == 1) {
									System.out.println("Random0/1: " + random);
									Shadow shadow = GregorovicInstanceController.firstShadows
											.get(0);
									if (!shadow.isDead())
										shadow.firstSwitchTiles();
								} else if (random == 2 || random == 3) {
									System.out.println("Random2/3: " + random);
									Shadow shadow = GregorovicInstanceController.firstShadows
											.get(1);
									if (!shadow.isDead())
										shadow.firstSwitchTiles();
								}
								switched1 = false;
							}, 1800, TimeUnit.MILLISECONDS);
		}
		if (!switched2 && getHitpoints() <= 60000) {
			switched2 = true;
			GameExecutorManager.slowExecutor
					.schedule(
							() -> {
								int random = Utils.random(5);
								if (random == 0 || random == 1) {
									System.out.println("Random0/1: " + random);
									Shadow shadow = GregorovicInstanceController.secondShadows
											.get(0);
									if (!shadow.isDead())
										shadow.firstSwitchTiles();
								} else if (random == 2 || random == 3) {
									System.out.println("Random2/3: " + random);
									Shadow shadow = GregorovicInstanceController.secondShadows
											.get(1);
									if (!shadow.isDead())
										shadow.firstSwitchTiles();
								} else if (random == 4 || random == 5) {
									System.out.println("Random4/5: " + random);
									Shadow shadow = GregorovicInstanceController.secondShadows
											.get(2);
									if (!shadow.isDead())
										shadow.firstSwitchTiles();
								}
								switched2 = false;
							}, 1800, TimeUnit.MILLISECONDS);
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

		GregorovicInstanceController.firstShadows.forEach(n -> {
			if (n.getId() == 22444) {
				n.sendDeath(source);
			}
		});
		GregorovicInstanceController.secondShadows.forEach(n -> {
			if (n.getId() == 22444) {
				n.sendDeath(source);
			}
		});
		GregorovicInstanceController.firstShadows.clear();
		GregorovicInstanceController.secondShadows.clear();
		super.sendDeath(source);
	}

	@Override
	public void spawn() {
		super.spawn();
		setNextAnimation(new Animation(28264));
		GameExecutorManager.slowExecutor.schedule(() -> {
			GregorovicInstanceController.Player.forEach(player -> {
				player.getInterfaceManager().sendOverlayInterface(1648);
				player.getVarsManager().sendVar(6372, 200000);
				player.getVarsManager().sendVar(5775, 200000);
				phase = 0;
				GameExecutorManager.slowExecutor.schedule(
						() -> {
							player.getPackets().sendIComponentText(1648, 27,
									"Gregorovic");
						}, 6410, TimeUnit.MILLISECONDS);
			});
		}, 6000, TimeUnit.MILLISECONDS);
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