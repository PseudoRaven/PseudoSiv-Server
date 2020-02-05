package net.nocturne.api.rewindscript;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RewindScriptParser {

	/**
	 * The pattern to match scalar lines
	 */
	private final Pattern scalarPattern = Pattern
			.compile("\\s*(.*?)\\s*\"((?:\\\"|[^\"])+?)\"\\s*(?:\\#.*)?$");

	/**
	 * The pattern to match arrays..
	 */
	private final Pattern arrayPattern = Pattern
			.compile("\\s*(.*?)\\s*\\s*\\{\\s*(?:\\#.*)?$");

	/**
	 * The pattern to match nested arrays
	 */
	private final Pattern nestedArrayPattern = Pattern
			.compile("\\s*(.*?)\\s*\"((?:\\\"|[^\"])+?)\"\\s*\\(\\s*(?:\\#.*)?$");

	/**
	 * The pattern to match nested hashes
	 */
	private final Pattern nestedHashPattern = Pattern
			.compile("\\s*(.*?)\\s*\"((?:\\\\\"|[^\"])+?)\"\\s*\\{\\s*(?:\\#.*)?$");

	/**
	 * The patter to find the end of a nested array
	 */
	private final Pattern nestedEndPattern = Pattern
			.compile("^\\s*(}|\\))\\s*");

	/**
	 * The reader object which the configuration is parsed from
	 */
	private BufferedReader reader;

	public RewindScriptParser(InputStream input) {
		this.reader = new BufferedReader(new InputStreamReader(input));
	}

	/**
	 * Parse the configuration from the specified file
	 *
	 * @return The configuration
	 * @throws IOException
	 */
	public RewindScriptNode parse() throws IOException {
		RewindScriptNode node = new RewindScriptNode();
		try {
			parse(node);
		} finally {
			reader.close();
		}
		return node;
	}

	/**
	 * Parse a block of data, reading line per line from the reader.
	 */
	public void parse(RewindScriptNode node) throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return;
		}
		line = line.trim();

		if (!line.startsWith("#") && line.length() != 0) {
			// Scalar match
			Matcher scalar = scalarPattern.matcher(line);
			Matcher array = arrayPattern.matcher(line);
			Matcher nestedArrayBlock = nestedArrayPattern.matcher(line);
			Matcher nestedHashBlock = nestedHashPattern.matcher(line);
			if (scalar.find()) {
				node.set(scalar.group(1), scalar.group(2));
			} else if (nestedArrayBlock.find()) {
				String name = nestedArrayBlock.group(1);
				String key = nestedArrayBlock.group(2);
				key = key.replaceAll("\\\"", "\"");
				if (!node.has(name)) {
					node.set(name, new RewindScriptNode());
				}
				parse(node.nodeFor(name));
			} else if (nestedHashBlock.find()) {
				String name = nestedHashBlock.group(1);
				String key = nestedHashBlock.group(2);
				RewindScriptNode sub = node.has(name) ? node.nodeFor(name)
						: new RewindScriptNode();
				if (!node.has(name)) {
					node.set(name, sub);
				}
				if (!sub.has(key)) {
					sub.set(key, new RewindScriptNode());
				}
				parse(sub.nodeFor(key));
			} else if (array.find()) {
				RewindScriptNode newNode = new RewindScriptNode();
				node.set(array.group(1), newNode);
				parse(newNode);
			}
			Matcher nestedEnd = nestedEndPattern.matcher(line);
			if (nestedEnd.find()) {
				return;
			}
		}
		parse(node);
	}
}