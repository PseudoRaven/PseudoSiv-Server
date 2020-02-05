package net.nocturne.game.player.actions.skills.prayer;

import java.io.Serializable;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.cache.loaders.GeneralRequirementMap;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.dungeonnering.DungeonBoss;
import net.nocturne.game.npc.godwars.zaros.Nex;
import net.nocturne.game.npc.others.WildyWyrm;
import net.nocturne.game.npc.qbd.QueenBlackDragon;
import net.nocturne.game.npc.randomEvent.CombatEventNPC;
import net.nocturne.game.player.InterfaceManager;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Prayer implements Serializable {

	private static final long serialVersionUID = -2313278544306151600L;

	public static final int STEEL_SKIN = 0, ULTIMATE_STRENGTH = 1,
			INCREDIBLE_REFLEXES = 2, EAGLE_EYE = 3, OVERPOWERING_FORCE = 4,
			MYSTIC_MIGHT = 5, OVERCHARGE = 6, RAPID_RESTORE = 7,
			RAPID_HEAL = 8, PROTECT_ITEM = 9, PROTECT_FROM_SUMMONING = 10,
			PROTECT_FROM_MAGIC = 11, PROTECT_FROM_MISSILES = 12,
			PROTECT_FROM_MELEE = 13, RETRIBUTION = 14, REDEMPTION = 15,
			SMITE = 16, CHIVALRY = 17, RAPID_RENEWAL = 18, PIETY = 19,
			RIGOUR = 20, AUGURY = 21, PROTECT__ITEM = 0, SAP_WARRIOR = 1,
			SAP_RANGER = 2, SAP_RANGE_STRENGTH = 3, SAP_MAGE = 4,
			SAP_MAGE_STRENGTH = 5, SAP_SPIRIT = 6, SAP_DEFENCE = 7,
			SAP_STRENGTH = 8, BERSERKER = 9, DEFLECT_SUMMONING = 10,
			DEFLECT_MAGIC = 11, DEFLECT_MISSILES = 12, DEFLECT_MELEE = 13,
			LEECH_ATTACK = 14, LEECH_RANGED = 15, LEECH_RANGE_STRENGTH = 16,
			LEECH_MAGIC = 17, LEECH_MAGIC_STRENGTH = 18, LEECH_DEFENCE = 19,
			LIGHT_FORM = 20, DARK_FORM = 21, LEECH_STRENGTH = 22,
			LEECH_ENERGY = 23, LEECH_ADRENALINE = 24,
			CHRONICLE_ABSORPTION = 25, SOUL_LINK = 26, WRATH = 27,
			TEAMWORK_PROTECTION = 28, SUPERHEAT_FORM = 29, SOUL_SPLIT = 30,
			FORTITUDE = 31, TURMOIL = 32, ANGUISH = 33, TORMENT = 34;

	/**
	 * Enumeration containing the normal and curse structs.
	 */
	private static final ClientScriptMap NORMAL_STRUCTS, CURSE_STRUCTS;

	static {
		NORMAL_STRUCTS = ClientScriptMap.getMap(6759);
		CURSE_STRUCTS = ClientScriptMap.getMap(6760);
	}

	/**
	 * Tree Struct Keys
	 */
	private static final int FIRST_TIER_LEVEL = 2963, FIRST_TIER_MAP = 2964,
			SECOND_TIER_LEVEL = 2965, SECOND_TIER_MAP = 2966,
			THIRD_TIER_LEVEL = 2967, THIRD_TIER_MAP = 2968;

	/**
	 * Branch Struct Keys
	 */
	private static final int LEVEL_REQUIREMENT = 2807, TREE_STRUCT_KEY = 2962,
			NAME = 2794, DESCRIPTION = 2795, REQUIREMENT_MESSAGE = 2808;

	/**
	 * Prayer book types.
	 */
	public static final byte NORMAL = 0, CURSES = 1;

	/**
	 * The default amount of prayer points given at the first player login.
	 */
	private static final int DEFAULT_PRAYER_COUNT = 10;

	/**
	 * Current player instance.
	 */
	private transient Player player;

	/**
	 * Prayers currently active.
	 */
	private transient boolean[] active;

	/**
	 * Quick Prayer.
	 */
	private boolean quick[];

	/**
	 * If quick prayers are currently toggled.
	 */
	private boolean quickActive;

	/**
	 * Type of book the player is using. 0 = normal, 1 = curses
	 */
	private byte type;

	/**
	 * The amount of prayer points a player currently has.
	 */
	private int points;

	private transient int[] leechBonuses;
	private transient boolean boostedLeech, open;

	public Prayer() {
		this.leechBonuses = new int[4];
		setPoints(Integer.MAX_VALUE, true, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
		leechBonuses = new int[4];
		resetActive();
	}

	public void init() {
		refreshType();
		unlock();
	}

	public void reset() {
		closeAllPrayers(true);
		points = max();
		refreshPrayerPoints();
	}

	public void unlock() {
		player.getPackets().sendIComponentSettings(1458, 34, 0, 22, 10320902);
		player.getPackets().sendIComponentSettings(1458, 33, 0, 35, 10320902);
	}

	public boolean hasFullPrayerPoints() {
		return points == max();
	}

	public void boost() {
		boost(max());
	}

	public void boost(int points) {
		setPoints(points, true);
	}

	public void drain(int points) {
		setPoints(points, false);
	}

	public void drainPrayer(int amount) {
		if ((points - amount) >= 0)
			points -= amount;
		else
			points = 0;
		refreshPrayerPoints();
	}

	public void setPoints(int points, boolean boost) {
		setPoints(points, boost, true);
	}

	public void refreshPrayerPoints() {
		player.getVarsManager().sendVarBit(16736, points * 10);
		player.updateBuffs();
	}

	private void setPoints(int points, boolean boost, boolean refresh) {
		int replaced = boost ? this.points + points : this.points - points;
		this.points = replaced = boost ? Math.min(max(), replaced) : Math.max(
				0, replaced);
		if (this.points >= max())
			this.points = max();
		if (!refresh
				|| !player.getVarsManager().sendVarBit(16736, this.points * 10))
			return;
	}

	public boolean isAncientCurses() {
		return type == 1;
	}

	private void setType(byte type) {
		this.type = type;
		refreshType();
	}

	/**
	 * Maximum amount of points available.
	 * 
	 * @return The max.
	 */
	public int max() {
		return player == null ? DEFAULT_PRAYER_COUNT : player.getSkills()
				.getLevelForXp(Skills.PRAYER) * 10;
	}

	public int getPoints() {
		return points;
	}

	public boolean isActive(int slot) {
		if (slot > 21 && type == 0)
			return false;
		return active[slot];
	}

	public boolean isQuickOpen() {
		return open;
	}

	public void activateQuick() {
		quickActive = !quickActive;
		if (!quickActive)
			return;
		for (int slot = 0; slot < quick.length; slot++) {
			if (!quick[slot])
				active[slot] = false;
			else
				activate(slot);
			update(slot);
		}
		player.getInterfaceManager().openGameTab(
				InterfaceManager.PRAYER_BOOK_TAB);
	}

	public void selectQuick() {
		open = !open;
		player.getPackets().sendIComponentSettings(1458, 34, -1, 40, 14);
		player.getPackets().sendCSVarInteger(181, open ? 1 : 0);
		if (open)
			for (int i = 0; i < quick.length; i++)
				update(i);
	}

	public void activate(int slot) {
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				if (player.getEffectsManager().hasActiveEffect(
						EffectType.PROTECTION_DISABLED)
						&& isProtectionPrayer(slot)) {
					player.getPackets().sendGameMessage(
							"Your protection players are currently disabled.");
					return;
				}
				if (open) {
					quick[slot] = !quick[slot];
					collisions(slot);
					update(slot);
					return;
				}
				if (!active[slot]) {
					String requirementMessage = getRequirementMessage(slot);
					if (getLevelRequirement(slot) > player.getSkills()
							.getLevel(Skills.PRAYER)) {
						player.getPackets().sendGameMessage(requirementMessage);
						return;
					}
					if (!checkPrayer())
						return;
				}
				if (Settings.DEBUG)
					player.getPackets().sendGameMessage(
							"Clicked on prayer: " + getName(slot) + ", "
									+ getStruct(slot).getId());
				active[slot] = !active[slot];
				if (active[slot]) {
					collisions(slot);
					handlePrayer(slot);
				}
				update(slot);
				player.updateBuffs();
				player.getAppearence().generateIconsData();
			}
		});
	}

	private boolean checkPrayer() {
		if (points <= 0) {
			player.getVarsManager().sendVar(
					isAncientCurses() ? (quickActive ? 1768 : 3275)
							: (quickActive ? 1770 : 3272), 0);
			player.getPackets().sendGameMessage(
					"Please recharge your prayer at the lumbridge church.");
			return false;
		}
		return true;
	}

	public boolean handlePrayer(int slot) {
		if (slot == Prayer.PROTECT_ITEM
				|| slot == Prayer.PROTECT_FROM_SUMMONING
				|| slot == Prayer.PROTECT_FROM_MAGIC
				|| slot == Prayer.PROTECT_FROM_MELEE
				|| slot == Prayer.PROTECT_FROM_MISSILES
				|| slot == Prayer.RETRIBUTION || slot == Prayer.REDEMPTION
				|| slot == Prayer.SMITE)
			return true;
		else if (slot == Prayer.PROTECT__ITEM && isAncientCurses()) {
			player.setNextAnimationNoPriority(new Animation(12567));
			player.setNextGraphics(new Graphics(2213));
		} else if (slot == Prayer.BERSERKER) {
			if (!quickActive) {
				player.setNextAnimationNoPriority(new Animation(12589));
				player.setNextGraphics(new Graphics(2266));
			}
		} else if (slot == Prayer.WRATH || slot == Prayer.SOUL_SPLIT
				|| slot == Prayer.DEFLECT_SUMMONING
				|| slot == Prayer.DEFLECT_MAGIC || slot == Prayer.DEFLECT_MELEE
				|| slot == Prayer.DEFLECT_MISSILES)
			return true;
		else if (slot == Prayer.TURMOIL || slot == Prayer.ANGUISH
				|| slot == Prayer.TORMENT) {
			if (!quickActive) {
				player.setNextAnimationNoPriority(new Animation(12565));
				player.setNextGraphics(new Graphics(2226));
			}
		}
		return false;
	}

	private void update(int slot) {
		int varBit = getActivationVarBit(slot, open);
		assert varBit != -1 : "Unhandled prayer at slot " + slot;
		player.getVarsManager().sendVarBit(varBit,
				(open ? quick[slot] : active[slot]) ? 1 : 0);
	}

	private void collisions(int slot) {
		int[][][] prayerGrouping = {
				{
						{ Prayer.AUGURY, Prayer.RIGOUR, Prayer.CHIVALRY,
								Prayer.PIETY },
						{ Prayer.INCREDIBLE_REFLEXES, Prayer.CHIVALRY,
								Prayer.PIETY },
						{ Prayer.ULTIMATE_STRENGTH, Prayer.CHIVALRY,
								Prayer.PIETY },
						{ Prayer.OVERPOWERING_FORCE, Prayer.RIGOUR },
						{ Prayer.EAGLE_EYE, Prayer.RIGOUR },
						{ Prayer.OVERCHARGE, Prayer.AUGURY },
						{ Prayer.MYSTIC_MIGHT, Prayer.AUGURY },
						{},
						{ Prayer.RAPID_RENEWAL },
						{},
						{ Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE },
						{ Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE,
								Prayer.PROTECT_FROM_MELEE,
								Prayer.PROTECT_FROM_MISSILES },
						{ Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE,
								Prayer.PROTECT_FROM_MELEE,
								Prayer.PROTECT_FROM_MAGIC },
						{ Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE,
								Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MISSILES },
						{ Prayer.PROTECT_FROM_MELEE, Prayer.REDEMPTION,
								Prayer.SMITE, Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MISSILES },
						{ Prayer.RETRIBUTION, Prayer.PROTECT_FROM_MELEE,
								Prayer.SMITE, Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MISSILES },
						{ Prayer.RETRIBUTION, Prayer.PROTECT_FROM_MELEE,
								Prayer.REDEMPTION, Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MISSILES },
						{ Prayer.PIETY, Prayer.ULTIMATE_STRENGTH,
								Prayer.INCREDIBLE_REFLEXES },
						{ Prayer.RAPID_HEAL },
						{ Prayer.CHIVALRY, Prayer.ULTIMATE_STRENGTH,
								Prayer.INCREDIBLE_REFLEXES },
						{ Prayer.EAGLE_EYE, Prayer.OVERPOWERING_FORCE,
								Prayer.AUGURY },
						{ Prayer.EAGLE_EYE, Prayer.OVERPOWERING_FORCE,
								Prayer.RIGOUR } },
				{
						{},
						{ Prayer.LEECH_ATTACK, Prayer.TURMOIL },
						{ Prayer.LEECH_RANGED, Prayer.ANGUISH },
						{ Prayer.LEECH_RANGE_STRENGTH, Prayer.ANGUISH },
						{ Prayer.LEECH_MAGIC, Prayer.TORMENT },
						{ Prayer.LEECH_MAGIC_STRENGTH, Prayer.TORMENT },
						{ Prayer.LEECH_ADRENALINE },
						{ Prayer.LEECH_DEFENCE, Prayer.TURMOIL },
						{ Prayer.LEECH_STRENGTH, Prayer.TURMOIL },
						{},
						{ Prayer.SOUL_SPLIT, Prayer.WRATH },
						{ Prayer.DEFLECT_MELEE, Prayer.DEFLECT_MISSILES,
								Prayer.SOUL_SPLIT, Prayer.WRATH },
						{ Prayer.DEFLECT_MAGIC, Prayer.DEFLECT_MELEE,
								Prayer.SOUL_SPLIT, Prayer.WRATH },
						{ Prayer.DEFLECT_MISSILES, Prayer.DEFLECT_MAGIC,
								Prayer.SOUL_SPLIT, Prayer.WRATH },
						{ Prayer.SAP_WARRIOR, Prayer.TURMOIL },
						{ Prayer.SAP_RANGER, Prayer.ANGUISH },
						{ Prayer.SAP_RANGE_STRENGTH, Prayer.ANGUISH },
						{ Prayer.SAP_MAGE, Prayer.TORMENT },
						{ Prayer.SAP_MAGE_STRENGTH, Prayer.TORMENT },
						{ Prayer.SAP_DEFENCE, Prayer.TURMOIL },
						{},// Light form
						{},// Dark form
						{ Prayer.SAP_STRENGTH, Prayer.TURMOIL },
						{},
						{ Prayer.SAP_SPIRIT },
						{},// Chronicle absorbtion
						{},// Soul link
						{ Prayer.PROTECT_FROM_SUMMONING,
								Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MELEE,
								Prayer.PROTECT_FROM_MISSILES, Prayer.SOUL_SPLIT },
						{},// Teamwork protection
						{},// Superheat form
						{ Prayer.PROTECT_FROM_SUMMONING,
								Prayer.PROTECT_FROM_MAGIC,
								Prayer.PROTECT_FROM_MELEE,
								Prayer.PROTECT_FROM_MISSILES, Prayer.WRATH },
						{},// Fortitude
						{ Prayer.ANGUISH, Prayer.TORMENT, Prayer.SAP_DEFENCE,
								Prayer.SAP_STRENGTH, Prayer.SAP_WARRIOR,
								Prayer.LEECH_ATTACK, Prayer.LEECH_DEFENCE,
								Prayer.LEECH_STRENGTH },
						{ Prayer.TORMENT, Prayer.TURMOIL,
								Prayer.SAP_RANGE_STRENGTH, Prayer.SAP_RANGER,
								Prayer.LEECH_RANGE_STRENGTH,
								Prayer.LEECH_RANGED },
						{ Prayer.ANGUISH, Prayer.TURMOIL, Prayer.SAP_MAGE,
								Prayer.SAP_MAGE_STRENGTH, Prayer.LEECH_MAGIC,
								Prayer.LEECH_MAGIC_STRENGTH } } };
		for (int index : prayerGrouping[type][slot]) {
			if (open)
				quick[index] = false;
			else
				active[index] = false;
			update(index);
		}
		if (!open) {
			player.updateBuffs();
			player.getAppearence().generateIconsData();
		}
	}

	public void collapse(int... slots) {
		for (int slot : slots) {
			active[slot] = false;
			update(slot);
		}
	}

	public void switchBook(byte type) {
		setType(type);
		collapse();
		resetActive();
	}

	private void refreshType() {
		player.getVarsManager().sendVarBit(16789, type == CURSES ? 1 : 0);
	}

	private void resetActive() {
		quick = active = new boolean[type == CURSES ? CURSE_STRUCTS.getSize()
				: NORMAL_STRUCTS.getSize()];
	}

	private GeneralRequirementMap getStruct(int slot) {
		GeneralRequirementMap tierMap = GeneralRequirementMap
				.getMap(type == CURSES ? CURSE_STRUCTS.getIntValue(slot)
						: NORMAL_STRUCTS.getIntValue(slot));
		if (tierMap.getIntValue(FIRST_TIER_LEVEL) == 0)
			return tierMap;
		int level = player.getSkills().getLevel(Skills.PRAYER);
		if (level >= tierMap.getIntValue(THIRD_TIER_LEVEL))
			return GeneralRequirementMap.getMap(tierMap
					.getIntValue(THIRD_TIER_MAP));
		else if (level >= tierMap.getIntValue(SECOND_TIER_LEVEL))
			return GeneralRequirementMap.getMap(tierMap
					.getIntValue(SECOND_TIER_MAP));
		else if (level >= tierMap.getIntValue(FIRST_TIER_LEVEL))
			return GeneralRequirementMap.getMap(tierMap
					.getIntValue(FIRST_TIER_MAP));
		throw new IllegalStateException("Struct cannot be found for slot "
				+ slot + ".");
	}

	private int getLevelRequirement(int slot) {
		return getStruct(slot).getIntValue(LEVEL_REQUIREMENT);
	}

	private String getRequirementMessage(int slot) {
		return getStruct(slot).getStringValue(REQUIREMENT_MESSAGE);
	}

	private String getDescription(int slot) {
		return getStruct(slot).getStringValue(DESCRIPTION);
	}

	private String getName(int slot) {
		return getStruct(slot).getStringValue(NAME);
	}

	private int getActivationVarBit(int slot, boolean quick) {
		switch (type == CURSES ? CURSE_STRUCTS.getIntValue(slot)
				: NORMAL_STRUCTS.getIntValue(slot)) {
		case 14540:
		case 14541:
		case 14542:
		case 14543:
			return quick ? 5942 : 16739;
		case 14544:
		case 14545:
		case 14546:
		case 14547:
			return quick ? 5943 : 16740;
		case 14548:
		case 14549:
		case 14550:
		case 14551:
			return quick ? 5944 : 16741;
		case 14572:
			return quick ? 5945 : 16742;
		case 14573:
			return quick ? 5946 : 16743;
		case 14575:
			return quick ? 5947 : 16744;
		case 14576:
			return quick ? 5948 : 16745;
		case 14577:
			return quick ? 5949 : 16746;
		case 14578:
			return quick ? 5950 : 16747;
		case 14580:
			return quick ? 5951 : 16748;
		case 14581:
			return quick ? 5952 : 16749;
		case 14582:
			return quick ? 5953 : 16750;
		case 14552:
		case 14553:
		case 14554:
		case 14555:
			return quick ? 5954 : 16751;
		case 14560:
		case 14561:
		case 14562:
		case 14563:
			return quick ? 5955 : 16752;
		case 14556:
		case 14557:
		case 14558:
		case 14559:
			return quick ? 5956 : 16753;
		case 14564:
		case 14565:
		case 14566:
		case 14567:
			return quick ? 5957 : 16754;
		case 14579:
			return quick ? 5958 : 16755;
		case 14568:
			return quick ? 5959 : 16756;
		case 14569:
			return quick ? 5960 : 16757;
		case 14574:
			return quick ? 5961 : 16758;
		case 14570:
			return quick ? 5962 : 16759;
		case 14571:
			return quick ? 5963 : 16760;
		case 14583:
			return quick ? 5964 : 16761;
		case 14584:
			return quick ? 5965 : 16762;
		case 14585:
			return quick ? 5966 : 16763;
		case 14586:
			return quick ? 5989 : 16786;
		case 14587:
			return quick ? 5967 : 16764;
		case 14588:
			return quick ? 5988 : 16785;
		case 14591:
			return quick ? 5990 : 16787;
		case 14590:
			return quick ? 5991 : 16788;
		case 14589:
			return quick ? 5968 : 16765;
		case 14592:
			return quick ? 5969 : 16766;
		case 14593:
			return quick ? 5970 : 16767;
		case 14594:
			return quick ? 5971 : 16768;
		case 14595:
			return quick ? 5972 : 16769;
		case 14596:
			return quick ? 5973 : 16770;
		case 14597:
			return quick ? 5974 : 16771;
		case 14598:
			return quick ? 5975 : 16772;
		case 14599:
			return quick ? 5984 : 16781;
		case 14600:
			return quick ? 5976 : 16773;
		case 14601:
			return quick ? 5985 : 16782;
		case 14602:
			return quick ? 5977 : 16774;
		case 14603:
			return quick ? 5978 : 16775;
		case 14604:
			return quick ? 5979 : 16776;
		case 14605:
			return quick ? 5980 : 16777;
		case 14606:
			return quick ? 5981 : 16778;
		case 14607:
			return quick ? 5982 : 16779;
		case 14608:
			return quick ? 5983 : 16780;
		case 14609:
			return quick ? 5987 : 16784;
		case 14610:
			return quick ? 5986 : 16783;
		case 32272:
			return quick ? 29048 : 29065;
		case 32273:
			return quick ? 29049 : 29066;
		case 32274:
			return quick ? 29050 : 29067;
		case 32275:
			return quick ? 29051 : 29068;
		case 32276:
			return quick ? 29052 : 29069;
		case 32277:
			return quick ? 29053 : 29070;
		case 32278:
			return quick ? 29054 : 29071;
		}
		throw new IllegalStateException("Struct cannot be found for slot "
				+ slot + ".");
	}

	public void processPrayerDrain() {
		double[][] drainValues = {
				{ 1.66666666667, 1.66666666667, 1.66666666667, 1.66666666667,
						1.66666666667, 1.66666666667, 1.66666666667,
						0.16666666666, 0.16666666666, 0.16666666666, 2.5, 2.5,
						2.5, 2.5, 2.5, 2.5, 3.33333333333, 2.5, 1.66666666667,
						3.33333333333, 3.33333333333, 3.33333333333 },// 22
				{ 0.16666666666, 1.66666666667, 1.66666666667, 1.66666666667,
						1.66666666667, 1.66666666667, 1.66666666667,
						1.66666666667, 1.66666666667, 1.2, 2.5, 2.5, 2.5, 2.5,
						3.33333333333, 3.33333333333, 3.33333333333,
						3.33333333333, 3.33333333333, 3.33333333333,
						3.33333333333, 3.33333333333, 3.33333333333,
						3.33333333333, 3.33333333333, 3.33333333333,
						3.33333333333, 3.33333333333, 3.33333333333,
						3.33333333333, 5, 5, 5, 5, 5 } };
		if (!player.getTemporaryAttributtes().contains("CurseTimer"))// 28
			player.getTemporaryAttributtes().put("CurseTimer", 0);
		Double totalPoints = (double) 0;
		boolean decreaseLeechs = (int) player.getTemporaryAttributtes().get(
				"CurseTimer") % 60 == 0;
		for (int i = 0; i < active.length; i++)
			if (active[i])
				totalPoints += drainValues[type][i];
		if (points > 0 && totalPoints != 0.0) {
			drainPrayer((int) Math.ceil(totalPoints));
			if (!hasPrayersOn())
				closeAllPrayers(true);
		} else
			closeAllPrayers(true);
		if (decreaseLeechs)
			player.getPrayer().decreaseLeechBonus();
		player.getTemporaryAttributtes().put("CurseTimer",
				(int) player.getTemporaryAttributtes().get("CurseTimer") + 1);
	}

	public void setPrayerBook(boolean ancientCurses) {
		switchBook((byte) (ancientCurses ? 1 : 0));
	}

	public void closeAllPrayers() {
		closeAllPrayers(true);
	}

	public void closeAllPrayers(boolean reset) {
		for (int i = 0; i < active.length; i++) {
			active[i] = false;
			update(i);
		}
		if (reset) {
			player.getAppearence().generateIconsData();
			player.getPackets().sendCSVarInteger(182, 0);
			leechBonuses = new int[4];
		}
		player.updateBuffs();
	}

	public boolean isUsingProtectionPrayer() {
		return isMageProtecting() || isRangeProtecting() || isMeleeProtecting();
	}

	public boolean isProtectingItem() {
		return (isActive(Prayer.PROTECT__ITEM) || isActive(Prayer.PROTECT_ITEM));
	}

	public boolean isMageProtecting() {
		return (isActive(Prayer.DEFLECT_MAGIC) || isActive(Prayer.PROTECT_FROM_MAGIC));
	}

	public boolean isRangeProtecting() {
		return (isActive(Prayer.DEFLECT_MISSILES) || isActive(Prayer.PROTECT_FROM_MISSILES));
	}

	public boolean isMeleeProtecting() {
		return (isActive(Prayer.DEFLECT_MELEE) || isActive(Prayer.PROTECT_FROM_MELEE));
	}

	private boolean isProtectionPrayer(int slot) {
		if (type == 0)
			return slot == Prayer.PROTECT_FROM_MAGIC
					|| slot == Prayer.PROTECT_FROM_MELEE
					|| slot == Prayer.PROTECT_FROM_MISSILES
					|| slot == Prayer.PROTECT_FROM_SUMMONING;
		else
			return slot == Prayer.DEFLECT_MAGIC || slot == Prayer.DEFLECT_MELEE
					|| slot == Prayer.DEFLECT_MISSILES
					|| slot == Prayer.DEFLECT_SUMMONING;
	}

	public void closeProtectionPrayers() {
		for (int i = 0; i < active.length; i++)
			if (type == 0)
				if (i == Prayer.PROTECT_FROM_MAGIC
						|| i == Prayer.PROTECT_FROM_MELEE
						|| i == Prayer.PROTECT_FROM_MISSILES
						|| i == Prayer.PROTECT_FROM_SUMMONING) {
					active[i] = false;
					update(i);
				} else if (i == Prayer.DEFLECT_MAGIC
						|| i == Prayer.DEFLECT_MELEE
						|| i == Prayer.DEFLECT_MISSILES
						|| i == Prayer.DEFLECT_SUMMONING) {
					active[i] = false;
					update(i);
				}
		player.updateBuffs();
	}

	public boolean hasPrayersOn() {
		for (int i = 0; i < active.length; i++)
			if (active[i] == true)
				return true;
		return false;
	}

	public boolean canReflect(Entity entity) {
		if (entity instanceof DungeonBoss || entity instanceof WildyWyrm
				|| entity instanceof CombatEventNPC || entity instanceof Nex
				|| entity instanceof QueenBlackDragon) {
			player.getPackets().sendGameMessage(
					"You are unable to reflect damage back to this creature.",
					true);
			return false;
		}
		return entity.getMaxHitpoints() > 1;
	}

	public double getCombatRatingMultiplier(int combatType) {
		if (combatType == Combat.MELEE_TYPE)
			return getAttackRatingMultiplier();
		if (combatType == Combat.RANGE_TYPE)
			return getRangeRatingMultiplier();
		return getMageRatingMultiplier();
	}

	public double getCombatDamageMultiplier(int combatType) {
		if (combatType == Combat.MELEE_TYPE)
			return getAttackDamageMultiplier();
		if (combatType == Combat.RANGE_TYPE)
			return getRangeDamageMultiplier();
		return getMageDamageMultiplier();
	}

	public double getEnemyCombatRatingMultiplier(int combatType) {
		if (combatType == Combat.MELEE_TYPE)
			return getEnemyAttackRatingMultiplier();
		if (combatType == Combat.RANGE_TYPE)
			return getEnemyRangeRatingMultiplier();
		return getEnemyMageRatingMultiplier();
	}

	public double getEnemyCombatDamageMultiplier(int combatType) {
		if (combatType == Combat.MELEE_TYPE)
			return getEnemyAttackDamageMultiplier();
		if (combatType == Combat.RANGE_TYPE)
			return getEnemyRangeDamageMultiplier();
		return getEnemyMageDamageMultiplier();
	}

	public double getMageRatingMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.MYSTIC_MIGHT))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_MAGIC)) {
				double d = leechBonuses[2];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.TORMENT))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyMageRatingMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.LEECH_ATTACK)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_MAGIC)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.TORMENT)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getMageDamageMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.MYSTIC_MIGHT)) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER);
				value += level >= 45 ? 0.06 : level >= 27 ? 0.04 : 0.02;
			} else if (isActive(Prayer.OVERCHARGE))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_MAGIC_STRENGTH)) {
				double d = leechBonuses[2];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.TORMENT))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyMageDamageMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_MAGE)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_MAGIC_STRENGTH)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.TORMENT)) {
			double d = leechBonuses[2];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getRangeRatingMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.EAGLE_EYE))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_RANGED)) {
				double d = leechBonuses[1];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.ANGUISH))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyRangeRatingMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_RANGER)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_RANGED)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.ANGUISH)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getRangeDamageMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.OVERPOWERING_FORCE)) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER);
				value += level >= 44 ? 0.06 : level >= 26 ? 0.04 : 0.02;
			} else if (isActive(Prayer.EAGLE_EYE))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_RANGE_STRENGTH)) {
				double d = leechBonuses[1];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.ANGUISH))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyRangeDamageMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_RANGE_STRENGTH)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_RANGE_STRENGTH)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.ANGUISH)) {
			double d = leechBonuses[1];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getAttackRatingMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.INCREDIBLE_REFLEXES)) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER);
				value += level >= 34 ? 0.06 : level >= 16 ? 0.04 : 0.02;
			} else if (isActive(Prayer.CHIVALRY))
				value += 0.07;
			else if (isActive(Prayer.PIETY))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_ATTACK)) {
				double d = leechBonuses[0];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.TURMOIL))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyAttackRatingMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_WARRIOR)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_ATTACK)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.TURMOIL)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getAttackDamageMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.ULTIMATE_STRENGTH)) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER);
				value += level >= 31 ? 0.06 : level >= 13 ? 0.04 : 0.02;
			} else if (isActive(Prayer.CHIVALRY))
				value += 0.07;
			else if (isActive(Prayer.PIETY))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_STRENGTH)) {
				double d = leechBonuses[2];
				value += (d / 100) + 0.02;
			} else if (isActive(Prayer.TURMOIL))
				value += 0.1;
		}
		return value;
	}

	public double getEnemyAttackDamageMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_STRENGTH)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_STRENGTH)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.TURMOIL)) {
			double d = leechBonuses[0];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public double getDefenceRatingMultiplier() {
		if (!hasPrayersOn())
			return 1.0;
		double value = 1.0;
		if (((byte) type == 0)) {
			if (isActive(Prayer.STEEL_SKIN)) {
				int level = player.getSkills().getLevelForXp(Skills.PRAYER);
				value += level >= 28 ? 0.06 : level >= 10 ? 0.04 : 0.02;
			} else if (isActive(Prayer.CHIVALRY))
				value += 0.07;
			else if (isActive(Prayer.PIETY) || isActive(Prayer.EAGLE_EYE)
					|| isActive(Prayer.MYSTIC_MIGHT))
				value += 0.08;
		} else {
			if (isActive(Prayer.LEECH_DEFENCE)) {

				double d = leechBonuses[3];
				value += (d / 100) + 0.02;
				// 1, 10 SUMMONING
			} else if (isActive(Prayer.DEFLECT_MELEE)
					|| isActive(Prayer.DEFLECT_MISSILES)
					|| isActive(Prayer.DEFLECT_MAGIC)) {

				value += 0.25;
			}

		}
		return value;
	}

	public double getEnemyDefenceRatingMultiplier() {
		if (!hasPrayersOn() || ((byte) type == 0))
			return 1.0;
		double value = 1.0;
		if (isActive(Prayer.SAP_DEFENCE)) {
			double d = leechBonuses[3];
			value -= (d / 100) + 0.03;
		} else if (isActive(Prayer.LEECH_DEFENCE)) {
			double d = leechBonuses[3];
			value -= (d / 100) + 0.06;
		} else if (isActive(Prayer.TURMOIL) || isActive(Prayer.ANGUISH)
				|| isActive(Prayer.TORMENT)) {
			double d = leechBonuses[3];
			value -= (d / 100) + 0.09;
		}
		return value;
	}

	public boolean reachedMax(int bonus) {
		return leechBonuses[bonus] >= 6;
	}

	public void increaseLeechBonus(int bonus) {
		boostedLeech = true;
		if (reachedMax(bonus))
			return;
		leechBonuses[bonus]++;
	}

	public void decreaseLeechBonus() {
		for (int i = 0; i < leechBonuses.length; i++)
			if (leechBonuses[i] > 0)
				leechBonuses[i]--;
	}

	private static final int CURSE_PROC_CHANCE = 5;

	public void handleHitPrayers(final Entity target, Hit hit) {
		if (!hasPrayersOn() || hit.getDamage() == 0)
			return;
		if (((byte) type == 0)) { // updated to rs3
			if (isActive(Prayer.SMITE) && target instanceof Player)
				((Player) target).getPrayer().drain(hit.getDamage() / 150);
		} else if (!boostedLeech) {
			// turmoil
			if (((isActive(Prayer.TURMOIL) && hit.getLook() == HitLook.MELEE_DAMAGE)
					|| (isActive(Prayer.ANGUISH) && hit.getLook() == HitLook.RANGE_DAMAGE) || (isActive(Prayer.TORMENT) && hit
					.getLook() == HitLook.MAGIC_DAMAGE))
					&& Utils.random(CURSE_PROC_CHANCE) == 0) { // turmoil
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's "
								+ (isActive(Prayer.TURMOIL) ? "melee"
										: isActive(Prayer.ANGUISH) ? "ranged"
												: "magic")
								+ " attack, strength and defence.", true);
				increaseLeechBonus(isActive(Prayer.TURMOIL) ? 0
						: isActive(Prayer.ANGUISH) ? 1 : 2);
				increaseLeechBonus(3);
				return;
			}
			// saps
			if ((isActive(Prayer.SAP_WARRIOR) || isActive(Prayer.SAP_MAGE))
					&& hit.getLook() == HitLook.MELEE_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's melee "
								+ (isActive(Prayer.SAP_WARRIOR) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(0);
				leech(target, 12569, 2214, 2215, 2216);
			}
			if ((isActive(Prayer.SAP_RANGER) || isActive(Prayer.SAP_RANGE_STRENGTH))
					&& hit.getLook() == HitLook.RANGE_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's ranged "
								+ (isActive(Prayer.SAP_RANGER) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(1);
				leech(target, 12569, 2217, 2218, 2219);
			}
			if ((isActive(Prayer.SAP_MAGE) || isActive(Prayer.SAP_MAGE))
					&& hit.getLook() == HitLook.MAGIC_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's magic "
								+ (isActive(Prayer.SAP_MAGE) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(2);
				leech(target, 12569, 2220, 2221, 2222);
			}
			if (isActive(Prayer.SAP_DEFENCE)
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's defence.",
						true);
				increaseLeechBonus(3);
				leech(target, 12569, 2223, 2224, 2225);
			}
			if (isActive(Prayer.SAP_SPIRIT) && target instanceof Player
					&& Utils.random(CURSE_PROC_CHANCE * 3) == 0) {
				player.getPackets()
						.sendGameMessage(
								"<col=00FF00>Your curse drains the enemy's special attack.",
								true);
				((Player) target).getCombatDefinitions().increaseSpecialAttack(
						-10);
				boostedLeech = true;
				leech(target, 12569, 2223, 2224, 2225);
			}
			// curses
			if ((isActive(Prayer.LEECH_ATTACK) || isActive(Prayer.LEECH_STRENGTH))
					&& hit.getLook() == HitLook.MELEE_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's melee "
								+ (isActive(Prayer.LEECH_ATTACK) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(0);
				leech(target, 12575, -1, 2231, 2232);
			}
			if ((isActive(Prayer.LEECH_RANGED) || isActive(Prayer.LEECH_RANGE_STRENGTH))
					&& hit.getLook() == HitLook.RANGE_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's ranged "
								+ (isActive(Prayer.LEECH_RANGED) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(1);
				leech(target, 12575, -1, 2236, 2238);
			}
			if ((isActive(Prayer.LEECH_MAGIC) || isActive(Prayer.LEECH_MAGIC_STRENGTH))
					&& hit.getLook() == HitLook.MAGIC_DAMAGE
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's magic "
								+ (isActive(Prayer.LEECH_MAGIC) ? "attack"
										: "strength") + ".", true);
				increaseLeechBonus(2);
				leech(target, 12575, -1, 2240, 2242);
			}
			if (isActive(Prayer.LEECH_DEFENCE)
					&& Utils.random(CURSE_PROC_CHANCE * 2) == 0) {
				player.getPackets().sendGameMessage(
						"<col=00FF00>Your curse drains the enemy's defence.",
						true);
				increaseLeechBonus(3);
				leech(target, 12575, -1, 2244, 2246);
			}
			if (isActive(Prayer.LEECH_ENERGY) && target instanceof Player
					&& Utils.random(CURSE_PROC_CHANCE * 3) == 0) {
				player.getPackets()
						.sendGameMessage(
								"<col=00FF00>Your curse drains the enemy's run energy.",
								true);
				((Player) target)
						.setRunEnergy(((Player) target).getRunEnergy() > 10 ? ((Player) target)
								.getRunEnergy() - 10 : 0);
				player.setRunEnergy(player.getRunEnergy() > 90 ? 100 : player
						.getRunEnergy() + 10);
				boostedLeech = true;
				leech(target, 12575, -1, 2256, 2258);
			}
			if (isActive(Prayer.LEECH_ADRENALINE) && target instanceof Player
					&& Utils.random(CURSE_PROC_CHANCE * 3) == 0) {
				player.getPackets()
						.sendGameMessage(
								"<col=00FF00Your curse drains the enemy's special attack.",
								true);
				((Player) target).getCombatDefinitions().increaseSpecialAttack(
						-10);
				player.getCombatDefinitions().increaseSpecialAttack(10);
				boostedLeech = true;
				leech(target, 12575, -1, 2252, 2254);
			}
		}
	}

	private void leech(Entity target, int emote, int gfx, int projectile,
			int targetGfx) {
		player.setNextAnimation(new Animation(emote));
		if (gfx != -1)
			player.setNextGraphics(new Graphics(gfx));
		target.setNextGraphics(new Graphics(targetGfx,
				World.sendProjectile(player, target, projectile, 35, 35, 20, 5,
						0, 0).getEndTime() / 10, 0));
	}

	public int getPrayerHeadIcon() {
		if (!hasPrayersOn())
			return -1;
		int value = -1;
		if ((byte) type == 0) {
			if (isActive(Prayer.PROTECT_FROM_SUMMONING))
				value += 8;
			if (isActive(Prayer.PROTECT_FROM_MAGIC))
				value += 3;
			else if (isActive(Prayer.PROTECT_FROM_MISSILES))
				value += 2;
			else if (isActive(Prayer.PROTECT_FROM_MELEE))
				value += 1;
			else if (isActive(Prayer.RETRIBUTION))
				value += 4;
			else if (isActive(Prayer.REDEMPTION))
				value += 6;
			else if (isActive(Prayer.SMITE))
				value += 5;
		} else {
			if (isActive(Prayer.DEFLECT_SUMMONING)) {
				value += 16;
				if (isActive(Prayer.DEFLECT_MISSILES))
					value += 2;
				else if (isActive(Prayer.DEFLECT_MAGIC))
					value += 3;
				else if (isActive(Prayer.DEFLECT_MELEE))
					value += 1;
			} else if (isActive(Prayer.DEFLECT_MAGIC))
				value += 14;
			else if (isActive(Prayer.DEFLECT_MISSILES))
				value += 15;
			else if (isActive(Prayer.DEFLECT_MELEE))
				value += 13;
			else if (isActive(Prayer.WRATH))
				value += 20;
			else if (isActive(Prayer.SOUL_SPLIT))
				value += 21;
			else if (isActive(Prayer.SOUL_LINK))
				value += 33;
			else if (isActive(Prayer.TEAMWORK_PROTECTION))
				value += 34;
		}
		// TODO
		return value;
	}

}