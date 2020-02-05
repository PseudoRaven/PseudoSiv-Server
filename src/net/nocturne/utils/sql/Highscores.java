package net.nocturne.utils.sql;

import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;

public class Highscores {

	private Player player;

	public Highscores(Player player) {
		this.player = player;
	}

	/**
	 * Generates and submits a query to DbManager to be processed in a queue
	 */
	public void submit() {
		HighscoresManager.addToQueue("DELETE FROM hs_users WHERE username='"
				+ player.getUsername() + "'");

		String query = generateQuery();

		if (query == null)
			return;

		HighscoresManager.addToQueue(query);
	}

	public String generateQuery() {
		if (player.getRights() > 1)
			return null;

		StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO hs_users (")
				.append("username, ")
				// .append("password, ")
				.append("clanId, ").append("clanRank, ").append("kills, ")
				.append("deaths, ").append("playtime, ").append("rights, ")
				.append("difficulty, ").append("overall_lvl, ")
				.append("overall_xp, ");

		for (int i = 0; i <= Skills.DIVINATION; i++) {
			if (i == 25) {
				sb.append(Skills.SKILL_NAME[i].toLowerCase() + "_lvl, ");
				sb.append(Skills.SKILL_NAME[i].toLowerCase() + "_xp)");
			} else {
				sb.append(Skills.SKILL_NAME[i].toLowerCase() + "_lvl, ");
				sb.append(Skills.SKILL_NAME[i].toLowerCase() + "_xp, ");
			}
		}

		sb.append(" VALUES (");

		String clanRank;

		int gameMode = 0;

		if (player.isHardcoreIronman())
			gameMode = 2;
		else if (player.isIronman())
			gameMode = 1;

		clanRank = "Unranked";

		sb.append("'" + player.getUsername() + "', ")
				// .append("'"+player.getPassword()+"', ")
				.append("-1, ").append("'" + clanRank + "', ")
				.append(player.getKillCount() + ", ")
				.append(player.getDeathCount() + ", ")
				.append(player.totalMinutes + ", ");

		if (player.getDonationManager().isDonator()) {
			sb.append("4, ");
		} else if (player.getDonationManager().isExtremeDonator()) {
			sb.append("5, ");
		} else if (player.getDonationManager().isLegendaryDonator()) {
			sb.append("6, ");
		} else if (player.getDonationManager().isSupremeDonator()) {
			sb.append("7, ");
		} else if (player.getDonationManager().isDivineDonator()) {
			sb.append("8, ");
		} else if (player.getDonationManager().isAngelicDonator()) {
			sb.append("9, ");
		} else if (player.getDonationManager().isDemonicDonator()) {
			sb.append("10, ");
		} else if (player.getDonationManager().isHeroicDonator()) {
			sb.append("11, ");
		} else {
			sb.append(player.getRights() + ", ");
		}

		sb.append(gameMode + ", ")
				.append(player.getSkills().getTotalLevel() + ", ")
				.append(player.getSkills().getTotalXp() + ", ");

		for (int i = 0; i <= 25; i++) {
			int exp = (int) player.getSkills().getXp(i);
			int lvl = (int) player.getSkills().getLevel(i);
			if (i == 25) {
				sb.append(lvl + ", ");
				sb.append(exp + ")");
			} else {
				sb.append(lvl + ", ");
				sb.append(exp + ", ");
			}
		}
		return sb.toString();
	}

}