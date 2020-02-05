package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.magic.Magic;
import net.nocturne.game.player.dialogues.Dialogue;

public class FamiliarInspection extends Dialogue {

	@Override
	public void start() {
		sendOptionsDialogue("Teleport to " + parameters[0] + "?", "Yes.", "No.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		if (componentId == OPTION_1)
			Magic.sendNormalTeleportSpell(player, 1, 0,
					(WorldTile) parameters[1]);
		end();
	}

	@Override
	public void finish() {

	}
}
