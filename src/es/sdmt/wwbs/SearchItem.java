package es.sdmt.wwbs;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SearchItem extends AmazonAPI {

      
    public SearchItem(){
    	
    	super();
    }
    
    public static Item searchItem(String country, String author, String title) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {

		SignedRequestsHelper helper = SignedRequestsHelper.getInstance(getEndpoint(country), key, secret);
		Item item = null;

		System.out.println("############ Searching");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2011-08-01");
		params.put("Operation", "ItemSearch");
		params.put("SearchIndex", "Books");
		params.put("Author", author);
		params.put("Title", title);
		params.put("AssociateTag", getAssociateTag(country));

		String url = helper.sign(params);
		try {
			Document response = getResponse(url);
			//printResponse(response);
			item = parseResponse(response);
		} catch (Exception ex) {
			Logger.getLogger(RequestTopSellers.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println(toString);
    }
    
    private static Item parseResponse(Document response)  {

    	String ASIN = null;
		String URL = null;
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
					System.out.println("ZZZZZZZZZZZZZZ found one: " + child.getTextContent());
					item.setASIN((child.getTextContent()));
					return item;
				}																				
			}						
		}
		
		return item;

	}

}