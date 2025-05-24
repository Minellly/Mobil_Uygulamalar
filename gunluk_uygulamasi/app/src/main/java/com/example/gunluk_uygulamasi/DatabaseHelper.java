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

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gunlukler.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "gunlukler";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ENTRY = "entry";
    public static final String COLUMN_PHOTO_PATH = "photo_path";
    public static final String COLUMN_DATE = "date";  // ✅ Tarih kolonu eklendi

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);         // Günlükler tablosu
        db.execSQL(USER_TABLE_CREATE);    // Kullanıcılar tablosu
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ✅ Günlük eklerken TARİH de kaydediliyor
    public void insertDailyEntry(String title, String entry, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_ENTRY, entry);
        values.put(COLUMN_PHOTO_PATH, photoPath);

        // 📅 Bugünün tarihi
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        values.put(COLUMN_DATE, currentDate);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // 🔹 Eski metod (dilersen silebilirsin)
    @Deprecated
    public void insertDailyEntry(String title, String entry) {
        insertDailyEntry(title, entry, null);
    }

    public void deleteDailyEntry(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TITLE + " = ?", new String[]{title});
        db.close();
    }

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

    public void updateDailyEntry(String title, String newEntry, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENTRY, newEntry);
        values.put(COLUMN_PHOTO_PATH, photoPath);
        db.update(TABLE_NAME, values, COLUMN_TITLE + " = ?", new String[]{title});
        db.close();
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("users", null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // 🔹 Tüm başlıkları ve tarihleri birlikte getir
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
