package meetme.android.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import Service.GetFriendsTask;
import Service.User;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import meetme.android.app.MeetMeCacheService.MeetMeCacheBinder;
import meetme.android.app.adapters.FriendLazyAdapter;
import meetme.android.app.adapters.PersonViewModel;
import meetme.android.core.*;
import meetme.android.core.dialogs.TimePickerButton;

public class FriendListActivity extends ActionBarActivity {

	private ListView friendListView;
	
	
	private Boolean bound;
	private MeetMeCacheService cacheService;
	private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	MeetMeCacheBinder binder = (MeetMeCacheBinder) service;
        	cacheService = binder.getService();
        	bound = true;
        	
        	//Log.i("number", String.valueOf(cacheService.getNumber()));
        	
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	bound = false;
        }
    };
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		Intent intent = new Intent(this, MeetMeCacheService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
		
		/*String facebookToken = Session.getActiveSession().getAccessToken();	
		new GetFriendsTask() {
			 protected void onPostExecute(ArrayList<User> meetMeUsers) {
				 
				 for(User user : meetMeUsers) 				 {
					 Log.i("Get Friends result", "id:"+user.getFacebookId()+" "+ user.getComment().toString());
				 }
				 
				 
				 
				ArrayList<PersonViewModel> friendList = new ArrayList<PersonViewModel>();
				populateFriendList(meetMeUsers, friendList);

				friendList.add(new PersonViewModel("stefan", "blabla", "niedlugo", ""));
				 
				FriendLazyAdapter adapter=new FriendLazyAdapter(FriendListActivity.this, friendList);
				friendListView = (ListView)findViewById(R.id.friendListView);
				friendListView.setAdapter(adapter);
				        
			 }
		}.execute(facebookToken);*/
		
		
	}
	
	
	@Override
	protected void onStart()	{
		super.onStart();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_bar_map:
	            openMap();
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void populateFriendList(final List<User> meetMeUsers, final List<PersonViewModel> result) {
			
		
		Session session = Session.getActiveSession();
		
		Request friendRequest = Request.newMyFriendsRequest(session, 
 	            new Request.GraphUserListCallback(){
 	                @Override
 	                public void onCompleted(List<GraphUser> facebookUsers,
 	                        Response response) {
 	                    
 	                	/*String string = "";
 	                	for(GraphUser user : facebookUsers)
 	                	{
 	                		string += user.getId() + " ";
 	                	}
 	                	Log.i("FriendListActivity", "facebook users received: "+string);*/
 	                	
 	                    for(User meetMeUser : meetMeUsers)
 	                    {
 	                    	PersonViewModel person = meetMeUserToPersonViewModel(meetMeUser, facebookUsers);
 	                    	if(person != null)
 	                    	{
 	                    		Log.i("FriendListActivity", "meetMeUser id matches person: "+person.name);
 	                    		result.add(person);   
 	                    	}
 	                    	else
 	                    		Log.w("FriendListActivity", "meetMeUser id matches no facebook id");
 	                    	
 	                    }
 	                }
 	    });
 	    Bundle params = new Bundle();
 	    //params.putString("fields", "id, name, picture");
 	    params.putString("fields", "id, name, picture");
 	    friendRequest.setParameters(params);
 	    friendRequest.executeAsync();
	}

	private PersonViewModel meetMeUserToPersonViewModel(User meetMeUser, List<GraphUser> facebookUsers)
	{
		for(GraphUser facebookUser : facebookUsers)
     	{
     		if(facebookUser.getId().equals(String.valueOf(meetMeUser.getFacebookId())))
     		{
     			
     			Calendar calendar = Calendar.getInstance(); 
     			Long untilStart = meetMeUser.getFrom().getTime() - calendar.getTime().getTime();
     			String availabilityInfo;
     			if(untilStart > 0) { //not available yet
     				String untilStartStr = milisecondsToTimeString(untilStart);
     				Long duration = meetMeUser.getTo().getTime() - meetMeUser.getFrom().getTime();
     				String  durationStr = milisecondsToTimeString(duration);
     				availabilityInfo = 
     					getString(R.string.available_in)+" "+untilStartStr + " "+
     					getString(R.string.available_for)+" "+durationStr;
     						
     			} else { //already available
     				Long untilEnd = meetMeUser.getTo().getTime() - calendar.getTime().getTime();
     				String untilEndStr = milisecondsToTimeString(untilEnd);
     				availabilityInfo = getString(R.string.available_already)+" "+untilEndStr;
     			}
     			
     			//Log.i("INFO", user.getName());
             	return new PersonViewModel(
             		facebookUser.getName(),
             		meetMeUser.getComment(),
             		availabilityInfo,
             		"http://graph.facebook.com/"+facebookUser.getId()+"/picture?type=large"
             	);
     			
     		}
     		
     	}
		
		return null;
	}

	private String milisecondsToTimeString(Long milliseconds)
	{
		Date date = new Date(milliseconds);
		String hours = date.getHours() > 0 ? date.getHours()+"h ":"";
		String minutes = date.getMinutes()+"min";
		return hours+minutes;
		
	}
	
	private void openMap() {
		// TODO Auto-generated method stub
		
	}
	
	
	/*private List<GraphUser> getFriends(){
    Request friendRequest = Request.newMyFriendsRequest(session, 
					 	            new GraphUserListCallback(){
					 	                @Override
					 	                public void onCompleted(List<GraphUser> users,
					 	                        Response response) {
					 	                    
					 	                    for(GraphUser user : users)
					 	                    {
					 	                    	Log.i("INFO", user.getName());
					 	                    	
					 	                    }
					 	                }
					 	        });
					 	        Bundle params = new Bundle();
					 	        //params.putString("fields", "id, name, first_name, last_name");
					 	        params.putString("fields", "id, name");
					 	        friendRequest.setParameters(params);
					 	        friendRequest.executeAsync();
	}*/

}
