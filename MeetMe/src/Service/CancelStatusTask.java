package Service;

import android.os.AsyncTask;
import android.util.Log;

public class CancelStatusTask extends AsyncTask<String, Void, Boolean> {
    @Override
    //pierwszy parametr to token.
    protected Boolean doInBackground(String... params) {
        try {
        	if(params == null || params[0] == null) throw new RuntimeException("Nie podano tokenu");
        	        	   	
        	MeetMeClient client = new MeetMeClient();        	
        	
        	Boolean b = client.cancelStatus((String)params[0]);             		
        	
            return b;
        }  catch (Exception e) {       
            Log.e("MainActivity", e.getMessage(), e);            
        }		
        
        return null;
    }
/*
    @Override
    protected void onPostExecute(Boolean result) {     
    	Log.i("Cancel Status result", result.toString());
    }
*/
}