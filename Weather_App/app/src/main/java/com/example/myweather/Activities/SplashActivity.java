package com.example.myweather.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;


public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prefs = getSharedPreferences("minhduc", MODE_PRIVATE);
    }
    @Override
    protected void onResume() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onResume();

        final Handler handler = new Handler();
        // Chạy lần đầu xong tắt xài cái này
//        handler.postDelayed(() -> {
//            if (prefs.getBoolean("FirstRun", true)) {
//
//                prefs.edit().putBoolean("FirstRun", false).commit();
//                startActivity(new Intent(this , first_activity.class));
//                finish();
//            }
//            else {
//                startActivity(new Intent(this , MainActivity.class));
//                finish();
//            }
//        }, 900);

        // Không cần chạy lần đầu là tắt thì xài cái này
        handler.postDelayed(() -> {
                startActivity(new Intent(this , first_activity.class));
                finish();
        }, 900);
    }
}

