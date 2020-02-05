package net.nocturne.game.player.dialogues.impl.skills;

import net.nocturne.game.player.content.SkillsDialogue.ToolReference;
import net.nocturne.game.player.dialogues.Dialogue;

public class ChooseAToolD extends Dialogue {

	private ToolReference[] tools;

	@Override
	public void start() {
		String message = (String) parameters[0];
		tools = (ToolReference[]) parameters[1];
		for (int i = 0; i < 15; i++)
			player.getPackets().sendCSVarInteger(1703 + i,
					i >= tools.length ? -1 : tools[i].getToolId());
		player.getPackets().sendIComponentText(1179, 0, message);
		player.getInterfaceManager().sendCentralInterface(1179);
	}

	public static int getOptionSlot(int componentId) {
		return componentId == 19 ? 0 : componentId == 36 ? 1
				: componentId == 39 ? 2 : componentId == 42 ? 3
						: componentId == 45 ? 4 : componentId == 48 ? 5
								: componentId == 51 ? 6 : 7;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		int option = getOptionSlot(componentId);
		end();
		if (option >= tools.length)
			return;
		tools[option].select(player);
	}

	@Override
	public void finish() {
		player.getInterfaceManager().removeCentralInterface();
	}
}