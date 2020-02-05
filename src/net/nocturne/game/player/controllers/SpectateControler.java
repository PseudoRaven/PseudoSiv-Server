package net.nocturne.game.player.controllers;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

/**
 * @author Taht One Guy (Brennen)
 */
public class SpectateControler extends Controller {

	public transient Player spectating;

	public WorldTile startTile;

	@Override
	public void start() {
		spectating = (Player) this.getArguments()[0];
		startTile = player.getWorldTile();
		spectating.addSpectator(player);
		spectating.refreshHitPoints();
		spectating.getMoneyPouch().refreshCoins();
		spectating.getSkills().init();
		player.setNextWorldTile(spectating);
		// spellbook
		player.getPackets().sendConfig(181,
				spectating.getPrayer().isUsingProtectionPrayer() ? 1 : 0);
		player.getPackets().sendConfig(1584,
				spectating.getPrayer().isAncientCurses() ? 1 : 0);
		player.getPackets().sendItems(93, spectating.getInventory().getItems());
		player.getPackets().sendItems(94, spectating.getEquipment().getItems());
		player.setHitpoints(0);
		player.getAppearence().setHidden(true);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendGameMessage(
				"You are spectating " + spectating.getDisplayName()
						+ " - ::stopspectate to go back to normal.");
	}

	public void stop() {
		if (spectating != null && !spectating.hasFinished()) {
			spectating.removeSpectator(player);
		}
		player.setLocation(startTile);
		player.setNextWorldTile(startTile);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getAppearence().setHidden(false);
			}
		}, 1);
		player.getInventory().init();
		player.getEquipment().init();
		player.getPrayer().init();
		player.getMoneyPouch().refreshCoins();
		player.reset();
		player.getPackets().sendGameMessage(
				"You have stopped spectating " + spectating.getDisplayName()
						+ ".");
		player.getControllerManager().removeControllerWithoutCheck();
	}

	@Override
	public void process() {
		if (spectating.hasWalkSteps()) {
			player.setRun(spectating.getRun());
			player.setRunEnergy(100);
			player.setWalkSteps(spectating.getWalkSteps());
		} else {
			if (player.getX() != spectating.getX()
					&& player.getY() != spectating.getY()) {
				player.setNextWorldTile(new WorldTile(spectating.getX(),
						spectating.getY(), spectating.getPlane()));
			}
		}
		if (spectating.getNextWorldTile() != null) {
			player.setNextWorldTile(spectating.getNextWorldTile());
		}
		if (spectating.hasFinished()) {
			player.getPackets().sendGameMessage(
					"The player you were spectating has logged out.");
			stop();
		}
	}

	@Override
	public void forceClose() {
		player.setLocation(startTile);
		stop();
	}

	@Override
	public boolean logout() {
		stop();
		return true;
	}

	@Override
	public boolean login() {
		player.setNextWorldTile(startTile);
		return true;
	}
}