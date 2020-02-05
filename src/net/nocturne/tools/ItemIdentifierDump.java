package net.nocturne.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.nocturne.cache.Cache;
import net.nocturne.cache.loaders.ItemDefinitions;
import net.nocturne.utils.Utils;

/**
 * 
 * @author Miles Black (bobismyname)
 * @date Dec 6, 2016
 */

@SuppressWarnings("ucd")
public class ItemIdentifierDump {

	public static List<String> complete = new ArrayList<String>();

	public static void main(String[] args) throws Throwable {
		Cache.init();
		File file = new File("./information/identifierDump.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) { // Starting
																	// at id
																	// 22300
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
			ItemDefinitions prev = ItemDefinitions.getItemDefinitions(i - 1);
			if (def == null || prev == null)
				continue;
			String name = Normalizer.normalize(
					def.name.toUpperCase().replace(" ", "_")
							.replace("&", "AND").replace("+", "_PLUS")
							.replace("/", "").replace("'", "").replace("-", "")
							.replace("(", "").replace(")", "").replace("?", "")
							.replace("23", "TWO_THIRDS")
							.replace("13", "ONE_THIRD")
							.replace("12", "ONE_FORTH").replace("%", "PERCENT")
							.replace(".", "").replace(",", "").replace(":", "")
							.replace("[", "").replace("]", ""),
					Normalizer.Form.NFD);
			name = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
					.matcher(name).replaceAll("");
			if (def.name.equalsIgnoreCase(""))
				continue;
			else if (def.name.equalsIgnoreCase(prev.name))
				name += "_NOTED";
			if (complete.contains(name))
				continue;
			complete.add(name);
			writer.append("	public static final int " + name + " = " + def.id
					+ ";");
			writer.newLine();
			writer.flush();
			System.out.println("Completed the identifier for " + name + ".");
		}
		writer.close();
	}

}
