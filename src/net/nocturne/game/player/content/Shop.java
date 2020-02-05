package net.nocturne.game.player.content;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.Equipment;
import net.nocturne.game.player.InterfaceManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.utils.ItemExamines;
import net.nocturne.utils.ItemSetsKeyGenerator;
import net.nocturne.utils.Utils;

public class Shop {

	private static final int MAIN_STOCK_ITEMS_KEY = ItemSetsKeyGenerator
			.generateKey();

	private static final int MAX_SHOP_ITEMS = 55;
	public static final int COINS = 995;
	private static final int TOKKUL = 6529;

	/* Vote shop 1 */
	private static final int[][] auraShops = { { 22889, 10 }, { 22895, 10 },
			{ 22897, 10 }, { 20966, 10 }, { 22905, 10 }, { 22891, 10 },
			{ 22294, 10 }, { 23848, 10 }, { 22296, 10 }, { 22280, 10 },
			{ 22300, 10 }, { 20958, 10 }, { 22284, 10 }, { 22893, 10 },
			{ 22292, 10 }, { 20965, 10 }, { 20962, 10 }, { 22899, 10 },
			{ 20967, 10 }, { 20964, 10 }, { 22927, 10 }, { 22298, 10 },
			{ 22268, 15 }, { 22270, 15 }, { 22274, 15 }, { 22276, 15 },
			{ 22278, 15 }, { 22282, 15 }, { 22286, 15 }, { 22290, 15 },
			{ 22885, 15 }, { 22901, 15 }, { 22907, 15 }, { 22929, 15 },
			{ 23842, 15 }, { 23850, 15 }, { 22302, 15 }, { 22272, 15 },

			{ 22887, 25 }, { 22903, 25 }, { 22909, 25 }, { 22911, 25 },
			{ 22917, 25 }, { 22919, 25 }, { 22921, 25 }, { 22923, 25 },
			{ 22925, 25 }, { 22931, 25 }, { 22933, 25 }, { 23844, 25 },
			{ 23852, 25 }, { 22913, 25 }, { 22915, 25 },

			{ 23854, 35 }, { 23856, 35 }, { 23858, 35 }, { 23860, 35 },
			{ 23862, 35 }, { 23864, 35 }, { 23866, 35 }, { 23868, 35 },
			{ 23870, 35 }, { 23872, 35 }, { 23874, 35 }, { 23876, 35 },
			{ 23878, 35 }, };

	/* Vote shop 2 */
	private static final int[][] voteshop2 = { { 10686, 10 }, { 7386, 10 },
			{ 7390, 10 }, { 7394, 10 }, { 1837, 5 }, { 10400, 5 },
			{ 10402, 5 }, { 10394, 5 }, { 2643, 5 }, { 5553, 15 },
			{ 5555, 15 }, { 5557, 15 }, { 20750, 50 }, { 20765, 50 },
			{ 13612, 10 }, { 13614, 10 }, { 13617, 10 }, { 13618, 10 },
			{ 20787, 50 }, { 20788, 50 }, { 20789, 50 }, { 20790, 50 },
			{ 20791, 50 }, { 20792, 50 }, { 30920, 50 }, { 22332, 35 },
			{ 6199, 40 }, { 26384, 60 } };

	/* Skill task shop */
	private static final int[][] skillTaskShop = { { 31041, 5 }, { 31042, 5 },
			{ 31043, 5 }, { 31044, 5 }, { 25205, 20 }, { 30372, 1 },
			{ 7409, 10 }, { 15345, 15 }, { 15347, 20 }, { 15349, 25 },
			{ 19748, 35 }, { 13560, 15 }, { 13561, 20 }, { 13562, 25 },
			{ 19760, 35 }, { 4251, 15 }, { 13661, 30 }, { 27477, 30 } };

	/* Boss shop */
	private static final int[][] bossShop = { { 3062, 50 }, { 23679, 400 },
			{ 23680, 380 }, { 23681, 380 }, { 23682, 380 }, { 23683, 300 },
			{ 23684, 200 }, { 23685, 205 }, { 23686, 205 }, { 23687, 205 },
			{ 23688, 205 }, { 23689, 85 }, { 23690, 250 }, { 23691, 140 },
			{ 23692, 400 }, { 23693, 120 }, { 23694, 120 }, { 23695, 130 },
			{ 23696, 80 }, { 23697, 900 }, { 23698, 1000 }, { 23699, 950 },
			{ 23700, 900 }, { 30815, 100 }, { 30816, 120 }, { 30817, 120 },
			{ 30818, 280 }, { 30822, 120 } };

	/* Boss shop 2 */
	private static final int[][] bossShop2 = { { 13898, 200 }, { 13886, 200 },
			{ 13892, 200 }, { 13889, 200 }, { 13895, 200 }, { 13901, 200 },
			{ 13878, 200 }, { 13872, 200 }, { 13875, 200 }, { 13866, 200 },
			{ 13860, 200 }, { 13863, 200 }, { 13869, 200 } };

	private final String name;
	private final Item[] mainStock;
	private final int[] defaultQuantity;
	private Item[] generalStock;
	private final boolean ironMan;
	private final int money;
	private final CopyOnWriteArrayList<Player> viewingPlayers;
	private final int key;

	public Shop(int key, String name, int money, Item[] mainStock,
			boolean isGeneralStore, boolean isIronMan) {
		viewingPlayers = new CopyOnWriteArrayList<>();
		this.key = key;
		this.money = money;
		this.ironMan = isIronMan;
		this.name = name;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public boolean isIronMan() {
		return ironMan;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getTemporaryAttributtes().put("Shop", this);
		player.setCloseInterfacesEvent(() -> {
			viewingPlayers.remove(player);
			player.getTemporaryAttributtes().remove("Shop");
			player.getTemporaryAttributtes().remove("shop_transaction");
			player.getTemporaryAttributtes().remove("isShopBuying");
			player.getTemporaryAttributtes().remove("ShopSelectedSlot");
			player.getTemporaryAttributtes().remove("ShopSelectedInventory");
			player.getInterfaceManager().sendLockGameTab(
					InterfaceManager.INVENTORY_TAB, false);
		});
		player.getInterfaceManager().sendLockGameTab(
				InterfaceManager.INVENTORY_TAB, true);

		player.refreshVerboseShopDisplayMode();
		player.getVarsManager().sendVar(304,
				generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY);
		player.getVarsManager().sendVar(305, -1); // sample items container id
		// (TODO: add support for it)
		player.getVarsManager().sendVar(306, money);
		resetSelected(player);
		sendStore(player);
		player.getInterfaceManager().sendCentralInterface(1265); // opens shop
		if ((money == COINS && key >= 1200))
			player.getPackets()
					.sendGameMessage(
							"Warning! All oracle items are based in grand exchange prices but 40% more expensive so you're recommended to use g.e. instead.");
		resetTransaction(player);
		setBuying(player, true);
		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize(),
				2097406); // unlocks
		// stock
		// slots
		// rs stoped sending inv. but locks old one ye.
		// sendInventory(player);
		player.getPackets().sendCSVarString(2360, name);
	}

	private void resetTransaction(Player player) {
		setTransaction(player, 1);
	}

	public void increaseTransaction(Player player, int amount) {
		setTransaction(player, getTransaction(player) + amount);
	}

	private int getTransaction(Player player) {
		Integer transaction = (Integer) player.getTemporaryAttributtes().get(
				"shop_transaction");
		return transaction == null ? 1 : transaction;
	}

	public void pay(Player player) {
		Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get(
				"ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getTemporaryAttributtes().get(
				"ShopSelectedInventory");
		if (selectedSlot == null || inventory == null)
			return;
		int amount = getTransaction(player);
		if (inventory)
			sell(player, selectedSlot, amount);
		else
			buy(player, selectedSlot, amount);
	}

	private int getSelectedMaxAmount(Player player) {
		Integer selectedSlot = (Integer) player.getTemporaryAttributtes().get(
				"ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getTemporaryAttributtes().get(
				"ShopSelectedInventory");
		if (selectedSlot == null || inventory == null)
			return 1;
		if (inventory) {
			Item item = player.getInventory().getItem(selectedSlot);
			if (item == null)
				return 1;
			return player.getInventory().getAmountOf(item.getId());
		} else {
			if (selectedSlot >= getStoreSize())
				return 1;
			Item item = selectedSlot >= mainStock.length ? generalStock[selectedSlot
					- mainStock.length]
					: mainStock[selectedSlot];
			if (item == null)
				return 1;
			return item.getAmount();
		}
	}

	public void setTransaction(Player player, int amount) {
		int max = getSelectedMaxAmount(player);
		if (amount > max)
			amount = max;
		else if (amount < 1)
			amount = 1;
		player.getTemporaryAttributtes().put("shop_transaction", amount);
		player.getVarsManager().sendVar(302, amount);
	}

	public static void setBuying(Player player, boolean buying) {
		player.getTemporaryAttributtes().put("isShopBuying", buying);
		player.getVarsManager().sendVar(303, buying ? 0 : 1);
	}

	public static boolean isBuying(Player player) {
		Boolean isBuying = (Boolean) player.getTemporaryAttributtes().get(
				"isShopBuying");
		return isBuying != null && isBuying;
	}

	public void buyAll(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		buy(player, slotId, item.getAmount());
	}

	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions()
				.getWearingSkillRequiriments();
		int price = getBuyPrice(item);
		int amountCoins = money == COINS ? player.getInventory()
				.getCoinsAmount() : player.getInventory().getItems()
				.getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();
		boolean hasRequiriments = true;
		boolean enoughCoins = maxQuantity >= buyQ;
		if (player.isAnIronMan() && generalStock != null
				&& generalStock.length > 0) {
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] != null
						&& generalStock[i].getId() == item.getId()) {
					player.getPackets().sendGameMessage(
							"You cannot buy items other players has sold.");
					return;
				}
			}
		}
		if (item.getName().contains("cape (t)")
				|| item.getName().contains(" master cape")
				|| item.getName().equalsIgnoreCase("Dungeoneering cape")) {
			int skill = 0, level = !item.getName().contains("cape (t)") ? 120
					: 99;
			assert requiriments != null;
			for (int skillId : requiriments.keySet()) {
				if (skillId > 25 || skillId < 0)
					continue;
				level = requiriments.get(skillId);
				skill = skillId;
				if (player.getSkills().getLevelForXp(skillId) < requiriments
						.get(skillId)) {
					hasRequiriments = false;
				}
			}
			if (!hasRequiriments) {
				player.getPackets().sendGameMessage(
						"You need a " + player.getSkills().getSkillName(skill)
								+ " of " + level + " to buy a "
								+ item.getName() + ".");
				return;
			}
		}
		if (money != 995) {
			for (int[] auraShop : auraShops) {
				if (item.getId() == auraShop[0]) {
					if (player.getVotePoints() < auraShop[1] * quantity) {
						player.getPackets()
								.sendGameMessage(
										"You need: "
												+ auraShop[1]
												+ " vote points to buy "
												+ item.getName()
												+ ", you "
												+ ((player.getVotePoints() == 0) ? "have 0."
														: "only have: "
																+ player.getVotePoints()
																+ "."));
						return;
					} else {
						player.getPackets().sendGameMessage(
								"You have bought a " + item.getName() + ".",
								true);
						player.getInventory().addItem(auraShop[0], 1);
						player.setVotePoints(player.getVotePoints()
								- auraShop[1]);
						return;
					}
				}
			}
			for (int[] aVoteshop2 : voteshop2) {
				if (item.getId() == aVoteshop2[0]) {
					if (player.getVotePoints() < aVoteshop2[1] * quantity) {
						player.getPackets()
								.sendGameMessage(
										"You need: "
												+ aVoteshop2[1]
												+ " vote points to buy "
												+ item.getName()
												+ ", you "
												+ ((player.getVotePoints() == 0) ? "have 0."
														: "only have: "
																+ player.getVotePoints()
																+ "."));
						return;
					} else {
						if (aVoteshop2[0] == 6199
								&& !player.getDonationManager().isDonator()) {
							player.getPackets()
									.sendGameMessage(
											"You need to be a donator to purchase this item.");
							return;
						}
						player.getPackets().sendGameMessage(
								"You have bought a " + item.getName() + ".",
								true);
						player.getInventory().addItem(aVoteshop2[0], 1);
						player.setVotePoints(player.getVotePoints()
								- aVoteshop2[1]);
						return;
					}
				}
			}
			for (int[] aSkillTaskShop : skillTaskShop) {
				if (item.getId() == aSkillTaskShop[0]) {
					if (player.getTaskPoints() < aSkillTaskShop[1] * quantity) {
						player.getPackets()
								.sendGameMessage(
										"You need: "
												+ aSkillTaskShop[1]
												+ " skill task points to buy "
												+ item.getName()
												+ ", you "
												+ ((player.getTaskPoints() == 0) ? "have 0."
														: "only have: "
																+ player.getTaskPoints()
																+ "."));
						return;
					} else {
						if (aSkillTaskShop[0] == 13661) {
							if (player.getSkills().getXp(Skills.WOODCUTTING) <= 15000000
									|| player.getSkills().getXp(
											Skills.FIREMAKING) <= 15000000) {
								player.getPackets()
										.sendGameMessage(
												"You need 15,000,000 xp in woodcutting and firemaking to buy this.");
								return;
							}
						}
						player.getPackets().sendGameMessage(
								"You have bought a " + item.getName() + ".",
								true);
						player.getInventory().addItem(aSkillTaskShop[0], 1);
						player.setTaskPoints(player.getTaskPoints()
								- aSkillTaskShop[1]);
						return;
					}
				}
			}
		}
		if (!enoughCoins) {
			player.getPackets().sendGameMessage(
					"You don't have enough "
							+ ItemDefinitions.getItemDefinitions(money)
									.getName().toLowerCase() + ".");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.getPackets().sendGameMessage(
					"The shop has run out of stock.");
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			if (player.getInventory().removeItemMoneyPouch(
					new Item(money, totalPrice))) {
				player.getInventory().addItem(item.getId(), buyQ);
				item.setAmount(item.getAmount() - buyQ);
				if (item.getAmount() <= 0 && slotId >= mainStock.length)
					generalStock[slotId - mainStock.length] = null;
				refreshShop();
				resetSelected(player);
			}
		}
	}

	public void restoreItems(boolean general) {
		Item[] stock = general ? generalStock : mainStock;
		int[] restoreQuantity = getPercentOfStock(stock, 0.05, general);// ten
		// percent
		for (int idx = 0; idx < stock.length; idx++) {
			Item item = stock[idx];
			if (item == null || !general
					&& item.getAmount() == defaultQuantity[idx])
				continue;
			boolean subtraction = general
					|| stock[idx].getAmount() > defaultQuantity[idx];
			int quantity = restoreQuantity[idx];
			item.setAmount(item.getAmount()
					+ (subtraction ? -quantity : quantity));
			if (subtraction) {
				if (item.getAmount() <= 0)
					stock[idx] = null;
			} else if (item.getAmount() > defaultQuantity[idx])
				item.setAmount(defaultQuantity[idx]);
		}
		refreshShop();
	}

	private int[] getPercentOfStock(Item[] stock, double percent,
			boolean general) {
		int[] percentArray = new int[stock.length];
		for (int idx = 0; idx < stock.length; idx++) {
			Item item = stock[idx];
			if (item == null)
				continue;
			percentArray[idx] = (int) Math.max(1, (general ? item.getAmount()
					: defaultQuantity[idx]) * percent);
		}
		return percentArray;
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		int originalId = item.getId();
		if (item.getDefinitions().isNoted()
				&& item.getDefinitions().getCertId() != -1)
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item);
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		refreshShop();
		resetSelected(player);
		if (price == 0)
			return;
		player.getInventory().addItemMoneyPouch(
				new Item(money, price * quantity));
	}

	private int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	private void resetSelected(Player player) {
		player.getTemporaryAttributtes().remove("ShopSelectedSlot");
		player.getVarsManager().sendVar(301, -1);
	}

	public void sendInfo(Player player, int slotId, boolean inventory) {
		if (!inventory && slotId >= getStoreSize())
			return;
		Item item = inventory ? player.getInventory().getItem(slotId)
				: slotId >= mainStock.length ? generalStock[slotId
						- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (inventory
				&& (!ItemConstants.isTradeable(item) || item.getId() == money)) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			resetSelected(player);
			return;
		}
		resetTransaction(player);
		player.getTemporaryAttributtes().put("ShopSelectedSlot", slotId);
		player.getTemporaryAttributtes()
				.put("ShopSelectedInventory", inventory);
		player.getVarsManager().sendVar(
				299,
				inventory ? 93 : generalStock != null ? 139
						: MAIN_STOCK_ITEMS_KEY); // inv
		// key
		player.getVarsManager().sendVar(306, item.getId());
		player.getVarsManager().sendVar(301, slotId);
		player.getPackets()
				.sendCSVarString(2361, ItemExamines.getExamine(item));
		player.getPackets().sendCSVarInteger(1876,
				getSubDescription(player, item));
		int price = inventory ? getSellPrice(item) : getBuyPrice(item);
		for (int[] aBossShop : bossShop) {
			if (item.getId() == aBossShop[0] && !isGeneralStore()) {
				player.getPackets().sendGameMessage(
						item.getDefinitions().getName() + " costs "
								+ Utils.format(aBossShop[1]) + " boss points.");
				player.getPackets().sendIComponentText(1265, 205,
						"" + aBossShop[1]);
				return;
			}
		}
		for (int[] aBossShop2 : bossShop2) {
			if (item.getId() == aBossShop2[0] && !isGeneralStore()) {
				player.getPackets()
						.sendGameMessage(
								item.getDefinitions().getName() + " costs "
										+ Utils.format(aBossShop2[1])
										+ " boss points.");
				player.getPackets().sendIComponentText(1265, 205,
						"" + aBossShop2[1]);
				return;
			}
		}
		for (int[] auraShop : auraShops) {
			if (item.getId() == auraShop[0] && !isGeneralStore()) {
				player.getPackets().sendGameMessage(
						item.getDefinitions().getName() + " costs "
								+ Utils.format(auraShop[1]) + " vote points.");
				player.getPackets().sendIComponentText(1265, 205,
						"" + auraShop[1]);
				return;
			}
		}
		for (int[] aVoteshop2 : voteshop2) {
			if (item.getId() == aVoteshop2[0] && !isGeneralStore()) {
				player.getPackets()
						.sendGameMessage(
								item.getDefinitions().getName() + " costs "
										+ Utils.format(aVoteshop2[1])
										+ " vote points.");
				player.getPackets().sendIComponentText(1265, 205,
						"" + aVoteshop2[1]);
				return;
			}
		}
		for (int[] aSkillTaskShop : skillTaskShop) {
			if (item.getId() == aSkillTaskShop[0] && !isGeneralStore()) {
				player.getPackets().sendGameMessage(
						item.getDefinitions().getName() + " costs "
								+ Utils.format(aSkillTaskShop[1])
								+ " skill task points.");
				player.getPackets().sendIComponentText(1265, 205,
						"" + aSkillTaskShop[1]);
				return;
			}
		}
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
						+ ": shop will "
						+ (inventory ? "buy" : "sell")
						+ " for: "
						+ price
						+ " "
						+ Utils.formatPlayerNameForDisplay(ItemDefinitions
								.getItemDefinitions(money).getName()));
	}

	private int getBuyPrice(Item item) {
		switch (money) {
		case 24444: // TROHPY
			if (item.getId() >= 24450 && item.getId() <= 24454)
				return 30 + (item.getId() - 24450) * 5;
			if (item.getId() >= 24455 && item.getId() <= 24457)
				return 1500;
			break;
		default:
			if (money == COINS && key >= 1200
					&& ItemConstants.isTradeable(item)) {
				int price = (int) (item.getDefinitions().getPrice() * 1.4);
				return price == 0 ? 1 : price;

			}
			int price = ClientScriptMap.getMap(731).getIntValue(item.getId());
			if (money == TOKKUL && price > 0)
				return price;
			price = ClientScriptMap.getMap(733).getIntValue(item.getId());
			if (price > 0)
				return price;
			if (item.getDefinitions().hasShopPriceAttributes())
				return 99000;
			// Prices
			price = item.getDefinitions().getPrice();

			if (item.getId() == 11864)
				price = 15000;
			else if (item.getId() == 11866)
				price = 20000;
			else if (item.getId() == 11868)
				price = 25000;
			else if (item.getId() == 11870)
				price = 30000;
			else if (item.getId() == 24386)
				price = 45000;
			else if (item.getId() == 20768)
				price = 1;
			else if (item.getId() == 20767)
				price = 2670000;
			else if (item.getId() == 32151)
				price = 2670000;
			else if (item.getName().contains("master cape")
					|| item.getName().contains("Mastercape"))
				price = 5000000;
			else if (item.getId() == 28828)
				price = 25000000;
			else if (item.getId() == 34786)
				price = 25000000;
			else if (item.getId() == 28833)
				price = 20000000;
			else if (item.getId() == 34785)
				price = 20000000;
			else if (item.getId() == 28833)
				price = 15000000;
			else if (item.getId() == 34785)
				price = 15000000;
			else if (item.getId() == 26567)
				price = 18000000;
			else if (item.getId() == 26568)
				price = 18000000;
			else if (item.getId() == 34785)
				price = 20000000;
			else if (item.getId() == 28833)
				price = 15000000;
			else if (item.getId() == 30368)
				price = 15000000;
			else if (item.getId() == 27215)
				price = 15000000;

			// Torts R Us Prices
			else if (item.getId() == 37775 || item.getId() == 37776
					|| item.getId() == 37777 || item.getId() == 37745
					|| item.getId() == 37746 || item.getId() == 38602)
				price = 40000;
			// fish oil shop prices
			else if (item.getId() == 37762)// Raw tarpon
				price = 2500;
			else if (item.getId() == 37761) // Fishing Bait
				price = 300;
			else if (item.getId() == 37764) // Raw SeerFish
				price = 3000;
			else if (item.getId() == 37766) // Raw Sillago
				price = 5000;
			else if (item.getId() == 37768) // Raw Wobbegong
				price = 6000;
			else if (item.getId() == 37734) // Fishoil
				price = 10000;
			else if (item.getId() == 37739) // Wobbegong Oil
				price = 15000;
			// End

			if (money == TOKKUL)
				price = (price * 3) / 2;
			return Math.max(price, 1);

		}
		return 1;
	}

	private int getSellPrice(Item item) {
		int price = ClientScriptMap.getMap(732).getIntValue(item.getId());
		if (money == TOKKUL && price > 0)
			return price;
		price = ClientScriptMap.getMap(1441).getIntValue(item.getId());
		if (price > 0)
			return price / 10;
		return Math.max(1, (item.getDefinitions().getPrice() * 30) / 65);
	}

	private void refreshShop() {
		for (Player player : viewingPlayers) {
			player.getPackets().sendCSVarString(336,
					customcs2configstring(player));
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	private String customcs2configstring(Player player) {
		String str = "";

		for (int i = 0; i < player.getInventory().getItemsContainerSize(); i++) {
			Item item = player.getInventory().getItem(i);
			if (item == null) {
				String id = Integer.toString(-1, 16);
				while (id.length() < 8)
					id = "0" + id;
				String price = Integer.toString(0, 16);
				while (price.length() < 8)
					price = "0" + price;

				str += id + price;
				continue;
			}

			String id = Integer.toString(item.getId(), 16);
			while (id.length() < 8)
				id = "0" + id;
			String price = Integer.toString(getSellPrice(item), 16);
			while (price.length() < 8)
				price = "0" + price;

			str += id + price;
		}

		int written = 28;
		for (Item aMainStock : mainStock) {
			if (written >= 100)
				break;
			if (aMainStock == null)
				continue;
			String id = Integer.toString(aMainStock.getId(), 16);
			while (id.length() < 8)
				id = "0" + id;
			String price = Integer.toString(getBuyPrice(aMainStock), 16);
			while (price.length() < 8)
				price = "0" + price;
			str += id + price;
			written++;
		}

		if (generalStock != null) {
			for (Item aGeneralStock : generalStock) {
				if (written >= 100)
					break;
				if (aGeneralStock == null)
					continue;
				String id = Integer.toString(aGeneralStock.getId(), 16);
				while (id.length() < 8)
					id = "0" + id;
				String price = Integer.toString(aGeneralStock.getId(), 16);
				while (price.length() < 8)
					price = "0" + price;
				str += id + price;
				written++;
			}
		}

		return str.toUpperCase();

	}

	private int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}

	private void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
		player.getPackets().sendItems(
				generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY, stock);
	}

	private static int getSubDescription(Player player, Item item) {
		ItemDefinitions defs = item.getDefinitions();
		switch (defs.getCSOpcode(2195)) {
		case 12:
			return 33;// Edible Food
		case 6:
			return 34;// Raw Food
		case 35:
			return player.getToolbelt().containsItem(item.getId()) ? 31
					: player.getToolbelt().isTool(item.getId()) ? 30 : 32;
		case 32:
			return 36;// Runes
		default:// There is another way to do this, but this is the best and
			// simplest
			int slot = defs.getEquipSlot();
			if (slot != -1) {
				switch (slot) {
				case Equipment.SLOT_HAT:
					return 0;
				case Equipment.SLOT_CAPE:
					return 1;
				case Equipment.SLOT_CHEST:
					return 3;
				case Equipment.SLOT_LEGS:
					return 4;
				case Equipment.SLOT_HANDS:
					return 5;
				case Equipment.SLOT_FEET:
					return 6;
				case Equipment.SLOT_AMULET:
					return 2;
				case Equipment.SLOT_RING:
					return 7;
				case Equipment.SLOT_ARROWS:
					return 8;
				case Equipment.SLOT_AURA:
					return 60;
				case Equipment.SLOT_SHIELD:
					return 9;
				case Equipment.SLOT_WEAPON:
					if (defs.isMeleeTypeWeapon())
						return Equipment.isTwoHandedWeapon(item) ? 14 : 11;
					else if (defs.isRangeTypeWeapon())
						return Equipment.isTwoHandedWeapon(item) ? 15 : 12;
					else if (defs.isMagicTypeWeapon())
						return Equipment.isTwoHandedWeapon(item) ? 16 : 13;
				}
			}
			return -1;
		}
	}
}