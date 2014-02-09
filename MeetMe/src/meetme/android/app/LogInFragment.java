package meetme.android.app;

import java.util.Arrays;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class LogInFragment extends Fragment{
	
	private static final String TAG = "LogInFragment";
	
	private LoggedInListener loggedInListener;
	public void setLoggedInListener(LoggedInListener callback)
	{
		loggedInListener = callback;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_facebook, container, false);

	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	   /* authButton.setSessionStatusCallback(new Session.StatusCallback(){

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if(state.isOpened())
				{
					Log.i("call", String.valueOf(loggedInListener != null));
					
					
				}
				
			}
	    	
	    });*/
	    //authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
	    //authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));

	    
	    
	    return view;
	}
	
	private static final List<String> PERMISSIONS = Arrays.asList("email", "user_groups");

	public static boolean checkPermissions(List<String> needed, List<String> available) {
	    boolean ret = true;
	    for (String s : needed) {
	        if (!available.contains(s)) {
	            ret = false;
	            break;
	        }
	    }
	    return ret;
	}
	
	
	
	
	
	
	
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	    
	    if(loggedInListener != null) loggedInListener.call();
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	
	
	@Override
	public void onResume() {
	    super.onResume();

	 // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	public abstract static class LoggedInListener
	{
		public abstract void call();
	}
	
}
