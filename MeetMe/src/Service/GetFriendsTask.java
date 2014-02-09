package Service;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class GetFriendsTask extends AsyncTask<String, Void, ArrayList<User>> {
    @Override
  //parametr to token.
    protected ArrayList<User> doInBackground(String... params) {
        try {
        	if(params == null || params[0] == null) 
        		throw new RuntimeException("Nie podano tokenu w GetFriendsTask");
        	        	   	
        	MeetMeClient client = new MeetMeClient();        	
        	
        	ArrayList<User> users = client.getFriends(params[0]);             		
        	
            return users;
        }  catch (Exception e) {       
            Log.e("MainActivity", e.getMessage(), e);            
        }		
        
        return null;
    }
/*
    @Override
    protected void onPostExecute(ArrayList<User> obj) {     
    	if(obj != null) Log.d("Get Friends result", obj.toString());
    }
*/
}