package com.example.bartomiej.mapandgame;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;

/**
 * Created by Bartłomiej on 29.04.2017.
 */

public class RouteHandler {
    private GoogleMap map;
    private Vector<LatLng> route;
    private GetDirectionHandler getDirectionHandler;
    private Activity mapAndGame;


    RouteHandler(GoogleMap map, Activity mapAndGame){
        route = new Vector<>();
        this.mapAndGame = mapAndGame;
        this.map = map;
        getDirectionHandler = new GetDirectionHandler(map, mapAndGame);
    }

    public void addPointToRoute(LatLng point){
        route.add(point);
    }

    public void deleteRoute(){
        route.clear();
        getDirectionHandler.removeRoute();
        getDirectionHandler.clearRouteLatLng();
    }

    public void deleteAllRoute(){
        route.clear();
        getDirectionHandler.removeAllRoute();
        getDirectionHandler.clearRouteLatLng();
    }

    public void drawRoute(Vector<LatLng> route){
        getDirectionHandler.setRouteLatLng(route);
        getDirectionHandler.createRoute();
        //TODO:usuwanie route?
    }

    public void drawRouteThroughPoints(){
        if(route.size()>1) {
            getDirectionHandler.setRouteLatLng(this.route);
            getDirectionHandler.createRoute();
            //usuwanie route?
        }
    }
}

