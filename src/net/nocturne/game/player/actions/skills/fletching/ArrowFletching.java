package net.nocturne.game.player.actions.skills.fletching;
 
import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;
 
public class ArrowFletching extends Action {
   
    public enum FletchArrowAction {
        
 
    	BRONZE_ARROW(1, 19.5, -1, new Item[]{ new Item(39, 15), new Item (53, 15)}, new Item(882, 15), Skills.FLETCHING),
    	IRON_ARROW(15, 37.5, -1, new Item[]{ new Item(40, 15), new Item (53, 15)}, new Item(884, 15), Skills.FLETCHING),
    	STEEL_ARROW(30, 75, -1, new Item[]{ new Item(41, 15), new Item (53, 15)}, new Item(886, 15), Skills.FLETCHING),
    	MITHRIL_ARROW(45, 112.5, -1, new Item[]{ new Item(42, 15), new Item (53, 15)}, new Item(888, 15), Skills.FLETCHING),
    	BROAD_ARROW(52, 225, -1, new Item[]{ new Item(13278, 15), new Item (53, 15)}, new Item(4160, 15), Skills.FLETCHING),
    	ADAMANT_ARROW(60, 150, -1, new Item[]{ new Item(43, 15), new Item (53, 15)}, new Item(890, 15), Skills.FLETCHING),
    	RUNE_ARROW(75, 187.5, -1, new Item[]{ new Item(44, 15), new Item (53, 15)}, new Item(892, 15), Skills.FLETCHING),
    	DRAGONBANE_ARROW(76, 187.5, -1, new Item[]{ new Item(21823, 15), new Item (53, 15)}, new Item(21640, 15), Skills.FLETCHING),
    	BASILISKBANE_ARROW(76, 187.5, -1, new Item[]{ new Item(21833, 15), new Item (53, 15)}, new Item(21650, 15), Skills.FLETCHING),
    	WALLASALKIBANE_ARROW(76, 187.5, -1, new Item[]{ new Item(21828, 15), new Item (53, 15)}, new Item(21645, 15), Skills.FLETCHING),
    	ABYSSALBANE_ARROW(76, 187.5, -1, new Item[]{ new Item(21838, 15), new Item (53, 15)}, new Item(21655, 15), Skills.FLETCHING),
    	DRAGON_ARROW(90, 225, -1, new Item[]{ new Item(11237, 15), new Item (53, 15)}, new Item(11212, 15), Skills.FLETCHING),
    	DARK_ARROW(95, 262.5, -1, new Item[]{ new Item(29729, 15), new Item (53, 15)}, new Item(29617, 15), Skills.FLETCHING),
    	FRAGMENT_ARROW(60, 7.5, -1, new Item[]{ new Item(28435, 15), new Item (53, 15)}, new Item(28464, 15), Skills.FLETCHING),
    	
    	OGRE_ARROW(5, 6, -1, new Item[]{ new Item(4819, 15), new Item (2865, 15)}, new Item(2866, 15), Skills.FLETCHING),
    	BRONZE_BRUTAL_ARROW(7, 8.4, -1, new Item[]{ new Item(4820, 15), new Item (2865, 15)}, new Item(4773, 15), Skills.FLETCHING),
    	IRON_BRUTAL_ARROW(18, 15.6, -1, new Item[]{ new Item(4821, 15), new Item (2865, 15)}, new Item(4778, 15), Skills.FLETCHING),
    	STEEL_BRUTAL_ARROW(33, 30.6, -1, new Item[]{ new Item(1539, 15), new Item (2865, 15)}, new Item(4783, 15), Skills.FLETCHING),
    	BLACK_BRUTAL_ARROW(38, 38.4, -1, new Item[]{ new Item(4821, 15), new Item (2865, 15)}, new Item(4788, 15), Skills.FLETCHING),
    	MITHRIL_BRUTAL_ARROW(49, 45, -1, new Item[]{ new Item(4822, 15), new Item (2865, 15)}, new Item(4793, 15), Skills.FLETCHING),
    	ADAMANT_BRUTAL_ARROW(62, 60.6, -1, new Item[]{ new Item(4823, 15), new Item (2865, 15)}, new Item(4798, 15), Skills.FLETCHING),
    	RUNE_BRUTAL_ARROW(77, 75, -1, new Item[]{ new Item(4824, 15), new Item (2865, 15)}, new Item(4803, 15), Skills.FLETCHING),
    	
    	OPAL_BOLT(11, 16, -1, new Item[]{ new Item(877, 15), new Item (45, 15)}, new Item(879, 15), Skills.FLETCHING),
    	PEARL_BOLT(41, 32, -1, new Item[]{ new Item(9140, 15), new Item (46, 15)}, new Item(880, 15), Skills.FLETCHING),
    	JADE_BOLT(26, 24, -1, new Item[]{ new Item(9139, 15), new Item (9187, 15)}, new Item(9335, 15), Skills.FLETCHING),
    	TOPAZ_BOLT(48, 39, -1, new Item[]{ new Item(9141, 15), new Item (9188, 15)}, new Item(9336, 15), Skills.FLETCHING),
    	BARBED_BOLT(51, 95, -1, new Item[]{ new Item(877, 15), new Item (47, 15)}, new Item(881, 15), Skills.FLETCHING),
    	SAPPHIRE_BOLT(56, 47, -1, new Item[]{ new Item(9142, 15), new Item (9189, 15)}, new Item(9337, 15), Skills.FLETCHING),
    	EMERALD_BOLT(58, 55, -1, new Item[]{ new Item(9142, 15), new Item (9190, 15)}, new Item(9338, 15), Skills.FLETCHING),
    	MITHRIL_GRAPPLE(59, 11, -1, new Item[]{ new Item(9142), new Item (9191, 15)}, new Item(9337, 15), Skills.FLETCHING),
    	RUBY_BOLT(63, 63, -1, new Item[]{ new Item(9143, 15), new Item (9191, 15)}, new Item(9339, 15), Skills.FLETCHING),
    	DIAMOND_BOLT(65, 70, -1, new Item[]{ new Item(9143, 15), new Item (9192, 15)}, new Item(9340, 15), Skills.FLETCHING),
    	DRAGON_BOLT(71, 82, -1, new Item[]{ new Item(9144, 15), new Item (9193, 15)}, new Item(9341, 15), Skills.FLETCHING),
    	ONYX_BOLT(73, 94, -1, new Item[]{ new Item(9144, 15), new Item (9194, 15)}, new Item(9342, 15), Skills.FLETCHING),
    	FRAGMENT_BOLT(60, 5, -1, new Item[]{ new Item(877, 15), new Item (28435, 15)}, new Item(28463, 15), Skills.FLETCHING),
    	ASCENSION_BOLT(90, 200, -1, new Item[]{ new Item(28436, 15)}, new Item(28465, 15), Skills.FLETCHING),
    	ASCENDRI_BOLT(80, 106, -1, new Item[]{ new Item(28465, 15), new Item (31867, 15)}, new Item(31868, 15), Skills.FLETCHING),
    
    	SAGAEI(83, 200, -1, new Item[]{ new Item(21353, 5), new Item (21358, 5)}, new Item(21364, 5), Skills.FLETCHING),
    	BOLAS(87, 50, -1, new Item[]{ new Item(21359, 2), new Item (21358, 1)}, new Item(21365), Skills.FLETCHING);
       
 
       
 
        public static FletchArrowAction getBarByProduce(int id) {
            for(FletchArrowAction fletch : FletchArrowAction.values()) {
            if(fletch.getProducedBow().getId() == id)
                return fletch;
            }
            return null;
        }
 
        public static FletchArrowAction getBar(int id) {
            for(FletchArrowAction fletch : FletchArrowAction.values()) {
            for(Item item : fletch.getItemsRequired())
                if(item.getId() == id)
                return fletch;
            }
            return null;
        }
        
		private static final HashMap<Integer, FletchArrowAction> FLETCHSTUFF = new HashMap<Integer, FletchArrowAction>();
		
		static {
			for (FletchArrowAction f : values()) {
				FLETCHSTUFF.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static FletchArrowAction getFletchItem(int id) {
			return FLETCHSTUFF.get(id);
		}
 
        private int levelRequired;
        private double experience;
        private int emote;
        private Item[] itemsRequired;
        private Item producedBow;
        private int skillType;
 
        private FletchArrowAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedBow, int skillType) {
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
 
        public FletchArrowAction fletch;
        public Item item;
        public int ticks;
        private int xpMultiplier = 1;
 
        public ArrowFletching(FletchArrowAction fletch, Item item, int ticks) {
        this.fletch = fletch;
        this.item = item;
        this.ticks = ticks;
        }
       
 
    @SuppressWarnings("static-access")
	@Override
    public boolean start(Player player) {
        if (fletch == null || player == null) {
            return false;
        }
        if (ticks > 20)
        	ticks = 20;
        int bitValue = player.getVarsManager().getBitValue(9065);
        if (fletch == fletch.BROAD_ARROW  && bitValue != 1){
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
        if (ticks >= 20){
        	player.getInterfaceManager().removeInterface(1251);
        	return false;
        }
        return true;
        }
       
        public static FletchArrowAction isAttaching(Player player, Item used, Item usedWith) {
        	FletchArrowAction fletch = null;
        	fletch = FletchArrowAction.getFletchItem(used.getId());
        	Item selected;
    		if (fletch != null)
    			selected = usedWith;
    		else {
    			fletch = FletchArrowAction.getFletchItem(usedWith.getId());
    			selected = used;
    		}
    		if (fletch != null)
    			return fletch;
    		else
    		return null;
        }

    @Override
    public int processWithDelay(Player player) {
        ticks++;           
        int multiplier = 1;
        double xp = fletch.getExperience();
        int amount = fletch.getProducedBow().getAmount() * multiplier ;
        int maxQuantity = player.getVarsManager().getBitValue(1002);
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
        player.setNextAnimation(new Animation(19516));
        return 1;
    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}