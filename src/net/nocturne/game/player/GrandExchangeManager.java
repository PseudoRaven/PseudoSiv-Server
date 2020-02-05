package net.nocturne.game.player;

import java.io.Serializable;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.content.Shop;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.content.grandExchange.Offer;
import net.nocturne.game.player.content.grandExchange.OfferHistory;
import net.nocturne.game.player.controllers.DungeonController;
import net.nocturne.network.decoders.WorldPacketsDecoder;
import net.nocturne.utils.ItemExamines;
import net.nocturne.utils.Utils;

public class GrandExchangeManager implements Serializable {

	private static final long serialVersionUID = -866326987352331696L;

	/*
	 * public static final int ITEMID_CONFIG = 1109, AMOUNT_CONFIG = 1110,
	 * PRICE_PER_CONFIG = 1111, SLOT_CONFIG = 1112, TYPE_CONFIG = 1113;
	 */
	public static final int ITEM_ID_VAR = 135, AMOUNT_VAR = 136, PRICE_PER_VAR = 137, SLOT_VAR = 138, TYPE_VAR = 139,
			MARKET_PRICE_VAR = 140;

	private transient Player player;

	private long[] offerUIds;
	private OfferHistory[] history;

	public GrandExchangeManager() {
		offerUIds = new long[8];
		history = new OfferHistory[5];
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		GrandExchange.linkOffers(player);
	}

	public void stop() {
		GrandExchange.unlinkOffers(player);
	}

	public long[] getOfferUIds() {
		return offerUIds;
	}

	public boolean isSlotFree(int slot) {
		return offerUIds[slot] == 0;
	}

	public void addOfferHistory(OfferHistory o) {
		OfferHistory[] dest = new OfferHistory[history.length];
		dest[0] = o;
		System.arraycopy(history, 0, dest, 1, history.length - 1);
		history = dest;
	}

	public void openHistory() {
		if (player.isAnIronMan()) {
			player.getPackets().sendGameMessage("You are an " + player.getIronmanTitle(true) + ", you stand alone.");
			return;
		}
		player.getInterfaceManager().sendCentralInterface(643);
		for (int i = 0; i < history.length; i++) {
			OfferHistory o = history[i];
			player.getPackets().sendIComponentText(643, 9 + i,
					o == null ? "" : o.isBought() ? "You bought" : "You sold");
			player.getPackets().sendIComponentText(643, 19 + i,
					o == null ? "" : ItemDefinitions.getItemDefinitions(o.getId()).getName());
			player.getPackets().sendIComponentText(643, 14 + i,
					o == null ? "" : Utils.getFormattedNumber(o.getQuantity()));
			player.getPackets().sendIComponentText(643, 24 + i,
					o == null ? "" : Utils.getFormattedNumber(o.getPrice()));
		}
	}

	public void openGrandExchange() {
		/*
		 * if (player.isAnIronMan()) { player.getPackets().sendGameMessage(
		 * "You are an " + player.getIronmanTitle(true) + ", you stand alone.");
		 * return; }
		 */
		if (!player.getBank().hasVerified(4))
			return;
		player.getInterfaceManager().openMenu(5, 3);
		player.getPackets().sendUnlockIComponentOptionSlots(105, 77, -1, 0, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(105, 79, -1, 0, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(107, 7, 0, 27, 0, 1);
		cancelOffer();
		// player.setCloseInterfacesEvent(new Runnable() {
		// @Override
		// void run() {
		// removeGEItemSearch();
		// stop();
		// }
		// });
	}

	public void openCollectionBox() {
		if (player.isAnIronMan()) {
			player.getPackets().sendGameMessage("You are an " + player.getIronmanTitle(true) + ", you stand alone.");
			return;
		}
		if (!player.getBank().hasVerified(5))
			return;
		if (player.getControllerManager().getController() instanceof DungeonController) {
			return;
		}
		player.getInterfaceManager().sendCentralInterface(109);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 15, 0, 2, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 13, 0, 2, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 10, 0, 2, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 7, 0, 2, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 4, 0, 2, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(109, 1, 0, 2, 0, 1);
	}

	public void setSlot(int slot) {
		player.getVarsManager().sendVar(SLOT_VAR, slot);
	}

	public void setMarketPrice(int price) {
		player.getVarsManager().sendVar(MARKET_PRICE_VAR, price);
	}

	public void setPricePerItem(int price) {
		player.getVarsManager().sendVar(PRICE_PER_VAR, price);
	}

	public int getPricePerItem() {
		return player.getVarsManager().getValue(PRICE_PER_VAR);
	}

	public int getCurrentSlot() {
		return player.getVarsManager().getValue(SLOT_VAR);
	}

	public void setItemId(int id) {
		player.getVarsManager().sendVar(ITEM_ID_VAR, id);
	}

	public int getItemId() {
		return player.getVarsManager().getValue(ITEM_ID_VAR);
	}

	public void setAmount(int amount) {
		player.getVarsManager().sendVar(AMOUNT_VAR, amount);
	}

	public int getAmount() {
		return player.getVarsManager().getValue(AMOUNT_VAR);
	}

	public void setType(int amount) {
		player.getVarsManager().sendVar(TYPE_VAR, amount);
	}

	public int getType() {
		return player.getVarsManager().getValue(TYPE_VAR);
	}

	public void handleButtons(int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 105) {
			switch (componentId) {
			case 15: // converted
				cancelOffer();
				break;
			case 17: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(0);
				else
					abortOffer(0);
				break;
			case 18: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(1);
				else
					abortOffer(1);
				break;
			case 19: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(2);
				else
					abortOffer(2);
				break;
			case 20: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(3);
				else
					abortOffer(3);
				break;
			case 21: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(4);
				else
					abortOffer(4);
				break;
			case 22: // converted
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
					viewOffer(5);
				else
					abortOffer(5);
				break;
			case 61: // converted
				abortCurrentOffer();
				break;
			case 175: // converted
				makeOffer(0, false);
				break;
			case 180: // converted
				makeOffer(0, true);
				break;
			case 186: // converted
				makeOffer(1, false);
				break;
			case 192: // converted
				makeOffer(1, true);
				break;
			case 198: // converted
				makeOffer(2, false);
				break;
			case 204: // converted
				makeOffer(2, true);
				break;
			case 211: // converted
				makeOffer(3, false);
				break;
			case 217: // converted
				makeOffer(3, true);
				break;
			case 227: // converted
				makeOffer(4, false);
				break;
			case 233: // converted
				makeOffer(4, true);
				break;
			case 243: // converted
				makeOffer(5, false);
				break;
			case 249: // converted
				makeOffer(5, true);
				break;
			case 121: // converted
				modifyAmount(getAmount() - 1);
				break;
			case 124: // converted
				modifyAmount(getAmount() + 1);
				break;
			case 311: // converted
				modifyAmount(getAmount() + 1);
				break;
			case 128: // converted
				modifyAmount(getAmount() + 10);
				break;
			case 134: // converted
				modifyAmount(getAmount() + 100);
				break;
			case 140: // converted
				modifyAmount(getType() == 0 ? getAmount() + 1000 : getItemAmount(new Item(getItemId())));
				break;
			case 292: // converted
				editAmount();
				break;
			case 304:
				enterItemName();
				break;
			case 145: // converted
				modifyPricePerItem(getPricePerItem() - 1);
				break;
			case 148: // converted
				modifyPricePerItem(getPricePerItem() + 1);
				break;
			case 158: // converted
				modifyPricePerItem(GrandExchange.getPrice(getItemId()));
				break;
			case 298: // converted
				editPrice();
				break;
			case 163: // converted
				modifyPricePerItem((int) (Math.ceil(getPricePerItem() * 1.05)));
				break;
			case 152: // converted
				modifyPricePerItem((int) (getPricePerItem() * 0.95));
				break;
			case 169: // converted
				confirmOffer();
				break;
			case 9: // converted
				chooseItem();
				break;
			case 79: // converted
			case 63: // converted
				collectItems(getCurrentSlot(), 0, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				// break;
				collectItems(getCurrentSlot(), 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			}
		} else if (interfaceId == 107 && componentId == 7) {// converted
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
				if (isSlotFree(0))
					makeOffer(0, true);
				else if (isSlotFree(1))
					makeOffer(1, true);
				else if (isSlotFree(2))
					makeOffer(2, true);
				else if (isSlotFree(3))
					makeOffer(3, true);
				else if (isSlotFree(4))
					makeOffer(4, true);
				else if (isSlotFree(5)) {
					if (player.getDonationManager().isDonator())
						makeOffer(5, true);
					else
						player.getPackets().sendGameMessage("You must be a gold member to use over 5 slots!");
				} else
					player.getPackets().sendGameMessage("You do not have any slots left.");
				offer(slotId);
			} else
				player.getInventory().sendExamine(slotId);
		} else if (interfaceId == 389 && componentId == 8) // added
			removeGEItemSearch();
		else if (interfaceId == 109) {
			switch (componentId) {
			case 15: // converted
				collectItems(0, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			case 13: // converted
				collectItems(1, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			case 10: // converted
				collectItems(2, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			case 7: // converted
				collectItems(3, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			case 4: // converted
				collectItems(4, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			case 1:
				collectItems(5, slotId == 0 ? 0 : 1, packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET ? 0 : 1);
				break;
			// case 48:
			case 56: // added
				collectAll(componentId == 48);
				break;
			}
		}
	}
	
	public void enterItemName() {
		player.getVarsManager().sendVar(138, 2);
		player.getVarsManager().sendVar(139, 0);
		player.getPackets().sendCSVarInteger(2235, 6881581);
		player.getPackets().sendCSVarInteger(2236, 10);
		player.getPackets().sendCSVarInteger(2237, 0);
	}

	public void collectAll(boolean bank) {
		// kinda a cheat way to collect all to bank / inv
		for (int i = 0; i < 6; i++) {
			Offer offer = GrandExchange.getOffer(player, i);
			if (offer == null)
				continue;
			if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage("Not enough space in your inventory.");
				return;
			}
			for (int i2 = 0; i2 < 2; i2++) {
				Item item = offer.getReceivedItems().get(i2);
				if (item == null)
					continue;
				collectItems(i, i2, 0); // tries to collect everything
				if (bank) {
					int slot = player.getInventory().getItems().getThisItemSlot(item);
					if (slot == -1)
						continue;
					player.getBank().depositItem(slot, item.getAmount(), false);
				}
			}
		}
	}

	public void refreshMainInterface() {
		player.getInterfaceManager().openMenu(5, player.getSubMenus()[5] + 1);
		player.getPackets().sendUnlockIComponentOptionSlots(105, 77, -1, 0, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(105, 79, -1, 0, 0, 1);
		player.getPackets().sendUnlockIComponentOptionSlots(107, 7, 0, 27, 0, 1);
		cancelOffer();
	}

	public void cancelOffer() {
		setItemId(-1);
		setAmount(0);
		setPricePerItem(1);
		setMarketPrice(0);
		setSlot(-1);
		//removeGEItemSearch();
		setType(-1);
	}

	public void editAmount() {
		if (getType() == -1)
			return;
		player.getTemporaryAttributtes().put("GEQUANTITYSET", Boolean.TRUE);
		player.getPackets().sendInputIntegerScript(
				"Enter the quantity you wish to " + (getType() == 0 ? "purchase" : "sell") + ":");
		
		/*player.getPackets().sendCSVarInteger(2235, 6881569);//Idk how to make this work with the enter_integer_packet
		player.getPackets().sendCSVarInteger(2236, 7);
		player.getPackets().sendCSVarInteger(2237, 10);
		*/
	}

	public void editPrice() {
		if (getType() == -1)
			return;
		if (getItemId() == -1) {
			player.getPackets().sendGameMessage("You must choose an item first.");
			return;
		}
		player.getTemporaryAttributtes().put("GEPRICESET", Boolean.TRUE);
		player.getPackets().sendInputIntegerScript(
				"Enter the price you wish to to " + (getType() == 0 ? "buy" : "sell") + " for:");
				
		/*player.getPackets().sendCSVarInteger(2235, 6881575);//Idk how to make this work with the enter_integer_packet
		player.getPackets().sendCSVarInteger(2236, 7);
		player.getPackets().sendCSVarInteger(2237, 10);
		*/
	}

	public void modifyPricePerItem(int value) {
		if (getType() == -1)
			return;
		if (getItemId() == -1) {
			player.getPackets().sendGameMessage("You must choose an item first.");
			return;
		}
		if (value < 1)
			value = 1;
		setPricePerItem(value);
	}

	public void modifyAmount(int value) {
		if (getType() == -1)
			return;
		if (value < 0)
			value = 0;
		setAmount(value);
	}

	public void abortCurrentOffer() {
		int slot = getCurrentSlot();
		if (slot == -1)
			return;
		abortOffer(slot);
	}

	public void abortOffer(int slot) {
		if (isSlotFree(slot))
			return;
		GrandExchange.abortOffer(player, slot);
		player.getPackets().sendGameMessage(
				"Abort request acknowledged. Please be aware that your offer may have already been completed.");
	}

	public void collectItems(int slot, int invSlot, int option) {
		if (slot == -1 || isSlotFree(slot))
			return;
		GrandExchange.collectItems(player, slot, invSlot, option);
		refreshMainInterface();
	}

	public void viewOffer(int slot) {
		if (isSlotFree(slot) || getCurrentSlot() != -1) {
			return;
		}
		Offer offer = GrandExchange.getOffer(player, slot);
		if (offer == null)
			return;
		setSlot(slot);
		setExtraDetails(offer.getId());
	}

	/*
	 * includes noted
	 */
	public int getItemAmount(Item item) {
		int notedId = item.getDefinitions().certId;
		return player.getInventory().getAmountOf(item.getId()) + player.getInventory().getAmountOf(notedId);
	}

	public void chooseItem(int id) {
		if (!player.getInterfaceManager().containsInterface(105))
			return;
		setItem(new Item(id), false);
	}

	public void chooseItem() {
		if (getType() != 0)
			return;
		sendGEItemSearch();
	}

	public void sendGEItemSearch() {
		player.getInterfaceManager().sendInputTextInterface(389);
		player.getPackets().sendExecuteScript(570, "Grand Exchange Item Search");
	}

	public void removeGEItemSearch() {
		if (getType() == 0)
			player.getPackets().sendExecuteScriptReverse(571);
		player.getInterfaceManager().removeInputTextInterface();
	}

	public void offer(int slot) {
		if (getType() == -1)
			return;
		Item item = player.getInventory().getItem(slot);
		if (item == null)
			return;
		setItem(item, getType() == 1);
	}

	public void setExtraDetails(int id) {
		setItemId(id);
		setMarketPrice(GrandExchange.getPrice(id));
		player.getPackets().sendIComponentText(105, 3, ItemExamines.getExamine(new Item(id)));
	}

	public void setItem(Item item, boolean sell) {
		if (item.getId() == Shop.COINS || (!ItemConstants.isTradeable(item))) {
			player.getPackets().sendGameMessage("This item cannot be sold on the Grand Exchange.");
			return;
		}
		if (item.getDefinitions().isNoted() && item.getDefinitions().getCertId() != -1)
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		int price = GrandExchange.getPrice(item.getId());
		setPricePerItem(price);
		setAmount(item.getAmount());
		setExtraDetails(item.getId());
	}

	public void confirmOffer() {
		int type = getType();
		if (type == -1)
			return;
		int slot = getCurrentSlot();
		if (slot == -1 || !isSlotFree(slot))
			return;
		boolean buy = type == 0;
		int itemId = getItemId();
		if (itemId == -1) {
			player.getPackets().sendGameMessage("You must choose an item to " + (buy ? "buy" : "sell") + "!");
			return;
		}
		int amount = getAmount();
		if (amount == 0) {
			player.getPackets()
					.sendGameMessage("You must choose the quantity you wish to " + (buy ? "buy" : "sell") + "!");
			return;
		}
		int pricePerItem = getPricePerItem();
		if (pricePerItem != 0) {
			if (amount > 2147483647 / pricePerItem) { // TOO HIGH
				player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
				return;
			}
		}
		if (buy) {
			int price = pricePerItem * amount;
			if (player.getInventory().getCoinsAmount() < price) {
				player.getPackets().sendGameMessage("You do not have enough coins to cover the offer.");
				return;
			}
			player.getInventory().removeItemMoneyPouch(new Item(995, price));
		} else {
			int inventoryAmount = getItemAmount(new Item(itemId));
			if (amount > inventoryAmount) {
				player.getPackets()
						.sendGameMessage("You do not have enough of this item in your inventory to cover the offer.");
				return;
			}
			int notedId = ItemDefinitions.getItemDefinitions(itemId).certId; // -1
			// if
			// not
			// noteable
			// anyway
			int notedAmount = player.getInventory().getAmountOf(notedId);
			if (notedAmount < amount) {
				player.getInventory().deleteItem(notedId, notedAmount);
				player.getInventory().deleteItem(itemId, amount - notedAmount);
			} else
				player.getInventory().deleteItem(notedId, amount);
		}
		GrandExchange.sendOffer(player, slot, itemId, amount, pricePerItem, buy);
		cancelOffer();
	}

	public void makeOffer(int slot, boolean sell) {
		if (!isSlotFree(slot) || getCurrentSlot() != -1) {
			return;
		}
		if (slot > 4 && !player.getDonationManager().isDonator()) {
			player.getPackets().sendGameMessage("You must be donator to use over 5 slots!");
			return;
		}
		setType(sell ? 1 : 0);
		setSlot(slot);
		//if (!sell)
		//	sendGEItemSearch();
	}
}