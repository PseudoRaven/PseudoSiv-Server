package net.nocturne.game.player.actions.skills.agility;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;

public class Agility {

	public enum AgilityCourses {
		AGILITY_PYRAMID, APE_ATOLL, BARBARIAN_OUTPOST, BARBARIAN_OUTPOST_ADVANCED, GNOME_AGILITY, GNOME_AGILITY_ADVANCED, WILDERNESS_AGILITY, HEFIN_AGILITY;
	}

	public static boolean hasLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.AGILITY) < level) {
			player.getPackets().sendGameMessage(
					"You need an agility level of " + level
							+ " to use this obstacle.", true);
			return false;
		}
		return true;
	}

	public static void sendSlipperyLog(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;
			final boolean running = player.getRun();

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(155);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7.5);
					player.getPackets().sendGameMessage(
							"... and make it safely to the other side.", true);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void sendClothingLine(final Player player) {
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;
			final boolean running = player.getRun();

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearence().setRenderEmote(155);
				} else {
					player.getAppearence().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getPackets().sendGameMessage(
							"You passed the clothing line successfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

}
