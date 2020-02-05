package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class RobustGlass extends Action {

	public enum CreateGlass {

		ROBUST_GLASS(1, new Item[] { new Item(23194) }, new Item(23193),
				Skills.CRAFTING),

		CRYSTAL_GLASS(91, new Item[] { new Item(32847) }, new Item(32845),
				Skills.CRAFTING);

		public static CreateGlass getBarByProduce(int id) {
			for (CreateGlass bar : CreateGlass.values()) {
				if (bar.getProducedBar().getId() == id)
					return bar;
			}
			return null;
		}

		public static CreateGlass getBar(int id) {
			for (CreateGlass bar : CreateGlass.values()) {
				for (Item item : bar.getItemsRequired())
					if (item.getId() == id)
						return bar;
			}
			return null;
		}

		private int levelRequired;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private CreateGlass(int levelRequired, Item[] itemsRequired,
				Item producedBar, int skillType) {
			this.levelRequired = levelRequired;
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

		public int getSkillType() {
			return skillType;
		}
	}

	public CreateGlass bar;
	public int ticks;

	public RobustGlass(CreateGlass bar, int ticks) {
		this.bar = bar;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (bar == null || player == null) {
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				bar.getItemsRequired()[0].getId(),
				bar.getItemsRequired()[0].getAmount())) {
			player.getPackets().sendEntityMessage(
					1,
					14338209,
					player,
					"You need "
							+ bar.getItemsRequired()[0].getDefinitions()
									.getName() + " to melt a "
							+ bar.getProducedBar().getDefinitions().getName()
							+ ".");
			return false;
		}
		if (player.getSkills().getLevel(bar.getSkillType()) < bar
				.getLevelRequired()) {
			player.getPackets().sendEntityMessage(
					1,
					14338209,
					player,
					"You need a Crafting level of at least "
							+ bar.getLevelRequired() + " to melt "
							+ bar.getProducedBar().getDefinitions().getName());
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (!player.getInventory().containsItem(
				bar.getItemsRequired()[0].getId(), 1))
			return false;
		WorldObject object = new WorldObject(94067, 10, 0, 2139, 3350, 1);
		WorldObject object2 = new WorldObject(67968, 10, 0, 2584, 2854, 0);
		if (player.getX() == 2582 && player.getY() == 2852)
			World.sendObjectAnimation(object2, new Animation(25041));
		else
			World.sendObjectAnimation(object, new Animation(25041));
		player.setNextAnimation(new Animation(25120));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				int amount = player.getInventory().getAmountOf(
						bar.getItemsRequired()[0].getId());
				player.getInventory().deleteItem(
						bar.getItemsRequired()[0].getId(), amount);
				player.getInventory().addItem(bar.getProducedBar().getId(),
						amount);
				stop();
			}
		}, 8, 1);
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		return -1;
	}

	@Override
	public void stop(Player player) {
		// setActionDelay(player, 3);
	}

}
