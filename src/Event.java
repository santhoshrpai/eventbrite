import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.String;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


public class Event {
	
	// API Keys
	final String key = "IEIB7ASOPLGGBW72Z5";
	final String personalOAuth = "ES7RJYGPUN7KVWZVLT72";
	final String anonOAuth = "OCG567EBYOFD3G7CGGXN";
	final String clientSecret = "DGOQ75ENABK47WJGNMBXGNR7BQ4NFXJFOUST337IIS47ZQNOCZ";
	final String authHeader = "Bearer " + anonOAuth;
	
	public static void main(String[] args) throws Exception {

		Event http = new Event();

		System.out.println("TEST 1 -- ASU Events");
		http.AllAsuEvents();
		
	}
		
	// HTTP GET Request: Retrieves events with the keyword "ASU"
	public ArrayList<Event> AllAsuEvents() throws Exception {
		String url = "https://www.eventbriteapi.com/v3/events/search/?token="+ anonOAuth +"&q=asu";
		HttpClient client =HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		
		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + 
                       response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		// Result as a string
		String resultStr = result.toString();
		
		System.out.println(resultStr);
		
//		// Convert result to a JSON array
//	    JSONParser parser = new JSONParser();
//	    Object obj = parser.parse(resultStr);
//	    JSONArray resultJson = new JSONArray();
//	    resultJson.add(obj);
//	      
//	    System.out.println("The 2nd element of array");
//	    System.out.println(resultJson.get(0));


		return null;
	}
	
	
}
