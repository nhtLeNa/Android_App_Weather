package com.example.myweather.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myweather.Model.WeatherResult;
import com.example.myweather.R;
import com.example.myweather.Task.GetCurrentWeather;
import com.example.myweather.Task.GetWeatherTask;
import com.example.myweather.Task.ParseResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;

    public String recentCityId = "";
    private androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherResult weatherResult = new WeatherResult();
        GetCurrentWeather task = new GetCurrentWeather(MainActivity.this, weatherResult, "SaiGon");
        task.execute();

        setContentView(R.layout.activity_scrolling);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);

        return true;
    }

    public static long saveLastUpdateTime(SharedPreferences defaultSharedPreferences) {
        Calendar now = Calendar.getInstance();
        defaultSharedPreferences.edit().putLong("lastUpdate", now.getTimeInMillis()).commit();
        return now.getTimeInMillis();
    }
}
