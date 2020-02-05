package net.nocturne.game.player.actions.skills.thieving;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.decoders.handlers.ObjectHandler;
import net.nocturne.utils.Utils;

public class LockPicking extends Action {

	private enum LockedObject {

		ARDOUGNE_GOLD_DOOR(34812, -1, 1, 3.5, 1, new int[] { 2246, -1 }, false),

		ARDOUGNE_HOUSE_DOOR(2556, -1, 13, 15, 1, new int[] { 2246, -1 }, false),

		ARDOUGNE_NATURE_DOOR(34812, -1, 16, 15, 1, new int[] { 2246, -1 },
				false),

		HAM_TRAP_DOOR(5492, 5492, 17, 4, 250, new int[] { 2244, 981 }, false),

		AXE_HUT_DOOR(2557, -1, 32, 25, 1, new int[] { 2246, -1 }, true),

		ARDOUGNE_SEWERS_GATE(-1, -1, 31, 25, 1, new int[] { 2246, -1 }, false),

		PIRATE_HALL(65717, -1, 39, 35, 1, new int[] { 2246, -1 }, true),

		CHAOS_DRUID_TOWER(2554, -1, 46, 37.5, 1, new int[] { 2246, -1 }, false),

		PALADIN_DOOR(2555, -1, 61, 50, 1, new int[] { 2246, -1 }, false),

		YANILLE_AGILITY_DUNGEON(2559, -1, 82, 50, 1, new int[] { 2246, -1 },
				true);

		public static LockedObject getObject(WorldObject obj) {
			for (final LockedObject locked : LockedObject.values()) {
				if (locked.getId() == 34812) {
					if (obj.getY() == 3308)
						return ARDOUGNE_GOLD_DOOR;
					else
						return ARDOUGNE_NATURE_DOOR;
				} else if (locked.getId() == obj.getId())
					return locked;
			}
			return null;
		}

		private final int id;
		private final int open;
		private final int level;
		private final double xp;
		private final int damage;
		private final int[] anims;

		private final boolean pick;

		LockedObject(int id, int open, int level, double xp, int damage,
				int[] anims, boolean pick) {
			this.id = id;
			this.open = open;
			this.level = level;
			this.xp = xp;
			this.damage = damage;
			this.anims = anims;
			this.pick = pick;
		}

		public int[] getAnimation() {
			return anims;
		}

		public int getDamage() {
			return damage;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public int getOpenId() {
			return open;
		}

		public double getXp() {
			return xp;
		}

		public boolean requiresPick() {
			return pick;
		}

	}

	public static boolean isLocked(Player player, WorldObject object, int option) {
		for (final LockedObject locked : LockedObject.values())
			if (locked.getId() == object.getId()) {
				if (option == 1)
					player.getPackets().sendGameMessage(
							"The door seems to be locked.");
				else if (option == 2)
					player.getActionManager()
							.setAction(new LockPicking(object));
				return true;
			}
		return false;
	}

	private final LockedObject locked;

	private final WorldObject object;

	private LockPicking(WorldObject obj) {
		this.object = obj;
		this.locked = LockedObject.getObject(obj);
	}

	private boolean getSuccess(Player player) {
		final int chance = locked.getLevel()
				+ (player.getSkills().getLevel(Skills.THIEVING) / 5);
		return Utils.random(chance) > locked.getLevel();
	}

	@Override
	public boolean process(final Player player) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getSuccess(player)) {
					player.getPackets().sendGameMessage(
							"You successfully pick the lock.");
					player.getSkills().addXp(Skills.THIEVING, locked.getXp());
					if (locked.getOpenId() != -1) {
						final WorldObject openedObject = new WorldObject(object
								.getId(), object.getType(), object
								.getRotation() - 1, object.getX(), object
								.getY(), object.getPlane());
						if (World.removeTemporaryObject(object, 1200, false))
							World.spawnObjectTemporary(openedObject, 1200,
									false, false);
					} else {
						player.lock(2);
						player.stopAll();
						player.setRun(false);
						ObjectHandler.handleDoor(player, object, 1000);
						if (player.getX() == object.getX()) {
							if (player.getY() < object.getY())
								player.addWalkSteps(object.getX(),
										player.getY() + 1, -1, false);
							else
								player.addWalkSteps(object.getX(),
										player.getY() - 1, -1, false);
						} else {
							if (player.getX() < object.getX())
								player.addWalkSteps(player.getX() + 1,
										object.getY(), -1, false);
							else
								player.addWalkSteps(player.getX() - 1,
										object.getY(), 1, false);
						}
					}
				} else {
					if (Utils.random(5) != 1) {
						player.getPackets().sendGameMessage(
								"You attempt to pick the lock...");
						player.setNextAnimation(new Animation(locked
								.getAnimation()[0]));
						player.lock(3);
					} else {
						player.getPackets()
								.sendGameMessage(
										"You fail to pick the lock and trigger a trap!");
						player.setNextGraphics(new Graphics(locked
								.getAnimation()[1]));
						player.applyHit(new Hit(player, Utils.random(locked
								.getDamage()), HitLook.REGULAR_DAMAGE));
						player.lock(5);
						player.stopAll();
					}
				}
			}
		}, 3);
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean start(Player player) {
		if (player.getSkills().getLevel(Skills.THIEVING) < locked.getLevel()) {
			player.getPackets().sendGameMessage(
					"You must have a thieving level of at least "
							+ locked.getLevel() + " to pick this lock.");
			return false;
		}
		if (locked.requiresPick()
				&& !player.getInventory().containsItem(1523, 1)) {
			player.getPackets().sendGameMessage(
					"You must have a lock pick in order to pick this lock.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {
		// TODO Auto-generated method stub

	}

}
