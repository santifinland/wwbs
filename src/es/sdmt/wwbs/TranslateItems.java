package es.sdmt.wwbs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TranslateItems {		
	
	public TranslateItems(){}
	
	public static List<Item> translate(String country, List<Item> itemList) throws InvalidKeyException, IllegalArgumentException, NoSuchAlgorithmException, IOException {
		
		System.out.println("############ Tranlating");
		
		List<Item> translatedList = new ArrayList<Item>();
		Iterator<Item> i = itemList.iterator();
		while (i.hasNext()) {
			Item item = i.next();
			String title = item.getTitle();
			System.out.println("############ Tranlating: " + title);
			BingTranslator bingTranslator = new BingTranslator();			
			BingAccessToken bingAccessToken = BingTranslator.getAccessToken();
			String translatedTitle = bingTranslator.translate(bingAccessToken, title, country, "en");
			String author = item.getAuthor();
			SearchItem searchItem = new SearchItem();
			Item translatedItem = searchItem.searchItem("US", author, translatedTitle);
			translatedList.add(translatedItem);
			
		}
		
		return translatedList;
	}


	

}
