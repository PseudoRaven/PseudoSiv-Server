package net.nocturne.game.npc.dungeonnering;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.game.player.actions.skills.dungeoneering.RoomReference;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class BalLakThePummeler extends DungeonBoss {

	private boolean skip;
	private boolean reduced = false;
	private int barPercentage;

	private List<PoisonPuddle> puddles = new CopyOnWriteArrayList<>();

	public BalLakThePummeler(int id, WorldTile tile, DungeonManager manager,
			RoomReference reference) {
		super(id, tile, manager, reference);
		setLureDelay(6000);
	}

	@Override
	public void processNPC() {
		if (isDead())
			return;
		super.processNPC();
		skip = !skip;
		if (!skip) {
			reduced = isReduced();
			handleBarPercentage();
		}
	}

	private void handleBarPercentage() {

		if (!reduced) {
			if (!isUnderCombat()) {
				if (barPercentage > 0) {
					barPercentage--;
					sendDefenceBar();
				}
			} else {
				if (barPercentage < 100) {
					barPercentage++;
					sendDefenceBar();
				}
			}
		}
	}

	private boolean isReduced() {

		for (PoisonPuddle puddle : puddles) {
			puddle.cycles++;
			if (puddle.canDestroyPoison()) {
				puddles.remove(puddle);
				continue;
			}
			List<Entity> targets = getPossibleTargets(true, true);
			if (Utils.colides(getX(), getY(), getSize(), puddle.tile.getX(),
					puddle.tile.getY(), 1)) {
				barPercentage = barPercentage > 1 ? barPercentage - 2 : 0;
				sendDefenceBar();
				reduced = true;
			}
			for (Entity t : targets) {
				if (!t.withinDistance(puddle.tile, 2))
					continue;
				t.applyHit(new Hit(this,
						Utils.random((int) (t.getHitpoints() * 0.25)) + 1,
						HitLook.REGULAR_DAMAGE));
			}
		}
		return reduced;
	}

	@Override
	public void processHit(Hit hit) {
		int damage = hit.getDamage();
		HitLook look = hit.getLook();
		if (damage > 0) {
			if (look == HitLook.MELEE_DAMAGE || look == HitLook.RANGE_DAMAGE
					|| look == HitLook.MAGIC_DAMAGE) {
				double multiplier = (100D - (barPercentage)) / 100D;
				hit.setDamage((int) (damage * multiplier));
			}
		}
		super.processHit(hit);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		puddles.clear();
		sendDefenceBar();
	}

	private void sendDefenceBar() {
		if (isDead())
			getManager().hideBar(getReference());
		else
			getManager().showBar(getReference(), "Demon's Defence",
					barPercentage);
	}

	private static class PoisonPuddle {
		final WorldTile tile;
		int cycles;

		PoisonPuddle(WorldTile tile) {
			this.tile = tile;
		}

		boolean canDestroyPoison() {
			return cycles == 15;
		}
	}

	public void addPoisionBubble(WorldTile tile) {
		puddles.add(new PoisonPuddle(tile));
		World.sendGraphics(this, new Graphics(2588), tile);
	}

	public List<PoisonPuddle> getPoisonPuddles() {
		return puddles;
	}
}