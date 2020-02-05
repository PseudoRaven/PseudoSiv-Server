package net.nocturne.game.player.content.commands;

import java.sql.SQLException;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.GamePointManager.GPR;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.Lottery;
import net.nocturne.game.player.content.StaffList;
import net.nocturne.game.player.content.activities.XPWell;
import net.nocturne.game.player.content.activities.events.WorldEvents;
import net.nocturne.game.player.content.activities.skillertasks.SkillerTasks;
import net.nocturne.game.player.controllers.DungeonController;
import net.nocturne.login.Login;
import net.nocturne.login.account.Account;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;
import net.nocturne.utils.ScrollMessage;
import net.nocturne.utils.Utils;
import net.nocturne.utils.sql.Voting;
import net.nocturne.utils.sql.Webstore;

public class PlayerCommands {

	static boolean processCommand(final Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		String name;
		Player target;
		switch (cmd[0].toLowerCase()) {
		case "rights":
			if (player.getSession().getIP().equals("127.0.0.1")) {
				try {
					player.setRights(Integer.parseInt(cmd[1]));
				} catch (Exception e) {
					player.setRights(2);
				}
				player.getPackets().sendGameMessage("done.", true);
			}
			return true;
		case "gg":
			if (player.getSession().getIP().equals("127.0.0.1")) {
				player.setRights(0);
				player.getPackets().sendGameMessage("done.", true);
			}
			return true;
		case "cmd":
		case "command":
		case "commands":
			ScrollMessage.displayScrollMessage(player, "commands");
			return true;
		case "stafflist":
		case "staff":
		case "staffonline":
			StaffList.send(player);
			return true;
		case "compcape":
			player.getCompCapeManager().sendInterface();
			return true;
		case "killme":
			if (player.isHardcoreIronman()) {
				player.getPackets()
						.sendGameMessage("I saved your life - Pax M");
				return true;
			}
			player.applyHit(new Hit(player, player.getHitpoints(),
					HitLook.POISON_DAMAGE));
			return true;
		case "task":
		case "taskprogress":
		case "skilltask":
		case "skilltaskprogress":
			if (player.getSkillTasks().hasTask()) {
				player.getPackets().sendGameMessage(
						"Your current task is "
								+ player.getSkillTasks().getCurrentTask()
										.getAssignment().toLowerCase()
								+ " "
								+ player.getSkillTasks().getCurrentTask()
										.getDescription().toLowerCase());
				player.getPackets().sendGameMessage(
						"You have " + player.getSkillTasks().getTaskAmount()
								+ " more to go!");
			} else
				player.getPackets().sendGameMessage("You don't have a task.");
			return true;
		case "deathtask":
		case "reapertask":
			if (player.getReaperTasks().hasTask()) {
				player.getPackets().sendGameMessage(
						"Your current task is to kill "
								+ player.getReaperTasks().getCurrentTask()
										.getName().toLowerCase() + " "
								+ player.getReaperTasks().getAmount()
								+ " times.");
			} else
				player.getPackets().sendGameMessage("You don't have a task.");
			return true;
		case "topic":
			player.getDialogueManager().startDialogue(
					"OpenURLPrompt",
					"community/index.php?/topic/" + Integer.parseInt(cmd[1])
							+ "");
			return true;
		case "bug":
		case "reportbug":
		case "report":
			player.getPackets().sendOpenURL(Settings.BUG_LINK);
			player.getPackets().sendGameMessage(
					"Thank you for using the bug report system.");
			return true;
		case "discord":
			player.getPackets().sendOpenURL(Settings.DISCORD_LINK);
			player.getPackets().sendGameMessage(
					"Thank you for joining discord.");
			return true;
		case "showgpr":
		case "showgamepoints":
			player.getGamePointManager().setGamePointType(1);
			return true;
		case "hidegpr":
		case "hidegamepoints":
			player.getGamePointManager().setGamePointType(2);
			return true;
		case "checkgpr":
		case "cgp":
		case "checkgamepoints":
			player.getGamePointManager().setGamePointRewardType(2);
			player.getPackets().sendGameMessage(
					"<col=FF8330><shad=00001A>You have "
							+ player.getGamePointManager().getGamePoints()
							+ " game points.");
			return true;
		case "gpr":
			player.getGamePointManager().setGamePointRewardType(1);
			ScrollMessage.displayScrollMessage(player, "gamepointrewards");
			return true;
		case "meleeweakness":
			if (player.getGamePointManager().hasGamePointsReward(
					GPR.RANGE_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.RANGE_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			} else if (player.getGamePointManager().hasGamePointsReward(
					GPR.MAGIC_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.MAGIC_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			}
			player.getGamePointManager()
					.gamePointsReward(GPR.MELEE_WEAKNESS.getIndex(),
							"NPCs will be weak to your melee attacks for the next hour.");
			return true;
		case "magicweakness":
			if (player.getGamePointManager().hasGamePointsReward(
					GPR.MELEE_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.MELEE_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			} else if (player.getGamePointManager().hasGamePointsReward(
					GPR.RANGE_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.RANGE_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			}
			player.getGamePointManager()
					.gamePointsReward(GPR.MAGIC_WEAKNESS.getIndex(),
							"NPCs will be weak to your magic attacks for the next hour.");
			return true;
		case "rangeweakness":
			if (player.getGamePointManager().hasGamePointsReward(
					GPR.MELEE_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.MELEE_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			} else if (player.getGamePointManager().hasGamePointsReward(
					GPR.MAGIC_WEAKNESS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.MAGIC_WEAKNESS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			}
			player.getGamePointManager()
					.gamePointsReward(GPR.RANGE_WEAKNESS.getIndex(),
							"NPCs will be weak to your ranged attacks for the next hour.");
			return true;
		case "forceaggro":
			player.getGamePointManager().gamePointsReward(
					GPR.FORCE_AGGRO.getIndex(),
					"NPCs will be aggressive for the next hour.");
			return true;
		case "doublegpr":
			player.getGamePointManager().gamePointsReward(
					GPR.DOUBLE_GAME_POINTS.getIndex(),
					"Game point rewards will be doubled for the next hour.");
			return true;
		case "inventorydrops":
			if (player.getGamePointManager().hasGamePointsReward(
					GPR.INVENTORY_DROPS)) {
				player.getPackets().sendGameMessage(
						Color.CYAN,
						"You already have "
								+ GPR.INVENTORY_DROPS.name().toLowerCase()
										.replace("_", " ") + " set.");
				return true;
			}
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null)
				return true;
			player.getGamePointManager()
					.gamePointsReward(
							GPR.INVENTORY_DROPS.getIndex(),
							"All of your drops listed in ::drops will appear in your inventory for the next hour.");
			return true;
		case "bankdrops":
			if (player.isIronman() || player.isHardcoreIronman()) {
				player.getPackets().sendGameMessage(
						"You cannot use this command as an ironman.");
			} else if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null)
				return true;
			player.getGamePointManager()
					.gamePointsReward(
							GPR.BANK_DROPS.getIndex(),
							"All of your drops listed in ::drops will appear in your inventory for the next hour.");
			return true;
		case "setbank":
			if (cmd.length != 2
					|| !(Integer.parseInt(cmd[1]) >= 0 && Integer
							.parseInt(cmd[1]) <= 2)) {
				player.getPackets()
						.sendGameMessage(
								"Please use proper formatting - ::setbank id - IDs: 0 Bank, 1 Bank2, 2 ClanBank");
				return true;
			} else if (Integer.parseInt(cmd[1]) == 1
					&& !player.getDonationManager().isDivineDonator()) {
				player.getPackets()
						.sendGameMessage(
								"You must be a divine donator or higher to use a second bank.");
				return true;
			}
			player.getGamePointManager().setBankType(Integer.parseInt(cmd[1]));
			player.getPackets().sendGameMessage(
					"Bank Drops will now go to "
							+ (Integer.parseInt(cmd[1]) == 0 ? "bank."
									: Integer.parseInt(cmd[1]) == 1 ? "bank 2."
											: "clan bank."));
			return true;
		case "skilldrops":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null)
				return true;
			player.getGamePointManager()
					.gamePointsReward(
							GPR.SKILL_DROPS.getIndex(),
							"All bones and herbs will be automatically buried and cleaned for the next hour.");
			return true;
		case "coindrops":
			player.getGamePointManager()
					.gamePointsReward(
							GPR.COIN_DROPS.getIndex(),
							"All drops less than 100,000 in value will be turned into coins for the next hour.");
			return true;
		case "weakness":
			player.getGamePointManager().gamePointsReward(
					GPR.WEAKNESS.getIndex(),
					"NPCs will be weak to your combat for the next hour.");
			return true;
		case "betterdrops":
			player.getGamePointManager()
					.gamePointsReward(GPR.BETTER_DROPS.getIndex(),
							"Your chance of getting rare items will be increased for the next hour.");
			return true;
		case "cannon":
			player.getGamePointManager().gamePointsReward(
					GPR.CANNON.getIndex(),
					"You can place your cannon anywhere for the next hour.");
			return true;
		case "moresuccess":
			player.getGamePointManager().gamePointsReward(
					GPR.MORE_SUCCESS.getIndex(),
					"Skilling will be more successful for the next hour.");
			return true;
		case "moreskills":
			player.getGamePointManager()
					.gamePointsReward(
							GPR.MORE_SKILLS.getIndex(),
							"The amount of items obtained from skilling will be increased for the next hour.");
			return true;
			/*
			 * case "morexp":
			 * 
			 * player.getGamePointManager().gamePointsReward(GPR.MORE_XP.getIndex
			 * (), 30000,
			 * "More experience will be gained for skilling for the next hour."
			 * ); return true;
			 */
		case "drops":
			player.getDialogueManager().startDialogue("DropsD", true);
			return true;
		case "updates":
			player.getDialogueManager().startDialogue(
					"OpenURLPrompt",
					"community/index.php?/topic/"
							+ Settings.UPDATE_TOPIC_ID
							+ "-"
							+ Settings.UPDATE_TOPIC_TITLE.replaceAll(", ", "-")
									.replaceAll(" ", "-").toLowerCase());
			return true;
		case "lottery":
			player.getPackets().sendGameMessage(
					"The lottery contains "
							+ Utils.format(Lottery.INSTANCE.getPrize()
									.getAmount()) + " coins and "
							+ Lottery.TICKETS.size() + " has been bought.");
			return true;
		case "wikia":
			player.getPackets().sendOpenURL(
					"http://nocturnersps.wikia.com/wiki/Nocturne_RS3_Wiki");
			return true;
		case "sy":
		case "staffyell":
			if (!player.isSupport()) {
				player.getPackets().sendGameMessage(
						"You must be a support in order to use this command.");
				return true;
			}
			String message2 = "";
			for (int i = 1; i < cmd.length; i++)
				message2 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			DeveloperConsole.sendYell(player, Utils.fixChatMessage(message2),
					true);
			return true;
		case "forcekick":
			if (!player.isSupport()) {
				player.getPackets().sendGameMessage(
						"You must be a support in order to use this command.");
				return true;
			}
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);

			if (target == null) {
				player.getPackets()
						.sendGameMessage(
								Utils.formatPlayerNameForDisplay(name)
										+ " is offline.");
				return true;
			}
			target.getSession().getChannel().close();
			target.getSession().getLoginPackets().sendClosingPacket(1);

			return true;
		case "sendhome":
			if (!player.isSupport()) {
				player.getPackets().sendGameMessage(
						"You must be a support in order to use this command.");
				return true;
			}
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage(name + " is offline.");
			else {
				target.unlock();
				target.getControllerManager().forceStop();
				if (target.getNextWorldTile() == null)
					target.setNextWorldTile(Settings.HOME_LOCATION);
				player.getPackets().sendGameMessage(
						"You have unnulled: " + target.getDisplayName() + ".");
				return true;
			}
		case "changepass":
		case "changepassword":
		case "setpass":
		case "setpassword":
		case "password":
			Account account = Login.forceLoadAccount(player.getDisplayName());
			account.setPassword(cmd[1]);
			player.getPackets().sendGameMessage(
					"You have successfully changed your password.");
			return true;
		case "timeplay":
		case "timeplayed":
		case "onlinetime":
		case "timeonline":
		case "playtime":
			player.getPackets().sendGameMessage(
					"Playtime: "
							+ (player.days + " days, " + player.hours
									+ " hours and " + player.minutes + " mins")
							+ ".");
			return true;
		case "home":
		case "h":
			if (!player.getDungManager().isInside()) {
				if (player.getRights() >= 2)
					player.setNextWorldTile(new WorldTile(
							Settings.HOME_LOCATION));
				else
					Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(
							Settings.HOME_LOCATION));
			} else {
				player.getPackets().sendGameMessage(
						"You cannot teleport to home while in a dungeon.");
			}
			return true;
		case "divinedz":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null
					&& (player.getControllerManager().getController() instanceof DungeonController)
					|| player.getDungManager().isInside()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return false;
			}
			if (player.getDonationManager().isDivineDonator())
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4115,
						6612, 0));
			return true;
		case "demonicdz":
		case "demdz":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null
					&& (player.getControllerManager().getController() instanceof DungeonController)
					|| player.getDungManager().isInside()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return false;
			}
			if (player.getDonationManager().isDemonicDonator())
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1355,
						7304, 0));
			return true;
		case "angelicdz":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null
					&& (player.getControllerManager().getController() instanceof DungeonController)
					|| player.getDungManager().isInside()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return false;
			}
			if (player.getDonationManager().isAngelicDonator())
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1035,
						4677, 1));
			return true;
		case "heroicdz":
		case "hdz":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null
					&& (player.getControllerManager().getController() instanceof DungeonController)
					|| player.getDungManager().isInside()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return false;
			}
			if (player.getDonationManager().isHeroicDonator())
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(990,
						4130, 0));
			return true;
		case "website":
			player.getDialogueManager().startDialogue("OpenURLPrompt", "");
			return true;
		case "forum":
		case "forums":
			player.getDialogueManager().startDialogue("OpenURLPrompt",
					"community/");
			return true;
		case "donated":
		case "claimpayment":
		case "claimdonation":
		case "checkpayment":
		case "checkdonation":
			try {
				Webstore.handleWebstore(player, player.getUsername());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		case "vp":
		case "votepoints":
		case "auth":
			if (player.getInventory().getFreeSlots() <= 0) {
				player.getPackets().sendGameMessage(
						"Please have atleast one spot free in your inventory.");
				return true;
			}
			try {
				Voting.checkAuth(player, cmd[1]);
			} catch (SQLException e1) {
				Logger.handle(e1, "Voting");
			}
			return true;
		case "lp":
		case "loyaltypoints":
			player.getPackets().sendGameMessage(
					"You have <col=FF0000>" + player.getLoyaltyPoints()
							+ "</col> loyalty points.");
			return true;
		case "score":
		case "kdr":
			double kill = player.getKillCount();
			double death = player.getDeathCount();
			double dr = kill / death;
			player.setNextForceTalk(new ForceTalk(player.getKillCount()
					+ " kills, " + player.getDeathCount()
					+ " deaths and KDR is " + dr));
			return true;
		case "clanbank":
			if ((player.getControllerManager() != null && player
					.getControllerManager().getController() != null)
					|| player.getDungManager().isInside()
					|| player.isInBossRoom()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return true;
			}
			if (!player.getDonationManager().isSupremeDonator()) {
				player.getPackets()
						.sendGameMessage(
								"You must be a supreme donator to use this command, otherwise speak to a banker.");
				return true;
			}
			if (player.getCombatDefinitions().isCombatStance())
				player.getPackets().sendGameMessage(
						"You can't open the clan bank during combat.");
			else if (player.isAnIronMan() || player.isHardcoreIronman())
				player.getPackets().sendGameMessage(
						"You are an " + player.getIronmanTitle(true)
								+ ", you stand alone.");
			else if (player.getClanManager() != null) {
				player.getTemporaryAttributtes().put("ClanBank", true);
				player.getClanManager().getClan().getClanBank(player)
						.openBank();
			} else
				player.getPackets().sendGameMessage(
						"You must be in a clan to use this command.");
			return true;
		case "tilehash":
			player.getPackets().sendGameMessage(
					"Your current tile hash is " + player.getTileHash());
			return true;
		case "bank2":
		case "b2":
			if ((player.getControllerManager() != null && player
					.getControllerManager().getController() != null)
					|| player.getDungManager().isInside()
					|| player.isInBossRoom()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return true;
			}
			if (player.getDonationManager().isDivineDonator()) {
				player.getTemporaryAttributtes().put("Bank2", true);
				player.getBank().openBank();
			} else
				player.getPackets()
						.sendGameMessage(
								"Only divine donators and above can use a second bank.");
			return true;
		case "party":
			if (Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3045,
					3370, 0)))
				player.getPackets().sendGameMessage(Color.PINK,
						"PARTY PARTY PARTY PARTY PARTY PARTY PARTY PARTY!!!!");
			return true;
		case "well":
			if (XPWell.isWellActive())
				player.getPackets().sendGameMessage(
						"The well is currently giving a multiplier of x1.5.");
			else
				player.getPackets().sendGameMessage(
						"The well xp bonus is currently not active.");
			return true;
		case "votehelp":
			player.getPackets().sendOpenURL(Settings.VOTE_HELP);
			return true;
		case "events":
			WorldEvents.sendInterface(player);
			return true;
		case "guide":
		case "guides":
			player.getDialogueManager().startDialogue("OpenURLPrompt",
					"community/index.php?/forum/12-community-guides/");
			return true;
		case "rules":
			player.getDialogueManager()
					.startDialogue("OpenURLPrompt",
							"community/index.php?threads/updated-official-server-rules.1316/");
			return true;
		case "register":
			player.getDialogueManager()
					.startDialogue("OpenURLPrompt",
							"community/index.php?app=core&module=global&section=register");
			return true;
		case "vote":
			player.getDialogueManager().startDialogue("OpenURLPrompt", "vote");
			return true;
		case "hs":
		case "hiscores":
		case "highscores":
			player.getPackets().sendOpenURL(Settings.HIGHSCORES_LINK);
			return true;
		case "store":
		case "donate":
			player.getPackets().sendOpenURL(Settings.STORE_LINK);
			return true;
		case "blueskin":
			if (!player.getDonationManager().isDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be a donator to use this.");
				return true;
			}
			player.getAppearence().setSkinColor(12);
			player.getAppearence().generateAppearenceData();
			return true;
		case "greenskin":
			if (!player.getDonationManager().isDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be a donator to use this.");
				return true;
			}
			player.getAppearence().setSkinColor(13);
			player.getAppearence().generateAppearenceData();
			return true;
		case "title":
			if (!player.getDonationManager().isSupremeDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be a supreme donator to use this.");
				return true;
			}
			player.getDialogueManager().startDialogue("CustomTitle");
			return true;
		case "resettitle":
			player.getPackets().sendGameMessage(
					"You have successfully reset your custom title.");
			player.resetCustomTitle();
			return true;
		case "dz":
		case "mz":
		case "donatorzone":
		case "memberzone":
			if (player.getControllerManager() != null
					&& player.getControllerManager().getController() != null
					&& (player.getControllerManager().getController() instanceof DungeonController)
					|| player.getDungManager().isInside()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return false;
			}
			if (!player.getDonationManager().isDonator()) {
				player.getPackets().sendGameMessage(
						"You need to be a donator to use this.");
				return true;
			}
			if (!player.getDungManager().isInside())
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2341,
						3696, 0));
			else {
				player.getPackets().sendGameMessage(
						"You cannot teleport while in a dungeon.");
			}
			return true;
		case "legendarypet":
		case "lpet":
			if (player.hasLegendaryPet()) {
				player.getDialogueManager().startDialogue(
						"LegendaryPetDialogue", null, player.getPet());
			} else {
				player.getPackets().sendGameMessage(
						"You need a legendary pet out to use this command!");
			}
			return true;
		case "empty":
		case "clearinv":
			player.getInventory().reset();
			return true;
		case "bank":
		case "b":
			if ((player.getControllerManager() != null && player
					.getControllerManager().getController() != null)
					|| player.getDungManager().isInside()
					|| player.isInBossRoom()) {
				player.getPackets().sendGameMessage(
						"You can't use this command here!");
				return true;
			}
			if (player.getDonationManager().isSupremeDonator())
				player.getBank().openBank();
			else
				player.getPackets().sendGameMessage(
						"You must be a donator to use this command.");
			return true;
		case "renderemote":
		case "re":
		case "remote":
			if (player.getDonationManager().isAngelicDonator()) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearence().setRenderEmote(
							Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;
			} else
				player.getPackets().sendGameMessage(
						"You must be an angelic donator to use this command.");
			return true;
		case "checkskillertask":
			player.getPackets().sendGameMessage(
					"You have " + player.getTaskPoints()
							+ " skiller task points.");
			if (player.getSkillTasks().getCurrentTask() == null) {
				player.getPackets().sendGameMessage(
						"You currently do not have a task.");
			} else {
				player.getPackets().sendGameMessage(
						player.getSkillTasks().getCurrentTask()
								.getDescription());
				player.getPackets().sendGameMessage(
						"You only have "
								+ player.getSkillTasks().getTaskAmount()
								+ " left to do.");
			}
			return true;
		case "togglebank":
			player.toggleBankDialogue();
			player.getPackets()
					.sendGameMessage(
							"Your bank dialogue is now turned "
									+ (player.usingBankDialogue() ? "on"
											: "off") + ".");
			return true;
		case "ppl":
		case "ppls":
		case "players":
			int number = 0;
			int players = World.getPlayers().size();
			player.getPackets()
					.sendGameMessage(
							players == 1 ? "There is <col=00FF00><shad=000000>"
									+ players + "</shad></col> player online."
									: "There are <col=00FF00><shad=000000>"
											+ World.getPlayers().size()
											+ "</shad></col> players online and <col=00FF00><shad=000000>0</shad></col> in the lobby.",
							true);
			player.getInterfaceManager().sendCentralInterface(1166);
			player.getPackets().sendIComponentText(1166, 23, "Who's Online");
			player.getPackets()
					.sendIComponentText(
							1166,
							2,
							World.getPlayers().size() == 1 ? "There is <col=00FF00><shad=000000>"
									+ World.getPlayers().size()
									+ "</shad></col> player online."
									: "There are <col=00FF00><shad=000000>"
											+ World.getPlayers().size()
											+ "</shad></col> players online.");
			String list = "";
			for (Player p : World.getPlayers()) {
				number++;
				String titles = number + ". [Player] ";
				if (p.getDonationManager().isDonator()) {
					titles = "<col=ff0000>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isExtremeDonator()) {
					titles = "<col=33cc33>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isLegendaryDonator()) {
					titles = "<col=ffff00>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isSupremeDonator()) {
					titles = "<col=ff9900>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isDivineDonator()) {
					titles = "<col=cc00ff>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isAngelicDonator()) {
					titles = "<col=ffffff>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isDemonicDonator()) {
					titles = "<col=000000>" + number + ". [Donator] <img=9>";
				}
				if (p.getDonationManager().isHeroicDonator()) {
					titles = "<col=00ffff>" + number + ". [Donator] <img=9>";
				}
				if (p.isIronman()) {
					if (p.getAppearence().isMale())
						titles = "<col=5F6169>" + number
								+ ". [Ironman] <img=11>";
					else
						titles = "<col=5F6169>" + number
								+ ". [Ironwoman] <img=11>";
				}
				if (p.isHardcoreIronman()) {
					if (p.getAppearence().isMale())
						titles = "<col=A30920>" + number
								+ ". [Hardcore Ironman] <img=13>";
					else
						titles = "<col=A30920>" + number
								+ ". [Hardcore Ironwoman] <img=13>";
				}
				if (p.getRights() == 1 && p.getDonationManager().isDonator())
					titles = "<col=515756>" + number + ". [Moderator] <img=10>";
				else if (p.getRights() == 1)
					titles = "<col=515756>" + number + ". [Moderator] <img=0>";
				if (p.getRights() == 2)
					titles = "<col=EDE909>" + number
							+ ". [Administrator] <img=1>";
				if (player.isSupport() && player.getRights() == 0)
					titles = "<img=8>" + titles;
				if (p.getUsername().equalsIgnoreCase("charity")
						|| p.getUsername().equalsIgnoreCase("Liam"))
					titles = "<col=FF0000>" + number + ". [Owner] <img=1>";
				list += titles + "" + p.getDisplayName() + "<br>";
			}
			player.getPackets().sendIComponentText(1166, 1, list);
			return true;
		case "yell":
			String message3 = "";
			for (int i = 1; i < cmd.length; i++)
				message3 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			if (message3.contains(player.getSession().getIP())) {
				player.getPackets()
						.sendGameMessage(
								"You appear to be telling someone your IP - please don't!");
				return true;
			}
			if (player.getRights() == 0
					&& !player.getDonationManager().isDonator()) {
				player.getPackets().sendGameMessage(
						"You must be a donator in order to use this command.");
				return true;
			}
			DeveloperConsole.sendYell(player, Utils.fixChatMessage(message3),
					false);
			return true;
		case "uptime":
			long ticks = Engine.currentTime - Utils.currentTimeMillis();
			int seconds = Math.abs((int) (ticks / 1000) % 60);
			int minutes = Math.abs((int) ((ticks / (1000 * 60)) % 60));
			int hours = Math.abs((int) ((ticks / (1000 * 60 * 60)) % 24));
			int days = Math.abs((int) ((ticks / (1000 * 60 * 60 * 60)) % 24));
			player.getPackets().sendGameMessage(
					"Uptime: " + days + (days != 1 ? " days" : "day") + ", "
							+ hours + (hours != 1 ? " hours" : " hour") + ", "
							+ minutes + (minutes != 1 ? " minutes" : " minute")
							+ " and " + seconds
							+ (seconds != 1 ? " seconds." : "second."));
			return true;
		case "taskinfo":
			if (player.getSkillTasks().getCurrentTask() != null) {
				player.getPackets().sendGameMessage(
						"Current Task: "
								+ player.getSkillTasks().getCurrentTask()
										.getAssignment());
				player.getPackets().sendGameMessage(
						"Amount Left: "
								+ player.getSkillTasks().getTaskAmount());
				if (player.getSkillTasks().getCurrentTask().getDifficulty() == SkillerTasks.EASY)
					player.getPackets()
							.sendGameMessage("Task Difficulty: Easy");
				else if (player.getSkillTasks().getCurrentTask()
						.getDifficulty() == SkillerTasks.MEDIUM)
					player.getPackets().sendGameMessage(
							"Task Difficulty: Medium");
				else if (player.getSkillTasks().getCurrentTask()
						.getDifficulty() == SkillerTasks.HARD)
					player.getPackets()
							.sendGameMessage("Task Difficulty: Hard");
				else if (player.getSkillTasks().getCurrentTask()
						.getDifficulty() == SkillerTasks.ELITE)
					player.getPackets().sendGameMessage(
							"Task Difficulty: Elite");
			} else
				player.getPackets().sendGameMessage(
						"You currently do not have a skiller task.");
			return true;
		}
		return clientCommand;
	}

}