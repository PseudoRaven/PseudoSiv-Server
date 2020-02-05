package net.nocturne.game.player.actions.skills.mining;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public abstract class MiningBase extends Action {

	protected int pickaxeTime;

	public enum PickAxeDefinitions {

		NOVITE(16295, 1, 13074, 1),

		BATHUS(16297, 10, 13075, 3),

		MARMAROS(16299, 20, 13076, 5),

		KRATONITE(16301, 30, 13077, 7),

		FRACTITE(16303, 40, 13078, 10),

		ZEPHYRIUM(16305, 50, 13079, 12),

		ARGONITE(16307, 60, 13080, 13),

		KATAGON(16309, 70, 13081, 15),

		GORGONITE(16311, 80, 13082, 16),

		PROMETHIUM(16313, 90, 13083, 17),

		PRIMAL(16315, 99, 13084, 20),

		BRONZE(1265, 1, 625, 1),

		IRON(1267, 1, 626, 2),

		STEEL(1269, 6, 627, 3),

		MITHRIL(1273, 21, 629, 5),

		ADAMANT(1271, 31, 628, 7),

		RUNE(1275, 41, 624, 10),

		DRAGON(15259, 61, 12189, 13),

		ADZ(13661, 61, 10222, 13),

		CRYSTAL(32646, 71, 25062, 16);

		private final int pickAxeId;
		private final int levelRequired;
		private final int animationId;
		private final int pickAxeTime;

		PickAxeDefinitions(int pickAxeId, int levelRequired, int animationId,
				int pickAxeTime) {
			this.pickAxeId = pickAxeId;
			this.levelRequired = levelRequired;
			this.animationId = animationId;
			this.pickAxeTime = pickAxeTime;
		}

		public int getPickAxeId() {
			return pickAxeId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getAnimationId() {
			return animationId;
		}

		public int getPickAxeTime() {
			return pickAxeTime;
		}
	}

	protected boolean hasPickaxe(Player player) {
		if (player.getInventory().containsItemToolBelt(15259)
				|| player.getInventory().containsItemToolBelt(1275)
				|| player.getInventory().containsItemToolBelt(1271)
				|| player.getInventory().containsItemToolBelt(1273)
				|| player.getInventory().containsItemToolBelt(1269)
				|| player.getInventory().containsItemToolBelt(1267)
				|| player.getInventory().containsItemToolBelt(1265)
				|| player.getInventory().containsItemToolBelt(13661)
				|| player.getInventory().containsItemToolBelt(32646)) {
			return true;
		}
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1265:// Bronze PickAxe
		case 1267:// Iron PickAxe
		case 1269:// Steel PickAxe
		case 1273:// Mithril PickAxe
		case 1271:// Adamant PickAxe
		case 1275:// Rune PickAxe
		case 15259:// Dragon PickAxe
		case 13661: // Inferno adze
		case 32646: // crystal pickaxe
			return true;
		default:
			return false;
		}
	}

	protected boolean setPickaxe(Player player) {
		int level = player.getSkills().getLevel(Skills.MINING);
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 15259: // dragon pickaxe
				if (level >= 61) {
					pickaxeTime = 13;
					return true;
				}
				break;
			case 1275: // rune pickaxe
				if (level >= 41) {
					pickaxeTime = 10;
					return true;
				}
				break;
			case 1271: // adam pickaxe
				if (level >= 31) {
					pickaxeTime = 7;
					return true;
				}
				break;
			case 1273: // mith pickaxe
				if (level >= 21) {
					pickaxeTime = 5;
					return true;
				}
				break;
			case 1269: // steel pickaxe
				if (level >= 6) {
					pickaxeTime = 3;
					return true;
				}
				break;
			case 1267: // iron pickaxe
				pickaxeTime = 2;
				return true;
			case 1265: // bronze axe
				pickaxeTime = 1;
				return true;
			case 32646: // crystal axe
				pickaxeTime = 15;
				return true;
			case 13661: // Inferno adze
				if (level >= 61) {
					pickaxeTime = 13;
					return true;

				}
				break;
			}
		}
		if (player.getInventory().containsItemToolBelt(15259)) {
			if (level >= 61) {
				pickaxeTime = 13;
				return true;
			}
		}

		if (player.getInventory().containsItemToolBelt(1275)) {
			if (level >= 41) {
				pickaxeTime = 10;
				return true;
			}
		}
		if (player.getInventory().containsItemToolBelt(1271)) {
			if (level >= 31) {
				pickaxeTime = 7;
				return true;
			}
		}
		if (player.getInventory().containsItemToolBelt(1273)) {
			if (level >= 21) {
				pickaxeTime = 5;
				return true;
			}
		}
		if (player.getInventory().containsItemToolBelt(1269)) {
			if (level >= 6) {
				pickaxeTime = 3;
				return true;
			}
		}
		if (player.getInventory().containsItemToolBelt(1267)) {
			pickaxeTime = 2;
			return true;
		}
		if (player.getInventory().containsItemToolBelt(1265)) {
			pickaxeTime = 1;
			return true;
		}
		if (player.getInventory().containsItemToolBelt(32646)) {
			pickaxeTime = 15;
			return true;
		}
		if (player.getInventory().containsItemToolBelt(13661)) {
			if (level >= 61) {
				pickaxeTime = 13;
				return true;
			}

		}
		return false;

	}

	public static void prospect(final Player player, final String endMessage) {
		prospect(player, "You examine the rock for ores....", endMessage);
	}

	public static void prospect(final Player player, String startMessage,
			final String endMessage) {
		player.getPackets().sendGameMessage(startMessage, true);
		player.lock(5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage(endMessage);
			}
		}, 4);
	}

	public static PickAxeDefinitions getPickAxeDefinitions(Player player,
			boolean dungeoneering) {
		for (int i = dungeoneering ? 10
				: PickAxeDefinitions.values().length - 1; i >= (dungeoneering ? 0
				: 11); i--) {
			PickAxeDefinitions def = PickAxeDefinitions.values()[i];
			if (player.getInventory().containsItemToolBelt(def.pickAxeId)
					|| player.getEquipment().getWeaponId() == def.pickAxeId) {
				if (player.getSkills().getLevel(Skills.MINING) >= def.levelRequired)
					return def;
			}
		}
		return null;
	}

	protected int getSummoningBonus(Player player, int summoningBonus) {
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342
					|| player.getFamiliar().getId() == 7342)
				summoningBonus += 10;
			else if (player.getFamiliar().getId() == 6832
					|| player.getFamiliar().getId() == 6831)
				summoningBonus += 1;
		}
		return summoningBonus;
	}

	protected boolean hasMiningSuit(Player player) {
		return player.getEquipment().getHatId() == 20789
				&& player.getEquipment().getChestId() == 20791
				&& player.getEquipment().getLegsId() == 20790
				&& player.getEquipment().getBootsId() == 20788;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}