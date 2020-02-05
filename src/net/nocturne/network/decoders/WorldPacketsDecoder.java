package net.nocturne.network.decoders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.cache.loaders.QuickChatOptionDefinition;
import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.actions.Bonds;
import net.nocturne.game.item.actions.Coalbag;
import net.nocturne.game.item.actions.RepairItems;
import net.nocturne.game.item.actions.RottenPotato;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.dagannoth.DagannothKing;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.familiar.Familiar.SpecialAttack;
import net.nocturne.game.npc.fightcaves.TzTok_Jad;
import net.nocturne.game.npc.fightkiln.HarAken;
import net.nocturne.game.npc.others.BarrowsBrother;
import net.nocturne.game.npc.others.DoorSupport;
import net.nocturne.game.npc.others.Legio;
import net.nocturne.game.npc.pet.Pets;
import net.nocturne.game.player.ActionBar;
import net.nocturne.game.player.ActionBar.DefenceAbilityShortcut;
import net.nocturne.game.player.ActionBar.HealAbilityShortcut;
import net.nocturne.game.player.ActionBar.ItemShortcut;
import net.nocturne.game.player.ActionBar.MagicAbilityShortcut;
import net.nocturne.game.player.ActionBar.MeleeAbilityShortcut;
import net.nocturne.game.player.ActionBar.PrayerShortcut;
import net.nocturne.game.player.ActionBar.RangeAbilityShortcut;
import net.nocturne.game.player.ActionBar.StrengthAbilityShortcut;
import net.nocturne.game.player.ChatMessage;
import net.nocturne.game.player.FarmingManager.FarmingSpot;
import net.nocturne.game.player.FarmingManager.SpotInfo;
import net.nocturne.game.player.InterfaceManager;
import net.nocturne.game.player.Inventory;
import net.nocturne.game.player.LogicPacket;
import net.nocturne.game.player.MoneyPouch;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.PublicChatMessage;
import net.nocturne.game.player.QuickChatMessage;
import net.nocturne.game.player.RouteEvent;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.player.actions.PlayerCombat;
import net.nocturne.game.player.actions.PlayerFollow;
import net.nocturne.game.player.actions.skills.construction.House;
import net.nocturne.game.player.actions.skills.construction.TabletMaking;
import net.nocturne.game.player.actions.skills.crafting.AccessorySmithing;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonRewardShop;
import net.nocturne.game.player.actions.skills.firemaking.Firemaking;
import net.nocturne.game.player.actions.skills.firemaking.Firemaking.Fire;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.FriendsChat;
import net.nocturne.game.player.content.Lottery;
import net.nocturne.game.player.content.Shop;
import net.nocturne.game.player.content.SkillCapeCustomizer;
import net.nocturne.game.player.content.TicketSystem;
import net.nocturne.game.player.content.TicketSystem.TicketEntry;
import net.nocturne.game.player.content.activities.XPWell;
import net.nocturne.game.player.content.activities.clans.ClansManager;
import net.nocturne.game.player.content.activities.minigames.Sawmill;
import net.nocturne.game.player.content.activities.minigames.Sawmill.Plank;
import net.nocturne.game.player.content.activities.minigames.clanwars.ClanWars;
import net.nocturne.game.player.content.activities.minigames.duel.DuelArena;
import net.nocturne.game.player.content.activities.minigames.duel.DuelController;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationController;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationLobbyController;
import net.nocturne.game.player.content.commands.DeveloperConsole;
import net.nocturne.game.player.content.reports.PlayerReporting;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.player.controllers.DungeonController;
import net.nocturne.game.player.controllers.SawmillController;
import net.nocturne.game.player.dialogues.impl.CustomTitle;
import net.nocturne.game.player.dialogues.impl.EnchantingOrbsDialogue;
import net.nocturne.game.player.dialogues.impl.RiseOfTheSixD;
import net.nocturne.game.player.dialogues.impl.SkillAlchemist;
import net.nocturne.game.player.dialogues.impl.cities.taverly.PetShopOwner;
import net.nocturne.game.route.RouteFinder;
import net.nocturne.game.route.strategy.FixedTileStrategy;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.LoginClientChannelManager;
import net.nocturne.network.LoginProtocol;
import net.nocturne.network.Session;
import net.nocturne.network.decoders.handlers.ButtonHandler;
import net.nocturne.network.decoders.handlers.InventoryOptionsHandler;
import net.nocturne.network.decoders.handlers.NPCHandler;
import net.nocturne.network.decoders.handlers.ObjectHandler;
import net.nocturne.network.encoders.LoginChannelsPacketEncoder;
import net.nocturne.stream.InputStream;
import net.nocturne.utils.Censor;
import net.nocturne.utils.Logger;
import net.nocturne.utils.SerializableFilesManager;
import net.nocturne.utils.Utils;
import net.nocturne.utils.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[123];

	private final static int WALKING_PACKET = 11;
	private final static int MINI_WALKING_PACKET = 44;
	public final static int ACTION_BUTTON1_PACKET = 50;
	public final static int ACTION_BUTTON2_PACKET = 72;
	public final static int ACTION_BUTTON3_PACKET = 14;
	public final static int ACTION_BUTTON4_PACKET = 18;
	public final static int ACTION_BUTTON5_PACKET = 31;
	public final static int ACTION_BUTTON6_PACKET = 45;
	public final static int ACTION_BUTTON7_PACKET = 62;
	public final static int ACTION_BUTTON8_PACKET = 21;
	public final static int ACTION_BUTTON9_PACKET = 23;
	public final static int ACTION_BUTTON10_PACKET = 4;
	public final static int WORLD_MAP_CLICK = 37;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 15;
	private final static int MOVE_CAMERA_PACKET = 79;
	private final static int INTERFACE_ON_OBJECT = 107;
	private final static int CLICK_PACKET = 73;
	private final static int CLICK_PACKET_2 = 27;
	private final static int MOUVE_MOUSE_PACKET = 53;
	private final static int MOUSE_MOVEMENT_DELAY = 116;
	private final static int KEY_TYPED_PACKET = 86;
	private final static int CLOSE_INTERFACE_PACKET = 56;
	private final static int COMMANDS_PACKET = 109;
	private final static int INTERFACE_ON_INTERFACE = 102;
	private final static int IN_OUT_SCREEN_PACKET = 25;
	private final static int DONE_LOADING_REGION_PACKET = 76;
	private final static int PING_PACKET = 119;
	private final static int SCREEN_PACKET = 20;
	private final static int CHAT_TYPE_PACKET = 2;
	private final static int CHAT_PACKET = 87;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 71;
	private final static int ADD_FRIEND_PACKET = 9;
	private final static int ADD_IGNORE_PACKET = 80;
	private final static int REMOVE_IGNORE_PACKET = 96;
	private final static int JOIN_FRIEND_CHAT_PACKET = 92;
	private final static int SET_FC_RANK_PACKET = 103;
	private final static int CHANGE_FRIEND_CHAT_PACKET = -1;
	private final static int KICK_FRIEND_CHAT_PACKET = 97;
	private final static int KICK_CLAN_CHAT_PACKET = -1;
	private final static int REMOVE_FRIEND_PACKET = 75;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 58;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 33;
	private final static int OBJECT_CLICK1_PACKET = 36;
	private final static int OBJECT_CLICK2_PACKET = 39;
	private final static int OBJECT_CLICK3_PACKET = 40;
	private final static int OBJECT_CLICK4_PACKET = 41;
	private final static int OBJECT_CLICK5_PACKET = 90;
	private final static int OBJECT_EXAMINE_PACKET = 29;
	private final static int NPC_CLICK1_PACKET = 59;
	private final static int ATTACK_NPC_PACKET = 65;
	private final static int NPC_CLICK2_PACKET = 52;
	private final static int NPC_CLICK3_PACKET = 61;
	private final static int NPC_CLICK4_PACKET = 88;
	private static final int NPC_EXAMINE_PACKET = 38;
	private final static int PLAYER_OPTION_1_PACKET = 100;
	private final static int PLAYER_OPTION_2_PACKET = 68;
	private final static int PLAYER_OPTION_3_PACKET = 49;
	private final static int PLAYER_OPTION_4_PACKET = 17;
	private final static int PLAYER_OPTION_6_PACKET = 67;
	private final static int PLAYER_OPTION_7_PACKET = 7;
	private final static int PLAYER_OPTION_9_PACKET = 78;
	private final static int ITEM_TAKE_PACKET = 26;
	private final static int GROUND_ITEM_OPTION_2 = 94;
	private final static int GROUND_ITEM_EXAMINE = 0;
	private final static int DIALOGUE_CONTINUE_PACKET = 113;
	private final static int ENTER_INTEGER_PACKET = 81;
	private final static int ENTER_NAME_PACKET = 114;
	private final static int ENTER_LONG_TEXT_PACKET = 111;
	private final static int SWITCH_INTERFACE_COMPONENTS_PACKET = 12;
	private final static int INTERFACE_ON_PLAYER = 85;
	private final static int INTERFACE_ON_NPC = 32;
	private final static int COLOR_ID_PACKET = 91;
	private static final int FORUM_THREAD_ID_PACKET = 42;
	private final static int OPEN_URL_PACKET = -1;
	private final static int REPORT_ABUSE_PACKET = -1;
	private final static int GRAND_EXCHANGE_ITEM_SELECT_PACKET = 34;
	private final static int WORLD_LIST_UPDATE = 108;
	private final static int UPDATE_GAMEBAR_PACKET = 83;
	private final static int NIS_VAR_PACKET = 46;
	private final static int MUSIC_PACKET = 82;
	private final static int REQUEST_PLAY_MUSIC_PACKET = 93;
	private final static int CUTSCENE_DONE_PACKET = 19;
	private final static int GARBAGE_CLEAR_PACKET = 8;

	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		PACKET_SIZES[0] = 7;
		PACKET_SIZES[1] = 15;
		PACKET_SIZES[2] = 1;
		PACKET_SIZES[3] = -1;
		PACKET_SIZES[4] = 8;
		PACKET_SIZES[5] = -1;
		PACKET_SIZES[6] = 0;
		PACKET_SIZES[7] = 3;
		PACKET_SIZES[8] = 4;
		PACKET_SIZES[9] = -1;
		PACKET_SIZES[10] = -1;
		PACKET_SIZES[11] = 5;
		PACKET_SIZES[12] = 16;
		PACKET_SIZES[13] = -1;
		PACKET_SIZES[14] = 8;
		PACKET_SIZES[15] = 4;
		PACKET_SIZES[16] = -1;
		PACKET_SIZES[17] = 3;
		PACKET_SIZES[18] = 8;
		PACKET_SIZES[19] = 1;
		PACKET_SIZES[20] = 6;
		PACKET_SIZES[21] = 8;
		PACKET_SIZES[22] = 0;
		PACKET_SIZES[23] = 8;
		PACKET_SIZES[24] = 7;
		PACKET_SIZES[25] = 1;
		PACKET_SIZES[26] = 7;
		PACKET_SIZES[27] = 7;
		PACKET_SIZES[28] = 7;
		PACKET_SIZES[29] = 9;
		PACKET_SIZES[30] = -2;
		PACKET_SIZES[31] = 8;
		PACKET_SIZES[32] = 11;
		PACKET_SIZES[33] = -1;
		PACKET_SIZES[34] = 2;
		PACKET_SIZES[35] = 2;
		PACKET_SIZES[36] = 9;
		PACKET_SIZES[37] = 4;
		PACKET_SIZES[38] = 3;
		PACKET_SIZES[39] = 9;
		PACKET_SIZES[40] = 9;
		PACKET_SIZES[41] = 9;
		PACKET_SIZES[42] = -1;
		PACKET_SIZES[43] = 3;
		PACKET_SIZES[44] = 18;
		PACKET_SIZES[45] = 8;
		PACKET_SIZES[46] = -2;
		PACKET_SIZES[47] = 12;
		PACKET_SIZES[48] = -2;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[50] = 8;
		PACKET_SIZES[51] = -2;
		PACKET_SIZES[52] = 3;
		PACKET_SIZES[53] = -1;
		PACKET_SIZES[54] = 3;
		PACKET_SIZES[55] = -1;
		PACKET_SIZES[56] = 0;
		PACKET_SIZES[57] = 0;
		PACKET_SIZES[58] = -2;
		PACKET_SIZES[59] = 3;
		PACKET_SIZES[60] = 1;
		PACKET_SIZES[61] = 3;
		PACKET_SIZES[62] = 8;
		PACKET_SIZES[63] = 4;
		PACKET_SIZES[64] = 15;
		PACKET_SIZES[65] = 3;
		PACKET_SIZES[66] = -1;
		PACKET_SIZES[67] = 3;
		PACKET_SIZES[68] = 3;
		PACKET_SIZES[69] = 9;
		PACKET_SIZES[70] = 7;
		PACKET_SIZES[71] = -1;
		PACKET_SIZES[72] = 8;
		PACKET_SIZES[73] = 6;
		PACKET_SIZES[74] = -2;
		PACKET_SIZES[75] = -1;
		PACKET_SIZES[76] = 4;
		PACKET_SIZES[77] = 1;
		PACKET_SIZES[78] = 3;
		PACKET_SIZES[79] = 4;
		PACKET_SIZES[80] = -1;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[82] = 4;
		PACKET_SIZES[83] = -2;
		PACKET_SIZES[84] = 3;
		PACKET_SIZES[85] = 11;
		PACKET_SIZES[86] = -2;
		PACKET_SIZES[87] = -1;
		PACKET_SIZES[88] = 3;
		PACKET_SIZES[89] = 5;
		PACKET_SIZES[90] = 9;
		PACKET_SIZES[91] = 2;
		PACKET_SIZES[92] = -1;
		PACKET_SIZES[93] = 4;
		PACKET_SIZES[94] = 7;
		PACKET_SIZES[95] = -1;
		PACKET_SIZES[96] = -1;
		PACKET_SIZES[97] = -1;
		PACKET_SIZES[98] = -1;
		PACKET_SIZES[99] = 9;
		PACKET_SIZES[100] = 3;
		PACKET_SIZES[101] = 3;
		PACKET_SIZES[102] = 16;
		PACKET_SIZES[103] = -1;
		PACKET_SIZES[104] = -2;
		PACKET_SIZES[105] = -1;
		PACKET_SIZES[106] = 0;
		PACKET_SIZES[107] = 17;
		PACKET_SIZES[108] = 4;
		PACKET_SIZES[109] = -1;
		PACKET_SIZES[110] = 18;
		PACKET_SIZES[111] = -1;
		PACKET_SIZES[112] = -2;
		PACKET_SIZES[113] = 6;
		PACKET_SIZES[114] = -1;
		PACKET_SIZES[115] = -1;
		PACKET_SIZES[116] = -1;
		PACKET_SIZES[117] = 1;
		PACKET_SIZES[118] = -2;
		PACKET_SIZES[119] = 0;
		PACKET_SIZES[120] = -1;
		PACKET_SIZES[121] = -2;
		PACKET_SIZES[122] = 4;
	};

	private Player player;
	private int chatType;

	// temp spam protection
	private int[] pthrotlecounter = new int[256];
	private long[] pthrotletimer = new long[256];

	// temp spam protection

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public int decode(InputStream stream) {
		while (stream.getRemaining() > 0 && !player.hasFinished()) {

			int start = stream.getOffset();
			int opcode = stream.readPacket(player);
			if (opcode < 0 || opcode >= PACKET_SIZES.length) {
				if (Settings.DEBUG)
					System.out.println("Invalid opcode: " + opcode + ".");
				return -1; // drop
			}

			int length = PACKET_SIZES[opcode];
			if ((length == -1 && stream.getRemaining() < 1)
					|| (length == -2 && stream.getRemaining() < 2))
				return start;

			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Invalid size for PacketId " + opcode
						+ ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("PacketId " + opcode
						+ " has fake size. - expected size " + length);
				break;
			}

			byte[] data = new byte[length];
			stream.readBytes(data);
			try {
				processPackets(opcode, new InputStream(data));
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
		return stream.getOffset();
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int opcode = packet.getId();
		InputStream stream = packet.getStream();
		if (opcode == WALKING_PACKET || opcode == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked() || player.isCantWalk() || player.isStunned())
				return;
			if (player.isBound()) {
				player.getPackets().sendGameMessage(
						"A magical force prevents you from moving.");
				return;
			}
			int toX = stream.readUnsignedShort();
			int toY = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			player.stopAll();
			if (forceRun)
				player.setRun(forceRun);
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER,
					player.getX(), player.getY(), player.getPlane(),
					player.getSize(), new FixedTileStrategy(toX, toY), true);
			int[] bufferX = RouteFinder.getLastPathBufferX();
			int[] bufferY = RouteFinder.getLastPathBufferY();
			int last = -1;
			for (int i = steps - 1; i >= 0; i--) {
				if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
					break;
				last = i;
			}

			if (last != -1) {
				WorldTile tile = new WorldTile(bufferX[last], bufferY[last],
						player.getPlane());
				player.getPackets().sendMinimapFlag(tile.getXInScene(player),
						tile.getYInScene(player));
			} else {
				player.getPackets().sendResetMinimapFlag();
			}
		} else if (opcode == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote())
				return;
			int interfaceHash = stream.readInt();
			final int itemId = stream.readUnsignedShort128();
			final boolean forceRun = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShortLE128();
			final int slot = stream.readUnsignedShort();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash & 0xFF;
			if (Settings.DEBUG)
				System.out.println("interface on player - player index:"
						+ playerIndex + ", inter " + interfaceId + ", "
						+ componentId + ", " + slot + ", " + itemId);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			final Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll();
			if (forceRun)
				player.setRun(forceRun);

			switch (interfaceId) {
			case 1430:
				if (componentId >= 55 && componentId <= 229)
					player.getActionbar().pushShortcutOnSomething(
							(componentId - 57) / 13, p2);
				break;
			case 1110:
			case 1140:
			case 234:
				if ((interfaceId == 1110 && componentId == 16)
						|| (interfaceId == 1140 && componentId == 151)
						|| (interfaceId == 234 && componentId == 10))
					player.getPackets().sendGameMessage("debugging");
				ClansManager manager = player.getClanManager();
				if (manager == null) {
					player.getPackets().sendGameMessage(
							"You must be in a clan to do that.");
					return;
				}
				synchronized (manager) {
					if (!manager.hasRankToInvite(player)) {
						player.getPackets().sendGameMessage(
								"You don't have permissions to invite.");
						return;
					}
					if (manager.getClan().getBannedUsers()
							.contains(p2.getUsername())) {
						player.getPackets().sendGameMessage(
								"This player has been banned from this clan.");
						return;
					}
					/*
					 * if (manager.clan.getMembers().size() >= Clan.MAX_MEMBERS)
					 * { player.getPackets().sendGameMessage(
					 * "Clans can't have over 500 members."); return; }
					 */
				}
				if (p2.getClanManager() != null) {
					player.getPackets().sendGameMessage(
							"This player is already a member of another clan.");
					return;
				}
				if (p2.getInterfaceManager().containsScreenInterface()
						|| p2.getControllerManager().getController() != null
						&& p2.getControllerManager().getController() instanceof StealingCreationLobbyController) {
					player.getPackets().sendGameMessage(
							"The other player is busy.");
					return;
				}
				player.getPackets().sendGameMessage(
						"Sending " + p2.getDisplayName() + " a invitation...");
				p2.getPackets().sendClanInviteMessage(player);
				p2.getTemporaryAttributtes().put("claninvite", Boolean.TRUE);
				break;
			case Inventory.INVENTORY_INTERFACE:
			case Inventory.INVENTORY_INTERFACE_2:
				final Item item = player.getInventory().getItem(slot);
				if (item == null || item.getId() != itemId)
					return;
				player.setRouteEvent(new RouteEvent(p2, new Runnable() {
					@Override
					public void run() {
						if (!player.getControllerManager().processItemOnPlayer(
								p2, item, slot))
							return;
						if (itemId == ItemIdentifiers.BOND)
							Bonds.useOnPlayer(player, p2);
						if (itemId == ItemIdentifiers.BOND_UNTRADEABLE)
							player.getPackets()
									.sendGameMessage(
											"You must convert this bond in order to use it on a player.");
						if (itemId == 5733)
							RottenPotato.useOnPlayer(player, p2);
						if (itemId == 4155)
							player.getSlayerManager().invitePlayer(p2);
						if (itemId == 962) {
							if (player.getInventory().getFreeSlots() < 3
									|| p2.getInventory().getFreeSlots() < 3) {
								player.getPackets()
										.sendGameMessage(
												(player.getInventory()
														.getFreeSlots() < 3 ? "You do"
														: "The other player does")
														+ " not have enough inventory space to open this cracker.");
								return;
							}
							if (player.getInventory().containsItem(962, 1)) {
								player.getDialogueManager().startDialogue(
										"ChristmasCrackerD", p2, itemId);
							}
						}
					}
				}));
				break;
			case 662:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 662 && componentId == 151)
						|| (interfaceId == 662 && componentId == 38)) {
					if ((interfaceId == 662 && componentId == 151)
							|| (interfaceId == 662 && componentId == 38)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets()
								.sendGameMessage(
										"You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.getPackets()
								.sendGameMessage(
										"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 38
										|| interfaceId == 662
										&& componentId == 110);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 1461:
				if (componentId == 1)
					player.getActionbar().useAbility(
							new MagicAbilityShortcut(slot), p2);
				// Magic.handleSpellOnEntity(player, interfaceSlot, p2);
				break;
			case 1449:
				if (componentId == 1)
					player.getActionbar()
							.useAbility(
									player.getCombatDefinitions()
											.onDefenceMenu() ? new DefenceAbilityShortcut(
											slot) : new HealAbilityShortcut(
											slot), p2);
				break;
			case 1460:
				if (componentId == 1)
					player.getActionbar()
							.useAbility(
									player.getCombatDefinitions()
											.onStrengthMenu() ? new StrengthAbilityShortcut(
											slot) : new MeleeAbilityShortcut(
											slot), p2);
				break;

			case 1452:
				if (componentId == 1)
					player.getActionbar().useAbility(
							new RangeAbilityShortcut(slot), p2);
				break;
			}
		} else if (opcode == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote())
				return;

			int interfaceSlot = stream.readUnsignedShortLE();
			int interfaceSlot2 = stream.readUnsignedShort128();
			boolean forceRun = stream.readByteC() == 1;
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntV2();

			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);

			if (Settings.DEBUG)
				System.out.println("interface on npc - player index:"
						+ npcIndex + ", inter " + interfaceId + ", "
						+ componentId + ", " + interfaceSlot + ", "
						+ interfaceSlot2);

			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			player.stopAll();
			if (forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case 1430:
				if (componentId >= 55 && componentId <= 229)
					player.getActionbar().pushShortcutOnSomething(
							(componentId - 57) / 13, npc);
				if (componentId == 25) {
					if (player.getFamiliar() == null)
						return;
					player.resetWalkSteps();
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage(
									"You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar()
								.canAttack(familiar.getOwner())) {
							player.getPackets()
									.sendGameMessage(
											"You can only attack players in a player-vs-player area.");
							return;
						}
					} else if (!npc.getDefinitions().hasAttackOption()) {
						player.getPackets().sendGameMessage(
								"You can't attack this npc.");
						return;
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets()
								.sendGameMessage(
										"You can only use your familiar in a multi-zone area.");
						return;
					}
				}
				break;
			case Inventory.INVENTORY_INTERFACE:
			case Inventory.INVENTORY_INTERFACE_2:
				Item item = player.getInventory().getItem(interfaceSlot);
				if (item == null
						|| !player.getControllerManager().processItemOnNPC(npc,
								item))
					return;
				else if (npc instanceof Familiar) {
					Familiar familiar = (Familiar) npc;
					if (familiar != player.getFamiliar()) {
						player.getPackets().sendGameMessage(
								"This is not your familiar!");
						return;
					}
				}
				NPCHandler.handleItemOnNPC(player, npc, interfaceSlot, item);
				break;
			case 1165:
				/*
				 * if (componentId == 3) { if
				 * (!player.getControllerManager().canAttack(npc)) {
				 * player.getInterfaceManager().closeInventory(); return; } else
				 * if (player.getAttackedBy() == null) {
				 * player.getPackets().sendGameMessage(
				 * "You need to have a target in order to deploy a dreadnip.");
				 * player.getInterfaceManager().closeInventory(); return; }
				 * player.getInventory().deleteItem(22370, 1); Dreadnip dread =
				 * new Dreadnip(player, Utils.getFreeTile(player, 2), -1, true);
				 * dread.getCombat().setTarget(dread.getTarget().getAttackedBy()
				 * ); }
				 */
				break;
			case 662:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if (interfaceId == 662 && componentId == 38
						|| componentId == 151) {
					if (interfaceId == 662 && componentId == 38
							|| componentId == 151) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage(
									"You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar()
								.canAttack(familiar.getOwner())) {
							player.getPackets()
									.sendGameMessage(
											"You can only attack players in a player-vs-player area.");
							return;
						}
					} else if (!npc.getDefinitions().hasAttackOption()) {
						player.getPackets().sendGameMessage(
								"You can't attack this npc.");
						return;
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets()
								.sendGameMessage(
										"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && componentId == 38
										|| componentId == 151);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 1461:
				if (componentId == 1)
					player.getActionbar().useAbility(
							new MagicAbilityShortcut(interfaceSlot), npc);
				// Magic.handleSpellOnEntity(player, interfaceSlot, npc);
				break;
			case 1449:
				if (componentId == 1)
					player.getActionbar()
							.useAbility(
									player.getCombatDefinitions()
											.onDefenceMenu() ? new DefenceAbilityShortcut(
											interfaceSlot)
											: new HealAbilityShortcut(
													interfaceSlot), npc);
				break;
			case 1452:
				if (componentId == 1)
					player.getActionbar().useAbility(
							new RangeAbilityShortcut(interfaceSlot), npc);
				break;
			case 1460:
				if (componentId == 1)
					player.getActionbar()
							.useAbility(
									player.getCombatDefinitions()
											.onStrengthMenu() ? new StrengthAbilityShortcut(
											interfaceSlot)
											: new MeleeAbilityShortcut(
													interfaceSlot), npc);
				break;
			}
		} else if (opcode == INTERFACE_ON_OBJECT) {

			int x = stream.readShortLE128();
			int interfaceHash = stream.readIntLE();
			int y = stream.readShortLE128();
			int objectId = stream.readIntV1();
			int itemId = stream.readShortLE128();
			boolean forceRun = stream.read128Byte() == 1;
			int slotId = stream.readShort128();

			final int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);

			if (Settings.DEBUG)
				Logger.log(WorldPacketsDecoder.class, "inter on object - "
						+ interfaceId + ", " + componentId + ", " + x + ", "
						+ y + ", " + objectId + ", " + slotId + ", " + itemId);

			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote())
				return;
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			WorldObject mapObject = World.getObjectWithId(tile, objectId);
			if (mapObject == null || mapObject.getId() != objectId)
				return;
			final WorldObject object = mapObject;
			if (player.isDead()
					|| Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (player.isLocked())
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			player.stopAll();
			if (forceRun)
				player.setRun(forceRun);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE: // inventory
			case Inventory.INVENTORY_INTERFACE_2:
				ObjectHandler.handleItemOnObject(player, object, interfaceId,
						slotId, itemId);
				break;
			case 430:// lunars
				switch (componentId) {
				case 55:
					if (player.getSkills().getLevel(Skills.MAGIC) < 66) {
						player.getPackets()
								.sendGameMessage(
										"You need a level of 65 in order to cast Cure Plant.");
						return;
					}
					if (!Magic.checkRunes(player, true, Magic.ASTRAL_RUNE, 1,
							Magic.EARTH_RUNE, 8))
						return;
					final FarmingSpot spot = player.getFarmingManager()
							.getSpot(SpotInfo.getInfo(object.getId()));
					if (spot == null || spot.isDead()) {
						player.getPackets().sendGameMessage(
								"This cannot be cured.");
						return;
					} else if (!spot.isDiseased()) {
						player.getPackets().sendGameMessage(
								"Your patch is not diseased.");
						return;
					}
					player.lock(3);
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							spot.setDiseased(false);
							spot.refresh();
						}
					}, 2);
					player.getSkills().addXp(Skills.MAGIC, 60);
					player.setNextGraphics(new Graphics(742, 0, 150));
					player.setNextAnimation(new Animation(4409));
					player.getPackets()
							.sendGameMessage(
									"You cast the spell and your patch is in perfect health.");
					break;
				}
				break;
			case 192: // regular spellbook
				switch (componentId) {
				case 60: // water charge
				case 64: // earth charge
				case 71: // fire charge
				case 74: // air charge
					for (int index = 0; index < 3; index++) {
						if (EnchantingOrbsDialogue.COMPONENTS[index] == componentId) {
							if (!Magic
									.checkRunes(
											player,
											false,
											EnchantingOrbsDialogue.REQUIRED_RUNES[index]))
								break;
							else if (!Magic.checkSpellLevel(player,
									EnchantingOrbsDialogue.LEVELS[index]))
								break;
							else {
								if (object.getId() == EnchantingOrbsDialogue.OBJECTS[index]) {
									player.faceObject(object);
									player.getDialogueManager().startDialogue(
											"EnchantingOrbsDialogue", index);
								}
							}
						}
					}
					break;
				}
				break;
			}
		} else if (opcode == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			Player p2 = World.getPlayers().get(playerIndex);
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();

			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote()
					|| !player.getControllerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControllerManager().canAttack(p2))
				return;
			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets()
						.sendGameMessage(
								"You can only attack players in a player-vs-player area.");
				return;
			}
			player.getActionManager().setAction(new PlayerCombat(p2));
		} else if (opcode == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked() || player.isCantWalk())
				return;
			if (!player.getControllerManager().canPlayerOption2(p2))
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.getActionManager().setAction(new PlayerFollow(p2));
		} else if (opcode == PLAYER_OPTION_3_PACKET) {
			boolean forceRun = stream.readUnsignedByte128() == 1;
			int playerIndex = stream.readUnsignedShort128();
			final Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.setRouteEvent(new RouteEvent(p2, new Runnable() {

				@Override
				public void run() {
					if (!player.getControllerManager().canPlayerOption4(p2))
						return;
					player.stopAll();
					if (player.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								"You are an " + player.getIronmanTitle(true)
										+ ", you stand alone.");
						return;
					}
					if (p2.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								p2.getDisplayName() + " is an "
										+ p2.getIronmanTitle(true)
										+ ", and assist.");
						return;
					}
					if (player.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Starter accounts cannot assist for the first hour of playing time.");
						return;
					}
					if (player.isCantTrade()
							|| player.getControllerManager().getController() != null
							&& player.getControllerManager().getController() instanceof StealingCreationLobbyController) {
						player.getPackets().sendGameMessage(
								"You are busy at the moment.");
						return;
					}
					if (p2.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Your target is a starter account, which cannot assist for the first hour of playing time.");
						return;
					}
					if (p2.getAssistanceManager().isAssisting()
							|| p2.getInterfaceManager()
									.containsScreenInterface()
							|| p2.isCantTrade()
							|| p2.getControllerManager().getController() != null
							&& p2.getControllerManager().getController() instanceof StealingCreationLobbyController
							|| p2.isLocked()) {
						player.getPackets().sendGameMessage(
								"The other player is busy at the moment.");
						return;
					}
					if (!p2.withinDistance(player, 14)) {
						player.getPackets().sendGameMessage(
								"Unable to find target " + p2.getDisplayName()
										+ ".");
						return;
					}
					if (!player.getBank().hasVerified(10)) {
						return;
					}
					if (p2.getTemporaryAttributtes().get("AssistTarget") == player) {
						p2.getTemporaryAttributtes().remove("AssistTarget");
						player.getAssistanceManager().acceptRequest(p2);
						return;
					}
					player.getTemporaryAttributtes().put("AssistTarget", p2);
					player.getPackets().sendGameMessage(
							"Sending the other player an assist request...");
					p2.getPackets().sendAssistRequestMessage(player);
				}
			}));
		} else if (opcode == PLAYER_OPTION_4_PACKET) {
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			final Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.setRouteEvent(new RouteEvent(p2, new Runnable() {

				@Override
				public void run() {
					if (!player.getControllerManager().canPlayerOption4(p2))
						return;
					player.stopAll();
					if (player.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								"You are an " + player.getIronmanTitle(true)
										+ ", you stand alone.");
						return;
					}
					if (p2.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								p2.getDisplayName() + " is an "
										+ p2.getIronmanTitle(true)
										+ ", and cannot trade.");
						return;
					}
					if (player.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Starter accounts cannot trade for the first hour of playing time.");
						return;
					}
					if (player.isCantTrade()
							|| player.getControllerManager().getController() != null
							&& player.getControllerManager().getController() instanceof StealingCreationLobbyController) {
						player.getPackets().sendGameMessage(
								"You are busy at the moment.");
						return;
					}
					if (p2.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Your target is a starter account, which cannot trade for the first hour of playing time.");
						return;
					}
					if (p2.getInterfaceManager().containsScreenInterface()
							|| p2.isCantTrade()
							|| p2.getControllerManager().getController() != null
							&& p2.getControllerManager().getController() instanceof StealingCreationLobbyController
							|| p2.isLocked()) {
						player.getPackets().sendGameMessage(
								"The other player is busy at the moment.");
						return;
					}
					if (!p2.withinDistance(player, 14)) {
						player.getPackets().sendGameMessage(
								"Unable to find target " + p2.getDisplayName()
										+ ".");
						return;
					}
					if (!player.getBank().hasVerified(10)) {
						return;
					}
					if (p2.getTemporaryAttributtes().get("TradeTarget") == player) {
						p2.getTemporaryAttributtes().remove("TradeTarget");
						player.getTrade().openTrade(p2);
						p2.getTrade().openTrade(player);
						return;
					}
					player.getTemporaryAttributtes().put("TradeTarget", p2);
					player.getPackets().sendGameMessage(
							"Sending the other player a trade request...");
					p2.getPackets().sendTradeRequestMessage(player);
				}
			}));
		} else if (opcode == 999) { // option 5 implement
			boolean forceRun = stream.readUnsignedByte128() == 1;
			int playerIndex = stream.readUnsignedShort128();
			final Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.setRouteEvent(new RouteEvent(p2, new Runnable() {

				@Override
				public void run() {
					if (!player.getControllerManager().canPlayerOption4(p2))
						return;
					player.stopAll();
					if (player.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								"You are an " + player.getIronmanTitle(true)
										+ ", you stand alone.");
						return;
					}
					if (p2.isAnIronMan()) {
						player.getPackets().sendGameMessage(
								p2.getDisplayName() + " is an "
										+ p2.getIronmanTitle(true)
										+ ", and assist.");
						return;
					}
					if (player.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Starter accounts cannot assist for the first hour of playing time.");
						return;
					}
					if (player.isCantTrade()
							|| player.getControllerManager().getController() != null
							&& player.getControllerManager().getController() instanceof StealingCreationLobbyController) {
						player.getPackets().sendGameMessage(
								"You are busy at the moment.");
						return;
					}
					if (p2.isBeginningAccount()) {
						player.getPackets()
								.sendGameMessage(
										"Your target is a starter account, which cannot assist for the first hour of playing time.");
						return;
					}
					if (p2.getAssistanceManager().isAssisting()
							|| p2.getInterfaceManager()
									.containsScreenInterface()
							|| p2.isCantTrade()
							|| p2.getControllerManager().getController() != null
							&& p2.getControllerManager().getController() instanceof StealingCreationLobbyController
							|| p2.isLocked()) {
						player.getPackets().sendGameMessage(
								"The other player is busy at the moment.");
						return;
					}
					if (!p2.withinDistance(player, 14)) {
						player.getPackets().sendGameMessage(
								"Unable to find target " + p2.getDisplayName()
										+ ".");
						return;
					}
					if (!player.getBank().hasVerified(10)) {
						return;
					}
					if (p2.getTemporaryAttributtes().get("AssistTarget") == player) {
						p2.getTemporaryAttributtes().remove("AssistTarget");
						player.getAssistanceManager().acceptRequest(p2);
						return;
					}
					player.getTemporaryAttributtes().put("AssistTarget", p2);
					player.getPackets().sendGameMessage(
							"Sending the other player an assist request...");
					p2.getPackets().sendAssistRequestMessage(player);
				}
			}));
		} else if (opcode == PLAYER_OPTION_6_PACKET) {
			System.out.println("OPTION 6 BABY");
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			if (p2.getPlayerExamineManager().privacyOn
					&& player.getRights() < 1) {
				player.getPackets().sendGameMessage(
						p2.getDisplayName() + " has privacy mode enabled.");
				return;
			}
			player.getPlayerExamineManager().openExamineDetails(p2);

		} else if (opcode == PLAYER_OPTION_9_PACKET) {// TODO no longer used
			System.out.println("OPTION 9 BABY");
			int playerIndex = stream.readUnsignedShortLE();
			boolean forceRun = stream.readUnsigned128Byte() == 1;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2 == player || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.isLocked())
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			if (ClansManager.viewInvite(player, p2))
				return;
			if (p2.getTemporaryAttributtes().get("social_request") == player)
				player.getSlayerManager().invitePlayer(p2);
		} else if (opcode == ATTACK_NPC_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			boolean forceRun = stream.readUnsignedByteC() == 1;
			int npcIndex = stream.readUnsignedShortLE128();
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())
					|| !npc.getDefinitions().hasAttackOption())
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote())
				return;
			if (!player.getControllerManager().canAttack(npc))
				return;
			if (forceRun) // you scrwed up cutscenes
				player.setRun(forceRun);
			player.stopAll();
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage(
							"You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage(
							"You can't attack this npc.");
					return;
				}
			} else if (npc instanceof DoorSupport) {
				if (!((DoorSupport) npc).canDestroy(player)) {
					player.getPackets().sendGameMessage(
							"You cannot see a way to open this door...");
					return;
				}
			}
			RecordKey key = RecordKey.forName(npc.getDefinitions().getName()
					.toLowerCase().replace("'", ""));
			if (key != null || npc instanceof BarrowsBrother
					|| npc instanceof HarAken || npc instanceof Legio
					|| npc instanceof TzTok_Jad || npc instanceof DagannothKing)
				player.getTimersManager().sendTimer();
			player.getActionManager().setAction(new PlayerCombat(npc));
		} else if (opcode == NPC_CLICK1_PACKET)
			NPCHandler.handleOption1(player, stream);
		else if (opcode == NPC_CLICK2_PACKET)
			NPCHandler.handleOption2(player, stream);
		else if (opcode == NPC_CLICK3_PACKET)
			NPCHandler.handleOption3(player, stream);
		else if (opcode == NPC_CLICK4_PACKET)
			NPCHandler.handleOption4(player, stream);
		else if (opcode == OBJECT_CLICK1_PACKET)
			ObjectHandler.handleOption(player, stream, 1);
		else if (opcode == OBJECT_CLICK2_PACKET)
			ObjectHandler.handleOption(player, stream, 2);
		else if (opcode == OBJECT_CLICK3_PACKET)
			ObjectHandler.handleOption(player, stream, 3);
		else if (opcode == OBJECT_CLICK4_PACKET)
			ObjectHandler.handleOption(player, stream, 4);
		else if (opcode == OBJECT_CLICK5_PACKET)
			ObjectHandler.handleOption(player, stream, 5);
		else if (opcode == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked())
				return;
			boolean forceRun = stream.read128Byte() == 1;
			int x = stream.readShortLE();
			int y = stream.readUnsignedShort128();
			int id = stream.readUnsignedShort128();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.setRouteEvent(new RouteEvent(item, new Runnable() {

				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId)
							.getGroundItem(id, tile, player);
					if (item == null
							|| !player.getControllerManager().canTakeItem(item))
						return;
					if (item.getOwner() != null) {
						if (player.isAnIronMan()
								&& item.getOwner() != player.getUsername()) {
							player.getPackets()
									.sendGameMessage(
											"You cannot pick up items dropped by other players.");
							return;
						}
					}
					if (World.removeGroundItem(player, item)) {
						try {
							DateFormat dateFormat = new SimpleDateFormat(
									"MM/dd/yy HH:mm:ss");
							Calendar cal = Calendar.getInstance();
							final String FILE_PATH = Settings
									.getDropboxLocation() + "logs/pickups/";
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(FILE_PATH
											+ player.getUsername() + ".txt",
											true));
							writer.write("["
									+ dateFormat.format(cal.getTime())
									+ ", IP: "
									+ player.getSession().getIP()
									+ "] has picked up item [ id: "
									+ item.getId()
									+ ", amount: "
									+ item.getAmount()
									+ " ] originally owned to "
									+ (item.getOwner() == null ? "no owner"
											: item.getOwner()));
							writer.newLine();
							writer.flush();
							writer.close();
						} catch (IOException er) {
							er.printStackTrace();
						}
					}
				}
			}));
		} else if (opcode == GROUND_ITEM_OPTION_2) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked())
				return;
			boolean forceRun = stream.read128Byte() == 1;
			int x = stream.readShortLE();
			int y = stream.readUnsignedShort128();
			int id = stream.readUnsignedShort128();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			if (forceRun)
				player.setRun(forceRun);
			player.stopAll();
			player.setRouteEvent(new RouteEvent(item, new Runnable() {

				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId)
							.getGroundItem(id, tile, player);
					if (item == null)
						return;
					for (Fire fire : Fire.values()) {
						if (item.getId() == fire.getLogId()) {
							player.getActionManager().setAction(
									new Firemaking(fire, true));
							return;
						}
					}
				}

			}));
		}
	}

	public void processPackets(final int opcode, InputStream stream)
			throws ClassNotFoundException, IOException {
		if (opcode != NIS_VAR_PACKET) { // dont want interface t oget messed up
			// <...<
			long ctime = System.nanoTime();
			if ((ctime - pthrotletimer[opcode]) > (1000000 * 600)) {
				pthrotlecounter[opcode] = 0;
				pthrotletimer[opcode] = ctime;
			}

			if (++pthrotlecounter[opcode] > 10) {
				pthrotletimer[opcode] = ctime; // reset timer to completly
				// mitigate ddos
				return;
			}
		}
		// System.out.println("packet: "+packetId +", "+
		// Thread.currentThread().getName());
		if (opcode == PING_PACKET) {
			player.getPackets().sendPing();
		} else if (opcode == CUTSCENE_DONE_PACKET) {
			@SuppressWarnings("unused")
			boolean done = stream.readUnsignedByte() == 1;
			// if not done means skipped
			player.loadMapRegions();
		} else if (opcode == NIS_VAR_PACKET) {
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			int count = (stream.getLength() - 1) / 6;
			for (int i = 0; i < count; i++) {
				Integer id = stream.readUnsignedShort();
				Integer value = stream.readInt();
				if (value == 0)
					player.getILayoutVars().remove(id);
				else
					player.getILayoutVars().put(id, value);
			}
			if (player.getILayoutVars().size() > 1000)
				player.resetILayoutVars();
			player.getPackets().sendResetNISVars();
		} else if (opcode == MUSIC_PACKET) {
			int archiveId = stream.readInt(); // started playing, request send
			// again
			// player.getMusicsManager().resetMusicDelay(player.getMusicsManager().getMusicId(archiveId));
			player.getMusicsManager().resetMusicDelay(
					player.getMusicsManager().getMusicId(archiveId));
		} else if (opcode == REQUEST_PLAY_MUSIC_PACKET) { // request play such
			// as after turn off
			// and on
			int archiveId = stream.readInt();
			player.getMusicsManager().resetMusicDelay(
					player.getMusicsManager().getMusicId(archiveId));
		} else if (opcode == WORLD_LIST_UPDATE) {
			if (!player.isLobby())
				return;
			int checksum = stream.readInt();
			LoginClientChannelManager
					.sendReliablePacket(LoginChannelsPacketEncoder
							.encodePlayerWorldListStatusRequest(
									player.getUsername(), checksum).getBuffer());
		} else if (opcode == MOUVE_MOUSE_PACKET
				|| opcode == MOUSE_MOVEMENT_DELAY) {
			// USELESS PACKET
		} else if (opcode == KEY_TYPED_PACKET) {
			// USELESS PACKET
		} else if (opcode == RECEIVE_PACKET_COUNT_PACKET) {
			// count
			stream.readInt();
		} else if (opcode == INTERFACE_ON_INTERFACE) {
			InventoryOptionsHandler.handleInterfaceOnInterface(player, stream);
		} else if (opcode == CLOSE_INTERFACE_PACKET) {
			player.stopAll();
		} else if (opcode == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readShort();
			stream.readShortLE128();
		} else if (opcode == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		} else if (opcode == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished()
					|| displayMode == player.getDisplayMode()/*
															 * || ! player .
															 * getInterfaceManager
															 * ( ) .
															 * containsInterface
															 * ( 742 )
															 */)
				return;
			player.setDisplayMode(displayMode);
			/*
			 * player.getInterfaceManager().removeAll();
			 * player.getInterfaceManager().sendInterfaces();
			 * player.getInterfaceManager().sendCentralInterface(742);
			 */
		} else if (opcode == CLICK_PACKET) {
			int mouseHash = stream.readUnsignedShort();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntLE();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			@SuppressWarnings("unused")
			boolean clicked;
			/*
			 * BotDetector.readMousePosition(player, x, y, opcode);
			 * System.err.println(x + ", " + y);
			 */
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
					|| y > player.getScreenHeight()) {
				// player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (opcode == CLICK_PACKET_2) { // TODO

		} else if (opcode == DIALOGUE_CONTINUE_PACKET) {
			int slotId = stream.readShortLE128();
			int interfaceHash = stream.readIntLE();
			int interfaceId = interfaceHash >> 16;
			int buttonId = (interfaceHash & 0xFF);

			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				player.getPackets().sendGameMessage(e + ".");
				player.getDialogueManager().finishDialogue();
			}
			if (interfaceId == 662) {
				if (buttonId == 55) {
					System.out.println(player.getFamiliar().toString());
					System.out.println(player.getFamiliar().getSpecialAttack()
							.toString());
					if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
						System.out.println("hellloo");
						player.getFamiliar().setSpecial(true);
					}
					if (player.getFamiliar().hasSpecialOn()) {
						System.out.println("test");
						player.getFamiliar().submitSpecial(player);
					}
				}
			}
			if (interfaceId == 1369) {
				if (buttonId == 25)
					player.getTemporaryAttributtes().put("WillDuelFriendly",
							true);
				else if (buttonId == 26)
					player.getTemporaryAttributtes().put("WillDuelFriendly",
							false);
				DuelController.challenge(player);
			}

			if (interfaceId == 1183) {
				if (buttonId == 6) {
					DungeonRewardShop.purchase(player);
					DungeonRewardShop.removeConfirmationPurchase(player);
				} else if (buttonId == 7) {
					DungeonRewardShop.removeConfirmationPurchase(player);
				}
			}

			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				// hack, or server error or client error
				// player.getSession().getChannel().close();
				return;
			}
			if (Settings.DEBUG)
				Logger.log(this, "Dialogue: " + interfaceId + ", " + buttonId
						+ ", " + slotId);
			if (!player.isRunning()
					|| !player.getInterfaceManager().containsInterface(
							interfaceId))
				return;

			int componentId = interfaceHash - (interfaceId << 16);
			player.getDialogueManager().continueDialogue(interfaceId,
					componentId, slotId);
		} else if (opcode == WORLD_MAP_CLICK) {
			int coordinateHash = stream.readIntV2();
			int x = coordinateHash >> 14;
			int y = coordinateHash & 0x3fff;
			int plane = coordinateHash >> 28;
			Integer hash = (Integer) player.getTemporaryAttributtes().get(
					"worldHash");
			if (hash == null || coordinateHash != hash)
				player.getTemporaryAttributtes().put("worldHash",
						coordinateHash);
			else {
				player.getTemporaryAttributtes().remove("worldHash");
				player.getHintIconsManager().addHintIcon(x, y, plane, 20, 0, 2,
						-1, true);
				player.getVarsManager().sendVar(2807, coordinateHash);
			}
		} else if (opcode == ACTION_BUTTON1_PACKET
				|| opcode == ACTION_BUTTON2_PACKET
				|| opcode == ACTION_BUTTON4_PACKET
				|| opcode == ACTION_BUTTON5_PACKET
				|| opcode == ACTION_BUTTON6_PACKET
				|| opcode == ACTION_BUTTON7_PACKET
				|| opcode == ACTION_BUTTON8_PACKET
				|| opcode == ACTION_BUTTON3_PACKET
				|| opcode == ACTION_BUTTON9_PACKET
				|| opcode == ACTION_BUTTON10_PACKET) {
			player.afk = Utils.currentTimeMillis()
					+ (Settings.DEBUG ? Integer.MAX_VALUE : 15 * 60 * 1000);
			ButtonHandler.handleButtons(player, stream, opcode);
		} else if (opcode == ENTER_NAME_PACKET) {
			if (!player.isRunning()
					|| player.isDead()
					|| !player.getInterfaceManager()
							.containsInputTextInterface())
				return;
			player.getInterfaceManager().removeInputTextInterface();
			String value = stream.readString();
			if (value.equals(""))
				return;
			else if (player.getDialogueManager().continueDialogue(value))
				return;
			else if (player.getTemporaryAttributtes().remove("setclan") != null)
				ClansManager.createClan(player, value);
			if (player.getInterfaceManager().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);
			else if (player.getTemporaryAttributtes().remove("joinguestclan") != null)
				ClansManager.connectToClan(player, value, true);
			else if (player.getTemporaryAttributtes().remove("banclanplayer") != null)
				ClansManager.banPlayer(player, value);
			else if (player.getTemporaryAttributtes().remove("unbanclanplayer") != null)
				ClansManager.unbanPlayer(player, value);
			else if (player.getTemporaryAttributtes()
					.remove(Key.DUNGEON_INVITE) != null)
				player.getDungManager().invite(value);
			else if (player.getTemporaryAttributtes()
					.remove(Key.CLAN_WARS_VIEW) != null)
				ClanWars.enter(player, value);
			else if (player.getTemporaryAttributtes().remove("enterhouse") != null)
				House.enterHouse(player, value);
			else {
				Boss boss = (Boss) player.getTemporaryAttributtes().remove(
						Key.JOIN_BOSS_INSTANCE);
				if (boss != null)
					BossInstanceHandler.joinInstance(player, boss,
							value.toLowerCase(), false);
			}
		} else if (opcode == ENTER_LONG_TEXT_PACKET) {
			if (!player.isRunning()
					|| player.isDead()
					|| !player.getInterfaceManager()
							.containsInputTextInterface())
				return;
			player.getInterfaceManager().removeInputTextInterface();
			String value = stream.readString();
			if (value.equals(""))
				return;
			else if (player.getDialogueManager().continueDialogue(value))
				return;
			if (player.getTemporaryAttributtes().remove(Key.PERSONAL_MESSAGE) != null) {
				player.getPlayerExamineManager().setPersonalMessage(value);
			} else if (player.getTemporaryAttributtes().get("Title") != null) {
				if (Censor.containsProfanity(value.toLowerCase())) {
					player.getPackets()
							.sendGameMessage(
									"The title that you have entered is innappropriate.");
					return;
				}
				if (CustomTitle.containsInvalidCharacter(value)) {
					player.getPackets()
							.sendGameMessage(
									"The title that you have entered contains an invalid character.");
					return;
				}
				switch ((int) player.getTemporaryAttributtes().get("Title")) {
				case CustomTitle.TITLE_IN_FRONT:
					player.setCustomTitle(null);
					player.setTitleString(null);
					player.setCustomTitle(value);
					break;
				case CustomTitle.TITLE_IN_BACK:
					player.setCustomTitle(null);
					player.setTitleString(null);
					player.getAppearence().setTitle(100);
					player.setTitleString(value);
					break;
				case CustomTitle.TITLE_COLOR:
					if (!Utils.isValidHex(value)) {
						player.getPackets()
								.sendGameMessage(
										"<col=FF0000>Title color must be a valid hex color code.</col>");
						return;
					}
					player.setTitleColor(value);
					break;
				case CustomTitle.TITLE_SHADE:
					if (!Utils.isValidHex(value)) {
						player.getPackets()
								.sendGameMessage(
										"<col=FF0000>Title color must be a valid hex color code.</col>");
						return;
					}
					player.setTitleShade(value);
					break;
				}
				player.getTemporaryAttributtes().remove("Title");
				player.getAppearence().generateAppearenceData();
			} else if (player.getTemporaryAttributtes().remove("senddialogue") == Boolean.TRUE) {
				String name = value;
				player.getDialogueManager().startDialogue(name, 1);
				player.getPackets().sendPanelBoxMessage(
						"Playing dialogue " + name + ".");
			} else if (player.getTemporaryAttributtes().remove("currentpass") == Boolean.TRUE) {
				/*
				 * Account account = null; if
				 * (name.equals(player.getPassword(account))) {
				 * player.getDialogueManager().startDialogue("SimpleMessage",
				 * "Correct current password."); } else {
				 * player.getDialogueManager().startDialogue("SimpleMessage",
				 * "Invalid current password. Please try again."); }
				 */
				player.getDialogueManager().startDialogue("SimpleMessage",
						"Error.");
			} else if (player.getTemporaryAttributtes().remove("lottery") == Boolean.TRUE) {
				Integer.parseInt(value);
				Lottery.INSTANCE.addPlayer(player, null);
			} else if (player.getTemporaryAttributtes().remove("nogood") == Boolean.TRUE) {
				player.getPackets().sendGameMessage(
						"No... This is no good - miles M", true);
			} else if (player.getTemporaryAttributtes().remove("coalWithdraw") == Boolean.TRUE) {
				Coalbag.withdrawCoal(player, Integer.parseInt(value));
			} else if (player.getTemporaryAttributtes().remove(
					"lootbeamMinValue") == Boolean.TRUE) {
				int tot = Integer.parseInt(value);
				if (tot >= 1000) {
					player.setLootbeamAmount(tot);
					player.getPackets().sendIComponentText(1623, 121, "" + tot);
				} else {
					player.getDialogueManager()
							.startDialogue("SimpleMessage",
									"Sorry, but the amount must be higher than 1000 gp.");
				}
			} else if (player.getTemporaryAttributtes().remove("clancitadel") == Boolean.TRUE) {
				if (SerializableFilesManager.containsClan(value)) {
					player.getPackets().sendGameMessage("Getting clan data...");
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getPackets().sendGameMessage(
									"Welcome to " + Settings.SERVER_NAME + ".");
							player.getPackets().sendGameMessage(
									"Entering citadel...");
							player.setNextWorldTile(new WorldTile(5504, 4494, 0));
							stop();
						}
					}, 2, 0);
				} else {
					player.getPackets().sendGameMessage(
							"Not a valid clan name.");
				}
			} else if (player.getTemporaryAttributtes().remove("customTitle") == Boolean.TRUE) {
				String[] invalid = { "<euro", "<img", "<img=", "<col", "<col=",
						"<shad", "<shad=", "<str>", "<u>", "@", "mod", "admin",
						"support", "danny", "pax", "flavius", "charity", "sex",
						"porn", "dick", "penis", "vagina", "pussy", "gay" };
				for (String s : invalid)
					if (value.contains(s)) {
						player.getDialogueManager()
								.startDialogue("SimpleMessage",
										"Your input value contains one or more invalid characters");
						return;
					}
				if (value.equalsIgnoreCase(" ") || value.equalsIgnoreCase("  "))
					return;
				if (value.length() >= 14)
					player.getDialogueManager()
							.startDialogue("SimpleMessage",
									"The input value cannot be longer than 14 characters.");
				else {
					player.setCustomTitleActive(true);
					player.setCustomTitleColor("551A8B");
					player.setCustomTitle(value);
					player.getDialogueManager().startDialogue(
							"SimpleMessage",
							"You have changed your custom title to '<col="
									+ player.getCustomTitleColor() + ">"
									+ player.getCustomTitle() + "</col>'.");
					player.getAppearence().generateAppearenceData();
				}
			} else if (player.getTemporaryAttributtes().remove("fc") == Boolean.TRUE) {
				player.getPackets()
						.sendGameMessage("Use ctrl+shift you idiot.");
			} else if (player.getTemporaryAttributtes().remove("risesix") == Boolean.TRUE) {
				RiseOfTheSixD.joinParty(player, value);
			} else if (player.getTemporaryAttributtes().remove("fc_to") == Boolean.TRUE) {
				Player target = World.getPlayerByDisplayName(value);
				if (target == null) {
					player.getPackets().sendGameMessage("Player is offline.");
					return;
				}
				player.getDialogueManager().startDialogue("OPTeleto", target);
			} else if (player.getTemporaryAttributtes().remove("fc_tome") == Boolean.TRUE) {
				Player target = World.getPlayerByDisplayName(value);
				if (target == null) {
					player.getPackets().sendGameMessage("Player is offline.");
					return;
				}
				player.getDialogueManager().startDialogue("OPTeletome", target);
			} else if (player.getTemporaryAttributtes().remove("entering_note") == Boolean.TRUE)
				player.getNotes().add(value);
			else if (player.getTemporaryAttributtes().remove("editing_note") == Boolean.TRUE)
				player.getNotes().edit(value);
			else if (player.getTemporaryAttributtes().remove("ticket_other") != null) {
				TicketSystem.addTicket(new TicketEntry(player, value));
				player.getDialogueManager().startDialogue("SimpleMessage",
						"Your ticket has been submitted.");
			} else if (player.getTemporaryAttributtes().remove(
					"forum_authuserinput") == Boolean.TRUE) {
				player.getTemporaryAttributtes().put("forum_authuser", value);
				player.getTemporaryAttributtes().put("forum_authpasswordinput",
						true);
				player.getPackets().sendInputLongTextScript(
						"Enter your forum password:");
			} else if (player.getTemporaryAttributtes().remove(
					"forum_authpasswordinput") == Boolean.TRUE) {
				String authuser = (String) player.getTemporaryAttributtes()
						.get("forum_authuser");
				String authpassword = value;
				if (authuser == null || authpassword == null)
					return;
				LoginClientChannelManager
						.sendReliablePacket(LoginChannelsPacketEncoder
								.encodeAccountVarUpdate(player.getUsername(),
										LoginProtocol.VAR_TYPE_AUTH,
										authuser + "@AUTHSPLIT@" + authpassword)
								.getBuffer());
				player.getTemporaryAttributtes().remove("forum_authuser");
				// player.getPackets().sendGameMessage("Feature disabled due to
				// rework.");
			} else if (player.getTemporaryAttributtes().remove(
					"change_troll_name") == Boolean.TRUE) {
				value = Utils.formatPlayerNameForDisplay(value);
				if (value.length() < 3 || value.length() > 14) {
					player.getPackets()
							.sendGameMessage(
									"You can't use a name shorter than 3 or longer than 14 characters.");
					return;
				}
				if (value.equalsIgnoreCase("none")) {
					player.getPetManager().setTrollBabyName(null);
				} else {
					player.getPetManager().setTrollBabyName(value);
					if (player.getPet() != null
							&& player.getPet().getId() == Pets.TROLL_BABY
									.getBabyNpcId()) {
						player.getPet().setName(value);
					}
				}
			} else if (player.getTemporaryAttributtes().remove("yellcolor") == Boolean.TRUE) {
				if (value.length() != 6) {
					player.getPackets()
							.sendGameMessage(
									"The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if (Utils.containsInvalidCharacter(value)
						|| value.contains("_")) {
					player.getPackets()
							.sendGameMessage(
									"The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getPackets().sendGameMessage(
							"Your yell color has been changed to <col="
									+ player.getYellColor() + ">"
									+ player.getYellColor() + "</col>.");
				}
			} else if (player.getTemporaryAttributtes().get("yellcolor") == Boolean.TRUE) {
				if (value.length() != 6) {
					player.getDialogueManager()
							.startDialogue("SimpleMessage",
									"The HEX yell color you wanted to pick cannot be longer and shorter then 6.");
				} else if (Utils.containsInvalidCharacter55(value)
						|| value.contains("_")) {
					player.getDialogueManager()
							.startDialogue("SimpleMessage",
									"The requested yell color can only contain numeric and regular characters.");
				} else {
					player.setYellColor(value);
					player.getDialogueManager().startDialogue(
							"SimpleMessage",
							"Your yell color has been changed to <col="
									+ player.getYellColor() + ">"
									+ player.getYellColor() + "</col>.");
				}
				player.getTemporaryAttributtes()
						.put("yellcolor", Boolean.FALSE);
			} else if (player.getTemporaryAttributtes().remove("setdisplay") == Boolean.TRUE) {
				if (Utils.invalidAccountName(Utils
						.formatPlayerNameForProtocol(value))) {
					player.getPackets()
							.sendGameMessage(
									"Name contains invalid characters or is too short/long.");
					return;
				}
				LoginClientChannelManager
						.sendReliablePacket(LoginChannelsPacketEncoder
								.encodeAccountVarUpdate(player.getUsername(),
										LoginProtocol.VAR_TYPE_DISPLAY_NAME,
										Utils.formatPlayerNameForDisplay(value))
								.getBuffer());
				// player.getPackets().sendGameMessage("Feature disabled due to
				// rework.");
			} else if (player.getTemporaryAttributtes().remove(Key.CLAN_MOTTO) == Boolean.TRUE)
				ClansManager.setClanMottoInterface(player, value);
		} else if (opcode == ENTER_INTEGER_PACKET) {
			if (!player.isRunning()
					|| player.isDead()
					|| !player.getInterfaceManager()
							.containsInputTextInterface())
				return;
			player.getInterfaceManager().removeInputTextInterface();
			int value = stream.readInt();
			if (value < 0)
				return;
			if (player.getTemporaryAttributtes().remove("withdrawingPouch") == Boolean.TRUE) {
				player.getMoneyPouch().sendDynamicInteraction(value, true,
						MoneyPouch.TYPE_POUCH_INVENTORY);
			}
			if ((player.getInterfaceManager().containsInterface(762))
					|| player.getInterfaceManager().containsInterface(11)) {
				Integer BankSlot = (Integer) player.getTemporaryAttributtes()
						.remove("slotBank");
				Integer BoBSlot = (Integer) player.getTemporaryAttributtes()
						.remove("slotBoB");
				if (player.isOnBoBWindow) {
					if (BoBSlot == null)
						return;
					if (player.getTemporaryAttributtes().remove("withdrawBoB") != null)
						player.getBank().withdrawBoBItem(BoBSlot, value);
					else
						player.getBank().depositBoBItem(
								BoBSlot,
								value,
								player.getInterfaceManager().containsInterface(
										11) ? false : true);
				} else {
					if (BankSlot == null)
						return;
					if (player.getTemporaryAttributtes().remove("withdrawItem") != null)
						player.getBank().withdrawItem(BankSlot, value);
					else
						player.getBank().depositItem(
								BankSlot,
								value,
								player.getInterfaceManager().containsInterface(
										11) ? false : true);
				}
			} else if (player.getInterfaceManager().containsInterface(631)
					&& player.getTemporaryAttributtes().get(
							Key.DUEL_COIN_WITHDRAWL) != null) {
				DuelArena arena = (DuelArena) player.getTemporaryAttributtes()
						.get(Key.DUEL_COIN_WITHDRAWL);
				Controller control = player.getControllerManager()
						.getController();
				if (control == null || control != arena)
					return;
				int coinsAmount = player.getInventory().getCoinsAmount();
				if (coinsAmount == 0)
					return;
				else if (value >= coinsAmount)
					value = coinsAmount;
				arena.addItem(new Item(995, value));
			} else if (player.getTemporaryAttributtes().get("WellOfExperience") == Boolean.TRUE) {
				try {
					XPWell.addWellAmount(player, value);
					player.getTemporaryAttributtes().put("WellOfExperience",
							Boolean.FALSE);
				} catch (Exception e) {
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Invalid format.");
				}
			} else if (player.getTemporaryAttributtes().get("Repair") != null) {
				int repairId = (int) player.getTemporaryAttributtes().get(
						"Ritem");
				if (!player.getInventory().containsItem(repairId, value))
					value = player.getInventory().getAmountOf(repairId);
				RepairItems.repair(player, repairId, value);
				player.getTemporaryAttributtes().remove("Repair");
			} else if (player.getInterfaceManager().containsInterface(
					AccessorySmithing.ACCESSORY_INTERFACE)
					&& player.getTemporaryAttributtes().get(
							Key.JEWLERY_SMITH_COMP) != null) {
				AccessorySmithing.handleButtonClick(player, (int) player
						.getTemporaryAttributtes().get(Key.JEWLERY_SMITH_COMP),
						value);
			} else if (player.getInterfaceManager().containsInterface(206)
					&& player.getInterfaceManager().containsInterface(207)) {
				Integer pc_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot,
							value);
				else
					player.getPriceCheckManager()
							.addItem(pc_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(400)) {
				Integer create_tab_X_component = (Integer) player
						.getTemporaryAttributtes().remove(
								"create_tab_X_component");
				if (create_tab_X_component == null)
					return;
				TabletMaking.handleTabletCreation(player,
						create_tab_X_component, value);
			} else if (player.getInterfaceManager().containsInterface(671)
					|| player.getInterfaceManager().containsInterface(662)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob()
							.removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob()
							.addItem(bob_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(335)
					&& player.getInterfaceManager().containsInterface(336)) {
				if (player.getTemporaryAttributtes().remove(
						Key.TRADE_COIN_WITHDRAWL) != null) {
					int coinsAmount = player.getInventory().getCoinsAmount();
					if (coinsAmount == 0)
						return;
					else if (value >= coinsAmount)
						value = coinsAmount;
					Item item = new Item(995, value);
					player.getInventory().removeItemMoneyPouch(item);
					player.getTrade().addItem(item);
					return;
				}
				Integer trade_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("trade_item_X_Slot");
				if (trade_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("trade_isRemove") != null)
					player.getTrade().removeItem(trade_item_X_Slot, value);
				else
					player.getTrade().addItem(trade_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(403)
					&& player.getTemporaryAttributtes().get("PlanksConvert") != null) {
				Sawmill.convertPlanks(player, (Plank) player
						.getTemporaryAttributtes().remove("PlanksConvert"),
						value);

			} else if (player.getTemporaryAttributtes().get("SetSecureCode") != null) {
				if (player.getSecureCode() == 0) {
					player.setSecureCode(value);
					player.getDialogueManager().startDialogue(
							"SimpleNPCMessage",
							19560,
							"Your Secure Code has been set to <col=FF0000>"
									+ player.getSecureCode() + "</col>.");
					player.getPackets().sendGameMessage(
							"Your Secure Code has been set to <col=FF0000>"
									+ player.getSecureCode() + "</col>.");
					player.getTemporaryAttributtes().remove("SetSecureCode");
				} else {
					if (player.getSecureCode() != value) {
						player.getDialogueManager()
								.startDialogue("SimpleNPCMessage", 19560,
										"The Secure Code does not match. Please try again.");
					} else {
						player.getTemporaryAttributtes()
								.remove("SetSecureCode");

						player.getTemporaryAttributtes().put(
								"SetSecureCodeTwo", 0);
						player.getPackets().sendInputIntegerScript(
								"Please enter your new secure code:");
					}
				}
			} else if (player.getTemporaryAttributtes().get("SetSecureCodeTwo") != null) {
				player.setSecureCode(value);
				player.getDialogueManager().startDialogue(
						"SimpleNPCMessage",
						19560,
						"Your Secure Code has been set to <col=FF0000>"
								+ player.getSecureCode() + "</col>.");
				player.getPackets().sendGameMessage(
						"Your Secure Code has been set to <col=FF0000>"
								+ player.getSecureCode() + "</col>.");
				player.getTemporaryAttributtes().remove("SetSecureCodeTwo");
			} else if (player.getTemporaryAttributtes().get("SecureCode") != null) {
				if (player.getSecureCode() != value) {
					player.secureAttempts++;

					if (player.secureAttempts >= 5) {
						int type = LoginProtocol.OFFENCE_ADDTYPE_BAN;
						long expires = 1000 * 60 * 60 * 6; // 6 hours
						LoginClientChannelManager
								.sendUnreliablePacket(LoginChannelsPacketEncoder
										.encodeAddOffence(type,
												player.getUsername(),
												player.getUsername(),
												"Account hacking", expires)
										.getBuffer());
						player.getInterfaceManager().removeInputTextInterface();
						player.getTemporaryAttributtes().remove("SecureCode");
						World.sendStaffMessage("[STAFF] - The account "
								+ player.getDisplayName()
								+ " has been hacked, protection has kicked in. Contact an Administrator.");
						try {
							DateFormat dateFormat2 = new SimpleDateFormat(
									"MM/dd/yy HH:mm:ss");
							Calendar cal2 = Calendar.getInstance();
							BufferedWriter writer = new BufferedWriter(
									new FileWriter(
											Settings.getDropboxLocation()
													+ "logs/securecode/"
													+ player.getUsername()
													+ ".txt", true));
							writer.write("["
									+ dateFormat2.format(cal2.getTime())
									+ "] IP: " + player.getSession().getIP());
							writer.newLine();
							writer.flush();
							writer.close();
						} catch (IOException er) {
							er.printStackTrace();
						}
						return;
					}

					player.getTemporaryAttributtes().remove("SecureCode");

					player.getTemporaryAttributtes().put("SecureCode", 0);
					player.getPackets().sendInputIntegerScript(
							"Invalid Secure Code (" + player.secureAttempts
									+ "/5 attempts left):");
				} else {
					player.secureAttempts = 0;
					player.unlock();
					player.getInterfaceManager().removeCentralInterface();
					player.getPackets().sendGameMessage(
							"Secure Code entered correctly.");
					player.getTemporaryAttributtes().remove("SecureCode");
				}
			} else if (player.getTemporaryAttributtes().get("DeleteSecureCode") != null) {
				if (player.getSecureCode() != value) {
					player.getDialogueManager().startDialogue(
							"SimpleNPCMessage", 19560,
							"Invalid Secure Code, please try again.");
				} else {
					player.setSecureCode(0);
					player.getDialogueManager().startDialogue(
							"SimpleNPCMessage", 19560,
							"Your Secure Code has been removed.");
					player.getPackets().sendGameMessage(
							"Your Secure Code has been removed.");
				}
			} else if (player.getInterfaceManager().containsInterface(902)
					&& player.getTemporaryAttributtes().get("PlankMake") != null) {
				Integer type = (Integer) player.getTemporaryAttributtes()
						.remove("PlankMake");
				if (player.getControllerManager().getController() instanceof SawmillController)
					((SawmillController) player.getControllerManager()
							.getController()).cutPlank(type, value);
			} else if (player.getInterfaceManager().containsInterface(903)
					&& player.getTemporaryAttributtes().get("PlankWithdraw") != null) {
				Integer type = (Integer) player.getTemporaryAttributtes()
						.remove("PlankWithdraw");
				if (player.getControllerManager().getController() instanceof SawmillController)
					((SawmillController) player.getControllerManager()
							.getController()).withdrawFromCart(type, value);
			} else if (player.getInterfaceManager().containsInterface(105)
					&& player.getTemporaryAttributtes().remove("GEPRICESET") != null) {
				player.getGeManager().modifyPricePerItem(value);
			} else if (player.getInterfaceManager().containsInterface(105)
					&& player.getTemporaryAttributtes().remove("GEQUANTITYSET") != null) {
				player.getGeManager().modifyAmount(value);
			} else if (player.getControllerManager().getController() != null
					&& player.getTemporaryAttributtes().get(
							Key.SERVANT_REQUEST_ITEM) != null) {
				Integer type = (Integer) player.getTemporaryAttributtes()
						.remove(Key.SERVANT_REQUEST_TYPE);
				Integer item = (Integer) player.getTemporaryAttributtes()
						.remove(Key.SERVANT_REQUEST_ITEM);
				if (!player.getHouse().isLoaded()
						|| !player.getHouse().getPlayers().contains(player)
						|| type == null || item == null)
					return;
				player.getHouse().getServantInstance()
						.requestType(item, value, type.byteValue());
			} else if (player.getTemporaryAttributtes().remove("xformring") == Boolean.TRUE)
				player.getAppearence().transformIntoNPC(value);
			else if (player.getTemporaryAttributtes().remove(
					Key.SELL_SPIRIT_SHARDS) != null)
				PetShopOwner.sellShards(player, value);
			else if (player.getTemporaryAttributtes().get("selected_neg") != null) {
				int selectedSkill = (int) player.getTemporaryAttributtes().get(
						"selected_neg");
				int skillLevel = player.getSkills()
						.getLevelForXp(selectedSkill);
				if (value >= skillLevel) {
					if (skillLevel == 1)
						value = 1;
					else
						value = skillLevel - 1;
				} else if (value == 0)
					value = 1;
				int skillOffset = skillLevel - value;
				if (skillOffset <= 0)
					skillOffset = 0;
				int price = SkillAlchemist.calculatePrice(player, skillOffset);
				player.getDialogueManager().finishDialogue();
				if (player.getInventory().getCoinsAmount() < price) {
					player.getDialogueManager()
							.startDialogue(
									"SimpleNPCMessage",
									5585,
									"You need "
											+ price
											+ " amount of coins, in order to reduce your level by "
											+ skillOffset + ".");
					return;
				} else if (player.getEquipment().wearingArmour()) {
					player.getDialogueManager()
							.startDialogue(
									"SimpleNPCMessage",
									5585,
									"Please remove any equipment you have equipped, the tranmutation possibly could damage other metals and fabrics.");
					return;
				} else {
					if (player.getFamiliar() != null)
						player.getFamiliar().dissmissFamiliar(false);
					player.getPrayer().closeAllPrayers(true);
					player.getSkills().set(selectedSkill, value);
					player.getSkills().setXp(selectedSkill,
							Skills.getXPForLevel(value));
					player.getAppearence().generateAppearenceData();
					player.getInventory().removeItemMoneyPouch(
							new Item(995, price));
					player.getDialogueManager()
							.startDialogue("SimpleMessage",
									"As your coins transmute, you begin feel like your forgetting something...");
					if (player.getDonationManager().isDonator())
						player.getPackets()
								.sendGameMessage(
										"You notice that your gold is still the same quantity as before....");
				}
			} else if (player.getTemporaryAttributtes().remove("kilnX") != null) {
				int componentId = (Integer) player.getTemporaryAttributtes()
						.get("sc_component");
				if (player.getControllerManager().getController() instanceof StealingCreationController) {
					StealingCreationController controller = (StealingCreationController) player
							.getControllerManager().getController();
					player.getTemporaryAttributtes().put("sc_amount_making",
							value);
					controller.processKilnExchange(componentId, 50);
				}
			} else if (player.getTemporaryAttributtes().get("xpSkillTarget") != null) {
				try {
					int xpTarget = value;
					Integer skillId = (Integer) player
							.getTemporaryAttributtes().remove("xpSkillTarget");
					if (xpTarget < player.getSkills().getXp(
							player.getSkills().getSkillIdByTargetId(skillId))
							|| player.getSkills().getXp(
									player.getSkills().getSkillIdByTargetId(
											skillId)) >= 200000000)
						return;
					if (xpTarget > 200000000)
						xpTarget = 200000000;
					player.getSkills().setSkillTarget(false, skillId, xpTarget);
				} catch (Exception e) {
					player.getPackets().sendGameMessage("error: " + e + ".");
					e.printStackTrace();
				}
			} else if (player.getTemporaryAttributtes().get("levelSkillTarget") != null) {
				try {
					int levelTarget = value;
					Integer skillId = (Integer) player
							.getTemporaryAttributtes().remove(
									"levelSkillTarget");
					int curLevel = player.getSkills().getLevel(
							player.getSkills().getSkillIdByTargetId(skillId));
					if (curLevel >= (skillId == 24 ? 120 : 99))
						return;
					if (levelTarget > (skillId == 24 ? 120 : 99))
						levelTarget = skillId == 24 ? 120 : 99;
					if (levelTarget < player.getSkills().getLevel(
							player.getSkills().getSkillIdByTargetId(skillId)))
						return;
					player.getSkills().setSkillTarget(true, skillId,
							levelTarget);
				} catch (Exception e) {
					player.getPackets().sendGameMessage("error: " + e + ".");
					e.printStackTrace();
				}
			} else if (player.getTemporaryAttributtes().get("sc_request") != null) {
				int requestedId = (int) player.getTemporaryAttributtes().get(
						"sc_request");
				WorldTile tile = (WorldTile) player.getTemporaryAttributtes()
						.get("sc_object");
				if (ItemDefinitions.getItemDefinitions(requestedId)
						.isStackable()) {
					FloorItem item = World.getRegion(player.getRegionId())
							.getGroundItem(requestedId, tile, player);
					if (item == null)
						return;
					if (item.getAmount() > value) {
						World.addGroundItem(
								new Item(requestedId, item.getAmount() - value),
								tile);
						item.setAmount(value);
					}
					if (player.getControllerManager().canTakeItem(item))
						World.removeGroundItem(player, item);
				} else {
					if (value > 28)
						value = 28;
					for (int i = 0; i < value; i++) {
						FloorItem item = World.getRegion(player.getRegionId())
								.getGroundItem(requestedId, tile, player);
						if (item == null
								|| !player.getControllerManager().canTakeItem(
										item))
							break;
						World.removeGroundItem(player, item);
					}
				}
			} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				Integer skill = (Integer) player.getTemporaryAttributtes()
						.remove("skillId");
				player.getDialogueManager().finishDialogue();
				if (value > 99) {
					player.getPackets().sendGameMessage(
							"Please choose a valid level.");
					return;
				}
				player.getSkills().set(skill, value);
				player.getSkills().setXp(skill, Skills.getXPForLevel(value));
				player.getPrayer().closeAllPrayers(true);
				player.getAppearence().generateAppearenceData();
			}
		} else if (opcode == SWITCH_INTERFACE_COMPONENTS_PACKET) {
			int toSlot = stream.readUnsignedShort128();
			int fromInterfaceHash = stream.readIntV2();
			int fromSlotId2 = stream.readUnsignedShortLE();
			int toSlotId2 = stream.readUnsignedShortLE128();
			int fromSlot = stream.readUnsignedShort();
			int toInterfaceHash = stream.readInt();

			int toInterfaceId = toInterfaceHash >> 16;
			int toComponentId = toInterfaceHash - (toInterfaceId << 16);
			int fromInterfaceId = fromInterfaceHash >> 16;
			int fromComponentId = fromInterfaceHash - (fromInterfaceId << 16);
			if (Settings.DEBUG)
				System.out.println("Switch item " + fromInterfaceId + ", "
						+ toInterfaceId + ", " + fromSlot + ", " + toSlot
						+ ", " + fromComponentId + ", " + toComponentId);

			if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId
					|| Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.getInterfaceManager()
					.containsInterface(fromInterfaceId)
					|| !player.getInterfaceManager().containsInterface(
							toInterfaceId))
				return;
			if (fromComponentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1
					&& Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			if (toInterfaceId == 1670
					&& (toComponentId >= 11 && toComponentId <= 180)) { // FIRST
																		// //
																		// MULTI-ACTIONBAR
				if (fromInterfaceId == 1670
						&& (fromComponentId >= 11 && fromComponentId <= 180))
					player.getActionbar().switchShortcut1(
							(fromComponentId - 11) / 13,
							(toComponentId - 11) / 13);
				else if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						|| fromInterfaceId == Inventory.INVENTORY_INTERFACE_2) {
					if (fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					Item item = player.getInventory().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							new ItemShortcut(item.getId()));
				} else if ((fromInterfaceId == 1464 && fromComponentId == 15)
						|| (fromInterfaceId == 1464 && fromComponentId == 14)) {
					if (fromSlot >= player.getEquipment().getItems().getSize())
						return;
					Item item = player.getEquipment().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							new ItemShortcut(item.getId()));
				} else if (fromInterfaceId == 1461 && fromComponentId == 1) {
					if (Magic.getSpellData(fromSlot) == null) // fake spell
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							new MagicAbilityShortcut(fromSlot));

				} else if (fromInterfaceId == 1449 && fromComponentId == 1) {
					boolean usingDefenceAbilities = player
							.getCombatDefinitions().onDefenceMenu();
					if (ActionBar
							.getAbilityData(
									usingDefenceAbilities ? ActionBar.DEFENCE_ABILITY_SHORTCUT
											: ActionBar.HEAL_ABILITY_SHORTCUT,
									fromSlot) == null)
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							usingDefenceAbilities ? new DefenceAbilityShortcut(
									fromSlot) : new HealAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1452 && fromComponentId == 1) {
					if (ActionBar.getAbilityData(
							ActionBar.RANGED_ABILITY_SHORTCUT, fromSlot) == null)
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							new RangeAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1460 && fromComponentId == 1) {
					boolean usingStrAbilities = player.getCombatDefinitions()
							.onStrengthMenu();
					if (ActionBar
							.getAbilityData(
									usingStrAbilities ? ActionBar.STRENGTH_ABILITY_SHORTCUT
											: ActionBar.MELEE_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						return;
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							usingStrAbilities ? new StrengthAbilityShortcut(
									fromSlot) : new MeleeAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1458
						&& (fromComponentId == 33 || fromComponentId == 34)) {
					player.getActionbar().setShortcut1(
							(toComponentId - 11) / 13,
							new PrayerShortcut(fromSlot, player.getPrayer()
									.isAncientCurses()));
				}
			} // end

			else if (toInterfaceId == 1671
					&& (toComponentId >= 8 && toComponentId <= 177)) { // SECOND
																		// MULTI-ACTIONBAR
				if (fromInterfaceId == 1671
						&& (fromComponentId >= 8 && fromComponentId <= 177))
					player.getActionbar().switchShortcut2(
							(fromComponentId - 8) / 13,
							(toComponentId - 8) / 13);
				else if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						|| fromInterfaceId == Inventory.INVENTORY_INTERFACE_2) {
					if (fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					Item item = player.getInventory().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							new ItemShortcut(item.getId()));
				} else if ((fromInterfaceId == 1464 && fromComponentId == 15)
						|| (fromInterfaceId == 1464 && fromComponentId == 14)) {
					if (fromSlot >= player.getEquipment().getItems().getSize())
						return;
					Item item = player.getEquipment().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							new ItemShortcut(item.getId()));
				} else if (fromInterfaceId == 1461 && fromComponentId == 1) {
					if (Magic.getSpellData(fromSlot) == null) // fake spell
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							new MagicAbilityShortcut(fromSlot));

				} else if (fromInterfaceId == 1449 && fromComponentId == 1) {
					boolean usingDefenceAbilities = player
							.getCombatDefinitions().onDefenceMenu();
					if (ActionBar
							.getAbilityData(
									usingDefenceAbilities ? ActionBar.DEFENCE_ABILITY_SHORTCUT
											: ActionBar.HEAL_ABILITY_SHORTCUT,
									fromSlot) == null)
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							usingDefenceAbilities ? new DefenceAbilityShortcut(
									fromSlot) : new HealAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1452 && fromComponentId == 1) {
					if (ActionBar.getAbilityData(
							ActionBar.RANGED_ABILITY_SHORTCUT, fromSlot) == null)
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							new RangeAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1460 && fromComponentId == 1) {
					boolean usingStrAbilities = player.getCombatDefinitions()
							.onStrengthMenu();
					if (ActionBar
							.getAbilityData(
									usingStrAbilities ? ActionBar.STRENGTH_ABILITY_SHORTCUT
											: ActionBar.MELEE_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						return;
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							usingStrAbilities ? new StrengthAbilityShortcut(
									fromSlot) : new MeleeAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1458
						&& (fromComponentId == 33 || fromComponentId == 34)) {
					player.getActionbar().setShortcut2(
							(toComponentId - 8) / 13,
							new PrayerShortcut(fromSlot, player.getPrayer()
									.isAncientCurses()));
				}
			} // end

			if (toInterfaceId == 1672
					&& (toComponentId >= 7 && toComponentId <= 176)) { // THIRD
																		// MULTI-ACTIONBAR
				if (fromInterfaceId == 1672
						&& (fromComponentId >= 7 && fromComponentId <= 176))
					player.getActionbar().switchShortcut3(
							(fromComponentId - 7) / 13,
							(toComponentId - 7) / 13);
				else if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						|| fromInterfaceId == Inventory.INVENTORY_INTERFACE_2) {
					if (fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					Item item = player.getInventory().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							new ItemShortcut(item.getId()));
				} else if ((fromInterfaceId == 1464 && fromComponentId == 15)
						|| (fromInterfaceId == 1464 && fromComponentId == 14)) {
					if (fromSlot >= player.getEquipment().getItems().getSize())
						return;
					Item item = player.getEquipment().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							new ItemShortcut(item.getId()));
				} else if (fromInterfaceId == 1461 && fromComponentId == 1) {
					if (Magic.getSpellData(fromSlot) == null) // fake spell
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							new MagicAbilityShortcut(fromSlot));

				} else if (fromInterfaceId == 1449 && fromComponentId == 1) {
					boolean usingDefenceAbilities = player
							.getCombatDefinitions().onDefenceMenu();
					if (ActionBar
							.getAbilityData(
									usingDefenceAbilities ? ActionBar.DEFENCE_ABILITY_SHORTCUT
											: ActionBar.HEAL_ABILITY_SHORTCUT,
									fromSlot) == null)
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							usingDefenceAbilities ? new DefenceAbilityShortcut(
									fromSlot) : new HealAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1452 && fromComponentId == 1) {
					if (ActionBar.getAbilityData(
							ActionBar.RANGED_ABILITY_SHORTCUT, fromSlot) == null)
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							new RangeAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1460 && fromComponentId == 1) {
					boolean usingStrAbilities = player.getCombatDefinitions()
							.onStrengthMenu();
					if (ActionBar
							.getAbilityData(
									usingStrAbilities ? ActionBar.STRENGTH_ABILITY_SHORTCUT
											: ActionBar.MELEE_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						return;
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							usingStrAbilities ? new StrengthAbilityShortcut(
									fromSlot) : new MeleeAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1458
						&& (fromComponentId == 33 || fromComponentId == 34)) {
					player.getActionbar().setShortcut3(
							(toComponentId - 7) / 13,
							new PrayerShortcut(fromSlot, player.getPrayer()
									.isAncientCurses()));
				}
			} // end
			if (toInterfaceId == 1673
					&& (toComponentId >= 7 && toComponentId <= 176)) { // FOURTH
																		// MULTI-ACTIONBAR
				if (fromInterfaceId == 1673
						&& (fromComponentId >= 11 && fromComponentId <= 180))
					player.getActionbar().switchShortcut4(
							(fromComponentId - 7) / 13,
							(toComponentId - 7) / 13);
				else if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						|| fromInterfaceId == Inventory.INVENTORY_INTERFACE_2) {
					if (fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					Item item = player.getInventory().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							new ItemShortcut(item.getId()));
				} else if ((fromInterfaceId == 1464 && fromComponentId == 15)
						|| (fromInterfaceId == 1464 && fromComponentId == 14)) {
					if (fromSlot >= player.getEquipment().getItems().getSize())
						return;
					Item item = player.getEquipment().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							new ItemShortcut(item.getId()));
				} else if (fromInterfaceId == 1461 && fromComponentId == 1) {
					if (Magic.getSpellData(fromSlot) == null) // fake spell
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							new MagicAbilityShortcut(fromSlot));

				} else if (fromInterfaceId == 1449 && fromComponentId == 1) {
					boolean usingDefenceAbilities = player
							.getCombatDefinitions().onDefenceMenu();
					if (ActionBar
							.getAbilityData(
									usingDefenceAbilities ? ActionBar.DEFENCE_ABILITY_SHORTCUT
											: ActionBar.HEAL_ABILITY_SHORTCUT,
									fromSlot) == null)
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							usingDefenceAbilities ? new DefenceAbilityShortcut(
									fromSlot) : new HealAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1452 && fromComponentId == 1) {
					if (ActionBar.getAbilityData(
							ActionBar.RANGED_ABILITY_SHORTCUT, fromSlot) == null)
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							new RangeAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1460 && fromComponentId == 1) {
					boolean usingStrAbilities = player.getCombatDefinitions()
							.onStrengthMenu();
					if (ActionBar
							.getAbilityData(
									usingStrAbilities ? ActionBar.STRENGTH_ABILITY_SHORTCUT
											: ActionBar.MELEE_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						return;
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							usingStrAbilities ? new StrengthAbilityShortcut(
									fromSlot) : new MeleeAbilityShortcut(
									fromSlot));
				} else if (fromInterfaceId == 1458
						&& (fromComponentId == 33 || fromComponentId == 34)) {
					player.getActionbar().setShortcut4(
							(toComponentId - 7) / 13,
							new PrayerShortcut(fromSlot, player.getPrayer()
									.isAncientCurses()));
				}
			} // end

			if ((fromInterfaceId == Inventory.INVENTORY_INTERFACE || fromInterfaceId == Inventory.INVENTORY_INTERFACE_2)
					&& (toInterfaceId == Inventory.INVENTORY_INTERFACE || toInterfaceId == Inventory.INVENTORY_INTERFACE_2)) {
				if (toSlot < 0
						|| toSlot >= player.getInventory()
								.getItemsContainerSize()
						|| fromSlot >= player.getInventory()
								.getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if ((fromInterfaceId == Inventory.INVENTORY_INTERFACE || fromInterfaceId == Inventory.INVENTORY_INTERFACE_2)
					&& ((toInterfaceId == 1462 && toComponentId == 14) || (toInterfaceId == 1464 && toComponentId == 15))) {
				if (fromSlot >= player.getInventory().getItemsContainerSize()
						|| player.getInterfaceManager()
								.containsInventoryInter())
					return;
				Item item = player.getInventory().getItem(fromSlot);
				if (item == null)
					return;
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(item
						.getId());
				if (!defs.isWearItem())
					return;
				InventoryOptionsHandler.handleItemOption2(player, fromSlot,
						item.getId(), item);
			} else if (((fromInterfaceId == 1462 && fromComponentId == 14) || (fromInterfaceId == 1464 && fromComponentId == 15))
					&& (toInterfaceId == Inventory.INVENTORY_INTERFACE || toInterfaceId == Inventory.INVENTORY_INTERFACE_2)) {
				if (fromSlot >= player.getEquipment().getItems().getSize()
						|| player.getInterfaceManager()
								.containsInventoryInter())
					return;

				Item item = player.getEquipment().getItem(fromSlot);
				if (item == null)
					return;
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(item
						.getId());
				if (!defs.isWearItem())
					return;
				player.getEquipment()
						.handleEquipment(defs.getEquipSlot(), item.getId(),
								WorldPacketsDecoder.ACTION_BUTTON1_PACKET);
			} else if (toInterfaceId == 1430
					&& (toComponentId >= 57 && toComponentId <= 229)
					|| (toInterfaceId == 1436 && (toComponentId >= 25 && toComponentId <= 194))) { // ability
				// bar
				// switch shortcut
				if (fromInterfaceId == 1430
						&& (fromComponentId >= 57 && fromComponentId <= 231)
						|| (fromInterfaceId == 1436 && (fromComponentId >= 25 && fromComponentId <= 194)))
					player.getActionbar()
							.switchShortcut(
									(fromComponentId - (fromInterfaceId == 1430 ? 57
											: 25)) / 13,
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13);
				// item shortcut inv
				else if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
						|| fromInterfaceId == Inventory.INVENTORY_INTERFACE_2) {
					if (fromSlot >= player.getInventory()
							.getItemsContainerSize())
						return;
					Item item = player.getInventory().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									new ItemShortcut(item.getId()));
					// item shortcut equip
				} else if ((fromInterfaceId == 1464 && fromComponentId == 15)
						|| (fromInterfaceId == 1464 && fromComponentId == 14)) {
					if (fromSlot >= player.getEquipment().getItems().getSize())
						return;
					Item item = player.getEquipment().getItem(fromSlot);
					if (item == null || item.getId() != fromSlotId2)
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									new ItemShortcut(item.getId()));
					// spell shortcut
					// Switch item 1464, 1430, 3, 65535, 15, 142
				} else if (fromInterfaceId == 1461 && fromComponentId == 1) {
					if (Magic.getSpellData(fromSlot) == null) // fake spell
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									new MagicAbilityShortcut(fromSlot));

				} else if (fromInterfaceId == 1449 && fromComponentId == 1) {
					boolean usingDefenceAbilities = player
							.getCombatDefinitions().onDefenceMenu();
					if (ActionBar
							.getAbilityData(
									usingDefenceAbilities ? ActionBar.DEFENCE_ABILITY_SHORTCUT
											: ActionBar.HEAL_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						// spell
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									usingDefenceAbilities ? new DefenceAbilityShortcut(
											fromSlot)
											: new HealAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1452 && fromComponentId == 1) {
					if (ActionBar.getAbilityData(
							ActionBar.RANGED_ABILITY_SHORTCUT, fromSlot) == null) // fake
						// spell
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									new RangeAbilityShortcut(fromSlot));
					// prayer shortcut
				} else if (fromInterfaceId == 1460 && fromComponentId == 1) {
					boolean usingStrAbilities = player.getCombatDefinitions()
							.onStrengthMenu();
					if (ActionBar
							.getAbilityData(
									usingStrAbilities ? ActionBar.STRENGTH_ABILITY_SHORTCUT
											: ActionBar.MELEE_ABILITY_SHORTCUT,
									fromSlot) == null) // fake
						// spell
						return;
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									usingStrAbilities ? new StrengthAbilityShortcut(
											fromSlot)
											: new MeleeAbilityShortcut(fromSlot));
				} else if (fromInterfaceId == 1458 && fromComponentId == 33) {
					player.getActionbar()
							.setShortcut(
									(toComponentId - (toInterfaceId == 1430 ? 57
											: 25)) / 13,
									new PrayerShortcut(fromSlot, player
											.getPrayer().isAncientCurses()));
				}
			} else if (fromInterfaceId == 1430
					&& (fromComponentId >= 57 && fromComponentId <= 229)
					|| (fromInterfaceId == 1436 && (fromComponentId >= 25 && fromComponentId <= 194))) {
				if ((toInterfaceId == InterfaceManager.RESIZABLE_WINDOW_ID && toComponentId == 17))
					player.getActionbar().clearShortcut(
							(fromComponentId - (fromInterfaceId == 1430 ? 57
									: 25)) / 13);
			} else if ((fromInterfaceId == Inventory.INVENTORY_INTERFACE || fromInterfaceId == Inventory.INVENTORY_INTERFACE_2)
					&& (toInterfaceId == InterfaceManager.RESIZABLE_WINDOW_ID && toComponentId == 17)) {
				if (fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				Item item = player.getInventory().getItem(fromSlot);
				if (item == null || item.getId() != fromSlotId2)
					return;
				InventoryOptionsHandler.handleItemOption8(player, fromSlot,
						fromSlotId2, item);
				System.out.println("Switch item " + fromInterfaceId + ", "
						+ toInterfaceId + ", " + fromSlot + ", " + toSlot
						+ ", " + fromComponentId + ", " + toComponentId);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				if (fromComponentId == 10 && toComponentId == 10) {
					if (player.isOnBoBWindow) {
						if (toSlot >= player.getFamiliar().getBob()
								.getItemsContainerSize()
								|| fromSlot >= player.getFamiliar().getBob()
										.getItemsContainerSize())
							return;
						player.getFamiliar().getBob()
								.switchItem(fromSlot, toSlot);
					} else {
						if (toSlot >= player.getInventory()
								.getItemsContainerSize()
								|| fromSlot >= player.getInventory()
										.getItemsContainerSize())
							return;
						player.getInventory().switchItem(fromSlot, toSlot);
					}
				} else if (fromComponentId == 243 && toComponentId == 243)
					player.getBank().switchItem(fromSlot, toSlot);
				else if (fromComponentId == 243 && toComponentId == 10) {
					if (player.isOnBoBWindow)
						player.getBank().withdrawBoBItem(fromSlot, 1);
					else
						player.getBank().withdrawItem(fromSlot, 1);
				} else if ((fromComponentId == 10 && toComponentId == 243)
						|| (fromComponentId == 10 && toComponentId >= 281 && toComponentId <= 289)) {
					if (player.isOnBoBWindow)
						player.getBank().depositBoBItem(fromSlot, 1, false);
					else
						player.getBank().depositItem(fromSlot, 1, false);
				} else if (fromComponentId == 243
						&& (toComponentId >= 148 && toComponentId <= 212)) {
					player.getBank().switchItem(fromSlot, toSlot,
							fromComponentId, toComponentId);
				} // 103 is edit mode
			} else if (fromInterfaceId == 1265
					&& toInterfaceId == 1266
					&& player.getTemporaryAttributtes().get("is_buying") != null) {
				if ((boolean) player.getTemporaryAttributtes().get("is_buying") == true) {
					Shop shop = (Shop) player.getTemporaryAttributtes().get(
							"shop_instance");
					if (shop == null)
						return;
					// shop.buyItem(player, fromSlot, 1);
				}
			} else if ((fromInterfaceId == 34 || fromInterfaceId == 1417)
					&& (toInterfaceId == 34 || toInterfaceId == 1417))
				player.getNotes().switchNotes(fromSlot, toSlot);
		} else if (opcode == GARBAGE_CLEAR_PACKET
				|| opcode == DONE_LOADING_REGION_PACKET) {
			if (!player.isRunAfterLoad())
				player.runAfterLoad();
			if (!player.clientHasLoadedMapRegionFinished()) {
				// load objects and items here
				player.setClientHasLoadedMapRegion();
				player.refreshSpawnedObjects();
				player.refreshSpawnedItems();
			}
		} else if (opcode == WALKING_PACKET || opcode == MINI_WALKING_PACKET
				|| opcode == GROUND_ITEM_OPTION_2 || opcode == ITEM_TAKE_PACKET
				|| opcode == PLAYER_OPTION_2_PACKET
				|| opcode == PLAYER_OPTION_3_PACKET
				|| opcode == PLAYER_OPTION_4_PACKET
				|| opcode == PLAYER_OPTION_6_PACKET
				|| opcode == PLAYER_OPTION_9_PACKET
				|| opcode == PLAYER_OPTION_1_PACKET
				|| opcode == ATTACK_NPC_PACKET || opcode == INTERFACE_ON_PLAYER
				|| opcode == INTERFACE_ON_NPC || opcode == NPC_CLICK1_PACKET
				|| opcode == NPC_CLICK2_PACKET || opcode == NPC_CLICK3_PACKET
				|| opcode == NPC_CLICK4_PACKET
				|| opcode == OBJECT_CLICK1_PACKET
				|| opcode == SWITCH_INTERFACE_COMPONENTS_PACKET
				|| opcode == OBJECT_CLICK2_PACKET
				|| opcode == OBJECT_CLICK3_PACKET
				|| opcode == OBJECT_CLICK4_PACKET
				|| opcode == OBJECT_CLICK5_PACKET
				|| opcode == INTERFACE_ON_OBJECT) {
			if (!player.isRunning())
				return;
			player.afk = Utils.currentTimeMillis()
					+ (Settings.DEBUG ? Integer.MAX_VALUE : 15 * 60 * 1000);
			player.addLogicPacketToQueue(new LogicPacket(opcode, stream));
		} else if (opcode == OBJECT_EXAMINE_PACKET) {
			ObjectHandler.handleOption(player, stream, -1);
		} else if (opcode == NPC_EXAMINE_PACKET) {
			NPCHandler.handleExamine(player, stream);
		} else if (opcode == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;

			String str = stream.getLength() == 0 ? null : stream.readString();
			if (str == null)
				FriendsChat.requestLeave(player);
			else
				FriendsChat.requestJoin(player,
						Utils.formatPlayerNameForDisplay(str));
		} else if (opcode == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getCurrentFriendsChat() != null)
				player.getCurrentFriendsChat().kickMember(player,
						Utils.formatPlayerNameForDisplay(stream.readString()));
		} else if (opcode == SET_FC_RANK_PACKET) {
			player.getPackets().sendGameMessage(
					"This feature is currently disabled.");
			// Todo
		} else if (opcode == KICK_CLAN_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			boolean guest = stream.readByte() == 1;
			if (!guest)
				return;
			stream.readUnsignedShort();
			player.kickPlayerFromClanChannel(stream.readString());
		} else if (opcode == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted()
					|| !player.getInterfaceManager().containsInterface(1108))
				return;
			player.getFriendsIgnores().changeRank(
					Utils.formatPlayerNameForDisplay(stream.readString()),
					stream.readUnsignedByteC());
		} else if (opcode == UPDATE_GAMEBAR_PACKET) {
			if (!player.hasStarted())
				return;
			int public_ = stream.readUnsignedByte();
			int private_ = stream.readUnsignedByte();
			int trade = stream.readUnsignedByte();
			if (!player.isLobby()) {
				player.setPublicStatus(public_);
				player.setTradeStatus(trade);
			}
			player.getFriendsIgnores().setPmStatus(private_, true);
		} else if (opcode == ADD_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addFriend(
					Utils.formatPlayerNameForDisplay(stream.readString()));
		} else if (opcode == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeFriend(
					Utils.formatPlayerNameForDisplay(stream.readString()));
		} else if (opcode == ADD_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addIgnore(
					Utils.formatPlayerNameForDisplay(stream.readString()),
					stream.readUnsignedByte() == 1);
		} else if (opcode == REMOVE_IGNORE_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeIgnore(
					Utils.formatPlayerNameForDisplay(stream.readString()));
		} else if (opcode == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted())
				return;
			String target = stream.readString();
			String message = Huffman.decodeString(150, stream);
			if (message.contains(player.getSession().getIP())) {
				player.getPackets()
						.sendGameMessage(
								"You appear to be telling someone your IP - please don't!");
				return;
			}
			if (target == null) {
				player.getPackets().sendGameMessage(
						"This player appears to be offline.");
				return;
			}
			player.getFriendsIgnores().sendPrivateMessage(target, message);
			final String FILE_PATH = Settings.getDropboxLocation()
					+ "logs/chat/private/";
			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"MM/dd/yy HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						FILE_PATH + player.getUsername() + ".txt", true));
				writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: "
						+ player.getSession().getIP() + "]: to " + target + " "
						+ message);
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (opcode == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);

			int quickChatType = stream.readUnsignedByte(); // quickchat does not
			// use chattype as
			// it's only
			// temporary!!!

			int qcFileId = stream.readUnsignedShort();

			long[] data = null;
			QuickChatOptionDefinition option = QuickChatOptionDefinition
					.loadOption(qcFileId);
			if (option.dynamicDataTypes != null) {
				data = new long[option.dynamicDataTypes.length];
				for (int i = 0; i < option.dynamicDataTypes.length; i++) {
					if (option.getType(i).clientToServerBytes > 0) {
						data[i] = stream
								.readDynamic(option.getType(i).clientToServerBytes);
					}

				}
			}
			if (quickChatType == 0)
				player.sendPublicChatMessage(new QuickChatMessage(player,
						option, data));
			else if (quickChatType == 1) {
				if (player.getCurrentFriendsChat() != null)
					player.getCurrentFriendsChat().sendMessage(player, option,
							data);
			} else if (quickChatType == 2)
				player.sendClanChannelQuickMessage(new QuickChatMessage(player,
						option, data));
			else if (quickChatType == 3)
				player.sendGuestClanChannelQuickMessage(new QuickChatMessage(
						player, option, data));
			else if (Settings.DEBUG)
				Logger.log(this, "Unknown chat type: " + quickChatType);
		} else if (opcode == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (opcode == CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.afk = Utils.currentTimeMillis()
					+ (Settings.DEBUG ? Integer.MAX_VALUE : 15 * 60 * 1000);
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.decodeString(200, stream);
			if (message.contains("gang") || message.contains("scape")) {
				String msg = message.replaceAll(
						message.contains("gang") ? "shit" : "shit", "shit");
				int effects = (colorEffect << 8) | (moveEffect & 0xff);
				if ((effects & 0x8000) != 0)
					return;
				if (chatType == 1) {
					if (player.getCurrentFriendsChat() != null) {
						if (msg.equals("[Attempting to kick/ban user from this Friends Chat.]"))
							return;
						player.getCurrentFriendsChat().sendMessage(player, msg);
					}
				} else if (chatType == 2)
					player.sendClanChannelMessage(new ChatMessage(msg));
				else if (chatType == 3)
					player.sendGuestClanChannelMessage(new ChatMessage(msg));
				else {
					if (player.getControllerManager().getController() != null
							&& player.getControllerManager().getController() instanceof DungeonController) {
						for (Player party : player.getDungManager().getParty()
								.getTeam()) {
							party.getPackets().sendPublicMessage(player,
									new PublicChatMessage(msg, effects));
						}
					} else
						player.sendPublicChatMessage(new PublicChatMessage(msg,
								effects));
				}
				return;
			}
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (message.startsWith("::") || message.startsWith(";;")) {
				// if command exists and processed wont send message as public
				// message
				DeveloperConsole.processCommand(player,
						message.replace("::", "").replace(";;", ""), false,
						false);
				return;
			}
			if (player.isMuted()) {
				player.getPackets()
						.sendGameMessage(
								"You have been temporarily muted due to breaking a rule.");
				player.getPackets().sendGameMessage(
						"This mute will remain for a further "
								+ player.getMutedFor() + " hours.");
				player.getPackets().sendGameMessage(
						"To prevent further mutes please read the rules.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if ((effects & 0x8000) != 0)
				return; // someone
						// trying
						// to
						// crash
						// server
						// using
						// qc
						// as
						// chat
			// effect in normal chat
			if (message.contains(player.getSession().getIP())) {
				player.getPackets()
						.sendGameMessage(
								"You appear to be telling someone your IP - please don't!");
				return;
			}
			if (chatType == 1) {
				if (player.getCurrentFriendsChat() != null) {
					if (message
							.equals("[Attempting to kick/ban user from this Friends Chat.]"))
						return;
					player.getCurrentFriendsChat().sendMessage(player, message);
				}
			} else if (chatType == 2)
				player.sendClanChannelMessage(new ChatMessage(message));
			else if (chatType == 3)
				player.sendGuestClanChannelMessage(new ChatMessage(message));
			else {
				// Think i also fixed the large view scene thing, but just
				// incase, spoof message
				if (player.getControllerManager().getController() != null
						&& player.getControllerManager().getController() instanceof DungeonController) {
					for (Player party : player.getDungManager().getParty()
							.getTeam()) {
						/*
						 * if (player.getLocalPlayerUpdate().getLocalPlayers()[
						 * party.getIndex()] == null ||
						 * party.getLocalPlayerUpdate().getLocalPlayers()[player
						 * .getIndex()] == null) {
						 * party.getPackets().sendGameMessage(player.
						 * getDisplayName() + ":<col=7fa9ff> " + message); }
						 */
						party.getPackets().sendPublicMessage(player,
								new PublicChatMessage(message, effects));
					}
				} else
					player.sendPublicChatMessage(new PublicChatMessage(message,
							effects));
			}
			final String FILE_PATH = Settings.getDropboxLocation()
					+ "logs/chat/public/";
			try {
				DateFormat dateFormat = new SimpleDateFormat(
						"MM/dd/yy HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						FILE_PATH + player.getUsername() + ".txt", true));
				writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: "
						+ player.getSession().getIP() + "] : " + message);
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (opcode == COMMANDS_PACKET) {
			if (!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			if (!DeveloperConsole.processCommand(player, command, true,
					clientCommand) && Settings.DEBUG)
				Logger.log(this, "Command: " + command);
		} else if (opcode == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player,
						colorId);
			else if (player.getTemporaryAttributtes().get("MottifCustomize") != null)
				ClansManager.setMottifColor(player, colorId);
			else if (player.getTemporaryAttributtes().remove(
					Key.COSTUME_COLOR_CUSTOMIZE) != null)
				SkillCapeCustomizer.handleCostumeColor(player, colorId);
		} else if (opcode == REPORT_ABUSE_PACKET) {
			if (!player.hasStarted())
				return;
			String displayName = stream.readString();
			int type = stream.readUnsignedByte();
			boolean mute = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			String unknown2 = stream.readString();
			PlayerReporting.report(player, displayName, type, mute);
		} else if (opcode == FORUM_THREAD_ID_PACKET) {
			String threadId = stream.readString();
			if (player.getInterfaceManager().containsInterface(1100))
				ClansManager.setThreadIdInterface(player, threadId);
			else if (Settings.DEBUG)
				Logger.log(this, "Called FORUM_THREAD_ID_PACKET: " + threadId);
		} else if (opcode == OPEN_URL_PACKET) {
			String type = stream.readString();
			String path = stream.readString();
			String unknown = stream.readString();
			int flag = stream.readUnsignedByte();
			if (Settings.DEBUG)
				Logger.log(WorldPacketsDecoder.class, "openUrl(" + type + ","
						+ path + "," + unknown + "," + flag + ")");
			if (path.contains("messages"))
				player.getPackets()
						.sendOpenURL(
								"https://nocturne3.org/community/index.php?app=members&module=messaging");
			else if (path.contains("RuneScape_Wiki_-_Game_Guides_%26_Support"))
				player.getPackets().sendOpenURL(
						"http://runescape.wikia.com/wiki/RuneScape_Wiki");
			else if (path.contains("forums.ws?94,95"))
				player.getPackets()
						.sendOpenURL(
								"https://nocturne3.org/community/index.php?/forum/38-clan-recruitment/");
			else if (path.contains("set_members_dob")
					|| path.contains("userdetails"))
				player.getPackets().sendOpenURL(Settings.STORE_LINK);
			else if (path.contains("title.ws")
					|| path.toLowerCase().contains("squeal")
					|| path.toLowerCase().contains("treasure hunter"))
				player.getPackets().sendOpenURL(Settings.FORUMS_LINK);
		} else if (opcode == GRAND_EXCHANGE_ITEM_SELECT_PACKET) {
			int itemId = stream.readUnsignedShort();
			player.getGeManager().chooseItem(itemId);
		} else if (opcode == 30) {
			switch (stream.getLength()) {
			case 24:
				player.getPackets()
						.sendOpenURL(
								"https://nocturne3.org/community/index.php?/forum/38-clan-recruitment/");
				break;
			}
		} else {
			System.out.println("Unhandled Packet: " + opcode);
			if (Settings.DEBUG)
				Logger.log(this, "Missing packet " + opcode + ", length: "
						+ stream.getLength() + ", sizedef: "
						+ PACKET_SIZES[opcode]);
		}
	}
}
