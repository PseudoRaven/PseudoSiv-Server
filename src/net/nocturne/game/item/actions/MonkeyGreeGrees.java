package net.nocturne.game.item.actions;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.player.Player;

/**
 * @author Miles Black (bobismyname)
 */

public class MonkeyGreeGrees {

	private enum GreeGree {

		SMALL_NINJA(4024, 1480, "Ninja"), LARGE_NINJA(4025, 1481, "Ninja"), GORILLA(
				4026, 1482, "Gorilla"), BEARDED_GORILLA(4027, 1483, "Gorilla"), BLUE_GORILLA(
				4028, 1484, "Gorilla"), SMALL_ZOMBIE(4029, 1485, "Zombie"), LARGE_ZOMBIE(
				4030, 1486, "Zombie"), KARAMJA(4031, 1487, "Karamja");

		public static Map<Integer, GreeGree> greegrees = new HashMap<Integer, GreeGree>();

		public static GreeGree forId(int id) {
			return greegrees.get(id);
		}

		static {
			for (GreeGree greegree : GreeGree.values()) {
				greegrees.put(greegree.itemId, greegree);
			}
		}

		private int itemId;
		private int npcId;
		private String type;

		GreeGree(int itemId, int npcId, String type) {
			this.itemId = itemId;
			this.npcId = npcId;
			this.type = type;
		}

		@SuppressWarnings("unused")
		public int getItemId() {
			return itemId;
		}

		public int getNpcId() {
			return npcId;
		}

		public String getType() {
			return type;
		}
	}

	public static boolean transform(Player player, int itemId) {
		if (GreeGree.forId(itemId) != null) {
			GreeGree monkey = GreeGree.forId(itemId);
			if (!isAtApeAtoll(player)) {
				player.getPackets()
						.sendGameMessage(
								"You have to be in Ape Atoll in order to transform into a monkey.");
				return true;
			}
			player.getAppearence().transformIntoNPC(monkey.getNpcId());
			player.getPackets().sendGameMessage(
					"You transform into a " + monkey.getType().toLowerCase()
							+ " monkey.");
			return true;
		}
		return false;
	}

	public static void checkMovement(Player player) {
		if (player.getAppearence().getTransformedNpcId() == -1) {
			return;
		}
		if (GreeGree.forId(player.getEquipment().getWeaponId()) != null) {
			GreeGree monkey = GreeGree.forId(player.getEquipment()
					.getWeaponId());
			if (!isAtApeAtoll(player)
					&& player.getAppearence().getTransformedNpcId() == monkey
							.getNpcId()) {
				player.getAppearence().transformIntoNPC(-1);
				player.getPackets().sendGameMessage(
						"You transform back into a human.");
			}
		}
	}

	public static boolean isAtApeAtoll(Player player) {
		return (player.getX() >= 2693 && player.getX() <= 2821
				&& player.getY() >= 2693 && player.getY() <= 2817);
	}

}