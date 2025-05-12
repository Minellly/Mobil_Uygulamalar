package com.example.gunluk_uygulamasi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyTitleAdapter extends RecyclerView.Adapter<DailyTitleAdapter.DailyTitleViewHolder> {

    private final List<String> titles;

    public DailyTitleAdapter(List<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public DailyTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Başlıkları görüntülemek için item layout'ını inflate ediyoruz
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_title_item, parent, false);
        return new DailyTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTitleViewHolder holder, int position) {
        // Listeden başlığı alıp TextView'e set ediyoruz
        String title = titles.get(position);
        holder.titleTextView.setText(title);  // Başlıkları bağlama
    }

    @Override
    public int getItemCount() {
        return titles.size();  // Listede kaç öğe olduğunu döndürüyoruz
    }

    // ViewHolder sınıfı
    public static class DailyTitleViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;

        public DailyTitleViewHolder(View itemView) {
            super(itemView);
            // Layout içerisindeki TextView'i buluyoruz
            titleTextView = itemView.findViewById(R.id.dailyTitle);
        }
    }
}
