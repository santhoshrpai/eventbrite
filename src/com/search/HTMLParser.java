package com.search;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HTMLParser {
	public static String convertEventToJson(String htmlResult) {
		String jsonResult = htmlResult.replaceAll("<.*?>","");
		jsonResult = jsonResult.replaceAll("\\s+", " ");
		jsonResult = jsonResult.replace("&quot;", "\"");
		jsonResult = jsonResult.substring(jsonResult.indexOf("{ \"changed\":"));
		jsonResult = jsonResult.substring(0, jsonResult.indexOf("// When GET"));
		jsonResult = jsonResult.substring(0, jsonResult.indexOf("pagination"));
		
		return jsonResult;
	}
	
	public static String convertOrgToJson(String htmlResult) {
		String jsonResult = htmlResult.replaceAll("<.*?>","");
		jsonResult = jsonResult.replaceAll("\\s+", " ");
		jsonResult = jsonResult.replace("&quot;", "\"");
		jsonResult = jsonResult.substring(jsonResult.indexOf("\"description\": "));
		jsonResult = jsonResult.substring(0, jsonResult.indexOf("// When GET"));
		
		return jsonResult;
	}
	
	public static List<Event> getEventsList(String jsonResult) throws Exception {
		List<String> lines = Arrays.asList(jsonResult.split("\\}, \\{"));
		
		List<Event> eventList = new ArrayList<Event>();
		
		for (String line : lines) {
			Event eventBean = new Event();
			
			String id = line.substring(line.indexOf("\"id\": \""));
			
			// In case of logo id
			if (!Character.isDigit(id.charAt(16))) {
				id = id.substring(id.indexOf("edge_color_set"));
				id = id.substring(id.indexOf("\"id\": \""));
			}
			id = id.substring(7, id.indexOf("\","));

			
			String title = line.substring(line.indexOf("\"name\":"));
			title = title.substring(0, title.indexOf(","));
			title = title.substring(title.indexOf(": \""));
			title = title.substring(3, title.length() - 1);
		
			String desc = line.substring(line.indexOf("\"description\":"));
			desc = desc.substring(26, desc.indexOf("\", \"html\": "));
			
			
			String cat = null;
			cat = line.substring(line.lastIndexOf(":"));
			cat = cat.trim();
			cat = cat.substring(3, cat.length() - 1);
			if (cat.contains("\"")) {
				cat = cat.substring(0, cat.indexOf("\""));
			}
			else if (cat.length() > 4) {
				cat = null;
			}
			
			String startDt = line.substring(line.indexOf("\"start\":"));
			startDt = startDt.substring(startDt.indexOf("\"local\":"));
			startDt = startDt.substring(startDt.indexOf(":") + 3, startDt.indexOf(",") - 1);
			Date start = convertToDate(startDt, "yyyy-MM-dd'T'HH:mm:ss");
			

			String endDt = line.substring(line.indexOf("\"start\":"));
			endDt = endDt.substring(endDt.indexOf("\"local\":"));
			endDt = endDt.substring(endDt.indexOf(":") + 3, endDt.indexOf(",") - 1);
			Date end = convertToDate(endDt, "yyyy-MM-dd'T'HH:mm:ss");
						
			String tz = line.substring(line.indexOf("\"timezone\":"));
			tz = tz.substring(tz.indexOf(":") + 3, tz.indexOf(",") - 1);			

			String createdDt = line.substring(line.indexOf("\"created\":"));
			createdDt = createdDt.substring(createdDt.indexOf(":") + 3, createdDt.indexOf(",") - 1);			
			Date created = convertToDate(createdDt, "yyyy-MM-dd'T'HH:mm:ss'Z'");
			
			String priv = line.substring(line.indexOf("\"privacy_setting\": "));
			priv = priv.substring(priv.indexOf(":") + 3, priv.indexOf(",") - 1);			

			String url = line.substring(line.indexOf("\"url\": "));
			url = url.substring(url.indexOf(":") + 3, url.indexOf(",") - 1);			
			
			String logo = null;
			if (line.contains("\"logo_id\": ")) {
				logo = line.substring(line.indexOf("\"logo_id\": "));
				logo = logo.substring(logo.indexOf(":") + 3, logo.indexOf(",") - 1);
			}


			Venue ven = new Venue();
			String venId = line.substring(line.indexOf("\"venue_id\":"));
			venId = venId.substring(venId.indexOf(":") + 3, venId.indexOf(",") - 1);
			ven.setId(venId);
			
			//Organizer org = new Organizer();
			String orgId = line.substring(line.indexOf("\"organizer_id\":"));
			if (orgId.length() > 35){
				orgId = orgId.substring(orgId.indexOf(":") + 3, orgId.indexOf(",") - 1);
			}
			else {
				orgId = orgId.substring(orgId.indexOf(":") + 3, orgId.length() - 2);
			}
			Organizer org = OrganizerOperation.getOrgById(orgId);
			
			eventBean.setCategory(cat); 
			eventBean.setStartDate(start);
			eventBean.setEndDate(end);
			eventBean.setTimeZone(tz);
			eventBean.setCreated(created);
			eventBean.setPrivacy(priv);
			eventBean.setUrl(url);
			eventBean.setLogo(logo);
			eventBean.setVenue(ven);
			eventBean.setOrganizer(org);
			eventBean.setId(id);
			eventBean.setTitle(title);
			eventBean.setDescription(desc);
			eventList.add(eventBean);
			
		}
		return eventList;
		
	}
	
	public static Date convertToDate(String dt, String format) throws ParseException {
	    DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
	    Date result =  df.parse(dt);  
		return result;
		
	}
}
