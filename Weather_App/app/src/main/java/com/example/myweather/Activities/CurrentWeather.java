package com.example.myweather.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrentWeather extends AsyncTask<String, String, String> {
    private Context ScrollingContext;
    private Context ContentMainContext;

    private TextView todayTemperature;
    private TextView todayDescription;
    private TextView todayDes;
    private TextView todayWind;
    private TextView todayPressure;
    private TextView todayHumidity;
    private TextView todayUvIndex;
    private ImageView todayIcon;

    private String city_name;
    private String temperature;
    private String description;
    private String wind;
    private String humid;
    private String press;
    private String UV = "0";

    private String location = "SaiGon";

    public CurrentWeather(Context scrollingContext){
        this.ScrollingContext = scrollingContext;
        //this.ContentMainContext = R.layout.content_main;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        todayTemperature = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayTemperature);
        todayDescription = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayDescription);
        todayDes = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayDes);
        todayWind = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayWind);
        todayPressure = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayPressure);
        todayHumidity = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayHumidity);
        todayUvIndex = (TextView) ((Activity) ScrollingContext).findViewById(R.id.todayUvIndex);
        todayIcon = (ImageView) ((Activity) ScrollingContext).findViewById(R.id.todayIcon);

        todayDes.setText("loading");
        todayTemperature.setText("loading" +  "\u2103");
        todayDescription.setText("loading");
        todayWind.setText("loading");
        todayPressure.setText("loading");
        todayHumidity.setText("loading");
        todayUvIndex.setText("loading");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        RequestQueue requestQueue = Volley.newRequestQueue(this.ScrollingContext);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&appid=bd142ad7fbb16236247446591df59f0e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("data", response);
                        try {
                            JSONObject weather = new JSONObject(response);
                            JSONObject mainData = weather.getJSONObject("main");
                            JSONArray weatherData = weather.getJSONArray("weather");

                            city_name = weather.getString("name");
                            temperature = mainData.getString("temp");
                            description = weatherData.getJSONObject(0).getString ("description");
                            wind = weather.getJSONObject("wind").getString("speed");
                            humid = mainData.getString("humidity");
                            UV = "9";
                            press = mainData.getString("pressure");

                            Log.d("data2", city_name + "222");

                            todayDes.setText(city_name);
                            todayTemperature.setText(temperature +  "\u2103");
                            todayDescription.setText(description);
                            todayWind.setText(wind + " m/s");
                            todayPressure.setText(press + " hPA");
                            todayHumidity.setText(humid + " %");
                            todayUvIndex.setText(UV);

                            if(description.equals("broken clouds"))
                                todayIcon.setImageResource(R.drawable.brokenclouds);
                            else if (description.equals("tornado"))
                                todayIcon.setImageResource(R.drawable.tornado);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}
