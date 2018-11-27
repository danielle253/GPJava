package model;

import com.google.maps.model.LatLng;

public class Coordinate {
	
	private double latitude;
	private double longitude;
	
	private Coordinate() {}
	
	public Coordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
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
