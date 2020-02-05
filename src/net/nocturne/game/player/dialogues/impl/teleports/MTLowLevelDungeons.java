package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTLowLevelDungeons extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Low Level Dungeons/Areas",
				"Stronghold of Security", "Stronghold of Player Safety",
				"Lumbridge Swamp", "Karamja Volcano", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3081,
						3421, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3074,
						3456, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3198,
						3177, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2855,
						3168, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Low Level Dungeons/Area - 2",
						"Varrock Sewer", "Draynor Sewer", "Edgeville Dungeon",

						"None");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3237,
						3459, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3083,
						3272, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3095,
						3468, 0));
			} else if (componentId == OPTION_4) {
				end();
			}
		}

	}

	@Override
	public void finish() {

	}
}
