package com.example.myweather.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myweather.R;

public class first_activity extends AppCompatActivity {
    private ImageView pixelImageView;
    private Button pixelButton;
    private TextView helloTextView;
    private TextView progressTextView;
    private TextView introTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        pixelButton = findViewById(R.id.pixelbutton);
        pixelImageView = findViewById(R.id.pixelimage);
        helloTextView = findViewById(R.id.hello);
        progressTextView = findViewById(R.id.progresstext);
        introTextView = findViewById(R.id.intro);

        final Animation fade = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade);
        final Animation fadeButton = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadebutton);
        final Animation fadeFirst = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadefirst);
        final Animation fadeIn = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
        final Animation floating = AnimationUtils.loadAnimation(getBaseContext(), R.anim.floating);

        helloTextView.startAnimation(fadeFirst);
        new Handler().postDelayed(() -> { }, 400);
        pixelImageView.startAnimation(fadeIn);
        new Handler().postDelayed(() -> { }, 400);
        introTextView.startAnimation(fade);
        pixelButton.startAnimation(fadeButton);

        pixelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(first_activity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
    }
}
