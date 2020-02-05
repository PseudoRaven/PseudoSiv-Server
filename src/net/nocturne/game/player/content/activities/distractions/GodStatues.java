package net.nocturne.game.player.content.activities.distractions;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.actions.Lamps;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

/**
 * @author Miles Black (bobismyname)
 * @date Jan 2, 2017
 */

public class GodStatues {

	private enum Statue {

		SARADOMIN(81949, 17688, 7), ZAMORAK(81950, 17689, 12), BANDOS(81951,
				17690, 17), GUTHIX(81948, 17687, 2), SEREN(93234, 24942, 22);

		private int objectId, varbit, baseValue;

		Statue(int objectId, int configId, int baseValue) {
			this.objectId = objectId;
			this.varbit = configId;
			this.baseValue = baseValue;
		}

		public static Statue forId(int id) {
			for (Statue statue : Statue.values()) {
				if (statue.objectId == id)
					return statue;
			}
			return null;
		}
	}

	public static boolean handleObject(Player player, WorldObject object,
			int option) {
		Statue statue = Statue.forId(object.getId());
		if (statue == null)
			return false;
		if (option == 1)
			player.getPackets().sendGameMessage(
					"Looks like I should build on this statue.");
		else {
			int displacement = statue == Statue.SEREN ? 0
					: getDisplacement(player);
			WorldTasksManager.schedule(new WorldTask() {
				int stage = 0;

				@Override
				public void run() {
					if (stage == 0) {
						player.getVarbits().put(statue.varbit, 1);
						player.loadVarbits();
						player.setNextAnimation(new Animation(7299));
					} else if (stage == 2)
						player.setNextAnimation(new Animation(7299));
					else if (stage == 4) {
						player.getVarbits().put(statue.varbit,
								statue.baseValue + displacement);
						player.loadVarbits();
						player.getSkills().addXpLamp(
								Skills.CONSTRUCTION,
								Lamps.getExp(
										player.getSkills().getLevel(
												Skills.CONSTRUCTION), 3));
						player.getPackets()
								.sendGameMessage(
										"You successfully build a statue to "
												+ statue.toString()
														.toLowerCase() + ".");
					}
					stage++;
				}
			}, 3);
		}
		return true;
	}

	private static int getDisplacement(Player player) {
		if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 90)
			return 4;
		else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 72)
			return 3;
		else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 52)
			return 2;
		else if (player.getSkills().getLevel(Skills.CONSTRUCTION) >= 32)
			return 1;
		return 0;
	}

}
