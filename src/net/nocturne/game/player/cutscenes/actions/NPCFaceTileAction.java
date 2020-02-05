package net.nocturne.game.player.cutscenes.actions;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.Cutscene;

public class NPCFaceTileAction extends CutsceneAction {

	private int x, y;

	public NPCFaceTileAction(int cachedObjectIndex, int x, int y,
			int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene
				.getBaseY() + y, npc.getPlane()));
	}

}
