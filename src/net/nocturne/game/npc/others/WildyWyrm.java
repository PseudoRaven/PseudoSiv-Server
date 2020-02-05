package net.nocturne.game.npc.others;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.Drop;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class WildyWyrm extends NPC {

	private boolean emerged;
	private int cycle;

	public WildyWyrm(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(2000);
		setCantFollowUnderCombat(true);
		setForceTargetDistance(16);
		setDropRateFactor(1.25);
	}

	@Override
	public void reset() {
		setNextNPCTransformation(getId() == 3334 ? 2417 : 20629);
		setCombatLevel(-1);
		emerged = false;
		cycle = 0;
		setCantInteract(false);
		super.reset();
	}

	@Override
	public void drop() {
		if (getId() == 3334 || getId() == 2417)
			for (Entity t : getReceivedDamageSources()) {
				if (!(t instanceof Player))
					continue;
				Player player = (Player) t;
				player.getPackets()
						.sendGameMessage(
								"You contributed to the wyrm's death, and it leaves a fury shark behind.");
				sendDrop(player, new Drop(20429, 1, 1));
				player.getCompCapeManager().increaseRequirement(
						Requirement.WILDY_WYRM, 1);
			}
		super.drop();
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		if (getCombat().getTarget() != null
				&& !getCombat().getTarget().withinDistance(this, 16))
			this.removeTarget();
		super.processNPC();
	}

	public static void handleInspect(final Player player, final NPC npc) {
		if (!(npc instanceof WildyWyrm))
			return;
		final WildyWyrm wyrm = (WildyWyrm) npc;
		if (wyrm.emerged || wyrm.isCantInteract() || wyrm.cycle != 0
				|| wyrm.getCombat().getTarget() != null) {
			player.getPackets().sendGameMessage("Someone else is doing that.");
			return;
		}
		wyrm.setHitpoints(npc.getMaxHitpoints());
		wyrm.setCantInteract(true);
		player.setNextAnimation(new Animation(4278));
		player.lock(3);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				wyrm.setNextNPCTransformation(wyrm.getId() == 2417 ? 3334
						: 20630);
				wyrm.setCantInteract(false);
				wyrm.setTarget(player);
			}
		}, 2);
	}
}
