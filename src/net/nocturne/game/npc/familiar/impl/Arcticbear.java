package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Arcticbear extends Familiar {

	private static final long serialVersionUID = 6173794214333315091L;

	public Arcticbear(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Arctic Blast";
	}

	@Override
	public String getSpecialDescription() {
		return "Can hit a maximum of 150 damage, with a chance of stunning the opponent.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) { // TODO find real animation of
		// bear
		final Entity target = (Entity) object;
		final Familiar npc = this;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(4929));
		setNextGraphics(new Graphics(1405));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				setNextAnimation(new Animation(-1));
				World.sendProjectile(npc, target, 1406, 34, 16, 30, 35, 16, 0);
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						target.applyHit(new Hit(getOwner(), Utils.random(150),
								HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1407));
					}
				}, 2);
			}
		});
		return true;
	}
}
