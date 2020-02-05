package net.nocturne.game.player.actions.skills.runecrafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;

public class TectonicCrafting extends Action {

	public enum CraftTectonicAction {

		TECTONIC_MASK(91, 500, new Item[] {
				new Item(ItemIdentifiers.TECTONIC_ENERGY, 14),
				new Item(ItemIdentifiers.STONE_OF_BINDING, 1) }, new Item(
				ItemIdentifiers.TECTONIC_MASK), Skills.RUNECRAFTING), TECTONIC_TOP(
				93, 1500, new Item[] {
						new Item(ItemIdentifiers.TECTONIC_ENERGY, 42),
						new Item(ItemIdentifiers.STONE_OF_BINDING, 3) },
				new Item(ItemIdentifiers.TECTONIC_ROBE_TOP),
				Skills.RUNECRAFTING), TECTONIC_BOTTOMS(92, 1000, new Item[] {
				new Item(ItemIdentifiers.TECTONIC_ENERGY, 28),
				new Item(ItemIdentifiers.STONE_OF_BINDING, 2) }, new Item(
				ItemIdentifiers.TECTONIC_ROBE_BOTTOM), Skills.RUNECRAFTING);

		public static CraftTectonicAction getBarByProduce(int id) {
			for (CraftTectonicAction craft : CraftTectonicAction.values()) {
				if (craft.getProducedBar().getId() == id)
					return craft;
			}
			return null;
		}

		public static CraftTectonicAction getBar(int id) {
			for (CraftTectonicAction craft : CraftTectonicAction.values()) {
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

		private CraftTectonicAction(int levelRequired, double experience,
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

	public CraftTectonicAction craft;
	public int ticks;

	public TectonicCrafting(CraftTectonicAction craft, int ticks) {
		this.craft = craft;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (craft == null || player == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (craft == null || player == null) {
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				craft.getItemsRequired()[0].getId(),
				craft.getItemsRequired()[0].getAmount())) {
			return false;
		}

		if (craft.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					craft.getItemsRequired()[1].getId(),
					craft.getItemsRequired()[1].getAmount())) {
				return false;
			}
		}
		if (player.getSkills().getLevel(craft.getSkillType()) < craft
				.getLevelRequired()) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		double xp = craft.getExperience();
		int amount = craft.getProducedBar().getAmount() * multiplier;
		for (Item required : craft.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(23250));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}
