package net.nocturne.game.npc.pet;

import java.io.Serializable;

import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.npc.familiar.impl.BeastOfBurden;
import net.nocturne.game.npc.others.Pet;
import net.nocturne.game.player.Player;

public class LegendaryPetBoB implements Serializable {

	private static final int ITEMS_KEY = 530;

	private static final long serialVersionUID = -2090871604834210257L;

	private transient Player player;
	private transient Pet pet;

	private ItemsContainer<Item> beastItems;

	LegendaryPetBoB(int size) {
		beastItems = new ItemsContainer<>(size, false);
	}

	void setEntity(Player player, Pet pet) {
		this.player = player;
		this.pet = pet;
	}

	public void open() {
		player.getInterfaceManager().sendCentralInterface(671);
		player.getInterfaceManager().sendInventoryInterface(665);
		player.getPackets().sendIComponentSettings(671, 26, 0, 28, 62);
		player.getPackets().sendIComponentSettings(671, 31, 0, 28, 62);
		player.getVarsManager().sendVar(3923, 1);
		sendInterItems();
		sendOptions();
	}

	public void dropBob() {
		int size = pet.getSize();
		WorldTile WorldTile = new WorldTile(pet.getCoordFaceX(size),
				pet.getCoordFaceY(size), pet.getPlane());
		for (int i = 0; i < beastItems.getSize(); i++) {
			Item item = beastItems.get(i);
			if (item != null)
				World.addGroundItem(item, WorldTile, player, false, -1);
		}
		beastItems.reset();
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
		refreshItems(itemsBefore);
	}

	public void addItem(int slot, int amount) {
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		if (!ItemConstants.isTradeable(item)
				&& item.getId() != 23194
				|| item.getId() == 4049
				|| (item.getDefinitions().getPrice() * item.getAmount()) > 50000) {
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
		BeastOfBurden.hasSpace(item, freeSpace, player);
		beastItems.add(item);
		beastItems.shift();
		player.getInventory().deleteItem(slot, item);
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

	private void sendInterItems() {
		player.getPackets().sendItems(ITEMS_KEY, beastItems);
		player.getPackets().sendItems(93, player.getInventory().getItems());
	}

	public ItemsContainer<Item> getBeastItems() {
		return beastItems;
	}

}