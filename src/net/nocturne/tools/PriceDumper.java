package net.nocturne.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.nocturne.cache.Cache;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.utils.Utils;

public class PriceDumper {

	/**
	 * @author: miles M
	 */

	/**
	 * After price dump set custom prices for: magic notepaper, mystery box
	 */

	private static int retryCounts;

	public static void main(String[] args) throws IOException {
		Cache.init();
		for (int itemId = 15517; itemId < Utils.getItemDefinitionsSize(); itemId++) {
			dumpItem(itemId);
		}
	}

	private static final void dumpItem(int itemId) {
		try {
			WebPage page = new WebPage("tip.it/runescape/index.php?gec&itemid="
					+ itemId);
			try {
				page.load();
			} catch (Exception e) {
				/*
				 * System.out.println(ItemDefinitions.getItemDefinitions(itemId).
				 * getName() + " (" + itemId + ") was not found, skipping.");
				 */
			}
			for (String lines : page.getLines()) {
				if (lines
						.contains("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					String price = lines.replaceAll("<tr>", "")
							.replaceAll("<td colspan=\"4\">", "")
							.replaceAll("<b>", "").replaceAll("</b>", "")
							.replaceAll("</td>", "").replaceAll("</tr>", "")
							.replaceAll(",", "").replaceAll("gp", "")
							.replaceAll("Current Market Price: ", "");
					if (Integer.parseInt(price) > Integer.MAX_VALUE / 4) {
						System.out.println("Price is above "
								+ Utils.format(Integer.parseInt(price))
								+ ", skipping.");
						return;
					}
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							"data/items/prices2.json", true));
					writer.write(itemId + "=: " + price);
					writer.newLine();
					writer.flush();
					writer.close();
					int left = Utils.getItemDefinitionsSize() - itemId;
					System.out.println("GE Dumped "
							+ ItemDefinitions.getItemDefinitions(itemId)
									.getName() + " (" + itemId + ") for "
							+ Utils.format(Integer.parseInt(price))
							+ " gp - another " + left + " items left.");
				}
			}
		} catch (IOException e) {
			dumpItem(itemId);
			System.err.println("Error, retrying in " + retryCounts++ + ".");
		}
	}
}