package meetme.android.core.dialogs;

import meetme.android.core.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;


@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener 
{
	private Context context;
	private TimePickerDialog.OnTimeSetListener timeSetListener;
	private int hour, minute;
	
	public TimePickerFragment(Context context, TimePickerDialog.OnTimeSetListener timeSetListener, int hour, int minute)
	{
		super();
		this.context = context;
		this.timeSetListener = timeSetListener;
		this.hour = hour;
		this.minute = minute;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), dialog);
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), dialog);
		return dialog;
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
		timeSetListener.onTimeSet(view, hourOfDay, minute);
	}
}