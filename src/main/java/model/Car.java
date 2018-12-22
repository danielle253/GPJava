package model;

import java.util.ArrayList;
import java.util.List;

public class Car extends Entity{
	private List<Coordinate> position;
	private String bookingID;
	private boolean available;
	private boolean suspended;

	public Car() {
		position = new ArrayList<Coordinate>();
		suspended = false;
	}
	
	public Car(String bookingID, boolean available) {
		this();
		this.bookingID = bookingID;
		this.available = available;
	}

	public String getBookingID() {
		return bookingID;
	}

	public boolean isAvailable() {
		return available;
	}

	public List<Coordinate> getPosition() {
		return position;
	}

	public void setPositions(List<Coordinate> position) {
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
	
	public Coordinate getLastPosition() {
		return position.get(position.size() - 1);
	}
}
