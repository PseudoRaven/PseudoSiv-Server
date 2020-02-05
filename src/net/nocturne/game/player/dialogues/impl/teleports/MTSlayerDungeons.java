package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTSlayerDungeons extends Dialogue {

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3423,
						3544, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4182,
						5729, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(4180,
						5731, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3025,
						9224, 0));
				player.getControllerManager().startController("JadinkoLair");
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Areas - 2", "Strykwyrms",
						"Asgarnian ice dungeon", "Fremmenik Slayer Dungeon",
						"Kuradel's Slayer Dungeon", "More");
				stage = 1;
			}
		} else if (stage == 1) {
			if (componentId == OPTION_1) {
				player.getDialogueManager().startDialogue("Stryke");
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3006,
						9549, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2806,
						10004, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(1738,
						5313, 1));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Areas - 3", "Temple Of Light",
						"Smoke Dungeon", "Taverley Slayer Dungeon",
						"Brine Rat Cavern", "More");
				stage = 2;
			}
		} else if (stage == 2) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2009,
						4639, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3308,
						2962, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2218,
						4532, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2696,
						10120, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Areas - 4", "Desert Lizards",
						"Meiyerditch Dungeon", "Poison Waste Slayer Dungeon",
						"Mudskipper Point", "More");
				stage = 3;
			}
		} else if (stage == 3) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3418,
						3039, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3627,
						9741, 0));
			} else if (componentId == OPTION_3) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2321,
						3100, 0));
			} else if (componentId == OPTION_4) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2992,
						3112, 0));
			} else if (componentId == OPTION_5) {
				sendOptionsDialogue("Slayer Areas - 5", "Tarn's Lair",
						"Mos Le'Harmless Cave", "None");
				stage = 4;
			}
		} else if (stage == 4) {
			if (componentId == OPTION_1) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3185,
						4598, 0));
			} else if (componentId == OPTION_2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3751,
						2971, 0));
			} else if (componentId == OPTION_3) {
				end();
			}
		}

	}

	@Override
	public void start() {
		sendOptionsDialogue("Slayer Areas", "Slayer Tower", "Polypore Dungeon",
				"Glacor Cave", "Jadinko Lair", "More");
		stage = -1;
	}
}