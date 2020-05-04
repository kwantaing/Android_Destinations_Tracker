package com.example.destinationstracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private SQLiteDatabase db;

    private Cursor cursor;

    public String origin;
    public Destination detailDestination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        origin = getIntent().getStringExtra("ORIGIN");
        if (origin.equals("detail")) {
            detailDestination = (Destination) getIntent().getSerializableExtra("DESTINATION");
        }else{
            Toast.makeText(this,"Blue: Dream, Green: Planned, Yellow: Explored",Toast.LENGTH_LONG).show();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (origin.equals("detail")) {
            mMap = googleMap;

            // Add a marker in selected Destination
            LatLng point = new LatLng(detailDestination.latitude, detailDestination.longitude);
            mMap.addMarker(new MarkerOptions().position(point)
                    .title("Marker in " + detailDestination.destinationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        } else {
            mMap = googleMap;

            //plots marker in all destinations color coded by destination type
            pin_all_markers(googleMap);
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && origin.equals("main")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && origin.equals("detail")) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("DESTINATION_ID", detailDestination.destinationID);
            startActivity(intent);
        }
    }

    public void pin_all_markers(GoogleMap googleMap) {
        SQLiteOpenHelper DestinationDBHelper;
        DestinationDBHelper = new DatabaseHelper(this);
        try {
            db = DestinationDBHelper.getReadableDatabase();

            //dream = 0
            cursor = db.query("Destinations", new String[]{"_id", "destination_name","latitude","longitude","destination_type"},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                String destinationName = cursor.getString(cursor.getColumnIndexOrThrow("destination_name"));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndexOrThrow("longitude"));
                int destination_type = cursor.getInt(cursor.getColumnIndexOrThrow("destination_type"));
                pin_marker(googleMap,destinationName,latitude,longitude,destination_type);
            }

        } catch (SQLiteException e) {

        }
    }

    public void pin_marker(GoogleMap googleMap, String destinationName, String latitude, String longitude, int destination_type) {
        float marker_color = BitmapDescriptorFactory.HUE_BLUE;
        switch (destination_type) {
            case 0:
                marker_color = BitmapDescriptorFactory.HUE_BLUE;
                break;
            case 1:
                marker_color = BitmapDescriptorFactory.HUE_GREEN;
                break;
            case 2:
                marker_color = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            default:
                break;
        }
        LatLng point = new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));

        googleMap.addMarker(new MarkerOptions()
                .position(point)
                .title(destinationName)
                .icon(BitmapDescriptorFactory.defaultMarker(marker_color)));


            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));


    }
}

