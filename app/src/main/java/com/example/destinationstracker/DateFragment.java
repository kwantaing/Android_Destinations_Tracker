package com.example.destinationstracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DateFragment extends Fragment {

    String start_date;
    String end_date;

    public DateFragment(String start_date, String end_date){
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.travel_date_fragment,container,false);

        TextView start_date_tv = v.findViewById(R.id.start_date_tv);
        TextView end_date_tv = v.findViewById(R.id.end_date_tv);

        start_date_tv.setText(start_date);
        end_date_tv.setText(end_date);

        return v;
    }
}
