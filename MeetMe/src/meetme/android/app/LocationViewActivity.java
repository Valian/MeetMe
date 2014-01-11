package meetme.android.app;

import meetme.android.core.GooglePlayConnector;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

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
	
	
	/* Google play services handler */
	 @Override
	    public void onConnected(Bundle dataBundle) {

	        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).getMap();
	        Location location = googlePlayConnector.getLastLocation();
	        
	        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
      
	        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));

	        map.addMarker(new MarkerOptions()
	                .title("Meee!")
	                .snippet("Moje smieci")
	                .position(currentLocation));
	    }
	 
	 /* Google play services handler */
	    @Override
	    public void onDisconnected() {
	        Toast.makeText(this, "Disconnected!", Toast.LENGTH_SHORT).show();
	    }
 
	    /* Google play services handler */
	    @Override
	    public void onConnectionFailed(ConnectionResult connectionResult) {
	    	 Toast.makeText(this, "Connection failed!", Toast.LENGTH_SHORT).show();
	    }
	    
	
	    
	@SuppressLint("NewApi")
	@Override
    protected void onStart() {
        super.onStart();   
        try {
        	googlePlayConnector.connect();
        }
        catch(Exception e)
        {
        	Toast.makeText(this, "Failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
   
    /*
     * Called when the Activity is no longer visible.
     */
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
		
		googlePlayConnector = new GooglePlayConnector(this);
		googlePlayConnector.setConnectionCallbacks(this);
		googlePlayConnector.setOnConnectionFailedListener(this);
		
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).getMap();
        map.setMyLocationEnabled(true);
        
        
	    
	    
	}



}
