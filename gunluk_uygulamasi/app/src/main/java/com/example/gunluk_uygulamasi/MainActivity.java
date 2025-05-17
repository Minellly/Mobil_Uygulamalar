package com.example.gunluk_uygulamasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DailyTitleAdapter adapter;
    private List<String> dailyTitles;
    private DatabaseHelper dbHelper;
    private Button addButton;
    private Button logoutButton; // Çıkış butonu

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "+" butonuna basıldığında AddDailyActivity açılır
                Intent intent = new Intent(MainActivity.this, AddDailyActivity.class);
                startActivity(intent);
            }
        });

        // Çıkış butonuna tıklanınca LoginActivity'e dön
        logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // MainActivity'yi kapat
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dailyTitles = dbHelper.getAllTitles(); // Veritabanından başlıkları alıyoruz
        adapter = new DailyTitleAdapter(MainActivity.this, dailyTitles);
        recyclerView.setAdapter(adapter);

    }
}
