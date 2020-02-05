package net.nocturne.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Jan 24, 2017
 */

public class SpawnConverter {

	static BufferedReader reader;
	static BufferedWriter writer;
	static HashMap<Integer, String> spawns = new HashMap<Integer, String>();

	/**
	 * Takes the dump logs from MGI/Cjay's npc spawn dump and converts it to
	 * Matrix format.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("./information/spawns.txt");
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if (spawns.containsKey(Integer.parseInt(data[0])))
					continue;
				spawns.put(Integer.parseInt(data[0]), data[1] + " - " + data[2]
						+ " " + data[3] + " " + data[4]);
			}
			writer = new BufferedWriter(new FileWriter(file));
			for (String info : spawns.values()) {
				writer.newLine();
				writer.append(info);
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
