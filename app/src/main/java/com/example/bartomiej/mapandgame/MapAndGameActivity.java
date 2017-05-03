package com.example.bartomiej.mapandgame;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;

import java.util.Vector;

import static com.example.bartomiej.mapandgame.R.id.map;


/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and mapAndGame to represent areas.
 */
public class MapAndGameActivity extends AppCompatActivity
        implements
        OnMapReadyCallback {

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final int MY_LOCATION_PERMISSION = 0;
    private static final String ROUTE_MODE_MESSAGE = "Touch on map where you want to \n place start and end point for route";
    private int clicCounter = 0;
    private final int getSpeedPeriod = 500;

    //Line on a map made from LatLngs objects (points)
    private Polyline polyline;
    private Vector<LatLng> routeLatLng;

    //Point on map describing user presence
    private LatLng pointOfPresence;
    //Any point to be printed on map
    private LatLng pointOnMap;
    // reference to googlemap
    private GoogleMap myMap;
    //allow to recognize route mode through points created on map
    private boolean poiontToPointRouteModeOn = true;
    // allow to recognize rote mode from only one point to another
    private boolean shortestRouteModeOn = false;
    //starts speedThread only once on first click
    private boolean speedThreadStarted = false;
    //creating MyLocation object with location and speed fields
    private MyLocation myLocation;
    // handling all markers on map
    private MarkerHandler markerHandler;
    //handling all routes created and drawing them on a map
    private RouteHandler routeHandler;

    // Test GUI buttons
    Button locationButton;
    Button pointToPointRouteButton;
    Button routeModeButton;
    TextView speedText;


    /*

                    INTERFACE

    */


    //interface of GoogleMaps for our application
    //Create poliline from latlngs: objects representing google maps coordinates
    public void createRoute(Vector<LatLng> latLngs) {
        routeLatLng = latLngs;

    }
    //test method//destroy one part of the route from point A to B
    public void deleteRoute(View v){
        routeHandler.deleteRoute();
    }
    //destroy all route from A to B and through all waypoints
    public void deleteAllRoute(View v){
        routeHandler.deleteAllRoute();
    }
    //test method
    public void drawRouteThroughPoints(View v){
        routeHandler.drawRouteThroughPoints();
    }

    public void setPointOnMap(LatLng pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public LatLng getLocation() {
        MyLocation myLocation = new MyLocation(this);
        return myLocation.getLocation();
    }

    public void setLocation(LatLng location) {
        this.pointOfPresence = location;
    }

    // delete marker indicating position of user
    public void deleteLocationPoint() {
        pointOfPresence = null;
    }

    // delete settled marker
    public void deletePointOnMap() {
        pointOnMap = null;
    }
    //quite unusefull method
    public void drawPolilineMode() {
        myMap.setContentDescription(ROUTE_MODE_MESSAGE);
    }
    //enable user to create his own route or
    //disable user ability to creating own route
    public void setRouteMode(View v){
        if(shortestRouteModeOn) {
            shortestRouteModeOn = false;
            routeModeButton.setText("Route Mode OFF");
        }
        else {
            shortestRouteModeOn = true;
            routeModeButton.setText("Route Mode ON");
        }
    }

    public void setRouteModeThroughPoints(View v){
        if(poiontToPointRouteModeOn){
            poiontToPointRouteModeOn = false;
            pointToPointRouteButton.setText("creating route disabled");
        }
        else {
            poiontToPointRouteModeOn = true;
            pointToPointRouteButton.setText("creating route enabled");
        }
    }

    public void showLocationOnMap(View v){
        LatLng loc = myLocation.getLocation();

        if(loc != null) {
            markerHandler.drawLocationMarkerOnMap(loc);
            setLocation(loc);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Cannot get Your position").setTitle("Location Error");
            AlertDialog dialog = builder.create();
        }
    }

    /*

                    PRIVATE METHODS

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map_and_game);
        //myLocationListener = new MyLocationListener(MapAndGameActivity.this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        locationButton = (Button)findViewById(R.id.locationButton);
        routeModeButton = (Button)findViewById(R.id.routeModeButton);
        speedText = (TextView)findViewById(R.id.speedText);
        // Polylines are useful to show a route or some other connection between points.
        Vector<Polyline> polylines = new Vector<>();
        //vector of route points
        routeLatLng = new Vector<>();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and mapAndGame to represent routes and areas on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;
        // Position the map's camera near Warsaw
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.2297700, 21.0117800), 10));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.remove();
                markerHandler.onMarkerClickHandler(marker);
                //TODO: jakas madra obsluga znacznikow-> usuwanie co drugi, przenoszenie znacznika itd

                return false;
            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //allows to create only 2 points before drawing a route between them
                if(shortestRouteModeOn){
                    if(clicCounter==0 ){
                        routeLatLng.add(latLng);
                        //drawMarkerOnMap(latLng);
                        clicCounter++;
                    }else {
                        routeLatLng.add(latLng);
                        //drawMarkerOnMap(latLng);
                        routeHandler.drawRoute(routeLatLng);

                        routeLatLng.clear();
                        clicCounter=0;
                    }
                }else if(poiontToPointRouteModeOn){
                    pointOnMap = latLng;
                    markerHandler.drawMarkerOnMap(latLng, poiontToPointRouteModeOn);
                    routeHandler.addPointToRoute(latLng);
                    routeHandler.drawRouteThroughPoints();
                }
            }
        });
        //thred responsible for getting speed of user every 1 sec
        if(!speedThreadStarted) {
            printSpeed();
            speedThreadStarted = true;
        }

        //handlers with
        markerHandler = new MarkerHandler(myMap);
        routeHandler = new RouteHandler(myMap, MapAndGameActivity.this);
        myLocation = new MyLocation(this);
    }

    private void printSpeed(){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(getSpeedPeriod);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String speedToPrint = myLocation.getSpeed() + "";
                                if(speedToPrint.length()>6)
                                    speedToPrint=speedToPrint.substring(0,5);

                                speedToPrint = speedToPrint + " km/h";
                                speedText.setText(speedToPrint);
                            }
                        });
                    }
                } catch (InterruptedException e) {}
            }
        };

        t.start();
    }


    /**
     * Styles the polyline, based on type.
     *
     * @param polyline The polyline object that needs styling.
     */
    /*
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_pause_light), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    /**
     * Listens for clicks on a polyline.
     *
     * @param polyline The polyline object that the user has clicked.
     */
    /*
    @Override
    public void onPolylineClick(Polyline polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();

        //       Location location = myLocationListener.getLocation();

    }
    */
}