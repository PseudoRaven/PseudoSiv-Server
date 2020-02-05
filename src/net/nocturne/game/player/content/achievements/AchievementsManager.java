package net.nocturne.game.player.content.achievements;

import java.io.Serializable;

import net.nocturne.Settings;
import net.nocturne.game.Graphics;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Color;
import net.nocturne.utils.Utils;

public class AchievementsManager implements Serializable {

	/**
	 * @author: Pax M
	 */

	private static final long serialVersionUID = 6967994601536944038L;

	private transient Player player;

	private int achievementPoints;

	public enum Achievements {

		/**
		 * Easy achievements
		 */
		TELEPORT_HOME_EASY(1, "Teleport to Home", "", 0, -1, -1, 0),

		BUY_EASY(2, "Buy an item from a Shop", "", 0, -1, -1, 0),

		SELL_EASY(3, "Sell an item to a Shop", "", 0, -1, -1, 0),

		ONLINE_TIME_EASY(4, "Played for an hour", "", 0, -1, -1, 0),

		DIED_EASY(5, "Have died once", "", 0, -1, -1, 0),

		KILL_NPC_EASY(6, "Killed any npc", "", 0, -1, -1, 0),

		BANK_MONEY_EASY(4, "Have over 10M in the bank", "", 1, -1, -1, 0),

		VOTE_EASY(4, "Have voted for " + Settings.SERVER_NAME, "", 1, -1, -1, 0),

		/**
		 * Medium achievements
		 */
		BANK_MONEY_MEDIUM(4, "Have over 100M in the bank", "", 1, -1, -1, 0),

		STORE_MEDIUM(4, "Purchase a product from the " + Settings.SERVER_NAME
				+ " Store", "", 1, -1, -1, 0),

		MYSTERY_BOX_MEDIUM(4, "Opened a Mystery Box", "", 1, -1, -1, 0),

		TREASURE_CHEST_MEDIUM(4, "Opened a Treasure Box", "", 1, -1, -1, 0),

		KILL_PLAYER_MEDIUM(4, "Kill a player", "", 1, -1, -1, 0),

		LOGIN_1337_MEDIUM(4, "Login exactly at 13:37", "", 1, -1, -1, 0),

		KILL_NPC_1337_MEDIUM(4, "Killed any npc exactly at 13:37", "", 1, -1,
				-1, 0),

		KILL_PLAYER_1337_MEDIUM(4, "Killed a player exactly at 13:37", "", 1,
				-1, -1, 0),

		VOTE_MEDIUM(4, "Have voted over 10 times for " + Settings.SERVER_NAME,
				"", 1, -1, -1, 0),

		/**
		 * Hard achievements
		 */
		BANK_MONEY_HARD(4, "Have over 250M in the bank", "", 1, -1, -1, 0),

		KILL_VORAGO_HARD(4, "Killed vorago", "", 2, -1, -1, 0),

		KILL_ARAXXOR_HARD(4, "Killed araxxor", "", 2, -1, -1, 0),

		VOTE_HARD(4, "Have voted over 25 times for " + Settings.SERVER_NAME,
				"", 1, -1, -1, 0),

		ACHIEVEMENT_POINTS_HARD(4, "Have over 250k achievement points", "", 1,
				-1, -1, 0),

		/**
		 * Elite achievements
		 */
		BANK_MONEY_ELITE(4, "Have over 500M in the bank", "", 3, -1, -1, 0),

		RECEIVE_PM_ELITE(4,
				"Have received a private message from a maxed player", "", 3,
				-1, -1, 0),

		VOTE_ELITE(4, "Have voted over 50 times for " + Settings.SERVER_NAME,
				"", 1, -1, -1, 0),

		ACHIEVEMENT_POINTS_ELITE(4, "Have over 500k achievement points", "", 1,
				-1, -1, 0),

		/**
		 * Master achievements
		 */
		BANK_MONEY_MASTER(4, "Have over 1000M in the bank", "", 4, -1, -1, 0),

		RECEIVE_PM_MASTER(4,
				"Have received a private message from a completionist player",
				"", 3, -1, -1, 0),

		VOTE_MASTER(4, "Have voted over 100 times for " + Settings.SERVER_NAME,
				"", 1, -1, -1, 0),

		ACHIEVEMENT_POINTS_MASTER(4, "Have over 1000k achievement points", "",
				1, -1, -1, 0),

		;

		private String name;
		private String description;

		private int id;
		private int difficulty;
		private int skillId;
		private int skillXp;
		private int achievementPoints;

		/**
		 * @param name
		 *            : name of the achievemt
		 * @param description
		 *            : more indept what the achievement is
		 * @param difficulty
		 *            : 0=easy 1=medium 2=hard 3=elite 4=master
		 * @param skillId
		 *            : the skill you get xp in
		 * @param skillXp
		 *            : the xp
		 * @param achievementPoints
		 *            : amount of points
		 */
		Achievements(int id, String name, String description, int difficulty,
				int skillId, int skillXp, int achievementPoints) {
			this.id = id;
			this.name = name;
			this.description = description;
			this.difficulty = difficulty;
			this.skillId = skillId;
			this.skillXp = skillXp;
			this.achievementPoints = achievementPoints;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public int getId() {
			return id;
		}

		public int getDifficulty() {
			return difficulty;
		}

		public int getSkillId() {
			return skillId;
		}

		public int getSkillXp() {
			return skillXp;
		}

		public int getAchievementPoints() {
			return achievementPoints;
		}
	}

	private Achievements achievement;

	public AchievementsManager(Player player) {
		this.player = player;
	}

	public void process() {

	}

	public void handleReward() {
		boolean skillReward = true;
		if (achievement.getSkillId() == -1 && achievement.getSkillXp() == -1)
			skillReward = false;
		player.setNextGraphics(new Graphics(199));
		player.setNextGraphics(new Graphics(1765));
		setAchievementPoints(getAchievementPoints()
				+ achievement.getAchievementPoints());

		setCompletedAchievement(achievement.getId());
		player.getPackets().sendGameMessage(
				Color.PURPLE,
				"Well done! You've completed the achievement: "
						+ achievement.getName()
						+ " and been awarded "
						+ (skillReward ? Utils.format(achievement.getSkillXp())
								+ " xp in "
								+ player.getSkills().getSkillName(
										achievement.getSkillId()) + " and "
								: "")
						+ Utils.format(achievement.getAchievementPoints())
						+ " achievement points.");
	}

	public Achievements getCompletedAchievement() {
		return achievement;
	}

	public void setCompletedAchievement(int achievementId) {
		// TODO: set the achievement id to completed, so you can't repeat it
	}

	public int getAchievementPoints() {
		return achievementPoints;
	}

	public void setAchievementPoints(int achievementPoints) {
		this.achievementPoints = achievementPoints;
	}
}