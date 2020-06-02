package com.example.myweather.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myweather.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;


public class CovidStatusActivity extends AppCompatActivity {
    //Covid
    TextView tvCases,tvRecovered,tvDeaths,tvActive;
    TextView tvTodayCases,tvTodayRecovered,tvTodayDeaths,tvTodayActive;
    PieChart pieChart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_status);

        //Covid
        tvCases = findViewById(R.id.confirmed_textView);
        tvRecovered = findViewById(R.id.recovered_textView);
        tvDeaths = findViewById(R.id.death_textView);
        tvActive = findViewById(R.id.active_textView);

        tvTodayCases = findViewById(R.id.confirmed_new_textView);
        tvTodayActive = findViewById(R.id.active_new_textView);
        tvTodayDeaths = findViewById(R.id.death_new_textView);
        tvTodayRecovered = findViewById(R.id.recovered_new_textView);
        pieChart = findViewById(R.id.piechart);
        fetchData();
    }

    private void fetchData() {

        String url  = "https://corona.lmao.ninja/v2/all/";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            tvCases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvDeaths.setText(jsonObject.getString("deaths"));
                            tvActive.setText(jsonObject.getString("active"));

                            tvTodayCases.setText(jsonObject.getString("todayCases"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));

                            pieChart.addPieSlice(new PieModel("Recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#0F9D58")));
                            pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(tvDeaths.getText().toString()), Color.parseColor("#DB4437")));
                            pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#348af7")));
                            pieChart.startAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CovidStatusActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
