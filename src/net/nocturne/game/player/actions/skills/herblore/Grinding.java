package net.nocturne.game.player.actions.skills.herblore;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

import java.util.HashMap;

/**
 * author: Tommeh
 */

public class Grinding extends Action {
	
	public enum GrindAction {

        UNICORN_HORN_DUST(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.UNICORN_HORN)}, new Item(ItemIdentifiers.UNICORN_HORN_DUST), Skills.HERBLORE),
        DRAGON_SCALE_DUST(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.BLUE_DRAGON_SCALE)}, new Item(ItemIdentifiers.DRAGON_SCALE_DUST), Skills.HERBLORE),
        CRUSHED_NEST(1, 1, 364, new Item[]{ new Item(5075)}, new Item(ItemIdentifiers.CRUSHED_NEST), Skills.HERBLORE),
        KEBBIT_TEETH_DUST(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.KEBBIT_TEETH)}, new Item(ItemIdentifiers.KEBBIT_TEETH_DUST), Skills.HERBLORE),
        GOAT_HORN_DUST(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.DESERT_GOAT_HORN)}, new Item(ItemIdentifiers.GOAT_HORN_DUST), Skills.HERBLORE),
        GROUND_MUD_RUNE(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.MUD_RUNE)}, new Item(ItemIdentifiers.GROUND_MUD_RUNES), Skills.HERBLORE),
        ANCHOVY_PASTE(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.RAW_ANCHOVIES)}, new Item(ItemIdentifiers.ANCHOVY_PASTE), Skills.HERBLORE),
        GORAK_CLAW_POWDER(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.GORAK_CLAWS)}, new Item(ItemIdentifiers.GORAK_CLAW_POWDER), Skills.HERBLORE),
        
        CHOCOLATE_DUST(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.CHOCOLATE_BAR)}, new Item(ItemIdentifiers.CHOCOLATE_DUST), Skills.COOKING),
        GROUND_GUAM(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.CLEAN_GUAM)}, new Item(ItemIdentifiers.GROUND_GUAM), Skills.COOKING),
        GROUND_SEAWEED(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.SEAWEED)}, new Item(ItemIdentifiers.GROUND_SEAWEED), Skills.COOKING),
        GROUND_CRAB_MEAT(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.CRAB_MEAT)}, new Item(ItemIdentifiers.GROUND_CRAB_MEAT), Skills.COOKING),
        GROUND_COD(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.RAW_COD)}, new Item(ItemIdentifiers.GROUND_COD), Skills.COOKING),
        GROUND_KELP(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.KELP)}, new Item(ItemIdentifiers.GROUND_KELP), Skills.COOKING),
        BREADCRUMBS(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.BREAD)}, new Item(ItemIdentifiers.BREADCRUMBS), Skills.COOKING),
        
        GUAM_TAR(19, 30, 364, new Item[]{ new Item(ItemIdentifiers.SWAMP_TAR, 15), new Item(ItemIdentifiers.CLEAN_GUAM)}, new Item(ItemIdentifiers.GUAM_TAR, 15), Skills.HERBLORE),
        MARRENTIL_TAR(31, 42.5, 364, new Item[]{ new Item(ItemIdentifiers.SWAMP_TAR, 15), new Item(ItemIdentifiers.CLEAN_MARRENTILL)}, new Item(ItemIdentifiers.MARRENTILL_TAR, 15), Skills.HERBLORE),
        TARROMIN_TAR(39, 55, 364, new Item[]{ new Item(ItemIdentifiers.SWAMP_TAR, 15), new Item(ItemIdentifiers.CLEAN_TARROMIN)}, new Item(ItemIdentifiers.TARROMIN_TAR, 15), Skills.HERBLORE),
        HARRALANDER_TAR(44, 72.5, 364, new Item[]{ new Item(ItemIdentifiers.SWAMP_TAR, 15), new Item(ItemIdentifiers.CLEAN_HARRALANDER)}, new Item(ItemIdentifiers.HARRALANDER_TAR, 15), Skills.HERBLORE),
        
        GROUND_ASHES(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.ASHES)}, new Item(ItemIdentifiers.GROUND_ASHES), Skills.HERBLORE),
        GROUND_TOOTH(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.SUQAH_TOOTH)}, new Item(ItemIdentifiers.GROUND_TOOTH), Skills.HERBLORE),
        DUST_OF_ARMADYL(1, 1, 364, new Item[]{ new Item(ItemIdentifiers.SHARDS_OF_ARMADYL)}, new Item(ItemIdentifiers.DUST_OF_ARMADYL, 8), Skills.HERBLORE);
        
        

		
		public static GrindAction getHerbByProduce(int id) {
		    for(GrindAction herb : GrindAction.values())
				if(herb.getProducedHerb().getId() == id)
				    return herb;
		    return null;
		}

		public static GrindAction getHerb(int id) {
		    for(GrindAction herb : GrindAction.values())
				for(Item item : herb.getItemsRequired())
				    if(item.getId() == id)
				    	return herb;
		    return null;
		}
		
		private static final HashMap<Integer, GrindAction> HERBLORE = new HashMap<>();
		
		static {
			for (GrindAction f : values())
				HERBLORE.put(f.getItemsRequired()[0].getId(), f);
		}
		
		public static GrindAction getHerbItem(int id) {
			return HERBLORE.get(id);
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedHerb;
		private int skillType;
		private int emote;

		private GrindAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedHerb, int skillType) {
		    this.levelRequired = levelRequired;
		    this.experience = experience;
		    this.itemsRequired = itemsRequired;
		    this.producedHerb = producedHerb;
		    this.skillType = skillType;
		    this.emote = emote;
		}

		public Item[] getItemsRequired() {
		    return itemsRequired;
		}

		public int getLevelRequired() {
		    return levelRequired;
		}
		
        
		public Item getProducedHerb() {
		    return producedHerb;
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

    public GrindAction herb;
    public Item item;
    public int ticks;
    private int xpMultiplier = 1;

    public Grinding(GrindAction herb, Item item, int ticks) {
		this.herb = herb;
		this.item = item;
		this.ticks = ticks;
    }
	

	@Override
	public boolean start(Player player) {
		if (herb == null || player == null)
		    return false;
		return true;
	}

    @Override
    public boolean process(Player player) {
		if (herb == null || player == null){
			player.getInterfaceManager().removeInterface(1251);
		    return false;
		}
		else if (!player.getInventory().containsItemToolBelt(herb.getItemsRequired()[0].getId(), herb.getItemsRequired()[0].getAmount())){
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		else if (herb.getItemsRequired().length > 1 && !player.getInventory().containsItemToolBelt(herb.getItemsRequired()[1].getId(), herb.getItemsRequired()[1].getAmount())){
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		else if (!player.getSkills().hasLevel(herb.getSkillType(), herb.getLevelRequired())){
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}  
		return true;
	}
	    
	public static GrindAction isAttaching(Player player, Item used, Item usedWith) {
    	GrindAction herb = GrindAction.getHerbItem(used.getId());
		if (herb == null)
			herb = GrindAction.getHerbItem(usedWith.getId());
		return herb;
    }

	@Override
	public int processWithDelay(Player player) {
		ticks--;			
		int multiplier = 1;	
		int emote = herb.getEmote();
		double xp = herb.getExperience();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = herb.getProducedHerb().getAmount() * multiplier ;
		for (Item required : herb.getItemsRequired())
		    player.getInventory().deleteItem(required.getId(), required.getAmount());
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, herb.getProducedHerb().getId(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(herb.getProducedHerb().getId(), amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,	herb.getProducedHerb().getId(), Skills.HERBLORE);
		player.getSkills().addXp(herb.getSkillType(), xp);
		player.setNextAnimation(new Animation(emote));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
