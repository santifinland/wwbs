package es.sdmt.wwbs.translator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.sdmt.wwbs.amazon.Item;
import es.sdmt.wwbs.amazon.SearchItem;


public class TranslateItems {
	
	private static Logger logger = LogManager.getLogger(TranslateItems.class.getName());
	
	public TranslateItems(){}
	
	public static List<Item> translate(String country, List<Item> itemList) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		
		logger.debug("############ Tranlating");
		
		List<Item> translatedList = new ArrayList<Item>();
		Iterator<Item> i = itemList.iterator();
		while (i.hasNext()) {
			Item item = i.next();
			String title = item.getTitle();
			logger.debug("############ Tranlating: " + title);
			BingTranslator bingTranslator = new BingTranslator();			
			BingAccessToken bingAccessToken = BingTranslator.getAccessToken();
			if (country.contains("us")) country = "en";
			String translatedTitle = bingTranslator.translate(bingAccessToken, title, country, "en");
			String author = item.getAuthor();
			SearchItem searchItem = new SearchItem();
			Item translatedItem = searchItem.searchItem("US", author, translatedTitle);
			translatedList.add(translatedItem);
			
		}
		
		return translatedList;
	}


	

}
