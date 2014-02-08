package Service;

import java.util.Date;

public class User
{	
	private Long facebookId;
	private Boolean active;
	private String comment;
	private Double latitude;
	private Double longitude;
	private Date from;
	private Date to;
	
	public Long getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(Long facebookId) {
		this.facebookId = facebookId;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	
	
	
}
