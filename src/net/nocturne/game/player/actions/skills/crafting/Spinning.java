package net.nocturne.game.player.actions.skills.crafting;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class Spinning extends Action {

	public enum CreateSpin {

		BALL_OF_WOOL(1, 2.5, new Item[] { new Item(ItemIdentifiers.WOOL) },
				new Item(ItemIdentifiers.BALL_OF_WOOL), Skills.CRAFTING), BOWSTRING(
				1, 15, new Item[] { new Item(ItemIdentifiers.FLAX) }, new Item(
						ItemIdentifiers.BOWSTRING), Skills.CRAFTING), CROSSBOW_STRING(
				10, 15, new Item[] { new Item(ItemIdentifiers.SINEW) },
				new Item(ItemIdentifiers.CROSSBOW_STRING), Skills.CRAFTING), MAGIC_STRING(
				1, 2.5, new Item[] { new Item(ItemIdentifiers.MAGIC_ROOTS) },
				new Item(ItemIdentifiers.MAGIC_STRING), Skills.CRAFTING), ROPE(
				1, 2.5, new Item[] { new Item(ItemIdentifiers.HAIR) },
				new Item(ItemIdentifiers.ROPE), Skills.CRAFTING), SALVE(1, 2.5,
				new Item[] { new Item(ItemIdentifiers.SALVE_NETTLES) },
				new Item(ItemIdentifiers.SALVE_CLOTH), Skills.CRAFTING), WILDERCRESS(
				10, 9.2, new Item[] { new Item(ItemIdentifiers.WILDERCRESS) },
				new Item(ItemIdentifiers.WILDERCRESS_CLOTH), Skills.CRAFTING), BLIGHTLEAF(
				20, 13, new Item[] { new Item(ItemIdentifiers.BLIGHTLEAF) },
				new Item(ItemIdentifiers.BLIGHTLEAF_CLOTH), Skills.CRAFTING), ROSEBLOOD(
				30, 15, new Item[] { new Item(ItemIdentifiers.ROSEBLOOD) },
				new Item(ItemIdentifiers.ROSEBLOOD_CLOTH), Skills.CRAFTING), BRYLL(
				40, 18, new Item[] { new Item(ItemIdentifiers.BRYLL) },
				new Item(ItemIdentifiers.BRYLL_CLOTH), Skills.CRAFTING), DUSKWEED(
				50, 22, new Item[] { new Item(ItemIdentifiers.DUSKWEED) },
				new Item(ItemIdentifiers.DUSKWEED_CLOTH), Skills.CRAFTING), SOULBELL(
				60, 26, new Item[] { new Item(ItemIdentifiers.SOULBELL) },
				new Item(ItemIdentifiers.SOULBELL_CLOTH), Skills.CRAFTING), ECTOGRASS(
				70, 29, new Item[] { new Item(ItemIdentifiers.ECTOGRASS) },
				new Item(ItemIdentifiers.ECTOCLOTH), Skills.CRAFTING), RUNELEAF(
				80, 32, new Item[] { new Item(ItemIdentifiers.RUNELEAF) },
				new Item(ItemIdentifiers.RUNIC_CLOTH), Skills.CRAFTING), SPIRITBLOOM(
				90, 35, new Item[] { new Item(ItemIdentifiers.SPIRITBLOOM) },
				new Item(ItemIdentifiers.SPIRITBLOOM_CLOTH), Skills.CRAFTING);

		public static CreateSpin getSpinByProduce(int id) {
			for (CreateSpin spin : CreateSpin.values()) {
				if (spin.getProducedItem().getId() == id)
					return spin;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private Item producedItem;
		private int skillType;

		private CreateSpin(int levelRequired, double experience,
				Item[] itemsRequired, Item producedItem, int skillType) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedItem = producedItem;
			this.skillType = skillType;
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedItem() {
			return producedItem;
		}

		public double getExperience() {
			return experience;
		}

		public int getSkillType() {
			return skillType;
		}
	}

    private CreateSpin spin;
    private int ticks;
    private int xpMultiplier = 1;

	public Spinning(CreateSpin spin, int ticks) {
		this.spin = spin;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (spin == null || player == null)
			return false;
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (spin == null || player == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(spin.getItemsRequired()[0].getId(),
				spin.getItemsRequired()[0].getAmount())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (spin.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(spin.getItemsRequired()[1].getId(),
					spin.getItemsRequired()[1].getAmount())) {
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (!player.getSkills().hasLevel(spin.getSkillType(), spin.getLevelRequired())) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (!player.getInventory().containsItemToolBelt(
				spin.getItemsRequired()[0].getId(),
				spin.getItemsRequired()[0].getAmount())) {
			return false;
		}
		if (spin.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					spin.getItemsRequired()[1].getId(),
					spin.getItemsRequired()[1].getAmount())) {
				return false;
			}
		}
		if (!player.getSkills().hasLevel(spin.getSkillType(),
				spin.getLevelRequired())) {
			return false;
		}
		return true;
	}


	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int multiplier = 0;
		double xp = spin.getExperience();
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		int amount = spin.getProducedItem().getAmount() * multiplier;
		for (Item required : spin.getItemsRequired()) {
			player.getInventory().deleteItem(required.getId(),
					required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, spin.getProducedItem().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		player.getInventory().addItem(spin.getProducedItem().getId(), amount);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					spin.getProducedItem().getId(), Skills.CRAFTING);
		player.getSkills().addXp(spin.getSkillType(), xp);
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
