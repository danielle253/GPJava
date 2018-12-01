package model;

public class Car extends Entity{
	private Coordinate position;
	private String bookingID;
	private boolean available;
	private boolean suspended;

	public Car() {suspended = false;}
	
	public Car(Coordinate position, String bookingID, boolean available) {
		this.position = position;
		this.bookingID = bookingID;
		this.available = available;
	}
	
	public Coordinate getPosition() {
		return position;
	}

	public String getBookingID() {
		return bookingID;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public void setBookingID(String bookingID) {
		this.bookingID = bookingID;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
}
