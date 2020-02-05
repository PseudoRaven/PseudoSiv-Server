package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;

public class Irontitan extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6059371477618091701L;

	public Irontitan(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Iron Within";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts three melee attacks instead of one in the next attack.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}
}
