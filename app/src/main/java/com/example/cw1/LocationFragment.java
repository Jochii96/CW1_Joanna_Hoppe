package com.example.cw1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

public class LocationFragment extends Fragment {
    private static final String ARG_LOCATION_NAME = "locationName";
    private static final String ARG_CONDITION = "condition";
    private static final String ARG_TEMPERATURE = "temperature";
    private static final String ARG_TIME = "time";
    private static final String ARG_DATE = "date";
    // Additional arguments for day forecasts
    private static final String ARG_CONDITION_DAY1 = "conditionDay1";
    private static final String ARG_CONDITION_DAY2 = "conditionDay2";
    private static final String ARG_CONDITION_DAY3 = "conditionDay3";

    public static LocationFragment newInstance(String locationName, String condition, String temperature, String time, String date, String conditionDay1, String conditionDay2, String conditionDay3) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_NAME, locationName);
        args.putString(ARG_CONDITION, condition);
        args.putString(ARG_TEMPERATURE, temperature);
        args.putString(ARG_TIME, time);
        args.putString(ARG_DATE, date);
        args.putString(ARG_CONDITION_DAY1, conditionDay1);
        args.putString(ARG_CONDITION_DAY2, conditionDay2);
        args.putString(ARG_CONDITION_DAY3, conditionDay3);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        TextView locationTextView = view.findViewById(R.id.locationName);
        TextView dateTextView = view.findViewById(R.id.dateObs);
        TextView timeTextView = view.findViewById(R.id.timeObs);
        TextView dayTextView = view.findViewById(R.id.dayObs);
        TextView temperatureTextView = view.findViewById(R.id.tempObs);
        TextView con1TextView = view.findViewById(R.id.con1);
        TextView con2TextView = view.findViewById(R.id.con2);
        TextView con3TextView = view.findViewById(R.id.con3);

        Bundle args = getArguments();
        if (args != null) {
            String locationName = args.getString(ARG_LOCATION_NAME, "N/A");
            String condition = args.getString(ARG_CONDITION, "N/A");
            String temperature = args.getString(ARG_TEMPERATURE, "N/A");
            String time = args.getString(ARG_TIME, "N/A");
            String date = args.getString(ARG_DATE, "N/A");
            String conditionDay1 = args.getString(ARG_CONDITION_DAY1, "N/A");
            String conditionDay2 = args.getString(ARG_CONDITION_DAY2, "N/A");
            String conditionDay3 = args.getString(ARG_CONDITION_DAY3, "N/A");

            // Log the values
            Log.d("LocationFragment", "Location Name: " + locationName);
            Log.d("LocationFragment", "Condition: " + condition);
            Log.d("LocationFragment", "Temperature: " + temperature);
            Log.d("LocationFragment", "Time: " + time);
            Log.d("LocationFragment", "Date: " + date);
            Log.d("LocationFragment", "Condition Day 1: " + conditionDay1);
            Log.d("LocationFragment", "Condition Day 2: " + conditionDay2);
            Log.d("LocationFragment", "Condition Day 3: " + conditionDay3);

            locationTextView.setText(locationName);
            dateTextView.setText(date);
            timeTextView.setText(time);
            dayTextView.setText(condition);
            temperatureTextView.setText(temperature);
            con1TextView.setText(conditionDay1);
            con2TextView.setText(conditionDay2);
            con3TextView.setText(conditionDay3);
        } else {
            Log.d("LocationFragment", "Arguments are null");
        }
        return view;
    }
}