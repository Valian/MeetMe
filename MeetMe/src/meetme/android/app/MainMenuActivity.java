package meetme.android.app;

import meetme.android.app.MeetMeCacheService.MeetMeCacheBinder;
import meetme.android.app.MeetMeCacheService.StatusReceivedListener;
import meetme.android.app.MeetMeCacheService.StatusResult;
import meetme.android.app.adapters.PersonViewModel;
import meetme.android.core.ImageLoader;
import Service.CancelStatusTask;
import Service.User;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class MainMenuActivity extends ActionBarActivity {

	private Button statusSetButton;
	private Button statusCancelButton;
	
	public static final String REFRESH_KEY = "refresh_on_load";
	private boolean loadingDisplay = false;
	//private Button friendListButton;

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
	    intent.putExtra(REFRESH_KEY, true);
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

    	StatusResult result = cacheService.getLastStatus();
    	
    	Bundle extras = getIntent().getExtras();
    	getIntent().removeExtra(REFRESH_KEY);
    	boolean refresh = false;
    	if (extras != null)  {
    		refresh = extras.getBoolean(REFRESH_KEY);
    		Log.i("MainMenuActivity", "refresh parameter received: " + String.valueOf(refresh));
    	}
    	
    	if(result.statusSet && !refresh) 
    	{
    		Log.i("MainMenuActivity", "using cached status");
    		if(result.user != null)
    		{
    			Log.i("MainMenuActivity", "status: comment: "+result.user.getComment() + 
						", from: "+ result.user.getFrom().toString() +
						", to: " + result.user.getTo().toString());
    		}
    		updateDisplay(result.user);
    	}
    	else
    	{
    		loadingDisplay = true;
    		
    		getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, splashScreen)
	        .commit();
    	}
    	
    	cacheService.getStatus(new StatusReceivedListener() {

			@Override
			public void call(User user) {
				

				if(user != null)
				{
					Log.i("status", 
						"received status: comment: "+user.getComment() + 
						", from: "+ user.getFrom().toString() +
						", to: " + user.getTo().toString());
				}
				
				updateDisplay(user);
		    	
				if(loadingDisplay){
					loadingDisplay = false;
					
					getSupportFragmentManager()
			        .beginTransaction()
			        .remove(splashScreen)
			        .commit();
				}
			}
    		
    	});
    }
    
    private void updateDisplay(User user)
    {
    	if(user != null)
    	{	
    		statusSetButton.setVisibility(View.INVISIBLE);
    		statusCancelButton.setVisibility(View.VISIBLE);    		

    		showUserInfo(user);
    	}
    	else
    	{
    		statusSetButton.setVisibility(View.VISIBLE);
    		statusCancelButton.setVisibility(View.INVISIBLE);
    		findViewById(R.id.status).setVisibility(View.INVISIBLE);
    	}
    }
    
	private void showUserInfo(final User user) {
		final TextView name = (TextView)findViewById(R.id.name);	
		final TextView availability = (TextView)findViewById(R.id.availabilityInfo);	
		final TextView comment = (TextView)findViewById(R.id.comment);	
		final ImageView thumbnail = (ImageView)findViewById(R.id.thumbnail);	
		
		if(name == null || availability == null || comment == null || thumbnail == null)
			return;
		
		Session fbSession = Session.getActiveSession();
		
		if(fbSession == null) return;
		
		Request request = Request.newMeRequest(fbSession, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser fbUser, Response response) {
				
				if(user != null && fbUser != null)
				{
					PersonViewModel vm = new PersonViewModel(user, fbUser, getString(R.string.available_in),  getString(R.string.available_for), getString(R.string.available_already));
					name.setText(vm.name);
					comment.setText(vm.comment);
					availability.setText(vm.availabilityInfo);					
					
					ImageLoader imageLoader = new ImageLoader(getBaseContext());
					findViewById(R.id.status).setVisibility(View.VISIBLE);
					
					imageLoader.DisplayImage(vm.thumbnailURL, thumbnail);
				}				
			}
		});
		
		request.executeAsync();
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
		
		if(loadingDisplay){
			getSupportFragmentManager()
	        .beginTransaction()
	        .remove(splashScreen)
	        .commit();
		}
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    
	}	
}
