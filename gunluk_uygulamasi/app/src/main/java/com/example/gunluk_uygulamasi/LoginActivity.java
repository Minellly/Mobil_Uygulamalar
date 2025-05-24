package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private TextView pinDots;
    private StringBuilder enteredPin = new StringBuilder();
    private final String correctPin = "1234"; // 🔐 PIN buraya yazılır

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        pinDots = findViewById(R.id.pinDots);
        updatePinDots(); // Başlangıçta ○ ○ ○ ○ göster

        int[] numberBtnIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberBtnIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    if (enteredPin.length() < 4) {
                        enteredPin.append(btn.getText().toString());
                        updatePinDots();
                        if (enteredPin.length() == 4) {
                            checkPin();
                        }
                    }
                });
            }
        }

        // Silme butonu
        Button btnDel = findViewById(R.id.btnDel);
        if (btnDel != null) {
            btnDel.setOnClickListener(v -> {
                if (enteredPin.length() > 0) {
                    enteredPin.deleteCharAt(enteredPin.length() - 1);
                    updatePinDots();
                }
            });
        }
    }

    private void updatePinDots() {
        if (pinDots == null) return;

        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < enteredPin.length(); i++) {
            dots.append("⬤ ");
        }
        for (int i = enteredPin.length(); i < 4; i++) {
            dots.append("○ ");
        }
        pinDots.setText(dots.toString().trim());
        pinDots.setVisibility(View.VISIBLE);
    }

    private void checkPin() {
        if (enteredPin.toString().equals(correctPin)) {
            Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Yanlış PIN", Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (v != null) v.vibrate(300);
            enteredPin.setLength(0);
            updatePinDots();
        }
    }
}
