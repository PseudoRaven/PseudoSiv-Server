package net.nocturne.game.player.content.commands;

import net.nocturne.Settings;
import net.nocturne.executor.PlayerHandlerThread;
import net.nocturne.game.Animation;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.JailController;
import net.nocturne.game.player.controllers.SpectateControler;
import net.nocturne.game.player.dialogues.impl.JModTable;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.LoginClientChannelManager;
import net.nocturne.network.LoginProtocol;
import net.nocturne.network.encoders.LoginChannelsPacketEncoder;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializationUtilities;
import net.nocturne.utils.Utils;

public class ModeratorCommands {

	static String name;
	static Player target;

	static boolean processCommand(final Player player, String[] cmd,
			boolean console, boolean clientCommand) {
		if (player.getRights() < 1)
			return false;
		switch (cmd[0].toLowerCase()) {
		case "jail":
		case "prison":
			if (cmd.length < 3)
				player.getPackets().sendGameMessage(
						"Please use the proper format - ::jail time user");
			final int time = Integer.valueOf(cmd[1]);
			name = "";
			for (int i = 2; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.setLastX(target.getX());
			target.setLastY(target.getY());
			target.setLastPlane(target.getPlane());

			player.setLastX(player.getX());
			player.setLastY(player.getY());
			player.setLastPlane(player.getPlane());

			target.lock(15);
			target.setNextForceTalk(new ForceTalk("HELP!!!!!!!!!!!!!!"));
			DeveloperConsole.performTeleEmote(target);
			final Player _target = target;
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(new WorldTile(2846, 5148, 0));
					_target.setNextAnimation(new Animation(-1));
					_target.setNextWorldTile(new WorldTile(2847, 5148, 0));
					JailController.imprison(_target, time);
				}
			}, 5);
			return true;
		case "unjail":
		case "unprison":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			target = World.getPlayerByDisplayName(name);
			assert target != null;
			target.unlock();
			target.setNextWorldTile(new WorldTile(target.getLastX(), target
					.getLastY(), target.getLastPlane()));
			player.setNextWorldTile(new WorldTile(player.getLastX(), player
					.getLastY(), player.getLastPlane()));
			player.getPackets().sendGameMessage(
					"Teleported " + target.getDisplayName()
							+ " to last location.");
			JailController.leaveJail(target, false);
			return true;
		case "accept":
			if (JModTable.PMOD_MEETING) {
				player.stopAll();

				player.setLastX(player.getX());
				player.setLastY(player.getY());
				player.setLastPlane(player.getPlane());

				player.setNextWorldTile(new WorldTile(2846, 5148, 0));
			} else {
				player.getPackets()
						.sendGameMessage(
								"There are no staff meetings being held at the moment.");
			}
			return true;
		case "unnull":
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
			}
			return true;
		case "interfaceloop":
			try {
				for (int i = 200; i < Utils.getInterfaceDefinitionsSize(); i++) {
					player.getInterfaceManager().sendCentralInterface(i);
					player.getPackets().sendGameMessage(
							"interfaceId: " + i + ".");
					Thread.sleep(400);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			player.getPackets()
					.sendGameMessage("type ::interfacestop to stop.");
			break;
		case "interfacedump":
			for (int i = 0; i < Utils
					.getInterfaceDefinitionsComponentsSize(Integer
							.parseInt(cmd[1])); i++) {
				player.getPackets().sendIComponentText(
						Integer.parseInt(cmd[1]), i, "" + i);
			}
			break;
		case "save":
		case "saveall":
			for (Player p : World.getPlayers()) {
				try {
					if (p == null || !p.hasStarted() || p.hasFinished())
						continue;

					byte[] data = SerializationUtilities.tryStoreObject(p);
					if (data == null || data.length <= 0)
						continue;
					GrandExchange.save();
					PlayerHandlerThread.addSave(p.getUsername(), data);
				} catch (Exception e) {
					Logger.log("Engine", "An error has occured: " + e);
					player.getPackets().sendGameMessage(e + ".");
				}
			}
			player.getPackets().sendGameMessage(
					"Successfully saved " + World.getPlayers().size()
							+ " players - it's been fixed btw...");
			return true;
		case "realnames":
			for (int i = 10; i < World.getPlayers().size() + 10; i++)
				player.getPackets().sendIComponentText(275, i, "");
			for (int i = 0; i < World.getPlayers().size() + 1; i++) {
				Player p2 = World.getPlayers().get(i);
				if (p2 == null)
					continue;
				player.getPackets().sendIComponentText(
						275,
						i + 10,
						p2.getDisplayName()
								+ " - "
								+ Utils.formatPlayerNameForDisplay(p2
										.getUsername()));
			}
			player.getPackets().sendIComponentText(275, 1,
					"Displayname - Username");
			player.getInterfaceManager().sendCentralInterface(275);
			return true;
		case "sy":
		case "staffyell":
			String message2 = "";
			for (int i = 1; i < cmd.length; i++)
				message2 += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			DeveloperConsole.sendYell(player, Utils.fixChatMessage(message2),
					true);
			return true;
		case "spectate":
			String username = "";
			for (int i = 1; i < cmd.length; i++)
				username += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			Player other = World.getPlayerByDisplayName(username);
			if (other == null) {
				player.getPackets().sendGameMessage(
						"The player " + username + " could not be found.");
				return true;
			}
			if (player.getControllerManager().getController() != null) {
				player.getPackets().sendGameMessage(
						"You cannot spectate at this location.");
				return true;
			}
			if (other.getControllerManager().getController() instanceof SpectateControler) {
				player.getPackets().sendGameMessage(
						"You cannot spectate someone who is spectating.");
				return true;
			}
			if (other == player) {
				player.getPackets().sendGameMessage(
						"You cannot spectate yourself.");
				return true;
			}
			player.getControllerManager().startController("SpectateControler",
					other);
			return true;
		case "stopspectate":
			if (!(player.getControllerManager().getController() instanceof SpectateControler)) {
				player.getPackets().sendGameMessage(
						"You can't stop spectating if you aren't spectating.");
				return true;
			}
			((SpectateControler) player.getControllerManager().getController())
					.stop();
			return true;
		case "mute":
		case "ban":
		case "ipmute":
		case "ipban":
		case "punish":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			name = Utils.formatPlayerNameForDisplay(name);
			player.getDialogueManager().startDialogue("AddOffenceD", name);
			return true;
		case "unmute":
			name = "";
			for (int i = 1; i < cmd.length; i++)
				name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			name = Utils.formatPlayerNameForDisplay(name);
			LoginClientChannelManager
					.sendUnreliablePacket(LoginChannelsPacketEncoder
							.encodeRemoveOffence(
									LoginProtocol.OFFENCE_REMOVETYPE_MUTES,
									name, player.getUsername()).getBuffer());
			player.getPackets().sendGameMessage(
					"You have unmuted the player " + name + ".");
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
			player.getPackets().sendGameMessage(
					"You have unmuted the player " + name + ".");
			return true;
		case "forcekick":
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
		case "kick":
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
			target.disconnect(true, false);
			player.getPackets().sendGameMessage(
					"You have kicked: " + target.getDisplayName() + ".");
			return true;
		}
		return false;
	}
}