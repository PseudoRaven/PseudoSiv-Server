package net.nocturne.game.player.content.activities.skillertasks;

import java.io.Serializable;
import java.util.ArrayList;

import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Utils;

/**
 * @author King Fox & Miles Black August 19, 2014 Handles the Skiller Task
 *         Assignments
 */

public class SkillerTasks implements Serializable {
	private static final long serialVersionUID = 3535856484888897359L;

	public final static int EASY = 0;
	public final static int MEDIUM = 1;
	public final static int HARD = 2;
	public final static int ELITE = 3;

	private SkillTasks task;
	private int taskAmount;
	private transient Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Adds all tasks to an array that they meet the requirements then picks a
	 * random task from that list.
	 * 
	 * @param tier
	 *            difficulty
	 */
	public SkillTasks getNewTask(int tier) {
		ArrayList<SkillTasks> tasks = new ArrayList<>();
		for (SkillTasks t : SkillTasks.values())
			if (t.getDifficulty() == tier)
				tasks.add(t);
		setCurrentTask(tasks.get(Utils.random(tasks.size() - 1)));
		setTaskAmount(task.getAmount());
		return task;
	}

	/**
	 * Set's the current task to be completed. if null, no task
	 * 
	 * @param task
	 *            Task
	 */
	public void setCurrentTask(SkillTasks task) {
		this.task = task;
	}

	/**
	 * Returns the current assigned task
	 * 
	 * @return task
	 */
	public SkillTasks getCurrentTask() {
		return task;
	}

	/**
	 * Sets the amount of the task to be completed. Decreases by 1 until ask is
	 * 0, at which points its completed.
	 * 
	 * @param amount
	 *            of the task to be completed
	 */
	private void setTaskAmount(int amount) {
		this.taskAmount = amount;
	}

	/**
	 * Returns the current task amount.
	 * 
	 * @return taskAmount
	 */
	public int getTaskAmount() {
		return taskAmount;
	}

	/**
	 * Checks if player has a task, if returns null, means there is no task.
	 *
	 */
	public boolean hasTask() {
		return task != null;
	}

	/**
	 * Checks if their task is completed.
	 * 
	 * @return true if they have a task, and the amount remaining is 0,
	 *         otherwise false
	 */
	public boolean isCompleted() {
		return hasTask() && taskAmount == 0;
	}

	/**
	 * Checks if the element matches the current skiller task. If the element
	 * matches this criteria then the task amount will decrease by the given
	 * amount.
	 * 
	 * @param element
	 *            matches the current skiller task.
	 * @param amount
	 *            will decrease by the given amount.
	 */
	public void handleTask(Object element, int amount) {
		if (!hasTask() || taskAmount == 0 || task.getElement() != element)
			return;
		taskAmount -= amount;
		if (taskAmount == 0)
			completeTask();
	}

	/**
	 * Handles the completion of a skiller task.
	 */
	private void completeTask() {
		switch (player.getSkillTasks().getCurrentTask().getDifficulty()) {
		case SkillerTasks.EASY:
			giveReward(player, 1);
			break;
		case SkillerTasks.MEDIUM:
			giveReward(player, 3);
			break;
		case SkillerTasks.HARD:
			giveReward(player, 7);
			break;
		case SkillerTasks.ELITE:
			giveReward(player, 10);
			break;
		}

		player.getSkillTasks().setCurrentTask(null);
		player.getPackets()
				.sendGameMessage(
						"<col=00FFFF>Your task has been completed! Return to Max for another assignment!</col>");
	}

	private void giveReward(Player player, int taskPoints) {
		player.setTaskPoints(player.getTaskPoints() + taskPoints);
		player.getMoneyPouch().setAmount(taskPoints * 100000, false);
	}

}
