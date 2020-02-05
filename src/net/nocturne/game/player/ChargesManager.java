package net.nocturne.game.player;

import java.io.Serializable;
import java.util.HashMap;

import net.nocturne.cache.loaders.BodyDefinitions;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public class ChargesManager implements Serializable {

	private static final long serialVersionUID = -5978513415281726450L;

	private transient Player player;

	private HashMap<Integer, Integer> charges;

	public ChargesManager() {
		charges = new HashMap<>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		Item[] items = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			int defaultCharges = ItemConstants.getItemDefaultCharges(item
					.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstants.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Utils.currentTimeMillis()
					&& ItemConstants.itemDegradesWhileCombating(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
		}
	}
	
	public void die(Integer[] slots, Integer[] slots2) {
		Item[] equipItems = player.getEquipment().getItems().getItems();
		Item[] invItems = player.getInventory().getItems().getItems();

		if (slots == null) {
			for (int slot = 0; slot < equipItems.length; slot++) {
				if (equipItems[slot] != null
						&& degradeCompletely(equipItems[slot], true) != -1)
					player.getEquipment().getItems().set(slot, new Item(degradeCompletely(equipItems[slot], false), 1));
			}
			for (int slot = 0; slot < invItems.length; slot++) {
				if (invItems[slot] != null && degradeCompletely(invItems[slot], true) != -1)
					player.getInventory().getItems().set(slot, new Item(degradeCompletely(invItems[slot], false), 1));
			}
		} else {
			for (int slot : slots) {
				if (slot >= (BodyDefinitions.getEquipmentContainerSize() + 1)) {
					if (invItems[slot
							- (BodyDefinitions.getEquipmentContainerSize() + 1)] != null
							&& degradeCompletely(invItems[slot
									- (BodyDefinitions
											.getEquipmentContainerSize() + 1)], true) != -1)
						player.getInventory().getItems().set(slot - (BodyDefinitions.getEquipmentContainerSize() + 1),
								new Item(degradeCompletely(invItems[slot
															- (BodyDefinitions
																	.getEquipmentContainerSize() + 1)], false), 1));
				} else {
					if (equipItems[slot - 1] != null
							&& degradeCompletely(equipItems[slot - 1], true) != -1)
						player.getEquipment().getItems().set(slot - 1, new Item(degradeCompletely(equipItems[slot - 1], false), 1));
				}
			}
			for (int slot : slots2) {
				if (slot >= (BodyDefinitions.getEquipmentContainerSize() + 1)) {
					if (invItems[slot
							- (BodyDefinitions.getEquipmentContainerSize() + 1)] != null
							&& degradeCompletely(invItems[slot
									- (BodyDefinitions
											.getEquipmentContainerSize() + 1)], true) != -1)
						player.getInventory()
								.getItems()
								.set(slot
										- (BodyDefinitions
												.getEquipmentContainerSize() + 1),
										new Item(degradeCompletely(invItems[slot
									- (BodyDefinitions
											.getEquipmentContainerSize() + 1)], false), 1));
				} else {
					if (equipItems[slot - 1] != null
							&& degradeCompletely(equipItems[slot - 1], true) != -1)
						player.getEquipment().getItems().set(slot - 1, new Item(degradeCompletely(equipItems[slot - 1], false), 1));
				}
			}
		}
	}

	public static final String REPLACE = "##";

	public void checkPercentage(String message, int id, boolean reverse) {
		int charges = getCharges(id);
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		int percentage = reverse ? (charges == 0 ? 0
				: (100 - (charges * 100 / maxCharges))) : charges == 0 ? 100
				: (charges * 100 / maxCharges);
		player.getPackets().sendGameMessage(
				message.replace(REPLACE, String.valueOf(percentage)));
	}

	public boolean checkCharges(Item item) {
		if (item.getDefinitions().containsInventoryOption(2, "Inspect")|| item.getDefinitions().containsInventoryOption(2, "Check")|| item.getDefinitions().containsInventoryOption(2,"Check state")
				|| item.getDefinitions().containsInventoryOption(2,"Check-charges")|| item.getDefinitions().containsInventoryOption(2,"Check charges")) {
			switch(item.getId())
			{
				case 11284:
					player.getDialogueManager().startDialogue("SimpleMessage","There are no charges left within this shield.");
					break;
				case 11283:
					checkCharges("There is " + REPLACE+ " charges remaining in your dragonfire shield.",item.getId());
					break;
				case 22444:
					checkCharges("There is " + REPLACE+ " doses of neem oil remaining.", item.getId());
					break;
				case 24450:
				case 24451:
				case 24452:
				case 24453:
				case 24454:
				case 22358:
				case 22359:
				case 22360:
				case 22361:
				case 22362:
				case 22363:
				case 22364:
				case 22365:
				case 22366:
				case 22367:
				case 22368:
				case 22369:
					checkPercentage("The gloves are " + REPLACE + "% degraded.",item.getId(), true);
					break;
				case 20171:
				case 20173:
					checkPercentage(item.getName() + ": has " + getCharges(item.getId())+ " shots left.", item.getId(), false);
					break;
			}
			if (item.getId() >= 22458 && item.getId() <= 22497)
				checkPercentage(item.getName() + ": " + REPLACE+ "% remaining.", item.getId(), false);
			else
				checkPercentage("Your " + item.getName().toLowerCase()+ " has " + REPLACE + "% of its charges left.",item.getId(), false);
			return true;
		}
		return false;
	}

	private void checkCharges( String message, int id ) {
		player.getPackets().sendGameMessage(
				message.replace(REPLACE, String.valueOf(getCharges(id))));
	}

	public int getCharges(int id) {
		Integer c = charges.get(id);
		return c == null ? 0 : c;
	}

	public void addCharges(int id, int amount, int wearSlot) {
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		if (maxCharges == -1) {
			System.out.println("This item cant get charges atm " + id);
			return;
		}
		Integer c = charges.get(id);
		int amt = c == null ? maxCharges : amount + c;
		if (amt > maxCharges)
			amt = maxCharges;
		if (amt <= 0) {
			int newId = ItemConstants.getItemDegrade(id);
			if (newId == -1) {
				if (wearSlot == -1)
					player.getInventory().deleteItem(id, 1);
				else
					player.getEquipment().getItems().set(wearSlot, null);
			} else if (wearSlot == -1) {
				player.getInventory().deleteItem(id, 1);
				player.getInventory().addItem(newId, 1);
			} else {
				Item item = player.getEquipment().getItem(wearSlot);
				if (item == null)
					return;
				item.setId(newId);
				player.getEquipment().refresh(wearSlot);
				player.getAppearence().generateAppearenceData();
			}
			resetCharges(id);
		} else
			charges.put(id, amt);
	}

	public void resetCharges(int id) {
		charges.remove(id);
	}

	public int degradeCompletely(Item item, boolean check) {
		int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
		if (!ItemConstants.itemDegradesInDeath(item.getId()))
			return -1;
		if (defaultCharges != -1
				|| ItemConstants.getItemDegrade(item.getId()) != -1) {
			int newId;
			if (ItemConstants.itemDegradesWhileWearing(item.getId())
					|| ItemConstants.itemDegradesWhileCombating(item
							.getId())) {
				charges.remove(item.getId());
				newId = ItemConstants.getItemDegrade(item.getId());
			} else {
				newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
				}
			}
			if (newId == -1)
				return -1;
			if (!check)
				player.getPackets().sendGameMessage(Color.RED, "Your " + item.getName().toLowerCase() + " completely degrades to dust.");
			return newId;
		}
		return -1;
	}

	public void wear(int slot) {
		Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName() + " has degraded slightly!");
	}

	void degrade(int itemId, int defaultCharges, int slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems()
						.set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1)
					player.getPackets().sendGameMessage(
							ItemDefinitions.getItemDefinitions(itemId)
									.getName() + " turned into dust.");
				else
					player.getPackets().sendGameMessage(
							ItemDefinitions.getItemDefinitions(itemId)
									.getName() + " has degraded slightly!");
				player.getEquipment().refresh(slot);
				player.getAppearence().generateAppearenceData();
				return;
			}
		}
		charges.put(itemId, c);
	}

}
