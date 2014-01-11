package meetme.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends ActionBarActivity {

	private Button statusChangeButton;
	private Button friendListButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}
	
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
				openFriends();
			}
		});
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

	private void openMap() {
		Intent intent = new Intent(MainMenuActivity.this, LocationViewActivity.class);
		startActivity(intent);
		
	}
	
	private void openFriends() {
		Intent intent = new Intent(MainMenuActivity.this, FriendListActivity.class);
		startActivity(intent);
	}
	

}
