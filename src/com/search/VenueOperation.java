package com.search;

public class VenueOperation {
	public static Venue getVenueById(String id) throws Exception {
		String url = "https://www.eventbriteapi.com/v3/venues/" + id 
				+ "?token=" + EventOperation.anonOAuth;
		String htmlRes = EventOperation.getRes(url);
		String response = HTMLParser.convertEventToJson(htmlRes);
		System.out.println(response);
		
		
		return null;
		
	}
}
