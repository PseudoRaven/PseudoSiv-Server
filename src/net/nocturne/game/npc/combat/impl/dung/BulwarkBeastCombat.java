package net.nocturne.game.npc.combat.impl.dung;

import java.util.ArrayList;
import java.util.List;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.combat.impl.KalphiteQueenCombat;
import net.nocturne.game.npc.dungeonnering.BulwarkBeast;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class BulwarkBeastCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bulwark beast" };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		((BulwarkBeast) npc).refreshBar();

		final NPCCombatDefinitions defs = npc.getCombatDefinitions();

		if (Utils.random(15) == 0) {
			List<Entity> targets = npc.getPossibleTargets();
			npc.setNextAnimation(new Animation(13007));
			for (Entity t : targets) {
				if (Utils.isOnRange(t.getX(), t.getY(), t.getSize(),
						npc.getX(), npc.getY(), npc.getSize(), 0)) {
					t.setNextGraphics(new Graphics(2400));
					delayHit(
							npc,
							1,
							t,
							getRegularHit(
									npc,
									1 + Utils.random((int) (npc
											.getMaxHit(NPCCombatDefinitions.MELEE) * 0.7))));
				}
			}
			return npc.getAttackSpeed();
		}

		// mage, range, melee
		int attackStyle = Utils.random(Utils.isOnRange(target.getX(),
				target.getY(), target.getSize(), npc.getX(), npc.getY(),
				npc.getSize(), 0) ? 3 : 2);
		switch (attackStyle) {
		case 0:
			npc.setNextAnimation(new Animation(13004));
			npc.setNextGraphics(new Graphics(2397));
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					KalphiteQueenCombat.attackMageTarget(
							new ArrayList<Player>(), npc, npc, target, 2398,
							2399);
				}

			});
			break;
		case 1:
			npc.setNextAnimation(new Animation(13006));
			npc.setNextGraphics(new Graphics(2394));
			List<Entity> targets = npc.getPossibleTargets();
			for (Entity t : targets) {
				World.sendProjectile(npc, t, 2395, 35, 30, 41, 40, 0, 0);
				t.setNextGraphics(new Graphics(2396, 75, 0));
				delayHit(
						npc,
						1,
						t,
						getRangedHit(npc,
								getMaxHit(npc, NPCCombatDefinitions.RANGE, t)));
			}
			break;
		case 2:
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			delayHit(
					npc,
					0,
					target,
					getMeleeHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MELEE, target)));
			break;
		}
		return npc.getAttackSpeed();
	}
}
