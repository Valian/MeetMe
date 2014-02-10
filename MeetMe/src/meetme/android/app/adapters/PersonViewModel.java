package meetme.android.app.adapters;

import java.util.Calendar;
import java.util.Date;

import Service.User;

import com.facebook.model.GraphUser;

public class PersonViewModel {

	public String name;
	public String comment;
	public String availabilityInfo;
	public String thumbnailURL;
	
	public PersonViewModel(String name, String comment, String availabilityInfo, String thumbnailURL)
	{
		this.name = name;
		this.comment = comment;
		this.availabilityInfo = availabilityInfo;
		this.thumbnailURL = thumbnailURL;
	}
	
	public PersonViewModel (User meetMeUser, GraphUser facebookUser, String available_in, String available_for, String available_already) {
		Calendar calendar = Calendar.getInstance(); 
		Long untilStart = meetMeUser.getFrom().getTime() - calendar.getTime().getTime();
		String availabilityInfo;
		if(untilStart > 0) { //not available yet
			String untilStartStr = milisecondsToTimeString(untilStart);
			Long duration = meetMeUser.getTo().getTime() - meetMeUser.getFrom().getTime();
			String  durationStr = milisecondsToTimeString(duration);
			availabilityInfo = 
					available_in+" "+untilStartStr + "&#10"+
					available_for+" "+durationStr;
					
		} else { //already available
			Long untilEnd = meetMeUser.getTo().getTime() - calendar.getTime().getTime();
			String untilEndStr = milisecondsToTimeString(untilEnd);
			availabilityInfo = available_already + " " + untilEndStr;
		}
		
		//Log.i("INFO", user.getName());
		
		this.name = facebookUser.getName();
		this.comment = meetMeUser.getComment();
		this.availabilityInfo = availabilityInfo;
		this.thumbnailURL = "http://graph.facebook.com/"+facebookUser.getId()+"/picture?type=large";
		
	}

	private static String milisecondsToTimeString(Long milliseconds)
	{
		Date date = new Date(milliseconds);
		String hours = date.getHours() > 0 ? date.getHours()+"h ":"";
		String minutes = date.getMinutes()+"min";
		return hours+minutes;		
	}
}
