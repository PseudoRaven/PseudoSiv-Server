package net.nocturne.game.npc.combat.impl;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.Projectile;
import net.nocturne.game.World;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.CombatScript;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.npc.dungeonnering.DungeonSlayerNPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.actions.skills.dungeoneering.DungeonManager;
import net.nocturne.utils.Utils;

public class Soulgazer extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 10705 };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		DungeonSlayerNPC dungeonNPC = (DungeonSlayerNPC) npc;
		DungeonManager manager = dungeonNPC.getManager();
		if (manager.isDestroyed())
			return -1;
		npc.setNextAnimation(new Animation(13779));
		for (Player player : manager.getParty().getTeam()) {
			if (!player.withinDistance(npc, 8)
					|| !npc.clipedProjectile(target, true))
				continue;
			Projectile projectile = World.sendProjectileNew(npc, target, 2615,
					41, 16, 35, 2, 10, 0);
			delayHit(
					npc,
					Utils.projectileTimeToCycles(projectile.getEndTime()) - 1,
					target,
					getMagicHit(npc,
							getMaxHit(npc, NPCCombatDefinitions.MAGE, target)));
		}
		return npc.getAttackSpeed();
	}
}
