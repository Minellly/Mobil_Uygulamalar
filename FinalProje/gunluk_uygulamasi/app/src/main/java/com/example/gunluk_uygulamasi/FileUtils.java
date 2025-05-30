package com.example.gunluk_uygulamasi;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * FileUtils sınıfı, galeriden seçilen bir görselin dosya sistemindeki yolunu almak için kullanılır.
 * Bu yardımcı sınıf (utility), fotoğraf işlemleriyle ilgili kod tekrarını önlemek amacıyla yazılmıştır.
 */
public class FileUtils {

    /**
     * Verilen Uri'ye karşılık gelen dosya sistemindeki gerçek yolu döndürür.
     * FileUtils.java sınıfı, uygulamada galeriden seçilen bir fotoğrafın dosya
     * yolunu almayı sağlayan yardımcı (utility) bir sınıf.
     *
     * @param context Uygulama bağlamı (Context)
     * @param uri     Seçilen görselin Uri'si
     * @return Görselin dosya yolu (String) veya null
     */
    public static String getPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }

        return null;
    }
}
