package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTMediumLevelBosses extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Medium Level Bosses", "King Black Dragon", "Bork",
				"Kalphite Queen", "Dagannoth Kings", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3067,
						10254, 0));
			} else if (componentId == OPTION_2) {
				player.getControllerManager().startController("BorkController",
						0, null);
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3420,
						9510, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2544,
						10143, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Medium Level Bosses  - 2",
						"Tormented Demons",
						"Chaos Elemental <col=FF0000>(Wilderness)",
						"Evil Chicken", "Giant Mole", "None");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2564,
						5739, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3285,
						3909, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2643,
						10413, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2987,
						3382, 0));
			} else
				end();
		}

	}

	@Override
	public void finish() {

	}
}
