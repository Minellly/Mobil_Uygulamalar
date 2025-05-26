package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import com.example.gunluk_uygulamasi.DailyItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        addButton = findViewById(R.id.addButton);
        logoutButton = findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddDailyActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<DailyItem> updatedItems = dbHelper.getAllItemsWithDates(); // artık başlık + tarih alıyoruz

        if (adapter != null) {
            adapter.updateData(updatedItems);
        } else {
            adapter = new DailyTitleAdapter(this, updatedItems);
            recyclerView.setAdapter(adapter);
        }
    }

}
