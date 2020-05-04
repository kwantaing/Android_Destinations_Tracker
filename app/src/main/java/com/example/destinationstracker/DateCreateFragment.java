package com.example.destinationstracker;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCreateFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener start_date_SetListener;
    private DatePickerDialog.OnDateSetListener end_date_SetListener;
    String start_date_text;
    String start_date;
    String end_date_text;
    String end_date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.travel_date_fragment,container,false);


        final TextView start_date_tv = v.findViewById(R.id.start_date_tv);
        start_date_tv.setText("Click to set");
        start_date_tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, start_date_SetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        start_date_SetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month+1;
                    Log.d("date",year+"-"+ month +"-"+day);
                    start_date_text = year+"-"+ month +"-"+day;
                    start_date = convertDatetoText(convertTexttoDate(start_date_text));
                    start_date_tv.setText(start_date);

            }
        };


        final TextView end_date_tv = v.findViewById(R.id.end_date_tv);
        end_date_tv.setText("Click to set");
        end_date_tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth, end_date_SetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        end_date_SetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                Log.d("date",year+"-"+ month +"-"+day);
                end_date_text = year+"-"+ month +"-"+day;
                end_date = convertDatetoText(convertTexttoDate(end_date_text));
                end_date_tv.setText(end_date);

            }
        };


        return v;

    }

    public String convertDatetoText(Date dateObject){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(dateObject);
    }
    public Date convertTexttoDate(String datetext){
        Date convertedDate = new Date();
        String pattern = "yyyy-M-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try{
            convertedDate =  sdf.parse(datetext);
        }catch(ParseException e){

        }
        return convertedDate;
    }
}
