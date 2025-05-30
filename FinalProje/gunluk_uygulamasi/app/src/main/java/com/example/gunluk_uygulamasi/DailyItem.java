package com.example.gunluk_uygulamasi;

/**
 * DailyItem sınıfı, bir günlük kaydının başlık ve tarih bilgilerini tutmak için oluşturulmuştur.
 * Bu sınıf uygulamadaki model (veri) katmanına aittir.
 */
public class DailyItem {

    // Günlük başlığı (kullanıcı tarafından girilen başlık)
    private String title;

    // Günlük tarihi (otomatik olarak atanır)
    private String date;

    /**
     * DailyItem sınıfının yapıcısı (constructor).(Günlük başlığı,Günlük tarihi)
     */
    public DailyItem(String title, String date) {
        this.title = title;
        this.date = date;
    }

    //Günlük başlığını döndürür.
    public String getTitle() {
        return title;
    }

    //Günlük tarihini döndürür.
    public String getDate() {
        return date;
    }
}
