package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class LeatherCrafting extends Action {

	public enum CraftAction {

		LEATHER_GLOVES(1, 13.8, new Item[] { new Item(1741), new Item(1734) },
				new Item(1059), Skills.CRAFTING), LEATHER_BOOTS(7, 16.3,
				new Item[] { new Item(1741), new Item(1734) }, new Item(1061),
				Skills.CRAFTING), LEATHER_COWL(9, 18.5, new Item[] {
				new Item(1741), new Item(1734) }, new Item(1167),
				Skills.CRAFTING), LEATHER_VAMBRACES(11, 22, new Item[] {
				new Item(1741), new Item(1734) }, new Item(1063),
				Skills.CRAFTING), LEATHER_BODY(14, 25, new Item[] {
				new Item(1741), new Item(1734) }, new Item(1129),
				Skills.CRAFTING), LEATHER_CHAPS(18, 27, new Item[] {
				new Item(1741), new Item(1734) }, new Item(1095),
				Skills.CRAFTING), LEATHER_SHIELD(19, 30, new Item[] {
				new Item(1741), new Item(1734) }, new Item(25806),
				Skills.CRAFTING), HARD_LEATHER_GLOVES(25, 32, new Item[] {
				new Item(1743), new Item(1734) }, new Item(25875),
				Skills.CRAFTING), HARD_LEATHER_BOOTS(27, 34, new Item[] {
				new Item(1743), new Item(1734) }, new Item(25821),
				Skills.CRAFTING), HARD_LEATHER_BODY(28, 35, new Item[] {
				new Item(1743), new Item(1734) }, new Item(32631),
				Skills.CRAFTING), HARD_LEATHER_SHIELD(29, 36, new Item[] {
				new Item(1743), new Item(1734) }, new Item(25808),
				Skills.CRAFTING), COIF(38, 37, new Item[] { new Item(1741),
				new Item(1734) }, new Item(1169), Skills.CRAFTING), IMPHIDE_GLOVES(
				10, 10, new Item[] { new Item(25545), new Item(1734) },
				new Item(25851), Skills.CRAFTING), IMPHIDE_BOOTS(11, 10,
				new Item[] { new Item(25545), new Item(1734) },
				new Item(25853), Skills.CRAFTING), IMPHIDE_HOOD(12, 20,
				new Item[] { new Item(25545, 2), new Item(1734) }, new Item(
						25845), Skills.CRAFTING), IMPHIDE_ROBE_BOTTOM(12, 20,
				new Item[] { new Item(25545, 2), new Item(1734) }, new Item(
						25849), Skills.CRAFTING), IMPHIDE_ROBE_TOP(14, 30,
				new Item[] { new Item(25545, 3), new Item(1734) }, new Item(
						25847), Skills.CRAFTING), IMPHIDE_BOOK(15, 30,
				new Item[] { new Item(25545, 3), new Item(1734) }, new Item(
						25662), Skills.CRAFTING), IMP_HORN_WAND(16, 30,
				new Item[] { new Item(25545, 3), new Item(1734) }, new Item(
						25642), Skills.CRAFTING), IMPHIDE_SHIELD(17, 40,
				new Item[] { new Item(25545, 4), new Item(1734) }, new Item(
						25855), Skills.CRAFTING), SPIDER_SILK_GLOVES(20, 12.5,
				new Item[] { new Item(25547), new Item(1734) },
				new Item(25843), Skills.CRAFTING), SPIDER_SILK_BOOTS(21, 12.5,
				new Item[] { new Item(25547), new Item(1734) },
				new Item(25841), Skills.CRAFTING), SPIDER_SILK_HOOD(22, 25,
				new Item[] { new Item(25547, 2), new Item(1734) }, new Item(
						25835), Skills.CRAFTING), SPIDER_SILK_ROBE_BOTTOM(23,
				25, new Item[] { new Item(25547, 2), new Item(1734) },
				new Item(25839), Skills.CRAFTING), SPIDER_SILK_ROBE_TOP(24,
				37.5, new Item[] { new Item(25547, 3), new Item(1734) },
				new Item(25837), Skills.CRAFTING), SPIDER_ORB(25, 25,
				new Item[] { new Item(25547, 2), new Item(1734) }, new Item(
						25658), Skills.CRAFTING), SPIDER_WAND(26, 26,
				new Item[] { new Item(25547, 2), new Item(1734) }, new Item(
						25650), Skills.CRAFTING), CARAPACE_GLOVES(30, 12,
				new Item[] { new Item(25551), new Item(1734) },
				new Item(25865), Skills.CRAFTING), CARAPACE_BOOTS(31, 12,
				new Item[] { new Item(25551), new Item(1734) },
				new Item(25863), Skills.CRAFTING), CARAPACE_HELM(33, 24,
				new Item[] { new Item(25551, 2), new Item(1734) }, new Item(
						25857), Skills.CRAFTING), CARAPACE_LEGS(34, 34,
				new Item[] { new Item(25551, 2), new Item(1734) }, new Item(
						32631), Skills.CRAFTING), CARAPACE_TORSO(35, 36,
				new Item[] { new Item(25551, 3), new Item(1734) }, new Item(
						25859), Skills.CRAFTING), BATWING_GLOVES(50, 50,
				new Item[] { new Item(25549), new Item(1734) },
				new Item(25829), Skills.CRAFTING), BATWING_BOOTS(52, 50,
				new Item[] { new Item(25549), new Item(1734) },
				new Item(25833), Skills.CRAFTING), BATWING_HOOD(54, 100,
				new Item[] { new Item(25549, 2), new Item(1734) }, new Item(
						25825), Skills.CRAFTING), BATWING_LEGS(55, 100,
				new Item[] { new Item(25549, 2), new Item(1734) }, new Item(
						25831), Skills.CRAFTING), BATWING_TORSO(56, 150,
				new Item[] { new Item(25549, 3), new Item(1734) }, new Item(
						25827), Skills.CRAFTING), BATWING_SHIELD(58, 200,
				new Item[] { new Item(25549, 4), new Item(1734) }, new Item(
						25802), Skills.CRAFTING), BATWING_BOOK(59, 200,
				new Item[] { new Item(25549, 4), new Item(1734) }, new Item(
						25660), Skills.CRAFTING), BATWING_WAND(61, 200,
				new Item[] { new Item(25549, 4), new Item(1734) }, new Item(
						25648), Skills.CRAFTING), SNAKESKIN_BOOTS(45, 30,
				new Item[] { new Item(6289, 6), new Item(1734) },
				new Item(6328), Skills.CRAFTING), SNAKESKIN_VAMBRACES(47, 35,
				new Item[] { new Item(6289, 8), new Item(1734) },
				new Item(6330), Skills.CRAFTING), SNAKESKIN_BANDANA(48, 45,
				new Item[] { new Item(6289, 5), new Item(1734) },
				new Item(6326), Skills.CRAFTING), SNAKESKIN_CHAPS(51, 50,
				new Item[] { new Item(6289, 12), new Item(1734) }, new Item(
						6324), Skills.CRAFTING), SNAKESKIN_BODY(53, 55,
				new Item[] { new Item(6289, 15), new Item(1734) }, new Item(
						6322), Skills.CRAFTING), GREEN_DHIDE_VAMBRACES(57, 62,
				new Item[] { new Item(1745), new Item(1734) }, new Item(1065),
				Skills.CRAFTING), GREEN_DHIDE_CHAPS(60, 124, new Item[] {
				new Item(1745, 2), new Item(1734) }, new Item(1099),
				Skills.CRAFTING), GREEN_DHIDE_COIF(61, 124, new Item[] {
				new Item(1745, 2), new Item(1734) }, new Item(12936),
				Skills.CRAFTING), GREEN_DHIDE_BODY(63, 186, new Item[] {
				new Item(1745, 3), new Item(1734) }, new Item(1135),
				Skills.CRAFTING), GREEN_DHIDE_SHIELD(64, 248, new Item[] {
				new Item(1745, 4), new Item(1734) }, new Item(25794),
				Skills.CRAFTING), BLUE_DHIDE_VAMBRACES(66, 70, new Item[] {
				new Item(2505), new Item(1734) }, new Item(2487),
				Skills.CRAFTING), BLUE_DHIDE_CHAPS(68, 140, new Item[] {
				new Item(2505, 2), new Item(1734) }, new Item(2493),
				Skills.CRAFTING), BLUE_DHIDE_COIF(69, 140, new Item[] {
				new Item(2505, 2), new Item(1734) }, new Item(12943),
				Skills.CRAFTING), BLUE_DHIDE_BODY(71, 210, new Item[] {
				new Item(2505, 3), new Item(1734) }, new Item(2499),
				Skills.CRAFTING), BLUE_DHIDE_SHIELD(72, 280, new Item[] {
				new Item(2505, 4), new Item(1734) }, new Item(25796),
				Skills.CRAFTING), RED_DHIDE_VAMBRACES(73, 78, new Item[] {
				new Item(2507), new Item(1734) }, new Item(2489),
				Skills.CRAFTING), RED_DHIDE_CHAPS(75, 156, new Item[] {
				new Item(2507, 2), new Item(1734) }, new Item(2495),
				Skills.CRAFTING), RED_DHIDE_COIF(77, 156, new Item[] {
				new Item(2507, 2), new Item(1734) }, new Item(12950),
				Skills.CRAFTING), RED_DHIDE_BODY(77, 234, new Item[] {
				new Item(2507, 3), new Item(1734) }, new Item(2501),
				Skills.CRAFTING), RED_DHIDE_SHIELD(78, 312, new Item[] {
				new Item(2507, 4), new Item(1734) }, new Item(25798),
				Skills.CRAFTING), BLACK_DHIDE_VAMBRACES(79, 86, new Item[] {
				new Item(2509), new Item(1734) }, new Item(2491),
				Skills.CRAFTING), BLACK_DHIDE_CHAPS(82, 172, new Item[] {
				new Item(2509, 2), new Item(1734) }, new Item(2497),
				Skills.CRAFTING), BLACK_DHIDE_COIF(83, 172, new Item[] {
				new Item(2509, 2), new Item(1734) }, new Item(12957),
				Skills.CRAFTING), BLACK_DHIDE_BODY(84, 258, new Item[] {
				new Item(2509, 3), new Item(1734) }, new Item(2503),
				Skills.CRAFTING), BLACK_DHIDE_SHIELD(85, 344, new Item[] {
				new Item(2509, 4), new Item(1734) }, new Item(25800),
				Skills.CRAFTING), ROYAL_DHIDE_VAMBRACES(87, 94, new Item[] {
				new Item(24374), new Item(1734) }, new Item(24376),
				Skills.CRAFTING), ROYAL_DHIDE_CHAPS(89, 188, new Item[] {
				new Item(24374, 2), new Item(1734) }, new Item(24379),
				Skills.CRAFTING), ROYAL_DHIDE_COIF(91, 188, new Item[] {
				new Item(24374, 2), new Item(1734) }, new Item(24388),
				Skills.CRAFTING), ROYAL_DHIDE_BODY(93, 282, new Item[] {
				new Item(24374, 3), new Item(1734) }, new Item(24382),
				Skills.CRAFTING), YAK_ARMOUR_LEGS(43, 32, new Item[] {
				new Item(10820), new Item(1734) }, new Item(10824),
				Skills.CRAFTING), YAK_ARMOUR_BODY(46, 32, new Item[] {
				new Item(10820, 2), new Item(1734) }, new Item(10822),
				Skills.CRAFTING), SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(1063), new Item(10113) }, new Item(32631),
				Skills.CRAFTING), GREEN_SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(1065), new Item(10113) }, new Item(10079),
				Skills.CRAFTING), BLUE_SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(2487), new Item(10113) }, new Item(10081),
				Skills.CRAFTING), RED_SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(2489), new Item(10113) }, new Item(10083),
				Skills.CRAFTING), BLACK_SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(2491), new Item(10113) }, new Item(10085),
				Skills.CRAFTING), ROYAL_SPIKY_VAMBRACES(32, 5.8, new Item[] {
				new Item(24376), new Item(10113) }, new Item(25553),
				Skills.CRAFTING), STUDDED_CHAPS(44, 42, new Item[] {
				new Item(32626), new Item(2370) }, new Item(1097),
				Skills.CRAFTING), STUDDED_BODY(41, 40, new Item[] {
				new Item(32626), new Item(2370) }, new Item(1133),
				Skills.CRAFTING), STUDDED_lEATHER_GLOVES(42, 40, new Item[] {
				new Item(32626), new Item(2370) }, new Item(25877),
				Skills.CRAFTING), STUDDED_lEATHER_BOOTS(43, 40, new Item[] {
				new Item(32626), new Item(2370) }, new Item(25823),
				Skills.CRAFTING), STUDDED_LEATHER_SHIELD(45, 43, new Item[] {
				new Item(32626), new Item(2370) }, new Item(25810),
				Skills.CRAFTING);

		public static CraftAction getBarByProduce(int id) {
			for (CraftAction craft : CraftAction.values()) {
				if (craft.getProducedBar().getId() == id)
					return craft;
			}
			return null;
		}

		public static CraftAction getBar(int id) {
			for (CraftAction craft : CraftAction.values()) {
				for (Item item : craft.getItemsRequired())
					if (item.getId() == id)
						return craft;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private CraftAction(int levelRequired, double experience,
				Item[] itemsRequired, Item producedBar, int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.skillType = skillType;
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedBar() {
			return producedBar;
		}

		public double getExperience() {
			return experience;
		}

		public int getSkillType() {
			return skillType;
		}
	}

	public CraftAction craft;
	public int ticks;
	public Item item;
	private int xpMultiplier = 1;

	public LeatherCrafting(CraftAction craft, Item item, int ticks) {
		this.craft = craft;
		this.item = item;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (craft == null || player == null) {
			return false;
		}
		int thread = player.getInventory().getAmountOf(1734);
		if (thread < 1)
			player.getPackets().sendGameMessage(
					"You need thread in order to make this.");
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (craft == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				craft.getItemsRequired()[0].getId(),
				craft.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (craft.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					craft.getItemsRequired()[1].getId(),
					craft.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (player.getSkills().getLevel(craft.getSkillType()) < craft.getLevelRequired()) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		double xp = craft.getExperience();
		int amount = craft.getProducedBar().getAmount() * multiplier;
		for (Item required : craft.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, craft.getProducedBar().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					craft.getProducedBar().getId(), Skills.CRAFTING);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(25594));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}
