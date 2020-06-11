package com.example.myweather.Activities;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Objects;

public class CovidStatusCountryActivity extends AppCompatActivity {
    TextView cases,todayCases,deaths,todayDeaths,recovered,active,name;
    PieChart cPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_country_status);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Covid Status");

        Bundle extras = getIntent().getExtras();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cases = findViewById(R.id.txtConfirmed);
        todayCases = findViewById(R.id.txtTodayConfirmed);
        deaths = findViewById(R.id.txtDeath);
        todayDeaths = findViewById(R.id.txtDeathToday);
        recovered = findViewById(R.id.txtRecovered);
        active = findViewById(R.id.txtActive);
        name = findViewById(R.id.txtName);
        cPieChart = findViewById(R.id.cpiechart);

        cases.setText(extras.getString("CASE"));
        todayCases.setText(extras.getString("TODAYCASE"));
        deaths.setText(extras.getString("DEATH"));
        todayDeaths.setText(extras.getString("TODAYDEATH"));
        recovered.setText(extras.getString("RECOVERED"));
        active.setText(extras.getString("ACTIVE"));
        name.setText(extras.getString("NAME"));

//        Log.d("data3", "A"+ recovered.getText().toString() + "B");
//        Log.d("name", "A"+ extras.getString("NAME") + "B");

        cPieChart.addPieSlice(new PieModel("Recoverd",Integer.parseInt(recovered.getText().toString()), Color.parseColor("#0F9D58")));
        cPieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(deaths.getText().toString()), Color.parseColor("#DB4437")));
        cPieChart.addPieSlice(new PieModel("Active",Integer.parseInt(active.getText().toString()), Color.parseColor("#348af7")));
        cPieChart.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}