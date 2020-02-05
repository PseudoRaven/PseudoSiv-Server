package net.nocturne.game.player.dialogues.impl.rottenpotato;

import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Utils;

public class OP1 extends Dialogue {

	/**
	 * @author: miles M
	 */

	@Override
	public void start() {
		sendOptionsDialogue("Rotten Potato - Option: 1",
				"Teleport to location.", "Teleport to Player.",
				"Teleport Player to me.", "Gear Setup.", "Idle Logout.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (componentId) {
		case OPTION_1:
			if (player.getRights() < 2) {
				end();
				return;
			}
			player.getPackets().sendInputLongTextScript(
					"Please enter the coordinates:");
			player.getTemporaryAttributtes().put("fc", Boolean.TRUE);
			end();
			break;
		case OPTION_2:
			if (player.getRights() < 2) {
				end();
				return;
			}
			player.getPackets().sendInputLongTextScript(
					"Please enter their Display name:");
			player.getTemporaryAttributtes().put("fc_to", Boolean.TRUE);
			end();
			break;
		case OPTION_3:
			if (player.getRights() < 2) {
				end();
				return;
			}
			player.getPackets().sendInputLongTextScript(
					"Please enter their Display name:");
			player.getTemporaryAttributtes().put("fc_tome", Boolean.TRUE);
			end();
			break;
		case OPTION_4:
			if (player.getRights() < 2) {
				end();
				return;
			}
			player.getDialogueManager().startDialogue("OPGear");
			break;
		case OPTION_5:
			if (player.getRights() < 2) {
				end();
				return;
			}
			player.afk = Utils.currentTimeMillis() + (Integer.MAX_VALUE);
			player.getPackets().sendGameMessage("setvar: afk to 2147483647.",
					true);
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}
}