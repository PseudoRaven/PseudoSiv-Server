package net.nocturne.game.npc.others;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Werewolf extends NPC {

	private int realId;

	public Werewolf(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		realId = id;
	}

	private boolean hasWolfbane(Entity target) {
		if (target instanceof NPC)
			return false;
		return ((Player) target).getEquipment().getWeaponId() == 2952;
	}

	@Override
	public void processNPC() {
		if (isDead() || isCantInteract())
			return;
		if (isUnderCombat() && getId() == realId && Utils.random(5) == 0) {
			final Entity target = getCombat().getTarget();
			if (!hasWolfbane(target)) {
				setNextAnimation(new Animation(6554));
				setCantInteract(true);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						setNextNPCTransformation(realId - 20);
						setNextAnimation(new Animation(-1));
						setCantInteract(false);
						setTarget(target);
					}
				}, 1);
				return;
			}
		}
		super.processNPC();
	}
	
	@Override
	public void sendDeath(Entity killer) {
		setNextNPCTransformation(-1);
		super.sendDeath(killer);
	}

	@Override
	public void reset() {
		setNextNPCTransformation(-1);
		super.reset();
	}

}
