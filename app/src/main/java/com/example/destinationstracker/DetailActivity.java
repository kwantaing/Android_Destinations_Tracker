package com.example.destinationstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destinationstracker.R;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {


    private SQLiteDatabase db;

    private Cursor cursor;
    int destinationID;
    Destination currentDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState!= null){
            destinationID = (Integer) savedInstanceState.getInt("DESTINATION_ID");

        }else{
            destinationID = (Integer) getIntent().getExtras().getInt("DESTINATION_ID");

        }
        populate_view(getDestination(destinationID));
    }

    public Destination getDestination(int id) {
        SQLiteOpenHelper DestinationDBHelper = new DatabaseHelper(this);
        List destinationNames = new ArrayList<>();
        List destinationIDs = new ArrayList<>();

        try {
            db = DestinationDBHelper.getReadableDatabase();

            cursor = db.query("Destinations", new String[]{"_id", "destination_name", "description", "longitude", "latitude", "start_date", "end_date", "destination_type"},
                    "_id = "+id, null, null, null, null);
            while(cursor.moveToNext()) {
                int destinationID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String destinationName = cursor.getString(cursor.getColumnIndexOrThrow("destination_name"));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                String travelstartDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String travelendDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                int destinationType = cursor.getInt(cursor.getColumnIndexOrThrow("destination_type"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                currentDestination = new Destination(destinationID, destinationName, longitude, latitude, travelstartDate, travelendDate, destinationType, description);
            }

        } catch (SQLiteException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();


        }
        return currentDestination;
    }
    public void populate_view(Destination destination){
        TextView destinationTV = (TextView) findViewById(R.id.destination_tv);
        destinationTV.setText(destination.destinationName);
        TextView descriptionTV = (TextView) findViewById(R.id.description_tv);
        descriptionTV.setText(destination.description);
        TextView latitudeTV = (TextView) findViewById(R.id.latitude_tv);
        latitudeTV.setText(Double.toString(destination.latitude));
        TextView longitudeTV = (TextView) findViewById(R.id.longitude_tv);
        longitudeTV.setText(Double.toString(destination.longitude));
        TabLayout tablayout = findViewById(R.id.tabs);
        tablayout.getTabAt(destination.destinationType).select();
        if(destination.destinationType == 1 || destination.destinationType == 2){
            addFragmentasNeeded();
        }


    }

    public String convertDatetoText(Date dateObject){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(dateObject);
    }
    public Date convertTexttoDate(String datetext){
        Date convertedDate = new Date();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try{
            convertedDate =  sdf.parse(datetext);
        }catch(ParseException e){

        }
        return convertedDate;
    }
    public void removeDestination(View view){
        SQLiteOpenHelper DestinationDBHelper = new DatabaseHelper(this);

        db = DestinationDBHelper.getWritableDatabase();
        db.delete("Destinations","_id = "+currentDestination.destinationID,null);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    public void return_to_tab(View view){
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.getTabAt(currentDestination.destinationType).select();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this,MapsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("DESTINATION", currentDestination);
            intent.putExtras(bundle);

            intent.putExtra("ORIGIN", "detail");
            startActivity(intent);
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //setContentView(R.verticalLayout);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("DESTINATION_ID",destinationID);
    }
    @Override

    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void addFragmentasNeeded() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        String start_date = convertDatetoText(currentDestination.travelstartDate);
        String end_date = convertDatetoText(currentDestination.travelendDate);
        DateFragment fragment = new DateFragment(start_date,end_date);
        ft.replace(R.id.placeholder,fragment);
        ft.commit();


    }


}