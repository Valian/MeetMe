package meetme.android.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import meetme.android.app.adapters.PersonViewModel;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import Service.GetFriendsTask;
import Service.GetStatusTask;
import Service.User;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MeetMeCacheService extends Service{

    private final IBinder meetMeBinder;

    public MeetMeCacheService()
    {
    	meetMeBinder = new MeetMeCacheBinder();
    }   
    
    public class MeetMeCacheBinder extends Binder{
    	MeetMeCacheService getService() {
    		//Log.i("cacheservice", "getService");
    		return MeetMeCacheService.this;
    	}
    	
    }

    @Override
    public IBinder onBind(Intent intent) {
    	//Log.i("cacheservice", "onBind");
    	
        return meetMeBinder;
    }
    
    /**************** Status calls *****************/
    private Boolean statusRequestSent = false;
    private ArrayList<StatusReceivedListener> statusCallbacks = new ArrayList<StatusReceivedListener>();
    private User userStatus = null;
    private boolean userStatusCached = false;
    
    public class StatusResult {
    	User user;
    	boolean statusSet;
    	
    	public StatusResult(User user,	boolean statusSet){
    		this.user = user;
    		this.statusSet = statusSet;
    	}
    }
    
    /** returns true if the status is cached **/
    public StatusResult getLastStatus()    {
    	return new StatusResult(userStatus, userStatusCached);
    }
    
    public void getStatus(StatusReceivedListener callback) {
    	
    	statusCallbacks.add(callback);
    	
    	if(!statusRequestSent)
    	{
    		
    		Log.i("MainMenuActivity", "getStatusCacheServiceDebug");
    		if(Session.getActiveSession() == null || !Session.getActiveSession().isOpened())
    		{
    			Log.i("MeetMeCacheService", "inactive facebook session");
    			return;		
    		}
    		
    		statusRequestSent = true;
	    	String facebookToken = Session.getActiveSession().getAccessToken();
	    	
	    	new GetStatusTask() {
				@Override
			    protected void onPostExecute(User result) {   
					statusRequestSent = false;
					userStatus = result;
					userStatusCached = true;
			    	for(StatusReceivedListener callback : statusCallbacks)
			    	{
			    		if(callback != null) callback.call(result);
			    	}
			    	
			    	statusCallbacks.clear();
			    }
			}.execute(facebookToken);
    	}
    }
    
    public interface StatusReceivedListener
    {
    	public void call(User user);
    }
    
    
    
    /**************** Friend list calls *****************/
    private Boolean friendListRequestSent = false;
    
    private Boolean meetMeUsersReceived = false;
    private Boolean facebookUsersReceived = false;
    
    private ArrayList<FriendListReceivedListener> friendListCallbacks = new ArrayList<FriendListReceivedListener>();
    private ArrayList<User> meetMeUsers;
    private List<GraphUser> facebookUsers;
    
    public ArrayList<User> getLastMeetMeFriendList()    {
    	return meetMeUsers;
    }
    
    public List<GraphUser> getLastFacebookUsers()
    {
    	return facebookUsers;
    }
    
    public void getFriendList(FriendListReceivedListener callback) {
    	
    	friendListCallbacks.add(callback);
    	
    	if(!friendListRequestSent) 
    	{
    		if(Session.getActiveSession() == null || !Session.getActiveSession().isOpened())
    		{
    			Log.i("MeetMeCacheService", "inactive facebook session");
    			return;		
    		}
    		
    		friendListRequestSent = true;
    		meetMeUsersReceived = false;
    		facebookUsersReceived = false;
    		
    		String facebookToken = Session.getActiveSession().getAccessToken();
    		
    		//meetme request
    		new GetFriendsTask() {
    			protected void onPostExecute(ArrayList<User> meetMeUsers) {
    				
    				meetMeUsersReceived = true;
    				MeetMeCacheService.this.meetMeUsers = meetMeUsers;
    				if(facebookUsersReceived) bothListsReceived();
    				
    			}
			}.execute(facebookToken);
    			
			
			
			//facebook request
			
    		Session session = Session.getActiveSession();		
    		Request friendRequest = Request.newMyFriendsRequest(session, 
    	 	            new Request.GraphUserListCallback(){
    	 	                @Override
    	 	                public void onCompleted(List<GraphUser> facebookUsers,
    	 	                        Response response) {
    	 	                    
    	 	                    facebookUsersReceived = true;
    	 	                    MeetMeCacheService.this.facebookUsers = facebookUsers;
    	 	                    if(meetMeUsersReceived) bothListsReceived();    
    	 	                }
    	 	});
    	 	Bundle params = new Bundle();
    	 	params.putString("fields", "id, name, picture");
    	 	friendRequest.setParameters(params);
    	 	friendRequest.executeAsync();			
		}


    }
    
    private void bothListsReceived()
    {
    	friendListRequestSent = false;
    	for(FriendListReceivedListener callback : friendListCallbacks) {
    		if(callback != null)  callback.call(meetMeUsers, facebookUsers);
         }
         friendListCallbacks.clear();
    }

	
    public interface FriendListReceivedListener
    {
    	public void call(ArrayList<User> meetMeUsers, List<GraphUser> facebookUsers);
    }

}

