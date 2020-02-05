package net.nocturne.game.player.content;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.controllers.DungeonController;

/**
 * @author Miles Black (bobismyname)
 */

public class SkillingTeleports {

	private static SkillData data;
	private static List<String> currentDescription;
	private static List<WorldTile> currentTiles;

	private enum SkillData {

		COMBAT(
				"Combat",
				1,
				142,
				new String[] { "Ghouls", "Cows", "Rock Crabs", "Yaks",
						"Gnome Khazard Battlefield", "Bandit Camp",
						"Experiments", "Ardougne Training Camp",
						"Waterfall Dungeon", "Taverly Dungeon",
						"Security Stronghold", "Automations", "Burthorpe Mine",
						"The Rift" },
				new WorldTile[] { new WorldTile(3500, 3540, 0),
						new WorldTile(3256, 3280, 0),
						new WorldTile(2709, 3718, 0),
						new WorldTile(2325, 3795, 0),
						new WorldTile(2528, 3202, 0),
						new WorldTile(3170, 2981, 0),
						new WorldTile(3576, 9927, 0),
						new WorldTile(2517, 3356, 0),
						new WorldTile(2574, 9864, 0),
						new WorldTile(2886, 9797, 0),
						new WorldTile(3080, 3422, 0),
						new WorldTile(1848, 5985, 0),
						new WorldTile(2878, 3570, 0),
						new WorldTile(3307, 3453, 0) },
				"An important set of skills used to fight monsters or players. For the complete list of WorldTiles, use the Monster button of the Crystal Teleport.",
				"Combat is often used to make money by fighting bosses or strong monsters and selling the loot to stores or players."),

		PRAYER(
				"Prayer",
				Skills.PRAYER,
				141,
				new String[] { "Ectofuntus", "Ancient Curses Altar",
						"Lumbridge Church", "Frost Dragons", "Jiggig",
						"Hill Giants", "Edgeville Monastery",
						"Mort'ton Shade Pyre", "Mort myre swamp",
						"Salve River Temple" },
				new WorldTile[] { new WorldTile(3661, 3517, 0),
						new WorldTile(3182, 5710, 0),
						new WorldTile(3243, 3208, 0),
						new WorldTile(1312, 4529, 0),
						new WorldTile(2463, 3052, 0),
						new WorldTile(3118, 9851, 0),
						new WorldTile(3051, 3490, 0),
						new WorldTile(3499, 3289, 0),
						new WorldTile(3444, 3458, 0),
						new WorldTile(3422, 9963, 0) },
				"A combat skill used to activate prayers that help you in combat and other events.",
				"As many of you may know, this skill does not make money. However you can sell bones for lots of money."),

		COOKING(
				"Cooking",
				Skills.COOKING,
				159,
				new String[] { "Rogue's Den", "Cooking Guild",
						"Lumbridge kitchen", "Catherby Range",
						"Edgeville Bank", "Oo'glog Bank", "Burthorpe Bank",
						"Al Kharid Bank", "Gnome Stronghold", "Seer's Village" },
				new WorldTile[] { new WorldTile(3030, 4958, 0),
						new WorldTile(3143, 3444, 0),
						new WorldTile(3209, 3213, 0),
						new WorldTile(2816, 3442, 0),
						new WorldTile(3079, 3494, 0),
						new WorldTile(2557, 2846, 0),
						new WorldTile(2893, 3529, 0),
						new WorldTile(3271, 3167, 0),
						new WorldTile(2449, 3509, 1),
						new WorldTile(2715, 3479, 0) },
				"This skill is used to cook and bake certain items such as fish or cakes.",
				"Cooking things can be profitable, depending on what you cook. Check prices to see which foods are more profitable."),

		WOODCUTTING(
				"Woodcutting",
				Skills.WOODCUTTING,
				144,
				new String[] { "Seer's Village Maples", "Draynor Willows",
						"Tai Bwo Wannai Trees", "Mobilizing Armies Trees",
						"Eagle's Peak", "South Falador Wall", "Varrock Palace",
						"Sorcerer's Tower", "Seer's Village Yews",
						"Gnome Stronghold" },
				new WorldTile[] { new WorldTile(2725, 3490, 0),
						new WorldTile(3086, 3238, 0),
						new WorldTile(2817, 3083, 0),
						new WorldTile(2443, 2855, 0),
						new WorldTile(2336, 3516, 0),
						new WorldTile(3045, 3327, 0),
						new WorldTile(3215, 3500, 0),
						new WorldTile(2702, 3398, 0),
						new WorldTile(2711, 3462, 0),// 8
						new WorldTile(2440, 3431, 0) },// 9
				"This skill is used to chop down trees in order to obtain logs.",
				"Logs are often sold to the stores for money, however you may also want to turn them into planks for maximum profits."),

		FLETCHING(
				"Fletching",
				Skills.FLETCHING,
				146,
				new String[] { "Soul Wars", "Burthorpe", "Castle Wars",
						"Duel Arena", "Grand Exchange", "Edgeville Bank",
						"Seer's Bank", "Yanille Bank", "Catherby Bank",
						"Varrock East" },
				new WorldTile[] { new WorldTile(1893, 3178, 0),
						new WorldTile(2891, 3530, 0),
						new WorldTile(2443, 3083, 0),
						new WorldTile(3382, 3268, 0),
						new WorldTile(3177, 3479, 0),
						new WorldTile(3093, 3493, 0),
						new WorldTile(2726, 3491, 0),
						new WorldTile(2611, 3093, 0),
						new WorldTile(2796, 3441, 0),
						new WorldTile(3186, 3439, 0) },
				"This skill is used to create amunition and ranged weapons.",
				"Making specific things such as bolts, arrow shafts, etc. can wield in profit. Double check the prices before you begin!"),

		FISHING(
				"Fishing",
				Skills.FISHING,
				153,
				new String[] { "Lumbridge", "Draynor", "Catherby",
						"Barbarian Village", "Shilo Village",
						"Karamja Karambwan", "Otto's Grotto", "Fishing Guild",
						"Living Rock Cavern", "Arc Fishing Spot",
						"Fishing Platform" },
				new WorldTile[] { new WorldTile(3255, 3203, 0),
						new WorldTile(3089, 3230, 0),
						new WorldTile(2850, 3431, 0),
						new WorldTile(3102, 3428, 0),
						new WorldTile(2858, 2971, 0),
						new WorldTile(2904, 3115, 0),
						new WorldTile(2498, 3509, 0),
						new WorldTile(2594, 3420, 0),
						new WorldTile(3634, 5140, 0),
						new WorldTile(1745, 11943, 0),
						new WorldTile(2786, 3276, 0) },
				"This skill is used to catch fish in any body of water.",
				"Usually favored, fishing is a great source of money. Fish lobsters, sharks, monkfish, and rocktails for money."),

		FIREMAKING(
				"Firemaking",
				Skills.FIREMAKING,
				149,
				new String[] { "Duel Arena", "Oo'glog", "Grand Exchange",
						"Edgeville Bank", "North Ardougne", "West Varrock",
						"Fist Of Guthix", "East Varrock", "East Falador",
						"Catherby" },
				new WorldTile[] { new WorldTile(3383, 3266, 0),
						new WorldTile(2556, 2844, 0),
						new WorldTile(3148, 3483, 0),
						new WorldTile(3093, 3497, 0),
						new WorldTile(2616, 3337, 0),
						new WorldTile(3186, 3431, 0),
						new WorldTile(1706, 5599, 0),
						new WorldTile(3253, 3428, 0),
						new WorldTile(3012, 3360, 0),
						new WorldTile(2809, 3439, 0) },
				"This skill is used to burn logs, light beacons, and more.",
				"Although you lose money burning logs, fire spirits may offer you rewards that could be valuable in time."),

		CRAFTING(
				"Crafting",
				Skills.CRAFTING,
				154,
				new String[] { "Catherby Flax", "Lumbridge Wheel",
						"Yanille Sandpit", "Robust Glass Machine",
						"Rellekka Pottery", "Barbarian Pottery", "Soul Wars",
						"Castle Wars", "Grand Exchange", "Burthorpe" },
				new WorldTile[] { new WorldTile(2743, 3444, 0),
						new WorldTile(3209, 3213, 1),
						new WorldTile(2541, 3103, 0),
						new WorldTile(2585, 2852, 0),
						new WorldTile(2618, 3663, 0),
						new WorldTile(3085, 3410, 0),
						new WorldTile(1893, 3179, 0),
						new WorldTile(2444, 3083, 0),
						new WorldTile(3177, 3502, 0),
						new WorldTile(2892, 3530, 0), },
				"This skill is used to craft many items, useful in many scenarios.",
				"There are many money making methods in crafting such as flax picking/spinning, hide tanning, etc."),

		SMITHING(
				"Smithing",
				Skills.SMITHING,
				148,
				new String[] { "Artisan Workshop", "Lumbridge Smithery",
						"Varrock Anvils", "Yanille Anvils",
						"Al Kharid Furnace", "Edgeville Furnace",
						"Falador Furnace", "Doric's House", "Seer's Anvils",
						"Ardougne Furnace" },
				new WorldTile[] { new WorldTile(3040, 3340, 0),
						new WorldTile(3226, 3254, 0),
						new WorldTile(3187, 3426, 0),
						new WorldTile(2612, 3082, 0),
						new WorldTile(3275, 3190, 0),
						new WorldTile(3107, 3500, 0),
						new WorldTile(2974, 3369, 0),
						new WorldTile(2959, 3440, 0),
						new WorldTile(2711, 3493, 0),
						new WorldTile(2604, 3310, 0) },
				"This skill is used to smelt ores and create armor and weapons.",
				"Various smithing creations wield profit, check prices, however cannonball making is where the major money is."),

		MINING(
				"Mining",
				Skills.MINING,
				143,
				new String[] { "Al Kharid Mine", "Falador Mine",
						"Karamja Gold", "Lava Flow Mine", "Red Sandstone",
						"Shilo Gem Mine", "Keldagrim North Mine",
						"Heroes Guild", "Granite Quarry", "Rune Essence",
						"Swamp Mining", "Burthorpe Mine" },
				new WorldTile[] { new WorldTile(3300, 3309, 0),
						new WorldTile(3055, 9778, 0),
						new WorldTile(2733, 3225, 0),
						new WorldTile(2178, 5664, 0),
						new WorldTile(2587, 2878, 0),
						new WorldTile(2825, 2999, 0),
						new WorldTile(2873, 10253, 0),
						new WorldTile(2921, 9913, 0),
						new WorldTile(3174, 2922, 0),
						new WorldTile(2910, 4832, 0),
						new WorldTile(3231, 3150, 0),
						new WorldTile(2878, 3502, 0) },
				"This skill is used to mine ores, gems, and rocks found around the server.",
				"Mining gems is great money for beginners. If you know your WorldTiles, mine runite ore, otherwise go with coal/iron."),

		HERBLORE(
				"Herblore",
				Skills.HERBLORE,
				150,
				new String[] { "Taverly", "Gu Tunoth", "Grand Exchange",
						"Edgeville Bank", "East Varrock", "Nardah",
						"West Falador", "Sorceress's Garden", "Castle Wars",
						"Soul Wars" },
				new WorldTile[] { new WorldTile(2910, 3498, 0),
						new WorldTile(2528, 3045, 0),
						new WorldTile(3151, 3502, 0),
						new WorldTile(3094, 3491, 0),
						new WorldTile(3253, 3420, 0),
						new WorldTile(3437, 2888, 0),
						new WorldTile(2946, 3368, 0),
						new WorldTile(2912, 5474, 0),
						new WorldTile(2444, 3083, 0),
						new WorldTile(1893, 3179, 0) },
				"This skill is used to clean herbs, create potions, and lot's more!",
				"Cleaning herbs can result in profit over time, however some potions can be profitable to make in masses."),

		AGILITY(
				"Agility",
				Skills.AGILITY,
				145,
				new String[] { "Gnome Stronghold", "Burthorpe",
						"Barbarian Outpost", "Brimhaven Arena",
						"Wilderness Course", "Werewolf Course/Skullball",
						"Agility Pyramid", "Bandos Throne Room",
						"Penguin Course", "Ape Atoll Course" },
				new WorldTile[] { new WorldTile(2473, 3438, 0),
						new WorldTile(2919, 3551, 0),
						new WorldTile(2552, 3556, 0),
						new WorldTile(2809, 3192, 0),
						new WorldTile(2998, 3914, 0),
						new WorldTile(3549, 9866, 0),
						new WorldTile(3352, 2827, 0),
						new WorldTile(2338, 4242, 0),
						new WorldTile(2638, 4053, 0),
						new WorldTile(2756, 2742, 0) },
				"This skill is used to access short cuts and certain areas throughout the world.",
				"Although training agility is not profitable, you can use shortcuts that can speed up skilling/pvm money making."),

		THIEVING(
				"Thieving",
				Skills.THIEVING,
				147,
				new String[] { "Edgeville", "Lumbridge", "H.A.M Base",
						"Draynor Market", "Ardougne Square", "Dorgesh Kaan",
						"The Grand Tree", "Dwarf Traders", "Lletya",
						"Rogue's Den" },
				new WorldTile[] { new WorldTile(3097, 3509, 0),
						new WorldTile(3224, 3242, 0),
						new WorldTile(3149, 9652, 0),
						new WorldTile(3081, 3250, 0),
						new WorldTile(2661, 3307, 0),
						new WorldTile(2744, 5352, 2),
						new WorldTile(2474, 3495, 1),
						new WorldTile(2878, 10198, 1),
						new WorldTile(2340, 3171, 0),
						new WorldTile(3042, 4964, 0) },
				"This skill can be used to pickpocket, lock pick, loot, and all those horrible acts.",
				"Everything wields money however the best money is looting chests and stalls. Also, pickpocketing master farmers."),

		SLAYER(
				"Slayer",
				Skills.SLAYER,
				151,
				new String[] { "Slayer Masters", "Lumbridge swamp",
						"Slayer Tower", "Fremennik Slayer Dungeon",
						"Smoke Dungeon", "Kuradal's Slayer Dungeon",
						"Smokey well", "Jadinko Lair", "Glacor Cavern",
						"Polypore Dungeon", "Monastery of Ascension",/*
																	 * "Mount Firewake"
																	 * ,
																	 */
						"Burthorpe Troll Mine" },
				new WorldTile[] { new WorldTile(1811, 5985, 0),
						new WorldTile(3168, 9572, 0),
						new WorldTile(3423, 3544, 0),
						new WorldTile(2806, 10004, 0),
						new WorldTile(3307, 2963, 0),
						new WorldTile(1661, 5257, 0),
						new WorldTile(3311, 2961, 0),
						new WorldTile(3035, 9234, 0),
						new WorldTile(4182, 5729, 0),
						new WorldTile(4624, 5459, 0),
						new WorldTile(2501, 2886, 0),
				/* new WorldTile(4767, 6077, 1) */},
				"This skill can be used to fight and successfully kill certain monsters.",
				"Killing monsters such as abyssal demons, dark beasts, ganodermic beasts, jadinkos, etc. have a large pay roll."),

		FARMING(
				"Farming",
				Skills.FARMING,
				155,
				new String[] { "Falador Garden", "Catherby", "Port Phasmatys",
						"South Falador", "North Ardougne",
						"Ardougne Monastery", "Gnome Stronghold", "Lletya",
						"Lumbridge", "Yanille Hops" },
				new WorldTile[] { new WorldTile(3002, 3375, 0),
						new WorldTile(2795, 3464, 0),
						new WorldTile(3603, 3528, 0),
						new WorldTile(3057, 3309, 0),
						new WorldTile(2669, 3376, 0),
						new WorldTile(2616, 3228, 0),
						new WorldTile(2437, 3417, 0),
						new WorldTile(2345, 3164, 0),
						new WorldTile(3229, 3248, 0),
						new WorldTile(2575, 3102, 0) },
				"This skill is used to plant and harvest all sorts of vegetation.",
				"Almost every seed makes value when you harvest the crop, search up prices on google to find which is the most profitable."),

		RUNECRAFTING(
				"Runecrafting",
				Skills.RUNECRAFTING,
				160,
				new String[] { "Abyss", "Astral Altar", "Air Altar",
						"Water Altar", "Earth Altar", "Fire Altar",
						"Nature Altar", "Death Altar", "Blood Altar",
						"Ourania Altar", "Runespan" },
				new WorldTile[] { new WorldTile(3039, 4834, 0),
						new WorldTile(2156, 3864, 0),
						new WorldTile(3127, 3409, 0),
						new WorldTile(3186, 3163, 0),
						new WorldTile(3304, 3476, 0),
						new WorldTile(3315, 3257, 0),
						new WorldTile(2867, 3021, 0),
						new WorldTile(1863, 4637, 0),
						new WorldTile(2467, 4888, 1),
						new WorldTile(3271, 4856, 0),
						new WorldTile(3101, 3154, 3) },
				"This skill is used to create runes out of both rune and pure essence.",
				"The most profitable runes to craft are natures, laws, and astrals. For lower levels, craft cosmics."),

		CONSTRUCTION(
				"Construction",
				Skills.CONSTRUCTION,
				152,
				new String[] { "Sawmill", "Varrock Estate Agent", "Stonemason",
						"Garden Supplier", "Taxidermist", "Ardougne Servants",
						"Rimmington", "Yanille", "Taverly", "Rellekka" },
				new WorldTile[] { new WorldTile(3302, 3490, 0),
						new WorldTile(3240, 3474, 0),
						new WorldTile(2849, 10185, 0),
						new WorldTile(3018, 3373, 0),
						new WorldTile(3479, 3483, 0),
						new WorldTile(2662, 3330, 0),
						new WorldTile(2955, 3224, 0),
						new WorldTile(2544, 3094, 0),
						new WorldTile(2885, 3453, 0),
						new WorldTile(2670, 3632, 0) },
				"This skill is used to build furniture and construct your own house.",
				"Construction is a major money loss, however you can use furniture to gain, such as lecterns to make teletabs."),

		HUNTER(
				"Hunter",
				Skills.HUNTER,
				156,
				new String[] { "Red Chin Jungle", "Piscatoris Hunter Area",
						"Grenwalls", "Puro Puro", "Relleka Hunter Area",
						"Karamja Graahks", "Swamp Lizards", "Ourania Altar",
						"Jadinkos", "Oo'glog" },
				new WorldTile[] { new WorldTile(2525, 2915, 0),
						new WorldTile(2333, 3583, 0),
						new WorldTile(2267, 3241, 0),
						new WorldTile(2592, 4317, 0),
						new WorldTile(2729, 3774, 0),
						new WorldTile(2774, 3001, 0),
						new WorldTile(3539, 3449, 0),
						new WorldTile(2447, 3222, 0),
						new WorldTile(2963, 2921, 0),
						new WorldTile(2506, 2825, 0) },
				"This skill is used to trap and catch many creatures in different environments.",
				"What you catch can be profitable. Looting high level imps are major, along with catching chinchompas and grenwalls."),

		SUMMONING(
				"Summoning",
				Skills.SUMMONING,
				157,
				new String[] { "Taverly", "Waterfiends", "Chaos Tunnels",
						"Khazard Battlefield", "Glacors", "Gu Tonoth",
						"Lunar Isle", "Piscatoris Obelisk", "Desert Obelisk",
						"Tav Slay Dungeon" },
				new WorldTile[] { new WorldTile(2930, 3448, 0),
						new WorldTile(2512, 3511, 0),
						new WorldTile(3145, 3520, 0),
						new WorldTile(2534, 3255, 0),
						new WorldTile(4180, 5731, 0),
						new WorldTile(2523, 3056, 0),
						new WorldTile(2111, 3916, 0),
						new WorldTile(2328, 3647, 0),
						new WorldTile(3304, 2899, 0),
						new WorldTile(2212, 4531, 0), },
				"This skill is used to create and summon monsters that aid you in many ways.",
				"Some pouches can make profit, however turning pouches into scrolls can make more money, alot more."),

		DUNGEONEERING(
				"Dungeoneering",
				Skills.DUNGEONEERING,
				158,
				new String[] { "Daemonheim", "Edgeville Resource",
						"Taverly Hellhound Resource",
						"Taverly Dragon Resource", "Waterfall Resource",
						"Karamja Resource", "Mining Guild Resource",
						"Brimhaven Resource", "Polypore Resource",
						"Frost Dragons" },
				new WorldTile[] { new WorldTile(3450, 3718, 0),
						new WorldTile(3131, 9933, 0),
						new WorldTile(2855, 9841, 0),
						new WorldTile(2912, 9809, 0),
						new WorldTile(2578, 9896, 0),
						new WorldTile(2844, 9557, 0),
						new WorldTile(3022, 9740, 0),
						new WorldTile(2698, 9442, 0),
						new WorldTile(4660, 5491, 0),
						new WorldTile(3033, 9597, 0) },
				"This skill is used to explore and raid dungeons to seek out treasures.",
				"You can access resource dungeons which bring lots of product and sell valuable dungeoneering rewards."),

		DIVINATION(
				"Divination",
				Skills.DIVINATION,
				159,
				new String[] { "Pale Wisps", "Flickering Wisps",
						"Bright Wisps", "Glowing Wisps", "Sparkling Wisps",
						"Gleaming Wisps", "Vibrant Wisps", "Lustrous Wisps",
						"Elder Wisps", "Brilliant", "Radiant",
						"Luminous Wisps", "Incadescent Wisps" },
				new WorldTile[] { new WorldTile(3121, 3219, 0),
						new WorldTile(3002, 3406, 0),
						new WorldTile(3305, 3400, 0),
						new WorldTile(2737, 3415, 0),
						new WorldTile(2765, 3598, 0),
						new WorldTile(2887, 3040, 0),
						new WorldTile(2415, 2863, 0),
						new WorldTile(3465, 3530, 0),
						new WorldTile(4267, 6318, 0),
						new WorldTile(3393, 3302, 0),
						new WorldTile(3814, 3556, 0),
						new WorldTile(3309, 2655, 0),
						new WorldTile(2294, 3059, 0), },
				"This skill allows you to become one with the gods of this forsaken world.",
				"Harvest divine energy to bend the will of the gods to your bidding.");
		;

		private String skill;
		private int id;
		private int shop;
		private String[] tileStrings;
		private WorldTile[] tiles;
		private String description;
		private String methods;

		SkillData(String skill, int id, int shop, String[] WorldTiles,
				WorldTile[] tiles, String description, String methods) {
			this.skill = skill;
			this.id = id;
			this.shop = shop;
			this.tileStrings = WorldTiles;
			this.tiles = tiles;
			this.description = description;
			this.methods = methods;
		}

		public int getId() {
			return id;
		}

		public String[] getTileStrings() {
			return tileStrings;
		}

		public WorldTile[] getWorldTiles() {
			return tiles;
		}

		public static SkillData getData(int id) {
			if (id >= 0 && id <= 6) {
				return SkillData.COMBAT;
			}
			for (SkillData skill : SkillData.values()) {
				if (skill.getId() == id) {
					return skill;
				}
			}
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public static void openInterface(Player player, int skillId) {
		if (player.getControllerManager().getController() != null
				&& player.getControllerManager().getController() instanceof DungeonController) {
			player.getPackets().sendGameMessage(
					"You can't use this feature in here.");
			return;
		}
		player.closeInterfaces();
		player.getTemporaryAttributtes().put("NPCTeleports", false);
		try {
			player.getTemporaryAttributtes().put("SkillData",
					SkillData.getData(skillId));
		} catch (NullPointerException e) {
			player.getPackets().sendGameMessage(
					"This skill has not been added yet.");
			return;
		}
		;
		player.getTemporaryAttributtes().put(
				"SkillTiles",
				new LinkedList<WorldTile>(Arrays.asList(((SkillData) player
						.getTemporaryAttributtes().get("SkillData"))
						.getWorldTiles())));
		player.getTemporaryAttributtes().put(
				"SkillDescription",
				new LinkedList<String>(Arrays.asList(((SkillData) player
						.getTemporaryAttributtes().get("SkillData"))
						.getTileStrings())));
		for (int i = ((LinkedList<WorldTile>) player.getTemporaryAttributtes()
				.get("SkillTiles")).size(); i <= 20; i++)
			((LinkedList<WorldTile>) player.getTemporaryAttributtes().get(
					"SkillTiles")).add(null);
		for (int i = ((LinkedList<String>) player.getTemporaryAttributtes()
				.get("SkillDescription")).size(); i <= 20; i++)
			((LinkedList<String>) player.getTemporaryAttributtes().get(
					"SkillDescription")).add("Empty");
		player.getPackets().sendHideIComponent(216, 26, true);
		player.getPackets().sendIComponentText(
				216,
				26,
				((SkillData) player.getTemporaryAttributtes().get("SkillData"))
						.toString().charAt(0)
						+ ((SkillData) player.getTemporaryAttributtes().get(
								"SkillData")).toString().toLowerCase()
								.substring(1) + " Teleports");
		int index = 0;
		for (int i = 38; i <= 190; i += 8) {
			String text = ((LinkedList<String>) player
					.getTemporaryAttributtes().get("SkillDescription"))
					.get(index++);
			player.getPackets().sendIComponentText(216, i, text);
		}
		player.getInterfaceManager().sendCentralInterface(216);
	}

	@SuppressWarnings("unchecked")
	public static void handleButtons(Player player, int componentId) {
		int index = (componentId - 35) / 8;
		if (((LinkedList<WorldTile>) player.getTemporaryAttributtes().get(
				"SkillTiles")).get(index) == null)
			player.getPackets().sendGameMessage(
					"The following slot has no teleport location.");
		else
			Magic.sendNormalTeleportSpell(player, 0, 0,
					((LinkedList<WorldTile>) player.getTemporaryAttributtes()
							.get("SkillTiles")).get(index));
	}

}
