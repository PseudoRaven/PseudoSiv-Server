package net.nocturne.game.player.actions.skills.prayer;

import java.util.HashMap;
import java.util.Map;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Scattering {

	public enum Ash {

		IMPIOUS(ItemIdentifiers.IMPIOUS_ASHES, 4), ACCURSED(
				ItemIdentifiers.ACCURSED_ASHES, 12), INFERNAL(
				ItemIdentifiers.INFERNAL_ASHES, 62), TORTURED(
				ItemIdentifiers.TORTURED_ASHES, 90), SEARING(
				ItemIdentifiers.SEARING_ASHES, 200);

		private int id;
		private double experience;

		private static final Map<Integer, Ash> ashes = new HashMap<>();

		static {
			for (Ash ash : Ash.values()) {
				ashes.put(ash.getId(), ash);
			}
		}

		public static Ash forId(int id) {
			return ashes.get(id);
		}

		Ash(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public double getExperience() {
			return experience;
		}

	}

	public static boolean scatterAsh(final Player player, final int itemId) {
		if (Ash.forId(itemId) != null) {
			final Ash ash = Ash.forId(itemId);
			if (!player.getInventory().containsItem(itemId, 1))
				return true;
			player.getInventory().deleteItem(itemId, 1);
			player.setNextAnimation(new Animation(445));

			switch (ash.getId()) {
			case ItemIdentifiers.IMPIOUS_ASHES:
				player.setNextGraphics(new Graphics(56));
				break;
			case ItemIdentifiers.ACCURSED_ASHES:
				player.setNextGraphics(new Graphics(47));
				break;
			case ItemIdentifiers.INFERNAL_ASHES:
				player.setNextGraphics(new Graphics(40));
				break;
			}
			player.getPackets().sendGameMessage(
					"You scatter the ashes in the wind.");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getSkills()
							.addXp(Skills.PRAYER, ash.getExperience());
					stop();
				}

			}, 2);
			return true;
		}
		return false;
	}
}
