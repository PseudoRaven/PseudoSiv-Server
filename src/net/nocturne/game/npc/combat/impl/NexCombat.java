package net.nocturne.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceMovement;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.godwars.zaros.Nex;
import net.nocturne.game.npc.godwars.zaros.Nex.NexPhase;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.ZarosGodwars;
import net.nocturne.game.player.cutscenes.NexCutScene;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class NexCombat extends CombatScript {

	/**
	 * The teleport coordinates used during nex's "no escape" attack.
	 */
	public static final WorldTile[] NO_ESCAPE_TELEPORTS = {
			new WorldTile(2924, 5213, 0), // north
			new WorldTile(2934, 5202, 0), // east,
			new WorldTile(2924, 5192, 0), // south
			new WorldTile(2913, 5202, 0), }; // west

	@Override
	public Object[] getKeys() {
		return new Object[] { "Nex" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final Nex nex = (Nex) npc;
		nex.setForceFollowClose(Utils.random(2) == 0);
		boolean hasDistance = false;
		int distanceX = target.getX() - npc.getX();
		int distanceY = target.getY() - npc.getY();
		int size = npc.getDefinitions().size;
		if (distanceX > size + 1 || distanceX < -1 || distanceY > size + 1
				|| distanceY < -1)
			hasDistance = true;
		nex.resetLastAttack();
		if (nex.isSiphioning() || nex.isFlying())
			return 0;
		switch (nex.getCurrentPhase().getPhaseValue()) {
		case 1: // Smoke
			if (nex.isFirstStageAttack()) {
				sendVirusAttack(nex);
				nex.setFirstStageAttack(false);
				return nex.getAttackSpeed();
			}
			switch (hasDistance ? Utils.random(7) : Utils.random(11)) {
			case 0:
			case 1:
			case 2:
				sendMagicAttack(nex, false);
				break;
			case 3:
				if (target.getEffectsManager().hasActiveEffect(
						EffectType.ANTICIPATION)
						|| target.getEffectsManager().hasActiveEffect(
								EffectType.FREEDOM))
					sendMagicAttack(nex, false);
				else
					sendPullAttack(nex);
				break;
			case 4:
				if (nex.getLastVirusAttack() > Utils.currentTimeMillis())
					return 0;
				sendVirusAttack(nex);
				break;
			case 5:
			case 6:
				sendMagicAttack(nex, true);
				break;
			case 7:
			case 8:
			case 9:
				sendMeleeAttack(nex, target);
				break;
			case 10:
				sendNoEscape(nex, target);
				break;
			}
			return nex.getAttackSpeed();
		case 2:// Shadow
			for (Player players : ZarosGodwars.getPlayers()) {
				if (nex.isFirstStageAttack()) {
					players.getInterfaceManager().sendOverlayInterface(599);
					nex.setFirstStageAttack(false);
					if (Utils.random(6) == 0)
						sendShadowTraps(nex);
					else
						sendEmbraceDarkness(nex);
				}
				switch (Utils.random(4)) {
				case 0:// shadow attack
					return sendShadowTraps(nex);
				case 1:
				case 2:
					sendRangeAttack(nex);
				default:
					if (Utils.random(5) == 0)
						sendEmbraceDarkness(nex);
					else
						sendRangeAttack(nex);
				}
				return nex.getAttackSpeed();
			}
		case 3:// Blood
			if (nex.getNexAttack() == 0)
				sendSipionAttack(nex, target);
			else if (nex.getNexAttack() >= 1 && nex.getNexAttack() <= 4)
				sendNormalBloodAttack(hasDistance, nex, target);
			else if (nex.getNexAttack() == 5)
				return sendBloodSacrifice(nex, target);
			else if (nex.getNexAttack() >= 6 && nex.getNexAttack() <= 10)
				sendNormalBloodAttack(hasDistance, nex, target);
			else if (nex.getNexAttack() == 11) {
				nex.resetNexAttack();
				sendSipionAttack(nex, target);
			} else
				nex.resetNexAttack();
			nex.incrementNexAttack();
			return nex.getAttackSpeed();
		case 4:// Ice
			if (nex.getNexAttack() == 0)
				sendIcePrison(nex, target, true);
			else if (nex.getNexAttack() >= 1 && nex.getNexAttack() <= 5
					|| nex.getNexAttack() >= 7 && nex.getNexAttack() <= 9)
				sendNormalIceAttacks(hasDistance, nex, target);
			else if (nex.getNexAttack() == 6)
				sendIceBarricade(nex);
			else if (nex.getNexAttack() == 10) {
				nex.resetNexAttack();
				sendIcePrison(nex, target, true);
			} else
				nex.resetNexAttack();
			nex.incrementNexAttack();
			return nex.getAttackSpeed();
		case 5:// POZ (Power of Zaros)
			switch (hasDistance ? Utils.random(3) : Utils.random(11) + 3) {
			case 0:
			case 1:
				sendMagicAttack(nex, false);
				break;
			case 2:
				sendMagicAttack(nex, true);
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				sendMeleeAttack(nex, target);
				break;
			case 13:
				sendNoEscape(nex, target);
				break;
			}
			return nex.getAttackSpeed();
		}
		return 0;
	}

	private void sendNormalIceAttacks(boolean hasDistance, final Nex nex,
			final Entity target) {
		switch (hasDistance ? 0 : Utils.random(3)) {
		case 0:
			sendIceBarrage(nex);
			break;
		case 1:
		case 2:
			sendMeleeAttack(nex, target);
			break;
		}
	}

	/**
	 * Send's nex's ice attack.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 */
	private void sendIceBarrage(Nex nex) {
		boolean usingPrayer = false;
		nex.setNextAnimation(new Animation(17414));
		nex.setNextGraphics(new Graphics(3375));
		for (final Entity possibleTarget : nex.getPossibleTargets()) {
			World.sendProjectile(nex, possibleTarget, 362, 20, 20, 20, 2, 10, 0);
			int damage = getMaxHit(nex, NPCCombatDefinitions.MAGE,
					possibleTarget);
			delayHit(nex, 1, possibleTarget, getMagicHit(nex, damage));
			if (damage > 0) {
				if (possibleTarget instanceof Player) {
					final Player player = (Player) possibleTarget;
					player.getPrayer().drainPrayer(damage / 4);
					if (player.getPrayer().isMageProtecting())
						usingPrayer = true;
				}
				final boolean finalPrayer = usingPrayer;
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						if (Utils.random(finalPrayer ? 6 : 3) == 0) {
							possibleTarget.setBoundDelay(30, true);
							possibleTarget.setNextGraphics(new Graphics(369));
						}
					}
				}, 2);
			}
		}
	}

	/**
	 * Traps a player within an ice boundary.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 * @param start
	 *            If the target is dead or a null to choose another target.
	 */
	private void sendIcePrison(final Nex nex, Entity target, boolean start) {
		if (start) {
			nex.setNextForceTalk(new ForceTalk("Die now, in a prison of ice!"));
			nex.playSoundEffect(3308);
			nex.setNextAnimation(new Animation(17414));
			nex.setNextGraphics(new Graphics(3375));
		}
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : ZarosGodwars.getPlayers()) {
			if (!player.withinDistance(nex, 14))
				continue;
			players.add(player);
		}
		if (players.size() > 0) {
			final Player player = players.get(Utils.random(players.size()));
			if (player == null || player.isDead()) {
				sendIcePrison(nex, target, false);
				return;
			}
			World.sendProjectile(nex, player, 362, 20, 20, 20, 2, 10, 0);
			final WorldTile base = player;
			final WorldTile tile = base.transform(+1, +1, player.getPlane());
			final WorldTile tile1 = base.transform(+1, -1, player.getPlane());
			final WorldTile tile2 = base.transform(-1, +1, player.getPlane());
			final WorldTile tile3 = base.transform(-1, -1, player.getPlane());
			final WorldTile tile4 = base.transform(+1, 0, player.getPlane());
			final WorldTile tile5 = base.transform(-1, 0, player.getPlane());
			final WorldTile tile6 = base.transform(0, +1, player.getPlane());
			final WorldTile tile7 = base.transform(0, -1, player.getPlane());
			final WorldObject object = new WorldObject(83134, 10, 0, tile);
			final WorldObject object1 = new WorldObject(83134, 10, 0, tile1);
			final WorldObject object2 = new WorldObject(83134, 10, 0, tile2);
			final WorldObject object3 = new WorldObject(83134, 10, 0, tile3);
			final WorldObject object4 = new WorldObject(83134, 10, 0, tile4);
			final WorldObject object5 = new WorldObject(83134, 10, 0, tile5);
			final WorldObject object6 = new WorldObject(83134, 10, 0, tile6);
			final WorldObject object7 = new WorldObject(83134, 10, 0, tile7);
			if (World.isTileFree(0, tile.getX(), tile.getY(), (object
					.getDefinitions().getSizeX() + object.getDefinitions()
					.getSizeY()) / 2))
				World.spawnObject(object);
			World.spawnObject(object1);
			World.spawnObject(object2);
			World.spawnObject(object3);
			World.spawnObject(object4);
			World.spawnObject(object5);
			World.spawnObject(object6);
			World.spawnObject(object7);
			WorldTasksManager.schedule(new WorldTask() {
				boolean remove = false;

				@Override
				public void run() {
					if (remove) {
						if (player.withinDistance(base, 1))
							player.applyHit(new Hit(nex, Utils.random(6000,
									8000), HitLook.REGULAR_DAMAGE));
						World.removeObject(object);
						World.removeObject(object1);
						World.removeObject(object2);
						World.removeObject(object3);
						World.removeObject(object4);
						World.removeObject(object5);
						World.removeObject(object6);
						World.removeObject(object7);
						stop();
						return;
					}
					remove = true;
					if (player.getX() == tile.getX()
							&& player.getY() == tile.getY()
							&& player.getX() == tile1.getX()
							&& player.getY() == tile1.getY()
							&& player.getX() == tile2.getX()
							&& player.getY() == tile2.getY()
							&& player.getX() == tile3.getX()
							&& player.getY() == tile3.getY()
							&& player.getX() == tile4.getX()
							&& player.getY() == tile4.getY()
							&& player.getX() == tile5.getX()
							&& player.getY() == tile5.getY()
							&& player.getX() == tile6.getX()
							&& player.getY() == tile6.getY()
							&& player.getX() == tile7.getX()
							&& player.getY() == tile7.getY()) {
						player.getPackets()
								.sendGameMessage(
										"The centre of the ice prison freezes you to the bone!");
						player.resetWalkSteps();
					}
				}
			}, 8, 0);

		} else {
			return;
		}
	}

	/**
	 * Send's a barricade made of ice fragments around nex.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 */

	private void sendIceBarricade(final Nex nex) {
		nex.setNextForceTalk(new ForceTalk("Contain this!"));
		nex.playSoundEffect(3316);
		nex.setNextAnimation(new Animation(17407));
		nex.setNextGraphics(new Graphics(3362));
		final WorldTile base = nex.transform(1, 1, 0);
		nex.resetWalkSteps();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final WorldTile base = nex;
				final WorldTile tile = base.transform(+1, +1, nex.getPlane());
				final WorldTile tile1 = base.transform(-1, +1, nex.getPlane());
				final WorldTile tile2 = base.transform(+1, -1, nex.getPlane());
				final WorldTile tile3 = base.transform(-1, -1, nex.getPlane());
				final WorldTile tile4 = base.transform(+1, 0, nex.getPlane());
				final WorldTile tile5 = base.transform(-1, 0, nex.getPlane());
				final WorldTile tile6 = base.transform(0, +1, nex.getPlane());
				final WorldTile tile7 = base.transform(0, -1, nex.getPlane());

				final WorldObject object = new WorldObject(57262, 10, 0, tile);
				final WorldObject object1 = new WorldObject(57262, 10, 0, tile1);
				final WorldObject object2 = new WorldObject(57262, 10, 0, tile2);
				final WorldObject object3 = new WorldObject(57262, 10, 0, tile3);
				if (tile != base
						&& World.isTileFree(0, tile.getX(), tile.getY(),
								(object.getDefinitions().getSizeX() + object
										.getDefinitions().getSizeY()) / 2)) {
					for (Player players : ZarosGodwars.getPlayers()) {
						if (players.getX() == tile4.getX()
								&& players.getY() == tile4.getY()
								|| players.getX() == tile5.getX()
								&& players.getY() == tile5.getY()
								|| players.getX() == tile6.getX()
								&& players.getY() == tile6.getY()
								|| players.getX() == tile7.getX()
								&& players.getY() == tile7.getY()) {
							players.applyHit(new Hit(nex, Utils.random(2000,
									3500), HitLook.REGULAR_DAMAGE));
							players.getPackets().sendGameMessage(
									"The icicle spikes you to the spot!");
							players.getPackets()
									.sendGameMessage(
											"You've been injured and cannot use "
													+ (players.getPrayer()
															.isAncientCurses() ? "deflect curses"
															: "protection prayers ")
													+ "!");
							players.resetWalkSteps();
							players.getPrayer().closeAllPrayers();
							players.getEffectsManager().startEffect(
									new Effect(EffectType.STUNNED, 10));
							players.getEffectsManager().startEffect(
									new Effect(EffectType.PROTECTION_DISABLED,
											12));
						}
					}
					World.spawnObjectTemporary(object, 7000);
					World.spawnObjectTemporary(object1, 7000);
					World.spawnObjectTemporary(object2, 7000);
					World.spawnObjectTemporary(object3, 7000);

				}
				return;
			}
		}, 2);
		/*
		 * final WorldTile base1 = nex; final WorldTile tile4 =
		 * base1.transform(0, 0, 0); final WorldTile tile5 = base1.transform(-1,
		 * 0, 0); final WorldTile tile6 = base1.transform(-2, 0, 0); final
		 * WorldTile tile7 = base1.transform(-3, 0, 0); final WorldTile tile8 =
		 * base1.transform(-4, 0, 0); final WorldTile tile9 =
		 * base1.transform(-5, 0, 0); final WorldTile tile10 =
		 * base1.transform(-6, 0, 0); final WorldObject object4 = new
		 * WorldObject(52285, 10, 0, tile4); final WorldObject object5 = new
		 * WorldObject(52285, 10, 0, tile5); final WorldObject object6 = new
		 * WorldObject(52285, 10, 0, tile6); final WorldObject object7 = new
		 * WorldObject(52285, 10, 0, tile7); final WorldObject object8 = new
		 * WorldObject(52285, 10, 0, tile8); final WorldObject object9 = new
		 * WorldObject(52285, 10, 0, tile9); final WorldObject object10 = new
		 * WorldObject(52285, 10, 0, tile10);
		 * 
		 * final Map<Integer, Integer> stalagmites = new HashMap<Integer,
		 * Integer>();
		 * 
		 * WorldTasksManager.schedule(new WorldTask() { int stage;
		 * 
		 * @Override public void run() { if (stage == 0) {
		 * World.spawnObject(object4); } else if (stage == 1) {
		 * World.removeObject(object4); World.spawnObject(object5); } else if
		 * (stage == 2) { World.removeObject(object5);
		 * World.spawnObject(object6); } else if (stage == 3) {
		 * World.removeObject(object6); World.spawnObject(object7); } else if
		 * (stage == 4) { World.removeObject(object7);
		 * World.spawnObject(object8); } else if (stage == 5) {
		 * World.removeObject(object8); World.spawnObject(object9); } else if
		 * (stage == 6) { World.removeObject(object9);
		 * World.spawnObject(object10); } else if (stage == 7) {
		 * World.removeObject(object10); }stage++; } }, 0,2);
		 */
	}

	/**
	 * Sends the 'normal' attacks during nex's blood stage.
	 * 
	 * @param hasDistance
	 *            If the {@code Nex} has distance between the target.
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 * @param target
	 *            The target being attacked.
	 */
	private void sendNormalBloodAttack(boolean hasDistance, Nex nex,
			Entity target) {
		switch (hasDistance ? 0 : Utils.random(3)) {
		case 0:
			for (Player player : ZarosGodwars.getPlayers()) {
				if (player.withinDistance(nex, 1))
					continue;
				sendBloodAttack(nex, player);
			}
			break;
		case 1:
		case 2:
			sendMeleeAttack(nex, target);
			break;
		}
	}

	/**
	 * Sends nex's blood sacrifice attack.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 * @param target
	 *            The entity chosen to be sacrificed.
	 * @return The delay of the attack.
	 */
	private int sendBloodSacrifice(final Nex nex, Entity target) {
		nex.setNextForceTalk(new ForceTalk("I demand a blood sacrifice!"));
		nex.playSoundEffect(3293);
		target.getEffectsManager().startEffect(
				new Effect(EffectType.BLOOD_SACRIFICE, 8, nex));
		nex.incrementNexAttack();
		return nex.getAttackSpeed();
	}

	/**
	 * Sends a blood barrage during the blood stage.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 * @param target
	 *            The target being attacked.
	 */
	private void sendBloodAttack(final Nex nex, final Entity target) {
		final int damage = getMaxHit(nex, NPCCombatDefinitions.MAGE, target);
		nex.setNextAnimation(new Animation(17414));
		nex.setNextGraphics(new Graphics(3375));
		Projectile projectile = World.sendProjectile(nex, target, 374, 20, 20,
				20, 2, 0, 0);
		int cycleTime = Utils.projectileTimeToCycles(projectile.getEndTime()) - 1;
		delayHit(nex, cycleTime, target, getMagicHit(nex, damage));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				target.setNextGraphics(new Graphics(376));
				nex.heal(damage / 4);
			}
		}, cycleTime);
	}

	/**
	 * Begin's nex's siphion attack during the blood stage.
	 * 
	 * @param nex
	 *            The instance of {@code Nex} being used.
	 */
	private void sendSipionAttack(final Nex nex, Entity target) {
		nex.getEffectsManager().startEffect(
				new Effect(EffectType.SIPHIONING, 8, nex));
		int size = NPCDefinitions.getNPCDefinitions(13458).size;
		int[][] dirs = Utils.getCoordOffsetsNear(size);
		int maximumAmount = Utils.random(3);
		int count = 0;
		if (maximumAmount != 0) {
			for (int dir = 0; dir < dirs[0].length; dir++) {
				final WorldTile tile = new WorldTile(new WorldTile(
						target.getX() + dirs[0][dir], target.getY()
								+ dirs[1][dir], target.getPlane()));
				if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(),
						size)) {
					NPC npc = nex.getBloodReavers()[count++] = new NPC(13458,
							tile, -1, true, true);
					npc.setNextGraphics(new Graphics(1315));
				}
				if (count == maximumAmount)
					break;
			}
		}
	}

	private void sendEmbraceDarkness(final Nex nex) {
		nex.setNextForceTalk(new ForceTalk("Embrace darkness!"));
		nex.playSoundEffect(3322);
		nex.setNextAnimation(new Animation(17412));
		nex.setNextGraphics(new Graphics(3353));

		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (nex.hasFinished()
						|| nex.getCurrentPhase() != NexPhase.SHADOW) {
					for (Entity entity : nex.getPossibleTargets()) {
						if (entity instanceof Player) {
							Player player = (Player) entity;
							player.getPackets().sendCSVarInteger(1435, 255);
						}
					}
					stop();
					return;
				}
				if (Utils.random(2) == 0) {
					for (Entity entity : nex.getPossibleTargets()) {
						if (entity instanceof Player) {
							Player player = (Player) entity;
							int distance = Utils.getDistance(player.getX(),
									player.getY(), nex.getX(), nex.getY());
							if (distance > 30)
								distance = 30;
							player.getPackets().sendCSVarInteger(1435,
									(distance * 255 / 30));
						}
					}
				}
			}
		}, 0, 0);
	}

	private void sendNoEscape(final Nex nex, final Entity target) {
		nex.setNextForceTalk(new ForceTalk("There is..."));
		nex.playSoundEffect(3294);
		nex.setCantInteract(true);
		final int index = Utils.random(NO_ESCAPE_TELEPORTS.length);
		final WorldTile dir = NO_ESCAPE_TELEPORTS[index];
		final WorldTile center = new WorldTile(2925, 5203, 0);
		WorldTasksManager.schedule(new WorldTask() {
			private int count;

			@Override
			public void run() {
				if (count == 0) {
					nex.setNextAnimation(new Animation(17411));
					nex.setNextGraphics(new Graphics(1216));
				} else if (count == 1) {
					nex.setNextWorldTile(dir);
					nex.setNextForceTalk(new ForceTalk("NO ESCAPE!"));
					nex.playSoundEffect(3292);
					nex.setNextForceMovement(new ForceMovement(dir, 1, center,
							3, index == 3 ? 1 : index == 2 ? 0 : index == 1 ? 3
									: 2));
					for (Entity entity : nex.calculatePossibleTargets(center,
							dir, index == 0 || index == 2)) {
						if (entity instanceof Player) {
							final Player player = (Player) entity;
							player.setAttackedBy(null);
							player.stopAll();
							player.getCutscenesManager().play(
									new NexCutScene(dir, index));
							Hit hit = new Hit(nex, Utils.random(4000, 7000),
									HitLook.REGULAR_DAMAGE);
							if (nex.getCurrentPhase() == NexPhase.ZAROS)
								sendSoulSplit(hit, nex, entity);
							player.applyHit(hit);
							player.setNextAnimation(new Animation(10070));
							player.setNextForceMovement(new ForceMovement(
									center, 2, index == 3 ? 3 : index == 2 ? 2
											: index == 1 ? 1 : 0));
							WorldTasksManager.schedule(new WorldTask() {

								@Override
								public void run() {
									player.setNextWorldTile(center);
								}
							}, 2);
						}
					}
				} else if (count == 3) {
					nex.setNextWorldTile(center);
				} else if (count == 4) {
					nex.setCantInteract(false);
					nex.setTarget(target);
					stop();
					return;
				}
				count++;
			}
		}, 0, 1);
	}

	protected void sendSoulSplit(Hit hit, Nex nex, Entity entity) {
		entity.sendSoulSplit(hit, nex);
	}

	private int sendShadowTraps(final Nex nex) {
		if (!nex.hasShadowTraps()) {
			nex.setHasShadowTraps(true);
			nex.setNextForceTalk(new ForceTalk("Fear the shadow!"));
			nex.playSoundEffect(3314);
			nex.setNextAnimation(new Animation(17407));
			nex.setNextGraphics(new Graphics(3362));
			final List<WorldTile> tiles = new LinkedList<WorldTile>();
			for (Entity t : nex.getPossibleTargets()) {
				WorldTile tile = new WorldTile(t);
				if (!tiles.contains(t)) {
					tiles.add(tile);
					World.spawnObjectTemporary(
							new WorldObject(57261, 10, 0, t.getX(), t.getY(), 0),
							2400);
				}
			}
			WorldTasksManager.schedule(new WorldTask() {
				private boolean firstCall;

				@Override
				public void run() {
					if (!firstCall) {
						for (WorldTile tile : tiles) {
							World.sendGraphics(null, new Graphics(383),
									new WorldTile(tile.getX(), tile.getY(),
											tile.getPlane()));
							for (Entity t : nex.getPossibleTargets())
								if (t.getX() == tile.getX()
										&& t.getY() == tile.getY()
										&& t.getPlane() == tile.getPlane())
									t.applyHit(new Hit(nex,
											Utils.random(3500) + 500,
											HitLook.REGULAR_DAMAGE));
						}
						firstCall = true;
					} else {
						nex.setHasShadowTraps(false);
						stop();
					}
				}
			}, 3, 3);
			return nex.getAttackSpeed();
		}
		return 0;
	}

	private void sendMeleeAttack(Nex nex, Entity target) {
		nex.setNextAnimation(new Animation(17453));
		Hit hit = getMeleeHit(nex,
				getMaxHit(nex, NPCCombatDefinitions.MELEE, target));
		delayHit(nex, 0, target, hit);
		if (nex.getCurrentPhase() == NexPhase.ZAROS)
			sendSoulSplit(hit, nex, target);
	}

	private void sendPullAttack(final Nex nex) {
		final Player target = (Player) nex.getFarthestTarget();
		target.lock(3);
		target.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;

			@Override
			public void run() {
				ticks++;

				if (ticks == 1) {
					target.setNextAnimation(new Animation(14388));
					target.setNextGraphics(new Graphics(2767));
					target.setNextForceMovement(new ForceMovement(nex, 2, Utils
							.getMoveDirection(
									nex.getCoordFaceX(target.getSize())
											- target.getX(),
									nex.getCoordFaceY(target.getSize())
											- target.getY())));
					nex.setTarget(target);
				} else if (ticks == 3) {
					target.setNextWorldTile(nex);
					target.getPrayer().closeProtectionPrayers();
					target.getPackets()
							.sendGameMessage(
									"You've been injured and you cannot use "
											+ (target.getPrayer()
													.isAncientCurses() ? "protective curses"
													: "protective prayers")
											+ "!");
					target.setStunDelay(5);
					target.getEffectsManager().startEffect(
							new Effect(EffectType.PROTECTION_DISABLED, 12));
				}
			}
		}, 0, 0);
	}

	private void sendRangeAttack(final Nex nex) {
		nex.setNextAnimation(new Animation(17413));
		int damage = 0;
		for (final Entity t : nex.getPossibleTargets()) {
			int distance = Utils.getDistance(t.getX(), t.getY(), nex.getX(),
					nex.getY());
			if (distance <= 10)
				damage = 4000 - (distance * 4000 / 11);
			else
				damage = 3000 + Utils.random(750);
			Projectile projectile = World.sendProjectileNew(nex, t, 380, 25,
					25, 32, 2, 0, 0);
			delayHit(
					nex,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					t,
					getRangedHit(
							nex,
							getMaxHit(nex, damage, NPCCombatDefinitions.RANGE,
									t)));
		}
	}

	private void sendVirusAttack(Nex nex) {
		nex.addVirusAttackDelay(45 + Utils.random(15));
		nex.setNextForceTalk(new ForceTalk("Let the virus flow through you!"));
		nex.playSoundEffect(3296);
		nex.setNextAnimation(new Animation(17414));
		nex.setNextGraphics(new Graphics(3375));
		List<Entity> targets = nex.getPossibleTargets();
		Entity target = targets.get(Utils.random(targets.size()));
		nex.setTarget(target);
		for (Entity t : targets) {
			if (!t.withinDistance(target, 3))
				continue;
			t.setNextForceTalk(new ForceTalk("*cough*"));
			// TODO skills drain?
			t.getTemporaryAttributtes().put("nex_infected", true);
		}
		nex.playSoundEffect(3296);
	}

	// 3370 blood nihil
	private void sendMagicAttack(final Nex nex, final boolean secondAttack) {
		for (final Entity t : nex.getPossibleTargets()) {
			nex.setNextAnimation(new Animation(17413));
			nex.setNextGraphics(new Graphics(1214));
			Projectile projectile = World.sendProjectileNew(nex, t, 3371, 25,
					25, 50, 2, 0, 0);

			final int damage = getMaxHit(nex, NPCCombatDefinitions.MAGE, t);
			Hit hit = getMagicHit(nex, damage);
			delayHit(nex,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					t, hit);
			if (nex.getCurrentPhase() == NexPhase.ZAROS)
				sendSoulSplit(hit, nex, t);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					t.setNextGraphics(new Graphics(3373));
					if (nex.getCurrentPhase() == NexPhase.SMOKE
							&& !secondAttack) {
						if (damage > 0 && Utils.random(5) == 0)
							EffectsManager.makePoisoned(t, 360);
					}
				}
			}, Utils.projectileTimeToCycles(projectile.getEndTime()) - 1);
		}
	}
}
