package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;

public class Steeltitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6377458256826528627L;

	public Steeltitan(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setNextAnimation(new Animation(8188));
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public String getSpecialName() {
		return "Steel of Legends";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence boost only applies to melee attacks. Scroll initiates attack on opponent, hitting four times, with either ranged or melee, depending on the distance to the target";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return true;
	}
}
