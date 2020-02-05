package net.nocturne.game.player.actions;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Portables {

	protected static Player player;

	public static void deployPortable(final Player owner, final int item,
			final int fobject, final int lobject) {
		if (owner.isCanPvp()) {
			owner.getPackets().sendGameMessage(
					"You cant deploy a portable while doing this action.");
			return;
		}
		if (World.getStandartObject(owner) != null) {
			owner.getPackets().sendGameMessage(
					"You can't deploy a portable here.");
			return;
		}
		if (!owner.getInventory().containsItem(item, 1)) {
			owner.getPackets().sendGameMessage(
					"You don't have a portable in your inventory.");
			return;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				owner.stopAll();
				owner.getInventory().deleteItem(item, 1);
				checkAll(owner, item);
				final WorldObject portablefinalstage = new WorldObject(lobject,
						10, 0, owner.getX() + 1, owner.getY(),
						owner.getPlane(), owner);
				World.spawnTemporarPortableObject(portablefinalstage, 250000,
						owner);
				owner.faceObject(portablefinalstage);
				owner.setNextAnimation(new Animation(21217));
				owner.portable = lobject;
				player = owner;
				owner.portableLimit++;
				owner.getPackets().sendGameMessage(
						"The portable will last 5 minutes.", true);
				stop();
			}
		}, 0, 0);
	}

	private static void checkAll(Player owner, int portable) {
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (owner.portable > 1)
					return;
				if (owner.portable == 0) {
					owner.getPackets().sendGameMessage(
							"Your portable has vanished.", true);
					owner.portableLimit = 0;
					stop();
				}

			}
		}, 0, 0);
	}

	public enum Portable {
		PORTABLEWELL(89770), PORTABLEFORGE(89767), PORTABLERANGE(89768), PORTABLESAWMILL(
				89769), PORTABLEBANK(75932);

		private int id;

		Portable(int id) {

			this.id = id;
		}

		public int getId() {

			return id;
		}
	}
}