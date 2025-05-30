package com.example.gunluk_uygulamasi;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * DatabaseHelper sınıfı, SQLite veritabanı işlemlerini yöneten yardımcı bir sınıftır.
 * Günlük kayıtları ve kullanıcı bilgilerini veritabanında saklamak için kullanılır.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Veritabanı adı ve sürümü
    private static final String DATABASE_NAME = "gunlukler.db";
    private static final int DATABASE_VERSION = 1;

    // Günlükler tablosu ve kolonları
    public static final String TABLE_NAME = "gunlukler";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ENTRY = "entry";
    public static final String COLUMN_PHOTO_PATH = "photo_path";
    public static final String COLUMN_DATE = "date";  // ✅ Tarih kolonu

    // SQL tablo oluşturma cümleleri
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_ENTRY + " TEXT, " +
                    COLUMN_PHOTO_PATH + " TEXT, " +
                    COLUMN_DATE + " TEXT);";

    private static final String USER_TABLE_CREATE =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT);";

    // Yapıcı (constructor)
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Veritabanı ilk kez oluşturulurken çağrılır
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);         // Günlükler tablosu
        db.execSQL(USER_TABLE_CREATE);    // Kullanıcılar tablosu
    }

    // Veritabanı versiyonu değiştiğinde tabloyu yeniden oluşturur
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ✅ Yeni bir günlük ekler (fotoğraf ve tarih dahil)
    public void insertDailyEntry(String title, String entry, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_ENTRY, entry);
        values.put(COLUMN_PHOTO_PATH, photoPath);

        // Tarih bilgisi otomatik ekleniyor
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        values.put(COLUMN_DATE, currentDate);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 🔹 Eski sürüm için (fotoğraf olmayan)
    @Deprecated
    public void insertDailyEntry(String title, String entry) {
        insertDailyEntry(title, entry, null);
    }

    // Belirli bir başlığa ait günlüğü siler
    public void deleteDailyEntry(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TITLE + " = ?", new String[]{title});
        db.close();
    }

    // Başlığa göre günlük içeriğini getirir
    public String getEntryByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ENTRY + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TITLE + " = ?", new String[]{title});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String entry = cursor.getString(cursor.getColumnIndex(COLUMN_ENTRY));
            cursor.close();
            return entry;
        }
        return "";
    }

    // Başlığa göre fotoğraf yolunu getirir
    public String getPhotoPathByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PHOTO_PATH + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TITLE + " = ?", new String[]{title});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_PATH));
            cursor.close();
            return path;
        }
        return null;
    }

    // Mevcut günlük kaydını günceller (içerik + fotoğraf)
    public void updateDailyEntry(String title, String newEntry, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENTRY, newEntry);
        values.put(COLUMN_PHOTO_PATH, photoPath);
        db.update(TABLE_NAME, values, COLUMN_TITLE + " = ?", new String[]{title});
        db.close();
    }

    // Yeni kullanıcı kaydı oluşturur
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    // Kullanıcı adı ve şifre kontrolü (giriş için)
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // 🔹 Tüm günlük başlıklarını ve tarihleri getirir (ListView için)
    public List<DailyItem> getAllItemsWithDates() {
        List<DailyItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TITLE + ", " + COLUMN_DATE + " FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                items.add(new DailyItem(title, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return items;
    }
}
