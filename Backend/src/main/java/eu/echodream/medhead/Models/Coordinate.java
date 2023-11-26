package eu.echodream.medhead.Models;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private final double lat;
    private final double lng;

    public Coordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }
}