package com.search;

import java.lang.String;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class EventOperation {

	// API Keys
	private static final String key = "IEIB7ASOPLGGBW72Z5";
	private static final String personalOAuth = "ES7RJYGPUN7KVWZVLT72";
	private static final String anonOAuth = "OCG567EBYOFD3G7CGGXN";
	private static final String clientSecret = "DGOQ75ENABK47WJGNMBXGNR7BQ4NFXJFOUST337IIS47ZQNOCZ";
	private static final String authHeader = "Bearer " + anonOAuth;

	public static void main(String[] args) throws Exception {
		String lat = "51.5034070";
		String lon = "-0.1275920";
		String url = "https://www.eventbriteapi.com/v3/events/search?token="
				+ anonOAuth
				+ "&q=asu&price=free&location.within=1km&location.latitude="
				+ lat + "&location.longitude=" + lon;
		List<Event> eventList = allASUEvents(url);
		System.out.println(eventList.size());

	}

	public static List<Event> searchByKey(String key, String lat, String lon)
			throws Exception {
		String url = "https://www.eventbriteapi.com/v3/events/search?token="
				+ anonOAuth + "&q=" + key
				+ "&price=free&location.within=1km&location.latitude=" + lat
				+ "&location.longitude=" + lon;
		List<Event> eventList = allASUEvents(url);
		return eventList;
	}

	public static List<Event> search(String lat, String lon) throws Exception {
		return searchByKey("asu", lat, lon);
	}

	// HTTP GET Request: Retrieves events with the keyword "ASU"
	public static List<Event> allASUEvents(String url) throws Exception {
		String response = getRespons(url);
		System.out.println(response);
		//List<Event> eventList = JSONUtil.getEventsList(response);
		return null;
	}

	// Send and receive requests and response
	public static String getRespons(String url) throws ClientProtocolException,
			IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);
		System.out.println(response);

		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		// Result as a string
		String resultStr = result.toString();
		return resultStr;
	}

	public static String getRes(String ur) throws Exception {
		URL url = new URL(ur);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// just want to do an HTTP GET here
		connection.setRequestMethod("GET");
//		connection.

		// uncomment this if you want to write output to this url
		// connection.setDoOutput(true);

		// give it 15 seconds to respond
		connection.setReadTimeout(15 * 1000);
		connection.connect();
		// read the output from the server
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}
		return stringBuilder.toString();

	}

}
