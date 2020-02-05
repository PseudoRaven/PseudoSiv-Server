package net.nocturne.game.player.dialogues.impl.rottenpotato;

import net.nocturne.game.events.DropEvent;
import net.nocturne.game.player.dialogues.Dialogue;

public class OP3 extends Dialogue {

	/**
	 * @author: miles M
	 */

	@Override
	public void start() {
		stage = 0;
		sendOptionsDialogue("Rotten Potato - Option: 3", "Fairy Ring.",
				"NPC Spawns.", "Object Spawns.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 0:
			switch (componentId) {
			case OPTION_1:
				if (player.getRights() < 2) {
					end();
					return;
				}
				player.getInterfaceManager().setBackgroundInterface(true, 475);
				end();
				break;
			case OPTION_2:
				if (player.getRights() < 2) {
					end();
					return;
				}
				player.getPackets().sendInputNameScript("Please enter NPC id");
				end();
				break;
			case OPTION_3:
				if (player.getRights() < 2) {
					end();
					return;
				}
				player.getPackets().sendInputNameScript("Please enter Object id");
				end();
				break;
			}
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				if (player.getUsername().equalsIgnoreCase("danny")
						|| player.getUsername().equalsIgnoreCase("charity")) {
					DropEvent.spawn(player);
				}
				end();
				break;
			case OPTION_2:
				if (player.getUsername().equalsIgnoreCase("danny")
						|| player.getUsername().equalsIgnoreCase("charity")) {
					// DropEvent.hideAndSeek(player);
					stage = -1;
					player.getDialogueManager().startDialogue("SimpleMessage",
							"Under development.");
				} else {
					stage = -1;
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You need to be a miles or a charity to do this...");
				}
				end();
				break;
			case OPTION_3:
				if (player.getUsername().equalsIgnoreCase("danny")
						|| player.getUsername().equalsIgnoreCase("charity")) {
					DropEvent.hideAndSeek(player);
				} else {
					stage = -1;
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You need to be a miles or a charity to do this...");
				}
				end();
				break;
			case OPTION_4:
				if (player.getRights() < 2) {
					end();
					return;
				}
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