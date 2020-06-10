package com.example.myweather.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.myweather.AirVisual.HTTPHandler;
import com.example.myweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AirVisual extends Activity {
    ArrayList<HashMap<String, String>> information;
    LinearLayout panel;
    TextView state;
    TextView aqi;
    TextView mainus;
    TextView collusion;
    TextView city;
    TextView country;
    TextView temperature;
    TextView pressure;
    TextView humidity;
    TextView wind_speed;
    TextView wind_direction;
    TextView timestamp;
    ImageView icon;

    float lon;
    float lat;

    private static final String AIRVISUAL_URL = "https://api.airvisual.com/v2/nearest_city?lat=%f&lon=%f&key=bda3c64f-8520-4e94-afd2-a7277dd60738";

    private static Map<Integer, String> STATE = new HashMap<>();

    static {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Good");
        map.put(1, "Moderate");
        map.put(2, "Unhealthy for Sensitive Groups");
        map.put(3, "Unhealthy");
        map.put(4, "Very Unhealthy");
        map.put(5, "Hazardous");
        STATE = Collections.unmodifiableMap(map);
    }

    private static Map<Integer, String> COLLUSION = new HashMap<>();

    static {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Little to no health risk");
        map.put(1, "Sensitive individuals may experience irritations");
        map.put(2, "Sensitive groups should limit outdoor exertion");
        map.put(3, "Harmful for sensitive groups, reduced outdoor activity");
        map.put(4, "Everyone can be affected. Avoid heavy outdoor activity");
        map.put(5, "Serious risk of respiratory effects. Everyone should avoid outdoor activity");
        COLLUSION = Collections.unmodifiableMap(map);
    }

    private static Map<String, String> MAINUS = new HashMap<>();

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put("p2", "pm2.5");
        map.put("p1", "pm10");
        map.put("o3", "Ozone 03");
        map.put("n2", "Nitrogen dioxide NO2");
        map.put("s2", "Sulfur dioxide SO2");
        map.put("co", "Carbon monoxide CO");
        MAINUS = Collections.unmodifiableMap(map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airvisual);

        panel = findViewById(R.id.airvisual_panel);
        state = findViewById(R.id.airvisual_state);
        aqi = findViewById(R.id.airvisual_aqi);
        mainus = findViewById(R.id.airvisual_mainus);
        collusion = findViewById(R.id.airvisual_collusion);
        city = findViewById(R.id.airvisual_city);
        country = findViewById(R.id.airvisual_country);
        temperature = findViewById(R.id.airvisual_temperature);
        pressure = findViewById(R.id.airvisual_pressure);
        humidity = findViewById(R.id.airvisual_humidity);
        wind_speed = findViewById(R.id.airvisual_wind_speed);
        wind_direction = findViewById(R.id.airvisual_wind_direction);
        timestamp = findViewById(R.id.airvisual_timestamp);
        icon = findViewById(R.id.airvisual_icon);

        Toolbar toolbar = findViewById(R.id.airvisual_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AirVisual.this.finish();
            }
        });

        information = new ArrayList<>();
        new GetAQI().execute();
    }

    private class GetAQI extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(AirVisual.this, "JSON Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AirVisual.this);
            lat = sp.getFloat("Lat", lat);
            lon = sp.getFloat("Lon", lon);

            HTTPHandler sh = new HTTPHandler();
            String url = String.format(AIRVISUAL_URL, lat, lon);
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject data = jsonObj.getJSONObject("data");
                    JSONObject current = data.getJSONObject("current");
                    JSONObject weather = current.getJSONObject("weather");
                    JSONObject pollution = current.getJSONObject("pollution");

                    String city = data.getString("city");
                    String country = data.getString("country");

                    String tp = weather.getString("tp");
                    String pr = weather.getString("pr");
                    String hu = weather.getString("hu");
                    String ws = weather.getString("ws");
                    String wd = weather.getString("wd");
                    String ic = weather.getString("ic");
                    String ts = weather.getString("ts");

                    String aqius = pollution.getString("aqius");
                    String mainus = pollution.getString("mainus");
                    String aqicn = pollution.getString("aqicn");
                    String maincn = pollution.getString("maincn");

                    HashMap<String, String> location_information = new HashMap<>();
                    HashMap<String, String> weather_information = new HashMap<>();
                    HashMap<String, String> pollution_information = new HashMap<>();

                    location_information.put("city", city);
                    location_information.put("country", country);

                    weather_information.put("temperature", tp);
                    weather_information.put("pressure", pr);
                    weather_information.put("humidity", hu);
                    weather_information.put("wind_speed", ws);
                    weather_information.put("wind_direction", wd);
                    weather_information.put("icon", ic);
                    weather_information.put("timestamp", ts);

                    pollution_information.put("aqius", aqius);
                    pollution_information.put("mainus", mainus);
                    pollution_information.put("aqicn", aqicn);
                    pollution_information.put("maincn", maincn);

                    // adding contact to contact list
                    information.add(location_information);
                    information.add(weather_information);
                    information.add(pollution_information);
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Data parsing failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get data from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                int index = Integer.parseInt(information.get(2).get("aqius")) - 1;
                index = index / 50;
                if (index == 5)
                    index = 4;
                else if (index > 5)
                    index = 5;

                switch (index) {
                    case 0:
                        panel.setBackgroundColor(Color.GREEN);
                        break;
                    case 1:
                        panel.setBackgroundColor(Color.YELLOW);
                        break;
                    case 2:
                        panel.setBackgroundColor(Color.rgb(255, 165, 0));
                        break;
                    case 3:
                        panel.setBackgroundColor(Color.RED);
                        break;
                    case 4:
                        panel.setBackgroundColor(Color.rgb(128, 0, 128));
                        break;
                    case 5:
                        panel.setBackgroundColor(Color.rgb(103, 58, 63));
                        break;
                }

                switch (information.get(1).get("icon")) {
                    case "01d":
                        icon.setBackgroundResource(R.drawable.airvisual_01d);
                        break;
                    case "01n":
                        icon.setBackgroundResource(R.drawable.airvisual_01n);
                        break;
                    case "02d":
                        icon.setBackgroundResource(R.drawable.airvisual_02d);
                        break;
                    case "02n":
                        icon.setBackgroundResource(R.drawable.airvisual_02n);
                        break;
                    case "03d":
                        icon.setBackgroundResource(R.drawable.airvisual_03d);
                        break;
                    case "03n":
                        icon.setBackgroundResource(R.drawable.airvisual_03n);
                        break;
                    case "04d":
                        icon.setBackgroundResource(R.drawable.airvisual_04d);
                        break;
                    case "04n":
                        icon.setBackgroundResource(R.drawable.airvisual_04n);
                        break;
                    case "09d":
                        icon.setBackgroundResource(R.drawable.airvisual_09d);
                        break;
                    case "09n":
                        icon.setBackgroundResource(R.drawable.airvisual_09n);
                        break;
                    case "10d":
                        icon.setBackgroundResource(R.drawable.airvisual_10d);
                        break;
                    case "10n":
                        icon.setBackgroundResource(R.drawable.airvisual_10n);
                        break;
                    case "11d":
                        icon.setBackgroundResource(R.drawable.airvisual_11d);
                        break;
                    case "11n":
                        icon.setBackgroundResource(R.drawable.airvisual_11n);
                        break;
                    case "13d":
                        icon.setBackgroundResource(R.drawable.airvisual_13d);
                        break;
                    case "13n":
                        icon.setBackgroundResource(R.drawable.airvisual_13n);
                        break;
                    case "50d":
                        icon.setBackgroundResource(R.drawable.airvisual_50d);
                        break;
                    case "50n":
                        icon.setBackgroundResource(R.drawable.airvisual_50n);
                        break;
                }

                String time = information.get(1).get("timestamp");
                time = time.replace("T", " ");
                time = time.replace("Z", "");

                state.setText(STATE.get(index));
                aqi.setText("US AQI: " + information.get(2).get("aqius"));
                mainus.setText("Main pollutant: " + MAINUS.get(information.get(2).get("mainus")));
                collusion.setText(COLLUSION.get(index));
                city.setText("City: " + information.get(0).get("city"));
                country.setText("Country: " + information.get(0).get("country"));
                temperature.setText("Temperature: " + information.get(1).get("temperature") + "\u2103");
                pressure.setText("Pressure: " + information.get(1).get("pressure") + " hPa");
                humidity.setText("Humidity: " + information.get(1).get("humidity") + "%");
                wind_speed.setText("Wind speed: " + information.get(1).get("wind_speed") + " m/s");
                wind_direction.setText("Wind direction: " + information.get(1).get("wind_direction") + "\u00B0");
                timestamp.setText("Last updated: " + time);
            }catch (Exception e){
            }
        }
    }
}