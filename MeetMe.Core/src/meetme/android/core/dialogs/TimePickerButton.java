package meetme.android.core.dialogs;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;


@SuppressLint("ValidFragment")
public class TimePickerButton implements TimePickerDialog.OnTimeSetListener 
{
	public Context context;
	private int selectedHour, selectedMinute;
	private Button timeButton;
	FragmentManager fragmentManager;
	
	public TimePickerButton(Context context, FragmentManager fragmentManager, Button button, int hour, int minute)
	{
		super();
		this.context = context;
		this.fragmentManager = fragmentManager;
		timeButton = button;
		selectedHour = hour;
		selectedMinute = minute;
		
		SetSelectedTime(selectedHour, selectedMinute);	
		timeButton.setOnClickListener(new ClickListener());

	}
	
	private TimePickerButton outer()
	{
		return this;
	}
	
	private class ClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View view) {
			TimePickerFragment newFragment = new TimePickerFragment(context, outer(), selectedHour, selectedMinute);
    	    newFragment.show(fragmentManager, "timePicker");	
		}		
	}
	
	public int GetHours()
	{
		return selectedHour;
	}
	
	public int GetMinutes()
	{
		return selectedMinute;
	}
	
	private void SetSelectedTime(int hour, int minute)
	{
		selectedHour = hour;
		selectedMinute = minute;
		
		String hourString = Integer.toString(selectedHour);
		if(hour <= 9) hourString = "0" + hourString;
		String minuteString = Integer.toString(selectedMinute);
		if(minute <= 9) minuteString = "0" + minuteString;
		
		timeButton.setText(hourString + ":" + minuteString);
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
		SetSelectedTime(hourOfDay, minute);
	}
}