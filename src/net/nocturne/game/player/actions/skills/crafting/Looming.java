package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class Looming extends Action {

	public enum CreateCape {

		MILESTONE_CAPE_10(10, 10, new Item[] { new Item(
				ItemIdentifiers.BALL_OF_WOOL, 1) }, new Item(20754),
				Skills.CRAFTING), MILESTONE_CAPE_20(20, 20,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 2) },
				new Item(20755), Skills.CRAFTING), MILESTONE_CAPE_30(30, 30,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 3) },
				new Item(20756), Skills.CRAFTING), MILESTONE_CAPE_40(40, 40,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 4) },
				new Item(20757), Skills.CRAFTING), MILESTONE_CAPE_50(50, 50,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 5) },
				new Item(20758), Skills.CRAFTING), MILESTONE_CAPE_60(60, 60,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 6) },
				new Item(20759), Skills.CRAFTING), MILESTONE_CAPE_70(70, 70,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 7) },
				new Item(20760), Skills.CRAFTING), MILESTONE_CAPE_80(80, 80,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 8) },
				new Item(20761), Skills.CRAFTING), MILESTONE_CAPE_90(90, 90,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 9) },
				new Item(20762), Skills.CRAFTING), STRIP_OF_CLOTH(10, 12,
				new Item[] { new Item(ItemIdentifiers.BALL_OF_WOOL, 4) },
				new Item(ItemIdentifiers.STRIP_OF_CLOTH), Skills.CRAFTING), EMPTY_SACK(
				21, 38, new Item[] { new Item(ItemIdentifiers.JUTE_FIBRE, 4) },
				new Item(ItemIdentifiers.EMPTY_SACK), Skills.CRAFTING), BASKET(
				36, 56,
				new Item[] { new Item(ItemIdentifiers.WILLOW_BRANCH, 6) },
				new Item(ItemIdentifiers.BASKET), Skills.CRAFTING), UNFINISHED_NET(
				52, -1, new Item[] { new Item(ItemIdentifiers.FLAX, 6) },
				new Item(ItemIdentifiers.UNFINISHED_NET), Skills.CRAFTING);

		public static CreateCape getBarByProduce(int id) {
			for (CreateCape cape : CreateCape.values()) {
				if (cape.getProducedBar().getId() == id)
					return cape;
			}
			return null;
		}

		public static CreateCape getBar(int id) {
			for (CreateCape cape : CreateCape.values()) {
				for (Item item : cape.getItemsRequired())
					if (item.getId() == id)
						return cape;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private CreateCape(int levelRequired, double experience, Item[] itemsRequired, Item producedBar,
				int skillType) {
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

	public CreateCape cape;
	public int ticks;
	private int xpMultiplier = 1;

	public Looming(CreateCape cape, int ticks) {
		this.cape = cape;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (cape == null || player == null) {
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(cape.getItemsRequired()[0].getId(),
				cape.getItemsRequired()[0].getAmount())) {
			return false;
		}

		for (int x = 0; x < 26; x++)
			if (!player.getSkills().hasLevel(x, cape.getLevelRequired())) {
				player.getPackets().sendGameMessage("You don't have " + cape.getLevelRequired()
						+ " in all the skills to make this milestone cape.");
				return false;
			}

		return true;
	}

	@Override
	public boolean process(Player player) {
		if (cape == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(cape.getItemsRequired()[0].getId(),
				cape.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (cape.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(cape.getItemsRequired()[1].getId(),
					cape.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		double xp = cape.getExperience();
		int amount = cape.getProducedBar().getAmount() * multiplier;
		for (Item required : cape.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1) {
			SkillsDialogue.sendProgressBar(player, cape.getProducedBar().getId(), maxQuantity, (int) xp);
			player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
			xpMultiplier++;
		}
		player.getInventory().addItem(cape.getProducedBar().getId(), amount);
		player.getSkills().addXp(Skills.CRAFTING, xp);
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
