package meetme.android.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;


@SuppressLint("NewApi")
public class GooglePlayConnector 
implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{

	private final static int
    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
		
	private Activity context;
	
	private LocationClient locationClient;
	
	public GooglePlayConnector(Activity context)
	{
		this.context = context;	
		locationClient = new LocationClient(context, this, this);
	}
	
	private GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener;
	private GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks;
	
	public void setOnConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener)
	{
		onConnectionFailedListener = listener;
	}
	
	public void setConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener)
	{
		connectionCallbacks = listener;
	}
	
	public void connect() throws Exception
	{
		if(!servicesConnected()) throw new Exception("Google services unavailable");
		locationClient.connect();
	}
	
	public void disconnect()
	{
		locationClient.disconnect();
	}
	
	public Location getLastLocation()
	{
		return locationClient.getLastLocation();
	}
	

	private boolean servicesConnected()
	{
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		return resultCode == ConnectionResult.SUCCESS;

	}

	
	 @Override
	    public void onConnected(Bundle dataBundle) {
		 	if(connectionCallbacks != null) connectionCallbacks.onConnected(dataBundle);
	    }
	 
	    @Override
	    public void onDisconnected() {
	    	if(connectionCallbacks != null) connectionCallbacks.onDisconnected();
	    }

	    
	    @Override
	    public void onConnectionFailed(ConnectionResult connectionResult) {
	        
	         /** Google Play services can resolve some errors it detects.
	         * If the error has a resolution, try sending an Intent to
	         * start a Google Play services activity that can resolve
	         * error.*/
	         
	        if (connectionResult.hasResolution()) {
	            try {
	                // Start an Activity that tries to resolve the error
	                connectionResult.startResolutionForResult(
	                        context,
	                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
	                
	                 /** Thrown if Google Play services canceled the original
	                 * PendingIntent*/
	                 
	            } catch (IntentSender.SendIntentException e) {
	                
	                e.printStackTrace();
	                if(onConnectionFailedListener != null)
	                	onConnectionFailedListener.onConnectionFailed(connectionResult);
	            }
	        } else {
	        	if(onConnectionFailedListener != null)
	        		onConnectionFailedListener.onConnectionFailed(connectionResult);
	        }
	    }
    

}
