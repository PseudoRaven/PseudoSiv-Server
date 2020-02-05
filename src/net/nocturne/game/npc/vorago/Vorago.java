package net.nocturne.game.npc.vorago;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.FloorItem;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.Drop;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.impl.vorago.VoragoCombat;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.FriendsChat;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Vorago extends NPC {

	/**
	 * TODO List: Make bleed decrease as distance from Vorago increases Add a
	 * controller Instance it Make gravity fields actually go towards
	 * vorago[DONE] Bring it down interface bar fill[DONE] Ground break[DONE]
	 * Interface for waterfalls[DONE] Change screen view when mauling Vorago and
	 * bringing him down Work on dialogue Finish clone's abilities Find
	 * animations for gaps etc Add more ways of getting drops e.g most damage
	 * received Balancing and work out other flaws Make weapon pieces fall
	 * through too if not picked up & be instantly visible when dropped 5
	 * Players needed for a fight Stop the walking during bring it down Add
	 * other weeks (after working out the end add it into p5 scop)
	 */

	/**
	 * Weeks: 1 - Ceiling collapse 2 - Scopulus [DONE](apart from p5 should have
	 * the end) 3 - Vitalis 4 - Green Bomb 5 - Teamsplit 6 - The end
	 */

	private static final long serialVersionUID = 8957425938871772845L;

	/*** Phase Centres and Grave yard ***/
	public WorldTile GRAVE_AREA = new WorldTile(3098, 6165, 0);
	public WorldTile Centre[] = { new WorldTile(3102, 6110, 0),
			new WorldTile(3102, 6045, 0), new WorldTile(3038, 6046, 0),
			new WorldTile(3038, 5982, 0), new WorldTile(3100, 5982, 0) };
	public static WorldTile OUTSIDE = new WorldTile(3046, 6124, 0);

	private Player k;
	Player killers[] = { k, k, k, k, k };// Used for drops

	int dtX = Centre[4].getX() + 11;// Drop tile centre x coordinate
	int dtY = Centre[4].getY() + 2;// Drop tile centre y coordinate
	int dtZ = Centre[4].getPlane();// Drop tile centre z coordinate
	private WorldTile dropTiles[] = { new WorldTile(dtX - 1, dtY + 1, dtZ), // Phase
																			// 1
			new WorldTile(dtX + 1, dtY + 1, dtZ), // Phase 2
			new WorldTile(dtX, dtY, dtZ), // Phase 3
			new WorldTile(dtX - 1, dtY - 1, dtZ), // Phase 4
			new WorldTile(dtX + 1, dtY - 1, dtZ) };// Phase 5

	public int phase = 1;
	public int week = 2;// TODO other weeks
	public boolean special = false;
	public int specialType = 0;
	public int smashCount = 0;
	public int moves = 0;
	public boolean redBomb = true;// First attack is a red bomb
	public int fieldCount = 0;
	public boolean canDie = false;
	public Entity Reflectee;// Person that damage is being reflected onto
	public int bringDownCount = 0;
	public boolean isDown = false;
	public int transformCount = 0;
	public int downDamage = 0;
	public int scopDead = 0;
	public boolean canBeAttacked = true;
	public int rotation;
	public int wfCount = 0;
	public WorldTile wfTile;
	public WorldTile[] safeTiles = { wfTile, wfTile, wfTile, wfTile, wfTile,
			wfTile };
	public List<Player> safePlayers = Collections
			.synchronizedList(new ArrayList<Player>()); // the

	public boolean isReflecting = false;
	public Entity Clonee;// The person being cloned
	public int stoneId;
	public boolean cloneOut = false;
	private int specDrops = 0;
	public int startPushBack = 200000;

	public int pushBackDamage = startPushBack;
	private WorldTile tileFrom = new WorldTile(3116, 5985, 0);
	private WorldTile tileTo = new WorldTile(3104, 5985, 0);
	private WorldObject exitSphere = new WorldObject(84960, 10, 0, tileTo);
	static VoragoMinion S1 = VoragoHandler.scop1;
	static VoragoMinion S2 = VoragoHandler.scop2;

	public Vorago(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setNoDistanceCheck(true);
		setIntelligentRouteFinder(true);
	}

	public int getPhase() {
		return phase;
	}

	public WorldTile getCentre() {
		return Centre[phase - 1];
	}

	public void transformRago() {
		if (getId() == 17182) {
			setNextNPCTransformation(17183);
		} else {
			setNextNPCTransformation(17182);
		}
	}

	public void bringDownTransform() {
		if (transformCount == 0) {
			for (Player p : VoragoHandler.getPlayers()) {
				Entity n = VoragoHandler.vorago;
				p.setNextAnimation(new Animation(20394));
				p.setNextFaceEntity(n);
				p.getInterfaceManager().sendMinigameInterface(1413);
			}
			isDown = true;
			transformCount++;
			setCantInteract(true);
			setNextAnimation(new Animation(20382));
			WorldTasksManager.schedule(new WorldTask() {
				private int count = 0;

				@Override
				public void run() {
					if (count == 4) {
						setNextNPCTransformation(17184);
						stop();
						return;
					}
					count++;
				}
			}, 0, 1);

		} else if (transformCount == 1) {
			for (Player p : VoragoHandler.getPlayers()) {
				p.getInterfaceManager().removeMinigameInterface();
				p.getPackets().sendEntityMessage(
						1,
						15263739,
						p,
						"<col=1fe12c><shad>Vorago stumbles! Deal as much damage as possible"
								+ " to loosen the weapon piece!</shad></col>");
			}
			transformCount++;
			setCantInteract(false);
			setForceAgressive(false);
			int damageRequired = 15000 * VoragoHandler.getPlayersCount();// Not
																			// sure
																			// what
																			// a
																			// good
																			// damage
																			// required
																			// should
																			// be
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (downDamage < damageRequired) {// Resets vorago so you
														// have to do another 2
														// fields
						transformCount = 0;
						fieldCount = 2;
						specialType = 0;
						moves = 0;
						setNextNPCTransformation(17182);
						for (Player p : VoragoHandler.getPlayers()) {
							p.getPackets().sendCSVarInteger(2770, 0);
							p.setNextAnimation(new Animation(-1));
							p.getPackets().sendGameMessage(
									"You didn't do enough damage!");
						}
					} else {// Done enough damage so drop weapon piece
						for (Player p : VoragoHandler.getPlayers()) {
							p.getPackets()
									.sendEntityMessage(1, 15263739, p,
											"<col=1fe12c><shad>The weapon piece falls from Vorago's body!</shad></col>");
							p.setNextAnimation(new Animation(-1));

							World.addGroundItem(new Item(28602),
									getRandomJump());
							bringDownTransform();
						}
					}
					stop();
					return;
				}
			}, 20);

		} else if (transformCount == 2) {// Can now move onto P3
			transformCount++;
			setNextAnimation(new Animation(20362));
			WorldTasksManager.schedule(new WorldTask() {
				private int count = 0;

				@Override
				public void run() {
					if (count == 1) {
						setNextNPCTransformation(17182);
						special = false;
						canDie = true;
						stop();
						return;
					}
					count++;
				}
			}, 0, 1);
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (hit.getSource() != null && isReflecting == true) {
			int recoil = (int) (hit.getDamage());
			if (recoil > 0) {
				Hit hit2 = new Hit(this, recoil, hit.getLook());
				hit.setDamage(0);
				if (hit.isAbility()) {
					hit2.setAbilityMark();
				}
				if (hit.isCriticalHit()) {
					hit2.setCriticalMark();
				}
				Reflectee.applyHit(hit2);
			}
		}
		if (hit.getSource() != null && getId() == 17184) {// This is when
															// players have to
															// do enough damage
															// to loosen the
															// weapon piece when
															// he's down
			int dmg = (int) (hit.getDamage());
			heal(dmg);
			downDamage = downDamage + dmg;
		}
		if (hit.getSource() != null && !canBeAttacked) {
			hit.setDamage(0);
		}
		if (hit.getSource() != null && cloneOut) {// Damage is 1/8th when clone
													// is out
			hit.setDamage(hit.getDamage() / 8);
		}
		if (getPhase() == 5) {// pushes vorago back
			int dmg = (int) (hit.getDamage());
			pushBackDamage = pushBackDamage - dmg;
		}
		if (isCantInteract()) {
			hit.setDamage(0);
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		if (getPhase() == 2) {

		}
		if (getPhase() == 5) {// Handles the push back
			for (Player t : VoragoHandler.getPlayers()) {
				if (Utils.colides(Vorago.this, t)) {
					t.setNextWorldTile(new WorldTile(Vorago.this.getX() - 1, t
							.getY(), Vorago.this.getPlane()));
				}

				if (pushBackDamage <= startPushBack / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 10, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (2 * startPushBack) / 10
						&& pushBackDamage > (1 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 9, Centre[4].getY(), Centre[4]
									.getPlane()));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							t.getPackets()
									.sendEntityMessage(1, 15263739, t,
											"Nearly there, force Vorago back to finish him off the edge!");
							stop();
						}
					}, 1);
				}
				if (pushBackDamage <= (3 * startPushBack) / 8
						&& pushBackDamage > (2 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 8, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (4 * startPushBack) / 10
						&& pushBackDamage > (3 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 7, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (5 * startPushBack) / 10
						&& pushBackDamage > (4 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 6, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (6 * startPushBack) / 10
						&& pushBackDamage > (5 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 5, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (7 * startPushBack) / 10
						&& pushBackDamage > (6 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 4, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (8 * startPushBack) / 10
						&& pushBackDamage > (7 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 3, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (9 * startPushBack) / 10
						&& pushBackDamage > (8 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 2, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (11 * startPushBack) / 10
						&& pushBackDamage > (9 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() + 1, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (12 * startPushBack) / 10
						&& pushBackDamage > (11 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX(), Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (13 * startPushBack) / 10
						&& pushBackDamage > (12 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 1, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (14 * startPushBack) / 10
						&& pushBackDamage > (13 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 2, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (15 * startPushBack) / 10
						&& pushBackDamage > (14 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 3, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (16 * startPushBack) / 10
						&& pushBackDamage > (15 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 4, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (17 * startPushBack) / 10
						&& pushBackDamage > (16 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 5, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (18 * startPushBack) / 10
						&& pushBackDamage > (17 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 6, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
				if (pushBackDamage <= (19 * startPushBack) / 10
						&& pushBackDamage > (18 * startPushBack) / 10) {
					Vorago.this.setNextWorldTile(new WorldTile(
							Centre[4].getX() - 7, Centre[4].getY(), Centre[4]
									.getPlane()));
				}
			}
			if (pushBackDamage <= (20 * startPushBack) / 10
					&& pushBackDamage > (19 * startPushBack) / 10) {
				for (Player t : VoragoHandler.getPlayers()) {
					t.setNextAnimation(new Animation(20388));
					WorldTasksManager.schedule(new WorldTask() {

						@Override
						public void run() {
							t.sendDeath(Vorago.this);
							VoragoHandler.removePlayer(t);
						}

					}, 1);
				}
			}

		}
	}

	public void resetCounts() {// Used at the end of each phase
		smashCount = 0;
		moves = 0;
		redBomb = false;
	}

	public void resetVariables() {// Resets all the variables so can have
									// multiple
		// kills
		// VoragoCombat.removeGField();
		// VoragoCombat.removeWeaponPieces();
		World.ChallengerName = null;
		smashCount = 0;
		moves = 0;
		redBomb = false;
		phase = 1;
		special = false;
		specialType = 0;
		fieldCount = 0;
		canDie = false;
		bringDownCount = 0;
		isDown = false;
		transformCount = 0;
		downDamage = 0;
		scopDead = 0;
		canBeAttacked = true;
		wfCount = 0;
		isReflecting = false;
		cloneOut = false;
		specDrops = 0;
		startPushBack = 200000;
		pushBackDamage = startPushBack;
		week = 2;
		redBomb = true;
		GRAVE_AREA = new WorldTile(3098, 6165, 0);
		dtX = Centre[4].getX() + 11;// Drop tile centre x coordinate
		dtY = Centre[4].getY() + 2;// Drop tile centre y coordinate
		dtZ = Centre[4].getPlane();// Drop tile centre z coordinate
		WorldTile GRAVE_AREA = new WorldTile(3098, 6165, 0);
		WorldTile Centre[] = { new WorldTile(3102, 6110, 0),
				new WorldTile(3102, 6045, 0), new WorldTile(3038, 6046, 0),
				new WorldTile(3038, 5982, 0), new WorldTile(3100, 5982, 0) };
		WorldTile OUTSIDE = new WorldTile(3046, 6124, 0);
		Player k;
		int dtX = Centre[4].getX() + 11;// Drop tile centre x coordinate
		int dtY = Centre[4].getY() + 2;// Drop tile centre y coordinate
		int dtZ = Centre[4].getPlane();// Drop tile centre z coordinate
		WorldTile dropTiles[] = { new WorldTile(dtX - 1, dtY + 1, dtZ), // Phase
																		// 1
				new WorldTile(dtX + 1, dtY + 1, dtZ), // Phase 2
				new WorldTile(dtX, dtY, dtZ), // Phase 3
				new WorldTile(dtX - 1, dtY - 1, dtZ), // Phase 4
				new WorldTile(dtX + 1, dtY - 1, dtZ) };// Phase 5
		WorldTile wfTile;
		safePlayers = Collections.synchronizedList(new ArrayList<Player>());// The
																			// players
																			// that
																			// are
																			// in
																			// behind
																			// the
																			// waterfall
		boolean isReflecting = false;
		Entity Clonee;// The person being cloned
		int stoneId;
		boolean cloneOut = false;
		int specDrops = 0;
		int startPushBack = 200000;// This seems to be a decent start point,
									// making it higher makes P5 harder and vice
									// versa
		pushBackDamage = startPushBack;
		WorldTile tileFrom = new WorldTile(3116, 5985, 0);
		WorldTile tileTo = new WorldTile(3104, 5985, 0);
		WorldObject exitSphere = new WorldObject(84960, 10, 0, tileTo);
	}

	public WorldTile getRandomGfield() {
		int random = Utils.random(7);
		if (random == 0)
			return new WorldTile(3111, 6054, 0);
		if (random == 1)
			return new WorldTile(3111, 6041, 0);
		if (random == 2)
			return new WorldTile(3111, 6053, 0);
		if (random == 3)
			return new WorldTile(3104, 6056, 0);
		if (random == 4)
			return new WorldTile(3103, 6041, 0);
		if (random == 5)
			return new WorldTile(3095, 6043, 0);
		if (random == 6)
			return new WorldTile(3096, 6054, 0);
		if (random == 7)
			return new WorldTile(3104, 6055, 0);
		return new WorldTile(3106, 6037, 0);
	}

	public WorldTile getRandomJump() {// Using Math.random() as I don't feel
										// Utils.random is very random
		int a = (int) (-6 + Math.random() * 12);
		int b = (int) (-6 + Math.random() * 12);
		int c = (int) (-2 + Math.random() * 4);
		int d = (int) (-2 + Math.random() * 4);
		if (getPhase() < 5) {
			return new WorldTile(getCentre().getX() + a,
					getCentre().getY() + b, 0);
		} else {
			return new WorldTile(getCentre().getX() - 6 + c, getCentre().getY()
					+ 2 + d, 0);
		}
	}

	public void endPhase() {
		resetCounts();// emote 20381
		canDie = false;
		setCantInteract(true);
		setHitpoints(getMaxHitpoints());
		final WorldTile A = getRandomJump();
		final WorldTile B = getRandomJump();
		final WorldTile C = getRandomJump();
		setNextFaceWorldTile(A);
		setNextAnimation(new Animation(20365));
		setNextGraphics(new Graphics(4019));
		WorldTasksManager.schedule(new WorldTask() {
			int count = 0;

			@Override
			public void run() {
				if (count == 1)
					World.sendGraphics(
							Vorago.this,
							new Graphics(4037),
							new WorldTile(A.getX() + 2, A.getY() + 2, A
									.getPlane()));
				if (count == 3) {
					setNextWorldTile(A);
					setNextAnimation(new Animation(20367));
					setNextGraphics(new Graphics(4020));
					sendLandHit();
				}
				if (count == 4) {
					setNextFaceWorldTile(B);
					setNextAnimation(new Animation(20365));
					setNextGraphics(new Graphics(4019));
				}
				if (count == 5)
					World.sendGraphics(
							Vorago.this,
							new Graphics(4037),
							new WorldTile(B.getX() + 2, B.getY() + 2, B
									.getPlane()));
				if (count == 7) {
					setNextWorldTile(B);
					setNextAnimation(new Animation(20367));
					setNextGraphics(new Graphics(4020));
					sendLandHit();
				}
				if (count == 8) {
					setNextFaceWorldTile(C);
					setNextAnimation(new Animation(20365));
					setNextGraphics(new Graphics(4019));
				}
				if (count == 9)
					World.sendGraphics(
							Vorago.this,
							new Graphics(4037),
							new WorldTile(C.getX() + 2, C.getY() + 2, C
									.getPlane()));
				if (count == 11) {
					int[] xstart = { 3090, 3090, 3026, 3026 };// Phase start
																// locations for
																// X, 1, 2, 3, 4
					int[] ystart = { 6098, 6034, 6034, 5970 };// Phase start
																// locations for
																// Y, 1, 2, 3, 4
					World.spawnObjectTemporary(new WorldObject(84873, 10, 1,
							new WorldTile(xstart[phase - 1], ystart[phase - 1],
									0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84871, 10, 1,
							new WorldTile(xstart[phase - 1],
									ystart[phase - 1] + 9, 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84873, 10, 2,
							new WorldTile(xstart[phase - 1],
									ystart[phase - 1] + 18, 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84871, 10, 0,
							new WorldTile(xstart[phase - 1] + 9,
									ystart[phase - 1], 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84869, 10, 0,
							new WorldTile(xstart[phase - 1] + 9,
									ystart[phase - 1] + 9, 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84871, 10, 2,
							new WorldTile(xstart[phase - 1] + 9,
									ystart[phase - 1] + 18, 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84873, 10, 0,
							new WorldTile(xstart[phase - 1] + 18,
									ystart[phase - 1], 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84871, 10, 3,
							new WorldTile(xstart[phase - 1] + 18,
									ystart[phase - 1] + 9, 0)), 5000);
					World.spawnObjectTemporary(new WorldObject(84873, 10, 3,
							new WorldTile(xstart[phase - 1] + 18,
									ystart[phase - 1] + 18, 0)), 5000);
				}
				if (count == 10) {
					for (Entity p : VoragoHandler.getPlayers()) {
						setNextWorldTile(C);
						setNextAnimation(new Animation(20367));
						setNextGraphics(new Graphics(4020));
						sendLandHit();
					}
				}
				if (count == 12) {
					phase++;
					setNextAnimation(new Animation(20323));
					for (Entity p : VoragoHandler.getPlayers()) {
						p.setNextAnimation(new Animation(20402));
					}
				}
				if (count == 13) {
					for (Entity p : VoragoHandler.getPlayers()) {
						p.setNextWorldTile(new WorldTile(getRandomJump()));
						p.setNextAnimation(new Animation(20401));
					}
					setHitpoints(getMaxHitpoints());
				}
				if (count == 18) {
					for (Entity p : VoragoHandler.getPlayers()) {
						final WorldTile itemCoords = getRandomJump();
						final FloorItem item = World.getRegion(
								itemCoords.getRegionId()).getGroundItem(28602,
								itemCoords, (Player) p);
					}
					setNextWorldTile(getCentre());
					setNextAnimation(new Animation(20367));
					setNextGraphics(new Graphics(4020));
					setCantInteract(false);
					switch (phase) {
					case 2:
						special = true;
						specialType = 0;
						break;
					case 3:
						transformRago();
						if (week == 2) {
							canBeAttacked = false;
							special = false;

							VoragoCombat.spawnScop();
						}
						break;
					case 4:
						for (Player p : VoragoHandler.getPlayers()) {
							final WorldTile itemCoords = getRandomJump();
							final FloorItem item = World.getRegion(
									itemCoords.getRegionId()).getGroundItem(
									28602, itemCoords, (Player) p);
						}

						special = false;
						specialType = 1;
						VoragoCombat.sendWaterfall();
						break;
					case 5:
						if (getId() == 17182) {
							transformRago();
						}
						redBomb = true;
						special = false;
						specialType = 0;
						break;
					}
				}
				count++;
			}
		}, 0, 1);
	}

	public void sendLandHit() {
		for (Entity target1 : getPossibleTargets()) {
			if (Utils.colides(Vorago.this, target1)) {
				target1.applyHit(new Hit(Vorago.this, Utils.random(3000, 5001),
						HitLook.REGULAR_DAMAGE));
				if (target1 instanceof Player)
					target1.setNextAnimation(new Animation(10070));
			}
		}
	}

	private static Drop ENERGIES = new Drop(28627, 0, 2);// TODO work out why
															// the number of
															// energies isn't
															// what I set

	private static final Drop[] COMMON_DROPS = { new Drop(220, 15, 20),
			new Drop(28264, 10, 35), new Drop(15271, 75, 152),
			new Drop(28264, 10, 35), new Drop(1128, 3, 9),
			new Drop(5304, 4, 10), new Drop(452, 5, 28),
			new Drop(1748, 35, 55), new Drop(9245, 20, 55),
			new Drop(1514, 35, 128), new Drop(31867, 50, 50) };// TODO add trisk
																// fragments

	private static final Drop[] SPECIAL_DROPS = { new Drop(28617, 1, 1),
			new Drop(28621, 1, 1), new Drop(28626, 1, 1) };

	public Item sendDrop(Player player, Drop drop, WorldTile dropPlace) {
		if ((drop.getItemId() >= 28617 && drop.getItemId() <= 28626)
				|| drop.getItemId() == 33716) {
			player.getPackets().sendGraphics(new Graphics(4422), dropPlace);
			// World.sendGraphics(player, new Graphics(4422), dropPlace);
			World.sendNews(player, player.getDisplayName()
					+ " has received "
					+ ItemDefinitions.getItemDefinitions(drop.getItemId())
							.getName() + " drop!", 1, true);
		}
		boolean stackable = ItemDefinitions
				.getItemDefinitions(drop.getItemId()).isStackable();
		Item item = stackable ? new Item(drop.getItemId(),
				(drop.getMinAmount()) + Utils.random(drop.getExtraAmount()))
				: new Item(drop.getItemId(), drop.getMinAmount()
						+ Utils.random(drop.getExtraAmount()));
		if (!stackable && item.getAmount() > 1) {
			for (int i = 0; i < item.getAmount(); i++)
				World.addGroundItem(new Item(item.getId(), 1), dropPlace,
						player, true, 60);
		} else
			World.addGroundItem(item, dropPlace, player, true, 60);
		return item;
	}

	public List<Drop> generateDrops(Player killer, double e) {
		List<Drop> d = new ArrayList<Drop>();
		d.add(ENERGIES);
		if (Math.random() * 100 <= 30 * e && specDrops == 0) {// 30% Chance of a
																// special each
																// drop - only
																// one per kill
			specDrops++;
			d.add(SPECIAL_DROPS[Utils.random(SPECIAL_DROPS.length)]);
		} else {
			d.add(COMMON_DROPS[Utils.random(COMMON_DROPS.length)]);
		}
		return d;
	}

	@Override
	public void drop() {// Handles the drop - most of this is copied from the
		// actual drop in NPC.java

		for (int i = 0; i < dropTiles.length; i++) {
			Player killer = killers[i];
			if (killer == null)
				return;
			List<Player> players = FriendsChat.getLootSharingPeople(killer);

			double dropRate = 0;

			if (players == null || players.size() >= 1)
				dropRate = 1;

			List<Drop> dropL = generateDrops(killer, dropRate);

			if (players == null || players.size() == 1) {
				for (Drop drop : dropL) {
					sendDrop(killer, drop, dropTiles[i]);
				}
			} else {
				for (Drop drop : dropL) {
					Player luckyPlayer = players.get(Utils.random(players
							.size()));
					Item item = sendDrop(luckyPlayer, drop, dropTiles[i]);
					luckyPlayer.getPackets().sendGameMessage(
							"<col=00FF00>You received: " + item.getAmount()
									+ " " + item.getName() + ".");
					for (Player p2 : players) {
						if (p2 == luckyPlayer)
							continue;
						p2.getPackets().sendGameMessage(
								"<col=66FFCC>" + luckyPlayer.getDisplayName()
										+ "</col> received: "
										+ item.getAmount() + " "
										+ item.getName() + ".");
						p2.getPackets().sendGameMessage(
								"Your chance of receiving loot has improved.");
					}
				}
			}

		}
	}

	@Override
	public void sendDeath(Entity p) {
		if (canDie) {
			final int deathDelay = 20;
			if (getPhase() != 3) {// Phase 3 is done through the scopuli
				killers[getPhase() - 1] = getMostDamageReceivedSourcePlayer();
				resetReceivedDamage();
			}
			if (getPhase() == 1) {
				for (Player player : VoragoHandler.getPlayers())
					player.getVarsManager().sendVarBit(18553, 0);
			}
			if (getPhase() == 2) {
				for (Player player : VoragoHandler.getPlayers())
					if (player.pickedUpPiece2 = false)
						World.addGroundItem(new Item(28602), new WorldTile(
								3038, 6046, 0));
			}
			if (getPhase() == 4) {
				for (Player player : VoragoHandler.getPlayers())
					if (player.pickedUpPiece3 = false)
						World.addGroundItem(new Item(28604), new WorldTile(
								3100, 5982, 0));
			}
			if (getPhase() < 5) {
				setCantInteract(true);
				getCombat().removeTarget();
				endPhase();
			} else {
				setNextAnimation(new Animation(20352));
				setNextGraphics(new Graphics(4036));
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						drop();
						resetVariables();
						setLocation(new WorldTile(3141, 6132, 0));
						reset();
						finish();
						VoragoHandler.endFight();
						stop();
					}
				}, deathDelay);
				WorldTasksManager.schedule(new WorldTask() {
					int count = 0;

					@Override
					public void run() {
						if (count == 1)
							World.sendProjectileNew(tileFrom, tileTo, 4029, 9,
									9, 0, 1.1, 0, 0);
						if (count == 3) {
							for (Entity p : VoragoHandler.getPlayers())
								World.sendGraphics(p, new Graphics(4030),
										tileTo);
							World.spawnObject(exitSphere);
						}
						count++;
					}
				}, 16, 1);
			}
		} else {
			setHitpoints(76000);
		}
	}

}