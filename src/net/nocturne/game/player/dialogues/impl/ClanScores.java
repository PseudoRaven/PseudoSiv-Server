package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.activities.clans.ClanRank;
import net.nocturne.game.player.dialogues.Dialogue;

/**
 * @author Danny
 */

public class ClanScores extends Dialogue {

	public ClanScores() {
	}

	@Override
	public void finish() {

	}

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Choose a Leaderboard", "Gathered Resources",
				"Monster Kills", "Player Kills");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		if (stage == 1) {
			if (componentId == OPTION_1)
				ClanRank.showRanks(player, 0);
			else if (componentId == OPTION_2)
				ClanRank.showRanks(player, 1);
			else if (componentId == OPTION_3)
				ClanRank.showRanks(player, 2);
			end();
		}
	}

}