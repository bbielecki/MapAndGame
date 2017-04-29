package com.example.bartomiej.mapandgame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Bartłomiej on 28.04.2017.
 */
//TODO: zastanow sie jak zrobic z tego klase nie prywatna, czy da sie wykorzystac ten kod w MarkerHandler

    class GetDirectionHandler {
    private Activity activity;
    private GoogleMap map;
    private Vector<Vector<Polyline>> printedRoutes = new Vector<>();
    private Vector<LatLng> routeLatLng = new Vector<>();
    private List<LatLng> pontos = new ArrayList<>();
    private GetDirection getDirection = new GetDirection();

    GetDirectionHandler(Vector<LatLng> route, GoogleMap map, Activity activity) {
        routeLatLng = route;
        this.map = map;
        this.activity = activity;
    }

    public void clearRouteLatLng() {
        routeLatLng.clear();
    }

    public void createRoute(){
        new GetDirection().execute();
    }

    public void setDestination(String destLat, String destLng){
        getDirection.setDestination(destLat,destLng);
    }

    public void setOrigin(String originLat, String originLng){
        getDirection.setOrigin(originLat, originLng);
    }

    private class GetDirection extends AsyncTask<String, String, String> {

        private ProgressDialog dialog;
        private String origin = "Chicago,IL";
        private String destination = "Los Angeles,CA";

        public void setDestination(String destinationLat, String destinationLng) {
            destination = destinationLat + "," + destinationLng;
        }

        public void setOrigin(String originLat, String originLng) {
            origin = originLat + "," + originLng;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Drawing the route, please wait!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();

            destination = routeLatLng.lastElement().latitude + "," + routeLatLng.lastElement().longitude;
            origin = routeLatLng.get(routeLatLng.size() - 2).latitude + "," + routeLatLng.get(routeLatLng.size() - 2).longitude;
        }

        protected String doInBackground(String... args) {
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&sensor=false&mode=walking";
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

            } catch (Exception e) {
                dialog.setMessage("siec..");
                dialog.show();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            Vector<Polyline> parts = new Vector<>();
            for (int i = 0; i < pontos.size() - 1; i++) {
                LatLng src = pontos.get(i);
                LatLng dest = pontos.get(i + 1);
                try {
                    //here is where it will draw the polyline in your map
                    Polyline line = map.addPolyline(new PolylineOptions().add(new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude, dest.longitude)).width(2).color(Color.RED).geodesic(true));
                    parts.add(line);
                } catch (NullPointerException e) {
                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
                }

                if (i == pontos.size() - 2) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(pontos.get(i)));
                }
            }
            printedRoutes.add(parts);
            dialog.dismiss();
        }


        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
}