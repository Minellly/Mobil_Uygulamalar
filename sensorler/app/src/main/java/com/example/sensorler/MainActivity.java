package com.example.sensorler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button accelerometerButton = findViewById(R.id.accelerometerButton);
        Button compassButton = findViewById(R.id.compassButton);
        Button gyroscopeButton = findViewById(R.id.gyroscopeButton);
        Button humidityButton = findViewById(R.id.humidityButton);
        Button lightButton = findViewById(R.id.lightButton);
        Button magnetometerButton = findViewById(R.id.magnetometerButton);
        Button pressureButton = findViewById(R.id.pressureButton);
        Button proximityButton = findViewById(R.id.proximityButton);
        Button thermometerButton = findViewById(R.id.thermometerButton);

        // Butonlara tıklama olayları ekleyin
        accelerometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccelerometerActivity.class);
                startActivity(intent);
            }
        });

        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });

        gyroscopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GyroscopeActivity.class);
                startActivity(intent);
            }
        });

        humidityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HumidityActivity.class);
                startActivity(intent);
            }
        });

        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LightActivity.class);
                startActivity(intent);
            }
        });

        magnetometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MagnetometerActivity.class);
                startActivity(intent);
            }
        });

        pressureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PressureActivity.class);
                startActivity(intent);
            }
        });

        proximityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProximityActivity.class);
                startActivity(intent);
            }
        });

        thermometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThermometerActivity.class);
                startActivity(intent);
            }
        });
    }
}