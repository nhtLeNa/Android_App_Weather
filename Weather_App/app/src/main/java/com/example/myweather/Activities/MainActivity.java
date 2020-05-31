package com.example.myweather.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.myweather.Adapter.ViewPagerAdapter;
import com.example.myweather.Adapter.WeatherRecyclerAdapter;
import com.example.myweather.Fragment.AboutDialogFragment;
import com.example.myweather.Fragment.CheckRefreshClickListener;
import com.example.myweather.Fragment.RecyclerViewFragment;
import com.example.myweather.Fragment.ShowRoundDialogFragment;
import com.example.myweather.Model.Weather;
import com.example.myweather.R;
import com.example.myweather.Task.GenericRequestWeather;
import com.example.myweather.Task.ParseResult;
import com.example.myweather.Task.TaskOutput;
import com.example.myweather.Utils.FormatIcon;
import com.example.myweather.Utils.UnitConvertor;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;

public class MainActivity extends AppCompatActivity implements LocationListener, CheckRefreshClickListener {

    private MaterialSearchView searchView;

    //Unit
    private static Map<String, Integer> speedUnits = new HashMap<>(3);
    private static Map<String, Integer> pressUnits = new HashMap<>(3);
    private static boolean mappingsInitialised = false;
    // Weather model
    private Weather todayWeather = new Weather();
    private List<Weather> longTermWeather = new ArrayList<>();
    private List<Weather> longTermTodayWeather = new ArrayList<>();
    private List<Weather> longTermTomorrowWeather = new ArrayList<>();
    //TextViewv
    private TextView citytool;
    private TextView todayTemperature;
    private TextView todayDescription;
    private TextView todaydes;
    private TextView todayWind;
    private TextView todayPressure;
    private TextView todayHumidity;
    private TextView todaySunrise;
    private TextView todaySunset;
    private TextView todayUvIndex;
    private TextView lastUpdate;
    private ImageView todayIcon;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout mainLay;
    private TextView currdate;
    private LinearLayout peekLayout;

    //Util
    private FormatIcon formatIcon;

    //String
    private String wetaherArray[]={"Thunderstorm accompanied by gusty winds and lightning is expected in several parts.","Thunderstorm accompanied by gusty winds, rain and lightning is expected in several parts.","Heavy thunderstorm sounds, relaxing pouring rain & lightning.",
            "Thunderstorm accompanied by gusty winds and lightning is expected in several parts.","Snow falling soundlessly in the middle of the night will always fill my heart with sweet clarity.","And when it rains on your parade, look up rather than down. Without the rain, there would be no rainbow.",
            "Some people feel the rain. Others just get wet.","I saw old autumn in the misty morn Stand shadowless like silence, listening To silence.","DUST STORM TO DETERIORATE AIR QUALITY IN SEVERAL REGION.",
            "Haze, pollution causing low visibility over several parts.","Another foogy day and patchy morning with minimum temperature likely to go down.",
            "Sudden, sharp increase in wind speed lasting minutes with the possibility of rain.","Severe weather brings a tornado, flooding and hail to the region.",
            "You can plan whether to observe galaxies or planets or stay home and process image data.","No clouds; just a bright sunny day.","Volcano violently erupts spewing ash and smoke into the sky.",
            "Fraction of the sky obscured by clouds, possibilty of rain. "};
    private ProgressDialog progressDialog;


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


        // Set up viewpager and tab
        todayTemperature = (TextView) findViewById(R.id.todayTemperature);
        todayDescription = (TextView) findViewById(R.id.todayDescription);
        todaydes=findViewById(R.id.todayDes);
        todayWind = (TextView) findViewById(R.id.todayWind);
        todayPressure = (TextView) findViewById(R.id.todayPressure);
        todayHumidity = (TextView) findViewById(R.id.todayHumidity);
        todaySunrise = (TextView) findViewById(R.id.todaySunrise);
        todaySunset = (TextView) findViewById(R.id.todaySunset);
        todayUvIndex = (TextView) findViewById(R.id.todayUvIndex);
        lastUpdate = (TextView) findViewById(R.id.lastUpdate);
        mainLay=findViewById(R.id.main);
        citytool=findViewById(R.id.citytool);
        peekLayout=findViewById(R.id.peeklayout);
        todayIcon = findViewById(R.id.todayIcon);
        currdate=findViewById(R.id.todayDate);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        ViewPagerBottomSheetBehavior behavior = ViewPagerBottomSheetBehavior.from(peekLayout);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int orientation = getResources().getConfiguration().orientation;


        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            behavior.setPeekHeight((height/6-80));
        } else {
            // In portrait
            behavior.setPeekHeight((height/4+50));
        }

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



    // Location change
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

    public static String getWindDirectionString(SharedPreferences sp, Context context, Weather weatherItem) {
        return "";
    }

    // Du bao thoi tiet o day

    public WeatherRecyclerAdapter getAdapter(int id) {
        WeatherRecyclerAdapter weatherRecyclerAdapter;
        if (id == 0) {
            weatherRecyclerAdapter = new WeatherRecyclerAdapter(this, longTermTodayWeather);
        } else if (id == 1) {
            weatherRecyclerAdapter = new WeatherRecyclerAdapter(this, longTermTomorrowWeather);
        } else {
            weatherRecyclerAdapter = new WeatherRecyclerAdapter(this, longTermWeather);
        }
        return weatherRecyclerAdapter;
    }

    private void preloadWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String lastToday = sp.getString("lastToday", "");
        if (!lastToday.isEmpty()) {
            new TodayWeatherTask(this, this, progressDialog).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "cachedResponse", lastToday);
        }
        String lastLongterm = sp.getString("lastLongterm", "");
        if (!lastLongterm.isEmpty()) {
            new LongTermWeatherTask(this, this, progressDialog).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "cachedResponse", lastLongterm);
        }

    }

    private String localize(SharedPreferences sp, String preferenceKey, String defaultValueKey) {
        return localize(sp, this, preferenceKey, defaultValueKey);
    }

    public static String localize(SharedPreferences sp, Context context, String preferenceKey, String defaultValueKey) {
        String preferenceValue = sp.getString(preferenceKey, defaultValueKey);
        String result = preferenceValue;
        if ("speedUnit".equals(preferenceKey)) {
            if (speedUnits.containsKey(preferenceValue)) {
                result = context.getString(speedUnits.get(preferenceValue));
            }
        } else if ("pressureUnit".equals(preferenceKey)) {
            if (pressUnits.containsKey(preferenceValue)) {
                result = context.getString(pressUnits.get(preferenceValue));
            }
        }
        return result;
    }

    private String getTimeFromAndroid() {
        Date dt = new Date();
        String time="";
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if(hours>=1 && hours<5){
            time = "dark";
        }else if(hours>=5 && hours<12){
            time = "day";
        }
        else if(hours>=12 && hours<19){
            time = "day";
        }else if(hours>=19 && hours<24){
            time = "night";
        }
        return time;
    }

    private String getIntervalAndroid() {
        Date dt = new Date();
        String time="";
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if(hours>=1 && hours<5){
            time="night";
        }
        else if(hours>=5 && hours<=6) {
            time="dawn";
        }else if(hours>6 && hours<12){
            time="morning";
        }
        else if(hours>=12 && hours<16){
            time="afternoon";
        }else if(hours>=16 && hours<18){
            time="evening";
        }
        else if(hours>=18 && hours<19){
            time="dusk";
        }
        else if(hours>=19 && hours<=24){
            time="night";
        }
        return time;
    }

    private void checkWeather(){
        String cond = todayWeather.getDescription().toLowerCase();
        String time = getTimeFromAndroid();
        if(cond.contains("thunderstorm")){
            todaydes.setText("Thunderstorm accompanied by gusty winds and lightning is expected in several parts.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.stormday));

            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.stormnight));
            }
        }
        else if(cond.contains("drizzle")){
            todaydes.setText("Isolated Rain Cools down Few regions, While Others Remain Hotter");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.lightraindrops));
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.lightrain));

            }
        }
        else if(cond.contains("rain")){
            todaydes.setText("Isolated Rain Cools down Few regions, While Others Remain Hotter");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.stormday));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainnight));
                ;
            }
        }
        else if(cond.contains("snow")){

            todaydes.setText("Time Lapse Ride Through Snow-Covered Streets of the city.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.showersleet));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.snownight));
                ;
            }
        }
        else if(cond.contains("mist")){
            todaydes.setText("Time Lapse Ride Through mist covered areas of the city.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.fog));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.mist));
                ;
            }
        }
        else if(cond.contains("smoke")){
            todaydes.setText("DUST STORM TO DETERIORATE AIR QUALITY IN SEVERAL REGION.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.duskclouds));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.wind));
                ;
            }
        }
        else if(cond.contains("haze")||  cond.contains("clear")){
            todaydes.setText("Clear and blithe day with no clouds in the sky.");
            String interval = getIntervalAndroid();
            switch(interval){
                case "night":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.mist));
                    ;
                    break;
                case "morning":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunset));
                    ;
                    break;
                case "afternoon":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                    ;
                    break;
                case "evening":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.evening));
                    ;
                    break;
                case "dusk":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunset));
                    break;
                case "dawn":
                    todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunrise));
                    ;
                    break;
                default:
                    break;
            }
        }
        else if(cond.equals("dust") || cond.contains("sand")){
            todaydes.setText("Dust storm to deteriorate air quality in several region.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.dustday));

            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.sand));
                ;
            }
        }
        else if(cond.contains("fog")){
            todaydes.setText("Another foogy day with minimum temperature likely to go down.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.fog));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.fognight));
                ;
            }
        }
        else if(cond.contains("ash")){
            todaydes.setText("Volcano violently erupts spewing ash and smoke into the sky.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.duskclouds));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.fognight));
                ;
            }
        }
        else if(cond.contains("squall")){
            todaydes.setText("Sudden, sharp increase in wind speed lasting minutes with the possibility of rain.");
            if(time.equals("day")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.squall));
                ;
            }else if(time.equals("night")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.squall));
                ;
            }
        }
        else if(cond.contains("clouds")) {
            todaydes.setText("Fraction of the sky obscured by clouds, possibilty of rain.");
            if(cond.equals("few clouds")|| cond.equals("scattered clouds")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.scatteredclouds));
            }
            else if(cond.equals("broken clouds")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.brokenclouds));
            }
            else if(cond.equals("overcast clouds")){
                todayIcon.setImageDrawable(getResources().getDrawable(R.drawable.evening));
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private void updateTodayWeatherUI() {
        try {
            if (todayWeather.getCountry().isEmpty()) {
                preloadWeather();
                return;
            }
        } catch (Exception e) {
            preloadWeather();
            return;
        }
        String city = todayWeather.getCity();
        String country = todayWeather.getCountry();
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        citytool.setText(city +" ,"+country);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        // Temperature
        float temperature = UnitConvertor.convertTemperature(Float.parseFloat(todayWeather.getTemperature()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }

        // Rain
        double rain = Double.parseDouble(todayWeather.getRain());
        String rainString = UnitConvertor.getRainString(rain, sp);

        // Wind
        double wind;
        try {
            wind = Double.parseDouble(todayWeather.getWind());
        } catch (Exception e) {
            e.printStackTrace();
            wind = 0;
        }
        wind = UnitConvertor.convertWind(wind, sp);

        // Pressure
        double pressure = UnitConvertor.convertPressure((float) Double.parseDouble(todayWeather.getPressure()), sp);

        todayTemperature.setText(new DecimalFormat("0.#").format(temperature) + "\u00b0");
        todayDescription.setText(todayWeather.getDescription().substring(0, 1).toUpperCase() +
                todayWeather.getDescription().substring(1) + rainString);

        if (sp.getString("speedUnit", "m/s").equals("bft")) {
            todayWind.setText(
                    UnitConvertor.getBeaufortName((int) wind) +
                            (todayWeather.isWindDirectionAvailable() ? " " + getWindDirectionString(sp, this, todayWeather) : ""));
        } else {
            todayWind.setText(new DecimalFormat("0.0").format(wind) + " " +
                    localize(sp, "speedUnit", "m/s") +
                    (todayWeather.isWindDirectionAvailable() ? " " + getWindDirectionString(sp, this, todayWeather) : ""));
        }
        todayPressure.setText( new DecimalFormat("0.0").format(pressure) + " " +
                localize(sp, "pressureUnit", "hPa"));
        todayHumidity.setText(todayWeather.getHumidity() + " %");
        todaySunrise.setText(timeFormat.format(todayWeather.getSunrise()));
        todaySunset.setText(timeFormat.format(todayWeather.getSunset()));
        citytool=findViewById(R.id.citytool);
        citytool.setText(city);
        checkWeather();
        todayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void updateLongTermWeatherUI() {
//        if (destroyed) {
//            return;
//        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundleToday = new Bundle();
        bundleToday.putInt("day", 0);
        RecyclerViewFragment recyclerViewFragmentToday = new RecyclerViewFragment();
        recyclerViewFragmentToday.setArguments(bundleToday);
        viewPagerAdapter.addFragment(recyclerViewFragmentToday, getString(R.string.today));

        Bundle bundleTomorrow = new Bundle();
        bundleTomorrow.putInt("day", 1);
        RecyclerViewFragment recyclerViewFragmentTomorrow = new RecyclerViewFragment();
        recyclerViewFragmentTomorrow.setArguments(bundleTomorrow);
        viewPagerAdapter.addFragment(recyclerViewFragmentTomorrow, getString(R.string.tomorrow));

        Bundle bundle = new Bundle();
        bundle.putInt("day", 2);
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(recyclerViewFragment, getString(R.string.later));

        int currentPage = viewPager.getCurrentItem();

        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (currentPage == 0 && longTermTodayWeather.isEmpty()) {
            currentPage = 1;
        }
        viewPager.setCurrentItem(currentPage, false);
    }

    public static String getRainString(JSONObject rainObj) {
        String rain = "0";
        if (rainObj != null) {
            rain = rainObj.optString("3h", "fail");
            if ("fail".equals(rain)) {
                rain = rainObj.optString("1h", "0");
            }
        }
        return rain;
    }

    private ParseResult parseTodayJson(String result) {
        try {
            JSONObject reader = new JSONObject(result);

            final String code = reader.optString("cod");
            if ("404".equals(code)) {
                return ParseResult.CITY_NOT_FOUND;
            }

            String city = reader.getString("name");
            String country = "";
            JSONObject countryObj = reader.optJSONObject("sys");
            if (countryObj != null) {
                country = countryObj.getString("country");
                todayWeather.setSunrise(countryObj.getString("sunrise"));
                todayWeather.setSunset(countryObj.getString("sunset"));
            }
            todayWeather.setCity(city);

            todayWeather.setCountry(country);

            JSONObject coordinates = reader.getJSONObject("coord");
            if (coordinates != null) {
                todayWeather.setLat(coordinates.getDouble("lat"));
                todayWeather.setLon(coordinates.getDouble("lon"));
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putFloat("latitude", (float) todayWeather.getLat()).putFloat("longitude", (float) todayWeather.getLon()).commit();
            }

            JSONObject main = reader.getJSONObject("main");

            todayWeather.setTemperature(main.getString("temp"));
            todayWeather.setDescription(reader.getJSONArray("weather").getJSONObject(0).getString("description"));
            JSONObject windObj = reader.getJSONObject("wind");
            todayWeather.setWind(windObj.getString("speed"));
            if (windObj.has("deg")) {
                todayWeather.setWindDirectionDegree(windObj.getDouble("deg"));
            } else {
                Log.e("parseTodayJson", "No wind direction available");
                todayWeather.setWindDirectionDegree(null);
            }
            todayWeather.setPressure(main.getString("pressure"));
            todayWeather.setHumidity(main.getString("humidity"));

            JSONObject rainObj = reader.optJSONObject("rain");
            String rain;
            if (rainObj != null) {
                rain = getRainString(rainObj);
            } else {
                JSONObject snowObj = reader.optJSONObject("snow");
                if (snowObj != null) {
                    rain = getRainString(snowObj);
                } else {
                    rain = "0";
                }
            }
            todayWeather.setRain(rain);

            final String idString = reader.getJSONArray("weather").getJSONObject(0).getString("id");
            todayWeather.setId(idString);
            todayWeather.setIcon(formatIcon.setWeatherIcon(Integer.parseInt(idString), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString("lastToday", result);
            editor.commit();

        } catch (JSONException e) {
            Log.e("JSONException Data", result);
            e.printStackTrace();
            return ParseResult.JSON_EXCEPTION;
        }

        return ParseResult.OK;
    }

    public ParseResult parseLongTermJson(String result) {
        int i;
        try {
            JSONObject reader = new JSONObject(result);

            final String code = reader.optString("cod");
            if ("404".equals(code)) {
                if (longTermWeather == null) {
                    longTermWeather = new ArrayList<>();
                    longTermTodayWeather = new ArrayList<>();
                    longTermTomorrowWeather = new ArrayList<>();
                }
                return ParseResult.CITY_NOT_FOUND;
            }

            longTermWeather = new ArrayList<>();
            longTermTodayWeather = new ArrayList<>();
            longTermTomorrowWeather = new ArrayList<>();

            JSONArray list = reader.getJSONArray("list");
            for (i = 0; i < list.length(); i++) {
                Weather weather = new Weather();

                JSONObject listItem = list.getJSONObject(i);
                JSONObject main = listItem.getJSONObject("main");

                weather.setDate(listItem.getString("dt"));
                weather.setTemperature(main.getString("temp"));
                weather.setDescription(listItem.optJSONArray("weather").getJSONObject(0).getString("description"));
                JSONObject windObj = listItem.optJSONObject("wind");
                if (windObj != null) {
                    weather.setWind(windObj.getString("speed"));
                    weather.setWindDirectionDegree(windObj.getDouble("deg"));
                }
                weather.setPressure(main.getString("pressure"));
                weather.setHumidity(main.getString("humidity"));

                JSONObject rainObj = listItem.optJSONObject("rain");
                String rain = "";
                if (rainObj != null) {
                    rain = getRainString(rainObj);
                } else {
                    JSONObject snowObj = listItem.optJSONObject("snow");
                    if (snowObj != null) {
                        rain = getRainString(snowObj);
                    } else {
                        rain = "0";
                    }
                }
                weather.setRain(rain);

                final String idString = listItem.optJSONArray("weather").getJSONObject(0).getString("id");
                weather.setId(idString);

                final String dateMsString = listItem.getString("dt") + "000";
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(dateMsString));
                weather.setIcon(formatIcon.setWeatherIcon(Integer.parseInt(idString), cal.get(Calendar.HOUR_OF_DAY)));
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                Calendar tomorrow = (Calendar) today.clone();
                tomorrow.add(Calendar.DAY_OF_YEAR, 1);

                Calendar later = (Calendar) today.clone();
                later.add(Calendar.DAY_OF_YEAR, 2);

                if (cal.before(tomorrow)) {
                    longTermTodayWeather.add(weather);
                } else if (cal.before(later)) {
                    longTermTomorrowWeather.add(weather);
                } else {
                    longTermWeather.add(weather);
                }
            }
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString("lastLongterm", result);
            editor.commit();
        } catch (JSONException e) {
            Log.e("JSONException Data", result);
            e.printStackTrace();
            return ParseResult.JSON_EXCEPTION;
        }

        return ParseResult.OK;
    }

    class TodayWeatherTask extends GenericRequestWeather {
        public TodayWeatherTask(Context context, MainActivity activity, ProgressDialog progressDialog) {
            super(context, activity, progressDialog);
        }

        @Override
        protected void onPreExecute() {
            loading = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TaskOutput output) {
            super.onPostExecute(output);
            // Update widgets
//            AbstractWidgetProvider.updateWidgets(MainActivity.this);
//            DashClockWeatherExtension.updateDashClock(MainActivity.this);
        }

        @Override
        protected ParseResult parseResponse(String response) {
            return parseTodayJson(response);
        }

        @Override
        protected String getAPIName() {
            return "weather";
        }

        @Override
        protected void updateMainUI() {
            updateTodayWeatherUI();
//            updateLastUpdateTime();
//            updateUVIndexUI();
        }
    }

    class LongTermWeatherTask extends GenericRequestWeather {
        public LongTermWeatherTask(Context context, MainActivity activity, ProgressDialog progressDialog) {
            super(context, activity, progressDialog);
        }

        @Override
        protected ParseResult parseResponse(String response) {
            return parseLongTermJson(response);
        }

        @Override
        protected String getAPIName() {
            return "forecast";
        }

        @Override
        protected void updateMainUI() {
            updateLongTermWeatherUI();
        }
    }
}
