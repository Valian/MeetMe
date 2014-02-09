package meetme.android.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.facebook.Session;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;

import meetme.android.core.GooglePlayConnector;
import meetme.android.core.dialogs.TimePickerButton;
import Service.UpdateStatusTask;
import Service.User;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


public class StatusChangeActivity extends ActionBarActivity {
	
	private TimePickerButton startTime;
	private TimePickerButton howLong;
	
	private Date selectedDay;
	
	private Map<String, Date> datePickerValueMap;
	
	final int defaultHowLongHours = 0;
	final int defaultHowLongMinutes = 15;
	
	private GooglePlayConnector googlePlayConnector;
	Boolean saving = false;
	Location location;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_change);
		//setContentView(R.layout.activity_main_menu);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Button saveStatusButton = (Button) findViewById(R.id.saveStatus);
		saveStatusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
				saving = true;
				
				
				if(location != null)
					updateStatus();
				else
					Log.i("location", "location not updated when user clicked");
			
			}
		});
		
		
		Calendar calendar = Calendar.getInstance(); 
		
		//*** time button initialisation ***//
		
		datePickerValueMap = new HashMap<String, Date>();
		
		startTime = new TimePickerButton(this, getSupportFragmentManager(), (Button)findViewById(R.id.timeFromButton),
				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		
		howLong = new TimePickerButton(this, getSupportFragmentManager(), (Button)findViewById(R.id.howLongButton),
				defaultHowLongHours, defaultHowLongMinutes);
		
		/*** setting date spinner values ***/
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		
		
		Date date = calendar.getTime();
		String todayDate = "Today ("+ sdf.format(date) + ")";
		datePickerValueMap.put(todayDate, date);
		selectedDay = date;
		
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		date = calendar.getTime();
		String tomorrowDate = "Tomorrow (" + sdf.format(date)+ ")";
		datePickerValueMap.put(tomorrowDate, date);
		
		String[] datePickerValues = {todayDate, tomorrowDate};
		
		
		ArrayAdapter<CharSequence> adapter =
				new ArrayAdapter(this, android.R.layout.simple_spinner_item, datePickerValues);	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner dateSpinner = (Spinner)findViewById(R.id.dateSpinner);
		dateSpinner.setAdapter(adapter);	
		dateSpinner.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				Log.i("selected", dateSpinner.getItemAtPosition(pos).toString());
				selectedDay = datePickerValueMap.get(dateSpinner.getItemAtPosition(pos).toString());
				
				for(String key : datePickerValueMap.keySet())
				{
					Log.i("key", key);
				}
				
				for(Date date : datePickerValueMap.values())
				{
					Log.i("date", date.toString());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
			
		});
		
		
		
		googlePlayConnector = new GooglePlayConnector(this);
		googlePlayConnector.setConnectionCallbacks(new ConnectionCallbacks(){

			@Override
			public void onConnected(Bundle connectionHint) {
				location = googlePlayConnector.getLastLocation();
				Log.i("location", "got location: "+location.toString());
				if(saving)
				{
					updateStatus();
				}
				else
					Log.i("location", "not saving when received location: "+location.toString());
			}

			@Override
			public void onDisconnected() {
				Toast.makeText(StatusChangeActivity.this, "Failed to access your location!", Toast.LENGTH_SHORT).show();
			}
		
		});
		
	}
	

	@Override
	protected void onStart()
	{
		super.onStart();
		
		final Session session = Session.getActiveSession();
		if(session == null || !session.isOpened())
		{
			backToHome();
			return;
		}
		
		try{
			googlePlayConnector.connect();
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Google service unavailable!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void updateStatus()
	{
		
		User user = new User();
		String comment = ((EditText) findViewById(R.id.meeting_comment)).getText().toString();
		user.setComment(comment);
		
		Calendar calendar = Calendar.getInstance();
		
		if(selectedDay == null)
			Log.i("dzien", "nuuuuuul");
		else
			Log.i("dzien", selectedDay.toString());
		calendar.setTime(selectedDay);
		calendar.add(Calendar.HOUR_OF_DAY, startTime.GetHours());
		calendar.add(Calendar.MINUTE, startTime.GetMinutes());
		user.setFrom(calendar.getTime());
		
		calendar.add(Calendar.HOUR_OF_DAY, howLong.GetHours());
		calendar.add(Calendar.MINUTE, howLong.GetMinutes());
		user.setTo(calendar.getTime());
		
		user.setLatitude(location.getLatitude());
		user.setLongitude(location.getLongitude());
		
		new UpdateStatusTask() {
			@Override
		    protected void onPostExecute(Boolean result) {     
		    	Log.i("Update Status result", result.toString());
		    	
		    	Intent intent = new Intent(StatusChangeActivity.this, MainMenuActivity.class);
				startActivity(intent);
		    }
		}.execute(Session.getActiveSession().getAccessToken(), user);
	}
	
	/*private void SetSelectedTime(int hour, int minute)
	{
		selectedHour = hour;
		selectedMinute = minute;
		Button timeButton = (Button)findViewById(R.id.timeButton);
		timeButton.setText(Integer.toString(selectedHour) + ":" + Integer.toString(selectedMinute));
	}*/
	
	private void backToHome()
	{
		Intent intent = new Intent(StatusChangeActivity.this, HomeActivity.class);
		startActivity(intent);
	}

	public void meetingListButtonClicked(View view)
	{
		//todo tymczasowo 
		Toast toast = Toast.makeText(this, "Meeting list", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/*private class TimePickerListener implements TimePickerDialog.OnTimeSetListener 	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			SetSelectedTime(hourOfDay, minute);
		}	
	}*/
	
	
	/*public void timeButtonClicked(View view)
	{
		//TimePickerFragment newFragment = new TimePickerFragment(this, new TimePickerListener());
		
	    newFragment.show(getSupportFragmentManager(), "timePicker");

	}*/

}
