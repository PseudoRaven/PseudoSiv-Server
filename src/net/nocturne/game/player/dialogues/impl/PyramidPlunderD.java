package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.content.activities.minigames.PyramidPlunder;
import net.nocturne.game.player.dialogues.Dialogue;

public class PyramidPlunderD extends Dialogue {

	/**
	 * @author: miles M
	 */

	private PyramidPlunder pyramidPlunder;

	@Override
	public void start() {
		pyramidPlunder = (PyramidPlunder) parameters[0];
		stage = 1;
		sendOptionsDialogue("Really leave the pyramid?",
				"Yes, I'm out of here.",
				"Ah, I think I'll stay a little longer.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				pyramidPlunder.reset();
				player.setNextWorldTile(pyramidPlunder.OUTSIDE);
				player.closeInterfaces();
				pyramidPlunder.removeController();
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}