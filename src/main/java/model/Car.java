package model;

public class Car {
	private float longitude;
	private float latitude;
	private String serial;
	
	private Car() {}
	
	public Car(long longitude, long latitude, String serial) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.serial = serial;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
}
