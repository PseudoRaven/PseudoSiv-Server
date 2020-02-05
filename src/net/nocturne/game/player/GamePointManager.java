package net.nocturne.game.player;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.content.activities.events.GlobalEvents;
import net.nocturne.game.player.content.activities.events.GlobalEvents.Event;
import net.nocturne.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Dec 8, 2016
 */

public class GamePointManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4639093911019058481L;
	private Player player;
	private int gamePoints;
	private List<String> gamePointDrops, gamePointDropExclusions;
	private long[] gamePointRewardTimes;
	private int gamePointRewardType;
	private int gamePointType;
	private boolean gamePointAllDrops;
	private int bankType;
	private int lastIndex;
	private GPR gpr;

	public GamePointManager(Player player) {
		this.player = player;
		this.gamePointRewardTimes = new long[200];
		this.gamePointDrops = new ArrayList<>();
		this.gamePointDropExclusions = new ArrayList<>();
		this.gamePoints = 0;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setGamePointType(int value) {
		gamePointType = value;
	}

	public void setGamePointRewardType(int value) {
		gamePointRewardType = value;
	}

	public void setGamePointAllDrops(boolean b) {
		gamePointAllDrops = b;
	}

	public void setGamePoints(int amt) {
		gamePoints = amt;
	}

	public List<String> getGamePointDrops() {
		return gamePointDrops;
	}

	public List<String> getGamePointDropExclusions() {
		return gamePointDropExclusions;
	}

	public int getBankType() {
		return bankType;
	}

	public void setBankType(int type) {
		bankType = type;
	}

	public void increaseGamePoints(int points) {
		if (points == 0)
			return;
		double rateIncrease = 5;
		if (GlobalEvents.isActiveEvent(Event.GAME_POINTS))
			rateIncrease += 0.5;
		if (player.isAnIronMan())
			rateIncrease += 1;
		points *= rateIncrease;
		gamePoints += points;
		if (gamePointType <= 1)
			player.getPackets().sendGameMessage(
					"<col=FF8330><shad=00001A>You have received "
							+ Utils.getFormattedNumber(points)
							+ " game points, you now have a total of "
							+ Utils.getFormattedNumber(gamePoints)
							+ " game points.");
		if (gamePointType == 0 && Utils.random(5) == 0)
			player.getPackets()
					.sendGameMessage(
							"<col=FF8330><shad=00001A>If you want the game point message shown, "
									+ "type ::showgamepoints. If you want them hidden, type ::hidegamepoints.");
		if (gamePointRewardType == 0 && gamePoints >= 5000
				&& Utils.random(5) == 0)
			player.getPackets()
					.sendGameMessage(
							"<col=FF8330><shad=00001A>Congratulations!"
									+ " You now have enough game points for a reward! Check out ::gamepointrewards for more information.");
	}

	public int getGamePoints() {
		return gamePoints;
	}

	public enum GPR {

		DOUBLE_GAME_POINTS(0, 30000, false), MELEE_WEAKNESS(1, 25000, true), RANGE_WEAKNESS(
				2, 25000, true), MAGIC_WEAKNESS(3, 25000, true), INVENTORY_DROPS(
				4, 15000, false), BANK_DROPS(5, 150000, false), SKILL_DROPS(6,
				5000, false), COIN_DROPS(7, 10000, false), WEAKNESS(8, 35000,
				true), BETTER_DROPS(9, 100000, false), BANK_SKILLS(10, 50000,
				false), MORE_SUCCESS(11, 15000, false), MORE_SKILLS(12, 30000,
				false), MORE_XP(13, 30000, false), CANNON(14, 50000, true), FORCE_AGGRO(
				15, 40000, true);

		private int index, cost;
		private boolean legacy;

		GPR(int index, int cost, boolean legacy) {
			this.index = index;
			this.cost = cost;
			this.legacy = legacy;
		}

		public static Map<Integer, GPR> gprs = new HashMap<>();

		static {
			for (final GPR gpr : GPR.values()) {
				gprs.put(gpr.getIndex(), gpr);
			}
		}

		public static GPR forId(int id) {
			return gprs.get(id);
		}

		public int getCost() {
			return cost;
		}

		public int getIndex() {
			return index;
		}

		public boolean isLegacy() {
			return legacy;
		}
	}

	public void gamePointsReward(int index, String rewardMessage) {
		gpr = GPR.forId(index);
		int points = gpr.getCost();
		String name = gpr.name().toLowerCase().replace("_", " ");
		if (gpr.isLegacy() && !player.isInLegacyCombatMode()) {
			player.getPackets()
					.sendGameMessage(
							"<col=FF8330><shad=00001A>You must be in legacy mode in order to activate this reward!");
			return;
		} else if (gamePoints < points) {
			player.getPackets().sendGameMessage(
					"<col=FF8330><shad=00001A>You need "
							+ Utils.getFormattedNumber(points)
							+ " game points to get " + name + "!");
			return;
		}
		if (hasGamePointsReward(gpr)) {
			if (lastIndex != index) {
				player.getPackets().sendGameMessage(
						"<col=FF8330><shad=00001A>You still have "
								+ ((gamePointRewardTimes[index] - System
										.currentTimeMillis()) / 60000)
								+ " minutes of " + name + ".");
				player.getPackets()
						.sendGameMessage(
								"<col=FF8330><shad=00001A>Type this command again if you want to add another 60 minutes of "
										+ name + ".");
				lastIndex = index;
				return;
			}
			gamePoints -= points;
			gamePointRewardTimes[index] += 1000 * 60 * 60;
			player.getPackets().sendGameMessage(
					"<col=FF8330><shad=00001A>You've added another 60 minutes of "
							+ name + ".");
			return;
		}
		if (gamePointRewardType <= 1)
			player.getPackets()
					.sendGameMessage(
							"<col=FF8330><shad=00001A>If you want to check your time left on your game point reward, type ::checkgamepoints.");
		gamePoints -= points;
		gamePointRewardTimes[index] = System.currentTimeMillis() + 1000 * 60 * 60;
		player.getPackets().sendGameMessage(
				"<col=FF8330><shad=00001A>Congratulations! " + rewardMessage);
	}

	public boolean hasGamePointsReward(GPR gpr) {

		return !(gpr.isLegacy() && !player.isInLegacyCombatMode())
				&& hasGamePointsReward(gpr, true);
	}

	public boolean hasGamePointsReward(GPR gpr, boolean sendMessage) {
		boolean rewardIsActive = gamePointRewardTimes[gpr.getIndex()] > 0;
		boolean rewardTimeIsUp = gamePointRewardTimes[gpr.getIndex()] < System
				.currentTimeMillis();
		if (rewardIsActive && sendMessage)
			if (rewardTimeIsUp) {
				player.getPackets().sendGameMessage(
						"<col=FF8330><shad=00001A> Your game points reward, "
								+ gpr.name().toLowerCase().replace("_", " ")
								+ ", is no longer active.");
				gamePointRewardTimes[gpr.getIndex()] = 0;
			} else if (gamePointRewardType <= 1 && Utils.random(5) == 0)
				player.getPackets()
						.sendGameMessage(
								"<col=FF8330><shad=00001A>If you want to check your time left on your game point reward, type ::checkgamepoints.");
		return gamePointRewardTimes[gpr.getIndex()] > System
				.currentTimeMillis();
	}

	public boolean isGamePointDrop(String itemName) {
		for (String name : gamePointDropExclusions) {
			if (name == null)
				continue;
			if (itemName.toLowerCase().contains(name.toLowerCase()))
				return false;
		}
		if (gamePointAllDrops)
			return true;
		for (String name : gamePointDrops) {
			if (name == null)
				continue;
			if (itemName.toLowerCase().contains(name.toLowerCase()))
				return true;
		}
		return false;
	}

	public void checkGamePoints() {
		player.getPackets().sendGameMessage(
				"<col=FF8330><shad=00001A>You have a total of "
						+ Utils.getFormattedNumber(gamePoints)
						+ " game points.");
		for (int index = 0; index < gamePointRewardTimes.length; index++) {
			gpr = GPR.forId(index);
			if (hasGamePointsReward(gpr))
				player.getPackets().sendGameMessage(
						"<col=FF8330><shad=00001A>You have "
								+ ((gamePointRewardTimes[index] - System
										.currentTimeMillis()) / 60000)
								+ " minutes of "
								+ gpr.name().toLowerCase().replace("_", " ")
								+ " left.");
		}
	}

	public void addGamePointItem(Item item) {
		if (hasGamePointsReward(GPR.BANK_SKILLS)) {
			if (ItemConstants.isTradeable(item)) {
				if (!player.isAnIronMan()
						&& player.getClanManager() != null
						&& player.getClanManager().getClan()
								.getClanBank(player) != null) {
					if (player.getClanManager().getClan().getClanBank(player)
							.hasBankSpace()) {
						player.getPackets()
								.sendGameMessage(
										String.format(
												"<col=EE4000>The following item has been added to your clan bank: %sx %s.</col>",
												item.getAmount(),
												ItemDefinitions
														.getItemDefinitions(
																item.getId())
														.getName()
														.toLowerCase()), true);
						player.getClanManager().getClan().getClanBank(player)
								.addItem(item.getId(), item.getAmount(), true);
						return;
					}
				}
			}
			if (player.getBank().hasBankSpace()) {
				player.getPackets()
						.sendGameMessage(
								String.format(
										"<col=EE4000>The following item has been added to your bank: %sx %s.</col>",
										item.getAmount(),
										ItemDefinitions
												.getItemDefinitions(
														item.getId()).getName()
												.toLowerCase()), true);
				player.getBank().addItem(item, true);
				return;
			} else {
				player.getPackets().sendGameMessage(
						"Not enough space in your bank.");
			}
		}
		if (!player.getInventory().hasFreeSlots())
			player.getInventory().addItem(item.getId(), item.getAmount(), true);
		player.getInventory().addItemMoneyPouch(item);
	}

	public void resetGamePoints() {
		gamePoints = 0;
	}

}
