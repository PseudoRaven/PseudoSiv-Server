package net.nocturne.game.player.actions.skills.fletching;
 
import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;
 
public class Fletching extends Action {
   
    public enum FletchAction {

        SHORTBOW(1, 5, 24945, new Item[]{ new Item(50), new Item (1777)}, new Item(841), Skills.FLETCHING),
        SHIELDBOW(10, 10, 24952, new Item[]{ new Item(48), new Item (1777)}, new Item(839), Skills.FLETCHING),
        OAK_SHORTBOW(20, 16.5, 24946, new Item[]{ new Item(54), new Item (1777)}, new Item(843), Skills.FLETCHING),
        OAK_SHIELDBOW(25, 25, 24953, new Item[]{ new Item(56), new Item (1777)}, new Item(845), Skills.FLETCHING),
        COMP_OGRE_BOW(30, 45, 6684, new Item[]{ new Item(4825), new Item (1777)}, new Item(4827), Skills.FLETCHING),
        WILLOW_SHORTBOW(35, 33, 24947, new Item[]{ new Item(60), new Item (1777)}, new Item(849), Skills.FLETCHING),
        WILLOW_SHIELDBOW(40, 41.5, 24954, new Item[]{ new Item(58), new Item (1777)}, new Item(847), Skills.FLETCHING),
        MAPLE_SHORTBOW(50, 50, 24948, new Item[]{ new Item(64), new Item (1777)}, new Item(853), Skills.FLETCHING),
        MAPLE_SHIELDBOW(55, 58.3, 24955, new Item[]{ new Item(62), new Item (1777)}, new Item(851), Skills.FLETCHING),
        YEW_SHORTBOW(65, 67.5, 24949, new Item[]{ new Item(68), new Item (1777)}, new Item(857), Skills.FLETCHING),
        YEW_SHIELDBOW(70, 75, 24956, new Item[]{ new Item(66), new Item (1777)}, new Item(855), Skills.FLETCHING),
        MAGIC_SHORTBOW(80, 83, 24950, new Item[]{ new Item(72), new Item (1777)}, new Item(861), Skills.FLETCHING),
        MAGIC_SHIELDBOW(85, 91.5, 24957, new Item[]{ new Item(70), new Item (1777)}, new Item(859), Skills.FLETCHING),
        ELDER_SHORTBOW(90, 101.2, 24951, new Item[]{ new Item(29736), new Item (1777)}, new Item(29614), Skills.FLETCHING),
        ELDER_SHIELDBOW(95, 116.4, 24958, new Item[]{ new Item(29734), new Item (1777)}, new Item(29611), Skills.FLETCHING),
        
        BRONZE_CBOW_U(9, 6, -1, new Item[]{ new Item(9440), new Item (9420)}, new Item(9454), Skills.FLETCHING),
        BLURITE_CBOW_U(24, 16, -1, new Item[]{ new Item(9442), new Item (9421)}, new Item(9456), Skills.FLETCHING),
        IRON_CBOW_U(39, 22, -1, new Item[]{ new Item(9444), new Item (9423)}, new Item(9457), Skills.FLETCHING),
        STEEL_CBOW_U(46, 27, -1, new Item[]{ new Item(9446), new Item (9425)}, new Item(9459), Skills.FLETCHING),
        MITHRIL_CBOW_U(54, 32, -1, new Item[]{ new Item(9448), new Item (9427)}, new Item(9461), Skills.FLETCHING),
        ADAMANT_CBOW_U(61, 41, -1, new Item[]{ new Item(9450), new Item (9429)}, new Item(9463), Skills.FLETCHING),
        RUNE_CBOW_U(69, 50, -1, new Item[]{ new Item(9452), new Item (9431)}, new Item(9465), Skills.FLETCHING),
        DRAGON_CBOW_U(94, 94, -1, new Item[]{ new Item(25483), new Item (25481)}, new Item(25478), Skills.FLETCHING),
    	
    	BRONZE_CBOW(9, 6, 6671, new Item[]{ new Item(9454), new Item (9438)}, new Item(9174), Skills.FLETCHING),
    	OFFHAND_BRONZE_CBOW(9, 6, 6671, new Item[]{ new Item(9454), new Item (9438)}, new Item(25881), Skills.FLETCHING),
    	BRONZE_2H_CBOW(9, 6, 6671, new Item[]{ new Item(9454), new Item (9438)}, new Item(25919), Skills.FLETCHING),
    	BLURITE_CBOW(24, 16, 6672, new Item[]{ new Item(9456), new Item (9438)}, new Item(9176), Skills.FLETCHING),
    	IRON_CBOW(39, 22, 6673, new Item[]{ new Item(9457), new Item (9438)}, new Item(9177), Skills.FLETCHING),
    	OFFHAND_IRON_CBOW(39, 22, 6673, new Item[]{ new Item(9457), new Item (9438)}, new Item(25921), Skills.FLETCHING),
    	IRON_2H_CBOW(39, 22, 6673, new Item[]{ new Item(9457), new Item (9438)}, new Item(25478), Skills.FLETCHING),
    	STEEL_CBOW(46, 27, 6674, new Item[]{ new Item(9459), new Item (9438)}, new Item(9179), Skills.FLETCHING),
    	OFFHAND_STEEL_CBOW(46, 27, 6674, new Item[]{ new Item(9459), new Item (9438)}, new Item(25885), Skills.FLETCHING),
    	STEEL_2H_CBOW(46, 27, 6674, new Item[]{ new Item(9459), new Item (9438)}, new Item(25923), Skills.FLETCHING),
    	MITHRIL_CBOW(54, 32, 6675, new Item[]{ new Item(9461), new Item (9438)}, new Item(9181), Skills.FLETCHING),
    	OFFHAND_MITHRIL_CBOW(54, 32, 6675, new Item[]{ new Item(9461), new Item (9438)}, new Item(25925), Skills.FLETCHING),
    	MITHRIL_2H_CBOW(54, 32, 6675, new Item[]{ new Item(9461), new Item (9438)}, new Item(25478), Skills.FLETCHING),
    	ADAMANT_CBOW(61, 41, 6676, new Item[]{ new Item(9463), new Item (9438)}, new Item(9183), Skills.FLETCHING),
    	OFFHAND_ADAMANT_CBOW(61, 41, 6676, new Item[]{ new Item(9463), new Item (9438)}, new Item(25889), Skills.FLETCHING),
    	ADAMANT_2H_CBOW(61, 41, 6676, new Item[]{ new Item(9463), new Item (9438)}, new Item(25927), Skills.FLETCHING),
    	RUNE_CBOW(69, 50, 6677, new Item[]{ new Item(9465), new Item (9438)}, new Item(9185), Skills.FLETCHING),
    	OFFHAND_RUNE_CBOW(69, 50, 6677, new Item[]{ new Item(9465), new Item (9438)}, new Item(25891), Skills.FLETCHING),
    	RUNE_2H_CBOW(69, 50, 6677, new Item[]{ new Item(9465), new Item (9438)}, new Item(25929), Skills.FLETCHING),
    	DRAGON_CBOW(94, 100, 6677, new Item[]{ new Item(25478), new Item (9438)}, new Item(25917), Skills.FLETCHING),
    	OFFHAND_DRAGON_CBOW(94, 100, 6677, new Item[]{ new Item(25478), new Item (9438)}, new Item(25894), Skills.FLETCHING),
    	DRAGON_2H_CBOW(94, 100, 6677, new Item[]{ new Item(25478), new Item (9438)}, new Item(25932), Skills.FLETCHING);
    	
    	
       
       
 
       
 
        public static FletchAction getBarByProduce(int id) {
            for(FletchAction fletch : FletchAction.values()) {
            if(fletch.getProducedBow().getId() == id)
                return fletch;
            }
            return null;
        }
 
        public static FletchAction getBar(int id) {
            for(FletchAction fletch : FletchAction.values()) {
            for(Item item : fletch.getItemsRequired())
                if(item.getId() == id)
                return fletch;
            }
            return null;
        }
        
		private static final HashMap<Integer, FletchAction> FLETCHSTUFF = new HashMap<Integer, FletchAction>();
		
		static {
			for (FletchAction f : values()) {
				FLETCHSTUFF.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static FletchAction getFletchItem(int id) {
			return FLETCHSTUFF.get(id);
		}
 
        private int levelRequired;
        private double experience;
        private int emote;
        private Item[] itemsRequired;
        private Item producedBow;
        private int skillType;
 
        private FletchAction(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedBow, int skillType) {
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
 
        public FletchAction fletch;
        public Item item;
        public int ticks;
        private int xpMultiplier = 1;
 
        public Fletching(FletchAction fletch, Item item, int ticks) {
        this.fletch = fletch;
        this.item = item;
        this.ticks = ticks;
        }
       
 
    @Override
    public boolean start(Player player) {
        if (fletch == null || player == null) {
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
        return true;
        }
       
        public static FletchAction isAttaching(Player player, Item used, Item usedWith) {
        	FletchAction fletch = null;
        	fletch = FletchAction.getFletchItem(used.getId());
        	Item selected;
    		if (fletch != null)
    			selected = usedWith;
    		else {
    			fletch = FletchAction.getFletchItem(usedWith.getId());
    			selected = used;
    		}
    		System.out.println(fletch);
    		if (fletch != null)
    			return fletch;
    		else
    		return null;
              }
        
    @Override
    public int processWithDelay(Player player) {
        ticks--;           
        int multiplier = 0;
        int emote = fletch.getEmote();
        int maxQuantity = player.getVarsManager().getBitValue(1002);
        double xp = fletch.getExperience();
        int amount = fletch.getProducedBow().getAmount() * multiplier ;
        for (Item required : fletch.getItemsRequired()) {
            player.getInventory().deleteItem(required.getId(), required.getAmount());
        }
        player.getInventory().addItem(fletch.getProducedBow().getId(), amount);
		SkillsDialogue.sendProgressBar(player, fletch.getProducedBow().getId(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		if (player.getClanManager() != null && player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,	fletch.getProducedBow().getId(), Skills.FLETCHING);
        player.getSkills().addXp(fletch.getSkillType(), xp);
        player.setNextAnimation(new Animation(emote));
        return 4;
    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}