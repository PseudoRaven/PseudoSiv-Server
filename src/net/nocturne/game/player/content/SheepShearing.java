package net.nocturne.game.player.content;

import net.nocturne.game.Animation;
import net.nocturne.game.ForceTalk;
import net.nocturne.game.item.Item;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class SheepShearing {

	private static final Animation SHEARING = new Animation(893);

	public static void shearAttempt(final Player player, final NPC npc) {

		if (!player.getInventory().containsItemToolBelt(ItemIdentifiers.SHEARS)
				&& !player.getInventory().containsOneItem(
						ItemIdentifiers.SHEARS)) {
			player.getPackets().sendGameMessage(
					"You need a pair of shears in order to sheer the sheep.");
			return;
		}
		final boolean isBlack = npc.getId() == 8876;

		player.lock(3);
		npc.setBoundDelay(5);
		player.setNextAnimation(SHEARING);
		if (Utils.random(5) != 0) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					npc.faceEntity(player);
					player.getInventory().addItem(
							new Item(isBlack ? 15415 : 1737, 1));
					npc.setNextNPCTransformation(isBlack ? 8877
							: npc.getId() == 43 ? 42 : 5152);
					player.getPackets().sendGameMessage("You get some wool.");
				}
			}, 2);
		} else {
			player.getPackets().sendGameMessage(
					"The sheep manages to get away from you.");
			npc.addWalkSteps(player.getX() - 5, player.getX() - 5);
			if (Utils.random(2) == 0)
				npc.setNextForceTalk(new ForceTalk("Baaa"));
		}
	}
}