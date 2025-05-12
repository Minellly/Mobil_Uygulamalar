package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDailyActivity extends AppCompatActivity {

    private EditText dailyEntryEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);

        // DatabaseHelper'ı başlatıyoruz
        dbHelper = new DatabaseHelper(this);

        dailyEntryEditText = findViewById(R.id.dailyEntryEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dailyEntry = dailyEntryEditText.getText().toString().trim();
                if (!dailyEntry.isEmpty()) {
                    // Günlük kaydediliyor
                    saveDailyEntry(dailyEntry);
                    Toast.makeText(AddDailyActivity.this, "Günlük kaydedildi!", Toast.LENGTH_SHORT).show();

                    // Kaydedildikten sonra MainActivity'yi başlatıyoruz
                    Intent intent = new Intent(AddDailyActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // MainActivity'yi tekrar başlatıyoruz
                    startActivity(intent); // Yeni Activity başlatılıyor
                    finish(); // AddDailyActivity'yi kapatıyoruz
                } else {
                    Toast.makeText(AddDailyActivity.this, "Lütfen günlük yazınız!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDailyEntry(String dailyEntry) {
        // Veritabanına günlük kaydediyoruz
        int dailyCount = getCurrentDailyCount(); // Günlük sayısını alıyoruz
        String title = "Günlük " + (dailyCount + 1); // Başlık oluşturuyoruz

        dbHelper.insertDailyEntry(title, dailyEntry); // Veriyi kaydediyoruz
    }

    private int getCurrentDailyCount() {
        return dbHelper.getAllTitles().size(); // Günlük sayısını almak için başlıkların sayısını alıyoruz
    }
}
