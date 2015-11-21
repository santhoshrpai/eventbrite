package com.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	public static List<Event> getEventsList(String response)
			throws JSONException, Exception {
		List<Event> eventList = new ArrayList<Event>();
		JSONObject resultJson = new JSONObject(response);
		JSONArray res = resultJson.getJSONArray("top_match_events");
		for (int i = 0; i < 10; i++) {
			Event event = getEvent(res.getJSONObject(i));
			if (event != null) {
				eventList.add(event);
			}
		}
		return eventList;
	}

	public static JSONObject getJSONObject(JSONObject json, String key) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = json.getJSONObject(key);
		} catch (Exception e) {
			return null;
		}
		return jsonObject;
	}

	public static String getValue(JSONObject json, String key) {
		String string = new String();
		try {
			string = json.getString(key);
		} catch (Exception e) {
			return null;
		}
		return string;
	}

	public static Event getEvent(JSONObject jEvent) throws Exception {
		Event event = new Event();
		try {
			JSONObject names = getJSONObject(jEvent, "name");
			JSONObject desc = getJSONObject(jEvent, "description");
			JSONObject start = getJSONObject(jEvent, "start");
			JSONObject end = getJSONObject(jEvent, "end");
			JSONObject logo = getJSONObject(jEvent, "logo");
			String url = getValue(jEvent, "url");
			String organizerId = getValue(jEvent, "organizer_id");
			String venueId = getValue(jEvent, "venue_id");
			String categoryId = getValue(jEvent, "category_id");
			String id = getValue(jEvent, "id");
			int capacity = (int) jEvent.getInt("capacity");

			Venue venue = getVenue(venueId);
			if (venue != null) {
				event.setVenue(venue);
				event.setId(id);
				event.setDescription(getValue(desc, "text"));
				event.setTitle(getValue(names, "text"));
				event.setStartDate(getDate(start, "local"));
				event.setEndDate(getDate(end, "local"));
				event.setLogo(getValue(logo, "url"));
				event.setUrl(url);
				Organizer org = getOrganizer(organizerId);
				event.setOrganizer(org);
				String category = getCategory(categoryId);
				event.setCategory(category);
			} else {
				event = null;
			}
		} catch (JSONException e) {
			event = null;
		}
		return event;
	}

	public static Date getDate(JSONObject dateJson, String key)
			throws java.text.ParseException {
		String date = getValue(dateJson, key);
		date = date.replace("T", " ");
		date = date.replace("Z", "");
		System.out.println(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date parsedDate = formatter.parse(date);
		return parsedDate;
	}

	public static Venue getVenue(String venueId) throws Exception {
		Venue venue = new Venue();
		String url = "https://www.eventbriteapi.com/v3/venues/" + venueId
				+ "/?token=ES7RJYGPUN7KVWZVLT72";
		String response = EventOperation.getRespons(url);
		JSONObject resultJson = new JSONObject(response);
		float lat = (float) resultJson.getDouble("latitude");
		float longitude = (float) resultJson.getDouble("longitude");
		if (lat == 0 && longitude == 0) {
			return null;
		}
		JSONObject address = resultJson.getJSONObject("address");
		String add1 = getValue(address, "address_1");
		// String add2 = getValue(address, "address_2");
		String add2 = "";
		String city = getValue(address, "city");
		String region = getValue(address, "region");
		String zip = getValue(address, "postal_code");
		String country = getValue(address, "country");
		float lati = (float) address.getDouble("latitude");
		float longi = (float) address.getDouble("longitude");
		venue.setAddress(add1 + ", " + add2);
		venue.setCity(city);
		venue.setCountry(country);
		venue.setId(venueId);
		venue.setLatitudeLongitude(lati + "," + longi);
		venue.setPostalCode(zip);
		venue.setRegion(region);
		return venue;
	}

	public static Organizer getOrganizer(String organizerId) throws Exception {
		Organizer org = new Organizer();
		String url = "https://www.eventbriteapi.com/v3/organizers/"
				+ organizerId + "/?token=ES7RJYGPUN7KVWZVLT72";
		String response = EventOperation.getRespons(url);
		JSONObject orgJson = new JSONObject(response);
		String name = getValue(orgJson, "name");
		org.setName(name);
		org.setId(organizerId);
		return org;
	}

	public static String getCategory(String categoryId) throws Exception {
		String url = "https://www.eventbriteapi.com/v3/categories/"
				+ categoryId + "/?token=ES7RJYGPUN7KVWZVLT72";
		String response = EventOperation.getRespons(url);
		JSONObject orgJson = new JSONObject(response);
		return getValue(orgJson, "name");
	}
}
