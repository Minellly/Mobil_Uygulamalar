package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MagnetometerActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor magnetometer;
    private TextView magnetometerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetometer);

        // TextView öğesini tanımla
        magnetometerData = findViewById(R.id.magnetometerData);

        // SensorManager'ı al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Cihazda manyetometre sensörü olup olmadığını kontrol et
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (magnetometer == null) {
            // Eğer manyetometre sensörü yoksa kullanıcıya mesaj göster
            Toast.makeText(this, "Bu cihazda manyetometre sensörü bulunmamaktadır.", Toast.LENGTH_LONG).show();
            magnetometerData.setText("Manyetometre sensörü bulunamadı.");
        } else {
            // Eğer manyetometre sensörü varsa, sensörü dinlemeye başla
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Manyetometre verisini al
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // X, Y ve Z eksenlerindeki manyetik alanı al
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Veriyi TextView'e yazdır
                magnetometerData.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
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
        if (magnetometer != null) {
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
