package com.example.destinationstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DestinationsDB";

    private static final int DB_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void addDestination(SQLiteDatabase db, String dname, double longitude, double latitude, String description, int destinationType, String start_date, String end_date){

        ContentValues destinationValues = new ContentValues();

        destinationValues.put("destination_name",dname);
        destinationValues.put("longitude",longitude);
        destinationValues.put("latitude",latitude);
        destinationValues.put("destination_type",destinationType);
        destinationValues.put("description",description);
        destinationValues.put("start_date",start_date);
        destinationValues.put("end_date",end_date);

        db.insert("Destinations", null, destinationValues);

    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) {

            db.execSQL("CREATE TABLE Destinations (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "destination_name TEXT, "
                    + "description TEXT, "
                    + "longitude REAL, "
                    + "latitude REAL, "
                    + "start_date TEXT, "
                    + "end_date TEXT, "
                    + "destination_type INTEGER);");

            addDestination(db,"Paris",2.3522,48.8566,"it would be a lot of fun",0,"2020-05-20","2020-06-24");
            addDestination(db,"Tokyo",139.7690,35.6804,"its was a lot of fun",2,"2020-05-20","2020-06-24");
            addDestination(db,"London",-0.118092,51.509865,"its gonna be a lot of fun",1,"2020-05-20","2020-06-24");
            addDestination(db,"Frankfurt",8.682127,50.110924,"its was a lot of fun",2,"2020-05-20","2020-06-24");

        }else{

        }

    }
}
