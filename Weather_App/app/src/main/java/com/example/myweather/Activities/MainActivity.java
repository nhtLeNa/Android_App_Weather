package com.example.myweather.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myweather.Fragment.AboutDialogFragment;
import com.example.myweather.Fragment.CheckRefreshClickListener;
import com.example.myweather.Fragment.ShowRoundDialogFragment;
import com.example.myweather.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LocationListener, CheckRefreshClickListener {

    private MaterialSearchView searchView;

    public String recentCityId = "";
    private androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set SearchView
        searchView = findViewById(R.id.search_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bottommenu) {
            ShowRoundDialogFragment showRoundDialogFragment = ShowRoundDialogFragment.newInstance();
            showRoundDialogFragment.show(getSupportFragmentManager(), "add_menu_fragment");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGraphClick() {

    }

    @Override
    public void onUpdateClick() {

    }

    @Override
    public void onShareClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "\n\n For more weather updates, check this cool Weather app at: https://github.com/nhtien2010/Android_App_Weather");
        sendIntent.setType("text/plain");
        this.startActivity(sendIntent);
    }

    @Override
    public void onSettingsClick() {

    }

    @Override
    public void onAboutClick() {
        new AboutDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRefresh() {

    }

    public static long saveLastUpdateTime(SharedPreferences defaultSharedPreferences) {
        Calendar now = Calendar.getInstance();
        defaultSharedPreferences.edit().putLong("lastUpdate", now.getTimeInMillis()).commit();
        return now.getTimeInMillis();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
