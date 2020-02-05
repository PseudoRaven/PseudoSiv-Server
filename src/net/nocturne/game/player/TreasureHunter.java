package net.nocturne.game.player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.World;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.content.treasurehunter.Prize;
import net.nocturne.game.player.content.treasurehunter.Rewards;
import net.nocturne.utils.Color;
import net.nocturne.utils.ItemExamines;
import net.nocturne.utils.Utils;

public class TreasureHunter implements Serializable {

	private static final long serialVersionUID = -5330047553089876572L;

	private transient Player player;

	private static List<Integer> receivedItems = new ArrayList<Integer>();
	private static List<Item[]> veryRareItems = new ArrayList<Item[]>();

	private Prize reward;

	private Item[] rewards;
	private int[] rewardRarity;

	private Rewards itemRewards;

	public int daily = 0, earned = 1, bought = 2;

	private int rewardIndex;

	private int openMultiple;

	private int dailyKeys, earnedKeys, boughtKeys;

	private int frozenHearts = 100;

	private long delay;

	private boolean claimed = false;

	private int rarityType;

	private static final int COMMON = 0, UNCOMMON = 2, RARE = 3, VERY_RARE = 4;

	public static final double[] CHANCES = new double[] { 1.0D, 0.43D, 0.01D,
			0.005D };

	public static final int[] COMMON_COINS_AMOUNT = new int[] { 100, 250, 500,
			1000 };

	public static final int[] UNCOMMON_COINS_AMOUNT = new int[] { 2000, 5000,
			7500, 10000 };

	public static final int[] RARE_COINS_AMOUNT = new int[] { 100000, 250000,
			500000, 1000000 };

	public static final int[] VERY_RARE_COINS_AMOUNT = new int[] { 10 * 100000,
			5 * 100000 };

	public static final int[] COMMON_LAMPS = new int[] { 23713, 23717, 23721,
			23725, 23729, 23737, 23733, 23741, 23745, 23749, 23753, 23757,
			23761, 23765, 23769, 23778, 23774, 23786, 23782, 23794, 23790,
			23802, 23798, 23810, 23806, 23814, 29545 };

	public static final int[] UNCOMMON_LAMPS = new int[] { 23714, 23718, 23722,
			23726, 23730, 23738, 23734, 23742, 23746, 23750, 23754, 23758,
			23762, 23766, 23770, 23779, 23775, 23787, 23783, 23795, 23791,
			23803, 23799, 23811, 23807, 23815, 29546 };

	public static final int[] RARE_LAMPS = new int[] { 23715, 23719, 23723,
			23727, 23731, 23739, 23735, 23743, 23747, 23751, 23755, 23759,
			23763, 23767, 23771, 23780, 23776, 23788, 23784, 23796, 23792,
			23804, 23800, 23812, 23808, 23816, 29547 };

	public static final int[] VERY_RARE_LAMPS = new int[] { 23716, 23720,
			23724, 23728, 23732, 23740, 23736, 23744, 23748, 23752, 23756,
			23760, 23764, 23768, 23773, 23781, 23777, 23789, 23785, 23797,
			23793, 23805, 23801, 23813, 23809, 23817, 29548 };

	public static final Item[] COMMON_REWARDS = new Item[] { new Item(995, 1),
			new Item(995, 1), new Item(995, 1), new Item(27153, 1),
			new Item(30372, 100), new Item(15272, 25), new Item(7946, 20),
			new Item(15272, 15), new Item(15272, 20), new Item(329, 28),
			new Item(8782, 30), new Item(8782, 20), new Item(960, 20),
			new Item(960, 30), new Item(8780, 20), new Item(15270, 28),
			new Item(13435, 28), new Item(1747, 27), new Item(1761, 10),
			new Item(25549, 4), new Item(1751, 27), new Item(2357, 30),
			new Item(29324, 350), new Item(29191, 80), new Item(29323, 250),
			new Item(29320, 150), new Item(29313, 60), new Item(5304, 7),
			new Item(5301, 8), new Item(5318, Utils.random(5, 10)),
			new Item(5323, 5), new Item(1511, 14), new Item(1513, 28),
			new Item(31088, 1), new Item(313, 100), new Item(1777, 150),
			new Item(9381, 100), new Item(6693, 14), new Item(221, 100),
			new Item(199, 14), new Item(205, 28), new Item(223, 28),
			new Item(29303, 2), new Item(29301, 1), new Item(29302, 1),
			new Item(556, 100), new Item(556, 400), new Item(556, 500),
			new Item(29298, 1), new Item(29294, Utils.random(1, 3)),
			new Item(29296, 3), new Item(29297, 1), new Item(536, 6),
			new Item(536, 20), new Item(534, Utils.random(7, 12)),
			new Item(24336, 400), new Item(29617, 400), new Item(882, 200),
			new Item(877, 200), new Item(9142, 200), new Item(29617, 200),
			new Item(444, 150), new Item(453, 50), new Item(436, 50),
			new Item(447, 10), new Item(449, 10), new Item(2349, 20),
			new Item(29309, 1), new Item(29308, 1), new Item(29304, 1),
			new Item(30915, 40), new Item(1619, 15), new Item(1621, 15),
			new Item(1623, 20), new Item(1631, 10), new Item(6291, 23) };
	public static final Item[] UNCOMMON_REWARDS = new Item[] {
			new Item(995, 1), new Item(995, 1), new Item(995, 1),
			new Item(445, Utils.random(15, 45)),
			new Item(448, Utils.random(15, 45)), new Item(27154, 1),
			new Item(30372, 250), new Item(30372, 500), new Item(15272, 50),
			new Item(329, 56), new Item(373, 56), new Item(8782, 55),
			new Item(8782, 40), new Item(960, 40), new Item(13435, 56),
			new Item(383, 70), new Item(24372, 27), new Item(1761, 150),
			new Item(1761, 250), new Item(1761, 12), new Item(1749, 27),
			new Item(1617, 4), new Item(1658, 15), new Item(1660, 20),
			new Item(1656, 10), new Item(5304, 10), new Item(5374, 1),
			new Item(6034, 10), new Item(1511, 28), new Item(1513, 56),
			new Item(31088, 2), new Item(1777, 250), new Item(6693, 28),
			new Item(199, 28), new Item(3051, 28), new Item(565, 150),
			new Item(29296, 5), new Item(536, 30), new Item(534, 9),
			new Item(453, 150), new Item(453, 250), new Item(29309, 2) };
	public static final Item[] RARE_REWARDS = new Item[] { new Item(995, 1),
			new Item(995, 1), new Item(995, 1), new Item(25185, 1),
			new Item(25186, 1), new Item(25187, 1), new Item(25188, 1),
			new Item(25189, 1), new Item(25190, 1), new Item(25191, 1),
			new Item(25192, 1), new Item(25193, 1), new Item(25194, 1),
			new Item(25195, 1), new Item(25196, 1), new Item(25197, 1),
			new Item(25198, 1), new Item(25199, 1), new Item(29865, 1),
			new Item(29866, 1), new Item(29867, 1), new Item(29868, 1),
			new Item(29869, 1), new Item(31580, 1), new Item(31581, 1),
			new Item(31582, 1), new Item(31583, 1), new Item(31584, 1),
			new Item(31343, 1), new Item(31344, 1), new Item(31345, 1),
			new Item(31346, 1), new Item(31347, 1), new Item(27587, 1),
			new Item(27588, 1), new Item(27589, 1), new Item(27590, 1),
			new Item(27591, 1), new Item(36895, 1), new Item(36896, 1),
			new Item(36897, 1), new Item(36898, 1), new Item(36899, 1),
			new Item(36890, 1), new Item(36891, 1), new Item(36892, 1),
			new Item(36893, 1), new Item(36894, 1), new Item(31585, 1),
			new Item(31586, 1), new Item(31587, 1), new Item(31588, 1),
			new Item(31589, 1), new Item(31575, 1), new Item(31576, 1),
			new Item(31577, 1), new Item(31578, 1), new Item(31579, 1),
			new Item(28995, 1), new Item(28996, 1), new Item(28997, 1),
			new Item(28998, 1), new Item(28999, 1), new Item(25180, 1),
			new Item(25181, 1), new Item(25182, 1), new Item(25183, 1),
			new Item(25184, 1), new Item(32274, 1), new Item(32275, 1),
			new Item(32276, 1), new Item(32277, 1), new Item(34919, 1),
			new Item(34920, 1), new Item(34921, 1), new Item(34922, 1),
			new Item(30920, 1), new Item(27155, 1), new Item(27148, 1),
			new Item(27149, 1), new Item(27150, 1), new Item(27151, 1),
			new Item(27152, 1), new Item(27626, 1), new Item(27627, 1),
			new Item(27628, 1), new Item(27629, 1), new Item(27630, 1),
			new Item(27631, 1), new Item(27632, 1), new Item(27633, 1),
			new Item(27634, 1), new Item(27635, 1), new Item(35357, 1),
			new Item(35358, 1), new Item(35359, 1), new Item(35360, 1),
			new Item(35361, 1), new Item(35362, 1), new Item(35363, 1),
			new Item(35364, 1), new Item(35365, 1), new Item(35366, 1),
			new Item(37846, 1), new Item(37847, 1), new Item(37848, 1),
			new Item(37849, 1), new Item(37850, 1), new Item(37841, 1),
			new Item(37842, 1), new Item(37843, 1), new Item(37844, 1),
			new Item(37845, 1), new Item(28145, 1), new Item(6739, 1),
			new Item(15259, 1) };
	public static final Item[] VERY_RARE_REWARDS = new Item[] {
			new Item(995, 1), new Item(995, 1), new Item(995, 1),
			new Item(23679, 1), new Item(23680, 1), new Item(23681, 1),
			new Item(23682, 1), new Item(23683, 1), new Item(23684, 1),
			new Item(23685, 1), new Item(23686, 1), new Item(23687, 1),
			new Item(23688, 1), new Item(23689, 1), new Item(23690, 1),
			new Item(23691, 1), new Item(23692, 1), new Item(23693, 1),
			new Item(23694, 1), new Item(23695, 1), new Item(23696, 1),
			new Item(23697, 1), new Item(23698, 1), new Item(23699, 1),
			new Item(23700, 1), new Item(25952, 1), new Item(30814, 1),
			new Item(30815, 1), new Item(30816, 1), new Item(30817, 1),
			new Item(30818, 1), new Item(34069, 1), new Item(34071, 1),
			new Item(34073, 1), new Item(31075, 1), new Item(34077, 1),
			new Item(34079, 1), new Item(34081, 1), new Item(34083, 1),
			new Item(34085, 1), new Item(34087, 1), new Item(34089, 1),
			new Item(34091, 1), new Item(34093, 1), new Item(34095, 1),
			new Item(34859, 1), new Item(30915, 300), new Item(34037, 1),
			new Item(34029, 1), new Item(34030, 1), new Item(37134, 1),
			new Item(37129, 1), new Item(37131, 1), new Item(39298, 1),
			new Item(37132, 1), new Item(34038, 1), new Item(39160, 1),
			new Item(37343, 1), new Item(37344, 1), new Item(37345, 1),
			new Item(37346, 1), new Item(37347, 1), new Item(37348, 1),
			new Item(37349, 1), new Item(37350, 1), new Item(37351, 1),
			new Item(37352, 1), new Item(37353, 1), new Item(37354, 1),
			new Item(37355, 1), new Item(37356, 1), new Item(37357, 1) };

	public TreasureHunter() {
		delay = Utils.currentTimeMillis();
		dailyKeys = 0;
		earnedKeys = 0;
		boughtKeys = 0;
	}

	public void setPlayer(Player player) {
		try {
			this.player = player;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processClick(int packetId, int interfaceId, int componentId,
			int slotId, int slotId2) {
		switch (interfaceId) {
		case 1139:
			switch (componentId) {
			case 12:
				if (player.getInterfaceManager()
						.containsTreasureHunterInterface()) {
					player.getPackets()
							.sendGameMessage(
									"Please finish what you are doing before opening Treasure Hunter.");
					return;
				}
				openSpinInterface(true, false, true);
				break;
			default:
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
				break;
			}
			break;
		case 1252:
			switch (componentId) {
			case 5:
				player.getInterfaceManager().removeInterface(1252);
				player.getPackets()
						.sendGameMessage(
								"You can access Treasure Hunter from the side panel, and you can show the button again by"
										+ " logging out and back in.");
				break;
			default:
				if (player.getInterfaceManager()
						.containsTreasureHunterInterface()) {
					player.getPackets()
							.sendGameMessage(
									"Please finish what you are doing before opening Treasure Hunter.");
					return;
				}
				openSpinInterface(false, false, true);
				player.getPackets().sendGameMessage(
						"You can spam click 'I' to skip animations.");
				break;
			}
			break;
		case 1253:
			switch (componentId) {
			case 34:
				// player.getDialogueManager().startDialogue("Alice");
				break;
			case 346:
				player.getVarsManager().setVarBit(1450, -1140842495);
				player.getPackets().sendCSVarInteger(4081, -1);
				player.getPackets().sendCSVarInteger(2045, 1);
				player.getPackets().sendCSVarInteger(1784, 0);
				break;
			case 542:
				player.getPackets().sendHideIComponent(1253, 244, true);
				player.getPackets().sendHideIComponent(1253, 234, false);
				player.getPackets().sendHideIComponent(1253, 284, false);
				player.getPackets().sendHideIComponent(1253, 552, true);
				player.getPackets().sendCSVarInteger(4082, 133);
				player.getPackets().sendHideIComponent(1253, 552, false);
				player.getPackets().sendCSVarInteger(1993, 1);
				break;
			case 50:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(0);
				break;
			case 45:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(1);
				break;
			case 59:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(2);
				break;
			case 41:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(3);
				break;
			case 54:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(4);
				break;
			case 61:
				if (getAllKeys() < 1) {
					player.getPackets().sendGameMessage(
							"You do not have enough keys.");
					reset();
					return;
				}
				selectReward(Utils.random(0, 4));
				break;
			case 440:
			case 659:
				player.getDialogueManager().startDialogue("OpenURLPrompt",
						"store");
				break;
			case 281:
			case 250:
				boolean bank = componentId == 281;
				if (!claimed) {
					Item item = rewards[rewardIndex];
					String name = item.getDefinitions().getName().toLowerCase();
					if (!receivedItems.contains(item.getId())) {
						if (name.contains("warlord") || name.contains("mask")
								|| name.contains("archon")
								|| name.contains("ramokee")
								|| name.contains("artisan")
								|| name.contains("blacksmith")
								|| name.contains("botanist")
								|| name.contains("diviner")
								|| name.contains("sous chef")
								|| name.contains("first age")
								|| name.contains("shaman")
								|| name.contains("farmer")
								|| name.contains("skirmisher"))
							receivedItems.add(item.getId());
					}
					if (bank || !player.getInventory().hasFreeSlots()) {
						if (rarityType == VERY_RARE)
							announceWinner(item.getId(), item.getAmount());
						player.getBank().addItem(item.getId(),
								item.getAmount(), true);
						player.getPackets().sendGameMessage(
								"Your prize has been placed in your bank.");
						try {
							DateFormat dateFormat2 = new SimpleDateFormat(
									"MM/dd/yy HH:mm:ss");
							Calendar cal2 = Calendar.getInstance();
							final String FILE_PATH = Settings
									.getDropboxLocation()
									+ "logs/treasurehunter/";
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(FILE_PATH
											+ player.getUsername() + ".txt",
											true));
							writer.write("["
									+ dateFormat2.format(cal2.getTime())
									+ ", IP: " + player.getSession().getIP()
									+ ", bank] got " + item.getName() + " ("
									+ item.getId() + ") amount: "
									+ item.getAmount());
							writer.newLine();
							writer.flush();
							writer.close();
						} catch (IOException er) {
							er.printStackTrace();
						}
					} else {
						if (rarityType == VERY_RARE)
							announceWinner(item.getId(), item.getAmount());
						if (player.getInventory().getFreeSlots() >= (item
								.getDefinitions().isNoted()
								|| item.getDefinitions().isStackable() ? 1
									: item.getAmount())) {
							player.getInventory().addItem(item.getId(),
									item.getAmount());
							player.getPackets()
									.sendGameMessage(
											"Your prize has been placed in your inventory.",
											true);
						}
						try {
							DateFormat dateFormat2 = new SimpleDateFormat(
									"MM/dd/yy HH:mm:ss");
							Calendar cal2 = Calendar.getInstance();
							final String FILE_PATH = Settings
									.getDropboxLocation()
									+ "logs/treasurehunter/";
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(FILE_PATH
											+ player.getUsername() + ".txt",
											true));
							writer.write("["
									+ dateFormat2.format(cal2.getTime())
									+ ", IP: " + player.getSession().getIP()
									+ ", inventory] got " + item.getName()
									+ " (" + item.getId() + ") amount: "
									+ item.getAmount());
							writer.newLine();
							writer.flush();
							writer.close();
						} catch (IOException er) {
							er.printStackTrace();
						}
					}
					if (getAllKeys() >= 1) {
						player.getPackets().sendIComponentText(interfaceId,
								458, "Play again");
						if (openMultiple > 1) {
							openMultiple--;
							selectReward(Utils.random(0, 4));
						}
					} else {
						player.getPackets().sendIComponentText(interfaceId,
								458, "Exit");
					}
					claimed = true;
				} else if (System.currentTimeMillis() - player.delay < 1500)
					return;
				player.delay = System.currentTimeMillis();
				if (getAllKeys() >= 1) {
					player.getInterfaceManager().removeScreenInterfaceBG();
					openSpinInterface(false, false, true);
				} else
					player.getInterfaceManager().removeScreenInterfaceBG();
				break;
			case 262:
				if (openMultiple > 1) {
					openMultiple--;
					selectReward(Utils.random(0, 4));
				}
				if (getAllKeys() >= 1) {
					player.getInterfaceManager().removeScreenInterfaceBG();
					openSpinInterface(false, false, true);
				} else
					player.getInterfaceManager().removeScreenInterfaceBG();
				break;
			case 242:
				if (!claimed) {
					Item item = rewards[rewardIndex];
					player.getMoneyPouch().setAmount(
							ItemDefinitions.getItemDefinitions(item.getId())
									.getPrice() * item.getAmount(), false);
					player.getPackets().sendIComponentText(interfaceId, 458,
							"Play again");
					player.getPackets().sendGameMessage(
							"Your prize has been placed in your money pouch.",
							true);
					claimed = true;
					if (openMultiple > 1) {
						openMultiple--;
						selectReward(Utils.random(0, 4));
					}
				} else if (System.currentTimeMillis() - player.delay < 1500)
					return;
				if (getAllKeys() >= 1) {
					player.getInterfaceManager().removeScreenInterfaceBG();
					openSpinInterface(false, false, true);
				} else
					player.getInterfaceManager().removeScreenInterfaceBG();
				break;
			case 90:
				openMultiple = 1;
				break;
			case 93:
				player.getPackets().sendGameMessage(
						"This feature is currently disabled.");
				openMultiple = 10;
				break;
			case 766:
				player.getInterfaceManager().removeScreenInterfaceBG();
				openSpinInterface(false, false, true);
				break;
			case 105:
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
				break;
			case 778:
			case 356:
			case 479:
				player.getInterfaceManager().removeScreenInterfaceBG();
				break;
			}
			break;
		}
	}

	private void generateRewards(int type) {
		rewards = new Item[37];
		rewardRarity = new int[37];
		for (int i = 0; i < rewards.length; i++) {
			int random = Utils.random(500);
			int rarity = 0;
			if (i == rewards.length - 1)
				rarity = VERY_RARE;
			else if (random >= 1 && random <= 25)
				rarity = RARE;
			else if (random >= 26 && random <= 200)
				rarity = UNCOMMON;
			else
				rarity = COMMON;
			rewardRarity[i] = rarity;
			rewards[i] = generateReward(type, rarity);
			player.getVarsManager().sendVar(4065 + (i * 2), rewards[i].getId());
			// player.getVarsManager().sendVar(4038 + i,
			// rewards[i].getDefinitions().isMembersOnly() ? 1 : 0);
			player.getPackets().sendCSVarString(3947 + i,
					ItemExamines.getExamine(rewards[i]));
			player.getVarsManager().sendVarBit(21475 + (i * 2),
					rewards[i].getAmount());
			player.getVarsManager().sendVarBit(21474 + (i * 2), rarity + 1);
		}
		player.getVarsManager().sendVar(4335, 4012);
		player.getVarsManager().sendVar(4336, 1);
		rarityType = rewardRarity[Utils.random(0, 36)];
		rewardIndex = Utils.random(0, 36);
	}

	private Item generateReward(int type, int rarityType) {
		boolean isLamp = Utils.random(4) == 0;
		if (isLamp) {
			int[] lamps = COMMON_LAMPS;
			if (rarityType == VERY_RARE) {
				if (Utils.random(10) == 0)
					lamps = VERY_RARE_LAMPS;
				else
					lamps = RARE_LAMPS;
				this.rarityType = rarityType;
			} else if (rarityType == RARE) {
				lamps = RARE_LAMPS;
				this.rarityType = rarityType;
			} else if (rarityType == UNCOMMON) {
				lamps = UNCOMMON_LAMPS;
				this.rarityType = rarityType;
			}
			return new Item(lamps[Utils.random(lamps.length)], 1);
		} else {
			Item[] items = COMMON_REWARDS;
			if (rarityType == VERY_RARE) {
				items = VERY_RARE_REWARDS;
				this.rarityType = rarityType;
			} else if (rarityType == RARE) {
				items = RARE_REWARDS;
				this.rarityType = rarityType;
			} else if (rarityType == UNCOMMON) {
				items = UNCOMMON_REWARDS;
				this.rarityType = rarityType;
			}
			Item item = items[Utils.random(items.length)];
			if (item.getId() == 995) {
				int[] amounts = COMMON_COINS_AMOUNT;
				if (rarityType == VERY_RARE) {
					amounts = VERY_RARE_COINS_AMOUNT;
				} else if (rarityType == RARE) {
					amounts = RARE_COINS_AMOUNT;
				} else if (rarityType == UNCOMMON) {
					amounts = UNCOMMON_COINS_AMOUNT;
				}
				item.setAmount(amounts[Utils.random(amounts.length)]);
			}
			if (receivedItems.contains(item.getId()))
				generateReward(0, Utils.random(0, 4));
			return item;
		}
	}

	private void announceWinner(int itemId, int amount) {
		String itemName = ItemDefinitions.getItemDefinitions(itemId).getName()
				.toLowerCase();
		Item[] rareLamps = new Item[VERY_RARE_LAMPS.length];
		for (int i = 0; i < VERY_RARE_LAMPS.length; i++)
			rareLamps[i] = new Item(VERY_RARE_LAMPS[i], 1);
		if (!veryRareItems.contains(rareLamps))
			veryRareItems.add(rareLamps);
		else if (!veryRareItems.contains(VERY_RARE_REWARDS))
			veryRareItems.add(VERY_RARE_REWARDS);
		if (!veryRareItems.contains(itemId))
			return;
		World.sendNews(player,
				player.getDisplayName() + " has won x" + Utils.format(amount)
						+ " " + itemName + " on treasure hunter.", 1, true);
		player.getPackets().sendGameMessage(Color.PURPLE,
				"Congratulations! You've just won a very rare prize!");
	}

	public void selectReward(int chestIndex) {
		if (System.currentTimeMillis() - player.delay < 1500)
			return;
		player.delay = System.currentTimeMillis();
		if (reward != null) {
			open(true, false);
			if (reward.getItem().getId() == ItemIdentifiers.SILVERHAWK_BOOTS)
				player.setSilverhawkFeathers(500);
			return;
		}
		claimed = false;
		generateRewards(0);
		player.getCompCapeManager().increaseRequirement(
				Requirement.TREASURE_HUNTER, 1);
		sendReward(reward, chestIndex, true, openMultiple > 1);
	}

	public void sendReward(Prize prize, int chestIndex, boolean instant,
			boolean openAll) {
		if (getAllKeys() < 1) {
			player.getPackets().sendGameMessage("You do not have enough keys.");
			reset();
			return;
		}
		chestIndex += 1;
		int cashoutValue = ItemDefinitions.getItemDefinitions(
				rewards[rewardIndex].getId()).getPrice()
				* rewards[rewardIndex].getAmount();
		player.getVarsManager().setVarBit(20736, 1290);
		player.getVarsManager().setVarBit(20747, 0);
		player.getVarsManager().setVarBit(20742, 0);
		player.getVarsManager().setVarBit(20738, 1);
		player.getVarsManager().setVarBit(20749, 0);
		player.getVarsManager().setVarBit(20739, 1);
		player.getVarsManager().setVarBit(20752, 1);
		player.getVarsManager().setVarBit(20751, 0);
		player.getVarsManager().setVarBit(25533, 1);
		player.getVarsManager().setVarBit(28369, 1);
		player.getVarsManager().setVarBit(28370, 0);
		player.getVarsManager().setVarBit(20744, 12);
		player.getVarsManager().setVarBit(20738, 0);
		player.getVarsManager().setVarBit(20749, 90);
		player.getVarsManager().setVarBit(20739, 0);
		player.getVarsManager().setVarBit(20750, 73);
		player.getVarsManager().setVarBit(20752, 0);
		player.getVarsManager().setVarBit(20751, 30);
		player.getVarsManager().setVarBit(25533, 0);
		player.getVarsManager().setVarBit(25534, 30);
		player.getVarsManager().setVarBit(28369, 0);
		player.getVarsManager().setVarBit(28370, 15);
		player.getVarsManager().setVarBit(20736, 1289);
		player.getVarsManager().setVarBit(20747, 1);
		player.getVarsManager().setVarBit(20742, 1);
		player.getVarsManager().setVarBit(20738, 1);
		player.getVarsManager().setVarBit(20749, 0);
		player.getVarsManager().setVarBit(20739, 1);
		player.getVarsManager().setVarBit(20752, 1);
		player.getVarsManager().setVarBit(20751, 0);
		player.getVarsManager().setVarBit(25533, 1);
		player.getVarsManager().setVarBit(28369, 1);
		player.getVarsManager().setVarBit(28370, 0);
		player.getVarsManager().setVarBit(20744, 11);
		player.getVarsManager().setVarBit(20738, 0);
		player.getVarsManager().setVarBit(20749, 89);
		player.getVarsManager().setVarBit(20739, 0);
		player.getVarsManager().setVarBit(20750, 72);
		player.getVarsManager().setVarBit(20752, 0);
		player.getVarsManager().setVarBit(20751, 29);
		player.getVarsManager().setVarBit(25533, 0);
		player.getVarsManager().setVarBit(25534, 29);
		player.getVarsManager().setVarBit(28369, 0);
		player.getVarsManager().setVarBit(28370, 14);
		player.getVarsManager().setVarBit(20736, 1288);
		player.getVarsManager().setVarBit(20747, 2);
		player.getVarsManager().setVarBit(20742, 2);
		player.getVarsManager().setVarBit(20738, 1);
		player.getVarsManager().setVarBit(20749, 0);
		player.getVarsManager().setVarBit(20739, 1);
		player.getVarsManager().setVarBit(20752, 1);
		player.getVarsManager().setVarBit(20751, 0);
		player.getVarsManager().setVarBit(25533, 1);
		player.getVarsManager().setVarBit(28369, 1);
		player.getVarsManager().setVarBit(28370, 0);
		player.getVarsManager().setVarBit(20744, 10);
		player.getVarsManager().setVarBit(20738, 0);
		player.getVarsManager().setVarBit(20749, 88);
		player.getVarsManager().setVarBit(20739, 0);
		player.getVarsManager().setVarBit(20750, 71);
		player.getVarsManager().setVarBit(20752, 0);
		player.getVarsManager().setVarBit(20751, 28);
		player.getVarsManager().setVarBit(25533, 0);
		player.getVarsManager().setVarBit(25534, 28);
		player.getVarsManager().setVarBit(28369, 1);
		player.getVarsManager().setVarBit(28370, 13);
		player.getPackets().sendExecuteScript(7486,
				new Object[] { 41615361, 24130232 });
		player.getVarsManager().setVarBit(1450, -1140846591);
		player.getVarsManager().setVarBit(4478, 0);
		player.getVarsManager().setVarBit(1452, 18832243);
		player.getVarsManager().setVarBit(1449, 1342968724);
		player.getVarsManager().setVarBit(1448, 138412032);
		player.getVarsManager().setVarBit(1451, 139460608);
		player.getVarsManager().setVarBit(4141, 1);
		player.getVarsManager().setVarBit(1444, 268473236);
		player.getVarsManager().setVarBit(1450, -1140846591);
		player.getVarsManager().setVarBit(1454, cashoutValue);
		player.getVarsManager().setVarBit(1455, 1);
		player.getVarsManager().setVarBit(1451, 139722752);
		player.getPackets().sendCSVarInteger(2045, 1);
		// player.getPackets().sendExecuteScript(4121, new Object[] { 1 });
		player.getPackets().sendIComponentText(1253, 265, "Discard");
		player.getPackets().sendHideIComponent(1253, 31, true);
		player.getPackets().sendIComponentText(
				1253,
				227,
				(openMultiple > 1 ? Utils.format(11 - openMultiple) : "1")
						+ "/" + (openMultiple > 1 ? "10" : "1"));
		generateRewards(0);
		claimed = false;
		String description = ItemExamines.getExamine(rewards[rewardIndex]);
		player.getPackets()
				.sendExecuteScript(
						9122,
						new Object[] {
								instant ? 1 : 0,
								rewards[rewardIndex].getId(),
								rewards[rewardIndex].getAmount(),
								rarityType,// rarity
								0,// category
								0,
								ItemDefinitions.getItemDefinitions(
										rewards[rewardIndex].getId())
										.getPrice()
										* rewards[rewardIndex].getAmount(),
								2,
								(player.getInventory().getFreeSlots() >= (rewards[rewardIndex]
										.getDefinitions().isNoted()
										|| rewards[rewardIndex]
												.getDefinitions().isStackable() ? 1
										: rewards[rewardIndex].getAmount())) ? 1
										: 2,
								player.getBank().hasBankSpace() ? 3 : 4,
								description, rewards[rewardIndex].getAmount(),
								rewards[rewardIndex].getId(), instant ? 1 : 0 });
		handleKeys(1, true);
		player.getPackets().sendCSVarInteger(1800,
				getAllKeys() < 1 ? 0 : getAllKeys());
	}

	public void openSpinInterface(boolean force, boolean instant, boolean first) {
		openMultiple = 1;
		if (getAllKeys() < 1) {
			player.getPackets().sendGameMessage("You do not have enough keys.");
		}
		if (!force) {
			if (player.getInterfaceManager().containsInventoryInter()
					|| player.getInterfaceManager().containsScreenInterface()
					|| player.getInterfaceManager()
							.containsTreasureHunterInterface()) {
				player.getPackets()
						.sendGameMessage(
								"Please finish what you are doing before opening Treasure Hunter.");
				return;
			}
		}
		if (player.getControllerManager().getController() != null) {
			player.getPackets().sendGameMessage(
					"You can't open Treasure Hunter in this area.");
			return;
		}
		player.stopAll();
		player.getInterfaceManager().setWindowInterface(
				InterfaceManager.SCREEN_BACKGROUND_INTER_COMPONENT_ID, 1253);
		open(instant, first);
	}

	public void open(boolean instant, boolean first) {
		player.getPackets().sendIComponentText(1253, 31, "");
		player.getPackets()
				.sendIComponentText(1253, 445,
						"Earn spins by skilling, participating activities, donating, and more!");
		player.getVarsManager().setVarBit(4143, 0);
		player.getVarsManager().setVarBit(1451, 5242880);
		player.getVarsManager().setVarBit(1450, -1140842495);
		player.getVarsManager().setVarBit(1449, -1877732866);
		player.getPackets().sendCSVarInteger(4082, frozenHearts);
		player.getPackets().sendCSVarInteger(3906, 0);
		player.getPackets().sendCSVarInteger(4142, 10);
		player.getPackets().sendCSVarInteger(1800,
				getAllKeys() < 1 ? 0 : getAllKeys());
		player.getPackets().sendCSVarInteger(1781, 0);
		player.getPackets().sendCSVarInteger(2911, -1);
		player.getPackets().sendCSVarInteger(4142, 10);
		player.getPackets().sendCSVarInteger(1790, 0);
		player.getPackets().sendCSVarInteger(4079, 0);
		player.getPackets().sendCSVarInteger(4080, 1);
		player.getPackets().sendCSVarInteger(1993, 1);
		player.getVarsManager().setVarBit(4143, 0);
		/*
		 * player.getPackets().sendExecuteScript(1522, new Object[] { 0, "", -1,
		 * 0 }); player.getPackets().sendExecuteScript(1522, new Object[] { 0,
		 * "", -1, 1 }); player.getPackets().sendExecuteScript(1522, new
		 * Object[] { 0, "", -1, 2 });
		 * player.getPackets().sendExecuteScript(1522, new Object[] { 0, "", -1,
		 * 3 }); player.getPackets().sendExecuteScript(1522, new Object[] { 0,
		 * "", -1, 4 }); player.getPackets().sendExecuteScript(1522, new
		 * Object[] { 0, "", -1, 5 });
		 * player.getPackets().sendExecuteScript(1522, new Object[] { 0, "", -1,
		 * 6 }); player.getPackets().sendExecuteScript(1522, new Object[] { 0,
		 * "", -1, 7 }); player.getPackets().sendExecuteScript(1522, new
		 * Object[] { 0, "", -1, 8 });
		 * player.getPackets().sendExecuteScript(1522, new Object[] { 0, "", -1,
		 * 9 });
		 */
		player.getPackets().sendExecuteScript(1522,
				new Object[] { 1, "Treasure Hunter", 25687, 0 });
		player.getPackets().sendExecuteScript(2412, new Object[] { 88 });
		player.getPackets().sendCSVarInteger(3906, 0);
		player.getPackets().sendCSVarInteger(4142, 10);
		player.getPackets().sendCSVarInteger(4082, frozenHearts);
		player.getPackets().sendCSVarInteger(1800, getAllKeys());
		player.getPackets().sendCSVarInteger(2911, -1);
		player.getPackets().sendExecuteScript(187, new Object[] { 1, 7 });
		// player.getInterfaceManager().openWidget(1477, 486, 1253, false);
		player.getPackets().sendExecuteScript(8178, new Object[] {});
		player.getPackets().sendCSVarInteger(1928, 0); // enabling colorful
		player.getPackets().sendExecuteScript(6973, new Object[] {});
		player.getPackets().sendCSVarInteger(1993, 1);
		player.getPackets().sendExecuteScript(11189,
				new Object[] { 35, 24130227, 5012, 32102 });
		player.getPackets().sendHideIComponent(1253, 466, true);
		player.getPackets().sendHideIComponent(1253, 284, false);
		if (!first)
			sendReward(reward, rewardIndex, true, false);
	}

	public void handleKeys(int amount, boolean remove) {
		if (remove) {
			if (dailyKeys >= 1)
				dailyKeys -= amount;
			else if (earnedKeys >= 1)
				dailyKeys -= amount;
			else if (boughtKeys >= 1)
				dailyKeys -= amount;
		} else {
			if ((Utils.currentTimeMillis() - delay) < (12 * 60 * 60 * 1000))
				return;
			for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
				player.grandExchangeLimit[i] = 0;
			}
			delay = Utils.currentTimeMillis();
			if (player.getDonationManager().isExtremeDonator())
				dailyKeys += 2;
			else if (player.getDonationManager().isDivineDonator())
				dailyKeys += 3;
			else if (player.getDonationManager().isDemonicDonator())
				dailyKeys += 4;
			else if (player.getDonationManager().isHeroicDonator())
				dailyKeys += 5;
			else
				dailyKeys += 1;
			player.getPackets().sendGameMessage(
					"You have received your daily Treasure Hunter "
							+ (player.getDonationManager().isDonator() ? "keys"
									: "key") + ".");
		}
	}

	public void reset() {
		dailyKeys = 0;
		earnedKeys = 0;
		boughtKeys = 0;
	}

	public int getAllKeys() {
		return dailyKeys + earnedKeys + boughtKeys;
	}

	public int getKeys() {
		return dailyKeys;
	}

	public void setKeys(int dailyKeys) {
		this.dailyKeys = dailyKeys;
	}

	public int getEarnedKeys() {
		return earnedKeys;
	}

	public void setEarnedKeys(int earnedKeys) {
		this.earnedKeys = earnedKeys;
	}

	public int getBoughtKeys() {
		return boughtKeys;
	}

	public void setBoughtKeys(int boughtKeys) {
		this.boughtKeys = boughtKeys;
	}

	public Rewards getItemRewards() {
		return itemRewards;
	}

	public void setItemReward(Rewards itemRewards) {
		this.itemRewards = itemRewards;
	}

	public Prize getReward() {
		return reward;
	}

	public void setReward(Prize reward) {
		this.reward = reward;
	}

	public void handleBoughtKeys(int amount) {
		this.boughtKeys += amount;
	}

	public void handleEarnedKeys(int amount) {
		this.earnedKeys += amount;
	}
}