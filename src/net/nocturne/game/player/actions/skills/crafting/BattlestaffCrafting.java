package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class BattlestaffCrafting extends Action {

	public enum CraftStaffAction {

		WATER_BATTLESTAFF(54, 100, 16448, new Item[] { new Item(571),
				new Item(1391) }, new Item(1395), Skills.CRAFTING), EARTH_BATTLESTAFF(
				58, 112.5, 16447, new Item[] { new Item(575), new Item(1391) },
				new Item(1399), Skills.CRAFTING), FIRE_BATTLESTAFF(62, 125,
				16449, new Item[] { new Item(569), new Item(1391) }, new Item(
						1393), Skills.CRAFTING), AIR_BATTLESTAFF(66, 137.5,
				16446, new Item[] { new Item(573), new Item(1391) }, new Item(
						1397), Skills.CRAFTING), ARMADYL_BATTLESTAFF(77, 150,
				16450, new Item[] { new Item(21775), new Item(1391) },
				new Item(21777), Skills.CRAFTING);

		public static CraftStaffAction getBarByProduce(int id) {
			for (CraftStaffAction craft : CraftStaffAction.values()) {
				if (craft.getProducedBar().getId() == id)
					return craft;
			}
			return null;
		}

		public static CraftStaffAction getBar(int id) {
			for (CraftStaffAction craft : CraftStaffAction.values()) {
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

		private CraftStaffAction(int levelRequired, double experience,
				int emote, Item[] itemsRequired, Item producedBar, int skillType) {
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

	    public CraftStaffAction craft;
	    public Item item;
	    public int ticks;
	    private int xpMultiplier = 1;

	public BattlestaffCrafting(CraftStaffAction craft, Item item, int ticks) {
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
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				craft.getItemsRequired()[0].getId(),
				craft.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (craft.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					craft.getItemsRequired()[1].getId(),
					craft.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
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
		int multiplier = 0;
		double xp = craft.getExperience();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = craft.getProducedBar().getAmount() * multiplier ;
		for (Item required : craft.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, craft.getProducedBar().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					craft.getProducedBar().getId(), Skills.CRAFTING);
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(craft.getEmote()));
		return 1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}
