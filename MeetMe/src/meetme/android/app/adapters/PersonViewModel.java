package meetme.android.app.adapters;

public class PersonViewModel {

	public String name;
	public String comment;
	public String availabilityInfo;
	public String thumbnailURL;
	
	public PersonViewModel(String name, String comment, String availabilityInfo, String thumbnailURL)
	{
		this.name = name;
		this.comment = comment;
		this.availabilityInfo = availabilityInfo;
		this.thumbnailURL = thumbnailURL;
	}
}
