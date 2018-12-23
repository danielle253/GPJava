package model;

public class Alert extends Entity {
	private String description;
	private String time;
	private boolean displayed;
	//More Fields like User_ref, Car_ref, Booking_ref could be added
	//Just for testing purpose leave it simple
	
	private Alert() {}
	
	public Alert(String description, String time) {
		this.description = description;
		this.time = time;
		displayed = false;
	}

	public String getDescription() {
		return description;
	}

	public String getTime() {
		return time;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
}
