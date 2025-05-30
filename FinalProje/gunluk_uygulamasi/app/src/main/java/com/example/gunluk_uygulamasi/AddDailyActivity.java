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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * AddDailyActivity, günlük oluşturma, güncelleme, silme ve fotoğraf ekleme işlemlerini içerir.
 */
public class AddDailyActivity extends AppCompatActivity {

    // UI bileşenleri
    private EditText titleEditText;
    private EditText dailyEntryEditText;
    private Button saveButton;
    private Button cameraButton;
    private Button deleteButton;
    private Button backButton;
    private ImageView imageView;

    // Veritabanı helper sınıfı
    private DatabaseHelper dbHelper;

    // Kamera ve galeri işlemleri için sabitler
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    // Fotoğrafın saklanacağı yol
    private String currentPhotoPath;

    // Günlük güncelleniyorsa, başlık bilgisi buradan alınır
    private String titleFromIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);

        // Veritabanı başlatılır
        dbHelper = new DatabaseHelper(this);

        // Arayüz bileşenleri bağlanır
        titleEditText = findViewById(R.id.titleEditText);
        dailyEntryEditText = findViewById(R.id.dailyEntryEditText);
        saveButton = findViewById(R.id.saveButton);
        cameraButton = findViewById(R.id.camerabutton);
        deleteButton = findViewById(R.id.delete_btn);
        backButton = findViewById(R.id.btn_geri);
        imageView = findViewById(R.id.imageView2);

        // Geri dönme butonu
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
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

        // Günlük güncelleme mi kontrolü
        titleFromIntent = getIntent().getStringExtra("title");
        if (titleFromIntent != null) {
            // Günlük detayları getirilir
            String content = dbHelper.getEntryByTitle(titleFromIntent);
            String savedPhotoPath = dbHelper.getPhotoPathByTitle(titleFromIntent);
            dailyEntryEditText.setText(content);
            titleEditText.setText(titleFromIntent);
            titleEditText.setEnabled(false); // Güncellenemez
            saveButton.setText("Güncelle");
            deleteButton.setVisibility(View.VISIBLE);

            // Fotoğraf varsa göster
            if (savedPhotoPath != null && new File(savedPhotoPath).exists()) {
                imageView.setImageURI(Uri.fromFile(new File(savedPhotoPath)));
                currentPhotoPath = savedPhotoPath;
            }

            // Silme işlemi
            deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Günlüğü silmek istediğine emin misin?")
                        .setMessage("Bu işlem geri alınamaz.")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            dbHelper.deleteDailyEntry(titleFromIntent);
                            Toast.makeText(this, "Günlük silindi", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        })
                        .setNegativeButton("İptal", null)
                        .show();
            });
        } else {
            // Yeni günlük için silme butonu gizlenir
            deleteButton.setVisibility(View.GONE);
        }

        // Kaydet / Güncelle butonu
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

            if (titleFromIntent == null) { // Yeni günlük
                dbHelper.insertDailyEntry(title, entry, currentPhotoPath);
                Toast.makeText(this, "Günlük kaydedildi!", Toast.LENGTH_SHORT).show();
            } else { // Güncelleme
                dbHelper.updateDailyEntry(titleFromIntent, entry, currentPhotoPath);
                Toast.makeText(this, "Günlük güncellendi!", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Kamera veya galeri seçimi için buton
        cameraButton.setOnClickListener(v -> {
            String[] options = {"Kamera ile çek", "Galeriden seç"};

            new AlertDialog.Builder(this)
                    .setTitle("Fotoğraf Ekle")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            dispatchTakePictureIntent();
                        } else {
                            openGalleryToPickImage();
                        }
                    })
                    .show();
        });
    }

    // Kamera ile fotoğraf çek
    private void dispatchTakePictureIntent() {
        Log.d("KAMERA", "dispatchTakePictureIntent çağrıldı");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Kamera izni gerekli!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            getPackageName() + ".provider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Fotoğraf dosyasını oluştur
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Galeriyi aç
    private void openGalleryToPickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_PICK);
    }

    // Kamera veya galeri sonucu geri döner
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

        if (requestCode == REQUEST_GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String copiedPath = copyImageToInternalStorage(selectedImageUri);
                if (copiedPath != null) {
                    imageView.setImageURI(Uri.fromFile(new File(copiedPath)));
                    currentPhotoPath = copiedPath;
                } else {
                    Toast.makeText(this, "Fotoğraf kopyalanamadı", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Kamera izni sonucu
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

    // Galeriden alınan görseli uygulama içine kopyalar
    private String copyImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
