package com.example.myweather.Task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myweather.Model.Weather;
import com.example.myweather.Model.WeatherResult;
import com.example.myweather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class GetCurrentWeather extends AsyncTask<String, String, String> {
    private Context context;
    private WeatherResult weatherResult;
    private String currentLoaction;


    public GetCurrentWeather(Context context, WeatherResult weatherResult, String currentLoaction){
        this.context = context;
        this.weatherResult = weatherResult;
        this.currentLoaction = currentLoaction;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + currentLoaction +
                "&units=metric" +
                "&appid=" + this.context.getResources().getString(R.string.apiKey);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject weather = new JSONObject(response);
                            JSONObject mainData = weather.getJSONObject("main");
                            JSONArray weatherData = weather.getJSONArray("weather");

                            Gson gson = new Gson();
                            String mJsonString = weather.toString();
                            Log.d("data1", mJsonString);

                            Type weatherType = new TypeToken<ArrayList<WeatherResult>>(){}.getType();
                            //ArrayList<WeatherResult> wt = gson.fromJson(mJsonString, weatherType);
                            weatherResult = gson.fromJson(mJsonString, WeatherResult.class);

                            Log.d("data2", weatherResult.toString());

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
