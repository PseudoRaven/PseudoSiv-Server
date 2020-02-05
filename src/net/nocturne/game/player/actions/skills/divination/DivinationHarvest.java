package net.nocturne.game.player.actions.skills.divination;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Trenton
 * 
 */
public class DivinationHarvest extends Action {

	private Wisp wisp;
	private WispInfo info;

	public DivinationHarvest(Player player, Object[] args) {
		this.wisp = (Wisp) args[0];
		this.info = (WispInfo) args[1];
	}

	public static boolean checkAll(Player player, WispInfo info) {
		if (player.getSkills().getLevel(Skills.DIVINATION) < info.getLevel()) {
			player.getPackets().sendGameMessage(
					"You need a Divination level of " + info.getLevel()
							+ " to harvest from this spring.");
			return false;
		}
		if (player.getInventory().getFreeSlots() == 0) {
			player.getPackets().sendGameMessage(
					"You don't have enough space in your inventory.");
			return false;
		}
		return true;
	}

	public NPC getWisp() {
		return wisp;
	}

	public WispInfo getInfo() {
		return info;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player, info)) {
			return false;
		}
		player.setNextAnimation(new Animation(21231));
		player.faceEntity(wisp);
		return true;
	}

	@Override
	public boolean process(Player player) {
		return !(wisp == null || wisp.isUsedUp()) && checkAll(player, info);
	}

	@Override
	public void stop(Player player) {
		player.setNextAnimation(new Animation(21229));
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().addItem(info.getEnergyId(),
				player.getBoon(info.ordinal()) ? 2 : 1);
		int chance = Utils.random(2);
		if (Utils.random(30) >= 10 && info != WispInfo.NEGATIVE
				&& info != WispInfo.POSITIVE && info != WispInfo.ANCESTRAL) {
			if (info != WispInfo.PALE
					&& (Utils.random(30) <= 5 + (player.getSkills().getLevel(
							Skills.DIVINATION) - info.getLevel()))) {
				player.getSkills()
						.addXp(Skills.DIVINATION, info.getHarvestXp());
				player.setNextGraphics(new Graphics(4236));
				player.getInventory().addItem(info.getEnrichedMemoryId(), 1);
			} else {
				player.getSkills()
						.addXp(Skills.DIVINATION, info.getHarvestXp());
				player.setNextGraphics(new Graphics(4235));
				if (chance != 2)
					player.getInventory().addItem(info.getMemoryId(), 1);
			}
		} else {
			player.getSkills().addXp(Skills.DIVINATION, info.getHarvestXp());
		}
		player.getSkillTasks().handleTask(info, 1);
		return 2;
	}

}