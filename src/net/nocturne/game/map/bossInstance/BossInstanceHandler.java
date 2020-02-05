package net.nocturne.game.map.bossInstance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.nocturne.Settings;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.map.bossInstance.impl.*;
import net.nocturne.game.map.bossInstance.impl.godwars.CommanderZilyanaInstance;
import net.nocturne.game.map.bossInstance.impl.godwars.GeneralGraadorInstance;
import net.nocturne.game.map.bossInstance.impl.godwars.KreeArraInstance;
import net.nocturne.game.map.bossInstance.impl.godwars.KrilTsutsarothInstance;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public class BossInstanceHandler {

	public static final Object LOCK = new Object();

	public static enum Boss {

		Agoroth(AgorothInstance.class, -1, 250000, 200, false, true,
				Settings.HOME_LOCATION, new WorldTile(3895, 6816, 0), null,
				"BossInstanceController", 617),

		King_Black_Dragon(KingBlackDragonInstance.class, 30558, 50000, 200,
				false, true, new WorldTile(3051, 3520, 0), new WorldTile(2273,
						4681, 0), null, "BossInstanceController", 14),

		Kalphite_Queen(KalphiteQueenInstance.class, 30554, 100000, 200, false,
				true, new WorldTile(3446, 9496, 0),
				new WorldTile(3508, 9494, 0), null,
				"KalphiteQueenInstanceController", 212),

		Exiled_Kalphite_Queen(ExiledKalphiteQueenInstance.class, 30555, 100000,
				200, false, true, new WorldTile(2995, 1617, 0), new WorldTile(
						2967, 1711, 0), null,
				"KalphiteQueenInstanceController", 212),

		Dagannoth_Kings(DagannothKingsInstance.class, 30557, 150000, 200,
				false, true, new WorldTile(1912, 4367, 0), new WorldTile(2900,
						4449, 0), null, "DagannothKingsInstanceController", 365),

		Corporeal_Beast(CorporealBeastInstance.class, 30556, 300000, 200,
				false, true, new WorldTile(2970, 4384, 2), new WorldTile(2974,
						4384, 2), null, "CorporealBeastInstanceController", 617),

		General_Graador(GeneralGraadorInstance.class, 15307, 200000, 200,
				false, true, new WorldTile(2862, 5357, 0), new WorldTile(2863,
						5357, 0), null, "GeneralGraadorInstance", 617),

		Kree_Arra(KreeArraInstance.class, 19429, 200000, 200, false, true,
				new WorldTile(2835, 5294, 0), new WorldTile(2835, 5295, 0),
				null, "KreeArraInstance", 617),

		Commander_Zilyana(CommanderZilyanaInstance.class, 19427, 200000, 200,
				false, true, new WorldTile(2923, 5257, 0), new WorldTile(2923,
						5256, 0), null, "CommanderZilyanaInstance", 617),

		Kril_Tsutsaroth(KrilTsutsarothInstance.class, 19428, 200000, 200,
				false, true, new WorldTile(2925, 5333, 0), new WorldTile(2925,
						5332, 0), null, "KrilTsutsarothInstance", 617),

		Evil_Chicken(EvilChickenInstance.class, -1, 300000, 200, false, true,
				new WorldTile(2643, 10417, 0), new WorldTile(2643, 10418, 0),
				null, "EvilChickenInstanceController", 617),

		Kalphite_King(KalphiteKingInstance.class, 28664, 0, 200, false, false,
				new WorldTile(2971, 1656, 0), new WorldTile(2974, 1746, 0),
				null, "KalphiteKingInstanceController", 1140),

		Vorago(VoragoInstance.class, 19829, 1000000, 50, true, true,
				new WorldTile(2972, 3431, 0), new WorldTile(3043, 6100, 0),
				new WorldTile(3072, 6176, 0), "VoragoInstanceController", 1155), Rise_Of_The_Six(
				RotsInstance.class, 24181, 1, 5, false, false, new WorldTile(
						3542, 3310, 0), new WorldTile(3543, 3310, 0),
				new WorldTile(3542, 3310, 0), "RiseOfTheSixController", 1155);

		private final Map<String, BossInstance> cachedInstances = Collections
				.synchronizedMap(new HashMap<String, BossInstance>());
		private Class<? extends BossInstance> instance;
		private int initialCost, maxPlayers, structure, musicId;
		private boolean hasHM, publicVersion;
		private WorldTile insideTile, outsideTile, graveStoneTile;
		private String controllerName;

		private Boss(Class<? extends BossInstance> instance, int structure,
				int initialCost, int maxPlayers, boolean hasHM,
				boolean publicVersion, WorldTile outsideTile,
				WorldTile insideTile, WorldTile graveStoneTile,
				String controllerName, int musicId) {
			this.instance = instance;
			this.initialCost = initialCost;
			this.maxPlayers = maxPlayers;
			this.hasHM = hasHM;
			this.publicVersion = publicVersion;
			this.insideTile = insideTile;
			this.outsideTile = outsideTile;
			this.graveStoneTile = graveStoneTile;
			this.controllerName = controllerName;
			this.musicId = musicId;
			this.structure = structure;
		}

		public Map<String, BossInstance> getCachedInstances() {
			return cachedInstances;
		}

		public String getControllerName() {
			return controllerName;
		}

		public WorldTile getInsideTile() {
			return insideTile;
		}

		public WorldTile getOutsideTile() {
			return outsideTile;
		}

		public WorldTile getGraveStoneTile() {
			return graveStoneTile;
		}

		public boolean isHasHM() {
			return hasHM;
		}

		public int getStructure() {
			return structure;
		}

		public int getMaxPlayers() {
			return maxPlayers;
		}

		public int getInitialCost() {
			return initialCost;
		}

		public int getMusicId() {
			return musicId;
		}

		public boolean hasPublicVersion() {
			return publicVersion;
		}

	}

	public static void enterInstance(Player player, Boss boss) {
		player.getDialogueManager().startDialogue("BossInstanceD", boss);
	}

	private static void createInstance(Player player, Boss boss,
			int maxPlayers, int minCombat, int spawnSpeed, int protection,
			boolean practiceMode, boolean hardMode) {
		createInstance(player, new InstanceSettings(boss, maxPlayers,
				minCombat, spawnSpeed, protection, practiceMode, hardMode));
	}

	public static void createInstance(Player player, InstanceSettings settings) {
		synchronized (LOCK) {

			try {
				String key = player == null ? "" : player.getUsername();
				BossInstance instance = findInstance(settings.getBoss(), key);
				if (instance == null) {
					if (player == null && !settings.getBoss().publicVersion) {
						if (Settings.DEBUG)
							Logger.log(BossInstanceHandler.class,
									"Not a public instance. Can't create it.");
						return;
					}
					instance = settings.getBoss().instance
							.getDeclaredConstructor(Player.class,
									InstanceSettings.class).newInstance(player,
									settings);
					settings.getBoss().cachedInstances.put(key, instance);
				} else {
					// recreating the instance but not gonna replace settings
					// since already exists(instead, increase time)
					settings.setCreationTime(Utils.currentTimeMillis());
					joinInstance(player, settings.getBoss(), key, false); // enter
					// the
					// instance
					// normally
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}

	/*
	 * login means reloging in a public instance(u cant login into private, but
	 * lets keep this in case rs lets u in future)
	 */
	public static BossInstance joinInstance(Player player, Boss boss,
			String key, boolean login) {
		synchronized (LOCK) {
			BossInstance instance = findInstance(boss, key);
			if (instance == null) { // not username
				Player owner = World.getPlayerByDisplayName(key);
				if (owner != null) {
					key = owner.getUsername();
					instance = findInstance(boss, key);
				}
			}

			if (instance == null) {
				if (key.equals("")) { // supposed to be public instance
					player.getPackets().sendGameMessage(
							"This boss has no public instance.");
					return null;
				}
				player.getPackets().sendGameMessage(
						"That player is offline, or has privacy mode enabled.");
				return null;
			}
			// loading
			if (!instance.isInstanceReady())
				return null;
			if (!key.equals("") && !player.getUsername().equals(key)) {
				if (instance.getSettings().getMinCombat() > player.getSkills()
						.getCombatLevel()) {
					player.getPackets()
							.sendGameMessage(
									"Your combat level is too low to enter this session.");
					return null;
				}
				if (instance.getSettings().getProtection() == BossInstance.FRIENDS_ONLY) {
					Player owner = World.getPlayer(key);
					if (owner == null
							|| !owner.getFriendsIgnores().isFriend(
									player.getDisplayName())) {
						player.getPackets()
								.sendGameMessage(
										"That player is offline, or has privacy mode enabled.");
						return null;
					}
				}
				if (instance.getSettings().getMaxPlayers() - 1 <= instance
						.getPlayersCount()) {
					player.getPackets().sendGameMessage(
							"This instance is full.");
					return null;
				}
			}
			instance.enterInstance(player, login);
			return instance;
		}
	}

	public static BossInstance findInstance(Boss boss, String key) {
		synchronized (LOCK) {
			return boss.cachedInstances.get(key);
		}
	}

	public static final void init() {
		for (Boss boss : Boss.values()) {
			if (!boss.publicVersion)
				continue;
			try {
				createInstance(null, boss, boss.maxPlayers, 1,
						BossInstance.STANDARD, BossInstance.FFA, false, false);
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}
	}
}