package net.nocturne.game.player.dialogues.impl.rottenpotato;

import net.nocturne.game.Hit;
import net.nocturne.game.World;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.dialogues.Dialogue;

public class OPuON extends Dialogue {

	/**
	 * @author: miles M
	 */

	private NPC npc;

	@Override
	public void start() {
		npc = (NPC) parameters[0];
		stage = 1;
		sendOptionsDialogue("Rotten Potato - Option: NPC", "Duplicate NPC.", "Remove NPC.", "Transmogrify into NPC.",
				"Check all NPC locations.", "More.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case -1:
			end();
			break;
		case 1:
			switch (componentId) {
			case OPTION_1:
				stage = 3;
				sendOptionsDialogue("How many " + npc.getName() + "'s (" + npc.getId() + ") to spawn?", "Spawn 1.",
						"Spawn 5.", "Spawn 10.", "Spawn 25.", "Spawn 500.");
				break;
			case OPTION_2:
				stage = 2;
				int amount = 0;
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != npc.getId())
						continue;
					amount++;
				}
				sendOptionsDialogue("How many " + npc.getName() + "'s (" + npc.getId() + ") to kill?",
						"Remove this NPC.", "Remove all (" + amount + ") NPCs.", "Nevermind.");
				break;
			case OPTION_3:
				try {
					player.getAppearence().transformIntoNPC(npc.getId());
					end();
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				break;
			case OPTION_4:
				stage = -1;
				int amountx = 0;
				String location = "";
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != npc.getId())
						continue;
					amountx++;
					location += "[" + n.getX() + ", " + n.getY() + ", " + n.getPlane() + "] - ";
				}
				sendDialogue("Total " + amountx + "<br><br>" + location);
				break;
			case OPTION_5:
				end();
				break;
			}
			break;
		case 2:
			switch (componentId) {
			case OPTION_1:
				npc.applyHit(new Hit(player, npc.getMaxHitpoints(), HitLook.REGULAR_DAMAGE));
				end();
				break;
			case OPTION_2:
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != npc.getId())
						continue;
					n.applyHit(new Hit(player, n.getMaxHitpoints(), HitLook.REGULAR_DAMAGE));
				}
				end();
				break;
			case OPTION_3:
				end();
				World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage(
						"Npc: " + npc.getId() + " at " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane() + ".",
						true);
				end();
				break;
			}
			break;
		case 3:
			switch (componentId) {
			case OPTION_1:
				World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage(
						"Npc: " + npc.getId() + " at " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane() + ".",
						true);
				end();
				break;
			case OPTION_2:
				for (int i = 0; i < 5; i++)
					World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage("Npc: " + npc.getId() + " (x5) at " + npc.getX() + ", " + npc.getY()
						+ ", " + npc.getPlane() + ".", true);
				end();
				break;
			case OPTION_3:
				for (int i = 0; i < 10; i++)
					World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage("Npc: " + npc.getId() + " (x10) at " + npc.getX() + ", "
						+ npc.getY() + ", " + npc.getPlane() + ".", true);
				end();
				break;
			case OPTION_4:
				for (int i = 0; i < 25; i++)
					World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage("Npc: " + npc.getId() + " (x25) at " + npc.getX() + ", "
						+ npc.getY() + ", " + npc.getPlane() + ".", true);
				end();
				break;
			case OPTION_5:
				for (int i = 0; i < 500; i++)
					World.spawnNPC(npc.getId(), npc, -1, true, true);
				player.getPackets().sendGameMessage("Npc: " + npc.getId() + " (x500) at " + npc.getX() + ", "
						+ npc.getY() + ", " + npc.getPlane() + ".", true);
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