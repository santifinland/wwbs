package es.sdmt.wwbs;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

public class RequestItem extends AmazonAPI {
    
    public RequestItem(){
    	
    	super();
    }

    public static String getItem(String country, String ASIN) throws InvalidKeyException, IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {
    	
    	String detailPageURL = null;
    	
        SignedRequestsHelper helper = SignedRequestsHelper.getInstance("ecs.amazonaws.com", key, secret);

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", ASIN);        
        params.put("AssociateTag", getAssociateTag(country));

        String url = helper.sign(params);
        try {
            Document response = getResponse(url);
            printResponse(response);
            detailPageURL = parseResponse(response);
        } catch (Exception ex) {
            Logger.getLogger(RequestItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return detailPageURL;
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
    
    private static String parseResponse(Document response)  {

    	String detailPageURL = null;
		NodeList nodeList = response.getElementsByTagName("Item");
		int size = nodeList.getLength();
		for (int i = 0; i < size; i++) {
			Node topItem = nodeList.item(i);
			NodeList childnodes = topItem.getChildNodes();
			int sizechildnodes = childnodes.getLength();
			for (int j = 0; j < sizechildnodes; j++) {
				Node child = childnodes.item(j);
				if (child.getNodeName().compareTo("DetailPageURL") == 0) {
					System.out.println("TTTTTTTTTTTTTTTT   Datailed URL Page: " + child.getTextContent());
					detailPageURL = child.getTextContent();
				}								
			}
		}
		
		return detailPageURL;
	}
    
}