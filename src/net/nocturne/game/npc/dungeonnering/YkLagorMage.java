package net.nocturne.game.npc.dungeonnering;

import net.nocturne.game.Animation;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;

@SuppressWarnings("serial")
public class YkLagorMage extends DungeonNPC {

	private YkLagorThunderous boss;
	private int cycle;

	public YkLagorMage(YkLagorThunderous ykLagorThunderous, int id,
			WorldTile tile, DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		this.boss = ykLagorThunderous;
		setNextFaceEntity(boss);
		setCantFollowUnderCombat(true);
	}

	@Override
	public void processNPC() {
		if (isDead() || boss == null)
			return;
		if (isUnderCombat()) {
			super.processNPC();
			return;
		}
		if (cycle > 0) {
			cycle--;
			return;
		}
		cycle = 5;
		setNextFaceEntity(boss);
		setNextAnimation(new Animation(3645));
		World.sendProjectile(this, boss, 2704, 39, 39, 55, 70, 0, 0);
	}

	@Override
	public void drop() {

	}

	@Override
	public int getMaxHitpoints() {
		return 650;
	}

	@Override
	public int getCombatLevel() {
		return 65;
	}

	/*
	 * @Override public void sendDeath(Entity source) { super.sendDeath(source);
	 * }
	 */
}
