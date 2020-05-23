package com.example.myweather.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.myweather.Adapter.ViewPagerAdapter;
import com.example.myweather.Common.Common;
import com.example.myweather.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrolling);

        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);
        currentWeather.execute();

        TextView todayTemperature = (TextView) findViewById(R.id.todayTemperature);
        todayTemperature.setText("2200");

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
}
