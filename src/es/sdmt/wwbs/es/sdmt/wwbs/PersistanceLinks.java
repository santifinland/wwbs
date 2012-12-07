package es.sdmt.wwbs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistanceLinks {

	private static Properties properties = new Properties();
	private static Logger logger = LogManager
			.getLogger(WordWideBestSellers.class.getName());

	public PersistanceLinks() {
	}

	// Insert links into the site
	public static void refresh(List<String> itemLinks, String country)
			throws IOException {

		// Load previous links
		List<String> previousLinks = loadLinks(country);

		// Add previous links at the end and chop to ten
		Iterator<String> i = previousLinks.iterator();
		while (i.hasNext()) {
			String item = i.next();
			logger.info("Addgin previous links: " + item);
			itemLinks.add(item);
		}
		
		// Remove duplicates
		itemLinks = removeDuplicates(itemLinks);
		
		if (itemLinks.size() > 12) itemLinks = itemLinks.subList(0, 12);

		// Store new list
		storeLinks(itemLinks, country);
	}

	// Load links stored in files
	public static List<String> loadLinks(String country) throws IOException {

		// Get file name from properties
		getProperties();
		String LinksListFilePrefix = properties
				.getProperty("LinksListFilePrefix");
		logger.info("Property LinksListFilePrefix loaded: " + LinksListFilePrefix);

		List<String> links = readSmallTextFile(LinksListFilePrefix + country);
		Iterator<String> i = links.iterator();
		while (i.hasNext()) {
			logger.info(i.next());
		}

		return links;

	}

	// Store links stored in files
	public static void storeLinks(List<String> links, String country)
			throws IOException {

		// Get file name from properties		
		String LinksListFilePrefix = properties
				.getProperty("LinksListFilePrefix");
		writeSmallTextFile(links, LinksListFilePrefix + country);
	}

	private static List<String> readSmallTextFile(String aFileName)
			throws IOException {
		logger.info("Reading file: " + aFileName);
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, Charset.defaultCharset());
	}

	private static void writeSmallTextFile(List<String> aLines, String aFileName)
			throws IOException {
		logger.info("Writing file: " + aFileName);
		Path path = Paths.get(aFileName);
		Files.write(path, aLines, Charset.defaultCharset());
	}
	
	private static List<String> removeDuplicates(List<String> itemLinks) {
		
		//remove duplicates if any
		Set<String> setItems = new LinkedHashSet<String>(itemLinks);
		itemLinks.clear();
		itemLinks.addAll(setItems);
		List<String> itemLinksNoDuplicates = new ArrayList<String>();
		itemLinksNoDuplicates.addAll(setItems);
		return itemLinksNoDuplicates;
	}
	

	private static void getProperties() {

		// Load properties
		InputStream is = null;
		try {
			is = new FileInputStream("resources/main.properties");
			properties.load(is);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (null != is)
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
		}
	}

}
