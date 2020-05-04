package com.example.destinationstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==1 || tab.getPosition() == 2){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    DateCreateFragment fragment = new DateCreateFragment();
                    ft.add(R.id.placeholder,fragment,"DATECREATE");
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("DATECREATE");
                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void verifyInputs(View view){
        //get inputs
        EditText destination_input = findViewById(R.id.destination_input);
        String destinationName = destination_input.getText().toString();

        EditText longitude_input = findViewById(R.id.longitude_input);
        double longitude = Double.parseDouble(longitude_input.getText().toString());

        EditText latitude_input = findViewById(R.id.latitude_input);
        double latitude = Double.parseDouble(latitude_input.getText().toString());

        EditText description_input = findViewById(R.id.description_input);
        String description = description_input.getText().toString();


        //get tab selection
        TabLayout tabs = findViewById(R.id.tabs);
        int tab_position = tabs.getSelectedTabPosition();
        int destinationType = tab_position;

        if(destinationType == 1 || destinationType == 2){
            TextView start_date_tv = findViewById(R.id.start_date_tv);
            String start_date_input = start_date_tv.getText().toString();
            start_date_input = convertDatetoText(convertTexttoDate(start_date_input));

            TextView end_date_tv = findViewById(R.id.end_date_tv);
            String end_date_input = end_date_tv.getText().toString();
            end_date_input = convertDatetoText(convertTexttoDate(end_date_input));


            addtoDB(destinationName,longitude,latitude,description,destinationType,start_date_input,end_date_input);
        }else{
            addtoDB(destinationName,longitude,latitude,description,destinationType);
        }
        addDestination(view);


    }

    public void addDestination(View view) {
        Toast.makeText(this,"TEST",Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this,MainActivity.class);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void addtoDB(String dname, double longitude, double latitude, String description, int destinationType){
        SQLiteOpenHelper DestinationDBHelper = new DatabaseHelper(this);
        try{
            db = DestinationDBHelper.getWritableDatabase();

            ContentValues destinationValues = new ContentValues();

            destinationValues.put("destination_name",dname);
            destinationValues.put("longitude",longitude);
            destinationValues.put("latitude",latitude);
            destinationValues.put("destination_type",destinationType);
            destinationValues.put("description",description);


            db.insert("Destinations", null, destinationValues);
        }catch(SQLiteException e){

        }
    }
    public void addtoDB(String dname, double longitude, double latitude, String description, int destinationType, String start_date, String end_date){
        SQLiteOpenHelper DestinationDBHelper = new DatabaseHelper(this);
        try{
            db = DestinationDBHelper.getWritableDatabase();

            ContentValues destinationValues = new ContentValues();

            destinationValues.put("destination_name",dname);
            destinationValues.put("longitude",longitude);
            destinationValues.put("latitude",latitude);
            destinationValues.put("destination_type",destinationType);
            destinationValues.put("description",description);
            destinationValues.put("start_date",start_date);
            destinationValues.put("end_date",end_date);


            db.insert("Destinations", null, destinationValues);
        }catch(SQLiteException e){

        }
    }

    public String convertDatetoText(Date dateObject){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("date to text",sdf.format(dateObject));
        return sdf.format(dateObject);
    }
    public Date convertTexttoDate(String datetext){
        Date convertedDate = new Date();
        Log.d("before conversion",datetext);

        String pattern = "MMM d,yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try{
            convertedDate =  sdf.parse(datetext);
        }catch(ParseException e){

        }

        return convertedDate;
    }
}
