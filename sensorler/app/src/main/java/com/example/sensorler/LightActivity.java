package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LightActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        // TextView öğesini tanımla
        lightData = findViewById(R.id.lightData);

        // SensorManager'ı al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Cihazda ışık sensörü olup olmadığını kontrol et
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            // Eğer ışık sensörü yoksa kullanıcıya mesaj göster
            Toast.makeText(this, "Bu cihazda ışık sensörü bulunmamaktadır.", Toast.LENGTH_LONG).show();
            lightData.setText("Işık sensörü bulunamadı.");
        } else {
            // Eğer ışık sensörü varsa, sensörü dinlemeye başla
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Işık sensöründen veri alın
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                // Işık verisini al ve TextView'e yazdır
                float light = event.values[0];
                lightData.setText("Işık: " + light + " lx");
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
        if (lightSensor != null) {
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
