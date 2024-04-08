package com.example.cw1;



public class Weather {
    private String condition;
    private String temperature;
    private String time;
    private String date;
    private String locationName; // Added property for location name

    private String condition1;
    private String condition2; // Corrected to have separate variables
    private String condition3; // Corrected to have separate variables

    private ThreeDayForecast threeDayForecast;

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

    public String getConditionDay1() { return condition1; }

    public void setConditionDay1(String condition1) { this.condition1 = condition1; }

    public String getConditionDay2() { return condition2; } // Use condition2

    public void setConditionDay2(String conditionDay2) { this.condition2 = conditionDay2; } // Correctly assign to condition2

    public String getConditionDay3() { return condition3; } // Use condition3

    public void setConditionDay3(String conditionDay3) { this.condition3 = conditionDay3; } // Correctly assign to condition3

    public ThreeDayForecast getThreeDayForecast() {
        return threeDayForecast;
    }

    public void setThreeDayForecast(ThreeDayForecast threeDayForecast) {
        this.threeDayForecast = threeDayForecast;
    }
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    // Method to check if all data is complete
    public boolean isDataComplete() {
        return condition != null && temperature != null && time != null && date != null
                && condition1 != null && condition2 != null && condition3!= null;
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
