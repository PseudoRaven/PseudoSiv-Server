package net.nocturne.game.player.actions.skills.woodcutting;

import net.nocturne.game.Animation;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.skills.woodcutting.Woodcutting.HatchetDefinitions;
import net.nocturne.game.player.content.CarrierTravel;
import net.nocturne.game.player.content.CarrierTravel.Carrier;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Canoes {

	private static final int CANOE_SELECTION = 52;
	private static final int AREA_SELECTION = 53;
	private static final int[] VISIBLE_COMPONENTS = { 3, 2, 5 };
	private static final int[] INVISIBLE_COMPONENTS = { 9, 10, 8 };

	public static boolean chopCanoeTree(Player player, final int configIndex) {
		player.getActionManager().setAction(new Action() {

			private HatchetDefinitions hatchet;
			private boolean isComplete;

			@Override
			public boolean start(Player player) {
				return checkAll(player);
			}

			private boolean checkAll(Player player) {
				for (HatchetDefinitions def : HatchetDefinitions.values()) {
					if (player.getInventory().containsItemToolBelt(
							def.getItemId())
							|| player.getEquipment().getWeaponId() == def
									.getItemId()) {
						hatchet = def;
						if (player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet
								.getLevelRequired()) {
							hatchet = null;
							break;
						}
					}
				}
				if (hatchet == null) {
					hatchet = HatchetDefinitions.BRONZE;
					player.getPackets()
							.sendGameMessage(
									"A nearby overseer hands you a bronze hatchet to temporarily use.");
				}
				if (player.getSkills().getLevel(Skills.WOODCUTTING) < 12) {
					player.getPackets()
							.sendGameMessage(
									"You need a woodcutting level of at least 12 to make a canoe.");
					return false;
				}
				setActionDelay(player, 4);
				return true;
			}

			@Override
			public boolean process(Player player) {
				player.setNextAnimation(new Animation(hatchet.getEmoteId()));
				return !isComplete;
			}

			@Override
			public int processWithDelay(Player player) {
				player.getTemporaryAttributtes().put("canoe_chopped", true);
				player.getTemporaryAttributtes().put("canoe_config",
						1839 + configIndex);
				player.getVarsManager().sendVarBit(1839 + configIndex, 10);
				isComplete = true;
				stop(player);
				return 4;
			}

			@Override
			public void stop(Player player) {
				setActionDelay(player, -1);
				player.setNextAnimation(new Animation(-1));
			}
		});
		return true;
	}

	public static void openSelectionInterface(Player player) {
		player.getInterfaceManager().sendCentralInterface(CANOE_SELECTION);
		int level = 12;
		for (int index = 0; index < VISIBLE_COMPONENTS.length; index++) {
			level += 15;
			if (player.getSkills().getLevel(Skills.WOODCUTTING) >= level) {
				player.getPackets().sendHideIComponent(CANOE_SELECTION,
						VISIBLE_COMPONENTS[index], false);
				player.getPackets().sendHideIComponent(CANOE_SELECTION,
						INVISIBLE_COMPONENTS[index], true);
			}
		}
	}

	public static void createShapedCanoe(final Player player) {
		final int selectedCanoe = (int) player.getTemporaryAttributtes().get(
				"selected_canoe");
		player.closeInterfaces();
		HatchetDefinitions hatchet = null;
		for (HatchetDefinitions def : HatchetDefinitions.values()) {
			if (player.getInventory().containsItemToolBelt(def.getItemId())
					|| player.getEquipment().getWeaponId() == def.getItemId()) {
				hatchet = def;
				if (player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet
						.getLevelRequired()) {
					hatchet = null;
					break;
				}
			}
		}
		if (hatchet == null)
			hatchet = HatchetDefinitions.BRONZE;
		player.setNextAnimation(new Animation(hatchet.ordinal() + 11594));
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.getTemporaryAttributtes().put("canoe_shaped", true);
				player.unlock();
				player.getVarsManager().sendVarBit(
						(int) player.getTemporaryAttributtes().get(
								"canoe_config"), 11 + selectedCanoe);
			}
		}, 10);
	}

	public static void openTravelInterface(Player player, int canoeArea) {
		player.getTemporaryAttributtes().put("selected_area", canoeArea);
		player.getInterfaceManager().sendCentralInterface(AREA_SELECTION);
		player.getPackets().sendHideIComponent(AREA_SELECTION, 21, false); // default
		player.getPackets().sendHideIComponent(AREA_SELECTION,
				canoeArea == 3 ? 19 : 22 + (3 - canoeArea), false); // area
	}

	public static void deportCanoeStation(final Player player, int selectedArea) {
		final int selectedCanoe = (int) player.getTemporaryAttributtes().get(
				"selected_canoe");
		int canoeArea = (int) player.getTemporaryAttributtes().get(
				"selected_area");
		if (selectedArea != canoeArea) {
			if (selectedArea > (canoeArea + selectedCanoe + 1)
					|| selectedArea < (canoeArea - selectedCanoe - 1)) {
				player.getPackets()
						.sendGameMessage(
								"This is too far to reach, please pick another plot point or make a better canoe.");
			} else if (selectedArea == 4 && selectedCanoe != 3) {
				player.getPackets()
						.sendGameMessage(
								"Only a waka canoe can go to the depths of the wilderness.");
			} else {
				player.closeInterfaces();
				player.getVarsManager().sendVarBit(
						(int) player.getTemporaryAttributtes().get(
								"canoe_config"), 0);
				player.getTemporaryAttributtes().clear();
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						player.getControllerManager().startController(
								"Wilderness");
					}
				}, 2);
				CarrierTravel.sendCarrier(player,
						Carrier.values()[24 + selectedArea], false);
			}
		} else
			player.getPackets().sendGameMessage(
					"You are already at this location!");
	}
}
