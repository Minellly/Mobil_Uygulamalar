package com.example.gunluk_uygulamasi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDailyActivity extends AppCompatActivity {

    private EditText dailyEntryEditText;
    private Button saveButton;
    private Button cameraButton;
    private ImageView imageView;
    private DatabaseHelper dbHelper;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);

        // Veritabanı başlatılıyor
        dbHelper = new DatabaseHelper(this);

        // XML bileşenleri tanımlanıyor
        dailyEntryEditText = findViewById(R.id.dailyEntryEditText);
        saveButton = findViewById(R.id.saveButton);
        cameraButton = findViewById(R.id.camerabutton); // yeni buton
        imageView = findViewById(R.id.imageView2);      // imageView

        // Günlük Kaydet Butonu
        saveButton.setOnClickListener(v -> {
            String dailyEntry = dailyEntryEditText.getText().toString().trim();
            if (!dailyEntry.isEmpty()) {
                saveDailyEntry(dailyEntry);
                Toast.makeText(this, "Günlük kaydedildi!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddDailyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Lütfen günlük yazınız!", Toast.LENGTH_SHORT).show();
            }
        });

        // Kamera Butonu
        cameraButton.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });
    }

    // Fotoğraf çekme intenti
    private void dispatchTakePictureIntent() {
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

    // Geçici fotoğraf dosyası oluşturma
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Fotoğraf çekilip dönünce çağrılan kısım
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                imageView.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }

    // Günlük kaydetme metodu
    private void saveDailyEntry(String dailyEntry) {
        int dailyCount = getCurrentDailyCount();
        String title = "Günlük " + (dailyCount + 1);
        dbHelper.insertDailyEntry(title, dailyEntry);
    }

    private int getCurrentDailyCount() {
        return dbHelper.getAllTitles().size();
    }
}
