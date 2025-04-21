package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProximityActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private TextView proximityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        // TextView öğesini tanımla
        proximityData = findViewById(R.id.proximityData);

        // SensorManager'ı al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Cihazda proximity sensörü olup olmadığını kontrol et
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            // Eğer proximity sensörü yoksa kullanıcıya mesaj göster
            Toast.makeText(this, "Bu cihazda proximity sensörü bulunmamaktadır.", Toast.LENGTH_LONG).show();
            proximityData.setText("Proximity sensörü bulunamadı.");
        } else {
            // Eğer proximity sensörü varsa, sensörü dinlemeye başla
            sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Proximity sensöründen veri alın
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                // Proximity sensörü genellikle 0 (yakın) ve 5 (uzak) arasında değer verir
                float proximity = event.values[0];

                // Veriyi TextView'e yazdır
                if (proximity < proximitySensor.getMaximumRange()) {
                    proximityData.setText("Nesne yakında!");
                } else {
                    proximityData.setText("Nesne uzak!");
                }
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
        if (proximitySensor != null) {
            sensorManager.registerListener(sensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
