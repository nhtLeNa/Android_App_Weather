package com.example.myweather.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
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
    float lon;
    float lat;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
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
                    setUpMap();
                }

            }
        });

        Toolbar toolbar = findViewById(R.id.maps_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Maps.this.finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        lat = sp.getFloat("Lat", lat);
        lon = sp.getFloat("Lon", lon);
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
