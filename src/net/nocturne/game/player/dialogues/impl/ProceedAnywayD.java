package net.nocturne.game.player.dialogues.impl;

import java.io.IOException;

import net.nocturne.game.WorldTile;
import net.nocturne.game.player.dialogues.Dialogue;

public class ProceedAnywayD extends Dialogue {

	@Override
	public void start() {
		sendDialogue(
				"WARNING: You may be teleported to an area with aggressive creatures and",
				"you may be attacked on sight.");
		stage = 1;

	}

	@Override
	public void run(int interfaceId, int componentId, int slotId)
			throws ClassNotFoundException {
		switch (stage) {
		case 1:
			sendOptionsDialogue("Proceed anyway?", "Yes.", "No.");
			stage = 3;
			break;

		case 3:
			switch (componentId) {
			case OPTION_1:
				int bit = player.getVarsManager().getBitValue(25113);
				if (bit == 1) {
					player.setNextWorldTile(new WorldTile(3144, 5545, 0));
				} else if (bit == 2)
					player.setNextWorldTile(new WorldTile(4181, 5729, 0));
				else if (bit == 3)
					player.setNextWorldTile(new WorldTile(2568, 5738, 0));
				else if (bit == 4)
					player.setNextWorldTile(new WorldTile(2294, 3608, 0));
				else if (bit == 5)
					player.setNextWorldTile(new WorldTile(2982, 3386, 0));
				else if (bit == 6)
					player.setNextWorldTile(new WorldTile(3545, 3311, 0));
				else if (bit == 7)
					player.setNextWorldTile(new WorldTile(1911, 4369, 0));
				else if (bit == 8)
					player.setNextWorldTile(new WorldTile(2967, 4384, 0));
				else if (bit == 9)
					player.setNextWorldTile(new WorldTile(3051, 3518, 0));
				else if (bit == 10)
					player.setNextWorldTile(new WorldTile(1198, 6499, 0));
				else if (bit == 11)
					player.setNextWorldTile(new WorldTile(3446, 9495, 0));
				else if (bit == 12)
					player.setNextWorldTile(new WorldTile(2974, 1654, 0));
				else if (bit == 13)
					player.setNextWorldTile(new WorldTile(2919, 5268, 0));
				else if (bit == 14)
					player.setNextWorldTile(new WorldTile(2854, 5357, 0));
				else if (bit == 15)
					player.setNextWorldTile(new WorldTile(2834, 5284, 0));
				else if (bit == 16)
					player.setNextWorldTile(new WorldTile(2925, 5343, 0));
				else if (bit == 17)
					player.setNextWorldTile(new WorldTile(2896, 5203, 0));
				else if (bit == 18)
					player.setNextWorldTile(new WorldTile(1109, 585, 1));
				else if (bit == 19)
					player.setNextWorldTile(new WorldTile(3700, 3417, 0));
				else if (bit == 20)
					player.setNextWorldTile(new WorldTile(2972, 3430, 0));
				end();
				break;
			case OPTION_2:
				end();
				break;
			}
			break;
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
