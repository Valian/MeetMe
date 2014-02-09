package meetme.android.app.adapters;
 
import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import meetme.android.app.FriendListActivity;
import meetme.android.app.R;
import meetme.android.core.ImageLoader;
 
public class FriendLazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<PersonViewModel> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
 
    public FriendLazyAdapter(Activity a, ArrayList<PersonViewModel> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
    	if(position >= data.size() || position < 0) return null;
        return data.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.friend_list_row, null);
 
        TextView name = (TextView)vi.findViewById(R.id.name); // title
        TextView comment = (TextView)vi.findViewById(R.id.comment); // artist name
        TextView availabilityInfo = (TextView)vi.findViewById(R.id.availabilityInfo); // duration
        ImageView thumbnail =(ImageView)vi.findViewById(R.id.thumbnail); // thumb image
 
        PersonViewModel person = data.get(position);
 
        // Setting all values in listview
        name.setText(person.name);
        comment.setText(person.comment);
        availabilityInfo.setText(person.availabilityInfo);
        imageLoader.DisplayImage(person.thumbnailURL, thumbnail);
        
        return vi;
    }
}

