package com.example.gunluk_uygulamasi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PasswordManager sınıfı, SharedPreferences kullanarak uygulama içi şifre yönetimini sağlar.
 * Şifreyi kaydetme, alma ve olup olmadığını kontrol etme işlemleri burada yapılır.
 */
public class PasswordManager {

    // SharedPreferences dosyasının adı
    private static final String PREF_NAME = "PasswordPrefs";

    // Şifre anahtar ismi (key)
    private static final String KEY_PASSWORD = "password";

    /**
     * Kullanıcının belirlediği şifreyi cihazda SharedPreferences'a kaydeder.
     * @param context  Uygulama bağlamı (Context)
     * @param password Kaydedilecek şifre
     */
    public static void savePassword(Context context, String password) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }

    /**
     * SharedPreferences'tan kayıtlı şifreyi alır.
     * @param context Uygulama bağlamı (Context)
     * @return Kayıtlı şifre (varsa), yoksa null
     */
    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, null);
    }

    /**
     * Uygulamada şifre belirlenmiş mi kontrol eder.
     * @param context Uygulama bağlamı (Context)
     * @return true -> şifre mevcut, false -> şifre henüz belirlenmemiş
     */
    public static boolean isPasswordSet(Context context) {
        return getPassword(context) != null;
    }
}
