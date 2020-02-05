package net.nocturne.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.game.npc.combat.NPCCombatDefinitions;

public final class NPCCombatDefinitionsL {

	public final static HashMap<Integer, NPCCombatDefinitions> npcCombatDefinitions = new HashMap<Integer, NPCCombatDefinitions>();
	public final static HashMap<String, Integer> existingDefinitions = new HashMap<String, Integer>();
	public final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(
			1, -1, -1, -1, 33, -1, -1, 0.2, true, false, false, false, 2);
	private static final String PACKED_PATH = "data/npcs/packedCombatDefinitions.ncd";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedNPCCombatDefinitions();
		else
			loadUnpackedNPCCombatDefinitions();
	}

	public static NPCCombatDefinitions getNPCCombatDefinitions(int npcId) {
		NPCCombatDefinitions def = npcCombatDefinitions.get(npcId);
		if (def == null) {
			if (existingDefinitions.containsKey(NPCDefinitions
					.getNPCDefinitions(npcId).getName().toLowerCase()))
				return npcCombatDefinitions.get(existingDefinitions
						.get(NPCDefinitions.getNPCDefinitions(npcId).getName()
								.toLowerCase()));
			return DEFAULT_DEFINITION;
		}
		return def;
	}

	private static void loadUnpackedNPCCombatDefinitions() {
		int count = 0;
		Logger.log("NPCCombatDefinitionsL", "Packing npc combat definitions...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					PACKED_PATH));
			BufferedReader in = new BufferedReader(new FileReader(
					"data/npcs/unpackedCombatDefinitionsList.txt"));
			while (true) {
				String line = in.readLine();
				count++;
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2) {
					in.close();
					out.close();
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				}
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 13);
				if (splitedLine2.length != 13) {
					in.close();
					out.close();
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				}
				int hitpoints = Integer.parseInt(splitedLine2[0]);
				int attackAnim = Integer.parseInt(splitedLine2[1]);
				int defenceAnim = Integer.parseInt(splitedLine2[2]);
				int deathAnim = Integer.parseInt(splitedLine2[3]);
				int respawnDelay = Integer.parseInt(splitedLine2[4]);
				int attackGfx = Integer.parseInt(splitedLine2[5]);
				int attackProjectile = Integer.parseInt(splitedLine2[6]);
				double xp = Double.parseDouble(splitedLine2[7]);

				boolean follow = Boolean.parseBoolean(splitedLine2[8]);
				boolean poisonImmune = Boolean.parseBoolean(splitedLine2[9]);
				boolean poisonous = Boolean.parseBoolean(splitedLine2[10]);
				boolean agressivenessType = Boolean
						.parseBoolean(splitedLine2[11]);
				int agroRatio = Integer.parseInt(splitedLine2[12]);
				out.writeInt(npcId);
				out.writeInt(hitpoints);
				out.writeInt(attackAnim);
				out.writeInt(defenceAnim);
				out.writeInt(deathAnim);
				out.writeInt(respawnDelay);
				out.writeInt(attackGfx);
				out.writeInt(attackProjectile);
				out.writeDouble(xp);
				out.writeByte(follow ? 1 : 0);
				out.writeByte(poisonImmune ? 1 : 0);
				out.writeByte(poisonous ? 1 : 0);
				out.writeByte(agressivenessType ? 1 : 0);
				out.writeByte(agroRatio);
				npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(
						hitpoints, attackAnim, defenceAnim, deathAnim,
						respawnDelay, attackGfx, attackProjectile, xp, follow,
						poisonImmune, poisonous, agressivenessType, agroRatio));
				if (!existingDefinitions.containsKey(NPCDefinitions
						.getNPCDefinitions(npcId).getName().toLowerCase()))
					existingDefinitions.put(
							NPCDefinitions.getNPCDefinitions(npcId).getName()
									.toLowerCase(), npcId);
			}
			in.close();
			out.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private static void loadPackedNPCCombatDefinitions() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getInt();
				int hitpoints = buffer.getInt();
				int attackAnim = buffer.getInt();
				int defenceAnim = buffer.getInt();
				int deathAnim = buffer.getInt();
				int respawnDelay = buffer.getInt();
				int attackGfx = buffer.getInt();
				int attackProjectile = buffer.getInt();
				double xp = buffer.getDouble();
				boolean follow = buffer.get() == 1;
				boolean poisonImmune = buffer.get() == 1;
				boolean poisonous = buffer.get() == 1;
				boolean agressivenessType = buffer.get() == 1;
				int agroRatio = buffer.get() & 0xff;
				npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(
						hitpoints, attackAnim, defenceAnim, deathAnim,
						respawnDelay, attackGfx, attackProjectile, xp, follow,
						poisonImmune, poisonous, agressivenessType, agroRatio));
				if (!existingDefinitions.containsKey(NPCDefinitions
						.getNPCDefinitions(npcId).getName().toLowerCase()))
					existingDefinitions.put(
							NPCDefinitions.getNPCDefinitions(npcId).getName()
									.toLowerCase(), npcId);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	private NPCCombatDefinitionsL() {

	}
}