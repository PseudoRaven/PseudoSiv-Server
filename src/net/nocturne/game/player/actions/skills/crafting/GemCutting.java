package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class GemCutting extends Action {

	public enum CraftGemAction {

		UNCUT_OPAL(1, 15, 22778, new Item[] { new Item(1625) }, new Item(1609),
				Skills.CRAFTING), UNCUT_JADE(13, 20, 22779,
				new Item[] { new Item(1627) }, new Item(1611), Skills.CRAFTING), UNCUT_RED_TOPAZ(
				16, 25, 22780, new Item[] { new Item(1629) }, new Item(1613),
				Skills.CRAFTING), UNCUT_SAPPHIRE(20, 50, 22774,
				new Item[] { new Item(1623) }, new Item(1607), Skills.CRAFTING), UNCUT_EMERALD(
				27, 67.5, 22775, new Item[] { new Item(1621) }, new Item(1605),
				Skills.CRAFTING), UNCUT_RUBY(34, 85, 22776,
				new Item[] { new Item(1619) }, new Item(1603), Skills.CRAFTING), UNCUT_DIAMOND(
				43, 107.5, 22777, new Item[] { new Item(1617) },
				new Item(1601), Skills.CRAFTING), UNCUT_DRAGONSTONE(55, 137.5,
				22781, new Item[] { new Item(1631) }, new Item(1615),
				Skills.CRAFTING), UNCUT_ONYX(67, 167.5, 22782,
				new Item[] { new Item(6571) }, new Item(6573), Skills.CRAFTING), UNCUT_HYDRIX(
				79, 197.5, 24309, new Item[] { new Item(31853) }, new Item(
						31855), Skills.CRAFTING),

		WOLFBANE_ARROWHEADS(5, 4, -1, new Item[] { new Item(2859) }, new Item(
				2861, 4), Skills.FLETCHING), OPAL_BOLT_TIPS(11, 1.5, 22778,
				new Item[] { new Item(1609) }, new Item(45, 12),
				Skills.FLETCHING), JADE_BOLT_TIPS(26, 2.4, 22779,
				new Item[] { new Item(1611) }, new Item(9187, 12),
				Skills.FLETCHING), PEARL_BOLT_TIPSX6(41, 3.2, 886,
				new Item[] { new Item(411) }, new Item(46, 6), Skills.FLETCHING), PEARL_BOLT_TIPSX24(
				41, 3.2, 886, new Item[] { new Item(411) }, new Item(46, 24),
				Skills.FLETCHING), RED_TOPAZ_BOLT_TIPS(48, 3.9, 22780,
				new Item[] { new Item(1613) }, new Item(9188, 12),
				Skills.FLETCHING), SAPPHIRE_BOLT_TIPS(56, 4.7, 22774,
				new Item[] { new Item(1607) }, new Item(9189, 12),
				Skills.FLETCHING), EMERALD_BOLT_TIPS(58, 5.5, 22775,
				new Item[] { new Item(1605) }, new Item(9190, 12),
				Skills.FLETCHING), RUBY_BOLT_TIPS(63, 6.3, 22776,
				new Item[] { new Item(1603) }, new Item(9191, 12),
				Skills.FLETCHING), DIAMOND_BOLT_TIPS(65, 7, 22777,
				new Item[] { new Item(1601) }, new Item(9192, 12),
				Skills.FLETCHING), DRAGON_BOLT_TIPS(71, 8.2, 22781,
				new Item[] { new Item(1615) }, new Item(9193, 12),
				Skills.FLETCHING), ONYX_BOLT_TIPS(73, 9.4, 22782,
				new Item[] { new Item(6571) }, new Item(9194, 24),
				Skills.FLETCHING), HYDRIX_BOLT_TIPS(80, 10.6, 22782,
				new Item[] { new Item(31855) }, new Item(31867, 24),
				Skills.FLETCHING);

		public static CraftGemAction getBarByProduce(int id) {
			for (CraftGemAction craft : CraftGemAction.values()) {
				if (craft.getProducedBar().getId() == id)
					return craft;
			}
			return null;
		}

		public static CraftGemAction getBar(int id) {
			for (CraftGemAction craft : CraftGemAction.values()) {
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
		private int emote;

		private CraftGemAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedBar, int skillType) {
		    this.levelRequired = levelRequired;
		    this.experience = experience;
		    this.emote = emote;
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

		public int getEmote() {
			return emote;
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

	    public CraftGemAction craft;
	    public Item item;
	    public int ticks;
		private int xpMultiplier = 1;

	public GemCutting(CraftGemAction craft, Item item, int ticks) {
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
		if (!player.getInventory().containsItemToolBelt(craft.getItemsRequired()[0].getId(), craft.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (craft.getItemsRequired().length > 1) {
		    if (!player.getInventory().containsItemToolBelt(craft.getItemsRequired()[1].getId(), craft.getItemsRequired()[1].getAmount())) {
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
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a Crafting level of " + craft.getLevelRequired()
							+ " to cut that gem.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 1;	
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		double xp = craft.getExperience();
		int amount = craft.getProducedBar().getAmount() * multiplier;
		for (Item required : craft.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(craft.getEmote()));
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, craft.getProducedBar().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		if (craft == CraftGemAction.UNCUT_DIAMOND)
			player.getCompCapeManager().increaseRequirement(
					Requirement.GEM_CUTTING, 1);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					craft.getItemsRequired()[0].getId(), Skills.CRAFTING);

		return 1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}