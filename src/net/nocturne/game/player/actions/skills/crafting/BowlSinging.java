package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class BowlSinging extends Action {
	
	public enum CreateCrystal {

		CRYSAL_HALBERD(75, 750, new Item[]
			{ new Item(32206)}, new Item(32219), Skills.SMITHING),
		CRYSAL_DAGGER(75, 375, new Item[]
			{ new Item(32206)}, new Item(32222), Skills.SMITHING),
		CRYSAL_OFFHAND_DAGGER(75, 375, new Item[]
			{ new Item(32206)}, new Item(32225), Skills.SMITHING),
		CRYSAL_BOW(75, 750, new Item[]
			{ new Item(32206)}, new Item(32228), Skills.SMITHING),
		CRYSAL_CHAKRAM(75, 375, new Item[]
			{ new Item(32206)}, new Item(32231), Skills.SMITHING),
		CRYSAL_OFFHAND_CHAKRAM(75, 375, new Item[]
			{ new Item(32206)}, new Item(32234), Skills.SMITHING),
		CRYSAL_STAFF(75, 750, new Item[]
			{ new Item(32206)}, new Item(32210), Skills.SMITHING),
		CRYSAL_WAND(75, 375, new Item[]
			{ new Item(32206)}, new Item(32213), Skills.SMITHING),
		CRYSAL_ORB(75, 375, new Item[]
			{ new Item(32206)}, new Item(32216), Skills.SMITHING),
		CRYSAL_SHIELD(75, 750, new Item[]
			{ new Item(32623)}, new Item(32240), Skills.SMITHING),
		CRYSAL_DEFLECTOR(75, 375, new Item[]
			{ new Item(32623)}, new Item(32243), Skills.SMITHING),
		CRYSAL_WARD(75, 375, new Item[]
			{ new Item(32623)}, new Item(32637), Skills.SMITHING),
		CRYSAL_SAW(80, 150, new Item[]
			{ new Item(32208)}, new Item(32633), Skills.SMITHING),
		CRYSAL_KNIFE(80, 150, new Item[]
			{ new Item(32208)}, new Item(32635), Skills.SMITHING),
		CRYSAL_TINDERBOX(80, 150, new Item[]
			{ new Item(32208)}, new Item(32637), Skills.SMITHING),
		CRYSAL_HAMMER(80, 150, new Item[]
			{ new Item(32208)}, new Item(32640), Skills.SMITHING),
		CRYSAL_CHISEL(80, 150, new Item[]
			{ new Item(32208)}, new Item(32642), Skills.SMITHING),
		CRYSAL_CHIMES(80, 150, new Item[]
			{ new Item(32208)}, new Item(32644), Skills.SMITHING),
		CRYSAL_TELEPORT_SEED(80, 150, new Item[]
			{ new Item(6103)}, new Item(6099), Skills.SMITHING),
		ATTUNED_CRYSTAL_HALBERD(90, 2000, new Item[]
			{ new Item(32625)}, new Item(32647), Skills.SMITHING),
		ATTUNED_CRYSTAL_DAGGER(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32649), Skills.SMITHING),
		OFFHAND_CRYSTAL_DAGGER(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32651), Skills.SMITHING),
		ATTUNED_CRYSTAL_BOW(90, 2000, new Item[]
			{ new Item(32625)}, new Item(32653), Skills.SMITHING),
		ATTUNED_CRYSTAL_CHAKRAM(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32655), Skills.SMITHING),
		OFFHAND_CRYSTAL_CHAKRAM(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32657), Skills.SMITHING),
		ATTUNED_CRYSTAL_STAFF(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32659), Skills.SMITHING),
		ATTUNED_CRYSTAL_WAND(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32661), Skills.SMITHING),
		ATTUNED_CRYSTAL_ORB(90, 1000, new Item[]
			{ new Item(32625)}, new Item(32663), Skills.SMITHING),
		ATTUNED_CRYSTAL_SHIELD(90, 1000, new Item[]
			{ new Item(32626)}, new Item(32627), Skills.SMITHING),
		ATTUNED_CRYSTAL_DEFLECTOR(90, 1000, new Item[]
			{ new Item(32626)}, new Item(32629), Skills.SMITHING),
		ATTUNED_CRYSTAL_WARD(90, 1000, new Item[]
			{ new Item(32626)}, new Item(32631), Skills.SMITHING);
		
		

		

		public static CreateCrystal getBarByProduce(int id) {
		    for(CreateCrystal crystal : CreateCrystal.values()) {
			if(crystal.getProducedBar().getId() == id)
			    return crystal;
		    }
		    return null;
		}

		public static CreateCrystal getBar(int id) {
		    for(CreateCrystal crystal : CreateCrystal.values()) {
			for(Item item : crystal.getItemsRequired())
			    if(item.getId() == id)
				return crystal;
		    }
		    return null;
		}
		
		public static CreateCrystal getBar(Player player) {
			for (CreateCrystal crystal : CreateCrystal.values()) {
				for (Item item : crystal.getItemsRequired())
					if (!player.getInventory().containsItems(
							new Item(item.getId())))
						return crystal;
			}
			return null;
		}

		private int levelRequired;
		private int cost;
		private Item[] itemsRequired;
		private Item producedBar;
		private int skillType;

		private CreateCrystal(int levelRequired, int cost, Item[] itemsRequired, Item producedBar, int skillType) {
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

	    public CreateCrystal crystal;
	    public WorldObject object;
	    public int ticks;
	    private int xpMultiplier = 1;

	    public BowlSinging(CreateCrystal crystal, WorldObject object, int ticks) {
		this.crystal = crystal;
		this.object = object;
		this.ticks = ticks;
	    }

	    @Override
	    public boolean start(Player player) {
		if (crystal == null || player == null) {
		    return false;
		}
		return true;
	    }

	    @Override
	    public boolean process(Player player) {
		if (crystal == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
		    return false;
		}
		if (!player.getInventory().containsItemToolBelt(crystal.getItemsRequired()[0].getId(), crystal.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (crystal.getItemsRequired().length > 1) {
		    if (!player.getInventory().containsItemToolBelt(crystal.getItemsRequired()[1].getId(), crystal.getItemsRequired()[1].getAmount())) {
		    	player.getInterfaceManager().removeInterface(1251);
		    	return false;
		    }
		}
		if (player.getSkills().getLevel(crystal.getSkillType()) < crystal.getLevelRequired()) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		return true;
	    }
		  
		
	    



	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 1;	
		int cost = crystal.getCost();
		int xp = cost;
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = crystal.getProducedBar().getAmount() * multiplier ;
		for (Item required : crystal.getItemsRequired()) {
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1){
			SkillsDialogue.sendProgressBar(player, crystal.getProducedBar().getId(), maxQuantity, (int) xp);
			player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
			xpMultiplier++;
		}
		player.getInventory().addItem(crystal.getProducedBar().getId(), amount);
		player.getInventory().deleteItem(32622, cost);
		player.getSkills().addXp(Skills.CRAFTING, xp);
	    player.setNextAnimation(new Animation(25031));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
		
	}

}