package com.example.cw1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;
import java.util.Map;

public class Location_adapter extends FragmentStateAdapter {
    private final List<String> locationNames;
    private final Map<String, String> weatherDataAsString; // Ensure this is correctly typed

    public Location_adapter(@NonNull FragmentActivity fragmentActivity, List<String> locationNames, Map<String, String> weatherDataAsString) {
        super(fragmentActivity);
        this.locationNames = locationNames;
        this.weatherDataAsString = weatherDataAsString;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String locationName = locationNames.get(position);
        String weatherData = weatherDataAsString.containsKey(locationName) ? weatherDataAsString.get(locationName) : "No data available";
        return LocationFragment.newInstance(locationName, weatherData);
    }

    @Override
    public int getItemCount() {
        return locationNames.size();
    }
}
