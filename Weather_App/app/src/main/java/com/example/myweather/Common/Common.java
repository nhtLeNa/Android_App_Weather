package com.example.myweather.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String API_KEY = "7f04ec9d707085c3a4071198aabdb9de";
    public static Location current_location = null;

    public static String convertUnixToDate(int dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }

    public static String convertUnixToHour(int surise) {
        Date date = new Date(surise * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = simpleDateFormat.format(date);
        return formatted;
    }
}
