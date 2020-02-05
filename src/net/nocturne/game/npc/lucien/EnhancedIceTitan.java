package net.nocturne.game.npc.lucien;

import java.util.ArrayList;

import net.nocturne.game.Entity;
import net.nocturne.game.WorldTile;
import net.nocturne.game.npc.NPC;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
class EnhancedIceTitan extends NPC {

	private Lucien lucien;

	EnhancedIceTitan(WorldTile tile, Lucien lucien) {
		super(14257, tile, -1, true, true);
		setForceMultiAttacked(true);
		setForceMultiArea(true);
		this.lucien = lucien;
		changeTarget = 2;
	}

	private int changeTarget;
	private int delay;

	public void processNPC() {
		if (isDead() || hasFinished())
			return;
		if (delay > 0) {
			delay--;
			return;
		}
		if (changeTarget > 0) {
			if (changeTarget == 1) {
				ArrayList<Entity> possibleTarget = lucien.getPossibleTargets();
				if (possibleTarget.isEmpty()) {
					finish();
					lucien.removeEnhancedTitan();
					return;
				}
				Entity target = possibleTarget.get(Utils.random(possibleTarget
						.size() - 1));
				setTarget(target);
			}
		}

	}

	public void sendDeath(Entity source) {
		super.sendDeath(source);
		lucien.removeEnhancedTitan();
	}
}
