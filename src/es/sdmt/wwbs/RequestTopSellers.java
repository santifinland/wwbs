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

public class RequestTopSellers extends AmazonAPI {	
	
	public RequestTopSellers() {
		super();			
	}

	public List<Item> getTopSellers(String country) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {
		
		System.out.println("############ Getting top sellers from : " + country);
		SignedRequestsHelper helper = SignedRequestsHelper.getInstance(getEndpoint(country), key, secret);
		List<Item> itemList = new ArrayList<Item>();

		Map<String, String> params = new HashMap<String, String>();
		params.put("Service", "AWSECommerceService");
		params.put("Version", "2011-08-01");
		params.put("Operation", "BrowseNodeLookup");
		params.put("BrowseNodeId", getBrowseNodeId(country));
		params.put("ResponseGroup", "TopSellers");
		params.put("AssociateTag", getAssociateTag(country));

		String url = helper.sign(params);
		try {			
			Document response = getResponse(url);
			printResponse(response);
			itemList = parseResponse(response);
		} catch (Exception ex) {
			Logger.getLogger(RequestTopSellers.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return itemList;
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

	private static List<Item> parseResponse(Document response) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException,
	        NoSuchAlgorithmException, DOMException {

		String ASIN = null;
		String URL = null;
		List<Item> itemList = new ArrayList<Item>();
		

		NodeList nodeList = response.getElementsByTagName("TopItem");
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			Node topItem = nodeList.item(i);
			NodeList childnodes = topItem.getChildNodes();
			int sizechildnodes = childnodes.getLength();
			Item item = new Item();
			for (int j = 0; j < sizechildnodes; j++) {
				Node child = childnodes.item(j);

				
				if (child.getNodeName().compareTo("Title") == 0) {
					item.setTitle(child.getTextContent());
					System.out.println("Title: " + item.getTitle());
				}
				else if (child.getNodeName().compareTo("Author") == 0) {
					item.setAuthor(child.getTextContent());
				}
				
				/*else if (child.getNodeName().compareTo("ASIN") == 0) {
					ASIN = child.getTextContent();
				}
				else if (child.getNodeName().compareTo("DetailPageURL") == 0) {
					URL = child.getTextContent();
				}*/								
			}
			//System.out.println("<a href=\"" + URL + "\">" + "<img border=\"0\" src=\"http://images.amazon.com/images/P/" + ASIN
			//      + "\" width=\"114\" height=\"150\" style=\"margin-right: 8px\"> </a>");
			
			itemList.add(item);			
		}
		
		return itemList;

	}
}