package net.nocturne.game.player.content;

import net.nocturne.cache.loaders.GeneralRequirementMap;
import net.nocturne.game.player.Player;

public class LoyaltyProgram {

	public static final int LOYALTY_INTERFACE = 1143;
	private static final int[] REQUIREMENT_SCRIPTS = { 2586 };

	public static void open(Player player) {
		player.getInterfaceManager().setRootInterface(LOYALTY_INTERFACE, false);
		player.getPackets().sendCSVarInteger(1648, player.getLoyaltyPoints());
	}

	public static void handleButtonClick(Player player, int componentId) {
		if (componentId >= 7 && componentId <= 13)// tabs
			sendTab(player, componentId - 7);
	}

	private static void sendTab(Player player, int selectedTab) {
		GeneralRequirementMap map = GeneralRequirementMap
				.getMap(REQUIREMENT_SCRIPTS[selectedTab]);
		if (map == null)
			return;
		player.getPackets().sendUnlockIComponentOptionSlots(LOYALTY_INTERFACE,
				0, 0, map.getValues().size(), 0, 1);
	}
}