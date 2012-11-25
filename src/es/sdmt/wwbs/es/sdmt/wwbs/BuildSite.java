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

public class BuildSite {

	static Properties properties = new Properties();
	static RequestTopSellers requestTopSellers =  new RequestTopSellers();
	
	public BuildSite() {}
	
	public static void main(String[] args) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		
		// Load properties	
		BuildSite.getProperties();					

		// Request items for every country
		List<String> countries = new ArrayList<String>();
		countries.add("US");		
		countries.add("ES");
		countries.add("IT");
		countries.add("DE");
		//countries.add("CA");		
		countries.add("FR");
		countries.add("UK");
				
		List<String> index = new ArrayList<String>();
		
		Iterator<String> i = countries.iterator();
		while (i.hasNext()) {			
			String country = i.next();
			System.out.println("############ Country: " + country);						 
			List<Item> itemList = requestTopSellers.getTopSellers(country);	
			itemList = TranslateItems.translate(country.toLowerCase(), itemList);
			
			Iterator<Item> j = itemList.iterator();
			System.out.println("Found " + itemList.size() + " items translated");
			
			index.add(country);
			
			while (j.hasNext()) {
				System.out.println("SSSSSSSSSSSSSSSSSSSSSSS Item number: " + j);
				Item item = j.next();
				if (item.getASIN() != null) {
					String detailPageURL = RequestItem.getItem(country, item.getASIN());
					index.add("<a href=\"" + detailPageURL + "\"><img border=\"0\" src=\"http://images.amazon.com/images/P/" + item.getASIN() + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>" );									
				}
			}
		}
		
		System.out.println("ZZZZZZZZZZZZZZZZZ            Final index: ");
		Iterator<String> k = index.iterator();
		while (k.hasNext()) {
			String line = k.next();
			System.out.println(line);
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
