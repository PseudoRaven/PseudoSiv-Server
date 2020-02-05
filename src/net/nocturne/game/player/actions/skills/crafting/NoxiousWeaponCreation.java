package net.nocturne.game.player.actions.skills.crafting;

import java.util.HashMap;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class NoxiousWeaponCreation extends Action {
	
	public enum CreateWeapon {

		SPIDER_LEG(30, 100, 25594, new Item[]
			{ new Item(31719), new Item(31718), new Item(31720)},
			new Item(31721), Skills.CRAFTING),
		NOXIOUS_SCYTHE(90, 1000, 24108, new Item[]
				{ new Item(31721), new Item(31722) }, new Item(31725), Skills.CRAFTING),
		NOXIOUS_STAFF(90, 1000, 24109, new Item[]
				{ new Item(31721), new Item(31723)}, new Item(31729), Skills.CRAFTING),
		NOXIOUS_LONGBOW(90, 1000, 24110, new Item[]
				{ new Item(31721), new Item(31724)}, new Item(31733), Skills.CRAFTING);
		 
        public static CreateWeapon getWeaponByProduce(int id) {
            for(CreateWeapon weapon : CreateWeapon.values()) {
            if(weapon.getProducedWeapon().getId() == id)
                return weapon;
            }
            return null;
        }
 
        public static CreateWeapon getWeapon(int id) {
            for(CreateWeapon weapon : CreateWeapon.values()) {
            for(Item item : weapon.getItemsRequired())
                if(item.getId() == id)
                return weapon;
            }
            return null;
        }
        
		private static final HashMap<Integer, CreateWeapon> NOXIOUS = new HashMap<Integer, CreateWeapon>();
		
		static {
			for (CreateWeapon f : values()) {
				NOXIOUS.put(f.getItemsRequired()[0].getId(), f);
			}
		}
		
		public static CreateWeapon getFletchItem(int id) {
			return NOXIOUS.get(id);
		}
 
        private int levelRequired;
        private double experience;
        private int emote;
        private Item[] itemsRequired;
        private Item producedWeapon;
        private int skillType;
 
        private CreateWeapon(int levelRequired, double experience, int emote, Item[] itemsRequired, Item producedWeapon, int skillType) {
            this.levelRequired = levelRequired;
            this.experience = experience;
            this.emote = emote;
            this.itemsRequired = itemsRequired;
            this.producedWeapon = producedWeapon;
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
       
        public Item getProducedWeapon() {
            return producedWeapon;
        }
       
        public double getExperience() {
            return experience;
        }
 
        public int getSkillType() {
            return skillType;
        }
       
        }
 
        public CreateWeapon weapon;
        public int ticks;
        public Item item;
        private int xpMultiplier = 1;
 
        public NoxiousWeaponCreation(CreateWeapon weapon, Item item, int ticks) {
        this.weapon = weapon;
        this.ticks = ticks;
        this.item = item;
        }
       
 
    @Override
    public boolean start(Player player) {
        if (weapon == null || player == null) {
            return false;
        }
        return true;
        }
 
	@Override
	public boolean process(Player player) {
		if (weapon == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(weapon.getItemsRequired()[0].getId(),
				weapon.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (weapon.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(weapon.getItemsRequired()[1].getId(),
					weapon.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (player.getSkills().getLevel(weapon.getSkillType()) < weapon.getLevelRequired()) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		return true;
	}
       
        public static CreateWeapon isAttaching(Player player, Item used, Item usedWith) {
        	System.out.println(used.getId());
        	System.out.println(usedWith.getId());
        	CreateWeapon weapon = null;
        	weapon = CreateWeapon.getFletchItem(used.getId());
        	Item selected;
    		if (weapon != null)
    			selected = usedWith;
    		else {
    			weapon = CreateWeapon.getFletchItem(usedWith.getId());
    			selected = used;
    		}
    		if (weapon != null)
    			return weapon;
    		else
    		return null;
              }
    @Override
    public int processWithDelay(Player player) {
        ticks--;           
        int multiplier = 0;
        int maxQuantity = player.getVarsManager().getBitValue(1002);
        int emote = weapon.getEmote();
        double xp = weapon.getExperience();
        int amount = weapon.getProducedWeapon().getAmount() * multiplier ;
        for (Item required : weapon.getItemsRequired()) {
            player.getInventory().deleteItem(required.getId(), required.getAmount());
        }
        if (maxQuantity > 1){
    		SkillsDialogue.sendProgressBar(player, weapon.getProducedWeapon().getId(), maxQuantity, (int) xp);
    		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
    		xpMultiplier++;
    	}
        player.getInventory().addItem(weapon.getProducedWeapon().getId(), amount);
        player.getSkills().addXp(weapon.getSkillType(), xp);
        player.setNextAnimation(new Animation(emote));
        return 0;
    }
 
    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
       
    }
 
}