package net.nocturne.game.player.content;

import java.util.List;

import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Region;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.OwnedObjectManager;
import net.nocturne.game.player.OwnedObjectManager.ProcessEvent;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.content.activities.minigames.FightCaves;
import net.nocturne.game.player.content.activities.minigames.FightKiln;
import net.nocturne.game.player.content.activities.minigames.riseofthesix.RiseOfTheSix;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.player.controllers.VoragoController;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.player.controllers.bossInstance.BossInstanceController;
import net.nocturne.game.player.controllers.bossInstance.CorporealBeastInstanceController;
import net.nocturne.game.player.controllers.bossInstance.DagannothKingsInstanceController;
import net.nocturne.game.player.controllers.bossInstance.EvilChickenInstanceController;
import net.nocturne.game.player.controllers.bossInstance.KalphiteKingInstanceController;
import net.nocturne.game.player.controllers.bossInstance.KalphiteQueenInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars.CommanderZilyanaInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars.GeneralGraadorInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars.KreeArraInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars.KrilTsutsarothInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GorvekAndVindictaInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.GregorovicInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.HelwyrInstanceController;
import net.nocturne.game.player.controllers.bossInstance.godwars2.TwinFuriesInstanceController;
import net.nocturne.utils.Utils;

public class DwarfMultiCannon {

	public enum CannonType {
		NORMAL, GOLD, ROYALE
	}

	private static final int[] CANNON_PIECES = { 6, 8, 10, 12 };

	private static final int[] GOLD_CANNON_PIECES = { 20494, 20495, 20496,
			20497 };

	private static final int[] ROYALE_CANNON_PIECES = { 20498, 20499, 20500,
			20501 };

	private static final int[] CANNON_OBJECTS = { 7, 8, 9, 6 };
	private static final int[] GOLD_CANNON_OBJECTS = { 29398, 29401, 29402,
			29406 };
	private static final int[] ROYALE_CANNON_OBJECTS = { 29403, 29404, 29405,
			29408 };

	private static final int[] CANNON_EMOTES = { 303, 305, 307, 289, 184, 182,
			178, 291 };

	public static int getCannonAmount(Player player) {
		if (player.cannonType == CannonType.ROYALE)
			return 90;
		else if (player.cannonType == CannonType.GOLD)
			return 60;
		return 30;
	}

	public static void fire(Player player) {
		if (player.getCannonBalls() < getCannonAmount(player)) {
			int amount = player.getInventory().getAmountOf(2);
			if (amount == 0)
				player.getPackets()
						.sendGameMessage(
								"You need to load your cannon with cannon balls before firing it!");
			else {
				int add = getCannonAmount(player) - player.getCannonBalls();
				if (amount > add)
					amount = add;
				player.addCannonBalls(amount);
				player.getInventory().deleteItem(2, amount);
				player.getPackets()
						.sendGameMessage(
								"You load the cannon with " + amount
										+ " cannon balls.");
			}
		} else
			player.getPackets().sendGameMessage("Your cannon is full.");
	}

	public static void pickupCannon(Player player, int stage, WorldObject object) {
		if (!OwnedObjectManager.isPlayerObject(player, object))
			player.getPackets().sendGameMessage("This is not your cannon.");
		else {
			player.getTemporaryAttributtes().remove("Cannon");
			int space = player.getCannonBalls() > 0 ? stage + 1 : stage;
			if (player.getInventory().getFreeSlots() < space) {
				player.getPackets().sendGameMessage(
						"You need at least " + space
								+ " inventory spots to pickup your cannon.");
				return;
			}
			if (!OwnedObjectManager.removeObject(player, object))
				return;
			player.lock(3);
			player.getPackets().sendGameMessage(
					"You pick up the cannon. It's really heavy.");
			for (int i = 0; i < stage; i++)
				if (player.cannonType == CannonType.NORMAL)
					player.getInventory().addItem(CANNON_PIECES[i], 1);
				else if (player.cannonType == CannonType.GOLD)
					player.getInventory().addItem(GOLD_CANNON_PIECES[i], 1);
				else if (player.cannonType == CannonType.ROYALE)
					player.getInventory().addItem(ROYALE_CANNON_PIECES[i], 1);

			if (player.getCannonBalls() > 0) {
				player.getInventory().addItem(2, player.getCannonBalls());
				player.removeCannonBalls();
			}

		}
	}

	private static int getAngle(int i) {
		return i * 360 / CANNON_EMOTES.length;
	}

	public static void setUp(Player player) {
		if (OwnedObjectManager.containsObjectValue(player, CANNON_OBJECTS)
				&& player.getRights() != 2) {
			player.getPackets().sendGameMessage(
					"You can only have one cannon set at same time.");
			return;
		}
		if (player.withinDistance(Settings.HOME_LOCATION, 120)) {
			player.getPackets().sendGameMessage(
					"You cannot setup your cannon here.");
			return;
		}
		if (isRestrictedCannon(player, true))
			return;
		int count = 0;

		if (player.cannonType == CannonType.NORMAL) {
			for (int item : CANNON_PIECES) {
				if (!player.getInventory().containsItem(item, 1))
					break;
				count++;
			}
		} else if (player.cannonType == CannonType.GOLD) {
			for (int item : GOLD_CANNON_PIECES) {
				if (!player.getInventory().containsItem(item, 1))
					break;

				count++;
			}
		} else if (player.cannonType == CannonType.ROYALE) {
			for (int item : ROYALE_CANNON_PIECES) {
				if (!player.getInventory().containsItem(item, 1))
					break;

				count++;
			}
		}

		WorldTile pos = player.transform(-3, -3, 0);
		if (!World.isTileFree(pos.getPlane(), pos.getX(), pos.getY(), 3)
				|| World.getStandartObject(player) != null
				|| World.getStandartObject(pos) != null) {
			player.getPackets().sendGameMessage(
					"There isn't enough space to setup here.");
			return;
		}
		WorldObject[] objects = new WorldObject[count];
		for (int i = 0; i < count; i++)
			objects[i] = getObject(i, pos, player);
		final long[] cycles = new long[count];
		for (int i = 0; i < count - 1; i++)
			cycles[i] = 1200;
		cycles[count - 1] = 1500000;
		player.resetWalkSteps();
		player.lock();
		player.setNextFaceWorldTile(pos);
		OwnedObjectManager.addOwnedObjectManager(player, objects, cycles,
				new ProcessEvent() {

					private int step;
					private int rotation;
					private boolean warned;
					private long disappearTime;

					@Override
					public void spawnObject(Player player, WorldObject object) {
						player.setNextAnimation(new Animation(827));
						if (step == 0)
							player.getPackets().sendGameMessage(
									"You place the cannon base on the ground.");
						else if (step == 1)
							player.getPackets().sendGameMessage(
									"You add the stand.");
						else if (step == 2)
							player.getPackets().sendGameMessage(
									"You add the barrels.");
						else if (step == 3) {
							player.getPackets().sendGameMessage(
									"You add the furnace.");
							disappearTime = Utils.currentTimeMillis()
									+ cycles[cycles.length - 1];
						}
						player.getTemporaryAttributtes().put("Cannon", true);
						if (player.cannonType == CannonType.NORMAL)
							player.getInventory().deleteItem(
									CANNON_PIECES[step], 1);
						else if (player.cannonType == CannonType.GOLD)
							player.getInventory().deleteItem(
									GOLD_CANNON_PIECES[step], 1);
						else if (player.cannonType == CannonType.ROYALE)
							player.getInventory().deleteItem(
									ROYALE_CANNON_PIECES[step], 1);

						if (step++ == cycles.length - 1)
							player.lock(1);
					}

					@Override
					public void process(Player player, WorldObject currentObject) {
						if (step != CANNON_PIECES.length
								|| step != GOLD_CANNON_PIECES.length
								|| step != ROYALE_CANNON_PIECES.length
								|| !player.clientHasLoadedMapRegion()
								|| player.hasFinished())
							return;
						if (!warned
								&& (disappearTime - Utils.currentTimeMillis()) < 5 * 1000 * 60) {
							player.getPackets()
									.sendGameMessage(
											"<col=480000>Your cannon is about to decay!");
							warned = true;
						}
						if (player.getCannonBalls() == 0)
							return;
						rotation++;
						if (rotation == CANNON_EMOTES.length * 2)
							rotation = 0;
						if (rotation % 2 == 0)
							return;
						World.sendObjectAnimation(player, currentObject,
								new Animation(CANNON_EMOTES[rotation / 2]));
						NPC nearestN = null;
						double lastD = Integer.MAX_VALUE;
						int angle = getAngle(rotation / 2);
						int objectSizeX = currentObject.getDefinitions().sizeX;
						int objectSizeY = currentObject.getDefinitions().sizeY;
						for (int regionId : player.getMapRegionsIds()) {
							Region region = World.getRegion(regionId);
							List<Integer> npcIndexes = region.getNPCsIndexes();
							if (npcIndexes == null)
								continue;
							for (int npcIndex : npcIndexes) {
								NPC npc = World.getNPCs().get(npcIndex);
								if (npc == null
										|| npc == player.getFamiliar()
										|| npc.isDead()
										|| npc.hasFinished()
										|| npc.getPlane() != currentObject
												.getPlane()
										|| !Utils.isOnRange(npc.getX(),
												npc.getY(), npc.getSize(),
												currentObject.getX(),
												currentObject.getY(),
												objectSizeX, 10)
										|| !npc.getDefinitions()
												.hasAttackOption()
										|| !npc.clipedProjectile(currentObject,
												false) || npc.isCantInteract())
									continue;
								if (!player.getControllerManager().canHit(npc))
									continue;
								int size = npc.getSize();
								double xOffset = (npc.getX() + size / 2)
										- (currentObject.getX() + objectSizeX / 2);
								double yOffset = (npc.getY() + size / 2)
										- (currentObject.getY() + objectSizeY / 2);

								double distance = Math.hypot(xOffset, yOffset);
								double targetAngle = Math.toDegrees(Math.atan2(
										yOffset, xOffset));
								if (targetAngle < 0)
									targetAngle += 360;
								double ratioAngle = 22.5;// Math.toDegrees(Math.atan(distance))
								// / 2;
								if (targetAngle < angle - ratioAngle
										|| targetAngle > angle + ratioAngle
										|| lastD <= distance)
									continue;
								lastD = distance;
								nearestN = npc;
							}
						}
						if (nearestN != null) {

							double hitChance = Combat.getHitChance(player,
									nearestN, player.getCombatDefinitions()
											.getStyle(true), true);

							int damage = hitChance < Utils.random(500) ? 0
									: Utils.random(3500);
							World.sendProjectile(currentObject.transform(
									objectSizeX / 2, objectSizeY / 2, 0),
									nearestN, 53, 38, 38, 30, 40, 0, 0);
							nearestN.applyHit(new Hit(player, damage,
									HitLook.CANNON_DAMAGE, 60));
							player.addCannonBalls(-1);
							boolean twoBalls = player.getCannonBalls() > 0
									&& Utils.random(4) == 0;
							player.getSkills().addXp(Skills.RANGED, damage / 5);
							if (twoBalls) {
								damage = hitChance < Utils.random(500) ? 0
										: Utils.random(3500);
								World.sendProjectile(currentObject.transform(
										objectSizeX / 2, objectSizeY / 2, 0),
										nearestN, 53, 38, 38, 30, 60, 0, 0);
								nearestN.applyHit(new Hit(player, damage,
										HitLook.CANNON_DAMAGE, 80));
								player.getSkills().addXp(Skills.RANGED,
										damage / 5);
								player.addCannonBalls(-1);
							}
							nearestN.setTarget(player);
							if (nearestN instanceof Familiar)
								player.setWildernessSkull();
						}
					}
				});
	}

	private static WorldObject getObject(int i, WorldTile tile, Player player) {
		if (player.cannonType == CannonType.NORMAL) {
			return new WorldObject(CANNON_OBJECTS[i], 10, 0, tile.getX(),
					tile.getY(), tile.getPlane());
		} else if (player.cannonType == CannonType.GOLD) {
			return new WorldObject(GOLD_CANNON_OBJECTS[i], 10, 0, tile.getX(),
					tile.getY(), tile.getPlane());
		} else if (player.cannonType == CannonType.ROYALE) {
			return new WorldObject(ROYALE_CANNON_OBJECTS[i], 10, 0,
					tile.getX(), tile.getY(), tile.getPlane());
		}
		return null;
	}

	private static boolean isAtHome(WorldTile tile) {
		return tile.withinDistance(Settings.HOME_LOCATION, 20);
	}

	private static boolean isRestrictedCannon(Player player, boolean loading) {
		int destX = player.getX();
		int destY = player.getY();
		Controller controller = player.getControllerManager().getController();
		if ((controller != null && !(controller instanceof Wilderness
				|| controller instanceof TwinFuriesInstanceController
				|| controller instanceof HelwyrInstanceController
				|| controller instanceof GregorovicInstanceController
				|| controller instanceof GorvekAndVindictaInstanceController
				|| controller instanceof KrilTsutsarothInstanceController
				|| controller instanceof KreeArraInstanceController
				|| controller instanceof GeneralGraadorInstanceController
				|| controller instanceof CommanderZilyanaInstanceController
				|| controller instanceof VoragoController
				|| controller instanceof KalphiteQueenInstanceController
				|| controller instanceof KalphiteKingInstanceController
				|| controller instanceof EvilChickenInstanceController
				|| controller instanceof DagannothKingsInstanceController
				|| controller instanceof CorporealBeastInstanceController
				|| controller instanceof BossInstanceController
				|| controller instanceof RiseOfTheSix
				|| controller instanceof FightCaves || controller instanceof FightKiln))
				|| player.isInBossRoom() || isAtHome(player)) {
			player.getPackets().sendGameMessage(
					"You can't set up a cannon here.");
			return true;
		}
		if (player.getTemporaryAttributtes().contains("Cannon")) {
			player.getPackets().sendGameMessage(
					"You already have a cannon set up.");
			return true;
		}
		if (!loading
				&& !World.canMoveNPC(player.getPlane(), player.getX(),
						player.getY(), 3)) {
			player.getPackets().sendGameMessage("There isn't enough room!");
			return true;
		}
		if (!loading && World.getObject(player.getTile()) != null) {
			player.getPackets().sendGameMessage(
					"You can't set up a cannon here.");
			return true;
		}
		return isRestrictedArea(player, destX, destY);
	}

	private static boolean isRestrictedArea(Player player, int destX, int destY) {
		if ((destX >= 2172 && destX <= 2254 && destY >= 4106 && destY <= 4293) // Lucien
				|| (destX >= 2250 && destX <= 2302 && destY >= 4673 && destY <= 4725) // King
																						// BD
				|| (destX >= 3082 && destX <= 3122 && destY >= 5512 && destY <= 5560) // Bork
				|| (destX >= 5559 && destX <= 5696 && destY >= 4866 && destY <= 4997) // Home
				|| (destX >= 3462 && destX <= 3512 && destY >= 9473 && destY <= 9525) // Kalphite
																						// Queen
				|| (destX >= 2894 && destX <= 2948 && destY >= 4423 && destY <= 4479) // Dagganoth
																						// King
				|| (destX >= 2574 && destX <= 2634 && destY >= 5692 && destY <= 5759) // Tormented
																						// Demon
				|| (destX >= 2969 && destX <= 3005 && destY >= 4357 && destY <= 4405) // Corporeal
				|| (destX >= 2898 && destX <= 2946 && destY >= 5181 && destY <= 5226) // Nex
				|| (destX >= 2822 && destX <= 2936 && destY >= 5238 && destY <= 5379) // God
																						// Wars
				|| (destX >= 2943 && destX <= 3561 && destY >= 3521 && destY <= 4052) // Wilderness
				|| (destX >= 2518 && destX <= 2543 && destY >= 5226 && destY <= 5241) // Yklagor
				|| (destX >= 2844 && destX <= 2868 && destY >= 9625 && destY <= 9650) // Sunfreet
				|| (destX >= 1340 && destX <= 1409 && destY >= 4734 && destY <= 5706)// Vorago
				|| (destX >= 1365 && destX <= 1387 && destY >= 6613 && destY <= 6636)// Blink
				|| (destX >= 997 && destX <= 12250 && destY >= 690 && destY <= 760) // Monastery
																					// of
																					// Ascension
				|| (destX >= 4164 && destX <= 4223)
				&& (destY >= 5698 && destY <= 5754) // Glacor
				|| (destX >= 1700 && destX <= 1787 && destY >= 5000 && destY <= 5300)// Mole
		) {
			player.getPackets().sendGameMessage(
					"You can't set up a cannon here.");
			return true;
		}
		return false;
	}

}
