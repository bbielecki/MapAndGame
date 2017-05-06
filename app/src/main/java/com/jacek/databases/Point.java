package com.jacek.databases;

import java.util.ArrayList;

/**
 * Created by Jacexą on 2017-05-06.
 */

public class Point {

    private int id;
    private double latitude;
    private double longitude;

    public Point(){

    }

    public Point(double mLatitude, double mLongitude){
        latitude = mLatitude;
        longitude = mLongitude;
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

    public void setLongitude(double longtitude) {
        this.longitude = longtitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ArrayList<Point> makePoints(){
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1.32131, 512.231));
        points.add(new Point(5.2142, 231.125));
        points.add(new Point(4145.21, 512.216));
        return points;
    }
}
