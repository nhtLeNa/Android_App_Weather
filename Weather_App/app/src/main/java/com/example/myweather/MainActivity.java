package com.example.myweather;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private DrawerLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        mainLayout = (DrawerLayout)findViewById(R.id.mainLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mainLayout, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(mainLayout.isDrawerOpen((GravityCompat.START))) {
            mainLayout.closeDrawer(GravityCompat.START);
        }
        else {

            super.onBackPressed();
        }
    }
}