package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.utils.Utils;

public class AccessorySmithing {

	public static final int ACCESSORY_INTERFACE = 675;
	private static final int[] MOLDS = { 1592, 1597, 1595, 11065 };
	private static final int[] GEMS = { 2357, 1607, 1605, 1603, 1601, 1615,
			6573 };
	private static final int[][] ITEMS = {
			{ 1635, 1637, 1639, 1641, 1643, 1645, 6575 },
			{ 1654, 1656, 1658, 1660, 1662, 1664, 6577 },
			{ 1673, 1675, 1677, 1679, 1681, 1683, 6579 },
			{ 11069, 11072, 11076, 11085, 11092, 11115, 11130 } };
	private static final int[] COMPONENTS_BASE = { 20, 39, 58, 77 };
	private static final double[][] EXPERIENCE = {
			{ 15, 40, 55, 70, 85, 100, 115 }, { 20, 55, 60, 75, 90, 105, 120 },
			{ 25, 60, 65, 80, 95, 110, 125 }, { 30, 65, 70, 85, 100, 150, 165 } };
	private static final byte[][] LEVEL = { { 5, 20, 27, 34, 43, 55, 67 },
			{ 6, 22, 29, 40, 56, 72, 82 }, { 7, 23, 30, 42, 58, 74, 84 },
			{ 8, 24, 31, 50, 70, 80, 90 } };
	private static final int[] ONYX = { 6575, 6577, 6579, 11130 };
	private static final int[] PRE_IMBUED_RINGS = { 1635, 1637, 1639, 1641,
			1643, 1645, 9104, 4202, 6564, 6731, 6733, 6735, 6737 };
	public static final int[] POST_IMBUED_RINGS = { 15009, 15010, 15011, 15012,
			15013, 15014, 15015, 15016, 15017, 15018, 15019, 15020, 15220 };

	private static boolean checkAll(Player player, int primaryIndex,
			int secondaryIndex, boolean cs2Call) {
		int level = LEVEL[primaryIndex][secondaryIndex];
		if (!player.getSkills().hasLevel(Skills.CRAFTING, level))
			return false;
		else if (!player.getInventory().containsItem(2357, 1) && !cs2Call) {
			player.getPackets().sendGameMessage(
					"You need a gold bar in order to create an accessory.");
			return false;
		} else if (secondaryIndex != 0
				&& !player.getInventory().containsItem(GEMS[secondaryIndex], 1)) {
			if (!cs2Call)
				player.getPackets()
						.sendGameMessage(
								"You are missing required the required items in order to create this type of accessory.");
			return false;
		}
		return true;
	}

	public static void handleButtonClick(Player player, int componentId,
			final int tick) {
		for (int primaryIndex = 0; primaryIndex < COMPONENTS_BASE.length; primaryIndex++) {
			for (int secondaryIndex = 0; secondaryIndex < ITEMS[primaryIndex].length; secondaryIndex++) {
				if (componentId == (COMPONENTS_BASE[primaryIndex] + secondaryIndex * 2)) {
					if (!player.getInventory().containsItemToolBelt(
							MOLDS[primaryIndex]))
						return;
					final int actionPrimaryIndex = primaryIndex, actionSecondaryIndex = secondaryIndex;
					player.closeInterfaces();
					player.getActionManager().setAction(new Action() {

						int ticks;

						@Override
						public boolean start(Player player) {
							this.ticks = tick;
							return process(player);
						}

						@Override
						public boolean process(Player player) {
							return !(ticks <= 0 || !checkAll(player,
									actionPrimaryIndex, actionSecondaryIndex,
									false));
						}

						@Override
						public int processWithDelay(Player player) {
							ticks--;
							player.setNextAnimation(new Animation(3243));
							player.getInventory().deleteItem(2357, 1);
							int gem = GEMS[actionSecondaryIndex];
							if (gem != 2357)
								player.getInventory().deleteItem(gem, 1);
							player.getInventory()
									.addItem(
											actionSecondaryIndex == 6 ? ONYX[actionPrimaryIndex]
													: ITEMS[actionPrimaryIndex][actionSecondaryIndex],
											1);
							player.getSkills()
									.addXp(Skills.CRAFTING,
											EXPERIENCE[actionPrimaryIndex][actionSecondaryIndex]);
							return 2;
						}

						@Override
						public void stop(Player player) {
							setActionDelay(player, 3);
						}
					});
				}
			}
		}
	}

	private static final int[] EGG_IDS = { 3689, 3690, 3691, 3692, 3693, 3694 };

	public static void ringTransformation(Player player, final int itemId) {
		if (player.getActionManager().getAction() != null) {
			player.getPackets().sendGameMessage(
					"Please finish what you are doing before transforming.");
			return;
		}
		player.getActionManager().setAction(new Action() {

			@Override
			public boolean start(Player player) {
				player.stopAll();
				player.lock(2);
				int transformationId = EGG_IDS[Utils.random(EGG_IDS.length)];
				if (itemId == 6583)
					transformationId = 2626;
				player.getAppearence().transformIntoNPC(transformationId);
				player.getInterfaceManager().sendInventoryInterface(375);
				return true;
			}

			@Override
			public boolean process(Player player) {
				return true;
			}

			@Override
			public int processWithDelay(Player player) {
				return 0;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, 3);
				resetTransformation(player);
			}
		});
	}

	private static void resetTransformation(Player player) {
		player.getInterfaceManager().removeInventoryInterface();
		player.getInventory().init();
		player.setNextAnimation(new Animation(14884));
		player.getAppearence().transformIntoNPC(-1);
	}

	public static boolean isEmptyRing(int itemId) {
		for (int ring : PRE_IMBUED_RINGS) {
			if (ring == itemId)
				return true;
		}
		return false;
	}

	public static int getImbuedId(int itemId) {
		for (int index = 0; index < PRE_IMBUED_RINGS.length; index++) {
			if (PRE_IMBUED_RINGS[index] == itemId)
				return POST_IMBUED_RINGS[index];
		}
		return -1;
	}
}
