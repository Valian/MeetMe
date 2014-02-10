package meetme.android.app.adapters;

import com.google.android.gms.maps.model.LatLng;

public class PersonMapMarker {

	public String name;
	public LatLng location;
	
	public PersonMapMarker(String name, LatLng location) {
		this.name = name;
		this.location = location;
	}
}
