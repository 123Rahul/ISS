package com.ambeyindustry.iss;

public class IssLocation {
    double lat, lng;

    public IssLocation(String lat, String lng) {
        this.lat = Double.parseDouble(lat);
        this.lng = Double.parseDouble(lng);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
