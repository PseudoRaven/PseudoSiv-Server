package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.Action;

public class Tanning extends Action {

	public enum TanningData {

		LEATHER(1, 1, new Item[] { new Item(1739, 1), new Item(995, 0) },
				new Item(1741), 0), HARD_LEATHER(1, 1, new Item[] {
				new Item(1739, 1), new Item(995, 3) }, new Item(1743), 3), SNAKESKIN(
				1, 1, new Item[] { new Item(6287, 1), new Item(995, 15) },
				new Item(6289), 15), SNAKESKIN_TT(1, 1, new Item[] {
				new Item(7801, 1), new Item(995, 20) }, new Item(25611), 20), GREEN_DHIDE(
				1, 1, new Item[] { new Item(1753, 1), new Item(995, 20) },
				new Item(1745), 20), BLUE_DHIDE(1, 1, new Item[] {
				new Item(1751, 1), new Item(995, 20) }, new Item(2505), 20), RED_DHIDE(
				1, 1, new Item[] { new Item(1749, 1), new Item(995, 20) },
				new Item(2507), 20), BLACK_DHIDE(1, 1, new Item[] {
				new Item(1747, 1), new Item(995, 20) }, new Item(2509), 20), ROYAL_DHIDE(
				1, 1, new Item[] { new Item(24372, 1), new Item(995, 20) },
				new Item(24374), 20);

		public static TanningData getBarByProduce(int id) {
			for (TanningData leather : TanningData.values()) {
				if (leather.getProducedBar().getId() == id)
					return leather;
			}
			return null;
		}

		public static TanningData getBar(int id) {
			for (TanningData leather : TanningData.values()) {
				for (Item item : leather.getItemsRequired())
					if (item.getId() == id)
						return leather;
			}
			return null;
		}

		private int levelRequired;
		private int cost;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private TanningData(int levelRequired, int cost, Item[] itemsRequired,
				Item producedBar, int skillType) {
			this.levelRequired = levelRequired;
			this.cost = cost;
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

		public int getCost() {
			return cost;
		}

		public Item getProducedBar() {
			return producedBar;
		}

		public int getSkillType() {
			return skillType;
		}
	}

	public TanningData bar;
	public int ticks;
	public int npcId;

	public Tanning(TanningData bar, int npcId, int ticks) {
		this.bar = bar;
		this.npcId = npcId;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (bar == null || player == null) {
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					bar.getItemsRequired()[0].getId(),
					bar.getItemsRequired()[0].getAmount())) {
				player.getPackets().sendGameMessage(
						"You need "
								+ bar.getItemsRequired()[0].getAmount()
								+ " "
								+ bar.getItemsRequired()[0].getDefinitions()
										.getName()
								+ "'s to tan the "
								+ bar.getProducedBar().getDefinitions()
										.getName() + ".");
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (bar == null || player == null) {
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					bar.getItemsRequired()[0].getId(),
					bar.getItemsRequired()[0].getAmount())) {
				player.getPackets().sendGameMessage(
						"You need "
								+ bar.getItemsRequired()[0].getAmount()
								+ " "
								+ bar.getItemsRequired()[0].getDefinitions()
										.getName()
								+ "'s to tan a "
								+ bar.getProducedBar().getDefinitions()
										.getName() + ".");
				return false;
			}
		}

		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		int amount = 1;
		Item price = bar.getItemsRequired()[1];
		for (Item required : bar.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(), amount);
		}
		player.getInventory().removeItemMoneyPouch(price);
		player.getInventory().addItem(bar.getProducedBar().getId(), amount);
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}