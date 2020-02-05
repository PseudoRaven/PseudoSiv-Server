package net.nocturne.game.player.actions.skills.invention;

import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;

/**
 * @author Tommeh
 */

public class Invention extends Action {

	public enum InventionAction {

		CHARGE_PACK(1, 1, 1, new Item[] { new Item(36719) }, new Item(36719),
				Skills.INVENTION);

		public static InventionAction getByProduct(int id) {
			for (InventionAction invention : InventionAction.values())
				if (invention.getProducedItem().getId() == id)
					return invention;
			return null;
		}

		public static InventionAction getByBase(int id) {
			for (InventionAction invention : InventionAction.values())
				for (Item item : invention.getItemsRequired())
					if (item.getId() == id)
						return invention;
			return null;
		}

		private static final HashMap<Integer, InventionAction> INVENTION = new HashMap<Integer, InventionAction>();

		static {
			for (InventionAction f : values()) {
				INVENTION.put(f.getItemsRequired()[0].getId(), f);
			}
		}

		public static InventionAction getItem(int id) {
			return INVENTION.get(id);
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedItem;
		private int skillType;
		private int emote;

		private InventionAction(int levelRequired, double experience,
				int emote, Item[] itemsRequired, Item producedItem,
				int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedItem = producedItem;
			this.skillType = skillType;
			this.emote = emote;
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedItem() {
			return producedItem;
		}

		public double getExperience() {
			return experience;
		}

		public int getSkillType() {
			return skillType;
		}

		public int getEmote() {
			return emote;
		}
	}

	public InventionAction invention;
	public int ticks;
	public Item item;

	public Invention(InventionAction invention, Item item, int ticks) {
		this.invention = invention;
		this.item = item;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (invention == null || player == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (invention == null || player == null) {
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				invention.getItemsRequired()[0].getId(),
				invention.getItemsRequired()[0].getAmount())) {
			return false;
		}
		if (invention.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					invention.getItemsRequired()[1].getId(),
					invention.getItemsRequired()[1].getAmount())) {
				return false;
			}
		}
		if (!player.getSkills().hasLevel(invention.getSkillType(),
				invention.getLevelRequired()))
			return false;
		return true;
	}

	public static InventionAction isMixing(Player player, Item used,
			Item usedWith) {
		InventionAction invention = null;
		invention = InventionAction.getItem(used.getId());
		if (invention == null)
			invention = InventionAction.getItem(usedWith.getId());
		if (invention != null)
			return invention;
		return null;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		int emote = invention.getEmote();
		double xp = invention.getExperience();
		int amount = invention.getProducedItem().getAmount() * multiplier;
		for (Item required : invention.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		player.getInventory().addItem(invention.getProducedItem().getId(),
				amount);
		player.getSkills().addXp(invention.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));// TODO
		player.setNextGraphics(new Graphics(-1));// TODO
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}
