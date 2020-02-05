package net.nocturne.game.player.dialogues.impl;

import net.nocturne.Settings;
import net.nocturne.game.player.actions.skills.runecrafting.Runecrafting;
import net.nocturne.game.player.dialogues.Dialogue;

public class Teshmezon extends Dialogue {

	/**
	 * @author: miles M
	 */

	@Override
	public void start() {
		npcId = (int) parameters[0];
		stage = 1;
		sendEntityDialogue(1, "Teshmezon", npcId, HAPPY,
				"I can offer you to teleport to most altars around "
						+ Settings.SERVER_NAME + ".");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 1:
			stage = 2;
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Water Altar.",
					"Mind Altar.", "Body Altar.", "Earth Altar.", "More.");
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				Runecrafting.enterWaterAltar(player);
				end();
				break;
			case OPTION_2:
				Runecrafting.enterMindAltar(player);
				end();
				break;
			case OPTION_3:
				Runecrafting.enterBodyAltar(player);
				end();
				break;
			case OPTION_4:
				Runecrafting.enterEarthAltar(player);
				end();
				break;
			case OPTION_5:
				stage = 3;
				sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Fire Altar.",
						"Nature Altar.", "Cosmic Altar.", "Blood Altar.",
						"Chaos Altar.");
				break;
			}
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				Runecrafting.enterFireAltar(player);
				end();
				break;
			case OPTION_2:
				Runecrafting.enterNatureAltar(player);
				end();
				break;
			case OPTION_3:
				Runecrafting.enterCosmicAltar(player);
				end();
				break;
			case OPTION_4:
				Runecrafting.enterBloodAltar(player);
				end();
				break;
			case OPTION_5:
				Runecrafting.enterChaosAltar(player);
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