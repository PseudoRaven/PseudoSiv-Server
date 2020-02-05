package net.nocturne.game.npc.dungeonnering;

import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.Drop;
import net.nocturne.game.npc.Drops;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.skills.DungeoneeringTraps;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.NPCDrops;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class DungeonHunterNPC extends DungeonNPC {

	public DungeonHunterNPC(int id, WorldTile tile, DungeonManager manager,
			double multiplier) {
		super(id, tile, manager, multiplier);
	}

	@Override
	public void processNPC() {
		if (isCantInteract() || getId() >= 11096 || getManager() == null)
			return;
		super.processNPC();
		List<MastyxTrap> traps = getManager().getMastyxTraps();
		if (traps.isEmpty())
			return;
		final int tier = DungeoneeringTraps.getNPCTier(getId());
		for (final MastyxTrap trap : traps) {
			if (!withinDistance(trap, 3) || Utils.random(3) != 0)
				continue;
			trap.setCantInteract(true);
			setCantInteract(true);
			setNextFaceEntity(trap);
			addWalkSteps(trap.getX() + 1, trap.getY() + 1);

			final int trap_tier = trap.getTier();
			double successRatio = getSuccessRatio(tier, trap_tier);
			final boolean failed = successRatio < Math.random();

			setCantInteract(true);
			if (failed) {
				WorldTasksManager.schedule(new WorldTask() {

					int ticks = 0;

					@Override
					public void run() {
						ticks++;

						if (ticks == 5) {
							setNextAnimation(new Animation(13264));
						} else if (ticks == 8) {
							trap.setNextNPCTransformation(1957);
							trap.setNextGraphics(new Graphics(2561 + trap_tier));
						} else if (ticks == 16) {
							getManager().removeMastyxTrap(trap);
							setCantInteract(false);
							this.stop();
						}
					}
				}, 0, 0);
			} else {
				WorldTasksManager.schedule(new WorldTask() {

					int ticks = 0;

					@Override
					public void run() {
						ticks++;

						switch (ticks) {
						case 9:
							trap.setNextNPCTransformation(1957);
							trap.setNextGraphics(new Graphics(2551 + trap_tier));
							break;

						case 13:
							setNextAnimation(new Animation(13260));
							break;

						case 18:
							setNextNPCTransformation(getId() + 10);
							break;

						case 19:
							setCantInteract(false);
							getManager().removeMastyxTrap(trap);
							this.stop();
							break;

						}
					}
				}, 0, 0);
			}
		}
	}

	@Override
	public void drop() {
		World.addGroundItem(new Item(532), new WorldTile(this));
		Drops drops = NPCDrops.getDrops(getId());
		Drop drop = drops.getDrop(Drops.COMMON, Double.MAX_VALUE);
		if (drop == null)
			return;
		World.addGroundItem(new Item(drop.getItemId()), new WorldTile(this)); // hide
	}

	private static double getSuccessRatio(int tier, int trapTier) {
		double successRatio = 0.0;
		int tierProduct = trapTier - tier;
		if (tierProduct == 0)
			successRatio = 0.5;
		else if (tierProduct > 0)
			successRatio = 0.5 + (tierProduct / 10.0);

		if (successRatio > 0.9)
			successRatio = 0.9;
		return successRatio;
	}
}
