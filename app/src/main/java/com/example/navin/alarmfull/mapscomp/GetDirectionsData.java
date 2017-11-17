package com.example.navin.alarmfull.mapscomp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.navin.alarmfull.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by navin on 11/15/2017.
 */

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    public static String durationToDest="";
    public static String distanceToDest="";
    LatLng latLng;

    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;

    }

    @Override
    protected void onPostExecute(String s) {

        HashMap<String, String> directionsList = null;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        durationToDest = directionsList.get("duration");
        distanceToDest = directionsList.get("distance");

//        mMap.clear();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.draggable(true);
//        markerOptions.title("Duration = "+MainActivity.durationToDest);
//        markerOptions.snippet("Distance = "+MainActivity.distanceToDest);

        Log.e("Duration value:"," CLASS:GetDirectionsData"+durationToDest);
        Log.e("Distance value:"," CLASS:GetDirectionsData"+distanceToDest);

//        mMap.addMarker(markerOptions);

    }
}
