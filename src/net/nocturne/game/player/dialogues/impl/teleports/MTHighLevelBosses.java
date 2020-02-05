package net.nocturne.game.player.dialogues.impl.teleports;

import net.nocturne.game.WorldTile;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.map.bossInstance.BossInstanceHandler.Boss;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class MTHighLevelBosses extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("High Level Bosses", "Queen Black Dragon", "Corporeal Beast", "Nex", "Wildy Wyrm", "More");
		stage = -1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (stage == -1) {
			switch (componentId) {
			case OPTION_1:
				if (player.getSkills().getLevel(Skills.SUMMONING) < 60)
					player.getPackets().sendGameMessage("You need a summoning level of 60 to go to this monster.");
				else
					player.getControllerManager().startController("QueenBlackDragonController");
				break;
			case OPTION_2:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2969, 4383, 2));
				break;
			case OPTION_3:
				teleportPlayer(2904, 5203, 0);
				end();
				break;
			case OPTION_4:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3271, 3599, 0));
				break;
			case OPTION_5:
				sendOptionsDialogue("High Level Bosses  - 2", "Vorago", "Araxxi", "Kalphite King", "Agoroth",
						"Heart of Gielinor");
				stage = 1;
				break;
			}

		} else if (stage == 1) {
			switch (componentId) {
			case OPTION_1:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2973, 3430, 0));
				break;
			case OPTION_2:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3700, 3418, 0));
				break;
			case OPTION_3:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2970, 1655, 0));
				break;
			case OPTION_4:
				BossInstanceHandler.enterInstance(player, Boss.Agoroth);
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3895, 6817, 0));
				break;
			case OPTION_5:
				//Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3368, 2881, 0));
				sendDialogue("This feature is currently being worked on.");
				end();
				break;
			default:
				end();
			}
		} else if (stage == 2) {
			switch (componentId) {
			case OPTION_1:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2973, 3430, 0));
				break;
			case OPTION_2:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3700, 3418, 0));
				break;
			case OPTION_3:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2970, 1655, 0));
				break;
			case OPTION_4:
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3907, 6816, 0));
				break;
			default:
				end();

			}
		}
	}

	@Override
	public void finish() {

	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextWorldTile(new WorldTile(x, y, z));
		player.stopAll();
		player.getControllerManager().startController("GodWars");
	}
}
