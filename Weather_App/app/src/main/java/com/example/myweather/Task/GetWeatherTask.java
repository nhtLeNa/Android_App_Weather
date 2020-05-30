package com.example.myweather.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.myweather.Activities.MainActivity;
import com.example.myweather.Common;
import com.example.myweather.R;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWeatherTask extends AsyncTask<String, String, String> {

    ProgressDialog progressDialog;
    Context context;
    MainActivity mainActivity;

    public GetWeatherTask() {

    }


    @Override
    protected String doInBackground(String... args) {
        String API_KEY = Common.API_KEY;
        String response = "";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=10&lon=106&appid=7f04ec9d707085c3a4071198aabdb9de");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                System.out.println("zo");
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                response += stringBuilder.toString();
                close(r);
                urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
        return response;
    }

    @Override
    protected void onPostExecute(String res) {
        System.out.println(res);
    }


    private void restorePreviousCity() {
        if (!TextUtils.isEmpty(mainActivity.recentCityId)) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString("cityId", mainActivity.recentCityId);
            editor.commit();
            mainActivity.recentCityId = "";
        }
    }

    private static void close(Closeable x) {
        try {
            if (x != null) {
                x.close();
            }
        } catch (IOException e) {
            Log.e("IOException Data", "Error occurred while closing stream");
        }
    }


}
