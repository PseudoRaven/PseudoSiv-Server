package net.nocturne.game.item.actions;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.player.Player;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Dec 15, 2016
 */

public class ItemDyes {

	private final static int BARROWS = 33294;
	private final static int SHADOW = 33296;
	private final static int THIRDAGE = 33298;
	private final static int BLOOD = 36274;

	private enum Dyes {

		DRYGORE_MACE(26595, 33366, 33300, 33432, 36303), DRYGORE_OFF_MACE(
				26599, 33369, 33303, 33435, 36306), DRYGORE_RAPIER(26579,
				33372, 33306, 33438, 36309), DRYGORE_OFF_RAPIER(26583, 33375,
				33309, 33441, 36312), DRYGORE_LONGSWORD(26587, 33378, 33312,
				33444, 36315), DRYGORE_OFF_LONGSWORD(26591, 33381, 33315,
				33447, 36318), ASCENSION_CROSSBOW(28437, 33384, 33318, 33450,
				36321), ASCENSION_OFF_CROSSBOW(28441, 33387, 33321, 33453,
				36324), SEIMIC_WAND(28617, 33390, 33324, 33456, 36327), SEISMIC_SINGULARITY(
				28621, 33393, 33327, 33459, 36330), NOXIOUS_SCYTHE(31725,
				33396, 33330, 33462, 36333), NOXIOUS_STAFF(31729, 33399, 33333,
				33465, 36336), NOXIOUS_LONGBOW(31733, 33402, 33336, 33468,
				36339), TECTONIC_MASK(28608, 33405, 33339, 33471, 36276), TECTONIC_ROBE_TOP(
				28611, 33408, 33342, 33474, 36279), TECTONIC_ROBE_BOTTOM(28614,
				33411, 33345, 33477, 36282), SIRENIC_MASK(29854, 33414, 33348,
				33480, 36285), SIRENIC_HAUBERK(29857, 33417, 33351, 33483,
				36288), SIRENIC_CHAPS(29860, 33420, 33354, 33486, 36291), MALEVOLENT_HELM(
				30005, 33423, 33357, 33489, 36294), MALEVOLENT_CUIRASS(30008,
				33426, 33360, 33492, 36297), MALEVOLENT_GREAVES(30011, 33429,
				33363, 33495, 36300);

		public static Map<Integer, Dyes> dyes = new HashMap<>();

		public static Dyes forId(int id) {
			return dyes.get(id);
		}

		static {
			for (Dyes dye : Dyes.values()) {
				dyes.put(dye.originalId, dye);
			}
		}

		private int originalId;
		private int shadowId;
		private int barrowsId;
		private int thirdId;
		private int bloodId;

		Dyes(int originalId, int shadowId, int barrowsId, int thirdId,
				int bloodId) {
			this.originalId = originalId;
			this.shadowId = shadowId;
			this.barrowsId = barrowsId;
			this.thirdId = thirdId;
			this.bloodId = bloodId;
		}

		public int getShadowId() {
			return shadowId;
		}

		public int getBarrowsId() {
			return barrowsId;
		}

		public int getThirdId() {
			return thirdId;
		}

		public int getBloodId() {
			return bloodId;
		}
	}

	public static boolean dyeItem(Player player, int itemUsed, int usedWith) {
		int dyeId = -1, originalId = -1;
		if ((usedWith == BARROWS || usedWith == SHADOW || usedWith == THIRDAGE || usedWith == BLOOD)
				&& Dyes.forId(itemUsed) != null) {
			dyeId = usedWith;
			originalId = itemUsed;
		} else if ((itemUsed == BARROWS || itemUsed == SHADOW
				|| itemUsed == THIRDAGE || itemUsed == BLOOD)
				&& Dyes.forId(usedWith) != null) {
			dyeId = itemUsed;
			originalId = usedWith;
		}
		if (dyeId == -1 || originalId == -1)
			return false;
		Dyes dye = Dyes.forId(originalId);
		player.getInventory().deleteItem(dyeId, 1);
		player.getInventory().deleteItem(originalId, 1);

		switch (dyeId) {
		case BARROWS:
			player.getInventory().addItem(dye.getBarrowsId(), 1);
			break;
		case SHADOW:
			player.getInventory().addItem(dye.getShadowId(), 1);
			break;
		case THIRDAGE:
			player.getInventory().addItem(dye.getThirdId(), 1);
			break;
		case BLOOD:
			player.getInventory().addItem(dye.getBloodId(), 1);
			break;
		}
		return true;
	}

}
