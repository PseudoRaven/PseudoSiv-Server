package net.nocturne.game.npc.familiar;

import java.io.Serializable;

import net.nocturne.game.Animation;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.familiar.impl.BeastOfBurden;
import net.nocturne.game.npc.glacior.Glacyte;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public abstract class Familiar extends NPC implements Serializable {

	private static final long serialVersionUID = -3255206534594320406L;

	private transient boolean finished = false;
	private transient Player owner;
	private boolean trackDrain;
	private int specialEnergy;
	private int trackTimer;
	private int ticks;

	private BeastOfBurden bob;
	private Pouch pouch;

	public Familiar(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(Summoning.getNPCId(pouch.getRealPouchId()), tile,
				mapAreaNameHash, canBeAttackFromOutOfArea, false);
		this.owner = owner;
		this.pouch = pouch;
		owner.setFamiliar(this);
		specialEnergy = 60;
		if (getBOBSize() > 0)
			bob = new BeastOfBurden(canDepositOnly(), getBOBSize());
		call(true);
		resetTickets();
		setRun(true);
	}

	public void store() {
		if (bob == null)
			return;
		bob.open();
	}

	public boolean canDepositOnly() {
		return getDefinitions().hasOption("withdraw");
	}

	public boolean canStoreEssOnly() {
		return pouch == Pouch.ABYSSAL_LURKER || pouch == Pouch.ABYSSAL_PARASITE
				|| pouch == Pouch.ABYSSAL_TITAN;
	}

	public int getOriginalId() {
		return Summoning.getNPCId(pouch.getRealPouchId());
	}

	public void resetTickets() {
		ticks = (int) (pouch.getPouchTime() / 1000 / 30);
		trackTimer = 0;
	}

	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceEntity(owner);
		if (getEffectsManager().hasActiveEffect(EffectType.BOUND))
			return;
		int size = getSize();
		int targetSize = owner.getSize();
		if (Utils.colides(getX(), getY(), size, owner.getX(), owner.getY(),
				targetSize) && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + targetSize, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + targetSize)) {
						resetWalkSteps();
						if (!addWalkSteps(getX(), owner.getY() - size)) {
							return;
						}
					}
				}
			}
			return;
		}
		resetWalkSteps();
		if (!clipedProjectile(owner, true)
				|| !Utils.isOnRange(getX(), getY(), size, owner.getX(),
						owner.getY(), targetSize, 0))
			calcFollow(owner, 2, true, false);
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		unlockOrb();
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (trackDrain)
				owner.getSkills().drainSummoning(1);
			trackDrain = !trackDrain;
			if (ticks == 2)
				owner.getPackets()
						.sendGameMessage(
								"<col=FF0000><shad=000000>You have 1 minute before your familiar vanishes.</col></shad>");
			else if (ticks == 1) {
				owner.getPackets()
						.sendGameMessage(
								"<col=FF0000><shad=000000>You have 30 seconds before your familiar vanishes.</col></shad>");
			} else if (ticks == 0) {
				removeFamiliar();
				dissmissFamiliar(false);
				return;
			}
			sendTimeRemaining();
		}
		int originalId = getOriginalId() + 1;
		if (owner.isCanPvp() && getId() == getOriginalId()) {
			setNextNPCTransformation(originalId);
			call(false);
			return;
		} else if (!owner.isCanPvp() && getId() == originalId
				&& pouch != Pouch.MAGPIE && pouch != Pouch.IBIS
				&& pouch != Pouch.BEAVER && pouch != Pouch.MACAW
				&& pouch != Pouch.FRUIT_BAT) {
			setNextNPCTransformation(originalId - 1);
			call(false);
			return;
		} else if (!withinDistance(owner, 12)) {
			call(false);
			return;
		}
		if (!getCombat().process()) {
			if (isAgressive() && owner.getAttackedBy() != null
					&& owner.getAttackedByDelay() > Utils.currentTimeMillis()
					&& canAttack(owner.getAttackedBy())
					&& Utils.random(25) == 0)
				getCombat().setTarget(owner.getAttackedBy());
			else
				sendFollow();
		}
	}

	public boolean canAttack(Entity target) {
		if (target == this || target == owner)
			return false;
		if (target instanceof Player) {
			Player player = (Player) target;
			if (!owner.isCanPvp() || !player.isCanPvp())
				return false;
		} else if (target instanceof NPC) {
			NPC n = (NPC) target;
			if (n.getId() == 14301 || n.getId() == 14302 || n.getId() == 14303
					|| n.getId() == 14304) {
				Glacyte glacyte = (Glacyte) n;
				if (glacyte.getGlacor().getTargetIndex() != -1
						&& getOwner().getIndex() != glacyte.getGlacor()
								.getTargetIndex()) {
					getOwner().getPackets().sendGameMessage(
							"This isn't your target.");
					return false;
				}
			}
		}
		return !target.isDead()
				&& owner.getControllerManager().canAttack(target);
	}

	public boolean renewFamiliar() {
		if (ticks > 5) {
			owner.getPackets()
					.sendGameMessage(
							"You need to have at least two minutes and fifty seconds remaining before you can renew your familiar.",
							true);
			return false;
		} else if (!owner.getInventory().getItems()
				.contains(new Item(pouch.getRealPouchId(), 1))) {
			owner.getPackets()
					.sendGameMessage(
							"To renew your familiar's timer you must have a pouch of that familiar's type and be ready to sacrifice it.");
			return false;
		}
		resetTickets();
		owner.getInventory().deleteItem(pouch.getRealPouchId(), 1);
		call(true);
		owner.getPackets().sendGameMessage(
				"You use your remaining pouch to renew your familiar.");
		return true;
	}

	public void takeBob() {
		if (bob == null)
			return;
		if (bob.getBeastItems().getFreeSlots() == getBOBSize()) {
			owner.getPackets().sendGameMessage(
					"Your familiar currently is not carrying any items.");
			return;
		}
		bob.takeBob();
	}

	public void sendTimeRemaining() {
		owner.getPackets().sendExecuteScript(754, (ticks / 2), (ticks % 2));
	}

	public void sendMainConfigs() {
		switchOrb(true);
		refreshSpecialEnergy();
		sendTimeRemaining();
		owner.getVarsManager().sendVar(448, pouch.getRealPouchId());
		owner.getVarsManager().sendVarBit(6051, getSpecialAmount());
		owner.getPackets().sendCSVarString(204, getSpecialName());
		owner.getPackets().sendCSVarString(205, getSpecialDescription());
		owner.getPackets().sendCSVarInteger(1436,
				getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
		unlockOrb();
		sendFollowerDetails();
	}

	public void sendFollowerDetails() {
		owner.getInterfaceManager().setFamiliarInterface(662);
		owner.getPackets().sendIComponentSettings(662, 55, 0, 50, 0x2);
		owner.getPackets().sendHideIComponent(662, 49, false);
		owner.getPackets().sendIComponentText(662, 17,
				owner.getFamiliar().getName().toLowerCase());
		owner.getVarsManager().sendVar(1784, owner.getFamiliar().getId());
		owner.getVarsManager().sendVar(1831,
				owner.getFamiliar().getPouch().getRealPouchId());
		owner.getVarsManager().sendVar(1786,
				(int) (owner.getFamiliar().getPouch().getPouchTime() / 468.75));
		owner.getPackets().sendIComponentText(662, 11, "");
		owner.getPackets().sendIComponentText(662, 9, "");
		owner.getPackets().sendIComponentText(662, 6, "");
		owner.getPackets().sendIComponentText(662, 8, "");
		refreshSpecialEnergy();
	}

	public void switchOrb(boolean on) {
		owner.getVarsManager().sendVar(1174, on ? -1 : 0);
		if (!on)
			lockOrb();
	}

	public void unlockOrb() {

		sendLeftClickOption(owner);
	}

	public static void selectLeftOption(Player player) {
		sendLeftClickOption(player);
		player.getInterfaceManager().sendCentralInterface(880);
		player.getPackets().sendIComponentSettings(880, 2, 0, 3, 2);
		player.getPackets().sendIComponentSettings(880, 9, 0, 4, 2);
		player.getPackets().sendIComponentSettings(880, 28, 0, 7, 2);
	}

	public static void confirmLeftOption(Player player) {
		player.getInterfaceManager().removeFamiliarInterface();
	}

	public static void setLeftclickOption(Player player,
			int summoningLeftClickOption) {
		if (summoningLeftClickOption == player.getSummoningLeftClickOption())
			return;
		player.setSummoningLeftClickOption(summoningLeftClickOption);
		sendLeftClickOption(player);
	}

	public static void sendLeftClickOption(Player player) {
		player.getVarsManager().sendVar(1493,
				player.getSummoningLeftClickOption());
		player.getVarsManager().sendVar(1494,
				player.getSummoningLeftClickOption());
	}

	public void lockOrb() {
		refreshDefaultPetOptions(owner);
		// owner.getPackets().sendHideIComponent(1428, 15, true);
	}

	public static void refreshDefaultPetOptions(Player owner) {
		// COMP 33 - Call Familiar ONLY
		// COMP 6 - Left click option
		// owner.getPackets().sendHideIComponent(1428, 7, true);
		// owner.getPackets().sendHideIComponent(1428, 6, true);
	}

	private transient int[][] checkNearDirs;
	private transient boolean sentRequestMoveMessage;

	public void call() {
		if (isDead())
			return;
		if (getAttackedBy() != null
				&& getAttackedByDelay() > Utils.currentTimeMillis()) {
			owner.getPackets().sendGameMessage(
					"You can't call your familiar while it under combat.");
			return;
		}
		call(false);
	}

	public void call(boolean login) {
		int size = getSize();
		if (login) {
			if (bob != null)
				bob.setEntitys(owner, this);
			checkNearDirs = Utils.getCoordOffsetsNear(size);
			sendMainConfigs();
		} else
			removeTarget();
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX()
					+ checkNearDirs[0][dir], owner.getY()
					+ checkNearDirs[1][dir], owner.getPlane()));
			if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(),
					size)) { // if
				// found
				// done
				teleTile = tile;
				break;
			}
		}
		if (login || teleTile != null)
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					setNextGraphics(new Graphics(
							getDefinitions().size > 1 ? 1315 : 1314));
				}
			});
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				owner.getPackets()
						.sendGameMessage(
								"Your familiar is too large to fit in the area you are standing in. Move into a larger space and try again.");
				sentRequestMoveMessage = true;
			}
			return;
		}
		sentRequestMoveMessage = false;
		setNextWorldTile(teleTile);
	}

	public void removeFamiliar() {
		owner.setFamiliar(null);
	}

	public void dissmissFamiliar(boolean logged) {
		finish();
		if (!logged && !isFinished()) {
			setFinished(true);
			switchOrb(false);
			owner.getPackets().sendCSVarInteger(4619, 0);
			owner.getInterfaceManager().removeFamiliarInterface();
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 0);
			if (bob != null)
				bob.dropBob();
		}
	}

	private transient boolean dead;

	@Override
	public boolean isDead() {
		return dead || super.isDead();
	}

	@Override
	public void sendDeath(Entity source) {
		if (dead)
			return;
		dead = true;
		removeFamiliar();
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		setCantInteract(true);
		getCombat().removeTarget();
		setNextAnimation(null);
		owner.getPackets().sendCSVarInteger(4619, 0);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
					owner.getPackets().sendGameMessage(
							"Your familiar slowly begins to fade away..");
				} else if (loop >= defs.getDeathDelay()) {
					dissmissFamiliar(false);
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	public void respawnFamiliar(Player owner) {
		this.owner = owner;
		initEntity();
		deserialize();
		call(true);
	}

	public abstract String getSpecialName();

	public abstract String getSpecialDescription();

	public abstract int getBOBSize();

	public abstract int getSpecialAmount();

	public abstract SpecialAttack getSpecialAttack();

	public abstract boolean submitSpecial(Object object);

	public boolean isAgressive() {
		return true;
	}

	public static enum SpecialAttack {
		ITEM, ENTITY, CLICK, OBJECT
	}

	public BeastOfBurden getBob() {
		return bob;
	}

	public void refreshSpecialEnergy() {
		owner.getVarsManager().sendVar(1787, specialEnergy);
	}

	public void restoreSpecialAttack(int energy) {
		if (specialEnergy >= 60)
			return;
		specialEnergy = energy + specialEnergy >= 60 ? 60 : specialEnergy
				+ energy;
		refreshSpecialEnergy();
	}

	public void setSpecial(boolean on) {
		if (!on)
			owner.getTemporaryAttributtes().remove("FamiliarSpec");
		else {
			if (specialEnergy < getSpecialAmount()) {
				owner.getPackets().sendGameMessage(
						"Your special move bar is too low to use this scroll.");
				return;
			}
			owner.getTemporaryAttributtes().put("FamiliarSpec", Boolean.TRUE);
		}
	}

	public void drainSpecial(int specialReduction) {
		specialEnergy -= specialReduction;
		if (specialEnergy < 0) {
			specialEnergy = 0;
		}
		refreshSpecialEnergy();
	}

	public void drainSpecial() {
		specialEnergy -= getSpecialAmount();
		refreshSpecialEnergy();
	}

	public boolean hasSpecialOn() {
		if (owner.getTemporaryAttributtes().remove("FamiliarSpec") != null) {
			int scrollId = Summoning.getScrollId(pouch.getRealPouchId());
			if (!owner.getInventory().containsItem(scrollId, 1)) {
				owner.getPackets().sendGameMessage(
						"You don't have the scrolls to use this move.");
				return false;
			}
			if (!withinDistance(owner, 16)) {
				owner.getPackets()
						.sendGameMessage(
								"Your familiar is too far away to use that scroll, or it cannot see you.");
				return false;
			}
			owner.getInventory().deleteItem(scrollId, 1);
			drainSpecial();
			return true;
		}
		return false;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isFinished() {
		return finished;
	}

	public Pouch getPouch() {
		return pouch;
	}
}
