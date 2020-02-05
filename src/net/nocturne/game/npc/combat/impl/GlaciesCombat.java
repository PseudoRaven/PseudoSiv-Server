package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.content.activities.minigames.ZarosGodwars;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class GlaciesCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 13454 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		npc.setNextAnimation(new Animation(defs.getAttackEmote()));
		npc.setNextGraphics(new Graphics(3356));
		for (final Entity t : ZarosGodwars.nex.getPossibleTargets()) {
			World.sendProjectile(npc, t, 362, 20, 20, 20, 1, 10, 0);
			int damage = getMaxHit(npc, NPCCombatDefinitions.MAGE, t);
			delayHit(npc, 1, t, getMagicHit(npc, damage));
			if (damage > 0 && Utils.random(5) == 0) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						t.setBoundDelay(25);
						t.setNextGraphics(new Graphics(369));
					}
				}, 2);
			}
		}
		return npc.getAttackSpeed();
	}
}