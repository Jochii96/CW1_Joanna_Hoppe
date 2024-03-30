package com.example.cw1;

/*  Starter project for Mobile Platform Development in main diet 2023/2024
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 _________________
// Student ID           _________________
// Programme of Study   _________________
//

// UPDATE THE PACKAGE NAME to include your Student Identifier

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {
            ArrayList<String> tempStrings = new ArrayList<>();
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i + 1);
            Log.e("MyTag - cleaned", result);

            //
            // Now that you have the xml data you can parse it
            //

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean foundItem = false; // Flag to track if <item> tag is found

                // Now start parsing
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            foundItem = true; // Set flag when <item> tag is found
                            Log.d("MyTag", "New Thing found!");
                        } else if (foundItem) { // Process data only if <item> tag is found
                            if (xpp.getName().equalsIgnoreCase("link")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                tempStrings.add(temp);
                                // Do something with text
                                Log.d("MyTag", "Bolt is " + temp);
                            } else if (xpp.getName().equalsIgnoreCase("title")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                String rainStatus = temp.substring(temp.indexOf(":") + 2, temp.indexOf(",")).trim();
                                tempStrings.add(rainStatus);

                                // Do something with text
                                Log.d("MyTag", "Nut is " + temp);
                            } else if (xpp.getName().equalsIgnoreCase("link")) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                tempStrings.add(temp);
                                // Do something with text
                                Log.d("MyTag", "Washer is " + temp);
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            Log.d("MyTag", "Thing parsing completed!");
                            foundItem = false; // Reset flag when </item> tag is encountered
                        }
                    }
                    eventType = xpp.next(); // Get the next event before looping again
                }
            } catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.d("MyTag", "End of document reached");





            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String item : tempStrings) {
                        stringBuilder.append(item).append("\n"); // Add each item to the StringBuilder
                    }
                    String textToShow = stringBuilder.toString();

                    rawDataDisplay.setText(textToShow); // Set the text to rawDataDisplay
                }


            });
        }
    }}

