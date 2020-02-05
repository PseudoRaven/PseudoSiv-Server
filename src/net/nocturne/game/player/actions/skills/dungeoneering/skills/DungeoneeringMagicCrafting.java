package net.nocturne.game.player.actions.skills.dungeoneering.skills;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class DungeoneeringMagicCrafting extends Action {

    public enum CraftMageDung {

    SALVE_GLOVES(1, 12.5, new Item[]
        { new Item(17468, 1), new Item(17447, 1)}, new Item(17151), Skills.CRAFTING),
    SALVE_SHOES(2, 14.2, new Item[]
    	{ new Item(17468, 1), new Item(17447, 1)}, new Item(16911), Skills.CRAFTING),
    SALVE_HOOD(4, 31.8, new Item[]
        	{ new Item(17468, 2), new Item(17447, 1)}, new Item(16735), Skills.CRAFTING),
    SALVE_SHIELD(6, 52.8, new Item[]
        	{ new Item(17468, 3), new Item(17447, 1)}, new Item(27724), Skills.CRAFTING),
    SALVE_ROBE_BOTTOM(6, 52.8, new Item[]
        	{ new Item(17468, 3), new Item(17447, 1)}, new Item(16845), Skills.CRAFTING),
    SALVE_ROBE_TOP(8, 96.8, new Item[]
        	{ new Item(17468, 5), new Item(17447, 1)}, new Item(17217), Skills.CRAFTING),
    SALVE_ORB(8, 19.3, new Item[]
        	{ new Item(17468, 1), new Item(17447, 1)}, new Item(27937), Skills.CRAFTING),
    WILDERCRESS_GLOVES(10, 21.5, new Item[]
            { new Item(17470, 1), new Item(17447, 1)}, new Item(17153), Skills.CRAFTING),
    WILDERCRESS_SHOES(12, 22.7, new Item[]
        	{ new Item(17470, 1), new Item(17447, 1)}, new Item(16913), Skills.CRAFTING),
    WILDERCRESS_HOOD(14, 48.8, new Item[]
            	{ new Item(17470, 2), new Item(17447, 1)}, new Item(16737), Skills.CRAFTING),
    WILDERCRESS_SHIELD(16, 78.3, new Item[]
            	{ new Item(17470, 3), new Item(17447, 1)}, new Item(27726), Skills.CRAFTING),
    WILDERCRESS_ROBE_BOTTOM(16, 78.3, new Item[]
            	{ new Item(17470, 3), new Item(17447, 1)}, new Item(16847), Skills.CRAFTING),
    WILDERCRESS_ROBE_TOP(18, 139, new Item[]
            	{ new Item(17470, 5), new Item(17447, 1)}, new Item(17218), Skills.CRAFTING),
    WILDERCRESS_ORB(18, 27.8, new Item[]
            	{ new Item(17470, 1), new Item(17447, 1)}, new Item(27939), Skills.CRAFTING),
    BLIGHTLEAF_GLOVES(20, 29.5, new Item[]
            { new Item(17472, 1), new Item(17447, 1)}, new Item(17155), Skills.CRAFTING),
    BLIGHTLEAF_SHOES(22, 31.2, new Item[]
        	{ new Item(17472, 1), new Item(17447, 1)}, new Item(16912), Skills.CRAFTING),
    BLIGHTLEAF_HOOD(24, 65.8, new Item[]
            	{ new Item(17472, 2), new Item(17447, 1)}, new Item(16739), Skills.CRAFTING),
    BLIGHTLEAF_SHIELD(26, 103.8, new Item[]
            	{ new Item(17472, 3), new Item(17447, 1)}, new Item(27728), Skills.CRAFTING),
    BLIGHTLEAF_ROBE_BOTTOM(26, 103.8, new Item[]
            	{ new Item(17472, 3), new Item(17447, 1)}, new Item(16849), Skills.CRAFTING),
    BLIGHTLEAF_ROBE_TOP(28, 181.5, new Item[]
            	{ new Item(17472, 5), new Item(17447, 1)}, new Item(17222), Skills.CRAFTING),
    BLIGHTLEAF_ORB(28, 36.3, new Item[]
            	{ new Item(17472, 1), new Item(17447, 1)}, new Item(27943), Skills.CRAFTING),
    ROSEBLOOD_GLOVES(30, 38, new Item[]
            { new Item(17474, 1), new Item(17447, 1)}, new Item(17157), Skills.CRAFTING),
    ROSEBLOOD_SHOES(32, 39.7, new Item[]
        	{ new Item(17474, 1), new Item(17447, 1)}, new Item(16914), Skills.CRAFTING),
    ROSEBLOOD_HOOD(34, 82.8, new Item[]
            	{ new Item(17474, 2), new Item(17447, 1)}, new Item(16741), Skills.CRAFTING),
    ROSEBLOOD_SHIELD(36, 129.3, new Item[]
            	{ new Item(17474, 3), new Item(17447, 1)}, new Item(27730), Skills.CRAFTING),
    ROSEBLOOD_ROBE_BOTTOM(36, 129.3, new Item[]
            	{ new Item(17474, 3), new Item(17447, 1)}, new Item(16851), Skills.CRAFTING),
    ROSEBLOOD_ROBE_TOP(38, 224, new Item[]
            	{ new Item(17474, 5), new Item(17447, 1)}, new Item(17222), Skills.CRAFTING),
    ROSEBLOOD_ORB(38, 44.8, new Item[]
            	{ new Item(17474, 1), new Item(17447, 1)}, new Item(27943), Skills.CRAFTING),
    BRYLL_GLOVES(40, 46.5, new Item[]
            { new Item(17476, 1), new Item(17447, 1)}, new Item(17159), Skills.CRAFTING),
    BRYLL_SHOES(42, 48.2, new Item[]
        	{ new Item(17476, 1), new Item(17447, 1)}, new Item(16916), Skills.CRAFTING),
    BRYLL_HOOD(44, 99.8, new Item[]
            	{ new Item(17476, 2), new Item(17447, 1)}, new Item(16743), Skills.CRAFTING),
    BRYLL_SHIELD(46, 154.8, new Item[]
            	{ new Item(17476, 3), new Item(17447, 1)}, new Item(27732), Skills.CRAFTING),
    BRYLL_ROBE_BOTTOM(46, 154.8, new Item[]
            	{ new Item(17476, 3), new Item(17447, 1)}, new Item(16853), Skills.CRAFTING),
    BRYLL_ROBE_TOP(48, 266.5, new Item[]
            	{ new Item(17476, 5), new Item(17447, 1)}, new Item(17224), Skills.CRAFTING),
    BRYLL_ORB(48, 53.3, new Item[]
            	{ new Item(17476, 1), new Item(17447, 1)}, new Item(27945), Skills.CRAFTING),
    DUSKWEED_GLOVES(50, 55, new Item[]
            { new Item(17478, 1), new Item(17447, 1)}, new Item(17161), Skills.CRAFTING),
    DUSKWEED_SHOES(52, 56.7, new Item[]
        	{ new Item(17478, 1), new Item(17447, 1)}, new Item(16918), Skills.CRAFTING),
    DUSKWEED_HOOD(54, 116.8, new Item[]
            	{ new Item(17478, 2), new Item(17447, 1)}, new Item(16745), Skills.CRAFTING),
    DUSKWEED_SHIELD(56, 180.3, new Item[]
            	{ new Item(17478, 3), new Item(17447, 1)}, new Item(27734), Skills.CRAFTING),
    DUSKWEED_ROBE_BOTTOM(56, 180.3, new Item[]
            	{ new Item(17478, 3), new Item(17447, 1)}, new Item(16855), Skills.CRAFTING),
    DUSKWEED_ROBE_TOP(58, 309, new Item[]
            	{ new Item(17478, 5), new Item(17447, 1)}, new Item(17226), Skills.CRAFTING),
    DUSKWEED_ORB(58, 61.8, new Item[]
            	{ new Item(17478, 1), new Item(17447, 1)}, new Item(27947), Skills.CRAFTING),
    SOULBELL_GLOVES(60, 63.5, new Item[]
            { new Item(17480, 1), new Item(17447, 1)}, new Item(17163), Skills.CRAFTING),
    SOULBELL_SHOES(62, 65.2, new Item[]
        	{ new Item(17480, 1), new Item(17447, 1)}, new Item(16920), Skills.CRAFTING),
    SOULBELL_HOOD(64, 133.8, new Item[]
            	{ new Item(17480, 2), new Item(17447, 1)}, new Item(16747), Skills.CRAFTING),
    SOULBELL_SHIELD(66, 205.8, new Item[]
            	{ new Item(17480, 3), new Item(17447, 1)}, new Item(27736), Skills.CRAFTING),
    SOULBELL_ROBE_BOTTOM(66, 205.8, new Item[]
            	{ new Item(17480, 3), new Item(17447, 1)}, new Item(16857), Skills.CRAFTING),
    SOULBELL_ROBE_TOP(68, 351.5, new Item[]
            	{ new Item(17480, 5), new Item(17447, 1)}, new Item(17228), Skills.CRAFTING),
    SOULBELL_ORB(68, 70.3, new Item[]
            	{ new Item(17480, 1), new Item(17447, 1)}, new Item(27949), Skills.CRAFTING),
    ECTO_GLOVES(70, 72, new Item[]
            { new Item(17482, 1), new Item(17447, 1)}, new Item(17165), Skills.CRAFTING),
    ECTO_SHOES(72, 73.7, new Item[]
        	{ new Item(17482, 1), new Item(17447, 1)}, new Item(16922), Skills.CRAFTING),
    ECTO_HOOD(74, 150.8, new Item[]
            	{ new Item(17482, 2), new Item(17447, 1)}, new Item(16749), Skills.CRAFTING),
    ECTO_SHIELD(76, 231.3, new Item[]
            	{ new Item(17482, 3), new Item(17447, 1)}, new Item(27738), Skills.CRAFTING),
    ECTO_ROBE_BOTTOM(76, 231.3, new Item[]
            	{ new Item(17482, 3), new Item(17447, 1)}, new Item(16859), Skills.CRAFTING),
    ECTO_ROBE_TOP(78, 394, new Item[]
            	{ new Item(17482, 5), new Item(17447, 1)}, new Item(17230), Skills.CRAFTING),
    ECTO_ORB(78, 78.3, new Item[]
            	{ new Item(17482, 1), new Item(17447, 1)}, new Item(27951), Skills.CRAFTING),
    RUNIC_GLOVES(80, 80.5, new Item[]
            { new Item(17484, 1), new Item(17447, 1)}, new Item(17167), Skills.CRAFTING),
    RUNIC_SHOES(82, 82.2, new Item[]
        	{ new Item(17484, 1), new Item(17447, 1)}, new Item(16924), Skills.CRAFTING),
    RUNIC_HOOD(84, 167.8, new Item[]
            	{ new Item(17484, 2), new Item(17447, 1)}, new Item(16751), Skills.CRAFTING),
    RUNIC_SHIELD(86, 256.8, new Item[]
            	{ new Item(17484, 3), new Item(17447, 1)}, new Item(27740), Skills.CRAFTING),
    RUNIC_ROBE_BOTTOM(86, 256.8, new Item[]
            	{ new Item(17484, 3), new Item(17447, 1)}, new Item(16861), Skills.CRAFTING),
    RUNIC_ROBE_TOP(88, 436.5, new Item[]
            	{ new Item(17484, 5), new Item(17447, 1)}, new Item(17232), Skills.CRAFTING),
    RUNIC_ORB(88, 87.3, new Item[]
            	{ new Item(17484, 1), new Item(17447, 1)}, new Item(27953), Skills.CRAFTING),
    SPIRITBLOOM_GLOVES(90, 89, new Item[]
            { new Item(17486, 1), new Item(17447, 1)}, new Item(17169), Skills.CRAFTING),
    SPIRITBLOOM_SHOES(92, 90.7, new Item[]
        	{ new Item(17486, 1), new Item(17447, 1)}, new Item(16926), Skills.CRAFTING),
    SPIRITBLOOM_HOOD(94, 184.8, new Item[]
            	{ new Item(17486, 2), new Item(17447, 1)}, new Item(16753), Skills.CRAFTING),
    SPIRITBLOOM_SHIELD(96, 282.3, new Item[]
            	{ new Item(17486, 3), new Item(17447, 1)}, new Item(27742), Skills.CRAFTING),
    SPIRITBLOOM_ROBE_BOTTOM(96, 282.3, new Item[]
            	{ new Item(17486, 3), new Item(17447, 1)}, new Item(16863), Skills.CRAFTING),
    SPIRITBLOOM_ROBE_TOP(98, 479, new Item[]
            	{ new Item(17486, 5), new Item(17447, 1)}, new Item(17234), Skills.CRAFTING),
    SPIRITBLOOM_ORB(98, 95.8, new Item[]
            	{ new Item(17486, 1), new Item(17447, 1)}, new Item(27955), Skills.CRAFTING);

	public static CraftMageDung getBarByProduce(int id) {
	    for(CraftMageDung cape : CraftMageDung.values()) {
		if(cape.getProducedBar().getId() == id)
		    return cape;
	    }
	    return null;
	}

	public static CraftMageDung getBar(int id) {
	    for(CraftMageDung cape : CraftMageDung.values()) {
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

	private CraftMageDung(int levelRequired, double experience, Item[] itemsRequired, Item producedBar, int skillType) {
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

    public CraftMageDung cape;
    public Item item;
    public int ticks;
    private int xpMultiplier = 1;

    public DungeoneeringMagicCrafting(CraftMageDung cape, Item item, int ticks) {
	this.cape = cape;
	this.item = item;
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
	player.setNextAnimation(new Animation(25594));


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
