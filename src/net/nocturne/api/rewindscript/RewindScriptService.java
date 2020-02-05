package net.nocturne.api.rewindscript;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import net.nocturne.utils.Logger;

public class RewindScriptService {

	/**
	 * @author:
	 */

	@SuppressWarnings("serial")
	public static ArrayList<String> scripts = new ArrayList<String>() {
		{
			add("database");
			add("test");
		}
	};

	public static void main(String[] args) throws Exception {
		init();
	}

	public static void init() throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		int scriptAmount = scripts.size() - 1;
		for (int i = 0; i <= scriptAmount; i++) {
			FileInputStream fileInputStream = new FileInputStream(
					"rewindscript/" + scripts.get(i) + ".rws");
			try {
				RewindScriptParser parser = new RewindScriptParser(
						fileInputStream);
				RewindScriptNode mainNode = parser.parse();
				switch (scripts.get(i)) {
				case "database":
					if (mainNode.has("credentials")) {
						RewindScriptNode databaseNode = mainNode
								.nodeFor("database");
						String host = databaseNode.getString("host");
					}
					break;
				case "test":
					Logger.log("RewindScriptService",
							"We're good with the test.");
					break;
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				fileInputStream.close();
			}
		}
		Logger.log("RewindScriptService", "Initiating RewindScript executor ("
				+ scripts.size() + " scripts loaded)...");
	}
}