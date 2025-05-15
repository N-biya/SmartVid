package com.example.smartvid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Automatically shows the splash screen background
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen); // Shows "SmartVid" text

        // Delay and move to MainActivity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish(); // Close SplashActivity so user can't go back
        }, 1500); // 1.5 seconds
    }
}
