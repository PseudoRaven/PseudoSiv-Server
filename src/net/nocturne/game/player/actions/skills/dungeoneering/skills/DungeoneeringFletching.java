package net.nocturne.game.player.actions.skills.dungeoneering.skills;
 
import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;
 
public class DungeoneeringFletching extends Action {
   
    public enum DGFletch {
    	
    	HEADLESS_ARROWS(1, 5, -1, new Item[]{ new Item(17742, 15), new Item (17796, 15)}, new Item(17747), Skills.FLETCHING),
    	NOVITE_ARROWS(6, 5.7, -1, new Item[]{ new Item(17885, 15), new Item (17747, 15)}, new Item(16317), Skills.FLETCHING),
    	BATHUS_ARROWS(9, 11, -1, new Item[]{ new Item(17890, 15), new Item (17747, 15)}, new Item(16869), Skills.FLETCHING),
    	MARMORAS_ARROWS(26, 17.2, -1, new Item[]{ new Item(17895, 15), new Item (17747, 15, 15)}, new Item(16321), Skills.FLETCHING),
    	KRATONITE_ARROWS(31, 23, -1, new Item[]{ new Item(17900, 15), new Item (17747, 15)}, new Item(16873), Skills.FLETCHING),
    	FRACTITE_ARROWS(36, 26.4, -1, new Item[]{ new Item(17905, 15), new Item (17747, 15)}, new Item(16323), Skills.FLETCHING),
    	ZEPHYRIUM_ARROWS(41, 33, -1, new Item[]{ new Item(17910, 15), new Item (17747, 15)}, new Item(16875), Skills.FLETCHING),
    	ARGONITE_ARROWS(46, 37.9, -1, new Item[]{ new Item(17915, 15), new Item (17747, 15)}, new Item(16325), Skills.FLETCHING),
    	KATAGON_ARROWS(51, 45, -1, new Item[]{ new Item(17920, 15), new Item (17747, 15)}, new Item(16877), Skills.FLETCHING),
    	GORGONITE_ARROWS(56, 51.7, -1, new Item[]{ new Item(17925, 15), new Item (17747, 15)}, new Item(16327), Skills.FLETCHING),
    	PROMETHIUM_ARROWS(61, 59, -1, new Item[]{ new Item(17930, 15), new Item (17747, 15)}, new Item(16879), Skills.FLETCHING), 	
    	TANGLE_GUM_SHORTBOW(1, 5, 6677, new Item[]{ new Item(17702), new Item (17752)}, new Item(16867), Skills.FLETCHING),
    	TANGLE_GUM_LONGBOW(6, 5.7, 6677, new Item[]{ new Item(17722), new Item (17752)}, new Item(16317), Skills.FLETCHING),
    	SEEPING_ELM_SHORTBOW(9, 11, 6677, new Item[]{ new Item(17704), new Item (17752)}, new Item(16869), Skills.FLETCHING),
    	SEEPING_ELM_LONGBOW(16, 10.3, 6677, new Item[]{ new Item(17724), new Item (17752)}, new Item(16319), Skills.FLETCHING),
    	BLOOD_SPINDLE_SHORTBOW(21, 15, 6677, new Item[]{ new Item(17706), new Item (17752)}, new Item(16871), Skills.FLETCHING),
    	BLOOD_SPINDLE_LONGBOW(26, 17.2, 6677, new Item[]{ new Item(17726), new Item (17752)}, new Item(16321), Skills.FLETCHING),
    	UTUKU_SHORTBOW(31, 23, 6677, new Item[]{ new Item(17708), new Item (17752)}, new Item(16873), Skills.FLETCHING),
    	UTUKU_LONGBOW(36, 26.4, 6677, new Item[]{ new Item(17728), new Item (17752)}, new Item(16323), Skills.FLETCHING),
    	SPINEBEAM_SHORTBOW(41, 33, 6677, new Item[]{ new Item(17710), new Item (17752)}, new Item(16875), Skills.FLETCHING),
    	SPINEBEAM_LONGBOW(46, 37.9, 6677, new Item[]{ new Item(17730), new Item (17752)}, new Item(16325), Skills.FLETCHING),
    	BOVISTRANGLER_SHORTBOW(51, 45, 6677, new Item[]{ new Item(17712), new Item (17752)}, new Item(16877), Skills.FLETCHING),
    	BOVISTRANGLER_LONGBOW(56, 51.7, 6677, new Item[]{ new Item(17732), new Item (17752)}, new Item(16327), Skills.FLETCHING),
    	THIGAT_SHORTBOW(61, 59, 6677, new Item[]{ new Item(17714), new Item (17752)}, new Item(16879), Skills.FLETCHING),
    	THIGAT_LONGBOW(66, 67.8, 6677, new Item[]{ new Item(17734), new Item (17752)}, new Item(16329), Skills.FLETCHING),
    	CORPSETHRORN_SHORTBOW(71, 75, 6677, new Item[]{ new Item(17716), new Item (17752)}, new Item(16881), Skills.FLETCHING),
    	CORPSETHRORN_LONGBOW(76, 86.2, 6677, new Item[]{ new Item(17736), new Item (17752)}, new Item(16331), Skills.FLETCHING),
    	ENTGALLOW_SHORTBOW(81, 93, 6677, new Item[]{ new Item(17718), new Item (17752)}, new Item(16883), Skills.FLETCHING),
    	ENTGALLOW_LONGBOW(86, 106.9, 6677, new Item[]{ new Item(17738), new Item (17752)}, new Item(16333), Skills.FLETCHING),
    	GRAVECREEPER_SHORTBOW(91, 113, 6677, new Item[]{ new Item(17720), new Item (17752)}, new Item(16885), Skills.FLETCHING),
    	GRAVECREEPER_LONGBOW(96, 129.9, 6677, new Item[]{ new Item(17740), new Item (17752)}, new Item(16335), Skills.FLETCHING);
    	
    	public static DGFletch getBarByProduce(int id) {
            for(DGFletch fletch : DGFletch.values()) {
            if(fletch.getProducedBow().getId() == id)
                return fletch;
            }
            return null;
        }
 
        public static DGFletch getBar(int id) {
            for(DGFletch fletch : DGFletch.values()) {
            for(Item item : fletch.getItemsRequired())
                if(item.getId() == id)
                return fletch;
            }
            return null;
        }
        
		private static final HashMap<Integer, DGFletch> FLETCHSTUFF = new HashMap<Integer, DGFletch>();
		
		static {
			for (DGFletch f : values()) {
				FLETCHSTUFF.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static DGFletch getFletchItem(int id) {
			return FLETCHSTUFF.get(id);
		}
 
        private int levelRequired;
        private double experience;
        private int emote;
        private Item[] itemsRequired;
        private Item producedBow;
        private int skillType;
 
        private DGFletch(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedBow, int skillType) {
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
 
        public DGFletch fletch;
        public Item item;
        public int ticks;
        private int xpMultiplier = 1;
 
        public DungeoneeringFletching(DGFletch fletch, Item item, int ticks) {
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
		if (!player.getInventory().containsItemToolBelt(fletch.getItemsRequired()[0].getId(),
				fletch.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (fletch.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(fletch.getItemsRequired()[1].getId(),
					fletch.getItemsRequired()[1].getAmount())) {
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
       
        public static DGFletch isAttaching(Player player, Item used, Item usedWith) {
        	DGFletch fletch = null;
        	fletch = DGFletch.getFletchItem(used.getId());
        	Item selected;
    		if (fletch != null)
    			selected = usedWith;
    		else {
    			fletch = DGFletch.getFletchItem(usedWith.getId());
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
        double xp = fletch.getExperience();
        int amount = fletch.getProducedBow().getAmount() * multiplier ;
        int maxQuantity = player.getVarsManager().getBitValue(1002);
        for (Item required : fletch.getItemsRequired()) {
            player.getInventory().deleteItem(required.getId(), required.getAmount());
        }
        if (maxQuantity > 1){
    		SkillsDialogue.sendProgressBar(player, fletch.getProducedBow().getId(), maxQuantity, (int) xp);
    		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
    		xpMultiplier++;
    	}
        player.getInventory().addItem(fletch.getProducedBow().getId(), amount);
		if (player.getClanManager() != null && player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
        player.getSkills().addXp(fletch.getSkillType(), xp);
        player.setNextAnimation(new Animation(emote));
        return 0;
    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}