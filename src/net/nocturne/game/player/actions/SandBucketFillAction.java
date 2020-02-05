package net.nocturne.game.player.actions;

import net.nocturne.game.Animation;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;

public class SandBucketFillAction extends Action {

	public SandBucketFillAction() {

	}

	@Override
	public boolean start(Player player) {
		return true;
	}

	@Override
	public boolean process(Player player) {
		return player.getInventory().containsItem(ItemIdentifiers.EMPTY_BUCKET, 1);
	}

	@Override
	public int processWithDelay(Player player) {
		player.setNextAnimation(new Animation(895));
		player.getInventory().replaceItem( ItemIdentifiers.BUCKET_OF_SAND, 1, player.getInventory().getItems().getThisItemSlot(ItemIdentifiers.EMPTY_BUCKET));
		player.getPackets().sendGameMessage("You fill the bucket with sand.",true);
		return 3;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

}
