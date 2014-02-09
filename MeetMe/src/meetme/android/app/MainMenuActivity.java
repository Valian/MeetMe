package meetme.android.app;

import Service.CancelStatusTask;
import Service.GetStatusTask;
import Service.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.Session;

public class MainMenuActivity extends ActionBarActivity {

	private Button statusSetButton;
	private Button statusCancelButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);
		
		statusSetButton = (Button) findViewById(R.id.statusSetButton);	
		statusSetButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainMenuActivity.this, StatusChangeActivity.class);
				//Intent intent = new Intent(MainMenuActivity.this, LocationViewActivity.class);
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
		
	}
	
	public void reloadActivity() {

	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();

	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Session session = Session.getActiveSession();
		if(session == null || !session.isOpened())
		{
			backToHome();
			return;
		}
		
		
		final SplashScreenFragment splashScreen = new SplashScreenFragment();
		 getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, splashScreen)
	        .commit();

		new GetStatusTask() {
			@Override
		    protected void onPostExecute(User result) {     
		    			    	
		    	//if status set
		    	if(result != null)
		    	{
		    		Log.i("status", 
		    				"received status: comment: "+result.getComment() + 
		    				", from: "+ result.getFrom().toString() +
		    				", to: " + result.getTo().toString());
		    		
		    		statusSetButton.setVisibility(View.INVISIBLE);
		    		statusCancelButton.setVisibility(View.VISIBLE);
		    		
		    		//TODO wyswietlic status

		    	}
		    	else
		    	{
		    		statusSetButton.setVisibility(View.VISIBLE);
		    		statusCancelButton.setVisibility(View.INVISIBLE);
		    	}
		    	
		    	getSupportFragmentManager()
		        .beginTransaction()
		        .remove(splashScreen)
		        .commit();
		    }
		}.execute(session.getAccessToken());
	}
	
	private void backToHome()
	{
		Intent intent = new Intent(MainMenuActivity.this, HomeActivity.class);
		startActivity(intent);
	}
	
	public void Init()
	{
		//todo - pobrac status i ustalic styl buttona
		//
		//
		
		

		
		/*
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {
		
		
		  // callback when session changes state
		  @Override
		  public void call(final Session session, SessionState state, Exception exception) {
			  if (session.isOpened()) 
			  {	 
			        Request.newMeRequest(session, new Request.GraphUserCallback() 
			        {
			        	
				          @Override 
				          public void onCompleted(GraphUser user, Response response) 
				          {
				        	 
				        	  
						     //if (user != null)
						     if (false)
						     {
						    	  TextView label = (TextView) findViewById(R.id.user_name); 
					        	  label.setText(user.getName());
						         
						         User tempUser = new User();
						         tempUser.setComment("haha gowno dziaua");
						         tempUser.setLatitude(152.0);
						         tempUser.setLongitude(123.0);
						         tempUser.setFrom(new Date(2013, 12, 11));
						         tempUser.setTo(new Date(2013, 12, 11));
						         
						         new GetStatusTask(){
						        	 @Override 
						        	 protected void onPostExecute(User result) {
						        		 Log.i("Get Status id", result.getFacebookId().toString());
						        	 }
						         }.execute(session.getAccessToken());
						         
						         new GetFriendsTask(){
						        	 @Override 
						        	 protected void onPostExecute(ArrayList<User> result) {
						        		 for(User user : result)
						        		 {
						        			 Log.i("userinfo:", user.getFacebookId().toString());
						        		 }
						        			 
						        	 }
						         }.execute(session.getAccessToken());
						         
						         
						      }
						  }
			        }).executeAsync();		  			
			  } 
		   }		  
		});*/
		
		  
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
	

}
