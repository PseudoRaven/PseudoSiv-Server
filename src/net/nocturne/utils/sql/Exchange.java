package net.nocturne.utils.sql;

import java.io.IOException;
import java.sql.PreparedStatement;

import net.nocturne.cache.Cache;
import net.nocturne.game.item.ItemConstants;
import net.nocturne.game.player.content.grandExchange.GrandExchange;
import net.nocturne.game.player.content.grandExchange.Offer;
import net.nocturne.utils.Utils;

public class Exchange implements Runnable {

	public static void main(String[] args) throws IOException {
		Cache.init();
		GrandExchange.init();
		new Thread(new Exchange()).start();
	}

	@Override
	public void run() {
		try {
			Database db = new Database("185.62.188.4", "elveron_server",
					"Alligator0118!", "elveron_main");

			if (!db.initBatch()) {
				System.err
						.println("[DATABASE] No connection could be made to the database.");
				return;
			}

			db.executeQuery("TRUNCATE TABLE offers");
			PreparedStatement stmt = db
					.prepare("INSERT INTO offers (price, itemId, itemName, quantity, sold, type, ovalue) VALUES (?, ?, ?, ?, ?, ?, ?)");

			int count = 0;
			long time = Utils.currentTimeMillis();

			for (Offer o : GrandExchange.getOffers().values()) {

				if (o.getPrice() <= 1 || o.isCompleted()
						|| !ItemConstants.isTradeable(o)) {
					continue;
				}

				stmt.setInt(1, o.getPrice());
				stmt.setInt(2, o.getId());
				stmt.setString(3, o.getName());
				stmt.setInt(4, o.getAmount());
				stmt.setInt(5, o.getTotalAmountSoFar());
				stmt.setInt(6, o.isBuying() ? 1 : 0);
				stmt.setInt(7, o.getDefinitions().getValue());
				stmt.addBatch();
				count++;
				if (count % 500 == 0) { // execute every 500 statements
					stmt.executeBatch();
				}
			}

			stmt.executeBatch();
			long end = Utils.currentTimeMillis();
			System.err.println("Executed " + count + " queries in "
					+ (end - time) + " ms!");
			db.destroyAll();
		} catch (Exception e) {

		}
	}

}