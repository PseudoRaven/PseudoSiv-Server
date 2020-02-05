package net.nocturne.game.item.actions;

import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.utils.Utils;

public class Lamps {

	private static final int LAMP_SMALL = 0;
	private static final int LAMP_MEDIUM = 1;
	private static final int LAMP_BIG = 2;
	private static final int LAMP_HUGE = 3;
	private static final int REWARD_BOOK = 4;
	private static final int DRAGON_KIN = 2;

	private static final int[] SELECTABLE_XP_LAMPS = new int[] { 23713, 23714,
			23715, 23716, 20935, 18782, 12628 };
	private static final int[] SELECTABLE_XP_LAMPS_TYPES = new int[] {
			LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE, REWARD_BOOK,
			DRAGON_KIN, LAMP_BIG };
	private static final int[][] SKILL_LAMPS = new int[][] {
			{ 23717, 23718, 23719, 23720 }, { 23725, 23726, 23727, 23728 },
			{ 23721, 23722, 23723, 23724 }, { 23753, 23754, 23755, 23756 },
			{ 23729, 23730, 23731, 23732 }, { 23737, 23738, 23739, 23740 },
			{ 23733, 23734, 23735, 23736 }, { 23798, 23799, 23800, 23801 },
			{ 23806, 23807, 23808, 23809 }, { 23774, 23775, 23776, 23777 },
			{ 23794, 23795, 23796, 23797 }, { 23802, 23803, 23804, 23805 },
			{ 23769, 23770, 23771, 23773 }, { 23790, 23791, 23792, 23793 },
			{ 23786, 23787, 23788, 23789 }, { 23761, 23762, 23763, 23764 },
			{ 23757, 23758, 23759, 23760 }, { 23765, 23766, 23767, 23768 },
			{ 23778, 23779, 23780, 23781 }, { 23810, 23811, 23812, 23813 },
			{ 23741, 23742, 23743, 23744 }, { 23782, 23783, 23784, 23785 },
			{ 23745, 23746, 23747, 23748 }, { 23814, 23815, 23816, 23817 },
			{ 23749, 23750, 23751, 23752 }, { 29545, 29546, 29547, 29548 } };
	private static final int[][] SKILL_LAMPS_TYPES = new int[][] {
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE },
			{ LAMP_SMALL, LAMP_MEDIUM, LAMP_BIG, LAMP_HUGE }, };

	private static final int[] DIALOGUE_INTERFACE_C2S = new int[] {
			Skills.ATTACK, Skills.MAGIC, Skills.MINING, Skills.WOODCUTTING,
			Skills.AGILITY, Skills.FLETCHING, Skills.THIEVING, Skills.STRENGTH,
			Skills.RANGED, Skills.SMITHING, Skills.FIREMAKING, Skills.HERBLORE,
			Skills.SLAYER, Skills.CONSTRUCTION, Skills.DEFENCE, Skills.PRAYER,
			Skills.FISHING, Skills.CRAFTING, Skills.FARMING, Skills.HUNTER,
			Skills.SUMMONING, Skills.HITPOINTS, Skills.DUNGEONEERING,
			Skills.COOKING, Skills.RUNECRAFTING };

	// do not look at wiki, wiki contains a lot of incorrect xp data (I checked
	// myself)
	private static final double[] BASE_LAMPS_XP = new double[] { 62.5, 69, 77,
			85, 94, 104, 115, 127, 139, 154, 170, 188, 206, 229, 252, 262, 274,
			285, 298, 310, 325, 337, 352, 367.5, 384, 399, 405, 414, 453, 473,
			514, 528, 536, 551, 583, 609, 635, 662, 692, 721, 752, 785, 818,
			854, 890, 929, 971, 1013, 1055, 1101, 1149, 1200, 1250, 1305, 1362,
			1422, 1485, 1542, 1617, 1685, 1758, 1836, 1912, 2004.5, 2085, 2172,
			2269, 2379, 2471, 2593, 2693, 2810, 2947, 3082, 3214, 3339, 3496,
			3648, 3793, 3980, 4166, 4348, 4522, 4762, 4919, 5150, 5376, 5593,
			5923, 6122, 6452, 6615, 6929, 7236, 7533, 8065, 8348, 8602 };

	public static void processLampClick(Player player, int slot, int id,
			int level) {
		if (isSelectable(id)) {
			openSelectableDialog(player, slot, id, level);
		} else if (isSkillLamp(id)) {
			openSkillDialog(player, slot, id);
		}
	}

	private static void openSelectableDialog(Player player, final int slot,
			final int id, int level) {
		player.getDialogueManager().startDialogue(new Dialogue() {
			private int selectedSkill = -1;
			private int skillChosen = -1;

			@Override
			public void start() {
				player.getInterfaceManager().sendCentralInterface(1263);
				player.getPackets().sendCSVarString(2389,
						"What sort of XP would you like?");
				sendSelectedSkill();
				player.getPackets().sendCSVarInteger(1797, 0); // selectable
																// lamps
				player.getPackets().sendCSVarInteger(1798, level); // minimum
																	// level of
																	// 1
				player.getPackets().sendCSVarInteger(1799, id);
				player.getPackets().sendIComponentSettings(1263, 5, 0, 64, 1);
				/*
				 * for (int i = 14; i < 65; i++)
				 * player.getPackets().sendUnlockIComponentOptionSlots(1263, i,
				 * -1, 0, 0);
				 */
				player.getPackets().sendIComponentSettings(1263, 50, 0, 64, 0);
			}

			@Override
			public void run(int interfaceId, int componentId, int slotId) {
				System.out.println("yo");
				if (componentId == 5) {
					System.out.println("hello");
					selectedSkill = slotId;
					sendSelectedSkill();
					if (!player.getInventory().containsItem(id, 1)) {
						end();
						return;
					}
					if (System.currentTimeMillis() - player.delay < 500)
						return;
					player.delay = System.currentTimeMillis();
					player.getInventory().deleteItem(slot, new Item(id, 1));
					double exp = player.getSkills().addXpLamp(
							skillChosen,
							getExp(player.getSkills()
									.getLevelForXp(skillChosen),
									selectableLampType(id)));
					player.getInterfaceManager().removeCentralInterface();
					sendDialogue(
							"<col=0000ff>Your wish has been granted!</col>",
							"You have been awarded "
									+ Utils.getFormattedNumber(exp, ',')
									+ " XP in "
									+ Skills.SKILL_NAME[skillChosen] + "!");
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
				case 7:
					skillChosen = Skills.PRAYER;
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
	}

	private static void openSkillDialog(Player player, final int slot,
			final int id) {
		final int type = skillLampType(id);
		final int skillId = skillLampSkillId(id);

		player.getDialogueManager().startDialogue(new Dialogue() {
			@Override
			public void start() {
				if (!player.xpLampPrompt)
					sendOptionsDialogue("Rub Lamp?", "Gain experience in "
							+ Skills.SKILL_NAME[skillId] + ".",
							"Don't ask me again.", "Cancel.");
				else {
					if (System.currentTimeMillis() - player.delay < 1000)
						return;
					player.delay = System.currentTimeMillis();
					player.getInventory().deleteItem(slot, new Item(id, 1));
					double exp = player.getSkills().addXpLamp(
							skillId,
							getExp(player.getSkills().getLevelForXp(skillId),
									type));
					player.getDialogueManager().startDialogue(
							"SimpleMessage",
							"<col=0000ff>Your wish has been granted!</col>",
							"You have been awarded "
									+ Utils.getFormattedNumber(exp, ',')
									+ " XP in " + Skills.SKILL_NAME[skillId]
									+ "!");
				}
			}

			@Override
			public void run(int interfaceId, int componentId, int slotId) {
				if (componentId == Dialogue.OPTION_3
						|| !player.getInventory().containsItem(id, 1)) {
					end();
					return;
				} else if (componentId == Dialogue.OPTION_2) {
					player.xpLampPrompt = true;
					player.getDialogueManager().startDialogue("SimpleMessage",
							"You will no longer be prompted.");
					return;
				}
				if (System.currentTimeMillis() - player.delay < 1000)
					return;
				player.delay = System.currentTimeMillis();
				player.getInventory().deleteItem(slot, new Item(id, 1));
				double exp = player.getSkills()
						.addXpLamp(
								skillId,
								getExp(player.getSkills()
										.getLevelForXp(skillId), type));
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"<col=0000ff>Your wish has been granted!</col>",
						"You have been awarded "
								+ Utils.getFormattedNumber(exp, ',')
								+ " XP in " + Skills.SKILL_NAME[skillId] + "!");
			}

			@Override
			public void finish() {
			}

		});
	}

	public static double getExp(int skillLevel, int lampType) {
		double xp;
		if (skillLevel <= BASE_LAMPS_XP.length)
			xp = BASE_LAMPS_XP[skillLevel - 1] / 2;
		else
			xp = BASE_LAMPS_XP[BASE_LAMPS_XP.length - 1] / 2;

		for (int i = 0; i < lampType; i++)
			xp *= 2D;
		return xp;
	}

	private static int selectableLampType(int id) {
		for (int i = 0; i < SELECTABLE_XP_LAMPS.length; i++) {
			if (SELECTABLE_XP_LAMPS[i] == id)
				return SELECTABLE_XP_LAMPS_TYPES[i];
		}
		return -1;
	}

	public static boolean isSelectable(int id) {
		for (int SELECTABLE_XP_LAMP : SELECTABLE_XP_LAMPS) {
			if (SELECTABLE_XP_LAMP == id)
				return true;
		}
		return false;
	}

	private static int skillLampType(int id) {
		for (int skillId = 0; skillId < SKILL_LAMPS.length; skillId++) {
			for (int i = 0; i < SKILL_LAMPS[skillId].length; i++) {
				if (SKILL_LAMPS[skillId][i] == id)
					return SKILL_LAMPS_TYPES[skillId][i];
			}
		}
		return -1;
	}

	private static int skillLampSkillId(int id) {
		for (int skillId = 0; skillId < SKILL_LAMPS.length; skillId++) {
			for (int i = 0; i < SKILL_LAMPS[skillId].length; i++) {
				if (SKILL_LAMPS[skillId][i] == id)
					return skillId;
			}
		}
		return -1;
	}

	public static boolean isSkillLamp(int id) {
		for (int[] SKILL_LAMP : SKILL_LAMPS) {
			for (int aSKILL_LAMP : SKILL_LAMP) {
				if (aSKILL_LAMP == id)
					return true;
			}
		}
		return false;
	}
}