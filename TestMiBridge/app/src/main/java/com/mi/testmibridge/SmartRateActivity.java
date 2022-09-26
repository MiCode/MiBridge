package com.mi.testmibridge;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SmartRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_rate);

        findViewById(R.id.open_video).setOnClickListener(v -> {
            Intent intent = new Intent(SmartRateActivity.this, VideoActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.open_live).setOnClickListener(v -> {
            Intent intent = new Intent(SmartRateActivity.this, LiveActivity.class);
            startActivity(intent);
        });
    }
}