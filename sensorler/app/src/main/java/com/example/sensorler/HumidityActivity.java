package com.example.sensorler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HumidityActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor humiditySensor;
    private TextView humidityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);

        // TextView öğesini tanımla
        humidityData = findViewById(R.id.humidityData);

        // SensorManager'ı al
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Cihazda nem sensörü olup olmadığını kontrol et
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if (humiditySensor == null) {
            // Eğer nem sensörü yoksa kullanıcıya mesaj göster
            Toast.makeText(this, "Bu cihazda nem sensörü bulunmamaktadır.", Toast.LENGTH_LONG).show();
            humidityData.setText("Nem sensörü bulunamadı.");
        } else {
            // Eğer nem sensörü varsa, sensörü dinlemeye başla
            sensorManager.registerListener(sensorEventListener, humiditySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // SensorEventListener
    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Nem sensöründen veri alın
            if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                // Nem verisini al ve TextView'e yazdır
                float humidity = event.values[0];
                humidityData.setText("Nem: " + humidity + "%");
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
        if (humiditySensor != null) {
            sensorManager.registerListener(sensorEventListener, humiditySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
