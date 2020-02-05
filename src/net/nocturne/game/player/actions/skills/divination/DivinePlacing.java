package net.nocturne.game.player.actions.skills.divination;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class DivinePlacing {

	protected static Player player;

	private static void check(final Player owner) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (owner.divine > 1)
					return;
				if (owner.divine == 0) {
					owner.getPackets().sendGameMessage(
							"Your divine has vanished.");
					stop();
				}
			}
		}, 0, 0);
	}

	public static void placeDivine(final Player owner, final int item,
			final int fobject, final int lobject, final int lvl,
			final int skillId) {
		if ((Utils.currentTimeMillis() - owner.lastDivineLocation) < (24 * 60 * 60 * 1000)) {
			if ((owner.divineCount >= 6 && owner.getDonationManager()
					.isHeroicDonator())
					|| (owner.divineCount >= 5 && owner.getDonationManager()
							.isDemonicDonator())
					|| (owner.divineCount >= 4 && owner.getDonationManager()
							.isAngelicDonator())
					|| (owner.divineCount >= 3
							&& owner.getDonationManager().isDivineDonator()
							|| (owner.divineCount >= 2 && owner
									.getDonationManager().isDonator()) || (owner.divineCount >= 1 && !owner
							.getDonationManager().isDonator()))) {
				owner.getPackets()
						.sendGameMessage(
								"You have created the max amount of divine locations today.");
				return;
			}
		} else {
			if ((owner.divineCount >= 6 && owner.getDonationManager()
					.isHeroicDonator())
					|| (owner.divineCount >= 5 && owner.getDonationManager()
							.isDemonicDonator())
					|| (owner.divineCount >= 4 && owner.getDonationManager()
							.isAngelicDonator())
					|| (owner.divineCount >= 3
							&& owner.getDonationManager().isDivineDonator()
							|| (owner.divineCount >= 2 && owner
									.getDonationManager().isDonator()) || (owner.divineCount >= 1 && !owner
							.getDonationManager().isDonator())))
				owner.lastDivineLocation = Utils.currentTimeMillis();
		}
		owner.divineCount++;
		if ((owner.divineCount > 6 && owner.getDonationManager()
				.isHeroicDonator())
				|| (owner.divineCount > 5 && owner.getDonationManager()
						.isDemonicDonator())
				|| (owner.divineCount > 4 && owner.getDonationManager()
						.isAngelicDonator())
				|| (owner.divineCount > 3
						&& owner.getDonationManager().isDivineDonator()
						|| (owner.divineCount > 2 && owner.getDonationManager()
								.isDonator()) || (owner.divineCount > 1 && !owner
						.getDonationManager().isDonator())))
			owner.divineCount = 0;
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;

			@Override
			public void run() {
				ticks++;
				if (ticks == 1) {
					owner.getInventory().deleteItem(item, 1);
					owner.setNextAnimation(new Animation(21217));
					final WorldObject divinefirststage = new WorldObject(
							fobject, 10, 0, owner.getX() + 1, owner.getY(),
							owner.getPlane());
					owner.faceObject(divinefirststage);
					World.spawnTemporaryObject(divinefirststage, 2700);
				}
				if (ticks == 5) {
					check(owner);
					final WorldObject divinefinalstage = new WorldObject(
							lobject, 10, 0, owner.getX() + 1, owner.getY(),
							owner.getPlane(), owner);
					World.spawnTemporaryDivineObject(divinefinalstage, 40000,
							owner);
					owner.divine = lobject;
					player = owner;
					stop();
				}
			}
		}, 0, 0);
	}
}