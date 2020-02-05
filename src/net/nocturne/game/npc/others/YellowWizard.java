package net.nocturne.game.npc.others;

import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.RunespanController;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class YellowWizard extends NPC {

	private RunespanController Controller;
	private long spawnTime;

	public YellowWizard(WorldTile tile, RunespanController Controller) {
		super(15430, tile, -1, true, true);
		spawnTime = Utils.currentTimeMillis();
		this.Controller = Controller;
	}

	@Override
	public void processNPC() {
		if (spawnTime + 300000 < Utils.currentTimeMillis())
			finish();
	}

	@Override
	public void finish() {
		Controller.removeWizard();
		super.finish();
	}

	public static void giveReward(Player player) {

	}

	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == Controller.getPlayer()
				&& super.withinDistance(tile, distance);
	}

}
