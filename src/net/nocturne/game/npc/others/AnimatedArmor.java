package net.nocturne.game.npc.others;

import net.nocturne.game.Animation;
import net.nocturne.game.Entity;
import net.nocturne.game.World;
import net.nocturne.game.WorldTile;
import net.nocturne.game.item.Item;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.content.activities.minigames.WarriorsGuild;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

@SuppressWarnings("serial")
public class AnimatedArmor extends NPC {

	private transient Player player;

	public AnimatedArmor(Player player, int id, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.player = player;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!getCombat().underCombat())
			finish();
	}

	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (source instanceof Player) {
						Player player = (Player) source;
						for (Integer items : getDroppedItems()) {
							if (items == -1)
								continue;
							World.addGroundItem(new Item(items), new WorldTile(
									getCoordFaceX(getSize()),
									getCoordFaceY(getSize()), getPlane()),
									player, true, 60);
						}
						player.setWarriorPoints(3,
								WarriorsGuild.ARMOR_POINTS[getId() - 4278]);
					}
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	private int[] getDroppedItems() {
		int index = getId() - 4278;
		int[] droppedItems = WarriorsGuild.ARMOUR_SET[index].clone();
		if (Utils.random(100) == 0) // 1/100, before 15 chance of losing
			droppedItems[Utils.random(0, 2)] = -1;
		return droppedItems;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		super.finish();
		if (player != null) {
			player.getTemporaryAttributtes().remove("animator_spawned");
			if (!isDead()) {
				for (int item : getDroppedItems()) {
					if (item == -1)
						continue;
					player.getInventory().addItemDrop(item, 1);
				}
			}
		}
	}
}
