package meetme.android.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.internal.SessionTracker;
import com.facebook.model.GraphUser;

@SuppressLint("NewApi")
public class MainMenuActivity extends ActionBarActivity {

	private Button statusChangeButton;
	private Button friendListButton;
	
	private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		
	}
	
	

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	@Override
	protected void onStart() {
		super.onStart();
		//todo - pobrac status i ustalic styl buttona
		//status najlepiej trzymac gdzies w danych na telefonie, nie na serwerze.
		//
		//
		
		statusChangeButton = (Button) findViewById(R.id.statusChangeButton);	
		statusChangeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainMenuActivity.this, StatusChangeActivity.class);
				startActivity(intent);
			}
		});
		
		friendListButton = (Button) findViewById(R.id.friendListButton);	
		friendListButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				openFriendList();
			}
		});
		
		
		
		
		
		
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {
	
		  // callback when session changes state
		  @Override
		  public void call(Session session, SessionState state, Exception exception) {
			  if (session.isOpened()) 
			  {	 
			        Request.newMeRequest(session, new Request.GraphUserCallback() 
			        {
			          
				          @Override
				          public void onCompleted(GraphUser user, Response response) 
				          {
						      if (user != null)
						      {
						         TextView label = (TextView) findViewById(R.id.user_name); 
						         label.setText(user.getName());
						      }
						  }
			        }).executeAsync();		
			  }	 
			  
		   }		  
		});
		
		
		Session.StatusCallback callback = new Session.StatusCallback() {
			
			  // callback when session changes state
			  @Override
			  public void call(Session session, SessionState state, Exception exception) {
				  /*if (session.isOpened()) 
				  {	 
					  Log.i("facebook", "opened");
				        Request.newMeRequest(session, new Request.GraphUserCallback() 
				        {
				          
					          @Override
					          public void onCompleted(GraphUser user, Response response) 
					          {
							      if (user != null)
							      {
							         TextView label = (TextView) findViewById(R.id.user_name); 
							         label.setText(user.getName());
							      }
							  }
				        }).executeAsync();		
				  }	 */
				  
			   }	
		};
		
		
		/***********/
		
		/*Session session = new Builder(this).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI true) {
            Session.setActiveSession(session);
            
            
            session.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
            
            OpenRequest openRequest = new OpenRequest(this).setCallback(callback);
            session.O
            session.openForRead(openRequest);
        }*/
		
		
		/**************/
		
		/*Session session = Session.getActiveSession();
		boolean isReadAllowed = checkPermissions(Arrays.asList(PERMISSIONS), session.getPermissions());
		if (!session.isOpened() && !session.isClosed()) {
		    session.openForRead(new Session.OpenRequest(this).setPermissions(PERMISSIONS).setCallback( callback	));
		} else {
		    if (isReadAllowed) {
		        Session.openActiveSession(this, true, callback);
		    } else {                                    
		        session.requestNewReadPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS).setCallback(callback));
		    }
		}*/
		
		  
		/*//snippet to get android hash key
		PackageInfo info;
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

	
	public static boolean checkPermissions(List<String> needed, List<String> available) {
	    boolean ret = true;
	    for (String s : needed) {
	        if (!available.contains(s)) {
	            ret = false;
	            break;
	        }
	    }
	    return ret;
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
	            openFriendList();
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
	
	private void openFriendList() {
		Intent intent = new Intent(MainMenuActivity.this, FriendListActivity.class);
		startActivity(intent);
	}
	

}
