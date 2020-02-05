package net.nocturne.game.npc.dungeonnering;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.impl.dung.ToKashBloodChillerCombat;
import net.nocturne.game.player.Player;

@SuppressWarnings("serial")
public class FrozenAdventurer extends NPC {

	private transient Player player;

	public FrozenAdventurer(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
	}

	@Override
	public void processNPC() {
		if (player == null || player.isDead() || player.hasFinished()) {
			finish();
			return;
		} else if (!player.getAppearence().isNPC()) {
			ToKashBloodChillerCombat.removeSpecialFreeze(player);
			finish();
			return;
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getFrozenPlayer() {
		return player;
	}

}
