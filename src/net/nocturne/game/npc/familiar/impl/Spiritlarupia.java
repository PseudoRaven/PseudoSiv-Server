package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.*;
import net.nocturne.game.Hit.HitLook;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Spiritlarupia extends Familiar {

	private static final long serialVersionUID = 3060058918310360114L;

	public Spiritlarupia(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Rending";
	}

	@Override
	public String getSpecialDescription() {
		return "Attacks the player's opponent with a magic attack, and also drains opponent's Strength. ";
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
		Player player = getOwner();
		final int damage = Utils.random(107);
		setNextGraphics(new Graphics(1370));
		setNextAnimation(new Animation(7919));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		World.sendProjectile(this, target, 1371, 34, 16, 30, 35, 16, 0);
		if (damage > 20)
			if (target instanceof Player)
				((Player) target).getSkills().set(
						Skills.STRENGTH,
						((Player) target).getSkills().getLevel(Skills.STRENGTH)
								- (damage / 20));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				target.applyHit(new Hit(getOwner(), damage,
						HitLook.MAGIC_DAMAGE));
			}
		}, 2);
		return true;
	}
}
