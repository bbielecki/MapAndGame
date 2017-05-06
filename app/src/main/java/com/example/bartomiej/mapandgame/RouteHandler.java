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
    private Vector<Vector<LatLng>> routes;
    private GetDirectionHandler getDirectionHandler;
    private Activity mapAndGame;
    private int selectedRouteNumber;


    RouteHandler(GoogleMap map, Activity mapAndGame){
        routes = new Vector<>();
        this.mapAndGame = mapAndGame;
        this.map = map;
        getDirectionHandler = new GetDirectionHandler(map, mapAndGame);
    }

    public void selectRoute(int routeNumber){
        if((routeNumber >= 0) && (routeNumber < routes.size())){
            selectedRouteNumber = routeNumber;
            getDirectionHandler.setSelectedRouteNumber(selectedRouteNumber);
        }
    }
    // before add any point, route has to be initilized
    public void createRoute(){
        routes.add(new Vector<LatLng>());
        selectedRouteNumber = routes.size()-1;
        getDirectionHandler.setSelectedRouteNumber(selectedRouteNumber);
    }

    public void addPointToRoute(LatLng point){
        routes.get(selectedRouteNumber).add(point);
    }

//    public void deleteRoute(int routeNumber){
//        routes.remove(routeNumber);
//        getDirectionHandler.removeRoute(routeNumber);
//        getDirectionHandler.clearRouteLatLng();
//    }

    public void deleteAllRoute(int routeNumber){
        routes.remove(routeNumber);
        getDirectionHandler.removeAllRoute(routeNumber);
        getDirectionHandler.clearRouteLatLng();
    }

    public void drawRoute(Vector<LatLng> route){
        getDirectionHandler.setRouteLatLng(route);
        getDirectionHandler.createRoute();
    }

    public void drawRouteThroughPoints(){
        if(routes.size()>0) {
            getDirectionHandler.setRouteLatLng(routes.get(selectedRouteNumber));
            getDirectionHandler.createRoute();
            //usuwanie route?
        }
    }

    public void getDistanceBetween(LatLng A, LatLng B){

    }

    public void getDistance(int routeNumber){

    }
}

