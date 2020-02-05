package net.nocturne.game.npc.familiar.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.familiar.Familiar;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.summoning.Summoning.Pouch;

public class Packyak extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1397015887332756680L;

	public Packyak(Player owner, Pouch pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, false);
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public String getSpecialName() {
		return "Winter Storage";
	}

	@Override
	public String getSpecialDescription() {
		return "Use special move on an item in your inventory to send it to your bank.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public boolean isAgressive() {
		return false;
	}

	@Override
	public boolean submitSpecial(Object object) {
		int slotId = (Integer) object;
		if (getOwner().getBank().hasBankSpace()) {
			getOwner().getBank().depositItem(slotId, 1, false);
			getOwner().getPackets().sendGameMessage(
					"Your Pack Yak has sent an item to your bank.");
			getOwner().setNextGraphics(new Graphics(1316));
			getOwner().setNextAnimation(new Animation(7660));
			return true;
		}
		return false;
	}
}
