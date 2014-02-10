package meetme.android.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import meetme.android.app.MeetMeCacheService.MeetMeCacheBinder;
import meetme.android.app.MeetMeCacheService.StatusReceivedListener;
import meetme.android.app.R.style;

import Service.CancelStatusTask;
import Service.GetFriendsTask;
import Service.GetStatusTask;
import Service.MeetMeClient;
import Service.UpdateStatusTask;
import Service.User;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class MainMenuActivity extends ActionBarActivity {

	private Button statusSetButton;
	private Button statusCancelButton;
	private Button friendListButton;

	private SplashScreenFragment splashScreen = new SplashScreenFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);
		
		statusSetButton = (Button) findViewById(R.id.statusSetButton);	
		statusSetButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainMenuActivity.this, StatusChangeActivity.class);
				startActivity(intent);
			}
		});
		
		statusCancelButton = (Button) findViewById(R.id.statusCancelButton);	
		statusCancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
				
				String facebookToken = Session.getActiveSession().getAccessToken();	
				new CancelStatusTask() {
					@Override
					protected void onPostExecute(Boolean result) {     
				    	Log.i("Cancel Status result", result.toString());
				    	reloadActivity();
				    }
				}.execute(facebookToken);
				
				
			}
		});
		
		/*friendListButton = (Button) findViewById(R.id.friendListButton);	
		friendListButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				openFriends();
			}
		});*/
		
		Intent intent = new Intent(this, MeetMeCacheService.class);
        bindService(intent, cacheServiceConnection, Context.BIND_AUTO_CREATE);
        
	}
	
	public void reloadActivity() {

	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();

	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}
	
	private Boolean bound;
	private MeetMeCacheService cacheService;
	private ServiceConnection cacheServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	Log.i("MainMenuActivity", "connected to MeetMeCacheService");
        	MeetMeCacheBinder binder = (MeetMeCacheBinder) service;
        	cacheService = binder.getService();
        	bound = true;
        	
        	getStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	Log.w("MainMenuActivity", "disconnected from MeetMeCacheService");
        	bound = false;
        }
    };

    private void getStatus()
    {
    	User user = cacheService.getLastStatus();
    	
    	if(user != null) 
    		updateDisplay(user);
    	else
    	{
    		getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, splashScreen)
	        .commit();
    		
    	}
    	
    	cacheService.getStatus(new StatusReceivedListener() {

			@Override
			public void call(User user) {
				
				Log.i("status", 
						"received status: comment: "+user.getComment() + 
						", from: "+ user.getFrom().toString() +
						", to: " + user.getTo().toString());
				
				updateDisplay(user);
		    	
		    	getSupportFragmentManager()
		        .beginTransaction()
		        .remove(splashScreen)
		        .commit();
				
			}
    		
    	});
    }
    
    private void updateDisplay(User user)
    {
    	if(user != null)
    	{	
    		statusSetButton.setVisibility(View.INVISIBLE);
    		statusCancelButton.setVisibility(View.VISIBLE);
    		
    		//TODO wyswietlic status
    	}
    	else
    	{
    		statusSetButton.setVisibility(View.VISIBLE);
    		statusCancelButton.setVisibility(View.INVISIBLE);
    	}
    }
    
	@Override
	protected void onStart() {
		super.onStart();
		
		Session session = Session.getActiveSession();
		if(session == null || !session.isOpened()) {
			backToHome();
			return;
		}
		
		
	}
	
	@Override
	protected void onDestroy(){
		unbindService(cacheServiceConnection);
		super.onDestroy();
	}
	
	private void backToHome()
	{
		Intent intent = new Intent(MainMenuActivity.this, HomeActivity.class);
		startActivity(intent);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_bar_map:
	            openMap();
	            return true;
	        case R.id.action_bar_friends:
	            openFriends();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}	

	private void openMap() {
		Intent intent = new Intent(MainMenuActivity.this, LocationViewActivity.class);
		startActivity(intent);
		
	}
	
	private void openFriends() {
		Intent intent = new Intent(MainMenuActivity.this, FriendListActivity.class);
		startActivity(intent);
	}
	
	
	//snippet to get android hash key
			/*PackageInfo info;
			try {
			    info = getPackageManager().getPackageInfo("meetme.android.app", PackageManager.GET_SIGNATURES);
			    for (Signature signature : info.signatures) {
			        MessageDigest md;
			        md = MessageDigest.getInstance("SHA");
			        md.update(signature.toByteArray());
			        String something = new String(Base64.encode(md.digest(), 0));
			        //String something = new String(Base64.encodeBytes(md.digest()));
			        Log.e("hash key", something);
			    }
			} catch (NameNotFoundException e1) {
			    Log.e("name not found", e1.toString());
			} catch (NoSuchAlgorithmException e) {
			    Log.e("no such an algorithm", e.toString());
			} catch (Exception e) {
			    Log.e("exception", e.toString());
			}*/

}
