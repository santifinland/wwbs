package es.sdmt.wwbs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildSite {

	private static Properties properties = new Properties();
	private static Logger logger = LogManager
			.getLogger(WordWideBestSellers.class.getName());
	
	// Countries available
	private static List<String> countries = new ArrayList<String>();
			

	public BuildSite() {
	}

	// Insert links into the site
	public static void addLinksToSite() {		
		
		// Load properties
		getProperties();				
		
		// Countries available		
		countries.add("US");		
		//countries.add("ES");
		//countries.add("IT");
		//countries.add("DE");
		//countries.add("CA");		
		//countries.add("FR");
		//countries.add("UK");		
		
		// Make a new copy from the template
		newSiteFromTemplate();
	}
	
	private static void newSiteFromTemplate(){
			
		Path input = Paths.get(properties.getProperty("site.template"));
		Path output = Paths.get(properties.getProperty("site"));
		try {
			List<String> template = Files.readAllLines(input, Charset.defaultCharset());					
			logger.info("Going to replace");
			List<String> templateUS = replace(template, "US");
							
			Files.write(output, templateUS, Charset.defaultCharset());
		} catch (IOException e) {
			logger.error(e.getMessage());			
		}		
	}
	
	private static List<String> replace(List<String> template, String country) throws IOException {
		
		List<String> replaced = template;
		Iterator<String> i = template.iterator();
		while (i.hasNext()) {			
			String line = i.next();
			logger.info("Line: " + line);
			if (line.indexOf("<!-- " + country.toUpperCase() + " -->") != -1) {
				logger.info("Replacing for: " + country);
				replaced.addAll(PersistanceLinks.loadLinks(country));
			} else {
				replaced.add(line);
			}			
		}
		
		return replaced;
		
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