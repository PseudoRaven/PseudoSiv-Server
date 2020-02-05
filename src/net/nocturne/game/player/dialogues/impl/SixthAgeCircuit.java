package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.content.FadingScreen;
import net.nocturne.game.player.dialogues.Dialogue;

public class SixthAgeCircuit extends Dialogue {

	/**
	 * @author: miles M
	 */

	@Override
	public void start() {
		sendOptionsDialogue("Choose your destination", "Guthix's Shine.",
				"Cancel.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (componentId) {
		case OPTION_1:
			Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1923,
					5987, 0));
			FadingScreen.unfade(player,
					FadingScreen.fade(player, FadingScreen.TICK / 2),
					new Runnable() {
						@Override
						public void run() {

						}
					});
			end();
			break;
		case OPTION_2:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}