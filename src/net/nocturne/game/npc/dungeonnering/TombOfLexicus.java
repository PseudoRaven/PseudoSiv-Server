package net.nocturne.game.npc.dungeonnering;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;

@SuppressWarnings("serial")
public class TombOfLexicus extends DungeonNPC {

	private LexicusRunewright boss;

	public TombOfLexicus(LexicusRunewright boss, int id, WorldTile tile,
			DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		setForceAgressive(true);
		this.boss = boss;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public void drop() {

	}

	@Override
	public int getMaxHitpoints() {
		return 10;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		boss.removeBook(this);
	}
}
