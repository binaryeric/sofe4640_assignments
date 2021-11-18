package com.example.assignment2.utils;

public class LocationWrapper {

    private int ID;
    private double latitude;
    private double longitude;
    private String address;

    public LocationWrapper(int ID, String address, double latitude, double longitude) {
        this.ID = ID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public int getID() {
        return ID;
    }

}
