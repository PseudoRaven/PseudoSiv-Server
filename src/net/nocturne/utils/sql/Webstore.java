package net.nocturne.utils.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.nocturne.game.Graphics;
import net.nocturne.game.World;
import net.nocturne.game.item.ItemIdentifiers;
import net.nocturne.game.player.Player;
import net.nocturne.utils.Color;
import net.nocturne.utils.Logger;

public class Webstore {

	public static void handleWebstore(Player player, String username) {
		if (System.currentTimeMillis() - player.delay < 2500) {
			player.getPackets().sendGameMessage(
					"Please wait one second, and try again.");
			return;
		}
		player.delay = System.currentTimeMillis();
		try {
			username = username.replaceAll(" ", "_");
			String secret = "c52f1bd66cc19d05628bd8bf27af3ad6"; // YOUR SECRET
																// KEY!
			String email = "mablack01@aol.com"; // This is the one you use to
												// login into RSPS-PAY
			URL url = new URL(
					"http://rsps-pay.com/includes/listener.php?username="
							+ username + "&secret=" + secret + "&email="
							+ email);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String results = reader.readLine();
			if (results.toLowerCase().contains("!error:"))
				Logger.log(player, "[RSPS-PAY]" + results);
			else {
				String[] ary = results.split(",");
				for (int i = 0; i < ary.length; i++) {
					switch (ary[i]) {
					case "18896":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought a BOND_UNTRADEABLE. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLE has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 1, true);
						break;
					case "18897":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 2 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 2, true);
						break;
					case "18898":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 3 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 3, true);
						break;
					case "18899":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 5 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 5, true);
						break;
					case "18900":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 10 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 10, true);
						break;
					case "18901":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 20 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 20, true);
						break;
					case "18902":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 40 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 40, true);
						break;
					case "18903":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 80 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 80, true);
						break;
					case "18904":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 160 BOND_UNTRADEABLEs. Thank-you!",
								false);
						player.getPackets()
								.sendGameMessage(
										"Thank you, your BOND_UNTRADEABLEs has been placed your the bank.");
						player.getBank().addItem(
								ItemIdentifiers.BOND_UNTRADEABLE, 160, true);
						break;
					case "18905":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 5 treasure hunter keys. Thank-you!",
								false);
						player.getTreasureHunter().handleBoughtKeys(5);
						break;
					case "18906":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 20 treasure hunter keys. Thank-you!",
								false);
						player.getTreasureHunter().handleBoughtKeys(20);
						break;
					case "18907":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 80 treasure hunter keys. Thank-you!",
								false);
						player.getTreasureHunter().handleBoughtKeys(80);
						break;
					case "18908":
						player.setNextGraphics(new Graphics(1765));
						World.sendWorldMessage(
								"<col=FF0000><img=5>"
										+ player.getDisplayName()
										+ " has bought 500 treasure hunter keys. Thank-you!",
								false);
						player.getTreasureHunter().handleBoughtKeys(500);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
