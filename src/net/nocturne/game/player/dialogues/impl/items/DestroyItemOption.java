package net.nocturne.game.player.dialogues.impl.items;

import net.nocturne.game.item.Item;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ItemDestroys;

public class DestroyItemOption extends Dialogue {

	int slotId;
	Item item;

	@Override
	public void start() {
		slotId = (Integer) parameters[0];
		item = (Item) parameters[1];
		player.getInterfaceManager().sendDialogueInterface(1183);
		player.getPackets().sendIComponentText(1183, 4, item.getName());
		player.getPackets().sendHideIComponent(1183, 8,
				player.getInventory().getAmountOf(item.getId()) == 1);
		player.getPackets().sendIComponentText(1183, 9,
				ItemDestroys.getDestroy(item));
		player.getPackets().sendItemOnIComponent(1183, 10, item.getId(), 1);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (interfaceId == 1183 && (componentId == 6 || componentId == 8)) {
			slotId = this.slotId;
			if (componentId == 8) { // destroy all
				player.getInventory().deleteItem(
						new Item(item.getId(), player.getInventory()
								.getAmountOf(item.getId())));
			} else {
				player.getInventory().deleteItem(slotId, item);
				if (player.getDungManager().isInside()) {
					player.getDungManager().unbind(item);
				}
			}
			if (player.getCharges().degradeCompletely(item, true) != -1)
				item.setId(player.getCharges().degradeCompletely(item, false));
			if (item.getDefinitions().isBinded())
				player.getDungManager().unbind(item);
			end();
		}
	}

	@Override
	public void finish() {

	}

}
