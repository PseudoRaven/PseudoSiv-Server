package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class GlassBlowing extends Action {

	public enum CraftGlassAction {

		BEER_GLASS(1, 17.5, new Item[] { new Item(1775) }, new Item(1919),
				Skills.CRAFTING), CANDLE_LANTERN(4, 19, new Item[] { new Item(
				1775) }, new Item(4527), Skills.CRAFTING), OIL_LAMP_EMPTY(12,
				25, new Item[] { new Item(1741) }, new Item(4525),
				Skills.CRAFTING), VIAL(33, 35, new Item[] { new Item(1775) },
				new Item(9085), Skills.CRAFTING), FISHBOWL(42, 42.5,
				new Item[] { new Item(1775) }, new Item(6667), Skills.CRAFTING), UNPOWERED_ORB(
				46, 52.5, new Item[] { new Item(1775) }, new Item(567),
				Skills.CRAFTING), LANTERN_LENS(49, 55, new Item[] { new Item(
				1775) }, new Item(4542), Skills.CRAFTING), EMPTY_LIGHT_ORB(87,
				70, new Item[] { new Item(1775) }, new Item(10980),
				Skills.CRAFTING), LIGHT_ORB(87, 70,
				new Item[] { new Item(1775) }, new Item(10973), Skills.CRAFTING), POTION_FLASK(
				89, 100, new Item[] { new Item(23193) }, new Item(23191),
				Skills.CRAFTING), CRYSTAL_FLASK(89, 150, new Item[] { new Item(
				32845) }, new Item(32843), Skills.CRAFTING);

		public static CraftGlassAction getBarByProduce(int id) {
			for (CraftGlassAction craft : CraftGlassAction.values()) {
				if (craft.getProducedBar().getId() == id)
					return craft;
			}
			return null;
		}

		public static CraftGlassAction getBar(int id) {
			for (CraftGlassAction craft : CraftGlassAction.values()) {
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

		private CraftGlassAction(int levelRequired, double experience,
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

	    public CraftGlassAction craft;
	    public Item item;
	    public int ticks;
	    private int xpMultiplier = 1;

	public GlassBlowing(CraftGlassAction craft, Item item, int ticks) {
		this.craft = craft;
		this.item = item;
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
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(craft.getItemsRequired()[0].getId(),
				craft.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (craft.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(craft.getItemsRequired()[1].getId(),
					craft.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
			return false;
			}
		}
		if (player.getSkills().getLevel(craft.getSkillType()) < craft.getLevelRequired()) {
			player.getInterfaceManager().removeInterface(1251);
			if (!player.getInventory().containsItemToolBelt(
					craft.getItemsRequired()[1].getId(),
					craft.getItemsRequired()[1].getAmount())) {
				return false;
			}
		}
		if (player.getSkills().getLevel(craft.getSkillType()) < craft
				.getLevelRequired()) {
			player.getPackets().sendGameMessage(
					"You need a Crafting level of " + craft.getLevelRequired()
							+ " to create this.");
			return false;
		}
		return true;

	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		double xp = craft.getExperience();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = craft.getProducedBar().getAmount() * multiplier ;
		for (Item required : craft.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (craft.toString().toLowerCase().contains("crystal"))
			player.getCompCapeManager().increaseRequirement(
					Requirement.CRYSTAL_FLASK, 1);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,	craft.getProducedBar().getId(), Skills.CRAFTING);
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, craft.getProducedBar().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
			player.getDailyTask().incrementTask(player, 3,
					craft.getProducedBar().getId(), Skills.CRAFTING);
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(24890));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 50);

	}

}
