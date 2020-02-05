package net.nocturne.game.player.actions;

import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.player.Player;

public class Healing extends Action {

	@Override
	public boolean start(Player player) {
		if (!process(player))
			return false;
		setActionDelay(player, 3);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		if (!checkAll(player))
			stop(player);
		player.setNextGraphics(new Graphics(84));
		if (player.getMaxHitpoints() / 10 > player.getMaxHitpoints()
				- player.getHitpoints())
			player.heal(player.getMaxHitpoints() - player.getHitpoints());
		else
			player.heal(player.getMaxHitpoints() / 10);
		return 1;
	}

	@Override
	public void stop(Player player) {
		return;
	}

	public boolean checkAll(Player player) {
		if (World.isSafeZone(player) && !player.isUnderCombat()
				&& player.getHitpoints() < player.getMaxHitpoints())
			return true;
		return false;
	}
}
