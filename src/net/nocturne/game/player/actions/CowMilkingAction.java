package net.nocturne.game.player.actions;

import net.nocturne.game.Animation;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;

public class CowMilkingAction extends Action {

	public CowMilkingAction() {

	}

	@Override
	public boolean start(Player player) {
		if (!player.getInventory().containsItem(ItemIdentifiers.BUCKET, 1)) {
			player.getDialogueManager().startDialogue("GrilleGoatsD");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return player.getInventory().hasFreeSlots()
				&& player.getInventory()
						.containsItem(ItemIdentifiers.BUCKET, 1);
	}

	@Override
	public int processWithDelay(Player player) {
		player.setNextAnimation(new Animation(2305));
		player.getInventory().deleteItem(new Item(ItemIdentifiers.BUCKET, 1));
		player.getInventory().addItem(new Item(ItemIdentifiers.BUCKET_OF_MILK));
		player.getPackets().sendGameMessage("You milk the cow.");
		return 5;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
