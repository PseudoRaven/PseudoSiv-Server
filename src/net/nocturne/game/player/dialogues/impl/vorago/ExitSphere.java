package net.nocturne.game.player.dialogues.impl.vorago;

import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.WorldObject;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.vorago.VoragoHandler;
import net.nocturne.game.player.dialogues.Dialogue;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class ExitSphere extends Dialogue {

	private WorldObject exitSphere = new WorldObject(84960, 10, 0,
			new WorldTile(3104, 5985, 0));

	@Override
	public void start() {
		sendOptionsDialogue("Are you ready to leave?", "Leave.", "Stay here.");
		stage = 1;
	}

	@Override
	public void run(int interfaceId, int componentId, int slotId) {
		switch (stage) {
		case 1:
			switch (componentId) {
			case OPTION_1:
				World.sendProjectileNew(exitSphere, player, 4029, 5, 5, 0, 1.3,
						0, 0);
				WorldTasksManager.schedule(new WorldTask() {
					public void run() {
						player.setNextWorldTile(new WorldTile(3050, 6119, 0));
						player.setNextGraphics(new Graphics(4030));
						VoragoHandler.removePlayer(player);
						player.setCantWalk(false);
						if (VoragoHandler.getPlayersCount() == 1) {
							World.removeObject(exitSphere);
							VoragoHandler.endFight();
						}
						stop();
					}
				}, 2);
				end();
				break;
			case OPTION_2:
				player.setCantWalk(false);
				end();
				break;
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}

}
