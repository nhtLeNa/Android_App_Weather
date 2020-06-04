package com.example.myweather.Activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myweather.Maps.TransparentTile;
import com.example.myweather.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Spinner spinner;
    String type;
    TileOverlay tileOver;
    LocationManager locationManager;
    Location location;
    float lon;
    float lat;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = findViewById(R.id.tileType);

        String[] tileName = new String[]{"Mây", "Nhiệt độ", "Lượng mưa", "Tuyết", "Mưa", "Gió", "Mực nước biển"};

        ArrayAdapter adpt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tileName);

        spinner.setAdapter(adpt);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView parent) {

            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                // Check click
                switch (position) {
                    case 0:
                        type = "clouds";
                        break;
                    case 1:
                        type = "temp";
                        break;
                    case 2:
                        type = "precipitation";
                        break;
                    case 3:
                        type = "snow";
                        break;
                    case 4:
                        type = "rain";
                        break;
                    case 5:
                        type = "wind";
                        break;
                    case 6:
                        type = "pressure";
                        break;

                }

                if (mMap != null) {
                    if (tileOver != null)
                        tileOver.remove();
                    System.out.println("con cac o tren");
                    setUpMap();
                }

            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //GetLocation();
    }

    public void GetLocation() {
        if (ActivityCompat.checkSelfPermission(
                Maps.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Maps.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                location = locationGPS;

                //LatLng vietnam = new LatLng(location.getLatitude(), location.getLongitude());

                //mMap.addMarker(new MarkerOptions().position(vietnam).title("Marker in Vietnam"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(vietnam));
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        lat = sp.getFloat("Lat", lat);
        lon = sp.getFloat("Lon", lon);
        System.out.println("lat, lon 2 " + lat + " " + lon);
        LatLng vietnam = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(vietnam).title("Marker in Vietnam"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vietnam));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMap != null) {
            mMap.clear();
        }
    }

    private void setUpMap() {
        tileOver = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new TransparentTile(type)));
    }
}
