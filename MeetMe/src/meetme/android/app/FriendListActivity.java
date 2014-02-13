package meetme.android.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import meetme.android.app.MeetMeCacheService.FriendListReceivedListener;
import meetme.android.app.MeetMeCacheService.MeetMeCacheBinder;
import meetme.android.app.adapters.FriendLazyAdapter;
import meetme.android.app.adapters.PersonViewModel;
import Service.User;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.model.GraphUser;

public class FriendListActivity extends ActionBarActivity {


	private ListView friendListView;
	
	private SplashScreenFragment splashScreen = new SplashScreenFragment();
	private boolean loadingDisplay = false;
	
	private Boolean bound;
	private MeetMeCacheService cacheService;
	private ServiceConnection cacheServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

        	MeetMeCacheBinder binder = (MeetMeCacheBinder) service;
        	cacheService = binder.getService();
        	bound = true;
        	
        	updateDisplay();
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
        bindService(intent, cacheServiceConnection, Context.BIND_AUTO_CREATE);
			
	}
	
	private void updateDisplay()
	{
		List<User> meetMeUsers = cacheService.getLastMeetMeFriendList();
		List<GraphUser> facebookUsers = cacheService.getLastFacebookUsers();
		if(meetMeUsers != null && facebookUsers != null)
		{
			//ArrayList<PersonViewModel> friendList = ;
			displayFriendList(getPersonViewModels(facebookUsers, meetMeUsers));		
		}
		else
		{
			loadingDisplay = true;
			getSupportFragmentManager().beginTransaction()
	        .add(android.R.id.content, splashScreen).commit();
		}
		
		cacheService.getFriendList(new FriendListReceivedListener() {

			@Override
			public void call(ArrayList<User> meetMeUsers, List<GraphUser> facebookUsers) {
				displayFriendList(getPersonViewModels(facebookUsers, meetMeUsers));
				
				if(loadingDisplay)
				{
					getSupportFragmentManager().beginTransaction()
			        .remove(splashScreen).commit();
					loadingDisplay = false;
				}
			}
			
		});
		
	}
	
	private void displayFriendList(ArrayList<PersonViewModel> friendList)
	{
		FriendLazyAdapter adapter = new FriendLazyAdapter(FriendListActivity.this, friendList);
		friendListView = (ListView)findViewById(R.id.friendListView);
		friendListView.setAdapter(adapter);
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
	
	
	
	public ArrayList<PersonViewModel> getPersonViewModels(List<GraphUser> facebookUsers, final List<User> meetMeUsers)		
	{
		ArrayList<PersonViewModel> result = new ArrayList<PersonViewModel>();
		
		for(User meetMeUser : meetMeUsers)
		{
			//TODO to kiedys bedzie w serwisie i nie bedziemy dostawac nieaktualnych statusow
			if(meetMeUser.getTo().after(new Date()))
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
     	
		return result;     	
	}

	private PersonViewModel meetMeUserToPersonViewModel(User meetMeUser, List<GraphUser> facebookUsers)
	{
		for(GraphUser facebookUser : facebookUsers)
     	{
     		if(facebookUser.getId().equals(String.valueOf(meetMeUser.getFacebookId())))
     		{     			
             	return new PersonViewModel(meetMeUser, facebookUser, getString(R.string.available_in),  getString(R.string.available_for), getString(R.string.available_already));
       		}
     		
     	}
		
		return null;
	}
	
	private void openMap() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onDestroy(){
		unbindService(cacheServiceConnection);
		
		if(loadingDisplay){
			getSupportFragmentManager()
	        .beginTransaction()
	        .remove(splashScreen)
	        .commit();
		}
		super.onDestroy();
	}
}
