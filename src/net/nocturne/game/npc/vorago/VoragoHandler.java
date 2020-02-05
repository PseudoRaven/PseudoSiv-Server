package net.nocturne.game.npc.vorago;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.controllers.bossInstance.VoragoInstanceController;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public final class VoragoHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2729863290488946666L;

	public VoragoHandler(Player player) {
		this.players = Collections.synchronizedList(new ArrayList<Player>());
		this.players.add(player);
		this.owner = player;
	}

	private static List<Player> players = Collections
			.synchronizedList(new ArrayList<Player>());
	private Player owner;

	/*** TODO Make it so they aren't initiated like this ***/
	public static Vorago vorago = new Vorago(17182,
			new WorldTile(3141, 6132, 0), -1, true, true);
	public static VoragoMinion scop1 = new VoragoMinion(17185, new WorldTile(
			3141, 6132, 0), -1, true, true);
	public static VoragoMinion scop2 = new VoragoMinion(17185, new WorldTile(
			3141, 6132, 0), -1, true, true);

	public static int getPlayersCount() {
		return players.size();
	}

	public static List<Player> getPlayers() {
		return players;
	}

	private static void spawnNPCs() {
		deleteNPCS();
		if (vorago == null) {
			vorago = new Vorago(17182, new WorldTile(3141, 6132, 0), -1, true,
					true);
			vorago.setCantInteract(true);
		}
		if (scop1 == null) {
			scop1 = new VoragoMinion(17185, new WorldTile(3141, 6132, 0), -1,
					true, true);
		}
		if (scop2 == null) {
			scop2 = new VoragoMinion(17185, new WorldTile(3141, 6132, 0), -1,
					true, true);
		}
	}

	public void addPlayerInstance(Player player) {
		if (player.getControllerManager().getController() != null
				&& player.getControllerManager().getController() instanceof VoragoInstanceController) {
			((VoragoInstanceController) player.getControllerManager()
					.getController()).getVoragoInstance().getTile(
					vorago.getRandomJump());
			this.players.add(player);
			player.setNextAnimation(new Animation(20401));
		}
	}

	public static void addPlayer(Player player) {
		if (players.contains(player)) {
			player.setNextWorldTile(vorago.getRandomJump());
			System.out.println("Error with VoragoHandler.java");
			return;
		}
		players.add(player);
		// spawnNPCs();
		player.setNextWorldTile(vorago.getRandomJump());
		player.setNextAnimation(new Animation(20401));
	}

	public static void removePlayer(Player player) {
		players.remove(player);
		cancelFight();
	}

	public static void deleteNPCS() {
		if (vorago != null) {
			vorago.finish();
			vorago = null;
		}
		if (scop1 != null) {
			scop1.finish();
			scop1 = null;
		}
		if (scop2 != null) {
			scop2.finish();
			scop2 = null;
		}
	}

	public static void cancelFight() {
		if (getPlayersCount() <= 0) {
			spawnNPCs();
			vorago.resetVariables();
		}
	}

	public static ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(players.size());
		for (Player player : players) {
			if (player == null || player.isDead() || player.hasFinished()
					|| !player.isRunning())
				continue;
			possibleTarget.add(player);
		}
		return possibleTarget;
	}

	public static void endFight() {
		spawnNPCs();
	}

	public static void beginFight() {
		for (Player p : VoragoHandler.getPlayers()) {
			vorago.resetVariables();
			p.getControllerManager().startController("VoragoController");
			WorldTasksManager.schedule(new WorldTask() {
				private int count = 0;

				@Override
				public void run() {

					if (count == 0)

						p.setNextAnimation(new Animation(20401));
					if (count == 5) {
						vorago.setNextWorldTile(vorago.getCentre());
						vorago.setNextAnimation(new Animation(20367));
						vorago.setNextGraphics(new Graphics(4020));
						p.getVarsManager().sendVarBit(18553, 1);

					}
					if (count == 6) {
						if (vorago != null)
							vorago.setCantInteract(false);
						else
							endFight();
						stop();
					}

					count++;
				}
			}, 0, 1);

		}
	}
}
