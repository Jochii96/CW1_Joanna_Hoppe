package com.example.cw1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;
import java.util.Map;


public class LocationAdapter extends FragmentStateAdapter {
    private final List<String> locationNames;
    private final Map<String, Weather> weatherData;

    private final Map<String, ThreeDayForecast> forecastData;



    public LocationAdapter(FragmentActivity fragmentActivity, List<String> locationNames, Map<String, Weather> weatherData, Map<String, ThreeDayForecast> forecastData) {
        super(fragmentActivity);
        this.locationNames = locationNames;
        this.weatherData = weatherData;
        this.forecastData =forecastData;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String locationName = locationNames.get(position);
        Weather weather = weatherData.getOrDefault(locationName, new Weather());
        ThreeDayForecast forecast = forecastData.getOrDefault(locationName, new ThreeDayForecast());
        return LocationFragment.newInstance(locationName, weather.getCondition(), weather.getTemperature(), weather.getTime(), weather.getDate(), forecast.getConditionDay1(),forecast.getConditionDay2(), forecast.getConditionDay3());
    }

    @Override
    public int getItemCount() {
        return locationNames.size();
    }
}