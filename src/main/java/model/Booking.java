package model;

import java.util.ArrayList;

import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;

public class Booking extends Entity{

    private LatLng source;
    private LatLng destination;
    
    private Distance distance;
    private Duration duration;
    
    private String carID;
	private String userID;
	
	private boolean notificationSent;

	private Booking() {}

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

	public Booking(LatLng source, LatLng destination, String userID) {
        this.source = source;
        this.destination = destination;
        this.userID = userID;
    }

    public LatLng getSource() {
		return source;
	}

	public void setSource(LatLng source) {
		this.source = source;
	}

	public LatLng getDestination() {
		return destination;
	}

	public void setDestination(LatLng destination) {
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
}