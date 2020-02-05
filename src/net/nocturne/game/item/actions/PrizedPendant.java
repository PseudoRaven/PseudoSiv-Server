package net.nocturne.game.item.actions;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;

public class PrizedPendant {

	public enum Pendants {

		ATTACK_PENDANT(24714, Skills.ATTACK, 2.0), STRENGTH_PENDANT(24716,
				Skills.STRENGTH, 2.0), DEFENCE_PENDANT(24718, Skills.DEFENCE,
				2.0), RANGING_PENDANT(24720, Skills.RANGED, 2.0), MAGIC_PENDANT(
				24722, Skills.MAGIC, 2.0), PRAYER_PENDANT(24724, Skills.PRAYER,
				2.0), RUNECRAFTING_PENDANT(24726, Skills.RUNECRAFTING, 2.0), CONSTRUCTION_PENDANT(
				24728, Skills.CONSTRUCTION, 2.0), DUNGEONEERING_PENDANT(24730,
				Skills.DUNGEONEERING, 2.0), CONSTITUTION_PENDANT(24732,
				Skills.HITPOINTS, 2.0), AGILITY_PENDANT(24734, Skills.AGILITY,
				2.0), HERBLORE_PENDANT(24736, Skills.HERBLORE, 2.0), THIEVERY_PENDANT(
				24738, Skills.THIEVING, 2.0), CRAFTING_PENDANT(24740,
				Skills.CRAFTING, 2.0), FLETCHING_PENDANT(24742,
				Skills.FLETCHING, 2.0), SLAYING_PENDANT(24744, Skills.SLAYER,
				2.0), HUNTING_PENDANT(24746, Skills.HUNTER, 2.0), MINING_PENDANT(
				24748, Skills.MINING, 2.0), SMITHING_PENDANT(24750,
				Skills.SMITHING, 2.0), FISHING_PENDANT(24752, Skills.FISHING,
				2.0), COOKING_PENDANT(24754, Skills.COOKING, 2.0), FIREMAKING_PENDANT(
				24756, Skills.FIREMAKING, 2.0), WOODCUTTING_PENDANT(24758,
				Skills.WOODCUTTING, 2.0), FARMING_PENDANT(24760,
				Skills.FARMING, 2.0), SUMMONING_PENDANT(24762,
				Skills.SUMMONING, 2.0);

		int id;
		int skill;
		double modifier;

		Pendants(int id, int skill, double modifier) {
			this.id = id;
			this.skill = skill;
			this.modifier = modifier;
		}

		public double getModifier() {
			return modifier;
		}

		public int getSkillId() {
			return skill;
		}

		public static Map<Integer, Pendants> pendants = new HashMap<Integer, Pendants>();

		public static Pendants forId(int id) {
			return pendants.get(id);
		}

		static {
			for (Pendants pendant : Pendants.values()) {
				pendants.put(pendant.id, pendant);
			}
		}
	}

	public double getModifier(Player player) {
		if (Pendants.forId(player.getEquipment().getAmuletId()) != null) {
			Pendants pendant = Pendants.forId(player.getEquipment()
					.getAmuletId());
			return pendant.getModifier();
		}
		return 1.0;
	}

	public int getSkill(Player player) {
		if (Pendants.forId(player.getEquipment().getAmuletId()) != null) {
			Pendants pendant = Pendants.forId(player.getEquipment()
					.getAmuletId());
			return pendant.getSkillId();
		}
		return -1;
	}

	public PrizedPendant() {

	}

	public static void openInterface(Player player, int id) {
		if (id == Skills.ATTACK)
			player.getInventory().addItemMoneyPouch(24714, 1);
		else if (id == Skills.STRENGTH)
			player.getInventory().addItemMoneyPouch(24716, 1);
		else if (id == Skills.DEFENCE)
			player.getInventory().addItemMoneyPouch(24718, 1);
		else if (id == Skills.RANGED)
			player.getInventory().addItemMoneyPouch(24720, 1);
		else if (id == Skills.MAGIC)
			player.getInventory().addItemMoneyPouch(24722, 1);
		else if (id == Skills.PRAYER)
			player.getInventory().addItemMoneyPouch(24724, 1);
		else if (id == Skills.RUNECRAFTING)
			player.getInventory().addItemMoneyPouch(24726, 1);
		else if (id == Skills.CONSTRUCTION)
			player.getInventory().addItemMoneyPouch(24728, 1);
		else if (id == Skills.DUNGEONEERING)
			player.getInventory().addItemMoneyPouch(24730, 1);
		else if (id == Skills.HITPOINTS)
			player.getInventory().addItemMoneyPouch(24732, 1);
		else if (id == Skills.AGILITY)
			player.getInventory().addItemMoneyPouch(24734, 1);
		else if (id == Skills.HERBLORE)
			player.getInventory().addItemMoneyPouch(24736, 1);
		else if (id == Skills.THIEVING)
			player.getInventory().addItemMoneyPouch(24738, 1);
		else if (id == Skills.CRAFTING)
			player.getInventory().addItemMoneyPouch(24740, 1);
		else if (id == Skills.FLETCHING)
			player.getInventory().addItemMoneyPouch(24742, 1);
		else if (id == Skills.SLAYER)
			player.getInventory().addItemMoneyPouch(24744, 1);
		else if (id == Skills.HUNTER)
			player.getInventory().addItemMoneyPouch(24746, 1);
		else if (id == Skills.MINING)
			player.getInventory().addItemMoneyPouch(24748, 1);
		else if (id == Skills.SMITHING)
			player.getInventory().addItemMoneyPouch(24750, 1);
		else if (id == Skills.FISHING)
			player.getInventory().addItemMoneyPouch(24752, 1);
		else if (id == Skills.COOKING)
			player.getInventory().addItemMoneyPouch(24754, 1);
		else if (id == Skills.FIREMAKING)
			player.getInventory().addItemMoneyPouch(24756, 1);
		else if (id == Skills.WOODCUTTING)
			player.getInventory().addItemMoneyPouch(24758, 1);
		else if (id == Skills.FARMING)
			player.getInventory().addItemMoneyPouch(24760, 1);
		else if (id == Skills.SUMMONING)
			player.getInventory().addItemMoneyPouch(24762, 1);
		player.closeInterfaces();
	}

	public static boolean chooseSkill(Player player, int itemId) {
		if (itemId == 25469) {
			player.getDialogueManager().startDialogue(new Dialogue() {
				private int selectedSkill = -1;
				private int skillChosen = -1;

				@Override
				public void start() {
					player.getInterfaceManager().sendCentralInterface(1263);
					player.getPackets().sendCSVarString(358,
							"What sort of pendant would you like?");
					sendSelectedSkill();
					player.getPackets().sendCSVarInteger(1797, 0); // selectable
																	// lamps
					player.getPackets().sendCSVarInteger(1798, 1); // minimum
																	// level of
																	// 1
					player.getPackets().sendCSVarInteger(1799, 1);
					player.getPackets().sendIComponentSettings(1263, 50, 0, 64,
							1);
					player.getPackets().sendIComponentSettings(1263, 5, 0, 64,
							1);
				}

				@Override
				public void run(int interfaceId, int componentId, int slotId) {
					if (componentId == 5) {
						selectedSkill = slotId;
						sendSelectedSkill();
						if (!player.getInventory().containsItem(25469, 1)) {
							end();
							return;
						}
						if (System.currentTimeMillis() - player.delay < 500)
							return;
						player.delay = System.currentTimeMillis();
						player.getInventory().deleteItem(25469, 1);
						openInterface(player, skillChosen);
					} else
						end();
				}

				private void sendSelectedSkill() {
					switch (selectedSkill) {
					case 1:
						skillChosen = Skills.ATTACK;
						break;
					case 6:
						skillChosen = Skills.HITPOINTS;
						break;
					case 13:
						skillChosen = Skills.MINING;
						break;
					case 2:
						skillChosen = Skills.STRENGTH;
						break;
					case 8:
						skillChosen = Skills.AGILITY;
						break;
					case 14:
						skillChosen = Skills.SMITHING;
						break;
					case 5:
						skillChosen = Skills.DEFENCE;
						break;
					case 9:
						skillChosen = Skills.HERBLORE;
						break;
					case 15:
						skillChosen = Skills.FISHING;
						break;
					case 3:
						skillChosen = Skills.RANGED;
						break;
					case 10:
						skillChosen = Skills.THIEVING;
						break;
					case 16:
						skillChosen = Skills.COOKING;
						break;
					case 11:
						skillChosen = Skills.CRAFTING;
						break;
					case 17:
						skillChosen = Skills.FIREMAKING;
						break;
					case 4:
						skillChosen = Skills.MAGIC;
						break;
					case 19:
						skillChosen = Skills.FLETCHING;
						break;
					case 18:
						skillChosen = Skills.WOODCUTTING;
						break;
					case 12:
						skillChosen = Skills.RUNECRAFTING;
						break;
					case 20:
						skillChosen = Skills.SLAYER;
						break;
					case 21:
						skillChosen = Skills.FARMING;
						break;
					case 22:
						skillChosen = Skills.CONSTRUCTION;
						break;
					case 23:
						skillChosen = Skills.HUNTER;
						break;
					case 24:
						skillChosen = Skills.SUMMONING;
						break;
					case 25:
						skillChosen = Skills.DUNGEONEERING;
						break;
					case 26:
						skillChosen = Skills.DIVINATION;
						break;
					}
				}

				@Override
				public void finish() {
					// TODO Auto-generated method stub

				}
			});
			return true;
		}
		return false;
	}

}