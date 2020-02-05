package net.nocturne.game.player.actions.skills.fletching;
 
import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;
 
public class BoltFletching extends Action {
   
    public enum FletchBoltAction {
        
    	
    	HEADLESS_ARROW(1, 15, 19516, new Item[]{ new Item(52, 15), new Item (314, 15)}, new Item(53, 15), Skills.FLETCHING),
    	FLIGHTED_OGRE_ARROW(5, 5.4, -1, new Item[]{ new Item(2864, 15), new Item (314, 15)}, new Item(2865, 15), Skills.FLETCHING),
    	
    	BRONZE_BOLT(9, 5, -1, new Item[]{ new Item(9375, 15), new Item (314, 15)}, new Item(877, 15), Skills.FLETCHING),
    	BLURITE_BOLT(24, 10, -1, new Item[]{ new Item(9376, 15), new Item (314, 15)}, new Item(9139, 10), Skills.FLETCHING),
    	IRON_BOLT(39, 15, -1, new Item[]{ new Item(9377, 15), new Item (314, 15)}, new Item(9140, 15), Skills.FLETCHING),
    	SILVER_BOLT(43, 25, -1, new Item[]{ new Item(9382, 15), new Item (314, 15)}, new Item(9145, 15), Skills.FLETCHING),
    	STEEL_BOLT(46, 35, -1, new Item[]{ new Item(9378, 15), new Item (314, 15)}, new Item(9141, 15), Skills.FLETCHING),
        MITHRIL_BOLT(54, 50, -1, new Item[]{ new Item(9379, 15), new Item (314, 15)}, new Item(9142, 15), Skills.FLETCHING),
        BROAD_TIPPED_BOLT(55, 3, -1, new Item[]{ new Item(13279, 15), new Item (314, 15)}, new Item(13280, 15), Skills.FLETCHING),
        ADAMANT_BOLT(61, 70, -1, new Item[]{ new Item(9380, 15), new Item (314, 15)}, new Item(9143, 15), Skills.FLETCHING),
    	RUNITE_BOLT(69, 100, -1, new Item[]{ new Item(9381, 15), new Item (314, 15)}, new Item(9144, 15), Skills.FLETCHING),
    	DRAGONBANE_BOLT(80, 70, -1, new Item[]{ new Item(21843, 15), new Item (314)}, new Item(21660, 15), Skills.FLETCHING),
    	WALLASALKIBANE_BOLT(80, 70, -1, new Item[]{ new Item(21853, 15), new Item (314)}, new Item(21665, 15), Skills.FLETCHING),
    	BASILISKBANE_BOLT(80, 70, -1, new Item[]{ new Item(21848, 15), new Item (314)}, new Item(21670, 15), Skills.FLETCHING),
    	ABYSSALBANE_BOLT(80, 70, -1, new Item[]{ new Item(21858, 15), new Item (314)}, new Item(21675, 15), Skills.FLETCHING),
    	
    	BRONZE_DART(11, 18, -1, new Item[]{ new Item(819, 15), new Item (314, 15)}, new Item(806, 15), Skills.FLETCHING),
    	OFFHAND_BRONZE_DART(11, 18, -1, new Item[]{ new Item(819, 15), new Item (314, 15)}, new Item(25909, 15), Skills.FLETCHING),
    	IRON_DART(22, 38, -1, new Item[]{ new Item(820, 15), new Item (314, 15)}, new Item(807, 15), Skills.FLETCHING),
    	OFFHAND_IRON_DART(22, 38, -1, new Item[]{ new Item(820, 15), new Item (314, 15)}, new Item(25910, 15), Skills.FLETCHING),
    	STEEL_DART(37, 75, -1, new Item[]{ new Item(821, 15), new Item (314, 15)}, new Item(808, 15), Skills.FLETCHING),
    	OFFHAND_STEEL_DART(31, 75, -1, new Item[]{ new Item(821, 15), new Item (314, 15)}, new Item(25911, 15), Skills.FLETCHING),
    	MITHRIL_DART(52, 112, -1, new Item[]{ new Item(822, 15), new Item (314, 15)}, new Item(809, 15), Skills.FLETCHING),
    	OFFHAND_MITHRIL_DART(52, 117, -1, new Item[]{ new Item(822, 15), new Item (314, 15)}, new Item(25913, 15), Skills.FLETCHING),
    	ADAMANT_DART(67, 150, -1, new Item[]{ new Item(823, 15), new Item (314, 15)}, new Item(810, 15), Skills.FLETCHING),
    	OFFHAND_ADAMANT_DART(67, 150, -1, new Item[]{ new Item(823, 15), new Item (314, 15)}, new Item(25914, 15), Skills.FLETCHING),
    	RUNITE_DART(81, 188, -1, new Item[]{ new Item(824, 15), new Item (314, 15)}, new Item(811, 15), Skills.FLETCHING),
    	OFFHAND_RUNITE_DART(81, 188, -1, new Item[]{ new Item(824, 15), new Item (314, 15)}, new Item(25915, 15), Skills.FLETCHING),
    	DRAGON_DART(95, 250, -1, new Item[]{ new Item(11232, 15), new Item (314, 15)}, new Item(11230, 15), Skills.FLETCHING),
    	OFFHAND_DRAGON_DART(95, 250, -1, new Item[]{ new Item(11232, 15), new Item (314, 15)}, new Item(25916, 15), Skills.FLETCHING);
    	
       
       
 
       
 
        public static FletchBoltAction getBarByProduce(int id) {
            for(FletchBoltAction fletch : FletchBoltAction.values()) {
            if(fletch.getProducedBow().getId() == id)
                return fletch;
            }
            return null;
        }
 
        public static FletchBoltAction getBar(int id) {
            for(FletchBoltAction fletch : FletchBoltAction.values()) {
            for(Item item : fletch.getItemsRequired())
                if(item.getId() == id)
                return fletch;
            }
            return null;
        }
        
		private static final HashMap<Integer, FletchBoltAction> FLETCHSTUFF = new HashMap<Integer, FletchBoltAction>();
		
		static {
			for (FletchBoltAction f : values()) {
				FLETCHSTUFF.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static FletchBoltAction getFletchItem(int id) {
			return FLETCHSTUFF.get(id);
		}
 
        private int levelRequired;
        private double experience;
        private int emote;
        private Item[] itemsRequired;
        private Item producedBow;
        private int skillType;
 
        private FletchBoltAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedBow, int skillType) {
            this.levelRequired = levelRequired;
            this.experience = experience;
            this.emote = emote;
            this.itemsRequired = itemsRequired;
            this.producedBow = producedBow;
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
       
        public Item getProducedBow() {
            return producedBow;
        }
       
        public double getExperience() {
            return experience;
        }
 
        public int getSkillType() {
            return skillType;
        }
       
        }
 
        public FletchBoltAction fletch;
        public Item item;
        public int ticks;
        private int xpMultiplier = 1;
 
        public BoltFletching(FletchBoltAction fletch, Item item, int ticks) {
        this.fletch = fletch;
        this.item = item;
        this.ticks = ticks;
        }
       
 
    @Override
    public boolean start(Player player) {
        if (fletch == null || player == null) {
            return false;
        }
        if (ticks > 10)
        	ticks = 10;
        
        int bitValue = player.getVarsManager().getBitValue(9065);
        if (fletch == fletch.BROAD_TIPPED_BOLT  && bitValue != 1){
        	player.getPackets().sendGameMessage("You need to learn how to make these from a slayer master first.");
        	return false;
        }
        return true;
        }
 
        @Override
        public boolean process(Player player) {
        if (fletch == null || player == null) {
        	player.getInterfaceManager().removeInterface(1251);
            return false;
        }
        if (!player.getInventory().containsItemToolBelt(fletch.getItemsRequired()[0].getId(), fletch.getItemsRequired()[0].getAmount())) {
        	player.getInterfaceManager().removeInterface(1251);
        	return false;
        }
 
 
        if (fletch.getItemsRequired().length > 1) {
            if (!player.getInventory().containsItemToolBelt(fletch.getItemsRequired()[1].getId(), fletch.getItemsRequired()[1].getAmount())) {
            	player.getInterfaceManager().removeInterface(1251);
            	return false;
            }
        }
        if (player.getSkills().getLevel(fletch.getSkillType()) < fletch.getLevelRequired()) {
        	player.getInterfaceManager().removeInterface(1251);
        	return false;
        }
        if (ticks <= 0) {
        	player.getInterfaceManager().removeInterface(1251);
        	return false;
        }
        return true;
        }
       
        public static FletchBoltAction isAttaching(Player player, Item used, Item usedWith) {
        	FletchBoltAction fletch = null;
        	fletch = FletchBoltAction.getFletchItem(used.getId());
        	Item selected;
    		if (fletch != null)
    			selected = usedWith;
    		else {
    			fletch = FletchBoltAction.getFletchItem(usedWith.getId());
    			selected = used;
    		}
    		if (fletch != null)
    			return fletch;
    		else
    		return null;
              }
        
    @Override
    public int processWithDelay(Player player) {
        ticks--;           
        int multiplier = 1;
        int emote = fletch.getEmote();
        double xp = fletch.getExperience();
        int maxQuantity = player.getVarsManager().getBitValue(1002);
        int amount = fletch.getProducedBow().getAmount() * multiplier ;
        for (Item required : fletch.getItemsRequired()) {
            player.getInventory().deleteItem(required.getId(), required.getAmount());
        }
        player.getInventory().addItem(fletch.getProducedBow().getId(), amount);
        if (maxQuantity > 1){
        SkillsDialogue.sendProgressBar(player, fletch.getProducedBow().getId(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
        }
        if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,	fletch.getProducedBow().getId(), Skills.FLETCHING);
        player.getSkills().addXp(fletch.getSkillType(), xp);
        player.setNextAnimation(new Animation(emote));
        return 0;
    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}