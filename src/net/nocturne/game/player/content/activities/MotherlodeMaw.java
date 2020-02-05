package net.nocturne.game.player.content.activities;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

/**
 * @author Miles (bobismyname)
 */

public class MotherlodeMaw {

	private static final Item[] commonRewards = new Item[] { new Item(995, Utils.random(10000, 30000)), new Item(985, 1), new Item(987, 1), new Item(989, 1) };
	private static final Item[] uncommonRewards = new Item[] { new Item(1213, 1), new Item(5288, 1), new Item(5289, 1), new Item(5315, 1), new Item(5316, 1), new Item(5317, 1), new Item(32947, Utils.random(5, 10)), new Item(32848, Utils.random(5, 10)), new Item(32958, 5), new Item(32970, 5), new Item(32982, 5), new Item(32994, 5), new Item(33006, 5), new Item(33018, 5), new Item(33030, 5), new Item(33042, 5), new Item(33054, 5), new Item(33066, 5), new Item(33078, 5), new Item(33090, 5), new Item(33102, 5), new Item(33114, 5), new Item(33126, 5), new Item(33138, 5), new Item(33150, 5), new Item(33162, 5), new Item(33174, 5), new Item(33186, 5), new Item(33210, 5), new Item(33234, 5), new Item(33246, 5), new Item(33258, 5), new Item(15513, 1), new Item(15515, 1), new Item(15517, 1), new Item(32272, 1) };
	private static final Item[] rareRewards = new Item[] { new Item(31796, 1), new Item(32623, 1), new Item(32208, 1), new Item(32206, 1), new Item(32262, 100), new Item(3140, 1), new Item(11732, 1), new Item(11335, 1), new Item(14479, 1), new Item(4087, 1), new Item(1187, 1), new Item(24365, 1), new Item(29534, 1), new Item(29543, 57), new Item(25758, 1), new Item(15259, 1), new Item(7158, 1), new Item(1434, 1), new Item(1305, 1), new Item(6739, 1), new Item(11212, 26), new Item(25555, 1), new Item(14484, 1), new Item(25932, 1), new Item(32228, 1), new Item(32231, 1), new Item(32234, 1), new Item(32219, 1), new Item(32225, 1) };

	public static boolean handleObject(Player player, WorldObject object) {
		if (object.getId() == 94273) {
			if (player.canUseMotherlode()) {
				if (player.getInventory().containsItem(31427, 10)) {
					player.setNextAnimation(new Animation(25028));
					player.getInventory().deleteItem(31427, 10);
					generateReward(player);
				} else
					player.getPackets().sendGameMessage("You must have at least 10 crystal motherlode shards.");
			} else
				player.getPackets().sendGameMessage("You must wait at least 24 hours before using the motherload again.");
			return true;
		}
		return false;
	}

	private static void generateReward(Player player) {
		int chance = Utils.random(100);
		if (chance <= 5)
			player.getInventory().addItem(rareRewards[Utils.random(0, rareRewards.length - 1)]);
		else if (chance > 5 && chance <= 50)
			player.getInventory().addItem(uncommonRewards[Utils.random(0, uncommonRewards.length - 1)]);
		if (chance > 50)
			player.getInventory().addItem(commonRewards[Utils.random(0, commonRewards.length - 1)]);
		player.setLastUsedMotherLode(Utils.currentTimeMillis());
	}

}