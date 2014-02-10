package meetme.android.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import meetme.android.app.MeetMeCacheService.FriendListReceivedListener;
import meetme.android.app.MeetMeCacheService.MeetMeCacheBinder;
import meetme.android.app.adapters.PersonMapMarker;
import meetme.android.app.adapters.PersonViewModel;
import meetme.android.core.GooglePlayConnector;
import Service.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



@SuppressLint("NewApi")
public class LocationViewActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener
{

	private GooglePlayConnector googlePlayConnector;
	
	
	 /*Google play services handler */
	 @Override
	    public void onConnected(Bundle dataBundle) {
	        
	    }
	 
	  /*Google play services handler */
	    @Override
	    public void onDisconnected() {
	        Toast.makeText(this, "Disconnected!", Toast.LENGTH_SHORT).show();
	    }
 
	     /*Google play services handler */
	    @Override
	    public void onConnectionFailed(ConnectionResult connectionResult) {
	    	 Toast.makeText(this, "Connection failed!", Toast.LENGTH_SHORT).show();
	    }
	    
	
	    
	@SuppressLint("NewApi")
	@Override
    protected void onStart() {
        super.onStart();   
        
        try {
			googlePlayConnector = new GooglePlayConnector(this);
			googlePlayConnector.setConnectionCallbacks(this);
			googlePlayConnector.setOnConnectionFailedListener(this);
			
        	googlePlayConnector.connect();
        }
        catch(Exception e)
        {
        	Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }

	
	private Boolean bound;
	private MeetMeCacheService cacheService;
	private ServiceConnection cacheServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	Log.i("LocationViewActivity", "connected to MeetMeCacheService");
        	MeetMeCacheBinder binder = (MeetMeCacheBinder) service;
        	cacheService = binder.getService();
        	bound = true;
        	
        	updateDisplay();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.w("LocationViewActivity", "disconnected from MeetMeCacheService");
        	bound = false;
        }
    };
    
    private void updateDisplay()
    {
    	cacheService.getFriendList(new FriendListReceivedListener() {

			@Override
			public void call(ArrayList<User> meetMeUsers, List<GraphUser> facebookUsers) {
				ArrayList<PersonMapMarker> friendList = getPersonMapMarkers(facebookUsers, meetMeUsers);
				
				GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).getMap();	     
				
				Location location = googlePlayConnector.getLastLocation();
		        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

		        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
				
		        map.addMarker(new MarkerOptions()
                .position(currentLocation));
		        
				for(PersonMapMarker person : friendList)
				{
					
					

			        map.addMarker(new MarkerOptions()
			                .title(person.name)
			                .snippet(person.name)
			                .position(person.location));
			        
				}
			}
    		
    	});
    }
    
    
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
    	googlePlayConnector.disconnect();
        super.onStop();
    }
    
    
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this line must go first!
		setContentView(R.layout.activity_location_view);
		
		Intent intent = new Intent(this, MeetMeCacheService.class);
        bindService(intent, cacheServiceConnection, Context.BIND_AUTO_CREATE);
		
		//GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).getMap();
        //map.setMyLocationEnabled(true);
	    
	}

	public ArrayList<PersonMapMarker> getPersonMapMarkers(List<GraphUser> facebookUsers, final List<User> meetMeUsers)		
	{

		ArrayList<PersonMapMarker> result = new ArrayList<PersonMapMarker>();
		
		for(User meetMeUser : meetMeUsers)
		{
			PersonMapMarker person = meetMeUserToPersonMapMarker(meetMeUser, facebookUsers);
	     	if(person != null)
	     	{
	     		Log.i("FriendListActivity", "meetMeUser id matches person: "+person.name);
	     		result.add(person);   
	     	}
	     	else
	     		Log.w("FriendListActivity", "meetMeUser id matches no facebook id");
		}
     	
		return result;
	}

	private PersonMapMarker meetMeUserToPersonMapMarker(User meetMeUser, List<GraphUser> facebookUsers)
	{
		for(GraphUser facebookUser : facebookUsers)
     	{
     		if(facebookUser.getId().equals(String.valueOf(meetMeUser.getFacebookId())))
     		{
     			//Log.i("INFO", user.getName());
             	return new PersonMapMarker(
             		facebookUser.getName(),
             		new LatLng(meetMeUser.getLatitude(), meetMeUser.getLongitude())
             	);
     			
     		}
     		
     	}
		
		return null;
	}
	
	@Override
	protected void onDestroy(){
		unbindService(cacheServiceConnection);
		super.onDestroy();
	}

}
