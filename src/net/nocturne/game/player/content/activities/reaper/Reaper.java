package net.nocturne.game.player.content.activities.reaper;

import java.io.Serializable;
import java.util.ArrayList;

import net.nocturne.Settings;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

public class Reaper implements Serializable {

	private static final long serialVersionUID = -8124545900972130417L;

	private transient Player player;
	public ReaperTasks task;
	public int taskAmount, xp;
	public int points;

	public ReaperTasks generateTask() {
		final ArrayList<ReaperTasks> tasks = new ArrayList<ReaperTasks>();
		for (final ReaperTasks t : ReaperTasks.values()) {
			if (player.getSkills().getCombatLevel() < t.getRequirement()
					|| (!player.hasGroupAssignments() && t.isGroup()))
				continue;
			tasks.add(t);
		}
		player.reaperTask++;
		if (player.getDonationManager().isDonator()) {
			if (player.reaperTask >= 3) {
				player.lastReaperTask = Utils.currentTimeMillis();
			} else if (player.reaperTask >= 4) {
				player.reaperTask = 0;
			}
		} else {
			if (player.reaperTask >= 1) {
				player.lastReaperTask = Utils.currentTimeMillis();
			} else if (player.reaperTask >= 2) {
				player.reaperTask = 0;
			}
		}
		/**
		 * Basically, you need to continue the dialogue here with info like
		 * "Unfortunately, you don't meet the requirements yet. Cant I just do
		 * the same where the method is being called like this
		 */
		setCurrentTask(tasks.get(Utils.random(tasks.size())));
		setTaskAmount(player.hasLargerTasks() ? giveAmount() * 2 : giveAmount());
		setXp();
		setPoints();
		return task;
	}

	public int getPoints() {
		int points = this.points;
		if (task.isGroup())
			points += this.points / 4;
		if (player.hasLargerTasks())
			points += this.points / 4;
		if (player.getRerollCount() > 1)
			points -= (this.points / 4) * (player.getRerollCount() - 1);
		return points;
	}

	private int getXp() {
		return xp;
	}

	public void setPoints(int reaperpoints) {
		this.points = reaperpoints;
	}

	private int setPoints() {
		switch (task.getName()) {
		// case 19457: ARAXXOR
		// return this.points = 20;
		// case 2025: BARROWS
		// return this.points = 7;
		// case 17161: VORAGO
		// return this.points = 25;
		case "King Black Dragon":
		case "Giant Mole":
			return this.points = 7;
		case "Queen Black Dragon":
		case "Kalphite Queen":
		case "Exiled Kalphite Queen":
		case "Chaos Elemental":
		case "Dagannoth Rex":
		case "Dagannoth Supreme":
		case "Dagannoth Prime":
		case "TzTok-Jad":
		case "TokHaar-Jad":
			return this.points = 10;
		case "Corporeal Beast":
		case "General Graardor":
		case "Kree'arra":
		case "K'ril Tsutsaroth":
		case "Commander Zilyana":
		case "Legio Primus":
		case "Legio Secundus":
		case "Legio Tertius":
		case "Legio Quartus":
		case "Legio Quintus":
		case "Legio Sextus":
			return this.points = 12;
		case "Kalphite King":
			return this.points = 15;
		case "Nex":
			return this.points = 17;
		}
		player.getVarsManager().sendVarBit(22905, points);
		return this.points = 7;
	}

	private int setXp() {
		switch (task.getName()) {
		// case 19457: ARAXXOR
		// return this.points = 20;
		// case 2025: BARROWS
		// return this.points = 7;
		// case 17161: VORAGO
		// return this.points = 25;
		case "King Black Dragon":
		case "Giant Mole":
			return this.xp = 7000;
		case "Queen Black Dragon":
		case "Kalphite Queen":
		case "Exiled Kalphite Queen":
		case "Chaos Elemental":
		case "Dagannoth Rex":
		case "Dagannoth Supreme":
		case "Dagannoth Prime":
		case "TzTok-Jad":
		case "TokHaar-Jad":
			return this.xp = 10000;
		case "Corporeal Beast":
		case "General Graardor":
		case "Kree'arra":
		case "K'ril Tsutsaroth":
		case "Commander Zilyana":
		case "Legio Primus":
		case "Legio Secundus":
		case "Legio Tertius":
		case "Legio Quartus":
		case "Legio Quintus":
		case "Legio Sextus":
			return this.xp = 12000;
		case "Kalphite King":
			return this.xp = 15000;
		case "Nex":
			return this.xp = 17000;
		}
		return this.xp = 7000;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int giveAmount() {
		if (task.getName().equalsIgnoreCase("TzTok-Jad")
				|| task.getName().equalsIgnoreCase("TokHaar-Jad"))
			return 10;
		switch (Utils.random(5)) {
		case 0:
			return 14;
		case 1:
			return 17;
		case 2:
			return 19;
		case 3:
			return 22;
		case 4:
			return 34;
		}
		return 20;
	}

	public int getAmount() {
		return taskAmount;
	}

	public ReaperTasks getCurrentTask() {
		return task;
	}

	public boolean hasTask() {
		return task != null;
	}

	public void setCurrentTask(ReaperTasks task) {
		this.task = task;
	}

	public void setTaskAmount(int amount) {
		this.taskAmount = amount;
	}

	public void checkKillsLeft() {
		if (task == null) {
			player.getPackets().sendGameMessage(
					"You have no reaper assignment.");
			return;
		}
		player.getPackets().sendGameMessage(
				"You are currently assigned to collect souls from: "
						+ task.getName() + ".");
		player.getPackets().sendGameMessage(
				"You must retrieve " + getAmount()
						+ " to complete your assignment.");
	}

	public void updateTask() {
		if (getAmount() < (player.getDonationManager().isDonator() ? 4 : 2)) {
			player.getPackets().sendGameMessage(
					"<col=009a00>You have completed Death's assignment, gaining "
							+ Utils.format(getXp() * Settings.XP_RATE / 2)
							+ " Slayer XP and " + getPoints()
							+ " reaper points.");
			player.getSkills().addSkillXpRefresh(18,
					getXp() * Settings.XP_RATE / 2);
			player.setReaperPoints(player.getReaperPoints() + getPoints());
			setCurrentTask(null);
			setTaskAmount(0);
			xp = 0;
			points = 0;
			player.resetRerollCount();
			return;
		}
		taskAmount--;
	}
}