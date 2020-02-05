package net.nocturne.game.player.controllers;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.map.MapInstance;
import net.nocturne.game.map.MapInstance.Stages;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.others.Bork;
import net.nocturne.game.player.MusicsManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class BorkController extends Controller {

	private static final WorldTile OUTSIDE = new WorldTile(3143, 5545, 0);
	private MapInstance instance;
	private NPC surokMagis;
	private Bork bork;
	private boolean earthquake;
	private int timer;

	public static void enterBork(Player player) {
		player.getControllerManager().startController("BorkController");
	}

	@Override
	public void start() {
		if (!player.canFightBork()) {
			player.getControllerManager().removeControllerWithoutCheck();
			player.getPackets().sendGameMessage(
					"You can only fight bork once every hour.");
			return;
		}
		player.setLastFoughtBork(Utils.currentTimeMillis());
		enter();
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 77745) {
			leave(2);
			return false;
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == 7136 || npc.getId() == 7137) {
			player.getPackets().sendGameMessage("Your attack has no effect.");
			return false;
		}
		return true;
	}

	private void enter() {
		instance = new MapInstance(385, 690);

		player.setNextAnimation(new Animation(17803));
		final long time = FadingScreen.fade(player);
		instance.load(() -> FadingScreen.unfade(
				player,
				time,
				() -> {
					player.setNextWorldTile(instance.getTile(25, 17));
					surokMagis = World.spawnNPC(
							player.getLastBork() != 0 ? 7137 : 7136,
							instance.getTile(22, 20), -1, true, true);
					surokMagis.setCantSetTargetAutoRelatio(true);
					bork = new Bork(instance.getTile(12, 15),
							BorkController.this);
					startFight();
				}));
	}

	public void leave(int type) {
		player.stopAll();
		if (type != 0) {
			if (type == 1)
				player.lock(3);
			else
				player.useStairs(17803, OUTSIDE, 2, 3);
			player.getMusicsManager().reset();
			if (earthquake)
				player.getPackets().sendStopCameraShake();
			removeController();
		} else
			player.setLocation(OUTSIDE);
		instance.destroy(null);
	}

	@Override
	public boolean logout() {
		leave(0);
		return true;
	}

	@Override
	public boolean login() {
		player.setNextWorldTile(OUTSIDE);
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		leave(1);
	}

	@Override
	public boolean sendDeath() {
		player.lock(8);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(player.getDeathAnimation());
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					leave(1);
					player.getControllerManager().startController("DeathEvent",
							OUTSIDE, player.hasSkull());
				} else if (loop == 4) {
					player.getMusicsManager().playMusicEffect(
							MusicsManager.DEATH_MUSIC_EFFECT);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;

	}

	public void startFight() {
		player.unlock();
		bork.setCantInteract(false);
		surokMagis.setTarget(player);
	}

	public void startEarthquake() {
		player.unlock();
		player.getPackets()
				.sendGameMessage(
						"Something is shaking the whole cavern! You should get out of here quick!");
		player.getPackets().sendCameraShake(3, 12, 25, 12, 25);
		earthquake = true;
		timer = 30;
	}

	@Override
	public void process() {
		if (!earthquake)
			return;
		if (timer > 0) {
			timer--;
			return;
		}
		player.applyHit(new Hit(player, Utils.random(499) + 1,
				HitLook.REGULAR_DAMAGE));
		timer = 30;
	}

	public void spawnMinions() {
		player.getPackets().sendGameMessage(
				"Bork strikes the ground with his axe.");
	}

	public void killBork() {
		bork = null;
		surokMagis.setCantInteract(true);
		surokMagis.setNextFaceEntity(null);
		surokMagis.setNextWorldTile(instance.getTile(22, 20));
		player.getSkills().addXp(Skills.SLAYER,
				player.getLastBork() == 0 ? 500 : 300);
		player.setLastBork(Utils.currentTimeMillis());
		player.getPackets().sendGameMessage("You have Defeated Bork!");
		player.getPackets().sendGameMessage(
				"Please leave the room via the portal when you are ready!");
		player.getPackets()
				.sendGameMessage(
						"if you wish to start again, you will need to leave & Re-enter.");
		surokMagis.setNextAnimation(new Animation(8939));
		surokMagis.setNextGraphics(new Graphics(1576));
	}

	public Stages getStage() {
		return instance.getStage();
	}

}