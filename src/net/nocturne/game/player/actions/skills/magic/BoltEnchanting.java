package net.nocturne.game.player.actions.skills.magic;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;
 
public class BoltEnchanting extends Action {
   
    public enum EnchantAction {
       

    	OPAL_BOLTS_E(4, 9, new Item[]{ new Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236, 10), Skills.MAGIC),
    	SAPPHIRE_BOLTS_E(7, 17.5, new Item[]{ new Item(9337, 10), new Item(564), new Item(555), new Item(558)}, new Item(9240, 10), Skills.MAGIC),
    	JADE_BOLTS_E(14, 19, new Item[]{ new Item(9335, 10), new Item(564), new Item(557, 2)}, new Item(9237, 10), Skills.MAGIC),
    	PEARL_BOLTS_E(24, 29.5, new Item[]{ new Item(880, 10), new Item(564), new Item(555, 2)}, new Item(9238, 10), Skills.MAGIC),
    	EMERALD_BOLTS_E(27, 37, new Item[]{ new Item(9339, 10), new Item(564), new Item(556, 2), new Item(561)}, new Item(9241, 10), Skills.MAGIC),
    	TOPAZ_BOLTS_E(29, 33.5, new Item[]{ new Item(9336, 10), new Item(564), new Item(554, 2)}, new Item(9239, 10), Skills.MAGIC),
    	RUBY_BOLTS_E(49, 59, new Item[]{ new Item(9339, 10), new Item(564), new Item(554, 5), new Item(565)}, new Item(9242, 10), Skills.MAGIC),
    	DIAMOND_BOLTS_E(57, 67, new Item[]{ new Item(9340, 10), new Item(564), new Item(557, 10), new Item(563, 2)}, new Item(9243, 10), Skills.MAGIC),
    	DRAGON_BOLTS_E(68, 78, new Item[]{ new Item(9341, 10), new Item(564), new Item(557, 15), new Item(566)}, new Item(9244, 10), Skills.MAGIC),
    	ONYX_BOLTS_E(87, 97, new Item[]{ new Item(9342, 10), new Item(564), new Item(554, 20), new Item(560)}, new Item(9245, 10), Skills.MAGIC),
    	ASCENDRI_BOLTS_E(90, 120, new Item[]{ new Item(31868, 10), new Item(564), new Item(554, 20), new Item(560)}, new Item(31881, 10), Skills.MAGIC);
    	
        public static EnchantAction getBarByProduce(int id) {
            for(EnchantAction enchant : EnchantAction.values()) {
            if(enchant.getProducedBow().getId() == id)
                return enchant;
            }
            return null;
        }
 
        public static EnchantAction getBar(int id) {
            for(EnchantAction enchant : EnchantAction.values()) {
            for(Item item : enchant.getItemsRequired())
                if(item.getId() == id)
                return enchant;
            }
            return null;
        }
 
        private int levelRequired;
        private double experience;
        private Item[] itemsRequired;
        private Item producedBow;
        private int skillType;
 
        private EnchantAction(int levelRequired, double experience, Item[] itemsRequired, Item producedBow, int skillType) {
            this.levelRequired = levelRequired;
            this.experience = experience;
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
 
        public EnchantAction enchant;
        public int ticks;
        private int xpMultiplier = 1;
 
        public BoltEnchanting(EnchantAction enchant, int ticks) {
        this.enchant = enchant;
        this.ticks = ticks;
        }
       
 
        @Override
        public boolean start(Player player) {
        	if (enchant == null || player == null)
    		    return false;
            if (ticks > 10)
            	ticks = 10;
    		return true;
    	}

        @Override
        public boolean process(Player player) {
    		if (enchant == null || player == null) {
    			player.getInterfaceManager().removeInterface(1251);
    		    return false;
    		}
    		if (!player.getInventory().containsItemToolBelt(enchant.getItemsRequired()[0].getId(), enchant.getItemsRequired()[0].getAmount())) {
    			player.getInterfaceManager().removeInterface(1251);
    			return false;
    		}
    		if (enchant.getItemsRequired().length > 1) {
    		    if (!player.getInventory().containsItemToolBelt(enchant.getItemsRequired()[1].getId(), enchant.getItemsRequired()[1].getAmount())) {
    		    	player.getInterfaceManager().removeInterface(1251);
    		    	return false;
    		    }
    		}
    		if (!player.getSkills().hasLevel(enchant.getSkillType(), enchant.getLevelRequired())){
    			player.getInterfaceManager().removeInterface(1251);
    			return false;
    		}
            if (ticks <= 0) {
            	player.getInterfaceManager().removeInterface(1251);
            	return false;
            }
    		return true;
        }
        
        @Override
        public int processWithDelay(Player player) {
    		ticks--;			
    		int multiplier = 1;	
    		int maxQuantity = player.getVarsManager().getBitValue(1002);
    		double xp = enchant.getExperience();
    		int amount = enchant.getProducedBow().getAmount() * multiplier ;
    		for (Item required : enchant.getItemsRequired()) {
    		    player.getInventory().deleteItem(required.getId(), required.getAmount());
    		}
    		if (maxQuantity > 1){
    			SkillsDialogue.sendProgressBar(player, enchant.getProducedBow().getId(), maxQuantity, (int) xp);
    			player.getPackets().sendExecuteScript(3373, 1018);
    			player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
    			xpMultiplier++;
    			}
    		player.getInventory().addItem(enchant.getProducedBow().getId(), amount);
    		if (player.getDailyTask() != null)
    			player.getDailyTask().incrementTask(player, 3,	enchant.getProducedBow().getId(), Skills.CRAFTING);
    		player.getSkills().addXp(enchant.getSkillType(), xp);
    		player.setNextAnimation(new Animation(24471));
    	
    	
    		if (ticks > 0) {
    		    return 1;
    		}
    		return 0;
    	    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}
