package com.example.cw1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.cw1.Weather;
import java.util.List;
import java.util.Map;


public class LocationAdapter extends FragmentStateAdapter {
    private final List<String> locationNames;
    private final Map<String, Weather> weatherData;

    public LocationAdapter(FragmentActivity fragmentActivity, List<String> locationNames, Map<String, Weather> weatherData) {
        super(fragmentActivity);
        this.locationNames = locationNames;
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String locationName = locationNames.get(position);
        Weather weather = weatherData.getOrDefault(locationName, new Weather());
        return LocationFragment.newInstance(locationName, weather.getCondition(), weather.getTemperature(), weather.getTime(), weather.getDate());
    }

    @Override
    public int getItemCount() {
        return locationNames.size();
    }
}