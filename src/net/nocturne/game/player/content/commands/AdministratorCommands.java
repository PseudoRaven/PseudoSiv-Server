package net.nocturne.game.player.content.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.cache.loaders.AnimationDefinitions;
import net.nocturne.cache.loaders.IComponentDefinitions;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Region;
import net.nocturne.game.SecondaryBar;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.araxxi.Araxxi;
import net.nocturne.game.npc.combat.impl.NexCombat;
import net.nocturne.game.npc.randomEvent.CombatEventNPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.SlayerManager;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonConstants;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonPartyManager;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonRewardShop;
import net.nocturne.game.player.actions.skills.dungeoneering.Room;
import net.nocturne.game.player.actions.skills.slayer.Slayer.SlayerMaster;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.player.content.LoyaltyProgram;
import net.nocturne.game.player.content.PlayerLook;
import net.nocturne.game.player.content.SkillCapeCustomizer;
import net.nocturne.game.player.content.StaffList;
import net.nocturne.game.player.content.activities.dailychallenges.DailyTasksInterface;
import net.nocturne.game.player.content.activities.distractions.PenguinHS;
import net.nocturne.game.player.content.activities.minigames.FightPits;
import net.nocturne.game.player.content.activities.minigames.clanwars.ClanWars;
import net.nocturne.game.player.content.activities.minigames.clanwars.WallHandler;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.GameArea;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.Helper;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationManager;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationShop;
import net.nocturne.game.player.content.activities.minigames.warbands.Warbands;
import net.nocturne.game.player.content.activities.minigames.warbands.Warbands.WarbandEvent;
import net.nocturne.game.player.content.activities.surpriseevents.ArenaFactory;
import net.nocturne.game.player.content.activities.surpriseevents.EventArena;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.Kalaboss;
import net.nocturne.game.player.cutscenes.DZGuideScene;
import net.nocturne.game.player.cutscenes.NexCutScene;
import net.nocturne.game.player.dialogues.impl.JModTable;
import net.nocturne.game.route.Flags;
import net.nocturne.game.route.RouteFinder;
import net.nocturne.game.route.WalkRouteFinder;
import net.nocturne.game.route.strategy.FixedTileStrategy;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.login.Login;
import net.nocturne.login.account.Account;
import net.nocturne.network.LoginClientChannelManager;
import net.nocturne.network.LoginProtocol;
import net.nocturne.network.encoders.LoginChannelsPacketEncoder;
import net.nocturne.utils.CacheSearch;
import net.nocturne.utils.Color;
import net.nocturne.utils.LoginFilesManager;
import net.nocturne.utils.ShopsHandler;
import net.nocturne.utils.Utils;
import net.nocturne.utils.sql.Highscores;
import net.nocturne.utils.sql.HighscoresManager;

class AdministratorCommands {

	private static WorldObject object;

	static boolean processCommand(final Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (player.getRights() < 2)
			return false;
		String name = "";
		Player target;
		switch (cmd[0].toLowerCase()) {
		case "getcontroller":
			player.getPackets().sendGameMessage(
					"Current controller: "
							+ player.getControllerManager().getController()
									.toString());
			return true;
		case "startpenguin":
			PenguinHS.startEvent();
			return true;
		case "testhighscores":
			new Highscores(player).submit();
			HighscoresManager.process();
			return true;
		case "icompdef":
			IComponentDefinitions.getInterface(Integer.parseInt(cmd[1]));
			return true;
		case "skillpop":
			player.getSkills().refresh(3);
			player.getSkills().sendLevelUpInterface(3);
			return true;
		case "fairyring":
			player.getFairyRings().openInterface();
			IComponentDefinitions.getInterface(734);
			return true;
		case "orbofoculus":
			IComponentDefinitions.getInterface(933);
			return true;
		case "actionbar":
			IComponentDefinitions.getInterface(970);
			return true;
		case "playerlook":
			PlayerLook.openCharacterCustomizing(player, true);
			return true;
		case "unlocksettings":
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(Integer
							.valueOf(cmd[1])); i++) {
				player.getPackets().sendIComponentSettings(
						Integer.valueOf(cmd[1]), i, 0, 300,
						Integer.valueOf(cmd[2]));
			}
			return true;
		case "faminter":
			player.getInterfaceManager().setWindowInterface(
					Integer.parseInt(cmd[1]), 662);
			player.getPackets()
					.sendGameMessage("" + Integer.getInteger(cmd[1]));
			return true;
		case "testinter1":
			player.getInterfaceManager().setMenuInterface(0, 320);
			player.getInterfaceManager().setMenuInterface(1, 1446);
			player.getPackets().sendIComponentText(1446, 94,
					player.getDisplayName());
			player.getSkills().unlockSkills(true);
			return true;
		case "completecape":
			player.getCompCapeManager().sendComplete();
			return true;
		case "tele":
			try {
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int xx = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int yy = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(xx, yy, plane));
				player.getPackets().sendGameMessage(
						"tele: " + xx + ", " + yy + ", " + plane + ".", true);
			} catch (Exception e) {
				player.getPackets().sendGameMessage(e + ".");
				player.getPackets().sendGameMessage("Use: ::teleport x y z");
			}
			return true;
		case "teleport":
			try {
				int xxx = Integer.valueOf(cmd[1]);
				int yyy = Integer.valueOf(cmd[2]);
				int zzz = Integer.valueOf(cmd[3]);
				player.resetWalkSteps();
				player.setNextWorldTile(new WorldTile(xxx, yyy,
						cmd.length >= 4 ? zzz : player.getPlane()));
			} catch (Exception e) {
				player.getPackets().sendGameMessage(e + ".");
			}
			return true;
		case "setdisplay":
		case "setdisplayname":
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage(name + " is offline.");
			else {
				target.getPackets().sendInputLongTextScript(
						"Please enter your desired display name");
				target.getTemporaryAttributtes()
						.put("setdisplay", Boolean.TRUE);
			}
			return true;
		case "teletome":
			if (player.getRights() < 2) {
				return true;
			}
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null)
				player.getPackets().sendGameMessage(name + " is offline.");
			else {
				target.setNextWorldTile(player);
			}
			return true;
		case "item":
			if (cmd.length < 2) {
				player.getPackets().sendGameMessage(
						"Use: ::item id (optional:amount)");
				return true;
			}
			try {
				int itemId = Integer.valueOf(cmd[1]);
				if (itemId == 995) {
					player.getMoneyPouch().sendDynamicInteraction(
							cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1,
							false);
					player.getPackets().sendPanelBoxMessage(
							"Put id: 995 - amount: "
									+ (cmd.length >= 3 ? Integer
											.valueOf(cmd[2]) : 1)
									+ " in pouch.");
					return true;
				}
				player.getInventory().addItem(itemId,
						cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				player.getPackets().sendPanelBoxMessage(
						"Put "
								+ itemId
								+ " - amount: "
								+ (cmd.length >= 3 ? Integer.valueOf(cmd[2])
										: 1) + " in inv.");
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage(
						"Use: ::item id (optional:amount)");
			}
			return true;
		case "unban":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			name = Utils.formatPlayerNameForDisplay(name);
			LoginClientChannelManager
					.sendUnreliablePacket(LoginChannelsPacketEncoder
							.encodeRemoveOffence(
									LoginProtocol.OFFENCE_REMOVETYPE_BANS,
									name, player.getUsername()).getBuffer());
			return true;
		case "givekeys":
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name.replaceAll(" ", "_"));
			if (target == null) {
				player.getPackets().sendGameMessage(
						"This player is currently offline.");
				return true;
			}
			target.getTreasureHunter().handleEarnedKeys(
					Integer.parseInt(cmd[1]));
			return true;
		case "giveadmin":
			if (player.getUsername().equalsIgnoreCase("Liam")
					|| player.getUsername().equalsIgnoreCase("charity")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World
						.getPlayerByDisplayName(name.replaceAll(" ", "_"));
				assert target != null;
				target.setRights(2);
				player.getPackets().sendGameMessage(
						"You have given admin status to " + target + ".");
			}

			else
				player.getPackets().sendGameMessage(
						"I think you need to ask charity or danny first.");
			return true;
		case "givemod":
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World
						.getPlayerByDisplayName(name.replaceAll(" ", "_"));
				assert target != null;
				target.setRights(1);
				player.getPackets().sendGameMessage(
						"You have given mod status to " + name + ".");
			} else
				player.getPackets().sendGameMessage(
						"I think you should ask Danny or Charity First!");
			return true;
		case "changepassother":
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Account account = Login.forceLoadAccount(name);
			if (account == null) {
				player.getPackets().sendGameMessage(
						"The account " + name + " does not exist.");
				return true;
			}
			account.setPassword(cmd[1]);
			LoginFilesManager.saveAccount(account);
			player.getPackets().sendGameMessage(
					"You have changed " + name + "'s password.");
			return true;
		case "update":
		case "shutdown":
		case "reboot":
			int delay = 300;
			if (cmd.length >= 2) {
				try {
					delay = Integer.valueOf(cmd[1]);
				} catch (NumberFormatException e) {
					return true;
				}
			}
			Engine.shutdown(delay, true, true);
			player.getPackets().sendGameMessage(
					"Sending shutdown, at " + delay + " seconds.");
			return true;

		case "givesupport":
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World
						.getPlayerByDisplayName(name.replaceAll(" ", "_"));
				assert target != null;
				target.setSupport(true);
				player.getPackets().sendGameMessage(
						"You have promoted " + target + " to support.");
			}
			return true;

		case "demote":
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")) {
				name = "";
				for (int i = 1; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World
						.getPlayerByDisplayName(name.replaceAll(" ", "_"));
				assert target != null;
				target.setRights(0);
				player.getPackets().sendGameMessage(
						"You have demoted " + target + ".");
			}

			else
				player.getPackets()
						.sendGameMessage(
								"If you have an issue with a staff member, report them to Charity!");
			return true;
		case "teleto":
			String name2222 = "";
			for (int i = 1; i < cmd.length; i++)
				name2222 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player target222 = World.getPlayerByDisplayName(name2222);
			if (target222 == null)
				player.getPackets().sendGameMessage(
						"Couldn't find player " + name2222 + ".");
			else {
				player.setNextWorldTile(target222);
			}
			return true;
		case "sendhome":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null
					|| (target.getControllerManager() != null && target
							.getControllerManager().getController() != null))
				return true;
			target.unlock();
			target.getControllerManager().forceStop();
			if (target.getNextWorldTile() == null)
				target.setNextWorldTile(Settings.HOME_LOCATION);
			player.getPackets().sendGameMessage(
					"You have unnulled: " + target.getDisplayName() + ".");
			return true;
		case "kill":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target == null
					|| (target.getControllerManager() != null && target
							.getControllerManager().getController() != null))
				return true;
			target.applyHit(new Hit(target, player.getHitpoints(),
					HitLook.POISON_DAMAGE));
			player.getPackets().sendGameMessage(
					"You have killed " + target.getDisplayName() + ".");
			target.stopAll();
			return true;
		case "tonpc":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::tonpc id(-1 for player)");
				return true;
			}
			try {
				player.getAppearence()
						.transformIntoNPC(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::tonpc id(-1 for player)");
			}
			return true;
		case "getid":
			name = "";
			for (int i = 1; i < cmd.length; i++) {
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			CacheSearch.handleSearch(player, name);
			return true;
		case "resetskill":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[1] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.getSkillTasks().setCurrentTask(null);
			player.getPackets().sendGameMessage(
					"You have reset " + target.getDisplayName()
							+ "'s skiller task.");
			target.getPackets().sendGameMessage(
					"Your skiller task has been reset by "
							+ player.getDisplayName() + ".");
			return true;
		case "setcompletefc":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[1] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.setCompletedFightCaves();
			return true;
		case "setcompletefk":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[1] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.setCompletedFightKiln();
			return true;

		case "isdead":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[1] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);

			player.getPackets().sendGameMessage(
					"Player is dead: "
							+ (target != null && target.isInDeathRoom));

			return true;
		case "getsession":
		case "getip":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[1] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);
			assert target != null;
			player.getPackets().sendGameMessage(
					"IP: " + target.getSession().getIP());
			player.getPackets().sendGameMessage(
					"Data: " + target.getSession().toString());
			player.getPackets().sendGameMessage(
					"Local: " + target.getSession().getLocalAddress());

			return true;

		case "dailychallenge":
			player.getDailyTask().generateDailyTasks(player, true);
			return true;
		case "alldance":
			for (Player p : World.getPlayers())
				p.setNextAnimation(new Animation(7071));
			player.getPackets().sendGameMessage("Everyone is now dancing!");
			return true;
		case "dailystatus":
			player.getPackets()
					.sendGameMessage(
							Color.ORANGE,
							(player.getDailyTask().hasDoneDaily ? "Daily Challenge Updated:"
									: "New Daily Challenge:")
									+ " "
									+ player.getDailyTask().reformatTaskName(
											player.getDailyTask().getName())
									+ " ("
									+ player.getDailyTask()
											.getAmountCompleted()
									+ " / "
									+ player.getDailyTask().getTotalAmount()
									+ ").");
			return true;

		case "warband":
		case "warbands":
			switch (cmd[1].toLowerCase()) {
			case "construction":
				player.getInventory().addItem(27636, 28);
				return true;
			case "herblore":
				player.getInventory().addItem(27637, 28);
				return true;
			case "smithing":
				player.getInventory().addItem(27638, 28);
				return true;
			case "farming":
				player.getInventory().addItem(27639, 28);
				return true;
			case "mining":
				player.getInventory().addItem(27640, 28);
				return true;
			case "lvl1":
			case "location1":
				player.setNextWorldTile(new WorldTile(3032, 3584, 0));
				return true;
			case "lvl2":
			case "location2":
				player.setNextWorldTile(new WorldTile(3305, 3776, 0));
				return true;
			case "lvl3":
			case "location3":
				player.setNextWorldTile(new WorldTile(3132, 3846, 0));
				return true;
			case "next":
			case "nextevent":
				player.getPackets()
						.sendGameMessage(
								"warbands "
										+ cmd[1]
										+ ": "
										+ (Warbands.warband == null ? "null"
												: Utils.getHoursMinsLeft(Warbands.warband.time))
										+ ".");
				return true;
			case "start":
			case "event":
			case "startevent":
			case "eventstart":
				if (Warbands.warband == null) {
					int random = Utils.random(WarbandEvent.values().length);
					if (WarbandEvent.getEvent(random) != null)
						Warbands.warband = new Warbands(random);
					player.getPackets().sendGameMessage(
							"warbands: " + random + ".");
				} else
					player.getPackets().sendGameMessage(
							"There is already an event happening...");
				return true;
			}
			return true;
		case "forcewarband":
			int random = Utils.random(WarbandEvent.values().length);
			if (WarbandEvent.getEvent(random) != null)
				Warbands.warband = new Warbands(random);
			player.getPackets().sendGameMessage("warbands: " + random + ".");
			return true;

		case "setvar":
			switch (cmd[1].toLowerCase()) {
			case "runenergy":
			case "re":
			case "energy":
				player.setRunEnergy(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						"runEnergy: to " + Integer.parseInt(cmd[2]) + ".");
				return true;
			case "sof":
			case "treasurehunter":
			case "th":
				switch (cmd[2].toLowerCase()) {
				case "dailykey":
				case "dailykeys":
				case "keys":
				case "key":
					player.getTreasureHunter()
							.setKeys(Integer.parseInt(cmd[3]));
					player.getPackets().sendGameMessage(
							cmd[1] + ": at " + cmd[2] + ": to "
									+ Integer.parseInt(cmd[3]) + ".");
					return true;
				}
				return true;
			case "controller":
				switch (cmd[2].toLowerCase()) {
				case "null":
					player.getControllerManager()
							.removeControllerWithoutCheck();
					player.getPackets().sendGameMessage(
							cmd[1] + " to " + cmd[2] + ".");
					return true;
				default:
					player.getPackets().sendGameMessage(
							"unable to " + cmd[1] + " to '" + cmd[2] + "'.");
					break;
				}
				return true;
			case "ironman":
				player.setIronman(!player.isIronman());
				player.setHardcoreIronMan(false);
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.isIronman() + ".");
				return true;
			case "hardcoreironman":
				player.setHardcoreIronMan(!player.isHardcoreIronman());
				player.setIronman(false);
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.isHardcoreIronman() + ".");
				return true;
			case "noironman":
				player.setHardcoreIronMan(false);
				player.setIronman(false);
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.isIronman() + " and "
								+ player.isHardcoreIronman() + ".");
				return true;
			case "lp":
			case "loyaltypoints":
				player.setLoyaltyPoints(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getLoyaltyPoints() + ".");
				return true;
			case "jadinko":
			case "favorpoints":
				player.setFavorPoints(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getFavorPoints() + ".");
				return true;
			case "silverhawk":
			case "silverhawkboots":
				player.setSilverhawkFeathers(Integer.parseInt(cmd[2]));
				player.getPackets()
						.sendGameMessage(
								cmd[1] + ": to "
										+ player.getSilverhawkFeathers() + ".");
				return true;
			case "reaper":
			case "reapertask":
			case "reapertasks":
			case "reaperassignment":
			case "reaperassignments":
				switch (cmd[2].toLowerCase()) {
				case "points":
				case "reaperpoints":
					player.setReaperPoints(Integer.parseInt(cmd[3]));
					player.getPackets().sendGameMessage(
							"var: " + cmd[1] + " refering to " + cmd[2]
									+ " to " + player.getReaperPoints() + ".");
					return true;
				case "first":
				case "firsttime":
				case "switch":
					player.firstReaperTask = !player.firstReaperTask;
					player.getPackets().sendGameMessage(
							"var: " + cmd[1] + " refering to " + cmd[2]
									+ " to " + player.firstReaperTask + ".");
					return true;
				case "settaskamount":
				case "taskamount":
					player.getReaperTasks().setTaskAmount(
							Integer.parseInt(cmd[3]));
					player.getPackets()
							.sendGameMessage(
									"var: "
											+ cmd[1]
											+ " refering to "
											+ cmd[2]
											+ " to "
											+ player.getReaperTasks()
													.getAmount() + ".");
					return true;
				case "hastask":
				case "settask":
				case "task":
				case "resettask":
					player.getReaperTasks().setCurrentTask(null);
					player.getPackets().sendGameMessage(
							"var: " + cmd[1] + " refering to " + cmd[2]
									+ " to "
									+ player.getReaperTasks().getCurrentTask()
									+ ".");
					return true;
				}
				return true;
			case "sp":
			case "slayerpoints":
				player.getSlayerManager().setPoints(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to "
								+ player.getSlayerManager().getPoints() + ".");
				return true;
			case "skilltask":
			case "skilltaskpoints":
			case "taskpoints":
			case "taskpoint":
				player.setTaskPoints(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getTaskPoints() + ".");
				return true;
			case "portable":
			case "plimit":
			case "portablelimit":
				player.setPortableLimit(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getPortableLimit() + ".");
				return true;
			case "dt":
			case "dungtokens":
			case "dungeoneeringtokens":
				player.getDungManager().setTokens(Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getDungManager().getTokens()
								+ ".");
				return true;
			case "spellbook":
				player.getCombatDefinitions().setSpellBook(
						Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1] + ": to "
								+ player.getCombatDefinitions().getSpellBook()
								+ ".");
				return true;
			case "prayer":
			case "prayerbook":
				player.getPrayer().setPrayerBook(
						!player.getPrayer().isAncientCurses());
				player.getPackets().sendGameMessage(
						cmd[1] + ": to " + player.getPrayer().isAncientCurses()
								+ ".");
				return true;
			case "adrenaline":
			case "spec":
			case "sa":
				player.getCombatDefinitions().increaseSpecialAttack(
						Integer.parseInt(cmd[2]));
				player.getPackets().sendGameMessage(
						cmd[1]
								+ ": to "
								+ player.getCombatDefinitions()
										.getSpecialAttackPercentage() + "%.");
				return true;
			case "god":
				try {
					if (Integer.parseInt(cmd[2]) == 0) {
						player.setGodMode(Integer.parseInt(cmd[2]));
						player.getPackets().sendGameMessage(
								"godMode to " + Integer.parseInt(cmd[2]) + ".");
						player.reset();
						return true;
					}
					player.setGodMode(Integer.parseInt(cmd[2]));
					player.setHitpoints(Integer.MAX_VALUE);
					for (int i = 0; i < 7; i++) {
						player.getCombatDefinitions().getStats()[i] = 50000;
						player.getSkills().set(i, 252);
					}
					player.getPackets().sendGameMessage(
							"hitpoints to " + Utils.format(Integer.MAX_VALUE)
									+ " and godMode to "
									+ Integer.parseInt(cmd[2]) + ".");
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage(
							"use 0: normal - use 1: instant kill");
				}
				return true;
			case "demigod":
				player.setHitpoints(Integer.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(
						player.getMaxHitpoints() - 9900);
				player.getPackets()
						.sendGameMessage(
								"hitpoints to "
										+ Utils.format(Integer.MAX_VALUE) + ".");
				return true;
			case "godwars":
			case "gs":
				switch (cmd[2].toLowerCase()) {
				case "armadyl":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				case "bandos":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				case "zamorak":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				case "saradomin":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				case "nex":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				case "seren":
					player.getPackets().sendGameMessage(
							cmd[1] + ": error while setting var.");
					return true;
				default:
					player.getPackets().sendGameMessage(
							"var: " + cmd[1] + " does not exist.");
					break;
				}
				return true;
			default:
				player.getPackets().sendGameMessage(
						"var: " + cmd[1] + " does not exist.");
				break;
			}
			return true;

		case "donated":
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.getDonationManager().increaseDonationAmount(
					Integer.valueOf(cmd[1]));
			player.getPackets().sendGameMessage(
					"You have confirmed that " + target + "has donated "
							+ (Integer.valueOf(cmd[1]) + "Dollars"));
			return true;

		case "setstat":
			switch (cmd[1].toLowerCase()) {
			case "magic":
				int level6 = Integer.valueOf(cmd[2]);
				player.getSkills().set(6, level6);
				player.getSkills().setXp(6, Skills.getXPForLevel(level6));
				player.getAppearence().generateAppearenceData();
				return true;
			case "attack":
				int level0 = Integer.valueOf(cmd[2]);
				player.getSkills().set(0, level0);
				player.getSkills().setXp(0, Skills.getXPForLevel(level0));
				player.getAppearence().generateAppearenceData();
				return true;
			case "defence":
				int level1 = Integer.valueOf(cmd[2]);
				player.getSkills().set(1, level1);
				player.getSkills().setXp(1, Skills.getXPForLevel(level1));
				player.getAppearence().generateAppearenceData();
				return true;
			case "strength":
				int level2 = Integer.valueOf(cmd[2]);
				player.getSkills().set(2, level2);
				player.getSkills().setXp(2, Skills.getXPForLevel(level2));
				player.getAppearence().generateAppearenceData();
				return true;
			case "hitpoints":
			case "consitution":
				int level3 = Integer.valueOf(cmd[2]);
				player.getSkills().set(3, level3);
				player.getSkills().setXp(3, Skills.getXPForLevel(level3));
				player.getAppearence().generateAppearenceData();
				return true;
			case "range":
			case "ranged":
				int level4 = Integer.valueOf(cmd[2]);
				player.getSkills().set(4, level4);
				player.getSkills().setXp(4, Skills.getXPForLevel(level4));
				player.getAppearence().generateAppearenceData();
				return true;
			case "slayer":
				int level18 = Integer.valueOf(cmd[2]);
				player.getSkills().set(18, level18);
				player.getSkills().setXp(18, Skills.getXPForLevel(level18));
				player.getAppearence().generateAppearenceData();
				return true;
			case "prayer":
				int level5 = Integer.valueOf(cmd[2]);
				player.getSkills().set(5, level5);
				player.getSkills().setXp(5, Skills.getXPForLevel(level5));
				player.getAppearence().generateAppearenceData();
				return true;
			case "cooking":
				int level7 = Integer.valueOf(cmd[2]);
				player.getSkills().set(7, level7);
				player.getSkills().setXp(7, Skills.getXPForLevel(level7));
				player.getAppearence().generateAppearenceData();
				return true;
			case "woodcutting":
				int level8 = Integer.valueOf(cmd[2]);
				player.getSkills().set(8, level8);
				player.getSkills().setXp(8, Skills.getXPForLevel(level8));
				player.getAppearence().generateAppearenceData();
				return true;
			case "fletching":
				int level9 = Integer.valueOf(cmd[2]);
				player.getSkills().set(9, level9);
				player.getSkills().setXp(9, Skills.getXPForLevel(level9));
				player.getAppearence().generateAppearenceData();
				return true;
			case "fishing":
				int level10 = Integer.valueOf(cmd[2]);
				player.getSkills().set(10, level10);
				player.getSkills().setXp(10, Skills.getXPForLevel(level10));
				player.getAppearence().generateAppearenceData();
				return true;
			case "firemaking":
				int level11 = Integer.valueOf(cmd[2]);
				player.getSkills().set(11, level11);
				player.getSkills().setXp(11, Skills.getXPForLevel(level11));
				player.getAppearence().generateAppearenceData();
				return true;
			case "crafting":
				int level12 = Integer.valueOf(cmd[2]);
				player.getSkills().set(12, level12);
				player.getSkills().setXp(12, Skills.getXPForLevel(level12));
				player.getAppearence().generateAppearenceData();
				return true;
			case "smithing":
				int level13 = Integer.valueOf(cmd[2]);
				player.getSkills().set(13, level13);
				player.getSkills().setXp(13, Skills.getXPForLevel(level13));
				player.getAppearence().generateAppearenceData();
				return true;
			case "mining":
				int level14 = Integer.valueOf(cmd[2]);
				player.getSkills().set(14, level14);
				player.getSkills().setXp(14, Skills.getXPForLevel(level14));
				player.getAppearence().generateAppearenceData();
				return true;
			case "herblore":
				int level15 = Integer.valueOf(cmd[2]);
				player.getSkills().set(15, level15);
				player.getSkills().setXp(15, Skills.getXPForLevel(level15));
				player.getAppearence().generateAppearenceData();
				return true;
			case "agility":
				int level16 = Integer.valueOf(cmd[2]);
				player.getSkills().set(16, level16);
				player.getSkills().setXp(16, Skills.getXPForLevel(level16));
				player.getAppearence().generateAppearenceData();
				return true;
			case "thieving":
				int level17 = Integer.valueOf(cmd[2]);
				player.getSkills().set(17, level17);
				player.getSkills().setXp(17, Skills.getXPForLevel(level17));
				player.getAppearence().generateAppearenceData();
				return true;
			case "farming":
				int level19 = Integer.valueOf(cmd[2]);
				player.getSkills().set(19, level19);
				player.getSkills().setXp(19, Skills.getXPForLevel(level19));
				player.getAppearence().generateAppearenceData();
				return true;
			case "runecrafting":
				int level20 = Integer.valueOf(cmd[2]);
				player.getSkills().set(20, level20);
				player.getSkills().setXp(20, Skills.getXPForLevel(level20));
				player.getAppearence().generateAppearenceData();
				return true;
			case "hunter":
				int level21 = Integer.valueOf(cmd[2]);
				player.getSkills().set(21, level21);
				player.getSkills().setXp(21, Skills.getXPForLevel(level21));
				player.getAppearence().generateAppearenceData();
				return true;
			case "construction":
				int level22 = Integer.valueOf(cmd[2]);
				player.getSkills().set(22, level22);
				player.getSkills().setXp(22, Skills.getXPForLevel(level22));
				player.getAppearence().generateAppearenceData();
				return true;
			case "summoning":
				int level23 = Integer.valueOf(cmd[2]);
				player.getSkills().set(23, level23);
				player.getSkills().setXp(23, Skills.getXPForLevel(level23));
				player.getAppearence().generateAppearenceData();
				return true;
			case "dungeoneering":
				int level24 = Integer.valueOf(cmd[2]);
				player.getSkills().set(24, level24);
				player.getSkills().setXp(24, Skills.getXPForLevel(level24));
				player.getAppearence().generateAppearenceData();
				return true;
			case "divination":
				int level25 = Integer.valueOf(cmd[2]);
				player.getSkills().set(25, level25);
				player.getSkills().setXp(25, Skills.getXPForLevel(level25));
				player.getAppearence().generateAppearenceData();
				return true;
			case "invention":
				int level26 = Integer.valueOf(cmd[2]);
				player.getSkills().set(26, level26);
				player.getSkills().setXp(26, Skills.getXPForLevel(level26));
				player.getAppearence().generateAppearenceData();
				return true;
			default:
				player.getPackets().sendGameMessage(
						"skill: '" + cmd[1] + "' does not exist.");
				break;
			}
			player.getPackets().sendGameMessage(
					cmd[1] + " (-1) to " + Integer.valueOf(cmd[2]) + ".");
			return true;

		case "lock":
			player.lock();
			return true;

		case "secure":
		case "securecode":
			player.getTemporaryAttributtes().put("SetSecureCode", 0);
			player.getPackets()
					.sendInputIntegerScript(
							player.getSecureCode() == 0 ? "Please enter your new secure code:"
									: "Please enter your current secure code:");
			return true;

		case "removeobject":
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"data/map/unpackedSpawnsList.txt", true));
				writer.newLine();
				writer.write("-1 10 0 - " + player.getX() + " " + player.getY()
						+ " " + player.getPlane());
				writer.flush();
				writer.close();
				player.getPackets().sendGameMessage("Removed object.");
			} catch (IOException er) {
				er.printStackTrace();
				player.getPackets().sendGameMessage(
						"Error while removing object.");
			}
			return true;

		case "god":
			player.getEquipment().setEquipmentHpIncrease(
					player.getMaxHitpoints() - 9900);
			for (int i = 0; i < 7; i++) {
				player.getCombatDefinitions().getStats()[i] = 50000;
				player.getSkills().set(i, 252);
			}
			player.setHitpoints(Integer.MAX_VALUE);
			player.getPackets().sendGameMessage(
					"hitpoints to " + Utils.format(Integer.MAX_VALUE)
							+ " and 0-6 stats: 50000.");
			return true;

		case "addobject":
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"data/map/unpackedSpawnsList.txt", true));
				writer.newLine();
				writer.write(Integer.parseInt(cmd[1]) + " 10 "
						+ Integer.parseInt(cmd[2]) + " - " + player.getX()
						+ " " + player.getY() + " " + player.getPlane());
				writer.flush();
				writer.close();
				player.getPackets().sendGameMessage(
						"Added object: " + Integer.parseInt(cmd[1])
								+ " to your position.");
			} catch (IOException er) {
				er.printStackTrace();
				player.getPackets().sendGameMessage(
						"Error while adding object.");
			}
			return true;

		case "addnpc":
			try {
				final BufferedWriter bw = new BufferedWriter(new FileWriter(
						"data/npcs/spawnsList.txt", true));
				bw.newLine();
				bw.write(Integer.parseInt(cmd[1]) + " - " + player.getX() + " "
						+ player.getY() + " " + player.getPlane());
				bw.flush();
				bw.close();
				player.getPackets().sendGameMessage(
						"Added npc: " + Integer.parseInt(cmd[1])
								+ " to your position.");
				World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
			} catch (final Throwable tt) {
				tt.printStackTrace();
			}
			return true;
		case "stafflist":
			StaffList.send(player);
			return true;
		case "evilchicken":
			player.setNextWorldTile(new WorldTile(2643, 10413, 0));
			return true;
		case "adddungtoken":
		case "givedungtoken":
		case "setdungtoken":
		case "setdungtokens":
			name = "";
			int tokens = Integer.parseInt(cmd[1]);
			for (int i = 2; i < cmd.length; i++)
				name += cmd[2] + ((i == cmd.length - 1) ? "" : " ");

			Player pTarget = World.getPlayerByDisplayName(name);
			player.getPackets().sendGameMessage("Done.", true);
			assert pTarget != null;
			pTarget.getDungManager().addTokens(tokens);
			return true;
		case "givebonds":
			name = "";
			int bondsAmount = Integer.parseInt(cmd[1]);
			for (int i = 2; i < cmd.length; i++)
				name += cmd[2] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			player.getPackets().sendGameMessage(
					"You have given " + target.getDisplayName() + " "
							+ bondsAmount + " bonds.", true);
			target.getPackets()
					.sendGameMessage(
							"You have received " + bondsAmount
									+ " bonds in your bank.", true);
			target.getBank().addItem(
					new Item(ItemIdentifiers.BOND_UNTRADEABLE, bondsAmount),
					true);
			return true;
		case "checkitem":
			for (Player players : World.getPlayers()) {
				if (!players.containsItem(Integer.valueOf(cmd[1]))) {
					continue;
				}
				ItemDefinitions itemName = ItemDefinitions
						.getItemDefinitions(Integer.valueOf(cmd[1]));
				player.getPackets().sendGameMessage(
						players.getDisplayName() + " has " + itemName.getName()
								+ " (" + itemName.getId() + ").");
			}
			return true;
		case "dupers":
		case "listdupers":
		case "maxamount":
			player.getPackets().sendGameMessage("Listing dupers below...");
			for (Player players : World.getPlayers()) {
				for (int i = 1; i < 35000; i++) {
					if (!players.getInventory().containsItem(i, 1000000000)) {
						continue;
					}
					ItemDefinitions itemName = ItemDefinitions
							.getItemDefinitions(i);
					player.getPackets().sendGameMessage(
							"<col=FF0000>" + players.getDisplayName()
									+ " has x"
									+ player.getInventory().getAmountOf(i)
									+ " of " + itemName.getName() + " ("
									+ itemName.getId() + ").");
				}
			}
			return true;

		case "offers":
		case "geoffers":
			GrandExchange.showOffers(player);
			return true;

		case "testkk":
			DeveloperConsole.max(player, 200000000);
			player.getPackets().sendGameMessage(
					
					".maybe");
			return true;

		case "araxxi":
		case "araxxor":
			player.setNextWorldTile(new WorldTile(4485, 6266, 1));
			return true;

		case "araxxifight":
			player.getControllerManager().startController("AraxxiController",
					true, player);
			return true;

		case "araxxorfight":
			player.getControllerManager().startController("AraxxiController",
					true, player);
			return true;

		case "executescript":
			if (cmd.length == 2)
				player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]));
			else if (cmd.length == 3)
				player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]),
						Integer.parseInt(cmd[2]));
			else if (cmd.length == 4)
				player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]),
						Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
			else if (cmd.length == 5)
				player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]),
						Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]),
						Integer.parseInt(cmd[4]));
			break;

		case "max":
		case "master":
			DeveloperConsole.max(player, 200000000);
			return true;

		case "drop":
			for (Player players : World.getPlayers()) {
				players.getPackets().sendGroundItem(
						new FloorItem(new Item(Integer.valueOf(cmd[1]), 1),
								new WorldTile(players.getX() - 1, players
										.getY(), players.getPlane()), players,
								false, false));
				ItemDefinitions def = ItemDefinitions
						.getItemDefinitions(Integer.valueOf(cmd[1]));
				players.getPackets().sendGameMessage(
						"Oh look! A wild " + def.getName() + " appears!");
			}
			return true;

		case "troll":
			World.sendNews(
					Utils.fixChatMessage(cmd[1])
							+ " has received "
							+ ItemDefinitions.getItemDefinitions(
									Integer.parseInt(cmd[2])).getName()
							+ " drop!", 1, true);
			return true;

		case "forcedrop":
			World.addGroundItem(new Item(Integer.parseInt(cmd[1])),
					new WorldTile(player), player, true, 1);
			ItemDefinitions def2 = ItemDefinitions.getItemDefinitions(Integer
					.valueOf(cmd[1]));
			player.getPackets().sendGameMessage(
					"Dropped " + def2.getName() + " under you.");
			return true;

		case "interfaceg":
			player.getInterfaceManager().sendMinigameInterface(601);
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(601); i++) {
				player.getPackets().sendIComponentText(601, i, "" + i);
			}
			return true;

		case "interface":
			player.getInterfaceManager().sendCentralInterface(
					Integer.valueOf(cmd[1]));
			player.getPackets().sendPanelBoxMessage(
					"interface: " + Integer.valueOf(cmd[1]) + ".");
			return true;

		case "interface2":
			player.getInterfaceManager().sendCentralInterface(
					Integer.parseInt(cmd[1]));
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(Integer
							.parseInt(cmd[1])); i++)
				player.getPackets().sendIComponentText(
						Integer.parseInt(cmd[1]), i, "" + i);
			return true;

		case "interface3":
			player.getInterfaceManager().setBackgroundInterface(false,
					Integer.parseInt(cmd[1]));
			return true;

		case "interface4":
			player.getInterfaceManager().setBackgroundInterface(false,
					Integer.parseInt(cmd[1]));
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(Integer
							.parseInt(cmd[1])); i++) {
				player.getPackets().sendIComponentText(
						Integer.parseInt(cmd[1]), i, "" + i);
			}
			return true;

		case "interface5":
			player.getInterfaceManager().sendMinigameInterface(
					Integer.parseInt(cmd[1]));
			return true;

		case "interface6":
			player.getInterfaceManager().sendMinigameInterface(
					Integer.parseInt(cmd[1]));
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(Integer
							.parseInt(cmd[1])); i++) {
				player.getPackets().sendIComponentText(
						Integer.parseInt(cmd[1]), i, "" + i);
			}
			return true;

		case "playrecorder":
			Engine.getDiscordBot().getChannel("248969620919287818")
					.sendMessage("~play runescape recorder");
			player.getPackets().sendGameMessage(
					"Thank you Miles for adding this amazing Discord bot!");
			return true;

		case "logout":
		case "disconnect":
		case "dc":
			player.disconnect(true, false);
			return true;

		case "badge":
			player.getPackets().sendGameMessage(
					"<img=" + Integer.valueOf(cmd[1]) + ">");
			return true;

		case "pports":
			player.getPlayerPorts().initalizePort();
			return true;

		case "unlock":
			player.unlock();
			player.getPackets().sendGameMessage("done.");
			return true;

		case "chunk":
			player.getPackets().sendGameMessage(
					"Chunk X: " + player.getChunkX() + ", Chunk Y: "
							+ player.getChunkY());
			return true;

		case "region":
			player.getPackets().sendGameMessage(
					"Region ID: " + player.getRegionId());
			return true;

		case "coords":
		case "mypos":
		case "position":
			player.getPackets().sendPanelBoxMessage(
					player.getX() + ", " + player.getY() + ", "
							+ player.getPlane() + ". Hash: "
							+ player.getTileHash() + " Region: "
							+ player.getXInRegion() + ", "
							+ player.getYInRegion() + ". Chunk: "
							+ player.getXInChunk() + ", "
							+ player.getYInChunk() + ". Scene: "
							+ player.getXInScene(player) + ", "
							+ player.getYInScene(player) + ". Map: "
							+ player.getMapRegionsIds() + ".");
			System.out.println(player.getX() + ", " + player.getY() + ", "
					+ player.getPlane() + ". Region: " + player.getXInRegion()
					+ ", " + player.getYInRegion() + ". Chunk: "
					+ player.getXInChunk() + ", " + player.getYInChunk()
					+ ". Scene: " + player.getXInScene(player) + ", "
					+ player.getYInScene(player) + ", "
					+ player.getZInScene(player) + ". Map: "
					+ player.getMapRegionsIds() + ".");
			return true;

		case "getips":
			player.getInterfaceManager().sendCentralInterface(1166);
			player.getPackets().sendIComponentText(1166, 23, "IP List");
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
			String list2 = "";
			for (Player p : World.getPlayers()) {
				list2 += p.getDisplayName() + " - " + p.getSession().getIP()
						+ "<br>";
			}

			player.getPackets().sendIComponentText(1166, 1, list2);
			return true;
		case "voragofloortest":
			int phase = Integer.valueOf(cmd[1]);
			int[] xstart = { 3090, 3026, 3090, 3026 };// Phase start locations
														// for X, 1, 2, 3, 4
			int[] ystart = { 6098, 6034, 6034, 5970 };// Phase start locations
														// for Y, 1, 2, 3, 4
			WorldTasksManager.schedule(new WorldTask() {
				int count = 0;

				@Override
				public void run() {
					if (count == 1) {
						World.spawnObjectTemporary(new WorldObject(84873, 10,
								1, new WorldTile(xstart[phase - 1],
										ystart[phase - 1], 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84871, 10,
								1, new WorldTile(xstart[phase - 1],
										ystart[phase - 1] + 9, 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84873, 10,
								2, new WorldTile(xstart[phase - 1],
										ystart[phase - 1] + 18, 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84871, 10,
								0, new WorldTile(xstart[phase - 1] + 9,
										ystart[phase - 1], 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84869, 10,
								0, new WorldTile(xstart[phase - 1] + 9,
										ystart[phase - 1] + 9, 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84871, 10,
								2, new WorldTile(xstart[phase - 1] + 9,
										ystart[phase - 1] + 18, 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84873, 10,
								0, new WorldTile(xstart[phase - 1] + 18,
										ystart[phase - 1], 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84871, 10,
								3, new WorldTile(xstart[phase - 1] + 18,
										ystart[phase - 1] + 9, 0)), 5000);
						World.spawnObjectTemporary(new WorldObject(84873, 10,
								3, new WorldTile(xstart[phase - 1] + 18,
										ystart[phase - 1] + 18, 0)), 5000);
					} else if (count == 2) {
						player.setNextAnimation(new Animation(20402));
						stop();
					}
					count++;
				}

			}, 0, 1);
			return true;

		case "ros":
		case "rots":
		case "riseofsix":
		case "riseofthesix":
			player.getControllerManager().startController("RiseOfTheSix", true,
					player);
			player.getPackets().sendGameMessage("controller: 'RiseOfTheSix'.");
			return true;

		case "dialogue":
			player.getPackets().sendInputLongTextScript(
					"Please enter the dialogue name:");
			player.getTemporaryAttributtes().put("senddialogue", Boolean.TRUE);
			return true;

		case "Controller":
		case "controller":
			player.getPackets().sendInputLongTextScript(
					"Please enter the controller name:");
			player.getTemporaryAttributtes()
					.put("sendcontroller", Boolean.TRUE);
			return true;

		case "emusic":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::emusic effectId");
				return true;
			}
			try {
				player.getMusicsManager().playMusicEffect(
						Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::emusic effectId");
			}
			return true;

		case "wave":
			World.sendGraphics(player,
					new Graphics(3560, 0, 0, Integer.parseInt(cmd[1]), true),
					new WorldTile(player));
			break;
		case "hint":
			player.getHintIconsManager().addHintIcon(player, 3, 8000, false);
			break;
		case "praybook":
			player.getPrayer().setPrayerBook(
					!player.getPrayer().isAncientCurses());
			break;
		case "qbd":
			player.getControllerManager().startController(
					"QueenBlackDragonController");
			break;
		case "fade":
			FadingScreen.unfade(player, FadingScreen.fade(player, 1000),
					() -> {

					});
			break;
		case "toggledailytasks":
			DailyTasksInterface.openTaskDialogue(player);
			break;
		case "pmsg":
			player.getPackets().sendEntityMessage(Integer.parseInt(cmd[2]),
					0xFFFFFF, player, cmd[1]);
			return true;
		case "ptest":
			EffectsManager.makePoisoned(player, Integer.parseInt(cmd[1]));
			return true;
		case "girl":
		case "female":
			player.getAppearence().female();
			player.getAppearence().generateAppearenceData();
			return true;
		case "boy":
		case "male":
			player.getAppearence().male();
			player.getAppearence().generateAppearenceData();
			return true;
		case "randomevent":
			CombatEventNPC.startRandomEvent(player, Integer.parseInt(cmd[1]));
			return true;
		case "book":
			player.getCombatDefinitions()
					.setSpellBook(Integer.parseInt(cmd[1]));
			return true;
		case "corruptxp":
			int skillid = Integer.parseInt(cmd[1]);
			target = World.getPlayer(cmd[2]);
			if (target != null)
				target.getSkills().setXp(skillid, 14000000);
			return true;
		case "anon":
			player.getAppearence().setIdentityHide(
					!player.getAppearence().isIdentityHidden());
			return true;
		case "evearena":
			EventArena a = ArenaFactory.randomEventArena(true);
			if (a != null) {
				a.create();
				player.getPackets().sendGameMessage(
						"Pos:" + a.minX() + "," + a.minY());

				player.setForceNextMapLoadRefresh(true);
				player.loadMapRegions();
				player.setNextWorldTile(new WorldTile(a.minX(), a.minY(), 0));
			}
			break;
		case "costumecolor":
			SkillCapeCustomizer.costumeColorCustomize(player);
			return true;
		case "setprice":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: ::setprice i i");
				return true;
			}
			GrandExchange.setPrice(Integer.parseInt(cmd[1]),
					Integer.parseInt(cmd[2]));
			GrandExchange.savePrices();
			return true;

		case "floorf":
			System.out.println(World.isFloorFree(player.getPlane(),
					player.getX(), player.getY()));
			return true;
		case "leak":
			GameExecutorManager.fastExecutor.scheduleAtFixedRate(
					new TimerTask() {

						@Override
						public void run() {
							if (player.hasFinished()) {
								cancel();
								return;
							}
							player.setForceNextMapLoadRefresh(true);
							player.loadMapRegions();

						}

					}, 0, 5000);
			return true;
		case "checkbank":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player Other = World.getPlayerByDisplayName(name);
			try {
				assert Other != null;
				player.getPackets().sendItems(95,
						Other.getBank().getContainerCopy());
				player.getBank().openPlayerBank(Other);
			} catch (Exception e) {
				e.printStackTrace();
				player.getPackets().sendGameMessage("error: " + e + ".");
			}
			return true;
		case "reloadshops":
			ShopsHandler.forceReload();
			return true;
		case "shop":
			ShopsHandler.openShop(player, Integer.parseInt(cmd[1]));
			return true;
		case "resethouse":
			player.getHouse().reset();
			return true;
		case "pestpoints":
			player.setCommendation(500);
			return true;
		case "hide":
			player.getAppearence()
					.setHidden(!player.getAppearence().isHidden());
			player.getPackets().sendGameMessage(
					"Hidden:" + player.getAppearence().isHidden());
			return true;
		case "maxdung":
			player.getDungManager().setMaxComplexity(6);
			player.getDungManager().setMaxFloor(60);
			player.getPackets().sendGameMessage(
					"You have completed all aspects of dungeoneering.");
			return true;
		case "sprite":
			for (int i = 0; i < 100; i++)
				player.getPackets().sendIComponentSprite(408, i, 1);
			return true;
		case "nextclue":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.getTreasureTrailsManager().setNextClue(0);
				player.getPackets().sendGameMessage("Complete.");
				target.getPackets().sendGameMessage(
						"Your clue has been automatically completed.");
			} else {
				player.getPackets().sendGameMessage(name + " is offline.");
			}
			return true;
		case "enablebxp":
			World.sendWorldMessage("<col=551177>Bonus EXP has been"
					+ "<col=88aa11> enabled.", false);
			if (!Settings.DOUBLE_XP)
				World.addIncreaseElapsedBonusMinutesTak();
			Settings.DOUBLE_XP = true;
			return true;
		case "disablebxp":
			World.sendWorldMessage("<col=551177>Bonus EXP has been"
					+ "<col=990022> disabled.", false);
			Settings.DOUBLE_XP = false;
			return true;
		case "scshop":
			player.increaseStealingCreationPoints(100);
			StealingCreationShop.openInterface(player);
			return true;
		case "clipflag":
			int mask = World.getMask(player.getPlane(), player.getX(),
					player.getY());
			StringBuilder flagbuilder = new StringBuilder();
			flagbuilder.append('(');
			for (Field field : Flags.class.getDeclaredFields()) {
				try {
					if ((mask & field.getInt(null)) == 0)
						continue;
				} catch (Throwable t) {
					continue;
				}

				if (flagbuilder.length() <= 1) {
					flagbuilder.append("Flags.").append(field.getName());
				} else {
					flagbuilder.append(" | Flags.").append(field.getName());
				}
			}
			flagbuilder.append(')');
			System.err.println("Flag is:" + flagbuilder.toString());
			System.out.println(player.getXInRegion() + ", "
					+ player.getYInRegion());
			return true;
		case "walkto":
			int wx = Integer.parseInt(cmd[1]);
			int wy = Integer.parseInt(cmd[2]);
			boolean checked = cmd.length > 3 && Boolean.parseBoolean(cmd[3]);
			long rstart = System.nanoTime();
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER,
					player.getX(), player.getY(), player.getPlane(),
					player.getSize(), new FixedTileStrategy(wx, wy), false);
			long rtook = (System.nanoTime() - rstart)
					- WalkRouteFinder.debug_transmittime;
			player.getPackets().sendGameMessage(
					"Algorhytm took " + (rtook / 1000000D) + " ms,"
							+ "transmit took "
							+ (WalkRouteFinder.debug_transmittime / 1000000D)
							+ " ms, steps:" + steps);
			int[] bufferX = RouteFinder.getLastPathBufferX();
			int[] bufferY = RouteFinder.getLastPathBufferY();
			for (int i = steps - 1; i >= 0; i--) {
				player.addWalkSteps(bufferX[i], bufferY[i], Integer.MAX_VALUE,
						checked);
			}

			return true;
		case "loyalty":
			LoyaltyProgram.open(player);
			return true;
		case "ugd":
			player.getControllerManager().startController("UnderGroundDungeon",
					false, true, true);
			return true;
		case "ss2":
			player.getMoneyPouch().init();
			return true;
		case "sendscriptblank":
			player.getPackets().sendExecuteScriptReverse(
					Integer.parseInt(cmd[1]));
			return true;
		case "script":
			player.getPackets().sendExecuteScriptReverse(
					Integer.parseInt(cmd[1]));
			return true;
		case "script1":
			player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]),
					Integer.parseInt(cmd[2]));
			return true;
		case "script2":
			player.getPackets().sendExecuteScript(Integer.parseInt(cmd[1]),
					Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
			return true;
		case "ss":
			player.getPackets().sendExecuteScriptReverse(8865,
					Integer.parseInt(cmd[1]));
			return true;
		case "testresetsof":
			player.getPackets().sendExecuteScriptReverse(5879); // sof_setupHooks();
			// should work
			return true;
		case "sendsofempty":
			player.getPackets().sendItems(665, new Item[13]);
			return true;
		case "sendsofitems":
			Item[] items = new Item[13];
			for (int i = 0; i < items.length; i++)
				items[i] = new Item(995, i + 1);// items[i] = new
			// Item(995,
			// Utils.random(1000000000)
			// + 1);
			player.getPackets().sendItems(665, items);
			return true;
		case "senditems":
			for (int i = 0; i < 5000; i++)
				player.getPackets().sendItems(i, new Item[] { new Item(i, 1) });
			return true;
		case "forcewep":
			player.getAppearence().setForcedWeapon(Integer.parseInt(cmd[1]));
			return true;
		case "clearst":
			for (Player p2 : World.getPlayers())
				p2.getSlayerManager().skipCurrentTask(false);
			return true;
		case "ectest":
			player.getDialogueManager().startDialogue(
					"EconomyTutorialCutsceneDialog");
			return true;
		case "ecotestcutscene":
			player.getCutscenesManager().play("EconomyTutorialCutscene");
			return true;
		case "istest":
			player.getSlayerManager().sendSlayerInterface(
					SlayerManager.BUY_INTERFACE);
			return true;
		case "st":
			player.getSlayerManager()
					.setCurrentTask(true, SlayerMaster.KURADAL);
			return true;
		case "addpoints":
			player.getSlayerManager().setPoints(5000);
			return true;
		case "testdeath":
			player.getInterfaceManager().sendCentralInterface(18);
			player.getPackets().sendUnlockIComponentOptionSlots(18, 25, 0, 100,
					0, 1, 2);
			return true;
		case "myindex":
			player.getPackets().sendGameMessage(
					"My index is:" + player.getIndex());
			return true;
		case "gw":
		case "godwars":
			player.getControllerManager().startController("GodWars");
			return true;
		case "getspawned": {
			List<WorldObject> spawned = World.getRegion(player.getRegionId())
					.getSpawnedObjects();
			player.getPackets().sendGameMessage(
					"region:" + player.getRegionId());
			player.getPackets().sendGameMessage("-------");
			spawned.stream()
					.filter(o -> o.getChunkX() == player.getChunkX()
							&& o.getChunkY() == player.getChunkY()
							&& o.getPlane() == player.getPlane())
					.forEach(
							o -> player.getPackets().sendGameMessage(
									o.getId() + "," + o.getX() + "," + o.getY()
											+ "," + o.getPlane()));
			player.getPackets().sendGameMessage("-------");
			return true;
		}
		case "removeobjects": {
			List<WorldObject> objects = World.getRegion(player.getRegionId())
					.getAllObjects();
			objects.stream()
					.filter(o -> o.getChunkX() == player.getChunkX()
							&& o.getChunkY() == player.getChunkY()
							&& o.getPlane() == player.getPlane())
					.forEach(World::removeObject);
			return true;
		}
		case "clearspot":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				target.getFarmingManager().resetSpots();
				player.getPackets().sendGameMessage(
						"You have cleared the target's spot.");
			}
			return true;
		case "clearall":// fail safe only
			for (Player p2 : World.getPlayers()) {
				if (p2 == null)
					continue;
				p2.getFarmingManager().resetSpots();
			}
			return true;
		case "reward":
			player.getPackets().sendGameMessage(
					"You have opened the dungeoneering shop.");
			DungeonRewardShop.openRewardShop(player);
			return true;
		case "getclipflag": {
			mask = World.getMask(player.getPlane(), player.getX(),
					player.getY());
			player.getPackets().sendGameMessage("[" + mask + "]");
			return true;
		}
		case "scbariertest": {
			int minX = (player.getChunkX() << 3) + Helper.BARRIER_MIN[0];
			int minY = (player.getChunkY() << 3) + Helper.BARRIER_MIN[1];
			int maxX = (player.getChunkX() << 3) + Helper.BARRIER_MAX[0];
			int maxY = (player.getChunkY() << 3) + Helper.BARRIER_MAX[1];

			World.spawnObject(new WorldObject(39615, 1, 1, new WorldTile(minX,
					minY, 0)));
			World.spawnObject(new WorldObject(39615, 1, 2, new WorldTile(minX,
					maxY, 0)));
			World.spawnObject(new WorldObject(39615, 1, 3, new WorldTile(maxX,
					maxY, 0)));
			World.spawnObject(new WorldObject(39615, 1, 0, new WorldTile(maxX,
					minY, 0)));

			for (int x = minX + 1; x <= maxX - 1; x++) {
				World.spawnObject(new WorldObject(39615, 0, 1, new WorldTile(x,
						minY, 0)));
				World.spawnObject(new WorldObject(39615, 0, 3, new WorldTile(x,
						maxY, 0)));
			}
			for (int y = minY + 1; y <= maxY - 1; y++) {
				World.spawnObject(new WorldObject(39615, 0, 2, new WorldTile(
						minX, y, 0)));
				World.spawnObject(new WorldObject(39615, 0, 0, new WorldTile(
						maxX, y, 0)));
			}
			return true;
		}
		case "startscblue": {
			boolean team = cmd[0].contains("red");
			List<Player> blue = new ArrayList<>();
			List<Player> red = new ArrayList<>();
			(team ? red : blue).add(player);
			StealingCreationManager.createGame(8, blue, red);
			return true;
		}
		case "hugemap":
			player.setForceNextMapLoadRefresh(true);
			player.setMapSize(3);
			return true;
		case "normmap":
			player.setForceNextMapLoadRefresh(true);
			player.setMapSize(0);
			return true;
		case "testmap":
			player.setForceNextMapLoadRefresh(true);
			player.setMapSize(5);
			return true;
		case "test":
			player.getInterfaceManager().sendMinigameInterface(316);
			player.getVarsManager().forceSendVar(3008, 1);
			return true;
		case "testscarea":
			int size = cmd.length < 2 ? 8 : Integer.parseInt(cmd[1]);
			GameArea area = new GameArea(size);
			area.calculate();
			area.create();
			player.setNextWorldTile(new WorldTile(area.getMinX(), area
					.getMinY(), 0));
			return true;
		case "sgar":
			player.getControllerManager().startController("SorceressGarden");
			return true;
		case "scg":
			player.getControllerManager().startController(
					"StealingCreationsGame", true);
			return true;
		case "gesearch":
			player.getInterfaceManager().setInterface(true, 752, 7, 389);
			player.getPackets().sendExecuteScriptReverse(570,
					"Grand Exchange Item Search");
			return true;
		case "ge":
			player.getGeManager().openGrandExchange();
			return true;
		case "ge2":
			player.getGeManager().openCollectionBox();
			return true;
		case "ge3":
			player.getGeManager().openHistory();
			return true;
		case "configsize":
			player.getPackets().sendGameMessage(
					"Config definitions size: 2633, BConfig size: 1929.");
			return true;
		case "npcmask":
			for (NPC n : World.getNPCs()) {
				if (n != null && Utils.getDistance(player, n) < 30) {
					n.setNextSecondaryBar(new SecondaryBar(Integer
							.parseInt(cmd[1]), Integer.parseInt(cmd[2]),
							Integer.parseInt(cmd[3]), Boolean
									.parseBoolean(cmd[4])));
				}
			}
			return true;
		case "runespan":
			player.getControllerManager().startController("RuneSpanController");
			return true;
		case "house":
			player.getHouse().enterMyHouse();
			return true;
		case "killingfields":
			player.getControllerManager().startController("KillingFields");
			return true;

		case "isprite":
			player.getPackets().sendIComponentSprite(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]));
			// player.getPackets().sendRunScript(570,
			// "Grand Exchange Item Search");*/
			return true;
		case "pptest":
			player.getDialogueManager().startDialogue("SimplePlayerMessage",
					"123");
			return true;
		case "sd":
			/*
			 * int v = Integer.valueOf(cmd[1]);
			 * player.getAppearence().setHairStyle(v);
			 * player.getAppearence().setTopStyle(v);
			 * player.getAppearence().setBootsStyle(v);
			 * player.getAppearence().setArmsStyle(v);
			 * player.getAppearence().setHandsStyle(v);
			 * player.getAppearence().setLegsStyle(v);
			 * player.getAppearence().setBeardStyle(v);
			 * player.getAppearence().generateAppearenceData();
			 */
			return true;

		case "debugobjects":
			Region r = World.getRegion(player.getRegionY()
					| (player.getRegionX() << 8));
			if (r == null) {
				player.getPackets().sendGameMessage("Region is null!");
				return true;
			}
			List<WorldObject> objects = r.getAllObjects();
			if (objects == null) {
				player.getPackets().sendGameMessage("Objects are null!");
				return true;
			}
			for (WorldObject o : objects) {
				if (o == null || !o.matches(player)) {
					continue;
				}
				System.out.println("Objects coords: " + o.getX() + ", "
						+ o.getY());
				System.out.println("[Object]: id=" + o.getId() + ", type="
						+ o.getType() + ", rot=" + o.getRotation() + ".");
			}
			return true;
		case "pickuppet":
			if (player.getPet() != null) {
				player.getPet().pickup();
				return true;
			}
			player.getPackets().sendGameMessage(
					"You do not have a pet to pickup!");
			return true;
		case "canceltask":
			name = "";
			for (int i = 1; i < cmd.length; i++) {
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			target = World.getPlayerByDisplayName(name);
			if (target != null)
				target.getSlayerManager().skipCurrentTask(false);
			return true;
		case "messagetest":
			player.getPackets().sendMessage(Integer.parseInt(cmd[1]), "YO",
					player);
			return true;
		case "restartfp":
			FightPits.endGame();
			player.getPackets().sendGameMessage("Fight pits restarted!");
			return true;
		case "modelid":
			int id = Integer.parseInt(cmd[1]);
			player.getPackets().sendMessage(
					99,
					"Model id for item " + id + " is: "
							+ ItemDefinitions.getItemDefinitions(id).modelId,
					player);
			return true;

		case "pos":
			try {
				File file = new File("data/positions.txt");
				BufferedWriter writer = new BufferedWriter(new FileWriter(file,
						true));
				writer.write("|| player.getX() == " + player.getX()
						+ " && player.getY() == " + player.getY() + "");
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;

		case "agilitytest":
			player.getControllerManager().startController("BrimhavenAgility");
			return true;

		case "partyroom":
			player.getInterfaceManager().sendCentralInterface(647);
			player.getInterfaceManager().sendInventoryInterface(336);
			player.getPackets().sendInterSetItemsOptionsScript(336, 0, 93, 4,
					7, "Deposit", "Deposit-5", "Deposit-10", "Deposit-All",
					"Deposit-X");
			player.getPackets().sendIComponentSettings(336, 0, 0, 27, 1278);
			player.getPackets().sendInterSetItemsOptionsScript(336, 30, 90, 4,
					7, "Value");
			player.getPackets().sendIComponentSettings(647, 30, 0, 27, 1150);
			player.getPackets().sendInterSetItemsOptionsScript(647, 33, 90, 4,
					7, "Examine");
			player.getPackets().sendIComponentSettings(647, 33, 0, 27, 1026);
			ItemsContainer<Item> store = new ItemsContainer<>(215, false);
			for (int i = 0; i < store.getSize(); i++) {
				store.add(new Item(1048, i));
			}
			player.getPackets().sendItems(529, true, store); // .sendItems(-1,
			// -2, 529,
			// store);

			ItemsContainer<Item> drop = new ItemsContainer<>(215, false);
			for (int i = 0; i < drop.getSize(); i++) {
				drop.add(new Item(1048, i));
			}
			player.getPackets().sendItems(91, true, drop);// sendItems(-1,
			// -2, 91,
			// drop);

			ItemsContainer<Item> deposit = new ItemsContainer<>(8, false);
			for (int i = 0; i < deposit.getSize(); i++) {
				deposit.add(new Item(1048, i));
			}
			player.getPackets().sendItems(92, true, deposit);// sendItems(-1,
			// -2, 92,
			// deposit);
			return true;

		case "objectname":
			name = cmd[1].replaceAll("_", " ");
			String option = cmd.length > 2 ? cmd[2] : null;
			List<Integer> loaded = new ArrayList<>();
			for (int x = 0; x < 12000; x += 2) {
				for (int y = 0; y < 12000; y += 2) {
					int regionId = y | (x << 8);
					if (!loaded.contains(regionId)) {
						loaded.add(regionId);
						r = World.getRegion(regionId, false);
						r.loadRegionMap();
						List<WorldObject> list = r.getAllObjects();
						if (list == null) {
							continue;
						}
						for (WorldObject o : list) {
							if (o.getDefinitions().name.equalsIgnoreCase(name)
									&& (option == null || o.getDefinitions()
											.containsOption(option))) {
								System.out.println("Object found - [id="
										+ o.getId() + ", x=" + o.getX()
										+ ", y=" + o.getY() + "]");
								// player.getPackets().sendGameMessage("Object
								// found - [id="
								// + o.getId() + ", x=" + o.getX() +
								// ", y="
								// + o.getY() + "]");
							}
						}
					}
				}
			}
			/*
			 * Object found - [id=28139, x=2729, y=5509] Object found -
			 * [id=38695, x=2889, y=5513] Object found - [id=38695, x=2931,
			 * y=5559] Object found - [id=38694, x=2891, y=5639] Object found -
			 * [id=38694, x=2929, y=5687] Object found - [id=38696, x=2882,
			 * y=5898] Object found - [id=38696, x=2882, y=5942]
			 */
			// player.getPackets().sendGameMessage("Done!");
			System.out.println("Done!");
			return true;

		case "msgtest":
			player.getPackets().sendGameMessage(Color.PURPLE,
					"Congratulations! You've just won a very rare prize!");
			return true;

		case "bork":
			player.getControllerManager().startController("BorkController");
			return true;

		case "killnpc":
			for (NPC n : World.getNPCs()) {
				if (n == null || n.getId() != Integer.parseInt(cmd[1]))
					continue;
				n.applyHit(new Hit(player, n.getMaxHitpoints(),
						HitLook.REGULAR_DAMAGE));
			}
			return true;
		case "sound":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
				return true;
			}
			try {
				player.getPackets().sendSoundEffect(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
			}
			return true;

		case "music":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::music musicid");
				return true;
			}
			try {
				player.getMusicsManager().playMusic(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::music musicid");
			}
			return true;
		case "testdialogue":
			player.getDialogueManager().startDialogue("DagonHai", 7137, player,
					Integer.parseInt(cmd[1]));
			return true;

		case "removenpcs":
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")
					|| player.getUsername().equalsIgnoreCase("nath")
					|| player.getUsername().equalsIgnoreCase("tom")
					|| player.getUsername().equalsIgnoreCase("pax"))
				for (NPC n : World.getNPCs()) {
					if (n.getId() == Integer.parseInt(cmd[1])) {
						n.reset();
						n.finish();
					}
				}
			else
				player.getPackets().sendGameMessage(
						"You must be a developer or higher to remove npcs");
			return true;
		case "resetkdr":
			player.setKillCount(0);
			player.setDeathCount(0);
			return true;

		case "newtut":
			player.getControllerManager().startController("TutorialIsland", 0);
			return true;

		case "removeController":
		case "forcestop":
		case "removecontroller":
		case "stopController":
		case "stopcontroller":
			player.getControllerManager().forceStop();
			player.getInterfaceManager().sendInterfaces();
			return true;

		case "nomads":
			for (Player p : World.getPlayers())
				p.getControllerManager().startController("NomadsRequiem");
			return true;

		case "give":
			StringBuilder sb = new StringBuilder(cmd[1]);
			int amount2 = 1;
			if (cmd.length > 2) {
				for (int i = 2; i < cmd.length; i++) {
					if (cmd[i].startsWith("+")) {
						amount2 = Integer.parseInt(cmd[i].replace("+", ""));
					} else {
						sb.append(" ").append(cmd[i]);
					}
				}
			}
			name = sb.toString().toLowerCase().replace("[", "(")
					.replace("]", ")").replaceAll(",", "'");
			for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
				ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
				if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
					if (def.getName().toLowerCase().equalsIgnoreCase("rp")) {
						player.getInventory().addItem(5733, 1);
						player.getInventory().addItem(i, amount2);
						player.getPackets().sendPanelBoxMessage(
								"Put " + def.getName().toLowerCase() + " (id: "
										+ i + ") - amount: " + amount2
										+ " in inv.");
						return true;
					}
					if (def.getName().toLowerCase().equalsIgnoreCase("coins")) {
						player.getMoneyPouch().sendDynamicInteraction(amount2,
								false);
						player.getPackets().sendPanelBoxMessage(
								"Put " + def.getName().toLowerCase()
										+ " (id: 995) - amount: " + amount2
										+ " in pouch.");
						return true;
					}
					player.getInventory().addItem(i, amount2);
					player.getPackets().sendPanelBoxMessage(
							"Put " + def.getName().toLowerCase() + " (id: " + i
									+ ") - amount: " + amount2 + " in inv.");
					return true;
				}
			}
			return true;

		case "givebank":
			StringBuilder sb2 = new StringBuilder(cmd[1]);
			int amount3 = 1;
			if (cmd.length > 2) {
				for (int i = 2; i < cmd.length; i++) {
					if (cmd[i].startsWith("+")) {
						amount3 = Integer.parseInt(cmd[i].replace("+", ""));
					} else {
						sb2.append(" ").append(cmd[i]);
					}
				}
			}
			name = sb2.toString().toLowerCase().replace("[", "(")
					.replace("]", ")").replaceAll(",", "'");
			for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
				ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
				if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
					player.getBank().addItem(i, amount3, true);
					player.getPackets().sendPanelBoxMessage(
							"Put " + def.getName().toLowerCase() + " (id: " + i
									+ ") - amount: " + amount3 + " in bank.");
					return true;
				}
			}
			return true;
		case "itembank":
			if (cmd.length < 2) {
				player.getPackets().sendGameMessage(
						"Use: ::itembank id (optional:amount)");
				return true;
			}
			try {
				int itemId = Integer.valueOf(cmd[1]);
				player.getBank().addItem(itemId,
						cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1, true);
				player.getPackets().sendPanelBoxMessage(
						"Put "
								+ itemId
								+ " - amount: "
								+ (cmd.length >= 3 ? Integer.valueOf(cmd[2])
										: 1) + " in bank.");
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage(
						"Use: ::itembank id (optional:amount)");
			}
			return true;

		case "copy":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player p2 = World.getPlayerByDisplayName(name);
			if (p2 == null) {
				player.getPackets().sendGameMessage(name + " is offline.");
				return true;
			}
			items = p2.getEquipment().getItems().getItemsCopy();
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null)
					continue;
				HashMap<Integer, Integer> requiriments = items[i]
						.getDefinitions().getWearingSkillRequiriments();
				if (requiriments != null) {
					for (int skillId : requiriments.keySet()) {
						if (skillId > 24 || skillId < 0)
							continue;
						int level = requiriments.get(skillId);
						if (level < 0 || level > 120)
							continue;
						if (player.getSkills().getLevelForXp(skillId) < level) {
							name = Skills.SKILL_NAME[skillId].toLowerCase();
							player.getPackets().sendGameMessage(
									"You need to have a"
											+ (name.startsWith("a") ? "n" : "")
											+ " " + name + " level of " + level
											+ ".");
						}

					}
				}
				player.getEquipment().getItems().set(i, items[i]);
				player.getEquipment().refresh(i);
			}
			player.getAppearence().generateAppearenceData();
			return true;
		case "prayertest":
			player.getEffectsManager().startEffect(
					new Effect(EffectType.PROTECTION_DISABLED, 8));
			return true;

		case "karamja":
			player.getDialogueManager().startDialogue(
					"KaramjaTrip",
					Utils.random(1) == 0 ? 11701
							: (Utils.random(1) == 0 ? 11702 : 11703));
			return true;
		case "clanwars":
			// player.setClanWars(new ClanWars(player, player));
			// player.getClanWars().setWhiteTeam(true);
			// ClanChallengeInterface.openInterface(player);
			return true;
		case "watereast":
			for (int i = 0; i < 10; i++) {
				World.spawnObjectTemporary(new WorldObject(37227, 10, 0,
						new WorldTile(player.getX() + i * 2, player.getY() + 1,
								player.getPlane())), 2000);
				World.spawnObjectTemporary(new WorldObject(37227, 10, 2,
						new WorldTile(player.getX() + i * 2, player.getY() - 4,
								player.getPlane())), 2000);
			}
			return true;
		case "dungsmall":
			player.getDungManager().leaveParty();
			DungeonPartyManager testParty = new DungeonPartyManager();
			testParty.add(player);
			testParty.setFloor(50);
			testParty.setComplexity(6);
			testParty.setDificulty(1);
			testParty.setKeyShare(true);
			testParty.setSize(DungeonConstants.LARGE_DUNGEON);
			testParty.start();
			return true;
		case "dung":
			player.getDungManager().leaveParty();
			DungeonPartyManager party = new DungeonPartyManager();
			party.add(player);
			party.setFloor(48);// 60
			party.setComplexity(6);
			party.setDificulty(1);
			party.setSize(DungeonConstants.TEST_DUNGEON);
			party.setKeyShare(true);
			party.start();
			return true;
		case "dungtest":
			party = player.getDungManager().getParty();
			for (Player p : World.getPlayers()) {
				if (p == player
						|| !p.hasStarted()
						|| p.hasFinished()
						|| !(p.getControllerManager().getController() instanceof Kalaboss))
					continue;
				p.getDungManager().leaveParty();
				party.add(p);
			}
			party.setFloor(1);
			party.setComplexity(6);
			party.setDificulty(party.getTeam().size());
			party.setSize(DungeonConstants.TEST_DUNGEON);
			party.setKeyShare(true);
			player.getDungManager().enterDungeon(false);
			return true;
		case "objects":
			for (int i = 0; i < 4; i++) {
				object = World.getObjectWithSlot(player, i);
				player.getPackets().sendPanelBoxMessage(
						"object: "
								+ (object == null ? ("null " + i) : ("id: "
										+ object.getId() + ", "
										+ object.getType() + ", " + object
										.getRotation())));
			}
			// int setting =
			// World.getRegion(player.getRegionId()).getSettings(player.getPlane(),
			// player.getXInRegion(), player.getYInChunk());
			player.getPackets().sendPanelBoxMessage(
					"setting: " + player.getXInRegion() + ", "
							+ player.getYInRegion() + ", "
							+ player.getRegionId());
			return true;
		case "checkdisplay":
			for (Player p : World.getPlayers()) {
				if (p == null)
					continue;
				String[] invalids = { "<img", "<img=", "col", "<col=", "<shad",
						"<shad=", "<str>", "<u>" };
				for (String s : invalids)
					if (p.getDisplayName().contains(s)) {
						player.getPackets().sendGameMessage(
								Utils.formatPlayerNameForDisplay(p
										.getUsername()));
					} else {
						player.getPackets().sendGameMessage("None exist!");
					}
			}
			return true;
		case "cutscene":
			player.getPackets().sendCutscene(Integer.parseInt(cmd[1]));
			return true;
		case "dzs":
			player.getCutscenesManager().play(new DZGuideScene());
			return true;
		case "noescape":
			player.getCutscenesManager().play(
					new NexCutScene(NexCombat.NO_ESCAPE_TELEPORTS[1], 1));
			return true;
		case "dungcoords":
			int chunkX = player.getX() / 16 * 2;
			int chunkY = player.getY() / 16 * 2;
			int x = player.getX() - chunkX * 8;
			int y = player.getY() - chunkY * 8;

			player.getPackets().sendPanelBoxMessage(
					"Room chunk : " + chunkX + ", " + chunkY + ", pos: " + x
							+ ", " + y);

			if (player.getDungManager().isInside()) {
				Room room = player
						.getDungManager()
						.getParty()
						.getDungeon()
						.getRoom(
								player.getDungManager().getParty().getDungeon()
										.getCurrentRoomReference(player));

				if (room != null) {
					int[] xy = DungeonManager.translate(x, y,
							(4 - room.getRotation()) & 0x3, 1, 1, 0);
					player.getPackets().sendPanelBoxMessage(
							"Dungeon Detected! Current rotation: "
									+ room.getRotation());
					player.getPackets().sendPanelBoxMessage(
							"Real Room chunk : " + room.getRoom().getChunkX()
									+ ", " + room.getRoom().getChunkY()
									+ ", real pos for rot0: " + xy[0] + ", "
									+ xy[1]);
				}
			}

			return true;

		case "itemoni":
			player.getPackets().sendItemOnIComponent(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]), 1);
			return true;

		case "items":
			for (int i = 0; i < 2000; i++) {
				player.getPackets().sendItems(i, new Item[] { new Item(i, 1) });
			}
			return true;

		case "giveitem":
			int itemId = Integer.parseInt(cmd[1]);
			int itemAmount = Integer.parseInt(cmd[2]);
			name = "";
			for (int i = 3; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);

			if (target == null) {
				player.getPackets().sendGameMessage("Player is offline.");
				return false;
			}

			target.getInventory().addItem(itemId, itemAmount);
			target.getPackets().sendGameMessage(
					"You have been given a "
							+ ItemDefinitions.getItemDefinitions(itemId)
									.getName() + " by "
							+ player.getDisplayName() + ".");
			player.getPackets().sendGameMessage(
					"You have sent "
							+ ItemDefinitions.getItemDefinitions(itemId)
									.getName() + " to "
							+ target.getDisplayName() + ".");

			return true;
		case "trade":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");

			target = World.getPlayerByDisplayName(name);
			if (target != null) {
				player.getTrade().openTrade(target);
				target.getTrade().openTrade(player);
			}
			return true;

		case "maxotherlevels":
			if (cmd.length < 4) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevel skillId level name");
				return false;
			}
			try {
				name = "";
				for (int i = 3; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 120) {
						player.getPackets().sendGameMessage(
								"Please choose a valid level.");
					}
					for (int i = 0; i < 25; i++) {
						if (i == 24 || i == 26) {
							target.getSkills().set(i, 120);
							target.getSkills().setXp(i,
									Skills.getXPForLevel(level));
						}
						target.getSkills().set(i, 99);
						target.getSkills()
								.setXp(i, Skills.getXPForLevel(level));

					}
					target.getAppearence().generateAppearenceData();
				} else {
					player.getPackets().sendGameMessage(
							"Unable to find " + name + ".");
				}
				return false;
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevel skillId level");
			}
			return true;

		case "setlevelother":
		case "setlevelp":
			if (cmd.length < 4) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevel skillId level name");
				return false;
			}
			try {
				name = "";
				for (int i = 3; i < cmd.length; i++)
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				target = World.getPlayerByDisplayName(name);
				if (target != null) {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 120) {
						player.getPackets().sendGameMessage(
								"Please choose a valid level.");
					}
					target.getSkills().set(skill, level);
					target.getSkills()
							.setXp(skill, Skills.getXPForLevel(level));
					target.getAppearence().generateAppearenceData();
					player.getPackets().sendGameMessage(
							"You have set " + skill + " to " + level + " for "
									+ target.getDisplayName() + ".");
				} else {
					player.getPackets().sendGameMessage(name + " is offline.");
				}
				return false;
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevelp skillId level name");
			}
			return true;

		case "setlevel":
			if (cmd.length < 3) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevel skillId level");
				return true;
			}
			try {
				int skill = Integer.parseInt(cmd[1]);
				int level = Integer.parseInt(cmd[2]);
				if (level < 0 || level > 99) {
					player.getPackets().sendGameMessage(
							"Please choose a valid level.");
					return true;
				}
				player.getSkills().set(skill, level);
				player.getSkills().setXp(skill, Skills.getXPForLevel(level));
				player.getAppearence().generateAppearenceData();
				player.getPackets().sendGameMessage(
						player.getSkills().getSkillName(skill) + " (" + skill
								+ ") to " + level + ".");
				return true;
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage(
						"Usage ::setlevel skillId level");
			}
			return true;

		case "gano":
		case "ganodermic":
			player.getInventory().addItem(22482, 1);
			player.getInventory().addItem(22490, 1);
			player.getInventory().addItem(22486, 1);
			player.getInventory().addItem(25978, 1);
			player.getInventory().addItem(25980, 1);
			player.getPackets().sendGameMessage("Put ganodermic set in inv.");
			return true;

		case "npcadd":
			name = cmd[1].toLowerCase().replace("[", "(").replace("]", ")")
					.replaceAll(",", "'");
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")
					|| player.getUsername().equalsIgnoreCase("nath")
					|| player.getUsername().equalsIgnoreCase("pax")
					|| player.getUsername().equalsIgnoreCase("tom"))
				for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
					NPCDefinitions def = NPCDefinitions.getNPCDefinitions(i);
					if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
						World.spawnNPC(i, player, -1, true, true);
						player.getPackets().sendPanelBoxMessage(
								"NPC: " + def.getName().toLowerCase()
										+ " (id: " + i + ") - at "
										+ player.getX() + ", " + player.getY()
										+ ", " + player.getPlane() + ".");
						return true;
					}
				}
			else
				player.getPackets().sendGameMessage(
						"You must be a developer or higher to add npcs.");
			return true;

		case "npc":
			try {
				if (Integer.parseInt(cmd[1]) == 19464) {
					new Araxxi(19464, player, 0, false, true, player);
					return true;
				}
				World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
				player.getPackets().sendGameMessage(
						"Npc: " + Integer.parseInt(cmd[1]) + " at "
								+ player.getX() + ", " + player.getY() + ", "
								+ player.getPlane() + ".", true);
				return true;
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::npc id(Integer)");
			}
			return true;

		case "loadwalls":
			WallHandler.loadWall(player.getCurrentFriendsChat().getClanWars());
			return true;

		case "cwbase":
			try {
				ClanWars cw = player.getCurrentFriendsChat().getClanWars();
				WorldTile base = cw.getBaseLocation();
				player.getPackets().sendGameMessage(
						"Base x=" + base.getX() + ", base y=" + base.getY());
				base = cw.getBaseLocation()
						.transform(
								cw.getAreaType().getNorthEastTile().getX()
										- cw.getAreaType().getSouthWestTile()
												.getX(),
								cw.getAreaType().getNorthEastTile().getY()
										- cw.getAreaType().getSouthWestTile()
												.getY(), 0);
				player.getPackets()
						.sendGameMessage(
								"Offset x=" + base.getX() + ", offset y="
										+ base.getY());
			} catch (Exception e) {
				e.printStackTrace();
				player.getPackets().sendGameMessage("error: " + e + ".");
			}
			return true;

		case "object":
		case "objectadd":
			try {
				int type = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 10;
				int rotation = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
				if (type > 22 || type < 0) {
					type = 10;
				}
				World.spawnObject(new WorldObject(Integer.valueOf(cmd[1]),
						type, rotation, player.getX(), player.getY(), player
								.getPlane()));
				player.getPackets().sendGameMessage(
						"Object: " + Integer.parseInt(cmd[1]) + " at "
								+ player.getX() + ", " + player.getY() + ", "
								+ player.getPlane() + ".", true);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: object id");
			}
			return true;
		case "ltab":
			player.getInterfaceManager().sendLockGameTab(
					Integer.valueOf(cmd[1]), true);
			return true;
		case "otab":
			player.getInterfaceManager().openGameTab(Integer.valueOf(cmd[1]));
			return true;
		case "tab":
			try {
				player.getInterfaceManager().setWindowInterface(
						Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: tab id inter");
			}
			return true;
		case "sethp":
			player.setHitpoints(Integer.valueOf(cmd[1]));
			return true;

		case "hidec":
			if (cmd.length < 4) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::hidec interfaceid componentId hidden");
				return true;
			}
			try {
				player.getPackets().sendHideIComponent(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[3]), Boolean.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::hidec interfaceid componentId hidden");
			}
			return true;

		case "string":
			try {
				player.getInterfaceManager().sendCentralInterface(
						Integer.valueOf(cmd[1]));
				for (int i = 0; i <= Integer.valueOf(cmd[2]); i++)
					player.getPackets().sendIComponentText(
							Integer.valueOf(cmd[1]), i, "child: " + i);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: string inter childid");
			}
			return true;

		case "gametab":
			player.getInterfaceManager().openGameTab(Integer.parseInt(cmd[1]));
			player.getPackets().sendGameMessage(
					"Gametab: " + Integer.parseInt(cmd[1]) + ".");
			return true;

		case "istringl":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}

			try {
				for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
					player.getPackets().sendCSVarString(i, "String " + i);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;

		case "istring":
			try {
				player.getPackets().sendCSVarString(Integer.valueOf(cmd[1]),
						"String " + Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: String id value");
			}
			return true;

		case "iconfig":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
					player.getPackets().sendCSVarInteger(
							Integer.parseInt(cmd[2]), i);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "nisvar":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: nisvar id value");
				return true;
			}
			try {
				player.getPackets().sendNISVar(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "var":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				player.getVarsManager().forceSendVar(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "forcemovement":
			WorldTile toTile = player.transform(0, 5, 0);
			player.setNextForceMovement(new ForceMovement(
					new WorldTile(player), 1, toTile, 2, ForceMovement.NORTH));

			return true;

		case "ab":
			player.getVarsManager().sendVar(727,
					(Integer.valueOf(cmd[1]) << 4 | 7));
			return true;

		case "varbit":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				player.getVarsManager().sendVarBit(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]));
				player.getPackets().sendGameMessage(
						"varBit: " + Integer.valueOf(cmd[1]) + " "
								+ Integer.valueOf(cmd[2]) + ".");
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;

		case "hit":
			HitLook.MELEE_DAMAGE.setMark(Integer.valueOf(cmd[1]));
			player.applyHit(new Hit(player, 300, HitLook.MELEE_DAMAGE, 0));

			return true;
		case "menu":
			/*
			 * player.getPackets().sendExecuteScript(8862, 0, 7);
			 * player.getPackets().sendExecuteScript(8862, 0, 8);
			 * player.getPackets().sendExecuteScript(8862, 1, 5);
			 */
			player.getInterfaceManager().openMenu(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]));
			return true;

		case "iloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++)
					player.getInterfaceManager().sendCentralInterface(i);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;

		case "tloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++)
					player.getInterfaceManager().setWindowInterface(i,
							Integer.valueOf(cmd[3]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "hloop":
			if (cmd.length < 5) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[2]); i < Integer
						.valueOf(cmd[3]); i++) {
					player.getPackets()
							.sendHideIComponent(Integer.valueOf(cmd[1]), i,
									Boolean.valueOf(cmd[4]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "varloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++) {
					player.getVarsManager().sendVar(i, Integer.valueOf(cmd[3]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "varloop2":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++) {
					player.getVarsManager().sendVar(i, i);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "varbitloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++)
					player.getVarsManager().sendVarBit(i,
							Integer.valueOf(cmd[3]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;
		case "objectanim":
			object = cmd.length == 4 ? World.getStandartObject(new WorldTile(
					Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player
							.getPlane())) : World.getObjectWithType(
					new WorldTile(Integer.parseInt(cmd[1]), Integer
							.parseInt(cmd[2]), player.getPlane()), Integer
							.parseInt(cmd[3]));
			if (object == null) {
				player.getPackets().sendPanelBoxMessage("No object was found.");
				return true;
			}
			player.getPackets().sendObjectAnimation(
					object,
					new Animation(Integer
							.parseInt(cmd[cmd.length == 4 ? 3 : 4])));
			return true;
		case "loopoanim":
			x = Integer.parseInt(cmd[1]);
			y = Integer.parseInt(cmd[2]);
			final WorldObject object1 = World.getObjectWithSlot(player,
					Region.OBJECT_SLOT_FLOOR);
			if (object1 == null) {
				player.getPackets().sendPanelBoxMessage(
						"Could not find object at [x=" + x + ", y=" + y
								+ ", z=" + player.getPlane() + "].");
				return true;
			}
			System.out.println("Object found: " + object1.getId());
			final int start = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 10;
			final int end = cmd.length > 4 ? Integer.parseInt(cmd[4]) : 20000;
			GameExecutorManager.fastExecutor.scheduleAtFixedRate(
					new TimerTask() {
						int current = start;

						@Override
						public void run() {
							while (AnimationDefinitions
									.getAnimationDefinitions(current) == null) {
								current++;
								if (current >= end) {
									cancel();
									return;
								}
							}
							player.getPackets().sendPanelBoxMessage(
									"Current object animation: " + current
											+ ".");
							player.getPackets().sendObjectAnimation(object1,
									new Animation(current++));
							if (current >= end) {
								cancel();
							}
						}
					}, 1800, 1800);
			return true;
		case "bconfigloop":
			if (cmd.length < 3) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
				return true;
			}
			try {
				for (int i = Integer.valueOf(cmd[1]); i < Integer
						.valueOf(cmd[2]); i++) {
					player.getPackets().sendCSVarInteger(i,
							Integer.valueOf(cmd[3]));
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: config id value");
			}
			return true;

		case "reset":
			if (cmd.length < 2) {
				for (int skill = 0; skill < Skills.SKILL_NAME.length; skill++) {
					player.getSkills().setXp(skill, 0);
					player.getSkills().set(skill, 1);
				}
				player.getSkills().init();
				return true;
			}
			try {
				player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
				player.getSkills().set(Integer.valueOf(cmd[1]), 1);

			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::master skill");
			}
			return true;
		case "build":
			player.getVarsManager().sendVar(483, 1024);
			player.getVarsManager().sendVar(483, 1025);
			player.getVarsManager().sendVar(483, 1026);
			player.getVarsManager().sendVar(483, 1027);
			player.getVarsManager().sendVar(483, 1028);
			player.getVarsManager().sendVar(483, 1029);
			player.getVarsManager().sendVar(483, 1030);
			player.getVarsManager().sendVar(483, 1031);
			player.getVarsManager().sendVar(483, 1032);
			player.getVarsManager().sendVar(483, 1033);
			player.getVarsManager().sendVar(483, 1034);
			player.getVarsManager().sendVar(483, 1035);
			player.getVarsManager().sendVar(483, 1036);
			player.getVarsManager().sendVar(483, 1037);
			player.getVarsManager().sendVar(483, 1038);
			player.getVarsManager().sendVar(483, 1039);
			player.getVarsManager().sendVar(483, 1040);
			player.getVarsManager().sendVar(483, 1041);
			player.getVarsManager().sendVar(483, 1042);
			player.getVarsManager().sendVar(483, 1043);
			player.getVarsManager().sendVar(483, 1044);
			player.getVarsManager().sendVar(483, 1045);
			player.getVarsManager().sendVar(483, 1024);
			player.getVarsManager().sendVar(483, 1027);
			player.getPackets().sendCSVarInteger(841, 0);
			player.getPackets().sendCSVarInteger(199, -1);
			player.getPackets().sendIComponentSettings(1306, 55, -1, -1, 0);
			player.getPackets().sendIComponentSettings(1306, 8, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 15, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 22, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 29, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 36, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 43, 4, 4, 1);
			player.getPackets().sendIComponentSettings(1306, 50, 4, 4, 1);
			System.out.println("Build");
			return true;
		case "givexp":
			String n = "";
			for (int i = 3; i < cmd.length; i++)
				n += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player t = World.getPlayerByDisplayName(n);
			assert t != null;
			t.getSkills().addXp(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]), true);
			player.getPackets().sendGameMessage(
					"Giving " + t.getDisplayName() + " "
							+ Integer.valueOf(cmd[2]) + " xp in "
							+ Integer.valueOf(cmd[1]));
			return true;
		case "pintest":
			player.getBank().setRecoveryTime(50000);
			return true;
		case "givetokens":
			String na = "";
			for (int i = 2; i < cmd.length; i++)
				na += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player ta = World.getPlayerByDisplayName(na);
			assert ta != null;
			ta.getDungManager().addTokens(Integer.valueOf(cmd[1]));
			player.getPackets().sendGameMessage(
					"Giving " + ta.getDisplayName() + " "
							+ Integer.valueOf(cmd[1]) + " tokens.");
			return true;
		case "addxp":
			for (int skill = 0; skill < 26; skill++)
				player.getSkills().addXp(skill, 1000000, true);
			return true;
		case "window":
			player.getInterfaceManager().setRootInterface(1143, false);
			return true;
		case "bconfig":
			if (cmd.length < 3) {
				player.getPackets()
						.sendPanelBoxMessage("Use: bconfig id value");
				return true;
			}
			try {
				player.getPackets().sendCSVarInteger(Integer.valueOf(cmd[1]),
						Integer.valueOf(cmd[2]));
			} catch (NumberFormatException e) {
				player.getPackets()
						.sendPanelBoxMessage("Use: bconfig id value");
			}
			return true;
		case "inter":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}
			try {
				if (Integer.valueOf(cmd[1]) > Utils
						.getInterfaceDefinitionsSize())
					return true;
				player.getInterfaceManager().sendCentralInterface(
						Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;
		case "pane":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::pane interfaceId");
				return true;
			}
			try {
				player.getPackets().sendRootInterface(Integer.valueOf(cmd[1]),
						0);
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::pane interfaceId");
			}
			return true;
		case "overlay":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}
			int child = cmd.length > 2 ? Integer.parseInt(cmd[2]) : 28;
			try {
				player.getInterfaceManager().setInterface(
						true,
						player.getInterfaceManager().hasRezizableScreen() ? 746
								: 548, child, Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;

		case "resetprices":
			player.getPackets().sendGameMessage("Starting!");
			GrandExchange.reset(true, false);
			player.getPackets().sendGameMessage("Done!");
			return true;
		case "recalcprices":
			player.getPackets().sendGameMessage("Starting!");
			GrandExchange.recalcPrices();
			player.getPackets().sendGameMessage("Done!");
			return true;

		case "interh2":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = Integer.valueOf(cmd[2]); componentId < Integer
						.valueOf(cmd[3]); componentId++) {
					player.getPackets().sendHideIComponent(interId,
							componentId, false);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;
		case "interh":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = 0; componentId < Utils
						.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					player.getPackets().sendHideIComponent(interId,
							componentId, false);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;
		case "intertrue":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = 0; componentId < Utils
						.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					player.getPackets().sendHideIComponent(interId,
							componentId, true);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;

		case "inters":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
				return true;
			}

			try {
				int interId = Integer.valueOf(cmd[1]);
				for (int componentId = 0; componentId < Utils
						.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
					player.getPackets().sendIComponentText(interId,
							componentId, "cid: " + componentId);
				}
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage(
						"Use: ::inter interfaceId");
			}
			return true;
		case "treasurehunter":
		case "sof":
			if (player.getUsername().equalsIgnoreCase("danny")
					|| player.getUsername().equalsIgnoreCase("charity")
					|| player.getUsername().equalsIgnoreCase("pax")) {
				if (cmd.length < 2) {
					player.getPackets().sendGameMessage(
							"Wrong, try '" + cmd[0] + " amount'.");
					return true;
				}
				player.majorDelay = System.currentTimeMillis();
				for (Player all : World.getPlayers()) {
					all.getTreasureHunter().handleEarnedKeys(
							Integer.parseInt(cmd[1]));
					all.getPackets().sendGameMessage(
							"<col=FF0000>Everyone online has been gifted with "
									+ Integer.parseInt(cmd[1])
									+ " Treasure Hunter keys by "
									+ player.getDisplayName() + ".");
					all.getPackets().sendGameMessage(
							"You have " + all.getTreasureHunter().getAllKeys()
									+ " Treasure Hunter keys.");
				}
			}
			return true;

		case "devnpc":
			if (Utils.random(8) >= 5)
				player.getAppearence().transformIntoNPC(9484);
			else if (Utils.random(5) >= 2)
				player.getAppearence().transformIntoNPC(13844);
			else
				player.getAppearence().transformIntoNPC(22900);
			return true;
		case "bank":
			player.getBank().openBank();
			return true;

		case "doublevotes":

			Settings.DOUBLE_VOTES = true;
			World.sendNews(player, "Double vote rewards is now active", 1, true);

			return true;
		case "endvotes":
			Settings.DOUBLE_VOTES = false;
			World.sendNews(
					player,
					"Double vote rewards have now expired! thank you to all those that voted!",
					1, false);
		case "anim":

		case "animation":
		case "emote":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				return true;
			}
			try {
				player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
				player.getPackets().sendGameMessage(
						cmd[0] + " to " + Integer.valueOf(cmd[1]) + ".");
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
			}
			return true;

		case "renderemote":
		case "remote":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				return true;
			}
			try {
				player.getAppearence().setRenderEmote(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::emote id");
			}
			return true;
		case "addgpr":
			player.getGamePointManager().setGamePoints(Integer.MAX_VALUE);
		case "quake":
			player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]),
					Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]),
					Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
			return true;

		case "getrender":
			player.getPackets().sendGameMessage("Testing renders");
			for (int i = 0; i < 3000; i++) {
				try {
					player.getAppearence().setRenderEmote(i);
					player.getPackets().sendGameMessage("Testing " + i);
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return true;

		case "setlook":
			PlayerLook.setSet(player, Integer.valueOf(cmd[1]));
			return true;
		case "color":
			PlayerLook.openCharacterCustomizing(player, true);
			return true;

		case "tryinter":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 1;

				@Override
				public void run() {
					if (player.hasFinished()) {
						stop();
					}
					player.getInterfaceManager().sendCentralInterface(i);
					System.out.println("Inter - " + i);
					i++;
				}
			}, 0, 1);
			return true;

		case "tryanim":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 16700;

				@Override
				public void run() {
					if (i >= Utils.getAnimationDefinitionsSize()) {
						stop();
						return;
					}
					if (player.getLastAnimationEnd() > Utils
							.currentTimeMillis()) {
						player.setNextAnimation(new Animation(-1));
					}
					if (player.hasFinished()) {
						stop();
					}
					player.setNextAnimation(new Animation(i));
					System.out.println("Anim - " + i);
					i++;
				}
			}, 0, 3);
			return true;

		case "animcount":
			System.out.println(Utils.getAnimationDefinitionsSize() + " anims.");
			return true;

		case "trygfx":
			WorldTasksManager.schedule(new WorldTask() {
				int i = 2100;

				@Override
				public void run() {
					if (i >= Utils.getGraphicDefinitionsSize()) {
						stop();
					}
					if (player.hasFinished()) {
						stop();
					}
					player.setNextGraphics(new Graphics(i));
					System.out.println("GFX - " + i);
					i++;
				}
			}, 0, 3);
			return true;
		case "gfx":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				return true;
			}
			try {
				player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1]),
						cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 0,
						cmd.length == 4 ? Integer.valueOf(cmd[3]) : 0));
				player.getPackets().sendGameMessage(
						"gfx: " + Integer.parseInt(cmd[1]) + ".");
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
			}
			return true;
		case "gfxp":
			if (cmd.length < 2) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				return true;
			}
			try {
				player.getPackets().sendGraphics(
						new Graphics(Integer.valueOf(cmd[1])),
						new WorldTile(player));
			} catch (NumberFormatException e) {
				player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
			}
			return true;
		case "sync":
			int animId = Integer.parseInt(cmd[1]);
			int gfxId = Integer.parseInt(cmd[2]);
			int height = cmd.length > 3 ? Integer.parseInt(cmd[3]) : 0;
			player.setNextAnimation(new Animation(animId));
			player.setNextGraphics(new Graphics(gfxId, 0, height));
			return true;
		case "staffmeeting":
		case "modmeeting":
		case "meeting":
			if (!JModTable.PMOD_MEETING) {
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					JModTable.PMOD_MEETING = true;
					staff.getPackets()
							.sendGameMessage(
									"<col=FF0000>A staff meeting has been requested by "
											+ player.getDisplayName()
											+ " please use the command ::accept to teleport.");
				}
			} else {
				for (Player staff : World.getPlayers()) {
					if (staff.getRights() == 0)
						continue;
					JModTable.PMOD_MEETING = false;

					staff.setNextWorldTile(new WorldTile(staff.getLastX(),
							staff.getLastY(), staff.getLastPlane()));

					staff.getPackets()
							.sendGameMessage(
									"<col=FF0000>The staff meeting has ended, you have been teleported to your last location.");
				}
			}
			return true;
		case "testsongfromthedepths":
			player.getPackets().sendGameMessage(
					"Starting song from the depths development version.");
			player.getControllerManager().startController("SongFromTheDepths");
			return true;
		case "testlucilledia":
			player.getDialogueManager().startDialogue("Lucille");
			return true;
		}
		return false;
	}

}
