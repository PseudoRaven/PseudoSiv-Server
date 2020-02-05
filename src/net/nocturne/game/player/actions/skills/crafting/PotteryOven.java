package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class PotteryOven extends Action {

	public enum FinishPot {

		EMPTY_POT(1, 6.3, new Item[] { new Item(1787) }, new Item(1931),
				Skills.CRAFTING), CLAY_RING(4, 11,
				new Item[] { new Item(20052) }, new Item(20053),
				Skills.CRAFTING), PIE_DISH(7, 10,
				new Item[] { new Item(1789) }, new Item(2313), Skills.CRAFTING), BOWL(
				8, 15, new Item[] { new Item(1791) }, new Item(1923),
				Skills.CRAFTING), PLANT_POT(19, 17.5, new Item[] { new Item(
				5352) }, new Item(5350), Skills.CRAFTING), POT_LID(25, 20,
				new Item[] { new Item(4438) }, new Item(4440), Skills.CRAFTING), CRACKED_COOKING_URN_NR(
				2, 18, new Item[] { new Item(20349) }, new Item(20350),
				Skills.CRAFTING), FRAGILE_COOKING_URN_NR(12, 24,
				new Item[] { new Item(20355) }, new Item(20356),
				Skills.CRAFTING), COOKING_URN_NR(36, 42.9,
				new Item[] { new Item(20361) }, new Item(20362),
				Skills.CRAFTING), STRONG_COOKING_URN_NR(51, 52.5,
				new Item[] { new Item(20367) }, new Item(20368),
				Skills.CRAFTING), DECORATED_COOKING_URN_NR(81, 78,
				new Item[] { new Item(20373) }, new Item(20374),
				Skills.CRAFTING), CRACKED_FISHING_URN_NR(2, 18,
				new Item[] { new Item(20319) }, new Item(20320),
				Skills.CRAFTING), FRAGILE_FISHING_URN_NR(15, 30,
				new Item[] { new Item(20325) }, new Item(20326),
				Skills.CRAFTING), FISHING_URN_NR(41, 46.8,
				new Item[] { new Item(20331) }, new Item(20332),
				Skills.CRAFTING), STRONG_FISHING_URN_NR(53, 54,
				new Item[] { new Item(20337) }, new Item(20338),
				Skills.CRAFTING), DECORATED_FISHING_URN_NR(76, 72,
				new Item[] { new Item(20343) }, new Item(20344),
				Skills.CRAFTING), CRACKED_MINING_URN_NR(1, 16.8,
				new Item[] { new Item(20379) }, new Item(20380),
				Skills.CRAFTING), FRAGILE_MINING_URN_NR(17, 31.8,
				new Item[] { new Item(20385) }, new Item(20386),
				Skills.CRAFTING), MINING_URN_NR(32, 40.8,
				new Item[] { new Item(20391) }, new Item(20392),
				Skills.CRAFTING), STRONG_MINING_URN_NR(48, 49.2,
				new Item[] { new Item(20397) }, new Item(20398),
				Skills.CRAFTING), DECORATED_MINING_URN_NR(59, 57,
				new Item[] { new Item(20403) }, new Item(20404),
				Skills.CRAFTING), IMPIOUS_URN_NR(2, 18, new Item[] { new Item(
				20409) }, new Item(20410), Skills.CRAFTING), ACCURSED_URN_NR(
				26, 37.5, new Item[] { new Item(20415) }, new Item(20416),
				Skills.CRAFTING), INFERNAL_URN_NR(62, 60,
				new Item[] { new Item(20421) }, new Item(20422),
				Skills.CRAFTING), CRACKED_SMELTING_NR(4, 23.1,
				new Item[] { new Item(20271) }, new Item(20272),
				Skills.CRAFTING), FRAGILE_SMELTING_NR(17, 31.8,
				new Item[] { new Item(20277) }, new Item(20278),
				Skills.CRAFTING), SMELTING_URN_NR(35, 42,
				new Item[] { new Item(20283) }, new Item(20284),
				Skills.CRAFTING), STRONG_SMELTING_UNR_NR(49, 50.1,
				new Item[] { new Item(20289) }, new Item(20290),
				Skills.CRAFTING), CRACKED_WOODCUTTING_URN_NR(4, 23.1,
				new Item[] { new Item(20295) }, new Item(20296),
				Skills.CRAFTING), FRAGILE_WOODCUTTING_URN_NR(15, 30,
				new Item[] { new Item(20301) }, new Item(20302),
				Skills.CRAFTING), WOODCUTTING_URN_NR(44, 48,
				new Item[] { new Item(20307) }, new Item(20308),
				Skills.CRAFTING), STRONG_WOODCUTTING_URN_NR(61, 58.2,
				new Item[] { new Item(20313) }, new Item(20314),
				Skills.CRAFTING), DECORATED_WOODUCTTING_URN_NR(76, 72,
				new Item[] { new Item(39008) }, new Item(39010),
				Skills.CRAFTING);

		public static FinishPot getPotByProduce(int id) {
			for (FinishPot pot : FinishPot.values()) {
				if (pot.getProducedPot().getId() == id)
					return pot;
			}
			return null;
		}

		public static FinishPot getPot(int id) {
			for (FinishPot pot : FinishPot.values()) {
				for (Item item : pot.getItemsRequired())
					if (item.getId() == id)
						return pot;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private FinishPot(int levelRequired, double experience,
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

		public Item getProducedPot() {
			return producedBar;
		}

		public double getExperience() {
			return experience;
		}

		public int getSkillType() {
			return skillType;
		}
	}

    public FinishPot pot;
    public int ticks;
    private int xpMultiplier = 1;

	public PotteryOven(FinishPot pot, int ticks) {
		this.pot = pot;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (pot == null || player == null) {
			return false;
		}
		return true;
	    }

    @Override
    public boolean process(Player player) {
	if (pot == null || player == null) {
		player.getInterfaceManager().removeInterface(1251);
	    return false;
	}
	if (!player.getInventory().containsItemToolBelt(pot.getItemsRequired()[0].getId(), pot.getItemsRequired()[0].getAmount())) {
		player.getInterfaceManager().removeInterface(1251);
		return false;
	}
	if (pot.getItemsRequired().length > 1) {
	    if (!player.getInventory().containsItemToolBelt(pot.getItemsRequired()[1].getId(), pot.getItemsRequired()[1].getAmount())) {
	    	player.getInterfaceManager().removeInterface(1251);
	    	return false;
	    }
	}
	if (!player.getSkills().hasLevel(pot.getSkillType(), pot.getLevelRequired())){
		player.getInterfaceManager().removeInterface(1251);
		return false;
	}
	return true;
    }

    @Override
    public int processWithDelay(Player player) {
	ticks--;			
	int multiplier = 0;	
	double xp = pot.getExperience();
	int maxQuantity = player.getVarsManager().getBitValue(1002);
	int amount = pot.getProducedPot().getAmount() * multiplier ;
	for (Item required : pot.getItemsRequired()) {
	    player.getInventory().deleteItem(required.getId(), required.getAmount());
	}
	if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, pot.getProducedPot().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
	}
	if (player.getDailyTask() != null)
		player.getDailyTask().incrementTask(player, 3,	pot.getProducedPot().getId(), Skills.CRAFTING);
	player.getInventory().addItem(pot.getProducedPot().getId(), amount);
	player.getSkills().addXp(pot.getSkillType(), xp);
	player.setNextAnimation(new Animation(24975));
	return 2;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
