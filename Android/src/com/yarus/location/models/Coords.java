package com.yarus.location.models;

public class Coords {
	private double lattitude;
	private double longitude;
	
	public Coords(double lattitude, double longitude){
		this.lattitude = lattitude;
		this.longitude = longitude;
	}
	
	public double getLattitude(){
		return lattitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
