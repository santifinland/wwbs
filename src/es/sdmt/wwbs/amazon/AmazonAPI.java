package es.sdmt.wwbs.amazon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AmazonAPI {

	private static Properties properties = new Properties();
	protected static String key;
	protected static String secret;
	protected static Map<String, String> endpoints =  new HashMap<String, String>();
	protected static Map<String, String> browseNodeIds =  new HashMap<String, String>();
	protected static Map<String, String> associateTags =  new HashMap<String, String>();

	public AmazonAPI() {

		// Load properties
		getProperties();
		
		
		//Iterator<String> iterator = endpoints.keySet().iterator();  
		   
		//while (iterator.hasNext()) {  
		  // String key = iterator.next().toString();  
		   //String value = endpoints.get(key).toString();  
		   
		   //System.out.println(key + " " + value);  
		//}  
	}

	protected void getProperties() {

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
		
		setKey(properties.getProperty("AWS_KEY"));
		setSecret(properties.getProperty("SECRET_KEY"));
		
		String countriesstring = properties.getProperty("NumberOfContries");
		int countries = Integer.parseInt(countriesstring);		
		for (int i = 0; i < countries; i++) {
		
			String country = properties.getProperty("countryprefix" + (i+1));					
			String endpoint = properties.getProperty(country + ".endpoint");		
			endpoints.put(country, endpoint);
			String browseNodeId = properties.getProperty(country + ".BrowseNodeId");
			browseNodeIds.put(country, browseNodeId);
			String associateTag = properties.getProperty(country + ".AssociateTag");
			associateTags.put(country, associateTag);
		}				  		   		
	}

	public static String getKey() {

		return key;
	}

	public static void setKey(String key) {

		AmazonAPI.key = key;
	}

	public static String getSecret() {

		return secret;
	}

	public static void setSecret(String secret) {

		AmazonAPI.secret = secret;
	}

	public static String getEndpoint(String country) {
					
		return endpoints.get(country);

	}

	public static String getBrowseNodeId(String country) {

		return browseNodeIds.get(country);

	}

	public static String getAssociateTag(String country) {

		return associateTags.get(country);

	}

}
