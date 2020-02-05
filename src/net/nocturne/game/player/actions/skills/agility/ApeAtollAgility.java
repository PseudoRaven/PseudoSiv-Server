package net.nocturne.game.player.actions.skills.agility;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.agility.Agility.AgilityCourses;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 */

public class ApeAtollAgility {

	public static boolean handleObject(Player player, WorldObject object) {
		int id = object.getId();
		switch (id) {
		case 12568:
			jumpSteppingStone(player, object);
			return true;
		case 12570:
			climbUpTropicalTree(player, object);
			return true;
		case 12573:
			crossMonkeyBars(player, object);
			return true;
		case 95626:
			climbUpSkullSlope(player, object);
			return true;
		case 12578:
			swingRope(player, object);
			return true;
		case 12618:
			climbDownTropicalTree(player, object);
			return true;
		case 12622:
			climbDownVine(player, object);
			return true;
		}
		return false;
	}

	public static void climbDownTropicalTree(final Player player,
			final WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}
		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}

		if (!player.withinDistance(object, 2))
			return;

		player.lock();

		final WorldTile toTile = new WorldTile(2770, 2747, 0);
		player.addWalkSteps(toTile.getX(), toTile.getY(), 10, false);
		player.addWalkSteps(2758, 2735, 3);
		player.getAppearence().setRenderEmote(740);

		player.getPackets().sendGameMessage("You climb the vine...");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage(
						"..And make it carefully to the end of it.");
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(Skills.AGILITY, 450);
				player.getSkillTasks().handleTask(AgilityCourses.APE_ATOLL, 1);
				player.getAppearence().setRenderEmote(-1);
				player.unlock();
				stop();
			}
		}, 2);
	}

	public static void climbDownVine(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}
		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}
		player.lock(3);
		final WorldTile toTile = new WorldTile(player.getX(), player.getY(), 0);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("You climb down the vine.");
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(Skills.AGILITY, 36);
				stop();
			}
		}, 1);
	}

	public static void climbUpSkullSlope(final Player player,
			final WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}
		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}
		if (player.getX() < 2747)
			return;
		player.lock(4);
		final WorldTile toTile = new WorldTile(2743, 2741, 0);
		player.addWalkSteps(toTile.getX(), toTile.getY(), 10, false);
		player.getAppearence().setRenderEmote(739);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage(
						"You climb up the skull slope.");
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(Skills.AGILITY, 45);
				player.getAppearence().setRenderEmote(-1);
				stop();
			}
		}, 2);
	}

	public static void climbUpTropicalTree(final Player player,
			WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}

		if (!player.withinDistance(object, 2))
			return;

		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}
		player.lock(3);
		final WorldTile toTile = new WorldTile(2752, 2742, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("You climb up the tree...");
				player.setNextAnimation(new Animation(1382));
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(Skills.AGILITY, 25);
				stop();
			}
		}, 1);
	}

	public static void crossMonkeyBars(final Player player,
			final WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}

		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}

		if (!player.withinDistance(object, 2))
			return;

		player.lock(4);
		final WorldTile toTile = new WorldTile(2747, 2741, 0);
		final WorldTile toTile2 = new WorldTile(2747, 2741, 2);
		player.addWalkSteps(toTile2.getX(), toTile2.getY(), 10, false);
		player.getAppearence().setRenderEmote(744);
		player.getPackets().sendGameMessage("You jump to the monkey bars...");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage(
						"..And made it carefully to the other side.");
				player.getAppearence().setRenderEmote(-1);
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(Skills.AGILITY, 35);
				stop();
			}
		}, 3);
	}

	public static void jumpSteppingStone(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}

		if (!player.withinDistance(object, 2))
			return;

		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to transform into a ninja monkey to use the ape atoll courses.");
			return;
		}

		player.lock(3);

		final WorldTile toTile = new WorldTile(object.getX(), object.getY(),
				object.getPlane());
		final WorldTile toTile2 = new WorldTile(player.getX() == 2755 ? 2753
				: 2755, 2742, object.getPlane());
		final WorldTile waterTile = new WorldTile(2756, 2746, object.getPlane());
		final WorldTile Land = new WorldTile(2757, 2746, object.getPlane());

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage(
						"You jump to the stepping stone...");
				player.setNextAnimation(new Animation(1381));
				player.setNextWorldTile(toTile);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (Utils.random(5) == 0) {
							player.setNextAnimation(new Animation(1381));
							player.getPackets().sendGameMessage(
									"..And accidently fall to the water.");
							player.applyHit(new Hit(player, Utils.random(200),
									HitLook.REGULAR_DAMAGE));
							player.addWalkSteps(waterTile.getX(),
									waterTile.getY(), 10, false);
							player.getAppearence().setRenderEmote(741);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									player.getAppearence().setRenderEmote(-1);
									player.setNextWorldTile(Land);
									stop();
								}
							}, 1);
							stop();
						} else {
							player.setNextAnimation(new Animation(1381));
							player.setNextWorldTile(toTile2);
							player.getSkills().addXp(Skills.AGILITY, 15);
							player.getPackets()
									.sendGameMessage(
											"..And made it carefully to the other side.");
							stop();
						}
					}
				}, 1);
				stop();
			}
		}, 1);
	}

	public static void swingRope(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 48)) {
			player.getPackets()
					.sendGameMessage(
							"You must have an agility level of atleast 48 to perform this action.");
			return;
		}

		if (player.getX() == 2756)
			return;

		if (player.getEquipment().getWeaponId() != 4024) {
			player.getPackets()
					.sendGameMessage(
							"You need to be a ninja monkey to be able to do this agility.");
			return;
		}

		if (!player.withinDistance(object, 2))
			return;

		player.lock(4);
		player.setNextAnimation(new Animation(1388));
		World.sendObjectAnimation(player, object, new Animation(497));
		final WorldTile toTile = new WorldTile(2756, 2731, object.getPlane());
		player.addWalkSteps(toTile.getX(), toTile.getY(), 10, false);
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.getPackets().sendGameMessage("You skillfully swing across.",
				true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				stop();
			}
		}, 1);
	}
}