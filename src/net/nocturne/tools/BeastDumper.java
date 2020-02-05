package net.nocturne.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.nocturne.cache.Cache;
import net.nocturne.utils.Utils;

public class BeastDumper {

	private String jsonData;
	private static BufferedWriter writer;
	private static BufferedReader reader;
	private static final String BESTIARY_URL = "http://services.runescape.com/m=itemdb_rs/bestiary/bestiary/beastData.json?beastid=";
	private HashMap<String, String> values;
	private static HashMap<Integer, String> combinations;
	private static ArrayList<String> definitions;
	private static final Pattern JSON_PATTERN = Pattern
			.compile("\"(\\w+?)\":\\[?\"?([\\w ]+)+\"?\\]?");

	public BeastDumper(final int id, int realId) throws NullPointerException {
		try {
			System.out.println("Attempting ID: " + realId);
			jsonData = getData(id);
			if (jsonData.equalsIgnoreCase(""))
				new BeastDumper(id - 1, id);
			Matcher matcher = JSON_PATTERN.matcher(jsonData);
			values = new HashMap<String, String>();
			while (matcher.find()) {
				String key = matcher.group(1);
				String value = matcher.group(2);
				values.put(key, value);
			}
			if (values == null || values.get("attack") == null
					|| values.get("attack").equalsIgnoreCase("null"))
				return;
			writer.newLine();
			writer.append(realId + " - " + values.get("lifepoints") + " "
					+ values.get("attack") + " -1 " + values.get("death")
					+ " 30 -1 -1 " + values.get("xp") + " true false "
					+ values.get("poisonous") + " " + values.get("aggressive")
					+ " 20");
			writer.flush();
			System.out.println("Completed NPC: " + values.get("name") + "("
					+ realId + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getDeathAnimation() {
		return Integer.parseInt(values.get("death"));
	}

	public int getAttackAnimation() {
		return Integer.parseInt(values.get("attack"));
	}

	public int getSize() {
		return Integer.parseInt(values.get("size"));
	}

	public String getName() {
		return values.get("name");
	}

	public String getWeakness() {
		return values.get("weakness");
	}

	public String getProperty(String a) {
		return values.get(a);
	}

	public int getLifepoints() {
		return Integer.parseInt(values.get("lifepoints"));
	}

	public int getId() {
		return Integer.parseInt(values.get("id"));
	}

	public String getDescription() {
		return getProperty("description");
	}

	public boolean getPoisonous() {
		return Boolean.parseBoolean(getProperty("poisonous"));
	}

	public boolean getAttackable() {
		return Boolean.parseBoolean(getProperty("attackable"));
	}

	public boolean getMembers() {
		return Boolean.parseBoolean(getProperty("members"));
	}

	public int getDefence() {
		return Integer.parseInt(values.get("defence"));
	}

	public int getMagic() {
		return Integer.parseInt(values.get("magic"));
	}

	public int getRanged() {
		return Integer.parseInt(getProperty("ranged"));
	}

	public String getAreas() {
		return getProperty("areas");
	}

	public static void main(String[] args) {
		/*
		 * try { Cache.init(); File file = new
		 * File("./information/beastDump.txt"); if (file.exists())
		 * file.delete(); else file.createNewFile(); writer = new
		 * BufferedWriter(new FileWriter(file)); for (int i = 0; i <
		 * Utils.getNPCDefinitionsSize(); i++) new BeastDumper(i, i);
		 * writer.close(); combineData(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		combineData();
	}

	public static void combineData() {
		combinations = new HashMap<Integer, String>();
		definitions = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(new File(
					"./data/npcs/oldDefs.txt")));
			String data;
			while ((data = reader.readLine()) != null) {
				if (data.contains("//"))
					continue;
				String[] npc = data.split(" - ");
				String[] fields = npc[1].split(" ");
				combinations.put(Integer.parseInt(npc[0]), fields[8] + " "
						+ fields[9] + " " + fields[10] + " " + fields[11] + " "
						+ fields[12]);
			}
			reader.close();
			reader = new BufferedReader(new FileReader(new File(
					"./data/npcs/unpackedCombatDefinitionsList.txt")));
			while ((data = reader.readLine()) != null) {
				if (data.contains("//"))
					continue;
				String[] npc = data.split(" - ");
				String[] fields = npc[1].split(" ");
				if (combinations.containsKey(Integer.parseInt(npc[0])))
					definitions.add(Integer.parseInt(npc[0]) + " - "
							+ fields[0] + " " + fields[1] + " " + fields[2]
							+ " " + fields[3] + " " + fields[4] + " "
							+ fields[5] + " " + fields[6] + " " + fields[7]
							+ " " + combinations.get(Integer.parseInt(npc[0])));
				else
					definitions.add(Integer.parseInt(npc[0]) + " - "
							+ fields[0] + " " + fields[1] + " " + fields[2]
							+ " " + fields[3] + " " + fields[4] + " "
							+ fields[5] + " " + fields[6] + " " + fields[7]
							+ " " + fields[8] + " " + fields[9] + " "
							+ fields[10] + " " + fields[11] + " " + fields[12]);
			}
			reader.close();
			writer = new BufferedWriter(new FileWriter(new File(
					"./data/npcs/test.txt")));
			for (String def : definitions) {
				System.out.println(def);
				writer.newLine();
				writer.write(def);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getData(final int id) throws IOException {
		URL url = new URL(BESTIARY_URL + id);
		final InputStream in = url.openStream();
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int read;
		while ((read = in.read(buffer)) != -1) {
			bos.write(buffer, 0, read);
		}
		bos.flush();
		in.close();
		return new String(bos.toByteArray());
	}
}