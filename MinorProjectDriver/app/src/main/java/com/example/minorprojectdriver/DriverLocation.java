package com.example.minorprojectdriver;

public class DriverLocation {
    private double latitude;
    private double longitude;

    public DriverLocation() {
        // Default constructor required for Firebase Realtime Database
    }

    public DriverLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
