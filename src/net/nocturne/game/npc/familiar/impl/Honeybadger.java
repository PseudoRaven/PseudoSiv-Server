package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;

public class Honeybadger extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7544357522011400153L;

	public Honeybadger(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Insane ferocity";
	}

	@Override
	public String getSpecialDescription() {
		return "Decreases the owner's Magic, Range, and Defence, but also increasing Strength and Attack, there is also a chance of hitting twice.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = getOwner();
		int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
		int realLevel = player.getSkills().getLevel(Skills.ATTACK);
		int level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.ATTACK,
				(int) (level + (realLevel * 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
		realLevel = player.getSkills().getLevel(Skills.STRENGTH);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.STRENGTH,
				(int) (level + (realLevel * 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
		realLevel = player.getSkills().getLevel(Skills.DEFENCE);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(
				Skills.DEFENCE,
				(int) (level > (realLevel * 0.15) ? level - (realLevel * 0.15)
						: 1));// (int)
								// (level
								// -
								// (realLevel
								// *
								// 0.15)));

		actualLevel = player.getSkills().getLevel(Skills.MAGIC);
		/*
		 * realLevel = player.getSkills().getLevel(Skills.MAGIC); level =
		 * actualLevel > realLevel ? realLevel : actualLevel;
		 */
		player.getSkills().set(Skills.MAGIC, level > 5 ? level - 5 : 1);

		actualLevel = player.getSkills().getLevel(Skills.RANGED);
		realLevel = player.getSkills().getLevel(Skills.RANGED);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(
				Skills.RANGED,
				(int) (level > (realLevel * 0.1) ? level - (realLevel * 0.1)
						: 1)/*
							 * ( int ) ( level - ( realLevel * 0.1 ) )
							 */);
		setNextAnimation(new Animation(7930));
		setNextGraphics(new Graphics(1397));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1399));
		return false;
	}
}
