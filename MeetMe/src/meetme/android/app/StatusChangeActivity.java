package meetme.android.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import meetme.android.core.dialogs.TimePickerButton;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class StatusChangeActivity extends ActionBarActivity {
	
	private TimePickerButton startTime;
	private TimePickerButton howLong;
	
	final int defaultHowLongHours = 0;
	final int defaultHowLongMinutes = 15;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_status_change);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Calendar calendar = Calendar.getInstance(); 
		
		/*** time button initialisation ***/

		startTime = new TimePickerButton(this, getSupportFragmentManager(), (Button)findViewById(R.id.timeFromButton),
				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		
		howLong = new TimePickerButton(this, getSupportFragmentManager(), (Button)findViewById(R.id.howLongButton),
				defaultHowLongHours, defaultHowLongMinutes);
		
		/*** setting date spinner values ***/
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		
		Date date = calendar.getTime();
		String todayDate = sdf.format(date);
		
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		date = calendar.getTime();
		String tomorrowDate = sdf.format(date);
		
		
		Spinner dateSpinner = (Spinner)findViewById(R.id.dateSpinner);
		
		String[] datePickerValues = {"Today ("+todayDate+")", "Tomorrow ("+tomorrowDate+")"};
		
		ArrayAdapter<CharSequence> adapter =
				new ArrayAdapter(this, android.R.layout.simple_spinner_item, datePickerValues);	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dateSpinner.setAdapter(adapter);				
	}

	/*private void SetSelectedTime(int hour, int minute)
	{
		selectedHour = hour;
		selectedMinute = minute;
		Button timeButton = (Button)findViewById(R.id.timeButton);
		timeButton.setText(Integer.toString(selectedHour) + ":" + Integer.toString(selectedMinute));
	}*/
	

	public void meetingListButtonClicked(View view)
	{
		//todo tymczasowo 
		CharSequence text = "Meeting list";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
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