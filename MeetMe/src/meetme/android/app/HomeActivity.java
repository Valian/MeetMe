package meetme.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.Session;

public class HomeActivity extends FragmentActivity {

	private LogInFragment logInFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main_menu);

		Session session = Session.getActiveSession();
		if(session != null && session.isOpened())
		{
			Log.i("else", "else");
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
						Log.i("call", "call");
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
