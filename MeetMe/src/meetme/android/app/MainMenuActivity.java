package meetme.android.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_menu);
		
		Calendar calendar = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
		
		Date date = calendar.getTime();
		String todayDate = sdf.format(date);
		
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		date = calendar.getTime();
		String tomorrowDate = sdf.format(date);
		
		/*** setting date spinner values ***/
		Spinner dateSpinner = (Spinner)findViewById(R.id.dateSpinner);
		
		String[] datePickerValues = {"Today ("+todayDate+")", "Tomorrow ("+tomorrowDate+")"};
		
		ArrayAdapter<CharSequence> adapter = 
				new ArrayAdapter(this, android.R.layout.simple_spinner_item, datePickerValues);	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dateSpinner.setAdapter(adapter);		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public void MeetingListButtonClicked(View view)
	{
		CharSequence text = "Meeting list";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

}
