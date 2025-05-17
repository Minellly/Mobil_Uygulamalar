package com.example.gunluk_uygulamasi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDailyActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dailyEntryEditText;
    private Button saveButton;
    private Button cameraButton;
    private Button deleteButton;
    private Button backButton; // geri butonu
    private ImageView imageView;
    private DatabaseHelper dbHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private String currentPhotoPath;

    private String titleFromIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);

        dbHelper = new DatabaseHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        dailyEntryEditText = findViewById(R.id.dailyEntryEditText);
        saveButton = findViewById(R.id.saveButton);
        cameraButton = findViewById(R.id.camerabutton);
        deleteButton = findViewById(R.id.delete_btn);
        backButton = findViewById(R.id.btn_geri); // geri butonunu tanıdık
        imageView = findViewById(R.id.imageView2);

        // Geri butonuna tıklanınca MainActivity'ye dön
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddDailyActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Kamera izni kontrolü
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        // Günlük güncelleme mi?
        titleFromIntent = getIntent().getStringExtra("title");
        if (titleFromIntent != null) {
            String content = dbHelper.getEntryByTitle(titleFromIntent);
            dailyEntryEditText.setText(content);
            titleEditText.setText(titleFromIntent);
            titleEditText.setEnabled(false); // başlık değişmesin
            saveButton.setText("Güncelle");
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Günlüğü silmek istediğine emin misin?")
                        .setMessage("Bu işlem geri alınamaz.")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            dbHelper.deleteDailyEntry(titleFromIntent);
                            Toast.makeText(this, "Günlük silindi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("İptal", null)
                        .show();
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        // Kaydet / Güncelle
        saveButton.setOnClickListener(v -> {
            String entry = dailyEntryEditText.getText().toString().trim();
            String title = titleEditText.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Lütfen bir başlık giriniz!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (entry.isEmpty()) {
                Toast.makeText(this, "Lütfen günlük yazınız!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (titleFromIntent == null) {
                dbHelper.insertDailyEntry(title, entry);
                Toast.makeText(this, "Günlük kaydedildi!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateDailyEntry(titleFromIntent, entry);
                Toast.makeText(this, "Günlük güncellendi!", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Kamera butonu
        cameraButton.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void dispatchTakePictureIntent() {
        Log.d("KAMERA", "dispatchTakePictureIntent çağrıldı");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Kamera izni gerekli!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("KAMERA", "onActivityResult çağrıldı");

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                imageView.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Kamera izni verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kamera izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
