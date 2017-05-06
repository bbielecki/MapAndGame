package com.jacek.databases;

import java.util.ArrayList;

/**
 * Created by Jacexą on 2017-05-06.
 */

public class Point {

    private int id;
    private double latitude;
    private double longitude;
    private int id_route;


    public Point(){

    }

    public Point(double mLatitude, double mLongitude){
        latitude = mLatitude;
        id_route = 1;
        longitude = mLongitude;
    }

    public Point(double mLatitude, double mLongitude, int mId_route){
        latitude = mLatitude;
        longitude = mLongitude;
        id_route = mId_route;
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

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public static ArrayList<Point> makePoints(){
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(1.32131, 512.231));
        points.add(new Point(5.2142, 231.125));
        points.add(new Point(4145.21, 512.216));
        return points;
    }
}
