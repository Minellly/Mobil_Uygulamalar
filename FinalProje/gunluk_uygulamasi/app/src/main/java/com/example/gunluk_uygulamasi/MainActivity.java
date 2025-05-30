package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import com.example.gunluk_uygulamasi.DailyItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MainActivity, uygulamanın ana ekranıdır.
 * Günlüklerin listesini gösterir ve kullanıcıya günlük ekleme ya da çıkış yapma imkanı sunar.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DailyTitleAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button addButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Arayüz bileşenleri bağlanıyor
        addButton = findViewById(R.id.addButton);
        logoutButton = findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Yeni günlük ekleme ekranına geçiş
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddDailyActivity.class);
            startActivity(intent);
        });

        // Çıkış yap → LoginActivity'e dön
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Mevcut aktiviteyi kapat
        });
    }

    /**
     * Her geri dönüşte (örneğin AddDailyActivity'den), liste güncellenir.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Veritabanından en güncel başlık + tarih verisi alınır
        List<DailyItem> updatedItems = dbHelper.getAllItemsWithDates();

        // Liste güncellenir
        if (adapter != null) {
            adapter.updateData(updatedItems);
        } else {
            adapter = new DailyTitleAdapter(this, updatedItems);
            recyclerView.setAdapter(adapter);
        }
    }
}
