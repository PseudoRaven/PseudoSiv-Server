package net.nocturne.game.player.actions.skills.fletching;

import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class LogsFletching extends Action {

	public enum FletchData {

		ARROW_SHAFTS(1, 5, 24938, 1511, 52, 15, Skills.FLETCHING),

		ARROW_SHAFTS_OAK(15, 7.5, 6684, 1521, 52, 20, Skills.FLETCHING),

		ARROW_SHAFTS_WILLOW(30, 10, 6684, 1519, 52, 25, Skills.FLETCHING),

		ARROW_SHAFTS_MAPLE(45, 12.5, 6684, 1517, 52, 30, Skills.FLETCHING),

		ARROW_SHAFTS_YEW(60, 15, 6684, 1515, 52, 35, Skills.FLETCHING),

		ARROW_SHAFTS_MAGIC(75, 17.5, 6684, 1513, 52, 40, Skills.FLETCHING),

		ARROW_SHAFTS_ELDER(90, 20, 6684, 1511, 52, 45, Skills.FLETCHING),

		SHORTBOW_U(1, 5, 24938, 1511, 50, 1, Skills.FLETCHING),

		WOODEN_STOCK(9, 6, 24938, 1511, 9440, 1, Skills.FLETCHING),

		SHIELDBOW_U(10, 10, 24938, 1511, 48, 1, Skills.FLETCHING),

		OAK_SHORTBOW_U(20, 16.5, 24938, 1521, 54, 1, Skills.FLETCHING),

		OAK_STOCK(24, 16, 24938, 1521, 9442, 1, Skills.FLETCHING),

		OAK_SHIELDBOW_U(25, 25, 24938, 1521, 56, 1, Skills.FLETCHING),

		WILLOW_SHORTBOW_U(35, 33, 24938, 1519, 60, 1, Skills.FLETCHING),

		WILLOW_STOCK(39, 22, 1248, 24938, 9444, 1, Skills.FLETCHING),

		WILLOW_SHIELDBOW_U(40, 41.5, 24938, 1519, 58, 1, Skills.FLETCHING),

		TEAK_STOCK(46, 27, 1248, 6333, 9446, 1, Skills.FLETCHING),

		MAPLE_SHORTBOW_U(50, 50, 24938, 1517, 64, 1, Skills.FLETCHING),

		MAPLE_STOCK(54, 32, 1248, 24938, 9448, 1, Skills.FLETCHING),

		MAPLE_SHIELDBOW_U_U(55, 58.3, 24938, 1517, 62, 1, Skills.FLETCHING),

		MAHOGANY_STOCK(61, 41, 1248, 24938, 9450, 1, Skills.FLETCHING),

		YEW_SHORTBOW_U(65, 67.5, 24938, 1515, 68, 1, Skills.FLETCHING),

		YEW_STOCK(69, 50, 24938, 1515, 9452, 1, Skills.FLETCHING),

		YEW_SHIELDBOW_U(70, 75, 24938, 1515, 66, 1, Skills.FLETCHING),

		MAGIC_SHORTBOW_U(80, 83, 24939, 1513, 72, 1, Skills.FLETCHING),

		MAGIC_SHIELDBOW_U(85, 91.5, 24939, 1513, 70, 1, Skills.FLETCHING),

		MAGIC_STOCK(92, 100, 24939, 1513, 25483, 1, Skills.FLETCHING),

		BLISTERWOOD_POLEARM(70, 100, 24940, 21600, 21582, 1, Skills.FLETCHING),

		BLISTERWOOD_STAKE(70, 100, 24940, 21600, 21581, 10, Skills.FLETCHING),

		OFFHAND_BLISTERWOOD_STAKE(70, 100, 24940, 21600, 28575, 10,
				Skills.FLETCHING),

		BLISTERWOOD_STAFF(70, 100, 24940, 21600, 21580, 1, Skills.FLETCHING),

		ELDER_SHORTBOW_U(90, 101.2, 24938, 29556, 29736, 1, Skills.FLETCHING),

		ELDER_SHIELDBOW_U(90, 101.2, 24938, 29556, 29734, 1, Skills.FLETCHING);

		public static FletchData getBarByProduce(int id) {
			for (FletchData fletch : FletchData.values()) {
				if (fletch.getProducedBow() == id)
					return fletch;
			}
			return null;
		}

		public static FletchData getBarByProduce(int id, int quant) {
			if (id == 34676)
				id = 52;
			for (FletchData fletch : FletchData.values()) {
				if (fletch.getProducedBow() == id
						&& (fletch.getAmountProduct() == quant || quant == 0))
					return fletch;
			}
			return null;
		}

		public static FletchData getBar(int id) {
			for (FletchData fletch : FletchData.values()) {
				int item = fletch.getItemsRequired();
				if (item == id)
					return fletch;
			}
			return null;
		}

		public static FletchData getItem(Player player) {
			for (FletchData fletch : FletchData.values()) {
				int item = fletch.getItemsRequired();
				if (!player.getInventory().containsItems(new Item(item)))
					return fletch;
			}
			return null;
		}

		private static final HashMap<Integer, FletchData> FLETCHSTUFF = new HashMap<Integer, FletchData>();

		static {
			for (FletchData f : values()) {
				FLETCHSTUFF.put(f.getItemsRequired(), f);
			}
		}

		public static FletchData getFletchItem(int id) {
			return FLETCHSTUFF.get(id);
		}

		private int levelRequired;
		private double experience;
		private int emote;
		private int itemsRequired;
		private int producedBow;
		private int amount;
		private int skillType;

		private FletchData(int levelRequired, double experience, int emote,
				int itemsRequired, int producedBow, int amount, int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.emote = emote;
			this.itemsRequired = itemsRequired;
			this.producedBow = producedBow;
			this.amount = amount;
			this.skillType = skillType;
		}

		public int getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getEmote() {
			return emote;
		}

		public int getProducedBow() {
			return producedBow;
		}

		public double getExperience() {
			return experience;
		}

		public int getAmountProduct() {
			return amount;
		}

		public int getSkillType() {
			return skillType;
		}

	}

	public FletchData fletch;
	public int ticks;
	public int product;
	private int xpMultiplier = 1;

	public LogsFletching(FletchData fletch, int product, int ticks) {
		this.fletch = fletch;
		this.product = product;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (fletch == null || player == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (fletch == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				fletch.getItemsRequired(), 1)) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getSkills().hasLevel(fletch.getSkillType(),
				fletch.getLevelRequired())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		return true;
	}

	public static FletchData isAttaching(Player player, Item used, Item usedWith) {
		FletchData fletch = null;
		fletch = FletchData.getFletchItem(used.getId());
		Item selected;
		if (fletch != null)
			selected = usedWith;
		else {
			fletch = FletchData.getFletchItem(usedWith.getId());
			selected = used;
		}
		if (fletch != null)
			return fletch;
		else
			return null;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 1;
		int emote = fletch.getEmote();
		double xp = fletch.getExperience();
		int amount = fletch.getAmountProduct() * multiplier;
		int item = fletch.getItemsRequired();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		player.getInventory().deleteItem(item, 1);
		player.getInventory().addItem(fletch.getProducedBow(), amount);
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, fletch.getProducedBow(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		if (player.getClanManager() != null
				&& player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					fletch.getProducedBow(), Skills.FLETCHING);
		player.getSkills().addXp(fletch.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));
		return 2;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}
}