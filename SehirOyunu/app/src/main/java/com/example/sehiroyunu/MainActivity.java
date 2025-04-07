package com.example.sehiroyunu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar1;
    EditText editTextText;
    Button btnBasla, btnOnayla;
    TextView textViewPlaka;

    int plakaNo = 1;
    long startTime;
    HashMap<Integer, String> plakaMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar1 = findViewById(R.id.seekBar1);
        editTextText = findViewById(R.id.editTextText);
        btnBasla = findViewById(R.id.btnBasla);
        btnOnayla = findViewById(R.id.btnOnayla);
        textViewPlaka = findViewById(R.id.textViewPlaka); // SeekBar üzerindeki yazı

        plakaMap = PlakaData.getPlakaMap();

        // SeekBar başlangıçta 0'dan başlar ama biz 1-81 istiyoruz
        seekBar1.setMax(80); // 0–80 arası = 1–81 plaka
        seekBar1.setProgress(0);
        textViewPlaka.setText("Plaka: 1");

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                plakaNo = progress + 1;
                textViewPlaka.setText("Plaka: " + plakaNo);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnBasla.setOnClickListener(v -> {
            editTextText.setText("");
            startTime = System.currentTimeMillis();
        });

        btnOnayla.setOnClickListener(v -> {
            String girilen = editTextText.getText().toString().trim();
            String dogruSehir = plakaMap.get(plakaNo);
            long sure = (System.currentTimeMillis() - startTime) / 1000;

            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("dogruMu", girilen.equalsIgnoreCase(dogruSehir));
            intent.putExtra("sehir", dogruSehir);
            intent.putExtra("sure", sure);
            startActivity(intent);
        });
    }
}
