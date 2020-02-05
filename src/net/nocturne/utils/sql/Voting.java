package net.nocturne.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.nocturne.Settings;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.game.World;
import net.nocturne.game.player.CompletionistCapeManager.Requirement;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;

import com.mysql.jdbc.Statement;

/**
 * @author Miles Black (bobismyname)
 * @author Pax M
 * @date Dec 8, 2016
 */

public class Voting {

	private final static String URL = "jdbc:mysql://cpanel.blazingfast.io:3306/elveron_voting";
	private final static String USER = "elveron_server";
	private final static String PASS = "Alligator0118!";

	public static void checkAuth(Player player, String authCode)
			throws SQLException {
		if (!Settings.SQL) {
			Logger.logErr("Voting", "Voting denied access, service is closed.");
			player.getPackets().sendGameMessage(Color.RED,
					"Voting has been temporarily disabled.");
			return;
		}
		Connection conn;
		Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASS);
			if (conn != null) {
				stmt = (Statement) conn.createStatement();
				String sql = "SELECT rewardid FROM claim WHERE status='"
						+ authCode + "'";
				ResultSet rs = stmt.executeQuery(sql);
				int itemId = -1, amount = 1;
				while (rs.next()) {
					int id = Integer.parseInt(rs.getString("rewardid"));
					switch (id) {
					case 1: // 500,000 coins
						itemId = 995;
						amount = 500000;
						break;
					case 3: // 15 spin tickets
						itemId = 24154;
						amount = 15;
						break;
					case 5: // 1 mystery box
						itemId = 6199;
						break;
					case 6: // 3 vote points
						itemId = 29977;
						amount = 3;
						break;
					}
				}
				if (itemId == -1) {
					player.getPackets().sendGameMessage(
							"You do not have a reward waiting for you.");
					return;
				}
				if (Settings.DOUBLE_VOTES)
					amount *= 2;
				if (!(ItemDefinitions.getItemDefinitions(itemId).isNoted() || ItemDefinitions
						.getItemDefinitions(itemId).isStackable())
						&& amount > player.getInventory().getFreeSlots()
						&& itemId != 24154 && itemId != 29977) {
					player.getPackets()
							.sendGameMessage(
									"Please make enough room in your inventory for your reward.");
					return;
				}
				sql = "DELETE FROM claim WHERE status='" + authCode + "'";
				stmt.execute(sql);
				rs.close();
				stmt.close();
				conn.close();
				String name = ItemDefinitions.getItemDefinitions(itemId)
						.getName().toLowerCase();
				player.getPackets().sendGameMessage(Color.GREEN,
						"You've received your vote reward! Congratulations!");
				if (itemId == 24154) {
					player.getTreasureHunter().handleEarnedKeys(amount);
					name = "treasure hunter keys";
				} else if (itemId == 29977) {
					player.setVotePoints(player.getVotePoints() + amount);
					name = "vote points";
				} else
					player.getBank().addItem(itemId, amount, true);
				World.sendWorldMessage(
						"<col=ffffff><shad=198F9E>News: </col></shad>"
								+ player.getUsername().replace("_", " ")
								+ " <col=ffffff><shad=198F9E>has just voted for a "
								+ name
								+ "!"
								+ (Settings.DOUBLE_VOTES ? " Double Vote Rewards are ACTIVE!"
										: "") + "", false);
				player.getCompCapeManager().increaseRequirement(
						Requirement.VOTES, 1);
			} else {
				player.getPackets()
						.sendGameMessage(
								Color.BLUE,
								"ERROR 502: Connection to the voting database has failed, please contact a developer!");
				System.out.println("Failed to make connection!");
			}
		} catch (Exception e) {
			Logger.handle(e, "Voting");
		}
	}

}