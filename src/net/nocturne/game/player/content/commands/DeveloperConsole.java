package net.nocturne.game.player.content.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationController;
import net.nocturne.utils.Censor;

public final class DeveloperConsole {

	public static boolean processCommand(Player player, String command,
			boolean console, boolean clientCommand) {
		if (command.length() == 0 || player.isLobby())
			return false;
		String[] cmd = command.split(" ");
		if (cmd.length == 0)
			return false;
		archiveLogs(player, cmd);
		if (AdministratorCommands.processCommand(player, cmd, console,
				clientCommand))
			return true;
		else if (ModeratorCommands.processCommand(player, cmd, console,
				clientCommand))
			return true;
		else if (PlayerCommands.processCommand(player, cmd, console,
				clientCommand))
			return true;
		return false;

	}

	static void sendYell(Player player, String message, boolean staffYell) {
		message = Censor.getFilteredMessage(message);
		if (player.isMuted()) {
			player.getPackets().sendGameMessage(
					"You have been temporarily muted due to breaking a rule.");
			player.getPackets().sendGameMessage(
					"This mute will remain for a further "
							+ player.getMutedFor() + " hours.");
			player.getPackets().sendGameMessage(
					"To prevent further mutes please read the rules.");
			return;
		}
		if (player.getCustomTitle() != null) {
			int icon = player.getRights() > 0 ? player.getRights() - 1 : player
					.getDonationManager().isDonator() ? 9 : -1;
			World.sendYellMessage(
					player,
					"["
							+ player.getFullTitle(true, true)
							+ "] "
							+ (player.getRights() >= 2 ? "<col=f46f27>"
									: player.getRights() == 1 ? "<col=f46f27>"
											: "") + " <img=" + icon + "> "
							+ player.getDisplayName() + ": " + message
							+ "</col>");
			return;
		}
		if (staffYell) {
			switch (player.getRights()) {
			case 1:
				if (player.getDonationManager().isDonator()) {
					World.sendIgnoreableWorldMessage(player,
							"[<col=f46f27>Moderator</col>] <col=f46f27><img=10>"
									+ player.getDisplayName() + ": " + message,
							true);
				} else {
					World.sendIgnoreableWorldMessage(player,
							"[<col=f46f27>Moderator</col>] <col=f46f27><img=0>"
									+ player.getDisplayName() + ": " + message,
							true);
				}
				break;
			case 2:
				if (player.getUsername().equals("danny")) {
					World.sendIgnoreableWorldMessage(player,
							"[<col=f46f27>Owner</col>] <col=f46f27><img=1>"
									+ player.getDisplayName() + ": " + message,
							true);
				} else if (player.getUsername().equals("charity")) {
					World.sendIgnoreableWorldMessage(player,
							"[<col=f46f27>Co Owner</col>] <col=f46f27><img=1>"
									+ player.getDisplayName() + ": " + message,
							true);
				} else if (player.getUsername().equals("Nath")
						|| player.getUsername().equals("Tom")
						|| player.getUsername().equals("Pax")) {
					World.sendIgnoreableWorldMessage(player,
							"[<col=660C89>Developer</col>] <col=660C89><img=1>"
									+ player.getDisplayName() + ": " + message,
							true);
				} else {
					World.sendIgnoreableWorldMessage(player,
							"[<col=f46f27>Administrator</col>] <col=f46f27><img=1>"
									+ player.getDisplayName() + ": " + message,
							true);
				}
				break;
			}
			return;
		}
		if (player.isSupport() && player.getRights() == 0) {
			World.sendIgnoreableWorldMessage(
					player,
					"[<col=e352ff>Support</col>] <col=71a4f7><img=8>"
							+ player.getDisplayName() + ": " + message, false);
			return;
		}
		if (message.length() > 100)
			message = message.substring(0, 100);
		String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=",
				"<shad", "<shad=", "<str>", "<u>" };
		for (String s : invalid)
			if (message.contains(s)) {
				player.getPackets().sendGameMessage(
						"You cannot use the tag '" + Arrays.toString(invalid)
								+ "'.");
				return;
			}
		switch (player.getRights()) {
		case 0:
			if (player.isIronman() || player.isHardcoreIronman()) {
				World.sendYellMessage(player,
						"<col=2792f4>[" + player.getIronmanTitle(true)
								+ "] <img=" + player.getIronmanBadge() + ">"
								+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isDonator()) {
				World.sendYellMessage(player, "<col=ff0000>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isExtremeDonator()) {
				World.sendYellMessage(player, "<col=00ff00>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isLegendaryDonator()) {
				World.sendYellMessage(player, "<col=0000ff>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isSupremeDonator()) {
				World.sendYellMessage(player, "<col=ff9900>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isDivineDonator()) {
				World.sendYellMessage(player, "<col=cc00ff>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isAngelicDonator()) {
				World.sendYellMessage(player, "<col=ffffff>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isDemonicDonator()) {
				World.sendYellMessage(player, "<col=000000>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			} else if (player.getDonationManager().isHeroicDonator()) {
				World.sendYellMessage(player, "<col=00ffff>[Donator] <img=9>"
						+ player.getDisplayName() + ": " + message);
			}
			break;
		case 1:
			if (player.getDonationManager().isDonator()) {
				World.sendYellMessage(
						player,
						"<col=2792f4>[Moderator] <img=10>"
								+ player.getDisplayName() + ": " + message);
			} else {
				World.sendYellMessage(player, "<col=2792f4>[Moderator] <img=0>"
						+ player.getDisplayName() + ": " + message);
			}
			break;
		case 2:
			if (player.getUsername().equalsIgnoreCase("Liam"))
				World.sendYellMessage(player,
						"<col=ffffff><shad=000000>[Owner]<img=10><col=000000>"
								+ player.getDisplayName()
								+ ": </col><col=ff0000><shad=000000>" + message
								+ "</col>");
			else if (player.getRights() == 2)
				World.sendYellMessage(
						player,
						"<col=2792f4>[Staff Member] <img=1>"
								+ player.getDisplayName() + ": " + message);
			else
				World.sendYellMessage(
						player,
						"<col=2792f4>[Staff Member] <img=1>"
								+ player.getDisplayName() + ": " + message);
			break;
		}
		final String FILE_PATH = Settings.getDropboxLocation()
				+ "logs/chat/yell/";
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH
					+ player.getUsername() + ".txt", true));
			writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: "
					+ player.getSession().getIP() + "] : " + message);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean canWearItem(Player player, int itemId) {
		ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
		if (defs.isStealingCreationItem()) {
			if (player.getControllerManager().getController() instanceof StealingCreationController)
				return true;
			player.getPackets().sendGameMessage(
					"How did you get this item? Send a bug report to admin@"
							+ Settings.WEBSITE_LINK.replaceAll("https://", "")
							+ ".");
			return false;
		}

		if (!player.getDungManager().isInside() && defs.isDungeoneeringItem()) {
			player.getPackets().sendGameMessage(
					"How did you get this item? Send a bug report to admin@"
							+ Settings.WEBSITE_LINK.replaceAll("https://", "")
							+ ".");
			player.getInventory().deleteItem(itemId,
					player.getInventory().getAmountOf(itemId));
			return false;
		}

		return true;
	}

	private static void archiveLogs(Player player, String[] cmd) {
		try {
			String location = "";
			if (player.getRights() == 2) {
				location = Settings.getDropboxLocation()
						+ "logs/commands/admin/" + player.getUsername()
						+ ".txt";
			} else if (player.getRights() == 1) {
				location = Settings.getDropboxLocation() + "logs/commands/mod/"
						+ player.getUsername() + ".txt";
			} else if (player.getRights() == 0) {
				location = Settings.getDropboxLocation()
						+ "logs/commands/player/" + player.getUsername()
						+ ".txt";
			}
			String afterCMD = "";
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			for (int i = 1; i < cmd.length; i++) {
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(location,
					true));
			writer.write("[" + dateFormat.format(cal.getTime()) + "], IP: "
					+ player.getSession().getIP() + " - ::" + cmd[0] + " "
					+ afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public static void performPointEmote(Player target) {
		target.setNextAnimation(new Animation(17540));
		target.setNextGraphics(new Graphics(3401));
	}

	static void performTeleEmote(Player target) {
		target.setNextAnimation(new Animation(17544));
		target.setNextGraphics(new Graphics(3403));
	}

	public static void performKickBanEmote(Player target) {
		target.setNextAnimation(new Animation(17542));
		target.setNextGraphics(new Graphics(3402));
	}

	public static void max(Player player, double xp) {
		if (player != null) {
			for (int i = 0; i <= 26; i++) {
				if (i == 24 || i == 26)
					player.getSkills().set(i, 120);
				else
					player.getSkills().set(i, 99);
				player.getSkills().setXp(i, xp);
				player.getAppearence().generateAppearenceData();
			}
		}
	}

	public static void reset(Player player) {
		if (player != null) {
			for (int i = 0; i < 26; i++) {
				if (i == 24)
					player.getSkills().set(i, 1);
				else
					player.getSkills().set(i, 1);
				player.getSkills().setXp(i, 0);
				player.getAppearence().generateAppearenceData();
			}
		}
	}

	private DeveloperConsole() {

	}
}