package model;

import java.util.List;

import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

public class Booking extends Entity{

    private Coordinate source;
    private Coordinate destination;
    
    private List<Coordinate> path;
    
    private Distance distance;
    private Duration duration;
    
    private String sourceAddress;
    private String destinationAddress;
    
    private String carID;
	private String userID;
	
	private String stage;
	private String state;
	
	private boolean notificationSent;
	
	private long hold;
	
	private Booking() {}
	
	public Booking(Coordinate source, Coordinate destination, String userID) {
        this.source = source;
        this.destination = destination;
        this.userID = userID;
    }

    public List<Coordinate> getPath() {
		return path;
	}

	public void setPath(List<Coordinate> path) {
		this.path = path;
	}

	public Distance getDistance() {
		return distance;
	}
    
	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

    public Coordinate getSource() {
		return source;
	}

	public void setSource(Coordinate source) {
		this.source = source;
	}

	public Coordinate getDestination() {
		return destination;
	}

	public void setDestination(Coordinate destination) {
		this.destination = destination;
	}

	public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public long getHold() {
		return hold;
	}

	public void setHold(long hold) {
		this.hold = hold;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public String getState() {
		return state;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
}