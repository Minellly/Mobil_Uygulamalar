package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // activity_login.xml senin tasarlayacağın arayüz

        // EditText ve Button'ları id'leriyle eşleştiriyoruz
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // DatabaseHelper'ı başlat
        dbHelper = new DatabaseHelper(this);

        // Giriş yap butonu
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Hatalı kullanıcı adı veya şifre!", Toast.LENGTH_SHORT).show();
            }
        });

        // Kayıt ol butonu
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.registerUser(username, password)) {
                Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bu kullanıcı adı zaten alınmış.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
