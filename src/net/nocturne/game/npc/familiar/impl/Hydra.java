package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;

public class Hydra extends Familiar {

	private static final long serialVersionUID = -3444038441050159955L;

	public Hydra(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Regrowth";
	}

	@Override
	public String getSpecialDescription() {
		return "Automatically regrows the associated tree without delay.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		setNextGraphics(new Graphics(1487));
		setNextAnimation(new Animation(7939));
		player.getFarmingManager().resetTreeTrunks();
		return false;
	}
}
