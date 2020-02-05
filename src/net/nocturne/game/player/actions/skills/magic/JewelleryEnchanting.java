package net.nocturne.game.player.actions.skills.magic;

import java.util.HashMap;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class JewelleryEnchanting extends Action {

	public enum JewelleryAction {

		RING_OF_RECOIL(7, 17.5, 24317, 5054, new Item[] { new Item(1637),
				new Item(564), new Item(555) }, new Item(2550), Skills.MAGIC), GAMES_NECKLACE(
				7, 17.5, 24310, 5054, new Item[] { new Item(1656),
						new Item(564), new Item(555) }, new Item(3853),
				Skills.MAGIC), BRACELET_OF_CLAY(7, 17.5, 24317, 5054,
				new Item[] { new Item(11072), new Item(564), new Item(555) },
				new Item(11074), Skills.MAGIC), AMULET_OF_MAGIC(7, 17.5, 24310,
				5054,
				new Item[] { new Item(1694), new Item(564), new Item(555) },
				new Item(1727), Skills.MAGIC), CASTLE_WARS_BRACE(27, 37, 24317,
				5054, new Item[] { new Item(11076), new Item(564),
						new Item(556, 3) }, new Item(11079), Skills.MAGIC), RING_OF_DUELLING(
				27, 37, 24317, 5054, new Item[] { new Item(1639),
						new Item(564), new Item(556, 3) }, new Item(2552),
				Skills.MAGIC), BINDING_NECKLACE(27, 37, 24311, 5054,
				new Item[] { new Item(1658), new Item(564), new Item(556, 3) },
				new Item(5521), Skills.MAGIC), AMULET_OF_DEFENCE(27, 37, 24311,
				5054, new Item[] { new Item(1696), new Item(564),
						new Item(556, 3) }, new Item(1729), Skills.MAGIC), AMULET_OF_NATURE(
				27, 37, 24311, 5054, new Item[] { new Item(6041),
						new Item(564), new Item(556, 3) }, new Item(6040),
				Skills.MAGIC), RING_OF_FORGING(49, 59, 24317, 5054, new Item[] {
				new Item(1641), new Item(564), new Item(554, 5) }, new Item(
				2568), Skills.MAGIC), INOCULATION_BRACE(
				49,
				59,
				24317,
				5054,
				new Item[] { new Item(11087), new Item(564), new Item(554, 5) },
				new Item(11090), Skills.MAGIC), AMULET_OF_STRENGTH(49, 59,
				24312, 5054, new Item[] { new Item(1698), new Item(564),
						new Item(554, 5) }, new Item(1725), Skills.MAGIC), DIGSITE_PENDANT(
				49, 59, 24312, 5054, new Item[] { new Item(1660),
						new Item(564), new Item(554, 5) }, new Item(11194),
				Skills.MAGIC), RING_OF_LIFE(57, 67, 24317, 5054, new Item[] {
				new Item(1643), new Item(564), new Item(557, 10) }, new Item(
				2570), Skills.MAGIC), PHOENIX_NECKLACE(
				57,
				67,
				24313,
				5054,
				new Item[] { new Item(1662), new Item(564), new Item(557, 10) },
				new Item(11090), Skills.MAGIC), FORINTHRY_BRACELET(57, 67,
				24317, 5054, new Item[] { new Item(11092), new Item(564),
						new Item(557, 10) }, new Item(11095), Skills.MAGIC), AMULET_OF_POWER(
				57, 67, 24313, 5054, new Item[] { new Item(1700),
						new Item(564), new Item(557, 10) }, new Item(1731),
				Skills.MAGIC), RING_OF_WEALTH(68, 78, 24317, 5054, new Item[] {
				new Item(1645), new Item(564), new Item(557, 15),
				new Item(555, 15) }, new Item(2572), Skills.MAGIC), SKILLS_NECKLACE(
				68, 78, 24314, 5054, new Item[] { new Item(1664),
						new Item(564), new Item(557, 15), new Item(555, 15) },
				new Item(11113), Skills.MAGIC), COMBAT_BRACELET(68, 78, 24317,
				5054, new Item[] { new Item(11115), new Item(564),
						new Item(557, 15), new Item(555, 15) },
				new Item(11126), Skills.MAGIC), AMULET_OF_GLORY(68, 78, 24314,
				5054, new Item[] { new Item(1702), new Item(564),
						new Item(557, 15), new Item(555, 15) }, new Item(1704),
				Skills.MAGIC), RING_OF_STONE(87, 97, 24317, 5054, new Item[] {
				new Item(6575), new Item(564), new Item(557, 20),
				new Item(554, 20) }, new Item(6583), Skills.MAGIC), BERSERKER_NECKLACE(
				87, 97, 24317, 5054, new Item[] { new Item(6577),
						new Item(564), new Item(557, 20), new Item(554, 20) },
				new Item(11126), Skills.MAGIC), REGEN_BRACELET(87, 97, 24317,
				5054, new Item[] { new Item(11130), new Item(564),
						new Item(557, 20), new Item(554, 20) },
				new Item(11133), Skills.MAGIC), AMULET_OF_FURY(87, 97, 24315,
				5054, new Item[] { new Item(6581), new Item(564),
						new Item(557, 20), new Item(554, 20) }, new Item(6585),
				Skills.MAGIC), RING_OF_DEATH(87, 97, 24317, 5054, new Item[] {
				new Item(31857), new Item(564), new Item(557, 20),
				new Item(554, 20) }, new Item(31869), Skills.MAGIC), REAPER_NECKLACE(
				87, 97, 24319, 5054, new Item[] { new Item(31859),
						new Item(564), new Item(557, 20), new Item(554, 20) },
				new Item(31872), Skills.MAGIC), DEATHTOUCH_BRACELET(87, 97,
				24317, 5054, new Item[] { new Item(31865), new Item(564),
						new Item(557, 20), new Item(554, 20) },
				new Item(31878), Skills.MAGIC), AMULET_OF_SOULS(87, 97, 24319,
				5054, new Item[] { new Item(31863), new Item(564),
						new Item(557, 20), new Item(554, 20) },
				new Item(31875), Skills.MAGIC);

		/**
		 * Silver Enchanting RING_OF_METAMORPHOSIS(4, 9, 24317, 5054, new
		 * Item[]{ new Item(879, 10), new Item (564), new Item(556, 2)}, new
		 * Item(9236, 10), Skills.MAGIC), FEATHERFINGERED_NECKLACE(4, 9, 24317,
		 * 5054, new Item[]{ new Item(879, 10), new Item (564), new Item(556,
		 * 2)}, new Item(9236, 10), Skills.MAGIC),
		 * AMULET_OF_BOUNTIFUL_HARVEST(4, 9, 24317, 5054, new Item[]{ new
		 * Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236, 10),
		 * Skills.MAGIC), RING_OF_RESPAWN(27, 9, 24317, 5054, new Item[]{ new
		 * Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236, 10),
		 * Skills.MAGIC), TRAVELLERS_NECKLACE(27, 9, 24317, 5054, new Item[]{
		 * new Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236,
		 * 10), Skills.MAGIC), FLAMTAER_BRACELET(4, 9, 24317, 5054, new Item[]{
		 * new Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236,
		 * 10), Skills.MAGIC), BOTANIST_AMULET(4, 9, 24317, 5054, new Item[]{
		 * new Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236,
		 * 10), Skills.MAGIC), COLUMBARIUM_RING(4, 9, 24317, 5054, new Item[]{
		 * new Item(879, 10), new Item (564), new Item(556, 2)}, new Item(9236,
		 * 10), Skills.MAGIC), NECKLACE_OF_GLUTTONY(4, 9, 24317, 5054, new
		 * Item[]{ new Item(879, 10), new Item (564), new Item(556, 2)}, new
		 * Item(9236, 10), Skills.MAGIC), HEADHUNTER_THRILL(4, 9, 24317, 5054,
		 * new Item[]{ new Item(879, 10), new Item (564), new Item(556, 2)}, new
		 * Item(9236, 10), Skills.MAGIC), ENLIGHTENED_AMULET(4, 9, 24317, 5054,
		 * new Item[]{ new Item(879, 10), new Item (564), new Item(556, 2)}, new
		 * Item(9236, 10), Skills.MAGIC),
		 */

		public static JewelleryAction getBarByProduce(int id) {
			for (JewelleryAction enchant : JewelleryAction.values()) {
				if (enchant.getProducedBow().getId() == id)
					return enchant;
			}
			return null;
		}

		public static JewelleryAction getBar(int id) {
			for (JewelleryAction enchant : JewelleryAction.values()) {
				for (Item item : enchant.getItemsRequired())
					if (item.getId() == id)
						return enchant;
			}
			return null;
		}

		private static final HashMap<Integer, JewelleryAction> enchantSTUFF = new HashMap<Integer, JewelleryAction>();

		static {
			for (JewelleryAction f : values()) {
				enchantSTUFF.put(f.getItemsRequired()[0].getId(), f);
			}
		}

		public static JewelleryAction getenchantItem(int id) {
			return enchantSTUFF.get(id);
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedBow;
		private int skillType;
		private int emoteId;
		private int graphicsId;

		private JewelleryAction(int levelRequired, double experience,
				int emoteId, int graphicsId, Item[] itemsRequired,
				Item producedBow, int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBow = producedBow;
			this.emoteId = emoteId;
			this.graphicsId = graphicsId;
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

		public int getEmoteId() {
			return emoteId;
		}

		public int getGraphicsId() {
			return graphicsId;
		}
	}

	public JewelleryAction enchant;
	public int ticks;
	private int xpMultiplier = 1;

	public JewelleryEnchanting(JewelleryAction enchant, int ticks) {
		this.enchant = enchant;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (enchant == null || player == null) {
			return false;
		}
		for (Item req : enchant.getItemsRequired())
			if (!player.getInventory().containsItem(req.getId(), 1)) {
				player.getPackets()
						.sendGameMessage(
								"This spell can only be cast on amulets, necklaces, rings, braclets or shapes in the training arena.");
				return false;
			}
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
		if (!player.getInventory().containsItemToolBelt(
				enchant.getItemsRequired()[0].getId(),
				enchant.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}

		if (enchant.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					enchant.getItemsRequired()[1].getId(),
					enchant.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (player.getSkills().getLevel(enchant.getSkillType()) < enchant
				.getLevelRequired()) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (ticks <= 0) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		return true;
	}

	public static JewelleryAction isAttaching(Player player, Item used,
			Item usedWith) {
		JewelleryAction enchant = null;
		enchant = JewelleryAction.getenchantItem(used.getId());
		Item selected;
		if (enchant != null)
			selected = usedWith;
		else {
			enchant = JewelleryAction.getenchantItem(usedWith.getId());
			selected = used;
		}
		System.out.println(enchant);
		if (enchant != null)
			return enchant;
		else
			return null;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 1;
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int emoteId = enchant.getEmoteId();
		int graphicsId = enchant.getGraphicsId();
		double xp = enchant.getExperience();
		int amount = enchant.getProducedBow().getAmount() * multiplier;
		for (Item required : enchant.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, enchant.getProducedBow().getId(), maxQuantity, (int) xp);
		player.getPackets().sendExecuteScript(3373, 1018);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(enchant.getProducedBow().getId(), amount);
		if (player.getClanManager() != null
				&& player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
		player.getSkills().addXp(enchant.getSkillType(), xp);
		player.setNextAnimation(new Animation(emoteId));
		player.setNextGraphics(new Graphics(graphicsId));
		return 0;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);

	}

}
