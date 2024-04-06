package com.example.cw1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationFragment extends Fragment {
    private static final String ARG_LOCATION_NAME = "locationName";
    private static final String ARG_CONDITION = "condition";
    private static final String ARG_TEMPERATURE = "temperature";
    private static final String ARG_TIME = "time";
    private static final String ARG_DATE = "date";

    public static LocationFragment newInstance(String locationName, String condition, String temperature, String time, String date) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString("locationName", locationName);
        args.putString("condition", condition);
        args.putString("temperature", temperature);
        args.putString("time", time);
        args.putString("date", date);
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
        TextView weatherDetailsTextView = view.findViewById(R.id.weatherDetails);


        Bundle args = getArguments();
        if (args != null) {
            String locationName = args.getString(ARG_LOCATION_NAME);
            String condition = args.getString(ARG_CONDITION);
            String temperature = args.getString(ARG_TEMPERATURE);
            String time = args.getString(ARG_TIME);
            String date = args.getString(ARG_DATE);

            locationTextView.setText(locationName);
            dateTextView.setText(String.format("Date: %s", date));
            timeTextView.setText(String.format("Time: %s", time));
            dayTextView.setText(String.format("Day: %s", condition));
            temperatureTextView.setText(String.format("Temperature: %s°C", temperature));
            weatherDetailsTextView.setText(String.format("Condition: %s\nTemperature: %s\nTime: %s\nDate: %s", condition, temperature, time, date));
        }
        return view;
    }
}