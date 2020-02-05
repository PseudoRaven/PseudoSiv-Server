package net.nocturne.game.player.actions;

import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;

public class WaterFilling extends Action {

	public enum Fill {
		VIAL(ItemIdentifiers.EMPTY_VIAL, ItemIdentifiers.VIAL_OF_WATER), BOWL(
				ItemIdentifiers.BOWL, ItemIdentifiers.BOWL_OF_WATER), BUCKET(
				ItemIdentifiers.BUCKET, ItemIdentifiers.BUCKET_OF_WATER), JUG(
				ItemIdentifiers.JUG, ItemIdentifiers.JUG_OF_WATER), VASE(
				ItemIdentifiers.VASE, ItemIdentifiers.VASE_OF_WATER), PLANT_POT(
				ItemIdentifiers.PLANT_POT,
				ItemIdentifiers.PLANT_POT_FILLED_WATER), CLAY(
				ItemIdentifiers.CLAY, ItemIdentifiers.SOFT_CLAY), WATERING_CAN_0(
				ItemIdentifiers.WATERING_CAN, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_1(
				ItemIdentifiers.WATERING_CAN_1, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_2(
				ItemIdentifiers.WATERING_CAN_2, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_3(
				ItemIdentifiers.WATERING_CAN_3, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_4(
				ItemIdentifiers.WATERING_CAN_4, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_5(
				ItemIdentifiers.WATERING_CAN_5, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_6(
				ItemIdentifiers.WATERING_CAN_6, ItemIdentifiers.WATERING_CAN_8), WATERING_CAN_7(
				ItemIdentifiers.WATERING_CAN_7, ItemIdentifiers.WATERING_CAN_8), DUNGEONEERING_VIAL(
				ItemIdentifiers.DUNG_VIAL, ItemIdentifiers.DUNG_VIAL_OF_WATER);

		public int empty;
		private int full;

		Fill(int empty, int full) {
			this.empty = empty;
			this.full = full;
		}

		public int getEmpty() {
			return empty;
		}

		public int getFull() {
			return full;
		}

	}

	public static Fill getFillByProduce(int produce) {
		for (Fill fill : Fill.values()) {
			if (fill.full == produce)
				return fill;
		}
		return null;
	}

	public static boolean isFilling(Player player, int empty, boolean isSpot) {
		for (Fill fill : Fill.values()) {
			if (fill.empty == empty) {
				if (isSpot && fill.ordinal() <= 4)
					return false;
				fill(player, fill);
				return true;
			}
		}
		return false;
	}

	private static void fill(Player player, Fill fill) {
		if (player.getInventory().getItems()
				.getNumberOf(new Item(fill.empty, 1)) <= 1)
			player.getActionManager().setAction(new WaterFilling(fill, 1));
		else
			player.getDialogueManager().startDialogue("WaterFillingD", fill);
	}

	private Fill fill;
	private int quantity;

	public WaterFilling(Fill fill, int quantity) {
		this.fill = fill;
		this.quantity = quantity;
	}

	public boolean checkAll(Player player) {
		if (!player.getInventory().containsOneItem(fill.empty)) {
			player.getPackets().sendGameMessage(
					"You don't have any "
							+ ItemDefinitions.getItemDefinitions(fill.empty)
									.getName().toLowerCase() + " to fill.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			setActionDelay(player, 1);
			player.setNextAnimation(new Animation(832));
			String name = ItemDefinitions.getItemDefinitions(fill.full)
					.getName();
			if (name.contains(" ("))
				name = name.substring(0, name.indexOf(" ("));
			player.getPackets().sendGameMessage("You fill the " + name + ".");
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(fill.empty, 1);
		player.getInventory().addItem(fill.full, 1);
		quantity--;
		if (quantity <= 0)
			return -1;
		player.setNextAnimation(new Animation(fill.ordinal() == 5 ? 2272 : 832));
		return fill.ordinal() == 5 ? 3 : 0;
	}

	@Override
	public void stop(final Player player) {
		setActionDelay(player, 3);
	}
}