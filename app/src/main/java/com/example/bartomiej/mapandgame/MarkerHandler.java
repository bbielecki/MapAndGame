package com.example.bartomiej.mapandgame;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

/**
 * Created by Bartłomiej on 28.04.2017.
 */
/*
class resposible for handling markers on map. Creates groups of markers which belong to one route
and handling a display process of task lists connected with particular marker.
 */
public class MarkerHandler {
    private GoogleMap myMap;

    private Vector<Marker> freeMarkers;
    private Marker locationMarker;


    MarkerHandler(GoogleMap map){

        freeMarkers = new Vector<>();
        myMap = map;

    }

    public void removeLocationMarker(){
        locationMarker.remove();
    }

    public void removeFreeMarkers(){
        for (Marker m : freeMarkers)
            m.remove();
    }

    // draw point of user location on map
    public void drawLocationMarkerOnMap(LatLng loc){
        //if any location marker was created before, it should be deleted
        if(locationMarker != null){
            locationMarker.remove();
        }
        if (loc != null) {
            locationMarker=myMap.addMarker(new MarkerOptions().position(loc).title("tu jestem!"));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 19));
        }
    }
    // draw marker on map
    public Marker drawMarkerOnMap(LatLng point, boolean routeMode) {
        if (point != null) {
            //if route mode is on, created markers should be treated as another checkpoints for created route
            if(routeMode) {
                return myMap.addMarker(new MarkerOptions().position(point));
                // myMap.moveCamera(CameraUpdateFactory.zoomBy(15));
            }
            //else they are simple markers which means in fact nothing
            else {
                Marker freeMarker = myMap.addMarker((new MarkerOptions().position(point)));
                freeMarkers.add(freeMarker);
                return freeMarker;
            }
        }
        return null;
    }

    public void drawRoute(){

    }
}
