package net.nocturne.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import net.nocturne.cache.Cache;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.utils.Utils;

public class ItemDropsPacker {

	static class NPCDrop {

		private int itemId, minAmount, maxAmount, rarity;

		NPCDrop(int itemId, int minAmount, int maxAmount, int rarity) {
			if (itemId == 617)
				itemId = 995;
			if (itemId == 2513)
				itemId = 3140;
			this.itemId = itemId;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.rarity = rarity;
		}

		int getMinAmount() {
			return minAmount;
		}

		int getMaxAmount() {
			return maxAmount;
		}

		public int getItemId() {
			return itemId;
		}

		public int getRarity() {
			return rarity;
		}
	}

	public static void main(String[] args) {
		int marker = -1;
		try {
			Cache.init();
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					"data/npcs/packedDrops.d"));
			for (int npcId = 0; npcId < Utils.getNPCDefinitionsSize(); npcId++) {
				marker = npcId;
				File file = new File("data/npcs/drops/" + npcId + ".txt");
				if (file.exists()) {
					BufferedReader reader = new BufferedReader(new FileReader(
							file));
					boolean rareDropTable;
					rareDropTable = reader.readLine().contains("true");
					List<NPCDrop> drops = new ArrayList<>();
					while (true) {
						String line = reader.readLine();
						if (line == null)
							break;
						String[] l = line.split(", ");
						int id = Integer.parseInt(l[0]);
						if (id == -1)
							continue;
						if (NPCDefinitions.getNPCDefinitions(npcId).name
								.equalsIgnoreCase("skeleton") && id == 532)
							continue;
						if (NPCDefinitions.getNPCDefinitions(npcId).name
								.equalsIgnoreCase("terror dog") && id == 526)
							continue;
						if (id >= 30318 && id <= 30321)
							continue;
						drops.add(new NPCDrop(id, Integer.parseInt(l[1]),
								Integer.parseInt(l[2]), Integer.parseInt(l[3])));
					}
					reader.close();
					out.writeShort(npcId);
					out.writeBoolean(rareDropTable);
					out.writeByte(drops.size());
					for (NPCDrop drop : drops) {
						out.writeShort(drop.getItemId());
						out.writeShort(drop.getMinAmount());
						out.writeShort(drop.getMaxAmount());
						out.writeByte(drop.getRarity());
					}
				}
			}
			out.flush();
			out.close();
		} catch (NumberFormatException | IOException e) {
			System.out.println("ERROR AT NPC: " + marker);
			e.printStackTrace();
		}
	}
}