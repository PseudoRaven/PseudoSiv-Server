package net.nocturne.game.player.controllers;

import java.util.List;

import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.araxxor.Araxxor;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

class AraxxiController extends Controller {

	private Araxxor npc;

	/**
	 * Dynamic region chunk data.
	 */
	private int[] chunks;
	private WorldTile base;

	/**
	 * Minigame coordinates.
	 */
	private static final WorldTile EXIT = new WorldTile(Settings.HOME_LOCATION);
	private static final WorldTile LOBBY = new WorldTile(Settings.HOME_LOCATION);
	private static final WorldTile GRAVEYARD = new WorldTile(
			Settings.HOME_LOCATION);

	@Override
	public void start() {
		player.araxxorHeal = false;
		player.AraxxorEggBurst = false;
		player.AraxxorThirdStage = false;
		player.AraxxorLastState = false;
		player.hasSpawnedEggs = false;
		boolean isHost = (boolean) getArguments()[0];
		Player host = (Player) getArguments()[1];
		player.setForceMultiArea(true);
		if (isHost) {
			chunks = MapBuilder.findEmptyChunkBound(32, 32);
			base = new WorldTile(chunks[0] << 3, chunks[1] << 3, 1);
			MapBuilder.copyAllPlanesMap(550, 780, chunks[0], chunks[1], 32, 32);
			npc = new Araxxor(player, base.transform(105, 21, 0), base);
			npc.xDifference = base.getX();
			npc.setForceFollowClose(true);
			player.setNextWorldTile(base.transform(90, 25, 0));
			player.setForceMultiArea(true);
			player.AraxxorNormAttackDelay = 0;
			player.ArraxorAttackDelay = 0;
			player.araxxorEggAttack = false;
			player.AraxxorCompleteAttack = true;
			player.AraxxorAttackCount = 0;
			player.araxxorCacoonTime = 0;
			handleObjects();
			WorldTasksManager.schedule(new WorldTask() {
				int stage;

				@Override
				public void run() {
					if (stage == 60)
						stop();
					stage++;
				}

			}, 0, 1);
		} else {
			player.setNextWorldTile(host.getNextWorldTile());
		}
	}

	private void handleObjects() {
		World.spawnObject(new WorldObject(91504, 10, 0, base.transform(104, 32,
				0)));
		World.spawnObject(new WorldObject(91509, 10, 0, base.transform(111, 21,
				0)));
		World.spawnObject(new WorldObject(91511, 10, 0, base.transform(107, 8,
				0)));
		spawnWestBurn();
		spawnMidBurn();
		spawnEastBurn();
		World.spawnObject(new WorldObject(91520, 10, 0, base.transform(135, 20,
				0)));
	}

	private void spawnWestBurn() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;

			@Override
			public void run() {
				time++;
				if (time == 9) {
					for (int i = 0; i < 12; i++) {
						World.spawnObject(
								new WorldObject(91667, 10, 0, base.transform(
										109 - i, 31 + i, 0)), true);

						time = 0;
						stop();
					}
				}
			}
		}, 0, 0);
	}

	private void spawnMidBurn() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;

			@Override
			public void run() {
				time++;
				if (time == 9) {
					for (int i = 0; i < 12; i++) {
						World.spawnObject(
								new WorldObject(91668, 10, 0, base.transform(
										112, 20 + i, 0)), true);

						time = 0;
						stop();
					}
				}
			}
		}, 0, 0);
	}

	private void spawnEastBurn() {
		WorldTasksManager.schedule(new WorldTask() {
			int time;

			@Override
			public void run() {
				time++;
				if (time == 9) {
					for (int i = 0; i < 12; i++) {
						World.spawnObject(
								new WorldObject(91669, 10, 0, base.transform(
										101 + i, 5 + i, 0)), true);

						time = 0;
						stop();
					}
				}
			}
		}, 0, 0);
	}

	@Override
	public void process() {
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		int objectId = object.getId();
		switch (objectId) {
		case 91673:
			player.setNextWorldTile(LOBBY);
			// npc.openRewardChest(true);
			return false;
		}
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int objectId = object.getId();
		switch (objectId) {
		case 91669:
			World.spawnBurningWeb(
					new WorldObject(91506, 10, 0, base.transform(40, 32, 0)),
					60000);
			for (int i = 0; i < 12; i++) {
				World.removeObject(new WorldObject(91669, 10, 0, base
						.transform(39, 34 + i, 0)));
				World.spawnTemporaryObject(
						new WorldObject(91666, 10, 0, base.transform(39,
								34 + i, 0)), 60000);
			}
			return true;
		case 91673:
			if (npc.canLoot)
				npc.openRewardChest(true);
			else
				player.getPackets().sendGameMessage(
						"An error occurred, you cannot receive loot.");
			return false;
		case 91670:
			player.setNextAnimation(new Animation(10743));
			player.setNextWorldTile(base.transform(224 - 64, 86 - 64, 0));
			player.AraxxorThirdStage = true;
			return true;
		case 91668:
			World.spawnBurningWeb(
					new WorldObject(91506, 10, 0, base.transform(111, 21, 0)),
					60000);
			for (int i = 0; i < 12; i++)
				handlePathTwoObjects(i);
			return true;
		}
		return true;

	}

	private void handlePathTwoObjects(int i) {
		World.removeObject(new WorldObject(91668, 10, 0, base.transform(112,
				20 + i, 0)));
		World.spawnTemporaryObject(
				new WorldObject(91666, 10, 0, base.transform(112, 20 + i, 0)),
				60000);
		World.removeObject(new WorldObject(91669, 10, 0, base.transform(
				101 + i, 5 + i, 0)));
		World.spawnObject(
				new WorldObject(91666, 10, 0, base.transform(101 + i, 5 + i, 0)),
				true);
		World.removeObject(new WorldObject(91667, 10, 0, base.transform(
				109 - i, 31 + i, 0)));
		World.spawnObject(
				new WorldObject(91666, 10, 0, base
						.transform(109 - i, 31 + i, 0)), true);
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		if (player.getFamiliar() != null) {
			player.getFamiliar().sendDeath(player);
		}
		final WorldTile graveTile = GRAVEYARD;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					npc.finish();
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.reset();
					player.getControllerManager().startController("DeathEvent",
							graveTile, player.hasSkull());
				} else if (loop == 4) {
					player.getPackets().sendMusicEffect(90, 1);
					stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	private void destroyMap() {
		GameExecutorManager.slowExecutor.execute(() -> {
			try {
				MapBuilder.destroyMap(chunks[0], chunks[1], 8, 8);
			} catch (Exception | Error e) {
				e.printStackTrace();
			}
		});
	}

	public void deleteNPCs(int regionId) {
		List<Integer> npcsIndexes = World.getRegion(regionId).getNPCsIndexes();
		if (npcsIndexes != null) {
			for (int npcIndex : npcsIndexes) {
				NPC npc = World.getNPCs().get(npcIndex);
				if (npc == null/* || npc.isDead() */|| npc.hasFinished()) {
					continue;
				}
				for (int i = 19457; i <= 19457; i++) {
					if (npc.getId() == i) {
						npc.finish();
					}
				}
			}
		}
	}

	private void endGame() {
		destroyMap();
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		player.setNextWorldTile(EXIT);
		player.stopAll();
	}

	private void endGame(WorldTile tile) {
		destroyMap();
		player.setForceMultiArea(false);
		player.getControllerManager().forceStop();
		Magic.sendNormalTeleportSpell(player, 0, 0, tile);
		player.stopAll();
	}

	@Override
	public boolean login() {
		endGame();
		return false;
	}

	@Override
	public boolean logout() {
		endGame();
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int slotId2, int packetId) {
		if (npc == null)
			return true;
		switch (interfaceId) {
		case 1284:
			switch (componentId) {
			case 8:
				player.getBank().addItems(npc.getRewards().toArray(), false);
				npc.getRewards().clear();
				player.getPackets().sendGameMessage(
						"All the items were moved to your bank.");
				break;
			case 9:
				npc.getRewards().clear();
				player.getPackets().sendGameMessage(
						"All the items were removed from the chest.");
				break;
			case 10:
				for (int slot = 0; slot < npc.getRewards().toArray().length; slot++) {
					Item item = npc.getRewards().get(slot);
					if (item == null) {
						continue;
					}
					boolean added = true;
					if (item.getDefinitions().isStackable()
							|| item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slot] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets()
								.sendGameMessage(
										"You only had enough space in your inventory to accept some of the items.");
						break;
					}
				}
				break;
			case 7:
				Item item = npc.getRewards().get(slotId);
				if (item == null) {
					return true;
				}
				switch (packetId) {
				case 52:
					player.getPackets().sendGameMessage(
							"It's a " + item.getDefinitions().getName());
					return false;
				case 4:
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 64:
					player.getBank().addItems(
							new Item[] { npc.getRewards().toArray()[slotId] },
							false);
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 61:
					boolean added = true;
					if (item.getDefinitions().isStackable()
							|| item.getAmount() < 2) {
						added = player.getInventory().addItem(item);
						if (added) {
							npc.getRewards().toArray()[slotId] = null;
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (!player.getInventory().addItem(single)) {
								added = false;
								break;
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.getPackets()
								.sendGameMessage(
										"You only had enough space in your inventory to accept some of the items.");
						break;
					}
					break;
				default:
					return true;
				}
				break;
			default:
				return true;
			}
			npc.openRewardChest(false);
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		endGame(toTile);
		return true;
	}
}