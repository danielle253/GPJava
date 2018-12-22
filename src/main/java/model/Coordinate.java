package model;

import com.google.maps.model.LatLng;

public class Coordinate {
	
	private double latitude;
	private double longitude;
	private String time;
	
	private Coordinate() {}
	
	public Coordinate(double latitude, double longitude, String time) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Coordinate(LatLng latlng) {
		this.latitude = latlng.lat;
		this.longitude = latlng.lng;
	}
	
	public LatLng toLatLng() {
		return new LatLng(latitude, longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
