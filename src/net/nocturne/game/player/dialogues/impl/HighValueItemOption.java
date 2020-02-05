package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public class HighValueItemOption extends Dialogue {

	public Item item;

	@Override
	public void start() {
		item = (Item) parameters[0];
		sendOptionsDialogue(
				"Drop " + item.getName() + " (worth:"
						+ Utils.format(GrandExchange.getPrice(item.getId()))
						+ " gp)?", "Yes, drop it!", "No, don't!");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			player.getInventory().deleteItem(item);
			if (player.getCharges().degradeCompletely(item, true) != -1)
				item.setId(player.getCharges().degradeCompletely(item, false));
			if (player.isBeginningAccount()) {
				World.addGroundItem(item, new WorldTile(player), player, true,
						60, 2, 0);
			} else if (player.getControllerManager().getController() instanceof Wilderness
					&& ItemConstants.isTradeable(item))
				World.addGroundItem(item, new WorldTile(player), player, false,
						-1);
			else
				World.addGroundItem(item, new WorldTile(player), player, true,
						60);
			Logger.globalLog(player.getUsername(), player.getSession().getIP(),
					new String(" has dropped item [ id: " + item.getId()
							+ ", amount: " + item.getAmount() + " ]."));
			end();
		} else if (componentId == OPTION_2) {
			end();
		}
	}

	@Override
	public void finish() {

	}
}