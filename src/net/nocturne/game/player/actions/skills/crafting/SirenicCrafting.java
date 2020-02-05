package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class SirenicCrafting extends Action {
	
	public enum CraftSirenicAction {

	
		SIRENIC_MASK(91, 500, new Item[]
				{ new Item(ItemIdentifiers.SIRENIC_SCALE, 14), new Item(ItemIdentifiers.ALGARUM_THREAD, 1)}, new Item(ItemIdentifiers.SIRENIC_MASK), Skills.CRAFTING),
	    SIRENIC_HAUBERK(93, 1500, new Item[]
				{ new Item(ItemIdentifiers.SIRENIC_SCALE, 42), new Item(ItemIdentifiers.ALGARUM_THREAD, 3)}, new Item(ItemIdentifiers.SIRENIC_HAUBERK), Skills.CRAFTING),
		SIRENIC_CHAPS(92, 1000, new Item[]
				{ new Item(ItemIdentifiers.SIRENIC_SCALE, 28), new Item(ItemIdentifiers.ALGARUM_THREAD, 2)}, new Item(ItemIdentifiers.SIRENIC_CHAPS), Skills.CRAFTING);
			
		public static CraftSirenicAction getBarByProduce(int id) {
		    for(CraftSirenicAction craft : CraftSirenicAction.values()) {
			if(craft.getProducedBar().getId() == id)
			    return craft;
		    }
		    return null;
		}

		public static CraftSirenicAction getBar(int id) {
		    for(CraftSirenicAction craft : CraftSirenicAction.values()) {
			for(Item item : craft.getItemsRequired())
			    if(item.getId() == id)
				return craft;
		    }
		    return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private CraftSirenicAction(int levelRequired, double experience, Item[] itemsRequired, Item producedBar, int skillType) {
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

	    public CraftSirenicAction craft;
	    public int ticks;
	    private int xpMultiplier = 1;

	    public SirenicCrafting(CraftSirenicAction craft, int ticks) {
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
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 0;	
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		double xp = craft.getExperience();
		int amount = craft.getProducedBar().getAmount() * multiplier ;
		for (Item required : craft.getItemsRequired()) {
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, craft.getProducedBar().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(craft.getProducedBar().getId(), amount);
		player.getSkills().addXp(craft.getSkillType(), xp);
		player.setNextAnimation(new Animation(25594));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}
