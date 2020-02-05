package net.nocturne.game.map.bossInstance.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.Animation;
import net.nocturne.game.DeathBar;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.map.bossInstance.BossInstanceHandler;
import net.nocturne.game.map.bossInstance.InstanceSettings;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.riseofthesix.Ahrim;
import net.nocturne.game.npc.riseofthesix.Dharok;
import net.nocturne.game.npc.riseofthesix.Guthan;
import net.nocturne.game.npc.riseofthesix.Karil;
import net.nocturne.game.npc.riseofthesix.Torag;
import net.nocturne.game.npc.riseofthesix.Verac;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.Cutscene;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Logger;
import net.nocturne.utils.Utils;

public class RotsInstance extends BossInstance {

	private List<Player> playersOn;
	public boolean entered = false;
	boolean vent1Left = false, vent2Left = false, vent3Left = false,
			vent4Left = false, vent5Left = false, vent6Left = false,
			vent7Left = false, vent8Left = false, vent1Right = false,
			vent2Right = false, vent3Right = false, vent4Right = false,
			vent5Right = false, vent6Right = false, vent7Right = false,
			vent8Right = false, vent1RightRed = false, vent2RightRed = false,
			vent3RightRed = false, vent4RightRed = false,
			vent5RightRed = false, vent6RightRed = false,
			vent7RightRed = false, vent8RightRed = false, plate1 = false,
			plate2 = false, plate3 = false, plate4 = false, fightEnded = false;

	public boolean tasksStarted = false, disrupted = false;
	int valueActiveLeftPlatform;
	int valueActiveRightPlatform;
	long nextPlatformChangeLeft;
	long nextPlatformChangeRight;
	public boolean fightStarted = false;
	public boolean pressurePads = false;
	int toragDeathTimer = 0;
	int dharokDeathTimer = 0;
	int veracDeathTimer = 0;
	int guthanDeathTimer = 0;
	int ahrimDeathTimer = 0;
	int karilDeathTimer = 0;
	public int deadTimer;
	boolean ahrimSpec = false;
	boolean karilSpec = false;
	int bar = 0;
	boolean barIncrement = false;
	int inactiveCount = 0;
	boolean portalCollapsed = false;
	boolean ahrimSpecDone = false;
	boolean karilSpecDone = false;
	boolean flown = false;
	long flownTime = 0;
	public boolean fightFinished = false;
	boolean startedCollapse = false;
	boolean usedPlates = false;
	int shadowRealmCount = 0;
	boolean vent1 = false;
	boolean vent2 = false;
	boolean vent3 = false;
	boolean vent4 = false;
	boolean vent5 = false;
	boolean vent6 = false;
	boolean vent7 = false;
	boolean vent8 = false;
	boolean sideSwitchLeft = false;
	boolean sideSwitchRight = false;
	int portalDamage = 0;
	boolean portalUsed = false;
	boolean portalSpawned = false;
	boolean boundedSpot = false;
	long lastDash = 0;
	boolean multiVent = false;
	boolean ahrimPillar = false;
	boolean karilPillar = false;
	public boolean leftDisabled = false;
	public boolean rightDisabled = false;
	boolean startedCollapsing = true;
	int shakeValue = 0;
	long karilPillarTime = 0;
	boolean bombard = false;
	boolean bleed = false;
	long caveBombsTime = 0;
	long caveBombsDelay = 0;
	boolean damaged1 = false;
	boolean damaged2 = false;
	boolean damaged3 = false;
	boolean damaged4 = false;
	boolean damaged5 = false;
	boolean damaged6 = false;

	int sizeX = 2; // horizontal size
	int sizeY = 6; // vertical size

	int chunkX = 308; // bottom left chunk x
	int chunkY = 754; // bottom left chunk y

	Torag torag = (Torag) World.spawnNPC(18544, new WorldTile(0, 0, 0), -1,
			true);
	Ahrim ahrim = (Ahrim) World.spawnNPC(18538, new WorldTile(0, 0, 0), -1,
			true);
	Dharok dharok = (Dharok) World.spawnNPC(18540, new WorldTile(0, 0, 0), -1,
			true);
	Karil karil = (Karil) World.spawnNPC(18543, new WorldTile(0, 0, 0), -1,
			true);
	Guthan guthan = (Guthan) World.spawnNPC(18541, new WorldTile(0, 0, 0), -1,
			true);
	Verac verac = (Verac) World.spawnNPC(18545, new WorldTile(0, 0, 0), -1,
			true);
	public NPC portalRight = (NPC) World.spawnNPC(18552,
			new WorldTile(0, 0, 0), -1, true);
	public NPC portalLeft = (NPC) World.spawnNPC(18552, new WorldTile(0, 0, 0),
			-1, true);

	private int[] boundChunks;

	public RotsInstance(Player owner, InstanceSettings settings) {
		super(owner, settings);
		playersOn = new CopyOnWriteArrayList<Player>();
	}

	public List<Player> getPlayersOn() {
		return playersOn;
	}

	@Override
	public int[] getMapSize() {
		return new int[] { 2, 2 };
	}

	@Override
	public int[] getMapPos() {
		return new int[] { 288, 752 };
	}

	@Override
	public void leaveInstance(Player player, int type) {
		super.leaveInstance(player, type);
	}

	public void leaveBattle(Player player) {
		synchronized (BossInstanceHandler.LOCK) {
			player.getTimersManager().removeTimer(); // removes timer without
														// saving, wont do
														// anything if it wasnt
														// enabled yet
			playersOn.remove(player);
			if (getPlayersOn().isEmpty()) {
				finish();
				removeMapChunks(null);
			}
		}
	}

	// accept challenge
	public void enterBattle(Player player) {
		if (getPlayersCount() == 5) {
			leaveInstance(player, BossInstance.EXITED);
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					stage = 0;
					sendNPCDialogue(18498, NORMAL,
							"Only 4 are permitted entry within the dungeon.");
				}

				@Override
				public void run(int interfaceId, int componentId, int slotId) {
					end();
				}

				@Override
				public void finish() {

				}

			});
			return;
		}
		if (fightStarted == true) {
			leaveInstance(player, BossInstance.EXITED);
			player.getDialogueManager().startDialogue(new Dialogue() {

				@Override
				public void start() {
					stage = 0;
					sendNPCDialogue(18498, NORMAL,
							"This fight has already begun, thus you cannot join it.");
				}

				@Override
				public void run(int interfaceId, int componentId, int slotId) {
					end();
				}

				@Override
				public void finish() {

				}

			});
			return;
		}
		player.setGuthanBleed(false);
		playersOn.add(player);
		player.setInRots(false);
		player.setDisrupted(false);
	}

	public void processPressurePads() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					if (getPlayersCount() == 0) {
						return;
					}
					if (plate1 == true && plate2 == true && plate3 == true
							&& plate4 == true && fightEnded == false
							&& usedPlates == false) {
						usedPlates = true;
						for (Player p : getPlayers()) {
							p.lock(3);
							p.setNextAnimation(new Animation(21916));
							p.setNextGraphics(new Graphics(4413));
							GameExecutorManager.slowExecutor.schedule(
									new Runnable() {
										@Override
										public void run() {
											try {
												p.setNextAnimation(new Animation(
														21915));
												p.setNextGraphics(new Graphics(
														4413));
												p.setNextWorldTile(new WorldTile(
														p.getX() + 2, p.getY(),
														p.getPlane()));
											} catch (Throwable e) {
												Logger.handle(e);
											}
										}
									}, 500, TimeUnit.MILLISECONDS);
						}
					}
					for (Player p : getPlayersOn()) {
						if (p.getGuthanBleed() == true
								&& fightFinished == false) {
							p.setNextGraphics(new Graphics(4411, 1, 120, 0,
									true));
							p.setNextGraphics(new Graphics(4407, 1, 100, 0,
									true));
						}
					}
					for (Player p : getPlayersOn()) {
						// first pressure pad
						if (p.getX() == getWorldTile(28, 75).getX()
								&& p.getY() == getWorldTile(28, 75).getY()) {
							plate1 = true;
						} else if (p.getX() != getWorldTile(28, 75).getX()
								&& p.getY() != getWorldTile(28, 75).getY()) {
							plate1 = false;
						}
						// second pressure pad
						if (p.getX() == getWorldTile(28, 74).getX()
								&& p.getY() == getWorldTile(28, 74).getY()) {
							plate2 = true;
						} else if (p.getX() != getWorldTile(28, 74).getX()
								&& p.getY() != getWorldTile(28, 74).getY()) {
							plate2 = false;
						}
						// third pressure pad
						if (p.getX() == getWorldTile(28, 69).getX()
								&& p.getY() == getWorldTile(28, 69).getY()) {
							plate3 = true;
						}
						if (p.getX() != getWorldTile(28, 69).getX()
								&& p.getY() != getWorldTile(28, 69).getY()) {
							plate3 = false;
						}
						// fourth pressure pad
						if (p.getX() == getWorldTile(28, 68).getX()
								&& p.getY() == getWorldTile(28, 68).getY()) {
							plate4 = true;
						}
						if (p.getX() != getWorldTile(28, 68).getX()
								&& p.getY() != getWorldTile(28, 68).getY()) {
							plate4 = false;
						}

					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 50, TimeUnit.MILLISECONDS);
	}

	public void barManager() {
		if (ahrim.getId() == 18546) {
			ahrim.getNextHitBars().add(new DeathBar(ahrim, bar));
		}
		if (dharok.getId() == 18547) {
			dharok.getNextHitBars().add(new DeathBar(dharok, bar));
		}
		if (guthan.getId() == 18548) {
			guthan.getNextHitBars().add(new DeathBar(guthan, bar));
		}
		if (karil.getId() == 18549) {
			karil.getNextHitBars().add(new DeathBar(karil, bar));
		}
		if (torag.getId() == 18550) {
			torag.getNextHitBars().add(new DeathBar(torag, bar));
		}
		if (verac.getId() == 18551) {
			verac.getNextHitBars().add(new DeathBar(verac, bar));
		}
	}

	public void transformToActive() {
		if (ahrim.getId() == 18546 && bar == 260) {
			inactiveCount--;
			ahrim.setNextAnimation(new Animation(21914));
			ahrim.setNextNPCTransformation(18538);
			ahrim.setHitpoints(25000);
			barIncrement = false;
		}
		if (dharok.getId() == 18547 && bar == 260) {
			inactiveCount--;
			dharok.setNextAnimation(new Animation(21914));
			dharok.setNextNPCTransformation(18540);
			dharok.setHitpoints(25000);
			barIncrement = false;
		}
		if (guthan.getId() == 18548 && bar == 260) {
			inactiveCount--;
			guthan.setNextAnimation(new Animation(21914));
			guthan.setNextNPCTransformation(18541);
			guthan.setHitpoints(25000);
			barIncrement = false;
		}
		if (karil.getId() == 18549 && bar == 260) {
			inactiveCount--;
			karil.setNextAnimation(new Animation(21914));
			karil.setNextNPCTransformation(18543);
			karil.setHitpoints(25000);
			barIncrement = false;
		}
		if (torag.getId() == 18550 && bar == 260) {
			inactiveCount--;
			torag.setNextAnimation(new Animation(21914));
			torag.setNextNPCTransformation(18544);
			torag.setHitpoints(25000);
			barIncrement = false;
		}
		if (verac.getId() == 18551 && bar == 260) {
			inactiveCount--;
			verac.setNextAnimation(new Animation(21914));
			verac.setNextNPCTransformation(18545);
			verac.setHitpoints(25000);
			barIncrement = false;
		}
		if (bar == 260) {
			barIncrement = false;
			bar = 0;
		}
	}

	public void healOthers() {
		if (torag.getId() == 18544) {
			torag.heal(5000);
		}
		if (dharok.getId() == 18540) {
			dharok.heal(5000);
		}
		if (ahrim.getId() == 18538 || ahrim.getId() == 18539) {
			ahrim.heal(5000);
		}
		if (verac.getId() == 18545) {
			verac.heal(5000);
		}
		if (guthan.getId() == 18541 || guthan.getId() == 18542) {
			guthan.heal(5000);
		}
		if (karil.getId() == 18543) {
			karil.heal(5000);
		}
	}

	public void transformToInactive() {
		if (ahrim.getId() == 18538 && ahrim.getHitpoints() == 0
				|| ahrim.getId() == 18539 && ahrim.getHitpoints() == 0) {
			ahrim.setNextNPCTransformation(18546);
			healOthers();
			barIncrement = true;
			inactiveCount++;
			bar = 0;
		}
		if (dharok.getId() == 18540 && dharok.getHitpoints() == 0) {
			dharok.setNextNPCTransformation(18547);
			barIncrement = true;
			healOthers();
			inactiveCount++;
			bar = 0;
		}
		if (guthan.getId() == 18541 && guthan.getHitpoints() == 0
				|| guthan.getId() == 18542 && guthan.getHitpoints() == 0) {
			guthan.setNextNPCTransformation(18548);
			barIncrement = true;
			healOthers();
			inactiveCount++;
			bar = 0;
		}
		if (karil.getId() == 18543 && karil.getHitpoints() == 0) {
			karil.setNextNPCTransformation(18549);
			barIncrement = true;
			healOthers();
			inactiveCount++;
			bar = 0;
		}
		if (torag.getId() == 18544 && torag.getHitpoints() == 0) {
			torag.setNextNPCTransformation(18550);
			healOthers();
			barIncrement = true;
			inactiveCount++;
			bar = 0;
		}
		if (verac.getId() == 18545 && verac.getHitpoints() == 0) {
			verac.setNextNPCTransformation(18551);
			healOthers();
			barIncrement = true;
			inactiveCount++;
			bar = 0;
		}
	}

	public void processBrothers() {
		transformToActive();
		transformToInactive();
		barManager();
		if (fightStarted == true && fightFinished == false
				&& portalSpawned == false && Utils.random(100) == 1
				&& karil.getId() == 18543 && !karil.isCantInteract()
				&& lastDash < Utils.currentTimeMillis() && karilPillar == false) {
			lastDash = Utils.currentTimeMillis() + 30000;
			spawnPortal();
		}
		if (ahrim.getId() == 18538 && flown == false
				&& ahrim.getHitpoints() <= 45000 && fightStarted == true) {
			flownTime = +Utils.currentTimeMillis() + 45000;
			flown = true;
			ahrim.setNextAnimation(new Animation(21926));
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					ahrim.setNextNPCTransformation(18539);
					ahrim.setForceFollowClose(false);
				}
			}, 700, TimeUnit.MILLISECONDS);
		}
		if (ahrim.getId() == 18539 && Utils.currentTimeMillis() >= flownTime) {
			ahrim.setNextAnimation(new Animation(21927));
			GameExecutorManager.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					ahrim.setNextNPCTransformation(18538);
				}
			}, 700, TimeUnit.MILLISECONDS);
		}
	}

	public void processHits() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					WorldTile boundSpot = getWorldTile(58, 84);
					if (getPlayersCount() == 0) {
						return;
					}
					if (inactiveCount == 6) {
						return;
					}
					if (vent1RightRed == true || vent2RightRed == true
							|| vent3RightRed == true || vent4RightRed == true
							|| vent5RightRed == true || vent6RightRed == true
							|| vent7RightRed == true || vent8RightRed == true) {
						AhrimTile();
						GuthanTile();
						ToragTile();
						VeracTile();
						KarilTile();
						DharokTile();
					}
					if (Utils.random(60) == 44 && bleed == false
							&& guthan.getHitpoints() >= 1
							& fightStarted == true && guthan.getId() == 18541
							&& fightFinished == false && isInRightArea(guthan)) {
						for (Player p : getPlayers()) {
							if (!isInRightArea(p)
									&& guthan
											.getMostDamageReceivedSourcePlayer() != p) {
								bleed = true;
								WorldTasksManager.schedule(new WorldTask() {
									int stages = 0;

									@Override
									public void run() {
										int X = 0;
										int Y = 0;
										if (stages == 1) {
											if (p.withinDistance(guthan, 8)) {
												p.getPackets()
														.sendEntityMessage(1,
																14338209, p,
																"<col=ff0000>Guthan prepares to throw his spear!");
											}
										}
										if (stages == 5) {
											guthan.setCantInteract(true);
										}
										if (stages == 6) {
											startBleed();
											guthan.setNextAnimation(new Animation(
													21944));
											finish();
										}
										stages++;
									}

								}, 1, 1);
							}

						}
					}
					if (ahrimSpecDone == false && fightStarted == true) {
						ahrim.setTarget(null);
						ahrimSpecDone = true;
						WorldTasksManager.schedule(new WorldTask() {
							int stages = 0;

							@Override
							public void run() {
								int X = 0;
								int Y = 0;
								if (stages == 1) {
									ahrimSpecial();
									stop();
								}
								stages++;
							}

						}, 1, 1);
					}
					if (karilSpecDone == false && fightStarted == true) {
						karil.setTarget(null);
						karilSpecDone = true;
						karil.setNextWorldTile(getWorldTile(58, 84));
						WorldTasksManager.schedule(new WorldTask() {
							int stages = 0;

							@Override
							public void run() {
								int X = 0;
								int Y = 0;
								if (stages == 1) {
									karilSpecial();
								}
								stages++;
							}

						}, 1, 1);
					}
					if (Utils.random(70) == 23 && bombard == false
							&& karilPillar == true && fightFinished == false) {
						karil.setNextAnimation(new Animation(21929));
						for (Player p : getPlayers())
							p.getPackets().sendEntityMessage(1, 14338209, p,
									"Karil launched his bombard attack!");
						sendBombard();
					}
					if (ahrimSpec == true) {
						for (Player p : getPlayersOn()) {
							if (p.withinDistance(ahrim, 4)) {
								p.applyHit(new Hit(p, Utils.random(300, 1500),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
					if (karilPillar == true && fightFinished == false) {
						for (Player p : getPlayersOn()) {
							if (p.getAttackedBy() == karil && isInRightArea(p)) {

							}
						}
					}
					if (Utils.currentTimeMillis() >= karilPillarTime
							&& karilPillar == true && fightFinished == false) {
						karilPillar = false;
						karilPillarTime = 0;
						karil.pillarMode = false;
						for (Player p : getPlayers()) {
							if (!isInRightArea(p)) {
								karil.setNextWorldTile(getWorldTile(
										Utils.random(38, 43),
										Utils.random(78, 90)));
							} else
								karil.setNextWorldTile(getWorldTile(
										Utils.random(55, 60),
										Utils.random(78, 90)));

						}
					}
					if (karilSpec == true) {
						for (Player p : getPlayersOn()) {
							if (p.withinDistance(karil, 4)) {
								p.applyHit(new Hit(p, Utils.random(300, 1500),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
					for (Player p : getPlayersOn()) {
						if (p.isStunned()) {
							p.applyHit(new Hit(p, 250, HitLook.REGULAR_DAMAGE));
						}
					}
					for (Player p : getPlayersOn()) {
						if (p.getGuthanBleed() == true) {
							p.applyHit(new Hit(p, Utils.random(400, 500),
									HitLook.REGULAR_DAMAGE));
						}
					}
					for (Player p : getPlayersOn()) {
						if (p.getGuthanBleed() == true
								&& p.withinDistance(guthan, 1)) {
							guthan.setCantInteract(true);
							p.lock(4);
							WorldTasksManager.schedule(new WorldTask() {
								int stages = 0;

								@Override
								public void run() {
									int X = 0;
									int Y = 0;
									if (stages == 1) {
										guthan.setNextFaceEntity(p);
									}
									if (stages == 2) {
										guthan.setNextAnimation(new Animation(
												21947));
										guthan.setNextNPCTransformation(18541);
										p.setNextAnimation(new Animation(21945));
									}
									if (stages == 3) {
										p.applyHit(new Hit(p, 1000,
												HitLook.REGULAR_DAMAGE));
										guthan.setCantInteract(false);
										p.setGuthanBleed(false);
										p.unlock();
										bleed = false;
										finish();
									}

									stages++;
								}

							}, 1, 1);
						}
					}
					if (karilPillar == false
							&& Utils.random(150) == Utils.random(200)
							&& !karil.isCantInteract()
							&& karil.getId() == 18543
							&& karil.getHitpoints() >= 3000
							&& fightStarted == true) {
						karilPillar = true;
						karilPillar();
					}
					if (Utils.currentTimeMillis() >= caveBombsTime
							&& Utils.currentTimeMillis() >= caveBombsDelay
							&& startedCollapse == true && fightFinished == true) {
						for (Player p : getPlayersOn()) {
							caveBombsDelay = Utils.currentTimeMillis() + 1500;
							p.applyHit(new Hit(p, Utils.random(800, 1200),
									HitLook.REGULAR_DAMAGE));
							World.sendGraphics(
									p,
									new Graphics(4412),
									new WorldTile(p.getX() + 7, p.getY(), p
											.getPlane()));
							World.sendGraphics(
									p,
									new Graphics(4412),
									new WorldTile(p.getX(), p.getY() + 8, p
											.getPlane()));
							World.sendGraphics(
									p,
									new Graphics(4412),
									new WorldTile(p.getX(), p.getY() - 9, p
											.getPlane()));
							World.sendGraphics(
									p,
									new Graphics(4412),
									new WorldTile(p.getX() - 4, p.getY(), p
											.getPlane()));
						}
					}
					for (Player p : getPlayersOn()) {
						if (verac.getId() == 18545
								&& isInRightArea(p)
								&& boundedSpot == false
								&& Utils.random(65) == 1
								&& verac.getMostDamageReceivedSourcePlayer() == p) {
							boundedSpot = true;
							verac.getMostDamageReceivedSourcePlayer()
									.getPackets()
									.sendEntityMessage(0, 16711680, p,
											"Verac has bound your soul to a specific place!");
							verac.getMostDamageReceivedSourcePlayer()
									.setBinded(true);
						}
						if (p.getBinded() && boundedSpot == true) {
							p.getPackets().sendGraphics(new Graphics(3273),
									boundSpot);
							p.getPrayer().drainPrayer(50);
						}
						if (p.getX() == boundSpot.getX()
								&& p.getY() == boundSpot.getY()
								&& p.getBinded()) {
							boundedSpot = false;
							p.getPackets()
									.sendEntityMessage(0, 16711680, p,
											"You've been released of your bind to verac!");
							p.setBinded(false);
						}
					}

					for (Player p : getPlayersOn()) {
						if (p.isStunned() && !torag.isHammering) {
							p.setStunDelay(0);
							p.getAppearence().setRenderEmote(-1);
							torag.isHammering = false;
						}
					}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void startBleed() {
		if (fightFinished == true) {
			return;
		}
		for (Player p : getPlayersOn()) {
			if (isInRightArea(p)
					&& guthan.getMostDamageReceivedSourcePlayer() != p) {
				Projectile projectile = World.sendProjectileNew(guthan, p,
						4411, 41, 25, 20, 1, 15, Utils.random(5));
				p.getPackets().sendEntityMessage(0, 16711680, ahrim,
						"<col=ff0000>Guthan Throws his spear at you!");
				guthan.setNextFaceWorldTile(p);
				guthan.setCantInteract(false);
				guthan.setNextNPCTransformation(18542);
				p.setGuthanBleed(true);
				p.lock(3);
				p.setNextAnimation(new Animation(21945));
			}
		}
	}

	public void processRots() {
		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					if (getPlayersCount() == 0) {
						return;
					}

					if (inactiveCount == 6 && portalCollapsed == false) {
						portalCollapse();
						portalCollapsed = true;
						World.spawnObject(new WorldObject(-1, 10, 1,
								getWorldTile(34, 74)));
						World.spawnObject(new WorldObject(-1, 10, 1,
								getWorldTile(29, 74)));
						World.spawnObject(new WorldObject(-1, 10, 1,
								getWorldTile(29, 68)));
						World.spawnObject(new WorldObject(-1, 10, 1,
								getWorldTile(57, 72)));
						fightFinished = true;
						ahrim.finish();
						dharok.finish();
						torag.finish();
						karil.finish();
						guthan.finish();
						verac.finish();
						portalLeft.finish();
						portalRight.finish();
						resetLeftSide();
						resetRightSide();
					}
					processBrothers();
					for (Player p : getPlayersOn()) {
						if (p.getInRots() == false) {
							p.setNextWorldTile(getWorldTile(9, -8));
							p.setInRots(true);
						}
					}

					for (Player p : getPlayersOn()) {
						if (!p.isStunned()
								&& p.getAppearence().getRenderEmote() == 2985) {
							p.getAppearence().setRenderEmote(-1);
						}
					}
					if (pressurePads == false) {
						pressurePads = true;
						processPressurePads();
					}

					for (Player p : getPlayersOn()) {
						if (p.getDisrupted() == true && disrupted == false) {
							p.getTimersManager().sendTimer();
							System.out.println("started");
							disrupted = true;
							fightStarted = true;
							ahrim.setNextForceTalk(new ForceTalk(
									"You dare disturb the shadow!"));
							// disrupt portal remove Option
							World.spawnObject(new WorldObject(-1, 10, 2,
									getWorldTile(48, 82)));
							p.getPackets()
									.sendEntityMessage(0, 16711680, ahrim,
											"<col=ff0000>Ahrim the Blighted: You dare disturb the darkness!");
							// p.setNextWorldTile(getWorldTilePlaneUp(26,-176));
							// World.spawnObject(new WorldObject(1, 10, 1,
							// getWorldTilePlaneUp(26, -176)));--TODO Shadow
							// Realm
						}

						if (!isInLeftArea(p) && fightStarted == true
								&& fightFinished == false
								&& sideSwitchLeft == false) {
							sideSwitchLeft = true;
							p.getPackets()
									.sendEntityMessage(
											1,
											16711680,
											p,
											"<col=ff0000>As there is no one on the other side of the portal, it empowers the Barrows Brothers to destroy everyone!!");
							GameExecutorManager.slowExecutor.schedule(
									new Runnable() {
										@Override
										public void run() {
											torag.setNextWorldTile(getWorldTile(
													52, 87));
											ahrim.setNextWorldTile(getWorldTile(
													52, 84));
											dharok.setNextWorldTile(getWorldTile(
													52, 81));
											// animation-spawn
											dharok.setNextAnimation(new Animation(
													21915));
											ahrim.setNextAnimation(new Animation(
													21915));
											torag.setNextAnimation(new Animation(
													21915));
											// gfx-spawn
											dharok.setNextGraphics(new Graphics(
													4413));
											ahrim.setNextGraphics(new Graphics(
													4413));
											torag.setNextGraphics(new Graphics(
													4413));
										}
									}, 3, TimeUnit.SECONDS);
						}

						if (!isInRightArea(p) && fightStarted == true
								&& fightFinished == false
								&& sideSwitchRight == false) {
							sideSwitchRight = true;
							p.getPackets()
									.sendEntityMessage(
											1,
											16711680,
											p,
											"<col=ff0000>As there is no one on the other side of the portal, it empowers the Barrows Brothers to destroy everyone!!");
							GameExecutorManager.slowExecutor.schedule(
									new Runnable() {
										@Override
										public void run() {
											karil.setNextWorldTile(getWorldTile(
													47, 81));
											guthan.setNextWorldTile(getWorldTile(
													47, 87));
											verac.setNextWorldTile(getWorldTile(
													47, 84));
											// animation-spawn
											karil.setNextAnimation(new Animation(
													21915));
											guthan.setNextAnimation(new Animation(
													21915));
											verac.setNextAnimation(new Animation(
													21915));
											// gfx-spawn
											karil.setNextGraphics(new Graphics(
													4413));
											guthan.setNextGraphics(new Graphics(
													4413));
											verac.setNextGraphics(new Graphics(
													4413));
										}
									}, 3, TimeUnit.SECONDS);
						}

					}
					if (fightStarted == true && tasksStarted == false) {
						System.out.println("tasks");
						rotsPanelTask();
						rotsDamageTask();
						processHits();
						processBrothers();
						tasksStarted = true;
						spawnBrothers();
					}
					if (barIncrement == true) {
						bar++;
						bar++;
					}
					for (Player p : getPlayersOn())
						if (fightFinished == true
								&& p.getX() < getWorldTile(20, 74).getX()
								&& startedCollapse == false) {
							caveBombsTime = Utils.currentTimeMillis() + 30000;
							startedCollapse = true;
							startCollapse();
						}
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 250, TimeUnit.MILLISECONDS);
	}

	public void spawnPortal() {
		if (fightFinished == true) {
			return;
		}
		WorldTile karilTile = new WorldTile(karil.getX(), karil.getY(),
				karil.getPlane());
		WorldTile portalLeftTile = new WorldTile(portalLeft.getX(),
				portalLeft.getY(), portalLeft.getPlane());
		portalUsed = false;
		portalSpawned = true;
		WorldTasksManager.schedule(new WorldTask() {
			int stages = 0;

			@Override
			public void run() {
				if (stages == 1) {
					for (Player p : getPlayers())
						p.getPackets().sendEntityMessage(1, 14338209, p,
								"Karil dashes into the shadows!");
					karil.setNextAnimation(new Animation(21917));
					karil.setNextGraphics(new Graphics(4413));
					karil.setCantInteract(true);
				}
				if (stages == 2) {
					karil.setNextWorldTile(new WorldTile(1, 1, 1));
					portalRight.setNextWorldTile(karilTile);
					portalLeft.setNextWorldTile(getWorldTile(
							Utils.random(38, 43), Utils.random(78, 90)));
				}
				if (stages == 5) {
					if (leftDisabled == false) {
						for (Player p : getPlayers()) {
							if (p.withinDistance(portalLeftTile, 4)) {
								World.sendGraphics(portalLeft, new Graphics(
										4412),
										new WorldTile(portalLeftTile.getX(),
												portalLeftTile.getY(),
												portalLeftTile.getPlane()));
								portalLeft.setNextWorldTile(new WorldTile(0, 0,
										0));
								p.applyHit(new Hit(p, p.withinDistance(
										portalLeftTile, 1) ? Utils.random(5000,
										8000) : p.withinDistance(
										portalLeftTile, 2) ? Utils.random(4000,
										6000) : p.withinDistance(
										portalLeftTile, 3) ? Utils.random(3000,
										5000) : p.withinDistance(
										portalLeftTile, 4) ? Utils.random(2000,
										4000) : 0, HitLook.REGULAR_DAMAGE));
							}
						}
					}
					if (rightDisabled == false) {
						for (Player p : getPlayers()) {
							if (p.withinDistance(karilTile, 4)) {
								World.sendGraphics(portalRight, new Graphics(
										4412), new WorldTile(karilTile.getX(),
										karilTile.getY(), karilTile.getPlane()));
								portalRight.setNextWorldTile(new WorldTile(0,
										0, 0));
								p.applyHit(new Hit(p, Utils.random(4800, 8000),
										HitLook.REGULAR_DAMAGE));
							}
						}
					}
				}
				if (stages == 7) {
					leftDisabled = false;
					rightDisabled = false;
					portalLeft.setNextWorldTile(new WorldTile(0, 0, 0));
					portalRight.setNextWorldTile(new WorldTile(0, 0, 0));
					karil.setCantInteract(false);
					portalSpawned = false;
					karil.setNextAnimation(new Animation(21915));
					karil.setNextGraphics(new Graphics(4413));
					for (Player p : getPlayers()) {
						if (!isInRightArea(p)) {
							karil.setNextWorldTile(getWorldTile(
									Utils.random(38, 43), Utils.random(78, 90)));
						} else
							karil.setNextWorldTile(getWorldTile(
									Utils.random(55, 60), Utils.random(78, 90)));
					}
					finish();
				}

				stages++;
			}

		}, 1, 1);
	}

	public void karilPillar() {
		if (fightFinished == true) {
			return;
		}
		karil.pillarMode = true;
		karilPillarTime = Utils.currentTimeMillis() + 25000;
		for (Player p : getPlayers())
			p.getPackets().sendEntityMessage(1, 14338209, p,
					"Karil has teleported onto the Northern Pillar!");
		karilPillar = true;
		karil.setNextWorldTile(getWorldTile(49, 93));
		if (dharok.getHitpoints() >= 1) {
			karil.setTarget(dharok.getMostDamageReceivedSourcePlayer());
		} else if (torag.getHitpoints() >= 1) {
			karil.setTarget(torag.getMostDamageReceivedSourcePlayer());
		} else if (ahrim.getHitpoints() >= 1) {
			karil.setTarget(ahrim.getMostDamageReceivedSourcePlayer());
		}
	}

	public void sendBombard() {
		if (fightFinished == true) {
			return;
		}
		WorldTile firstBolt = getWorldTile(41, 81);
		WorldTile secondBolt = getWorldTile(41, 85);
		WorldTile thirdBolt = getWorldTile(41, 89);
		bombard = true;
		WorldTasksManager.schedule(new WorldTask() {
			int stages = 0;

			@Override
			public void run() {
				int X = 0;
				int Y = 0;
				if (stages == 1) {
					World.sendGraphics(karil, new Graphics(4410),
							new WorldTile(firstBolt));
				}
				if (stages == 2) {
					for (Player p : getPlayers()) {
						if (p.withinDistance(firstBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}
				if (stages == 3) {
					World.sendGraphics(karil, new Graphics(4410),
							new WorldTile(secondBolt));
					for (Player p : getPlayers()) {
						if (p.withinDistance(firstBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}
				if (stages == 4) {
					for (Player p : getPlayers()) {
						if (p.withinDistance(secondBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}
				if (stages == 5) {
					World.sendGraphics(karil, new Graphics(4410),
							new WorldTile(thirdBolt));
					for (Player p : getPlayers()) {
						if (p.withinDistance(secondBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}
				if (stages == 6) {
					for (Player p : getPlayers()) {
						if (p.withinDistance(thirdBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
				}
				if (stages == 7) {
					for (Player p : getPlayers()) {
						if (p.withinDistance(thirdBolt, 3)) {
							p.applyHit(new Hit(p, Utils.random(1000, 1500),
									HitLook.REGULAR_DAMAGE));
						}
					}
					bombard = false;
					finish();
				}
				stages++;
			}

		}, 1, 1);

	}

	public boolean nearWall1(WorldTile tile) {
		int destXBottom = getWorldTile(2, 4).getX();
		int destYTop = getWorldTile(5, 6).getY();
		int destXTop = getWorldTile(5, 6).getX();
		int destYBottom = getWorldTile(2, 4).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public boolean nearWall2(WorldTile tile) {
		int destXBottom = getWorldTile(8, 8).getX();
		int destYTop = getWorldTile(10, 11).getY();
		int destXTop = getWorldTile(10, 11).getX();
		int destYBottom = getWorldTile(8, 8).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public boolean nearWall3(WorldTile tile) {
		int destXBottom = getWorldTile(4, 20).getX();
		int destYTop = getWorldTile(6, 22).getY();
		int destXTop = getWorldTile(6, 22).getX();
		int destYBottom = getWorldTile(4, 20).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public boolean nearWall4(WorldTile tile) {
		int destXBottom = getWorldTile(10, 34).getX();
		int destYTop = getWorldTile(12, 37).getY();
		int destXTop = getWorldTile(12, 37).getX();
		int destYBottom = getWorldTile(10, 34).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public boolean nearWall5(WorldTile tile) {
		int destXBottom = getWorldTile(2, 39).getX();
		int destYTop = getWorldTile(5, 41).getY();
		int destXTop = getWorldTile(5, 41).getX();
		int destYBottom = getWorldTile(2, 39).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public boolean nearWall6(WorldTile tile) {
		int destXBottom = getWorldTile(11, 54).getX();
		int destYTop = getWorldTile(13, 57).getY();
		int destXTop = getWorldTile(13, 57).getX();
		int destYBottom = getWorldTile(11, 54).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public void startCollapse() {
		for (Player p : getPlayers()) {
			WorldObject obj = World.getObjectWithId(
					new WorldTile(p.getX(), p.getY(), 0), 88067);
			p.getPackets()
					.sendEntityMessage(1, 14338209, p,
							"The portal's movement causes the cave to start collapsing!");
			// Arena Bridges
			World.spawnObject(new WorldObject(88060, 10, 1, getWorldTile(9, 22)));
			World.spawnObject(new WorldObject(88060, 10, 1,
					getWorldTile(10, 42)));
			World.spawnObject(new WorldObject(88060, 10, 1, getWorldTile(5, 69)));
			// Arena Collapse Vents
			World.spawnObject(new WorldObject(88067, 10, 2, getWorldTile(8, 1)));
			World.spawnObject(new WorldObject(88067, 10, 0, getWorldTile(6, 6)));
			World.spawnObject(new WorldObject(88067, 10, 3, getWorldTile(12, 3)));
			World.spawnObject(new WorldObject(88067, 10, 0, getWorldTile(5, 12)));
			World.spawnObject(new WorldObject(88067, 10, 0, getWorldTile(6, 18)));
			World.spawnObject(new WorldObject(88067, 10, 1,
					getWorldTile(12, 26)));
			World.spawnObject(new WorldObject(88067, 10, 3, getWorldTile(7, 29)));
			World.spawnObject(new WorldObject(88067, 10, 0, getWorldTile(8, 33)));
			World.spawnObject(new WorldObject(88067, 10, 2, getWorldTile(5, 38)));
			World.spawnObject(new WorldObject(88067, 10, 2,
					getWorldTile(12, 38)));
			World.spawnObject(new WorldObject(88067, 10, 0, getWorldTile(9, 46)));
			World.spawnObject(new WorldObject(88067, 10, 3, getWorldTile(5, 50)));
			World.spawnObject(new WorldObject(88067, 10, 2, getWorldTile(8, 57)));
			World.spawnObject(new WorldObject(88067, 10, 1, getWorldTile(6, 59)));
			World.spawnObject(new WorldObject(88067, 10, 2, getWorldTile(1, 55)));
			World.spawnObject(new WorldObject(88067, 10, 3, getWorldTile(8, 66)));
			World.spawnObject(new WorldObject(88067, 10, 1, getWorldTile(8, 75)));
			World.spawnObject(new WorldObject(88067, 10, 2,
					getWorldTile(10, 74)));
			// Arena Collapse Agility items
			World.spawnObject(new WorldObject(88065, 10, 0, getWorldTile(8, 42)));
			World.spawnObject(new WorldObject(88063, 10, 0,
					getWorldTile(11, 21)));
			World.spawnObject(new WorldObject(88063, 10, 0, getWorldTile(5, 68)));
			GameExecutorManager.slowExecutor.scheduleWithFixedDelay(
					new Runnable() {
						@Override
						public void run() {
							if (getPlayers().isEmpty()) {
								return;
							}

							if (nearWall1(p) && damaged1 == false) {
								damaged1 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(2, 7),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007, 10, 3,
														new WorldTile(
																getWorldTile(2,
																		7))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}
							if (nearWall2(p) && damaged2 == false) {
								damaged2 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(11, 8),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007, 10, 0,
														new WorldTile(
																getWorldTile(
																		11, 8))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}
							if (nearWall3(p) && damaged3 == false) {
								damaged3 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(4, 23),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007, 10, 3,
														new WorldTile(
																getWorldTile(4,
																		23))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}
							if (nearWall4(p) && damaged4 == false) {
								damaged4 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(13, 34),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007,
														10,
														0,
														new WorldTile(
																getWorldTile(
																		13, 34))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}
							if (nearWall5(p) && damaged5 == false) {
								damaged5 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(2, 42),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007, 10, 3,
														new WorldTile(
																getWorldTile(2,
																		42))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}

							if (nearWall6(p) && damaged6 == false) {
								damaged6 = true;
								final WorldObject object = World
										.getObjectWithId(getWorldTile(14, 54),
												88006);
								World.sendObjectAnimation(p, object,
										new Animation(21905));
								p.applyHit(new Hit(p, Utils.random(300, 500),
										HitLook.REGULAR_DAMAGE));
								GameExecutorManager.slowExecutor.schedule(
										new Runnable() {
											@Override
											public void run() {
												World.spawnObject(new WorldObject(
														88007,
														10,
														0,
														new WorldTile(
																getWorldTile(
																		14, 54))));
											}
										}, 2000, TimeUnit.MILLISECONDS);
							}

						}
					}, 0, 250, TimeUnit.MILLISECONDS);
		}
	}

	public boolean isInLeftArea(WorldTile tile) {
		int destXBottom = getWorldTile(34, 74).getX();
		int destYTop = getWorldTile(48, 95).getY();
		int destXTop = getWorldTile(48, 95).getX();
		int destYBottom = getWorldTile(34, 74).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public void spawnBrothers() {
		lastDash = Utils.currentTimeMillis() + 5000;
		dharok.setNextWorldTile(getWorldTile(47, 81));
		torag.setNextWorldTile(getWorldTile(47, 87));
		ahrim.setNextWorldTile(getWorldTile(47, 84));
		guthan.setNextWorldTile(getWorldTile(52, 87));
		verac.setNextWorldTile(getWorldTile(52, 84));
		karil.setNextWorldTile(getWorldTile(52, 81));
		// animation-spawn
		dharok.setNextAnimation(new Animation(21915));
		ahrim.setNextAnimation(new Animation(21915));
		torag.setNextAnimation(new Animation(21915));
		guthan.setNextAnimation(new Animation(21915));
		verac.setNextAnimation(new Animation(21915));
		karil.setNextAnimation(new Animation(21915));
		// gfx-spawn
		dharok.setNextGraphics(new Graphics(4413));
		ahrim.setNextGraphics(new Graphics(4413));
		torag.setNextGraphics(new Graphics(4413));
		guthan.setNextGraphics(new Graphics(4413));
		verac.setNextGraphics(new Graphics(4413));
		karil.setNextGraphics(new Graphics(4413));
	}

	public boolean isInRightArea(WorldTile tile) {
		// Bottom Left -- (50, 67)
		// Top Right -- (66, 96)
		int destXBottom = getWorldTile(50, 67).getX();
		int destYTop = getWorldTile(66, 96).getY();
		int destXTop = getWorldTile(66, 96).getX();
		int destYBottom = getWorldTile(50, 67).getY();

		for (Player p : getPlayers()) {
			if (p.getX() >= destXBottom && p.getX() <= destXTop
					&& p.getY() >= destYBottom && p.getY() <= destYTop) {
				return true;
			}
		}
		return false;
	}

	public void resetLeftSide() {
		// Left side - (Left Column)
		vent1Left = false;
		vent2Left = false;
		vent3Left = false;
		vent4Left = false;
		vent5Left = false;
		vent6Left = false;
		vent7Left = false;
		vent8Left = false;
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(38, 90)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(38, 86)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(38, 78)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(38, 82)));
		// Left side - (Right Column)
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(43, 90)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(43, 86)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(43, 78)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(43, 82)));
	}

	public void resetRightSide() {
		vent1Right = false;
		vent2Right = false;
		vent3Right = false;
		vent4Right = false;
		vent5Right = false;
		vent6Right = false;
		vent7Right = false;
		vent8Right = false;
		// Right side - (Right Column)
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(60, 90)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(60, 86)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(60, 78)));
		World.spawnObject(new WorldObject(88092, 10, 2, getWorldTile(60, 82)));
		// Left side - (Left Column)
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(55, 90)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(55, 86)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(55, 78)));
		World.spawnObject(new WorldObject(88092, 10, 3, getWorldTile(55, 82)));

		// (48, 82)
	}

	public void ahrimSpecial() {
		ahrim.setNextWorldTile(getWorldTile(40, 85));
		ahrim.setCantInteract(true);
		ahrimSpec = true;
		ahrim.setNextAnimation(new Animation(21909));
		// ahrim.getAppearence().setRenderEmote(2990);
		WorldTasksManager.schedule(new WorldTask() {
			int stages = 0;

			@Override
			public void run() {
				int X = 0;
				int Y = 0;
				if (stages == 1) {
					X = 0;
					Y = 3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextGraphics(new Graphics(4409));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 2) {
					X = 3;
					Y = 3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 3) {
					X = 3;
					Y = 0;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 4) {
					X = 3;
					Y = -3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 5) {
					X = 0;
					Y = -3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 6) {
					X = -3;
					Y = -3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 7) {
					X = -3;
					Y = 0;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 8) {
					X = -3;
					Y = 3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 9) {
					X = 0;
					Y = 3;
					ahrim.setNextAnimation(new Animation(21911));
					ahrim.setNextFaceWorldTile(new WorldTile(ahrim.getX() + X,
							ahrim.getY() + Y, ahrim.getPlane()));
				}
				if (stages == 10) {
					ahrimSpec = false;
					ahrim.setCantInteract(false);
					ahrim.setNextAnimation(new Animation(21910));
					// ahrim.getAppearence().setRenderEmote(-1);
					stop();
				}
				stages++;
			}

		}, 1, 1);
	}

	public void karilSpecial() {
		karil.setCantInteract(true);
		karil.setNextAnimation(new Animation(21909));
		// karil.getAppearence().setRenderEmote(2990);
		WorldTasksManager.schedule(new WorldTask() {
			int stages = 0;

			@Override
			public void run() {
				int X = 0;
				int Y = 0;
				if (stages == 1) {
					karil.setNextAnimation(new Animation(21911));
					karil.setNextGraphics(new Graphics(4408));
					X = 0;
					Y = 3;
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 2) {
					X = 3;
					Y = 3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 3) {
					X = 3;
					Y = 0;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 4) {
					X = 3;
					Y = -3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 5) {
					X = 0;
					Y = -3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 6) {
					X = -3;
					Y = -3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 7) {
					X = -3;
					Y = 0;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 8) {
					X = -3;
					Y = 3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 9) {
					X = 0;
					Y = 3;
					karil.setNextAnimation(new Animation(21911));
					karil.setNextFaceWorldTile(new WorldTile(karil.getX() + X,
							karil.getY() + Y, karil.getPlane()));
				}
				if (stages == 10) {
					karilSpec = false;
					karil.setNextAnimation(new Animation(21910));
					karil.setCantInteract(false);
					// karil.getAppearence().setRenderEmote(-1);
					stop();
				}
				stages++;
			}

		}, 1, 1);
	}

	public void rotsDamageTask() {

		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					if (inactiveCount == 6) {
						return;
					}
					if (getPlayersCount() == 0) {
						return;
					}
					setDamageFromFloorLeft();
					setDamageFromFloorRight();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 700, TimeUnit.MILLISECONDS);

	}

	public void rotsPanelTask() {

		GameExecutorManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					if (inactiveCount == 6) {
						return;
					}
					if (getPlayersCount() == 0) {
						return;
					}
					floorDamageMainAreaLeft();
					floorDamageMainAreaRight();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, 15, TimeUnit.SECONDS);

	}

	public WorldTile npcCheckTileRight() {
		if (vent1RightRed == true) {
			return getWorldTile(60, 90);
		}
		if (vent2RightRed == true) {
			return getWorldTile(60, 86);
		}
		if (vent3RightRed == true) {
			return getWorldTile(60, 78);
		}
		if (vent4RightRed == true) {
			return getWorldTile(60, 82);
		}
		if (vent5RightRed == true) {
			return getWorldTile(55, 90);
		}
		if (vent6RightRed == true) {
			return getWorldTile(55, 86);
		}
		if (vent7RightRed == true) {
			return getWorldTile(55, 78);
		}
		if (vent8RightRed == true) {
			return getWorldTile(55, 82);
		}
		return null;
	}

	public WorldTile playerCheckTileRight() {
		if (valueActiveRightPlatform == 1) {
			return getWorldTile(60, 90);
		}
		if (valueActiveRightPlatform == 2) {
			return getWorldTile(60, 86);
		}
		if (valueActiveRightPlatform == 3) {
			return getWorldTile(60, 78);
		}
		if (valueActiveRightPlatform == 4) {
			return getWorldTile(60, 82);
		}
		if (valueActiveRightPlatform == 5) {
			return getWorldTile(55, 90);
		}
		if (valueActiveRightPlatform == 6) {
			return getWorldTile(55, 86);
		}
		if (valueActiveRightPlatform == 7) {
			return getWorldTile(55, 78);
		}
		if (valueActiveRightPlatform == 8) {
			return getWorldTile(55, 82);
		}
		return null;
	}

	public WorldTile playerCheckTileLeft() {
		if (valueActiveLeftPlatform == 1 || multiVent == true) {
			return getWorldTile(38, 90);
		}
		if (valueActiveLeftPlatform == 2 || multiVent == true) {
			return getWorldTile(38, 86);
		}
		if (valueActiveLeftPlatform == 3 || multiVent == true) {
			return getWorldTile(38, 78);
		}
		if (valueActiveLeftPlatform == 4 || multiVent == true) {
			return getWorldTile(38, 82);
		}
		if (valueActiveLeftPlatform == 5 || multiVent == true) {
			return getWorldTile(43, 90);
		}
		if (valueActiveLeftPlatform == 6 || multiVent == true) {
			return getWorldTile(43, 86);
		}
		if (valueActiveLeftPlatform == 7 || multiVent == true) {
			return getWorldTile(43, 78);
		}
		if (valueActiveLeftPlatform == 8 || multiVent == true) {
			return getWorldTile(43, 82);
		}
		return null;
	}

	public void setDamageFromFloorLeft() {
		for (Player player : getPlayers()) {
			if (player.getX() == playerCheckTileLeft().getX()
					&& player.getY() == playerCheckTileLeft().getY()
					|| player.getX() == playerCheckTileLeft().getX() + 1
					&& player.getY() == playerCheckTileLeft().getY()
					|| player.getX() == playerCheckTileLeft().getX() + 1
					&& player.getY() == playerCheckTileLeft().getY() + 1
					|| player.getX() == playerCheckTileLeft().getX()
					&& player.getY() == playerCheckTileLeft().getY() + 1) {
				player.applyHit(new Hit(player, Utils.random(300, 500),
						HitLook.REGULAR_DAMAGE));
				return;
			}
		}
	}

	public void setDamageFromFloorRight() {
		for (Player player : getPlayers()) {
			if (player.getX() == playerCheckTileRight().getX()
					&& player.getY() == playerCheckTileRight().getY()
					|| player.getX() == playerCheckTileRight().getX() + 1
					&& player.getY() == playerCheckTileRight().getY()
					|| player.getX() == playerCheckTileRight().getX() + 1
					&& player.getY() == playerCheckTileRight().getY() + 1
					|| player.getX() == playerCheckTileRight().getX()
					&& player.getY() == playerCheckTileRight().getY() + 1) {
				player.applyHit(new Hit(player, Utils.random(300, 500),
						HitLook.REGULAR_DAMAGE));
				return;
			}
		}
	}

	public void AhrimTile() {
		if (ahrim.getX() == npcCheckTileRight().getX()
				&& ahrim.getY() == npcCheckTileRight().getY()
				|| ahrim.getX() == npcCheckTileRight().getX() + 1
				&& ahrim.getY() == npcCheckTileRight().getY()
				|| ahrim.getX() == npcCheckTileRight().getX() + 1
				&& ahrim.getY() == npcCheckTileRight().getY() + 1
				|| ahrim.getX() == npcCheckTileRight().getX()
				&& ahrim.getY() == npcCheckTileRight().getY() + 1) {
			ahrim.applyHit(new Hit(ahrim, 500, HitLook.HEALED_DAMAGE));
			ahrim.heal(500);
			return;
		}
	}

	public void DharokTile() {
		if (dharok.getX() == npcCheckTileRight().getX()
				&& dharok.getY() == npcCheckTileRight().getY()
				|| dharok.getX() == npcCheckTileRight().getX() + 1
				&& dharok.getY() == npcCheckTileRight().getY()
				|| dharok.getX() == npcCheckTileRight().getX() + 1
				&& dharok.getY() == npcCheckTileRight().getY() + 1
				|| dharok.getX() == npcCheckTileRight().getX()
				&& dharok.getY() == npcCheckTileRight().getY() + 1) {
			dharok.applyHit(new Hit(dharok, 500, HitLook.HEALED_DAMAGE));
			dharok.heal(500);
			return;
		}
	}

	public void GuthanTile() {
		if (guthan.getX() == npcCheckTileRight().getX()
				&& guthan.getY() == npcCheckTileRight().getY()
				|| guthan.getX() == npcCheckTileRight().getX() + 1
				&& guthan.getY() == npcCheckTileRight().getY()
				|| guthan.getX() == npcCheckTileRight().getX() + 1
				&& guthan.getY() == npcCheckTileRight().getY() + 1
				|| guthan.getX() == npcCheckTileRight().getX()
				&& guthan.getY() == npcCheckTileRight().getY() + 1) {
			guthan.applyHit(new Hit(guthan, 500, HitLook.HEALED_DAMAGE));
			guthan.heal(500);
			return;
		}
	}

	public void VeracTile() {
		if (verac.getX() == npcCheckTileRight().getX()
				&& verac.getY() == npcCheckTileRight().getY()
				|| verac.getX() == npcCheckTileRight().getX() + 1
				&& verac.getY() == npcCheckTileRight().getY()
				|| verac.getX() == npcCheckTileRight().getX() + 1
				&& verac.getY() == npcCheckTileRight().getY() + 1
				|| verac.getX() == npcCheckTileRight().getX()
				&& verac.getY() == npcCheckTileRight().getY() + 1) {
			verac.applyHit(new Hit(verac, 500, HitLook.HEALED_DAMAGE));
			verac.heal(500);
			return;
		}
	}

	public void ToragTile() {
		if (torag.getX() == npcCheckTileRight().getX()
				&& torag.getY() == npcCheckTileRight().getY()
				|| torag.getX() == npcCheckTileRight().getX() + 1
				&& torag.getY() == npcCheckTileRight().getY()
				|| torag.getX() == npcCheckTileRight().getX() + 1
				&& torag.getY() == npcCheckTileRight().getY() + 1
				|| torag.getX() == npcCheckTileRight().getX()
				&& torag.getY() == npcCheckTileRight().getY() + 1) {
			torag.applyHit(new Hit(torag, 500, HitLook.HEALED_DAMAGE));
			torag.heal(500);
			return;
		}
	}

	public void KarilTile() {
		if (karil.getX() == npcCheckTileRight().getX()
				&& karil.getY() == npcCheckTileRight().getY()
				|| karil.getX() == npcCheckTileRight().getX() + 1
				&& karil.getY() == npcCheckTileRight().getY()
				|| karil.getX() == npcCheckTileRight().getX() + 1
				&& karil.getY() == npcCheckTileRight().getY() + 1
				|| karil.getX() == npcCheckTileRight().getX()
				&& karil.getY() == npcCheckTileRight().getY() + 1) {
			karil.applyHit(new Hit(karil, 500, HitLook.HEALED_DAMAGE));
			karil.heal(500);
			return;
		}
	}

	public void floorDamageMainAreaLeft() {
		nextPlatformChangeLeft = Utils.currentTimeMillis()
				+ Utils.random(15000, 25000);
		resetLeftSide();
		int randomiserLeft = Utils.random(1, 8);
		valueActiveLeftPlatform = randomiserLeft;
		if (randomiserLeft == 1) {
			// Left side - (Left Column)
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(38, 90)));
			vent1Left = true;
		}
		if (randomiserLeft == 2) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(38, 86)));
			vent2Left = true;
		}
		if (randomiserLeft == 3) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(38, 78)));
			vent3Left = true;
		}
		if (randomiserLeft == 4) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(38, 82)));
			vent4Left = true;
		}
		if (randomiserLeft == 5) {
			// Left side - (Right Column)
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(43, 90)));
			vent5Left = true;
		}
		if (randomiserLeft == 6) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(43, 86)));
			vent6Left = true;
		}
		if (randomiserLeft == 7) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(43, 78)));
			vent7Left = true;
		}
		if (randomiserLeft == 8) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(43, 82)));
			vent8Left = true;
		}
	}

	public void floorDamageMainAreaRight() {
		nextPlatformChangeRight = Utils.currentTimeMillis()
				+ Utils.random(15000, 25000);
		multiVent = false;
		resetRightSide();
		vent1RightRed = false;
		vent2RightRed = false;
		vent3RightRed = false;
		vent4RightRed = false;
		vent5RightRed = false;
		vent6RightRed = false;
		vent7RightRed = false;
		vent8RightRed = false;
		int randomiserRight = Utils.random(1, 8);
		valueActiveRightPlatform = randomiserRight;
		if (ahrim.getId() == 18539 && multiVent == false) {
			multiVent = true;
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 90)));
			vent1Right = true;
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 86)));
			vent2Right = true;
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 78)));
			vent3Right = true;
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 82)));
			vent4Right = true;
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 90)));
			vent5Right = true;
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 86)));
			vent6Right = true;
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 78)));
			vent7Right = false;
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 82)));
			vent8Right = true;
			if (randomiserRight == 1) {
				vent1RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 90)));
			}
			if (randomiserRight == 2) {
				vent2RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 86)));
			}
			if (randomiserRight == 3) {
				vent3RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 78)));
			}
			if (randomiserRight == 4) {
				vent4RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 82)));
			}
			if (randomiserRight == 5) {
				vent5RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 90)));
			}
			if (randomiserRight == 6) {
				vent6RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 86)));
			}
			if (randomiserRight == 7) {
				vent7RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 78)));
			}
			if (randomiserRight == 8) {
				vent8RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 82)));
			}
		}
		if (Utils.random(25) == 5) {
			if (randomiserRight == 1) {
				vent1RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 90)));
				return;
			}
			if (randomiserRight == 2) {
				vent2RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 86)));
				return;
			}
			if (randomiserRight == 3) {
				vent3RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 78)));
				return;
			}
			if (randomiserRight == 4) {
				vent4RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 2, getWorldTile(
						60, 82)));
				return;
			}
			if (randomiserRight == 5) {
				vent5RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 90)));
				return;
			}
			if (randomiserRight == 6) {
				vent6RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 86)));
				return;
			}
			if (randomiserRight == 7) {
				vent7RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 78)));
				return;
			}
			if (randomiserRight == 8) {
				vent8RightRed = true;
				World.spawnObject(new WorldObject(88094, 10, 3, getWorldTile(
						55, 82)));
				return;
			}
		}
		if (randomiserRight == 1) {
			// Left side - (Right Column)
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 90)));
			vent1Right = true;
		}
		if (randomiserRight == 2) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 86)));
			vent2Right = true;
		}
		if (randomiserRight == 3) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 78)));
			vent3Right = true;
		}
		if (randomiserRight == 4) {
			World.spawnObject(new WorldObject(88093, 10, 2,
					getWorldTile(60, 82)));
			vent4Right = true;
		}
		if (randomiserRight == 5) {
			// Right side - (Left Column)
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 90)));
			vent5Right = true;
		}
		if (randomiserRight == 6) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 86)));
			vent6Right = true;
		}
		if (randomiserRight == 7) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 78)));
			vent7Right = true;
		}
		if (randomiserRight == 8) {
			World.spawnObject(new WorldObject(88093, 10, 3,
					getWorldTile(55, 82)));
			vent8Right = true;
		}

	}

	public void removeMapChunks(final Runnable run) {
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					MapBuilder.destroyMap(boundChunks[0] - 4,
							boundChunks[1] - 4, 32, 32);
					if (run != null)
						run.run();
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 500, TimeUnit.MILLISECONDS);
	}

	public WorldTile getWorldTile(int mapX, int mapY) {
		return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8
				+ mapY, 0);
	}

	public WorldTile getWorldTilePlaneUp(int mapX, int mapY) {
		return new WorldTile(boundChunks[0] * 8 + mapX, boundChunks[1] * 8
				+ mapY, 1);
	}

	@Override
	public void playMusic(Player player) {
		super.playMusic(player);
	}

	@Override
	public void loadMapInstance() {
		boundChunks = MapBuilder.findEmptyChunkBound(sizeX, sizeY);
		MapBuilder.copyAllPlanesMap(chunkX, chunkY, boundChunks[0],
				boundChunks[1], sizeX, sizeY);
		MapBuilder.copyAllPlanesMap(310, 754, boundChunks[0],
				boundChunks[1] + 6, 2, 2);
		MapBuilder.copyAllPlanesMap(287, 736, boundChunks[0] + 2,
				boundChunks[1] + 8, 8, 8);
		MapBuilder.copy2RatioSquare(304, 758, boundChunks[0] + 2,
				boundChunks[1] + 8, 1, 0, 1);
		MapBuilder.copy2RatioSquare(306, 756, boundChunks[0],
				boundChunks[1] + 8, 3, 0, 1);
		MapBuilder.copy2RatioSquare(304, 752, boundChunks[0],
				boundChunks[1] - 2, 2, 0, 1);
		MapBuilder.copy2RatioSquare(310, 752, boundChunks[0] - 2,
				boundChunks[1] + 6, 3, 0, 1);
		resetLeftSide();
		resetRightSide();
		processRots();
	}

	public void portalCollapse() {
		bleed = false;
		for (Player p : getPlayersOn()) {
			p.lock(16);
			p.getAppearence().setRenderEmote(-1);
			p.getPackets().sendCameraLook(
					Cutscene.getX(p, getWorldTile(49, 83).getX()),
					Cutscene.getY(p, getWorldTile(49, 83).getY()), 250);
			p.getPackets().sendCameraPos(
					Cutscene.getX(p, getWorldTile(53, 78).getX()),
					Cutscene.getY(p, getWorldTile(53, 78).getY()), 1500);
		}

		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				for (Player p : getPlayersOn()) {
					p.getPackets().sendCameraShake(3, 12, 25, 12, 25);
					p.getPackets()
							.sendCameraLook(
									Cutscene.getX(p, getWorldTile(50, 79)
											.getX()),
									Cutscene.getY(p, getWorldTile(55, 79)
											.getY()), 1500);
					p.getPackets()
							.sendCameraPos(
									Cutscene.getX(p, getWorldTile(55, 74)
											.getX()),
									Cutscene.getY(p, getWorldTile(55, 74)
											.getY()), 2200);
				}
				World.spawnObject(new WorldObject(88088, 10, 0, getWorldTile(
						48, 91)));
				World.spawnObject(new WorldObject(88088, 10, 2, getWorldTile(
						48, 75)));
				World.spawnObject(new WorldObject(87995, 10, 0, getWorldTile(
						49, 83)));
			}
		}, 3000, TimeUnit.MILLISECONDS);
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.spawnObject(new WorldObject(87996, 10, 1, getWorldTile(
						40, 84)));
				for (Player p : getPlayersOn()) {
					p.getPackets().sendCameraShake(1, 1, 1, 1, 1);
					p.getPackets()
							.sendCameraLook(
									Cutscene.getX(p, getWorldTile(49, 83)
											.getX()),
									Cutscene.getY(p, getWorldTile(49, 83)
											.getY()), 2000);
					p.getPackets()
							.sendCameraPos(
									Cutscene.getX(p, getWorldTile(60, 84)
											.getX()),
									Cutscene.getY(p, getWorldTile(60, 84)
											.getY()), 3000);
				}
			}
		}, 5000, TimeUnit.MILLISECONDS);
		// chasm-jumpable
		GameExecutorManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				World.spawnObject(new WorldObject(88091, 10, 0, getWorldTile(
						48, 78)));
				// north frame after
				World.spawnObject(new WorldObject(88089, 10, 0, getWorldTile(
						48, 91)));
				// south frame after
				World.spawnObject(new WorldObject(88089, 10, 2, getWorldTile(
						48, 75)));
				// portal removed
				World.spawnObject(new WorldObject(-1, 10, 0, getWorldTile(49,
						83)));
				for (Player p : getPlayersOn()) {
					p.getPackets().sendResetCamera();
				}
			}
		}, 10000, TimeUnit.MILLISECONDS);

	}

}
