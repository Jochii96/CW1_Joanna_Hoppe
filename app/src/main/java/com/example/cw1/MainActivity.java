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


    private Map<String, String> locationIdToNameMap = new HashMap<>();
    private Map<String, Weather> weatherData = new HashMap<>();

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
        String url = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + locationId;
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
            final Weather weather = new Weather();
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
                            // Extracting weather condition from the title
                            String titleText = xpp.nextText();
                            String condition = titleText.split(" - ")[0].trim();
                            weather.setCondition(condition);

                            String[] parts = titleText.split(" - ")[1].split(":", 3);
                            String time = parts[0] + ":" + parts[1].trim().split(" ")[0];
                            weather.setTime(time);

                            String[] temperaturePart = titleText.split("Â°C")[0].split(",");  // Isolate the part ending with the temperature
                            String temperature = temperaturePart[temperaturePart.length - 1].split(" ")[1].trim(); // Extract "16"
                            weather.setTemperature(temperature);

                        } else if (insideItem && "pubDate".equals(xpp.getName())) {
                            String pubDateFullText = xpp.nextText();
                            // Extracting publication date from the pubDate tag
                            String[] dateParts = pubDateFullText.split(" ");
                            String date = dateParts[1] + " " + dateParts[2] + " " + dateParts[3];
                            weather.setDate(date);
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

            final String locationName = locationIdToNameMap.get(locationId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateWeatherData(locationName, weather);
                }
            });
        }

        private void updateWeatherData(String locationName, Weather weather) {
            if (locationName != null && weather != null) {
                weatherData.put(locationName, weather);
            }

            if (weatherData.size() == locationIdToNameMap.size()) {
                updateViewPager();
            }
        }



        // Check if all weather data is fetched and parsed

    }
    private void updateViewPager() {
        List<String> locationNames = new ArrayList<>(locationIdToNameMap.values());
        LocationAdapter adapter = new LocationAdapter(this, locationNames, weatherData);
        viewPager.setAdapter(adapter);
    }



    }
