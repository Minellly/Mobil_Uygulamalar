package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * LoginActivity, kullanıcıdan 4 haneli PIN kodu alarak giriş kontrolü yapan ekrandır.
 * Doğru PIN girildiğinde MainActivity'e geçiş yapılır.
 */
public class LoginActivity extends AppCompatActivity {

    private TextView pinDots;                     // PIN giriş gösterimi için daireler
    private StringBuilder enteredPin = new StringBuilder();  // Girilen PIN
    private final String correctPin = "1234";     // 🔐 Doğru PIN sabit olarak tanımlı

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pinDots = findViewById(R.id.pinDots);
        updatePinDots(); // Başlangıçta ○ ○ ○ ○ gösterilir

        // Sayısal butonları tanımla ve dinle
        int[] numberBtnIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberBtnIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(v -> {
                    // PIN 4 haneli olacak
                    if (enteredPin.length() < 4) {
                        enteredPin.append(btn.getText().toString());
                        updatePinDots();
                        if (enteredPin.length() == 4) {
                            checkPin(); // 4 karakter tamamlanınca kontrol et
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

    /**
     * PIN girişini dairelerle gösteren metod.
     * Girilen kadar dolu daire (⬤), kalanlar boş daire (○) şeklinde gösterilir.
     */
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

    /**
     * PIN kontrolünü yapar. Doğruysa giriş başarılıdır, değilse kullanıcıya uyarı verir.
     */
    private void checkPin() {
        if (enteredPin.toString().equals(correctPin)) {
            Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Yanlış PIN", Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (v != null) v.vibrate(300); // Titreşim
            enteredPin.setLength(0);       // PIN sıfırlanır
            updatePinDots();               // Görsel yeniden ayarlanır
        }
    }
}
