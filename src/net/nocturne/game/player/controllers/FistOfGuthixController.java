package net.nocturne.game.player.controllers;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.fistofguthix.MinigameManager;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class FistOfGuthixController extends Controller {

	@Override
	public void start() {
		player.getInventory().addItem(12850, 1000);
		player.getInventory().addItem(12851, 300);
		player.getInventory().addItem(12853, 1);
		player.getInventory().addItem(12853, 1);
		player.getInventory().addItem(12853, 1);
		player.getInventory().addItem(12853, 1);
		player.getInventory().addItem(12853, 1);
		player.getInventory().addItem(12855, 1);
		player.reset(false);
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean canAttack(Entity target) {
		target = (Player) target;
		return !(MinigameManager.INSTANCE().fistOfGuthix().lobbyMembers()
				.contains(target)
				|| MinigameManager.INSTANCE().fistOfGuthix().lobbyMembers()
						.contains(player)
				|| !MinigameManager.INSTANCE().fistOfGuthix().gameMembers()
						.contains(target) || !MinigameManager.INSTANCE()
				.fistOfGuthix().gameMembers().contains(player))
				&& !(target == null
						|| MinigameManager.INSTANCE().fistOfGuthix()
								.team(player).hunted() == null || MinigameManager
						.INSTANCE().fistOfGuthix().team(player).hunter() == null)
				&& (MinigameManager.INSTANCE().fistOfGuthix().team(player)
						.hunted() == target || MinigameManager.INSTANCE()
						.fistOfGuthix().team(player).hunter() == target);
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
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					MinigameManager.INSTANCE().fistOfGuthix().death(player);
					player.setNextAnimation(new Animation(-1));
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90, 0);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		exit(true);
		return true;
	}

	@Override
	public boolean logout() {
		exit(true);
		if (MinigameManager.INSTANCE().fistOfGuthix().team(player) != null) {// if
																				// the
																				// player
																				// !=
																				// null,
																				// it'll
																				// remove
																				// them
																				// from
																				// their
																				// team
			MinigameManager.INSTANCE().fistOfGuthix().team(player)
					.forfeit(player);
			exit(true);
		} else {// else, it'll remove them from the game / lobby
			if (MinigameManager.INSTANCE().fistOfGuthix().lobbyMembers()
					.contains(player))
				MinigameManager.INSTANCE().fistOfGuthix().lobbyMembers()
						.remove(player);
			else if (MinigameManager.INSTANCE().fistOfGuthix().gameMembers()
					.contains(player))
				MinigameManager.INSTANCE().fistOfGuthix().gameMembers()
						.remove(player);
			exit(false);
		}
		return false;// doesnt remove the controller; removed upon login.
	}

	public void exit() {
		exit(false);
	}

	public void exit(boolean tele) {
		if (tele)
			player.setNextWorldTile(new WorldTile(1698, 5600, 0));
		player.getInventory().deleteItem(12850, 1000);
		player.getInventory().deleteItem(12851, 300);
		player.getInventory().deleteItem(12853, 1);
		player.getInventory().deleteItem(12853, 1);
		player.getInventory().deleteItem(12853, 1);
		player.getInventory().deleteItem(12853, 1);
		player.getInventory().deleteItem(12853, 1);
		player.getInventory().deleteItem(12855, 1);
		player.getInventory().deleteItem(12845, 1);
		player.getEquipment().deleteItem(12845, 1);
	}

}
