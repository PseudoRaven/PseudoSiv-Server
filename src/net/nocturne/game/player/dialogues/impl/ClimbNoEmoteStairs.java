package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.dialogues.Dialogue;

public class ClimbNoEmoteStairs extends Dialogue {

	private WorldTile upTile;
	private WorldTile downTile;

	// uptile, downtile, climbup message, climbdown message, emoteid
	@Override
	public void start() {
		upTile = (WorldTile) parameters[0];
		downTile = (WorldTile) parameters[1];
		sendOptionsDialogue("What would you like to do?",
				(String) parameters[2], (String) parameters[3], "Never mind.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1) {
			player.useStairs(-1, upTile, 0, 1);
		} else if (componentId == OPTION_2)
			player.useStairs(-1, downTile, 0, 1);
		end();
	}

	@Override
	public void finish() {

	}

}
