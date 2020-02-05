package net.nocturne.game.npc.godwars2.vindictagorvek;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.Hit;
import net.nocturne.game.WorldTile;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.NPC;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Gorvek extends NPC {

	public Gorvek(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setRun(true);
	}

	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
	}

	@Override
	public void spawn() {
		super.spawn();
		setNextAnimation(new Animation(28264));
	}

}