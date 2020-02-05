package net.nocturne.game.player.actions.skills.hunter;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.Animation;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.actions.skills.hunter.TrapAction.Traps;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class FlyingEntityHunter {

	private static final Animation CAPTURE_ANIMATION = new Animation(6606);
	private static final Item[] CHARMS = {
			new Item(ItemIdentifiers.GOLD_CHARM, 1),
			new Item(ItemIdentifiers.GREEN_CHARM, 1),
			new Item(ItemIdentifiers.CRIMSON_CHARM, 1),
			new Item(ItemIdentifiers.BLUE_CHARM, 1) };

	public enum FlyingEntities {

		BABY_IMPLING(1028, 11238, 20, 25, 17, new Item[] { new Item(946, 1),
				new Item(1755, 1), new Item(1734, 1), new Item(1733, 1),
				new Item(2347, 1), new Item(1985, 1) }, new Item[] {
				new Item(1927, 1), new Item(319, 1), new Item(2007, 1),
				new Item(1779, 1), new Item(7170, 1), new Item(401, 1),
				new Item(1438, 1) },
				new Item[] { new Item(2355), new Item(1607), new Item(1743),
						new Item(379), new Item(1761) }, null),

		YOUNG_IMPLING(1029, 11240, 48, 65, 22, new Item[] { new Item(361, 1),
				new Item(1901, 1), new Item(1539, 5), new Item(1784, 4),
				new Item(1523, 1), new Item(7936, 1), new Item(5970, 1) },
				new Item[] { new Item(855, 1), new Item(1353, 1),
						new Item(2293, 1), new Item(7178, 1), new Item(247, 1),
						new Item(453, 1), new Item(1777, 1), new Item(231, 1),
						new Item(2347, 1) }, new Item[] { new Item(1097, 1),
						new Item(1157, 1), new Item(8778, 1), new Item(133, 1),
						new Item(2359, 1), }, null),

		GOURMET_IMPLING(1030, 11242, 82, 113, 28, new Item[] {
				new Item(361, 1), new Item(365, 1), new Item(1897, 1),
				new Item(2007, 1), new Item(2011, 1), new Item(2293, 1),
				new Item(2327, 1), new Item(5970, 1) }, new Item[] {
				new Item(247, 1), new Item(379, 1), new Item(385, 1),
				new Item(1883, 1), new Item(1885, 1), new Item(5755, 1),
				new Item(6969, 1), new Item(7170, 1), new Item(7178, 1),
				new Item(7188, 1), new Item(7754, 1), new Item(8244, 1),
				new Item(8526, 1) }, new Item[] { new Item(373, 1),
				new Item(5406, 1), new Item(5755, 1), new Item(7170, 1),
				new Item(7178, 1), new Item(7178, 1), new Item(7188, 1),
				new Item(7754, 1), new Item(8244, 1), new Item(8526, 1),
				new Item(10136, 1), new Item(14831, 1), new Item(10137, 5) },
				null),

		EARTH_IMPLING(1031, 11244, 126, 177, 36, new Item[] { new Item(444, 1),
				new Item(557, 32), new Item(1441, 6), new Item(1442, 1),
				new Item(2353, 1), new Item(5104, 1), new Item(5535, 1),
				new Item(6032, 1), new Item(20904, 1), new Item(20914, 1),
				new Item(20942, 1), new Item(24216, 1), new Item(24767, 1) },
				new Item[] { new Item(237, 1), new Item(447, 1),
						new Item(1273, 1), new Item(454, 6), new Item(1487, 1),
						new Item(5311, 2), new Item(5294, 2) },
				new Item[] { new Item(448, 1), new Item(1606, 2),
						new Item(6035, 2), }, new Item[] { new Item(1603, 1),
						new Item(5303, 1) }),

		ESSENCE_IMPLING(
				1032,
				11246,
				160,
				225,
				42,
				new Item[] { new Item(562, 4), new Item(554, 50),
						new Item(555, 30), new Item(556, 60),
						new Item(559, 30), new Item(1448, 1),
						new Item(7937, 20) },
				new Item[] { new Item(564, 4), new Item(4694, 4),
						new Item(4696, 4), new Item(4698, 4), },
				new Item[] { new Item(560, 13), new Item(561, 13),
						new Item(563, 13), new Item(566, 13), new Item(1442, 1) },
				null),

		ECLECTIC_IMPLING(1033, 11248, 205, 289, 50, new Item[] {
				new Item(1273, 1), new Item(5970, 1), new Item(231, 1),
				new Item(556, 41), new Item(8779, 4), new Item(12111, 1) },
				new Item[] { new Item(2358, 5), new Item(444, 1),
						new Item(4527, 1), new Item(237, 1),
						new Item(7937, 25), new Item(1199, 1),
						new Item(2349, 1), new Item(2351, 1),
						new Item(2353, 1), new Item(1199, 1) }, new Item[] {
						new Item(2493, 1), new Item(10083, 1),
						new Item(1213, 1), new Item(450, 10),
						new Item(7208, 1), new Item(5760, 2),
						new Item(5321, 3), }, new Item[] { new Item(1391, 1),
						new Item(1601, 1) }),

		SPIRIT_IMPLING(7866, 15513, 227, 321, 54, new Item[] {
				new Item(12155, 25), new Item(12109, 1), new Item(12113, 1),
				new Item(12121, 1), new Item(12115, 1), new Item(12117, 1),
				new Item(12111, 1), new Item(2350, 6), new Item(2351, 5),
				new Item(2354, 2), new Item(2359, 1), new Item(2361, 1),
				new Item(2363, 1), new Item(1115, 1), new Item(1120, 2),
				new Item(1636, 10), new Item(2135, 25), new Item(2139, 25),
				new Item(9979, 15), new Item(3363, 5), new Item(10095, 1),
				new Item(10103, 1), new Item(10819, 7), new Item(6155, 1),
				new Item(7939, 1), new Item(6291, 1), new Item(6319, 1),
				new Item(2860, 3), new Item(237, 1), new Item(10149, 1),
				new Item(2151, 5), new Item(12156, 1), new Item(1934, 14),
				new Item(6033, 12), new Item(6010, 1), new Item(12148, 3),
				new Item(12134, 1), new Item(12153, 1), new Item(1520, 65),
				new Item(5934, 2), new Item(1964, 25), new Item(8431, 2),
				new Item(3138, 1), new Item(2462, 1), new Item(249, 4),
				new Item(951, 18), new Item(311, 14), new Item(6980, 5),
				new Item(1442, 1), new Item(1438, 1), new Item(572, 2),
				new Item(9737, 2), new Item(9976, 4), new Item(10117, 1),
				new Item(383, 1), new Item(1444, 1), new Item(1444, 1), },
				null, null, null) {

			@Override
			public void effect(Player player) {
				if (Utils.random(2) == 0) {
					Item charm = CHARMS[Utils.random(CHARMS.length)];
					int charmAmount = Utils.random(charm.getAmount());
					player.getDialogueManager().startDialogue(
							"ItemMessage",
							"The impling was carrying a"
									+ charm.getName().toLowerCase() + ".",
							charm.getId());
					player.getInventory().addItemDrop(charm.getId(),
							charmAmount);
				}
			}
		},

		NATURE_IMPLING(1034, 11250, 250, 353, 58, new Item[] {
				new Item(5100, 1), new Item(5104, 1), new Item(5281, 1),
				new Item(5294, 1), new Item(6016, 1), new Item(1513, 1),
				new Item(253, 4), }, new Item[] { new Item(5298, 5),
				new Item(5299, 1), new Item(5297, 1), new Item(3051, 1),
				new Item(5285, 1), new Item(5286, 1), new Item(5313, 1),
				new Item(5974, 1), }, new Item[] { new Item(5304, 1),
				new Item(5295, 1) }, new Item[] { new Item(5303, 1),
				new Item(219, 1), new Item(269, 1) }),

		MAGPIE_IMPLING(1035, 11252, 289, 409, 65, new Item[] {
				new Item(1682, 3), new Item(1732, 3), new Item(2569, 3),
				new Item(3391, 1), new Item(5541, 1), new Item(1748, 6), },
				new Item[] { new Item(1333, 1), new Item(1347, 1),
						new Item(2571, 5), new Item(4097, 1),
						new Item(4095, 1), new Item(2364, 2),
						new Item(1603, 1), },
				new Item[] { new Item(1215, 1), new Item(1185, 1),
						new Item(1601, 1), new Item(5287, 1), new Item(987, 1),
						new Item(985, 1), }, new Item[] { new Item(5300, 1),
						new Item(12121, 1), new Item(993, 1) }),

		NINJA_IMPLING(6053, 11254, 339, 481, 74, new Item[] {
				new Item(6328, 1), new Item(3385, 1), new Item(3391, 1),
				new Item(4097, 1), new Item(4095, 1), new Item(3101, 1),
				new Item(1333, 1), new Item(1347, 1), new Item(1215, 1),
				new Item(6313, 1), new Item(892, 70), new Item(811, 70),
				new Item(868, 40), new Item(1747, 16), new Item(140, 4),
				new Item(805, 40), new Item(25496, 4), }, new Item[] {
				new Item(9342, 1), new Item(6155, 1) }, new Item[] {
				new Item(2363, 1), new Item(9194, 1) }, null),

		DRAGON_IMPLING(6054, 11256, 390, 553, 83,
				new Item[] {
						new Item(ItemIdentifiers.BABY_DRAGON_BONES_NOTED,
								Utils.random(70, 120)),
						new Item(ItemIdentifiers.DRAGON_ARROW, Utils.random(52,
								99)),
						new Item(ItemIdentifiers.DRAGON_ARROWHEADS,
								Utils.random(100, 500)),
						new Item(ItemIdentifiers.DRAGON_BOLT_TIPS,
								Utils.random(10, 49)),
						new Item(ItemIdentifiers.DRAGON_BOLTS, Utils.random(3,
								40)),
						new Item(ItemIdentifiers.DRAGON_DART_TIP, Utils.random(
								105, 200)) }, new Item[] {
						new Item(ItemIdentifiers.DRAGON_BONES_NOTED,
								Utils.random(34, 70)),
						new Item(ItemIdentifiers.DRAGONSTONE_NOTED,
								Utils.random(3, 6)),
						new Item(ItemIdentifiers.MAGIC_SEED, 1),
						new Item(ItemIdentifiers.DRAGON_DAGGER, 3),
						new Item(ItemIdentifiers.DRAGON_DART,
								Utils.random(105 - 240)) }, new Item[] {
						new Item(ItemIdentifiers.AMULET_OF_GLORY_NOTED,
								Utils.random(2, 3)),
						new Item(ItemIdentifiers.DRAGONSTONE_AMULET_NOTED,
								Utils.random(2, 3)),
						new Item(ItemIdentifiers.MYSTIC_ROBE_BOTTOM, 1),
						new Item(ItemIdentifiers.SUMMER_PIE_NOTED,
								Utils.random(5, 15)),
						new Item(ItemIdentifiers.SNAPDRAGON_SEED, 6) },
				new Item[] { new Item(ItemIdentifiers.DEATH_TIARA, 1) }),

		ZOMBIE_IMPLING(7902, 15515, 412, 585, 87, new Item[] {
				new Item(ItemIdentifiers.BABY_DRAGON_BONES_NOTED, Utils.random(
						2, 6)),
				new Item(ItemIdentifiers.BIG_BONES_NOTED, Utils.random(5, 15)),
				new Item(ItemIdentifiers.DRAGON_BONES_NOTED, 3) }, new Item[] {
				new Item(ItemIdentifiers.CURVED_BONE, 1),
				new Item(ItemIdentifiers.LONG_BONE, 1) }, new Item[] {
				new Item(ItemIdentifiers.FAYRG_BONES, 3),
				new Item(ItemIdentifiers.OURG_BONES, 1),
				new Item(ItemIdentifiers.RAURG_BONES, 2),
				new Item(ItemIdentifiers.WYVERN_BONES, 3) },
				new Item[] { new Item(ItemIdentifiers.MONKEY_SKULL, 1) }),

		KINGLY_IMPLING(
				7903,
				15517,
				434,
				617,
				91,
				new Item[] { new Item(1705, Utils.random(3, 11)),
						new Item(1684, 3), new Item(1618, Utils.random(2, 4)),
						new Item(990, 2) },
				new Item[] { new Item(9341, 40 + Utils.random(30)),
						new Item(9342, 57), new Item(15511, 1),
						new Item(15509, 1), new Item(15505, 1),
						new Item(15507, 1), new Item(15503, 1),
						new Item(11212, 40 + Utils.random(104)),
						new Item(9193, 62 + Utils.random(8)),
						new Item(11230, Utils.random(20, 40)),
						new Item(11232, 70),
						new Item(1306, Utils.random(1, 2)), new Item(1249, 1) },
				new Item[] { new Item(9194, Utils.random(1, 71)) },
				new Item[] { new Item(7158, 1), new Item(2366, 1),
						new Item(6571, 1) }),

		CRYSTAL_IMPLING(20102, 32272, 525, 820, 95, new Item[] {
				new Item(ItemIdentifiers.LOOP_HALF_OF_A_KEY, 1),
				new Item(ItemIdentifiers.TOOTH_HALF_OF_A_KEY, 1) }, new Item[] {
				new Item(ItemIdentifiers.COCONUT_NOTED, 20),
				new Item(ItemIdentifiers.ONYX_BOLT_TIPS, 16),
				new Item(ItemIdentifiers.UNCUT_DIAMOND_NOTED, 10),
				new Item(ItemIdentifiers.UNCUT_DRAGONSTONE_NOTED, 4),
				new Item(ItemIdentifiers.MAGIC_LOGS_NOTED, 30),
				new Item(ItemIdentifiers.PRAYER_POTION_4_NOTED, 8),
				new Item(ItemIdentifiers.SUPER_RESTORE_4_NOTED, 8),
				new Item(ItemIdentifiers.CRYSTAL_TOOL_SEED, 1),
				new Item(ItemIdentifiers.CRYSTAL_WEAPON_SEED),
				new Item(ItemIdentifiers.ELDER_SEED, 1),
				new Item(ItemIdentifiers.LANTADYME_SEED, 2),
				new Item(ItemIdentifiers.MAGIC_SEED, 1),
				new Item(ItemIdentifiers.TORSTOL_SEED, 2) },
				new Item[] { new Item(ItemIdentifiers.CRYSTAL_ACORN, 1) },
				new Item[] { new Item(ItemIdentifiers.THINKER_BOOTS, 1),
						new Item(ItemIdentifiers.THINKER_GLOVES, 1) }),

		BUTTERFLYTEST(1, 1, 434, 617, 91, null, null, null, null) {

			@Override
			public void effect(Player player) {
				// stat boost
			}
		};

		static final Map<Short, FlyingEntities> flyingEntities = new HashMap<>();

		static {
			for (FlyingEntities impling : FlyingEntities.values())
				flyingEntities.put((short) impling.reward, impling);
		}

		public static FlyingEntities forItem(short reward) {
			return flyingEntities.get(reward);
		}

		private int npcId, level, reward;
		private double puroExperience, rsExperience;
		private Item[] rarelyCommon, common, rare, extremelyRare;

		FlyingEntities(int npcId, int reward, double puroExperience,
				double rsExperience, int level, Item[] rarelyCommon,
				Item[] common, Item[] rare, Item[] extremelyRare) {
			this.npcId = npcId;
			this.reward = reward;
			this.puroExperience = puroExperience;
			this.rsExperience = rsExperience;
			this.level = level;
			this.rarelyCommon = rarelyCommon;
			this.common = common;
			this.rare = rare;
			this.extremelyRare = extremelyRare;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevel() {
			return level;
		}

		public int getReward() {
			return reward;
		}

		public double getPuroExperience() {
			return puroExperience;
		}

		public double getRsExperience() {
			return rsExperience;
		}

		public Item[] getRarelyCommon() {
			return rarelyCommon;
		}

		public Item[] getCommon() {
			return common;
		}

		public Item[] getRare() {
			return rare;
		}

		public Item[] getExtremelyRare() {
			return extremelyRare;
		}

		public void effect(Player player) {

		}

		public static FlyingEntities forId(int itemId) {
			for (FlyingEntities entity : FlyingEntities.values()) {
				if (itemId == entity.getReward())
					return entity;
			}
			return null;
		}

		public static FlyingEntities forNPC(NPC npc) {
			for (FlyingEntities entity : FlyingEntities.values()) {
				if (npc.getId() == entity.getNpcId()
						|| entity.toString().toLowerCase().replace("_", " ")
								.contains(npc.getName().toLowerCase()))
					return entity;
			}
			return null;
		}
	}

	public static void captureFlyingEntity(final Player player, final NPC npc) {
		final String name = npc.getDefinitions().getName().toUpperCase();
		final FlyingEntities instance = FlyingEntities.forNPC(npc);
		if (instance == null)
			return;
		final boolean isImpling = name.toLowerCase().contains("impling");
		if (!player.getInventory().containsItem(isImpling ? 11260 : 10012, 1)) {
			player.getPackets().sendGameMessage(
					"You don't have an empty "
							+ (isImpling ? "impling jar" : "butterfly jar")
							+ " in which to keep "
							+ (isImpling ? "an impling" : "a butterfly") + ".");
			return;
		}
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != 11259 && weaponId != 10010 && isImpling) {
			player.getPackets()
					.sendGameMessage(
							"You need to have a butterfly net equipped in order to capture an impling.");
			return;
		}
		if (player.getSkills().getLevel(Skills.HUNTER) < instance.getLevel()) {
			player.getPackets().sendGameMessage(
					"You need a hunter level of " + instance.getLevel()
							+ " to capture a " + name.toLowerCase() + ".");
			return;
		}
		if (System.currentTimeMillis() - player.lastImpling < 5000) {
			long totalTime = System.currentTimeMillis() - player.lastImpling;
			long sec = 200 - totalTime / 1000;
			player.getPackets().sendGameMessage(
					"You need to wait " + sec
							+ " seconds before you can catch another impling.");
			return;
		}
		player.lock(2);
		player.getPackets().sendGameMessage("You swing your net...");
		player.setNextAnimation(CAPTURE_ANIMATION);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (isSuccessful()) {
					if (player.getInventory().addItem(
							new Item(instance.getReward(), 1))) {
						player.lastImpling = System.currentTimeMillis();
						player.getInventory().deleteItem(new Item(11260, 1));
						player.getSkills().addXp(Skills.HUNTER,
								instance.getPuroExperience());
						npc.setRespawnTask(); // sets loc and finishes auto
						player.getSkillTasks().handleTask(Traps.NET, 1);
						if (player.getDailyTask() != null)
							player.getDailyTask().incrementTask(player, 3,
									instance.getReward(), Skills.HUNTER);
						player.getCompCapeManager().increaseRequirement(
								Requirement.IMPLINGS, 1);
						player.getPackets().sendGameMessage(
								"You manage to catch the " + name.toLowerCase()
										+ " and squeeze it into a jar.");
					}
				} else {
					if (isImpling) {
						npc.setNextForceTalk(new ForceTalk(
								"Tehee, you missed me!"));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								WorldTile teleTile = npc;
								for (int trycount = 0; trycount < 10; trycount++) {
									teleTile = new WorldTile(npc, 3);
									if (World.isTileFree(npc.getPlane(),
											teleTile.getX(), teleTile.getY(),
											player.getSize()))
										break;
									teleTile = npc;
								}
								npc.setNextWorldTile(teleTile);
							}
						}, 2);
					}
					player.getPackets()
							.sendGameMessage(
									"...you stumble and miss the "
											+ name.toLowerCase());
				}
			}
		});
	}

	public static void openJar(Player player, FlyingEntities instance, int slot) {
		boolean isImpling = instance.toString().toLowerCase()
				.contains("impling");
		if (isImpling) {
			double chance = Utils.randomDouble() * 100;
			Item item = null;
			Item[] list;
			if (chance <= 60)
				list = instance.getRarelyCommon();
			else if (chance <= 90)
				list = instance.getCommon();
			else if (chance <= 99.8)
				list = instance.getRare();
			else
				// 3% of extreme rare
				list = instance.getExtremelyRare();
			if (list != null)
				item = list[Utils.random(list.length)];
			if (item == null)
				return;
			player.getInventory().getItem(slot).setId(11260);
			player.getInventory().refresh(slot);
			player.getInventory().addItemDrop(item.getId(),
					Utils.random(item.getAmount()) + 1);

		}
		instance.effect(player);
		if (Utils.random(2) == 0) {
			player.getInventory().deleteItem(new Item(isImpling ? 11260 : 11));
			player.getPackets()
					.sendGameMessage(
							"You press too hard on the jar and the glass shatters in your hands.");
			player.applyHit(new Hit(player, 10, HitLook.REGULAR_DAMAGE));
		}
	}

	public static boolean isSuccessful() {
		return Utils.random(4) != 0;
	}
}