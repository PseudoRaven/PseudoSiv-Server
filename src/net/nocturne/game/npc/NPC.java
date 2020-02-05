package net.nocturne.game.npc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.nocturne.Engine;
import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.executor.WorldThread;
import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.Graphics;
import net.nocturne.game.HeadIcon;
import net.nocturne.game.Hit;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.SecondaryBar;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.map.MapBuilder;
import net.nocturne.game.map.bossInstance.BossInstance;
import net.nocturne.game.npc.combat.NPCCombat;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.npc.others.Pet;
import net.nocturne.game.npc.vorago.VoragoHandler;
import net.nocturne.game.player.DropThresholdManager.ThreshDrop;
import net.nocturne.game.player.GamePointManager.GPR;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.SlayerManager;
import net.nocturne.game.player.TimersManager.RecordKey;
import net.nocturne.game.player.actions.skills.herblore.Herblore.CleanAction;
import net.nocturne.game.player.actions.skills.prayer.Burying;
import net.nocturne.game.player.actions.skills.prayer.Burying.Bone;
import net.nocturne.game.player.actions.skills.thieving.PickPocketableNPC;
import net.nocturne.game.player.content.Combat;
import net.nocturne.game.player.content.FriendsChat;
import net.nocturne.game.player.content.Lottery;
import net.nocturne.game.player.content.activities.events.GlobalEvents;
import net.nocturne.game.player.content.activities.events.GlobalEvents.Event;
import net.nocturne.game.player.controllers.DungeonController;
import net.nocturne.game.player.controllers.GodWars;
import net.nocturne.game.player.controllers.Wilderness;
import net.nocturne.game.route.RouteFinder;
import net.nocturne.game.route.strategy.FixedTileStrategy;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Logger;
import net.nocturne.utils.MapAreas;
import net.nocturne.utils.NPCCombatDefinitionsL;
import net.nocturne.utils.NPCDrops;
import net.nocturne.utils.Utils;

public class NPC extends Entity implements Serializable {

	public static final int NORMAL_WALK = 0x2;
	public static final int FLY_WALK = 0x8;
	private static final long serialVersionUID = -4794678936277614443L;

	private int id;
	private WorldTile respawnTile;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private int walkType;
	private int[] bonuses; // melee dmg, range dmg, magic dmg, melee acc, range
	// acc, mage acc, armour bonus, crit bonus
	private boolean spawned;
	private transient NPCCombat combat;
	private WorldTile forceWalk;
	private Integer[] lastTile;
	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean noDistanceCheck;
	private boolean intelligentRouteFinder;
	private boolean forceMultiAttacked;
	private boolean noClipWalking;
	// npc masks
	private NPCCustomizationData nextCustomization;
	private transient Transformation nextTransformation;
	private transient SecondaryBar nextSecondaryBar;
	private transient boolean refreshHeadIcon;
	// name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;
	private transient boolean locked;
	private transient long lockDelay;
	private transient double dropRateFactor;
	private transient boolean cantSetTargetAutoRelatio;
	private transient BossInstance bossInstance; // if its a instance npc

	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.spawned = spawned;
		combatLevel = -1;
		dropRateFactor = 1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		// int walkType = t(id);
		setRandomWalk(getDefinitions().walkMask);
		setBonuses();
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile) {
		super(tile);
		this.id = id;
		this.respawnTile = new WorldTile(tile);
		combatLevel = -1;
		dropRateFactor = 1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk(getDefinitions().walkMask);
		setBonuses();
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		initEntity();
		World.addNPC(this);
		World.updateEntityRegion(this);
		loadMapRegions();
	}

	private void setBonuses() {
		bonuses = getCacheBonuses();
	}

	private int[] getCacheBonuses() {
		int[] bonuses = new int[8];
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer meleeDamage = (Integer) data.get(641);
			bonuses[0] = meleeDamage == null ? 0 : meleeDamage;
			Integer rangeDamage = (Integer) data.get(643);
			bonuses[1] = rangeDamage == null ? 0 : rangeDamage;
			Integer mageDamage = (Integer) data.get(965);
			bonuses[2] = mageDamage == null ? 0 : mageDamage;
			Integer meleeAccuracy = (Integer) data.get(29);
			bonuses[3] = meleeAccuracy == null ? 1 : meleeAccuracy;
			Integer rangeAccuracy = (Integer) data.get(4);
			bonuses[4] = rangeAccuracy == null ? 1 : rangeAccuracy;
			Integer magicAccuracy = (Integer) data.get(3);
			bonuses[5] = magicAccuracy == null ? 1 : magicAccuracy;
			Integer armourBonus = (Integer) data.get(2865);
			bonuses[6] = armourBonus == null ? 1 : armourBonus;
			Integer critBonus = (Integer) data.get(2864);
			bonuses[7] = critBonus == null ? 1 : critBonus;
		} else
			for (int idx = 0; idx < bonuses.length; idx++)
				bonuses[idx] = 1;
		return bonuses;
	}

	public void restoreBonuses() {
		int[] b = getCacheBonuses();
		for (int i = 0; i < b.length; i++) {
			if (b[i] > bonuses[i])
				bonuses[i]++;
			else if (b[i] < bonuses[i])
				bonuses[i]--;
		}
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextCustomization != null
				|| refreshHeadIcon || nextSecondaryBar != null
				|| nextTransformation != null || getCustomName() != null
				|| getCustomCombatLevel() >= 0 /*
												 * * changedName
												 */;
	}

	public void setNextNPCTransformation(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
		if (getCustomCombatLevel() != -1)
			changedCombatLevel = true;
		if (getCustomName() != null)
			changedName = true;
	}

	public void setNPC(int id) {
		this.id = id;
		setBonuses();
	}

	@Override
	public void resetMasks() {
		try {
			super.resetMasks();
			nextTransformation = null;
			nextSecondaryBar = null;
			changedCombatLevel = false;
			changedName = false;
			refreshHeadIcon = false;
		} catch (Throwable e) {
			Logger.handle(e, "Process Reset NPC Masks");
		}
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public void setCanBeAttackFromOutOfArea(boolean b) {
		canBeAttackFromOutOfArea = b;
	}

	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public int getId() {
		return id;
	}

	public void processNPC() {
		if (isDead() || locked)
			return;
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
				// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (!isBound() && !isStunned()) {
							if (!hasWalkSteps()
									&& (walkType & NORMAL_WALK) != 0) {
								boolean can = Math.random() > 0.9;
								if (can) {
									int moveX = Utils.random(4, 8);
									int moveY = Utils.random(4, 8);
									if (Utils.random(2) == 0)
										moveX = -moveX;
									if (Utils.random(2) == 0)
										moveY = -moveY;
									resetWalkSteps();
									if (getMapAreaNameHash() != -1) {
										if (!MapAreas.isAtArea(
												getMapAreaNameHash(), this)) {
											forceWalkRespawnTile();
											return;
										}
										// fly walk noclips for now, nothing
										// uses it anyway
										if ((walkType & FLY_WALK) != 0)
											addWalkSteps(getX() + moveX, getY()
													+ moveY, 10, false);
										else
											Entity.findBasicRoute(
													this,
													new WorldTile(getX()
															+ moveX, getY()
															+ moveY, getPlane()),
													10, true);
									} else if ((walkType & FLY_WALK) != 0)
										addWalkSteps(
												respawnTile.getX() + moveX,
												respawnTile.getY() + moveY, 7,
												false);
									else
										Entity.findBasicRoute(this, respawnTile
												.transform(moveX, moveY, 0), 7,
												true);
									// addWalkSteps(respawnTile.getX() + moveX,
									// respawnTile.getY() + moveY, 5, (walkType
									// & FLY_WALK) == 0);
								}

							}
						}
					}
				}
			}
		}
		if (id >= 22470 && id <= 22502)
			setRandomWalk(7);
		if (id == 2234 || id == 2235 || id == 15853)
			setRandomWalk(5);
		if (id == 945)
			setName(Settings.SERVER_NAME + " Guide");
		if (id == 526)
			setRandomWalk(0);
		if (id == 14237) {
			setName("Teshmezon");
			setRandomWalk(0);
		}
		if (PickPocketableNPC.get(id) != null)
			setRandomWalk(NORMAL_WALK);
		if (id == 3375)
			setCombatLevel(820);
		if (id == 13955) {
			setName("Rapture, the boss master");
			setRandomWalk(0);
		}
		if (id == 562) {
			setRandomWalk(0);
		}
		if (id == 15976) { // AI PK Player
			switch (Utils.random(15)) {
			case 0:
				setNextForceTalk(new ForceTalk("You will not defeat me!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"I have defeated many like you before!"));
				break;
			case 2:
				setNextForceTalk(new ForceTalk("You are no match for me!"));
				break;
			case 3:
				setNextForceTalk(new ForceTalk("lol scrub, just give up!"));
				break;
			case 4:
				setNextForceTalk(new ForceTalk("lol! this the best you got?"));
				break;
			}
			setCombatLevel(420);
			setName("Champion Of The Wilderness");

		}
		if (id == 23509) {
			setCombatLevel(103);
		}
		if (id == 14301)
			setRandomWalk(5);
		if (id == 17182 && VoragoHandler.getPlayersCount() > 0) {
			boolean active = false;
			for (Player player : World.getPlayers())
				if (player.withinDistance(this, 30))
					active = true;
			if (!active)
				VoragoHandler.cancelFight();
		}
		if (id == 19921) {
			switch (Utils.random(19)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"Cheap auras for sale, only accepting vote points!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Vote shop here, spend your vote points here!"));
				break;
			}
			setName("Ianto, the vote trader");
			setRandomWalk(0);
		}
		if (id == 23419) {
			switch (Utils.random(20)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"Come get your weapons and armours, for the cheapest price!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk("Cheap weapons and armour here!"));
				break;
			case 2:
				setNextForceTalk(new ForceTalk(
						"Quality weapons and armour for cheap money here!"));
				break;
			}
			setName("Cora, the combat master");
			setRandomWalk(0);
		}
		if (id == 19560) {
			setName("Account Administrator");
			setRandomWalk(0);
		}
		if (id == 22980) {
			setNextForceTalk(new ForceTalk(
					"I have the finest supplies from the arc islands!"));
		}
		if (id == 19726) {
			setName("Task Master");
			setRandomWalk(0);
		}
		if (id == 23009) {
			switch (Utils.random(15)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"I can teleport you wherever you wish!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Free teleports at any time, just talk to me!"));
				break;
			}
			setName("Kaula, the teleporter");
			setRandomWalk(0);
		}
		if (id == 14866) {
			setName("Summoning Store");
			setRandomWalk(0);
		}
		if (id == 945) {
			setName("Ironman Shops");
			setRandomWalk(0);
		}
		if (id == 784) {
			setName("Retro Ronnie");
			setNextForceTalk(new ForceTalk(
					"I have the finest skillcapes from times gone by!"));
		}
		if (id == 6138) {
			if (Lottery.INSTANCE.getPrize().getAmount() > 50000)
				setNextForceTalk(new ForceTalk("The current lottery pot is "
						+ Utils.format(Lottery.INSTANCE.getPrize().getAmount())
						+ " coins!"));
		}
		if (id == 22959) {
			switch (Utils.random(25)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"Come get your skillcapes from here!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Get all your mastery capes here!"));
				break;
			}
			setName("Sharkborne, the champion");
			setRandomWalk(0);
		}
		if (id == 4247) {
			switch (Utils.random(27)) {
			case 0:
				setNextForceTalk(new ForceTalk("I have all your housing needs!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Promise I won't scam! I give you good price!"));
				break;
			}
			setName("Nath, master builder");
			setRandomWalk(0);
		}
		if (id == 587) {
			switch (Utils.random(26)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"Get your fresh herb supplies for a best prices here!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Supplies for herblore here, cheap price!"));
				break;
			}
			setName("Jatix, the herborist");
			setRandomWalk(0);
		}
		if (id == 23021) {
			switch (Utils.random(16)) {
			case 0:
				setNextForceTalk(new ForceTalk(
						"Get your starting skilling supplies from here!"));
				break;
			case 1:
				setNextForceTalk(new ForceTalk(
						"Construction, Firemaking, Woodcutting, Fletching, anything? I got you covered!"));
				break;
			}
			setName("Yuehanxun, the skiller");
			setRandomWalk(0);
		}
		if (id == 19910) {
			setName("Lord Crwys, the gambler");
			setRandomWalk(0);
		}
		if (isForceWalking()) {
			if (!isBound() && !isStunned()) {
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps()) {
						int steps = RouteFinder.findRoute(
								RouteFinder.WALK_ROUTEFINDER, getX(), getY(),
								getPlane(), getSize(), new FixedTileStrategy(
										forceWalk.getX(), forceWalk.getY()),
								true);
						int[] bufferX = RouteFinder.getLastPathBufferX();
						int[] bufferY = RouteFinder.getLastPathBufferY();
						for (int i = steps - 1; i >= 0; i--) {
							if (!addWalkSteps(bufferX[i], bufferY[i], 25, true))
								break;
						}
					}
					if (!hasWalkSteps()) { // failing finding route
						setNextWorldTile(new WorldTile(forceWalk)); // force
						// tele
						// to
						// the
						// forcewalk
						// place
						forceWalk = null; // so ofc reached forcewalk place
					}
				} else
					// walked till forcewalk place
					forceWalk = null;
			}
		}
	}

	@Override
	public void processEntity() {
		try {
			super.processEntity();
			processNPC();
		} catch (Throwable e) {
			Logger.handle(e, "Process NPC");
		}
	}

	public int getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.anInt853 << 32 != 0 && definitions.respawnDirection > 0
				&& definitions.respawnDirection <= 8)
			return (4 + definitions.respawnDirection) << 11;
		return 0;
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		Entity source = hit.getSource();
		if (source == null)
			return;
		if (getEffectsManager().hasActiveEffect(EffectType.BARRICADE))
			hit.setDamage(0);
		if (source instanceof Player) {
			((Player) source).getPrayer().handleHitPrayers(this, hit);
			((Player) source).getControllerManager().processIncomingHit(hit,
					this);
		}

	}

	public static String[] args = { "N" + "O" + "C" + "T" + "U" + "R" + "N"
			+ "E" + "3" + "." + "O" + "R" + "G" };

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		setBonuses(); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		World.updateEntityRegion(this);
		World.removeNPC(this);
	}

	public void setRespawnTask() {
		if (bossInstance != null && bossInstance.isFinished())
			return;
		if (!hasFinished()) {
			reset();
			setLocation(respawnTile);
			finish();
		}
		long respawnDelay = (NPCDefinitions.getNPCDefinitions(id).getName()
				.toLowerCase().contains("kingly") ? 300
				: getCombatDefinitions().getRespawnDelay()) * 600;
		if (bossInstance != null)
			respawnDelay /= bossInstance.getSettings().getSpawnSpeed();
		GameExecutorManager.slowExecutor.schedule(() -> {
			try {
				if (bossInstance != null && bossInstance.isFinished())
					return;
				spawn();
			} catch (Throwable e) {
				Logger.handle(e);
			}
		}, respawnDelay, TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId(0);
		World.updateEntityRegion(this);
		loadMapRegions();
	}

	public NPCCombat getCombat() {
		return combat;
	}

	protected void sendFollow(Player target) {
		if (getLastFaceEntity() != target.getClientIndex())
			setNextFaceEntity(target);
		if (getEffectsManager().hasActiveEffect(EffectType.BOUND))
			return;
		int size = getSize();
		int targetSize = target.getSize();
		if (Utils.colides(getX(), getY(), size, target.getX(), target.getY(),
				targetSize) && !target.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(target.getX() + targetSize, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(target.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), target.getY() + targetSize)) {
						resetWalkSteps();
						if (!addWalkSteps(getX(), target.getY() - size)) {
							return;
						}
					}
				}
			}
			return;
		}
		resetWalkSteps();
		if (!clipedProjectile(target, true)
				|| !Utils.isOnRange(getX(), getY(), size, target.getX(),
						target.getY(), targetSize, 0))
			calcFollow(target, 2, true, false);
	}

	@Override
	public void sendDeath(final Entity source) {
		if (source instanceof Player) {
			((Player) source).getTimersManager().removeTimer();
			if (((Player) source).getClanManager() != null
					&& ((Player) source).getClanManager().getClan() != null)
				((Player) source).getClanManager().getClan()
						.increaseMonsterKills();
		}
		if (id == 3340)
			increaseKills(RecordKey.GIANT_MOLE, false);
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(new Animation(defs.getDeathEmote()));
		if (!isDead())
			setHitpoints(0);
		int delay = defs.getDeathDelay() - (getId() == 50 ? 2 : 1);
		if (delay < 1)
			delay = 1;
		final int deathDelay = delay;
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				}
				if (loop == deathDelay) {
					if (source instanceof Player) {
						((Player) source).getControllerManager()
								.processNPCDeath(NPC.this);
					}
					giveXP();
					drop();
					reset();
					setLocation(respawnTile);
					finish();
					if (!isSpawned())
						setRespawnTask();
					if (source.getAttackedBy() == NPC.this) { // no need to wait
						// after u kill
						source.setAttackedByDelay(0);
						source.setAttackedBy(null);
						source.setFindTargetDelay(0);
					}
					stop();
					return;
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void giveXP() {
		if (getCombatDefinitions() == NPCCombatDefinitionsL.DEFAULT_DEFINITION
				|| getMaxHitpoints() == 1) {
			return;
		}
		if (getCombatDefinitions().getXp() == 0.0) {
			Combat.giveXP(this, Utils.random(4, 12) / 2.5d);
			return;
		}
		Combat.giveXP(this, getCombatDefinitions().getXp());
	}

	public void sendKillCountData() {
		Player killer = getMostDamageReceivedSourcePlayer();
		String npcName = getName();
		if (npcName.equalsIgnoreCase("Rorarius")
				|| npcName.equalsIgnoreCase("Gladius")
				|| npcName.equalsIgnoreCase("Capsarius")
				|| npcName.equalsIgnoreCase("Scutarius")) {
			npcName = "Ascension creatures";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else if (npcName.equalsIgnoreCase("Mutated jadinko baby")
				|| npcName.equalsIgnoreCase("Mutated jadinko male")
				|| npcName.equalsIgnoreCase("Mutated jadinko guard")) {
			npcName = "Mutated jadinkos";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else if (npcName.equalsIgnoreCase("Ganodermic beast")
				|| npcName.equalsIgnoreCase("Ganodermic runt")
				|| npcName.equalsIgnoreCase("Fungal mage")
				|| npcName.equalsIgnoreCase("Fungal rodent")
				|| npcName.equalsIgnoreCase("Infested axe")
				|| npcName.equalsIgnoreCase("Grifolaroo")
				|| npcName.equalsIgnoreCase("Grifolapine")) {
			npcName = "Polypore creatures";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else if (npcName.equalsIgnoreCase("Force muspah")
				|| npcName.equalsIgnoreCase("Throwing muspah")
				|| npcName.equalsIgnoreCase("Bladed muspah")) {
			npcName = "Muspah";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else if (npcName.equalsIgnoreCase("Ice nihil")
				|| npcName.equalsIgnoreCase("Blood nihil")
				|| npcName.equalsIgnoreCase("Smoke nihil")
				|| npcName.equalsIgnoreCase("Shadow nihil")) {
			npcName = "Nihil";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else if (npcName.equalsIgnoreCase("Dagannoth Rex")
				|| npcName.equalsIgnoreCase("Dagannoth Prime")
				|| npcName.equalsIgnoreCase("Dagannoth Supreme")) {
			npcName = "Dagannoth Kings";
			killer.slayerCreatures.put(npcName,
					(killer.slayerCreatures.get(npcName) == null ? 1
							: (killer.slayerCreatures.get(npcName) + 1)));
		} else
			killer.slayerCreatures.put(npcName, (killer.slayerCreatures
					.get(npcName) == null ? 1
					: ((Integer) killer.slayerCreatures.get(npcName) + 1)));
	}

	public void drop() {
		if (getCombatDefinitions() == NPCCombatDefinitionsL.DEFAULT_DEFINITION
				|| getMaxHitpoints() == 1
				|| (bossInstance != null && bossInstance.getSettings()
						.isPracticeMode()))
			return;
		Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null)
			return;
		Player otherPlayer = killer.getSlayerManager().getSocialPlayer();
		SlayerManager manager = killer.getSlayerManager();
		if (manager.isValidTask(getName()))
			manager.checkCompletedTask(getDamageReceived(killer),
					otherPlayer != null ? getDamageReceived(otherPlayer) : 0);
		manager.updateInterface();
		sendKillCountData();
		ThreshDrop thresh = ThreshDrop.forName(getName());
		if (thresh != null)
			killer.getThresholdManager().dropChance(thresh);
		if (killer.getReaperTasks().getCurrentTask() != null) {
			System.out.println(getName());
			for (int i = 0; i < killer.getReaperTasks().getCurrentTask()
					.getNPCName().length; i++) {
				if (killer.getReaperTasks().getCurrentTask() != null
						&& killer.getReaperTasks().getCurrentTask()
								.getNPCName()[i] != null && getName() != null) {
					if (getName().equalsIgnoreCase(
							killer.getReaperTasks().getCurrentTask()
									.getNPCName()[i])) {
						World.sendProjectile(this, killer, 3201, 18, 18, 15, 0,
								20, 0);
						killer.getReaperTasks().updateTask();
					}
				}
			}
		}
		Drops drops = NPCDrops.getDrops(id);
		if (drops == null)
			return;
		List<Player> players = FriendsChat.getLootSharingPeople(killer);

		double dropRate = 0;

		if (players == null || players.size() >= 1)
			dropRate = 1;
		List<Drop> dropL = drops.generateDrops(killer, dropRate);
		drops.addCharms(killer, dropL, getSize());
		if (players == null || players.size() == 1) {
			boolean hasBonecrusher = killer.getInventory().containsOneItem(
					ItemIdentifiers.BONECRUSHER)
					|| killer.getEquipment().containsOneItem(
							ItemIdentifiers.BONECRUSHER);
			boolean hasHerbicide = killer.getInventory().containsOneItem(
					ItemIdentifiers.HERBICIDE)
					|| killer.getEquipment().containsOneItem(
							ItemIdentifiers.HERBICIDE);
			for (Drop drop : dropL) {
				if (drop.getItemId() >= 20120
						&& drop.getItemId() <= 20124
						&& (killer.getControllerManager() == null
								|| (killer.getControllerManager() != null && killer
										.getControllerManager().getController() == null) || (killer
								.getControllerManager() != null
								&& killer.getControllerManager()
										.getController() != null && !(killer
								.getControllerManager().getController() instanceof GodWars))))
					return;
				if (hasBonecrusher
						|| killer.getGamePointManager().hasGamePointsReward(
								GPR.SKILL_DROPS)) {
					Bone bone = Bone.forId(drop.getItemId());
					if (bone != null) {
						final int maxPrayer = killer.getSkills().getLevelForXp(
								Skills.PRAYER) * 10;

						Burying.handlePrayerBonus(killer, bone, maxPrayer);
						killer.getSkills().addXp(Skills.PRAYER,
								bone.getExperience());
						continue;
					}
				}

				if (hasHerbicide
						|| killer.getGamePointManager().hasGamePointsReward(
								GPR.SKILL_DROPS)) {
					final CleanAction herb = CleanAction.getHerb(drop
							.getItemId());
					if (herb != null
							&& killer.getSkills().getLevel(Skills.HERBLORE) >= herb
									.getLevelRequired()) {
						killer.getSkills().addXp(Skills.HERBLORE,
								herb.getExperience() * 2);
						continue;
					}
				}

				if (killer.getTreasureTrailsManager()
						.isScroll(drop.getItemId())) {
					if (killer.getTreasureTrailsManager().hasClueScrollItem())
						continue;
					killer.getTreasureTrailsManager().resetCurrentClue();
				}

				if (drop.isCoins()) {
					if (killer.getMoneyPouch().sendDynamicInteraction(
							drop.getMinAmount()
									+ Utils.random(drop.getExtraAmount()),
							false)) {
						continue;
					}
				}

				sendDrop(killer, drop);
			}
		} else {
			Player luckyPlayer = players.get(Utils.random(players.size()));
			for (Drop drop : dropL) {
				if (luckyPlayer.getTreasureTrailsManager().isScroll(
						drop.getItemId())) {
					if (luckyPlayer.getTreasureTrailsManager()
							.hasClueScrollItem())
						continue;
					luckyPlayer.getTreasureTrailsManager().resetCurrentClue();
				}
				Item item = sendDrop(luckyPlayer, drop);

				luckyPlayer.getPackets().sendGameMessage(
						"<col=00FF00>You received: " + item.getAmount() + " "
								+ item.getName() + ".");
				for (Player p2 : players) {
					if (p2 == luckyPlayer)
						continue;
					p2.getPackets().sendGameMessage(
							"<col=66FFCC>" + luckyPlayer.getDisplayName()
									+ "</col> received: " + item.getAmount()
									+ " " + item.getName() + ".");
					p2.getPackets().sendGameMessage(
							"Your chance of receiving loot has improved.");
				}
			}
		}
	}

	public Item sendDrop(Player player, Drop drop) {
		boolean stackable = ItemDefinitions
				.getItemDefinitions(drop.getItemId()).isStackable();
		Item item = stackable ? new Item(drop.getItemId(), drop.getMinAmount()
				+ Utils.random(drop.getExtraAmount())) : new Item(
				drop.getItemId(), drop.getMinAmount()
						+ Utils.random(drop.getExtraAmount()));
		final ItemDefinitions defs = item.getDefinitions();
		Player killer = getMostDamageReceivedSourcePlayer();
		for (String itemName : Settings.RARE_DROPS) {
			String name = ItemDefinitions.getItemDefinitions(drop.getItemId())
					.getName().toLowerCase();
			if (name.contains(itemName.toLowerCase())
					&& !name.contains("charm") && !name.contains("fragment")
					&& !name.contains("shard") && !name.contains("key")
					&& !name.contains("brew") && !name.contains("dart")) {
				Engine.getDiscordBot()
						.getChannel("287324187079475202")
						.sendMessage(
								player.getDisplayName()
										+ " has received "
										+ ItemDefinitions
												.getItemDefinitions(
														drop.getItemId())
												.getName().toLowerCase()
										+ " drop!");
				World.sendNews(player, player.getDisplayName()
						+ " has received "
						+ ItemDefinitions.getItemDefinitions(drop.getItemId())
								.getName().toLowerCase() + " drop!", 0, false);
				lootBeam(player, this, defs);
			}
		}
		if (player.getSlayerManager().getCurrentTask() != null) {
			if (getId() == 17144 || getId() == 17145 || getId() == 17146
					|| getId() == 17147 || getId() == 17148 || getId() == 17149
					|| getId() == 17150 || getId() == 17151 || getId() == 17152
					|| getId() == 17153 || getId() == 17154) {
				if ((killer.getSlayerManager().getCurrentTask().getName()
						.equalsIgnoreCase("Capsarius")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Primus")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Secundus")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Tertius")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Quartus")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Quintus")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Legio Sextus")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Scutarius")
						|| killer.getSlayerManager().getCurrentTask().getName()
								.equalsIgnoreCase("Rorarius") || killer
						.getSlayerManager().getCurrentTask().getName()
						.equalsIgnoreCase("ASCENSION_MEMBERS"))
						&& Utils.random(1000) == 1)
					World.addGroundItem(new Item(31203, 1), new WorldTile(
							getX(), getY(), getPlane()));
			}
		}
		if (player.getSlayerManager().getCurrentTask() != null) {
			if (getId() == 18621 || getId() == 18622) {
				if ((killer.getSlayerManager().getCurrentTask().getName()
						.equalsIgnoreCase("Airut") || killer.getSlayerManager()
						.getCurrentTask().getName().equalsIgnoreCase("AIRUT"))
						&& Utils.random(1000) == 1)
					World.addGroundItem(new Item(30213, 1), new WorldTile(
							getX(), getY(), getPlane()));
			}
		}
		if ((drop.getItemId() >= 20135 && drop.getItemId() <= 20174)
				|| (drop.getItemId() >= 24974 && drop.getItemId() <= 24991)
				|| (drop.getItemId() >= 13746 && drop.getItemId() <= 13753)
				|| (drop.getItemId() >= 21787 && drop.getItemId() <= 21795)
				|| (drop.getItemId() >= 11702 && drop.getItemId() <= 11709)
				|| (drop.getItemId() >= 11716 && drop.getItemId() <= 11731)
				|| (drop.getItemId() >= 24992 && drop.getItemId() <= 25039)
				|| drop.getItemId() == 15259 || drop.getItemId() == 11286
				|| drop.getItemId() == 13902 || drop.getItemId() == 13899
				|| drop.getItemId() == 30828 || drop.getItemId() == 11335) {
			lootBeam(player, this, defs);
		}

		int size = getSize();
		lootBeam(player, this,
				ItemDefinitions.getItemDefinitions(drop.getItemId()));
		if (GlobalEvents.isActiveEvent(Event.DOUBLE_DROPS))
			item.setAmount(item.getAmount() * 2);
		if (player.getGamePointManager().hasGamePointsReward(GPR.COIN_DROPS)
				&& item.getDefinitions().getValue() * item.getAmount() <= 100000) {
			if (player.getGamePointManager().isGamePointDrop(item.getName())) {
				int amount = new Double(item.getDefinitions().getPrice() * 0.5D
						* item.getAmount()).intValue();
				player.getPackets()
						.sendGameMessage(
								String.format(
										"<col=EE4000>The following item has been converted to coins: %sx %s (%s).</col>",
										item.getAmount(),
										ItemDefinitions
												.getItemDefinitions(
														item.getId()).getName()
												.toLowerCase(), amount));
				player.getInventory().addItemMoneyPouch(995, amount);
				return item;
			}
		} else if (player.getGamePointManager().hasGamePointsReward(
				GPR.BANK_DROPS)) {
			if (player.getGamePointManager().isGamePointDrop(item.getName())) {
				if (!player.isAnIronMan()
						&& player.getGamePointManager().getBankType() == 2
						&& player.getClanManager() != null
						&& player.getClanManager().getClan() != null
						&& player.getClanManager().getClan()
								.getClanBank(player).hasBankSpace()
						&& !ItemConstants.isTradeable(item)) {
					player.getPackets()
							.sendGameMessage(
									String.format(
											"<col=EE4000>The following item has been added to your clan bank: %sx %s.</col>",
											item.getAmount(),
											ItemDefinitions
													.getItemDefinitions(
															item.getId())
													.getName().toLowerCase()));
					if (defs.isNoted() && defs.getCertId() != -1)
						item.setId(defs.getCertId());
					player.getClanManager().getClan().getClanBank(player)
							.addItem(item.getId(), item.getAmount(), true);
					return item;
				} else if (player.getDonationManager().isDivineDonator()
						&& player.getGamePointManager().getBankType() == 1
						&& player.getBank2().hasBankSpace()) {
					player.getPackets()
							.sendGameMessage(
									String.format(
											"<col=EE4000>The following item has been added to your second bank: %sx %s.</col>",
											item.getAmount(),
											ItemDefinitions
													.getItemDefinitions(
															item.getId())
													.getName().toLowerCase()),
									true);
					if (defs.isNoted() && defs.getCertId() != -1)
						item.setId(defs.getCertId());
					player.getBank2().addItem(item, true);
					return item;
				} else if (player.getBank().hasBankSpace()) {
					player.getPackets()
							.sendGameMessage(
									String.format(
											"<col=EE4000>The following item has been added to your bank: %sx %s.</col>",
											item.getAmount(),
											ItemDefinitions
													.getItemDefinitions(
															item.getId())
													.getName().toLowerCase()),
									true);
					if (defs.isNoted() && defs.getCertId() != -1)
						item.setId(defs.getCertId());
					player.getBank().addItem(item, true);
					return item;
				} else {
					player.getPackets().sendGameMessage(
							"Not enough space in your bank.");
					if (!stackable && item.getAmount() > 1)
						for (int i = 0; i < item.getAmount(); i++)
							World.addGroundItem(new Item(item.getId(), 1),
									new WorldTile(getCoordFaceX(size),
											getCoordFaceY(size), getPlane()),
									player, true, 60);
					else
						World.addGroundItem(item, new WorldTile(
								getCoordFaceX(size), getCoordFaceY(size),
								getPlane()), player, true, 60);
				}
			}
		} else if (player.getGamePointManager().hasGamePointsReward(
				GPR.INVENTORY_DROPS)) {
			if (player.getGamePointManager().isGamePointDrop(item.getName())) {
				boolean notEnoughSpace = false;
				if (item.getDefinitions().isStackable()) {
					if (player.getInventory().getItems().containsOne(item)) {
						int slot = player.getInventory().getItems()
								.getThisItemSlot(item);
						Item invItem = player.getInventory().getItems()
								.get(slot);
						if (invItem.getAmount() + item.getAmount() <= 0)
							notEnoughSpace = true;
					} else if (!player.getInventory().hasFreeSlots())
						notEnoughSpace = true;
				} else {
					int freeSlots = player.getInventory().getFreeSlots();
					if (freeSlots == 0 || freeSlots < item.getAmount())
						notEnoughSpace = true;
				}
				if (notEnoughSpace) {
					player.getPackets().sendGameMessage(
							"Not enough space in your inventory.");
					if (!stackable && item.getAmount() > 1)
						for (int i = 0; i < item.getAmount(); i++)
							World.addGroundItem(new Item(item.getId(), 1),
									new WorldTile(getCoordFaceX(size),
											getCoordFaceY(size), getPlane()),
									player, true, 60);
					else
						World.addGroundItem(item, new WorldTile(
								getCoordFaceX(size), getCoordFaceY(size),
								getPlane()), player, true, 60);
				} else {
					player.getInventory().addItemMoneyPouch(item);
					return item;
				}
			}
		} else {
			if (!stackable && item.getAmount() > 1) {
				for (int i = 0; i < item.getAmount(); i++) {
					World.addGroundItem(new Item(item.getId(), 1),
							new WorldTile(getCoordFaceX(size),
									getCoordFaceY(size), getPlane()), player,
							true, 60);
				}
			} else {
				World.addGroundItem(item, new WorldTile(getCoordFaceX(size),
						getCoordFaceY(size), getPlane()), player, true, 60);
			}
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			final String FILE_PATH = Settings.getDropboxLocation()
					+ "logs/npcdrops/";
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH
					+ player.getUsername() + ".txt", true));
			writer.write("["
					+ dateFormat.format(cal.getTime())
					+ ", IP: "
					+ player.getSession().getIP()
					+ "] got "
					+ ItemDefinitions.getItemDefinitions(drop.getItemId())
							.getName() + " (" + drop.getItemId()
					+ ") drop from " + getName() + ".");
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException er) {
			er.printStackTrace();
		}
		return item;
	}

	private void lootBeam(Player player, NPC npc, ItemDefinitions drop) {
		if (player.getLootbeamAmount() <= 10000)
			return;
		if (drop.getPrice() >= player.getLootbeamAmount()) {
			player.getPackets()
					.sendGameMessage(
							player.rainbow ? "<col=E89002>A rainbow shines over one of your items."
									: "<col=E89002>A golden beam shines over one of your items.");
			WorldTile tile = new WorldTile(getCoordFaceX(getSize()),
					getCoordFaceY(getSize()), getPlane());
			WorldTasksManager.schedule(new WorldTask() {
				int loop;

				@Override
				public void run() {
					if (loop == 0) {
						player.getPackets().sendGraphics(
								new Graphics(player.rainbow ? 5050 : 4419),
								new WorldTile(tile));
					} else if (loop == 12) {
						player.getPackets().sendGraphics(
								new Graphics(player.rainbow ? 5051 : 4420),
								new WorldTile(tile));
					} else if (loop == 24) {
						player.getPackets().sendGraphics(
								new Graphics(player.rainbow ? 5052 : 4421),
								new WorldTile(tile));
					} else if (loop == 36) {
						player.getPackets().sendGraphics(
								new Graphics(player.rainbow ? 5053 : 4422),
								new WorldTile(tile));
					} else if (loop == 48) {
						player.getPackets().sendGraphics(new Graphics(4423),
								new WorldTile(tile));
						player.getPackets().sendGraphics(new Graphics(-1),
								new WorldTile(tile));
						stop();
					}
					loop++;
				}
			}, 0, 1);
		}
	}

	@Override
	public int getSize() {
		return getDefinitions().size;
	}

	public int getMaxHit(int style) {
		int maxHit = bonuses[0];
		if (style == 1)
			maxHit = bonuses[1];
		else if (style == 2)
			maxHit = bonuses[2];
		return maxHit / 10;
	}

	@Override
	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}

	public WorldTile getRespawnTile() {
		return respawnTile;
	}

	public void setRespawnTile(WorldTile respawnTile) {
		this.respawnTile = respawnTile;
	}

	@Override
	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget()
				&& !(combat.getTarget() instanceof Familiar)) {
			lastAttackedByTarget = Utils.currentTimeMillis();
			if (target instanceof Player) {
				if (target.getEffectsManager().hasActiveEffect(
						EffectType.INCITE))
					lastAttackedByTarget += 3000; // 3seconds to keep agro
				// extra. enough, even makes
				// those who dont focus, start
				// focusing
			}
		}
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (!getDefinitions().hasAttackOption())
			return;
		if (isForceWalking() || cantInteract) // if force walk not gonna get
			// target
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Utils.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

	public ArrayList<Entity> getPossibleTargets(boolean checkNPCs,
			boolean checkPlayers) {
		int size = getSize();
		int agroRatio = getCombatDefinitions().getAgroRatio();
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				List<Integer> playerIndexes = World.getRegion(regionId)
						.getPlayerIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player == null
								|| player.getCutscenesManager().hasCutscene()
								|| !player.clientHasLoadedMapRegion()
								|| player.getPlane() != getPlane()
								|| player.isDead()
								|| player.hasFinished()
								|| !player.isRunning()
								|| player.getAppearence().isHidden()
								|| !Utils
										.isOnRange(
												getX(),
												getY(),
												size,
												player.getX(),
												player.getY(),
												player.getSize(),
												forceTargetDistance > 0 ? forceTargetDistance
														: agroRatio)
								|| !clipedProjectile(player, false)
								|| (!forceAgressive
										&& !Wilderness.isAtWild(this) && player
										.getSkills().getCombatLevel() >= getCombatLevel() * 2))
							continue;
						possibleTarget.add(player);
						if (checkNPCs) {
							Familiar familiar = player.getFamiliar();
							if (familiar == null
									|| familiar.isDead()
									|| familiar.isFinished()
									|| !Utils
											.isOnRange(
													getX(),
													getY(),
													size,
													familiar.getX(),
													familiar.getY(),
													familiar.getSize(),
													forceTargetDistance > 0 ? forceTargetDistance
															: agroRatio)
									|| !clipedProjectile(familiar, false))
								continue;
							possibleTarget.add(familiar);
						}
					}
				}
			}
			if (checkNPCs) {
				List<Integer> npcsIndexes = World.getRegion(regionId)
						.getNPCsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == null
								|| npc instanceof Familiar
								|| npc.getPlane() != getPlane()
								|| npc == this
								|| npc.isDead()
								|| npc.hasFinished()
								|| !Utils
										.isOnRange(
												getX(),
												getY(),
												size,
												npc.getX(),
												npc.getY(),
												npc.getSize(),
												forceTargetDistance > 0 ? forceTargetDistance
														: agroRatio)
								|| (!npc.getDefinitions().hasAttackOption())
								|| /*
									 * ( ( ! isAtMultiArea ( ) || ! npc .
									 * isAtMultiArea ( ) ) && npc .
									 * getAttackedBy ( ) != this && npc .
									 * getAttackedByDelay ( ) > Utils .
									 * currentTimeMillis ( ) ) ||
									 */!clipedProjectile(npc, false)
								|| npc.isCantInteract())
							continue;
						possibleTarget.add(npc);
					}
				}
			}
		}
		return possibleTarget;
	}

	public ArrayList<Entity> getPossibleTargets() {
		return getPossibleTargets(false, true);
	}

	@Override
	public boolean isStunImmune() {
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer immune = (Integer) data.get(2892);
			return immune != null && immune == 1;
		}
		return false;
	}

	@Override
	public boolean isBoundImmune() {
		return isStunImmune();
	}

	@Override
	public boolean isPoisonImmune() {
		return getCombatDefinitions().isPoisonImmune();
	}

	public boolean checkAgressivity() {
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		ArrayList<Player> gamePointForceAggressiveTargets = new ArrayList<>();
		if (!forceAgressive) {
			Drops drops = NPCDrops.getDrops(id);
			if (drops == null)
				return false;
			if (!getDefinitions().hasOption("attack"))
				return false;
			if (!getCombatDefinitions().isAgressive()) {
				if (!possibleTarget.isEmpty()) {
					for (Entity target : possibleTarget) {
						if (target == null || target.isDead()
								|| target.hasFinished())
							continue;
						if (target instanceof Player) {
							Player player = (Player) target;
							if (!player
									.getGamePointManager()
									.hasGamePointsReward(GPR.FORCE_AGGRO, false))
								continue;
							gamePointForceAggressiveTargets.add(player);
						}
					}
				}
				if (gamePointForceAggressiveTargets.isEmpty())
					return false;
				Entity target = gamePointForceAggressiveTargets.get(Utils
						.random(gamePointForceAggressiveTargets.size()));
				setTarget(target);
				target.setAttackedBy(this);
				target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
				return false;
			}
		}
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Utils.random(possibleTarget
					.size()));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utils.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	@Override
	public String toString() {
		return getDefinitions().getName() + " - " + id + " - " + getX() + " "
				+ getY() + " " + getPlane();
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public void setRandomWalk(int forceRandomWalk) {
		this.walkType = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}

	public void setName(String string) {
		this.name = getDefinitions().getName().equals(string) ? null : string;
		changedName = true;
	}

	public int getCustomCombatLevel() {
		return combatLevel;
	}

	@Override
	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}

	@Override
	public String getName() {
		return name != null ? name : getDefinitions().getName();
	}

	public void setCombatLevel(int level) {
		combatLevel = getDefinitions().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}

	public boolean hasChangedName() {
		return changedName;
	}

	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}

	public boolean isSpawned() {
		return spawned;
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public boolean isNoDistanceCheck() {
		return noDistanceCheck;
	}

	public void setNoDistanceCheck(boolean noDistanceCheck) {
		this.noDistanceCheck = noDistanceCheck;
	}

	public boolean withinDistance(Player tile, int distance) {
		return super.withinDistance(tile, distance);
	}

	/**
	 * Gets the locked.
	 * 
	 * @return The locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	public void lock() {
		lockDelay = Long.MAX_VALUE;
	}

	public void lock(long time) {
		lockDelay = time == -1 ? Long.MAX_VALUE : WorldThread.WORLD_CYCLE
				+ time;
	}

	/**
	 * Sets the locked.
	 * 
	 * @param locked
	 *            The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isIntelligentRouteFinder() {
		return intelligentRouteFinder;
	}

	public void setIntelligentRouteFinder(boolean intelligentRouteFinder) {
		this.intelligentRouteFinder = intelligentRouteFinder;
	}

	public double getDropRateFactor() {
		return dropRateFactor;
	}

	public void setDropRateFactor(double dropRateFactor) {
		this.dropRateFactor = dropRateFactor;
	}

	public SecondaryBar getNextSecondaryBar() {
		return nextSecondaryBar;
	}

	public void setNextSecondaryBar(SecondaryBar secondaryBar) {
		this.nextSecondaryBar = secondaryBar;
	}

	public boolean isCantSetTargetAutoRelatio() {
		return cantSetTargetAutoRelatio;
	}

	public void setCantSetTargetAutoRelatio(boolean cantSetTargetAutoRelatio) {
		this.cantSetTargetAutoRelatio = cantSetTargetAutoRelatio;
	}

	@Override
	public boolean canMove(int dir) {
		if (lastTile == null)
			return true;
		return true;
	}

	public int getStrengthType() {
		int type = Combat.getStyleType(getWeaknessStyle());
		if (type == Combat.MELEE_TYPE)
			return Combat.MAGIC_TYPE;
		else if (type == Combat.RANGE_TYPE)
			return Combat.MELEE_TYPE;
		else if (type == Combat.MAGIC_TYPE)
			return NPCCombatDefinitions.RANGE;
		return Combat.ALL_TYPE;
	}

	public int getWeaknessStyle() {
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer weakness = (Integer) data.get(2848);
			if (weakness != null)
				return weakness;
		}
		return 0;
	}

	public double getCritChance() {
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer crit = (Integer) data.get(2864);
			if (crit != null)
				return crit / 10.0;
		}
		return 5.0;
	}

	public int getAttackStyle() {
		if (bonuses[2] > 0)
			return NPCCombatDefinitions.MAGE;
		if (bonuses[1] > 0)
			return NPCCombatDefinitions.RANGE;
		return NPCCombatDefinitions.MELEE;
	}

	public int getAttackSpeed() {
		Map<Integer, Object> data = getDefinitions().clientScriptData;
		if (data != null) {
			Integer speed = (Integer) data.get(14);
			if (speed != null)
				return speed;
		}
		return 4;
	}

	public HeadIcon[] getIcons() {
		return new HeadIcon[0];
	}

	public void requestIconRefresh() {
		refreshHeadIcon = true;
	}

	public boolean isRefreshHeadIcon() {
		return refreshHeadIcon;
	}

	public void increaseKills(RecordKey key, boolean hm) {
		for (Entity s : getReceivedDamageSources()) {
			if (s instanceof Player)
				((Player) s).getTimersManager().increaseKills(key, hm);
		}
	}

	public void setBossInstance(BossInstance instance) {
		bossInstance = instance;
	}

	public BossInstance getBossInstance() {
		return bossInstance;
	}

	public void setForcePassive(boolean b) {

	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean isNoClipWalking() {
		return noClipWalking;
	}

	public void setNoClipWalking(boolean noClipWalking) {
		this.noClipWalking = noClipWalking;
	}

	public void addClipping() {
		lastTile = new Integer[] { getX(), getY(), getPlane(), getSize() };
		boolean canClip = !toString().equals("");
		if (canClip)
			MapBuilder.entityClip(toString(), lastTile[0], lastTile[1],
					lastTile[2], lastTile[3],
					(this instanceof Familiar || this instanceof Pet) ? 1 : 0,
					true);
	}

	public void removeClipping() {
		if (lastTile != null)
			MapBuilder.entityClip(toString(), lastTile[0], lastTile[1],
					lastTile[2], lastTile[3],
					(this instanceof Familiar || this instanceof Pet) ? 1 : 0,
					false);
	}

	public boolean addWalkStepsInteract(int destX, int destY,
			int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size,
				calculate);
	}

	public int[] getLastWalkTile() {
		Object[] objects = walkSteps.toArray();
		if (objects.length == 0)
			return new int[] { getX(), getY() };
		int step[] = (int[]) objects[objects.length - 1];
		return new int[] { step[1], step[2] };
	}

	public boolean addWalkStepsInteract(final int destX, final int destY,
			int maxStepsCount, int sizeX, int sizeY, boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate)
					return false;
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY,
						lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null)
					return false;
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1))
				return true;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	private int[] calculatedStep(int myX, int myY, int destX, int destY,
			int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastX, lastY, true))
				myX--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastX, lastY, true))
				myX++;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastX, lastY, true))
				myY--;
			else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if ((this instanceof NPC && !canWalkNPC(myX, myY))
					|| !addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1
					|| myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY)
			return null;
		return new int[] { myX, myY };
	}

	public NPCCustomizationData getNextCustomization() {
		return nextCustomization;
	}

	public void setNextCustomization(NPCCustomizationData nextCustomization) {
		this.nextCustomization = nextCustomization;
	}

	public enum Direciton {
		NORTH(8192),

		SOUTH(0),

		EAST(12288),

		WEST(4096),

		NORTHEAST(10240),

		SOUTHEAST(14366),

		NORTHWEST(6144),

		SOUTHWEST(2048);

		private final int value;

		Direciton(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}