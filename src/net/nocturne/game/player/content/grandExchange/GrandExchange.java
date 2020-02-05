package net.nocturne.game.player.content.grandExchange;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.utils.SerializableFilesManager;
import net.nocturne.utils.Utils;

public class GrandExchange {

	private static final Object LOCK = new Object();
	// offer uid
	private static HashMap<Long, Offer> OFFERS;
	private static ArrayList<OfferHistory> OFFERS_TRACK;
	private static HashMap<Integer, Integer> PRICES;

	private static boolean edited;

	public static void init() {
		OFFERS = SerializableFilesManager.loadGEOffers();
		OFFERS_TRACK = SerializableFilesManager.loadGEHistory();
		PRICES = SerializableFilesManager.loadGEPrices();
	}

	public static void reset(boolean track, boolean price) {
		if (track)
			OFFERS_TRACK.clear();
		if (price)
			PRICES.clear();
		recalcPrices();
	}

	public static void recalcPrices() {
		ArrayList<OfferHistory> track = new ArrayList<>(OFFERS_TRACK);
		HashMap<Integer, BigInteger> averagePrice = new HashMap<>();
		HashMap<Integer, BigInteger> averageQuantity = new HashMap<>();
		for (OfferHistory o : track) {
			BigInteger price = averagePrice.get(o.getId());
			if (price != null) {
				BigInteger quantity = averageQuantity.get(o.getId());
				averagePrice.put(o.getId(),
						price.add(BigInteger.valueOf(o.getPrice())));
				averageQuantity.put(o.getId(),
						quantity.add(BigInteger.valueOf(o.getQuantity())));
			} else {
				averagePrice.put(o.getId(), BigInteger.valueOf(o.getPrice()));
				averageQuantity.put(o.getId(),
						BigInteger.valueOf(o.getQuantity()));
			}
		}

		for (int id : averagePrice.keySet()) {
			BigInteger price = averagePrice.get(id);
			BigInteger quantity = averageQuantity.get(id);

			long oldPrice = getPrice(id);
			long newPrice = price.divide(quantity).longValue();
			long min = (long) (oldPrice * 0.95 + -1);
			long max = (long) (oldPrice * 1.05 + 1);
			if (newPrice < min)
				newPrice = min;
			else if (newPrice > max)
				newPrice = max;
			if (newPrice < 1)
				newPrice = 1;
			else if (newPrice > Integer.MAX_VALUE)
				newPrice = Integer.MAX_VALUE;
			int shopValue = ItemDefinitions.getItemDefinitions(id).value;
			if (newPrice < shopValue)
				newPrice = shopValue;
			PRICES.put(id, (int) newPrice);
		}
		OFFERS_TRACK.clear();
		saveOffersTrack();
		savePrices();
	}

	public static void setPrice(int id, int price) {
		PRICES.put(id, price);
	}

	public static void savePrices() {
		SerializableFilesManager.saveGEPrices(new HashMap<>(PRICES));
	}

	private static void saveOffersTrack() {
		SerializableFilesManager.saveGEHistory(new ArrayList<>(OFFERS_TRACK));
	}

	public static void save() {
		if (!edited)
			return;
		SerializableFilesManager.saveGEOffers(new HashMap<>(OFFERS));
		saveOffersTrack();
		edited = false;
	}

	public static void linkOffers(Player player) {
		boolean itemsWaiting = false;
		for (int slot = 0; slot < player.getGeManager().getOfferUIds().length; slot++) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				continue;
			offer.link(slot, player);
			offer.update();
			if (!itemsWaiting && offer.hasItemsWaiting()) {
				itemsWaiting = true;
				player.getPackets()
						.sendGameMessage(
								"You have items from the Grand Exchange waiting in your collection box.");
			}
		}
	}

	public static Offer getOffer(Player player, int slot) {
		synchronized (LOCK) {
			long uid = player.getGeManager().getOfferUIds()[slot];
			if (uid == 0)
				return null;
			Offer offer = OFFERS.get(uid);
			if (offer == null) {
				player.getGeManager().getOfferUIds()[slot] = 0;
				return null;
			}
			return offer;
		}

	}

	public static void sendOffer(Player player, int slot, int itemId,
			int amount, int price, boolean buy) {
		synchronized (LOCK) {
			Offer offer = new Offer(itemId, amount, price, buy);
			player.getGeManager().getOfferUIds()[slot] = createOffer(offer);
			offer.link(slot, player);
			findBuyerSeller(offer);
		}
	}

	public static void abortOffer(Player player, int slot) {
		synchronized (LOCK) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				return;
			edited = true;
			OFFERS.remove(player.getGeManager().getOfferUIds()[slot]);
			if (offer.cancel() && offer.forceRemove())
				deleteOffer(player, slot); // shouldnt here happen anyway
		}
	}

	public static void collectItems(Player player, int slot, int invSlot,
			int option) {
		synchronized (LOCK) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				return;
			edited = true;
			if (offer.collectItems(invSlot, option) && offer.forceRemove()) {
				deleteOffer(player, slot); // should happen after none left and
				// offer completed
				if (offer.getTotalAmountSoFar() != 0) {
					OfferHistory o = new OfferHistory(offer.getId(),
							offer.getTotalAmountSoFar(),
							offer.getTotalPriceSoFar(), offer.isBuying());
					OFFERS_TRACK.add(o);
					player.getGeManager().addOfferHistory(o);
				}
			}
		}
	}

	private static void deleteOffer(Player player, int slot) {
		player.getGeManager().cancelOffer(); // sends back to original screen if
		// seeing an offer
		OFFERS.remove(player.getGeManager().getOfferUIds()[slot]);
		player.getGeManager().getOfferUIds()[slot] = 0;
	}

	private static void findBuyerSeller(Offer offer) {
		while (!offer.isCompleted()) {
			Offer bestOffer = null;
			for (Offer o : OFFERS.values()) {
				// owner is null when not logged in but u online its on so works
				if (o.getOwner() == offer.getOwner()
						|| o.isBuying() == offer.isBuying()
						|| o.getId() != offer.getId()
						|| o.isCompleted()
						|| (offer.isBuying() && o.getPrice() > offer.getPrice())
						|| (!offer.isBuying() && o.getPrice() < offer
								.getPrice()) || offer.isOfferTooHigh(o))
					continue;
				if (bestOffer == null
						|| (offer.isBuying() && o.getPrice() < bestOffer
								.getPrice())
						|| (!offer.isBuying() && o.getPrice() > bestOffer
								.getPrice()))
					bestOffer = o;
			}
			if (bestOffer == null)
				break;
			offer.updateOffer(bestOffer);
		}
		offer.update();
	}

	private static long createOffer(Offer offer) {
		edited = true;
		long uid = getUId();
		OFFERS.put(uid, offer);
		return uid;
	}

	private static long getUId() {
		while (true) {
			long uid = ThreadLocalRandom.current().nextLong();
			if (OFFERS.containsKey(uid))
				continue;
			return uid;
		}
	}

	public static void showOffers(Player player) {
		if (player.isAnIronMan()) {
			player.getPackets().sendGameMessage(
					"You are an " + player.getIronmanTitle(true)
							+ ", you stand alone.");
			return;
		}
		player.getInterfaceManager().sendCentralInterface(1166);
		player.getPackets().sendIComponentText(1166, 23, "GE Offers");
		player.getPackets().sendIComponentText(
				1166,
				2,
				"<col=00FF00>" + OFFERS.values().size()
						+ "</col> items for sale");
		String list = "";

		int index = 0;
		for (final Offer offers : OFFERS.values()) {
			index++;
			if (index > 49)
				break;
			if (offers.isBuying())
				list += "<col=FF0000>[Buy]</col> " + offers.getAmount() + "x "
						+ offers.getName() + " - "
						+ Utils.format(offers.getPrice()) + " gp<br>";
			else
				list += "<col=FF0000>[Sell]</col> " + offers.getAmount() + "x "
						+ offers.getName() + " - "
						+ Utils.format(offers.getPrice()) + " gp<br>";
		}
		player.getPackets().sendIComponentText(1166, 1, list.trim());
	}

	public static int getPrice(int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		return defs.getGrandExchangePrice();
	}

	public static void unlinkOffers(Player player) {
		for (int slot = 0; slot < player.getGeManager().getOfferUIds().length; slot++) {
			Offer offer = getOffer(player, slot);
			if (offer == null)
				continue;
			offer.unlink();
		}
	}

	public static List<OfferHistory> getHistory() {
		return OFFERS_TRACK;
	}

	public static HashMap<Long, Offer> getOffers() {
		return OFFERS;
	}
}