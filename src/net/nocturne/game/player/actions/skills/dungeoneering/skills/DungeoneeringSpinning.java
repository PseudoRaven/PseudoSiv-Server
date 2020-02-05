package net.nocturne.game.player.actions.skills.dungeoneering.skills;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class DungeoneeringSpinning extends Action {

    public enum DungSpin {

	
    SALVE_CLOTH(1, 2.5, new Item[]
    	{ new Item(17448)}, new Item(17468), Skills.CRAFTING),
    WILDERCRESS_CLOTH(10, 3, new Item[]
        	{ new Item(17450)}, new Item(17470), Skills.CRAFTING),
    BLIGHTLEAF_CLOTH(20, 3.6, new Item[]
        	{ new Item(17452)}, new Item(17472), Skills.CRAFTING),
    ROSEBLOOD_CLOTH(30, 4.3, new Item[]
        	{ new Item(17454)}, new Item(17474), Skills.CRAFTING),
    BRYLL_CLOTH(40, 5.2, new Item[]
        	{ new Item(17456)}, new Item(17476), Skills.CRAFTING),
    DUSKWEED_CLOTH(50, 6.2, new Item[]
        	{ new Item(17458)}, new Item(17478), Skills.CRAFTING),
    SOULBELL_CLOTH(60, 7.5, new Item[]
        	{ new Item(17460)}, new Item(17480), Skills.CRAFTING),
    ECTO_CLOTH(70, 9, new Item[]
        	{ new Item(17462)}, new Item(17482), Skills.CRAFTING),
    RUNIC_CLOTH(80, 10.7, new Item[]
        	{ new Item(17464)}, new Item(17484), Skills.CRAFTING),
    SPIRITBLOOM_CLOTH(90, 12.9, new Item[]
        	{ new Item(17466)}, new Item(17486), Skills.CRAFTING);

	public static DungSpin getBarByProduce(int id) {
	    for(DungSpin cape : DungSpin.values()) {
		if(cape.getProducedBar().getId() == id)
		    return cape;
	    }
	    return null;
	}

	public static DungSpin getBar(int id) {
	    for(DungSpin cape : DungSpin.values()) {
		for(Item item : cape.getItemsRequired())
		    if(item.getId() == id)
			return cape;
	    }
	    return null;
	}

	private int levelRequired;
	private double experience;
	private Item[] itemsRequired;
	private Item producedBar;
	private int skillType;

	private DungSpin(int levelRequired, double experience, Item[] itemsRequired, Item producedBar, int skillType) {
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

    public DungSpin cape;
    public int ticks;
    private int xpMultiplier = 1;

    public DungeoneeringSpinning(DungSpin cape,int ticks) {
	this.cape = cape;
	this.ticks = ticks;
    }

    @Override
    public boolean start(Player player) {
    	if (cape == null || player == null) {
		    return false;
		}
		return true;
	    }

    @Override
    public boolean process(Player player) {
	if (cape == null || player == null) {
		player.getInterfaceManager().removeInterface(1251);
		return false;
	}
	if (!player.getInventory().containsItemToolBelt(cape.getItemsRequired()[0].getId(), cape.getItemsRequired()[0].getAmount())) {
		player.getInterfaceManager().removeInterface(1251);
		return false;
	}
	if (cape.getItemsRequired().length > 1) {
	    if (!player.getInventory().containsItemToolBelt(cape.getItemsRequired()[1].getId(), cape.getItemsRequired()[1].getAmount())) {
	    	player.getInterfaceManager().removeInterface(1251);
	    	return false;
	    }
	}
	if (!player.getSkills().hasLevel(cape.getSkillType(), cape.getLevelRequired())){
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
	double xp = cape.getExperience();
	int amount = cape.getProducedBar().getAmount() * multiplier ;
	for (Item required : cape.getItemsRequired()) {
	    player.getInventory().deleteItem(required.getId(), required.getAmount());
	}
	if (maxQuantity > 1){
	SkillsDialogue.sendProgressBar(player, cape.getProducedBar().getId(), maxQuantity, (int) xp);
	player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
	xpMultiplier++;
	}
	player.getInventory().addItem(cape.getProducedBar().getId(), amount);
	player.getSkills().addXp(cape.getSkillType(), xp);
	player.setNextAnimation(new Animation(883));


	if (ticks > 0) {
	    return 1;
	}
	return -1;
    }

    @Override
    public void stop(Player player) {
	setActionDelay(player, 3);
    }


}
