package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class PotteryWheel extends Action {

	public enum CreatePot {

		POT_UNFIRED(1, 6.3, new Item[] { new Item(1761) }, new Item(1787),
				Skills.CRAFTING), PIE_DISH_UNFIRED(7, 15,
				new Item[] { new Item(1761) }, new Item(1789), Skills.CRAFTING), BOWL_UNFIRED(
				8, 18, new Item[] { new Item(1761) }, new Item(1791),
				Skills.CRAFTING), PLANT_POT_UNFIRED(19, 20,
				new Item[] { new Item(1761) }, new Item(5352), Skills.CRAFTING), POT_LID_UNFIRED(
				25, 20, new Item[] { new Item(1761) }, new Item(4438),
				Skills.CRAFTING), CRACKED_COOKING_URN_UNF(2, 12,
				new Item[] { new Item(1761, 2) }, new Item(20349),
				Skills.CRAFTING), FRAGILE_COOKING_URN_UNF(12, 16,
				new Item[] { new Item(1761, 2) }, new Item(20355),
				Skills.CRAFTING), COOKING_URN_UNF(36, 28.6,
				new Item[] { new Item(1761, 2) }, new Item(20361),
				Skills.CRAFTING), STRONG_COOKING_URN_UNF(51, 35,
				new Item[] { new Item(1761, 2) }, new Item(20367),
				Skills.CRAFTING), DECORATED_COOKING_URN_UNF(81, 52,
				new Item[] { new Item(1761, 2) }, new Item(20373),
				Skills.CRAFTING), CRACKED_FISHING_URN_UNF(2, 12,
				new Item[] { new Item(1761, 2) }, new Item(20319),
				Skills.CRAFTING), FRAGILE_FISHING_URN_UNF(15, 20,
				new Item[] { new Item(1761, 2) }, new Item(20325),
				Skills.CRAFTING), FISHING_URN_UNF(41, 31.2,
				new Item[] { new Item(1761, 2) }, new Item(20331),
				Skills.CRAFTING), STRONG_FISHING_URN_UNF(53, 36,
				new Item[] { new Item(1761, 2) }, new Item(20337),
				Skills.CRAFTING), DECORATED_FISHING_URN_UNF(76, 48,
				new Item[] { new Item(1761, 2) }, new Item(20343),
				Skills.CRAFTING), CRACKED_MINING_URN_UNF(1, 11.2,
				new Item[] { new Item(1761, 2) }, new Item(20379),
				Skills.CRAFTING), FRAGILE_MINING_URN_UNF(17, 21.2,
				new Item[] { new Item(1761, 2) }, new Item(20385),
				Skills.CRAFTING), MINING_URN_UNF(32, 27.2,
				new Item[] { new Item(1761, 2) }, new Item(20391),
				Skills.CRAFTING), STRONG_MINING_URN_UNF(48, 30.8,
				new Item[] { new Item(1761, 2) }, new Item(20397),
				Skills.CRAFTING), DECORATED_MINING_URN_UNF(59, 38,
				new Item[] { new Item(1761, 2) }, new Item(20403),
				Skills.CRAFTING), IMPIOUS_URN_UNFIRED(2, 12,
				new Item[] { new Item(1761, 2) }, new Item(20409),
				Skills.CRAFTING), ACCURSED_URN_UNFIRED(26, 25,
				new Item[] { new Item(1761, 2) }, new Item(20415),
				Skills.CRAFTING), INFERNAL_URN_UNFIRED(62, 40,
				new Item[] { new Item(1761, 2) }, new Item(20421),
				Skills.CRAFTING), CRACKED_SMELTING_URN_UNF(4, 15.4,
				new Item[] { new Item(1761, 2) }, new Item(20271),
				Skills.CRAFTING), FRAGILE_SMELTING_URN_UNF(17, 21.2,
				new Item[] { new Item(1761, 2) }, new Item(20277),
				Skills.CRAFTING), SMELTING_URN_UNF(35, 28,
				new Item[] { new Item(1761, 2) }, new Item(20283),
				Skills.CRAFTING), STRONG_SMELTING_URN_UNF(49, 33.4,
				new Item[] { new Item(1761, 2) }, new Item(20289),
				Skills.CRAFTING), CRACKED_WOODCUTTING_URN_UNF(4, 15.4,
				new Item[] { new Item(1761, 2) }, new Item(20295),
				Skills.CRAFTING), FRAGILE_WOODCUTTING_URN_UNF(15, 20,
				new Item[] { new Item(1761, 2) }, new Item(20301),
				Skills.CRAFTING), WOODCUTTING_URN_UNF(44, 32,
				new Item[] { new Item(1761, 2) }, new Item(20307),
				Skills.CRAFTING), STRONG_WOODCUTTING_URN_UNF(61, 38.8,
				new Item[] { new Item(1761, 2) }, new Item(20313),
				Skills.CRAFTING), DECORATED_WOODUCTTING_URN_UNF(76, 48,
				new Item[] { new Item(1761, 2) }, new Item(39008),
				Skills.CRAFTING);

		public static CreatePot getPotByProduce(int id) {
			for (CreatePot pot : CreatePot.values()) {
				if (pot.getProducedPot().getId() == id)
					return pot;
			}
			return null;
		}

		public static CreatePot getPot(int id) {
			for (CreatePot pot : CreatePot.values()) {
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

		private CreatePot(int levelRequired, double experience,
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

    public CreatePot pot;
    public int ticks;
    private int xpMultiplier = 1;

	public PotteryWheel(CreatePot pot, int ticks) {
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
		if (!player.getInventory().containsItemToolBelt(pot.getItemsRequired()[0].getId(),
				pot.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (pot.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(pot.getItemsRequired()[1].getId(),
					pot.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (!player.getSkills().hasLevel(pot.getSkillType(), pot.getLevelRequired())) {
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
		int amount = pot.getProducedPot().getAmount() * multiplier;
		for (Item required : pot.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1) {
			SkillsDialogue.sendProgressBar(player, pot.getProducedPot().getId(), maxQuantity, (int) xp);
			player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
			xpMultiplier++;
		}
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3, pot.getProducedPot().getId(), Skills.CRAFTING);
		player.getInventory().addItem(pot.getProducedPot().getId(), amount);
		player.getSkills().addXp(pot.getSkillType(), xp);
		player.setNextAnimation(new Animation(883));

		if (ticks > 0) {
			return 1;
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
