package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.player.dialogues.Dialogue;

public class DrakansMedallion extends Dialogue {

	/**
	 * @author: miles M
	 */

	@Override
	public void start() {
		stage = 1;
		sendOptionsDialogue("Letters glow dimly on the metal:", "Barrows.",
				"Burgh de Rott.", "Meiyerditch.", "Darkmeyer.",
				"Meiyerditch Laboratories.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 1:
			switch (componentId) {
			default:
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