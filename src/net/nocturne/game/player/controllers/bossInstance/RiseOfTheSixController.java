package net.nocturne.game.player.controllers.bossInstance;

import java.util.concurrent.TimeUnit;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.map.bossInstance.impl.RotsInstance;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.others.GraveStone;
import net.nocturne.game.player.MusicsManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.player.controllers.events.DeathEvent;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class RiseOfTheSixController extends Controller {

	private BossInstance instance;
	private boolean claimed = false;

	private static final Item[] COMMON_REWARDS = {
			new Item(30030, Utils.random(2, 6)),
			new Item(1748, Utils.random(60, 80)),
			new Item(9245, Utils.random(50, 65)),
			new Item(1128, Utils.random(6, 10)),
			new Item(1514, Utils.random(190, 210)),
			new Item(452, Utils.random(30, 34)),
			new Item(15271, Utils.random(120, 150)),
			new Item(5303, Utils.random(6, 10)),
			new Item(5302, Utils.random(6, 10)),
			new Item(5316, Utils.random(2, 4)),
			new Item(1392, Utils.random(35, 45)) };

	private static final Item[] RARE_REWARDS = { new Item(30014, 1),
			new Item(30018, 1), new Item(30022, 1) };

	@Override
	public void start() {
		instance = (BossInstance) getArguments()[0]; // cant save this as not
														// serializable would
														// null :L
		getArguments()[0] = instance.getBoss(); // gotta save reference for
												// which instance im at
		getRotsInstance().enterBattle(player);
	}

	@Override
	public void process() {
		if (instance != null && Utils.currentWorldCycle() % 120 == 0)
			instance.playMusic(player);
	}

	@Override
	public boolean login() {
		player.setNextWorldTile(new WorldTile(3542, 3310, 0));
		return true;
	}

	public BossInstance getInstance() {
		return instance;
	}

	public void sendReward() {
		int chance = Utils.random(1, 1000);
		if (chance >= 990) {
			drop(RARE_REWARDS[Utils.random(RARE_REWARDS.length)]);
		}
		if (chance <= 989 && chance >= 200)
			drop(COMMON_REWARDS[Utils.random(COMMON_REWARDS.length)]);
		drop(new Item(30026, Utils.random(1, 3))); // money reward at least
													// always

	}

	public void drop(Item item) {
		player.getInventory().addItemDrop(item.getId(), item.getAmount());
		if (item.getDefinitions().getName().contains("kiteshield")) {
			Engine.getDiscordBot()
					.getChannel("287324187079475202")
					.sendMessage(
							player.getDisplayName() + " has received "
									+ item.getName().toLowerCase() + " drop!");
			World.sendNews(player, player.getDisplayName() + " has received "
					+ item.getName().toLowerCase() + " drop!", 1, false);
		}
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 88065 && player.getY() > object.getY()) {
			player.lock(6);
			WorldTasksManager.schedule(new WorldTask() {

				int x;

				@Override
				public void run() {
					if (x++ == 2) {
						stop();
						return;
					}
					final WorldTile toTile = new WorldTile(object.getX(),
							player.getY() - 2, player.getPlane());
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							ForceMovement.SOUTH));
					player.setNextAnimation(new Animation(741));
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							player.setNextWorldTile(toTile);
						}
					}, 0);
				}
			}, 2, 1);
		}

		if (object.getId() == 87998) {
			if (player.getInventory().containsOneItem(30026)) {
				player.getCompCapeManager().increaseRequirement(
						Requirement.RISE_OF_SIX, 1);
				player.getTimersManager().removeTimer(
						RecordKey.THE_BARROWS_RISE_OF_THE_SIX);
				player.getPackets()
						.sendEntityMessage(
								1,
								41984,
								player,
								"As the dungeon collapses behind you, your malevolent energy stabilises and becomes usable.");
				player.getInventory()
						.replaceItem(
								30027,
								player.getInventory().getAmountOf(30026),
								player.getInventory().getItems()
										.getThisItemSlot(30026));
			}
			getRotsInstance().leaveBattle(player);
			instance.leaveInstance(player, BossInstance.EXITED);
			removeController();
		}
		if (object.getId() == 88063 && player.getY() > object.getY()) {
			player.faceObject(object);
			player.lock(4);
			player.setNextAnimation(new Animation(751));
			World.sendObjectAnimation(player, object, new Animation(497));
			final WorldTile toTile = new WorldTile(object.getX(),
					object.getY(), player.getPlane());
			player.setNextForceMovement(new ForceMovement(player, 1, toTile, 2,
					ForceMovement.SOUTH));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					player.setNextWorldTile(toTile);
					player.unlock();
				}
			}, 1);
		}

		if (object.getId() == 88001) {
			if (getRotsInstance().fightStarted == true) {
				player.getPackets()
						.sendEntityMessage(1, 16751104, player,
								"The fight has already begun, entry has been restricted.");
				return false;
			}
			if (object.getRotation() == 3) {
				player.lock(2);
				player.setNextWorldTile(new WorldTile(player.getX() + 2, player
						.getY(), 0));
			} else if (object.getRotation() == 2) {
				player.lock(4);
				player.setNextWorldTile(new WorldTile(player.getX(), player
						.getY() + 2, 0));
			}
		}
		if (object.getId() == 87996 && claimed == false) {
			claimed = true;
			sendReward();
		}
		if (object.getId() == 88091) {
			player.lock(8);
			player.setNextAnimation(new Animation(15461));
			player.setNextFaceWorldTile(new WorldTile(player.getX() < object
					.getX() ? player.getX() + 6 : player.getX() - 6, player
					.getY(), player.getPlane()));
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					player.setNextAnimation(new Animation(-1));
					player.setNextWorldTile(new WorldTile(
							player.getX() < object.getX() ? player.getX() + 4
									: player.getX() - 4, player.getY(), player
									.getPlane()));
				}
			}, 3000, TimeUnit.MILLISECONDS);

		}
		if (object.getId() == 87994) {
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					stage = 0;
					sendNPCDialogue(
							18498,
							NORMAL,
							"Be warned! Some of my faithful servants have taken a "
									+ "Sojourn in the Shadow Realm. The shadow has...changed "
									+ "them. I'd advise you to turn back but - well - what fun "
									+ "would that be?");
				}

				@Override
				public void run(int interfaceId, int componentId, int slotId) {
					if (stage == 0) {
						sendOptionsDialogue(
								"<col=ff0000>WARNING:</col> DO YOU WISH TO UNLEASHE THE SHADOW?",
								"Yes.", "I'm not ready yet.");
					}
					if (componentId == OPTION_1) {
						stage = 1;
						player.setDisrupted(true);
					} else if (componentId == OPTION_2) {
						end();
					}
					if (stage == 1) {
						end();
					}

				}

				@Override
				public void finish() {

				}

			});
		}

		return true;
	}

	@Override
	public boolean sendDeath() {
		getRotsInstance().leaveBattle(player);
		player.lock(8);
		player.stopAll();
		if (player.getInventory().containsItem(30026,
				player.getInventory().getAmountOf(30026))) {
			player.getInventory().deleteItem(30026,
					player.getInventory().getAmountOf(30026));
			player.getPackets()
					.sendEntityMessage(
							1,
							41984,
							player,
							"<col=ff000>Your unstable malevolent energy crumbles to dust as you are pulled into the afterlife!");
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextWorldTile(instance.getBoss().getOutsideTile());
					player.setNextAnimation(player.getDeathAnimation());
				} else if (loop == 3) {
					instance.leaveInstance(player, BossInstance.DIED);
					removeController();
					if (instance.getSettings().getBoss() == Boss.Rise_Of_The_Six) {
						player.reset();
						player.setNextWorldTile(Settings.HOME_LOCATION);
					} else {

						WorldTile graveStoneLoc = instance.getBoss()
								.getGraveStoneTile();
						if (graveStoneLoc == null)
							graveStoneLoc = instance.isPublic() ? new WorldTile(
									player) : instance.getBoss()
									.getOutsideTile();

						if (instance.getBoss() == Boss.Corporeal_Beast) { // drop
																			// items
																			// instead
							player.reset();
							Integer[][] slots = GraveStone
									.getItemSlotsKeptOnDeath(player, true,
											player.hasSkull(), player
													.getPrayer()
													.isProtectingItem());
							WorldTile respawnTile = DeathEvent
									.getRespawnHub(player);
							player.sendItemsOnDeath(null, graveStoneLoc,
									respawnTile, true, slots, true);
							player.setNextWorldTile(respawnTile);
						}

					}
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

	@Override
	public boolean logout() {
		getRotsInstance().leaveBattle(player);
		instance.leaveInstance(player, BossInstance.LOGGED_OUT);
		if (!instance.isPublic())
			removeController();
		return false; // false. it will remove script normaly if needed
	}

	// controller stopped for some reason or in purpose
	@Override
	public void forceClose() {
		if (instance != null)
			instance.leaveInstance(player, BossInstance.EXITED);
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getPackets()
				.sendEntityMessage(1, 41984, player,
						"The shadow magic encasing the dungeon prevents you from teleporting out.");
		return false;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		int destXBottom = getRotsInstance().getWorldTile(34, 74).getX();
		int destYTop = getRotsInstance().getWorldTile(48, 95).getY();
		int destXTop = getRotsInstance().getWorldTile(48, 95).getX();
		int destYBottom = getRotsInstance().getWorldTile(34, 74).getY();
		if (npc.getId() == 18552) {
			player.applyHit(new Hit(player, 500, HitLook.REGULAR_DAMAGE));
			getRotsInstance().rightDisabled = true;
			if (player.getX() >= destXBottom && player.getX() <= destXTop
					&& player.getY() >= destYBottom
					&& player.getY() <= destYTop) {
				getRotsInstance().portalRight.setNextWorldTile(new WorldTile(1,
						1, 1));
				for (Player p : getRotsInstance().getPlayers()) {
					if (!getRotsInstance().isInLeftArea(p)) {
						player.setNextWorldTile(getRotsInstance().getWorldTile(
								Utils.random(38, 43), Utils.random(78, 90)));
					} else
						player.setNextWorldTile(getRotsInstance().getWorldTile(
								Utils.random(55, 60), Utils.random(78, 90)));
				}
			} else
				for (Player p : getRotsInstance().getPlayers()) {
					if (!getRotsInstance().isInRightArea(p)) {
						player.setNextWorldTile(getRotsInstance().getWorldTile(
								Utils.random(55, 60), Utils.random(78, 90)));
					} else
						player.setNextWorldTile(getRotsInstance().getWorldTile(
								Utils.random(38, 43), Utils.random(78, 90)));
				}
			player.setNextWorldTile(getRotsInstance().getWorldTile(
					Utils.random(38, 43), Utils.random(78, 90)));
			getRotsInstance().portalLeft
					.setNextWorldTile(new WorldTile(1, 1, 1));
			player.applyHit(new Hit(player, 500, HitLook.REGULAR_DAMAGE));
			getRotsInstance().leftDisabled = true;
		}
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getPackets()
				.sendEntityMessage(1, 41984, player,
						"The shadow magic encasing the dungeon prevents you from teleporting out.");
		return false;
	}

	public RotsInstance getRotsInstance() {
		return (RotsInstance) getInstance();
	}

}