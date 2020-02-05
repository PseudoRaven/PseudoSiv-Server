package net.nocturne.game.player.content;

import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class RecipeShop {
	private static int INTER = 1555;

	public static void handleButtons(Player player, int componentId, int slotId) {
		if (componentId == 8) {
			if (slotId >= 0 && slotId <= 19) {
				player.getTemporaryAttributtes().put("VarId", 25954 + slotId);
			} else if (slotId >= 20 && slotId <= 21) {
				player.getTemporaryAttributtes().put("VarId",
						25978 + (slotId - 20));
			} else
				player.getTemporaryAttributtes().put("VarId",
						25974 + (slotId - 22));
			player.getTemporaryAttributtes().put("SelectedSlot", slotId);
		} else if (componentId == 22) {
			slotId = (int) player.getTemporaryAttributtes().remove(
					"SelectedSlot");
			int price = (slotId >= 0 && slotId <= 4) ? 100000
					: (slotId == 5) ? 150000
							: (slotId >= 6 && slotId <= 8) ? 200000
									: (slotId >= 9 && slotId <= 11) ? 300000
											: (slotId == 12) ? 400000
													: (slotId >= 13 && slotId <= 17) ? 500000
															: (slotId == 18) ? 650000
																	: (slotId == 19) ? 750000
																			: (slotId == 20 || slotId == 21) ? 700000
																					: (slotId == 22) ? 800000
																							: (slotId == 23) ? 900000
																									: 1000000;
			if (player.getInventory()
					.removeItemMoneyPouch(new Item(995, price))) {
				player.getVarbits().put(
						(int) player.getTemporaryAttributtes().remove("VarId"),
						1);
				player.getPackets()
						.sendEntityMessage(0, 15263739, player,
								"You have unlocked the recipe for this combination potion.");
			} else
				player.getPackets().sendGameMessage(
						"You don't have enough money to buy this recipe.");
		}
	}

	public static void sendInterface(final Player player) {
		player.getInterfaceManager().sendCentralInterface(INTER);
		player.getPackets().sendIComponentSettings(INTER, 8, 0, 25, 0x2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				stop();
			}
		}, 0, 1);
	}

	public static void handleRecipe(Player player) {
		if (!player.getInventory().containsItem(
				ItemIdentifiers.MEILYR_POTION_RECIPE, 1))
			return;
		int varbit = Utils.random(25914, 25922);
		if (player.getVarbits().containsKey(varbit)) {
			handleRecipe(player);
			return;
		}
		player.getInventory().removeItemMoneyPouch(
				ItemIdentifiers.MEILYR_POTION_RECIPE, 1);
		player.getVarbits().put(varbit, 1);
		player.loadVarbits();
		player.getPackets().sendGameMessage("You have unlocked a new recipe!");
	}
}
