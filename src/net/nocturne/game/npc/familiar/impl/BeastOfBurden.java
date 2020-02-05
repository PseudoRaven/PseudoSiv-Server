package net.nocturne.game.npc.familiar.impl;

import java.io.Serializable;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class BeastOfBurden implements Serializable {

	private static final int ITEMS_KEY = 530;

	private static final long serialVersionUID = -2090871604834210257L;

	private transient Player player;
	private transient Familiar familiar;

	private ItemsContainer<Item> beastItems;
	private boolean canDepositOnly;

	public BeastOfBurden(boolean canDepositOnly, int size) {
		this.canDepositOnly = canDepositOnly;
		beastItems = new ItemsContainer<Item>(size, false);
	}

	public void setEntitys(Player player, Familiar familiar) {
		this.player = player;
		this.familiar = familiar;
	}

	public boolean isBoB(Player player) {
		if (player.getFamiliar().getId() == 6873
				|| player.getFamiliar().getId() == 6815
				|| player.getFamiliar().getId() == 6794
				|| player.getFamiliar().getId() == 6867)
			return true;
		return false;
	}

	public void open() {
		player.getInterfaceManager().sendCentralInterface(671);
		player.getPackets().sendIComponentSettings(671, 26, 0, 28, 62);
		player.getPackets().sendIComponentSettings(671, 31, 0, 28, 62);
		player.getVarsManager().sendVar(3923, 1);
		player.getVarsManager().forceSendVar(1784, familiar.getId());
		sendOptions();
	}

	public void dropBob() {
		int size = familiar.getSize();
		WorldTile WorldTile = new WorldTile(familiar.getCoordFaceX(size),
				familiar.getCoordFaceY(size), familiar.getPlane());
		for (int i = 0; i < beastItems.getSize(); i++) {
			Item item = beastItems.get(i);
			if (item != null)
				World.addGroundItem(item, WorldTile, player, false, -1);
		}
		beastItems.reset();
	}

	public void takeBob() {
		Item[] itemsBefore = beastItems.getItemsCopy();
		for (int i = 0; i < beastItems.getSize(); i++) {
			Item item = beastItems.get(i);
			if (item != null) {
				if (!player.getInventory().addItem(item))
					break;
				beastItems.remove(i, item);
			}
		}
		beastItems.shift();
		player.setNextAnimation(new Animation(23314));
		refreshItems(itemsBefore);
	}

	public void removeItem(int slot, int amount) {
		Item item = beastItems.get(slot);
		if (item == null)
			return;
		Item[] itemsBefore = beastItems.getItemsCopy();
		int maxAmount = beastItems.getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		int freeSpace = player.getInventory().getFreeSlots();
		if (!item.getDefinitions().isStackable()) {
			if (freeSpace == 0) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
			if (freeSpace < item.getAmount()) {
				item.setAmount(freeSpace);
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		} else {
			if (freeSpace == 0
					&& !player.getInventory().containsItem(item.getId(), 1)) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		}
		beastItems.remove(slot, item);
		beastItems.shift();
		player.getInventory().addItem(item);
		player.setNextAnimation(new Animation(23314));
		refreshItems(itemsBefore);
	}

	public boolean addItem(Item item) {
		if (item == null
				|| item.getId() < 0
				|| item.getAmount() < 0
				|| !Utils.itemExists(item.getId())
				|| !player.getControllerManager().canAddInventoryItem(
						item.getId(), item.getAmount()))
			return false;
		Item[] itemsBefore = beastItems.getItemsCopy();
		if (!beastItems.add(item)) {
			beastItems.add(new Item(item.getId(), beastItems.getFreeSlots()));
			player.getPackets().sendGameMessage(
					"Not enough space in your inventory.");
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}

	public Item getItem(int slot) {
		return beastItems.get(slot);
	}

	public boolean hasFreeSlots() {
		return beastItems.getFreeSlot() != -1;
	}

	public int getFreeSlots() {
		return beastItems.getFreeSlots();
	}

	public int getFreeSlots(Item[] item) {
		return beastItems.getFreeSlots(item);
	}

	public int getAmountOf(int itemId) {
		return beastItems.getNumberOf(itemId);
	}

	public int getItemsContainerSize() {
		return beastItems.getSize();
	}

	public void switchItem(int fromSlot, int toSlot) {
		Item[] itemsBefore = beastItems.getItemsCopy();
		Item fromItem = beastItems.get(fromSlot);
		Item toItem = beastItems.get(toSlot);
		beastItems.set(fromSlot, toItem);
		beastItems.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}

	public void deleteItem(Item item) {
		Item[] itemsBefore = beastItems.getItemsCopy();
		beastItems.remove(item);
		refreshItems(itemsBefore);
	}

	public void deleteItem(int slot, Item item) {
		if (!player.getControllerManager().canDeleteInventoryItem(item.getId(),
				item.getAmount()))
			return;
		Item[] itemsBefore = beastItems.getItemsCopy();
		beastItems.remove(slot, item);
		refreshItems(itemsBefore);
	}

	public void addItem(int slot, int amount) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (item.getDefinitions().isStackable()) {
			player.getPackets().sendGameMessage(
					"You cannot store stackable items in this familiar.");
			return;
		}
		if (item.getDefinitions().isNoted()) {
			player.getPackets().sendGameMessage(
					"You cannot store noted items in this familiar.");
			return;
		}

		if (canDepositOnly) {
			player.getPackets().sendGameMessage(
					"You cannot store items in this familiar.");
			return;
		} else if (!ItemConstants.isTradeable(item)
				&& item.getId() != 23194
				|| item.getId() == 4049
				|| (familiar.canStoreEssOnly() && item.getId() != 1436 && item
						.getId() != 7936)) {
			player.getPackets().sendGameMessage("You cannot store this item.");
			return;
		}
		Item[] itemsBefore = beastItems.getItemsCopy();
		int maxAmount = player.getInventory().getItems().getNumberOf(item);
		if (amount < maxAmount)
			item = new Item(item.getId(), amount);
		else
			item = new Item(item.getId(), maxAmount);
		int freeSpace = beastItems.getFreeSlots();
		if (!item.getDefinitions().isStackable()) {
			if (freeSpace == 0) {
				player.getPackets().sendGameMessage(
						"Not enough space in your familiar inventory.");
				return;
			}

			if (freeSpace < item.getAmount()) {
				item.setAmount(freeSpace);
				player.getPackets().sendGameMessage(
						"Not enough space in your familiar inventory.");
			}
		} else {
			if (freeSpace == 0 && !beastItems.containsOne(item)) {
				player.getPackets().sendGameMessage(
						"Not enough space in your familiar inventory.");
				return;
			}
		}
		beastItems.add(item);
		beastItems.shift();
		player.getInventory().deleteItem(slot, item);
		player.setNextAnimation(new Animation(23314));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			Item item = beastItems.getItems()[index];
			if (itemsBefore[index] != item) {
				changedSlots[count++] = index;
			}

		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public void refresh(int... slots) {
		player.getPackets().sendUpdateItems(ITEMS_KEY, beastItems, slots);
	}

	public void sendOptions() {
		player.getPackets().sendUnlockIComponentOptionSlots(665, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(665, 0, 93, 4, 7,
				"Store", "Store-5", "Store-10", "Store-All", "Store-X",
				"Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(671, 27, 0,
				ITEMS_KEY, 0, 1, 2, 3, 4, 5);
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (beastItems.containsOne(new Item(itemId, 1)))
				return true;
		}
		return false;
	}

	public void sendInterItems() {
		player.getPackets().sendItems(ITEMS_KEY, beastItems);
		player.getPackets().sendItems(93, player.getInventory().getItems());
	}

	public ItemsContainer<Item> getBeastItems() {
		return beastItems;
	}

	public static boolean hasSpace(Item item, int freeSpace, Player player) {
		if (!item.getDefinitions().isStackable()) {
			if (freeSpace == 0) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return true;
			}
			if (freeSpace < item.getAmount()) {
				item.setAmount(freeSpace);
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		} else {
			if (freeSpace == 0
					&& !player.getInventory().containsItem(item.getId(), 1)) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return true;
			}
		}
		return false;
	}
}
