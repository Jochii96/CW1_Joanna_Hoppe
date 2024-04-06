package com.example.cw1;

public class Weather {
    private String condition;
    private String temperature;
    private String time;
    private String date;
    private String locationName; // Added property for location name

    // Constructor
    public Weather() {
    }

    // Getters and Setters for each property
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    // Optional: Override toString() for easy debugging/printing
    @Override
    public String toString() {
        return "Weather{" +
                "condition='" + condition + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
