package com.example.sensorler;

import android.os.Bundle;
import android.os.BatteryManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ThermometerActivity extends AppCompatActivity {

    private TextView thermometerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermometer);

        // TextView öğesini tanımla
        thermometerData = findViewById(R.id.thermometerData);

        // Batarya sıcaklığını al
        getBatteryTemperature();
    }

    // Batarya sıcaklığını almak için fonksiyon
    private void getBatteryTemperature() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        if (batteryStatus != null) {
            // Batarya sıcaklık bilgisi (Celsius cinsinden)
            int temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            // Batarya sıcaklığı genellikle 10 ile çarpılarak °C cinsine dönüştürülür
            float celsius = temperature / 10.0f;

            // Sıcaklık verisini ekrana yazdır
            thermometerData.setText("Batarya Sıcaklığı: " + celsius + " °C");
        } else {
            // Eğer batarya durumu alınamazsa, kullanıcıya mesaj göster
            Toast.makeText(this, "Batarya sıcaklığı alınamadı.", Toast.LENGTH_SHORT).show();
        }
    }
}
