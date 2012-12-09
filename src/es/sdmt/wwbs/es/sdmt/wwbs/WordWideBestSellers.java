package es.sdmt.wwbs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.sdmt.wwbs.amazon.Item;
import es.sdmt.wwbs.amazon.RequestItem;
import es.sdmt.wwbs.amazon.RequestTopSellers;
import es.sdmt.wwbs.translator.TranslateItems;

public class WordWideBestSellers {

	private static Properties properties = new Properties();
	private static Logger logger = LogManager.getLogger(WordWideBestSellers.class.getName());
	
	static RequestTopSellers requestTopSellers =  new RequestTopSellers();
	
	private static List<String> itemLinksUS = new ArrayList<String>();
	private static List<String> itemLinksES = new ArrayList<String>();
	private static List<String> itemLinksIT = new ArrayList<String>();
	private static List<String> itemLinksDE = new ArrayList<String>();
	private static List<String> itemLinksFR = new ArrayList<String>();
	private static List<String> itemLinksUK = new ArrayList<String>();
	private static List<String> itemLinksCA = new ArrayList<String>();
	
	public WordWideBestSellers() {}
	
	public static void main(String[] args) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		
		// Get logger		
		logger.info("WWBS");
		logger.info("Log started");
		
		// Load properties	
		WordWideBestSellers.getProperties();			

		// Request items for every country
		List<String> countries = new ArrayList<String>();
		countries.add("US");		
		countries.add("ES");
		countries.add("IT");
		countries.add("DE");			
		countries.add("FR");
		countries.add("UK");
		countries.add("CA");
				
		// index contains all items of all contries translated
		List<String> index = new ArrayList<String>();
		
		Iterator<String> i = countries.iterator();
		while (i.hasNext()) {			
			
			String country = i.next();
			logger.info("Country: " + country);									
			
			// Search for items
			List<Item> itemList = requestTopSellers.getTopSellers(country);
			
			// Translate items
			itemList = TranslateItems.translate(country.toLowerCase(), itemList);
			
			Iterator<Item> j = itemList.iterator();		
			
			index.add(country);
			
			while (j.hasNext()) {
				logger.debug("Item number: " + j);
				Item item = j.next();
				if (item.getASIN() != null) {
					String detailPageURL = RequestItem.getItem(country, item.getASIN());
					switch (country) {
					case "US":
						itemLinksUS.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "ES":
						itemLinksES.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "IT":
						itemLinksIT.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "DE":
						itemLinksDE.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "FR":
						itemLinksFR.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "UK":
						itemLinksUK.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					case "CA":
						itemLinksCA.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
						break;
					default:
						break;
					}
					index.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>" );									
				}
			}
		}
		
		logger.info("Final index: ");
		Iterator<String> k = index.iterator();
		while (k.hasNext()) {
			String line = k.next();
			logger.info(line);
		}
		
		// Build site	
		PersistanceLinks.refresh(itemLinksUS, "US");
		PersistanceLinks.refresh(itemLinksES, "ES");
		PersistanceLinks.refresh(itemLinksIT, "IT");
		PersistanceLinks.refresh(itemLinksDE, "DE");
		PersistanceLinks.refresh(itemLinksFR, "FR");
		PersistanceLinks.refresh(itemLinksUK, "UK");
		PersistanceLinks.refresh(itemLinksCA, "CA");
		BuildSite.addLinksToSite();
			
		
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
			if (null != is) try {
				is.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
