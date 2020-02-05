package net.nocturne.game.player.content.activities;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.World;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;

public class XPWell {

	private static final int WELL_GOAL = 15000000;
	private static int wellAmount;

	/**
	 * Sends a dialogue for the amount to give.
	 *
	 * @param player
	 *            The Player giving the amount.
	 */
	public static void give(Player player) {
		if (isWellActive()) {
			player.getPackets().sendGameMessage(
					"The XP well is already active! Go train!");
			return;
		}
		player.getPackets().sendInputIntegerScript(
				"Progress: "
						+ NumberFormat.getNumberInstance(Locale.US).format(
								wellAmount)
						+ " GP ("
						+ ((wellAmount * 100) / WELL_GOAL)
						+ "% of Goal); Goal: "
						+ NumberFormat.getNumberInstance(Locale.US).format(
								WELL_GOAL) + " GP");
		player.getTemporaryAttributtes().put("WellOfExperience", Boolean.TRUE);
	}

	public static int getWellAmount() {
		return wellAmount;
	}

	public static int getWellGoal() {
		return WELL_GOAL;
	}

	public static void addWellAmount(Player player, int amount) {
		if (player.getInventory().getCoinsAmount() < amount) {
			player.getPackets().sendGameMessage(
					"You do not have enough coins to donate this amount.");
			return;
		} else if (amount > WELL_GOAL - wellAmount)
			amount = WELL_GOAL - wellAmount;
		player.getInventory().removeItemMoneyPouch(ItemIdentifiers.COINS,
				amount);
		wellAmount += amount;
		World.sendWorldMessage(
				"<col=FF0000>"
						+ player.getDisplayName()
						+ " has contributed "
						+ NumberFormat.getNumberInstance(Locale.US).format(
								amount) + " GP to the XP well! Progress now: "
						+ ((wellAmount * 100) / WELL_GOAL) + "%!", false);
		World.sendWorldMessage(
				"<col=FF0000> The XP well is currently active! The XP modifier is currently 1.5x.",
				false);
	}

	public static boolean isWellActive() {
		return wellAmount >= WELL_GOAL;
	}

	public static void addWellTask() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				wellAmount = 0;
				World.sendWorldMessage(
						"<col=FF0000>The XP well has been reset!", false);
			}
		}, 0, 3, TimeUnit.HOURS);
	}
}