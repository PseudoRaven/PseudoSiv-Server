package net.nocturne.game.player.dialogues.impl.cities.karamja;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.ShopsHandler;

/**
 * @Author arrow
 * @Contact<arrowrsps@gmail.com;skype:arrowrsps>
 */
public class Zambo extends Dialogue {

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		npc(NORMAL, "Hey, are you wanting to try some of my fine wines "
				+ "and spirits? ALl brewed locally on Karamja island.");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			sendOptionsDialogue(DEFAULT_OPTIONS_TITLE, "Yes please.",
					"No, thank you.");
			stage = 1;
			break;

		case 1:
			switch (componentId) {
			case OPTION_1:
				ShopsHandler.openShop(player, 172);
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
