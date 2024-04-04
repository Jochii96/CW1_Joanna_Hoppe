package com.example.cw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    private Map<String, String> weatherDataAsString = new HashMap<>();
    private Map<String, String> locationIdToNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateLocationIdToNameMap();
        viewPager = findViewById(R.id.viewPager);
        fetchWeatherForAllLocations();
    }

    private void populateLocationIdToNameMap() {
        locationIdToNameMap.put("2648579", "Glasgow");
        locationIdToNameMap.put("2643743", "London");
        locationIdToNameMap.put("5128581", "New York");
        locationIdToNameMap.put("287286", "Oman");
        locationIdToNameMap.put("934154", "Mauritius");
        locationIdToNameMap.put("1185241", "Bangladesh");
    }

    private void fetchWeatherForAllLocations() {
        for (String locationId : locationIdToNameMap.keySet()) {
            startProgress(locationId);
        }
    }

    private void startProgress(String locationId) {
        String url = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationId;
        new Thread(new Task(url, locationId)).start();
    }

    private class Task implements Runnable {
        private final String url;
        private final String locationId;

        Task(String url, String locationId) {
            this.url = url;
            this.locationId = locationId;
        }

        @Override
        public void run() {
            StringBuilder result = new StringBuilder();
            try {
                URL aurl = new URL(url);
                URLConnection yc = aurl.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }
                in.close();
            } catch (IOException e) {
                Log.e("MyTag", "IOException in reading XML", e);
            }
            parseXML(result.toString(), locationId);
        }

        private void parseXML(String xml, String locationId) {
            StringBuilder weatherDetailsBuilder = new StringBuilder();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(xml));
                int eventType = xpp.getEventType();
                boolean insideItem = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("item".equals(xpp.getName())) {
                            insideItem = true;
                        } else if (insideItem && "title".equals(xpp.getName())) {
                            // Assuming the title tag contains weather condition
                            String titleText = xpp.nextText();
                            // Extract and format the weather condition from the title
                            {
                                String condition = titleText.split(",")[0].split(":")[1].trim();
                                weatherDetailsBuilder.append("Condition: ").append(condition).append("\n");
                            }
                        }
                        // Extend with additional parsing rules as needed
                    } else if (eventType == XmlPullParser.END_TAG && "item".equals(xpp.getName())) {
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                Log.e("MyTag", "Parsing error", e);
            }

            final String weatherDetails = weatherDetailsBuilder.toString();
            runOnUiThread(() -> updateWeatherData(locationId, weatherDetails));
        }

    }

    private void updateWeatherData(String locationId, String weatherDetails) {
        String locationName = locationIdToNameMap.get(locationId);
        if (locationName != null) {
            weatherDataAsString.put(locationName, weatherDetails);
        }

        // Check if all weather data is fetched and parsed
        if (weatherDataAsString.size() == locationIdToNameMap.size()) {
            // Ensure this operation runs on the UI thread
            runOnUiThread(() -> {
                List<String> locationNames = new ArrayList<>(locationIdToNameMap.values());
                Location_adapter adapter = new Location_adapter(this, locationNames, weatherDataAsString);
                viewPager.setAdapter(adapter);
            });
        }
    }


    public static class Weather {
        private String condition;
        private String locationId;
        private String locationName;

        public void setCondition(String condition) {
            this.condition = condition;
        }
        public Weather(String locationId, String locationName) {
            this.locationId = locationId;
            this.locationName = locationName;
        }


        public String getCondition() {
            return condition;
        }
        public String getLocationName() {
            return locationName;
        }
    }
}
