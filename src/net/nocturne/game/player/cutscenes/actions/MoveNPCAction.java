package net.nocturne.game.player.cutscenes.actions;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.cutscenes.Cutscene;

public class MoveNPCAction extends CutsceneAction {

	private int x, y, plane, movementType;

	public MoveNPCAction(int cachedObjectIndex, int x, int y, boolean run,
			int actionDelay) {
		this(cachedObjectIndex, x, y, -1, run ? Cutscene.RUN_MOVE_TYPE
				: Cutscene.WALK_MOVE_TYPE, actionDelay);
	}

	public MoveNPCAction(int cachedObjectIndex, int x, int y, int plane,
			int movementType, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
		this.plane = plane;
		this.movementType = movementType;
	}

	@Override
	public void process(Player player, Object[] cache) {
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		Cutscene scene = (Cutscene) cache[0];
		if (movementType == Cutscene.TELE_MOVE_TYPE) {
			npc.setNextWorldTile(new WorldTile(scene.getBaseX() + x, scene
					.getBaseY() + y, plane));
			return;
		}
		npc.setRun(movementType == Cutscene.RUN_MOVE_TYPE);
		npc.addWalkSteps(scene.getBaseX() + x, scene.getBaseY() + y, -1, false);
	}

}
