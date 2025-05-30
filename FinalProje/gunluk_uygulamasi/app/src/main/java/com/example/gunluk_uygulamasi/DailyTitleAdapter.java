package com.example.gunluk_uygulamasi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * DailyTitleAdapter, günlük başlıklarını ve tarihlerini RecyclerView'da listelemek için kullanılır.
 * Kullanıcı bir başlığa tıkladığında ilgili günlük detayını gösteren ekrana yönlendirilir.
 */
public class DailyTitleAdapter extends RecyclerView.Adapter<DailyTitleAdapter.DailyTitleViewHolder> {

    private List<DailyItem> items;  // Liste verisi
    private final Context context;  // Aktivite bağlamı

    /**
     * Adapter constructor'ı: Context ve liste verisi alınır.
     */
    public DailyTitleAdapter(Context context, List<DailyItem> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * Her bir satır için ViewHolder oluşturur.
     */
    @NonNull
    @Override
    public DailyTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_title_item, parent, false);
        return new DailyTitleViewHolder(view);
    }

    /**
     * ViewHolder'a veri bağlar (başlık ve tarih yazdırılır).
     */
    @Override
    public void onBindViewHolder(@NonNull DailyTitleViewHolder holder, int position) {
        DailyItem item = items.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());

        // Kullanıcı başlığa tıklarsa detay ekranına gider
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddDailyActivity.class);
            intent.putExtra("title", item.getTitle());  // Sadece başlık gönderiliyor
            context.startActivity(intent);
        });
    }

    /**
     * Listedeki öğe sayısını döndürür.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Liste verisini güncellemek için çağrılır.
     */
    public void updateData(List<DailyItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder sınıfı, her bir satırın içeriğini tutar.
     */
    public static class DailyTitleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

        public DailyTitleViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dailyTitle);        // Başlık
            dateTextView = itemView.findViewById(R.id.dateTextView);       // Tarih
        }
    }
}
