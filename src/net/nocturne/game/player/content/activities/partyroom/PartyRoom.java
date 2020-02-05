package net.nocturne.game.player.content.activities.partyroom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.decoders.WorldPacketsDecoder;
import net.nocturne.utils.ItemExamines;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializableFilesManager;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 * @author Trent
 */

public class PartyRoom {

	public static final int CHEST_INTERFACE = 647;
	public static final int INVENTORY_INTERFACE = 648;

	public static boolean isDancing = false;
	public static boolean isDropping = false;
	public static int timer = -1;

	private static int[] BALLOON_IDS = { 115, 116, 117, 118, 119, 120, 121, 122 };
	private static String[] SONG = { "We're the knights of the party room",
			"We dance round and round like a loon",
			"Quite often we like to sing", "Unfortunately we make a din",
			"We're the knights of the party room",
			"Do you like our helmet plumes?",
			"Everyone's happy now we can move",
			"Like a party animal in the groove" };

	public static ItemsContainer<Item> store;
	public static ItemsContainer<Item> drop;

	@SuppressWarnings("unchecked")
	public static void init() throws ClassNotFoundException, IOException {
		File storeFile = new File("data/store.party");
		File dropFile = new File("data/drop.party");
		if (!storeFile.exists()) {
			store = new ItemsContainer<Item>(216, false);
		} else {
			store = (ItemsContainer<Item>) SerializableFilesManager
					.loadSerializedFile(storeFile);
		}
		if (!dropFile.exists()) {
			drop = new ItemsContainer<Item>(216, false);
		} else {
			drop = (ItemsContainer<Item>) SerializableFilesManager
					.loadSerializedFile(dropFile);
		}
	}

	public static void save() {
		try {
			File storeFile = new File("data/store.party");
			File dropFile = new File("data/drop.party");
			SerializableFilesManager.storeSerializableClass(store, storeFile);
			SerializableFilesManager.storeSerializableClass(drop, dropFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void openChest(Player player) {
		if (player.isAnIronMan())
			return;
		if (player.partyDeposit == null)
			player.partyDeposit = new ItemsContainer<Item>(8, false);
		player.getInterfaceManager().sendCentralInterface(CHEST_INTERFACE);
		player.getInterfaceManager()
				.sendInventoryInterface(INVENTORY_INTERFACE);
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE,
				0, 93, 4, 7, "Deposit", "Deposit-5", "Deposit-10",
				"Deposit-All", "Deposit-X");
		player.getPackets().sendIComponentSettings(INVENTORY_INTERFACE, 0, 0,
				27, 1278);
		player.getPackets().sendInterSetItemsOptionsScript(CHEST_INTERFACE, 23,
				529, 6, 36, "Value");
		player.getPackets().sendInterSetItemsOptionsScript(CHEST_INTERFACE, 24,
				91, 6, 36, "Value");
		player.getPackets().sendInterSetItemsOptionsScript(CHEST_INTERFACE, 25,
				92, 8, 1, "Withdraw");
		player.getPackets().sendIComponentSettings(CHEST_INTERFACE, 23, 0, 215,
				1150);
		player.getPackets().sendIComponentSettings(CHEST_INTERFACE, 24, 0, 215,
				1026);
		player.getPackets().sendIComponentSettings(CHEST_INTERFACE, 25, 0, 7,
				1026);
		player.getPackets().sendHideIComponent(CHEST_INTERFACE, 26, true);
		refreshItems(player);
	}

	public static void purchase(Player player, boolean balloons) {
		if (balloons) {
			if (isDropping) {
				player.getPackets().sendGameMessage(
						"Please wait for the previous party to end.");
				return;
			}
			for (Item item : store.getItems()) {
				if (item == null)
					continue;
				if (drop.freeSlots() > 0) {
					drop.add(item);
					store.remove(item);
				} else
					break;
			}
			if (drop.getUsedSlots() < 5) {
				player.getPackets()
						.sendGameMessage(
								"Please deposit 5 or more items into the chest before starting.");
				return;
			}
			startBalloonTimer();
		} else {
			if (!isDancing)
				startDancingKnights();
		}
	}

	public static void startDancingKnights() {
		isDancing = true;
		final NPC[] npcs = new NPC[6];
		for (int i = 0; i < 6; i++) {
			npcs[i] = new NPC(660, new WorldTile(3043 + i, 3378, 0), -1, false);
			npcs[i].setDirection(0);
			World.addNPC(npcs[i]);
		}
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop < SONG.length) {
					npcs[2].setNextForceTalk(new ForceTalk(SONG[loop]));
				} else {
					for (int i = 0; i < 6; i++) {
						npcs[i].finish();
					}
					isDancing = false;
					stop();
				}
				loop++;
			}
		}, 0, 3);

	}

	public static int getTimeLeft() {
		return (timer * Settings.WORLD_CYCLE_TIME) / 1000;
	}

	public static int getRandomBalloon() {
		return BALLOON_IDS[Utils.random(BALLOON_IDS.length)];
	}

	public static Item getNextItem() {
		ArrayList<Item> items = new ArrayList<Item>();
		for (Item item : drop.getItems()) {
			if (item != null)
				items.add(item);
		}
		if (items.isEmpty())
			return null;
		Item item = items.get(Utils.random(items.size()));
		drop.remove(item);
		return item;
	}

	public static void spawnBalloons() {
		ArrayList<Balloon> balloons = new ArrayList<Balloon>();
		for (int x = 3038; x < 3054; x++) {
			for (int y = 3373; y < 3384; y++) {
				if (x <= 3049 && x >= 3042 && y == 3378)
					continue;
				if (World.isTileFree(0, x, y, 1)
						&& World.getObject(new WorldTile(x, y, 0)) == null)
					balloons.add(new Balloon(getRandomBalloon(), 10, 0, x, y, 0));
			}
		}

		Collections.shuffle(balloons);
		organize();
		for (Balloon balloon : balloons) {
			if (balloon != null)
				World.spawnObjectTemporary(balloon.setItem(getNextItem()),
						120000);
		}
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					isDropping = false;
					timer = -1;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}

		}, 120000, TimeUnit.MILLISECONDS);
	}

	public static void organize() {
		ItemsContainer<Item> copy = new ItemsContainer<Item>(216, false);
		copy.addAll(drop);
		for (Item item : copy.getItems()) {
			if (item != null) {
				combineDrop(item);
				if (shouldSplitDrop(item)) {
					splitDrop(item);
				}
				/*
				 * if (skip.contains(item.getId())) continue; boolean stackable
				 * = item.getDefinitions().isStackable(); int certId =
				 * item.getDefinitions().getCertId(); boolean notable = certId
				 * != -1; int numberOf = drop.getNumberOf(item.getId()); boolean
				 * shouldBeNoted = !stackable && notable; if (numberOf > 5 &&
				 * (stackable || shouldBeNoted)) {//split up into multiple
				 * balloons int balloons = 5; int totalAmount = 0;
				 * while(totalAmount < numberOf) { int amount = 2 *
				 * Utils.random(numberOf / balloons); if (totalAmount + amount >
				 * numberOf) amount = numberOf - totalAmount; if (amount <= 0)
				 * break; copy.add(new Item(shouldBeNoted ? certId :
				 * item.getId(), amount)); totalAmount += amount; }
				 * skip.add(item.getId()); } else { copy.add(item); }
				 */
			}
		}
	}

	public static void combineDrop(Item item) {
		boolean stackable = item.getDefinitions().isStackable();
		int certId = item.getDefinitions().getCertId();
		boolean noteable = certId != -1;
		if (stackable) {
			return;
		}
		if (!noteable) {
			return;
		}
		int totalAmount = drop.getNumberOf(item);
		drop.removeAll(item);
		drop.add(new Item(item.getId(), totalAmount));
	}

	public static boolean shouldSplitDrop(Item item) {
		boolean stackable = item.getDefinitions().isStackable();
		// int certId = item.getDefinitions().getCertId();
		// boolean notable = certId != -1;
		int numberOf = drop.getNumberOf(item.getId());
		// boolean shouldBeNoted = !stackable && notable;
		// boolean noted = item.getDefinitions().isNoted();
		return numberOf > 5 && stackable;
	}

	public static void splitDrop(Item item) {
		drop.remove(item);
		int split = (int) Math.floor(item.getAmount() / 5);
		int remaining = item.getAmount() - (split * 4);
		for (int i = 0; i < 4; i++) {// 0, 1, 2, 3
			drop.add(new Item(item.getId(), split));
		}
		drop.add(new Item(item.getId(), remaining));
	}

	public static void startBalloonTimer() {
		isDropping = true;
		timer = 200;
	}

	public static void yellNpcs() {
		for (NPC npc : World.getNPCs()) {
			if (!npc.getDefinitions().name.toLowerCase().contains("banker")
					&& !npc.getDefinitions().name.toLowerCase().contains(
							"party") && npc.getId() != 5915
					&& npc.getId() != 13633)
				continue;
			npc.setNextForceTalk(new ForceTalk("There is a drop party worth "
					+ Utils.formatLong(getTotalCoins()) + " coins starting in "
					+ getTimeLeft() + " seconds at ::party!"));
		}
	}

	public static void refreshItems(Player player) {
		player.getPackets().sendItems(529, store);
		player.getPackets().sendItems(91, drop);
		player.getPackets().sendItems(92, player.partyDeposit);
	}

	public static void addToChest(Player player) {
		for (Item item : player.partyDeposit.getItems()) {
			if (item != null) {
				if (store.freeSlots() > 0) {
					player.partyDeposit.remove(item);
					store.add(item);
				} else {
					player.getPackets()
							.sendGameMessage(
									"There was not enough room in the chest for all those items.");
					return;
				}
			}
		}
		refreshItems(player);
	}

	public static void addDeposit(Player player, Item item, int amount) {
		if (!ItemConstants.isTradeable(item)) {
			player.getPackets().sendGameMessage(
					"You can't put this item in the party chest!");
			return;
		}
		if (player.getInventory().getAmountOf(item.getId()) < amount)
			amount = player.getInventory().getAmountOf(item.getId());
		if (player.getInventory().containsItem(item.getId(), amount)) {
			if (item.getDefinitions().getName().toLowerCase()
					.contains("teleport")
					|| item.getDefinitions().getName().toLowerCase()
							.contains("master")) {
				player.getPackets().sendGameMessage(
						"You can't put this item in the party chest!");
				return;
			}
			if (player.partyDeposit.freeSlots() > 0) {
				if (!item.getDefinitions().isStackable()
						&& player.partyDeposit.freeSlots() < amount)
					amount = player.partyDeposit.freeSlots();
				player.partyDeposit.add(new Item(item.getId(), amount));
				player.getInventory().deleteItem(item.getId(), amount);
			} else {
				player.getPackets()
						.sendGameMessage(
								"Please deposit or withdraw some items before putting more in.");
				return;
			}
		}
		refreshItems(player);
	}

	public static void removeDeposit(Player player, int slot) {
		Item item = player.partyDeposit.get(slot);
		if (item == null)
			return;
		if (player.partyDeposit.contains(item)) {
			if (player.getInventory().addItemMoneyPouch(item)) {
				player.partyDeposit.remove(new Item(item.getId(), item
						.getAmount()));
			} else {
				player.getPackets().sendGameMessage(
						"You don't have enough space in your inventory.");
				return;
			}
		}
		refreshItems(player);
	}

	public static void handleButtons(Player player, int interfaceId,
			int componentId, int slotId, int slotId2, int packetId) {
		if (interfaceId == CHEST_INTERFACE) {
			if (componentId == 25) {
				Item item = player.partyDeposit.get(slotId);
				if (item == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					removeDeposit(player, slotId);
				else
					player.getPackets().sendGameMessage(
							ItemExamines.getExamine(item));
			} else if (componentId == 21) {
				addToChest(player);
			} else if (componentId == 23) {
				Item item = store.get(slotId);
				if (item == null)
					return;
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					player.getPackets().sendGameMessage(
							"Item valued at: "
									+ item.getDefinitions().getValue());
				else
					player.getPackets().sendGameMessage(
							ItemExamines.getExamine(item));
			}
		} else if (interfaceId == INVENTORY_INTERFACE) {
			final Item item = player.getInventory().getItem(slotId);
			if (item == null)
				return;
			switch (packetId) {
			case WorldPacketsDecoder.ACTION_BUTTON1_PACKET:
				addDeposit(player, item, 1);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON2_PACKET:
				addDeposit(player, item, 5);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON3_PACKET:
				addDeposit(player, item, 10);
				break;
			case WorldPacketsDecoder.ACTION_BUTTON4_PACKET:
				addDeposit(player, item,
						player.getInventory().getAmountOf(item.getId()));
				break;
			/*
			 * case 68:
			 * player.sendInputInteger("How many would you like to deposit?",
			 * new InputIntegerEvent() {
			 * 
			 * @Override public void run(Player player) { if
			 * (player.getInterfaceManager().containsInterface(CHEST_INTERFACE))
			 * addDeposit(player, item, getInteger()); } }); break;
			 */
			case WorldPacketsDecoder.ACTION_BUTTON6_PACKET:
				player.getPackets().sendGameMessage(
						ItemExamines.getExamine(item));
				break;
			default:
				break;
			}
		}
	}

	public static long getTotalCoins() {
		long total = 0;
		for (Item item : drop.getItems()) {
			if (item != null)
				total += item.getDefinitions().getValue() * item.getAmount();
		}
		return total;
	}

}