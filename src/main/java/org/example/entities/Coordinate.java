package org.example.entities;

public class Coordinate {
    private final Float latitude;
    private final Float longitude;
    private String locationName;

    public Coordinate(Float latitude, Float longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
    public String getLocationName() {
        return locationName;
    }

    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }
}

