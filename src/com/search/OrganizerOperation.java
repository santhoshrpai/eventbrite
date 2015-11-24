package com.search;

public class OrganizerOperation {
	public static Organizer getOrgById(String id) throws Exception {
		String urlReq = "https://www.eventbriteapi.com/v3/organizers/" + id 
				+ "?token=" + EventOperation.anonOAuth;
		String htmlRes = EventOperation.getRes(urlReq);
		String response = HTMLParser.convertOrgToJson(htmlRes);
		
		Organizer org = new Organizer();
		String desc = response.substring(response.indexOf("\"description\":"));

		if (desc.contains("\", \"html\": ")) {
			desc = desc.substring(26, desc.indexOf("\", \"html\": "));
		}
		else {
			desc = null;
		}
		String url = response.substring(response.indexOf("\"url\":"));
		url = url.substring(url.indexOf(":") + 3, url.indexOf(",") - 1);
		
		String name = response.substring(response.indexOf("\"name\":"));
		name = name.substring(name.indexOf(":") + 3, name.indexOf(",") - 1);
		
		org.setId(id);
		org.setDescription(desc);
		org.setName(name);
		org.setUrl(url);
		
		return org;
	}
}
