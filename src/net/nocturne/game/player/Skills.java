package net.nocturne.game.player;

import java.io.Serializable;
import java.util.Arrays;

import net.nocturne.Settings;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.item.actions.PrizedPendant.Pendants;
import net.nocturne.game.item.actions.StrangeRock;
import net.nocturne.game.npc.randomEvent.CombatEventNPC;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.GamePointManager.GPR;
import net.nocturne.game.player.content.activities.XPWell;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;

	public static final double MAXIMUM_EXP = 200000000;
	private static final double RANDOM_EVENT_EXP = 50000;

	private boolean[] enabledSkillsTargets;
	private boolean[] skillsTargetsUsingLevelMode;
	private int[] skillsTargetsValues;

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23,
			DUNGEONEERING = 24, DIVINATION = 25, INVENTION = 26;

	public static final String[] SKILL_NAME = { "Attack", "Defence",
			"Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Hunter", "Construction", "Summoning",
			"Dungeoneering", "Divination", "Invention" };

	public static int[] FIXED_SLOTS = { ATTACK, HITPOINTS, MINING, STRENGTH,
			AGILITY, SMITHING, DEFENCE, HERBLORE, FISHING, RANGED, THIEVING,
			COOKING, PRAYER, CRAFTING, FIREMAKING, MAGIC, FLETCHING,
			WOODCUTTING, RUNECRAFTING, SLAYER, FARMING, CONSTRUCTION, HUNTER,
			SUMMONING, DUNGEONEERING, DIVINATION, INVENTION };

	private short level[];
	private double xp[];
	private double[] xpTracks;
	private boolean[] trackSkills;
	private byte[] trackSkillsIds;
	private boolean xpDisplay, xpPopup;
	private int elapsedBonusMinutes;
	private double trackXPREvent;
	private transient double xpBonusTrack;

	private transient int currentCounter;
	private transient Player player;
	public static int xpMultiplier = 1;

	public Skills() {
		level = new short[27];
		xp = new double[27];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		xpPopup = true;
		xpTracks = new double[3];
		trackSkills = new boolean[3];
		trackSkillsIds = new byte[3];
		trackSkills[0] = true;
		if (enabledSkillsTargets == null)
			enabledSkillsTargets = new boolean[27];
		if (skillsTargetsUsingLevelMode == null)
			skillsTargetsUsingLevelMode = new boolean[27];
		if (skillsTargetsValues == null)
			skillsTargetsValues = new int[27];
		for (int i = 0; i < trackSkillsIds.length; i++)
			trackSkillsIds[i] = 30;

	}

	private void sendXPDisplay() {
		for (int i = 0; i < trackSkills.length; i++) {
			player.getVarsManager().sendVarBit(229 + i, trackSkills[i] ? 1 : 0);
			player.getVarsManager().sendVarBit(225 + i, trackSkillsIds[i] + 1);
			refreshCounterXp(i);
		}
		refreshXpPopup();
		refreshXPDisplay();
		refreshCurrentCounter();
	}

	private void refreshCurrentCounter() {
		player.getVarsManager().sendVar(96, currentCounter + 1);
	}

	public void setCurrentCounter(int counter) {
		if (counter != currentCounter) {
			currentCounter = counter;
			refreshCurrentCounter();
		}
	}

	private void switchTrackCounter() {
		trackSkills[currentCounter] = !trackSkills[currentCounter];
		player.getVarsManager().sendVarBit(229 + currentCounter,
				trackSkills[currentCounter] ? 1 : 0);
	}

	private void resetCounterXP() {
		xpTracks[currentCounter] = 0;
		refreshCounterXp(currentCounter);
	}

	public void setCounterSkill(int skill) {
		xpTracks[currentCounter] = 0;
		trackSkillsIds[currentCounter] = (byte) skill;
		player.getVarsManager().sendVarBit(225 + currentCounter,
				trackSkillsIds[currentCounter] + 1);
		refreshCounterXp(currentCounter);
	}

	private void refreshCounterXp(int counter) {
		player.getVarsManager().sendVar(91 + counter,
				(int) (xpTracks[counter] * 10));
	}

	public void handleSetupXPCounter(int componentId) {
		if (componentId >= 13 && componentId <= 31)
			setCurrentCounter((componentId - 13) / 8);
		else if (componentId == 36)
			switchTrackCounter();
		else if (componentId == 70)
			resetCounterXP();
		else if (componentId == 2)
			switchXPDisplay();
		else if (componentId >= 41 && componentId <= 68)
			if (componentId == 43)
				setCounterSkill(4);
			else if (componentId == 44)
				setCounterSkill(2);
			else if (componentId == 45)
				setCounterSkill(3);
			else if (componentId == 52)
				setCounterSkill(18);
			else if (componentId == 59)
				setCounterSkill(11);
			else
				setCounterSkill(componentId >= 67 ? componentId - 38
						: componentId - 41);
	}

	public void unlockSkills(boolean menu) {
		player.getPackets().sendIComponentSettings(menu ? 320 : 1466,
				menu ? 9 : 7, 0, 27, 30);
	}

	private void refreshXpPopup() {
		player.getVarsManager().sendVarBit(228, xpPopup ? 0 : 1);
	}

	private void refreshXPDisplay() {
		player.getVarsManager().sendVarBit(19964, xpDisplay ? 0 : 1);
	}

	public void switchXPDisplay() {
		xpDisplay = !xpDisplay;
		refreshXPDisplay();
	}

	public void switchXPPopup() {
		xpPopup = !xpPopup;
		refreshXpPopup();
		player.getPackets().sendGameMessage(
				"XP pop-ups are now " + (xpPopup ? "en" : "dis") + "abled.");
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		// temporary
		if (xpTracks == null) {
			xpPopup = true;
			xpTracks = new double[3];
			trackSkills = new boolean[3];
			trackSkillsIds = new byte[3];
			trackSkills[0] = true;
			for (int i = 0; i < trackSkillsIds.length; i++)
				trackSkillsIds[i] = 30;
		}

		if (xp.length != 27) {
			xp = Arrays.copyOf(xp, 27);
			level = Arrays.copyOf(level, 27);
			level[DIVINATION] = 1;
		}
	}

	public short[] getLevels() {
		return level;
	}

	public double[] getXp() {
		return xp;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}

	public boolean hasLevel(int skill, int boost, int requiredLevel) {
		return hasLevel(skill, requiredLevel - boost);
	}

	public boolean hasLevel(int skill, int requiredLevel) {
		if (player.getAssistanceManager().hasLevel(skill, requiredLevel)
				|| level[skill] >= requiredLevel)
			return true;
		player.getPackets().sendGameMessage(
				"You must have a " + getSkillName(skill).toLowerCase()
						+ " level of at least " + requiredLevel
						+ " to do this.");
		return false;
	}

	public boolean hasRequiriments(int... skills) {
		for (int i = 0; i < skills.length; i += 2) {
			int skillId = skills[i];
			if (skillId == DUNGEONEERING)
				continue;
			int skillLevel = skills[i + 1];
			if (getLevelForXp(skillId) < skillLevel)
				return false;

		}
		return true;
	}

	public int getTotalLevel() {
		int level = 0;
		for (int i = 0; i < xp.length; i++)
			level += getLevelForXp(i);
		return level;
	}

	public long getTotalXp() {
		long xp = 0;
		for (int i = 0; i < this.xp.length; i++)
			xp += this.xp[i];
		return xp;
	}

	public int getCombatLevel() {
		int attack = getLevelForXp(Skills.ATTACK);
		int defence = getLevelForXp(Skills.DEFENCE);
		int strength = getLevelForXp(Skills.STRENGTH);
		int constitution = getLevelForXp(Skills.HITPOINTS);
		int prayer = getLevelForXp(Skills.PRAYER);
		int summoning = getLevelForXp(Skills.SUMMONING);
		int ranged = getLevelForXp(Skills.RANGED);
		int magic = getLevelForXp(Skills.MAGIC);
		int max = Math.max(strength + attack, Math.max(magic * 2, ranged * 2));
		int combat = (int) (((max * 1.3) + defence + constitution
				+ (prayer / 2) + (summoning / 2)) / 4);
		return combat;
	}

	public int getSummoningCombatLevel() {
		double summon = Math.floor(getLevelForXp(Skills.SUMMONING) / 2) * 0.25;
		return (int) summon;
	}

	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}

	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXp(int skill) {
		return getLevelForXp(xp[skill],
				(skill == DUNGEONEERING || skill == INVENTION) ? 120 : 99);
	}

	private static int getLevelForXp(double exp, int max) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= max; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return max;
	}

	public int getHighestSkillLevel() {
		int maxLevel = 1;
		for (int skill = 0; skill < level.length; skill++) {
			int level = getLevelForXp(skill);
			if (level > maxLevel)
				maxLevel = level;
		}
		return maxLevel;
	}

	public void init() {
		for (int skill = 0; skill < level.length; skill++)
			refresh(skill);
		sendXPDisplay();
		if (!Settings.DOUBLE_XP)
			elapsedBonusMinutes = 0;
		else
			refreshXpBonus();
	}

	private void refreshBonusXp() {
		player.getVarsManager().sendVar(1878, (int) (xpBonusTrack * 10));
	}

	private void refreshXpBonus() {
		player.getVarsManager().sendVarBit(7232, 1);
		refreshElapsedBonusMinutes();
		refreshBonusXp();
	}

	public void increaseElapsedBonusMinues() {
		elapsedBonusMinutes++;
		refreshElapsedBonusMinutes();
	}

	private void refreshElapsedBonusMinutes() {
		player.getVarsManager().sendVarBit(7233, elapsedBonusMinutes);
	}

	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}

	private static int getCounterSkill(int skill) {
		switch (skill) {
		case ATTACK:
			return 0;
		case STRENGTH:
			return 1;
		case DEFENCE:
			return 4;
		case RANGED:
			return 2;
		case HITPOINTS:
			return 5;
		case PRAYER:
			return 6;
		case AGILITY:
			return 7;
		case HERBLORE:
			return 8;
		case THIEVING:
			return 9;
		case CRAFTING:
			return 10;
		case MINING:
			return 12;
		case SMITHING:
			return 13;
		case FISHING:
			return 14;
		case COOKING:
			return 15;
		case FIREMAKING:
			return 16;
		case WOODCUTTING:
			return 17;
		case SLAYER:
			return 19;
		case FARMING:
			return 20;
		case CONSTRUCTION:
			return 21;
		case HUNTER:
			return 22;
		case SUMMONING:
			return 23;
		case DUNGEONEERING:
			return 24;
		case DIVINATION:
			return 25;
		case MAGIC:
			return 3;
		case FLETCHING:
			return 18;
		case RUNECRAFTING:
			return 11;
		default:
			return -1;
		}

	}

	public double addXp(int skill, double exp) {
		return addXp(skill, exp, false);
	}

	public double addXp(int skill, double exp, boolean forceRSXp) {
		player.getCompCapeManager().setRequirement(Requirement.SKILL_LEVELS,
				getTotalLevel());
		handleSkillPet(skill);
		player.getControllerManager().trackXP(skill, (int) exp);
		if (Utils.random(2500) == 1)
			handleGoldenRock(skill);
		if (Utils.random(2000) == 1 && player.getSkills().hasLevel(skill, 99))
			handleExpertShards(skill);
		boolean combatSkill = skill >= ATTACK && skill <= MAGIC;
		if (player.isXpLocked())
			return 0;
		if (player.getAuraManager().usingWisdom())
			exp *= 1.025;
		if (skill == SLAYER)
			exp *= 0.3;
		if (skill == World.SKILL_ID)
			exp *= (World.MODIFIER / 10);
		if (XPWell.isWellActive())
			exp *= 1.5;
		if (player.getEquipment().getBootsId() == ItemIdentifiers.SILVERHAWK_BOOTS
				&& player.getSilverhawkFeathers() >= 1) {
			player.setSilverhawkFeathers(player.getSilverhawkFeathers() - 1);
			exp *= 1.1;
		}
		if (player.getEquipment().getAmuletId() != -1
				&& player.getTemporaryAttributtes().contains("PrizedPendant")) {
			final Pendants pendant = Pendants.forId(player.getEquipment()
					.getAmuletId());
			if (pendant != null && skill == pendant.getSkillId())
				exp *= pendant.getModifier();
		}
		/*if (skill >= COOKING && skill <= CONSTRUCTION && skill != SLAYER)
			if (player.getInventory().hasFreeSlots()
					&& (skill == CONSTRUCTION || skill == HUNTER
							|| skill == FARMING || skill == AGILITY) ? Utils
					.random(50) == 0 : Utils.random(250) == 0)
				StrangeRock.earnRock(player, skill);
		xp[skill] += exp;*/
		if (!forceRSXp && (!player.isCanPvp() || !combatSkill)) {
			if (exp < RANDOM_EVENT_EXP && CombatEventNPC.canRandomEvent(player)) {
				trackXPREvent += exp;
				if (trackXPREvent >= RANDOM_EVENT_EXP) {
					trackXPREvent = 0;
				}
			}
			if (Settings.DOUBLE_XP) {
				exp *= Settings.XP_RATE * 1.5;
			}

			else {
				exp *= Settings.XP_RATE;
			}
		}
		exp *= player.getGamePointManager().hasGamePointsReward(GPR.MORE_XP) ? 1.4
				: 1;
		if (player.inMemberZone() && player.getDonationManager().isDonator())
			exp *= 1.05;
		if (Settings.DOUBLE_XP)
			player.getVarsManager().sendVar(2044, Settings.XP_RATE);
		player.getVarsManager().sendVar(1176, (int) exp * 10 * xpMultiplier);
		int oldLevel = getLevelForXp(skill);
		int oldCombatLevel = getCombatLevel();
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK
								|| skill == Skills.DEFENCE
								|| skill == Skills.STRENGTH
								|| skill == Skills.MAGIC
								|| skill == Skills.RANGED || skill == Skills.HITPOINTS))
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);

				}
			}
		}
		if (xp[skill] == MAXIMUM_EXP)
			return 0;
		double oldXp = xp[skill];

		xp[skill] += exp;
		if (xp[skill] > MAXIMUM_EXP)
			xp[skill] = MAXIMUM_EXP;
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			sendLevelUpInterface(skill);
			player.getDialogueManager().startDialogue("LevelUp", skill);
			player.getAppearence().generateAppearenceData();
			if (combatSkill) {
				if (oldCombatLevel != getCombatLevel())
					sendCombatLevel();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 100);
				else if (skill == PRAYER)
					player.getPrayer().boost(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
			player.getControllerManager().trackLevelUp(skill, level[skill]);
			set(skill, getLevelForXp(skill));
		}
		sendNews(skill, newLevel > oldLevel, oldCombatLevel, oldXp);
		refresh(skill);
		if (player.getClanManager() != null) {
			if (player.getRights() < 2)
				player.getClanManager().addXP((int) exp);
		}
		if (player.getAssistanceManager().isAssisting()
				&& player.getAssistanceManager().isValidSkill(skill))
			exp = player.getAssistanceManager().handleXP(skill, exp);
		xpMultiplier++;
		return exp;
	}

	void sendCombatLevel() {
		player.getPackets().sendCSVarInteger(1000, getCombatLevel());
	}

	public void sendLevelUpInterface(int skill) {
		int iconValue = getIconValue(skill);
		player.getPackets().sendCSVarInteger(5188, iconValue);
		player.getInterfaceManager().setWindowInterface(
				InterfaceManager.LEVEL_UP_COMPONENT_ID, 1216);
		int level = player.getSkills().getLevelForXp(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp[" + skill + "]",
				Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		if (level == 99 || level == 120)
			player.setNextGraphics(new Graphics(1765));
		String name = Skills.SKILL_NAME[skill];
		player.getPackets().sendGameMessage(
				"You've just advanced a"
						+ (name.startsWith("A") || name.startsWith("I") ? "n"
								: "") + " " + name
						+ " level! You have reached level " + level + ".");
		// player.getVarsManager().sendVarBit(3292, iconValue);
		switchFlash(skill, true);
		// player.getPackets().sendMusicEffectOld(LEVEL_MUSIC[skill]);
	}


	public int getIconValue(int skill) {
		if (skill == Skills.ATTACK)
			return 8388609 + (8388608 * (getLevel(Skills.ATTACK) - 1));
		if (skill == Skills.STRENGTH)
			return 8388610 + (8388608 * (getLevel(Skills.STRENGTH) - 1));
		if (skill == Skills.RANGED)
			return 8388611 + (8388608 * (getLevel(Skills.RANGED) - 1));
		if (skill == Skills.MAGIC)
			return 8388612 + (8388608 * (getLevel(Skills.MAGIC) - 1));
		if (skill == Skills.DEFENCE)
			return 8388613 + (8388608 * (getLevel(Skills.DEFENCE) - 1));
		if (skill == Skills.HITPOINTS)
			return 8388614 + (8388608 * (getLevel(Skills.HITPOINTS) - 1));
		if (skill == Skills.PRAYER)
			return 8388615 + (8388608 * (getLevel(Skills.PRAYER) - 1));
		if (skill == Skills.AGILITY)
			return 8388616 + (8388608 * (getLevel(Skills.AGILITY) - 1));
		if (skill == Skills.HERBLORE)
			return 8388617 + (8388608 * (getLevel(Skills.HERBLORE) - 1));
		if (skill == Skills.THIEVING)
			return 8388618 + (8388608 * (getLevel(Skills.THIEVING) - 1));
		if (skill == Skills.CRAFTING)
			return 8388619 + (8388608 * (getLevel(Skills.CRAFTING) - 1));
		if (skill == Skills.RUNECRAFTING)
			return 8388620 + (8388608 * (getLevel(Skills.RUNECRAFTING) - 1));
		if (skill == Skills.MINING)
			return 8388621 + (8388608 * (getLevel(Skills.MINING) - 1));
		if (skill == Skills.SMITHING)
			return 8388622 + (8388608 * (getLevel(Skills.SMITHING) - 1));
		if (skill == Skills.FISHING)
			return 8388623 + (8388608 * (getLevel(Skills.FISHING) - 1));
		if (skill == Skills.COOKING)
			return 8388624 + (8388608 * (getLevel(Skills.COOKING) - 1));
		if (skill == Skills.FIREMAKING)
			return 8388625 + (8388608 * (getLevel(Skills.FIREMAKING) - 1));
		if (skill == Skills.WOODCUTTING)
			return 8388626 + (8388608 * (getLevel(Skills.WOODCUTTING) - 1));
		if (skill == Skills.FLETCHING)
			return 8388627 + (8388608 * (getLevel(Skills.FLETCHING) - 1));
		if (skill == Skills.SLAYER)
			return 8388628 + (8388608 * (getLevel(Skills.SLAYER) - 1));
		if (skill == Skills.FARMING)
			return 8388629 + (8388608 * (getLevel(Skills.FARMING) - 1));
		if (skill == Skills.CONSTRUCTION)
			return 8388630 + (8388608 * (getLevel(Skills.CONSTRUCTION) - 1));
		if (skill == Skills.HUNTER)
			return 8388631 + (8388608 * (getLevel(Skills.HUNTER) - 1));
		if (skill == Skills.SUMMONING)
			return 8388632 + (8388608 * (getLevel(Skills.SUMMONING) - 1));
		else if (skill == Skills.DUNGEONEERING)
			return 8388633 + (8388608 * (getLevel(Skills.DUNGEONEERING) - 1));
		else if (skill == Skills.DIVINATION)
			return 8388634 + (8388608 * (getLevel(Skills.DIVINATION) - 1));
		else if (skill == Skills.INVENTION) {
			return 8388636 + (8388608 * (getLevel(Skills.INVENTION) - 1));
		}
		return 27;
	}

	private void sendNews(int skill, boolean levelUP, int combatLevelBefore,
			double oldXp) {
		boolean combatSkill = skill == SUMMONING
				|| (skill >= ATTACK && skill <= MAGIC);
		if (combatSkill)
			return;
		if (!levelUP) {
			if (xp[skill] >= 50000000) { // 50m
				if (getLevelForXp(oldXp, 120) != 120
						&& getLevelForXp(xp[skill], 120) == 120)
					World.sendNews(player, player.getDisplayName()
							+ " has achieved 104,273,167 XP in "
							+ Skills.SKILL_NAME[skill]
							+ " and can now purchase a Master Cape!",
							World.SERVER_NEWS, true);
				else {
					int next = (int) (xp[skill] / 50000000);
					int xpachievement = next * 50000000;
					if (oldXp < xpachievement && xp[skill] >= xpachievement)
						World.sendNews(player, player.getDisplayName()
								+ " has achieved " + (next * 50)
								+ " million XP in " + Skills.SKILL_NAME[skill]
								+ "!", World.SERVER_NEWS, true);
				}
			}
		} else {
			if (combatSkill && combatLevelBefore != 138
					&& getCombatLevel() == 138) {
				World.sendNews(player, player.getDisplayName()
						+ " has achieved level 138 combat.", World.SERVER_NEWS,
						true);
				return;
			}
			int level = getLevelForXp(skill);
			millestone: if (level % 10 == 0 || level == 99) {
				for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
					if (player.getSkills().getLevelForXp(i) < level)
						break millestone;
				}
				World.sendNews(player, player.getDisplayName()
						+ " has just achieved at least level " + level
						+ " in all skills!", level == 99 ? World.WORLD_NEWS
						: World.FRIEND_NEWS, true);
				return;
			}
			if (level == 99) {
				System.out.println("Reached lvl 99");
				World.sendNews(player, player.getDisplayName()
						+ " has achieved " + level + " "
						+ Skills.SKILL_NAME[skill] + ".", World.SERVER_NEWS,
						true);
			} else if (level == 120)
				World.sendNews(player, player.getDisplayName()
						+ " has achieved 104,273,167 XP in "
						+ Skills.SKILL_NAME[skill]
						+ " and can now purchase a Master Cape!",
						World.SERVER_NEWS, true);
		}
	}

	private void handleSkillPet(int skillId) {
		if ((skillId >= ATTACK && skillId <= MAGIC)
				|| ((skillId == CRAFTING || skillId == DIVINATION || skillId == FLETCHING) ? Utils
						.random(30000) : Utils.random(20000)) != 1)
			return;
		switch (skillId) {
		case Skills.AGILITY:
			player.getBank().addItem(
					new Item(ItemIdentifiers.DOJO_MOJO_PET, 1), true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Dojo Mojo, the Agility pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Dojo Mojo, the Agility pet. It has been added to your bank.");
			break;
		case Skills.CONSTRUCTION:
			player.getBank().addItem(
					new Item(ItemIdentifiers.BABY_YAGAS_HOUSE_PET, 1), true);
			World.sendNews(
					player,
					player.getDisplayName()
							+ " has received Baby Yaga's House, the Construction pet drop!",
					1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Baby Yaga's House, the Construction pet. It has been added to your bank.");
			break;
		case Skills.COOKING:
			player.getBank().addItem(new Item(ItemIdentifiers.RAMSAY_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Ramsay, the Cooking pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Ramsay, the Cooking pet. It has been added to your bank.");
			break;
		case Skills.CRAFTING:
			player.getBank().addItem(new Item(ItemIdentifiers.GEMI_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Gemi, the Crafting pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Gemi, the Crafting pet. It has been added to your bank.");
			break;
		case Skills.DIVINATION:
			player.getBank().addItem(new Item(ItemIdentifiers.WILLOW_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Willow, the Divination pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Willow, the Divination pet. It has been added to your bank.");
			break;
		case Skills.DUNGEONEERING:
			player.getBank().addItem(new Item(ItemIdentifiers.GORDIE_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Gordie, the Dungeoneering pet drop!", 1,
					true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Gordie, the Dungeoneering pet. It has been added to your bank.");
			break;
		case Skills.FARMING:
			player.getBank().addItem(new Item(ItemIdentifiers.BRAINS_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Brains, the Farming pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Brains, the Farming pet. It has been added to your bank.");
			break;
		case Skills.FISHING:
			player.getBank().addItem(new Item(ItemIdentifiers.BUBBLES_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Bubbles, the Fishing pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Bubbles, the Fishing pet. It has been added to your bank.");
			break;
		case Skills.FLETCHING:
			player.getBank()
					.addItem(new Item(ItemIdentifiers.FLO_PET, 1), true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Flo, the Fletching pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Flo, the Fletching pet. It has been added to your bank.");
			break;
		case Skills.HERBLORE:
			player.getBank().addItem(new Item(ItemIdentifiers.HERBERT_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Herbert, the Herblore pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Herbert, the Herblore pet. It has been added to your bank.");
			break;
		case Skills.HUNTER:
			player.getBank()
					.addItem(new Item(ItemIdentifiers.ACE_PET, 1), true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Ace, the Hunter pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(Color.GREEN,
							"While skilling, you find Ace, the Hunter pet. It has been added to your bank.");
			break;
		case Skills.INVENTION:
			player.getBank().addItem(new Item(ItemIdentifiers.MALCOLM_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Malcolm, the Invention pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Malcolm, the Invention pet. It has been added to your bank.");
			break;
		case Skills.MINING:
			player.getBank().addItem(new Item(ItemIdentifiers.ROCKY_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Rocky, the Mining pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Rocky, the Mining pet. It has been added to your bank.");
			break;
		case Skills.RUNECRAFTING:
			player.getBank()
					.addItem(new Item(ItemIdentifiers.RUE_PET, 1), true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Rue, the Runecrafting pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Rue, the Runecrafting pet. It has been added to your bank.");
			break;
		case Skills.SLAYER:
			player.getBank().addItem(new Item(ItemIdentifiers.CRABBE_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Crabbe, the Slayer pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Crabbe, the Slayer pet. It has been added to your bank.");
			break;
		case Skills.SMITHING:
			player.getBank().addItem(new Item(ItemIdentifiers.SMITHY_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Smithy, the Smithing pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Smithy, the Smithing pet. It has been added to your bank.");
			break;
		case Skills.THIEVING:
			player.getBank().addItem(new Item(ItemIdentifiers.RALPH_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Ralph, the Thieving pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Ralph, the Thieving pet. It has been added to your bank.");
			break;
		case Skills.WOODCUTTING:
			player.getBank().addItem(new Item(ItemIdentifiers.WOODY_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Woody, the Woodcutting pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Woody, the Woodcutting pet. It has been added to your bank.");
			break;
		case Skills.FIREMAKING:
			player.getBank().addItem(new Item(ItemIdentifiers.BERNIE_PET, 1),
					true);
			World.sendNews(player, player.getDisplayName()
					+ " has received Bernie, the Bernie pet drop!", 1, true);
			player.getPackets()
					.sendGameMessage(
							Color.GREEN,
							"While skilling, you find Bernie, the Firemaking pet. It has been added to your bank.");
			break;
		}
	}

	public void switchFlash(int skill, boolean on) {
		int id = 0;
		if (skill == Skills.ATTACK)
			id = 3267;
		else if (skill == Skills.STRENGTH)
			id = 3268;
		else if (skill == Skills.DEFENCE)
			id = 3269;
		else if (skill == Skills.RANGED)
			id = 3270;
		else if (skill == Skills.PRAYER)
			id = 3271;
		else if (skill == Skills.MAGIC)
			id = 3272;
		else if (skill == Skills.HITPOINTS)
			id = 3273;
		else if (skill == Skills.AGILITY)
			id = 3274;
		else if (skill == Skills.HERBLORE)
			id = 3275;
		else if (skill == Skills.THIEVING)
			id = 3276;
		else if (skill == Skills.CRAFTING)
			id = 3277;
		else if (skill == Skills.FLETCHING)
			id = 3278;
		else if (skill == Skills.MINING)
			id = 3279;
		else if (skill == Skills.SMITHING)
			id = 3280;
		else if (skill == Skills.FISHING)
			id = 3281;
		else if (skill == Skills.COOKING)
			id = 3282;
		else if (skill == Skills.FIREMAKING)
			id = 3283;
		else if (skill == Skills.WOODCUTTING)
			id = 3284;
		else if (skill == Skills.RUNECRAFTING)
			id = 3285;
		else if (skill == Skills.SLAYER)
			id = 3286;
		else if (skill == Skills.FARMING)
			id = 3287;
		else if (skill == Skills.CONSTRUCTION)
			id = 3288;
		else if (skill == Skills.HUNTER)
			id = 3289;
		else if (skill == Skills.SUMMONING)
			id = 3290;
		else if (skill == Skills.DUNGEONEERING)
			id = 3291;
		else if (skill == Skills.DIVINATION)
			id = 20114;
		else if (skill == Skills.INVENTION)
			id = -1;
		player.getVarsManager().sendVarBit(id, on ? 1 : 0);
	}

	public double addXpLamp(int skill, double exp) {
		player.getControllerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked())
			return 0;
		exp *= Settings.getLampXpRate();
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK
								|| skill == Skills.DEFENCE
								|| skill == Skills.STRENGTH
								|| skill == Skills.MAGIC
								|| skill == Skills.RANGED || skill == Skills.HITPOINTS))
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue("LevelUp", skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearence().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().boost(levelDiff * 10);
			}
			player.getQuestManager().checkCompleted();
		}
		int oldCombatLevel = getCombatLevel();
		double oldXp = xp[skill];
		sendNews(skill, newLevel > oldLevel, oldCombatLevel, oldXp);
		refresh(skill);
		return exp;
	}

	public void addSkillXpRefresh(int skill, double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelForXp(skill);
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	public boolean canObtainTrimmed() {
		int count99 = 0;
		for (int skill = 0; skill < SKILL_NAME.length; skill++) {
			if (level[skill] == 99)
				count99++;
		}
		return count99 >= 2;
	}

	public void setXp(double[] xp) {
		this.xp = xp;
	}

	public String getSkillName(int skill) {
		String skillName = null;
		switch (skill) {
		case 0:
			return "Attack";
		case 1:
			return "Defence";
		case 2:
			return "Strength";
		case 3:
			return "Constitution";
		case 4:
			return "Ranged";
		case 5:
			return "Prayer";
		case 6:
			return "Magic";
		case 7:
			return "Cooking";
		case 8:
			return "Woodcutting";
		case 9:
			return "Fletching";
		case 10:
			return "Fishing";
		case 11:
			return "Firemaking";
		case 12:
			return "Crafting";
		case 13:
			return "Smithing";
		case 14:
			return "Mining";
		case 15:
			return "Herblore";
		case 16:
			return "Agility";
		case 17:
			return "Thieving";
		case 18:
			return "Slayer";
		case 19:
			return "Farming";
		case 20:
			return "Runecrafting";
		case 21:
			return "Hunter";
		case 22:
			return "Construction";
		case 23:
			return "Summoning";
		case 24:
			return "Dungeoneering";
		case 25:
			return "Divination";
		case 26:
			return "Invention";
		}
		return skillName;
	}

	public boolean isMaxed() {
		if (getLevelForXp(ATTACK) == 99 && getLevelForXp(STRENGTH) == 99
				&& getLevelForXp(DEFENCE) == 99 && getLevelForXp(RANGED) == 99
				&& getLevelForXp(PRAYER) == 99 && getLevelForXp(MAGIC) == 99
				&& getLevelForXp(RUNECRAFTING) == 99
				&& getLevelForXp(CONSTRUCTION) == 99
				&& getLevelForXp(DUNGEONEERING) == 99
				&& getLevelForXp(HITPOINTS) == 99
				&& getLevelForXp(AGILITY) == 99
				&& getLevelForXp(HERBLORE) == 99
				&& getLevelForXp(THIEVING) == 99
				&& getLevelForXp(CRAFTING) == 99
				&& getLevelForXp(FLETCHING) == 99
				&& getLevelForXp(SLAYER) == 99 && getLevelForXp(HUNTER) == 99
				&& getLevelForXp(MINING) == 99 && getLevelForXp(SMITHING) == 99
				&& getLevelForXp(FISHING) == 99 && getLevelForXp(COOKING) == 99
				&& getLevelForXp(FIREMAKING) == 99
				&& getLevelForXp(WOODCUTTING) == 99
				&& getLevelForXp(FARMING) == 99
				&& getLevelForXp(SUMMONING) == 99
				&& getLevelForXp(CONSTRUCTION) == 99
				&& getLevelForXp(DUNGEONEERING) == 99
				&& getLevelForXp(DIVINATION) == 99 /*
													 * &&
													 * getLevelForXp(INVENTION)
													 * == 99
													 */) {
			return true;
		}
		return false;
	}

	public int getSkill(int slotId) {
		switch (slotId) {
		case 0:
		case 1:
		case 3:
		case 6:
		case 9:
		case 12:
		case 15:
			return 1;
		case 2:
			return Skills.MINING;
		case 4:
			return Skills.AGILITY;
		case 5:
			return Skills.SMITHING;
		case 7:
			return Skills.HERBLORE;
		case 8:
			return Skills.FISHING;
		case 10:
			return Skills.THIEVING;
		case 11:
			return Skills.COOKING;
		case 13:
			return Skills.CRAFTING;
		case 14:
			return Skills.FIREMAKING;
		case 16:
			return Skills.FLETCHING;
		case 17:
			return Skills.WOODCUTTING;
		case 18:
			return Skills.RUNECRAFTING;
		case 19:
			return Skills.SLAYER;
		case 20:
			return Skills.FARMING;
		case 21:
			return Skills.CONSTRUCTION;
		case 22:
			return Skills.HUNTER;
		case 23:
			return Skills.SUMMONING;
		case 24:
			return Skills.DUNGEONEERING;
		case 25:
			return Skills.DIVINATION;
		case 26:
			return Skills.INVENTION;
		default:
			return -1;
		}
	}

	public int getSkillIdByTargetId(int targetId) {
		switch (targetId) {
		case 0: // Attack
			return ATTACK;
		case 1: // Strength
			return STRENGTH;
		case 2: // Range
			return RANGED;
		case 3: // Magic
			return MAGIC;
		case 4: // Defence
			return DEFENCE;
		case 5: // Constitution
			return HITPOINTS;
		case 6: // Prayer
			return PRAYER;
		case 7: // Agility
			return AGILITY;
		case 8: // Herblore
			return HERBLORE;
		case 9: // Thieving
			return THIEVING;
		case 10: // Crafting
			return CRAFTING;
		case 11: // Runecrafting
			return RUNECRAFTING;
		case 12: // Mining
			return MINING;
		case 13: // Smithing
			return SMITHING;
		case 14: // Fishing
			return FISHING;
		case 15: // Cooking
			return COOKING;
		case 16: // Firemaking
			return FIREMAKING;
		case 17: // Woodcutting
			return WOODCUTTING;
		case 18: // Fletching
			return FLETCHING;
		case 19: // Slayer
			return SLAYER;
		case 20: // Farming
			return FARMING;
		case 21: // Construction
			return CONSTRUCTION;
		case 22: // Hunter
			return HUNTER;
		case 23: // Summoning
			return SUMMONING;
		case 24: // Dungeoneering
			return DUNGEONEERING;
		case 25: // Divination
			return DIVINATION;
		case 26: // Invention
			return INVENTION;
		default:
			return -1;
		}
	}

	private void refreshEnabledSkillsTargets() {
		int value = Utils.get32BitValue(enabledSkillsTargets, true);
		player.getVarsManager().sendVarBit(1966, value);
	}

	private void refreshUsingLevelTargets() {
		// int value = Utils.get32BitValue(skillsTargetsUsingLevelMode, true);
		int value = 0;
		if (skillsTargetsUsingLevelMode[0]) // Attack.
			value += 2;
		if (skillsTargetsUsingLevelMode[1]) // Strength.
			value += 4;
		if (skillsTargetsUsingLevelMode[2]) // Range.
			value += 8;
		if (skillsTargetsUsingLevelMode[3]) // Magic.
			value += 16;
		if (skillsTargetsUsingLevelMode[4]) // Defence.
			value += 32;
		if (skillsTargetsUsingLevelMode[5]) // Constitution.
			value += 64;
		if (skillsTargetsUsingLevelMode[6]) // Prayer.
			value += 128;
		if (skillsTargetsUsingLevelMode[7]) // Agility.
			value += 256;
		if (skillsTargetsUsingLevelMode[8]) // Herblore.
			value += 512;
		if (skillsTargetsUsingLevelMode[9]) // Theiving.
			value += 1024;
		if (skillsTargetsUsingLevelMode[10]) // Crafting.
			value += 2048;
		if (skillsTargetsUsingLevelMode[11]) // Runecrafting.
			value += 4096;
		if (skillsTargetsUsingLevelMode[12]) // Mining.
			value += 8192;
		if (skillsTargetsUsingLevelMode[13]) // Smithing.
			value += 16384;
		if (skillsTargetsUsingLevelMode[14]) // Fishing.
			value += 32768;
		if (skillsTargetsUsingLevelMode[15]) // Cooking.
			value += 65536;
		if (skillsTargetsUsingLevelMode[16]) // Firemaking.
			value += 131072;
		if (skillsTargetsUsingLevelMode[17]) // Woodcutting.
			value += 262144;
		if (skillsTargetsUsingLevelMode[18]) // Fletching.
			value += 524288;
		if (skillsTargetsUsingLevelMode[19]) // Slayer.
			value += 1048576;
		if (skillsTargetsUsingLevelMode[20]) // Farming.
			value += 2097152;
		if (skillsTargetsUsingLevelMode[21]) // Construction.
			value += 4194304;
		if (skillsTargetsUsingLevelMode[22]) // Hunter.
			value += 8388608;
		if (skillsTargetsUsingLevelMode[23]) // Summoning.
			value += 16777216;
		if (skillsTargetsUsingLevelMode[24]) // Dungeoneering.
			value += 33554432;
		if (skillsTargetsUsingLevelMode[25]) // Divination.
			value += 67108864;
		if (skillsTargetsUsingLevelMode[26]) // Invention.s
			value += 1;
		player.getVarsManager().sendVarBit(1968, value);
	}

	private void refreshSkillsTargetsValues() {
		for (int i = 0; i < 27; i++)
			player.getVarsManager()
					.sendVarBit(1969 + i, skillsTargetsValues[i]);
	}

	public void setSkillTargetEnabled(int id, boolean enabled) {
		enabledSkillsTargets[id] = enabled;
		refreshEnabledSkillsTargets();
	}

	public void setSkillTargetUsingLevelMode(int id, boolean using) {
		skillsTargetsUsingLevelMode[id] = using;
		refreshUsingLevelTargets();
	}

	public void setSkillTargetValue(int skillId, int value) {
		skillsTargetsValues[skillId] = value;
		refreshSkillsTargetsValues();
	}

	public void setSkillTarget(boolean usingLevel, int skillId, int target) {
		setSkillTargetEnabled(skillId, true);
		setSkillTargetUsingLevelMode(skillId, usingLevel);
		setSkillTargetValue(skillId, target);
	}

	private void handleGoldenRock(int skillId) {
		int itemId = -1;
		switch (skillId) {
		case Skills.AGILITY:
			itemId = ItemIdentifiers.GOLDEN_ROCK_AGILITY;
			break;
		case Skills.CONSTRUCTION:
			itemId = ItemIdentifiers.GOLDEN_ROCK_CONSTRUCTION;
			break;
		case Skills.CRAFTING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_CRAFTING;
			break;
		case Skills.DIVINATION:
			itemId = ItemIdentifiers.GOLDEN_ROCK_DIVINATION;
			break;
		case Skills.DUNGEONEERING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_DUNGEONEERING;
			break;
		case Skills.FARMING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_FARMING;
			break;
		case Skills.HERBLORE:
			itemId = ItemIdentifiers.GOLDEN_ROCK_HERBLORE;
			break;
		case Skills.MAGIC:
			itemId = ItemIdentifiers.GOLDEN_ROCK_MAGIC;
			break;
		case Skills.ATTACK:
		case Skills.DEFENCE:
		case Skills.STRENGTH:
			itemId = ItemIdentifiers.GOLDEN_ROCK_MELEE;
			break;
		case Skills.MINING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_MINING;
			break;
		case Skills.PRAYER:
			itemId = ItemIdentifiers.GOLDEN_ROCK_PRAYER;
			break;
		case Skills.RANGED:
			itemId = ItemIdentifiers.GOLDEN_ROCK_RANGED;
			break;
		case Skills.SLAYER:
			itemId = ItemIdentifiers.GOLDEN_ROCK_SLAYER;
			break;
		case Skills.SMITHING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_SMITHING;
			break;
		case Skills.SUMMONING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_SUMMONING;
			break;
		case Skills.WOODCUTTING:
			itemId = ItemIdentifiers.GOLDEN_ROCK_WOODCUTTING;
			break;
		}
		if (itemId != -1 && player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(new Item(itemId, 1), true);
			player.getPackets().sendGameMessage(Color.GREEN,
					"You find a golden rock and add it to your bank.");
		}
	}

	private void handleExpertShards(int skillId) {
		int itemId = -1;
		switch (skillId) {
		case Skills.AGILITY:
			itemId = 32087;
			break;
		case Skills.CONSTRUCTION:
			itemId = 32083;
			break;
		case Skills.CRAFTING:
			itemId = 32082;
			break;
		case Skills.DIVINATION:
			itemId = 32066;
			break;
		case Skills.DUNGEONEERING:
			itemId = 32085;
			break;
		case Skills.FARMING:
			itemId = 32067;
			break;
		case Skills.HERBLORE:
			itemId = 32081;
			break;
		case Skills.MAGIC:
			itemId = 32076;
			break;
		case Skills.ATTACK:
			itemId = 32069;
			break;
		case Skills.DEFENCE:
			itemId = 32071;
			break;
		case Skills.STRENGTH:
			itemId = 32070;
			break;
		case Skills.MINING:
			itemId = 32065;
			break;
		case Skills.PRAYER:
			itemId = 32073;
			break;
		case Skills.RANGED:
			itemId = 32075;
			break;
		case Skills.SLAYER:
			itemId = 32088;
			break;
		case Skills.SMITHING:
			itemId = 32084;
			break;
		case Skills.SUMMONING:
			itemId = 32072;
			break;
		case Skills.WOODCUTTING:
			itemId = 32064;
			break;
		case Skills.COOKING:
			itemId = 32077;
			break;
		case Skills.FISHING:
			itemId = 32063;
			break;
		case Skills.HITPOINTS:
			itemId = 32074;
			break;
		case Skills.HUNTER:
			itemId = 32068;
			break;
		case Skills.RUNECRAFTING:
			itemId = 32078;
			break;
		case Skills.FIREMAKING:
			itemId = 32086;
			break;
		case Skills.THIEVING:
			itemId = 32086;
			break;
		case Skills.FLETCHING:
			itemId = 32080;
			break;
		}
		if (player.getInventory().containsItem(itemId, 1)
				|| player.getBank().containsItem(itemId)) {
			return;
		}
		if (itemId != -1 && !player.getInventory().hasFreeSlots()) {
			player.getBank().addItem(new Item(itemId, 1), true);
			player.getPackets()
					.sendGameMessage(
							"You find a shard emanating magical energy during training, and place it in your bank.");
		} else
			player.getInventory().addItem(new Item(itemId, 1));
		player.getPackets()
				.sendGameMessage(
						"You find a shard emanating magical energy during training, and place it in your inventory.");

	}
}