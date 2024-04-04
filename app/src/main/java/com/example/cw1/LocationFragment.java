package com.example.cw1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LocationFragment extends Fragment {
    private static final String ARG_LOCATION_NAME = "locationName";
    private static final String ARG_WEATHER_CONDITION = "weatherCondition"; // New argument for weather condition

    public static LocationFragment newInstance(String locationName, String weatherCondition) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_NAME, locationName);
        args.putString(ARG_WEATHER_CONDITION, weatherCondition); // Pass weather condition
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        TextView locationTextView = view.findViewById(R.id.locationName); // Make sure this ID is correct in your layout
        TextView weatherInfoTextView = view.findViewById(R.id.weatherInfo); // You'll need to add this TextView to your layout

        Bundle args = getArguments();
        if (args != null) {
            String locationName = args.getString(ARG_LOCATION_NAME);
            String weatherCondition = args.getString(ARG_WEATHER_CONDITION); // Retrieve weather condition

            locationTextView.setText(locationName);
            weatherInfoTextView.setText(weatherCondition); // Display weather condition
        }
        return view;
    }
}