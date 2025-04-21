package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PressureActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private TextView pressureData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);

        // TextView öğesini tanımla
        pressureData = findViewById(R.id.pressureData);

        // SensorManager'ı al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Cihazda basınç sensörü olup olmadığını kontrol et
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if (pressureSensor == null) {
            // Eğer basınç sensörü yoksa kullanıcıya mesaj göster
            Toast.makeText(this, "Bu cihazda basınç sensörü bulunmamaktadır.", Toast.LENGTH_LONG).show();
            pressureData.setText("Basınç sensörü bulunamadı.");
        } else {
            // Eğer basınç sensörü varsa, sensörü dinlemeye başla
            sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Basınç sensöründen veri alın
            if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                // Basınç verisini al ve TextView'e yazdır
                float pressure = event.values[0];
                pressureData.setText("Basınç: " + pressure + " hPa");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Sensör doğruluğu değiştiğinde yapılacak işlem
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // Aktivite durduğunda sensör dinleyicisini kaldır
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aktivite yeniden başlatıldığında sensör dinleyicisini başlat
        if (pressureSensor != null) {
            sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
