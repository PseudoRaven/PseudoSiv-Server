package net.nocturne.game.npc.fightpits;

import java.util.ArrayList;

import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.FightPits;

@SuppressWarnings("serial")
public class FightPitsNPC extends NPC {

	public FightPitsNPC(int id, WorldTile tile) {
		super(id, tile, -1, true, true);
		setNoDistanceCheck(true);
	}

	@Override
	public void sendDeath(Entity source) {
		setNextGraphics(new Graphics(2924 + getSize()));
		super.sendDeath(source);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (Player player : FightPits.arena)
			possibleTarget.add(player);
		return possibleTarget;
	}

}
