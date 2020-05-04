package com.example.myweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myweather.Common.Common;
import com.example.myweather.Model.WeatherResult;
import com.example.myweather.Retrofit.IOpenWeatherMap;
import com.example.myweather.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class TodayWeatherFragment extends Fragment {

    private ImageView img_weather;
    private TextView txt_city_name;
    private TextView txt_humidity;
    private TextView txt_description;
    private TextView txt_sunset;
    private TextView txt_sunrise;
    private TextView txt_temperature;
    private TextView txt_pressure;
    private TextView txt_date_time;
    private TextView txt_wind;
    private TextView txt_geo_coord;
    private LinearLayout weather_panel;
    private ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayWeatherFragment instance;

    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public static TodayWeatherFragment getInstance() {
        if (instance == null) {
            instance = new TodayWeatherFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        img_weather = itemView.findViewById(R.id.img_weather);
        txt_city_name = itemView.findViewById(R.id.txt_city_name);
        txt_date_time = itemView.findViewById(R.id.txt_date_time);
        txt_description = itemView.findViewById(R.id.txt_description);
        txt_sunrise = itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = itemView.findViewById(R.id.txt_sunset);
        txt_humidity = itemView.findViewById(R.id.txt_humidity);
        txt_geo_coord = itemView.findViewById(R.id.txt_geo_coord);
        txt_wind = itemView.findViewById(R.id.txt_wind);
        txt_pressure = itemView.findViewById(R.id.txt_pressure);
        txt_temperature = itemView.findViewById(R.id.txt_temperature);

        weather_panel = itemView.findViewById(R.id.weather_panel);
        loading = itemView.findViewById(R.id.loading);
        
        getWeatherInformation();
        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()), Common.API_KEY, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        Picasso.get().load(String.valueOf(new StringBuilder("https://openweathermap.org/img/w")
                                .append(weatherResult.getWeather().get(0).getIcon()).append(".png"))).into(img_weather);

                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in")
                        .append(weatherResult.getName().toString()));
                        txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("C").toString());
                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());
                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Common.convertUnixToHour((weatherResult.getSys().getSunset())));
                        txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().getLat()).append(", " + weatherResult.getCoord().getLon() + "]"));

                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
