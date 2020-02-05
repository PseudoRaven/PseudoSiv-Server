package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTHighLevelDungeons extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("High Level Dungeons/Areas", "God Wars Dungeon",
				"Ancient Cavern", "Forinthry Dungeon", "Frost Dragons", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2916,
						3739, 0));
				player.stopAll();
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2512,
						3511, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3080,
						10057, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1312,
						4528, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("High Level Dungeons/Areas - 2",
						"Ape Atoll Temple", "Tirannwn Elf Camp",
						"Evil Chicken's Lair", "Ogre Enclave", "More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2787,
						2786, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2203,
						3253, 0));
			} else if (componentId == OPTION_3) {
				// Magic.sendNormalTeleportSpell(player, 0, 0, new
				// WorldTile(1576, 4363, 0));
				player.getPackets().sendGameMessage(
						"This area is currently disabled.");
				end();
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2589,
						9411, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("High Level Dungeons/Areas - 3",
						"Gorak Plane", "Ourania Cave", "Celestial Dragons",
						"Monastery of Ascension", "None");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3038,
						5346, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3271,
						4861, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1000,
						4523, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2508,
						2887, 0));
			} else if (componentId == OPTION_5) {
				end();
			}
		}

	}

	@Override
	public void finish() {

	}
}