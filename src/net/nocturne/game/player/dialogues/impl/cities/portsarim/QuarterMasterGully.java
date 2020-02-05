package net.nocturne.game.player.dialogues.impl.cities.portsarim;

import net.nocturne.game.player.dialogues.Dialogue;

public class QuarterMasterGully extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendNPCDialogue(npcId, PLAIN_TALKING,
				"Ready to sail when you are, cap'n.");
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
		player.getPackets().sendHideIComponent(1776, 10, false);
		player.getPackets().sendHideIComponent(1776, 11, false);
		player.getInterfaceManager().setBackgroundInterface(false, 1776);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}