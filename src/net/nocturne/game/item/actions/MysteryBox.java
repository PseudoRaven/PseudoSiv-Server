package net.nocturne.game.item.actions;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 */

public class MysteryBox {

	public static void openBox(final Player player, final Item item) {
		player.getInventory().removeItemMoneyPouch(item);
		player.getPackets().sendGameMessage("The mystery box slowly opens...");
		reward = rewards[Utils.random(rewards.length - 1)];
		player.getInventory().addItemMoneyPouch(reward);
		player.getPackets().sendGameMessage("You receive a " + reward.getName() + "!");
	}

	private static Item reward;

	private static Item[] rewards = { new Item(995, Utils.random(100000)),
		new Item(439, Utils.random(1000)), new Item(441, Utils.random(1000)),
		new Item(443, Utils.random(1000)), new Item(445, Utils.random(500)),
		new Item(448, Utils.random(300)), new Item(450, Utils.random(150)),
		new Item(452, Utils.random(50)), new Item(2350, Utils.random(1000)),
		new Item(2352, Utils.random(500)), new Item(2354, Utils.random(500)),
		new Item(2356, Utils.random(750)), new Item(2358, Utils.random(300)),
		new Item(2360, Utils.random(200)), new Item(2362, Utils.random(100)),
		new Item(2364, Utils.random(50)), new Item(1512, Utils.random(1000)),
		new Item(1514, Utils.random(150)), new Item(1516, Utils.random(400)),
		new Item(1518, Utils.random(1000)),
		new Item(1520, Utils.random(1000)),
		new Item(1522, Utils.random(8000)), new Item(384, Utils.random(500)),
		new Item(390, Utils.random(500)), new Item(396, Utils.random(500)),
		new Item(15271, Utils.random(500)),
		new Item(15265, Utils.random(500)), new Item(7945, Utils.random(500)) };

}
