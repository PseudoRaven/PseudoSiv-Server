package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Evilturnip extends Familiar {

	private static final long serialVersionUID = -326631497631235701L;

	public Evilturnip(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Evil flames";
	}

	@Override
	public String getSpecialDescription() {
		return "Magic based attack which will drain the enemy's magic level some and heal the Evil turnip a little.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8251));
		World.sendProjectile(this, target, 1330, 34, 16, 30, 35, 16, 0);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				int hitDamage = Utils.random(100);
				target.applyHit(new Hit(getOwner(), hitDamage,
						HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(1329));
				heal(hitDamage / 5);
			}
		}, 2);
		return true;
	}
}