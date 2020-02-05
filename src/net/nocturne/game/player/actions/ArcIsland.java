package net.nocturne.game.player.actions;

import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Jan 16, 2017
 */

public class ArcIsland extends Action {
	
	public enum ArcAction {
	
		ARC_GUMBO(94, 130, -1, new Item[]{new Item(ItemIdentifiers.TORTLE_SHELL_BOWL, 1), new Item(ItemIdentifiers.BUNDLE_OF_BAMBOO, 1), new Item(ItemIdentifiers.RUMBERRY, 1), new Item(ItemIdentifiers.FISH_OIL, 1), new Item(ItemIdentifiers.SEA_SALT, 1)}, new Item(ItemIdentifiers.UNCOOKED_ARC_GUMBO), Skills.COOKING),
		BAMBOO(96, 100, 7211, new Item[]{new Item(ItemIdentifiers.BAMBOO, 5)}, new Item(ItemIdentifiers.BUNDLE_OF_BAMBOO), Skills.FLETCHING),
		RAW_TARPON(91, 120, 896, new Item[]{new Item(ItemIdentifiers.RAW_TARPON, 9)}, new Item(ItemIdentifiers.FISH_OIL), Skills.COOKING),
		RAW_SEERFISH(91, 120, 896, new Item[]{new Item(ItemIdentifiers.RAW_SEERFISH, 6)}, new Item(ItemIdentifiers.FISH_OIL), Skills.COOKING),
		RAW_SILLAGO(93, 120, 896, new Item[]{new Item(ItemIdentifiers.RAW_SILLAGO, 3)}, new Item(ItemIdentifiers.FISH_OIL), Skills.COOKING),
		SHELL_CHIPPINGS(95, 120, -1, new Item[]{new Item(ItemIdentifiers.SHELL_CHIPPINGS, 4)}, new Item(ItemIdentifiers.TORTLE_SHELL_BOWL), Skills.CRAFTING),; 
 
		public static ArcAction getByProduct(int id) {
			for(ArcAction invention : ArcAction.values())
					if(invention.getProducedItem().getId() == id)
						return invention;
			return null;
		}

		public static ArcAction getByBase(int id) {
			for(ArcAction invention : ArcAction.values())
				for(Item item : invention.getItemsRequired())
					if(item.getId() == id)
						return invention;
			return null;
		}
		
		private static final HashMap<Integer, ArcAction> arcIslandSkills = new HashMap<Integer, ArcAction>();
		
		static {
			for (ArcAction f : values()) {
				arcIslandSkills.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static ArcAction getItem(int id) {
			return arcIslandSkills.get(id);
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedItem;
		private int skillType;
		private int emote;

		private ArcAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedItem, int skillType) {
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

	public ArcAction activity;
	public int ticks;
	public Item item;

	public ArcIsland(ArcAction invention, Item item, int ticks) {
		this.activity = invention;
		this.item = item;
		this.ticks = ticks;
	}
	
	@Override
	public boolean start(Player player) {
		if (activity == null || player == null)
			return false;
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		if (activity == null || player == null)
			return false;
		if (!player.getInventory().containsItemToolBelt(activity.getItemsRequired()[0].getId(), activity.getItemsRequired()[0].getAmount()))
			return false;
		if (activity.getItemsRequired().length > 1)
			if (!player.getInventory().containsItemToolBelt(activity.getItemsRequired()[1].getId(), activity.getItemsRequired()[1].getAmount()))
				return false;
		if (!player.getSkills().hasLevel(activity.getSkillType(), activity.getLevelRequired()))
			return false;
		return true;
	}
	
	public static ArcAction isValid(Player player, Item used, Item usedWith) {
		ArcAction arcAction = null;
		arcAction = ArcAction.getItem(used.getId());
			if (arcAction == null)
				arcAction = ArcAction.getItem(usedWith.getId());
			if (arcAction != null)
				return arcAction;
		return null;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 0;	
		int emote = activity.getEmote();
		double xp = activity.getExperience();
		int amount = activity.getProducedItem().getAmount() * multiplier ;
		for (Item required : activity.getItemsRequired())
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		player.getInventory().addItem(activity.getProducedItem().getId(), amount);
		player.getSkills().addXp(activity.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));//TODO
		player.setNextGraphics(new Graphics(-1));//TODO
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}