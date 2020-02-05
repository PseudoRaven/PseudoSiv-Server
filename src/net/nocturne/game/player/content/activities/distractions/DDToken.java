package net.nocturne.game.player.content.activities.distractions;

import java.io.Serializable;

import net.nocturne.game.player.Player;

public class DDToken implements Serializable {

	private static final long serialVersionUID = 3331602957234296986L;

	private Player player;

	public DDToken(Player player) {
		this.player = player;
	}

	public void claimToken(int itemId) {
		if (player.getInventory().containsItem(itemId, 1)) {
			switch (itemId) {
			case 27234: // daily
				resetDaily();
				break;
			case 27235: // weekly
				resetWeekly();
				break;
			case 27236: // monthly
				resetMonthly();
				break;
			}
		}
	}

	private void resetDaily() {
		player.getDailyTask().generateDailyTasks(player, true);
		player.getPackets().sendGameMessage(
				"You have set another Daily Challenge.");
		player.getInventory().deleteItem(27234, 1);
	}

	private void resetWeekly() {
		player.getPackets().sendGameMessage("TODO");
	}

	private void resetMonthly() {
		player.getPackets().sendGameMessage("TODO");
	}
}