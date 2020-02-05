package net.nocturne.game.npc.others;

import net.nocturne.game.Entity;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.godwars.bandos.GodwarsBandosFaction;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.player.controllers.Controller;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class Cyclopse extends GodwarsBandosFaction {

	public Cyclopse(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (source instanceof Player) {
			WarriorsGuild.killedCyclopses++;
			final NPC npc = this;
			final Player player = (Player) source;
			Controller controller = player.getControllerManager()
					.getController();
			if (controller == null || !(controller instanceof WarriorsGuild)
					|| Utils.random(15) != 0)
				return;
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					World.addGroundItem(
							new Item(WarriorsGuild.getBestDefender(player)),
							new WorldTile(getCoordFaceX(npc.getSize()),
									getCoordFaceY(npc.getSize()), getPlane()),
							player, true, 60);
				}
			}, getCombatDefinitions().getDeathDelay());
		}
	}
}