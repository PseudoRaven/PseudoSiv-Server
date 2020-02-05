package net.nocturne.game.player.actions.skills.agility;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class BrimhavenAgilityArena {

	public static List<Player> players;
	public static WorldTile dispenser;
	public static Long time;

	public static void process() {
		boolean pickNewDispenser = false;
		if (pickNewDispenser) {
			newDispenser();
		}
	}

	public static void showInterface(Player player) {
		if (time == null) {
			players = new ArrayList<Player>();
			newDispenser();
			return;
		}
		player.getHintIconsManager().addHintIcon(dispenser, 130, 3, 1, -1,
				false);
	}

	public static void removeInterface(Player player) {
		player.getHintIconsManager().removeUnsavedHintIcon();
	}

	public static void newDispenser() {
		dispenser = new WorldTile(2761 + Utils.random(4) * 11,
				9546 + Utils.random(4) * 11, 3);
		for (Player player : players) {
			player.getHintIconsManager().removeUnsavedHintIcon();
			player.getHintIconsManager().addHintIcon(dispenser, 130, 3, 1, -1,
					false);
			player.getTemporaryAttributtes().remove("tagged");
		}
	}

	public final static int CLIMBING_ROPE = 3610, ROPE_BALANCE = 3551,
			ROPE_SWING = 3566, PLANK = 3572, HAND_HOLDS = 3583, LOG = 3553,
			LOW_WALL = 3565, BALANCING_LEDGE = 3561, MONKEY_BARS = 3564,
			PILLAR = 3578, TICKET_DISPENSER = 3608, LEDGE = 3559,
			LEDGE2 = 3561;

	public final static int DODGE_DARTS = 1110, HURT_KNEE = 1113,
			HURT_HEAD = 1125, JUMP = 1115, JUMP_2 = 1133,
			HAND_HOLDS_START = 1117, HAND_HOLD = 1118, HAND_HOLDS_FAIL = 1119,
			HAND_HOLDS_END = 1120, // + 4 for other direction
			SWING = 1130, FALL_INTO_NOTHING = 1105, CLIMB_OVER_WALL = 1106;

	public final static int TIPTOE = 155, WALK_ON_WALL = 156,
			WALK_ON_WALL2 = 157;

	public static boolean handleObject(Player player, WorldObject object) {
		switch (object.getId()) {
		case CLIMBING_ROPE:
			climbRope(player, object);
			return true;
		case ROPE_BALANCE:
			walkRope(player, object);
			return true;
		case ROPE_SWING:
			ropeSwing(player, object);
			return true;
		case PLANK:
			walkPlank(player, object);
			return true;
		case HAND_HOLDS:
			handHolds(player, object);
			return true;
		case LOG:
			walkLog(player, object);
			return true;
		case LOW_WALL:
			climbWall(player, object);
			return true;
		case BALANCING_LEDGE:
			walkLedge(player, object);
			return true;
		case MONKEY_BARS:
			walkBars(player, object);
			return true;
		case PILLAR:
			jumpPillar(player, object);
			return true;
		case TICKET_DISPENSER:
			tagDispenser(player, object);
			return true;
		}
		return false;
	}

	private static void tagDispenser(Player player, WorldObject object) {
		if (object.getX() == dispenser.getX()
				&& object.getY() == dispenser.getY()) {
			if (player.getTemporaryAttributtes().get("tagged") == null) {
				player.getTemporaryAttributtes().put("tagged", true);
				player.getInventory().addItem(5555, 1);
				player.getPackets().sendGameMessage(
						"You tagged the dispenser! Here's a ticket.");
			}
		}
	}

	public static void jumpPillar(final Player player, final WorldObject object) {
		player.lock(8);
		// player.setNextAnimation(new Animation(9908));
		final boolean south = player.getY() == object.getY() + 1;
		final boolean north = player.getY() == object.getY() - 1;
		final boolean east = player.getX() == object.getX() - 1;
		final boolean west = player.getX() == object.getX() + 1;
		WorldTasksManager.schedule(new WorldTask() {
			WorldTile toTile = object;
			int tick;

			@Override
			public void run() {
				if (/* player.matches(lastTileFinal) || */tick++ == 7) {
					player.getSkills().addXp(Skills.AGILITY, 18);
					stop();
					return;
				}
				player.setNextAnimation(new Animation(741));
				if (south) {
					if (tick > 1)
						toTile = toTile.transform(0, -1, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							ForceMovement.SOUTH));
				} else if (north) {
					if (tick > 1)
						toTile = toTile.transform(0, 1, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							ForceMovement.NORTH));
				} else if (east) {
					if (tick > 1)
						toTile = toTile.transform(1, 0, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							ForceMovement.EAST));
				} else if (west) {
					if (tick > 1)
						toTile = toTile.transform(-1, 0, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							ForceMovement.WEST));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.setNextWorldTile(toTile);
					}
				}, 0);
			}
		}, 2, 1);
	}

	public static void walkBars(final Player player, WorldObject object) {
		final boolean running = player.getRun();
		player.setRunHidden(false);
		WorldTile startTile = object.transform(1, 0, 0);
		WorldTile toTile = null;
		switch (object.getRotation()) {
		case 0:// south
			startTile = object.transform(1, 0, 0);
			toTile = startTile.transform(0, -7, 0);
			break;
		case 1:// west
			startTile = object.transform(0, 1, 0);
			toTile = startTile.transform(-7, 0, 0);
			break;
		case 2:// north
			startTile = object.transform(1, 0, 0);
			toTile = startTile.transform(0, 7, 0);
			break;
		case 3:// east
			startTile = object.transform(0, 1, 0);
			toTile = startTile.transform(7, 0, 0);
			break;
		}
		player.lock(8);
		// player.addWalkSteps(object.getX(), object.getX(), -1, false);
		player.addWalkSteps(toTile.getX(), toTile.getY(), -1, false);
		player.getPackets().sendGameMessage(
				"You jump across the monkey bars...", true);
		player.getAppearence().setRenderEmote(662);
		WorldTasksManager.schedule(new WorldTask() {
			// boolean secondloop;
			@Override
			public void run() {
				// if (!secondloop) {
				// secondloop = true;
				player.getAppearence().setRenderEmote(662);// 744
				// } else {
				player.getAppearence().setRenderEmote(-1);
				player.setRunHidden(running);
				player.getSkills().addXp(Skills.AGILITY, 14);
				player.getPackets().sendGameMessage(
						"... and make it safely to the other side.", true);
				stop();
				// }
			}
		}, 9);
	}

	public static void walkLedge(final Player player, final WorldObject object) {
		final boolean running = player.getRun();
		player.setRunHidden(false);
		WorldTile toTile = null;
		switch (object.getId()) {
		case 3559:
			player.getAppearence().setRenderEmote(156);
			toTile = object.transform(6, 0, 0);
			// east
			break;
		case 3561:
			player.getAppearence().setRenderEmote(157);
			toTile = object.transform(-6, 0, 0);
			// west
			break;
		}
		player.lock(7);
		// player.addWalkSteps(object.getX(), object.getX(), -1, false);
		player.addWalkSteps(toTile.getX(), toTile.getY(), -1, false);
		player.getPackets().sendGameMessage("You walk across the ledge...",
				true);
		WorldTasksManager.schedule(new WorldTask() {
			// boolean secondloop;
			@Override
			public void run() {
				// if (!secondloop) {
				// secondloop = true;
				// player.getAppearence().setRenderEmote(157);//744
				// } else {
				player.getAppearence().setRenderEmote(-1);
				player.setRunHidden(running);
				player.getSkills().addXp(Skills.AGILITY, 16);
				player.getPackets().sendGameMessage(
						"... and make it safely to the other side.", true);
				stop();
				// }
			}
		}, 7);
	}

	public static void climbWall(final Player player, WorldObject object) {
		boolean south = player.getY() > object.getY();
		boolean north = player.getY() < object.getY();
		boolean east = player.getX() < object.getX();
		boolean west = player.getX() > object.getX();
		WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		if (south) {
			toTile = toTile.transform(0, -1, 0);
		} else if (north) {
			toTile = toTile.transform(0, 2, 0);
		} else if (east) {
			toTile = toTile.transform(2, 0, 0);
		} else if (west) {
			toTile = toTile.transform(-1, 0, 0);
		}
		player.lock(3);
		player.setNextAnimation(new Animation(1106));
		if (south) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					-1, 0), 3, ForceMovement.SOUTH));
			toTile = object.transform(0, -1, 0);
		} else if (north) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					2, 0), 3, ForceMovement.NORTH));
			toTile = object.transform(0, 2, 0);
		} else if (east) {
			player.setNextForceMovement(new ForceMovement(object.transform(2,
					0, 0), 3, ForceMovement.EAST));
			toTile = object.transform(2, 0, 0);
		} else if (west) {
			player.setNextForceMovement(new ForceMovement(object.transform(-1,
					0, 0), 3, ForceMovement.WEST));
			toTile = object.transform(-1, 0, 0);
		}
		final WorldTile tile = toTile;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(tile);
				player.getSkills().addXp(Skills.AGILITY, 8);
			}
		}, 2);
	}

	public static void walkLog(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage(
				"You walk carefully across the log...", true);
		player.lock(6);
		player.setNextAnimation(new Animation(9908));
		WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		boolean south = player.getY() == object.getY() + 1;
		boolean north = player.getY() == object.getY() - 1;
		boolean east = player.getX() == object.getX() - 1;
		boolean west = player.getX() == object.getX() + 1;
		if (south) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					-6, 0), 8, ForceMovement.SOUTH));
			toTile = object.transform(0, -6, 0);
		} else if (north) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					6, 0), 8, ForceMovement.NORTH));
			toTile = object.transform(0, 6, 0);
		} else if (east) {
			player.setNextForceMovement(new ForceMovement(object.transform(6,
					0, 0), 8, ForceMovement.EAST));
			toTile = object.transform(6, 0, 0);
		} else if (west) {
			player.setNextForceMovement(new ForceMovement(object.transform(-6,
					0, 0), 8, ForceMovement.WEST));
			toTile = object.transform(-6, 0, 0);
		}
		final WorldTile toTile2 = toTile;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile2);
				player.setNextAnimation(new Animation(-1));
				player.getSkills().addXp(Skills.AGILITY, 12);
				player.getPackets().sendGameMessage(
						"... and make it safely to the other side.", true);
			}
		}, 7);
	}

	public static void handHolds(final Player player, final WorldObject object) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
			player.getPackets().sendGameMessage(
					"You need an agility level of 20 to use hand holds.");
			return;
		}
		player.lock(8);
		// player.setNextAnimation(new Animation(9908));
		boolean s = object.getRotation() == 2;
		boolean n = object.getRotation() == 0;
		boolean e = object.getRotation() == 1;
		boolean w = object.getRotation() == 3;
		if (object.matches(new WorldTile(2792, 9592, 3))) {
			w = true;
			e = false;
		}
		if (object.matches(new WorldTile(2785, 9544, 3))) {
			w = false;
			e = true;
		}
		if (object.matches(new WorldTile(2759, 9566, 3))) {
			n = false;
			s = true;
		}
		final boolean south = s;
		final boolean north = n;
		final boolean east = e;
		final boolean west = w;
		WorldTasksManager.schedule(new WorldTask() {
			WorldTile toTile = object;
			int tick;

			@Override
			public void run() {
				if (/* player.matches(lastTileFinal) || */tick++ == 8) {
					player.getSkills().addXp(Skills.AGILITY, 22);
					player.setNextAnimation(new Animation(-1));
					stop();
					return;
				}
				switch (tick) {
				case 1:
					player.setNextAnimation(new Animation(1121));
					break;
				case 8:
					player.setNextAnimation(new Animation(1124));
					break;
				default:
					player.setNextAnimation(new Animation(1122));
					break;
				}
				int dir = 0;
				if (object.getY() == 9544) {
					dir = ForceMovement.SOUTH;
				} else if (object.getX() == 2759) {
					dir = ForceMovement.WEST;
				} else {
					dir = ForceMovement.NORTH;
				}
				if (south) {
					if (tick > 1)
						toTile = toTile.transform(0, -1, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							dir));
				} else if (north) {
					if (tick > 1)
						toTile = toTile.transform(0, 1, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							dir));
				} else if (east) {
					if (tick > 1)
						toTile = toTile.transform(1, 0, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							dir));
				} else if (west) {
					if (tick > 1)
						toTile = toTile.transform(-1, 0, 0);
					player.setNextForceMovement(new ForceMovement(toTile, 1,
							dir));
				}
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.setNextWorldTile(toTile);
					}
				}, 0);
			}
		}, 1, 1);
	}

	public static void walkPlank(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage(
				"You walk carefully across the plank...", true);
		player.lock(6);
		player.setNextAnimation(new Animation(9908));
		WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		boolean south = player.getY() == object.getY() + 1;
		boolean north = player.getY() == object.getY() - 1;
		boolean east = player.getX() == object.getX() - 1;
		boolean west = player.getX() == object.getX() + 1;
		if (south) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					-6, 0), 8, ForceMovement.SOUTH));
			toTile = object.transform(0, -6, 0);
		} else if (north) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					6, 0), 8, ForceMovement.NORTH));
			toTile = object.transform(0, 6, 0);
		} else if (east) {
			player.setNextForceMovement(new ForceMovement(object.transform(6,
					0, 0), 8, ForceMovement.EAST));
			toTile = object.transform(6, 0, 0);
		} else if (west) {
			player.setNextForceMovement(new ForceMovement(object.transform(-6,
					0, 0), 8, ForceMovement.WEST));
			toTile = object.transform(-6, 0, 0);
		}
		final WorldTile toTile2 = toTile;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile2);
				player.setNextAnimation(new Animation(-1));
				player.getSkills().addXp(Skills.AGILITY, 6);
				player.getPackets().sendGameMessage(
						"... and make it safely to the other side.", true);
			}
		}, 7);
	}

	public static void ropeSwing(final Player player, final WorldObject object) {
		boolean south = player.getY() > object.getY();
		boolean north = player.getY() < object.getY();
		boolean east = player.getX() < object.getX();
		boolean west = player.getX() > object.getX();
		WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		if (south) {
			toTile = toTile.transform(0, -3, 0);
		} else if (north) {
			toTile = toTile.transform(0, 3, 0);
		} else if (east) {
			toTile = toTile.transform(3, 0, 0);
		} else if (west) {
			toTile = toTile.transform(-3, 0, 0);
		}
		player.lock(3);
		player.setNextAnimation(new Animation(751));
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3,
				south ? ForceMovement.SOUTH : north ? ForceMovement.NORTH
						: east ? ForceMovement.EAST : ForceMovement.WEST));
		final WorldTile toTile2 = toTile;
		World.sendObjectAnimation(player, object, new Animation(497));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile2);
				player.setNextAnimation(new Animation(-1));
				player.getSkills().addXp(Skills.AGILITY, 24);
			}
		}, 3);
	}

	public static void walkRope(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage(
				"You walk carefully across the rope...", true);
		player.lock(6);
		player.setNextAnimation(new Animation(9908));
		WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		boolean south = object.getRotation() == 2;
		boolean north = object.getRotation() == 0;
		boolean east = object.getRotation() == 1;
		boolean west = object.getRotation() == 3;
		if (south) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					-6, 0), 8, ForceMovement.SOUTH));
			toTile = object.transform(0, -7, 0);
		} else if (north) {
			player.setNextForceMovement(new ForceMovement(object.transform(0,
					6, 0), 8, ForceMovement.NORTH));
			toTile = object.transform(0, 7, 0);
		} else if (east) {
			player.setNextForceMovement(new ForceMovement(object.transform(6,
					0, 0), 8, ForceMovement.EAST));
			toTile = object.transform(7, 0, 0);
		} else if (west) {
			player.setNextForceMovement(new ForceMovement(object.transform(-6,
					0, 0), 8, ForceMovement.WEST));
			toTile = object.transform(-7, 0, 0);
		}
		final WorldTile toTile2 = toTile;
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile2);
				player.setNextAnimation(new Animation(-1));
				player.getSkills().addXp(Skills.AGILITY, 10);
				player.getPackets().sendGameMessage(
						"... and make it safely to the other side.", true);
			}
		}, 7);
	}

	public static void spikeTrap(final Player player, final WorldObject object,
			int dir) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
			player.getPackets().sendGameMessage(
					"You need an agility level of 20 to cross pressure traps.");
			getHurt(player, dir, false);
			return;
		}
		player.getPackets().sendObjectAnimation(object, new Animation(1111));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendObjectAnimation(object,
						new Animation(-1));
			}
		}, 3);
		boolean pass = Utils.random(60) + 40 < player.getSkills().getLevel(
				Skills.AGILITY) + 40;
		if (pass) {
			player.lock(2);
			player.setNextAnimation(new Animation(JUMP));
			final WorldTile toTile = player.transform(
					Utils.DIRECTION_DELTA_X[dir] * 2,
					Utils.DIRECTION_DELTA_Y[dir] * 2, 0);
			int fmDir = dir == 1 ? ForceMovement.NORTH
					: dir == 6 ? ForceMovement.SOUTH
							: dir == 3 ? ForceMovement.WEST
									: ForceMovement.EAST;
			player.setNextForceMovement(new ForceMovement(toTile, 2, fmDir));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getSkills().addXp(Skills.AGILITY, 24);
					player.setNextWorldTile(toTile);
				}
			}, 2);
		} else {
			getHurt(player, dir, false);
		}
	}

	public static void pressureTrap(final Player player,
			final WorldObject object, int dir) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 20) {
			player.getPackets().sendGameMessage(
					"You need an agility level of 20 to cross pressure traps.");
			getHurt(player, dir, true);
			return;
		}
		boolean pass = Utils.random(60) + 40 < player.getSkills().getLevel(
				Skills.AGILITY) + 40;
		if (pass) {
			player.lock(2);
			player.setNextAnimation(new Animation(JUMP));
			final WorldTile toTile = player.transform(
					Utils.DIRECTION_DELTA_X[dir] * 2,
					Utils.DIRECTION_DELTA_Y[dir] * 2, 0);
			int fmDir = dir == 1 ? ForceMovement.NORTH
					: dir == 6 ? ForceMovement.SOUTH
							: dir == 3 ? ForceMovement.WEST
									: ForceMovement.EAST;
			player.setNextForceMovement(new ForceMovement(toTile, 2, fmDir));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getSkills().addXp(Skills.AGILITY, 24);
					player.setNextWorldTile(toTile);
				}
			}, 2);
		} else {
			getHurt(player, dir, true);
		}
	}

	public static void spinningBlades(final Player player,
			final WorldObject object, int dir) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
			player.getPackets()
					.sendGameMessage(
							"You need an agility level of 40 to cross spinning blades.");
			getHurt(player, dir, false);
			return;
		}
		player.getPackets().sendObjectAnimation(object, new Animation(1107));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendObjectAnimation(object,
						new Animation(-1));
			}
		}, 2);
		boolean pass = Utils.random(80) + 50 < player.getSkills().getLevel(
				Skills.AGILITY) + 50;
		if (pass) {
			player.lock(2);
			player.setNextAnimation(new Animation(JUMP));
			final WorldTile toTile = player.transform(
					Utils.DIRECTION_DELTA_X[dir] * 2,
					Utils.DIRECTION_DELTA_Y[dir] * 2, 0);
			int fmDir = dir == 1 ? ForceMovement.NORTH
					: dir == 6 ? ForceMovement.SOUTH
							: dir == 3 ? ForceMovement.WEST
									: ForceMovement.EAST;
			player.setNextForceMovement(new ForceMovement(toTile, 2, fmDir));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getSkills().addXp(Skills.AGILITY, 28);
					player.setNextWorldTile(toTile);
				}
			}, 2);
		} else {
			getHurt(player, dir, false);
		}
	}

	public static void dartTrap(final Player player, WorldObject object, int dir) {
		if (player.getSkills().getLevel(Skills.AGILITY) < 40) {
			player.getPackets().sendGameMessage(
					"You need an agility level of 40 to cross dart traps.");
			getHurt(player, dir, false);
			return;
		}
		boolean pass = Utils.random(80) + 50 < player.getSkills().getLevel(
				Skills.AGILITY) + 50;
		if (pass) {
			player.lock(2);
			// player.setNextAnimation(new Animation(MATRIX));
			final WorldTile toTile = player.transform(
					Utils.DIRECTION_DELTA_X[dir] * 2,
					Utils.DIRECTION_DELTA_Y[dir] * 2, 0);
			int fmDir = dir == 1 ? ForceMovement.NORTH
					: dir == 6 ? ForceMovement.SOUTH
							: dir == 3 ? ForceMovement.WEST
									: ForceMovement.EAST;
			player.setNextForceMovement(new ForceMovement(toTile, 2, fmDir));
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getSkills().addXp(Skills.AGILITY, 30);
					player.setNextWorldTile(toTile);
				}
			}, 2);
		} else {
			getHurt(player, dir, false);
		}
		// 30 xp
	}

	public static void getHurt(final Player target, final WorldObject object) {
		target.stopAll(true);
		target.applyHit(new Hit(target, 15 + Utils.getHalfRandom(target
				.getHitpoints() / 10), HitLook.REGULAR_DAMAGE));
		target.lock(2);
		switch (object.getId()) {
		case 3568:
			target.setNextAnimation(new Animation(HURT_KNEE));
			switch (object.getRotation()) {
			case 3:// west
				target.setNextForceMovement(new ForceMovement(object.transform(
						-1, 0, 0), 1, ForceMovement.EAST));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextWorldTile(object.transform(-1, 0, 0));
						target.setNextForceTalk(new ForceTalk("Ouch!"));
					}
				}, 1);
				break;
			case 2:// south
				target.setNextForceMovement(new ForceMovement(object.transform(
						0, -1, 0), 1, ForceMovement.NORTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextWorldTile(object.transform(0, -1, 0));
						target.setNextForceTalk(new ForceTalk("Ouch!"));
					}
				}, 1);
				break;
			}
			break;
		case 3569:
			target.setNextAnimation(new Animation(HURT_KNEE));
			switch (object.getRotation()) {
			case 3:// east
				target.setNextForceMovement(new ForceMovement(object.transform(
						1, 0, 0), 1, ForceMovement.WEST));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextWorldTile(object.transform(1, 0, 0));
						target.setNextForceTalk(new ForceTalk("Ouch!"));
					}
				}, 1);
				break;
			case 2:// north
				target.setNextForceMovement(new ForceMovement(object.transform(
						0, 1, 0), 1, ForceMovement.SOUTH));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextWorldTile(object.transform(0, 1, 0));
						target.setNextForceTalk(new ForceTalk("Ouch!"));
					}
				}, 1);
				break;
			}
			break;
		}
	}

	public static void getHurt(final Player target, int dir, boolean rocks) {
		target.stopAll(true);
		target.applyHit(new Hit(target, 15 + Utils.getHalfRandom(target
				.getHitpoints() / 10), HitLook.REGULAR_DAMAGE));
		target.lock(2);
		int oppositeDir = 7 - dir;
		final WorldTile toTile = new WorldTile(target.getX()
				+ Utils.DIRECTION_DELTA_X[oppositeDir], target.getY()
				+ Utils.DIRECTION_DELTA_Y[oppositeDir], target.getPlane());
		target.setNextAnimation(new Animation(rocks ? HURT_HEAD : HURT_KNEE));
		int fmDir = dir == 1 ? ForceMovement.NORTH
				: dir == 6 ? ForceMovement.SOUTH
						: dir == 3 ? ForceMovement.WEST : ForceMovement.EAST;
		target.setNextForceMovement(new ForceMovement(toTile, 1, fmDir));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				target.setNextWorldTile(toTile);
				target.setNextForceTalk(new ForceTalk("Ouch!"));
			}
		}, 1);
	}

	public static void climbRope(final Player player, WorldObject object) {
		player.setNextFaceWorldTile(object);
		player.setNextAnimation(new Animation(828));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(player.transform(0, 0, 3));
			}
		}, 1);
	}

	public static void swingOnRopeSwing(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 52))
			return;
		else if (player.getY() != 3953) {
			player.addWalkSteps(player.getX(), 3953);
			player.getPackets().sendGameMessage(
					"You'll need to get closer to make this jump.");
			return;
		}
		player.lock(4);
		player.setNextAnimation(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final WorldTile toTile = new WorldTile(object.getX(), 3958,
				object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3,
				ForceMovement.NORTH));
		player.getSkills().addXp(Skills.AGILITY, 20);
		player.getPackets().sendGameMessage("You skillfully swing across.",
				true);
	}
}
