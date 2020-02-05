package net.nocturne.game.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.nocturne.game.Animation;
import net.nocturne.game.Graphics;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

/**
 * @author Miles Black (bobismyname)
 * @date Jan 1, 2017
 */

public class AssistanceManager {

	private Player player, other;
	private boolean assisting;
	private Date lastAssist;
	private List<Integer> skills = new ArrayList<Integer>();
	private HashMap<Integer, Integer> xpEarned = new HashMap<Integer, Integer>();

	AssistanceManager(Player player) {
		this.player = player;
		this.assisting = false;
	}

	private void setOther(Player other) {
		this.other = other;
		this.assisting = false;
	}

	public boolean isAssisting() {
		return assisting;
	}

	private void setSkill(int skillId) {
		if (!skills.contains(skillId)) {
			skills.add(skillId);
			player.getPackets().sendGameMessage(
					Color.YELLOW,
					player.getSkills().getSkillName(skillId)
							+ " has been enabled for assistance.");
		} else if (skills.contains(skillId)) {
			skills.remove(skillId);
			player.getPackets().sendGameMessage(
					Color.YELLOW,
					player.getSkills().getSkillName(skillId)
							+ " has been disabled for assistance.");
		}
	}

	public void acceptRequest(Player other) {
		if (!player.getAcceptAid().isAcceptingAid(
				AcceptAid.ASSISTANCE_REQUESTS, other)) {
			other.getPackets().sendGameMessage(
					"This player currently has accept aid turned off.");
			return;
		}
		this.other = other;
		this.assisting = true;
		player.setNextFaceEntity(other);
		other.setNextFaceEntity(player);
		WorldTasksManager.schedule(new WorldTask() {
			int stage = 0;

			@Override
			public void run() {
				if (stage == 0)
					player.setNextAnimation(new Animation(7299));
				else if (stage == 1)
					player.setNextGraphics(new Graphics(1247));
				else if (stage == 2)
					other.setNextAnimation(new Animation(7299));
				stage++;
			}
		}, 3);
		other.getAssistanceManager().setOther(other);
		player.getPackets().sendVar(1180, 1);
		player.getPackets().sendHideIComponent(745, 3, false);
		other.getPackets().sendHideIComponent(745, 3, false);
		player.getInterfaceManager().setWindowInterface(364, 745);
		other.getInterfaceManager().setWindowInterface(364, 745);
		player.getInterfaceManager().sendLockGameTab(
				InterfaceManager.INVENTORY_TAB, true);
		player.getPackets().sendIComponentText(301, 10,
				"You are currently assisting " + other.getUsername() + ".");
		player.getInterfaceManager().sendCentralInterface(301);
		player.getPackets().sendGameMessage(
				"You are now assisting " + other.getUsername() + ".");
		other.getPackets().sendGameMessage(
				"You are now being assisted by " + player.getUsername() + ".");
		player.setCloseInterfacesEvent(() -> {
			this.other = null;
			this.assisting = false;
			other.getAssistanceManager().setOther(null);
			player.getPackets().sendHideIComponent(745, 3, true);
			other.getPackets().sendHideIComponent(745, 3, true);
			player.closeInterfaces();
			player.getInterfaceManager().sendLockGameTab(
					InterfaceManager.INVENTORY_TAB, false);
			player.getPackets().sendGameMessage(
					"You have stopped assisting " + other.getUsername() + ".");
			other.getPackets().sendGameMessage(
					"You are no longer being assisted by "
							+ player.getUsername() + ".");
		});
	}

	boolean hasLevel(int skillId, int level) {
		if (other == null || assisting == false || !skills.contains(skillId)
				|| other.getSkills().getLevel(skillId) < level)
			return false;
		return true;
	}

	double handleXP(int skillId, double exp) {
		if (TimeUnit.MILLISECONDS.toDays(lastAssist.getTime()
				- new Date().getTime()) >= 1)
			xpEarned = new HashMap<Integer, Integer>();
		double totalXp = xpEarned.values().stream().mapToInt(Number::intValue)
				.sum();
		double completeXp = exp;
		if (totalXp + exp >= 30000) {
			if (totalXp >= 30000)
				completeXp = 0;
			else
				completeXp = totalXp + exp - 30000;
			if (lastAssist == null)
				lastAssist = new Date();
		}
		xpEarned.put(skillId, new Double(completeXp).intValue());
		player.getPackets().sendIComponentText(301,
				getComponentForSkill(skillId),
				Utils.format(xpEarned.get(skillId)));
		player.getPackets().sendIComponentText(301, 84, Utils.format(totalXp));
		return completeXp;
	}

	public boolean handleButtons(int componentId) {
		switch (componentId) {
		case 74:
			setSkill(Skills.RUNECRAFTING);
			return true;
		case 75:
			setSkill(Skills.CRAFTING);
			return true;
		case 76:
			setSkill(Skills.FLETCHING);
			return true;
		case 77:
			setSkill(Skills.CONSTRUCTION);
			return true;
		case 78:
			setSkill(Skills.FARMING);
			return true;
		case 79:
			setSkill(Skills.MAGIC);
			return true;
		case 80:
			setSkill(Skills.SMITHING);
			return true;
		case 81:
			setSkill(Skills.COOKING);
			return true;
		case 82:
			setSkill(Skills.HERBLORE);
			return true;
		}
		return false;
	}

	int getComponentForSkill(int skillId) {
		switch (skillId) {
		case Skills.RUNECRAFTING:
			return 46;
		case Skills.CRAFTING:
			return 48;
		case Skills.FLETCHING:
			return 50;
		case Skills.CONSTRUCTION:
			return 52;
		case Skills.FARMING:
			return 54;
		case Skills.MAGIC:
			return 56;
		case Skills.SMITHING:
			return 58;
		case Skills.COOKING:
			return 60;
		case Skills.HERBLORE:
			return 62;
		default:
			return -1;
		}
	}

	boolean isValidSkill(int skillId) {
		switch (skillId) {
		case Skills.RUNECRAFTING:
		case Skills.CRAFTING:
		case Skills.FLETCHING:
		case Skills.CONSTRUCTION:
		case Skills.FARMING:
		case Skills.MAGIC:
		case Skills.SMITHING:
		case Skills.COOKING:
		case Skills.HERBLORE:
			return true;
		default:
			return false;
		}
	}

}
