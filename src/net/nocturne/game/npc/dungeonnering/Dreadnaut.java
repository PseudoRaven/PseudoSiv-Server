package net.nocturne.game.npc.dungeonnering;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Dreadnaut extends DungeonBoss {

	private List<GasPuddle> puddles;

	private int ticks;
	private boolean reduceMagicLevel;

	public Dreadnaut(int id, WorldTile tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, manager, reference);
		setForceFollowClose(true);
		setRun(true);
		setLureDelay(6000);
		puddles = new CopyOnWriteArrayList<>();
	}

	@Override
	public void processNPC() {
		if (puddles == null)
			return;
		super.processNPC();
		if (!reduceMagicLevel) {
			if (isUnderCombat()) {
				for (Entity t : getPossibleTargets()) {
					if (!t.withinDistance(this, 1)) {
						ticks++;
						break;
					}
				}
			}
			if (ticks == 25) {
				reduceMagicLevel = true;
				setNextForceTalk(new ForceTalk(
						"You cannot run from me forever!"));
			}
		}

		for (GasPuddle puddle : puddles) {
			puddle.cycles++;
			if (puddle.canDestroyPuddle()) {
				puddles.remove(puddle);
				continue;
			} else if (puddle.cycles % 2 != 0)
				continue;
			if (puddle.cycles % 2 == 0)
				puddle.refreshGraphics();
			List<Entity> targets = getPossibleTargets(true, true);
			for (Entity t : targets) {
				if (!t.matches(puddle.tile))
					continue;
				t.applyHit(new Hit(this,
						Utils.random((int) (t.getHitpoints() * 0.25)) + 1,
						HitLook.REGULAR_DAMAGE));
			}
		}
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.60;
	}

	public boolean canReduceMagicLevel() {
		return reduceMagicLevel;
	}

	public void addSpot(WorldTile tile) {
		GasPuddle puddle = new GasPuddle(this, tile);
		puddle.refreshGraphics();
		puddles.add(puddle);
	}

	private static class GasPuddle {
		final Dreadnaut boss;
		final WorldTile tile;
		int cycles;

		GasPuddle(Dreadnaut boss, WorldTile tile) {
			this.tile = tile;
			this.boss = boss;
		}

		void refreshGraphics() {
			World.sendGraphics(boss, new Graphics(2859, 0, 10), tile);
		}

		boolean canDestroyPuddle() {
			return cycles == 50;
		}
	}
}
