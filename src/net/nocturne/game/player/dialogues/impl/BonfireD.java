package net.nocturne.game.player.dialogues.impl;

import net.nocturne.game.WorldObject;
import net.nocturne.game.player.actions.skills.firemaking.Bonfire;
import net.nocturne.game.player.actions.skills.firemaking.Bonfire.Log;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.player.dialogues.impl.skills.ChooseAToolD;

public class BonfireD extends Dialogue {

	private Log[] logs;
	private WorldObject object;

	@Override
	public void start() {
		this.logs = (Log[]) parameters[0];
		this.object = (WorldObject) parameters[1];
		int[] ids = new int[logs.length];
		for (int i = 0; i < ids.length; i++)
			ids[i] = logs[i].getLogId();
		for (int i = 0; i < 15; i++)
			player.getPackets().sendCSVarInteger(1703 + i,
					i >= ids.length ? -1 : ids[i]);
		player.getPackets().sendIComponentText(1179, 0,
				"Which logs do you want to add to the bonfire?");
		player.getInterfaceManager().sendCentralInterface(1179);
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		end();
		int slot = ChooseAToolD.getOptionSlot(componentId);
		if (slot >= logs.length)
			return;
		player.getActionManager().setAction(new Bonfire(logs[slot], object));
	}

	@Override
	public void finish() {
		player.getInterfaceManager().removeCentralInterface();
	}

}
