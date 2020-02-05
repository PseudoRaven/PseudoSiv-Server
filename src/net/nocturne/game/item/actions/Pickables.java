package net.nocturne.game.item.actions;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

/**
 * Picking up things that are stackable and pickable.
 * 
 * @author Arham Siddiqui
 * @author Miles Black
 */
public class Pickables {

	/**
	 * Picks up the Pickable.
	 * 
	 * @param player
	 *            The Player picking up the Pickable.
	 * @param pickable
	 *            The Pickable item to pick.
	 */
	private static void pick(final Player player, final WorldObject object,
			final Pickable pickable) {
		if (player.getInventory().getFreeSlots() > 0) {
			player.setNextAnimation(new Animation(827));
			player.lock(2);
			World.removeObjectTemporary(object, 60000);
			player.getInventory()
					.addItemMoneyPouch(pickable.getHarvestedItem());
			if (pickable == Pickable.FLAX)
				player.getSkills().addXp(Skills.FARMING, 1);
		}
	}

	/**
	 * Handles if the object is a pickable.
	 * 
	 * @param player
	 *            The player picking the object.
	 * @param object
	 *            The literal pickable.
	 * @return If the object is a pickable.
	 */
	public static boolean handlePickable(Player player, WorldObject object) {
		for (Pickable pickable : Pickable.values()) {
			for (int i = 0; i < pickable.getObjectIds().length; i++) {
				if (pickable.getObjectIds()[i] == object.getId()
						|| object.getDefinitions().name.toLowerCase()
								.equalsIgnoreCase(
										pickable.name().toLowerCase()
												.replace("_", " "))) {
					pick(player, object, pickable);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The single pickable.
	 */
	private enum Pickable {

		CADAVA_BUSH(new int[] { 23625, 32626, 32627, 16531 }, new Item(
				ItemIdentifiers.CADAVA_BERRIES)), REDBERRY_BUSH(new int[] {
				23628, 23629, 23630 }, new Item(ItemIdentifiers.REDBERRIES)), PINAPPLE(
				new int[] { 4827 }, new Item(2114)), RUMBERRY_BUSH(
				new int[] { 104662 }, new Item(ItemIdentifiers.RUMBERRY)), ONION(
				new int[] { 3366, 5538, 8584 }, new Item(1957)), CABBAGE(
				new int[] { 1161, 8535, 8536, 8537, 8538, 8539, 8540, 8541,
						8542, 8543, 11494, 22301 }, new Item(1965)), WHEAT(
				new int[] { 313, 5583, 5584, 5585, 15506, 15507, 15508, 22300 },
				new Item(1947)), POTATO(new int[] { 312, 8562, 9408 },
				new Item(1942)), FLAX(new int[] { 2646, 15075, 15076, 15077,
				15078, 67264, 67263 }, new Item(1779));

		private int[] objectIds;
		private Item harvestedItem;

		Pickable(int[] objectIds, Item harvestedItem) {
			this.objectIds = objectIds;
			this.harvestedItem = harvestedItem;
		}

		public int[] getObjectIds() {
			return objectIds;
		}

		public Item getHarvestedItem() {
			return harvestedItem;
		}
	}
}