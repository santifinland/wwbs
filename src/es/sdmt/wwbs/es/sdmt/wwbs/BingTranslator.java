package es.sdmt.wwbs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import com.google.gson.Gson;

/**
 * The OAuth 2.0 Protocol draft-ietf-oauth-v2-10 -
 * http://tools.ietf.org/html/draft-ietf-oauth-v2-10
 **/
public class BingTranslator {

	static Properties properties = new Properties();

	public static final String RESTLET_HTTP_HEADERS = "org.restlet.http.headers";

	public BingTranslator() {

		// Load properties
		getProperties();
	}

	/**
	 * Set the value of a custom HTTP header
	 * 
	 * @param header the custom HTTP header to set the value for, for example
	 *        <code>X-MyApp-MyHeader</code>
	 * @param value the value of the custom HTTP header to set
	 */
	public static void setRequestCustomHttpHeader(ClientResource client, String header, String value) {

		ConcurrentMap<String, Object> reqAttribs = client.getRequest().getAttributes();
		Series<Header> headers = (Series<Header>) reqAttribs.get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (headers == null) {
			headers = new Series<Header>(Header.class);
			Series<Header> prev = (Series<Header>) reqAttribs.putIfAbsent(HeaderConstants.ATTRIBUTE_HEADERS, headers);
			if (prev != null) {
				headers = prev;
			}
		}
		headers.add(header, value);
		System.out.println("Header added [HEADER NAME: " + header + ", VALUE: " + value + "]");
	}

	public static String translate(BingAccessToken bingAccessToken, String text, String from, String to) throws IOException {

		String resource = properties.getProperty("microsoft.translator.endpoint") + "?text=" + URLEncoder.encode(text, "UTF-8").replace("+", "%20") + "&from=" + from + "&to=" + to;		

		ClientResource clientResource = new ClientResource(resource);
		setRequestCustomHttpHeader(clientResource, "Authorization", "Bearer " + bingAccessToken.getAccess_token());

		System.out.println("-> ClientResource Header:");
		System.out.println(clientResource.getRequestAttributes().get(RESTLET_HTTP_HEADERS));

		// Request data
		System.out.println("-> Executing request    GET [" + resource + "]");
		Representation responseRepresentation = null;
		responseRepresentation = clientResource.get();

		System.out.println("-> Data Response: ");
		String response = responseRepresentation.getText();
		System.out.println(response);

		return cleanResponse(response);
	}

	private static String cleanResponse(String response) {

		int begin = response.indexOf(">");
		response = response.substring(++begin);
		int end = response.indexOf("<");
		response = response.substring(0, end);
		int parentesis = response.indexOf("(");
		if (parentesis != -1) {
			response = response.substring(0, parentesis);
		}
		System.out.println("Cleanded response: " + response);
		return response;
	}

	public static BingAccessToken getAccessToken() throws IOException {

		BingAccessToken bingAccessToken = null;

		try {
			String clientId = properties.getProperty("microsoft.translator.oauth.client_id"); // Your
																							  // client
																							  // id
			String clientSecret = properties.getProperty("microsoft.translator.oauth.client_secret");// Your
																									 // client
																									 // secret
			final String uriAPI = properties.getProperty("microsoft.translator.oauth.endpoint");;

			HttpPost post = new HttpPost(uriAPI);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("grant_type", "client_credentials"));
			params.add(new BasicNameValuePair("client_id", clientId));
			params.add(new BasicNameValuePair("client_secret", clientSecret));
			params.add(new BasicNameValuePair("scope", "http://api.microsofttranslator.com"));

			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				Gson gson = new Gson();
				bingAccessToken = gson.fromJson(strResult, BingAccessToken.class);
				System.out.println(strResult);
			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return bingAccessToken;		
	}

	public static void main(String[] args) throws IOException {

		BingTranslator bingTranslator = new BingTranslator();
		BingTranslator.getAccessToken();
		// BingTranslator.translate("Hola cara de bola", "es", "en");

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
	}

}
