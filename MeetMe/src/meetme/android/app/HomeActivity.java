package meetme.android.app;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
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
import com.facebook.model.GraphUser;

public class HomeActivity extends FragmentActivity {

	private LogInFragment logInFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main_menu);

		Intent intent = new Intent(this, MeetMeCacheService.class);
		startService(intent);
		
		Session session = Session.getActiveSession();
		if(session != null && session.isOpened())
		{
			Log.i("HomeActivity", "logged in, redirecting to MainMenuActivity...");
			openMainMenu();
					}
		else
		{

			//if (savedInstanceState == null) {
		        // Add the fragment on initial activity setup
		        logInFragment = new LogInFragment();
		        logInFragment.setLoggedInListener(new LogInFragment.LoggedInListener() {
					@Override
					public void call() {	
						Log.i("HomeActivity", "logged in, redirecting to MainMenuActivity...");
						openMainMenu();	
					}	        
		        });
		        
		        getSupportFragmentManager()
		        .beginTransaction()
		        .add(android.R.id.content, logInFragment)
		        .commit();
		    /*} else {
		        // Or set the fragment from restored state info
		        logInFragment = (LogInFragment) getSupportFragmentManager()
		        .findFragmentById(android.R.id.content);
		    }*/
		}
		
	}
	
	private void openMainMenu()
	{
		Intent intent = new Intent(HomeActivity.this, MainMenuActivity.class);
		startActivity(intent);
	}


}
