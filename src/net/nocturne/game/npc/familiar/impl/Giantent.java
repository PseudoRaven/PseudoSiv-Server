package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Giantent extends Familiar {

	private static final long serialVersionUID = -743802297641852412L;

	public Giantent(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Acorn Missile";
	}

	@Override
	public String getSpecialDescription() {
		return "Hits all players around a tile radius (not including you) with damage that could inflict up to 187 points.";
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
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		final Familiar npc = this;
		Player player = getOwner();
		player.setNextAnimation(new Animation(7858));
		player.setNextGraphics(new Graphics(1316));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				WorldTasksManager.schedule(new WorldTask() {

					@Override
					public void run() {
						target.applyHit(new Hit(getOwner(), Utils.random(150),
								HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1363));
					}
				}, 2);
				World.sendProjectile(npc, target, 1362, 34, 16, 30, 35, 16, 0);
			}
		}, 1);
		return true;
	}
}
