package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GyroscopeActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private TextView gyroscopeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        // TextView öğesini tanımla
        gyroscopeData = findViewById(R.id.gyroscopeData);

        // SensorManager ve jiroskop sensörünü tanımla
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Jiroskop sensörünü dinlemeye başla
        sensorManager.registerListener(sensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Jiroskop verisini al
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                // X, Y ve Z eksenlerindeki dönüş hızlarını al
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Veriyi TextView'e yazdır
                gyroscopeData.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
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
        sensorManager.registerListener(sensorEventListener, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }
}
