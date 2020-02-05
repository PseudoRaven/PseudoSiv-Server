package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTLowLevelTraining extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				sendOptionsDialogue("Which Area?", "North", "East");
				stage = 1;
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2325,
						3795, 0));
			} else if (componentId == OPTION_3) {
				sendOptionsDialogue("Which Area?", "East", "West");
				stage = 2;
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3500,
						3540, 0));
				end();
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Low Level Training - 2", "Bandit Camp",
						"Gnome Khazard Battlefield", "None");
				stage = 4;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3175,
						3317, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3260,
						3264, 0));
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2704,
						3718, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2678,
						3717, 0));
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3416,
						3511, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3436,
						3466, 0));
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3170,
						2981, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2527,
						3202, 0));
			} else if (componentId == OPTION_3) {
				end();
			}
		}

	}

	@Override
	public void start() {
		sendOptionsDialogue("Low Level Training", "Cows", "Yaks", "Rock Crabs",
				"Ghouls", "Other");
		stage = -1;
	}
}