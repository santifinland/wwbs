package es.sdmt.wwbs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildSite {

	private static Properties properties = new Properties();
	private static Logger logger = LogManager
			.getLogger(WordWideBestSellers.class.getName());

	public BuildSite() {
	}

	// Insert links into the site
	public static void addLinksToSite() {

		// Load properties
		getProperties();

		// Make a new copy from the template
		newSiteFromTemplate();
	}

	private static void newSiteFromTemplate() {

		Path input = Paths.get(properties.getProperty("site.template"));
		Path output = Paths.get(properties.getProperty("site"));

		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;

		try {

			// Construct the BufferedReader object
			bufferedWriter = new BufferedWriter(new FileWriter(
					output.toString()));
			bufferedReader = new BufferedReader(
					new FileReader(input.toString()));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				// Process the data, here we just print it out
				if (line.indexOf("<!-- DE -->") != -1) {
					logger.info("Replacing for DE");
					List<String> links = PersistanceLinks.loadLinks("DE");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- ES -->") != -1) {
					logger.info("Replacing for ES");
					List<String> links = PersistanceLinks.loadLinks("ES");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- FR -->") != -1) {
					logger.info("Replacing for FR");
					List<String> links = PersistanceLinks.loadLinks("FR");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- GB -->") != -1) {
					logger.info("Replacing for UK");
					List<String> links = PersistanceLinks.loadLinks("UK");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- IT -->") != -1) {
					logger.info("Replacing for IT");
					List<String> links = PersistanceLinks.loadLinks("IT");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- US -->") != -1) {
					logger.info("Replacing for US");
					List<String> links = PersistanceLinks.loadLinks("US");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- CA -->") != -1) {
					logger.info("Replacing for CA");
					List<String> links = PersistanceLinks.loadLinks("CA");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else if (line.indexOf("<!-- CN -->") != -1) {
					logger.info("Replacing for CN");
					List<String> links = PersistanceLinks.loadLinks("CN");
					Iterator<String> i = links.iterator();
					while (i.hasNext()) {
						bufferedWriter.write(i.next());
					}
				} else {
					bufferedWriter.write(line);
				}
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the BufferedReader
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			// Close the BufferedWriter
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
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