package net.nocturne.game.player.controllers.trollinvasion;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

@SuppressWarnings("serial")
public class Cliff_Troll extends NPC {

	private TrollInvasion Controller;

	public Cliff_Troll(int id, WorldTile tile, TrollInvasion Controller) {
		super(id, tile, -1, true, true);
		this.Controller = Controller;
		setForceMultiArea(true);
		setNoDistanceCheck(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		Controller.addKill();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

}
