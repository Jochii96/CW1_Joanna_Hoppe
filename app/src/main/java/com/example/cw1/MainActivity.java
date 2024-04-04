package com.example.cw1;

import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView rawDataDisplay;
    private Button startButton;
    private Map<String, List<Weather>> weatherReportsByLocation = new HashMap<>();

    private Map<String, String> locationIdToNameMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationIdToNameMap.put("2648579", "Glasgow");
        locationIdToNameMap.put("2643743", "London");
        locationIdToNameMap.put("5128581", "New York");
        locationIdToNameMap.put("287286", "Oman");
        locationIdToNameMap.put("934154", "Mauritius");
        locationIdToNameMap.put("1185241", "Bangladesh");

        rawDataDisplay = findViewById(R.id.rawDataDisplay);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        weatherReportsByLocation.clear(); // Clear the list for fresh data on each click
        rawDataDisplay.setText(""); // Clear the display
        String[] locationIds = {"2648579", "2643743", "5128581", "287286", "934154", "1185241"};
        for (String id : locationIds) {
            startProgress(id);
        }
    }

    public void startProgress(String locationId) {
        String locationName = locationIdToNameMap.get(locationId); // Get the location name using the ID

        if (locationName == null) {
            Log.e("startProgress", "Location name for ID " + locationId + " not found.");
            return; // Optionally handle this case more gracefully
        }
        String url = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationId;
        new Thread(new Task(url, locationId, locationName)).start(); // Pass both the URL and location ID
    }

    private class Task implements Runnable {
        private String url;
        private String locationId;
        private String locationName;

        public Task(String url, String locationId, String locationName) {
            this.url = url;
            this.locationId = locationId;
            this.locationName = locationName;

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
                Log.e("MyTag", "IOException", e);
            }


            String locationName = locationIdToNameMap.get(locationId);
            parseXML(result.toString());
        }

        private void parseXML(String xml) {
            List<Weather> localList = new ArrayList<>();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(xml));
                int eventType = xpp.getEventType();
                Weather currentWeather = null;
                boolean insideItem = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && "item".equalsIgnoreCase(xpp.getName())) {
                        insideItem = true;
                        currentWeather = new Weather(locationId, locationName);
                    } else if (insideItem && "title".equalsIgnoreCase(xpp.getName())) {
                        String titleText = xpp.nextText();
                        String condition = titleText.split(",")[0].split(":")[1].trim();
                        currentWeather.setCondition(condition);
                    }
                    if (eventType == XmlPullParser.END_TAG && "item".equalsIgnoreCase(xpp.getName()) && currentWeather != null) {
                        localList.add(currentWeather);
                        currentWeather = null;
                        insideItem = false;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                Log.e("MyTag", "Parsing error", e);
            }

            Log.d("MyTag", "End of document reached");

            synchronized (this) {
                weatherReportsByLocation.putIfAbsent(locationName, new ArrayList<>());
                weatherReportsByLocation.get(locationName).addAll(localList);
            }

            MainActivity.this.runOnUiThread(() -> updateUI());
        }
    }

    private void updateUI() {
        StringBuilder displayText = new StringBuilder();
        for (Map.Entry<String, List<Weather>> entry : weatherReportsByLocation.entrySet()) {
            String locationName = entry.getKey();
            List<Weather> reports = entry.getValue();
            displayText.append(locationName).append(":\n");
            for (Weather report : reports) {
                displayText.append(report.getCondition()).append("\n");
            }
            displayText.append("\n"); // Extra newline for spacing between locations
        }
        rawDataDisplay.setText(displayText.toString());
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
