package Service;

import android.os.AsyncTask;
import android.util.Log;

public class GetStatusTask extends AsyncTask<String, Void, User> {
    @Override
    //pierwszy parametr to token.
    protected User doInBackground(String... params) {
        try {
        	if(params == null || params[0] == null) 
        		throw new RuntimeException("Nie podano tokenu");
        	        	   	
        	MeetMeClient client = new MeetMeClient();        	
        	
        	User user = client.getStatus((String)params[0]);             		
        	
            return user;
        }  catch (Exception e) {       
            Log.e("MainActivity", e.getMessage(), e);            
        }		
        
        return null;
    }
/*
    @Override
    protected void onPostExecute(User result) {     
    	Log.i("Get Status comment", result.getComment().toString());
    }
*/
}