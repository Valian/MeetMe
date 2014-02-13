package meetme.android.app.adapters;

import java.util.Calendar;
import java.util.Date;

import Service.User;

import android.text.format.Time;
import android.util.Log;

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
		
		Log.i("PersonViewModel", "from:"+meetMeUser.getFrom().toString());
		Log.i("PersonViewModel", "to:"+meetMeUser.getTo().toString());
		
		 Time now = new Time();
		 now.setToNow();
		 
		 Long untilStart = meetMeUser.getFrom().getTime() - now.toMillis(false);
		 String availabilityInfo;
		 if(untilStart > 0) { //not available yet
				String untilStartStr = milisecondsToTimeString(untilStart);
				Long duration = meetMeUser.getTo().getTime() - meetMeUser.getFrom().getTime();
				String  durationStr = milisecondsToTimeString(duration);
				availabilityInfo = 
						available_in+" "+untilStartStr + " " + available_for+" "+durationStr;
						
			} else { //already available
				Long untilEnd = meetMeUser.getTo().getTime() - now.toMillis(false);
				String untilEndStr = milisecondsToTimeString(untilEnd);
				availabilityInfo = available_already + " " + untilEndStr;
			}
		 
		 
		this.name = facebookUser.getName();
		this.comment = meetMeUser.getComment();
		this.availabilityInfo = availabilityInfo;
		this.thumbnailURL = "http://graph.facebook.com/"+facebookUser.getId()+"/picture?type=normal";
		
	}

	private static String milisecondsToTimeString(Long milliseconds)
	{
		Log.i("personviewmodel", "milliseconds: "+milliseconds);
		Date date = new Date(milliseconds);
		
		Time time = new Time();
		time.set(milliseconds);
		/*String days = date.getDay() > 0 ? date.getDay()+" days ":"";
		String hours = date.getHours() > 0 ? date.getHours()+"h ":"";
		String minutes = date.getMinutes()+"min";*/
		
		
		long minute = (milliseconds / (1000 * 60)) % 60;
		long hour = (milliseconds / (1000 * 60 * 60)) % 24;
		long day = milliseconds / (1000 * 60 * 60 * 24);
		
		/*String days = time.monthDay > 0 ? time.monthDay+" days ":"";
		String hours = time.hour > 0 ? time.hour+"h ":"";
		String minutes = time.minute+"min";*/
		
		String days = day > 0 ? day+" days ":"";
		String hours = hour > 0 ? hour+"h ":"";
		String minutes = minute+"min";
		
		return days+hours+minutes;		
	}
}
