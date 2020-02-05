package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Abyssalparasite extends Familiar {

	private static final long serialVersionUID = 7051216741726595486L;

	public Abyssalparasite(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Abyssal drain";
	}

	@Override
	public String getSpecialDescription() {
		return "Lowers an opponent's prayer with a magic attack.";
	}

	@Override
	public int getBOBSize() {
		return 7;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		final int damage = Utils.random(100);
		setNextAnimation(new Animation(7675));
		setNextGraphics(new Graphics(1422));
		World.sendProjectile(this, target, 1423, 34, 16, 30, 35, 16, 0);
		if (target instanceof Player)
			((Player) target).getPrayer().drain(damage / 2);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.applyHit(new Hit(getOwner(), damage,
						HitLook.MAGIC_DAMAGE));
			}
		}, 2);
		return false;
	}
}
