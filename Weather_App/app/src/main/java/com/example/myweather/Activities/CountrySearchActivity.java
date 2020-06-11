package com.example.myweather.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myweather.Adapter.CovidStatusCountryAdapter;
import com.example.myweather.Model.CovidCountry;
import com.example.myweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CountrySearchActivity extends AppCompatActivity {

    public static final String COUNTRY_NAME = "country";
    public static final String COUNTRY_CONFIRMED = "cases";
    public static final String COUNTRY_ACTIVE = "active";
    public static final String COUNTRY_DECEASED = "deaths";
    public static final String COUNTRY_NEW_CONFIRMED = "todayCases";
    public static final String COUNTRY_TESTS = "tests";
    public static final String COUNTRY_NEW_DECEASED = "todayDeaths";
    public static final String COUNTRY_FLAGURL = "flag";
    public static final String COUNTRY_RECOVERED = "recovered";

    private RecyclerView recyclerviewCountries;
    private CovidStatusCountryAdapter countryAdapter;
    private RequestQueue requestQueue;
    private ArrayList<CovidCountry> countryModelArrayList;
//    ProgressDialog progressDialog;
    public static int confirmation = 0;
    public static String testValue;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_countries);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Global Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerviewCountries = findViewById(R.id.recyclerviewCountries);
        search = findViewById(R.id.edtSearchCountry);

        recyclerviewCountries.setHasFixedSize(true);
        recyclerviewCountries.setLayoutManager(new LinearLayoutManager(this));
        countryModelArrayList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        extractData();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        ArrayList<CovidCountry> filteredList = new ArrayList<>();
        for (CovidCountry item : countryModelArrayList) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        countryAdapter.filterList(filteredList);
    }

    private void extractData() {
        String dataURL = "https://corona.lmao.ninja/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dataURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    countryModelArrayList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        String countryName = jsonObject.getString("country");
                        String countryConfirmed = jsonObject.getString("cases");
                        String countryActive = jsonObject.getString("active");
                        String countryRecovered = jsonObject.getString("recovered");
                        String countryDeceased = jsonObject.getString("deaths");
                        String countryNewConfirmed = jsonObject.getString("todayCases");
                        String countryNewDeceased = jsonObject.getString("todayDeaths");
                        JSONObject object = jsonObject.getJSONObject("countryInfo");
                        String flagUrl = object.getString("flag");
                        String countryTests = jsonObject.getString("tests");
                        testValue = countryTests;

                        countryModelArrayList.add(new CovidCountry(flagUrl, countryName, countryConfirmed, countryNewConfirmed, countryDeceased, countryNewDeceased, countryRecovered, countryActive));

                    }
                    if (!testValue.isEmpty()) {
                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                countryAdapter = new CovidStatusCountryAdapter(CountrySearchActivity.this, countryModelArrayList);
                                recyclerviewCountries.setAdapter(countryAdapter);
                                //countrywiseAdapter.setOnItemClickListner(CountrywiseDataActivity, this);
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 500);
                        confirmation = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}
