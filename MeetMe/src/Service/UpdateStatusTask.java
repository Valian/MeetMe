package Service;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateStatusTask extends AsyncTask<Object, Void, Boolean> {
    @Override
    //pierwszy parametr to token, drugi User.
    protected Boolean doInBackground(Object... params) {
        try {
        	if(params == null || params[0] == null || params[1] == null) 
        		throw new RuntimeException("Nie podano tokenu albo usera w UpdateStatusTask");
        	        	   	
        	MeetMeClient client = new MeetMeClient();        	
        	
        	Boolean b = client.updateStatus((String)params[0], (User)params[1]);             		
        	
            return b;
        }  catch (Exception e) {       
            Log.e("MainActivity", e.getMessage(), e);            
        }		
        
        return null;
    }
/*
    @Override
    protected void onPostExecute(Boolean result) {     
    	Log.i("Update Status result", result.toString());
    }
*/
}