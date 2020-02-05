package net.nocturne.utils.discord;

import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.game.World;
import net.nocturne.game.npc.Drop;
import net.nocturne.game.npc.Drops;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.activities.events.GlobalEvents;
import net.nocturne.game.player.content.activities.events.WorldEvents;
import net.nocturne.game.player.content.activities.minigames.warbands.Warbands;
import net.nocturne.utils.NPCDrops;
import net.nocturne.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miles Black (bobismyname)
 * @date Dec 25, 2016
 */

public class DiscordBot {

	private DiscordAPI api;
	private Server server;
	private Channel channel;
	private HashMap<User, Integer> warnings = new HashMap<>();
	final String token = "MjYyNjcxMjEyNDI4MjYzNDI1.C0G38A.Sfy3MgA2G0j4bccdzsEMvb7sDBI";
	final String serverId = "240615823524364288";

	public DiscordBot() {
		api = Javacord.getApi(token, true);
		api.connect(new FutureCallback<DiscordAPI>() {
			@Override
			public void onSuccess(DiscordAPI api)
					throws StringIndexOutOfBoundsException {
				server = api.getServerById(serverId);
				channel = api.getChannelById(serverId);
				api.registerListener((MessageCreateListener) (api1, message) -> {
					if (!message.isPrivateMessage())
						handleWarnings(message);
					if (message.getContent().charAt(0) != '.')
						return;
					Player player;
					String[] cmd = message.getContent().substring(1).split(" ");
					switch (cmd[0]) {
					case "players":
						message.getAuthor()
								.sendMessage(
										"There "
												+ (World.getPlayers().size() == 1 ? "is"
														: "are")
												+ " currently "
												+ World.getPlayers().size()
												+ " "
												+ (World.getPlayers().size() == 1 ? "person"
														: "people")
												+ " playing Nocturne RS3!");
						break;
					case "events":
						message.getAuthor()
								.sendMessage(
										"Current event information for Nocturne RS3:\n"
												+ "Evil Tree: "
												+ WorldEvents.EVIL_TREE_STATUS
												+ "\n"
												+ "Penguin H&S: Hint - "
												+ WorldEvents.PENGUIN_HS_STATUS
												+ "\n"
												+ "Harmonized Rocks: "
												+ WorldEvents.HARMONIZED_STATUS
												+ "\n"
												+ "Shooting Stars: Available - Check your house telescope for the location!\n"
												+ "Skiller's Dream: "
												+ (World.MODIFIER / 10)
												+ "x bonus for "
												+ Skills.SKILL_NAME[World.SKILL_ID]
												+ "!\n"
												+ "Wilderness Warbands: Time remaining - "
												+ (Warbands.warband == null ? "None"
														: Utils.getHoursMinsLeft(Warbands.warband.time))
												+ "\n"
												+ "Global Events: "
												+ GlobalEvents.getEvent()
														.getDescription());
						break;
					case "staff":
						List<String> moderators = new ArrayList<>(),
						administrators = new ArrayList<>(),
						developers = new ArrayList<>(),
						owners = new ArrayList<>();
						for (Player p : World.getPlayers()) {
							if (Settings.SERVER_OWNERS.contains(p.getUsername()
									.toLowerCase().replace(" ", "_")))
								owners.add(p.getUsername().substring(0, 1)
										.toUpperCase()
										+ p.getUsername().substring(1));
							else if (Settings.SERVER_DEVELOPERS.contains(p
									.getUsername().toLowerCase()
									.replace(" ", "_")))
								developers.add(p.getUsername().substring(0, 1)
										.toUpperCase()
										+ p.getUsername().substring(1));
							else if (p.getRights() == 1)
								moderators.add(p.getUsername().substring(0, 1)
										.toUpperCase()
										+ p.getUsername().substring(1));
							else if (p.getRights() == 2)
								administrators.add(p.getUsername()
										.substring(0, 1).toUpperCase()
										+ p.getUsername().substring(1));
						}
						int staffAmt = moderators.size()
								+ administrators.size() + developers.size()
								+ owners.size();
						message.getAuthor()
								.sendMessage(
										"There "
												+ (World.getPlayers().size() == 1 ? "is"
														: "are")
												+ " currently "
												+ staffAmt
												+ " staff "
												+ (staffAmt == 1 ? "member"
														: "members")
												+ " on Nocturne RS3!\n"
												+ "Owners: "
												+ owners.stream()
														.map(Object::toString)
														.collect(
																Collectors
																		.joining(", "))
														.toString()
												+ "\n"
												+ "Developers: "
												+ developers
														.stream()
														.map(Object::toString)
														.collect(
																Collectors
																		.joining(", "))
														.toString()
												+ "\n"
												+ "Administrators: "
												+ administrators
														.stream()
														.map(Object::toString)
														.collect(
																Collectors
																		.joining(", "))
														.toString()
												+ "\n"
												+ "Moderators: "
												+ moderators
														.stream()
														.map(Object::toString)
														.collect(
																Collectors
																		.joining(", "))
														.toString());
						break;
					case "stats":
						if (cmd.length < 2) {
							message.getAuthor()
									.sendMessage(
											"Use proper formatting: .stats <player_name>");
							break;
						}
						player = World.getPlayer(cmd[1].toLowerCase());
						if (player == null)
							message.getAuthor().sendMessage(
									"This player is currently offline.");
						else {
							String statsMessage = "Current stats for "
									+ (player.getUsername().substring(0, 1)
											.toUpperCase() + player
											.getUsername().substring(1)) + " ("
									+ player.getSkills().getCombatLevel()
									+ ")\n";
							for (int i = Skills.ATTACK; i <= Skills.DIVINATION; i++) {
								statsMessage += (Skills.SKILL_NAME[i]
										+ " - Level: "
										+ player.getSkills().getLevel(i)
										+ ", Exp: "
										+ Utils.format(player.getSkills()
												.getXp(i)) + "\n");
							}
							statsMessage += "Total Level: "
									+ Utils.format(player.getSkills()
											.getTotalLevel())
									+ ", Total Exp: "
									+ Utils.format(player.getSkills()
											.getTotalXp());
							message.getAuthor().sendMessage(statsMessage);
						}
						break;
					case "wealth":
						if (cmd.length < 2) {
							message.getAuthor()
									.sendMessage(
											"Use proper formatting: .wealth <player_name>");
							break;
						}
						player = World.getPlayer(cmd[1].toLowerCase());
						if (player == null)
							message.getAuthor().sendMessage(
									"This player is currently offline.");
						else {
							message.getAuthor()
									.sendMessage(
											"Current wealth of "
													+ (player.getUsername()
															.substring(0, 1)
															.toUpperCase() + player
															.getUsername()
															.substring(1))
													+ "\n"
													+ "Inventory: "
													+ Utils.format(player
															.getInventoryValue())
													+ "gp\n"
													+ "Money Pouch: "
													+ Utils.format(player
															.getMoneyPouch()
															.getCoinsAmount())
													+ "gp\n"
													+ "Equipment: "
													+ Utils.format(player
															.getEquipmentValue())
													+ "gp\n"
													+ "Bank: "
													+ Utils.format(player
															.getBankValue())
													+ "gp\n"
													+ "Total: "
													+ Utils.format(player
															.getTotalValue())
													+ "gp");
						}
						break;
					case "online":
						if (cmd.length < 2) {
							message.getAuthor()
									.sendMessage(
											"Use proper formatting: .online <player_name>");
							break;
						}
						player = World.getPlayer(cmd[1].toLowerCase());
						if (player == null)
							message.getAuthor().sendMessage(
									"This player is currently offline.");
						else
							message.getAuthor().sendMessage(
									"This player is currently online.");
						break;
					case "drops":
						if (cmd.length < 2) {
							message.getAuthor().sendMessage(
									"Use proper formatting: .drops <npc_id>");
							break;
						} else if (NPCDefinitions.getNPCDefinitions(Integer
								.parseInt(cmd[1])) == null) {
							message.getAuthor().sendMessage(
									"You have entered an invalid npc id.");
							break;
						}
						Drops drops = NPCDrops.getDrops(Integer
								.parseInt(cmd[1]));
						List<Drop> dropL = drops.getAllDrops();
						String dropText = "Drops for "
								+ NPCDefinitions.getNPCDefinitions(
										Integer.parseInt(cmd[1])).getName()
								+ " (" + cmd[1] + ")\n";
						for (Drop drop : dropL) {
							dropText += ItemDefinitions.getItemDefinitions(
									drop.getItemId()).getName()
									+ " Amount: "
									+ Utils.format(drop.getMinAmount())
									+ "-"
									+ Utils.format(drop.getMaxAmount()) + "\n";
						}
						dropText += "In addition to the global wealth drop table.";
						message.getAuthor().sendMessage(dropText);
						break;
					case "commands":
						message.getAuthor().sendMessage(
								"Current Nocturne RS3 commands:\n"
										+ ".players\n" + ".staff\n"
										+ ".events\n"
										+ ".online <player_name>\n"
										+ ".wealth <player_name>\n"
										+ ".stats <player_name>\n"
										+ ".drops <npc_id>");
						break;
					default:
						message.getAuthor()
								.sendMessage(
										"Invalid command, use .commands for a list of commands");
						break;
					}
				});
			}

			@Override
			public void onFailure(Throwable t) {
				System.out
						.println("ERROR 500: FAILURE TO CONNECT TO DISCORD CHANNEL!");
				t.printStackTrace();
			}
		});
	}

	public void handleWarnings(Message message) {
		if (message.getContent().contains("nigg")
				|| (message.getContent().contains("porch") && message
						.getContent().contains("monkey"))
				|| (message.getContent().contains("gang") && message
						.getContent().contains("scape"))) {
			warnings.putIfAbsent(message.getAuthor(), 0);
			warnings.put(message.getAuthor(),
					warnings.get(message.getAuthor()) + 1);
			String text = "You have been issued a warning for the following message:\n"
					+ message + "\n";
			switch (warnings.get(message.getAuthor())) {
			case 1:
				text += "This is a warning, the next offence is a kick, after that you will be banned.";
				break;
			case 2:
				channel.sendMessage(message.getAuthor().getName()
						+ " has been kicked from the server.");
				server.kickUser(message.getAuthor());
				text += "You have been kicked from the channel, one more warning and you will be banned.";
				break;
			case 3:
				channel.sendMessage(message.getAuthor().getName()
						+ " has been banned from the server.");
				server.banUser(message.getAuthor(), 1);
				text += "You have been banned from the channel for the next 24 hours.";
				break;
			}
			message.getAuthor().sendMessage(text);
			for (User user : server.getMembers())
				if (user.getRoles(server).contains(
						server.getRoleById("250050192227696641")))
					user.sendMessage(message.getAuthor().getName()
							+ " has been issued a warning for the following message:\n"
							+ message
							+ "\n"
							+ "They now have "
							+ warnings.get(message.getAuthor())
							+ " warning"
							+ (warnings.get(message.getAuthor()) == 1 ? ""
									: "s") + ".");
		}
	}

	public Server getServer() {
		return server;
	}

	public Channel getChannel() {
		return channel;
	}

	public Channel getChannel(String channelId) {
		return api.getChannelById(channelId);
	}

}
