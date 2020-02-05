package net.nocturne.game.player.controllers;

import java.util.concurrent.TimeUnit;

import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Color;

public class LucienController extends Controller {

	public static final WorldTile OUTSIDE = Settings.HOME_LOCATION;
	public static final WorldTile teleTile = new WorldTile(2228, 4245, 1);

	@Override
	public boolean canEat(int heal) {
		if (player.getInventory()
				.containsItem(ItemIdentifiers.PURPLE_SWEETS, 1)) {
			player.getPackets()
					.sendGameMessage(Color.RED,
							"You can't use purple sweets at this boss, please remove them.");
			return false;
		}
		return true;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean login() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		leave();
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		leave();
		return true;
	}

	public void leave() {
		player.setForceMultiArea(false);
		removeController();
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 27331) {
			player.getPackets().sendGameMessage(Color.RED,
					"You teleport to home!");
			leave();
			player.setNextWorldTile(OUTSIDE);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(Color.RED,
							"You have been defeated by Lucien!");
				} else if (loop == 3) {
					player.reset();
					player.setNextWorldTile(OUTSIDE);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffectOld(90);
					leave();
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public void start() {
		player.lock();
		final long time = FadingScreen.fade(player);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				FadingScreen.unfade(player, time, new Runnable() {
					@Override
					public void run() {
						final int maxPrayer = player.getSkills().getLevelForXp(
								Skills.PRAYER) * 10;
						player.setNextWorldTile(teleTile);
						player.getControllerManager().magicTeleported(1);
						if (player.getControllerManager().getController() == null)
							Magic.teleControllersCheck(player, teleTile);
						player.setNextFaceWorldTile(new WorldTile(teleTile
								.getX(), teleTile.getY() - 1, teleTile
								.getPlane()));
						player.setDirection(6);
						player.getPrayer().boost(maxPrayer);
						player.heal(player.getHitpoints() * 10);
						player.getCombatDefinitions().resetSpells(true);
						player.setForceMultiArea(true);
						player.stopAll();
						player.unlock();
					}
				});
			}
		}, 3000, TimeUnit.MILLISECONDS);
	}

}
