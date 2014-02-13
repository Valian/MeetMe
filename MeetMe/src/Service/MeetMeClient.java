package Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.util.Log;


public class MeetMeClient
{
	
	private static final String SERVICE_URL = "http://dev.isivi.pl:8600/MeetMeServer-0.0.1-SNAPSHOT";
	private static final String GET_FRIENDS_ACTION = "/getfriends";
	private static final String UPDATE_STATUS_ACTION = "/updatestatus";
	private static final String CANCEL_STATUS_ACTION = "/cancelstatus";
	private static final String GET_STATUS_ACTION = "/getstatus";
	
	private final RestTemplate rest;
	
	public MeetMeClient()
	{		
		rest = new RestTemplate();
		rest.getMessageConverters().add(new MappingJacksonHttpMessageConverter());		
	}
	
	public ArrayList<User> getFriends(String token)
	{				
		ArrayList<User> users = new ArrayList<User>();
		
		Map<String, Object> map = rest.getForObject(SERVICE_URL + GET_FRIENDS_ACTION + "?token=" + token, HashMap.class);
		
		if((Boolean)((Map<String, Object>)map.get("attributes")).get("success") == false) 
			return users;
		
		ArrayList<Map<String, Object>> friends = ((ArrayList<Map<String, Object>>)((Map<String, Object>)map.get("attributes")).get("friends"));
		
		for (Map<String, Object> friendMap : friends) 
			users.add(convertToUser(friendMap));		
		
		return users;
	}
	
	public boolean updateStatus(String token, User user)
	{	
		UserDTO userDTO = new UserDTO();
		userDTO.attributes.put("comment", user.getComment());
		userDTO.attributes.put("longitude", user.getLongitude());
		userDTO.attributes.put("latitude", user.getLatitude());
		userDTO.attributes.put("from", user.getFrom());
		userDTO.attributes.put("to", user.getTo());			
		
		Log.i("MeetMeClient", 
				"sending status: " +
						" comment:" + user.getComment() +
		" longitude:" + user.getLongitude() +
		" latitude:" + user.getLatitude() + 
		" from:" + user.getFrom() +
		" to:" + user.getTo() +
		" token:" + token
				);
		
		Map<String, Object> map = rest.postForObject(SERVICE_URL + UPDATE_STATUS_ACTION + "?token=" + token, userDTO, HashMap.class);
		
		if((Boolean)((Map<String, Object>)map.get("attributes")).get("success") == false) 
			return false;
		
		return true;
	}
	
	public boolean cancelStatus(String token)
	{						
		Map<String, Object> map = rest.postForObject(SERVICE_URL + CANCEL_STATUS_ACTION + "?token=" + token, null, HashMap.class);
		
		if((Boolean)((Map<String, Object>)map.get("attributes")).get("success") == false) 
			return false;
		
		return true;
	}
	
	public User getStatus(String token)
	{						
		Map<String, Object> map = rest.getForObject(SERVICE_URL + GET_STATUS_ACTION + "?token=" + token, HashMap.class);
		
		if((Boolean)((Map<String, Object>)map.get("attributes")).get("success") == false) 
			return null;
		
		if((Boolean)((Map<String, Object>)map.get("attributes")).get("active") == false) 
			return null;
		
		Map<String, Object> status = (Map<String, Object>)map.get("attributes");
		
		return convertToUser(status);
	}
	
	private static User convertToUser(Map<String, Object> friendMap)
	{				
		User user = new User();
		user.setActive((Boolean)friendMap.get("active"));
		user.setComment((String)friendMap.get("comment"));
		user.setFacebookId(Long.parseLong(friendMap.get("facebookId").toString()) );
		user.setFrom(new Date((Long)friendMap.get("from")));
		user.setTo(new Date((Long)friendMap.get("to")));
		user.setLatitude((Double)friendMap.get("latitude"));
		user.setLongitude((Double)friendMap.get("longitude"));			
		
		return user;
	}
	
}
