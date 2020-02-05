package net.nocturne.game.player.actions.skills.firemaking;

import java.util.ArrayList;

import net.nocturne.game.*;
import net.nocturne.game.EffectsManager.Effect;
import net.nocturne.game.EffectsManager.EffectType;
import net.nocturne.game.npc.others.FireSpirit;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.actions.skills.firemaking.Firemaking.Fire;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.utils.Utils;

public class Bonfire extends Action {

	private final Log log;
	private final WorldObject object;
	private int count;

	public Bonfire(Log log, WorldObject object) {
		this.log = log;
		this.object = object;
	}

	public static Log getLog(int logId) {
		for (Log log : Log.values()) {
			if (log.getLogId() == logId)
				return log;
		}
		return null;
	}

	public static void addLogs(Player player, WorldObject object) {
		ArrayList<Log> possibilities = new ArrayList<>();
		for (Log log : Log.values())
			if (player.getInventory().containsItem(log.logId, 1))
				possibilities.add(log);
		Log[] logs = possibilities.toArray(new Log[possibilities.size()]);
		if (logs.length == 0)
			player.getPackets().sendGameMessage(
					"You do not have any logs to add to this fire.");
		else if (logs.length == 1)
			player.getActionManager().setAction(new Bonfire(logs[0], object));
		else
			player.getDialogueManager().startDialogue("BonfireD", logs, object);
	}

	public static double getBonfireBoostMultiplier(Player player) {
		int fmLvl = player.getSkills().getLevel(Skills.FIREMAKING);
		if (fmLvl >= 90)
			return 1.1;
		if (fmLvl >= 80)
			return 1.09;
		if (fmLvl >= 70)
			return 1.08;
		if (fmLvl >= 60)
			return 1.07;
		if (fmLvl >= 50)
			return 1.06;
		if (fmLvl >= 40)
			return 1.05;
		if (fmLvl >= 30)
			return 1.04;
		if (fmLvl >= 20)
			return 1.03;
		if (fmLvl >= 10)
			return 1.02;
		return 1.01;

	}

	private boolean checkAll(Player player) {
		if (!World.containsObjectWithId(object, object.getId())
				|| !player.getInventory().containsItem(log.logId, 1))
			return false;
		if (player.getSkills().getLevel(Skills.FIREMAKING) < log.level) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need level " + log.level
							+ " Firemaking to add these logs to a bonfire.");
			return false;
		}
		return true;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			player.getAppearence().setRenderEmote(2498);
			return true;
		}
		return false;

	}

	@Override
	public boolean process(Player player) {
		if (checkAll(player)) {
			if (Utils.random(750) == 0) {
				new FireSpirit(new WorldTile(object, 1), player);
				player.getPackets().sendGameMessage(
						"<col=ff0000>A fire spirit emerges from the bonfire.");
			}
			return true;
		}
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		player.getInventory().deleteItem(log.logId, 1);
		player.getSkillTasks().handleTask(Fire.BONFIRE, 1);
		player.getSkillTasks().handleTask(log.fire, 1);
		player.getSkills().addXp(Skills.FIREMAKING,
				Firemaking.increasedExperience(player, log.xp));
		player.setNextAnimation(new Animation(16703));
		player.setNextGraphics(new Graphics(log.gfxId));
		player.getPackets().sendGameMessage("You add a log to the fire.", true);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3, log.logId,
					Skills.FIREMAKING);
		player.getCompCapeManager().increaseRequirement(
				Requirement.BURNING_LOGS, 1);
		if (count++ == 4
				&& player.getEffectsManager().hasActiveEffect(
						EffectType.BONFIRE)) {
			player.getEffectsManager().startEffect(
					new Effect(EffectType.BONFIRE, log.boostTime * 100));
			int percentage = (int) (getBonfireBoostMultiplier(player) * 100 - 100);
			player.getPackets().sendGameMessage(
					"<col=00ff00>The bonfire's warmth increases your maximum health by "
							+ percentage + "%. This will last " + log.boostTime
							+ " minutes.");
		}
		return 6;
	}

	@Override
	public void stop(final Player player) {
		player.getEmotesManager().setNextEmoteEnd(2400);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(16702));
				player.getAppearence().setRenderEmote(-1);
			}
		}, 3);
	}

	public enum Log {
		LOG(1511, 3098, 1, 40, 6, Fire.NORMAL), OAK(1521, 3099, 15, 60, 12,
				Fire.OAK), ACHEY_TREE(2862, 3110, 1, 50, 6, Fire.ACHEY), WILLOW(
				1519, 3101, 30, 90, 18, Fire.WILLOW), TEAK(6333, 3103, 35, 105,
				24, Fire.TEAK), MAPLE(1517, 3100, 45, 135, 36, Fire.MAPLE), MAHOGANY(
				6332, 3102, 50, 157.5, 42, Fire.MAHOGANY), EUCALYPTUS(12581,
				3112, 58, 193, 48, Fire.EUCALYPTUS), YEWS(1515, 3111, 60,
				202.5, 54, Fire.YEW), MAGIC(1513, 3135, 75, 303.8, 60,
				Fire.MAGIC), ELDER(29556, 3135, 91, 403.5, 75, Fire.ELDER), BLISTERWOOD(
				21600, 3113, 76, 434, 60, Fire.BLISTER_WOOD), CURSED_MAGIC(
				13567, 3116, 82, 434, 60, Fire.CURSED_MAGIC);

		private final int logId;
		private final int gfxId;
		private final int level;
		private final int boostTime;
		private final double xp;
		private final Fire fire;

		Log(int logId, int gfxId, int level, double xp, int boostTime, Fire fire) {
			this.logId = logId;
			this.gfxId = gfxId;
			this.level = level;
			this.xp = xp;
			this.boostTime = boostTime;
			this.fire = fire;
		}

		public int getLogId() {
			return logId;
		}

	}
}