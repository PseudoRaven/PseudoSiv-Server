package net.nocturne.game.player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.executor.PlayerHandlerThread;
import net.nocturne.executor.WorldThread;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.Region;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.ItemsContainer;
import net.nocturne.game.item.actions.PrizedPendant.Pendants;
import net.nocturne.game.map.bossInstance.InstanceSettings;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.others.GraveStone;
import net.nocturne.game.npc.others.MirrorBackSpider;
import net.nocturne.game.npc.others.Pet;
import net.nocturne.game.npc.pet.LegendaryPetAbilities;
import net.nocturne.game.npc.pet.PetManager;
import net.nocturne.game.npc.pet.Pets;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.actions.PlayerCombat;
import net.nocturne.game.player.actions.skills.construction.House;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.actions.skills.prayer.Prayer;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.player.content.DwarfMultiCannon.CannonType;
import net.nocturne.game.player.content.FairyRings;
import net.nocturne.game.player.content.FriendsChat;
import net.nocturne.game.player.content.Notes;
import net.nocturne.game.player.content.SkillCapeCustomizer;
import net.nocturne.game.player.content.activities.clans.ClansManager;
import net.nocturne.game.player.content.activities.dailychallenges.DailyTasks;
import net.nocturne.game.player.content.activities.dailychallenges.DailyTasks.Tasks;
import net.nocturne.game.player.content.activities.distractions.DDToken;
import net.nocturne.game.player.content.activities.jobs.JobManager;
import net.nocturne.game.player.content.activities.minigames.FightCaves;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.player.content.activities.minigames.clanwars.WarController;
import net.nocturne.game.player.content.activities.minigames.dominon.DTController;
import net.nocturne.game.player.content.activities.minigames.dominon.DominionTower;
import net.nocturne.game.player.content.activities.minigames.duel.DuelRules;
import net.nocturne.game.player.content.activities.minigames.soulwars.SoulWars;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationController;
import net.nocturne.game.player.content.activities.minigames.stealingcreation.StealingCreationLobbyController;
import net.nocturne.game.player.content.activities.ports.PlayerPorts;
import net.nocturne.game.player.content.activities.ports.missions.PortMissions;
import net.nocturne.game.player.content.activities.reaper.Reaper;
import net.nocturne.game.player.content.activities.skillertasks.SkillerTasks;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.player.controllers.GodWars;
import net.nocturne.game.player.controllers.NomadsRequiem;
import net.nocturne.game.player.controllers.QueenBlackDragonController;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.player.controllers.ZEDController;
import net.nocturne.game.player.controllers.bossInstance.BossInstanceController;
import net.nocturne.game.player.controllers.castlewars.CastleWarsPlaying;
import net.nocturne.game.player.controllers.castlewars.CastleWarsWaiting;
import net.nocturne.game.player.controllers.events.DeathEvent;
import net.nocturne.game.player.controllers.pestcontrol.PestControlGame;
import net.nocturne.game.player.controllers.pestcontrol.PestControlLobby;
import net.nocturne.game.player.cutscenes.Cutscene;
import net.nocturne.game.player.dialogues.impl.JModTable;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.login.Login;
import net.nocturne.login.account.Account;
import net.nocturne.network.LoginClientChannelManager;
import net.nocturne.network.Session;
import net.nocturne.network.decoders.WorldPacketsDecoder;
import net.nocturne.network.decoders.handlers.ButtonHandler;
import net.nocturne.network.encoders.LoginChannelsPacketEncoder;
import net.nocturne.network.encoders.WorldPacketsEncoder;
import net.nocturne.utils.Color;
import net.nocturne.utils.ILayoutDefaults;
import net.nocturne.utils.IsaacKeyPair;
import net.nocturne.utils.Logger;
import net.nocturne.utils.MachineInformation;
import net.nocturne.utils.SerializableFilesManager;
import net.nocturne.utils.Utils;
import net.nocturne.utils.sql.Highscores;
import net.nocturne.utils.sql.Webstore;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.google.common.base.Stopwatch;

public class Player extends Entity {

	private static final long serialVersionUID = 2011932556974180375L;

	// transient stuff
	private transient String username;
	private transient String password;
	private transient Session session;
	private transient long clientLoadedMapRegion;
	private transient int displayMode;
	private transient int screenWidth;
	private transient int screenHeight;
	private transient InterfaceManager interfaceManager;
	private transient DialogueManager dialogueManager;
	private transient HintIconsManager hintIconsManager;
	private transient ActionManager actionManager;
	private transient CutscenesManager cutscenesManager;
	private transient PriceCheckManager priceCheckManager;
	private transient AssistanceManager assistanceManager;
	private transient RouteEvent routeEvent;
	private transient FriendsIgnores friendsIgnores;
	private transient FriendsChat currentFriendsChat;
	private transient ClansManager clanManager, guestClanManager;
	private transient boolean lootShare;
	private transient Trade trade;
	private transient DuelRules duelRules;
	private transient IsaacKeyPair isaacKeyPair;
	private transient Pet pet;
	private transient VarsManager varsManager;
	private transient PlayerPorts playerPorts;
	private boolean[] boons;
	private FairyRings fairyRings;
	private int gameSettingsCategory;
	private int loginCount;
	private boolean support;
	private boolean groupAssignments, largerTasks;
	private int rerollCount;

	private boolean divineLight = false;
	private boolean divineCoin = false;

	public long canReceiveSeed;

	private int secureCode = 0;
	public int secureAttempts = 0;

	public int reaperPoints;

	private int silverhawkFeathers;

	public boolean pyramidReward;

	public boolean isOnBoBWindow = false;
	public transient Stopwatch godWarsWatch;
	public boolean spottedPenguin;

	public ItemsContainer<Item> partyDeposit;
	private HashMap<Integer, Integer> vars;
	private HashMap<Integer, Integer> varbits;

	// Legendary Pets
	public long petLifeSaver = 0;
	public long petHighAlchemy = 0;
	public long petItemRepair = 0;
	public long petItemBank = 0;
	public long petItemForge = 0;
	public long petItemForageReset = 0;
	public int petForageItems = 0;
	public long petNPCExecute = 0;
	public boolean petVamp = false;
	public long petVampReset = 0;

	public boolean hasLegendaryPet() {
		return getPet() != null
				&& (getPet().getPetType() == Pets.BLOODPOUNCER
						|| getPet().getPetType() == Pets.SKYPOUNCER
						|| getPet().getPetType() == Pets.BLAZEHOUND
						|| getPet().getPetType() == Pets.DRAGON_WOLF
						|| getPet().getPetType() == Pets.WARBORN_BEHEMOTH
						|| getPet().getPetType() == Pets.PROTOTYPE_COLOSSUS || getPet()
						.getPetType() == Pets.RORY_THE_REINDEER);
	}

	// Vorago
	public boolean defeatedVorago;
	public boolean talkedtoVorago;
	public boolean accChallenge;
	public boolean isSiphoningRago;
	public boolean pickedUpPiece2 = false;
	public boolean pickedUpPiece3 = false;

	public long delay, majorDelay, runEnergyDelay;

	public int runEnergyCount;

	private boolean online;
	/**
	 * O Factions
	 */
	public WorldTile ZAMORAK_TILE = new WorldTile(3168, 3195, 0);

	/**
	 * Skiller Task System
	 */
	private SkillerTasks skillTasks;

	private Reaper reaperTasks;

	private DDToken ddToken;

	protected transient List<Player> spectators;

	private int taskPoints;

	public int portableLimit = 0;

	public static WorldTile SARADOMIN_TILE = new WorldTile(3168, 3269, 0);

	public boolean isZamorak;
	// used for packets logic
	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;

	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;

	public String teleportType = "";

	private transient boolean started;
	private transient boolean running;
	private transient boolean runAfterLoad;
	private transient boolean lobby;

	private int[] mousePosition, lastMousePosition;

	public int ticks, minutes, hours, days, totalMinutes;

	private transient int resting;
	private transient boolean canPvp;
	private transient boolean cantTrade;
	private transient boolean cantWalk;
	private transient long lockDelay; // used for doors and stuff like that
	private transient long foodDelay;
	private transient long potDelay;
	private transient Runnable closeInterfacesEvent;
	private transient long lastPublicMessage;
	private transient List<Integer> switchItemCache;
	private transient boolean disableEquip;
	private transient MachineInformation machineInformation;
	private transient boolean castedVeng;
	private transient boolean invulnerable;
	private transient double hpBoostMultiplier;
	private transient boolean largeSceneView;
	private transient int cannonBalls;
	private transient String lastPlayerKill;
	private transient String lastPlayerMAC;
	private transient boolean refreshClanIcon;

	public boolean distrupted;
	public boolean inRots;
	public boolean isBinded;
	public boolean guthanBleed;

	private int gStone;

	public CannonType cannonType;

	// stuff received from login server
	private transient String displayName;
	private transient String email;

	public boolean settings[] = new boolean[25];
	private boolean setSettings;
	public int toggledAmount = 0;

	private boolean geBoolean;
	public int grandExchangeLimit[] = new int[Utils.getItemDefinitionsSize()];

	private DailyTasks daily;

	public DailyTasks getDailyTask() {
		return daily;
	}

	public DailyTasks setDailyTask(DailyTasks daily) {
		return this.daily = daily;
	}

	public boolean completedDaily;
	public boolean claimedDailyReward;
	private int dailyDate;

	public void setDailyDate(int date) {
		this.dailyDate = date;
	}

	public int getDailyDate() {
		return dailyDate;
	}

	public long afk;

	private transient int rights;
	private transient boolean muted;
	private transient long lastVote;

	private int coalBag;

	/**
	 * Ironman Modes
	 */
	private boolean ironMan;
	private boolean hardcoreIronMan;

	private int Loyaltypoints;

	private List<Player> partyMembers = new ArrayList<Player>();

	// saving stuff
	private DonationManager donationManager;
	private Appearence appearence;
	private Inventory inventory;
	private MoneyPouch moneyPouch;
	private Equipment equipment;
	private Skills skills;
	private CombatDefinitions combatDefinitions;
	private Prayer prayer;
	private Bank bank;
	private Bank bank2;
	private ControllerManager controllerManager;
	private MusicsManager musicsManager;
	private EmotesManager emotesManager;
	private Notes notes;
	private Toolbelt toolbelt;
	private DominionTower dominionTower;
	private Familiar familiar;
	private FarmingManager farmingManager;
	private AuraManager auraManager;
	private QuestManager questManager;
	private JobManager jobManager;
	private PortMissions pMissions;
	private PetManager petManager;
	private GrandExchangeManager geManager;
	private SlayerManager slayerManager;
	private TreasureHunter treasureHunter;
	private TreasureTrailsManager treasureTrailsManager;
	private CoalTrucksManager coalTrucksManager;
	private DungManager dungManager;
	private DropThresholdManager thresholdManager;
	private House house;
	private AcceptAid acceptAid;
	private ActionBar actionbar;
	private DoomsayerManager doomsayerManager;
	private TimersManager timersManager;
	private PlayerExamineManager playerExamineManager;
	private CosmeticsManager cosmeticsManager;
	private CompletionistCapeManager compCapeManager;
	private GamePointManager gamePointManager;
	private byte runEnergy;
	private int[] subMenus;

	// rs settings old
	private boolean allowChatEffects;
	private boolean rightClickReporting;
	private boolean mouseButtons;
	private boolean profanityFilter;

	// rs settings
	private boolean lockInterfaceCustomization;
	private boolean hideTitleBarsWhenLocked;
	private boolean slimHeaders;
	private boolean clickThroughtChatboxes;
	private boolean timeStamps;
	private boolean targetReticules;
	private boolean alwaysShowTargetInformation;
	private boolean legacyCombatMode;
	private boolean legacyInterfaceMode;
	private boolean lootInterface;
	private boolean splitPrivateChat;
	private boolean makeXProgressWindow;
	private boolean hideFamiliarOptions;
	private boolean guidanceSystemHints;
	private boolean toogleQuickChat;
	private boolean lockZoom;
	private boolean rs3Camera;
	private boolean taskCompletePopup;
	private boolean taskInformationWindow;
	private boolean publicChatEffects;
	private boolean tooglePlayerNotification;
	private boolean toogleAbilityCooldownTimer;
	private boolean skillTargetBasedXPPopup;
	private boolean toggleBuffTimers;
	private boolean slayerCounter;
	private boolean virtualLeveling;
	private boolean goldTrim;
	public boolean minimized = false;
	private byte utcClock;

	public boolean onClanBank = false;
	private int lastX = 2208, lastY = 3360, lastPlane = 1;

	private int restAnimation = 1;

	public int boneType;
	public boolean bonesGrinded;
	public int unclaimedEctoTokens;

	private Item prize;

	private int rakeStored, seedDibber, spadeStored, trowelStored,
			wateringCanStored, secateursStored;

	// rs3 chat settings
	private boolean alwaysOnChatMode;

	private int privateChatSetup;
	private int friendChatSetup;
	private int clanChatSetup;
	private int guestChatSetup;
	private int skullDelay;
	private int skullId;
	private int virtualIconId;
	private int virtualIconId2;
	public boolean isInDeathRoom = false;

	private boolean forceNextMapLoadRefresh;
	private boolean killedQueenBlackDragon;
	private int runeSpanPoints;
	private int pestPoints;
	private int stealingCreationPoints;
	private int favorPoints;
	private double[] warriorPoints;
	private boolean[] prayerBook;
	private int previousLodestone;
	private boolean instantSwitchToLegacy;

	public int[] varbit = new int[] { 26043, 26045, 26044, 26050, 26047, 26049,
			26046, 26051, 26063, 26052, 26040, 26054, 26055, 26057, 26041,
			26053, 26059, 26061, 26062, 26042, 26056, 26058, 26039, 26048,
			26060, 26038 };
	public boolean[] expertSkillShards = new boolean[26];

	// shop
	private boolean verboseShopDisplayMode;

	/**
	 * Fist of guthix
	 */
	private int fogRating;
	private transient int fogCharge;

	public int fogRating() {
		return fogRating;
	}

	public int fogWins;

	public void fogRating(int f) {
		this.fogRating = f;
	}

	public int fogCharge() {
		return fogCharge;
	}

	public void fogCharge(int f) {
		this.fogCharge = f;
	}

	/**
	 * Saved Timers
	 */
	private long lastStarSprite;
	private long lastBork;

	private int[] pouches;

	// game bar status
	private int gameStatus;
	private int publicStatus;
	private int personalStatus;
	private int clanStatus;
	private int tradeStatus;
	private int assistStatus;
	private int friendsChatStatus;

	// honor
	private int killCount, deathCount;
	private long lastArtefactTime;

	private int godMode;

	private ChargesManager charges;
	// barrows
	private boolean[] killedBarrowBrothers;
	private int hiddenBrother;
	private int barrowsKillCount;
	// strongholdofsecurity rewards
	private boolean[] shosRewards;
	private boolean killedLostCityTree;

	// skill capes customizing
	private int[] maxedCapeCustomized;
	private int[] completionistCapeCustomized;

	private String mutedFor;

	// crucible
	private boolean talkedWithMarv;
	private int crucibleHighScore;

	private int gameMode;

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	private boolean bankDialogue;

	// gravestone
	private int graveStone;

	private String lastFriendsChat;
	private int lastFriendsChatRank;
	private String clanName;// , guestClanChat;
	private boolean connectedClanChannel;

	private int summoningLeftClickOption;
	private transient boolean pouchFilter;
	private List<String> ownedObjectsManagerKeys;

	private String customTitle = "";
	private String customYellTag = "";
	private String customTitleColor = "551A8B";
	private boolean customTitleCapitalize = false;
	private boolean customTitleActive = false;
	private boolean customYellTagActive = false;

	private boolean hasDied = false;

	private int trollsToKill;
	private int trollsKilled;

	/**
	 * Objects
	 */
	// kalphite
	private boolean khalphiteLairEntranceSetted;
	private boolean khalphiteLairSetted;
	// red sandstone
	private int redStoneCount;
	private int crystalStoneCount;
	private long redStoneDelay;
	private long crystalStoneDelay;

	public int araxxiEnrage = 0;
	public long araxxiEnrageTimer = 0;
	public boolean AraxxorLastState;
	public boolean AraxxorThirdStage;
	public boolean AraxxorPause;
	public int AraxDeathX;
	public int AraxDeathY;
	public int AraxDeathZ;
	public int EGGX;
	public int EGGY;
	public int FINALAGGX;
	public int FINALAGGY;
	public int AcidLevel;
	public boolean AraxxorEggBurst;
	public int AraxxorAttackCount;
	public boolean AraxxorCompleteAttack;
	public int cacoonTime;
	public int araxxorCacoonTime;
	public int eggSpidersX;
	public int eggSpidersY;
	public boolean hasSpawnedEggs;
	@SuppressWarnings("unused")
	private boolean killedAraxxor;
	public int AraxxorNormAttackDelay;
	public int ArraxorAttackDelay;
	public boolean araxxorEggAttack;
	public WorldTile AraxxorBase;
	public boolean araxxorHeal;

	private boolean xpLocked;
	private boolean yellOff;

	private String yellColor = "ff0000";
	private boolean oldItemsLook; // selects whenever to play with old or new
	// items visual

	private int penguinPoints;
	private int votePoints;
	private boolean enteredDonatorZone;

	private String lastGameIp;
	private String lastGameMAC;
	private transient boolean masterLogin;
	private long lastGameLogin;

	private String lastBossInstanceKey;
	private InstanceSettings lastBossInstanceSettings;

	// new nsi system
	private Map<Integer, Integer> iLayoutVars;

	private boolean recievedStarter;

	private long lastUsedMotherLode, lastFoughtBork, lastClaimedBook;

	public HashMap<String, Integer> slayerCreatures;
	public Object[][] killcountsData = { { "Terror dog", 22921 },
			{ "Bloodveld", 22922 }, { "Aberrant spectre", 22924 },
			{ "Dust devil", 22925 }, { "Automaton Generator", 22926 },
			{ "Automaton Tracer", 22926 }, { "Automaton Guardian", 22926 },
			{ "Skeletal Wyvern", 22927 }, { "Jungle strykewyrm", 22928 },
			{ "Aquanite", 22929 }, { "Nechryael", 22930 },
			{ "Mutated jadinkos", 22931 }, { "Ascension creatures", 22932 },
			{ "Polypore creatures", 22933 }, { "Spiritual mage", 22934 },
			{ "Abyssal demon", 22935 }, { "Dark beast", 22936 },
			{ "Airut", 22937 }, { "Ice strykewyrm", 22938 },
			{ "Kal'gerion demon", 22939 }, { "Glacor", 22940 },
			{ "Tormented demon", 22941 }, { "Gargoyle", 22942 },
			{ "Muspah", 22943 }, { "Nihil", 22944 },
			{ "Desert strykewyrm", 22945 }, { "Edimmu", 25877 },
			{ "Lava strykewyrm", 27007 }, { "Adamant dragon", 28374 },
			{ "Runite dragon", 28375 }, { "Crystal Shapeshifter", 29381 },
			{ "Living Wyvern", 29818 }, { "Ripper Demon", 29819 },
			{ "Camel Warrior", 29820 }, { "Acheron Mammoth", 29821 },
			{ "Celestial dragon", 30017 }, { "Giant mole", 22946 },
			{ "King Black Dragon", 22948 }, { "Chaos Elemental", 22950 },
			{ "Kalphite Queen", 22951 }, { "TzTok-Jad", 22952 },
			{ "Corporeal Beast", 22953 }, { "Dagannoth Kings", 22954 },
			{ "Queen Black Dragon", 22955 }, { "Commander Zilyana", 22956 },
			{ "General Graardor", 22958 }, { "Kree'arra", 22960 },
			{ "K'ril Tsutsaroth", 22962 }, { "Har-Aken", 22964 },
			{ "Kalphite King", 22965 }, { "Legios", 22966 }, { "Nex", 22967 },
			{ "Araxxi", 22969 }, { "Vorago", 22970 },
			{ "Beastmaster Durzag", 28643 }, { "Yakamaru", 28644 },
			{ "Gorvek and Vindicta", 30840 },
			{ "Nymora, the Vengeful", 30841 },
			{ "Avaryss, the Unceasing", 30841 }, { "Helwyr", 30842 },
			{ "Gregorovic", 30843 }, };

	public void eggBurst(int X, int Y, int Z) {
		if (AraxxorEggBurst == false) {
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 3,
					Y, Z));
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 2,
					Y, Z));
			World.sendGraphics(null, new Graphics(4996), new WorldTile(X + 2,
					Y + 1, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 3,
					Y, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 2,
					Y, Z));
			World.sendGraphics(null, new Graphics(4982), new WorldTile(X + 2,
					Y + 1, Z));
			AraxxorEggBurst = true;
			stopAll();
		}
	}

	public boolean canFightBork() {
		return (Utils.currentTimeMillis() - lastFoughtBork) >= (60 * 60 * 1000);
	}

	public void setLastFoughtBork(long time) {
		this.lastFoughtBork = time;
	}

	public boolean canClaimBook() {
		return (Utils.currentTimeMillis() - lastClaimedBook) >= (7 * 24 * 60 * 60 * 1000);
	}

	public void setLastClaimedBook(long time) {
		this.lastClaimedBook = time;
	}

	public boolean canUseMotherlode() {
		return (Utils.currentTimeMillis() - lastUsedMotherLode) >= (24 * 60 * 60 * 1000);
	}

	public void setLastUsedMotherLode(long time) {
		this.lastUsedMotherLode = time;
	}

	public boolean getInRots() {
		return inRots;
	}

	public void setInRots(boolean inRots) {
		this.inRots = inRots;
	}

	public int getGraveStone1() {
		return gStone;
	}

	public boolean getDisrupted() {
		return distrupted;
	}

	public void setDisrupted(boolean distrupt) {
		this.distrupted = distrupt;
	}

	public boolean getBinded() {
		return isBinded;
	}

	public void setBinded(boolean binded) {
		this.isBinded = binded;
	}

	public boolean getGuthanBleed() {
		return guthanBleed;
	}

	public void setGuthanBleed(boolean bleed) {
		this.guthanBleed = bleed;
	}

	// creates Player and saved classes
	public Player() {
		super(Settings.STARTER_LOCATION);
		super.setHitpoints(1000);
		loginCount = 0;
		slayerCreatures = new HashMap<String, Integer>();
		vars = new HashMap<Integer, Integer>();
		varbits = new HashMap<Integer, Integer>();
		varbits.put(20940, 120);
		varbits.put(382, 11);
		varbits.put(9513, 1);
		acceptAid = new AcceptAid(this);
		donationManager = new DonationManager();
		appearence = new Appearence();
		inventory = new Inventory();
		moneyPouch = new MoneyPouch();
		equipment = new Equipment();
		skills = new Skills();
		combatDefinitions = new CombatDefinitions();
		thresholdManager = new DropThresholdManager(this);
		prayer = new Prayer();
		bank = new Bank();
		bank2 = new Bank();
		controllerManager = new ControllerManager();
		musicsManager = new MusicsManager();
		emotesManager = new EmotesManager();
		notes = new Notes();
		toolbelt = new Toolbelt();
		dominionTower = new DominionTower();
		charges = new ChargesManager();
		auraManager = new AuraManager();
		questManager = new QuestManager();
		jobManager = new JobManager(this);
		pMissions = new PortMissions(this);
		petManager = new PetManager();
		farmingManager = new FarmingManager();
		geManager = new GrandExchangeManager();
		slayerManager = new SlayerManager();
		treasureHunter = new TreasureHunter();
		treasureTrailsManager = new TreasureTrailsManager();
		coalTrucksManager = new CoalTrucksManager();
		dungManager = new DungManager();
		house = new House();
		actionbar = new ActionBar();
		skillTasks = new SkillerTasks();
		reaperTasks = new Reaper();
		doomsayerManager = new DoomsayerManager();
		timersManager = new TimersManager();
		playerExamineManager = new PlayerExamineManager();
		cosmeticsManager = new CosmeticsManager();
		compCapeManager = new CompletionistCapeManager(this);
		assistanceManager = new AssistanceManager(this);
		gamePointManager = new GamePointManager(this);
		playerPorts = new PlayerPorts();
		// dailyChallengeManager = new DailyChallenge(this);
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		profanityFilter = true;
		guidanceSystemHints = true;
		toogleQuickChat = true;
		makeXProgressWindow = true;
		taskInformationWindow = true;
		rs3Camera = true;
		targetReticules = true;
		alwaysShowTargetInformation = true;
		toggleBuffTimers = true;
		utcClock = 1;
		pouches = new int[4];
		resetBarrows();
		shosRewards = new boolean[4];
		warriorPoints = new double[6];
		subMenus = new int[12];
		boons = new boolean[15];
		SkillCapeCustomizer.resetSkillCapes(this);
		ownedObjectsManagerKeys = new LinkedList<String>();
		resetILayoutVars();
	}

	public void init(Session session, boolean lobby, String username,
			String displayName, String lastGameMAC, String email, int rights,
			int messageIcon, boolean masterLogin, boolean donator,
			boolean extremeDonator, boolean support, boolean gfxDesigner,
			boolean muted, long lastVote, int displayMode, int screenWidth,
			int screenHeight, MachineInformation machineInformation,
			IsaacKeyPair isaacKeyPair) {
		// temporary deleted after reset all chars
		if (doomsayerManager == null)
			doomsayerManager = new DoomsayerManager();
		if (slayerCreatures == null)
			slayerCreatures = new HashMap<String, Integer>();
		if (timersManager == null)
			timersManager = new TimersManager();
		if (playerExamineManager == null)
			playerExamineManager = new PlayerExamineManager();
		if (thresholdManager == null)
			thresholdManager = new DropThresholdManager(this);
		if (cosmeticsManager == null)
			cosmeticsManager = new CosmeticsManager();
		if (playerPorts == null)
			playerPorts = new PlayerPorts();
		if (jobManager == null)
			jobManager = new JobManager(this);
		if (pMissions == null)
			pMissions = new PortMissions(this);
		if (bank2 == null)
			bank2 = new Bank();
		if (boons == null)
			boons = new boolean[12];
		if (skillTasks == null)
			skillTasks = new SkillerTasks();
		if (reaperTasks == null)
			reaperTasks = new Reaper();
		if (fairyRings == null)
			fairyRings = new FairyRings(this);
		if (compCapeManager == null)
			compCapeManager = new CompletionistCapeManager(this);
		if (assistanceManager == null)
			assistanceManager = new AssistanceManager(this);
		if (gamePointManager == null)
			gamePointManager = new GamePointManager(this);
		if (ddToken == null)
			ddToken = new DDToken(this);
		if (setSettings == false) {
			settings = new boolean[25];
			for (int i = 0; i < 25; i++)
				settings[i] = true;
			setSettings = true;
		}
		spectators = new ArrayList<Player>();
		if (geBoolean == false) {
			grandExchangeLimit = new int[Utils.getItemDefinitionsSize()];
			for (int i = 0; i < Utils.getItemDefinitionsSize(); i++)
				grandExchangeLimit[i] = 0;
			geBoolean = true;
		}

		this.session = session;
		this.lobby = lobby;
		this.username = username;
		this.displayName = displayName;
		this.email = email;
		this.rights = rights;
		this.masterLogin = masterLogin;
		this.muted = muted;
		this.lastVote = lastVote;
		this.lastGameMAC = lastGameMAC;
		this.displayMode = displayMode;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.machineInformation = machineInformation;
		this.isaacKeyPair = isaacKeyPair;
		interfaceManager = new InterfaceManager(this);
		dialogueManager = new DialogueManager(this);
		hintIconsManager = new HintIconsManager(this);
		priceCheckManager = new PriceCheckManager(this);
		localPlayerUpdate = new LocalPlayerUpdate(this);
		localNPCUpdate = new LocalNPCUpdate(this);
		actionManager = new ActionManager(this);
		cutscenesManager = new CutscenesManager(this);
		trade = new Trade(this);
		varsManager = new VarsManager(this);
		friendsIgnores = new FriendsIgnores(this);
		// loads player on saved instances
		donationManager.setPlayer(this);
		compCapeManager.setPlayer(this);
		gamePointManager.setPlayer(this);
		appearence.setPlayer(this);
		skillTasks.setPlayer(this);
		reaperTasks.setPlayer(this);
		inventory.setPlayer(this);
		moneyPouch.setPlayer(this);
		equipment.setPlayer(this);
		skills.setPlayer(this);
		combatDefinitions.setPlayer(this);
		prayer.setPlayer(this);
		bank.setPlayer(this);
		bank2.setPlayer(this);
		controllerManager.setPlayer(this);
		musicsManager.setPlayer(this);
		emotesManager.setPlayer(this);
		notes.setPlayer(this);
		toolbelt.setPlayer(this);
		dominionTower.setPlayer(this);
		auraManager.setPlayer(this);
		charges.setPlayer(this);
		questManager.setPlayer(this);
		jobManager.setPlayer(this);
		petManager.setPlayer(this);
		house.setPlayer(this);
		actionbar.setPlayer(this);
		doomsayerManager.setPlayer(this);
		timersManager.setPlayer(this);
		playerExamineManager.setPlayer(this);
		cosmeticsManager.setPlayer(this);
		farmingManager.setPlayer(this);
		geManager.setPlayer(this);
		slayerManager.setPlayer(this);
		treasureHunter.setPlayer(this);
		// treasureHunter.setPlayer(this);
		treasureTrailsManager.setPlayer(this);
		coalTrucksManager.setPlayer(this);
		dungManager.setPlayer(this);
		playerPorts.setPlayer(this);
		initEntity(); // generates hash thats why
		if (!lobby) {
			setDirection(Utils.getAngle(0, -1));
			logicPackets = new ConcurrentLinkedQueue<LogicPacket>();
			switchItemCache = Collections
					.synchronizedList(new ArrayList<Integer>());
			World.addPlayer(this);
			World.updateEntityRegion(this);
		} else
			World.addLobbyPlayer(this);
		afk = Utils.currentTimeMillis()
				+ (Settings.DEBUG ? Integer.MAX_VALUE : 15 * 60 * 1000);
		// PlayersOnline.update();
		Logger.log(this, getDisplayName()+":"+session.getIP()+" logged in - players online:" + World.getPlayers().size());
	}

	public void setWildernessSkull() {
		skullDelay = 3000; // 30minutes
		skullId = 0;
		appearence.generateIconsData();
	}

	public void setFightPitsSkull() {
		skullDelay = Integer.MAX_VALUE; // infinite
		skullId = 1;
		appearence.generateIconsData();
	}

	public void setSkullInfiniteDelay(int skullId) {
		skullDelay = Integer.MAX_VALUE; // infinite
		this.skullId = skullId;
		appearence.generateIconsData();
	}

	public void removeSkull() {
		skullDelay = -1;
		appearence.generateIconsData();
	}

	public boolean hasSkull() {
		return skullDelay > 0;
	}

	public void sendData() {
		for (int i = 0; i < killcountsData.length - 1; i++)
			getVarsManager().sendVarBit(
					(int) killcountsData[i][1],
					slayerCreatures.get(killcountsData[i][0]) == null ? 0
							: slayerCreatures.get(killcountsData[i][0]));
	}

	public void refreshSpawnedItems() {
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getGroundItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if (item.isInvisible()
						&& (item.hasOwner() && !getUsername().equals(
								item.getOwner())))
					continue;
				getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : getMapRegionsIds()) {
			List<FloorItem> floorItems = World.getRegion(regionId)
					.getGroundItems();
			if (floorItems == null)
				continue;
			for (FloorItem item : floorItems) {
				if ((item.isInvisible())
						&& (item.hasOwner() && !getUsername().equals(
								item.getOwner())))
					continue;
				getPackets().sendGroundItem(item);
			}
		}
	}

	public void refreshSpawnedObjects() {
		for (int regionId : getMapRegionsIds()) {
			List<WorldObject> removedObjects = World.getRegion(regionId)
					.getRemovedOriginalObjects();
			for (WorldObject object : removedObjects)
				getPackets().sendRemoveObject(object);
			List<WorldObject> spawnedObjects = World.getRegion(regionId)
					.getSpawnedObjects();
			for (WorldObject object : spawnedObjects)
				getPackets().sendAddObject(object);
		}
	}

	// now that we inited we can start showing game
	public void start() {
		Logger.globalLog(username, session.getIP(), new String(
				" has logged in."));
		loadMapRegions();
		started = true;
		setOnline(true);
		run();
	}

	public void startLobby() {
		started = true;
		LoginClientChannelManager.sendReliablePacket(LoginChannelsPacketEncoder
				.encodePlayerWorldListStatusRequest(getUsername(), 0)
				.getBuffer());
		sendLobbyVars();
		runLobby();
	}

	private void runLobby() {
		interfaceManager.sendInterfaces();
		if (Engine.delayedShutdownStart != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - Engine.delayedShutdownStart) / 1000);
			getPackets().sendSystemUpdate(
					Engine.delayedShutdownDelay - delayPassed, true);
		}
		friendsIgnores.initialize();
		if (clanName != null)
			if (!ClansManager.connectToClan(this, clanName, false))
				clanName = null;
	}

	void sendLobbyVars() {
		getPackets().refreshLobbyInformation();
		// black screen if those not enabled
		getVarsManager().sendVar(1751, 6757431); // x
		getVarsManager().sendVar(1752, 6766321);// y
		getVarsManager().sendVar(1753, 225);
		getVarsManager().sendVar(1754, 69);
		// 1 - normal login. 2 ask for email(+ reason in varbit 16465)
		getVarsManager().sendVarBit(16464, 1);
		getVarsManager().sendVarBit(16465, 0);
		getPackets().sendCSVarInteger(3905, -1);// Enables banner clicking &
		// middle banner
		getPackets().sendCSVarInteger(4266, getTreasureHunter().getAllKeys());// Treasure
																				// chest
																				// key
																				// amount
		getPackets().sendCSVarInteger(4267, 0);// Treasure chest heart amount
		getPackets().sendCSVarInteger(4263, -1);// Enables Treasure trail banner
		getPackets().sendCSVarInteger(4264, -1);// Boss pets display
		getPackets().sendCSVarInteger(4265, -1);// Enables second right banner
		getPackets().sendCSVarInteger(4660, 0);// Loyalty Points
		getPackets().sendCSVarInteger(4659, 0);// Runecoins
		getPackets().sendExecuteScript(10931, 0, 12606, 1, -1,
				Settings.SERVER_NAME, 1);
		getPackets().sendIComponentSprite(907, 68, 2141);
		getPackets().sendIComponentSprite(907, 19, -1);
		getPackets().sendIComponentSprite(907, 23, -1);
		getPackets().sendIComponentSprite(907, 22, -1);
		getPackets().sendCSVarString(2508, displayName);
		// script_10012

	}

	public void stopAll() {
		stopAll(true);
	}

	public void stopAll(boolean stopWalk) {
		stopAll(stopWalk, true);
	}

	public void stopAll(boolean stopWalk, boolean stopInterface) {
		stopAll(stopWalk, stopInterface, true);
	}

	// as walk done clientsided
	public void stopAll(boolean stopWalk, boolean stopInterfaces,
			boolean stopActions) {
		routeEvent = null;
		if (stopInterfaces)
			closeInterfaces();
		if (stopWalk && !cantWalk)
			resetWalkSteps();
		if (stopActions)
			actionManager.forceStop();
		combatDefinitions.resetSpells(false);
		getInterfaceManager().removeInterface(1251);
		getVarsManager().sendVar(1176, 0);
		getSkills();
		Skills.xpMultiplier = 1;
	}

	@Override
	public void setHitpoints(int hitpoints) {
		super.setHitpoints(hitpoints);
		refreshHitPoints();
	}

	@Override
	public void reset(boolean attributes) {
		super.reset(attributes);
		hintIconsManager.removeAll();
		skills.restoreSkills();
		combatDefinitions.resetSpecialAttack();
		prayer.reset();
		combatDefinitions.resetSpells(false);
		resting = 0;
		skullDelay = 0;
		foodDelay = 0;
		potDelay = 0;
		castedVeng = false;
		cantWalk = false;
		setRunEnergy(100);
		appearence.generateAppearenceData();
		appearence.generateIconsData();
	}

	@Override
	public void reset() {
		reset(true);
	}

	public void closeInterfaces() {
		if (interfaceManager.containsScreenInterface())
			interfaceManager.removeCentralInterface();
		if (interfaceManager.containsBankInterface())
			interfaceManager.removeBankInterface();
		if (interfaceManager.containsInventoryInter())
			interfaceManager.removeInventoryInterface();
		if (interfaceManager.containsInputTextInterface())
			interfaceManager.removeInputTextInterface();
		if (interfaceManager.containsWorldMapInterface())
			interfaceManager.removeWorldMapInterface();
		dialogueManager.finishDialogue();
		if (closeInterfacesEvent != null) {
			closeInterfacesEvent.run();
			closeInterfacesEvent = null;
		}
	}

	private void setClientHasntLoadedMapRegion() {
		clientLoadedMapRegion = Utils.currentWorldCycle() + 30;
	}

	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		setClientHasntLoadedMapRegion();
		if (isAtDynamicRegion()) {
			getPackets().sendDynamicGameScene(!started);
			if (!wasAtDynamicRegion)
				localNPCUpdate.reset();
		} else {
			getPackets().sendGameScene(!started);
			if (wasAtDynamicRegion)
				localNPCUpdate.reset();
		}
		forceNextMapLoadRefresh = false;
	}

	void addAdrenalineBar() {
		if (getNextHitBars().isEmpty())
			getNextHitBars().add(new AdrenalineHitBar(this));
	}

	private void processLogicPackets() {
		LogicPacket packet;
		while ((packet = logicPackets.poll()) != null)
			WorldPacketsDecoder.decodeLogicPacket(this, packet);
	}

	public void processProjectiles() {
		try {
			for (int regionId : getMapRegionsIds()) {
				Region region = World.getRegion(regionId);
				for (Projectile projectile : region.getProjectiles()) {

					int fromSizeX, fromSizeY;
					if (projectile.getFrom() instanceof Entity)
						fromSizeX = fromSizeY = ((Entity) projectile.getFrom())
								.getSize();
					else if (projectile.getFrom() instanceof WorldObject) {
						ObjectDefinitions defs = ((WorldObject) projectile
								.getFrom()).getDefinitions();
						fromSizeX = defs.getSizeX();
						fromSizeY = defs.getSizeY();
					} else
						fromSizeX = fromSizeY = 1;
					int toSizeX, toSizeY;
					if (projectile.getTo() instanceof Entity)
						toSizeX = toSizeY = ((Entity) projectile.getTo())
								.getSize();
					else if (projectile.getTo() instanceof WorldObject) {
						ObjectDefinitions defs = ((WorldObject) projectile
								.getTo()).getDefinitions();
						toSizeX = defs.getSizeX();
						toSizeY = defs.getSizeY();
					} else
						toSizeX = toSizeY = 1;

					getPackets()
							.sendProjectileNew(
									projectile.getFrom(),
									fromSizeX,
									fromSizeY,
									projectile.getTo(),
									toSizeX,
									toSizeY,
									projectile.getFrom() instanceof Entity ? (Entity) projectile
											.getFrom() : null,
									projectile.getTo() instanceof Entity ? (Entity) projectile
											.getTo() : null,
									projectile.isAdjustFlyingHeight(),
									projectile.isAdjustSenderHeight(),
									projectile.getSenderBodyPart(),
									projectile.getGraphicId(),
									projectile.getStartHeight(),
									projectile.getEndHeight(),
									projectile.getStartTime(),
									projectile.getEndTime(),
									projectile.getSlope(),
									projectile.getAngle(), 0);
				}
			}
		} catch (Throwable e) {
			Logger.handle(e, "Process Projectiles");
		}
	}

	@Override
	public void processEntityUpdate() {
		super.processEntityUpdate();
	}

	@Override
	public void processEntity() {
		try {
			processLogicPackets();
			// actionbar.processQueuedShortcut();
			actionManager.process();
			if (routeEvent != null && routeEvent.processEvent(this))
				routeEvent = null;
			super.processEntity();
			charges.process();
			auraManager.process();
			timersManager.process();
			controllerManager.process();
			farmingManager.process();
			cutscenesManager.process();
			if (isDead())
				return;

			if (musicsManager.musicEnded())
				musicsManager.replayMusic();
			if (hasSkull()) {
				skullDelay--;
				if (!hasSkull())
					appearence.generateAppearenceData();
			}
			getCombatDefinitions().processCombatStance();
			if (hasStarted() && !hasFinished()) {
				ticks++;
				if (ticks >= 100) {
					totalMinutes++;
					minutes++;
					ticks -= 100;
					depletePendant();
				}
				if (minutes >= 60) {
					hours++;
					minutes -= 60;
				}
				if (hours >= 24) {
					days++;
					hours -= 24;
				}
			}
		} catch (Throwable e) {
			Logger.handle(e, "Process Player");
		}
	}

	private void depletePendant() {
		if (Pendants.forId(getEquipment().getAmuletId()) != null
				&& getTemporaryAttributtes().contains("PrizedPendant")) {
			if ((int) getTemporaryAttributtes().get("PrizedPendant") > 60) {
				getTemporaryAttributtes().remove("PrizedPendant");
			} else if ((int) getTemporaryAttributtes().get("PrizedPendant") == 55) {
				getPackets().sendGameMessage(Color.PINK,
						"You have 5 more minutes until your pendant depletes.");
			} else if ((int) getTemporaryAttributtes().get("PrizedPendant") == 60) {
				getEquipment().getItems().set(2, new Item(24712, 1));
				getPackets().sendGameMessage(Color.PINK,
						"Your pendant power has depleted.");
				getTemporaryAttributtes().remove("PrizedPendant");
				return;
			}
			getTemporaryAttributtes().put("PrizedPendant",
					(int) getTemporaryAttributtes().get("PrizedPendant") + 1);
		}
	}

	public boolean hasRecievedStarter() {
		return recievedStarter;
	}

	@Override
	public void processReceivedHits() {
		if (isLocked())
			return;
		super.processReceivedHits();
	}

	@Override
	public void applyHit(Hit hit) {
		Entity source = hit.getSource();
		if (source != this && source instanceof Player && !isCanPvp())
			return;
		super.applyHit(hit);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
	}

	public void toogleRun(boolean update) {
		super.setRun(!getRun());
		if (update)
			sendRunButtonConfig();
	}

	public void setRunHidden(boolean run) {
		super.setRun(run);
	}

	@Override
	public void setRun(boolean run) {
		if (run != getRun()) {
			super.setRun(run);
			sendRunButtonConfig();
		}
	}

	private void sendRunButtonConfig() {
		getVarsManager().sendVar(463,
				resting == 1 ? 3 : resting == 2 ? 4 : getRun() ? 1 : 0);
	}

	public void restoreRunEnergy() {
		if (getNextRunDirection() != -1)
			return;
		if (getDonationManager().isExtremeDonator())
			runEnergy += Utils.random(2, 4);
		else if (getDonationManager().isLegendaryDonator())
			runEnergy += Utils.random(2, 4);
		else if (getDonationManager().isSupremeDonator())
			runEnergy += Utils.random(4, 8);
		else if (getDonationManager().isDivineDonator())
			runEnergy += Utils.random(4, 8);
		else if (getDonationManager().isAngelicDonator())
			runEnergy = 100;
		else
			runEnergy++;
		if (runEnergy >= 100) {
			runEnergy = 100;
		}
		getPackets().sendRunEnergy();
	}

	/*
	 * notice this may end up not being called if player dcs at same time he
	 * logs in use it just for stuff that cant be sent right away
	 */
	public void runAfterLoad() {
		interfaceManager.refreshInterface(true);
		skills.sendCombatLevel();
		runAfterLoad = true;
	}

	public void run() {
		if (isHardcoreIronman() && getHasDied()) {
			SerializableFilesManager.deleteAccount(this);
			disconnect(true, false);
			return;
		}
		checkList();
		sendTabs();
		sendMisc();
		sendLoginMessages();
		sendInit();
		sendSecurity();
		logLogin();
	}

	private void sendMisc() {
		if (getX() == 2171 && getY() == 3437 && getPlane() == 1)
			setNextAnimation(new Animation(25010));
	}

	private void sendLoginMessages() {
		getPackets().sendForceGameMessage(
				!Settings.DEBUG ? "Welcome to " + Settings.SERVER_NAME + "."
						: "Welcome to Pax' crazy world.");
		getSlayerManager().updateInterface();
		if (getRights() >= 1 && JModTable.PMOD_MEETING)
			getPackets()
					.sendGameMessage(
							"<col=FF0000>A staff meeting has been requested, please use the command ::accept to teleport.");
		if (!getDonationManager().isDonator())
			getPackets()
					.sendGameMessage(
							"You are not a donator. Consider becoming one to support us.",
							true);
		if (Settings.DOUBLE_XP)
			getPackets().sendGameMessage("<col=FF0000>Bonus xp is enabled!",
					true);
		if (Settings.DOUBLE_VOTES)
			getPackets().sendGameMessage(
					"<col=FF0000>Double vote rewards are now active!", true);
		if (getPrize() != null)
			getPackets()
					.sendGameMessage(
							"<col=FF0000>You have won the lottery, talk to Gambler to claim your prize.");
		if (getRights() == 2 && !getInventory().containsItem(5733, 1))
			getInventory().addItem(5733, 1);
		loadVarbits();
		loginCount++;
	}

	private void checkList() {
		if (!masterLogin)
			lastGameIp = getSession().getIP();
		lastGameLogin = Utils.currentTimeMillis();
		interfaceManager.sendInterfaces();
		refreshInterfaceVars();
		if (Engine.delayedShutdownStart != 0) {
			int delayPassed = (int) ((Utils.currentTimeMillis() - Engine.delayedShutdownStart) / 1000);
			getPackets().sendSystemUpdate(
					Engine.delayedShutdownDelay - delayPassed, false);
		}
	}

	private void sendTabs() {
		getPackets().sendRunEnergy();
		getPackets().sendItemsLook();
		refreshAllowChatEffects();
		refreshRightClickReporting();
		refreshQuestConfigs();
		refreshMouseButtons();
		refreshHideFamiliarOptions();
		refreshGuidanceSystemHints();
		refreshToogleQuickChat();
		refreshProfanityFilter();
		refreshLockZoom();
		refreshCameraType();
		refreshChatsSetup();
		refreshTimeStamps();
		refreshVirtualLeveling();
		refreshGoldTrim();
		refreshFirstVirtualIcon();
		refreshSecondVirtualIcon();
		sendRunButtonConfig();
		getPrayer().init();
		Familiar.refreshDefaultPetOptions(this);
		varsManager.sendVarBit(19928, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode || hideTitleBarsWhenLocked ? 1 : 0);
		varsManager.sendVarBit(19925, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode || lockInterfaceCustomization ? 1 : 0);
		varsManager.sendVarBit(19924, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode || slimHeaders ? 1 : 0);
	}

	private void sendInit() {
		getTimersManager().removeTimer();
		getInterfaceManager().sendCustom(this);
		sendMuteInterface();
		sendDefaultPlayersOptions();
		inventory.init();
		moneyPouch.init();
		equipment.checkItems();
		equipment.init();
		bank.init();
		bank2.init();
		skills.init();
		combatDefinitions.init();
		prayer.init();
		refreshHitPoints();
		session.getWorldPackets().sendResetNISVars();
		warriorCheck();
		prayer.refreshPrayerPoints();
		getVarsManager().sendVar(1295, 1000); // unlock cant do this under
		getPackets().sendGameBarStages();
		musicsManager.init();
		emotesManager.init();
		questManager.init();
		notes.init();
		house.init();
		actionbar.init();
		doomsayerManager.init();
		timersManager.init();
		playerExamineManager.init();
		cosmeticsManager.init();
		farmingManager.init();
		toolbelt.init();
		geManager.init();
		coalTrucksManager.init();
		refreshBuffs();
		refreshLodestoneNetwork();
		friendsIgnores.initialize();
		if (lastFriendsChat != null)
			FriendsChat.requestJoin(this, lastFriendsChat);
		else {
			FriendsChat.requestJoin(this,
					Utils.formatPlayerNameForDisplay(Settings.MAIN_FC));
			lastFriendsChat = Utils
					.formatPlayerNameForDisplay(Settings.MAIN_FC);
		}
		if (clanName != null) {
			if (!ClansManager.connectToClan(this, clanName, false))
				clanName = null;
		}
		if (familiar != null)
			familiar.respawnFamiliar(this);
		else
			petManager.init();
		if (getDailyTask() == null)
			daily = DailyTasks.generateDailyTask(this, Tasks.SKILLING);
		else
			getDailyTask().generateDailyTasks(this, false);
		running = true;
		appearence.generateAppearenceData();
		appearence.generateIconsData();
		controllerManager.login();
		OwnedObjectManager.linkKeys(this);
		GraveStone.linkPlayer(this);
		updateStore();
		prayer.processPrayerDrain();
		if (getFamiliar() != null)
			getFamiliar().getBob().sendInterItems();
		treasureHunter.handleKeys(treasureHunter.daily, false);
		if (machineInformation != null)
			machineInformation.sendSuggestions(this);
		if (isDead())
			sendDeath(null);
		if (Settings.SERVER_OWNERS.contains(getUsername())
				|| Settings.SERVER_DEVELOPERS.contains(getUsername())
				|| Settings.SERVER_ADMINISTRATORS.contains(getUsername()))
			setRights(2);
		else if (Settings.SERVER_MODERATORS.contains(getUsername()))
			setRights(1);
		else
			setRights(0);
	}

	private void logLogin() {
		try {
			DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal2 = Calendar.getInstance();
			final String FILE_PATH = Settings.getDropboxLocation()
					+ "logs/logins/";
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH
					+ getUsername() + ".txt", true));
			writer.write("[" + dateFormat2.format(cal2.getTime()) + "] IP: "
					+ getSession().getIP());
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException er) {
			er.printStackTrace();
		}
		if (!recievedStarter)
			appendStarter();
	}

	private void sendSecurity() {
		if (getSecureCode() != 0) {
			lock();
			getInterfaceManager().sendCentralInterface(793);
			getPackets().sendIComponentText(793, 5, "Secure Code");
			getPackets()
					.sendIComponentText(
							793,
							13,
							"Please enter your secure code below.<br><br><br><col=e55d12>Session ID: "
									+ getSessionTime()
									+ "</col><br><br><br>Note: all actions are being recorded.");
			getPackets().sendIComponentText(793, 12, getSession().getIP());
			getPackets().sendIComponentText(793, 14, "Help");
			getPackets().sendIComponentText(793, 15, "Logout");
			getTemporaryAttributtes().put("SecureCode", 0);
			getPackets().sendInputIntegerScript(
					"Please enter your Secure Code (" + secureAttempts
							+ "/5 attempts left):");
		}
	}

	private void sendMuteInterface() {
		if (isMuted()) {
			getInterfaceManager().sendCentralInterface(801);
			getPackets().sendIComponentText(801, 15, "Muted - Quick Chat");
			getPackets()
					.sendIComponentText(
							801,
							18,
							"Please note that your accout has been restricted to Quick Chat, as your account have been involved in serious rule breaking - to prevent further mutes please read the rules.");
			getPackets().sendGameMessage(
					"You have been temporarily muted due to breaking a rule.");
			getPackets().sendGameMessage(
					"This mute will remain for a further " + getMutedFor()
							+ " hours.");
			getPackets().sendGameMessage(
					"To prevent further mutes please read the rules.");
		}
	}

	private void refreshLodestoneNetwork() {
		getVarsManager().sendVar(3, -1); // unlocks all lodestone teleports
		varbits.put(9482, 15); // bandit camp
		varbits.put(10236, 190); // lunar isle
		loadVarbits();
	}

	private void refreshQuestConfigs() {
		getVarsManager().sendVar(2091, 250);
		getVarsManager().sendVar(4260, 200);
		getVarsManager().sendVar(2599, 22368316);
		getVarsManager().sendVar(2802, 200);
		getVarsManager().sendVar(2430, 400);
		getVarsManager().sendVar(2732, 11);
		getVarsManager().sendVarBit(17711, 1);
		getVarsManager().sendVarBit(21782, 1);
		getVarsManager().sendVarBit(21783, 1);
	}

	public void setKalphiteLair() {
		varbits.put(16280, 1);
		loadVarbits();
	}

	public void setKalphiteLairEntrance() {
		varbits.put(16281, 1);
		loadVarbits();
	}

	public boolean isKalphiteLairEntranceSetted() {
		return varbits.containsKey(16281) && varbits.get(16281) == 1;
	}

	public boolean isKalphiteLairSetted() {
		return varbits.containsKey(16280) && varbits.get(16280) == 1;
	}

	private void appendStarter() {
		if (recievedStarter == true)
			return;
		if (session.getIP().equals("127.0.0.1") && Settings.DEBUG)
			return;
		FriendsChat.requestJoin(this,
				Utils.formatPlayerNameForDisplay(Settings.MAIN_FC));
		getDialogueManager().startDialogue("ServerGuide");
		treasureHunter.setKeys(3);
		getPackets().sendGameMessage("You have 2 Treasure Hunter keys.");
		getPackets().sendForceGameMessage(
				Color.PURPLE,
				"Please view ::topic " + Settings.STARTER_GUIDE
						+ " for an indept starter guide!");
		recievedStarter = true;
	}

	public void sendDefaultPlayersOptions() {
		getPackets().sendPlayerOption("Follow", 2, false);
		getPackets().sendPlayerOption("Trade with", 4, false);
		getPackets().sendPlayerOption("Req Assist", 5, false);
		getPackets().sendPlayerOption("Examine", 6, false);
	}

	/**
	 * Logs the player out.
	 * 
	 * @param lobby
	 *            If we're logging out to the lobby.
	 */
	public void logout(boolean lobby) {
		if (!running || !started)
			return;
		long currentTime = Utils.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			getPackets()
					.sendGameMessage(
							"You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (getEmotesManager().getNextEmoteEnd() >= currentTime) {
			getPackets().sendGameMessage(
					"You can't log out while performing an action.");
			return;
		}
		if (familiar != null)
			familiar.dissmissFamiliar(true);

		if (isLocked()) {
			getPackets().sendGameMessage(
					"You can't log out while performing an action.");
			return;
		}
		GrandExchange.save();
		disconnect(false, lobby);
	}

	public void disconnect(boolean immediate, boolean lobby) {
		immediateFinish = immediate;
		ChannelFuture future = getPackets().sendLogout(lobby);
		if (isLobby() || immediate) { // worldthread does this anyway, so just
			// need to send packet for normal logout
			if (future == null)
				getSession().getChannel().close();
			else
				future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private transient boolean immediateFinish;
	private transient boolean finishing;

	public int divine;

	public int portable;

	@Override
	public void finish() {
		try {
			finish(0);
		} catch (Throwable e) {
			Logger.handle(e, "Process Player Finish");
		}
	}

	public boolean isFinishing() {
		return finishing;
	}

	private void finish(final int tryCount) {
		if (finishing || hasFinished())
			return;
		finishing = true;
		setOnline(false);
		if (lobby) {
			finishLobby();
			return;
		}
		// if combating doesnt stop when xlog this way ends combat
		stopAll(false, true,
				!(actionManager.getAction() instanceof PlayerCombat));

		if (!immediateFinish
				&& ((isDead() || isUnderCombat() || isLocked() || getEmotesManager()
						.isDoingEmote())) && tryCount < 4) {
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						finishing = false;
						finish(tryCount + 1);
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			}, 10, TimeUnit.SECONDS);
			return;
		}
		realFinish();
	}

	private void finishLobby() {
		if (hasFinished())
			return;
		if (currentFriendsChat != null)
			FriendsChat.detach(this);
		if (clanManager != null)
			clanManager.disconnect(this, false);
		setFinished(true);
		session.getChannel().close();
		PlayerHandlerThread.addLogout(this);
		World.removeLobbyPlayer(this);
	}

	private void realFinish() {
		if (hasFinished())
			return;
		Logger.globalLog(username, session.getIP(), new String(
				" has logged out."));
		// login
		running = false;
		stopAll();
		cutscenesManager.logout();
		controllerManager.logout(); // checks what to do on before logout for
		house.finish();
		dungManager.finish();
		GrandExchange.unlinkOffers(this);
		if (currentFriendsChat != null)
			FriendsChat.detach(this);
		if (clanManager != null)
			clanManager.disconnect(this, false);
		if (guestClanManager != null)
			guestClanManager.disconnect(this, true);
		if (familiar != null && !familiar.isFinished())
			familiar.dissmissFamiliar(true);
		else if (pet != null)
			pet.finish();
		if (slayerManager.getSocialPlayer() != null)
			slayerManager.resetSocialGroup(true);
		if (getNextWorldTile() != null)
			setLocation(getNextWorldTile());
		setFinished(true);
		session.getChannel().close();
		updateHighscores();
		PlayerHandlerThread.addLogout(this);
		World.updateEntityRegion(this);
		World.removePlayer(this);
		Logger.log("Logout", username + ":" + getSession().getIP());
	}

	private void updateStore() {
		if (Settings.HOSTED == false)
			return;
		Webstore.handleWebstore(this, getUsername());
	}

	public void updateHighscores() {
		if (Settings.HOSTED == false)
			return;
		new Highscores(this).submit();
	}

	public void refreshHitPoints() {
		if (getVarsManager().sendVarBit(1668, getHitpoints()))
			updateBuffs();
	}

	@Override
	public int getMaxHitpoints() {
		return skills.getLevel(Skills.HITPOINTS) * 100
				+ equipment.getEquipmentHpIncrease();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void processHit(Hit hit) {
		if (appearence.isHidden())
			return;
		super.processHit(hit);
	}

	public int getRights() {
		return rights;
	}

	public void setRights(int rights) {
		Account acc = Login.forceLoadAccount(getDisplayName());
		if (acc != null)
			acc.setRights(rights);
		this.rights = rights;
	}

	public int getMessageIcon() {
		return getRights() == 2 ? 2 : getRights();
	}

	public void setMessageIcon(int icon) {

	}

	public WorldPacketsEncoder getPackets() {
		return session.getWorldPackets();
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean isRunning() {
		return running;
	}

	public String getEmail() {
		return email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String newName) {
		this.displayName = newName;
		getAppearence().generateAppearenceData();
	}

	public String getMutedFor() {
		return mutedFor;
	}

	public void setMutedFor(String mutedFor) {
		this.mutedFor = mutedFor;
	}

	public Appearence getAppearence() {
		return appearence;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public LocalPlayerUpdate getLocalPlayerUpdate() {
		return localPlayerUpdate;
	}

	public LocalNPCUpdate getLocalNPCUpdate() {
		return localNPCUpdate;
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public Session getSession() {
		return session;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public boolean clientHasLoadedMapRegion() {
		return clientLoadedMapRegion < Utils.currentWorldCycle();
	}

	public void setClientHasLoadedMapRegion() {
		clientLoadedMapRegion = -1;
	}

	public boolean clientHasLoadedMapRegionFinished() {
		return clientLoadedMapRegion == -1;
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public DonationManager getDonationManager() {
		return donationManager;
	}

	public Skills getSkills() {
		return skills;
	}

	public byte getRunEnergy() {
		return runEnergy;
	}

	public double getWeight() {
		return inventory.getInventoryWeight() + equipment.getEquipmentWeight();
	}

	public boolean hasItem(int item, int amount) {
		return (getInventory().containsItem(item, amount) ? true : (bank
				.getItem(item) != null ? true : equipment.hasItem(item)));
	}

	public void drainRunEnergy() {
		if (dungManager.isInside() || getDonationManager().isAngelicDonator())
			return;
		setRunEnergy(runEnergy - 1);
	}

	public void setRunEnergy(int runEnergy) {
		if (runEnergy == this.runEnergy)
			return;
		if (runEnergy < 0)
			runEnergy = 0;
		if (runEnergy > 100)
			runEnergy = 100;
		this.runEnergy = (byte) runEnergy;
		getPackets().sendRunEnergy();
	}

	public boolean isResting() {
		return resting > 0;
	}

	public void setResting(int resting) {
		this.resting = resting;
		sendRunButtonConfig();
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	public void setRouteEvent(RouteEvent routeEvent) {
		this.routeEvent = routeEvent;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}

	@Override
	public double getMagePrayerMultiplier() {

		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMeleePrayerMultiplier() {

		if (Utils.random(5) == 0 && Combat.fullVeracsEquipped(this))
			return 1.0;

		return 0.6;
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (invulnerable) {
			hit.setDamage(0);
			return;
		}
		if (auraManager.usingPenance()) {
			int amount = (int) (hit.getDamage() * 0.05);
			if (amount > 0)
				prayer.boost(amount);
		}
		final Entity source = hit.getSource();
		if (source == null)
			return;
		if (hit.getLook() == HitLook.REGULAR_DAMAGE
				&& getEffectsManager().hasActiveEffect(EffectType.REFLECT)) {
			int damage = new Double(hit.getDamage() * 0.5).intValue();
			source.applyHit(new Hit(this, damage, HitLook.REFLECTED_DAMAGE));
			hit.setDamage(0);
			return;
		}
		if (getEffectsManager().hasActiveEffect(EffectType.VESTA_IMMUNITY)) {
			if (equipment.getWeaponId() != 13905
					&& equipment.getWeaponId() != 13907)
				getEffectsManager().removeEffect(EffectType.VESTA_IMMUNITY);
			else {
				int damage = new Double(hit.getDamage() * 0.5).intValue();
				hit.setDamage(damage);
				source.applyHit(new Hit(Player.this, damage,
						HitLook.REFLECTED_DAMAGE));
			}
		} else if (getEffectsManager().hasActiveEffect(
				EffectType.MIRRORBACK_SPIDER)) {
			Effect e = getEffectsManager().getEffectForType(
					EffectType.MIRRORBACK_SPIDER);
			if (e != null) {
				MirrorBackSpider spider = (MirrorBackSpider) e.getArguments()[0];
				if (spider.isDead())
					getEffectsManager().removeEffect(e.getType());
				else {
					int damage = new Double(hit.getDamage() * 0.5).intValue();
					hit.setDamage(damage);
					Hit reflectedHit = new Hit(this, damage,
							HitLook.REFLECTED_DAMAGE);
					spider.applyHit(reflectedHit);
					source.applyHit(reflectedHit);
				}
			}
		} else if (getEffectsManager().hasActiveEffect(EffectType.BARRICADE))
			hit.setDamage(0);
		if (getEffectsManager().hasActiveEffect(EffectType.DEBILITATE))
			hit.setDamage((int) (hit.getDamage() * 0.50));
		if (getEffectsManager().hasActiveEffect(EffectType.IMMORTALITY))
			hit.setDamage((int) (hit.getDamage() * 0.75));
		if (getEffectsManager().hasActiveEffect(EffectType.STAFF_OF_LIGHT)) {
			Item staff = equipment.getItem(Equipment.SLOT_WEAPON);
			if (staff == null || !staff.getName().equals("Staff of light"))
				getEffectsManager().removeEffect(EffectType.STAFF_OF_LIGHT);
			else {
				if (hit.getLook() == HitLook.MELEE_DAMAGE)
					hit.setDamage(new Double(hit.getDamage() * 0.5).intValue());
			}
		}
		if (getEquipment().containsOneItem(ItemIdentifiers.KALPHITE_DEFENDER,
				ItemIdentifiers.KALPHITE_REBOUNDER,
				ItemIdentifiers.KALPHITE_REPRISER,
				ItemIdentifiers.ANCIENT_DEFENDER,
				ItemIdentifiers.ANCIENT_REPRISER,
				ItemIdentifiers.ANCIENT_LANTERN)
				&& Utils.random(15) == 1) {
			getTemporaryAttributtes().put("KalphitePassive", true);
			hit.setDamage(new Double(hit.getDamage() * 0.5).intValue());
		}
		if (getEffectsManager().hasActiveEffect(EffectType.BERSERK))
			hit.setDamage((int) (hit.getDamage() * 1.5));
		if (getEffectsManager().hasActiveEffect(EffectType.RESONANCE)) {
			int damage = hit.getDamage();
			if (damage > 1) {
				hit.setDamage(1);
				heal(damage, 0, 0, true);
				getEffectsManager().removeEffect(EffectType.RESONANCE);
			}
		}
		Effect revengeEffect = getEffectsManager().getEffectForType(
				EffectType.REVENGE);
		if (revengeEffect != null) {
			if (hit.getDamage() > 0) {
				double nextDmgMultiplier = (double) revengeEffect
						.getArguments()[0] + 0.10;
				revengeEffect.getArguments()[0] = nextDmgMultiplier > 2.0 ? 2.0
						: nextDmgMultiplier;
			}
		}
		if (prayer.hasPrayersOn() && hit.getDamage() != 0) {
			if (source instanceof Familiar) {
				Familiar fam = (Familiar) source;
				if (!fam.hasSpecialOn())
					return;
				Player owner = fam.getOwner();
				if (owner == null)
					return;
				if (prayer.isActive(Prayer.PROTECT_FROM_SUMMONING))
					hit.setDamage(new Double(hit.getDamage() * 0.5).intValue());
				else if (prayer.isActive(Prayer.DEFLECT_SUMMONING)) {
					hit.setDamage(new Double(hit.getDamage() * 0.5).intValue());
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					if (deflectedDamage > 0) {
						owner.applyHit(new Hit(this, deflectedDamage,
								HitLook.REFLECTED_DAMAGE));
						setNextGraphics(new Graphics(2227));
						setNextAnimationNoPriority(new Animation(12573));
					}
				}
			} else {
				boolean isPvPReducation = source instanceof Player;
				if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
					if (prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getMagePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
					} else if (prayer.isActive(Prayer.DEFLECT_MAGIC)) {
						int deflectedDamage = (int) (hit.getDamage() * 0.1);
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getMagePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
						if (deflectedDamage > 0 && prayer.canReflect(source)) {
							source.applyHit(new Hit(this, deflectedDamage,
									HitLook.REFLECTED_DAMAGE));
							setNextGraphics(new Graphics(2228));
							setNextAnimationNoPriority(new Animation(12573));
						}
					}
				} else if (hit.getLook() == HitLook.RANGE_DAMAGE) {
					if (prayer.isActive(Prayer.PROTECT_FROM_MISSILES)) {
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getRangePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
					} else if (prayer.isActive(Prayer.DEFLECT_MISSILES)) {
						int deflectedDamage = (int) (hit.getDamage() * 0.1);
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getRangePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
						if (deflectedDamage > 0 && prayer.canReflect(source)) {
							source.applyHit(new Hit(this, deflectedDamage,
									HitLook.REFLECTED_DAMAGE));
							setNextGraphics(new Graphics(2229));
							setNextAnimationNoPriority(new Animation(12573));
						}
					}
				} else if (hit.getLook() == HitLook.MELEE_DAMAGE) {
					if (prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getMeleePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
					} else if (prayer.isActive(Prayer.DEFLECT_MELEE)) {
						int deflectedDamage = (int) (hit.getDamage() * 0.1);
						hit.setDamage((int) (hit.getDamage() * (getEffectsManager()
								.hasActiveEffect(EffectType.DEVOTION) ? isPvPReducation ? 0.25
								: 0.0
								: source.getMeleePrayerMultiplier())));
						if (hit.getDamage() == 0)
							hit.setDamage(1);
						if (deflectedDamage > 0 && prayer.canReflect(source)) {
							source.applyHit(new Hit(this, deflectedDamage,
									HitLook.REFLECTED_DAMAGE));
							setNextGraphics(new Graphics(2230));
							setNextAnimationNoPriority(new Animation(12573));
						}
					}
				}
			}
		}
		int shieldId = equipment.getShieldId();
		if (shieldId == 13740 || shieldId == 23698 || shieldId == 13742
				|| shieldId == 23699 || shieldId == 13738 || shieldId == 23697) { // divine,
																					// eylsian,
																					// and
																					// arcane
			int drain = (int) (Math.ceil(hit.getDamage() * 0.06));
			if (prayer.getPoints() >= drain) {
				hit.setDamage((int) (hit.getDamage() * 0.70));
				prayer.drain(drain);
			}
		}
		if (castedVeng && hit.getDamage() >= 4) {
			castedVeng = false;
			setNextForceTalk(new ForceTalk("Taste vengeance!"));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					source.applyHit(new Hit(Player.this,
							(int) (hit.getDamage() * 0.75),
							HitLook.REGULAR_DAMAGE));
				}
			});
		}
		getControllerManager().processIngoingHit(hit);
		if (source instanceof Player) {
			((Player) source).getPrayer().handleHitPrayers(this, hit);
			((Player) source).getControllerManager().processIncomingHit(hit,
					this);
		}

	}

	@Override
	public void removeHitpoints(final Hit hit) {
		super.removeHitpoints(hit);
		if (isDead())
			return;
		if (getEquipment().getRingId() == 2550) {
			if (hit.getSource() != null
					&& hit.getSource() != this
					&& hit.getDamage() > 0
					&& (hit.getLook() == HitLook.MELEE_DAMAGE
							|| hit.getLook() == HitLook.RANGE_DAMAGE || hit
							.getLook() == HitLook.MAGIC_DAMAGE)) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						hit.getSource().applyHit(
								new Hit(hit.getSource(), (int) (Math.ceil(hit
										.getDamage() * 0.01) * 10),
										HitLook.REGULAR_DAMAGE));
					}
				});
			}
		}
		if (getPrayer().hasPrayersOn()) {
			if (getPrayer().isActive(Prayer.REDEMPTION)
					&& (getHitpoints() < getMaxHitpoints() * 0.1)) {
				setNextGraphics(new Graphics(436));
				setHitpoints((int) (getHitpoints() + getSkills().getLevelForXp(
						Skills.PRAYER) * 2.5));
				getSkills().set(Skills.PRAYER, 0);
				getPrayer().setPoints(0, false);
			}
		}
		if (getEquipment().getAmuletId() == 11090
				&& getHitpoints() <= getMaxHitpoints() * 0.2) {// priority
			// over
			// ring
			// of
			// life
			heal((int) (getMaxHitpoints() * 0.3));
			getEquipment().deleteItem(11090, 1);
			getAppearence().generateAppearenceData();
			resetReceivedHits();
			getPackets()
					.sendGameMessage(
							"Your pheonix necklace heals you, but is destroyed in the process.");
		} else if (getEquipment().getAmuletId() != 11090
				&& getEquipment().getRingId() == 11090
				&& getHitpoints() <= getMaxHitpoints() * 0.1) {
			Magic.sendNormalTeleportSpell(this, 1, 0,
					DeathEvent.getRespawnHub(this));
			getEquipment().deleteItem(11090, 1);
			resetReceivedHits();
			getPackets()
					.sendGameMessage(
							"Your ring of life saves you, but is destroyed in the process.");
		}
	}

	@Override
	public void sendDeath(final Entity source) {
		if (source instanceof Player
				&& ((Player) source).getClanManager() != null
				&& ((Player) source).getClanManager().getClan() != null)
			((Player) source).getClanManager().getClan().increasePlayerKills();
		if (getEffectsManager().hasActiveEffect(EffectType.IMMORTALITY)) {
			resetReceivedHits();
			setHitpoints((int) (getMaxHitpoints() * 0.40));
			setNextAnimation(new Animation(18119));
			setNextGraphics(new Graphics(3630));
			World.sendGraphics(this, new Graphics(3631, 0, 0, getDirection(),
					true), this);
			getEffectsManager().removeEffect(EffectType.IMMORTALITY);
			return;
		}
		if (hasLegendaryPet()) {
			if (LegendaryPetAbilities.petLifeSaver(this, getPet())) {
				getPet().setPetLifeSaver(this, Utils.currentTimeMillis());
				return;
			}
		}
		if (prayer.hasPrayersOn()
				&& getTemporaryAttributtes().get("startedDuel") != Boolean.TRUE) {
			if (prayer.isActive(Prayer.RETRIBUTION)) {
				setNextGraphics(new Graphics(437));
				final Player target = this;

				for (int regionId : getMapRegionsIds()) {
					List<Integer> playersIndexes = World.getRegion(regionId)
							.getPlayerIndexes();
					if (playersIndexes != null) {
						for (int playerIndex : playersIndexes) {
							Player player = World.getPlayers().get(playerIndex);
							if (player == null
									|| !player.hasStarted()
									|| player.isDead()
									|| player.hasFinished()
									|| !player.withinDistance(this, 1)
									|| !player.isCanPvp()
									|| !target.getControllerManager().canHit(
											player))
								continue;
							player.applyHit(new Hit(
									target,
									Utils.random((int) (skills
											.getLevelForXp(Skills.PRAYER) * 2.5)),
									HitLook.REGULAR_DAMAGE));
						}
					}
					List<Integer> npcsIndexes = World.getRegion(regionId)
							.getNPCsIndexes();
					if (npcsIndexes != null) {
						for (int npcIndex : npcsIndexes) {
							NPC npc = World.getNPCs().get(npcIndex);
							if (npc == null
									|| npc.isDead()
									|| npc.hasFinished()
									|| !npc.withinDistance(this, 1)
									|| !npc.getDefinitions().hasAttackOption()
									|| !target.getControllerManager().canHit(
											npc))
								continue;
							npc.applyHit(new Hit(
									target,
									Utils.random((int) (skills
											.getLevelForXp(Skills.PRAYER) * 25.00)),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}

				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1, target.getY(),
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() - 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX(), target.getY() + 1,
										target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() - 1,
										target.getY() + 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() - 1, target.getPlane()));
						World.sendGraphics(target, new Graphics(438),
								new WorldTile(target.getX() + 1,
										target.getY() + 1, target.getPlane()));
					}
				});
			} else if (prayer.isActive(Prayer.WRATH)) {
				World.sendProjectile(this, new WorldTile(getX() + 2,
						getY() + 2, getPlane()), 2261, 24, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2, getY(),
						getPlane()), 2261, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() + 2,
						getY() - 2, getPlane()), 2261, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX() - 2,
						getY() + 2, getPlane()), 2261, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2, getY(),
						getPlane()), 2261, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX() - 2,
						getY() - 2, getPlane()), 2261, 41, 0, 41, 35, 30, 0);

				World.sendProjectile(this, new WorldTile(getX(), getY() + 2,
						getPlane()), 2261, 41, 0, 41, 35, 30, 0);
				World.sendProjectile(this, new WorldTile(getX(), getY() - 2,
						getPlane()), 2261, 41, 0, 41, 35, 30, 0);
				final Player target = this;
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						setNextGraphics(new Graphics(2259));

						for (int regionId : getMapRegionsIds()) {
							List<Integer> playersIndexes = World.getRegion(
									regionId).getPlayerIndexes();
							if (playersIndexes != null) {
								for (int playerIndex : playersIndexes) {
									Player player = World.getPlayers().get(
											playerIndex);
									if (player == null
											|| !player.hasStarted()
											|| player.isDead()
											|| player.hasFinished()
											|| !player.isCanPvp()
											|| !player
													.withinDistance(target, 2)
											|| !target.getControllerManager()
													.canHit(player))
										continue;
									player.applyHit(new Hit(
											target,
											(int) Utils.random((skills
													.getLevelForXp(Skills.PRAYER) * 25.00)),
											HitLook.REGULAR_DAMAGE));
								}
							}
							List<Integer> npcsIndexes = World.getRegion(
									regionId).getNPCsIndexes();
							if (npcsIndexes != null) {
								for (int npcIndex : npcsIndexes) {
									NPC npc = World.getNPCs().get(npcIndex);
									if (npc == null
											|| npc.isDead()
											|| npc.hasFinished()
											|| !npc.withinDistance(target, 2)
											|| !npc.getDefinitions()
													.hasAttackOption()
											|| !target.getControllerManager()
													.canHit(npc))
										continue;
									npc.applyHit(new Hit(
											target,
											(int) Utils.random((skills
													.getLevelForXp(Skills.PRAYER) * 25.00)),
											HitLook.REGULAR_DAMAGE));
								}
							}
						}

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() + 2,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 2, getY() - 2,
										getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() + 2,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY(), getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 2, getY() - 2,
										getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX(), getY() + 2, getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX(), getY() - 2, getPlane()));

						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() + 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() + 1, getY() - 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() + 1,
										getPlane()));
						World.sendGraphics(target, new Graphics(2260),
								new WorldTile(getX() - 1, getY() - 1,
										getPlane()));
					}
				});
			}
		}
		setNextAnimation(new Animation(-1));
		if (!controllerManager.sendDeath())
			return;
		lock(8);
		stopAll();
		if (familiar != null)
			familiar.sendDeath(this);
		getInterfaceManager().removeFamiliarInterface();
		final WorldTile deathTile = new WorldTile(this);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(getDeathAnimation());
				} else if (loop == 1) {
					getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					controllerManager.startController("DeathEvent", deathTile,
							hasSkull());
					final String FILE_PATH = Settings.getDropboxLocation()
							+ "logs/deaths/";
					try {
						DateFormat dateFormat = new SimpleDateFormat(
								"MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + getUsername()
										+ ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime())
								+ ", IP: " + getSession().getIP() + "] : died.");
						writer.newLine();
						writer.write("[" + dateFormat.format(cal.getTime())
								+ "] player location: " + getX() + ", "
								+ getY() + ", " + getPlane() + ".");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (loop == 4) {
					getMusicsManager().playMusicEffect(
							MusicsManager.DEATH_MUSIC_EFFECT);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	private void sendItemsOnDeath(Player killer, boolean dropItems,
			boolean dropLostItems) {
		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(this, true,
				dropItems, getPrayer().isProtectingItem());
		sendItemsOnDeath(killer, getLastWorldTile(), new WorldTile(this), true,
				slots, dropLostItems);
	}

	/*
	 * default items on death, now only used for wilderness
	 */
	public void sendItemsOnDeath(Player killer, boolean dropItems) {
		sendItemsOnDeath(killer, dropItems, true);
	}

	/*
	 * default items on death, now only used for wilderness
	 */
	public void sendItemsOnDeath(Player killer) {
		sendItemsOnDeath(killer, hasSkull());
	}

	public void sendItemsOnDeath(Player killer, WorldTile deathTile,
			WorldTile respawnTile, boolean wilderness, Integer[][] slots,
			boolean dropLostItems) {
		/*
		 * if ((((killer != null && killer.getRights() == 2) || getRights() ==
		 * 2) && Settings.HOSTED) || hasFinished()) return;
		 */
		if (Settings.HOSTED) {
			if (getRights() >= 2 || hasFinished())
				return;
			if (killer != null) {
				if (killer.getRights() >= 2)
					return;
			}
		}
		charges.die(slots[1], slots[3]);
		auraManager.removeAura();
		Item[][] items = GraveStone.getItemsKeptOnDeath(this, slots);
		inventory.reset();
		equipment.reset();
		appearence.generateAppearenceData();
		for (Item item : items[0]) {
			inventory.addItem(item.getId(), item.getAmount());
		}
		if (dropLostItems) {
			if (items[1].length != 0) {
				if (wilderness) {
					if (!isBeginningAccount())
						for (Item item : items[1])
							World.addGroundItem(item, deathTile,
									killer == null ? this : killer, true, 60,
									killer == null ? 1 : 0);
				} else
					new GraveStone(this, deathTile, items[1]);
			}

		}
		if (killer != null)
			Logger.globalLog(
					username,
					session.getIP(),
					new String(killer.getUsername() + " has killed " + username
							+ " with the ip: " + killer.getSession().getIP()
							+ " items are as follows:"
							+ Arrays.toString(items[1]).replace("null,", "")
							+ " ."));
	}

	public void increaseKillCount(Player killed) {
		killed.deathCount++;
		if (!canIncreaseKillCount(killed))
			return;
		killCount++;
	}

	private boolean canIncreaseKillCount(Player killed) {
		if (killed.isBeginningAccount()
				|| killed.getLastGameMAC().equals(lastGameMAC)
				|| killed.getSession().getIP().equals(session.getIP())
				|| (lastPlayerKill != null && killed.getUsername().equals(
						lastPlayerKill))
				|| (lastPlayerMAC != null && killed.getLastGameMAC().equals(
						lastPlayerMAC)))
			return false;
		lastPlayerKill = killed.getUsername();
		lastPlayerMAC = killed.getLastGameMAC();
		return true;
	}

	@Override
	public int getSize() {
		return appearence.getSize();
	}

	public boolean isCanPvp() {
		return canPvp;
	}

	public void setCanPvp(boolean canPvp) {
		if (this.canPvp == canPvp)
			return;
		this.canPvp = canPvp;
		appearence.generateAppearenceData();
		getPackets().sendPlayerOption(canPvp ? "Attack" : "null", 1, true);
		getPackets().sendPlayerUnderNPCPriority(canPvp);
		if (canPvp)
			getPackets().sendGameMessage(
					"You cannot display cosmetic gear in PvP areas.", true);
		if (getControllerManager().getController() instanceof Wilderness
				&& familiar == null)
			skills.sendCombatLevel();
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public boolean isLocked() {
		return lockDelay > WorldThread.WORLD_CYCLE;// Utils.currentTimeMillis();
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = time == -1 ? Long.MAX_VALUE : WorldThread.WORLD_CYCLE
				+ time;/*
						 * Utils . currentTimeMillis ( ) + ( time * 600 )
						 */
		;
	}

	public void unlock() {
		lockDelay = 0;
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay,
			int totalDelay) {
		useStairs(emoteId, dest, useDelay, totalDelay, null);
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay,
			int totalDelay, final String message) {
		useStairs(emoteId, dest, useDelay, totalDelay, message, false);
	}

	public void useStairs(int emoteId, final WorldTile dest, int useDelay,
			int totalDelay, final String message, final boolean resetAnimation) {
		stopAll();
		lock(totalDelay);
		if (emoteId != -1)
			setNextAnimation(new Animation(emoteId));
		if (useDelay == 0)
			setNextWorldTile(dest);
		else {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (isDead())
						return;
					if (resetAnimation)
						setNextAnimation(new Animation(-1));
					setNextWorldTile(dest);
					if (message != null)
						getPackets().sendGameMessage(message);
				}
			}, useDelay - 1);
		}
	}

	public int getLastX() {
		return lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public int getLastPlane() {
		return lastPlane;
	}

	public void setLastX(int x) {
		this.lastX = x;
	}

	public void setLastY(int Y) {
		this.lastY = Y;
	}

	public void setLastPlane(int plane) {
		this.lastPlane = plane;
	}

	public Bank getBank() {
		if (!isAnIronMan() && getTemporaryAttributtes().get("ClanBank") != null
				&& (Boolean) getTemporaryAttributtes().get("ClanBank"))
			return getClanManager().getClan().getClanBank(this);
		else if (getDonationManager().isDivineDonator()
				&& getTemporaryAttributtes().get("Bank2") != null
				&& (Boolean) getTemporaryAttributtes().get("Bank2"))
			return bank2;
		return bank;
	}

	public Bank getBank2() {
		return bank2;
	}

	public ControllerManager getControllerManager() {
		return controllerManager;
	}

	public void switchMouseButtons() {
		mouseButtons = !mouseButtons;
		refreshMouseButtons();
	}

	public void switchAllowChatEffects() {
		allowChatEffects = !allowChatEffects;
		refreshAllowChatEffects();
	}

	public void switchProfanityFilter() {
		profanityFilter = !profanityFilter;
		refreshProfanityFilter();
	}

	public void switchRightClickReporting() {
		rightClickReporting = !rightClickReporting;
		getPackets().sendPlayerOption(rightClickReporting ? "Report" : "null",
				6, false);
		refreshRightClickReporting();
	}

	private void refreshAllowChatEffects() {
		getVarsManager().sendVar(456, allowChatEffects ? 0 : 1);
	}

	private void refreshRightClickReporting() {
		getVarsManager().sendVarBit(16564, rightClickReporting ? 1 : 0);
	}

	private void refreshProfanityFilter() {
		getPackets().sendCSVarInteger(2834, profanityFilter ? 1 : 0);
	}

	private void refreshMouseButtons() {
		getVarsManager().sendVar(455, mouseButtons ? 0 : 1);
	}

	private void refreshPrivateChatSetup() {
		getVarsManager().sendVarBit(7423, privateChatSetup);
	}

	private void refreshFriendChatSetup() {
		getVarsManager().sendVarBit(1190, friendChatSetup);
	}

	private void refreshClanChatSetup() {
		getVarsManager().sendVarBit(1188, clanChatSetup);
	}

	private void refreshGuestChatSetup() {
		getVarsManager().sendVarBit(1191, guestChatSetup);
	}

	private void refreshChatsSetup() {
		refreshFriendChatSetup();
		refreshClanChatSetup();
		refreshGuestChatSetup();
		refreshPrivateChatSetup();
	}

	public void setPrivateChatSetup(int privateChatSetup) {
		this.privateChatSetup = privateChatSetup;
		refreshPrivateChatSetup();
	}

	// color
	public void setChatSetup(int chatSetup) {
		/*
		 * if (componentId >= 34 && componentId <= 53)
		 * player.setClanChatSetup(componentId - 34); else if (componentId >= 63
		 * && componentId <= 80) player.setPrivateChatSetup(componentId - 63);
		 * else if (componentId >= 86 && componentId <= 105)
		 * player.setFriendChatSetup(componentId - 86); else if (componentId >=
		 * 110 && componentId <= 129) player.setGuestChatSetup(componentId -
		 * 110);
		 */
		Integer menuIndex = (Integer) getTemporaryAttributtes().get(
				Key.CHAT_SETUP);
		if (menuIndex == null || menuIndex == 0)
			setFriendChatSetup(chatSetup);
		else if (menuIndex == 1)
			setPrivateChatSetup(chatSetup);
		else if (menuIndex == 2)
			setClanChatSetup(chatSetup);
		else if (menuIndex == 3)
			setGuestChatSetup(chatSetup);
		else if (menuIndex == 4) { // group chat TODO

		} else if (menuIndex == 5) { // group chat team TODO

		} else if (menuIndex == 6) { // TODO

		}
	}

	public void setClanChatSetup(int clanChatSetup) {
		this.clanChatSetup = clanChatSetup;
		refreshClanChatSetup();
	}

	public void setGuestChatSetup(int guestChatSetup) {
		this.guestChatSetup = guestChatSetup;
		refreshGuestChatSetup();
	}

	public void setFriendChatSetup(int friendChatSetup) {
		this.friendChatSetup = friendChatSetup;
		refreshFriendChatSetup();
	}

	public int getPrivateChatSetup() {
		return privateChatSetup;
	}

	public boolean isForceNextMapLoadRefresh() {
		return forceNextMapLoadRefresh;
	}

	public void setForceNextMapLoadRefresh(boolean forceNextMapLoadRefresh) {
		this.forceNextMapLoadRefresh = forceNextMapLoadRefresh;
	}

	public FriendsIgnores getFriendsIgnores() {
		return friendsIgnores;
	}

	public void addPotDelay(long time) {
		potDelay = time + Utils.currentTimeMillis();
	}

	public long getPotDelay() {
		return potDelay;
	}

	public void addFoodDelay(long time) {
		foodDelay = time + Utils.currentTimeMillis();
	}

	public long getFoodDelay() {
		return foodDelay;
	}

	public void setAntipoisonDelay(int cycles) {
		getEffectsManager().startEffect(
				new Effect(EffectType.ANTIPOISON, cycles));
	}

	public MusicsManager getMusicsManager() {
		return musicsManager;
	}

	public HintIconsManager getHintIconsManager() {
		return hintIconsManager;
	}

	public boolean isCastVeng() {
		return castedVeng;
	}

	public void setCastVeng(boolean castVeng) {
		this.castedVeng = castVeng;
	}

	public int getKillCount() {
		return killCount;
	}

	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	public int setKillCount(int killCount) {
		return this.killCount = killCount;
	}

	public int getDeathCount() {
		return deathCount;
	}

	public int setDeathCount(int deathCount) {
		return this.deathCount = deathCount;
	}

	public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
		this.closeInterfacesEvent = closeInterfacesEvent;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public ChargesManager getCharges() {
		return charges;
	}

	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	public int getHiddenBrother() {
		return hiddenBrother;
	}

	public void resetBarrows() {
		hiddenBrother = -1;
		killedBarrowBrothers = new boolean[7]; // includes new bro for future
		// use
		barrowsKillCount = 0;
	}

	public int getPenguinPoints() {
		return penguinPoints;
	}

	public void setPenguinPoints(int amt) {
		penguinPoints = amt;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public void setVotePoints(int amt) {
		votePoints = amt;
	}

	public boolean hasVotedInLast12Hours() {
		return (Utils.currentTimeMillis() - lastVote) < (1000 * 60 * 60 * 12);
	}

	public int[] getPouches() {
		return pouches;
	}

	public EmotesManager getEmotesManager() {
		return emotesManager;
	}

	public String getLastGameIp() {
		return lastGameIp;
	}

	public String getLastGameMAC() {
		return lastGameMAC;
	}

	public long getLastGameLogin() {
		return lastGameLogin;
	}

	public PriceCheckManager getPriceCheckManager() {
		return priceCheckManager;
	}

	public void setCommendation(int pestPoints) {
		if (pestPoints >= 1000) {
			this.pestPoints = 1000;
			getPackets()
					.sendGameMessage(
							"You have reached the maximum amount of commendation points, you may only have 1000 at a time.");
			return;
		}
		this.pestPoints = pestPoints;
	}

	public int getCommendation() {
		return pestPoints;
	}

	public void increaseStealingCreationPoints(int scPoints) {
		stealingCreationPoints += scPoints;
	}

	public int getStealingCreationPoints() {
		return stealingCreationPoints;
	}

	public long getLastPublicMessage() {
		return lastPublicMessage;
	}

	public void setLastPublicMessage(long lastPublicMessage) {
		this.lastPublicMessage = lastPublicMessage;
	}

	public CutscenesManager getCutscenesManager() {
		return cutscenesManager;
	}

	public void kickPlayerFromClanChannel(String name) {
		if (clanManager == null)
			return;
		clanManager.kickPlayerFromChat(this, name);
	}

	public void sendClanChannelMessage(ChatMessage message) {
		if (clanManager == null)
			return;
		clanManager.sendMessage(this, message);
	}

	public void sendGuestClanChannelMessage(ChatMessage message) {
		if (guestClanManager == null)
			return;
		guestClanManager.sendMessage(this, message);
	}

	public void sendClanChannelQuickMessage(QuickChatMessage message) {
		if (clanManager == null)
			return;
		clanManager.sendQuickMessage(this, message);
	}

	public void sendGuestClanChannelQuickMessage(QuickChatMessage message) {
		if (guestClanManager == null)
			return;
		guestClanManager.sendQuickMessage(this, message);
	}

	public void sendPublicChatMessage(PublicChatMessage message) {

		for (int i = 0; i < getLocalPlayerUpdate()
				.getLocalPlayersIndexesCount(); i++) {
			Player player = getLocalPlayerUpdate().getLocalPlayers()[getLocalPlayerUpdate()
					.getLocalPlayersIndexes()[i]];
			if (player == null || !player.isRunning() || player.hasFinished()) // shouldnt
				continue;
			player.getPackets().sendPublicMessage(this, message);
		}
	}

	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	public void setCompletionistCapeCustomized(int[] skillcapeCustomized) {
		this.completionistCapeCustomized = skillcapeCustomized;
	}

	public int[] getMaxedCapeCustomized() {
		return maxedCapeCustomized;
	}

	public void setMaxedCapeCustomized(int[] maxedCapeCustomized) {
		this.maxedCapeCustomized = maxedCapeCustomized;
	}

	public void setSkullId(int skullId) {
		this.skullId = skullId;
	}

	public int getSkullId() {
		return skullId;
	}

	public void addLogicPacketToQueue(LogicPacket packet) {
		for (LogicPacket p : logicPackets) {
			if (p.getId() == packet.getId()) {
				logicPackets.remove(p);
				break;
			}
		}
		logicPackets.add(packet);
	}

	public DominionTower getDominionTower() {
		return dominionTower;
	}

	public void refreshMeleeAttackRating() {
		if (getVarsManager().sendVar(1029, getMeleeAttackRating()))
			updateBuffs();
	}

	public int getMeleeAttackRating() {
		int percentage = 0;
		if (getEffectsManager().hasActiveEffect(EffectType.DRAGON_BATTLEAXE))
			percentage -= 20;
		return percentage;
	}

	public void refreshMeleeStrengthRating() {
		if (getVarsManager().sendVar(1030, getMeleeStrengthRating()))
			updateBuffs();
	}

	public int getMeleeStrengthRating() {
		int percentage = 0;
		if (getEffectsManager().hasActiveEffect(EffectType.DRAGON_BATTLEAXE))
			percentage += 20;
		return percentage;
	}

	public int getDefenceRating() {
		int percentage = 0;
		if (getEffectsManager().hasActiveEffect(EffectType.DRAGON_BATTLEAXE))
			percentage -= 10;
		return percentage;
	}

	public void refreshDefenceRating() {
		if (getVarsManager().sendVar(1035, getDefenceRating()))
			updateBuffs();
	}

	public Trade getTrade() {
		return trade;
	}

	public void setDFSDelay(long teleDelay) {
		getTemporaryAttributtes().put("dfs_delay",
				teleDelay + Utils.currentTimeMillis());
		getTemporaryAttributtes().remove("dfs_shield_active");
	}

	public long getDFSDelay() {
		Long teleblock = (Long) getTemporaryAttributtes().get("dfs_delay");
		if (teleblock == null)
			return 0;
		return teleblock;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public FriendsChat getCurrentFriendsChat() {
		return currentFriendsChat;
	}

	public void setCurrentFriendsChat(FriendsChat chat) {
		this.currentFriendsChat = chat;
	}

	public int getLastFriendsChatRank() {
		return lastFriendsChatRank;
	}

	public void setLastFriendsChatRank(int rank) {
		lastFriendsChatRank = rank;
	}

	public String getLastFriendsChat() {
		return lastFriendsChat;
	}

	public void setLastFriendsChat(String chat) {
		this.lastFriendsChat = chat;
	}

	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	public boolean containsOneItem(int... itemIds) {
		if (getInventory().containsOneItem(itemIds))
			return true;
		if (getEquipment().containsOneItem(itemIds))
			return true;
		Familiar familiar = getFamiliar();
		if (familiar != null
				&& ((familiar.getBob() != null
						&& familiar.getBob().containsOneItem(itemIds) || familiar
							.isFinished())))
			return true;
		return false;
	}

	public boolean canSpawn() {
		if (getControllerManager().getController() instanceof BossInstanceController
				|| getControllerManager().getController() instanceof PestControlLobby
				|| getControllerManager().getController() instanceof PestControlGame
				|| getControllerManager().getController() instanceof ZEDController
				|| getControllerManager().getController() instanceof GodWars
				|| getControllerManager().getController() instanceof DTController
				|| getControllerManager().getController() instanceof CastleWarsPlaying
				|| getControllerManager().getController() instanceof CastleWarsWaiting
				|| getControllerManager().getController() instanceof FightCaves
				|| getControllerManager().getController() instanceof FightKiln
				|| getControllerManager().getController() instanceof NomadsRequiem
				|| getControllerManager().getController() instanceof QueenBlackDragonController
				|| getControllerManager().getController() instanceof WarController
				|| getControllerManager().getController() instanceof StealingCreationLobbyController
				|| getControllerManager().getController() instanceof StealingCreationController) {
			return false;
		}
		return !isCanPvp() && !dungManager.isInside();
	}

	public List<Integer> getSwitchItemCache() {
		return switchItemCache;
	}

	public AuraManager getAuraManager() {
		return auraManager;
	}

	public List<String> getOwnedObjectManagerKeys() {
		if (ownedObjectsManagerKeys == null) // temporary
			ownedObjectsManagerKeys = new LinkedList<String>();
		return ownedObjectsManagerKeys;
	}

	public boolean hasInstantSpecial(Item weapon) {
		return weapon.getName().contains("Noxious")
				|| weapon.getDefinitions().getCSOpcode(4331) == 1;
	}

	public void performInstantSpecial(final Item weapon) {
		int specAmt = weapon.getDefinitions().getSpecialAmmount();
		if (combatDefinitions.hasRingOfVigour())
			specAmt *= 0.9;
		if (combatDefinitions.getSpecialAttackPercentage() < specAmt) {
			getPackets().sendGameMessage("You don't have enough power left.");
			combatDefinitions.desecreaseSpecialAttack(0);
			return;
		}
		if (getSwitchItemCache().size() > 0) {
			ButtonHandler.submitSpecialRequest(this);
			return;
		}
		if (!isUnderCombat()) // cuz of sheating
			PlayerCombat.addAttackingDelay(this);
		switch (weapon.getName()) {
		case "Noxious scythe":
		case "Noxious staff":
		case "Noxious longbow":
		case "Granite maul":
			if (!(getActionManager().getAction() instanceof PlayerCombat)) {
				getPackets().sendGameMessage(
						"You need a target to use this ability.");
				return;
			}
			PlayerCombat combat = (PlayerCombat) getActionManager().getAction();
			Entity target = combat.getTarget();
			if (target == null || target.isDead()) {
				getPackets().sendGameMessage(
						"You need a target to use this ability.");
				return;
			}
			combat.handleSpecialAttack(this);
			return;
		case "Dragon battleaxe":
			setNextAnimation(new Animation(1056));
			setNextGraphics(new Graphics(246));
			setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
			getEffectsManager().startEffect(
					new Effect(EffectType.DRAGON_BATTLEAXE, 100));
			break;
		case "Staff of light":
			setNextAnimation(new Animation(12804));
			setNextGraphics(new Graphics(2319));// 2320
			setNextGraphics(new Graphics(2321));
			getEffectsManager().startEffect(
					new Effect(EffectType.STAFF_OF_LIGHT, 100));
			break;
		}
		combatDefinitions.desecreaseSpecialAttack(specAmt);
	}

	public void setDisableEquip(boolean equip) {
		disableEquip = equip;
	}

	public boolean isEquipDisabled() {
		return disableEquip;
	}

	public int getPublicStatus() {
		return publicStatus;
	}

	public void setPublicStatus(int publicStatus) {
		this.publicStatus = publicStatus;
		getPackets().sendGameBarStages();
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public int getCoalBag() {
		return coalBag;
	}

	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
		getPackets().sendGameBarStages();
	}

	public void setCoalBag(int coalBag) {
		this.coalBag = coalBag;
	}

	public int getClanStatus() {
		return clanStatus;
	}

	public void setClanStatus(int clanStatus) {
		this.clanStatus = clanStatus;
		getPackets().sendGameBarStages();
	}

	public int getPersonalStatus() {
		return personalStatus;
	}

	public void setPersonalStatus(int personalStatus) {
		if (legacyCombatMode || legacyInterfaceMode)
			friendsIgnores.setPmStatus(personalStatus, true);
		this.personalStatus = personalStatus;
		getPackets().sendGameBarStages();
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
		getPackets().sendGameBarStages();
	}

	public int getAssistStatus() {
		return assistStatus;
	}

	public void setAssistStatus(int assistStatus) {
		if (assistStatus != this.assistStatus) {
			this.assistStatus = assistStatus;
		}
		getPackets().sendGameBarStages();
	}

	public int getFriendsChatStatus() {
		return friendsChatStatus;
	}

	public void setFriendsChatStatus(int friendsChatStatus) {
		this.friendsChatStatus = friendsChatStatus;
		getPackets().sendGameBarStages();
	}

	public Notes getNotes() {
		return notes;
	}

	public IsaacKeyPair getIsaacKeyPair() {
		return isaacKeyPair;
	}

	public QuestManager getQuestManager() {
		return questManager;
	}

	public boolean isCompletedFightCaves() {
		return compCapeManager.hasCompleted(Requirement.FIGHT_CAVES);
	}

	public void setCompletedFightCaves() {
		if (!compCapeManager.hasCompleted(Requirement.FIGHT_CAVES)) {
			compCapeManager.increaseRequirement(Requirement.FIGHT_CAVES, 1);
			varbits.put(3910, 1);
			loadVarbits();
		}
	}

	public boolean isCompletedFightKiln() {
		return compCapeManager.hasCompleted(Requirement.FIGHT_KILN);
	}

	public void setCompletedFightKiln() {
		compCapeManager.increaseRequirement(Requirement.FIGHT_KILN, 1);
	}

	public boolean isCantTrade() {
		return cantTrade;
	}

	public void setCantTrade(boolean canTrade) {
		this.cantTrade = canTrade;
	}

	public String getYellColor() {
		return yellColor;
	}

	public void setYellColor(String yellColor) {
		this.yellColor = yellColor;
	}

	private String titleString;
	private String titleColor;
	private String titleShade;

	private String customChatShade;

	public String getCustomTitle() {
		return customTitle;
	}

	public String getTitleColor() {
		return titleColor;
	}

	public String getTitleShade() {
		return titleShade;
	}

	public void setCustomTitle(String newTitle) {
		this.customTitle = newTitle;
	}

	public void setTitleColor(String color) {
		this.titleColor = color;
	}

	public void setTitleShade(String color) {
		this.titleShade = color;
	}

	public String getTitleString() {
		return titleString;
	}

	public String setTitleString(String newTitle) {
		return titleString = newTitle;
	}

	public String getFullTitle(boolean trim, boolean yell) {
		if (yell && getDonationManager().isDonator()
				&& (customTitle == null || customTitle.equalsIgnoreCase("")))
			customTitle = "Donator";
		StringBuilder sb = new StringBuilder();
		sb.append(titleColor != null ? "<col=" + titleColor + ">" : "");
		sb.append(titleShade != null ? "<shad=" + titleShade + ">" : "");
		sb.append(trim ? customTitle.trim() : customTitle);
		sb.append(titleShade != null ? "</shad>" : "");
		sb.append(titleColor != null ? "</col>" : "");
		return sb.toString();
	}

	public String getFullTitle2(boolean trim) {
		if (getDonationManager().isDonator()
				&& (customTitle == null || customTitle.equalsIgnoreCase("")))
			customTitle = "Donator";
		StringBuilder sb = new StringBuilder();
		sb.append(titleColor != null ? "<col=" + titleColor + ">" : "");
		sb.append(titleShade != null ? "<shad=" + titleShade + ">" : "");
		sb.append(trim ? titleString.trim() : titleString);
		sb.append(titleShade != null ? "</shad>" : "");
		sb.append(titleColor != null ? "</col>" : "");
		sb.append(" ");
		return sb.toString();
	}

	public String getCustomMessage(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append(titleColor != null ? "<col=" + titleColor + ">" : "");
		sb.append(titleShade != null ? "<shad=" + titleShade + ">" : "");
		sb.append(msg);
		sb.append(titleShade != null ? "</shad>" : "");
		sb.append(titleColor != null ? "</col>" : "");
		return sb.toString();
	}

	public void resetCustomTitle() {
		this.customTitle = null;
		this.titleColor = null;
		this.titleShade = null;
		this.titleString = "";
	}

	public String getCustomTitleColor() {
		return customTitleColor;
	}

	public void setCustomTitleColor(String customTitleColor) {
		this.customTitleColor = customTitleColor;
	}

	public boolean getCustomTitleCapitalize() {
		return customTitleCapitalize;
	}

	public void setCustomTitleCapitalize(boolean customTitleCapitalize) {
		this.customTitleCapitalize = customTitleCapitalize;
	}

	public boolean getCustomTitleActive() {
		return customTitleActive;
	}

	public void setCustomTitleActive(boolean customTitleActive) {
		this.customTitleActive = customTitleActive;
	}

	public String getCustomYellTag() {
		return customYellTag;
	}

	public void setCustomYellTag(String customYellTag) {
		this.customYellTag = customYellTag;
	}

	public boolean getCustomYellTagActive() {
		return customYellTagActive;
	}

	public void setCustomYellTagActive(boolean customYellTagActive) {
		this.customYellTagActive = customYellTagActive;
	}

	/**
	 * Gets the pet.
	 * 
	 * @return The pet.
	 */
	public Pet getPet() {
		return pet;
	}

	/**
	 * Sets the pet.
	 * 
	 * @param pet
	 *            The pet to set.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

	/**
	 * Gets the petManager.
	 * 
	 * @return The petManager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}

	/**
	 * Sets the petManager.
	 * 
	 * @param petManager
	 *            The petManager to set.
	 */
	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

	public boolean isXpLocked() {
		return xpLocked;
	}

	public void setXpLocked(boolean locked) {
		this.xpLocked = locked;
	}

	public boolean isYellOff() {
		return yellOff;
	}

	public void setYellOff(boolean yellOff) {
		this.yellOff = yellOff;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	public double getHpBoostMultiplier() {
		return hpBoostMultiplier;
	}

	public void setHpBoostMultiplier(double hpBoostMultiplier) {
		this.hpBoostMultiplier = hpBoostMultiplier;
	}

	/**
	 * Gets the killedQueenBlackDragon.
	 * 
	 * @return The killedQueenBlackDragon.
	 */
	public boolean isKilledQueenBlackDragon() {
		return killedQueenBlackDragon;
	}

	/**
	 * Sets the killedQueenBlackDragon.
	 * 
	 * @param killedQueenBlackDragon
	 *            The killedQueenBlackDragon to set.
	 */
	public void setKilledQueenBlackDragon(boolean killedQueenBlackDragon) {
		this.killedQueenBlackDragon = killedQueenBlackDragon;
	}

	boolean hasLargeSceneView() {
		return largeSceneView;
	}

	public void setLargeSceneView(boolean largeSceneView) {
		this.largeSceneView = largeSceneView;
	}

	public boolean isOldItemsLook() {
		return oldItemsLook;
	}

	/**
	 * @return the runeSpanPoint
	 */
	public int getRuneSpanPoints() {
		return runeSpanPoints;
	}

	/**
	 * @param runeSpanPoints
	 *            the runeSpanPoint to set
	 */
	public void setRuneSpanPoint(int runeSpanPoints) {
		this.runeSpanPoints = runeSpanPoints;
	}

	/**
	 * Adds points
	 * 
	 * @param points
	 */
	public void addRunespanPoints(int points) {
		this.runeSpanPoints += points;
	}

	public DuelRules getDuelRules() {
		return duelRules;
	}

	public void setLastDuelRules(DuelRules duelRules) {
		this.duelRules = duelRules;
	}

	public boolean isTalkedWithMarv() {
		return talkedWithMarv;
	}

	public void setTalkedWithMarv() {
		talkedWithMarv = true;
	}

	public int getCrucibleHighScore() {
		return crucibleHighScore;
	}

	public void increaseCrucibleHighScore() {
		crucibleHighScore++;
	}

	public House getHouse() {
		return house;
	}

	public AcceptAid getAcceptAid() {
		return acceptAid;
	}

	public boolean isFilteringProfanity() {
		return profanityFilter;
	}

	public MoneyPouch getMoneyPouch() {
		return moneyPouch;
	}

	public int getCannonBalls() {
		return cannonBalls;
	}

	public void addCannonBalls(int cannonBalls) {
		this.cannonBalls += cannonBalls;
	}

	public void removeCannonBalls() {
		this.cannonBalls = 0;
	}

	public FarmingManager getFarmingManager() {
		return farmingManager;
	}

	public Toolbelt getToolbelt() {
		return toolbelt;
	}

	public VarsManager getVarsManager() {
		return varsManager;
	}

	public boolean isLootShareEnabled() {
		return lootShare;
	}

	public void enableLootShare() {
		if (!isLootShareEnabled()) {
			getPackets().sendGameMessage("LootShare is now active.");
			lootShare = true;
		}
		refreshLootShare();
	}

	public void disableLootShare() {
		lootShare = false;
		refreshLootShare();
	}

	void refreshLootShare() {
		// need to force cuz autoactivates when u click on it even if no chat
		varsManager.forceSendVarBit(3306, lootShare ? 1 : 0);
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || refreshClanIcon;
	}

	public boolean isRefreshClanIcon() {
		return refreshClanIcon;
	}

	public void setRefreshClanIcon(boolean refreshClanIcon) {
		this.refreshClanIcon = refreshClanIcon;
	}

	public ClansManager getClanManager() {
		return clanManager;
	}

	public void setClanManager(ClansManager clanManager) {
		this.clanManager = clanManager;
	}

	public ClansManager getGuestClanManager() {
		return guestClanManager;
	}

	public void setGuestClanManager(ClansManager guestClanManager) {
		this.guestClanManager = guestClanManager;
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String clanName) {
		this.clanName = clanName;
	}

	public boolean isConnectedClanChannel() {
		return connectedClanChannel || lobby;
	}

	public void setConnectedClanChannel(boolean connectedClanChannel) {
		this.connectedClanChannel = connectedClanChannel;
	}

	public void setVerboseShopDisplayMode(boolean verboseShopDisplayMode) {
		this.verboseShopDisplayMode = verboseShopDisplayMode;
		refreshVerboseShopDisplayMode();
	}

	public void refreshVerboseShopDisplayMode() {
		varsManager.sendVarBit(987, verboseShopDisplayMode ? 0 : 1);
	}

	public int getGraveStone() {
		return graveStone;
	}

	public void setGraveStone(int graveStone) {
		this.graveStone = graveStone;
	}

	public GrandExchangeManager getGeManager() {
		return geManager;
	}

	public SlayerManager getSlayerManager() {
		return slayerManager;
	}

	public TreasureHunter getTreasureHunter() {
		return treasureHunter;
	}

	public TreasureTrailsManager getTreasureTrailsManager() {
		return treasureTrailsManager;
	}

	public boolean[] getShosRewards() {
		return shosRewards;
	}

	public boolean isKilledLostCityTree() {
		return killedLostCityTree;
	}

	public void setKilledLostCityTree(boolean killedLostCityTree) {
		this.killedLostCityTree = killedLostCityTree;
	}

	public double[] getWarriorPoints() {
		return warriorPoints;
	}

	public void setWarriorPoints(int index, double pointsDifference) {
		warriorPoints[index] += pointsDifference;
		if (warriorPoints[index] < 0) {
			Controller Controller = getControllerManager().getController();
			if (Controller == null || !(Controller instanceof WarriorsGuild))
				return;
			WarriorsGuild guild = (WarriorsGuild) Controller;
			guild.inCyclopse = false;
			setNextWorldTile(WarriorsGuild.CYCLOPS_LOBBY);
			warriorPoints[index] = 0;
		} else if (warriorPoints[index] > 65535)
			warriorPoints[index] = 65535;
		refreshWarriorPoints(index);
	}

	public void refreshWarriorPoints(int index) {
		varsManager.sendVarBit(index + 15399, (int) warriorPoints[index]);
	}

	private void warriorCheck() {
		if (warriorPoints == null || warriorPoints.length != 6)
			warriorPoints = new double[6];
	}

	public int getFavorPoints() {
		return favorPoints;
	}

	public void setFavorPoints(int points) {
		if (points + favorPoints >= 2000) {
			points = 2000;
			getPackets()
					.sendGameMessage(
							"The offering stone is full! The jadinkos won't deposite any more rewards until you have taken some.");
		}
		this.favorPoints = points;
		refreshFavorPoints();
	}

	public void refreshFavorPoints() {
		varsManager.sendVarBit(9511, favorPoints);
	}

	public boolean containsItem(int id) {
		return getInventory().containsItemToolBelt(id)
				|| getEquipment().getItems().containsOne(new Item(id))
				|| getBank().containsItem(id);
	}

	public void increaseRedStoneCount() {
		redStoneCount++;
	}

	public void resetRedStoneCount() {
		redStoneCount = 0;
	}

	public int getRedStoneCount() {
		return redStoneCount;
	}

	public void increaseCrystalStoneCount() {
		crystalStoneCount++;
	}

	public void resetCrystalStoneCount() {
		crystalStoneCount = 0;
	}

	public int getCrystalStoneCount() {
		return crystalStoneCount;
	}

	public void setCrystalStoneDelay(long delay) {
		crystalStoneDelay = Utils.currentTimeMillis() + delay;
	}

	public void setRedStoneDelay(long delay) {
		redStoneDelay = Utils.currentTimeMillis() + delay;
	}

	public long getRedStoneDelay() {
		return redStoneDelay;
	}

	public boolean isLobby() {
		return lobby;
	}

	public CoalTrucksManager getCoalTrucksManager() {
		return coalTrucksManager;
	}

	public DungManager getDungManager() {
		return dungManager;
	}

	public boolean[] getPrayerBook() {
		return prayerBook;
	}

	public void setPouchFilter(boolean pouchFilter) {
		this.pouchFilter = pouchFilter;
	}

	public boolean isPouchFilter() {
		return pouchFilter;
	}

	public boolean isCantWalk() {
		return cantWalk;
	}

	public void setCantWalk(boolean cantWalk) {
		this.cantWalk = cantWalk;
	}

	@Override
	public boolean canMove(int dir) {
		return getControllerManager().canMove(dir);
	}

	@Override
	public int[] getBonuses() {
		return combatDefinitions.getBonuses();
	}

	public long getLastArtefactTime() {
		return lastArtefactTime;
	}

	public void setLastArtefactTime(long lastArtefactTime) {
		this.lastArtefactTime = lastArtefactTime;
	}

	public long getSessionTime() {
		return Utils.currentTimeMillis() - lastGameLogin;
	}

	public boolean isMasterLogin() {
		return masterLogin;
	}

	public boolean isBeginningAccount() {
		// return !Settings.DEBUG && getTotalOnlineTime() < 3600000;
		return days == 0 && hours < 1;
	}

	// updated to rs3
	@Override
	public int getHealRestoreRate() {
		if (isResting())
			return 1;
		int c = super.getHealRestoreRate();
		if (getPrayer().isActive(Prayer.RAPID_RENEWAL) || resting == -1)
			c /= 5;
		else if (getPrayer().isActive(Prayer.RAPID_HEAL))
			c /= 2;
		if (getEquipment().getGlovesId() == 11133)
			c /= 3;
		return c;
	}

	public long getLastStarSprite() {
		return lastStarSprite;
	}

	public void setLastStarSprite(long lastStarSprite) {
		this.lastStarSprite = lastStarSprite;
	}

	public long getLastBork() {
		return lastBork;
	}

	public void setLastBork(long lastBork) {
		this.lastBork = lastBork;
	}

	boolean hasEmailRestrictions() {
		return email == null;
	}

	public Map<Integer, Integer> getILayoutVars() {
		return iLayoutVars;
	}

	public void resetILayoutVars() {
		iLayoutVars = new HashMap<Integer, Integer>(
				ILayoutDefaults.INTERFACE_LAYOUT_VARS);
	}

	public boolean isLockInterfaceCustomization() {
		return lockInterfaceCustomization;
	}

	public void switchLockInterfaceCustomization() {
		lockInterfaceCustomization = !lockInterfaceCustomization;
		refreshLockInterfaceCustomization();
	}

	public boolean isHideInterfaceTitles() {
		return hideTitleBarsWhenLocked;
	}

	public void setHideInterfaceTitles(boolean hideInterfaceTitles) {
		this.hideTitleBarsWhenLocked = hideInterfaceTitles;
	}

	public int getMovementType() {
		return (getNextRunDirection() != -1
				|| (getRun() && getWalkSteps().size() > 1) ? Cutscene.RUN_MOVE_TYPE
					: Cutscene.WALK_MOVE_TYPE) + 1;
	}

	public int[] getMousePosition() {
		return mousePosition;
	}

	public void setMousePosition(int[] mousePosition) {
		this.mousePosition = mousePosition;
	}

	public int[] getLastMousePosition() {
		return lastMousePosition;
	}

	public void setLastMousePosition(int[] mousePosition) {
		this.lastMousePosition = mousePosition;
	}

	public void increaseFirstVirtualIcon() {
		setFirstVirtualIcon(virtualIconId + 1);
		refreshFirstVirtualIcon();
	}

	public void decreaseFirstVirtualIcon() {
		setFirstVirtualIcon(virtualIconId - 1);
		refreshFirstVirtualIcon();
	}

	public void setFirstVirtualIcon(int id) {
		this.virtualIconId = id;
	}

	private void refreshFirstVirtualIcon() {
		varsManager.sendVarBit(19010, virtualIconId);
	}

	public void increaseSecondVirtualIcon() {
		setSecondVirtualIcon(virtualIconId2 + 1);
		refreshSecondVirtualIcon();
	}

	public void decreaseSecondVirtualIcon() {
		setSecondVirtualIcon(virtualIconId2 - 1);
		refreshSecondVirtualIcon();
	}

	private void setSecondVirtualIcon(int id) {
		this.virtualIconId2 = id;
	}

	public void refreshSecondVirtualIcon() {
		varsManager.sendVarBit(19011, virtualIconId2);
	}

	private void refreshGoldTrim() {
		varsManager.sendVarBit(19009, goldTrim ? 1 : 0);
	}

	public void switchGoldTrim() {
		goldTrim = !goldTrim;
		refreshGoldTrim();
	}

	private void refreshVirtualLeveling() {
		varsManager.sendVarBit(19007, virtualLeveling ? 1 : 0);
	}

	public void switchVirtualLeveling() {
		virtualLeveling = !virtualLeveling;
		refreshVirtualLeveling();
	}

	public void switchLootInterface() {
		lootInterface = !lootInterface;
		refreshLootInventory();
	}

	private void refreshLootInventory() {
		varsManager.sendVarBit(27942, lootInterface ? 1 : 0);
	}

	public boolean isInLegacyCombatMode() {
		return legacyCombatMode;
	}

	public boolean isInLegacyInterfaceMode() {
		return legacyInterfaceMode;
	}

	public void switchLegacyInterfaceMode() {
		stopAll();
		legacyInterfaceMode = !legacyInterfaceMode;
		refreshInterfaceVars();
		varsManager.sendVarBit(19928, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(19925, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(19924, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(22875, legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(27169, legacyInterfaceMode ? 1 : 0);
		getInterfaceManager().sendMagicAbilities();
		getInterfaceManager().sendMeleeAbilities();
		getInterfaceManager().sendDefenceAbilities();

	}

	public void switchLegacyCombatMode() {
		stopAll();
		legacyCombatMode = !legacyCombatMode;
		varsManager.sendVarBit(19928, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(19925, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(19924, (legacyCombatMode && legacyInterfaceMode)
				|| legacyInterfaceMode ? 1 : 0);
		varsManager.sendVarBit(27168, legacyCombatMode ? 1 : 0);
		getCombatDefinitions().setDefaultAbilityMenu();
		getCombatDefinitions().setCombatMode(
				legacyCombatMode ? CombatDefinitions.LEGACY_COMBAT_MODE
						: CombatDefinitions.MANUAL_COMBAT_MODE);
		getCombatDefinitions().refreshShowCombatModeIcon();
		getCombatDefinitions().refreshAllowAbilityQueueing();
		getInterfaceManager().sendMagicAbilities();
		getInterfaceManager().sendDefenceAbilities();
		getInterfaceManager().sendLockGameTab(8,
				legacyCombatMode ? true : false);
	}

	public void switchLegacyModeOld() {
		stopAll();
		legacyCombatMode = !legacyCombatMode;
		refreshInterfaceVars();
		getCombatDefinitions().setDefaultAbilityMenu();
		getCombatDefinitions().setCombatMode(
				legacyCombatMode ? CombatDefinitions.LEGACY_COMBAT_MODE
						: CombatDefinitions.MANUAL_COMBAT_MODE);
		getCombatDefinitions().refreshShowCombatModeIcon();
		getCombatDefinitions().refreshAllowAbilityQueueing();
	}

	public DropThresholdManager getThresholdManager() {
		return thresholdManager;
	}

	public void refreshShardConfigs() {
		for (int i = 0; i < expertSkillShards.length; i++) {
			if (!expertSkillShards[i])
				continue;
			getVarsManager().sendVarBit(varbit[i], 1);
		}
	}

	private void refreshMapIcons() {
		varsManager.sendVarBit(22874,
				legacyCombatMode || legacyInterfaceMode ? 1 : 0);
	}

	private void refreshHideTitleBarsWhenLocked() {
		varsManager.sendVarBit(19928, hideTitleBarsWhenLocked ? 1 : 0);
	}

	private void refreshLockInterfaceCustomization() {
		varsManager.sendVarBit(19925, lockInterfaceCustomization ? 1 : 0);
	}

	private void refreshSlimHeaders() {
		varsManager.sendVarBit(19924, slimHeaders ? 1 : 0);
	}

	public void switchSlimHeaders() {
		slimHeaders = !slimHeaders;
		refreshSlimHeaders();
	}

	public void switchHideTitleBarsWhenLocked() {
		hideTitleBarsWhenLocked = !hideTitleBarsWhenLocked;
		refreshHideTitleBarsWhenLocked();
	}

	public void switchTimeStamps() {
		timeStamps = !timeStamps;
		refreshTimeStamps();
	}

	private void refreshTimeStamps() {
		varsManager.sendVarBit(27452, timeStamps ? 1 : 0);
	}

	public void switchClickThroughtChatBoxes() {
		clickThroughtChatboxes = !clickThroughtChatboxes;
		refreshClickThroughtChatBoxes();
	}

	private void refreshClickThroughtChatBoxes() {
		varsManager.sendVarBit(20188, legacyCombatMode || legacyInterfaceMode
				|| clickThroughtChatboxes ? 1 : 0);
	}

	private void refreshGameframe() {
		varsManager.sendVarBit(22875, (legacyInterfaceMode)
				|| (legacyCombatMode && legacyInterfaceMode) ? 1 : 0);
	}

	private void refreshTargetReticules() {
		varsManager.sendVarBit(19929, !legacyInterfaceMode && !legacyCombatMode
				&& targetReticules ? 0 : 1);
	}

	public void switchTargetReticules() {
		targetReticules = !targetReticules;
		refreshTargetReticules();
	}

	private void refreshAlwaysShowTargetInformation() {
		varsManager.sendVarBit(19927, legacyCombatMode || legacyInterfaceMode
				|| alwaysShowTargetInformation ? 0 : 1);
	}

	public boolean isAlwaysShowTargetInformation() {
		return !legacyInterfaceMode && !legacyCombatMode
				&& alwaysShowTargetInformation;
	}

	public boolean isTargetReticule() {
		return !legacyInterfaceMode && !legacyCombatMode && targetReticules;
	}

	public void switchAlwaysShowTargetInformation() {
		alwaysShowTargetInformation = !alwaysShowTargetInformation;
		refreshAlwaysShowTargetInformation();
	}

	private void refreshSplitPrivateChat() {
		varsManager.sendVarBit(20187, splitPrivateChat ? 1 : 0);
	}

	public void switchSplitPrivateChat() {
		splitPrivateChat = !splitPrivateChat;
		refreshSplitPrivateChat();
	}

	private void refreshMakeXProgressWindow() {
		varsManager.sendVarBit(3034, makeXProgressWindow ? 0 : 1);
	}

	public void switchMakeXProgressWindow() {
		makeXProgressWindow = !makeXProgressWindow;
		getPackets().sendGameMessage(
				"You have toggled the Production Progress Dialog "
						+ (makeXProgressWindow ? "on" : "off") + ".");
		refreshMakeXProgressWindow();
	}

	private void refreshHideFamiliarOptions() {
		varsManager.sendVarBit(18564, hideFamiliarOptions ? 1 : 0);
	}

	public void switchHideFamiliarOptions() {
		hideFamiliarOptions = !hideFamiliarOptions;
		refreshHideFamiliarOptions();
	}

	private void refreshGuidanceSystemHints() {
		varsManager.sendVarBit(20924, guidanceSystemHints ? 0 : 1);
	}

	public void switchGuidanceSystemHints() {
		guidanceSystemHints = !guidanceSystemHints;
		refreshGuidanceSystemHints();
	}

	private void refreshToogleQuickChat() {
		varsManager.sendVarBit(21242, toogleQuickChat ? 0 : 1);
	}

	private void refreshLockZoom() {
		varsManager.sendVarBit(19926, lockZoom ? 1 : 0);
	}

	private void refreshCameraType() {
		varsManager.sendVarBit(19949, rs3Camera ? 0 : 1);
	}

	public void setCameraType(boolean rs3Camera) {
		this.rs3Camera = rs3Camera;
		refreshCameraType();
	}

	public void switchLockZoom() {
		lockZoom = !lockZoom;
		refreshLockZoom();
	}

	public void switchToogleQuickChat() {
		toogleQuickChat = !toogleQuickChat;
		refreshToogleQuickChat();
	}

	private void refreshCombatMode() {
		varsManager.sendVarBit(27168, legacyCombatMode ? 1 : 0);
	}

	private void refreshInterfaceMode() {
		varsManager.sendVarBit(27169, legacyInterfaceMode ? 1 : 0);
	}

	public void refreshInterfaceVars() {
		resetAlwaysChatOnMode();
		refreshMapIcons();
		refreshSlimHeaders();
		refreshLockInterfaceCustomization();
		refreshClickThroughtChatBoxes();
		refreshHideTitleBarsWhenLocked();
		refreshTargetReticules();
		refreshAlwaysShowTargetInformation();
		refreshMakeXProgressWindow();
		refreshSplitPrivateChat();
		refreshTaskCompletePopups();
		refreshTaskInformationWindow();
		refreshTooglePlayerNotification();
		refreshToogleAbilityCooldownTimer();
		refreshSkillTargetBasedXPPopup();
		refreshUTCClock();
		refreshSlayerCounter();
		refreshGameframe();
		refreshInterfaceMode();
		refreshCombatMode();
		getInterfaceManager().sendMagicAbilities();
		refreshToggleBuffTimer(true);
	}

	public void switchBuffTimer() {
		toggleBuffTimers = !toggleBuffTimers;
		refreshToggleBuffTimer(false);
	}

	boolean hasBuffTimersEnabled() {
		return !legacyCombatMode || toggleBuffTimers;
	}

	private void refreshToggleBuffTimer(boolean login) {
		getVarsManager().sendVarBit(24832, toggleBuffTimers ? 1 : 0);
		getPackets().sendExecuteScript(364, toggleBuffTimers ? 1 : 0,
				login ? 0 : 1);
	}

	public void setUTCClock(int type) {
		this.utcClock = (byte) type;
		refreshUTCClock();
	}

	public void refreshSlayerCounter() {
		slayerCounter = !slayerCounter;
		getPackets().sendHideIComponent(1639, 0, slayerCounter ? true : false);
	}

	private void refreshUTCClock() {
		varsManager.sendVarBit(26696, utcClock);
		getPackets().sendHideIComponent(635, 2, utcClock == 0);
		getPackets().sendHideIComponent(635, 7, utcClock == 2);
	}

	public byte getUTCClock() {
		return utcClock;
	}

	public void switchSkillTargetBasedXPPopup() {
		skillTargetBasedXPPopup = !skillTargetBasedXPPopup;
		refreshSkillTargetBasedXPPopup();
	}

	private void refreshSkillTargetBasedXPPopup() {
		varsManager.sendVarBit(26632, skillTargetBasedXPPopup ? 1 : 0);
	}

	public void switchToogleAbilityCooldownTimer() {
		toogleAbilityCooldownTimer = !toogleAbilityCooldownTimer;
		refreshToogleAbilityCooldownTimer();
	}

	private void refreshToogleAbilityCooldownTimer() {
		varsManager.sendVarBit(25401, toogleAbilityCooldownTimer ? 1 : 0);
	}

	public void switchTooglePlayerNotification() {
		tooglePlayerNotification = !tooglePlayerNotification;
		refreshTooglePlayerNotification();
	}

	private void refreshTooglePlayerNotification() {
		varsManager.sendVarBit(24940, tooglePlayerNotification ? 1 : 0);
	}

	public void switchPublicEffects() {
		publicChatEffects = !publicChatEffects;
		refreshPublicEffects();
	}

	private void refreshPublicEffects() {
		varsManager.sendVar(456, publicChatEffects ? 1 : 0);
	}

	public void switchTaskInformationWindow() {
		taskInformationWindow = !taskInformationWindow;
		refreshTaskInformationWindow();
	}

	private void refreshTaskInformationWindow() {
		varsManager.sendVarBit(3568, taskInformationWindow ? 0 : 1); // TODO
	}

	public void switchTaskCompletePopups() {
		taskCompletePopup = !taskCompletePopup;
		refreshTaskCompletePopups();
	}

	private void refreshTaskCompletePopups() {
		// varsManager.sendVarBit(22310, taskCompletePopup ? 1 : 0); //TODO
	}

	private void resetAlwaysChatOnMode() {
		setAlwaysChatOnMode(legacyCombatMode);
	}

	public boolean isAlwaysChatOnMode() {
		return alwaysOnChatMode;
	}

	public void setAlwaysChatOnMode(boolean alwaysOnChatMode) {
		this.alwaysOnChatMode = alwaysOnChatMode;
		refreshAlwaysChatOnMode();
	}

	public void switchAlwaysChatOnMode() {
		alwaysOnChatMode = !alwaysOnChatMode;
	}

	private void refreshAlwaysChatOnMode() {
		varsManager.sendVarBit(22310, alwaysOnChatMode ? 1 : 0); // TODO
	}

	private void refreshChatBadge() {
		// varsManager.sendVarBit(22332, getChatBadge() ? 1 : 0); // aint
		// correct
	}

	public ActionBar getActionbar() {
		return actionbar;
	}

	public int getPreviousLodestone() {
		return previousLodestone;
	}

	public void setPreviousLodestone(int previousLodestone) {
		this.previousLodestone = previousLodestone;
	}

	public void updateBuffs() {
		getVarsManager().sendVar(895, getVarsManager().getValue(895) + 1);
	}

	// for buffs that arent used exept for showing, needs to be called at login
	private void refreshBuffs() {
		getEffectsManager().refreshAllBuffs();
		refreshMeleeAttackRating();
		refreshMeleeStrengthRating();
		refreshDefenceRating();
	}

	@Override
	public String getName() {
		return getDisplayName();
	}

	@Override
	public int getCombatLevel() {
		return skills.getCombatLevel();
	}

	public boolean isRunAfterLoad() {
		return runAfterLoad;
	}

	public Animation getDeathAnimation() {
		setNextGraphics(new Graphics(Utils.random(2) == 0 ? 4399 : 4398));
		return new Animation(21769);
	}

	public int[] getSubMenus() {
		return subMenus;
	}

	public boolean hasFireImmunity() {
		return getEffectsManager().hasActiveEffect(EffectType.FIRE_IMMUNITY)
				|| getEffectsManager().hasActiveEffect(
						EffectType.SUPER_FIRE_IMMUNITY);
	}

	@Override
	public boolean isPoisonImmune() {
		return getEffectsManager().hasActiveEffect(EffectType.ANTIPOISON);
	}

	public DoomsayerManager getDoomsayerManager() {
		return doomsayerManager;
	}

	public PlayerExamineManager getPlayerExamineManager() {
		return playerExamineManager;
	}

	@Override
	public void giveXP() {
		Combat.giveXP(this, getTotalDamageReceived() / 10);
	}

	public TimersManager getTimersManager() {
		return timersManager;
	}

	public CosmeticsManager getCosmeticsManager() {
		return cosmeticsManager;
	}

	public CompletionistCapeManager getCompCapeManager() {
		return compCapeManager;
	}

	public AssistanceManager getAssistanceManager() {
		return assistanceManager;
	}

	public GamePointManager getGamePointManager() {
		return gamePointManager;
	}

	public boolean isEnteredDonatorZone() {
		return enteredDonatorZone;
	}

	public void setCurrentOptionsMenu(int interfaceId) {
		getTemporaryAttributtes().put(Key.OPTION_MENU, interfaceId);
	}

	public String getLastBossInstanceKey() {
		return lastBossInstanceKey;
	}

	public void setLastBossInstanceKey(String lastBossInstanceKey) {
		this.lastBossInstanceKey = lastBossInstanceKey;
	}

	public InstanceSettings getLastBossInstanceSettings() {
		return lastBossInstanceSettings;
	}

	public void setLastBossInstanceSettings(
			InstanceSettings lastBossInstanceSettings) {
		this.lastBossInstanceSettings = lastBossInstanceSettings;
	}

	public boolean isInstantSwitchToLegacy() {
		return instantSwitchToLegacy;
	}

	void switchInstantSwitchToLegacy() {
		instantSwitchToLegacy = !instantSwitchToLegacy;
	}

	public void setChosenFaction(boolean chosenFaction) {
	}

	public boolean[] getBoons() {
		return boons;
	}

	public boolean getBoon(int index) {
		return boons[index];
	}

	public void setBoons(boolean[] boons) {
		this.boons = boons;
	}

	public PlayerPorts getPlayerPorts() {
		return playerPorts;
	}

	public void setPlayerPorts(PlayerPorts playerPorts) {
		this.playerPorts = playerPorts;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	/**
	 * Ironman
	 */

	public boolean isIronman() {
		return ironMan;
	}

	public boolean setIronman(boolean ironMan) {
		return this.ironMan = ironMan;

	}

	public boolean isHardcoreIronman() {
		return hardcoreIronMan;
	}

	public boolean setHardcoreIronMan(boolean hardcoreIronMan) {
		return this.hardcoreIronMan = hardcoreIronMan;
	}

	public boolean isAnIronMan() {
		if (isIronman())
			return true;
		if (isHardcoreIronman())
			return true;
		return false;
	}

	public String getIronmanTitle(boolean yell) {
		if (yell) {
			if (isIronman())
				if (getAppearence().isMale())
					return "Ironman";
				else
					return "Ironwoman";
			if (isHardcoreIronman())
				if (getAppearence().isMale())
					return "Hardcore Ironman";
				else
					return "Hardcore Ironwoman";
		} else {
			if (isIronman())
				if (getAppearence().isMale())
					return "<col=5F6169>Ironman </col>";
				else
					return "<col=5F6169>Ironwoman </col>";
			if (isHardcoreIronman())
				if (getAppearence().isMale())
					return "<col=A30920>Hardcore Ironman </col>";
				else
					return "<col=A30920>Hardcore Ironwoman </col>";
		}
		return "";
	}

	public int getIronmanBadge() {
		if (isIronman())
			return 11;
		if (isHardcoreIronman())
			return 13;
		return 11;
	}

	/**
	 * Quests
	 * 
	 * @return
	 */
	private boolean hiredByFred;

	private boolean chatBadge = true;

	/** The in soul wars. */
	public boolean inSoulWars = false;

	public boolean didPassRed;

	public boolean didPassBlue;

	public int zeal;

	private SoulWars soulWars;

	public boolean neverresetskills;

	public long lastDrop;

	public long lastDivineLocation = 0;
	public int divineCount;

	public int saveattack, savestrength, savedefence, savehitpoints,
			saveprayer, savemagic, saverange, savesummoning;

	public boolean firstReaperTask = true;

	public long lastReaperTask;

	public int reaperTask = 0;

	public boolean rainbow = false;

	private int lootbeamAmount = 10000;

	public int getLootbeamAmount() {
		return lootbeamAmount;
	}

	public void setLootbeamAmount(int amount) {
		this.lootbeamAmount = amount;
	}

	public long getTotalValue() {
		return getMoneyPouch().getCoinsAmount() + getInventoryValue()
				+ getEquipmentValue() + getBankValue();
	}

	public long getInventoryValue() {
		long value = 0;
		for (Item inventory : getInventory().getItems().toArray()) {
			if (inventory == null)
				continue;
			long amount = inventory.getAmount();
			value += inventory.getDefinitions().getPrice() * amount;
		}
		return value;
	}

	public long getEquipmentValue() {
		long value = 0;
		for (Item equipment : getEquipment().getItems().toArray()) {
			if (equipment == null)
				continue;
			long amount = equipment.getAmount();
			value += equipment.getDefinitions().getPrice() * amount;
		}
		return value;
	}

	public long getBankValue() {
		long value = 0;
		for (Item bank : getBank().getContainerCopy()) {
			if (bank == null)
				continue;
			long amount = bank.getAmount();
			value += bank.getDefinitions().getPrice() * amount;
		}
		return value;
	}

	/**
	 * Gets the soul wars.
	 *
	 * @return the soul wars
	 */
	public SoulWars getSoulWars() {
		return soulWars;
	}

	public boolean getChatBadge() {
		return chatBadge;
	}

	public boolean setChatBadge(boolean b) {
		refreshChatBadge();
		return chatBadge = b;
	}

	public boolean isHiredByFred() {
		return hiredByFred;
	}

	public JobManager getJobManager() {
		return jobManager;
	}

	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	public int getTaskPoints() {
		return taskPoints;
	}

	public int getPortableLimit() {
		return portableLimit;
	}

	public void setTaskPoints(int taskPoints) {
		this.taskPoints = taskPoints;
	}

	public void setPortableLimit(int portableLimit) {
		this.portableLimit = portableLimit;
	}

	public SkillerTasks getSkillTasks() {
		return skillTasks;
	}

	public void setSkillTasks(SkillerTasks skillTasks) {
		this.skillTasks = skillTasks;
	}

	public Reaper getReaperTasks() {
		return reaperTasks;
	}

	public void setReaperTasks(Reaper reaperTasks) {
		this.reaperTasks = reaperTasks;
	}

	public int getLoyaltyPoints() {
		return Loyaltypoints;
	}

	public void setLoyaltyPoints(int Loyaltypoints) {
		this.Loyaltypoints = Loyaltypoints;
	}

	public Item getPrize() {
		return prize;
	}

	public void setPrize(Item price) {
		this.prize = price;
	}

	public int getRakeStored() {
		return rakeStored;
	}

	public int getSeedDibberStored() {
		return seedDibber;
	}

	public int getSpadeStored() {
		return spadeStored;
	}

	public int getTrowelStored() {
		return trowelStored;
	}

	public int getWateringCanStored() {
		return wateringCanStored;
	}

	public int getSecateursStored() {
		return secateursStored;
	}

	public void setRakeStored(int amount) {
		this.rakeStored = amount;
	}

	public void setSeedDibberStored(int amount) {
		this.seedDibber = amount;
	}

	public void setSpadeStored(int amount) {
		this.spadeStored = amount;
	}

	public void setTrowelStored(int amount) {
		this.trowelStored = amount;
	}

	public void setWateringCanStored(int amount) {
		this.wateringCanStored = amount;
	}

	public void setSecateursStored(int amount) {
		this.secateursStored = amount;
	}

	public int getReaperPoints() {
		return reaperPoints;
	}

	public void setReaperPoints(int reaperPoints) {
		this.reaperPoints = reaperPoints;
	}

	public int getSilverhawkFeathers() {
		return silverhawkFeathers;
	}

	public void setSilverhawkFeathers(int amount) {
		this.silverhawkFeathers = amount;
	}

	public boolean getHasDied() {
		return hasDied;
	}

	public void setHasDied(boolean hasDied) {
		this.hasDied = hasDied;
	}

	public boolean hasMoney(int amount) {
		int money = getInventory().getAmountOf(995)
				+ getMoneyPouch().getCoinsAmount();
		return money >= amount;
	}

	public boolean takeMoney(int amount) {
		if (!hasMoney(amount))
			return false;
		if (amount < 0)
			return false;
		int inPouch = getMoneyPouch().getCoinsAmount();
		int inInventory = getInventory().getAmountOf(995);
		if (inPouch >= amount) {
			getMoneyPouch().setAmount(amount, true);
			return true;
		}
		if (inInventory >= amount) {
			getInventory().deleteItem(new Item(995, amount));
			return true;
		}
		if (inPouch + inInventory >= amount) {
			amount = amount - inPouch;
			getMoneyPouch().setAmount(inPouch, true);
			getInventory().deleteItem(new Item(995, amount));
			return true;
		}
		return false;
	}

	public int getRestAnimation() {
		return restAnimation;
	}

	public void setRestAnimation(int restAnimation) {
		this.restAnimation = restAnimation;
	}

	public boolean getDivineLight() {
		return divineLight;
	}

	public void setDivineLight(boolean divineLight) {
		this.divineLight = divineLight;
	}

	public boolean getDivineCoin() {
		return divineCoin;
	}

	public void setDivineCoin(boolean divineCoin) {
		this.divineCoin = divineCoin;
	}

	public boolean inMemberZone() {
		if ((getX() >= 2312 && getX() <= 2356 && getY() >= 3663 && getY() <= 3703))
			return true;
		else
			return false;
	}

	public int rosTrips;

	public long lastImpling;

	public boolean xpLampPrompt = true;

	public WorldTile getTile() {
		return new WorldTile(getX(), getY(), getPlane());
	}

	public int getTrollsToKill() {
		return trollsToKill;
	}

	public int setTrollsKilled(int trollsKilled) {
		return (this.trollsKilled = trollsKilled);
	}

	public int setTrollsToKill(int toKill) {
		return (this.trollsToKill = toKill);
	}

	public void addTrollKill() {
		trollsKilled++;
	}

	public int getTrollsKilled() {
		return trollsKilled;
	}

	public int getGodMode() {
		return godMode;
	}

	public void setGodMode(int i) {
		this.godMode = i;
	}

	public int getSecureCode() {
		return secureCode;
	}

	public void setSecureCode(int values) {
		this.secureCode = values;
	}

	public List<Player> getROSPartyMembers() {
		return partyMembers;
	}

	public void clearROSPartyMembers() {
		partyMembers = new ArrayList<Player>();
	}

	public void addROSPartyMember(Player player) {
		if (partyMembers.contains(player))
			return;
		partyMembers.add(player);
	}

	public void removeROSPartyMember(Player player) {
		partyMembers.remove(player);
	}

	public FairyRings getFairyRings() {
		return fairyRings;
	}

	public DDToken getDDToken() {
		return ddToken;
	}

	public HashMap<Integer, Integer> getVarbits() {
		return varbits;
	}

	public HashMap<Integer, Integer> getVars() {
		return vars;
	}

	public HashMap<String, Integer> getSlayerCreatures() {
		return slayerCreatures;
	}

	public void loadVarbits() {
		varbits.forEach((varbitId, value) -> getVarsManager().sendVarBit(
				varbitId, value));
	}

	public void loadVars() {
		vars.forEach((varbitId, value) -> getVarsManager().sendVar(varbitId,
				value));
	}

	public int getGameSettingsCategory() {
		return gameSettingsCategory;
	}

	public void setGameSettingsCategory(int gameSettingsCategory) {
		this.gameSettingsCategory = gameSettingsCategory;
	}

	public int getRenderDistance() {
		return largeSceneView ? 64 : 14;
	}

	public void setLoginCount(int amount) {
		loginCount = amount;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public boolean isInBossRoom() {
		return (withinDistance(new WorldTile(2870, 5362, 0), 10)
				|| withinDistance(new WorldTile(2926, 5324, 0), 10)
				|| withinDistance(new WorldTile(2923, 5249, 0), 10)
				|| withinDistance(new WorldTile(2829, 5302, 0), 10)
				|| withinDistance(new WorldTile(2925, 5203, 0), 16)
				|| withinDistance(new WorldTile(2986, 4384, 2), 17)
				|| withinDistance(new WorldTile(2926, 5324, 0), 10)
				|| withinDistance(new WorldTile(94, 97, 0), 25)
				|| withinDistance(new WorldTile(2914, 4448, 0), 20) || withinDistance(
					new WorldTile(3873, 6822, 0), 15));
	}

	public void toggleBankDialogue() {
		this.bankDialogue = !this.bankDialogue;
	}

	public boolean usingBankDialogue() {
		return this.bankDialogue;
	}

	public boolean isSupport() {
		return support || rights > 0;
	}

	public void setSupport(boolean support) {
		this.support = support;
	}

	public boolean hasGroupAssignments() {
		return groupAssignments;
	}

	public void toggleGroupAssignments() {
		if (!groupAssignments)
			varbits.put(22904, 0);
		else
			varbits.put(22904, 1);
		groupAssignments = !groupAssignments;
		loadVarbits();
	}

	public boolean hasLargerTasks() {
		return largerTasks;
	}

	public void toggleLargerTasks() {
		if (largerTasks)
			varbits.put(24833, 0);
		else
			varbits.put(24833, 1);
		largerTasks = !largerTasks;
		loadVarbits();
	}

	public int getRerollCount() {
		return rerollCount;
	}

	public void increaseRerollCount() {
		rerollCount++;
	}

	public void resetRerollCount() {
		rerollCount = 0;
	}

	public void addSpectator(Player player) {
		spectators.add(player);
	}

	public void removeSpectator(Player player) {
		spectators.remove(player);
	}

	public List<Player> getSpectators() {
		return spectators;
	}
}