package com.example.destinationstracker;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final ListView listDestinations = (ListView) findViewById(R.id.list_destinations);
        final ArrayList<String> destinationNames = new ArrayList<String>();



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //default tab option on load
        tabLayout.getTabAt(0).select();
        List startupListNames = fill_array(0,1);
        List startupListIDs = fill_array(0,0);

        final ArrayAdapter<String> startAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,startupListNames);
        listDestinations.setAdapter(startAdapter);
        initialize_item_onclick(listDestinations,startupListIDs);



        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                final List<String> destinationNames;
                final List<String> destinationIDs;

                if(tab.getPosition() == 0){
                    destinationNames = fill_array(0,1);
                    destinationIDs = fill_array(0,0);
                }else if (tab.getPosition() == 1) {
                    destinationNames = fill_array(1,1);
                    destinationIDs = fill_array(1,0);

                }else if(tab.getPosition() == 2){
                    destinationNames = fill_array(2,1);
                    destinationIDs = fill_array(2,0);

                }else{
                    destinationNames = fill_array(0,1);
                    destinationIDs = fill_array(0,0);

                }
                final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,destinationNames);
                listDestinations.setAdapter(listAdapter);

                initialize_item_onclick(listDestinations, destinationIDs);

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
   }

    public void initialize_item_onclick(ListView listDestinations, final List destinationIDs){
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("DESTINATION_ID", (int) destinationIDs.get(position));
                    startActivity(intent);
            }
        };
        listDestinations.setOnItemClickListener(itemClickListener);
    }

    //returntype is ids or names
    public List fill_array(int destination_type,int return_type){

        SQLiteOpenHelper DestinationDBHelper = new DatabaseHelper(this);
        List destinationNames = new ArrayList<>();
        List destinationIDs = new ArrayList<>();

        try{
            db = DestinationDBHelper.getReadableDatabase();

            cursor = db.query("Destinations",new String[]{"_id", "destination_name"},
                    "destination_type = '"+destination_type+"'",null,null,null,null);
            while(cursor.moveToNext()) {
                String destinationName = cursor.getString(cursor.getColumnIndexOrThrow("destination_name"));
                Integer destination_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                destinationNames.add(destinationName);
                destinationIDs.add(destination_id);

            }

        }catch(SQLiteException e) {

        }
        if (return_type == 0) {  //id
            return destinationIDs;
        }else{   //names
            return destinationNames;
        }
    }

    public void newDestination(View view){
        Intent intent = new Intent(this,CreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this,MapsActivity.class);
            intent.putExtra("ORIGIN","main");
            startActivity(intent);
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //setContentView(R.verticalLayout);
        }
    }



//    @Override

//    public void onDestroy(){
//        super.onDestroy();
//        cursor.close();
//        db.close();
//    }

}
