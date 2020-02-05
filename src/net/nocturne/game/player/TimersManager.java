package net.nocturne.game.player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.loaders.ClientScriptMap;
import net.nocturne.cache.loaders.GeneralRequirementMap;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.game.TemporaryAtributtes.Key;
import net.nocturne.game.player.actions.HomeTeleport;
import net.nocturne.game.player.controllers.DungeonController;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public class TimersManager implements Serializable {

	private static final long serialVersionUID = -3278352340946510939L;

	private static final int BEAST_MENU_VAR = 4517;
	private Player player;
	private Map<RecordKey, BossRecord> record;
	private int currentTime;

	public TimersManager() {

		record = new HashMap<>();
		currentTime = 0;
	}

	public void setPlayer(Player player) {
		this.player = player;
		if (record == null) // temporary
			record = new HashMap<>();
	}

	public void init() {
		sendBossKills();
		sendSlayerCreatureKills();
	}

	public boolean isActive() {
		return currentTime > 0;
	}

	public void process() {
		if (currentTime < (Integer.MAX_VALUE / 30))
			currentTime++;
	}

	/*
	 * when boss respawns / instance starts depending in type
	 */
	public void sendTimer() {
		if (!isActive()) {
			currentTime = 1;
			sendInterfaces();
		}
	}

	public void sendInterfaces() {
		if (isActive())
			player.getInterfaceManager().sendTimerInterface();
	}

	/*
	 * doesnt save
	 */
	public void removeTimer() {
		removeTimer(null);
	}

	public void removeTimer(RecordKey key, boolean hm) {
		if (key != null) {
			BossRecord record = getRecord(key);
			if (record == null) {
				record = new BossRecord();
				this.record.put(key, record);
			}
			record.fastestKill = (record.fastestKill <= 0 || record.fastestKill > currentTime) ? currentTime
					: record.fastestKill;
			System.out.println(record.fastestKill);
			sendBossKills();
		}
		currentTime = 0;
		player.getInterfaceManager().removeTimerInterface();
	}

	/*
	 * if null, doesnt save the time it done, if not null, saves
	 */
	public void removeTimer(RecordKey key) {
		removeTimer(key, false);
	}

	public void increaseKills(RecordKey key, boolean hm) {
		if (isActive())
			return;
		BossRecord record = getRecord(key);
		if (record == null) {
			record = new BossRecord();
			this.record.put(key, record);
		}
		if (key.hasHM && hm)
			record.totalHmKills++;
		else
			record.totalKills++;
		sendBossKills();
	}

	private BossRecord getRecord(RecordKey key) {
		return record.get(key);
	}

	public void setBeastMenu(int index) {
		if (index >= 3 || index < 0)
			return;
		player.getVarsManager().sendVar(BEAST_MENU_VAR, index);
		if (index == 0)
			openBossInfo(0);
	}

	// must be sent before
	private void sendBossKills() {

		BossRecord moleRecord = getRecord(RecordKey.GIANT_MOLE);

		player.getVarsManager().sendVarBit(22946,
				moleRecord != null ? moleRecord.totalKills : 0);

		player.getVarsManager().sendVarBit(22947,
				moleRecord != null ? moleRecord.totalHmKills : 0);

		BossRecord kbdRecord = getRecord(RecordKey.KING_BLACK_DRAGON);

		player.getVarsManager().sendVarBit(22948,
				kbdRecord != null ? kbdRecord.totalKills : 0);

		BossRecord barrowsRecord = getRecord(RecordKey.THE_BARROWS_BROTHERS);

		player.getVarsManager().sendVarBit(22949,
				barrowsRecord != null ? barrowsRecord.totalKills : 0);

		BossRecord chaosElementalRecord = getRecord(RecordKey.CHAOS_ELEMENTAL);

		player.getVarsManager().sendVarBit(
				22950,
				chaosElementalRecord != null ? chaosElementalRecord.totalKills
						: 0);

		BossRecord kalphiteQueenRecord = getRecord(RecordKey.KALPHITE_QUEEN);

		player.getVarsManager().sendVarBit(
				22951,
				kalphiteQueenRecord != null ? kalphiteQueenRecord.totalKills
						: 0);

		BossRecord fightCavesRecord = getRecord(RecordKey.FIGHT_CAVES);

		player.getVarsManager().sendVarBit(22952,
				fightCavesRecord != null ? fightCavesRecord.totalKills : 0);

		BossRecord corperealBeastRecord = getRecord(RecordKey.CORPOREAL_BEAST);

		player.getVarsManager().sendVarBit(
				22953,
				corperealBeastRecord != null ? corperealBeastRecord.totalKills
						: 0);

		BossRecord draganothKingsRecord = getRecord(RecordKey.DAGANNOTH_KINGS);

		player.getVarsManager().sendVarBit(
				22954,
				draganothKingsRecord != null ? draganothKingsRecord.totalKills
						: 0);

		BossRecord qbdRecord = getRecord(RecordKey.QUEEN_BLACK_DRAGON);

		player.getVarsManager().sendVarBit(22955,
				qbdRecord != null ? qbdRecord.totalKills : 0);

		BossRecord czRecord = getRecord(RecordKey.COMMANDER_ZILYANA);

		player.getVarsManager().sendVarBit(22956,
				czRecord != null ? czRecord.totalKills : 0);

		player.getVarsManager().sendVarBit(22957,
				czRecord != null ? czRecord.totalHmKills : 0);

		BossRecord generalGraadorRecord = getRecord(RecordKey.GENERAL_GRAARDOR);

		player.getVarsManager().sendVarBit(
				22958,
				generalGraadorRecord != null ? generalGraadorRecord.totalKills
						: 0);

		player.getVarsManager()
				.sendVarBit(
						22959,
						generalGraadorRecord != null ? generalGraadorRecord.totalHmKills
								: 0);

		BossRecord kreeAraRecord = getRecord(RecordKey.KREE_ARRA);

		player.getVarsManager().sendVarBit(22960,
				kreeAraRecord != null ? kreeAraRecord.totalKills : 0);

		player.getVarsManager().sendVarBit(22961,
				kreeAraRecord != null ? kreeAraRecord.totalHmKills : 0);

		BossRecord krilTsutsarothRecord = getRecord(RecordKey.KRIL_TSUTSAROTH);

		player.getVarsManager().sendVarBit(
				22962,
				krilTsutsarothRecord != null ? krilTsutsarothRecord.totalKills
						: 0);

		player.getVarsManager()
				.sendVarBit(
						22963,
						krilTsutsarothRecord != null ? krilTsutsarothRecord.totalHmKills
								: 0);

		BossRecord kilnRecord = getRecord(RecordKey.FIGHT_KILN);

		player.getVarsManager().sendVarBit(22964,
				kilnRecord != null ? kilnRecord.totalKills : 0);

		BossRecord kalphiteKingRecord = getRecord(RecordKey.KALPHITE_KING);

		player.getVarsManager().sendVarBit(22965,
				kalphiteKingRecord != null ? kalphiteKingRecord.totalKills : 0);

		BossRecord legionesRecord = getRecord(RecordKey.LEGIONES);

		player.getVarsManager().sendVarBit(22966,
				legionesRecord != null ? legionesRecord.totalKills : 0);

		BossRecord nexRecord = getRecord(RecordKey.NEX);

		player.getVarsManager().sendVarBit(22967,
				nexRecord != null ? nexRecord.totalKills : 0);

		BossRecord riseOfTheSixRecord = getRecord(RecordKey.THE_BARROWS_RISE_OF_THE_SIX);

		player.getVarsManager().sendVarBit(22968,
				riseOfTheSixRecord != null ? riseOfTheSixRecord.totalKills : 0);

		BossRecord araxxiRecord = getRecord(RecordKey.ARAXXI);

		player.getVarsManager().sendVarBit(22969,
				araxxiRecord != null ? araxxiRecord.totalKills : 0);

		// BossRecord evilChickenRecord = getRecord(RecordKey.EVIL_CHICKEN);

		// player.getVarsManager().sendVarBit(22972,
		// evilChickenRecord != null ? evilChickenRecord.totalKills : 0);

		BossRecord voragoRecord = getRecord(RecordKey.VORAGO);

		player.getVarsManager().sendVarBit(22970,
				voragoRecord != null ? voragoRecord.totalKills : 0);

		player.getVarsManager().sendVarBit(22971,
				voragoRecord != null ? voragoRecord.totalHmKills : 0);

	}

	public void sendSlayerCreatureKills() {
		/*
		 * player.getVarsManager().sendVarBit(22921, 0);// Terror Dogs
		 * player.getVarsManager().sendVarBit(22922, 420);// Bloodvields
		 * player.getVarsManager().sendVarBit(22923, 420);// Warped Tort
		 * player.getVarsManager().sendVarBit(22924, 420);// Abberant Specters
		 * player.getVarsManager().sendVarBit(22925, 0);// Dust Devils
		 * player.getVarsManager().sendVarBit(22926, 0);// Automations
		 * player.getVarsManager().sendVarBit(22927, 0);// Skeletal Wyverns
		 * player.getVarsManager().sendVarBit(22928, 0);// Jungle StykeWyrms
		 * player.getVarsManager().sendVarBit(22929, 0);// Aquanites
		 * player.getVarsManager().sendVarBit(22930, 0);// Nechraels
		 * player.getVarsManager().sendVarBit(22931, 0);// Mutated Jadinkos
		 * player.getVarsManager().sendVarBit(22932, 0);// Ascension Creatures
		 * player.getVarsManager().sendVarBit(22933, 0);// Polypore Creatures
		 * player.getVarsManager().sendVarBit(22934, 0);// Spiritual Mages
		 * player.getVarsManager().sendVarBit(22935, 0);// Abyssal Demons
		 * player.getVarsManager().sendVarBit(22936, 0);// Dark beasts
		 * player.getVarsManager().sendVarBit(22937, 0);// Airut
		 * player.getVarsManager().sendVarBit(22938, 0);// Ice StykeWyrms
		 * player.getVarsManager().sendVarBit(22939, 0);// Kal'gerion demons
		 * player.getVarsManager().sendVarBit(22940, 0);// Glacors
		 * player.getVarsManager().sendVarBit(22941, 0);// Tormented Demons
		 * player.getVarsManager().sendVarBit(22942, 0);// Gargoyls
		 * player.getVarsManager().sendVarBit(22943, 0);// Muspha
		 * player.getVarsManager().sendVarBit(22944, 0);// Nihil
		 * player.getVarsManager().sendVarBit(22945, 0);// Desert StykeWyrms
		 */
	}

	public void teleportToLodestone() {
		if (player.getVarsManager().getValue(BEAST_MENU_VAR) != 0)
			return;
		if (player.getControllerManager().getController() instanceof DungeonController) {
			player.getPackets().sendGameMessage(
					"You can't teleport to this location while in a dungeon.");
			return;
		}
		RecordKey key = (RecordKey) player.getTemporaryAttributtes().remove(
				Key.BOSS_INFO);
		if (key == null)
			return;
		player.getInterfaceManager().closeMenu();
		player.stopAll();
		HomeTeleport.useLodestone(player, key.lodestoneComponent);
	}

	public void openBossInfo(int index) {
		RecordKey key = RecordKey.forIndex(index);
		player.getTemporaryAttributtes().put(Key.BOSS_INFO, key);
		BossRecord record = getRecord(key);
		player.getPackets().sendCSVarInteger(4486,
				record == null ? 0 : record.totalKills);
		player.getPackets().sendCSVarInteger(4487,
				!key.hasHM ? -1 : record == null ? 0 : record.totalHmKills);
		player.getPackets().sendCSVarInteger(
				4488,
				!key.hasFastestKill ? -1 : record == null ? 0
						: record.fastestKill);
		GeneralRequirementMap data = GeneralRequirementMap
				.getMap(ClientScriptMap.getMap(9031).getIntValue(index));
		player.getPackets()
				.sendCSVarInteger(
						4484,
						NPCDefinitions.getNPCDefinitions(data.getIntValue(1347)).renderEmote);
		player.getPackets().sendCSVarInteger(4485, data.getId());
	}

	// ignore this. not related to code. only use removetimer and sendtimer
	// methods
	public void showTimer() {
		if (!isActive()) // shouldnt happen unless u open inter manualy
			return;

		player.getPackets().sendExecuteScript(720);
		player.getPackets().sendCSVarInteger(4258, currentTime * 30);
	}

	public enum RecordKey {
		ARAXXI(0, false, true, 29),

		THE_BARROWS_BROTHERS(1, false, false, 29),

		THE_BARROWS_RISE_OF_THE_SIX(2, false, true, 29),

		BEASTMASTER_DURZAG(3, false, false, 35),

		CHAOS_ELEMENTAL(4, false, false, 35),

		COMMANDER_ZILYANA(4, true, true, 18),

		CORPOREAL_BEAST(5, false, false, 35),

		DAGANNOTH_KINGS(5, false, false, 31),

		GENERAL_GRAARDOR(8, true, true, 18),

		GIANT_MOLE(9, true, true, 22),

		GREGOROVIC(10, false, false, 35),

		FIGHT_KILN(11, true, false, 32), // har-aken

		HELWYR(12, false, false, 35),

		KALPHITE_KING(13, true, false, 4),

		KALPHITE_QUEEN(20, false, false, 4),

		KING_BLACK_DRAGON(15, false, false, 35),

		KREE_ARRA(16, true, true, 18),

		KRIL_TSUTSAROTH(17, true, true, 18),

		LEGIONES(18, true, false, 33),

		NEX(19, true, false, 18),

		QUEEN_BLACK_DRAGON(20, true, false, 24),

		TELOS(21, false, true, 34),

		THE_TWIN_FURIES(22, false, true, 32),

		FIGHT_CAVES(23, true, false, 32), // tztok-jad

		VINDICTA_GORVEK(24, false, true, 34),

		VORAGO(25, true, true, 25),

		YAKAMARU(26, false, true, 26);

		// EVIL_CHICKEN(20, true, true, 22),

		// WILDY_WYRM(21, true, true, 22);

		private boolean hasHM, hasFastestKill;
		private int index, lodestoneComponent;

		RecordKey(int index, boolean hasHM, boolean hasFastestKill,
				int lodestoneComponent) {

			this.index = index;
			this.hasHM = hasHM;
			this.hasFastestKill = hasFastestKill;
			this.lodestoneComponent = lodestoneComponent;
		}

		public static RecordKey forName(String name) {
			for (RecordKey key : RecordKey.values())
				if (name.contains(key.toString().toLowerCase()
						.replace("_", " ")))
					return key;
			return null;
		}

		public static RecordKey forIndex(int id) {
			for (RecordKey key : RecordKey.values())
				if (key.ordinal() == id)
					return key;
			return null;
		}

		public int getIndex() {
			return index;
		}
	}

	private static class BossRecord implements Serializable {

		private static final long serialVersionUID = -8783909294743985403L;

		private int totalKills;
		private int totalHmKills;
		private int fastestKill;
	}
}