package net.nocturne.game.player.actions.skills.dungeoneering.skills;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;

public class DungeoneeringRunecrafting extends Action {

	public enum ImbueAction {
		AIR_RUNE(1, 0.5, 13659, 2571, new Item[] { new Item(17776, 5) },
				new Item(17780, 5), Skills.RUNECRAFTING), MIND_RUNE(2, 0.5,
				13659, 2571, new Item[] { new Item(17776, 5) }, new Item(17784,
						5), Skills.RUNECRAFTING), WATER_RUNE(5, 0.6, 13659,
				2571, new Item[] { new Item(17776, 5) }, new Item(17781, 5),
				Skills.RUNECRAFTING), EARTH_RUNE(9, 0.6, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17782, 5),
				Skills.RUNECRAFTING), FIRE_RUNE(14, 0.7, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17783, 5),
				Skills.RUNECRAFTING), BODY_RUNE(20, 0.7, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17788, 5),
				Skills.RUNECRAFTING), COSMIC_RUNE(27, 0.8, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17789, 5),
				Skills.RUNECRAFTING), CHAOS_RUNE(35, 0.8, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17785, 5),
				Skills.RUNECRAFTING), ASTRAL_RUNE(40, 0.8, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17790, 5),
				Skills.RUNECRAFTING), NATURE_RUNE(44, 0.9, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17791, 5),
				Skills.RUNECRAFTING), LAW_RUNE(54, 0.9, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17792, 5),
				Skills.RUNECRAFTING), DEATH_RUNE(65, 1.0, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17786, 5),
				Skills.RUNECRAFTING), BLOOD_RUNE(77, 1.0, 13659, 2571,
				new Item[] { new Item(17776, 5) }, new Item(17787, 5),
				Skills.RUNECRAFTING), CATALYTIC_WAND(65, 1.0, 13662, -1,
				new Item[] { new Item(27695) }, new Item(27707),
				Skills.RUNECRAFTING), CATALYTIC_ORB(65, 1.0, 13662, -1,
				new Item[] { new Item(27945) }, new Item(27957),
				Skills.RUNECRAFTING), CATALYTIC_STAFF(65, 1.0, 13662, -1,
				new Item[] { new Item(16985) }, new Item(17013),
				Skills.RUNECRAFTING), EMPOWERED_CATALYTIC_WAND(65, 1.0, 13662,
				-1, new Item[] { new Item(27705) }, new Item(27709),
				Skills.RUNECRAFTING), EMPOWERED_CATALYTIC_ORB(65, 1.0, 13662,
				-1, new Item[] { new Item(27955) }, new Item(27959),
				Skills.RUNECRAFTING), EMPORED_CATALYTIC_STAFF(65, 1.0, 13662,
				-1, new Item[] { new Item(16995) }, new Item(17015),
				Skills.RUNECRAFTING);

		public static ImbueAction getBarByProduce(int id) {
			for (ImbueAction cape : ImbueAction.values()) {
				if (cape.getProducedBar().getId() == id)
					return cape;
			}
			return null;
		}

		public static ImbueAction getBar(int id) {
			for (ImbueAction cape : ImbueAction.values()) {
				for (Item item : cape.getItemsRequired())
					if (item.getId() == id)
						return cape;
			}
			return null;
		}

		public static ImbueAction getBar(Player player) {
			for (ImbueAction imbue : ImbueAction.values()) {
				for (Item item : imbue.getItemsRequired())
					if (!player.getInventory().containsItems(
							new Item(item.getId())))
						return imbue;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;
		private int emoteId;
		private int graphicsId;

		private ImbueAction(int levelRequired, double experience, int emoteId,
				int graphicsId, Item[] itemsRequired, Item producedBar,
				int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.skillType = skillType;
			this.emoteId = emoteId;
			this.graphicsId = graphicsId;
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

		public int getEmote() {
			return emoteId;
		}

		public int getGraphics() {
			return graphicsId;
		}
	}

	public ImbueAction imbue;
	public int ticks;

	public DungeoneeringRunecrafting(ImbueAction cape, int ticks) {
		this.imbue = cape;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (imbue == null || player == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (imbue == null || player == null) {
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				imbue.getItemsRequired()[0].getId(),
				imbue.getItemsRequired()[0].getAmount())) {
			return false;
		}
		if (imbue.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					imbue.getItemsRequired()[1].getId(),
					imbue.getItemsRequired()[1].getAmount())) {
				return false;
			}
		}
		if (!player.getSkills().hasLevel(imbue.getSkillType(),
				imbue.getLevelRequired())) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 1;
		double xp = imbue.getExperience();
		int graphics = imbue.getGraphics();
		int emote = imbue.getEmote();
		int amount = imbue.getProducedBar().getAmount() * multiplier;
		for (Item required : imbue.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		player.getInventory().addItem(imbue.getProducedBar().getId(), amount);
		player.getSkills().addXp(imbue.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));
		player.setNextGraphics(new Graphics(graphics));

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
