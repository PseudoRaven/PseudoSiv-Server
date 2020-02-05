package net.nocturne.game.player.content;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.nocturne.Settings;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.World;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class Lottery {

	public final static Lottery INSTANCE = new Lottery();
	public final static ArrayList<Player> TICKETS = new ArrayList<>();
	public final static ArrayList<String> USERNAMES = new ArrayList<>();
	public final static boolean active = false;

	public void establish() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {

			private final AtomicInteger MINUTES = new AtomicInteger(10);

			@Override
			public void run() {
				switch (MINUTES.getAndDecrement()) {
				case 10:
				case 5:
					if (TICKETS.size() > 12)
						message("It is less than " + (MINUTES.get() + 1)
								+ " minutes till jackpot is being given out.");
					break;
				case 0:
					if (TICKETS.size() > 12) {
						giveLotteryPrice();
					}
					MINUTES.set(10);
					break;
				}
			}
		}, 0, 1, TimeUnit.MINUTES);
	}

	public void giveLotteryPrice() {
		final ArrayList<Player> POSSIBLE_WINNERS = new ArrayList<>(
				TICKETS.size());
		POSSIBLE_WINNERS.addAll(TICKETS.stream()
				.filter(e -> e != null && e.getPrize() == null)
				.collect(Collectors.toList()));
		if (POSSIBLE_WINNERS.size() > 0) {
			Player winner = POSSIBLE_WINNERS.get(Utils.random(POSSIBLE_WINNERS
					.size()));
			final Item prize = getPrize();
			message(winner.getDisplayName()
					+ " has won the lottery with a price of "
					+ Utils.format(prize.getAmount()) + " "
					+ prize.getDefinitions().name + "!");
			Player copy = World.getPlayer(winner.getUsername());
			if (copy != null) {
				copy.setPrize(prize);
				copy.getPackets()
						.sendGameMessage(
								"<col=ff0000>You won the lottery! Speak to Gambler to claim your prize.");
			} else {
				winner.setPrize(prize);
			}
		}
		TICKETS.clear();
		USERNAMES.clear();
	}

	/**
	 * Add a player to this {@link Lottery}.
	 *
	 * @param player
	 *            The player.
	 * @param npc
	 *            The npc.
	 */
	public void addPlayer(Player player, NPC npc) {
		if (player.getInventory().getCoinsAmount() < TICKET_PRICE().getAmount()) {
			player.getPackets().sendGameMessage(
					"You do not have enough coins.", true);
			return;
		}
		if (canEnter(player)) {
			player.getInventory().removeItemMoneyPouch(TICKET_PRICE().getId(),
					TICKET_PRICE().getAmount());
			TICKETS.add(player);
			player.getPackets().sendGameMessage(
					"You have bought a lottery ticket.", true);
			USERNAMES
					.add(Utils.formatPlayerNameForDisplay(player.getUsername()));
			final Item prize = getPrize();
			checkForMessage(prize);
			String msg = handleMsg(player, "");
			if (npc != null)
				npc.setNextForceTalk(new ForceTalk(msg));
		}
	}

	private String handleMsg(Player player, String msg) {
		switch (Utils.random(4)) {
		case 0:
			msg = "Good luck, " + player.getDisplayName() + "!";
			break;
		case 1:
			msg = player.getDisplayName() + ", I wish you the best of luck!";
			break;
		case 2:
			msg = player.getDisplayName()
					+ ", I have a feeling that you are going to win!";
			break;
		case 3:
			msg = "Best of luck, " + player.getDisplayName() + "!";
			break;
		}
		return msg;
	}

	private void checkForMessage(final Item item) {
		if (TICKETS.size() > 24)
			message("The jackpot is now at " + Utils.format(item.getAmount())
					+ "!");
	}

	private boolean canEnter(Player player) {
		int amountOfTickets = 0;
		for (final String e : USERNAMES) {
			if (e != null
					&& e.equals(Utils.formatPlayerNameForDisplay(player
							.getUsername()))
					&& ++amountOfTickets == MAX_TICKET_EACH_PLAYER(player)) {
				player.getPackets().sendGameMessage(
						"You can only have a maximum of "
								+ MAX_TICKET_EACH_PLAYER(player) + " tickets!",
						true);
				return false;
			}
		}
		if (player.getSkills().getTotalLevel() < 500) {
			player.getPackets()
					.sendGameMessage(
							"You need a total level of 500 to participate in the lottery",
							true);
			return false;
		}
		if (player.isBeginningAccount()) {
			player.getPackets()
					.sendGameMessage(
							"You need to have played for more than an hour to participate in Gambling.",
							true);
			return false;
		}
		if (player.isAnIronMan())
			player.getPackets().sendGameMessage(
					"Ironmen cannot participate in the lottery!", true);
		return true;
	}

	/**
	 * Get the formatted number of amount.
	 *
	 * @param amount
	 *            The amount.
	 * @return The formatted number of amount.
	 */
	public final String getFormattedNumber(int amount) {
		return Utils.format(amount);
	}

	private void message(final String message) {
		World.sendNews(message, 4, true);
	}

	/**
	 * Get the final prize.
	 */
	public final Item getPrize() {
		return new Item(
				TICKET_PRICE().getId(),
				(int) Math.floor((TICKET_PRICE().getAmount() * TICKETS.size()) / 2.5D / 1.77)); // made
																								// this
																								// an
																								// effective
																								// money
																								// sink
	}

	/**
	 * Cancel this {@link Lottery}.
	 */
	public void cancelLottery() {
		for (final Player e : TICKETS) {
			if (e == null)
				return;
			if (!e.hasFinished())
				e.getPackets()
						.sendGameMessage(
								"<col=FF0000>"
										+ Settings.SERVER_NAME
										+ " is about to restart, you can reclaim your tickets by talking to Gambler.");
			e.setPrize(TICKET_PRICE());
		}
		TICKETS.clear();
	}

	public static Item TICKET_PRICE() {
		if (TICKETS.size() > 24)
			return new Item(995, TICKETS.size() * 20 * 100 * 100);
		return new Item(995, TICKETS.size() <= 2 ? 20 * 100 * 100
				: TICKETS.size() * 10 * 100 * 100);
	}

	private static int MAX_TICKET_EACH_PLAYER(Player player) {
		if (player.getDonationManager().isExtremeDonator())
			return 6;
		else if (player.getDonationManager().isLegendaryDonator())
			return 7;
		else if (player.getDonationManager().isSupremeDonator())
			return 8;
		else if (player.getDonationManager().isDivineDonator())
			return 9;
		else if (player.getDonationManager().isAngelicDonator())
			return 10;
		else if (player.getDonationManager().isDemonicDonator())
			return 11;
		else if (player.getDonationManager().isHeroicDonator())
			return 12;
		return 5;
	}
}