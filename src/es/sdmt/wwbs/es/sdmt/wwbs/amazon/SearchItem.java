package es.sdmt.wwbs.amazon;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SearchItem extends AmazonAPI {
	
	private static Logger logger = LogManager.getLogger(SearchItem.class.getName());
      
    public SearchItem(){
    	
    	super();
    }
    
    public static Item searchItem(String country, String author, String title) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {

		SignedRequestsHelper helper = SignedRequestsHelper.getInstance(getEndpoint("US"), key, secret);
		Item item = null;

		logger.debug("Searching item in: " + country);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2011-08-01");
		params.put("Operation", "ItemSearch");
		params.put("SearchIndex", "Books");
		if (author == null) author = " ";
		if ((country.contains("zh-CHS")) || (country.contains("ja"))) {
			logger.debug("No author set in the item search");			
		} else {
			params.put("Author", author);
			logger.debug("Searching for author: " + author);
		}
		logger.debug("Searching for title: " + title);
		params.put("Title", title);
		params.put("AssociateTag", getAssociateTag("US"));

		String url = helper.sign(params);
		try {
			Document response = getResponse(url);
			//printResponse(response);
			item = parseResponse(response);
		} catch (Exception ex) {
			logger.error(ex);
		}
		
		return item;
	}
   

    private static Document getResponse(String url) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(url);
        return doc;
    }

    private static void printResponse(Document doc) throws TransformerException, FileNotFoundException {
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        Properties props = new Properties();
        props.put(OutputKeys.INDENT, "yes");
        trans.setOutputProperties(props);
        StreamResult res = new StreamResult(new StringWriter());
        DOMSource src = new DOMSource(doc);
        trans.transform(src, res);
        String toString = res.getWriter().toString();
        logger.debug(toString);
    }
    
    private static Item parseResponse(Document response)  {
    	
		Item item = new Item();
		
		NodeList nodeList = response.getElementsByTagName("Item");
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			Node topItem = nodeList.item(i);
			NodeList childnodes = topItem.getChildNodes();
			int sizechildnodes = childnodes.getLength();			
			for (int j = 0; j < sizechildnodes; j++) {
				Node child = childnodes.item(j);
				
				if (child.getNodeName().compareTo("ASIN") == 0) {
					logger.debug("found one: " + child.getTextContent());
					item.setASIN((child.getTextContent()));
					return item;
				}																				
			}						
		}
		
		return item;

	}

}