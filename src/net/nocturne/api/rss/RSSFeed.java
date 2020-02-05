package net.nocturne.api.rss;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RSSFeed {

	/**
	 * @author: Pax M
	 */

	/**
	 * The URL that we want to read from
	 */
	public static String URL = "https://nocturne3.org/community/index.php?/forum/2-news-desk.xml/";

	/**
	 * Read the RSS
	 * 
	 * @param read element
	 * @return element
	 */
	public static String read(String element) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new URL(URL).openConnection()
					.getInputStream());
			doc.getDocumentElement().normalize();

			for (int temp = 0; temp < doc.getElementsByTagName("channel")
					.getLength(); temp++) {
				Node nNode = doc.getElementsByTagName("channel").item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					return eElement.getElementsByTagName(element).item(0)
							.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}