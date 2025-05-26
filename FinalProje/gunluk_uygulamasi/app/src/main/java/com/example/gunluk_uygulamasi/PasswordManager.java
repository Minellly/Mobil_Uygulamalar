package com.example.gunluk_uygulamasi;

import android.content.Context;
import android.content.SharedPreferences;

public class PasswordManager {
    private static final String PREF_NAME = "PasswordPrefs";
    private static final String KEY_PASSWORD = "password";

    public static void savePassword(Context context, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, null);
    }

    public static boolean isPasswordSet(Context context) {
        return getPassword(context) != null;
    }
}
