package com.example.destinationstracker;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Destination implements Serializable {
    int destinationID;
    String destinationName;
    double longitude;
    double latitude;
    Date travelstartDate;
    Date travelendDate;
    int destinationType;
    String description;

    public Destination(String dname, double longitude, double latitude, String desc, int destinationType){
        this.destinationName = dname;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = desc;
        this.destinationType = destinationType;
    }

    public Destination(int _id, String dname, double longitude, double latitude, String travelstartDate, String travelendDate, int destinationType, String description){
        this.destinationID = _id;
        this.destinationName = dname;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.destinationType = destinationType;
        if(travelstartDate !=null && travelendDate !=null){
            this.travelstartDate = convertTexttoDate(travelstartDate);
            this.travelendDate = convertTexttoDate(travelendDate);
        }

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

    public String convertDatetoText(Date dateObject){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(dateObject);
    }

}
