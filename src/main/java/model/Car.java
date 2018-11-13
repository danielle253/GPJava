package model;

import com.google.maps.model.LatLng;

public class Car extends Entity{
	private float latitude;
	private float longitude;
	private LatLng position;
	private String serial;
	
	private Car() {}
	
	public Car(LatLng position, String serial) {
		this.position = position;
		this.serial = serial;
	}

	public LatLng getPosition() {
		return position;
	}

	public void setPosition(LatLng position) {
		this.position = position;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

}
