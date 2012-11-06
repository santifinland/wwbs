package es.sdmt.wwbs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class BuildSite {

	static Properties properties = new Properties();
	static RequestTopSellers requestTopSellers =  new RequestTopSellers();
	
	public BuildSite() {}
	
	public static void main(String[] args) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		
		// Load properties	
		BuildSite.getProperties();					

		// Request items for every country
		List<String> countries = new ArrayList<String>();
		//countries.add("US");		
		//countries.add("ES");
		countries.add("IT");
		//countries.add("DE");
		//countries.add("CA");		
		//countries.add("FR");
		//countries.add("UK");
		
		RequestItem requestItem = new RequestItem();
		
		Iterator<String> i = countries.iterator();
		while (i.hasNext()) {			
			String country = i.next();
			System.out.println("############ Country: " + country);
			String endpoint = properties.getProperty(country + ".endpoint");
			String browseNodeId = properties.getProperty(country + ".BrowseNodeId");
			String associateTag = properties.getProperty(country + ".AssociateTag");			 
			List<Item> itemList = requestTopSellers.getTopSellers(country);	
			itemList = TranslateItems.translate(country.toLowerCase(), itemList);
			
			Iterator<Item> j = itemList.iterator();
			System.out.println("Found " + itemList.size() + " items translated");
			while (j.hasNext()) {
				Item item = j.next();
				if (item.getASIN() != null) {
					requestItem.getItem(country, item.getASIN());
				}
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
			System.out.println(e.getMessage());
		} finally {
			if (null != is) try {
				is.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
