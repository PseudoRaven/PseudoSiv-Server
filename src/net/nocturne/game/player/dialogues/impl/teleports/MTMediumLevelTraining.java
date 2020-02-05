package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTMediumLevelTraining extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Medium Level Training", "Lumbridge Catacombs",
				"White Wolf Mountain", "Experiments", "Mountain Trolls",
				"Other");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3246,
						3198, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2846,
						3497, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3576,
						9927, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2875,
						3578, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Mediun Level Training - 2",
						"Chaos Druids", "Ardougne Training Camp",
						"VyreWatches", "None");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2928,
						9844, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2517,
						3356, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3599,
						3281, 0));
			} else if (componentId == OPTION_4) {
				end();
			}
		}

	}

	@Override
	public void finish() {

	}
}