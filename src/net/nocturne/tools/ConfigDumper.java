package net.nocturne.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import net.nocturne.cache.Cache;
import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.game.WorldObject;
import net.nocturne.utils.*;

public class ConfigDumper {

	/**
	 * @author: miles M
	 */

	public static long currentTime = 0;

	public static void main(String args[]) {
		currentTime = Utils.currentTimeMillis();
		try {
			Logger.log("ConfigDumper", "Initiating cache...");
			Cache.init();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.logErr("ConfigDumper",
				"Launched - took " + (Utils.currentTimeMillis() - currentTime)
						+ " milliseconds.");

		try {
			Logger.log("ConfigDumper", "Initiating process...");
			dumpObjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void dumpObjects() throws IOException {
		BufferedWriter writer;
		writer = new BufferedWriter(new FileWriter(
				"information/dumps/objectconfigs.txt", true));
		for (int objectId = 0; objectId < Utils.getObjectDefinitionsSize(); objectId++) {
			WorldObject object = new WorldObject(
					ObjectDefinitions.getObjectDefinitions(objectId).id, 0, 0,
					0, 0, 0);
			if (ObjectDefinitions.getObjectDefinitions(objectId).name
					.contains("'"))
				continue;
			writer.write("(#"
					+ ObjectDefinitions.getObjectDefinitions(objectId).id
					+ "#,#"
					+ ObjectDefinitions.getObjectDefinitions(objectId).name
					+ "#,#"
					+ ObjectExamines.getExamine(object).replaceAll("It#s a",
							"It is a") + "#),");
		}
		writer.flush();
		writer.close();
		Logger.logErr("ConfigDumper",
				"Finished process - took "
						+ (Utils.currentTimeMillis() - currentTime)
						+ " milliseconds.");
	}
}